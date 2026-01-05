package calendar.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Dialog for creating a single event.
 */
public class CreateEventDialog extends JDialog {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

  private JTextField subjectField;
  private JTextField dateField;
  private JTextField startTimeField;
  private JTextField endTimeField;
  private JTextField locationField;
  private JTextArea descriptionArea;
  private JCheckBox allDayCheckbox;
  private JCheckBox privateCheckbox;

  private boolean confirmed;
  private String subject;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private String location;
  private String description;
  private boolean isPrivate;

  /**
   * Constructor.
   *
   * @param parent the parent frame
   * @param defaultDate the default date
   */
  public CreateEventDialog(JFrame parent, LocalDate defaultDate) {
    super(parent, "Create New Event", true);
    this.confirmed = false;

    setSize(580, 600);
    setResizable(false);
    setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

    // Title panel
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    JLabel titleLabel = new JLabel("Create New Event");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titlePanel.add(titleLabel);
    mainPanel.add(titlePanel, BorderLayout.NORTH);

    // Content panel with GridBagLayout for proper alignment
    final JPanel contentPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 5, 8, 5);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    int row = 0;

    // Subject field
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0.0;
    subjectField = new JTextField(25);
    subjectField.setToolTipText("Enter the event title or subject");
    addLabeledFieldGrid(contentPanel, "Subject (required):", subjectField, true, gbc, row++);

    // Date field
    dateField = new JTextField(12);
    if (defaultDate != null) {
      dateField.setText(defaultDate.format(DATE_FORMAT));
    } else {
      dateField.setText(LocalDate.now().format(DATE_FORMAT));
    }
    dateField.setToolTipText("Date format: YYYY-MM-DD (e.g., 2024-01-15)");
    addLabeledFieldGrid(contentPanel, "Date (yyyy-MM-dd):", dateField, false, gbc, row++);

    // All-day checkbox
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 2;
    allDayCheckbox = new JCheckBox("All-day event (8:00 AM - 5:00 PM)");
    allDayCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 13));
    allDayCheckbox.addActionListener(e -> {
      boolean allDay = allDayCheckbox.isSelected();
      startTimeField.setEnabled(!allDay);
      endTimeField.setEnabled(!allDay);
      if (allDay) {
        startTimeField.setText("08:00");
        endTimeField.setText("17:00");
      }
    });
    contentPanel.add(allDayCheckbox, gbc);
    row++;
    gbc.gridwidth = 1;

    // Time fields in a horizontal layout
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 1;
    gbc.weightx = 0.0;
    JLabel timeLabel = new JLabel("Time:");
    timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
    timeLabel.setPreferredSize(new Dimension(140, 20));
    contentPanel.add(timeLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
    timePanel.setAlignmentX(LEFT_ALIGNMENT);

    JPanel startPanel = new JPanel();
    startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
    JLabel startLabel = new JLabel("Start (HH:mm):");
    startLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    startTimeField = new JTextField(8);
    startTimeField.setText("09:00");
    startTimeField.setFont(new Font("SansSerif", Font.PLAIN, 13));
    startTimeField.setToolTipText("Time format: HH:mm (24-hour, e.g., 14:30 for 2:30 PM)");
    startPanel.add(startLabel);
    startPanel.add(startTimeField);

    JPanel endPanel = new JPanel();
    endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
    JLabel endLabel = new JLabel("End (HH:mm):");
    endLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    endTimeField = new JTextField(8);
    endTimeField.setText("10:00");
    endTimeField.setFont(new Font("SansSerif", Font.PLAIN, 13));
    endTimeField.setToolTipText("Time format: HH:mm (24-hour, e.g., 16:00 for 4:00 PM)");
    endPanel.add(endLabel);
    endPanel.add(endTimeField);

    timePanel.add(startPanel);
    timePanel.add(endPanel);
    contentPanel.add(timePanel, gbc);
    row++;
    gbc.gridwidth = 1;

    // Location field
    addLabeledFieldGrid(contentPanel, "Location (optional):", 
                        locationField = new JTextField(25), false, gbc, row++);

    // Description field
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0.0;
    JLabel descLabel = new JLabel("Description (optional):");
    descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
    descLabel.setPreferredSize(new Dimension(140, 20));
    contentPanel.add(descLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    descriptionArea = new JTextArea(3, 25);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
    descriptionArea.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.GRAY),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    ));
    JScrollPane scrollPane = new JScrollPane(descriptionArea);
    scrollPane.setPreferredSize(new Dimension(0, 60));
    contentPanel.add(scrollPane, gbc);
    row++;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Private checkbox
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 2;
    privateCheckbox = new JCheckBox("Mark as private");
    privateCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 13));
    contentPanel.add(privateCheckbox, gbc);
    row++;

    mainPanel.add(contentPanel, BorderLayout.CENTER);

    // Button panel
    JPanel buttonPanelWrapper = new JPanel(new BorderLayout());
    buttonPanelWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
    final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

    JButton createButton = ButtonFactory.createSaveButton("Create Event", 
        () -> handleCreate());
    buttonPanel.add(createButton);

    JButton cancelButton = ButtonFactory.createCancelButton("Cancel", () -> {
      confirmed = false;
      dispose();
    });
    buttonPanel.add(cancelButton);

    buttonPanelWrapper.add(buttonPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanelWrapper, BorderLayout.SOUTH);

    add(mainPanel, BorderLayout.CENTER);
    setLocationRelativeTo(parent);
  }

  private void addLabeledFieldGrid(JPanel panel, String labelText, JTextField field, 
                                   boolean required, GridBagConstraints gbc, int row) {
    // Label
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0.0;
    JLabel label = new JLabel(labelText);
    label.setFont(new Font("SansSerif", Font.PLAIN, 13));
    if (required) {
      label.setForeground(CalendarTheme.REQUIRED_FIELD_LABEL);
    }
    label.setPreferredSize(new Dimension(140, 20));
    panel.add(label, gbc);

    // Field
    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    field.setFont(new Font("SansSerif", Font.PLAIN, 13));
    field.setPreferredSize(new Dimension(0, 28));
    panel.add(field, gbc);
    gbc.gridwidth = 1;
  }

  private void handleCreate() {
    try {
      this.subject = subjectField.getText().trim();
      if (subject.isEmpty()) {
        showError("Subject is required");
        return;
      }

      LocalDate date = LocalDate.parse(dateField.getText().trim(), DATE_FORMAT);
      LocalTime startTime = LocalTime.parse(startTimeField.getText().trim(), TIME_FORMAT);
      LocalTime endTime = LocalTime.parse(endTimeField.getText().trim(), TIME_FORMAT);

      this.startDateTime = LocalDateTime.of(date, startTime);
      this.endDateTime = LocalDateTime.of(date, endTime);

      if (!endDateTime.isAfter(startDateTime)) {
        showError("End time must be after start time");
        return;
      }

      String loc = locationField.getText().trim();
      this.location = loc.isEmpty() ? null : loc;

      String desc = descriptionArea.getText().trim();
      this.description = desc.isEmpty() ? null : desc;

      this.isPrivate = privateCheckbox.isSelected();
      this.confirmed = true;
      dispose();

    } catch (DateTimeParseException e) {
      showError("Invalid date or time format.\n\n"
          + "Please check:\n"
          + "• Date format: YYYY-MM-DD (e.g., 2024-01-15)\n"
          + "• Time format: HH:mm (24-hour, e.g., 14:30)\n"
          + "• Start time must be before end time");
    }
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Shows the dialog.
   *
   * @return true if confirmed, false otherwise
   */
  public boolean showDialog() {
    setVisible(true);
    return confirmed;
  }


  public String getSubject() {
    return subject;
  }

  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public String getEventLocation() {
    return location;
  }

  public String getDescription() {
    return description;
  }

  public boolean isPrivate() {
    return isPrivate;
  }
}

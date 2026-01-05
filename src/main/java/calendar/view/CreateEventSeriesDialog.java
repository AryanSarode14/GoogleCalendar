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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Dialog for creating a recurring event series.
 */
public class CreateEventSeriesDialog extends JDialog {
  private static final DateTimeFormatter DATE_FORMAT = 
      DateTimeFormatter.ofPattern(UIConstants.DATE_FORMAT_PATTERN);
  private static final DateTimeFormatter TIME_FORMAT = 
      DateTimeFormatter.ofPattern(UIConstants.TIME_FORMAT_PATTERN);

  private JTextField subjectField;
  private JTextField startDateField;
  private JTextField startTimeField;
  private JTextField endTimeField;
  private JTextField locationField;
  private JTextArea descriptionArea;
  private JCheckBox privateCheckbox;

  private JCheckBox monCheckbox;
  private JCheckBox tueCheckbox;
  private JCheckBox wedCheckbox;
  private JCheckBox thuCheckbox;
  private JCheckBox friCheckbox;
  private JCheckBox satCheckbox;
  private JCheckBox sunCheckbox;

  private JRadioButton endDateRadio;
  private JRadioButton occurrencesRadio;
  private JTextField endDateField;
  private JTextField occurrencesField;

  private boolean confirmed;
  private String subject;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private String location;
  private String description;
  private boolean isPrivate;
  private Set<DayOfWeek> weekdays;
  private LocalDate seriesEndDate;
  private Integer occurrences;

  /**
   * Constructor.
   *
   * @param parent the parent frame
   * @param defaultDate the default date
   */
  public CreateEventSeriesDialog(JFrame parent, LocalDate defaultDate) {
    super(parent, "Create Recurring Series", true);
    this.confirmed = false;

    setSize(650, 700);
    setResizable(false);
    setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

    // Title panel
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    JLabel titleLabel = new JLabel("Create Recurring Event Series");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
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
    subjectField = new JTextField(25);
    addLabeledFieldGrid(contentPanel, "Subject (required):", subjectField, true, gbc, row++);

    // Start Date field
    startDateField = new JTextField(12);
    if (defaultDate != null) {
      startDateField.setText(defaultDate.format(DATE_FORMAT));
    } else {
      startDateField.setText(LocalDate.now().format(DATE_FORMAT));
    }
    addLabeledFieldGrid(contentPanel, "Start Date (yyyy-MM-dd):", 
                        startDateField, false, gbc, row++);

    // Time fields
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0.0;
    JLabel timeLabel = new JLabel("Time:");
    timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    timeLabel.setPreferredSize(new Dimension(140, 20));
    contentPanel.add(timeLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));

    startTimeField = new JTextField(6);
    JPanel startTimePanel = createTimeFieldPanel("Start (HH:mm):", startTimeField, "09:00");
    endTimeField = new JTextField(6);
    JPanel endTimePanel = createTimeFieldPanel("End (HH:mm):", endTimeField, "10:00");

    timePanel.add(startTimePanel);
    timePanel.add(endTimePanel);
    contentPanel.add(timePanel, gbc);
    row++;
    gbc.gridwidth = 1;

    // Location field
    locationField = new JTextField(25);
    addLabeledFieldGrid(contentPanel, "Location:", locationField, false, gbc, row++);

    // Description field
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0.0;
    JLabel descLabel = new JLabel("Description:");
    descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    descLabel.setPreferredSize(new Dimension(140, 20));
    contentPanel.add(descLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    descriptionArea = new JTextArea(3, 25);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
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
    privateCheckbox = new JCheckBox("Private");
    privateCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 12));
    contentPanel.add(privateCheckbox, gbc);
    row++;
    gbc.gridwidth = 1;

    
    // Weekday selection
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 3;
    gbc.weightx = 1.0;
    JLabel weekdayLabel = new JLabel("Repeat on (select at least one):");
    weekdayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    weekdayLabel.setForeground(CalendarTheme.REQUIRED_FIELD_LABEL);
    contentPanel.add(weekdayLabel, gbc);
    row++;

    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 3;
    JPanel weekdayPanel = new JPanel(new GridLayout(2, 4, 10, 8));
    weekdayPanel.setPreferredSize(new Dimension(0, 70));

    Font cbFont = new Font("SansSerif", Font.PLAIN, 12);
    sunCheckbox = new JCheckBox("Sunday");
    sunCheckbox.setFont(cbFont);
    monCheckbox = new JCheckBox("Monday");
    monCheckbox.setFont(cbFont);
    tueCheckbox = new JCheckBox("Tuesday");
    tueCheckbox.setFont(cbFont);
    wedCheckbox = new JCheckBox("Wednesday");
    wedCheckbox.setFont(cbFont);
    thuCheckbox = new JCheckBox("Thursday");
    thuCheckbox.setFont(cbFont);
    friCheckbox = new JCheckBox("Friday");
    friCheckbox.setFont(cbFont);
    satCheckbox = new JCheckBox("Saturday");
    satCheckbox.setFont(cbFont);

    weekdayPanel.add(sunCheckbox);
    weekdayPanel.add(monCheckbox);
    weekdayPanel.add(tueCheckbox);
    weekdayPanel.add(wedCheckbox);
    weekdayPanel.add(thuCheckbox);
    weekdayPanel.add(friCheckbox);
    weekdayPanel.add(satCheckbox);
    weekdayPanel.add(new JPanel()); // Empty panel to fill the last grid cell
    contentPanel.add(weekdayPanel, gbc);
    row++;

    // Quick selection buttons
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 3;
    final JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

    JButton weekdaysBtn = new JButton("Weekdays");
    weekdaysBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
    weekdaysBtn.addActionListener(e -> {
      monCheckbox.setSelected(true);
      tueCheckbox.setSelected(true);
      wedCheckbox.setSelected(true);
      thuCheckbox.setSelected(true);
      friCheckbox.setSelected(true);
      satCheckbox.setSelected(false);
      sunCheckbox.setSelected(false);
    });

    JButton weekendBtn = new JButton("Weekend");
    weekendBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
    weekendBtn.addActionListener(e -> {
      satCheckbox.setSelected(true);
      sunCheckbox.setSelected(true);
      monCheckbox.setSelected(false);
      tueCheckbox.setSelected(false);
      wedCheckbox.setSelected(false);
      thuCheckbox.setSelected(false);
      friCheckbox.setSelected(false);
    });

    quickPanel.add(weekdaysBtn);
    quickPanel.add(weekendBtn);
    contentPanel.add(quickPanel, gbc);
    row++;

    // Series ends section
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 3;
    JLabel endLabel = new JLabel("Series ends:");
    endLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    endLabel.setForeground(CalendarTheme.REQUIRED_FIELD_LABEL);
    contentPanel.add(endLabel, gbc);
    row++;

    final ButtonGroup endGroup = new ButtonGroup();
    endDateRadio = new JRadioButton("On date:");
    occurrencesRadio = new JRadioButton("After # events:");
    endDateRadio.setFont(new Font("SansSerif", Font.PLAIN, 12));
    occurrencesRadio.setFont(new Font("SansSerif", Font.PLAIN, 12));
    endGroup.add(endDateRadio);
    endGroup.add(occurrencesRadio);
    endDateRadio.setSelected(true);

    // End date option
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 1;
    gbc.weightx = 0.0;
    contentPanel.add(endDateRadio, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    endDateField = new JTextField(12);
    endDateField.setFont(new Font("SansSerif", Font.PLAIN, 12));
    endDateField.setText(LocalDate.now().plusMonths(3).format(DATE_FORMAT));
    contentPanel.add(endDateField, gbc);
    row++;

    // Occurrences option
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 1;
    gbc.weightx = 0.0;
    contentPanel.add(occurrencesRadio, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    occurrencesField = new JTextField(6);
    occurrencesField.setFont(new Font("SansSerif", Font.PLAIN, 12));
    occurrencesField.setText("10");
    occurrencesField.setPreferredSize(new Dimension(80, 25));
    contentPanel.add(occurrencesField, gbc);
    row++;
    gbc.gridwidth = 1;

    endDateRadio.addActionListener(e -> {
      endDateField.setEnabled(true);
      occurrencesField.setEnabled(false);
    });
    occurrencesRadio.addActionListener(e -> {
      endDateField.setEnabled(false);
      occurrencesField.setEnabled(true);
    });
    occurrencesField.setEnabled(false);

    mainPanel.add(contentPanel, BorderLayout.CENTER);

    
    // Button panel
    JPanel buttonPanelWrapper = new JPanel(new BorderLayout());
    buttonPanelWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
    final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

    JButton createButton = ButtonFactory.createSaveButton("Create Series", 
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

  private JPanel createTimeFieldPanel(String label, JTextField field, String defaultVal) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
    JLabel lbl = new JLabel(label);
    lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
    panel.add(lbl);
    panel.add(Box.createVerticalStrut(2));
    
    field.setText(defaultVal);
    field.setFont(new Font("SansSerif", Font.PLAIN, 12));
    panel.add(field);
    
    return panel;
  }

  private void addLabeledFieldGrid(JPanel panel, String labelText, JTextField field, 
                                   boolean required, GridBagConstraints gbc, int row) {
    // Label
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0.0;
    JLabel label = new JLabel(labelText);
    label.setFont(new Font("SansSerif", Font.PLAIN, 12));
    if (required) {
      label.setForeground(CalendarTheme.REQUIRED_FIELD_LABEL);
    }
    label.setPreferredSize(new Dimension(140, 20));
    panel.add(label, gbc);

    // Field
    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    field.setFont(new Font("SansSerif", Font.PLAIN, 12));
    field.setPreferredSize(new Dimension(0, 26));
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

      LocalDate startDate = LocalDate.parse(startDateField.getText().trim(), DATE_FORMAT);
      LocalTime startTime = LocalTime.parse(startTimeField.getText().trim(), TIME_FORMAT);
      LocalTime endTime = LocalTime.parse(endTimeField.getText().trim(), TIME_FORMAT);

      this.startDateTime = LocalDateTime.of(startDate, startTime);
      this.endDateTime = LocalDateTime.of(startDate, endTime);

      if (!endDateTime.isAfter(startDateTime)) {
        showError("End time must be after start time");
        return;
      }

      String loc = locationField.getText().trim();
      this.location = loc.isEmpty() ? null : loc;

      String desc = descriptionArea.getText().trim();
      this.description = desc.isEmpty() ? null : desc;

      this.isPrivate = privateCheckbox.isSelected();

      this.weekdays = new HashSet<>();
      if (sunCheckbox.isSelected()) {
        weekdays.add(DayOfWeek.SUNDAY);
      }
      if (monCheckbox.isSelected()) {
        weekdays.add(DayOfWeek.MONDAY);
      }
      if (tueCheckbox.isSelected()) {
        weekdays.add(DayOfWeek.TUESDAY);
      }
      if (wedCheckbox.isSelected()) {
        weekdays.add(DayOfWeek.WEDNESDAY);
      }
      if (thuCheckbox.isSelected()) {
        weekdays.add(DayOfWeek.THURSDAY);
      }
      if (friCheckbox.isSelected()) {
        weekdays.add(DayOfWeek.FRIDAY);
      }
      if (satCheckbox.isSelected()) {
        weekdays.add(DayOfWeek.SATURDAY);
      }

      if (weekdays.isEmpty()) {
        showError("Please select at least one weekday");
        return;
      }

      if (endDateRadio.isSelected()) {
        this.seriesEndDate = LocalDate.parse(endDateField.getText().trim(), DATE_FORMAT);
        this.occurrences = null;
        if (!seriesEndDate.isAfter(startDate)) {
          showError("End date must be after start date");
          return;
        }
      } else {
        this.seriesEndDate = null;
        this.occurrences = Integer.parseInt(occurrencesField.getText().trim());
        if (occurrences <= 0) {
          showError("Occurrences must be positive");
          return;
        }
      }

      this.confirmed = true;
      dispose();

    } catch (DateTimeParseException e) {
      showError("Invalid date or time format.\n\nDate: yyyy-MM-dd\nTime: HH:mm");
    } catch (NumberFormatException e) {
      showError("Invalid number of occurrences");
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

  public Set<DayOfWeek> getWeekdays() {
    return weekdays;
  }

  public LocalDate getSeriesEndDate() {
    return seriesEndDate;
  }

  public Integer getOccurrences() {
    return occurrences;
  }
}

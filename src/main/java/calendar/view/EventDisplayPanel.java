package calendar.view;

import calendar.model.EventInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Panel component for displaying events for a selected day.
 * Handles formatting and display of event information.
 */
public class EventDisplayPanel extends JPanel {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter TIME_12HR_FORMATTER =
      DateTimeFormatter.ofPattern("h:mm a");

  private JLabel eventsHeaderLabel;
  private JTextArea eventsTextArea;

  /**
   * Constructor.
   */
  public EventDisplayPanel() {
    super(new BorderLayout(5, 5));
    setPreferredSize(new Dimension(350, 0));
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(0, 10, 0, 0),
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
    ));

    eventsHeaderLabel = new JLabel("Events");
    eventsHeaderLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
    eventsHeaderLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
    add(eventsHeaderLabel, BorderLayout.NORTH);

    eventsTextArea = new JTextArea();
    eventsTextArea.setEditable(false);
    eventsTextArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
    eventsTextArea.setLineWrap(true);
    eventsTextArea.setWrapStyleWord(true);
    eventsTextArea.setMargin(new java.awt.Insets(10, 10, 10, 10));

    JScrollPane scrollPane = new JScrollPane(eventsTextArea);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Displays events for a given date.
   *
   * @param date the date to display events for (can be null)
   * @param events the list of events for that date
   */
  public void displayEventsForDay(LocalDate date, List<EventInterface> events) {
    if (date == null) {
      eventsHeaderLabel.setText("Events");
      eventsTextArea.setText("No day selected");
      return;
    }

    String dayOfWeek = date.getDayOfWeek().toString();
    dayOfWeek = dayOfWeek.charAt(0) + dayOfWeek.substring(1).toLowerCase();
    eventsHeaderLabel.setText(String.format("Events on %s, %s",
        dayOfWeek, date.format(DATE_FORMATTER)));

    if (events.isEmpty()) {
      eventsTextArea.setText("No events scheduled for this day.");
      eventsTextArea.setForeground(Color.GRAY);
    } else {
      String formattedEvents = formatEventList(events);
      eventsTextArea.setText(formattedEvents);
      eventsTextArea.setForeground(Color.BLACK);
    }

    eventsTextArea.setCaretPosition(0);
  }

  /**
   * Formats a list of events into a display string with improved formatting.
   *
   * @param events the events to format
   * @return formatted string representation
   */
  private String formatEventList(List<EventInterface> events) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < events.size(); i++) {
      EventInterface event = events.get(i);

      // Event header with numbering
      sb.append(String.format("┌─ Event %d ─────────────────────────────────\n", i + 1));
      sb.append(String.format("│ Subject: %s\n", event.getSubject()));

      // Time display
      sb.append(String.format("│ Time: %s - %s\n",
          event.getStartDateTime().toLocalTime().format(TIME_12HR_FORMATTER),
          event.getEndDateTime().toLocalTime().format(TIME_12HR_FORMATTER)));

      // Location
      if (event.getLocation().isPresent() && !event.getLocation().get().isEmpty()) {
        String location = event.getLocation().get();
        String locationIcon = location.toLowerCase().contains("online") ? "🌐" : "📍";
        sb.append(String.format("│ %s Location: %s\n", locationIcon, location));
      }

      // Description/Notes
      if (event.getDescription().isPresent() && !event.getDescription().get().isEmpty()) {
        String desc = event.getDescription().get();
        if (desc.length() > 60) {
          desc = desc.substring(0, 57) + "...";
        }
        sb.append(String.format("│ 📝 Notes: %s\n", desc));
      }

      // Event tags
      boolean hasTags = false;
      if (event.getSeriesId().isPresent()) {
        sb.append("│ 🔄 [Recurring Event]\n");
        hasTags = true;
      }

      if (event.isPrivate()) {
        sb.append("│ 🔒 [Private]\n");
        hasTags = true;
      }

      if (!hasTags) {
        sb.append("│\n");
      }

      // Event footer
      sb.append("└───────────────────────────────────────\n");
      
      if (i < events.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}




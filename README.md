# Calendar Application

A modern, feature-rich calendar application built with Java and Swing, supporting both GUI and command-line interfaces.

## Features

- 📅 **Multiple Calendars**: Create and manage multiple calendars with different timezones
- 📝 **Event Management**: Create single events and recurring event series
- 🎨 **Modern GUI**: Beautiful, intuitive graphical interface with modern design
- ⌨️ **Keyboard Shortcuts**: Efficient navigation with keyboard shortcuts
- 📤 **Export/Import**: Export calendars to CSV and ICS formats
- 🖥️ **Multiple Modes**: GUI, interactive command-line, and batch processing modes
- 📊 **Week & Month Views**: Flexible calendar views for different needs
- 🔄 **Event Series**: Create recurring events with flexible scheduling

## Screenshots

The application features a clean, modern interface with:
- Color-coded calendar grids
- Interactive day selection
- Event display panel
- Comprehensive event creation dialogs

## Requirements

- Java 8 or higher
- Gradle 6.0 or higher (for building)

## Building

Clone the repository and build using Gradle:

```bash
git clone <repository-url>
cd f25-hw7-sent-code-group-NotChatGPT
./gradlew build
```

This will compile the code, run tests, and create a JAR file in `build/libs/calendar-1.0.jar`.

## Running

### GUI Mode (Default)

Launch the graphical user interface:

```bash
./gradlew run
```

Or using the JAR:

```bash
java -jar build/libs/calendar-1.0.jar
```

### Interactive Mode

Run the application in interactive command-line mode:

```bash
java -jar build/libs/calendar-1.0.jar --mode interactive
```

### Headless Mode

Process commands from a file:

```bash
java -jar build/libs/calendar-1.0.jar --mode headless <commands-file>
```

## Usage Examples

### Creating a Calendar

```
create calendar --name "MyCalendar" --timezone "America/New_York"
```

### Creating Events

**Single Event:**
```
create event --subject "Meeting" --start "2024-01-15T10:00" --end "2024-01-15T11:00" --location "Room 101"
```

**Recurring Event Series:**
```
create event series --subject "Weekly Meeting" --start "2024-01-15T10:00" --end "2024-01-15T11:00" --frequency weekly --until "2024-03-15"
```

### GUI Keyboard Shortcuts

- `Ctrl+N` - Create new event
- `Ctrl+R` - Create new recurring series
- `Ctrl+E` - Edit event
- `←/→` Arrow keys - Navigate months
- `T` - Jump to today

## Project Structure

```
src/
├── main/java/
│   ├── calendar/
│   │   ├── command/       # Command pattern implementation
│   │   ├── controller/    # MVC controllers
│   │   ├── model/         # Data models
│   │   ├── view/          # GUI and console views
│   │   ├── util/          # Utility classes
│   │   └── exceptions/    # Custom exceptions
│   └── CalendarRunner.java  # Main entry point
└── test/java/             # Unit tests
```

## Architecture

The application follows the Model-View-Controller (MVC) architecture pattern:

- **Model**: Manages calendar data and business logic
- **View**: Handles user interface (GUI and console)
- **Controller**: Coordinates between model and view

## Testing

Run all tests:

```bash
./gradlew test
```

Generate test coverage report:

```bash
./gradlew jacocoTestReport
```

View the HTML report at `build/reports/jacoco/test/html/index.html`

## Code Quality

The project uses Checkstyle for code quality enforcement:

```bash
./gradlew checkstyleMain checkstyleTest
```

## Contributing

This is an academic project. For improvements or bug fixes, please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is part of an academic assignment. Please respect academic integrity policies.

## Authors

Developed as part of a collaborative academic project.

## Acknowledgments

Built using:
- Java Swing for GUI
- JUnit for testing
- Gradle for build automation
- Checkstyle for code quality

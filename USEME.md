# Calendar Application - Usage Guide

This document provides instructions on how to use the calendar application that you have received.

## Building the Project

To build the project from source, use Gradle:

```bash
./gradlew build
```

This will compile the code, run tests, and create a JAR file in `build/libs/calendar-1.0.jar`.

## Running the Application

The calendar application supports three execution modes:

### 1. GUI Mode (Default)

To launch the graphical user interface:

```bash
java -jar build/libs/calendar-1.0.jar
```

Or, if running directly from source:

```bash
./gradlew run
```

Or using the main class directly:

```bash
java -cp build/classes/java/main CalendarRunner
```

The GUI will open, allowing you to interact with calendars visually.

### 2. Interactive Mode

To run the application in interactive command-line mode:

```bash
java -jar build/libs/calendar-1.0.jar --mode interactive
```

Or from source:

```bash
java -cp build/classes/java/main CalendarRunner --mode interactive
```

In this mode, you can type commands at the prompt. Type `exit` to quit.

### 3. Headless Mode

To process commands from a file (batch processing):

```bash
java -jar build/libs/calendar-1.0.jar --mode headless <path-to-commands-file>
```

Or from source:

```bash
java -cp build/classes/java/main CalendarRunner --mode headless <path-to-commands-file>
```

Example:

```bash
java -jar build/libs/calendar-1.0.jar --mode headless res/commands.txt
```

## Basic Usage Examples

### Creating a Calendar

First, create a calendar with a timezone:

```
create calendar --name "MyCalendar" --timezone "America/New_York"
```

### Using a Calendar

Before creating events, you need to select which calendar to use:

```
use calendar --name "MyCalendar"
```

### Creating Events

**Single Event:**
```
create event --subject "Meeting" --start "2024-01-15T10:00" --end "2024-01-15T11:00" --location "Room 101"
```

**All-Day Event:**
```
create all-day event --subject "Holiday" --date "2024-01-20"
```

**Recurring Event Series:**
```
create event series --subject "Weekly Meeting" --start "2024-01-15T10:00" --end "2024-01-15T11:00" --frequency weekly --until "2024-03-15"
```

### Viewing Events

**View events on a specific day:**
```
print events on 2024-01-15
```

**View events in a range:**
```
print events from 2024-01-15 to 2024-01-20
```

### Editing Events

Edit a single event:
```
edit event --id <event-id> --subject "New Subject" --location "New Location"
```

Edit an event series:
```
edit series --id <series-id> --subject "New Series Name"
```

### Exporting Calendar

Export to CSV:
```
export calendar --name "MyCalendar" --file "output.csv" --format csv
```

Export to ICS:
```
export calendar --name "MyCalendar" --file "output.ics" --format ics
```

### Copying Events

Copy a single event to another calendar:
```
copy event --id <event-id> --to-calendar "OtherCalendar"
```

## Code Status

Please refer to the `questionnaire.txt` file for a detailed status of which features are implemented and working. This file provides information about:

- Import/export functionality
- Multiple calendar support
- Event creation and management
- GUI features
- Program execution modes
- Testing coverage

## Known Issues

If you encounter any issues while using this code, please refer to the `questionnaire.txt` file which indicates which features are fully functional (Y) and which are not (N).

## Getting Help

If you have questions about using this code or encounter problems that prevent you from implementing required features, please contact the code providers. Issues should be documented in your Part 2 submission's USEME file.


# Calendar Application - User Guide

This document provides detailed instructions on how to use the calendar application.

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

The GUI will open, allowing you to interact with calendars visually.

### 2. Interactive Mode

To run the application in interactive command-line mode:

```bash
java -jar build/libs/calendar-1.0.jar --mode interactive
```

In this mode, you can type commands at the prompt. Type `exit` to quit.

### 3. Headless Mode

To process commands from a file (batch processing):

```bash
java -jar build/libs/calendar-1.0.jar --mode headless <path-to-commands-file>
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

## GUI Features

### Keyboard Shortcuts

- `Ctrl+N` - Create new event
- `Ctrl+R` - Create new recurring series  
- `Ctrl+E` - Edit event
- `←/→` Arrow keys - Navigate months/weeks
- `T` - Jump to today's date

### Interface Elements

- **Calendar Grid**: Click on any day to view its events
- **Event Display Panel**: Shows events for the selected day on the right
- **Navigation Buttons**: Use Previous/Next to move between months
- **View Toggle**: Switch between Month and Week views
- **Action Buttons**: Create events, edit events, and manage calendars

### Creating Events in GUI

1. Click "New Event" or "New Series" button
2. Fill in the event details in the dialog
3. For recurring events, select the days of the week
4. Click "Create Event" or "Create Series" to save

## Troubleshooting

### Common Issues

**Issue**: "Calendar not found" error
- **Solution**: Make sure you've created a calendar first using `create calendar` command or the GUI

**Issue**: "Invalid date format" error
- **Solution**: Use the format `YYYY-MM-DD` for dates (e.g., `2024-01-15`)

**Issue**: "End time must be after start time" error
- **Solution**: Ensure the end time is later than the start time

### Getting Help

For issues or questions, please check:
1. The README.md file for general information
2. Command format examples in this guide
3. Error messages which often provide specific guidance

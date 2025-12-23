package cs102groupproject.Client;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.SharedObjects.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages Google Calendar operations.
 * Author: Delfin Eryılmaz
 * Date: 19.12.2025
 */
public class GoogleCalendarAPI {
    private final Calendar calendar;

    public GoogleCalendarAPI(Credential credential) {
        this.calendar = new Calendar.Builder(
            new NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("Google Calendar App")
         .build();
    }

    /**
     * Returns all events from all calendar within a specific timeline
     * @return
     * @throws IOException
     */
    public List<Event> listEvents() throws IOException {
        String[] calendarIds = getAllCalendars();
        List<Event> allEvents = new ArrayList<>();

        for (String calendarId : calendarIds) {
            allEvents.addAll(getEventsFrom(calendarId));
        }

        return allEvents;
    }

    /**
     * Gets event from specific calendars.
     * @param calendarId
     * @return
     * @throws IOException
     */
    private List<Event> getEventsFrom(String calendarId) throws IOException {
        // Gets events within a specific time interval
        LocalDateTime now = LocalDateTime.now();
        // DateTime is the Google API's date format
        DateTime start = new DateTime(now.minusDays(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        DateTime end = new DateTime(now.plusDays(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        Events events = calendar.events().list(calendarId)
            .setTimeMin(start)
            .setTimeMax(end)
            .setSingleEvents(true) 
            .setOrderBy("startTime")
            .execute();

        return events.getItems();
    }

    /**
     * Creates a new event in the google calendar.
     * @param event
     * @return
     * @throws IOException
     */
    public Event createEvent(AppEvent event) throws IOException {
        // Converts LocalDate to DateTime
        long startMillis = event.getStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMillis = event.getFinish().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // 2. Create the Google DateTime objects
        DateTime start = new DateTime(startMillis);
        DateTime end = new DateTime(endMillis);

        Event googleEvent = new Event()
            .setSummary(event.getName())
            .setDescription("Created via Studium")
            .setStart(new EventDateTime().setDateTime(start).setTimeZone(ZoneId.systemDefault().toString()))
            .setEnd(new EventDateTime().setDateTime(end).setTimeZone(ZoneId.systemDefault().toString()));

        // Inserts the event into the Studium calendar
        return calendar.events()
            .insert(getStudiumCalendarId(), googleEvent)
            .execute();
    }

    /**
     * Creates a new event in google calendar based on the task created in the app.
     * @param task
     * @return
     * @throws IOException
     */
    public Event createEvent(Task task) throws IOException {
        // Converts LocalDate to DateTime
        long startMillis = task.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMillis = task.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // 2. Create the Google DateTime objects
        DateTime start = new DateTime(startMillis);
        DateTime end = new DateTime(endMillis);

        Event googleEvent = new Event()
            .setSummary(task.getName())
            .setDescription("Created via Studium")
            .setStart(new EventDateTime().setDateTime(start).setTimeZone(ZoneId.systemDefault().toString()))
            .setEnd(new EventDateTime().setDateTime(end).setTimeZone(ZoneId.systemDefault().toString()));

        // Inserts the event into the Studium calendar
        return calendar.events()
            .insert(getStudiumCalendarId(), googleEvent)
            .execute();
    }

    /**
     * Gets all calendar ID's.
     * @return
     * @throws IOException
     */
    private String[] getAllCalendars() throws IOException {
        CalendarList calendarList = calendar.calendarList().list().execute();
        List<CalendarListEntry> items = calendarList.getItems();
        String[] calendarNames = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            calendarNames[i] = items.get(i).getId();
        }
        return calendarNames;
    }

    /**
     * Gets the Studium calendar alreday, if it does not exists creates.
     * @return
     * @throws IOException
     */
    private String getStudiumCalendarId() throws IOException {
        // If Studium calendar exists
        CalendarList items = calendar.calendarList().list().execute();
        for (CalendarListEntry entry : items.getItems()) {
            if ("Studium".equals(entry.getSummary())) {
                return entry.getId();
            }
        }

        // Writing this way to prevent name conflicts 
        com.google.api.services.calendar.model.Calendar newCalendar = new com.google.api.services.calendar.model.Calendar();
        newCalendar.setSummary("Studium");
        newCalendar.setTimeZone(ZoneId.systemDefault().getId()); // Matches user's timezone

        com.google.api.services.calendar.model.Calendar studiumCalendar = calendar.calendars().insert(newCalendar).execute();
        
        return studiumCalendar.getId();
    }
}

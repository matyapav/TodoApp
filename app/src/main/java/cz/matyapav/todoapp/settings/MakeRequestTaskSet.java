package cz.matyapav.todoapp.settings;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;

import static cz.matyapav.todoapp.util.Constants.REQUEST_AUTHORIZATION;

/**
 * Asynchronní task pro handlování calendar API
 * Předání dat pro zaslání do API pomocí konstruktoru
 */
public class MakeRequestTaskSet extends AsyncTask<Void, Void, String> {

    private ProgressDialog mProgress;
    private final Activity context;
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private List<TodoDay> todoDays;
    private SettingsFragment fragment;

    MakeRequestTaskSet(Activity context, SettingsFragment fragment, GoogleAccountCredential credential, List<TodoDay> todoDays) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential).build();
        this.todoDays = todoDays;
        this.context = context;
        mProgress = new ProgressDialog(context);
        mProgress.setMessage(context.getResources().getString(R.string.fetching_data_calendar));
        this.fragment = fragment;
    }

    /**
     * Background task to call Google Calendar API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected String doInBackground(Void... params) {
        try {
            return setDataToApi(todoDays);
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
        }
        return null;
    }

    /**
     * Poslání dat do Calendar API
     */
    private String setDataToApi(List<TodoDay> todoDays) throws IOException {
        Date todoDayDate;
        String output = "";
        for (TodoDay todoDay : todoDays) {
            todoDayDate = todoDay.getDate();
            for (Todo todo : todoDay.getTodos()) {

                if (!todo.isCompleted()) {

                    Event event = remapTodoToEvent(todo);

                    String calendarId = "primary";
                    event = mService.events().insert(calendarId, event).execute();

                    output += String.format("%s (%s)", event.getSummary(), event.getStart());
                    output += "\n";

                }
            }
        }

        return output;

    }

    @Override
    protected void onPreExecute() {
        mProgress.show();
    }

    @Override
    protected void onPostExecute(String output) {
        mProgress.hide();
        if (output == null || output.isEmpty()) {
            Toast.makeText(context, R.string.no_result_returned, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, output, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled() {
        mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                GooglePlayServicesConnector.showGooglePlayServicesAvailabilityErrorDialog(context,
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                fragment.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        REQUEST_AUTHORIZATION);
            } else {
                Toast.makeText(context, R.string.error_occurred+"\n"
                        + mLastError.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,R.string.request_cancelled, Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * Přemapování TUDU na calendar Event
     * Namapování napevno stanovené notifikace na 15 minut
     * Namapování kategorie TUDU do názvu Eventu
     */
    private Event remapTodoToEvent(Todo todo) {

        Event event = new Event()
                .setSummary("[" + todo.getCathegory() + "] " + todo.getTitle())
//                            .setColorId() TODO SET color podle priority
                .setDescription(todo.getDescription());

        DateTime startDateTime = new DateTime(todo.getDateAndTimeStart());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = new DateTime(todo.getDateAndTimeEnd());
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setEnd(end);

        if (todo.isNotification()) {
            // Napevno nastavená notifika na 15 min, vyskakovací okno
            EventReminder[] reminderOverrides = new EventReminder[]{
                    new EventReminder().setMethod("popup").setMinutes(15)
            };
            Event.Reminders reminders = new Event.Reminders()
                    .setUseDefault(false)
                    .setOverrides(Arrays.asList(reminderOverrides));
            event.setReminders(reminders);
        }

        return event;

    }


}
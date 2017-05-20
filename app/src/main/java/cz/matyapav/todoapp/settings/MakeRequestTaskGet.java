package cz.matyapav.todoapp.settings;

import android.app.Activity;
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
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.util.enums.TodoPriority;
import cz.matyapav.todoapp.util.Storage;
import cz.matyapav.todoapp.util.Utils;

import static cz.matyapav.todoapp.util.Constants.REQUEST_AUTHORIZATION;

/**
 * Asynchronní task pro handlování calendar API
 * Získání dat z kalendáře
 */
public class MakeRequestTaskGet extends AsyncTask<Void, Void, List<TodoDay>> {

    ProgressDialog mProgress;
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private Date startDate;
    private Date endDate;
    private Activity context;

    public MakeRequestTaskGet(Activity context, GoogleAccountCredential credential, Date startDate, Date endDate) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .build();
        this.context = context;
        mProgress = new ProgressDialog(context);
        mProgress.setMessage(context.getString(R.string.fetching_data_calendar));
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Background task to call Google Calendar API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<TodoDay> doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Získání dat z calendar API
     */
    private List<TodoDay> getDataFromApi() throws IOException {
        Events events;
        if(startDate == null && endDate == null){
            events = mService.events().list("primary")
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            DateTime start = new DateTime(startDate);
            calendar.setTime(endDate);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            DateTime end = new DateTime(calendar.getTime());


            events = mService.events().list("primary")
                    .setTimeMin(start)
                    .setTimeMax(end)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        }
        List<Event> items = events.getItems();

        HashMap<String, TodoDay> todoDayHashMap = new HashMap<>();

        String dateString;
        Date date;
        for (Event event : items) {
            Todo todo = remapEventToTodo(event);
            if(todo == null){
                continue;
            }
            date = todo.getDateAndTimeStart();
            dateString = Utils.dateFormatter.format(date);

            if (todoDayHashMap.containsKey(dateString)) {
                todoDayHashMap.get(dateString).addTodo(todo);
            } else {
                TodoDay todoDay = new TodoDay();
                todoDay.setDate(date);
                List<Todo> todos = new ArrayList<>();
                todos.add(todo);
                todoDay.setTodos(todos);
                todoDayHashMap.put(dateString, todoDay);
            }
        }

        return new ArrayList<>(todoDayHashMap.values());
    }

    /**
     * Přemapování calendar Event do TUDU
     */
    private Todo remapEventToTodo(Event event) {
        Todo todo = new Todo();
        todo.setTitle(event.getSummary());
        todo.setDescription(event.getDescription());
        todo.setCompleted(false);
        todo.setPriority(TodoPriority.MEDIUM); //importovane todocka budou mit prioritu medium
        todo.setCathegory(null); //kategorie si user vytvari sam tudiz nelze urcit
        if(event.getStart().getDateTime() == null || event.getEnd().getDateTime() == null) {
            return null; //eventy co nemaji start/end cas (pouze datum) zahodime
        }else{
            todo.setDateAndTimeStart(new Date(event.getStart().getDateTime().getValue()));
            todo.setDateAndTimeEnd(new Date(event.getEnd().getDateTime().getValue()));
        }
        if (event.getReminders() != null || !event.getReminders().isEmpty()) {
            todo.setNotification(false); //TODO poresit notifikace az budou implementovany
        }

        return todo;

    }

    @Override
    protected void onPreExecute() {
        mProgress.show();
    }

    @Override
    protected void onPostExecute(List<TodoDay> output) {
        mProgress.hide();
        if (output == null || output.size() == 0) {
            Toast.makeText(context, R.string.no_result_returned, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, output.size() + " " + context.getString(R.string.todos_imported) , Toast.LENGTH_LONG).show();
            List<TodoDay> outpuCollection = output;
            for (TodoDay todoDay : outpuCollection) {
                for (Todo todo : todoDay.getTodos()) {
                    Storage.addNewTodo(todo, context);
                }
            }
            Storage.updateTodosInSharedPreferences(context);
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

                context.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        REQUEST_AUTHORIZATION);
            } else {
                Toast.makeText(context,context.getString(R.string.error_occurred)
                        + mLastError.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, R.string.request_cancelled, Toast.LENGTH_LONG).show();
        }
    }

}

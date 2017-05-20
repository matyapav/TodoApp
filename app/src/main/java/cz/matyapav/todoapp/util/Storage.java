package cz.matyapav.todoapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Cathegory;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.util.enums.TodoPriority;


/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class Storage {

    private static List<Cathegory> cathegories;
    private static HashMap<String, TodoDay> todoDaysList;

    public static List<Cathegory> getDummyCategories(){
        if(cathegories == null){
            cathegories = new ArrayList<>();
            cathegories.add(new Cathegory("Personal", null, R.drawable.ic_personal));
            cathegories.add(new Cathegory("Work", null, R.drawable.ic_work));
            cathegories.add(new Cathegory("Free time", null, R.drawable.ic_free_time));
        }
        return cathegories;
    }

    public static HashMap<String, TodoDay> getTodoDaysList(Context context) {
        if(todoDaysList == null){
            //load from shared preferences
            SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, 0); //private mode;
            String serializedTodos = preferences.getString(Constants.TODOS, null); //if not found then null
            if(serializedTodos == null){
                //no todos yet, create new map
                todoDaysList = new HashMap<>();
            }else{
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String, TodoDay>>(){}.getType();
                todoDaysList = gson.fromJson(serializedTodos, type);
            }
        }
        return todoDaysList;
    }

    public static TodoDay getTodoDayByDate(Date date, Context context){
        String dateStr = Utils.dateFormatter.format(date);
        TodoDay day = getTodoDaysList(context).get(dateStr);
        if(day == null){
            day = new TodoDay(date);
            todoDaysList.put(dateStr, day);
        }
        return day;
    }

    public static TodoDay getCurrentTodoDay(Context context){
        Calendar calendar = Calendar.getInstance();
        return getTodoDayByDate(calendar.getTime(), context);
    }

    public static boolean addNewTodo(Todo newTodo, Activity context) {
        newTodo.setId(Storage.getUniqueTodoId(context));
        Date startDate = newTodo.getDateAndTimeStart();
        Date endDate = newTodo.getDateAndTimeEnd();
        String startDateStr = Utils.dateFormatter.format(startDate);
        String endDateStr = Utils.dateFormatter.format(endDate);
        if(startDateStr.equals(endDateStr)) {
            TodoDay day = getTodoDayByDate(startDate, context);
            if (day != null) {
                day.addTodo(newTodo);
                Storage.sortTodosByTime(day);
                todoDaysList.put(startDateStr, day);
                return true;
            }
        }else{
            //task na vice dni
            newTodo.setMultipleDays(true);
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            TodoDay dayStart = getTodoDayByDate(startDate, context);
            newTodo.setDateAndTimeEnd(c.getTime());
            dayStart.addTodo(newTodo);
            Storage.sortTodosByTime(dayStart);
            todoDaysList.put(Utils.dateFormatter.format(startDate), dayStart);
            c.add(Calendar.MINUTE, 1);
            while(!Utils.dateFormatter.format(c.getTime()).equals(endDateStr)){
                TodoDay dayMiddle = getTodoDayByDate(c.getTime(), context);
                try {
                    Todo todoMiddle = (Todo) newTodo.clone();
                    todoMiddle.setDateAndTimeStart(c.getTime());
                    c.set(Calendar.HOUR_OF_DAY, 23);
                    c.set(Calendar.MINUTE, 59);
                    todoMiddle.setDateAndTimeEnd(c.getTime());
                    dayMiddle.addTodo(todoMiddle);
                    Storage.sortTodosByTime(dayMiddle);
                    todoDaysList.put(Utils.dateFormatter.format(c.getTime()), dayMiddle);
                    c.add(Calendar.MINUTE, 1);
                } catch (CloneNotSupportedException e) {
                    System.err.println("Todo "+newTodo.getTitle()+ " cannot be cloned.");
                    e.printStackTrace();
                    return false;
                }
            }
            try {
                Todo end = (Todo) newTodo.clone();
                end.setDateAndTimeStart(c.getTime());
                end.setDateAndTimeEnd(endDate);
                TodoDay dayEnd = getTodoDayByDate(c.getTime(), context);
                todoDaysList.put(Utils.dateFormatter.format(endDate), dayEnd);
                dayEnd.addTodo(end);
                Storage.sortTodosByTime(dayEnd);
                return true;
            } catch (CloneNotSupportedException e) {
                System.err.println("Todo "+newTodo.getTitle()+ " cannot be cloned.");
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static void updateTodosInSharedPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, 0); //open in private mode
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String tododays  = gson.toJson(getTodoDaysList(context));
        editor.putString(Constants.TODOS, tododays);
        editor.apply();
    }


    //TODO zbavit se toho az bude db
    public static int getUniqueTodoId(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, 0);
        SharedPreferences.Editor editor = preferences.edit();
        int uniqueId = preferences.getInt(Constants.TODO_ID, 0);
        editor.putInt(Constants.TODO_ID, uniqueId+1);
        editor.apply();
        return uniqueId;
    }

    public static boolean editTodo(int id, Todo todo, Context context) {
        Todo edited = getTodoByDateAndId(todo.getDateAndTimeStart(), id);
        if(edited != null) {
            edited.setTitle(todo.getTitle());
            edited.setNotification(todo.isNotification());
            edited.setCathegory(todo.getCathegory());
            edited.setPriority(todo.getPriority());
            edited.setDescription(todo.getDescription());
            Storage.sortTodosByTime(getTodoDayByDate(edited.getDateAndTimeStart(), context));
            return true;
        }
        return false;
    }

    private static Todo getTodoByDateAndId(Date date, int id) {
        String dateStr = Utils.dateFormatter.format(date);
        TodoDay day = todoDaysList.get(dateStr);
        for (Todo todo : day.getTodos()) {
            if(todo.getId() == id){
                return todo;
            }
        }
        return null;
    }


    public static void sortTodosByTime(TodoDay day) {
        Collections.sort(day.getTodos(), new Comparator<Todo>() {
            @Override
            public int compare(Todo lhs, Todo rhs) {
                return lhs.getDateAndTimeStart().compareTo(rhs.getDateAndTimeStart());
            }
        });
    }
}

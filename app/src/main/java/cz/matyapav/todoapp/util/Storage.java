package cz.matyapav.todoapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Cathegory;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.util.enums.TodoPriority;

/**
 * Storage layer - keeps all data in application and sets/gets them into/from SharedPreferences
 */
public class Storage {

    private static List<Cathegory> cathegories;
    private static HashMap<String, TodoDay> todoDays;

    /**
     * Gets dummy categories
     * TODO when category management is implemented remove this method
     * @return
     */
    public static List<Cathegory> getDummyCategories(){
        if(cathegories == null){
            cathegories = new ArrayList<>();
            cathegories.add(new Cathegory("Personal", null, R.drawable.ic_personal));
            cathegories.add(new Cathegory("Work", null, R.drawable.ic_work));
            cathegories.add(new Cathegory("Free time", null, R.drawable.ic_free_time));
        }
        return cathegories;
    }

    /**
     * Gets tododays map from SharedPreferences
     * @param context
     * @return
     */
    public static HashMap<String, TodoDay> getTodoDays(Activity context) {
        if(todoDays == null){
            //load from shared preferences
            SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, 0); //private mode;
            String serializedTodos = preferences.getString(Constants.TODOS, null); //if not found then null
            if(serializedTodos == null){
                //no todos yet, create new map
                todoDays = new HashMap<>();
                //easter egg
                Calendar calendar = Calendar.getInstance();
                TodoDay day = new TodoDay(calendar.getTime());
                Todo todo = new Todo();
                todo.setTitle("Kill all Jedi");
                todo.setPriority(TodoPriority.HIGH);
                todo.setDateAndTimeStart(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                todo.setDateAndTimeEnd(calendar.getTime());
                todo.setCathegory(getDummyCategories().get(1));
                todo.setDescription("Younglings in the Jedi temple as well. LOL.");
                todo.setNotification(true);
                Storage.addNewTodo(todo, context);
            }else{
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String, TodoDay>>(){}.getType();
                todoDays = gson.fromJson(serializedTodos, type);
            }
        }
        return todoDays;
    }

    /**
     * Gets tododay by specified date
     * @param date
     * @param context
     * @return corresponding tododay
     */
    public static TodoDay getTodoDayByDate(Date date, Activity context){
        String dateStr = Utils.dateFormatter.format(date);
        TodoDay day = getTodoDays(context).get(dateStr);
        if(day == null){
            day = new TodoDay(date);
            todoDays.put(dateStr, day);
        }
        return day;
    }

    /**
     * Gets tododay of current day
     * @param context
     * @return corrensponding tododay
     */
    public static TodoDay getCurrentTodoDay(Activity context){
        Calendar calendar = Calendar.getInstance();
        return getTodoDayByDate(calendar.getTime(), context);
    }

    /**
     * Adds new tudu into map
     * @param newTodo
     * @param context
     * @return true if saved successfully, false if errors occurred
     */
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
                todoDays.put(startDateStr, day);
                return true;
            }
        }else{
            //multiday task
            /*TODO idea of this application does not support multiday task but we want to give
            user an opportunity to make multiday tasks. So taks divided into multiple tasks
             which are saved in multiple days and are NO MORE RELATED to each other*/
            newTodo.setMultipleDays(true);
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            TodoDay dayStart = getTodoDayByDate(startDate, context);
            newTodo.setDateAndTimeEnd(c.getTime());
            dayStart.addTodo(newTodo);
            Storage.sortTodosByTime(dayStart);
            todoDays.put(Utils.dateFormatter.format(startDate), dayStart);
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
                    todoDays.put(Utils.dateFormatter.format(c.getTime()), dayMiddle);
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
                todoDays.put(Utils.dateFormatter.format(endDate), dayEnd);
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

    /**
     * Updates tudu map in shared preferences
     * @param context
     */
    public static void updateTodosInSharedPreferences(Activity context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, 0); //open in private mode
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String tododays  = gson.toJson(getTodoDays(context));
        editor.putString(Constants.TODOS, tododays);
        editor.apply();
    }


    /**
     * Gets tudu unique id from sharedpreferences - something like autoincrement
     * TODO get rid of it when we migrate from shared prefs to real database
     * @param activity
     * @return
     */
    public static int getUniqueTodoId(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, 0);
        SharedPreferences.Editor editor = preferences.edit();
        int uniqueId = preferences.getInt(Constants.TODO_ID, 0);
        editor.putInt(Constants.TODO_ID, uniqueId+1);
        editor.apply();
        return uniqueId;
    }

    /**
     * Edits tudu
     * @param id
     * @param todo
     * @param context
     * @return true if successfully edited, false otherwise
     */
    public static boolean editTodo(int id, Todo todo, Activity context) {
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

    /**
     * Gets tudu by date and id
     * @param date
     * @param id
     * @return corresponding tudu
     */
    private static Todo getTodoByDateAndId(Date date, int id) {
        String dateStr = Utils.dateFormatter.format(date);
        TodoDay day = todoDays.get(dateStr);
        for (Todo todo : day.getTodos()) {
            if(todo.getId() == id){
                return todo;
            }
        }
        return null;
    }

    /**
     * Sorts tudus in specified date by their dates and times in ascending order
     * @param day
     */
    private static void sortTodosByTime(TodoDay day) {
        Collections.sort(day.getTodos(), new Comparator<Todo>() {
            @Override
            public int compare(Todo lhs, Todo rhs) {
                return lhs.getDateAndTimeStart().compareTo(rhs.getDateAndTimeStart());
            }
        });
    }
}

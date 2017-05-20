package cz.matyapav.todoapp.util;

import android.app.Activity;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

    public static HashMap<String, TodoDay> getTodoDaysList() {
        if(todoDaysList == null){
            todoDaysList = new HashMap<>();
//            TodoDay today = new TodoDay();
//            Calendar calendar = Calendar.getInstance();
//            today.setDate(calendar.getTime());
//            //create todos in dummy tododay
//            for (int i = 0; i < 20; i++) {
//                Todo todo = new Todo();
//                todo.setTitle("Todo " + i);
//                todo.setDescription("Description " + i);
//                todo.setNotification(false);
//                if(i<=3) {
//                    todo.setPriority(TodoPriority.LOW);
//                    todo.setCathegory(getDummyCategories().get(0));
//                    todo.setCompleted(true);
//                }else if(i >=3 && i<=6){
//                    todo.setPriority(TodoPriority.MEDIUM);
//                    todo.setCathegory(getDummyCategories().get(1));
//                    todo.setCompleted(false);
//                }else{
//                    todo.setPriority(TodoPriority.HIGH);
//                    todo.setCathegory(getDummyCategories().get(2));
//                    todo.setCompleted(false);
//                }
//                calendar.set(Calendar.HOUR_OF_DAY, i + 1);
//                calendar.set(Calendar.MINUTE, i);
//                todo.setDateAndTimeStart(calendar.getTime());
//                calendar.set(Calendar.HOUR_OF_DAY, i + 2);
//                calendar.set(Calendar.MINUTE, 2*i);
//                todo.setDateAndTimeEnd(calendar.getTime());
//                today.addTodo(todo);
//            }
//
//            todoDaysList.put(Utils.dateFormatter.format(calendar.getTime()), today);
        }
        return todoDaysList;
    }

    public static List<TodoDay> getTodosForCurrentMonth(Date date) {
        List<TodoDay> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        for(String day : todoDaysList.keySet()){
            calendar.setTime(Utils.parseDate(day));
            if(calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month){
                result.add(todoDaysList.get(day));
            }
        }
        return result;
    }

    public static TodoDay getTodoDayByDate(Date date){
        String dateStr = Utils.dateFormatter.format(date);
        TodoDay day = getTodoDaysList().get(dateStr);
        if(day == null){
            day = new TodoDay(date);
            todoDaysList.put(dateStr, day);
        }
        return day;
    }

    public static TodoDay getCurrentTodoDay(){
        Calendar calendar = Calendar.getInstance();
        return getTodoDayByDate(calendar.getTime());
    }

    public static boolean addNewTodo(Todo newTodo) throws CloneNotSupportedException {
        Date startDate = newTodo.getDateAndTimeStart();
        Date endDate = newTodo.getDateAndTimeEnd();
        String startDateStr = Utils.dateFormatter.format(startDate);
        String endDateStr = Utils.dateFormatter.format(endDate);
        if(startDateStr.equals(endDateStr)) {
            TodoDay day = getTodoDayByDate(startDate);
            if (day != null) {
                day.addTodo(newTodo);
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
            TodoDay dayStart = getTodoDayByDate(startDate);
            newTodo.setDateAndTimeEnd(c.getTime());
            dayStart.addTodo(newTodo);
            todoDaysList.put(Utils.dateFormatter.format(startDate), dayStart);
            c.add(Calendar.MINUTE, 1);
            while(!Utils.dateFormatter.format(c.getTime()).equals(endDateStr)){
                TodoDay dayMiddle = getTodoDayByDate(c.getTime());
                Todo todoMiddle = (Todo) newTodo.clone();
                todoMiddle.setDateAndTimeStart(c.getTime());
                c.set(Calendar.HOUR_OF_DAY, 23);
                c.set(Calendar.MINUTE, 59);
                todoMiddle.setDateAndTimeEnd(c.getTime());
                dayMiddle.addTodo(todoMiddle);
                todoDaysList.put(Utils.dateFormatter.format(c.getTime()), dayMiddle);
                c.add(Calendar.MINUTE, 1);
            }
            Todo end = (Todo) newTodo.clone();
            end.setDateAndTimeStart(c.getTime());
            end.setDateAndTimeEnd(endDate);
            TodoDay dayEnd = getTodoDayByDate(c.getTime());
            todoDaysList.put(Utils.dateFormatter.format(endDate), dayEnd);
            dayEnd.addTodo(end);
            return true;
        }
        return false;
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

    public static boolean editTodo(int id, Todo todo) {
        Todo edited = getTodoByDateAndId(todo.getDateAndTimeStart(), id);
        if(edited != null) {
            edited.setTitle(todo.getTitle());
            edited.setDateAndTimeStart(todo.getDateAndTimeStart());
            edited.setDateAndTimeEnd(todo.getDateAndTimeEnd());
            edited.setNotification(todo.isNotification());
            edited.setCathegory(todo.getCathegory());
            edited.setPriority(todo.getPriority());
            edited.setDescription(todo.getDescription());
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
}

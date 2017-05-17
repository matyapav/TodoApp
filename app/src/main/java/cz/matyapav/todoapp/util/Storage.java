package cz.matyapav.todoapp.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
            TodoDay today = new TodoDay();
            Calendar calendar = Calendar.getInstance();
            today.setDate(calendar.getTime());
            //create todos in dummy tododay
            for (int i = 0; i < 20; i++) {
                Todo todo = new Todo();
                todo.setTitle("Todo " + i);
                todo.setDescription("Description " + i);
                todo.setNotification(false);
                if(i<=3) {
                    todo.setPriority(TodoPriority.LOW);
                    todo.setCathegory(getDummyCategories().get(0));
                    todo.setCompleted(true);
                }else if(i >=3 && i<=6){
                    todo.setPriority(TodoPriority.MEDIUM);
                    todo.setCathegory(getDummyCategories().get(1));
                    todo.setCompleted(false);
                }else{
                    todo.setPriority(TodoPriority.HIGH);
                    todo.setCathegory(getDummyCategories().get(2));
                    todo.setCompleted(false);
                }
                calendar.set(Calendar.HOUR_OF_DAY, i + 1);
                calendar.set(Calendar.MINUTE, i);
                todo.setDateAndTimeStart(calendar.getTime());
                calendar.set(Calendar.HOUR_OF_DAY, i + 2);
                calendar.set(Calendar.MINUTE, 2*i);
                todo.setDateAndTimeEnd(calendar.getTime());
                today.addTodo(todo);
            }

            todoDaysList.put(Utils.dateFormatter.format(calendar.getTime()), today);
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
        return getTodoDaysList().get(dateStr);
    }

    public static TodoDay getCurrentTodoDay(){
        Calendar calendar = Calendar.getInstance();
        return getTodoDayByDate(calendar.getTime());
    }
}

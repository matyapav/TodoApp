package cz.matyapav.todoapp.util;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.util.enums.TodoPriority;
import cz.matyapav.todoapp.todo.model.Cathegory;
import cz.matyapav.todoapp.todo.model.Todo;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class Utils {

    private static List<Cathegory> dummyCathegories;
    private static List<Todo> dummyTodoList;
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static List<Cathegory> getDummyCategories(){
        if(dummyCathegories == null){
            dummyCathegories = new ArrayList<>();
            dummyCathegories.add(new Cathegory("Personal",null, R.drawable.ic_personal));
            dummyCathegories.add(new Cathegory("Work", null, R.drawable.ic_work));
            dummyCathegories.add(new Cathegory("Free time", null, R.drawable.ic_free_time));
        }
        return dummyCathegories;
    }

    public static List<Todo> getDummyTodoList() {
        if(dummyTodoList == null){
            dummyTodoList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Todo todo = new Todo();
                todo.setTitle("Todo " + i);
                todo.setDescription("Description " + i);
                todo.setNotification(false);
                if(i<=3) {
                    todo.setPriority(TodoPriority.LOW);
                    todo.setCathegory(Utils.getDummyCategories().get(0));
                    todo.setCompleted(true);
                }else if(i >=3 && i<=6){
                    todo.setPriority(TodoPriority.MEDIUM);
                    todo.setCathegory(Utils.getDummyCategories().get(1));
                    todo.setCompleted(false);
                }else{
                    todo.setPriority(TodoPriority.HIGH);
                    todo.setCathegory(Utils.getDummyCategories().get(2));
                    todo.setCompleted(false);
                }
                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.set(2017,27,4,i+1,i);
                todo.setDateAndTimeStart(calendar.getTime());
                calendar.set(2017,27,4,i+2,2*i);
                todo.setDateAndTimeEnd(calendar.getTime());
                dummyTodoList.add(todo);
            }
        }
        return dummyTodoList;
    }

    public static int getValueSpinnerPosition(Spinner spinner, String value)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                index = i;
                break;
            }
        }
        return index;
    }


}

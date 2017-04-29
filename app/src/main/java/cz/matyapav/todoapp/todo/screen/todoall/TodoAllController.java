package cz.matyapav.todoapp.todo.screen.todoall;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.screen.list.TodoDayViewHolder;
import cz.matyapav.todoapp.todo.util.adapters.CalendarAdapter;
import cz.matyapav.todoapp.todo.util.adapters.TodoDayAdapter;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoAllController {

    Activity context;
    TodoAllViewHolder vh;


    public TodoAllController(Activity context, TodoAllViewHolder vh) {
        this.context = context;
        this.vh = vh;
    }

    void setCurrentMonth(){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        vh.currentMonth.setText(month+" "+year);
    }

    void setCurrentMonthDays(){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<TodoDay> days = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK)-2;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);


        //TODO odstranit dummy pridavani todolistu na aktualini den
        for (int i = 0; i < 42; i++) {
            if(calendar.get(Calendar.DAY_OF_MONTH) == currentDay && calendar.get(Calendar.MONTH) == currentMonth){
                days.add(new TodoDay(calendar.getTime(), Utils.getDummyTodoList()));
            }else{
                days.add(new TodoDay(calendar.getTime(), null));
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        vh.calendarView.setAdapter(new CalendarAdapter(context, days));
        // update grid
    }
}

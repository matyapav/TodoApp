package cz.matyapav.todoapp.todo.screen.todoall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.screen.list.TodoDayFragment;
import cz.matyapav.todoapp.todo.screen.list.TodoDayViewHolder;
import cz.matyapav.todoapp.todo.util.adapters.CalendarAdapter;
import cz.matyapav.todoapp.todo.util.adapters.TodoDayAdapter;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoAllController {

    FragmentActivity context;
    TodoAllViewHolder vh;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

    private static final int DAYS_COUNT = 42;


    public TodoAllController(FragmentActivity context, TodoAllViewHolder vh) {
        this.context = context;
        this.vh = vh;
    }

    void setFabAction(){
        vh.newTodoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(context, CreateTodoActivity.class);
            context.startActivity(i);
            }
        });
    }


    void setCurrentMonth(){
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        vh.currentMonth.setText(month+" "+year);
    }

    void setCurrentMonthDays(){
//        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        Calendar calendar = (Calendar) this.calendar.clone();
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<TodoDay> days = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK)-2;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);


        //TODO odstranit dummy pridavani todolistu na aktualini den
        for (int i = 0; i < DAYS_COUNT; i++) {
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

    void setListenersOnDays(){
        final FragmentManager fragmentManager = context.getSupportFragmentManager();
        vh.calendarView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Fragment fragment = TodoDayFragment.class.newInstance();
                    Bundle bundle = new Bundle();
                    TodoDay day = (TodoDay) parent.getAdapter().getItem(position);
                    Date date = day.getDate();
                    bundle.putSerializable("currentDate", date);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.mainLayout, fragment, "currentDate").commit();
                    ((NavigationView) context.findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_todo_today);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void setListenerOnPrevMonth(){
        vh.prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                setCurrentMonth();
                setCurrentMonthDays();
            }
        });
    }

    void setListenerOnNextMonth(){
        vh.nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                setCurrentMonth();
                setCurrentMonthDays();
            }
        });
    }

    //TODO set monthly total todo count and completed count
}

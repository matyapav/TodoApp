package cz.matyapav.todoapp.todo.screen.todoall;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.screen.list.TodoDayFragment;
import cz.matyapav.todoapp.todo.util.adapters.CalendarAdapter;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.Storage;
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


    public TodoAllController(FragmentActivity context, final TodoAllViewHolder vh) {
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
        vh.currentMonth.setText(month + " " + year);
    }

    void setCurrentMonthDays(){
//        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        Calendar calendar = (Calendar) this.calendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK)-2;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);


        HashMap<String, TodoDay> days = Storage.getTodoDaysList();
        List<TodoDay> daysToCalendar = new ArrayList<>();

        //TODO vylepsit kdyz bude cas
        for (int i = 0; i < DAYS_COUNT; i++) {
            for(String day : days.keySet()){
                if(Utils.dateFormatter.format(calendar.getTime()).equals(day)){
                    daysToCalendar.add(days.get(day));
                }else{
                    daysToCalendar.add(new TodoDay(calendar.getTime(), null));
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        vh.calendarView.setAdapter(new CalendarAdapter(context, daysToCalendar, this.calendar.get(Calendar.MONTH)));
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
                    bundle.putSerializable(Constants.CURRENT_DATE, date);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.mainLayout, fragment, Constants.CURRENT_DATE).commit();
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

    void animateCalendar(){



    }

    //TODO set monthly total todo count and completed count
}

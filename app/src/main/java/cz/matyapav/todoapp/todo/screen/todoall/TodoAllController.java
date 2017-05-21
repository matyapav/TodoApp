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

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.screen.list.TodoDayFragment;
import cz.matyapav.todoapp.todo.adapters.CalendarAdapter;
import cz.matyapav.todoapp.todo.util.enums.SupportedLanguages;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.Storage;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoAllController {

    private FragmentActivity context;
    private TodoAllViewHolder vh;
    private Calendar calendar;
    Fragment fragment;

    private static final int DAYS_COUNT = 42;

    TodoAllController(FragmentActivity context, final TodoAllViewHolder vh, Fragment fragment) {
        this.context = context;
        this.vh = vh;
        this.calendar = Calendar.getInstance();
        this.fragment = fragment;
    }

    /**
     * Sets action on floating action button
     */
    void setFabAction(){
        vh.newTodoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreateTodoActivity.class);
                fragment.startActivityForResult(i, Constants.TODO_CREATE_EDIT_REQUEST_CODE);
            }
        });
    }

    /**
     * Sets current displayed month
     */
    void setCurrentMonth(){
        String month = null;
        String locale = context.getResources().getConfiguration().locale.getLanguage();
        if(locale.equals(SupportedLanguages.CZECH.getLangAbbreviation())){
            month = Utils.getMonthStandaloneName(calendar.get(Calendar.MONTH));
        }else {
            month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, context.getResources().getConfiguration().locale);
        }
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        vh.currentMonth.setText(month + " " + year);
    }

    /**
     * Sets days in selected month
     */
    void setCurrentMonthDays(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, this.calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = (7 + calendar.get(Calendar.DAY_OF_WEEK)-Calendar.MONDAY) % 7;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        HashMap<String, TodoDay> days = Storage.getTodoDays(context);
        List<TodoDay> daysToCalendar = new ArrayList<>();
        for (int i = 0; i < DAYS_COUNT; i++) {
            boolean addedFromDays = false;
            for(String day : days.keySet()){
                String dayStr = Utils.dateFormatter.format(calendar.getTime());
                if(dayStr.equals(day)){
                    daysToCalendar.add(days.get(day));
                    addedFromDays = true;
                    break;
                }
            }
            if(!addedFromDays) {
                daysToCalendar.add(new TodoDay(calendar.getTime(), null));
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        setCurrentMonthTodosCount(daysToCalendar, this.calendar);

        vh.calendarView.setAdapter(new CalendarAdapter(context, daysToCalendar, this.calendar.get(Calendar.MONTH)));
        // update grid
    }

    /**
     * Sets number of completed and uncompleted todos in current displayed month
     * @param todos
     * @param calendar
     */
    private void setCurrentMonthTodosCount(List<TodoDay> todos, Calendar calendar) {
        int sumOfCompleted = 0;
        int sumOfAll = 0;

        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        for (TodoDay todo : todos) {
            Calendar c = Calendar.getInstance();
            c.setTime(todo.getDate());
            if(c.get(Calendar.MONTH) == currentMonth && c.get(Calendar.YEAR) == currentYear){
                sumOfAll+= todo.getTodosCount();
                sumOfCompleted += todo.getNumberOfCompletedTodos();
            }
        }
        vh.numberOfAll.setText(String.valueOf(sumOfAll));
        vh.numberOfCompleted.setText(String.valueOf(sumOfCompleted));
    }

    /**
     * Sets onclick listeners on days
     */
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

    /**
     * Sets onclick listener on previous month button
     */
    void setListenerOnPrevMonth(){
        vh.prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                setCurrentMonth();
                setCurrentMonthDays();
                animateCalendar();
            }
        });
    }

    /**
     * Sets onclick listener on next month button
     */
    void setListenerOnNextMonth(){
        vh.nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                setCurrentMonth();
                setCurrentMonthDays();
                animateCalendar();
            }
        });
    }

    /**
     * Notifies calendar adapter that data in it were changed externally
     */
    void notifyAdapterDataChanged(){
        vh.calendarView.setAdapter(null);
        setCurrentMonthDays();
    }

    /**
     * Animates calendar view
     */
    void animateCalendar(){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.calendar_animation);
        vh.calendarView.startAnimation(animation);
    }

}

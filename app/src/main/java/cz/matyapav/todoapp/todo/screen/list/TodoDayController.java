package cz.matyapav.todoapp.todo.screen.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.util.adapters.AdapterObserver;
import cz.matyapav.todoapp.todo.util.adapters.TodoDayAdapter;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.SimpleDividerItemDecoration;
import cz.matyapav.todoapp.util.Storage;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayController implements AdapterObserver {

    Activity context;
    TodoDayViewHolder vh;
    TodoDay currentDay;

    public TodoDayController(Activity context, TodoDayViewHolder vh) {
        this.context = context;
        this.vh = vh;
        this.currentDay = Storage.getCurrentTodoDay();
    }

    void setFabAction(){
        vh.newTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreateTodoActivity.class);
                context.startActivity(i);
            }
        });
    }

    void initTodoListAdapter(){
        vh.listView.setHasFixedSize(true);
        vh.listView.setLayoutManager(new LinearLayoutManager(context));
        vh.listView.setAdapter(new TodoDayAdapter(context, currentDay.getTodos(), this));
        vh.listView.addItemDecoration(new SimpleDividerItemDecoration(context));
    }


    void setDay(Date currentDate){
        Calendar calendar = Calendar.getInstance();
        if(currentDate != null) {
            calendar.setTime(currentDate);
        }
        vh.dayOfMonth.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        vh.dayOfWeek.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
        vh.month.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        currentDay = Storage.getTodoDayByDate(calendar.getTime());
        TodoDayAdapter adapter = (TodoDayAdapter) vh.listView.getAdapter();
        if(currentDay != null) {
            adapter.changeDataSet(currentDay.getTodos());
        }else{
            adapter.changeDataSet(new ArrayList<Todo>());
        }
    }

    void setTodoStatus(){
        if(currentDay != null) {
            int numberOfCompletedTodos = currentDay.getNumberOfCompletedTodos();
            vh.completedTodos.setText(String.valueOf(numberOfCompletedTodos));
            vh.totalTodos.setText(String.valueOf(currentDay.getTodos().size()));
        }
    }

    @Override
    public void onAdapterDataChanged() {
        setTodoStatus();
    }

    public void toggleCompletedTodoVisibility(){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        boolean showCompleted = sharedPref.getBoolean(Constants.PREFS_SHOW_COMPLETED, true);
        editor.putBoolean(Constants.PREFS_SHOW_COMPLETED, !showCompleted);
        editor.apply();
    }
}

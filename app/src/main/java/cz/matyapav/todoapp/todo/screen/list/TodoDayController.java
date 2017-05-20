package cz.matyapav.todoapp.todo.screen.list;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.adapters.AdapterObserver;
import cz.matyapav.todoapp.todo.adapters.TodoDayAdapter;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.SimpleDividerItemDecoration;
import cz.matyapav.todoapp.util.Storage;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayController implements AdapterObserver {

    Activity context;
    TodoDayViewHolder vh;
    TodoDay currentDay;
    Fragment fragment;

    public TodoDayController(Activity context, TodoDayViewHolder vh, Fragment fragment) {
        this.context = context;
        this.vh = vh;
        this.currentDay = Storage.getCurrentTodoDay(context);
        this.fragment = fragment;
    }

    void setFabAction(){
        vh.newTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreateTodoActivity.class);
                i.putExtra(Constants.CURRENT_DATE, Utils.dateFormatter.format(currentDay.getDate()));
                fragment.startActivityForResult(i, Constants.TODO_CREATE_EDIT_REQUEST_CODE);
            }
        });
    }

    private void initTodoDayDatePickerDialog(final Date chosenDate){
        vh.dayWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(chosenDate);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        setDay(newDate.getTime());
                        //vh.createTodoDate.setText(Utils.dateFormatter.format(newDate.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }


    void initTodoListAdapter(){
        vh.listView.setHasFixedSize(true);
        vh.listView.setLayoutManager(new LinearLayoutManager(context));
        vh.listView.setAdapter(new TodoDayAdapter(context, currentDay.getTodos(), this, fragment));
        vh.listView.addItemDecoration(new SimpleDividerItemDecoration(context));
    }


    void setDay(Date currentDate){
        Calendar calendar = Calendar.getInstance();
        if(currentDate != null) {
            calendar.setTime(currentDate);
        }
        vh.dayOfMonth.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        vh.dayOfWeek.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, context.getResources().getConfiguration().locale));
        vh.month.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, context.getResources().getConfiguration().locale));
        currentDay = Storage.getTodoDayByDate(calendar.getTime(), context);
        TodoDayAdapter adapter = (TodoDayAdapter) vh.listView.getAdapter();
        if(currentDay != null) {
            adapter.changeDataSet(currentDay.getTodos());
        }else{
            adapter.changeDataSet(new ArrayList<Todo>());
        }
        initTodoDayDatePickerDialog(calendar.getTime());
    }

    void setTodoStatus(){
        if(currentDay != null) {
            int numberOfCompletedTodos = currentDay.getNumberOfCompletedTodos();
            vh.completedTodos.setText(String.valueOf(numberOfCompletedTodos));
            vh.totalTodos.setText(String.valueOf(currentDay.getTodos().size()));
        }
    }

    public void notifyAdapterDataChanged(){
        TodoDayAdapter adapter = (TodoDayAdapter) vh.listView.getAdapter();
        adapter.changeDataSet();
    }

    @Override
    public void onAdapterDataChanged() {
        setTodoStatus();
        Storage.updateTodosInSharedPreferences(context);
    }

    public void toggleCompletedTodoVisibility(){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        boolean showCompleted = sharedPref.getBoolean(Constants.PREFS_SHOW_COMPLETED, true);
        editor.putBoolean(Constants.PREFS_SHOW_COMPLETED, !showCompleted);
        editor.apply();
    }
}

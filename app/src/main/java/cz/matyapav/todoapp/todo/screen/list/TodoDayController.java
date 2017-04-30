package cz.matyapav.todoapp.todo.screen.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.screen.todoall.TodoAllFragment;
import cz.matyapav.todoapp.todo.util.adapters.TodoDayAdapter;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayController {

    Activity context;
    TodoDayViewHolder vh;
    ArrayAdapter<Todo> arrayAdapter;

    public TodoDayController(Activity context, TodoDayViewHolder vh) {
        this.context = context;
        this.vh = vh;
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

    void setSwipeAction(){
        vh.listView.setOnTouchListener(new OnSwipeTouchListener(context, vh.listView, arrayAdapter));
    }

    void initTodoListAdapter(){
        arrayAdapter = new TodoDayAdapter(context, Utils.getDummyTodoList());

        vh.listView.setAdapter(arrayAdapter);
    }

    void setDay(Date currentDate){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        if(currentDate != null) {
            calendar.setTime(currentDate);
        }
        vh.dayOfMonth.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        vh.dayOfWeek.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
        vh.month.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
    }

    void setTodoStatus(){
        int numberOfCompletedTodos = getNumberOfCompletedTodos(Utils.getDummyTodoList());
        vh.completedTodos.setText(String.valueOf(numberOfCompletedTodos));
        vh.totalTodos.setText(String.valueOf(Utils.getDummyTodoList().size()));
    }

    private int getNumberOfCompletedTodos(List<Todo> todos){
        int result = 0;
        for (Todo todo : todos) {
            if(todo.isCompleted()){
                result++;
            }
        }
        return result;
    }
}

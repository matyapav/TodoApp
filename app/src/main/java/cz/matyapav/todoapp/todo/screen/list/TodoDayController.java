package cz.matyapav.todoapp.todo.screen.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.util.adapters.TodoDayAdapter;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayController {

    Activity context;
    TodoDayViewHolder vh;


    public TodoDayController(Activity context, TodoDayViewHolder vh) {
        this.context = context;
        this.vh = vh;
    }

    void setFabAction(){
        vh.newTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreateTodoActivity.class);
                context.startActivityForResult(i, 1);
            }
        });
    }

    void initTodoListAdapter(){
        TodoDayAdapter adapter = new TodoDayAdapter(context, Utils.getDummyTodoList());
        vh.listView.setAdapter(adapter);
    }

    void setDay(){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
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

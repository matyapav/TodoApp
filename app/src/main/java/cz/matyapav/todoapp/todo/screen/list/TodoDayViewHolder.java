package cz.matyapav.todoapp.todo.screen.list;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.ViewHolder;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayViewHolder extends ViewHolder {

    RecyclerView listView;
    TextView dayOfMonth;
    TextView dayOfWeek;
    TextView month;
    TextView completedTodos;
    TextView totalTodos;
    FloatingActionButton newTodoBtn;

    public TodoDayViewHolder(View context) {
        super(context);
    }

    @Override
    public void findViews() {
        listView = (RecyclerView) getContext().findViewById(R.id.todo_list);
        dayOfMonth = (TextView) getContext().findViewById(R.id.todo_day_in_month);
        dayOfWeek = (TextView) getContext().findViewById(R.id.todo_day_in_week);
        month = (TextView) getContext().findViewById(R.id.todo_month);
        completedTodos = (TextView) getContext().findViewById(R.id.todo_completed_number);
        totalTodos = (TextView) getContext().findViewById(R.id.todo_total_number);
        newTodoBtn = (FloatingActionButton) getContext().findViewById(R.id.create_todo_fab);
    }


}

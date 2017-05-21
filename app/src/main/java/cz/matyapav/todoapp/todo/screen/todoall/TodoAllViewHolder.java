package cz.matyapav.todoapp.todo.screen.todoall;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.ViewHolder;

/**
 * Holder for views used in TodoAllFragment
 */
public class TodoAllViewHolder extends ViewHolder {

    TextView currentMonth;
    Button nextMonth;
    Button prevMonth;
    GridView calendarView;
    FloatingActionButton newTodoFab;
    TextView numberOfCompleted;
    TextView numberOfAll;

    public TodoAllViewHolder(View context) {
        super(context);
    }

    @Override
    public void findViews() {
        currentMonth = (TextView) getContext().findViewById(R.id.calendar_date_display);
        nextMonth = (Button) getContext().findViewById(R.id.calendar_next_button);
        prevMonth = (Button) getContext().findViewById(R.id.calendar_prev_button);
        calendarView = (GridView) getContext().findViewById(R.id.calendar_grid);
        newTodoFab = (FloatingActionButton) getContext().findViewById(R.id.todo_all_create_fab);
        numberOfCompleted = (TextView) getContext().findViewById(R.id.todo_all_completed_number);
        numberOfAll = (TextView) getContext().findViewById(R.id.todo_all_total_number);
    }


}

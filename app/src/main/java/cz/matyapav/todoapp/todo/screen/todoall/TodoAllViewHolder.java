package cz.matyapav.todoapp.todo.screen.todoall;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.ViewHolder;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoAllViewHolder extends ViewHolder {

    TextView currentMonth;
    ImageView nextMonth;
    ImageView prevMonth;
    GridView calendarView;

    public TodoAllViewHolder(View context) {
        super(context);
    }

    @Override
    public void findViews() {
        currentMonth = (TextView) getContext().findViewById(R.id.calendar_date_display);
        nextMonth = (ImageView) getContext().findViewById(R.id.calendar_next_button);
        prevMonth = (ImageView) getContext().findViewById(R.id.calendar_prev_button);
        calendarView = (GridView) getContext().findViewById(R.id.calendar_grid);
    }


}

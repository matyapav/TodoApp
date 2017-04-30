package cz.matyapav.todoapp.todo.screen.create;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.ViewHolder;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class CreateTodoViewHolder extends ViewHolder {

    EditText createTodoDate;
    EditText createTodoStartTime;
    EditText createTodoEndTime;
    LinearLayout lowPrioWrapper;
    LinearLayout medPrioWrapper;
    LinearLayout highPrioWrapper;
    ImageView lowPrioIcon;
    ImageView medPrioIcon;
    ImageView highPrioIcon;
    Spinner cathegorySpinner;
    FloatingActionButton finishFab;
    EditText todoTitle;
    Switch notification;
    EditText description;


    public CreateTodoViewHolder(View context) {
        super(context);
    }

    @Override
    public void findViews() {
        createTodoDate = (EditText) getContext().findViewById(R.id.create_todo_date);
        createTodoStartTime = (EditText) getContext().findViewById(R.id.create_todo_start_time);
        createTodoEndTime = (EditText) getContext().findViewById(R.id.create_todo_end_time);
        lowPrioWrapper = (LinearLayout) getContext().findViewById(R.id.create_todo_low_priority_btn);
        medPrioWrapper = (LinearLayout) getContext().findViewById(R.id.create_todo_med_priority_btn);
        highPrioWrapper = (LinearLayout) getContext().findViewById(R.id.create_todo_high_priority_btn);
        lowPrioIcon = (ImageView) getContext().findViewById(R.id.create_todo_low_priority_icon);
        medPrioIcon = (ImageView) getContext().findViewById(R.id.create_todo_med_priority_icon);
        highPrioIcon = (ImageView) getContext().findViewById(R.id.create_todo_high_priority_icon);
        cathegorySpinner = (Spinner) getContext().findViewById(R.id.create_todo_category);
        finishFab = (FloatingActionButton) getContext().findViewById(R.id.finish_create_todo_fab);
        todoTitle = (EditText) getContext().findViewById(R.id.create_todo_title);
        notification = (Switch) getContext().findViewById(R.id.create_todo_notification);
        description = (EditText) getContext().findViewById(R.id.create_todo_description);
    }

}

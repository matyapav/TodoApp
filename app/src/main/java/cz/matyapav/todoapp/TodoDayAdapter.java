package cz.matyapav.todoapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.utils.Utils;


/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayAdapter extends ArrayAdapter<Todo> {

    private List<Todo> todos;

    public TodoDayAdapter(Context context, List<Todo> todos) {
        super(context, 0, todos);
        this.todos = todos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Todo todo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_list_item, parent, false);
        }

        TextView todoTime = (TextView) convertView.findViewById(R.id.todo_time);
        TextView todoTitle = (TextView) convertView.findViewById(R.id.todo_title);
        ImageView todoIcon = (ImageView) convertView.findViewById(R.id.todo_cathegory_icon);
        ImageView todoSecondaryAction = (ImageView) convertView.findViewById(R.id.todo_secondary_action_icon);

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(todo.getDateAndTimeStart());
        String start = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        calendar.setTime(todo.getDateAndTimeEnd());
        String end = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        todoTime.setText(start + " - " + end);
        todoTitle.setText(todo.getTitle());
        todoIcon.setImageResource(todo.getCathegory().getImgResourceId());
        int color = todo.getPriority().getColorId();
        GradientDrawable iconBg = (GradientDrawable) todoIcon.getBackground().mutate();
        iconBg.setColor(Utils.getColor(getContext(), color));

        return convertView;
    }

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Todo getItem(int position) {
        return todos.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }
}

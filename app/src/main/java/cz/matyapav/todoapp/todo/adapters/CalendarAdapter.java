package cz.matyapav.todoapp.todo.adapters;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.util.enums.TodoPriority;
import cz.matyapav.todoapp.util.Utils;

/**
 * Calendar adapter - used for creating custom calendar view
 */
public class CalendarAdapter extends ArrayAdapter<TodoDay> {

    List<TodoDay> days;
    int month;

    public CalendarAdapter(FragmentActivity context, List<TodoDay> days, int month) {
        super(context, 0, days);
        this.days = days;
        this.month = month;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TodoDay day = getItem(position);

       if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_item, parent, false);
       }

        TextView dayView = (TextView) convertView.findViewById(R.id.calendar_day_view);
        TextView lowTasks = (TextView) convertView.findViewById(R.id.calendar_day_view_low_priority_tasks);
        TextView medTasks = (TextView) convertView.findViewById(R.id.calendar_day_view_med_priority_tasks);
        TextView highTasks = (TextView) convertView.findViewById(R.id.calendar_day_view_high_priority_tasks);
        ImageView lowIcon = (ImageView) convertView.findViewById(R.id.calendar_day_view_low_priority_tasks_icon);
        ImageView medIcon = (ImageView) convertView.findViewById(R.id.calendar_day_view_med_priority_tasks_icon);
        ImageView highIcon = (ImageView) convertView.findViewById(R.id.calendar_day_view_high_priority_tasks_icon);

        GradientDrawable lowIconBg = (GradientDrawable) lowIcon.getBackground().mutate();
        lowIconBg.setColor(Utils.getColor(getContext(), TodoPriority.LOW.getColorId()));

        GradientDrawable medIconBg = (GradientDrawable) medIcon.getBackground().mutate();
        medIconBg.setColor(Utils.getColor(getContext(), TodoPriority.MEDIUM.getColorId()));

        GradientDrawable highIconBg = (GradientDrawable) highIcon.getBackground().mutate();
        highIconBg.setColor(Utils.getColor(getContext(), TodoPriority.HIGH.getColorId()));

        int lowTasksCount = day.getTodosCount(TodoPriority.LOW);
        int medTasksCount = day.getTodosCount(TodoPriority.MEDIUM);
        int highTasksCount = day.getTodosCount(TodoPriority.HIGH);

        if(lowTasksCount <= 0){
            lowTasks.setVisibility(View.INVISIBLE);
            lowIcon.setVisibility(View.INVISIBLE);
        }else{
            lowTasks.setText(String.valueOf(lowTasksCount));
        }
        if(medTasksCount <= 0){
            medTasks.setVisibility(View.INVISIBLE);
            medIcon.setVisibility(View.INVISIBLE);
        }else{
            medTasks.setText(String.valueOf(medTasksCount));
        }
        if(highTasksCount <= 0){
            highTasks.setVisibility(View.INVISIBLE);
            highIcon.setVisibility(View.INVISIBLE);
        }else{
            highTasks.setText(String.valueOf(highTasksCount));
        }

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.setTime(day.getDate());
        dayView.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        if(calendar.get(Calendar.MONTH) == month){
            dayView.setTextColor(Utils.getColor(getContext(), R.color.primary_text));
        }else{
            dayView.setTextColor(Utils.getColor(getContext(), R.color.secondary_text));
        }
        if(calendar.get(Calendar.DAY_OF_MONTH) == currentDay
                && calendar.get(Calendar.MONTH) == currentMonth
                && calendar.get(Calendar.YEAR) == currentYear){
            dayView.setTextColor(Utils.getColor(getContext(), R.color.colorAccent));
            dayView.setTypeface(null, Typeface.BOLD);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public TodoDay getItem(int position) {
        return days.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }
}

package cz.matyapav.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.todo.enums.TodoPriority;
import cz.matyapav.todoapp.todo.model.Cathegory;
import cz.matyapav.todoapp.todo.model.Todo;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_day_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.todo_list);
       // ViewCompat.setNestedScrollingEnabled(listView, true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        setHasOptionsMenu(false);

        List<Todo> todos = getDummyTodoList();
        TodoDayAdapter adapter = new TodoDayAdapter(getContext(), todos);
        listView.setAdapter(adapter);

        //set day
        TextView dayOfMonth = (TextView) view.findViewById(R.id.todo_day_in_month);
        TextView dayOfWeek = (TextView) view.findViewById(R.id.todo_day_in_week);
        TextView month = (TextView) view.findViewById(R.id.todo_month);

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        dayOfMonth.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        dayOfWeek.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
        month.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));

        //set todo status
        TextView completedTodos = (TextView) view.findViewById(R.id.todo_completed_number);
        TextView totalTodos = (TextView) view.findViewById(R.id.todo_total_number);
        int numberOfCompletedTodos = getNumberOfCompletedTodos(todos);
        completedTodos.setText(String.valueOf(numberOfCompletedTodos));
        totalTodos.setText(String.valueOf(todos.size()));
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
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

    private List<Todo> getDummyTodoList() {
        List<Todo> dummyTodoList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Todo todo = new Todo();
            todo.setTitle("Todo " + i);
            todo.setDescription("Description " + i);
            todo.setNotification(false);
            if(i<=3) {
                todo.setPriority(TodoPriority.LOW);
                todo.setCathegory(new Cathegory("Personal",null,R.drawable.ic_personal));
                todo.setCompleted(true);
            }else if(i >=3 && i<=6){
                todo.setPriority(TodoPriority.MEDIUM);
                todo.setCathegory(new Cathegory("Work", null, R.drawable.ic_work));
                todo.setCompleted(false);
            }else{
                todo.setPriority(TodoPriority.HIGH);
                todo.setCathegory(new Cathegory("Free time", null, R.drawable.ic_free_time));
                todo.setCompleted(false);
            }
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.set(2017,27,4,i+1,i);
            todo.setDateAndTimeStart(calendar.getTime());
            calendar.set(2017,27,4,i+2,2*i);
            todo.setDateAndTimeEnd(calendar.getTime());
            dummyTodoList.add(todo);
        }
        return dummyTodoList;
    }
}

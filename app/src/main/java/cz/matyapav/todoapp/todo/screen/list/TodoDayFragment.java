package cz.matyapav.todoapp.todo.screen.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.util.adapters.TodoDayAdapter;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.util.Constants;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDayFragment extends Fragment {

    TodoDayController controller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        Date currentDate = null;
        if(arguments != null)currentDate = (Date) arguments.getSerializable(Constants.CURRENT_DATE);
        View view = inflater.inflate(R.layout.todo_day_fragment, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.todo_today));
        setHasOptionsMenu(true);

        controller = new TodoDayController(getActivity(), new TodoDayViewHolder(view));
        controller.initTodoListAdapter();
        controller.setDay(currentDate);
        controller.setTodoStatus();
        controller.setFabAction();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.todo_day, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_show_completed){
            item.setChecked(!item.isChecked());
            controller.toggleCompletedTodoVisibility();
            return false;

        }
        return super.onOptionsItemSelected(item);
    }
}

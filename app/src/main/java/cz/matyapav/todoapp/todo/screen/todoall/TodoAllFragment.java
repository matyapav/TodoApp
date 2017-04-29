package cz.matyapav.todoapp.todo.screen.todoall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.screen.list.TodoDayController;
import cz.matyapav.todoapp.todo.screen.list.TodoDayViewHolder;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoAllFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_all_fragment, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.todo_all));
        setHasOptionsMenu(false);

        TodoAllController controller = new TodoAllController(getActivity(), new TodoAllViewHolder(view));
        controller.setCurrentMonth();
        controller.setCurrentMonthDays();
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }




}

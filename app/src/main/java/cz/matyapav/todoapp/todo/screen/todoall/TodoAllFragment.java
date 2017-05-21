package cz.matyapav.todoapp.todo.screen.todoall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.screen.list.TodoDayController;
import cz.matyapav.todoapp.todo.screen.list.TodoDayViewHolder;
import cz.matyapav.todoapp.util.Constants;

/**
 * Todoall fragment - displays monthly view of todos in calendar
 */
public class TodoAllFragment extends Fragment {
    TodoAllController controller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.todo_all_fragment, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.todo_all));
        setHasOptionsMenu(false);

        controller = new TodoAllController(getActivity(), new TodoAllViewHolder(view), this);
        controller.setCurrentMonth();
        controller.setCurrentMonthDays();
        controller.setListenersOnDays();
        controller.setListenerOnPrevMonth();
        controller.setListenerOnNextMonth();
        controller.setFabAction();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.TODO_CREATE_EDIT_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                controller.notifyAdapterDataChanged();
            }
        }
    }







}

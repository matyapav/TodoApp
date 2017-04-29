package cz.matyapav.todoapp.todo.screen.todoall;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.todo.screen.create.CreateTodoActivity;
import cz.matyapav.todoapp.todo.screen.list.TodoDayViewHolder;
import cz.matyapav.todoapp.todo.util.adapters.TodoDayAdapter;
import cz.matyapav.todoapp.util.Utils;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoAllController {

    Activity context;
    TodoDayViewHolder vh;


    public TodoAllController(Activity context, TodoDayViewHolder vh) {
        this.context = context;
        this.vh = vh;
    }
}

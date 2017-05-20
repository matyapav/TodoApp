package cz.matyapav.todoapp.todo.screen.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.Todo;
import cz.matyapav.todoapp.util.Constants;

public class CreateTodoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CreateTodoController controller;

        Todo prefilledTodo = (Todo) getIntent().getSerializableExtra(Constants.PREFILLED_TODO);
        controller = new CreateTodoController(this,new CreateTodoViewHolder(findViewById(R.id.activity_create_todo)), prefilledTodo);

        String currentDate = getIntent().getStringExtra(Constants.CURRENT_DATE);
        if(currentDate != null){
            controller.prefillStartAndEndDate(currentDate);
        }
        //set actions on date and time pickers
        controller.setListenersToDateAndTimePickers();

        //set colors to priority buttons
        controller.setBackgroundColorsToPriorityBtns();

        //set onclicks to priority buttons
        controller.setListenersToPriorityButtons();

        //set category spinner items
        controller.initCategorySpinner();

        //must be called after initialization of category spinner
        if(prefilledTodo != null){
            controller.fillTodoIntoView(prefilledTodo);
            setTitle(getResources().getString(R.string.edit_todo));
        }
        controller.setFabAction();
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    super.onBackPressed();
                }
                return true;
        }
        finish();
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

package cz.matyapav.todoapp.todo.screen.create;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cz.matyapav.todoapp.R;

public class CreateTodoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get views
        CreateTodoController controller =
                new CreateTodoController(this,new CreateTodoViewHolder(findViewById(R.id.activity_create_todo)));

        //set actions on date and time pickers
        controller.setListenersToDateAndTimePickers();

        //set colors to priority buttons
        controller.setBackgroundColorsToPriorityBtns();

        //set onclicks to priority buttons
        controller.setListenersToPriorityButtons();

        //set category spinner items
        controller.initCategorySpinner();

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    super.onBackPressed();
                }
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }
}

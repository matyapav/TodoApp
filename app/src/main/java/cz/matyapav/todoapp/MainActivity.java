package cz.matyapav.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cz.matyapav.todoapp.settings.SettingsFragment;
import cz.matyapav.todoapp.todo.screen.list.TodoDayFragment;
import cz.matyapav.todoapp.todo.screen.todoall.TodoAllFragment;
import cz.matyapav.todoapp.util.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_todo_today);

        //determine first fragment
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            String firstFragment = null;
            if((firstFragment = getIntent().getStringExtra(Constants.CURRENT_FRAGMENT)) != null){
                if(firstFragment.equals(Constants.SETTING_FRAGMENT_TAG)){
                    fragmentManager.beginTransaction().replace(R.id.mainLayout, SettingsFragment.class.newInstance()).commit();
                }
            }else {
                fragmentManager.beginTransaction().replace(R.id.mainLayout, TodoDayFragment.class.newInstance()).commit();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_announcement_24dp)
                    .setTitle(getResources().getString(R.string.close_app_title))
                    .setMessage(getResources().getString(R.string.close_app_message))
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_todo_today) {
            fragmentClass = TodoDayFragment.class;
        } else if (id == R.id.nav_todo_all) {
            fragmentClass = TodoAllFragment.class;
        } else if (id == R.id.nav_buzer_today) {
            // TODO Show buzer today
        } else if (id == R.id.nav_buzer_list) {
            // TODO Show buzer list
        } else if (id == R.id.nav_ideas) {
            // TODO Show ideas
        } else if (id == R.id.nav_settings) {
            fragmentClass = SettingsFragment.class;
        }else if (id == R.id.nav_help) {
            // TODO Show help
        }else if (id == R.id.nav_about) {
            // TODO Show about
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainLayout, fragment, Constants.SETTING_FRAGMENT_TAG).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

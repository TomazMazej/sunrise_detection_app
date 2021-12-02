package com.mazej.todo_list.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;
import com.mazej.todo_list.R;
import com.mazej.todo_list.fragments.MainFragment;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Menu myMenu;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SharedPreferences sp;
    private ApplicationTodoList app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        // Drawer
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // Load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        myMenu = menu;
        hideButtons();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // Handles toolbar buttons
        hideButtons();

        switch (item.getItemId()) {
            case R.id.delete_list_btn:

                break;
            case R.id.add_list_btn:
                showAddListDialog();
                break;
            case R.id.add_task_btn:
                showAddTaskDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) { // Handles side nav buttons

        hideButtons();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (menuItem.getItemId() == R.id.lists) {
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
            myMenu.findItem(R.id.add_list_btn).setVisible(true);
        }
        if (menuItem.getItemId() == R.id.privacy) {

        }
        if (menuItem.getItemId() == R.id.about) {

        }
        if (menuItem.getItemId() == R.id.settings) {

        }
        fragmentTransaction.commit();
        return true;
    }

    public static void hideButtons() { // Hides all the toolbar buttons
        myMenu.findItem(R.id.general).setVisible(false);
        myMenu.findItem(R.id.other).setVisible(false);
        myMenu.findItem(R.id.delete_list_btn).setVisible(false);
        myMenu.findItem(R.id.add_list_btn).setVisible(true);
        myMenu.findItem(R.id.add_task_btn).setVisible(true);
    }

    void showAddListDialog() {
        final Dialog dialog = new Dialog(this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.add_list_dialog);

        //Initializing the views of the dialog.
        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void showAddTaskDialog() {
        final Dialog dialog = new Dialog(this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.add_task_dialog);

        //Initializing the views of the dialog.
        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button);
        EditText descriptionEt = dialog.findViewById(R.id.description_et);
        DatePicker datePickerD = dialog.findViewById(R.id.date_picker);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                String description = descriptionEt.getText().toString();
                Date date = getDate(datePickerD.getYear(), datePickerD.getMonth(), datePickerD.getDayOfMonth());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
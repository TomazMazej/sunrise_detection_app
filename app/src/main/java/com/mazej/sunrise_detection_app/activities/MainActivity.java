package com.mazej.sunrise_detection_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.mazej.sunrise_detection_app.ApplicationSunrise;
import com.mazej.sunrise_detection_app.R;
import com.mazej.sunrise_detection_app.fragments.AboutFragment;
import com.mazej.sunrise_detection_app.fragments.MainFragment;
import com.mazej.sunrise_detection_app.fragments.PrivacyFragment;
import com.mazej.sunrise_detection_app.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Menu myMenu;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public static ApplicationSunrise app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sunrise Detection");
        setSupportActionBar(toolbar);

        // Drawer
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // Naložimo prvi fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();
    }

    @SuppressLint("StringFormatMatches")
    private void initData() {
        app = (ApplicationSunrise) getApplication();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) { // Gumbi na stranskem meniju
        hideButtons();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (menuItem.getItemId() == R.id.lists) {
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
        }
        if (menuItem.getItemId() == R.id.privacy) {
            fragmentTransaction.replace(R.id.container_fragment, new PrivacyFragment());
        }
        if (menuItem.getItemId() == R.id.about) {
            fragmentTransaction.replace(R.id.container_fragment, new AboutFragment());
        }
        if (menuItem.getItemId() == R.id.settings) {
            fragmentTransaction.replace(R.id.container_fragment, new SettingsFragment());
        }
        fragmentTransaction.commit();
        return true;
    }

    public static void hideButtons() { // Skrijemo gumbe na toolbaru
        myMenu.findItem(R.id.general).setVisible(false);
        myMenu.findItem(R.id.other).setVisible(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
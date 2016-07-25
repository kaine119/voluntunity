package com.percepshunnn.voluntunity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Current screen
    HomeScreenState currentScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating Action button. TODO: Delete if necessary
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //<editor-fold desc="nav drawer boilerplate code">
        // Code necessary for nav drawer. DO NOT DELETE
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //</editor-fold>

        // Begin here.
        // Initialise fragment container
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            MapFragment mapFragment = new MapFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mapFragment)
                    .commit();
            currentScreen = HomeScreenState.MAP;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private NavigationView navView;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        navView = (NavigationView) findViewById(R.id.nav_view);
        Menu navMenu = navView.getMenu();

        if (id == R.id.nav_map) {
            // Show the map filters when it's relevant
            navMenu.findItem(R.id.nav_map_filters).setVisible(true);

            // Fragment change
            if (currentScreen != HomeScreenState.MAP) {
                // Map is not the current screen, so change it!
                currentScreen = HomeScreenState.MAP;
                MapFragment frag = new MapFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, frag).commit();
            }

        } else if (id == R.id.nav_profile) {
            navMenu.findItem(R.id.nav_map_filters).setVisible(false);

            // Fragment change
            if (currentScreen != HomeScreenState.PROFILE) {
                // Profile is not current screen, change it!
                currentScreen = HomeScreenState.PROFILE;
                ProfileFragment frag = new ProfileFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, frag).commit();

            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

// represents which home screen the user is currently on.
enum HomeScreenState {
    MAP,
    PROFILE
}

package com.percepshunnn.voluntunity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Current screen
    HomeScreenState currentScreen;
    TextView mDrawerNameText;
    TextView mDrawerEmailText;
    TextView mDrawerScoreText;
    ImageView mDrawerProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //<editor-fold desc="nav drawer boilerplate code">
        // Code necessary for nav drawer. DO NOT DELETE
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_map);
        //</editor-fold>

        // Begin here.
        // Initialise fragment container
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            MapScreenFragment mapScreenFragment = new MapScreenFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mapScreenFragment)
                    .commit();
            currentScreen = HomeScreenState.MAP;
        }

        View view = navigationView.getHeaderView(0);
        mDrawerNameText = (TextView) view.findViewById(R.id.drawer_username_text);
        mDrawerEmailText = (TextView) view.findViewById(R.id.drawer_email_text);
        mDrawerScoreText = (TextView) view.findViewById(R.id.drawer_score_text);
        mDrawerProfileImage = (ImageView) view.findViewById(R.id.drawer_profile_image);


        FacebookSdk.sdkInitialize(getApplicationContext());

        // Persistency for email on left bar
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        if (sharedPrefs.getString("email", null) != null) {
            // User is signed in
            mDrawerNameText.setText(sharedPrefs.getString("name", null));
            Log.d("MainActivity", "onCreate: saved email: " + sharedPrefs.getString("email", null));
            mDrawerEmailText.setText(sharedPrefs.getString("email", null));
            Picasso.with(this).load(sharedPrefs.getString("picture", null)).resize(100, 100).into(mDrawerProfileImage);
            mDrawerScoreText.setVisibility(View.VISIBLE);
        } else {
            // user is signed out
            mDrawerNameText.setText("Logged out");
            mDrawerEmailText.setText("Please sign in");
            mDrawerScoreText.setVisibility(View.GONE);
            mDrawerProfileImage.setVisibility(View.GONE);
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
                MapScreenFragment frag = new MapScreenFragment();
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
        } else if (id == R.id.nav_leaderboard) {
            navMenu.findItem(R.id.nav_map_filters).setVisible(false);

            // Fragment change
            if (currentScreen != HomeScreenState.LEADERBOARD) {
                // Leaderboard is not current screen, change it!
                currentScreen = HomeScreenState.LEADERBOARD;
                LeaderboardFragment frag = new LeaderboardFragment();
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
    PROFILE,
    LEADERBOARD,
}

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
import android.widget.Toast;

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
    ImageView mDrawerProfileImage;

    // A global var for the current MapScreenFragment
    // For filtering
    MapScreenFragment mMapScreenFragment;

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
            mMapScreenFragment = mapScreenFragment;
        }

        View view = navigationView.getHeaderView(0);
        mDrawerNameText = (TextView) view.findViewById(R.id.drawer_username_text);
        mDrawerEmailText = (TextView) view.findViewById(R.id.drawer_email_text);
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
        } else {
            // user is signed out
            mDrawerNameText.setText("Logged out");
            mDrawerEmailText.setText("Please sign in");
            mDrawerProfileImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentScreen != HomeScreenState.MAP) {
            currentScreen = HomeScreenState.MAP;
            MapScreenFragment frag = new MapScreenFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, frag).commit();
            mMapScreenFragment = frag;
        } else if (mMapScreenFragment.panelIsShown) {
            mMapScreenFragment.hidePanel();
        }

        else {
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



        return super.onOptionsItemSelected(item);
    }

    private NavigationView navView;
    private MenuItem previousItem;

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
                mMapScreenFragment = frag;
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

        } else if (id == R.id.nav_upcoming) {
            navMenu.findItem(R.id.nav_map_filters).setVisible(false);
            setTitle("Upcoming");

            // Fragment change
            if (currentScreen != HomeScreenState.UPCOMING) {
                // Upcoming is not current screen, change it!
                currentScreen = HomeScreenState.UPCOMING;
                UpcomingFragment frag = new UpcomingFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, frag).commit();

            }
        } else {

            switch (id){
                case R.id.nav_filter_community:
                    mMapScreenFragment.filterEventByTag(EventInfo.Tag.COMMUNITY);
                    Toast.makeText(MainActivity.this, "Showing community opportunities", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_filter_children_and_youth:
                    mMapScreenFragment.filterEventByTag(EventInfo.Tag.CHILDREN_AND_YOUTH);
                    Toast.makeText(MainActivity.this, "Showing opportunities to help children and youth", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_filter_education:
                    mMapScreenFragment.filterEventByTag(EventInfo.Tag.EDUCATION);
                    Toast.makeText(MainActivity.this, "Showing opportunities for education", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_filter_elderly:
                    mMapScreenFragment.filterEventByTag(EventInfo.Tag.ELDERLY);
                    Toast.makeText(MainActivity.this, "Showing opportunities for helping the elderly", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_filter_fundraising:
                    mMapScreenFragment.filterEventByTag(EventInfo.Tag.FUNDRAISING);
                    Toast.makeText(MainActivity.this, "Showing fundraising opportunities", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_filter_health:
                    mMapScreenFragment.filterEventByTag(EventInfo.Tag.HEALTH);
                    Toast.makeText(MainActivity.this, "Showing opportunities to help the sick", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_filter_humanitarian:
                    mMapScreenFragment.filterEventByTag(EventInfo.Tag.HUMANITARIAN);
                    Toast.makeText(MainActivity.this, "Showing humanitarian opportunities", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_filter_social_service:
                    mMapScreenFragment.filterEventByTag(EventInfo.Tag.SOCIAL_SERVICE);
                    Toast.makeText(MainActivity.this, "Showing opportunities for social service", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_filter_all:
                    mMapScreenFragment.filterEventByTag(null);
                    Toast.makeText(MainActivity.this, "Showing all opportunities", Toast.LENGTH_SHORT).show();
                    break;
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
    UPCOMING
}

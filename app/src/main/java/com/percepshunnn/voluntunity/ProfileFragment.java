package com.percepshunnn.voluntunity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.FacebookServiceException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.percepshunnn.voluntunity.leaderboardview.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


/**
 * Profile Fragment
 */
public class ProfileFragment extends android.support.v4.app.Fragment {

    LoginButton mLoginButton;
    CallbackManager mCallbackManager;

    // View references
    // Profile page itself
    TextView mNameText;
    TextView mIdText;
    TextView mRepText;
    TextView mSkillsText;
    Button mLogoutButton;
    TextView mHoursText;
    ImageView mProfileImage;
    // Nav drawer
    TextView mDrawerNameText;
    TextView mDrawerEmailText;
    ImageView mDrawerProfileImage;

    // Keeping name persistent requires shared preferences.
    SharedPreferences mSharedPref;

    User mCurrentProfile;

    DatabaseReference usersRef;

    ProfileTracker mProfileTracker;


    private FacebookCallback<LoginResult> mLoginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                mDrawerNameText.setText(Profile.getCurrentProfile().getName());
                                mDrawerEmailText.setText(object.getString("email"));
                                mDrawerProfileImage.setVisibility(View.VISIBLE);

                                // Setting a picture source to a url natively is surprisingly hard.
                                // It's easier to do this with Picasso, a library.
                                String imgUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                Picasso.with(getContext()).load(imgUrl).resize(200, 200).into(mProfileImage);
                                Picasso.with(getContext()).load(imgUrl).resize(100, 100).into(mDrawerProfileImage);

                                // Persistency!
                                SharedPreferences.Editor ed = mSharedPref.edit();
                                ed.putString("email", object.getString("email"));
                                ed.putString("name", Profile.getCurrentProfile().getName());
                                ed.putString("picture", imgUrl);
                                ed.commit();
                            } catch (JSONException e) {
                                Log.d("MainActivity", "onCompleted: something real bad happened");
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "email,picture");
            request.setParameters(parameters);
            request.executeAsync();

            if (Profile.getCurrentProfile() == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile1) {

                        final Profile currentProfile = currentProfile1;
                        displayBasicDetails(currentProfile);
                        // Grab the database for users
                        // If the current user isn't on there, push data on
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        usersRef = database.getReference("users/");
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            String TAG = "ProfileFragment: FirebaseCallback";
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                    User user = userSnap.getValue(User.class);
                                    if (Profile.getCurrentProfile() != null) {
                                        if (Long.parseLong(currentProfile.getId()) == user.getId()) {
                                            Log.d(TAG, "onDataChange: User should not be added");
                                            displayExtraDetails(user);
                                            mProfileTracker.stopTracking();
                                            return;
                                        }
                                    }

                                }

                                // User hasn't been added.
                                Log.d(TAG, "onDataChange: User should be added ");
                                mCurrentProfile = new User(
                                        Profile.getCurrentProfile().getName(),
                                        Arrays.asList("Placeholder1", "Placeholder2"),
                                        Long.parseLong(currentProfile.getCurrentProfile().getId()),
                                        0,
                                        0
                                );
                                usersRef.push().setValue(mCurrentProfile);
                                mProfileTracker.stopTracking();
                                displayExtraDetails(mCurrentProfile);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };
            } else {
                displayBasicDetails(Profile.getCurrentProfile());
                // Grab the database for users
                // If the current user isn't on there, push data on
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                usersRef = database.getReference("users/");
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    String TAG = "ProfileFragment: FirebaseCallback";
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                            User user = userSnap.getValue(User.class);
                            if (Profile.getCurrentProfile() != null) {
                                if (Long.parseLong(Profile.getCurrentProfile().getId()) == user.getId()) {
                                    Log.d(TAG, "onDataChange: User should not be added");
                                    displayExtraDetails(user);
                                    return;
                                }
                            }

                        }

                        // User hasn't been added.
                        Log.d(TAG, "onDataChange: User should be added ");
                        mCurrentProfile = new User(
                                Profile.getCurrentProfile().getName(),
                                Arrays.asList("Placeholder1", "Placeholder2"),
                                Long.parseLong(Profile.getCurrentProfile().getId()),
                                0,
                                0
                        );
                        usersRef.push().setValue(mCurrentProfile);
                        displayExtraDetails(mCurrentProfile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {
            if (error instanceof FacebookServiceException) {
                Toast.makeText(getContext(), "Could not connect to Facebook", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialise facebook sdk
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();



        return inflater.inflate(R.layout.content_profile, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // <editor-fold desc="View References">
        mNameText = (TextView) view.findViewById(R.id.name_text);
        mLogoutButton = (Button) view.findViewById(R.id.logout_button);
        mIdText = (TextView) view.findViewById(R.id.id_text);
        mSkillsText = (TextView) view.findViewById(R.id.skillsText);
        mRepText = (TextView) view.findViewById(R.id.rep_val);
        mHoursText = (TextView) view.findViewById(R.id.hours_text);
        mProfileImage = (ImageView) view.findViewById(R.id.profile_picture);

        mDrawerEmailText = (TextView) getActivity().findViewById(R.id.drawer_email_text);
        mDrawerNameText = (TextView) getActivity().findViewById(R.id.drawer_username_text);
        mDrawerProfileImage = (ImageView) getActivity().findViewById(R.id.drawer_profile_image);

        //</editor-fold>

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users/");

        // This is a mcTesty for Facebook Login
        // TODO: this will probably be changed later
        mLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        mLoginButton.setReadPermissions(Arrays.asList("email", "user_friends"));

        mLoginButton.setFragment(this);

        mLoginButton.registerCallback(mCallbackManager, mLoginCallback);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();

                displayBasicDetails(null);
                displayExtraDetails(null);

                // Remove email from persistent storage
                SharedPreferences.Editor ed = mSharedPref.edit();
                ed.clear();
                ed.commit();
            }
        });

        mSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        if (Profile.getCurrentProfile() != null) {
            displayBasicDetails(Profile.getCurrentProfile());
            usersRef.addListenerForSingleValueEvent(firebaseCallback);
        } else {
            displayBasicDetails(null);
            displayExtraDetails(null);
        }

    }

    private void displayBasicDetails(Profile profile) {
        if (profile != null) {
            mNameText.setText(profile.getName());
            mLogoutButton.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.GONE);

            // Profile Picture
            String imgUrl = mSharedPref.getString("picture", null);
            if (imgUrl != null) {
                Picasso.with(getContext()).load(imgUrl).resize(200, 200).into(mProfileImage);
                mDrawerProfileImage.setVisibility(View.VISIBLE);
            }

            // Set extra detail fields to "loading..."
            mRepText.setText("Loading...");
            mHoursText.setText("Loading...");
            mSkillsText.setText("Loading...");
        }
        else if (profile == null) {
            mNameText.setText("Name");
            mLogoutButton.setVisibility(View.GONE);
            mLoginButton.setVisibility(View.VISIBLE);
            mProfileImage.setImageDrawable(null);
            mDrawerProfileImage.setImageDrawable(null);


            // Show placeholders in drawer
            mDrawerNameText.setText("Logged Out");
            mDrawerEmailText.setText("Please log in");
        }
    }

    private void displayExtraDetails(@Nullable User profile) {
        // for reputation, hours and skills
        if (profile != null) {
            mRepText.setText(Integer.toString(profile.getReputation()));
            mHoursText.setText(Integer.toString(profile.getHours()));

            mSkillsText.setText("");
            for (String current : profile.getSkills()) {
                mSkillsText.append(current + "\n");
            }
        }
        else if (profile == null) {
            mRepText.setText("");
            mHoursText.setText("");
            mSkillsText.setText("");
        }
    }

    private ValueEventListener firebaseCallback = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                User user = userSnap.getValue(User.class);
                if (Long.parseLong(Profile.getCurrentProfile().getId()) == user.getId()) {
                    displayExtraDetails(user);
                    return;
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        usersRef.removeEventListener(firebaseCallback);
    }


}

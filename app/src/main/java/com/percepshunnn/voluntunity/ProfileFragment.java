package com.percepshunnn.voluntunity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


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
    TextView mDrawerScoreText;

    // Keeping name persistent requires shared preferences.
    SharedPreferences mSharedPref;


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
                                mDrawerScoreText.setVisibility(View.VISIBLE);

                                // Setting a picture source to a url natively is surprisingly hard.
                                // It's easier to do this with Picasso, a library.
                                String imgUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                Picasso.with(getContext()).load(imgUrl).resize(200, 200).into(mProfileImage);

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
            displayProfileDetails(Profile.getCurrentProfile());
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

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
        mDrawerScoreText = (TextView) getActivity().findViewById(R.id.drawer_score_text);

        //</editor-fold>

        // This is a mcTesty for Facebook Login
        // TODO: this will probably be changed later
        mLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        mLoginButton.setReadPermissions("email");

        mLoginButton.setFragment(this);

        mLoginButton.registerCallback(mCallbackManager, mLoginCallback);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                displayProfileDetails(Profile.getCurrentProfile());

                // Remove email from persistent storage
                SharedPreferences.Editor ed = mSharedPref.edit();
                ed.clear();
                ed.commit();
            }
        });


        mSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        displayProfileDetails(Profile.getCurrentProfile());

    }

    private void displayProfileDetails(Profile profile) {
        // Takes a profile (Profile.getCurrentProfile()) and puts it on profile page.
        if (profile != null) {
            // logged in, replace login button with logout button
            mLogoutButton.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.GONE);

            // Show user details on profile page
            mNameText.setText(profile.getName());
            mIdText.setText(profile.getId());
            mRepText.setText(R.string.reputation_dummy_text);
            mSkillsText.setText(R.string.skillset_dummy_text);
            mHoursText.setText("36");

            // Profile image
            String imgUrl = mSharedPref.getString("picture", null);
            if (imgUrl != null) {
                Picasso.with(getContext()).load(imgUrl).resize(200, 200).into(mProfileImage);
            }
        }
        else if (profile == null) {
            // logged out, display placeholders
            mNameText.setText("Logged Out");
            mIdText.setText("User id: \n (Purely for testing purposes!)");
            mRepText.setText("");
            mSkillsText.setText("");
            mLogoutButton.setVisibility(View.GONE);
            mLoginButton.setVisibility(View.VISIBLE);
            mHoursText.setText("");
            mProfileImage.setImageDrawable(null);
            // Show placeholders on drawer
            mDrawerNameText.setText("Logged Out");
            mDrawerEmailText.setText("Please log in");
            mDrawerScoreText.setVisibility(View.GONE);
        }
    }

}

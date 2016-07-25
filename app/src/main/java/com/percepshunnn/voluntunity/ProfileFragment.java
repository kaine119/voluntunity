package com.percepshunnn.voluntunity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.w3c.dom.Text;


/**
 * Profile Fragment
 */
public class ProfileFragment extends android.support.v4.app.Fragment {

    LoginButton mLoginButton;
    CallbackManager mCallbackManager;
    TextView mName;
    TextView mIdText;
    TextView mRepText;
    TextView mSkillsText;
    Button mLogoutButton;


    private FacebookCallback<LoginResult> mLoginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken token = loginResult.getAccessToken();
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

        // View references
        mName = (TextView) view.findViewById(R.id.name_text);
        mLogoutButton = (Button) view.findViewById(R.id.logout_button);
        mIdText = (TextView) view.findViewById(R.id.id_text);
        mSkillsText = (TextView) view.findViewById(R.id.skillsText);
        mRepText = (TextView) view.findViewById(R.id.rep_val);

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
            }
        });

        displayProfileDetails(Profile.getCurrentProfile());
    }

    private void displayProfileDetails(Profile profile) {
        // Takes a profile (Profile.getCurrentProfile()) and puts it on profile page.
        if (profile != null) {
            // logged in, display profile details
            mName.setText(profile.getName());
            mIdText.setText(profile.getId());
            mRepText.setText(R.string.reputation_dummy_text);
            mSkillsText.setText(R.string.skillset_dummy_text);
            mLogoutButton.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.GONE);
        }
        else if (profile == null) {
            // logged out, display placeholders
            mName.setText("Logged Out");
            mIdText.setText("User id: \n (Purely for testing purposes!)");
            mRepText.setText("");
            mSkillsText.setText("");
            mLogoutButton.setVisibility(View.GONE);
            mLoginButton.setVisibility(View.VISIBLE);
        }
    }
}

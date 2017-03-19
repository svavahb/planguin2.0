package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.ProfileManager;
import com.example.svava.planguin.R;

public class ProfileActivity extends AppCompatActivity {

    ProfileManager profileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    private static final String EXTRA_PROFILE_BUTTON = "profileButton";

    public static Intent newIntent(Context packageContext, boolean profileButton) {
        Intent i = new Intent(packageContext, ProfileActivity.class);
        i.putExtra(EXTRA_PROFILE_BUTTON, profileButton);
        return i;
    }
}

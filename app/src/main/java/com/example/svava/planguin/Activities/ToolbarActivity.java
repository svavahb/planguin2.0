package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svava.planguin.R;

public class ToolbarActivity extends AppCompatActivity {

    private Button InvitationButton;
    private Button mSettingsButton;
    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ToolbarActivity.this);
        loggedInUser = sharedPreferences.getString("username","");

        if(loggedInUser.isEmpty()){
            Intent i = new Intent(ToolbarActivity.this, WelcomeActivity.class);
            startActivity(i);
        }
    }
}

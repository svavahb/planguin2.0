package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.svava.planguin.R;

public class SettingsActivity extends AppCompatActivity {

    Button button;
    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        loggedInUser = sharedPreferences.getString("username","");

        if(loggedInUser.isEmpty()){
            Intent i = new Intent(SettingsActivity.this, WelcomeActivity.class);
            startActivity(i);
        }

        button = (Button) findViewById(R.id.log_out_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;

                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("username");
                editor.apply();

                i = new Intent(SettingsActivity.this, WelcomeActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
    }

    public void onClick(View v) {

        Intent i;
        int id = v.getId();

        switch(id){
            case R.id.compare_button:
                i = new Intent(this, CompareActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.friendlist_button:
                i = new Intent(this, FriendListActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.grouplist_button:
                i = new Intent(this, GroupListActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.find_friends_button:
                i = new Intent(this, SearchActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.schedule_button:
                i = new Intent(this, ScheduleActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.profile_button:
                i = new Intent(this, ProfileActivity.class);
                i.putExtra("USER_CLICKED", loggedInUser);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            /*case R.id.log_out_button:
                i = new Intent(this, WelcomeActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;*/
            default:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }
    }
}

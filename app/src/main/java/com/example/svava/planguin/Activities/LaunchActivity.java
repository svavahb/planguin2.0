package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.R;

public class LaunchActivity extends AppCompatActivity {

    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LaunchActivity.this);
        loggedInUser = sharedPreferences.getString("username","");

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    System.out.println("sleeping...");
                    sleep(4000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    System.out.println("wake up!");
                    if(loggedInUser.isEmpty()){
                        Intent i = new Intent(LaunchActivity.this, WelcomeActivity.class);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(LaunchActivity.this, ScheduleActivity.class);
                        startActivity(i);
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loggedInUser.isEmpty()){
            Intent i = new Intent(LaunchActivity.this, WelcomeActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(LaunchActivity.this, ScheduleActivity.class);
            startActivity(i);
        }
    }
}

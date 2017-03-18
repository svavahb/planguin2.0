package com.example.svava.planguin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.ProfileManager;
import com.example.svava.planguin.R;

public class FriendListActivity extends AppCompatActivity {

    ProfileManager profileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
    }
}

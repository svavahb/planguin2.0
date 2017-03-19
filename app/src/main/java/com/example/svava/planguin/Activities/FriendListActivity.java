package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
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

    private static final String EXTRA_FRIENDLIST_BUTTON = "friendlistButton";

    public static Intent newIntent(Context packageContext, boolean friendslistButton){
        Intent i = new Intent(packageContext, FriendListActivity.class);
        i.putExtra(EXTRA_FRIENDLIST_BUTTON, friendslistButton);
        return i;
    }
}

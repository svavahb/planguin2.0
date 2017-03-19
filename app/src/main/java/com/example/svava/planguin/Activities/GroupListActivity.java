package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.GroupManager;
import com.example.svava.planguin.R;

public class GroupListActivity extends AppCompatActivity {

    GroupManager groupManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
    }

    private static final String EXTRA_GROUPLIST_BUTTON = "grouplistButton";

    public static Intent newIntent(Context packageContext, boolean grouplistButton){
        Intent i = new Intent(packageContext, GroupListActivity.class);
        i.putExtra(EXTRA_GROUPLIST_BUTTON, grouplistButton);
        return i;
    }
}

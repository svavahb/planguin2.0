package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
            case R.id.schedule_button:
                i = new Intent(this, ScheduleActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.settings_button:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.invitation_button:
                i = new Intent(this, InvitationActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            default:
                i = new Intent(this, GroupListActivity.class);
                startActivity(i);
                break;
        }
    }
}

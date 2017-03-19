package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.svava.planguin.Entities.Group;
import com.example.svava.planguin.Entities.Schedule;
import com.example.svava.planguin.Managers.ScheduleManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Activities.ToolbarActivity;

public class ScheduleActivity extends ToolbarActivity {

    ScheduleManager scheduleManager;

    private Button InvitationButton;
    private Button ScheduleButton;
    private Button CompareButton;
    private Button FriendListButton;
    private Button GroupListButton;
    private Button ProfileButton;

    boolean invitationButton;
    boolean scheduleButton;
    boolean compareButton;
    boolean friendlistButton;
    boolean groupListButton;
    boolean profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        InvitationButton = (Button) findViewById(R.id.invitation_button);
        InvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ScheduleActivity.this, "Button Clicked!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ScheduleActivity.this, InvitationActivity.class);

                i.putExtra("activity_invitation_button", invitationButton);
                startActivity(i);
            }
        });

        ScheduleButton = (Button) findViewById(R.id.schedule_button);
        ScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, ScheduleActivity.class);

                i.putExtra("activity_schedule_button", scheduleButton);
                startActivity(i);
            }
        });

        CompareButton = (Button) findViewById(R.id.compare_button);
        CompareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, CompareActivity.class);

                i.putExtra("activity_compare_button", compareButton);
                startActivity(i);
            }
        });

        FriendListButton = (Button) findViewById(R.id.friendlist_button);
        FriendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, FriendListActivity.class);

                i.putExtra("activity_friendlist_button", friendlistButton);
                startActivity(i);
            }
        });

        GroupListButton = (Button) findViewById(R.id.grouplist_button);
        GroupListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, GroupListActivity.class);

                i.putExtra("activity_grouplist_button", groupListButton);
                startActivity(i);
            }
        });

        ProfileButton = (Button) findViewById(R.id.profile_button);
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, ProfileActivity.class);

                i.putExtra("activity_profile_button", profileButton);
                startActivity(i);
            }
        });
    }
    private static final String EXTRA_SCHEDULE_BUTTON = "scheduleButton";

    public static Intent newIntent(Context packageContext, boolean scheduleButton) {
        Intent i = new Intent(packageContext, ScheduleActivity.class);
        i.putExtra(EXTRA_SCHEDULE_BUTTON, scheduleButton);
        return i;
    }
}

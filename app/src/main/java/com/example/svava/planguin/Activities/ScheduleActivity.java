package com.example.svava.planguin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.svava.planguin.Managers.ScheduleManager;
import com.example.svava.planguin.R;

public class ScheduleActivity extends AppCompatActivity {

    ScheduleManager scheduleManager;

    private Button InvitationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        InvitationButton = (Button) findViewById(R.id.invitation_button);
        InvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ScheduleActivity.this, "Button Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

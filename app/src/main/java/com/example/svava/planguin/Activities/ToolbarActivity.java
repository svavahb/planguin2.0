package com.example.svava.planguin.Activities;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);

        /*InvitationButton = (Button) findViewById(R.id.invitation_button);
        InvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ToolbarActivity.this, "Button Clicked!", Toast.LENGTH_SHORT).show();
            }
        });*/

        mSettingsButton = (Button) findViewById(R.id.settings_button);
    }
}

package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.svava.planguin.Managers.UserManager;
import com.example.svava.planguin.R;

public class WelcomeActivity extends AppCompatActivity {

    UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void onClick(View v) {

        Intent i;
        int id = v.getId();

        switch(id){
            case R.id.sign_up_button:
                i = new Intent(this, SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.log_in_button:
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
        }
    }
}

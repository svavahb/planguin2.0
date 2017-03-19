package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.InvitationManager;
import com.example.svava.planguin.R;

public class InvitationActivity extends AppCompatActivity {

    InvitationManager invitationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
    }

    private static final String EXTRA_INVITAION_BUTTON = "invitationButton";

    public static Intent newIntent(Context packageContext, boolean invitationButton){
        Intent i = new Intent(packageContext, InvitationActivity.class);
        i.putExtra(EXTRA_INVITAION_BUTTON, invitationButton);
        return i;
    }

}

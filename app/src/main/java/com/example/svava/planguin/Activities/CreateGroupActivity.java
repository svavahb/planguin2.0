package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;



public class CreateGroupActivity extends AppCompatActivity {

    private EditText GroupNameInput;
    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        GroupNameInput = (EditText) findViewById(R.id.namegroup);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CreateGroupActivity.this);
        loggedInUser = sharedPreferences.getString("username","");

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
            case R.id.schedule_button:
                i = new Intent(this, ScheduleActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.grouplist_button:
                i = new Intent(this, GroupListActivity.class);
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
            case R.id.created_group_button:
                onCreateGroup(GroupNameInput.getText().toString());
                break;
            default:
                i = new Intent(this, GroupListActivity.class);
                startActivity(i);
                break;
        }
    }

    public void onCreateGroup(final String groupname){
        PlanguinRestClient.get("createGroup/"+loggedInUser+"/"+groupname,new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statuscode, Header[] headers, JSONObject json){
                String success = json.optString("username");
                if (success.equals("true")) {
                    Intent i = new Intent(CreateGroupActivity.this, GroupPageActivity.class);
                    System.out.println(groupname);
                    i.putExtra("GROUP_CLICKED",groupname);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                System.out.println(e+" "+errorResponse);
            }
        });
    }
}

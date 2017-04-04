package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.svava.planguin.Managers.ProfileManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ProfileActivity extends AppCompatActivity {

    ProfileManager profileManager;
    String loggedInUser;
    int userToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        loggedInUser = sharedPreferences.getString("username","");


        userToAdd = getIntent().getIntExtra("USER_CLICKED", -1);
        TextView textView = (TextView) findViewById(R.id.Username);
    }
    public void addFriend(View v) {
        Log.d("addFriend", "/addFriend/"+loggedInUser+"/"+userToAdd);
        PlanguinRestClient.post("/addFriend/"+loggedInUser+"/"+userToAdd, new StringEntity("", "UTF-8"), "application/json;charset=UTF-8",  new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString){
                // String success = jsonfriends.optString("username");
                Log.d("addFriend","Success!");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                System.out.println(statusCode+" "+e);
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("addedFriend", "success!");
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                System.out.println(statusCode+" "+e);
            }
        });

    }


    private static final String EXTRA_PROFILE_BUTTON = "profileButton";

    public static Intent newIntent(Context packageContext, boolean profileButton) {
        Intent i = new Intent(packageContext, ProfileActivity.class);
        i.putExtra(EXTRA_PROFILE_BUTTON, profileButton);
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
            case R.id.grouplist_button:
                i = new Intent(this, GroupListActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            default:
                i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                break;
        }
    }
}

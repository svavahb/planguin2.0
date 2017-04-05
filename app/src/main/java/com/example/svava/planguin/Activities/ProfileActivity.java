package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.svava.planguin.Entities.User;
import com.example.svava.planguin.Managers.ProfileManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.JSONparser;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.R.attr.button;
import static android.R.attr.visibility;

public class ProfileActivity extends AppCompatActivity {
    String loggedInUser;
    User userToAdd;
    JSONparser jsonParser = new JSONparser();
    Button button1;
    Button button2;
    String usernameToAdd;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        loggedInUser = sharedPreferences.getString("username","");



        usernameToAdd = getIntent().getStringExtra("USER_CLICKED");
        TextView textView = (TextView) findViewById(R.id.Username);
        button1 = (Button) findViewById(R.id.addFriendButton);
        button2 = (Button) findViewById(R.id.removeFriendButton);
        textView.setText(usernameToAdd);

        PlanguinRestClient.get("/search/"+loggedInUser+"/"+usernameToAdd, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray users) {
                JSONObject jsonuser;
                boolean friendship;
                try {
                    // parse the user holder into a user object
                    jsonuser = users.getJSONObject(0);
                    friendship = jsonuser.getBoolean("friendship");

                    userToAdd = jsonParser.parseUser(jsonuser);
                    if(usernameToAdd.equals(loggedInUser)) {
                        button1.setVisibility(View.GONE);
                    }

                    if(friendship) {
                        button2.setVisibility(View.VISIBLE);
                    } else {
                        button2.setVisibility(View.GONE);
                    }

                    TextView textViewSchool = (TextView) findViewById(R.id.textViewSchool);
                    if(userToAdd.getSchool()!= "null") {
                        textViewSchool.setVisibility(View.VISIBLE);
                        textViewSchool.setText(userToAdd.getSchool());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                System.out.println("Virkar ekki :( "+statusCode+" "+e);
            }
        } );

    }
    public void addFriend(View v) {
        PlanguinRestClient.post("/addFriend/"+loggedInUser+"/"+userToAdd.getUserId(), new StringEntity("", "UTF-8"), "application/json;charset=UTF-8",  new JsonHttpResponseHandler() {

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
                button2.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                System.out.println(statusCode+" "+e);
            }
        });

    }

    public void deleteFriend(View v) {
        PlanguinRestClient.post("/deleteFriendship/"+loggedInUser+"/"+userToAdd.getUsername(), new StringEntity("", "UTF-8"), "application/json;charset=UTF-8",  new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString){
                // String success = jsonfriends.optString("username");
                Log.d("deleteFriend","Success!");

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                System.out.println(statusCode+" "+e);
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("deletedFriend", "success!");
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.GONE);
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

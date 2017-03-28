package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.svava.planguin.Managers.ProfileManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FriendListActivity extends AppCompatActivity {

    ProfileManager profileManager;

    //String[] users = new String[]{"Halldóra", "Þórunn", "Svava", "Þórdís"};
    List<String> friends = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PlanguinRestClient.get("getFriends/svava", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonfriends){
                for(int i=0; i<jsonfriends.length(); i++) {
                    String friend = jsonfriends.optString(i);
                    friends.add(friend);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                System.out.println(statusCode+" "+e);
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, friends);

        ListView listView = (ListView) findViewById(R.id.friendlist_list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(FriendListActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    private static final String EXTRA_FRIENDLIST_BUTTON = "friendlistButton";

    public static Intent newIntent(Context packageContext, boolean friendslistButton){
        Intent i = new Intent(packageContext, FriendListActivity.class);
        i.putExtra(EXTRA_FRIENDLIST_BUTTON, friendslistButton);
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
            case R.id.find_friends_button:
                i = new Intent(this, SearchActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);
                break;
            default:
                i = new Intent(this, FriendListActivity.class);
                startActivity(i);
                break;
        }
    }
}

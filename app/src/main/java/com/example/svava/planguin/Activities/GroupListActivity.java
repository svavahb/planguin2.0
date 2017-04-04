package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.svava.planguin.Entities.Group;
import com.example.svava.planguin.Managers.GroupManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.JSONparser;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GroupListActivity extends AppCompatActivity {

    GroupManager groupManager;
    JSONparser jsonparser = new JSONparser();

    //String[] groups = new String[]{"Húbbar","Többar"};
    List<String> groups = new ArrayList<>();
    ArrayAdapter adapter;
    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        // get the logged in user's name
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GroupListActivity.this);
        loggedInUser = sharedPreferences.getString("username","");

        if(loggedInUser.isEmpty()){
            Intent i = new Intent(GroupListActivity.this, WelcomeActivity.class);
            startActivity(i);
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, groups);

        final ListView listView = (ListView) findViewById(R.id.grouplist_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(GroupListActivity.this, GroupPageActivity.class);
                intent.putExtra("GROUP_CLICKED", listView.getItemAtPosition(position).toString());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        PlanguinRestClient.get("getGroups/"+loggedInUser, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsongroups){
                groups.clear();
                for(int i=0; i<jsongroups.length(); i++) {
                    Group group = jsonparser.parseGroup(jsongroups.optJSONObject(i));
                    groups.add(group.getGrpName());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                System.out.println(statusCode+" "+e);
            }
        });

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
            case R.id.friendlist_button:
                i = new Intent(this, FriendListActivity.class);
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
            case R.id.create_group_button:
                i = new Intent(this, CreateGroupActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);
                break;
            default:
                i = new Intent(this, GroupListActivity.class);
                startActivity(i);
                break;
        }
    }
}

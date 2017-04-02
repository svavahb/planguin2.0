package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;

import com.example.svava.planguin.Managers.ProfileManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AddFriendsToGroupActivity extends AppCompatActivity{

    ProfileManager profileManager;
    Button button;
    ListView listView;

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
        setContentView(R.layout.activity_add_friends_to_group);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, friends);

        listView = (ListView) findViewById(R.id.friendlist_checklist);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setAdapter(adapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(AddFriendsToGroupActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });*/
        button = (Button) findViewById(R.id.addtogroup_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++){
                    int position = checked.keyAt(i);
                    if (checked.valueAt(i))
                        selectedItems.add(adapter.getItem(position));
                }

                String[] outputStrArr = new String[selectedItems.size()];

                for (int i = 0; i < selectedItems.size(); i++){
                    outputStrArr[i] = selectedItems.get(i);
                }

                Intent i = new Intent(getApplicationContext(), GroupPageActivity.class);

                Bundle b = new Bundle();
                b.putStringArray("selectedItems", outputStrArr);

                i.putExtras(b);

                startActivity(i);
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
            /*case R.id.addtogroup_button:
                i = new Intent(this, GroupPageActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);
                break;*/
            default:
                i = new Intent(this, FriendListActivity.class);
                startActivity(i);
                break;
        }
    }
}

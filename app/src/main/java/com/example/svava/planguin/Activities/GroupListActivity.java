package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.svava.planguin.Managers.GroupManager;
import com.example.svava.planguin.R;

public class GroupListActivity extends AppCompatActivity {

    GroupManager groupManager;

    String[] groups = new String[]{"Húbbar","Többar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, groups);

        ListView listView = (ListView) findViewById(R.id.grouplist_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(GroupListActivity.this, GroupPageActivity.class);
                startActivity(intent);
                //overridePendingTransition(0, 0);
            }
        });

    }

    private static final String EXTRA_GROUPLIST_BUTTON = "grouplistButton";

    public static Intent newIntent(Context packageContext, boolean grouplistButton){
        Intent i = new Intent(packageContext, GroupListActivity.class);
        i.putExtra(EXTRA_GROUPLIST_BUTTON, grouplistButton);
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
            default:
                i = new Intent(this, GroupListActivity.class);
                startActivity(i);
                break;
        }
    }
}

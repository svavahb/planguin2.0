package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.svava.planguin.Entities.User;
import com.example.svava.planguin.Managers.SearchManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.JSONparser;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    SearchManager searchManager;
    JSONparser jsonParser;
    private Button SearchButton;
    private String searchString;
    private EditText SearchInput;
    private boolean searchButton;

    private List<String> resultList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchButton = (Button) findViewById(R.id.search_button);
        SearchInput = (EditText) findViewById(R.id.editText_search);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = SearchInput.getText().toString();
                search(searchString);
            }
        });

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resultList);

        ListView listView = (ListView) findViewById(R.id.search_list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    protected void search(String searchString) {
        // Call to server get method /search.
        // "svava" will be replaced by the logged in user's username
        PlanguinRestClient.get("search/svava/"+searchString, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray users) {
                JSONObject jsonuser;
                boolean friendship;
                try {
                    // parse the user holder into a user object
                    jsonuser = users.getJSONObject(0);
                    friendship = jsonuser.getBoolean("friendship");
                    User user = jsonParser.parseUser(jsonuser);

                    //Add the results from the http response
                    resultList.clear();
                    resultList.add(user.getUsername());

                    // Notify the adapter so the UI is updated
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                System.out.println("Virkar ekki :( "+statusCode+" "+e);
            }
        });
    }

    public void onClick(View v) {

        Intent i;
        int id = v.getId();

        switch (id) {
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
            default:
                i = new Intent(this, FriendListActivity.class);
                startActivity(i);
                break;
        }
    }
}

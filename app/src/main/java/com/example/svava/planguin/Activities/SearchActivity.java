package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private Button SearchButton;
    private String searchString;
    private EditText SearchInput;
    private TextView SearchResults;

    private List<String> resultList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
        loggedInUser = sharedPreferences.getString("username","");

        if(loggedInUser.isEmpty()){
            Intent i = new Intent(SearchActivity.this, WelcomeActivity.class);
            startActivity(i);
        }

        SearchButton = (Button) findViewById(R.id.search_button);
        SearchInput = (EditText) findViewById(R.id.editText_search);
        SearchResults = (TextView) findViewById(R.id.search_results_text);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = SearchInput.getText().toString();
                search(searchString);
            }
        });

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, resultList);

        final ListView listView = (ListView) findViewById(R.id.search_list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                intent.putExtra("USER_CLICKED", listView.getItemAtPosition(position).toString());
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
                    resultList.clear();
                    // parse the user holder into a user object
                    for (int i=0; i<users.length(); i++) {
                        JSONObject jsonuserholder = users.getJSONObject(i);
                        jsonuser = jsonuserholder.getJSONObject("user");
                        friendship = jsonuserholder.getBoolean("friendship");
                        //User user = jsonParser.parseUser(jsonuser);
                        if (jsonuser.optString("username").equals("null")) {
                            SearchResults.setText(new StringBuilder().append("No user with that username found. Try again"));
                        }
                        else if (users.length()>1) {
                            SearchResults.setText(new StringBuilder("Showing list of all users"));
                            resultList.add(jsonuser.optString("username"));
                        }
                        else {
                            SearchResults.setText(new StringBuilder().append("One user found"));
                            resultList.add(jsonuser.optString("username"));
                        }
                    }

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
            default:
                i = new Intent(this, FriendListActivity.class);
                startActivity(i);
                break;
        }
    }
}

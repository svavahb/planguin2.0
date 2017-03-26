package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.svava.planguin.Entities.User;
import com.example.svava.planguin.Managers.SearchManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    SearchManager searchManager;
    private Button SearchButton;
    private String searchString;
    private EditText SearchInput;
    private boolean searchButton;

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
                    User user = searchManager.parseJSONUser(jsonuser);
                    Toast.makeText(SearchActivity.this, user.getUsername()+" "+friendship, Toast.LENGTH_SHORT).show();
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
}

package com.example.svava.planguin.Managers;

import android.preference.PreferenceActivity;

import com.example.svava.planguin.Entities.User;
import java.util.ArrayList;
import java.util.List;
import com.loopj.android.http.*;

import org.json.*;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Svava on 14.03.17.
 */

public class SearchManager {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static List<User> search(String searchString) throws JSONException {

        client.get("localhost:8080/search/svava/"+searchString, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject user) {
                    System.out.println(user);
                }
        });
        return new ArrayList<User>();
    }
}

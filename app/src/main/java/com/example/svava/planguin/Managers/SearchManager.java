package com.example.svava.planguin.Managers;

import android.preference.PreferenceActivity;
import android.widget.Toast;

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

    public static String search(String searchString) throws JSONException {

        final String result[] = new String[1];

        client.get("https://planguinserver.herokuapp.com/search/svava/svava", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray user) {
                    result[0] = user.toString();
                }
        });
        return result[0];
    }
}

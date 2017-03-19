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
    public static String result;

    public static String search(String searchString) throws JSONException {

        client.get("https://planguinserver.herokuapp.com/search/svava/"+searchString, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray user) {
                    result = user.toString();
                }
        });
        return result;
    }
}

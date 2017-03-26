package com.example.svava.planguin.Managers;

import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.example.svava.planguin.Entities.User;
import java.util.ArrayList;
import java.util.List;

import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.*;

import org.json.*;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Svava on 14.03.17.
 */

public class SearchManager {

    public static String result;

    public static User parseJSONUser(JSONObject json) throws JSONException {
        User user = new User();
        JSONObject userjson = json.getJSONObject("user");
        user.setUserId(userjson.optInt("userId"));
        user.setUsername(userjson.optString("username"));
        user.setPassword(userjson.optString("password"));
        user.setPhoto(userjson.optString("photo"));
        user.setSchool(userjson.optString("school"));

        return user;
    }
}

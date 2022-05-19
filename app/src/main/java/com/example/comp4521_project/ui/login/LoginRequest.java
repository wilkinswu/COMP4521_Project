package com.example.comp4521_project.ui.login;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

public class LoginRequest extends JsonObjectRequest {
    LoginActivity loginAct;
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static String loginURL = "http://125.59.138.87:8090/api/basic/login";
    public LoginRequest(LoginActivity loginActivity, JSONObject obj, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, loginURL, obj, listener, errorListener);
        loginAct = loginActivity;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Map<String, String> responseHeaders = response.headers;
        if (responseHeaders != null && responseHeaders.containsKey(SET_COOKIE_KEY)) {
            String cookie = responseHeaders.get(SET_COOKIE_KEY);
            if (!TextUtils.isEmpty(cookie)) {
                // 将cookie存到本地，如Sharedpreference (/data/data/com.example.comp4521_project/shared_prefs/myshare.xml)
                Log.i("headers", cookie);
                SharedPreferences share = loginAct.getAppSharedPreferences();
                SharedPreferences.Editor edt = share.edit();
                edt.putString("Cookie", cookie);
                edt.apply();
            }
        }
        return super.parseNetworkResponse(response);
    }
}
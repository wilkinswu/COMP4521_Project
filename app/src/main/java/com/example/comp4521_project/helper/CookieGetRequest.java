package com.example.comp4521_project.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp4521_project.data.model.ProfileModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class CookieGetRequest {

    private final MutableLiveData<String> output ;

    public CookieGetRequest(Context activity, final String url) {
          output = new MutableLiveData<>();

        RequestQueue queue = Volley.newRequestQueue(activity);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        output.setValue(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                output.setValue("That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();

                SharedPreferences sp = activity.getSharedPreferences("myshare", Context.MODE_PRIVATE);
                String contentInSharePref = sp.getString("Cookie", "default value");
                Log.i("Cookie", contentInSharePref);
                localHashMap.put("Cookie", contentInSharePref);

                return localHashMap;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
//        return output;
    }

    public LiveData<String> getText() {
        return output;
    }
}

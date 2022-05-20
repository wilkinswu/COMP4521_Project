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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CookieOtherRequest {

    private final MutableLiveData<String> output;

    public CookieOtherRequest(Context activity, final String url, Map<String, String> params, int method) {
        output = new MutableLiveData<>();

        RequestQueue queue = Volley.newRequestQueue(activity);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(method, url,
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

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                return params;
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

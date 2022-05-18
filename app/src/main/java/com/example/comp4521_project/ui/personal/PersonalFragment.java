package com.example.comp4521_project.ui.personal;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp4521_project.databinding.FragmentPersonalBinding;
import com.example.comp4521_project.ui.login.LoginActivity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PersonalFragment extends Fragment {

    private FragmentPersonalBinding binding;
    private PersonalActivity personalActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PersonalViewModel personalViewModel =
                new ViewModelProvider(this).get(PersonalViewModel.class);

        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPersonal;
        final String GET_URL = "http://125.59.138.87:8090/api/basic/getProfile";

//        CookieStore cookieStore = new MyCookieStore();
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: " + response.substring(0, 500));
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap localHashMap = new HashMap();
                Log.i("cookie", getSettingNote("Cookie"));

                localHashMap.put("Cookie is ", getSettingNote("Cookie"));

                return localHashMap;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        personalViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String getSettingNote(String s){
        SharedPreferences read = personalActivity.getAppSharedPreferences();
        return  read.getString(s, "");

    }

}
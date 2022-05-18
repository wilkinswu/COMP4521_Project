package com.example.comp4521_project.ui.personal;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.google.gson.Gson;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class PersonalFragment extends Fragment {

    private FragmentPersonalBinding binding;
    private PersonalActivity personalActivity = new PersonalActivity();

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
                        // Display the payload.
                        ResponseProfile rspJson = new Gson().fromJson(response, ResponseProfile.class);
                        textView.setText(new Gson().toJson(rspJson.payload));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();

                Context context = getActivity();
                SharedPreferences sp = context.getSharedPreferences( "myshare", Context.MODE_PRIVATE);
                String contentInSharePref = sp.getString("Cookie", "default value");
                Log.i("Cookie", contentInSharePref);
                localHashMap.put("Cookie", contentInSharePref);

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


    public class Payload {
        int uid;
        String username;
        String nickname;
        int account_level;
        int account_status;
        String date_modified;
        String date_creation;
        String date_last_login;

    }

    public class ResponseProfile {
        String message;
        Payload payload;

    }

}
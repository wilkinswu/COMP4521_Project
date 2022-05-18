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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp4521_project.data.model.ProfileModel;
import com.example.comp4521_project.databinding.FragmentPersonalBinding;

import com.example.comp4521_project.helper.CookieRequest;
import com.google.gson.Gson;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class PersonalFragment extends Fragment {

    private FragmentPersonalBinding binding;
//    private PersonalActivity personalActivity = new PersonalActivity();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PersonalViewModel personalViewModel = new ViewModelProvider(this ).get(PersonalViewModel.class);

        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPersonal;


//        final String GET_URL = "http://125.59.138.87:8090/api/basic/getProfile";
//
//        CookieRequest cookieRequest = new CookieRequest(getActivity(), GET_URL);
//
//        cookieRequest.getText().observe(getViewLifecycleOwner(), textView::setText);

        personalViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
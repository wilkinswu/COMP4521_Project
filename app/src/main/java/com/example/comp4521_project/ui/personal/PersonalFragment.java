package com.example.comp4521_project.ui.personal;

import static android.content.Context.MODE_PRIVATE;

import static com.example.comp4521_project.helper.TimeInterval.getInterval;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class PersonalFragment extends Fragment {

    private FragmentPersonalBinding binding;
//    private PersonalActivity personalActivity = new PersonalActivity();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PersonalViewModel personalViewModel = new ViewModelProvider(this).get(PersonalViewModel.class);

        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView tvNickname = binding.tvNickname;
        final TextView tvUsername = binding.tvUsername;
        final TextView tvAge = binding.tvAge;
//        final TextView tvBlognumber = binding.tvBlognumber;
        final TextView tvLastlogin = binding.tvLastlogin;
        final TextView tvLevel = binding.tvLevel;
        final TextView tvStatus = binding.tvStatus;

        Gson gson = new Gson();

        final String GET_URL = "http://125.59.138.87:8090/api/basic/getProfile";

        CookieRequest cookieRequest = new CookieRequest(getActivity(), GET_URL);

        cookieRequest.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.isEmpty())
                    return;
                tvStatus.setText(s);
                ProfileModel.Response profileModel = gson.fromJson(s, ProfileModel.Response.class);
                Log.i("Get_Profile", profileModel.message);


                tvNickname.setText(profileModel.payload.nickname);
                tvUsername.setText(profileModel.payload.username);
                String age = "";
                String sincelastlogin = "";

                try {
                    age = getInterval( profileModel.payload.date_creation);
                    sincelastlogin= getInterval( profileModel.payload.date_last_login);
                    tvAge.setText(age);
                    tvLastlogin.setText(sincelastlogin);


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String level = "";
                String status = "";
                switch (profileModel.payload.account_level) {
                    case 0:
                        level = "NotRegistered";
                        break;
                    case 1:
                        level = "Normal";
                        break;
                    case 2:
                        level = "Goverment";
                        break;
                    case 3:
                        level = "Expert";
                        break;
                    default:
                        level = "Error";
                }
                switch (profileModel.payload.account_level) {
                    case 0:
                        status = "NotRegistered";
                        break;
                    case 1:
                        status = "Registered";
                        break;
                    case 2:
                        status = "Disabled";
                        break;
                    default:
                        status = "Error";
                }

                tvLevel.setText(level);
                tvStatus.setText(status);

            }
        });

        personalViewModel.getText().observe(getViewLifecycleOwner(), tvNickname::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
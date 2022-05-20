package com.example.comp4521_project.ui.personal;

import static com.example.comp4521_project.helper.TimeInterval.getInterval;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp4521_project.RegisterActivity;
import com.example.comp4521_project.data.model.ProfileModel;
import com.example.comp4521_project.databinding.FragmentPersonalBinding;

import com.example.comp4521_project.helper.CookieGetRequest;

import com.example.comp4521_project.ui.login.LoginActivity;
import com.example.comp4521_project.ui.setting.SettingActivity;
import com.google.gson.Gson;

import java.text.ParseException;

public class PersonalFragment extends Fragment {

    private FragmentPersonalBinding binding;
//    private PersonalActivity personalActivity = new PersonalActivity();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PersonalViewModel personalViewModel = new ViewModelProvider(this).get(PersonalViewModel.class);

        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final Button logout = binding.logout;
        final Button setting = binding.setting;
        final TextView tvNickname = binding.tvNickname;

        // personal view does nothing at all.
        personalViewModel.getText().observe(getViewLifecycleOwner(), tvNickname::setText);

        // get profile
        refresh();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new intent to open settings activity.
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        ScrollView scrollView = binding.ScrollView01;
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                if (scrollView.getScrollY() == 0) {
                    Log.i("Scroll", "TOP");
                    refresh();
                }

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void refresh(){


        final TextView tvUsername = binding.tvUsername;
        final TextView tvAge = binding.tvAge;
        final TextView tvLastlogin = binding.tvLastlogin;
        final TextView tvLevel = binding.tvLevel;
        final TextView tvStatus = binding.tvStatus;
        final TextView tvNickname = binding.tvNickname;

        Gson gson = new Gson();

        final String GET_URL = "http://125.59.138.87:8090/api/basic/getProfile";

        CookieGetRequest cookieRequest = new CookieGetRequest(getActivity(), GET_URL);

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
                    age = getInterval(profileModel.payload.date_creation);
                    sincelastlogin = getInterval(profileModel.payload.date_last_login);
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
                switch (profileModel.payload.account_status) {
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
    }
}
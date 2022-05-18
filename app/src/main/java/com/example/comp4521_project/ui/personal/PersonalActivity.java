package com.example.comp4521_project.ui.personal;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp4521_project.databinding.ActivityLoginBinding;
import com.example.comp4521_project.databinding.FragmentPersonalBinding;
import com.example.comp4521_project.ui.login.LoginViewModel;

public class PersonalActivity  extends AppCompatActivity {

    private PersonalViewModel personalViewModel;
    private FragmentPersonalBinding binding;
    public SharedPreferences getAppSharedPreferences() {
        return getSharedPreferences("myshare", MODE_PRIVATE);
    }
}

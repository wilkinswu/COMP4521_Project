package com.example.comp4521_project.ui.setting;

import static com.example.comp4521_project.helper.TimeInterval.getInterval;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.example.comp4521_project.R;
import com.example.comp4521_project.data.model.ProfileModel;
import com.example.comp4521_project.databinding.FragmentPersonalBinding;
import com.example.comp4521_project.helper.CookieGetRequest;
import com.example.comp4521_project.helper.CookieOtherRequest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        // below line is used to check if
        // frame layout is empty or not.
        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // below line is to inflate our fragment.
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.idFrameLayout, new SettingFragment())
                    .commit();
        }



    }


}

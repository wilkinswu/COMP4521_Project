package com.example.comp4521_project.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.example.comp4521_project.R;
import com.example.comp4521_project.helper.CookieOtherRequest;

import java.util.HashMap;
import java.util.Map;

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());


        final String UPDATE_OTHERS_URL = "http://125.59.138.87:8090/api/basic/updateProfile";
        final String UPDATE_PASSWORD_URL = "http://125.59.138.87:8090/api/basic/updatePassword";


        // Instance field for listener
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        //clear inputs in prefs

                        Map<String, String> params = new HashMap<String, String>();
                        // Your Implementation
                        String url = UPDATE_OTHERS_URL;
                        switch (key) {
                            case "update_username":
                                final String username = prefs.getString("update_username", null);
                                Log.i("updateU", username);
                                params.put("username", username);

                                break;
                            case "update_nickname":
                                final String nickname = prefs.getString("update_nickname", null);
                                Log.i("updateU", nickname);
                                params.put("nickname", nickname);

                                break;
                            case "update_account_level":
                                final String level = prefs.getString("update_account_level", null);
                                Log.i("updateL", level);
                                params.put("account_level", level);


                                break;

                            case "update_password":
                                url = UPDATE_PASSWORD_URL;
                                final String password = prefs.getString("update_password", null);
                                Log.i("updateP", password);
                                params.put("new_cipher", password);

                                break;


                        }


                        if ( params.isEmpty()) {
                            Log.i("update", "input cannot be empty");
                            return;
                        }
                        if(getActivity() == null)
                        {
                            return;
                        }
                        CookieOtherRequest cookieRequest =
                                new CookieOtherRequest(getActivity(),
                                        url,
                                        params,
                                        Request.Method.PUT);

                        cookieRequest.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String rsp) {
                                if (rsp.isEmpty())
                                    return;
                                Log.i("update", rsp);

                            }
                        });

                    }
                };

        prefs.registerOnSharedPreferenceChangeListener(listener);


    }

}

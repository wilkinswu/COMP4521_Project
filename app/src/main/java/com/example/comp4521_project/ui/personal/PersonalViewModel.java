package com.example.comp4521_project.ui.personal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp4521_project.databinding.FragmentPersonalBinding;

import java.io.ByteArrayOutputStream;

public class PersonalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private FragmentPersonalBinding binding;

    public PersonalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("will put personal info/setting here");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
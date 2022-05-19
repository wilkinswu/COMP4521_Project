package com.example.comp4521_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp4521_project.databinding.ActivityRegisterBinding;
import com.example.comp4521_project.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText usernameEditText = findViewById(R.id.regUsername);
        final EditText passwordEditText = findViewById(R.id.regPassword);
        final EditText nicknameEditText = findViewById(R.id.regNickname);
        final Button registerButton = findViewById(R.id.btn_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String nickname = nicknameEditText.getText().toString();
                final int level = 1;
                Log.i("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", "a");
                JSONObject obj = new JSONObject();
                try {
                    obj.put("username", username);
                    obj.put("pw_cipher", password);
                    obj.put("nickname", nickname);
                    obj.put("account_level", level);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseString = response.getString("message");

                            if (responseString.equals("Register Success!")) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Log.i("success", "yeahhhhhhhhhhhhhhhhhhhhhhh!");
                            }
                            else {
                                // Actually not reachable...
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                Log.i("AlertDialog", "Showing");
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body = null;
                        JSONObject jsonbody = null;
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                            jsonbody = new JSONObject(body);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            // exception
                        }
                        Log.i("Error Message", body);
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        Log.i("AlertDialog", "Showing");
                        try {
                            builder.setMessage("Register Failed: " + statusCode + "\n" + jsonbody.getString("message"))
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                String url = "http://125.59.138.87:8090/api/basic/register";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, responseListener, errorListener);
                queue.start();
                queue.add(jsonObjectRequest);
            }
        });
    }
}

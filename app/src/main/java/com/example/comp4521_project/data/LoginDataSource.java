package com.example.comp4521_project.data;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp4521_project.MainActivity;
import com.example.comp4521_project.RegisterActivity;
import com.example.comp4521_project.data.model.LoggedInUser;
import com.example.comp4521_project.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    boolean success = false;
    private static final Object waitObject=new Object();
    public Result<LoggedInUser> login(LoginActivity loginActivity, String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            JSONObject loginUser = new JSONObject();
            loginUser.put("username", username);
            loginUser.put("pw_cipher", password);
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String responseString = response.getString("message");
                        if (responseString.equals("Login Success")) {
                            Log.i("success", "yeahhhhhhhhhhhhhhhhhhhhhhh!");
                            success = true;
                            // Todo: Temporary solution
                            Toast.makeText(loginActivity, "Welcome!" + username, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginActivity, MainActivity.class);
                            loginActivity.startActivity(intent);
                        }
                        else {
                            // Actually not reachable...
                            AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
                            builder.setMessage("Login Failed: " + responseString)
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                        //synchronized (waitObject) {waitObject.notifyAll();}
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
                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        jsonbody = new JSONObject(body);
                    } catch (UnsupportedEncodingException | JSONException e) {
                        // exception
                    }
                    Log.i("Error Message", body);
                    AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
                    try {
                        builder.setMessage("Login Failed:" + statusCode + "\n" + jsonbody.getString("message"))
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            //synchronized (waitObject) {
                RequestQueue queue = Volley.newRequestQueue(loginActivity);
                String url = "http://125.59.138.87:8090/api/basic/login";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, loginUser, responseListener, errorListener);
                queue.start();
                queue.add(jsonObjectRequest);


            // Todo: needs to wait for HTTP response to record login user, now is asynchronized.
                //waitObject.wait();
                LoggedInUser user = new LoggedInUser(java.util.UUID.randomUUID().toString(), username);
                Log.i("LoginDataSource", String.valueOf(success));
                return (success) ? new Result.Success<>(user) : new Result.Error(new Exception());
            //}


        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

}

//interface VolleyCallBack {
//    void onSuccess();
//}
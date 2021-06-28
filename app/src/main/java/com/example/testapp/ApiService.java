package com.example.testapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.transition.MaterialSharedAxis;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiService {
    private static final String TAG = "ApiService";
    Context context;

    public ApiService(Context context) {
        this.context = context;
    }

    public void saveUser(JSONObject jsonObject, UserSaved userSaved) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.11:8080/7learn/SaveUser.php", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean saved = response.getBoolean("success");
                    userSaved.saved(saved);
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Toast.makeText(context, "خطا در برقراری با سرور", Toast.LENGTH_LONG).show();

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);

    }

    public void loginUser(JSONObject jsonObject, LoginComplete loginComplete) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.11:8080/7learn/SelectUser.php", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success=response.getBoolean("lodgedSuccessfully");
                    loginComplete.login(success);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                loginComplete.login(false);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(18000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public interface UserSaved {
        void saved(boolean success);
    }

    public interface LoginComplete {
        void login(boolean loginSuccessfully);
    }
}

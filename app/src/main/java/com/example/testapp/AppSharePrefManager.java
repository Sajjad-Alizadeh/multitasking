package com.example.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppSharePrefManager {
    private static final String TAG = "AppSharePrefManager";
    Context context;
    private static final String APP_SHARED_PREF_MANAGER = "app_shared_pref";
    private static final String KEY_STATE_LOGIN = "loginState";
    private SharedPreferences sharedPreferences;

    public AppSharePrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_SHARED_PREF_MANAGER, Context.MODE_PRIVATE);
    }

    public void save(boolean state) {
        Log.i(TAG, "save: "+state);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_STATE_LOGIN, state);
        editor.apply();
    }

    public boolean getState() {
        boolean state = sharedPreferences.getBoolean(KEY_STATE_LOGIN, false);
        Log.i(TAG, "getState: "+state);
        return state;

    }
}

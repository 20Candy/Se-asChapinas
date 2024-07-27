package com.example.screens.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "AppPreferences";
    private static final String IS_LOGGED = "isLogged";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public boolean isLogged() {
        return prefs.getBoolean(IS_LOGGED, false);
    }

    public void setLogged(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED, isLoggedIn);
        editor.apply();
    }
}

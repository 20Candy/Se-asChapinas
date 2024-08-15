package com.example.screens.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "AppPreferences";
    private static final String IS_LOGGED = "isLogged";
    private static final String FIRST_LOGIN = "firstLogin";
    private static final String SHOW_CHALLENGE = "showChallenge";
    private static final String OPEN_CAMERA = "openCamera";

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

    public boolean isFirstLogin() {
        return prefs.getBoolean(FIRST_LOGIN, true);
    }

    public void setFirstLogin(boolean isFirstLogin) {
        editor.putBoolean(FIRST_LOGIN, isFirstLogin);
        editor.apply();
    }

    public boolean isChallengeShow() {
        return prefs.getBoolean(SHOW_CHALLENGE, true);
    }

    public void setChallengeShow(boolean isShown) {
        editor.putBoolean(SHOW_CHALLENGE, isShown);
        editor.apply();
    }

    public boolean isOpenCamera() {
        return prefs.getBoolean(OPEN_CAMERA, true);
    }

    public void setOpenCamera(boolean isOpen) {
        editor.putBoolean(OPEN_CAMERA, isOpen);
        editor.apply();
    }
}

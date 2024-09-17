package com.screens.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "AppPreferences";
    private static final String FIRST_LOGIN = "firstLogin";
    private static final String SHOW_CHALLENGE = "showChallenge";
    private static final String OPEN_CAMERA = "openCamera";
    private static final String CHALLENGE_DATE = "challengeDate";
    private static final String ID_USUARIO = "id_user";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
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

    public String getLastChallengeShowDate() {
        return prefs.getString(CHALLENGE_DATE, "");
    }

    public void setLastChallengeShowDate(String date) {
        editor.putString(CHALLENGE_DATE, date);
        editor.apply();
    }

    public String getIdUsuario() {
        return prefs.getString(ID_USUARIO, "");
    }

    public void setIdUsuario(String idUsuario) {
        editor.putString(ID_USUARIO, idUsuario);
        editor.apply();
    }
}

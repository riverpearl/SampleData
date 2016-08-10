package com.tacademy.sampledata;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Tacademy on 2016-08-10.
 */
public class SharedPreferenceManager {

    private static SharedPreferenceManager instance;

    public static SharedPreferenceManager getInstance() {
        if (instance == null)
            instance = new SharedPreferenceManager();

        return instance;
    }

    SharedPreferences sPrefs;
    SharedPreferences.Editor sEditor;

    private SharedPreferenceManager() {
        Context context = MyApplication.getContext();
        sPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sEditor = sPrefs.edit();
    }

    private final static String KEY_EMAIL = "email";
    private final static String KEY_PASSWORD = "password";

    public void setEmail(String email) {
        sEditor.putString(KEY_EMAIL, email);
        sEditor.commit();
    }

    public String getEmail() {
        return sPrefs.getString(KEY_EMAIL, "");
    }

    public void setPassword(String password) {
        sEditor.putString(KEY_PASSWORD, password);
        sEditor.commit();
    }

    public String getPassword() {
        return sPrefs.getString(KEY_PASSWORD, "");
    }
}

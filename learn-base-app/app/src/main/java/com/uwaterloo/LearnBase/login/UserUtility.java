package com.uwaterloo.LearnBase.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public final class UserUtility {

    private static final String USERNAME_KEY = "username";


    public static void setUsername(Context context, String username) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME_KEY, username);
        editor.apply();
    }


    public static String getUsername(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString(USERNAME_KEY, null);
        return username;
    }


    public static boolean isUserSignedIn(Context context) {
        boolean isUserSignedIn = false;
        return isUserSignedIn;
    }
}

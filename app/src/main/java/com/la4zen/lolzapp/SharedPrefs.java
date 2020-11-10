package com.la4zen.lolzapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefs {

    private static SharedPreferences mSharedPref;

    public static void init(Context context) {
        if (mSharedPref == null)
            mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setCookie(String value) {
        mSharedPref.edit().putString("cookie", value).apply();
    }

    public static String getCookie() {
        return mSharedPref.getString("cookie", null);
    }

}
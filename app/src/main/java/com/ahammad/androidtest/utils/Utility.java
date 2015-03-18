package com.ahammad.androidtest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ahammad.androidtest.R;

/**
 * Created by Ala Hammad on 3/17/15.
 */
public class Utility {

    public static String getReminderTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_reminder_key),"0");
    }

    public static boolean isAllowedReminder (Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isAllow =prefs.getBoolean(context.getString(R.string.pref_allow_reminder_key),true);
        Log.v("hammad","isAllow : "+isAllow);
        return isAllow;
    }
}

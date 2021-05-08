package de.htwg.co2footprint_tracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import de.htwg.co2footprint_tracker.R;

public class PreferenceManagerHelper {

    public static void setStartTime(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(context.getString(R.string.saved_start_time), System.currentTimeMillis());
        editor.apply();
    }

    public static long getStartTime(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getLong(context.getString(R.string.saved_start_time), 0L);
    }

    public static void clearStoredStartTime(Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(context.getString(R.string.saved_start_time), System.currentTimeMillis());
        editor.apply();
    }


}

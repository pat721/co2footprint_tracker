package de.htwg.co2footprint_tracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.utils.Constants;

public class PreferenceManagerHelper {

    public static void setStartTime(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(Constants.PERSISTENCY.PREFERENCE_STARTING_TIME_KEY, System.currentTimeMillis());
        editor.apply();
    }

    public static long getStartTime(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getLong(Constants.PERSISTENCY.PREFERENCE_STARTING_TIME_KEY, 0L);
    }

    public static void clearStoredStartTime(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(Constants.PERSISTENCY.PREFERENCE_STARTING_TIME_KEY);
        editor.apply();
    }


}

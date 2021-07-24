package de.htwg.co2footprint_tracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import androidx.preference.PreferenceManager;

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

    public static void setDeviceType(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int independentPixelDensity = (int) (displayMetrics.widthPixels / displayMetrics.density);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        if (independentPixelDensity >= 600) {
            editor.putInt(Constants.PERSISTENCY.DEVICE_IS_TABLET, 1);
        } else {
            editor.putInt(Constants.PERSISTENCY.DEVICE_IS_TABLET, 0);
        }

        editor.apply();
    }

    public static int getDeviceType(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(Constants.PERSISTENCY.DEVICE_IS_TABLET, -1);
    }

    public static void setAdminArea(Context context, String adminArea) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Constants.PERSISTENCY.ADMINISTRATION_AREA, adminArea);
        editor.apply();
    }

    public static String getAdminArea(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(Constants.PERSISTENCY.ADMINISTRATION_AREA, "");
    }

    public static void setServiceState(Context context, String serviceState) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Constants.PERSISTENCY.SERVICE_STARTED, serviceState);
        editor.apply();
    }

    public static String getServiceState(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(Constants.PERSISTENCY.SERVICE_STARTED, "");
    }
}

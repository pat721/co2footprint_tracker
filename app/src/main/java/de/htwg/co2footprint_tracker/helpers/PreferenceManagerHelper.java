package de.htwg.co2footprint_tracker.helpers;

import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import androidx.preference.PreferenceManager;

public class PreferenceManagerHelper {

    public static void setStartTime(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(PREFERENCE_STARTING_TIME_KEY, System.currentTimeMillis());
        editor.apply();
    }

    public static long getStartTime(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getLong(PREFERENCE_STARTING_TIME_KEY, NO_START_TIME_SET);
    }

    public static void clearStoredStartTime(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(PREFERENCE_STARTING_TIME_KEY);
        editor.apply();
    }

    public static void setDeviceType(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int independentPixelDensity = (int) (displayMetrics.widthPixels / displayMetrics.density);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        if (independentPixelDensity >= 600) {
            editor.putInt(DEVICE_IS_TABLET, DEVICE_TYPE_TABLET);
        } else {
            editor.putInt(DEVICE_IS_TABLET, DEVICE_TYPE_NOT_TABLET);
        }

        editor.apply();
    }

    public static int getDeviceType(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(DEVICE_IS_TABLET, DEVICE_TYPE_NOT_SET);
    }

    public static void setAdminArea(Context context, String adminArea) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(ADMINISTRATION_AREA, adminArea);
        editor.apply();
    }

    public static String getAdminArea(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(ADMINISTRATION_AREA, "");
    }

    public static void setCountryISOCode(Context context, String country) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(COUNTRY_ISO_CODE, country);
        editor.apply();
    }

    public static String getCountryISOCode(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(COUNTRY_ISO_CODE, "");
    }

    public static void setServiceState(Context context, String serviceState) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(SERVICE_STARTED, serviceState);
        editor.apply();
    }

    public static String getServiceState(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(SERVICE_STARTED, "");
    }

    public static void setTodayTotalToggleState(Context context, int state) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(TODAY_TOTAL_TOGGLE_STATE, state);
        editor.apply();
    }

    public static int getTodayTotalToggleState(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(TODAY_TOTAL_TOGGLE_STATE, TOGGLE_STATE_NOT_SET);
    }


}

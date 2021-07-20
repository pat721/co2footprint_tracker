package de.htwg.co2footprint_tracker.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.htwg.co2footprint_tracker.utils.Constants;

public class LocationHelper {

    FusedLocationProviderClient fusedLocationProviderClient;

    private final Activity activity;

    //TODO memory leak?
    private static LocationHelper locationHelper;

    private LocationHelper(Activity activity) {
        this.activity = activity;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public static LocationHelper getInstance(Activity activity) {
        if (locationHelper == null) {
            locationHelper = new LocationHelper(activity);
        }
        return locationHelper;
    }


    public void updateCurrentAdminArea() {
        Log.e(Constants.LOG.TAG, "Getting Location");

        //duplicated code because android needs to check the permisstion each time it does use the permission
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(Constants.LOG.TAG, "Location Permission error");
            
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERMISSION.LOCATION_REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {

                        Geocoder geocoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );


                        PreferenceManagerHelper.setAdminArea(activity.getApplicationContext(), addresses.get(0).getAdminArea());
                        Log.e(Constants.LOG.TAG, "Area: \"" + addresses.get(0).getAdminArea() + "\" was set to storage");


                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(Constants.LOG.TAG, "exception!");
                    }

                }
            }
        });
    }


}






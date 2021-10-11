package de.htwg.co2footprint_tracker.helpers;

import static de.htwg.co2footprint_tracker.utils.Constants.PERMISSION.RC_LOCATION;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.htwg.co2footprint_tracker.MainActivity;
import de.htwg.co2footprint_tracker.utils.Constants;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LocationHelper extends Activity {

    FusedLocationProviderClient fusedLocationProviderClient;

    private final MainActivity activity = MainActivity.getWeakInstanceActivity();

    private static LocationHelper locationHelper;

    private LocationHelper() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public static LocationHelper getInstance() {
        if (locationHelper == null) {
            locationHelper = new LocationHelper();
        }
        return locationHelper;
    }


    //TODO: async to have less load on main thread?
    @AfterPermissionGranted(RC_LOCATION)
    public void updateCurrentAdminArea() {
        Log.e(Constants.LOG.TAG, "Getting Location");

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO rationale string
            EasyPermissions.requestPermissions(activity, "TODO: getString(R.string.rationale_ask)", RC_LOCATION, perms);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                try {

                    Geocoder geocoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1
                    );

                    PreferenceManagerHelper.setAdminArea(activity.getApplicationContext(), addresses.get(0).getAdminArea());
                    PreferenceManagerHelper.setCountryISOCode(activity.getApplicationContext(), addresses.get(0).getCountryCode());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(Constants.LOG.TAG, "Exception in location helper:" + e.getMessage());
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this, activity);
    }

}






package de.htwg.co2footprint_tracker.helpers;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import de.htwg.co2footprint_tracker.MainActivity;
import de.htwg.co2footprint_tracker.utils.Constants;

public class PermissionHelper {


    private final Activity activity;

    public PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    public void processPermissionHandling() {
        if (!permissionsGranted()) {
            requestPermissions();
        }
    }

    private boolean permissionsGranted() {
        return hasNetworkHistoryPermission() && hasPhoneStatsPermission() && hasLocationPermission();
    }

    private void requestPermissions() {
        if (!hasNetworkHistoryPermission()) {
            //nop
        } else if (!hasPhoneStatsPermission()) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, Constants.PERMISSION.REQUEST_CODE);
        } else if (hasLocationPermission()) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERMISSION.LOCATION_REQUEST_CODE);
        }
    }

    private boolean hasPhoneStatsPermission() {
        return !(ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED);
    }

    public boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasNetworkHistoryPermission() {
        final AppOpsManager appOps = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), activity.getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        Toast.makeText(activity, "Usage Stats permission is required", Toast.LENGTH_LONG).show();
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS, activity.getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                android.os.Process.myUid(), activity.getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
                        Intent intent = new Intent(activity, MainActivity.class);
                        if (activity.getIntent().getExtras() != null) {
                            intent.putExtras(activity.getIntent().getExtras());
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                });
        requestReadNetworkHistoryAccess();
        return false;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestReadNetworkHistoryAccess() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        activity.startActivity(intent);
    }
}

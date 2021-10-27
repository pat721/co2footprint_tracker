package de.htwg.co2footprint_tracker.helpers;

import static de.htwg.co2footprint_tracker.utils.Constants.PERMISSION.RC_LOCATION_READ_PHONE_STATE_ACCESS_NETWORK_STATE;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import de.htwg.co2footprint_tracker.MainActivity;
import de.htwg.co2footprint_tracker.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionHelper extends Activity {


    private static PermissionHelper permissionHelper;
    private final MainActivity activity = MainActivity.getWeakInstanceActivity();

    public static PermissionHelper getInstance() {
        if (permissionHelper == null) {
            permissionHelper = new PermissionHelper();
        }
        return permissionHelper;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this, activity);
    }

    @AfterPermissionGranted(RC_LOCATION_READ_PHONE_STATE_ACCESS_NETWORK_STATE)
    public void processPermissionHandling() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE};

        if (!hasNetworkHistoryReadingPermission()) {
            requestReadNetworkHistoryAccess();
        }

        if (!EasyPermissions.hasPermissions(activity, perms)) {

            //TODO rationale string
            EasyPermissions.requestPermissions(activity,
                    "TODO: getString(R.string.rationale_ask)",
                    RC_LOCATION_READ_PHONE_STATE_ACCESS_NETWORK_STATE
                    , perms);
        }
    }


    private boolean hasNetworkHistoryReadingPermission() {
        final AppOpsManager appOps = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), activity.getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        Toast.makeText(activity, R.string.usage_stats_permission_required, Toast.LENGTH_LONG).show();

        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS, activity.getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.Q)
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

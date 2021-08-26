package de.htwg.co2footprint_tracker.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import de.htwg.co2footprint_tracker.model.InitialBucketContainer;
import de.htwg.co2footprint_tracker.services.NetworkStatsExecutorService;
import de.htwg.co2footprint_tracker.utils.Constants;

public class TrackingHelper {

    private static TrackingHelper trackingHelper;


    public static TrackingHelper getInstance() {
        if (trackingHelper == null) {
            trackingHelper = new TrackingHelper();
        }
        return trackingHelper;
    }


    public void stopTracking(Context context) {
        foregroundServiceAction(Constants.ACTION.STOP_SERVICE, context);
        PreferenceManagerHelper.clearStoredStartTime(context);
        InitialBucketContainer.setNewRun(true);
        InitialBucketContainer.clearMappedPackageData();
    }

    public void startTracking(Context context) {
        PreferenceManagerHelper.setStartTime(context);
        Toast.makeText(context, "Tracking started!", Toast.LENGTH_LONG).show();
        foregroundServiceAction(Constants.ACTION.START_SERVICE, context);
    }

    private void foregroundServiceAction(String action, Context context) {

        if (PreferenceManagerHelper.getServiceState(context).equals(Constants.PERSISTENCY.SERVICE_STOPPED)
                && action.equals(Constants.ACTION.STOP_SERVICE)) {
            return;
        }

        Intent foregroundServiceIntent = new Intent(context, NetworkStatsExecutorService.class);
        foregroundServiceIntent.setAction(action);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(foregroundServiceIntent);
        } else {
            context.startService(foregroundServiceIntent);
        }
        Log.e(Constants.LOG.TAG, "service started with action: '" + action + "'");
    }


}

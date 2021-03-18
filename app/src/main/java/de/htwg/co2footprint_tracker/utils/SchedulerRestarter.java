package de.htwg.co2footprint_tracker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

import de.htwg.co2footprint_tracker.model.Package;

public class SchedulerRestarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Log.e(Constants.LOG.TAG, "restarting service...");


        if (intent.getAction().equals(Constants.ACTION.RESTART_SCHEDULER_SERVICE)) {
            Intent updateSchedulerIntent = new Intent(context, UpdateServiceSchedulerService.class);
            ArrayList<Package> packageList = intent.getParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST);
            updateSchedulerIntent.putExtra(Constants.PARAMS.PACKAGE_LIST, packageList);
            updateSchedulerIntent.setAction(Constants.ACTION.UPDATE_SERVICE_SCHEDULER_STARTED);
            Log.e(Constants.LOG.TAG, "intent created, starting service...");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(updateSchedulerIntent);
            } else {
                context.startService(updateSchedulerIntent);
            }

        }
    }
}

package de.htwg.co2footprint_tracker.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import de.htwg.co2footprint_tracker.utils.Constants;

// TODO: Raphi -> mit SystemService lösen
public class KillAppEventHandleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(Constants.LOG.TAG, "restarting service...");
        Toast.makeText(context, "restarting service!", Toast.LENGTH_LONG).show();

//        if (intent.getAction().equals(Constants.ACTION.RESTART_SCHEDULER_SERVICE)) {
//            Intent updateSchedulerIntent = new Intent(context, UpdateServiceSchedulerService.class);
//            ArrayList<Package> packageList = intent.getParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST);
//            updateSchedulerIntent.putExtra(Constants.PARAMS.PACKAGE_LIST, packageList);
//            updateSchedulerIntent.setAction(Constants.ACTION.UPDATE_SERVICE_SCHEDULER_STARTED);
//            Log.e(Constants.LOG.TAG, "intent created, starting service...");
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(updateSchedulerIntent);
//            } else {
//                context.startService(updateSchedulerIntent);
//            }
//
//        }
    }
}

package de.htwg.co2footprint_tracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.htwg.co2footprint_tracker.model.Package;
import de.htwg.co2footprint_tracker.utils.Constants;

import static de.htwg.co2footprint_tracker.helpers.PackageHelper.getPackagesData;

public class UpdateServiceSchedulerService extends Service implements Runnable {

    private Context context;
    private ArrayList<Package> packageList;
    private boolean testIsRunning;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(Constants.LOG.TAG, "UpdateServiceSchedulerService: onStartCommand");
        context = getApplicationContext();
        packageList = getPackagesData(this);
        testIsRunning = true;
        new Thread(this).start();
        Log.e(Constants.LOG.TAG, "thread was started, onStartCommand finished");

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        testIsRunning = false;
        Log.e(Constants.LOG.TAG, "Scheduling service was destroyed!");
        super.onDestroy();
    }

    @Override
    public void run() {
        Log.e(Constants.LOG.TAG, "starting scheduling...");
        while (testIsRunning) {
            updateStats();

/*            Log.e(Constants.LOG.TAG, "Updating stats in 1 minute seconds");
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/

            for (int i = 10; i >= 0; i--) {
                Log.e(Constants.LOG.TAG, "Updating stats in " + i + " seconds");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(Constants.LOG.TAG, "Scheduling finished!");
    }

    private void updateStats() {
        Log.e(Constants.LOG.TAG, "updating stats...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent updateStatsIntent = new Intent(context, NetworkStatsUpdateService.class);
            updateStatsIntent.putParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST, packageList);
            updateStatsIntent.putExtra(Constants.PARAMS.SAVE_STATS_TO_FILE, false);
            updateStatsIntent.setAction(Constants.ACTION.ACTION_UPDATE_STATS);
            context.startService(updateStatsIntent);
            Log.e(Constants.LOG.TAG, "...done updating stats");
        }
    }

}

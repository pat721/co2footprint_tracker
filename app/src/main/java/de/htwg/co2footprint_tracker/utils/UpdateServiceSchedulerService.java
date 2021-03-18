package de.htwg.co2footprint_tracker.utils;

import android.app.IntentService;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.htwg.co2footprint_tracker.NetworkStatsUpdateService;
import de.htwg.co2footprint_tracker.model.Package;

public class UpdateServiceSchedulerService extends Service implements Runnable {

    private Context context;
    private ArrayList<Package> packageList;
    private boolean testIsRunning;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.UPDATE_SERVICE_SCHEDULER_STARTED)) {
            Log.e(Constants.LOG.TAG, "onStartCommand with UPDATE_SERVICE_SCHEDULER_STARTED inten");
            context = getApplicationContext();
            packageList = intent.getParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST);
            testIsRunning = true;
            Log.e(Constants.LOG.TAG, "starting thread");
            new Thread(this).start();
            Log.e(Constants.LOG.TAG, "thread was started, startcommand finished");

            return START_STICKY;
        }

        return super.onStartCommand(intent, flags, startId);
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
        super.onDestroy();
    }

    @Override
    public void run() {
        Log.e(Constants.LOG.TAG, "inside thread!");
        while (testIsRunning) {
            updateStats();

//            try {
//                TimeUnit.MINUTES.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            for (int i = 10; i >= 0; i--) {
                Log.e(Constants.LOG.TAG, "Updating UI in " + i + " seconds");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(Constants.LOG.TAG, "Thread finished!");
    }

    private void updateStats() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent updateStatsIntent = new Intent(context, NetworkStatsUpdateService.class);
            updateStatsIntent.putParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST, packageList);
            updateStatsIntent.putExtra(Constants.PARAMS.SAVE_STATS_TO_FILE, false);
            updateStatsIntent.setAction(Constants.ACTION.ACTION_UPDATE_STATS);
            context.startService(updateStatsIntent);
        }
    }
}

package de.htwg.co2footprint_tracker.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.htwg.co2footprint_tracker.NetworkStatsUpdateService;
import de.htwg.co2footprint_tracker.model.Package;

public class UpdateServiceSchedulerThread extends Thread {

    private Context context;
    private ArrayList<Package> packageList;

    //TODO make sure this also runs when app is closed
    public UpdateServiceSchedulerThread(Context context, ArrayList<Package> packageList) {
        this.context = context;
        this.packageList = packageList;
    }

    @Override
    public void run() {

        //TODO: make it work when app is closed (service of some sort?)
        while (TimingHelper.getIsTestRunning(context)) {
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

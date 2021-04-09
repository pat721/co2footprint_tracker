package de.htwg.co2footprint_tracker.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.htwg.co2footprint_tracker.NetworkStatsUpdateService;
import de.htwg.co2footprint_tracker.model.Package;

public class UpdateServiceSchedulerService extends Service implements Runnable {

    private Context context;
    private ArrayList<Package> packageList;
    private boolean testIsRunning;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            Log.e(Constants.LOG.TAG, "UpdateServiceSchedulerService: onStartCommand");
            context = getApplicationContext();
            packageList = getPackagesData();
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

//            try {
//                TimeUnit.MINUTES.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

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

    private ArrayList<Package> getPackagesData() {
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        packageList = new ArrayList<>(packageInfoList.size());
        for (PackageInfo packageInfo : packageInfoList) {
            Package packageItem = new Package();
            packageItem.setVersion(packageInfo.versionName);
            packageItem.setPackageName(packageInfo.packageName);
            packageItem.setPackageUid(getPackageUid(getApplicationContext(), packageInfo.packageName));
            packageList.add(packageItem);
            ApplicationInfo ai = null;
            try {
                ai = packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (ai == null) {
                continue;
            }
            CharSequence appName = packageManager.getApplicationLabel(ai);
            if (appName != null) {
                packageItem.setName(appName.toString());
            }
        }
        //  Go through the package list and set whether the entries have duplicate uids
        for (int i = 0; i < packageList.size(); i++) {
            if (packageList.get(i).getDuplicateUids())
                continue;
            int packageUid = packageList.get(i).getPackageUid();
            for (int j = 0; j < packageList.size(); j++) {
                if (i == j)
                    continue;
                else {
                    if (packageUid == packageList.get(j).getPackageUid()) {
                        packageList.get(i).setDuplicateUids(true);
                        packageList.get(j).setDuplicateUids(true);
                    }
                }
            }
        }


        Set<Integer> usedDuplicatedIds = new HashSet<>();
        ArrayList<Package> packageListForReturn = new ArrayList<>();
        //rm duplicate uid-packages and return List without duplicate uids
        for (Package packet : packageList) {
            if (packet.getDuplicateUids() && usedDuplicatedIds.contains(packet.getPackageUid())) {
                continue;
            }
            if(packet.getDuplicateUids()){
                packet.setName("SystemApp" + packet.getPackageUid());
                packet.setPackageName("internal.uid." + packet.getPackageUid());
            }
            usedDuplicatedIds.add(packet.getPackageUid());
            packageListForReturn.add(packet);
        }
        return packageListForReturn;
    }

    private static int getPackageUid(Context context, String packageName) {
        /*
        uid
        The kernel user-ID that has been assigned to this application;
        currently this is not a unique ID (multiple applications can have the same uid).
         */
        PackageManager packageManager = context.getPackageManager();
        int uid = -1;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            uid = packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return uid;
    }




}

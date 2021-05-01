package de.htwg.co2footprint_tracker.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.htwg.co2footprint_tracker.model.Package;

public class PackageHelper {

    public static ArrayList<Package> getPackagesData(Context context) {

        ArrayList<Package> packageList;
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        packageList = new ArrayList<>(packageInfoList.size());
        for (PackageInfo packageInfo : packageInfoList) {
            Package packageItem = new Package();
            packageItem.setVersion(packageInfo.versionName);
            packageItem.setPackageName(packageInfo.packageName);
            packageItem.setPackageUid(getPackageUid(context, packageInfo.packageName));
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
            if (packet.getDuplicateUids()) {
                packet.setName("SystemApp" + packet.getPackageUid());
                packet.setPackageName("internal.uid." + packet.getPackageUid());
            }
            usedDuplicatedIds.add(packet.getPackageUid());
            packageListForReturn.add(packet);
        }
        return packageListForReturn;
    }

    public static int getPackageUid(Context context, String packageName) {
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

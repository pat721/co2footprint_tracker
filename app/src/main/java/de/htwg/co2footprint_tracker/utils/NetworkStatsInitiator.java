package de.htwg.co2footprint_tracker.utils;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import de.htwg.co2footprint_tracker.model.InitialBucketContainer;

public class NetworkStatsInitiator extends IntentService {

    public NetworkStatsInitiator() {
        super("NetworkStatsInitiator");
    }

    @SuppressLint("MissingPermission")
    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getSubscriberId();
            }
        }
        return "";
    }

    public void fillMapForNewRun(Context context) {


        long startTime = TimingHelper.getStartTime(context);

        if (InitialBucketContainer.isNewRun()) {
            InitialBucketContainer.setNewRun(false);
        }


        //NetworkStats networkStatsWifi = null;
        //NetworkStats networkStatsMobile = null;
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(context.NETWORK_STATS_SERVICE);
        //NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);

        try {
            NetworkStats networkStatsWifi = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, "", startTime, System.currentTimeMillis());
            do {
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                networkStatsWifi.getNextBucket(bucket);

                int bucketUid = bucket.getUid();
                //long rxBytesWifi = bucket.getRxBytes();
                InitialBucketContainer.putInitialReceivedWifiData(bucketUid, bucket.getRxBytes());

                //long txBytesWifi = bucket.getTxBytes();
                InitialBucketContainer.putInitialTransmittedWifiData(bucketUid, bucket.getTxBytes());

                //long rxPacketsWifi = bucket.getRxPackets();
                InitialBucketContainer.putInitialReceivedWifiPacket(bucketUid, bucket.getRxPackets());

                //long txPacketsWifi = bucket.getTxPackets();
                InitialBucketContainer.putInitialTransmittedWifiPacket(bucketUid, bucket.getTxPackets());

            } while (networkStatsWifi.hasNextBucket());
        } catch (RemoteException e) {
            Log.e(Constants.LOG.TAG, "Remote Exception: " + e.getMessage());
        }
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //nop
    }
}

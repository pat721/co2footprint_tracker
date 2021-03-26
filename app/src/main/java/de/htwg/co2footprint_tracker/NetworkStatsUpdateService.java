package de.htwg.co2footprint_tracker;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.model.DatabaseInterval;
import de.htwg.co2footprint_tracker.model.InitialBucketContainer;
import de.htwg.co2footprint_tracker.model.Package;
import de.htwg.co2footprint_tracker.utils.Constants;
import de.htwg.co2footprint_tracker.utils.TimingHelper;

import java.util.ArrayList;
import java.util.Collections;

public class NetworkStatsUpdateService extends IntentService {

    public NetworkStatsUpdateService() {
        super("NetworkStatsUpdateService");
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

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION.ACTION_UPDATE_STATS.equals(action)) {
                ArrayList<Package> packageList = intent.getParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST);

                long startTime = TimingHelper.getStartTime(this);
                long timeOnUpdate = System.currentTimeMillis();
                boolean isNewRun = InitialBucketContainer.isNewRun();
                InitialBucketContainer.setNewRun(false);

                for (int i = 0; i < packageList.size(); i++) {
                    long rxBytesWifi = 0;
                    long txBytesWifi = 0;
                    long rxBytesMobile = 0;
                    long txBytesMobile = 0;
                    long rxPacketsWifi = 0;
                    long txPacketsWifi = 0;
                    long rxPacketsMobile = 0;
                    long txPacketsMobile = 0;
                    long rxBytesTotal = 0;
                    long txBytesTotal = 0;
                    long rxPacketsTotal = 0;
                    long txPacketsTotal = 0;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        NetworkStats networkStatsWifi;
                        NetworkStats networkStatsMobile;
                        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);

                        try {
                            networkStatsWifi = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, "", startTime, timeOnUpdate);
                            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                            do {
                                if (bucket.getUid() == packageList.get(i).getPackageUid()) {
                                    if (isNewRun) {
                                        //if it is the first run, it is possible that buckets already contain data and do not start from zero,
                                        // so this initial data needs to be stores for further calculations
                                        InitialBucketContainer.putInitialReceivedWifiData(bucket.getUid(), bucket.getRxBytes());
                                        InitialBucketContainer.putInitialTransmittedWifiData(bucket.getUid(), bucket.getTxBytes());
                                        InitialBucketContainer.putInitialReceivedWifiPacket(bucket.getUid(), bucket.getRxPackets());
                                        InitialBucketContainer.putInitialTransmittedWifiPacket(bucket.getUid(), bucket.getTxPackets());
                                    }
                                    rxBytesWifi += bucket.getRxBytes();
                                    txBytesWifi += bucket.getTxBytes();
                                    rxPacketsWifi += bucket.getRxPackets();
                                    txPacketsWifi += bucket.getTxPackets();
                                }
                                networkStatsWifi.getNextBucket(bucket);
                            } while (networkStatsWifi.hasNextBucket());

                            //since we stored the data in the initial run, we subtract it
                            rxBytesWifi -= InitialBucketContainer.getInitialReceivedWifiData(packageList.get(i).getPackageUid());
                            txBytesWifi -= InitialBucketContainer.getInitialTransmittedWifiData(packageList.get(i).getPackageUid());
                            rxPacketsWifi -= InitialBucketContainer.getInitialReceivedWifiPacket(packageList.get(i).getPackageUid());
                            txPacketsWifi -= InitialBucketContainer.getInitialTransmittedWifiPacket(packageList.get(i).getPackageUid());

                            if (!isNewRun) {
                                InitialBucketContainer.putInitialReceivedWifiData(packageList.get(i).getPackageUid(), rxBytesWifi);
                                InitialBucketContainer.putInitialReceivedWifiPacket(packageList.get(i).getPackageUid(), rxPacketsWifi);
                                InitialBucketContainer.putInitialTransmittedWifiData(packageList.get(i).getPackageUid(), txBytesWifi);
                                InitialBucketContainer.putInitialTransmittedWifiPacket(packageList.get(i).getPackageUid(), txPacketsWifi);
                            }
                            rxBytesTotal = rxBytesWifi;
                            txBytesTotal = txBytesWifi;
                            rxPacketsTotal = rxPacketsWifi;
                            txPacketsTotal = txPacketsWifi;
                        } catch (Exception e) {
                            Log.e(Constants.LOG.TAG, "Remote Exception: WIFI " + e.getMessage());
                        }
                        /*try {
                            networkStatsMobile = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, getSubscriberId(this, ConnectivityManager.TYPE_MOBILE), startTime, System.currentTimeMillis());
                            do {
                                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                                networkStatsMobile.getNextBucket(bucket);
                                if (bucket.getUid() == packageList.get(i).getPackageUid()) {
                                    rxBytesMobile += bucket.getRxBytes();
                                    txBytesMobile += bucket.getTxBytes();
                                    rxPacketsMobile += bucket.getRxPackets();
                                    txPacketsMobile += bucket.getTxPackets();
                                }
                            } while (networkStatsMobile.hasNextBucket());
                            rxBytesTotal = rxBytesWifi + rxBytesMobile;
                            txBytesTotal = txBytesWifi + txBytesMobile;
                            rxPacketsTotal = rxPacketsWifi + rxPacketsMobile;
                            txPacketsTotal = txPacketsWifi + txPacketsMobile;

                        } catch (Exception e) {
                            Log.e(Constants.LOG.TAG, "Remote Exception: " + e.getMessage());
                        }
                    } else {
                        //  Note: These only return data for our own UID on M and higher
                        //  Note: These only reset to zero after every reboot so the start / stop test logic doesn't
                        //        make sense for these
                        //  Note: After playing around with these briefly I don't think they give us enough info and
                        //        add confusion.  Changing the minimum API level to Marshmallow
                        rxBytesTotal = TrafficStats.getUidRxBytes(packageList.get(i).getPackageUid());
                        txBytesTotal = TrafficStats.getUidTxBytes(packageList.get(i).getPackageUid());
                        rxPacketsTotal = TrafficStats.getUidRxPackets(packageList.get(i).getPackageUid());
                        txPacketsTotal = TrafficStats.getUidTxPackets(packageList.get(i).getPackageUid());
                         */
                    }

                    packageList.get(i).setReceivedBytesWifi(rxBytesWifi);
                    packageList.get(i).setReceivedBytesMobile(rxBytesMobile);
                    packageList.get(i).setReceivedBytesTotal(rxBytesTotal);
                    packageList.get(i).setTransmittedBytesWifi(txBytesWifi);
                    packageList.get(i).setTransmittedBytesMobile(txBytesMobile);
                    packageList.get(i).setTransmittedBytesTotal(txBytesTotal);
                    packageList.get(i).setReceivedPacketsWifi(rxPacketsWifi);
                    packageList.get(i).setReceivedPacketsMobile(rxPacketsMobile);
                    packageList.get(i).setReceivedPacketsTotal(rxPacketsTotal);
                    packageList.get(i).setTransmittedPacketsWifi(txPacketsWifi);
                    packageList.get(i).setTransmittedPacketsMobile(txPacketsMobile);
                    packageList.get(i).setTransmittedPacketsTotal(txPacketsTotal);
                }

                //  Put the highest using packages at the top
                Collections.sort(packageList);

                //  Return the updated package list to the main activity
                Intent packageListUpdatedIntent = new Intent(Constants.ACTION.PACKAGE_LIST_UPDATED);
                packageListUpdatedIntent.putParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST, packageList);
                packageListUpdatedIntent.putExtra(Constants.PARAMS.TIMESTAMP, timeOnUpdate);
                sendBroadcast(packageListUpdatedIntent);

                saveToDatabase(getApplicationContext(), timeOnUpdate, packageList);
            }
        }
    }


    private void saveToDatabase(Context context, long timeStamp, ArrayList<Package> packageList) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Log.e(Constants.LOG.TAG, "new db helper created");
        for (Package packet : packageList) {
            if (packetHasChanges(packet)) {
                packet.setTimestamp(timeStamp);
                databaseHelper.addData(DatabaseInterval.MINUTE, packet);
            }
        }
    }

    private boolean packetHasChanges(Package packet) {
        //if any total value is anything other than 0 there was some traffic happening
        return (packet.getReceivedBytesTotal() + packet.getReceivedPacketsTotal() +
                packet.getTransmittedBytesTotal() + packet.getTransmittedPacketsTotal() != 0);
    }


}

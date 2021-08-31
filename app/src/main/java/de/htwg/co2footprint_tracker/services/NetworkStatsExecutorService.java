package de.htwg.co2footprint_tracker.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.htwg.co2footprint_tracker.MainActivity;
import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.helpers.PreferenceManagerHelper;
import de.htwg.co2footprint_tracker.model.Package;
import de.htwg.co2footprint_tracker.utils.Constants;

import static de.htwg.co2footprint_tracker.helpers.PackageHelper.getPackagesData;

/***
 * This class creates a foreground service, that will restart itself if the app is closed or destroyed by user.
 * It runs the NetworkStatsUpdateService periodically every x time instances.
 */

public class NetworkStatsExecutorService extends Service {

    private PowerManager.WakeLock wakeLock;
    private boolean isServiceStarted = false;
    private ArrayList<Package> packageList;
    private TimerTask timerTask;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();
            packageList = getPackagesData(this);
            switch (action) {
                case Constants.ACTION.START_SERVICE:
                    startService();
                    break;
                case Constants.ACTION.STOP_SERVICE:
                    stopService();
                    break;
                default:
                    Log.e(Constants.LOG.TAG, "Illegal action argument: " + action);
            }
        } else {
            Log.e(Constants.LOG.TAG, "Null intent for foreground start command.");
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(Constants.LOG.TAG, "The ForegroundService for NetworkStats has been created".toUpperCase());

        Notification notification = createNotification();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(Constants.LOG.TAG, "The ForegroundService for NetworkStats has been destroyed".toUpperCase());
    }

    /**
     * If the task is removed by the user or the system for any reason, the NetworkStatsExecutorService
     * restarts itself.
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), NetworkStatsExecutorService.class);
        restartServiceIntent.setPackage(getPackageName());
        restartServiceIntent.setAction(Constants.ACTION.START_SERVICE);

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
    }

    private void startService() {
        if (isServiceStarted) {
            return;
        }

        Log.e(Constants.LOG.TAG, "Start the ForegroundService".toUpperCase());
        isServiceStarted = true;
        PreferenceManagerHelper.setServiceState(this, Constants.PERSISTENCY.SERVICE_STARTED);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "CO2FootprintTracker::NetworkStatsExecutorService");
        wakeLock.acquire();

        //create a timer task, that executes the NetworkStatsUpdateService periodically
        Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.e(Constants.LOG.TAG, "updating stats...");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent updateStatsIntent = new Intent(getApplicationContext(), NetworkStatsUpdateService.class);
                    updateStatsIntent.putParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST, packageList);
                    updateStatsIntent.putExtra(Constants.PARAMS.SAVE_STATS_TO_FILE, false);
                    updateStatsIntent.setAction(Constants.ACTION.PROCESS_LATEST_NETWORK_TRAFFIC);
                    getApplicationContext().startService(updateStatsIntent);
                    Log.e(Constants.LOG.TAG, "...done updating stats");
                }
            }
        };

        //run the timer task
        timer.schedule(timerTask, 0L, 1000 * 60);

    }

    private void stopService() {
        Log.e(Constants.LOG.TAG, "Stopping the ForgegroundService".toUpperCase());

        try {
            if (null == wakeLock) {
                Log.e(Constants.LOG.TAG, "Wakelock object is null. This indicates, that the service is stopped without being started");
                return;
            }
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
            stopForeground(true);
            stopSelf();
        } catch (Exception e) {
            Log.e(Constants.LOG.TAG, "Service stopped without being started: " + e.getMessage());
        }

        isServiceStarted = false;
        timerTask.cancel();
        PreferenceManagerHelper.setServiceState(this, Constants.PERSISTENCY.SERVICE_STOPPED);

    }

    private Notification createNotification() {
        String notificationChannelId = "CO2-FOOTPRINT-TRACKER SERVICE CHANNEL";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(notificationChannelId, "CO2-FOOTPRINT-TRACKER SERVICE CHANNEL", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Service channel for CO2-Footprint-Tracker.");
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, notificationChannelId);
        } else {
            builder = new Notification.Builder(this);
        }

        //TODO Evtl globale und localized variable nutzen für benamung

        Resources res = MainActivity.getWeakInstanceActivity().getResources();
        builder.setContentTitle(res.getString(R.string.app_name))
                .setContentText(res.getString(R.string.notification_context_text))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher) //TODO change icon
                .setTicker(res.getString(R.string.notification_context_text)) //ticker used for accessibility mode
                .setPriority(Notification.PRIORITY_HIGH);

        return builder.build();
    }
}

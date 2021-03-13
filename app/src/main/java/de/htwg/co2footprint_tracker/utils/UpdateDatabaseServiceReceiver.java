package de.htwg.co2footprint_tracker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;

import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.model.DatabaseInterval;
import de.htwg.co2footprint_tracker.model.Package;


public class UpdateDatabaseServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(Constants.LOG.TAG, "recieved intent");
        if (intent.getAction().equals(Constants.ACTION.PACKAGE_LIST_UPDATED)) {
            Log.e(Constants.LOG.TAG, "inside recieved intent");

            long timeStamp = intent.getLongExtra(Constants.PARAMS.TIMESTAMP, -1);

            ArrayList<Package> packageList = intent.getParcelableArrayListExtra(Constants.PARAMS.PACKAGE_LIST);

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            Log.e(Constants.LOG.TAG, "new db helper created");
            for (Package packet : packageList) {
                if (packetHasChanges(packet)) {
                    packet.setTimestamp(timeStamp);
                    databaseHelper.addData(DatabaseInterval.MINUTE, packet);
                }
            }
        }
    }

    private boolean packetHasChanges(Package packet) {
        //if any total value is anything other than 0 there was some traffic happening
        return (packet.getReceivedBytesTotal() + packet.getReceivedPacketsTotal() +
                packet.getTransmittedBytesTotal() + packet.getTransmittedPacketsTotal() != 0);
    }


}

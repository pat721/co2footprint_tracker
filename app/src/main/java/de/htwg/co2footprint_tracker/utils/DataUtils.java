package de.htwg.co2footprint_tracker.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.model.Consumer;

public class DataUtils {


    public static List<Consumer> CursorToConsumerList(Cursor data, Context ctx) {
        ArrayList<Consumer> consumers = new ArrayList<>();


        if (!data.moveToFirst()) {
            return consumers;
        }

        do {

            try {
                String packageid = data.getString(2);
                Drawable icon;

                if (packageid.contains("internal.uid")) {
                    icon = ContextCompat.getDrawable(ctx, R.drawable.ic_android_black_24dp);
                } else {
                    icon = ctx.getPackageManager().getApplicationIcon(packageid);
                }
                consumers.add(new Consumer(data.getDouble(1),
                        data.getString(0), icon
                ));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } while (data.moveToNext());
        return consumers;
    }

}

package de.htwg.co2footprint_tracker.utils;

import java.text.DecimalFormat;

public class UnitUtils {

    public static String humanReadableByteCountSI(long bytes) {

        if (bytes > 1024 * 1024 * 1024) {
            return (bytes / 1024 / 1024 / 1024) + " GB";
        } else if (bytes > 1024 * 1024) {
            return (bytes / 1024 / 1024) + " MB";
        } else if (bytes > 1024) {
            return (bytes / 1024) + " KB";
        } else
            return bytes + " b";
    }


    /**
     * @return unix timestamp converted to midnight
     */
    public static long getMidnightTimestamp() {
        long now = System.currentTimeMillis() / 1000;
        return now - now % 86400; // 24 * 60 * 60 sec in one day
    }

    public static String RoundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return df2.format(val);
    }

    public static String toFormattedWeight(double grams) {
        double kg = grams / 1000.0;
        if (grams >= 1000) {
            return RoundTo2Decimals(kg) + " kg";
        } else {
            return RoundTo2Decimals(grams) + " g";
        }

    }

}

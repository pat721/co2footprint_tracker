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
     * */
    public static long getMidnightTimestamp(long timestamp) {
        return timestamp - timestamp % 86400; // 24 * 60 * 60 sec in one day
    }

    public static double RoundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.parseDouble(df2.format(val));
    }

}

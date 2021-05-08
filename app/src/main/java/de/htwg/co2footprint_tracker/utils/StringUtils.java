package de.htwg.co2footprint_tracker.utils;

public class StringUtils {

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

}

package de.htwg.co2footprint_tracker.model;

import java.util.HashMap;

public class InitialBucketContainer {

    private final static String WIFI_PREFIX = "wifi_";
    private final static String MOBILE_PREFIX = "mobile_";

    private final static String RECEIVED_PREFIX = "rx_";
    private final static String TRANSMITTED_PREFIX = "tx_";

    private final static String DATA_PREFIX = "data_";
    private final static String PACKET_PREFIX = "pckt_";


    private static boolean newRun = true;
    private static HashMap<String, Long> mappedPackageData = new HashMap<>();


    //debugging only------

    public static HashMap<String, Long> getOurMap (){
        return mappedPackageData;
    }
    //debugging only------



    public static void putInitialTransmittedWifiData(int uid, long bytes) {
        mappedPackageData.put(DATA_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid, bytes);
    }

    public static long getInitialTransmittedWifiData(int uid) {
        return mappedPackageData.get(DATA_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid);
    }


    public static void putInitialReceivedWifiData(int uid, long bytes) {
        mappedPackageData.put(DATA_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid, bytes);
    }

    public static long getInitialReceivedWifiData(int uid) {
        return mappedPackageData.get(DATA_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid);
    }

    public static void putInitialTransmittedWifiPacket(int uid, long packet) {
        mappedPackageData.put(PACKET_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid, packet);
    }

    public static long getInitialTransmittedWifiPacket(int uid) {
        return mappedPackageData.get(PACKET_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid);
    }

    public static void putInitialReceivedWifiPacket(int uid, long packet) {
        mappedPackageData.put(PACKET_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid, packet);
    }

    public static long getInitialReceivedWifiPacket(int uid) {
        return mappedPackageData.get(PACKET_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid);
    }

    public static boolean isNewRun() {
        return newRun;
    }

    public static void setNewRun(boolean isNewRun) {
        InitialBucketContainer.newRun = isNewRun;
    }

}

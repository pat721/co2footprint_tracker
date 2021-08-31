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


    public static void putInitialTransmittedWifiData(int uid, long bytes) {
        if (mappedPackageData.containsKey(DATA_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid)) {
            long currentBytes = mappedPackageData.get(DATA_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid);
            long sumOfBytes = currentBytes + bytes;
            mappedPackageData.put(DATA_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid, sumOfBytes);
        } else {
            mappedPackageData.put(DATA_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid, bytes);
        }
    }

    public static long getInitialTransmittedWifiData(int uid) {
        if (mappedPackageData.containsKey(DATA_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid)) {
            return mappedPackageData.get(DATA_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid);
        } else {
            return 0;
        }
    }

    public static void putInitialReceivedWifiData(int uid, long bytes) {
        if (mappedPackageData.containsKey(DATA_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid)) {
            long currentBytes = mappedPackageData.get(DATA_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid);
            long sumOfBytes = currentBytes + bytes;
            mappedPackageData.put(DATA_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid, sumOfBytes);
        } else {
            mappedPackageData.put(DATA_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid, bytes);
        }
    }

    public static long getInitialReceivedWifiData(int uid) {
        if (mappedPackageData.containsKey(DATA_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid)) {
            return mappedPackageData.get(DATA_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid);
        } else {
            return 0;
        }
    }

    public static void putInitialTransmittedWifiPacket(int uid, long packets) {
        if (mappedPackageData.containsKey(PACKET_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid)) {
            long currentPackets = mappedPackageData.get(PACKET_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid);
            long sumOfPackets = currentPackets + packets;
            mappedPackageData.put(PACKET_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid, sumOfPackets);
        } else {
            mappedPackageData.put(PACKET_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid, packets);
        }
    }

    public static long getInitialTransmittedWifiPacket(int uid) {
        if (mappedPackageData.containsKey(PACKET_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid)) {
            return mappedPackageData.get(PACKET_PREFIX + WIFI_PREFIX + TRANSMITTED_PREFIX + uid);
        } else {
            return 0;
        }
    }

    public static void putInitialReceivedWifiPacket(int uid, long packets) {
        if (mappedPackageData.containsKey(PACKET_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid)) {
            long currentPackets = mappedPackageData.get(PACKET_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid);
            long sumOfPackets = currentPackets + packets;
            mappedPackageData.put(PACKET_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid, sumOfPackets);
        } else {
            mappedPackageData.put(PACKET_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid, packets);
        }
    }

    public static long getInitialReceivedWifiPacket(int uid) {
        if (mappedPackageData.containsKey(PACKET_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid)) {
            return mappedPackageData.get(PACKET_PREFIX + WIFI_PREFIX + RECEIVED_PREFIX + uid);
        } else {
            return 0;
        }
    }

    //MOBILE
    public static void putInitialTransmittedMobileData(int uid, long bytes) {
        if (mappedPackageData.containsKey(DATA_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid)) {
            long currentBytes = mappedPackageData.get(DATA_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid);
            long sumOfBytes = currentBytes + bytes;
            mappedPackageData.put(DATA_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid, sumOfBytes);
        } else {
            mappedPackageData.put(DATA_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid, bytes);
        }
    }

    public static long getInitialTransmittedMobileData(int uid) {
        if (mappedPackageData.containsKey(DATA_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid)) {
            return mappedPackageData.get(DATA_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid);
        } else {
            return 0;
        }
    }

    public static void putInitialReceivedMobileData(int uid, long bytes) {
        if (mappedPackageData.containsKey(DATA_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid)) {
            long currentBytes = mappedPackageData.get(DATA_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid);
            long sumOfBytes = currentBytes + bytes;
            mappedPackageData.put(DATA_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid, sumOfBytes);
        } else {
            mappedPackageData.put(DATA_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid, bytes);
        }
    }

    public static long getInitialReceivedMobileData(int uid) {
        if (mappedPackageData.containsKey(DATA_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid)) {
            return mappedPackageData.get(DATA_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid);
        } else {
            return 0;
        }
    }

    public static void putInitialTransmittedMobilePacket(int uid, long packets) {
        if (mappedPackageData.containsKey(PACKET_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid)) {
            long currentPackets = mappedPackageData.get(PACKET_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid);
            long sumOfPackets = currentPackets + packets;
            mappedPackageData.put(PACKET_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid, sumOfPackets);
        } else {
            mappedPackageData.put(PACKET_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid, packets);
        }
    }

    public static long getInitialTransmittedMobilePacket(int uid) {
        if (mappedPackageData.containsKey(PACKET_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid)) {
            return mappedPackageData.get(PACKET_PREFIX + MOBILE_PREFIX + TRANSMITTED_PREFIX + uid);
        } else {
            return 0;
        }
    }

    public static void putInitialReceivedMobilePacket(int uid, long packets) {
        if (mappedPackageData.containsKey(PACKET_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid)) {
            long currentPackets = mappedPackageData.get(PACKET_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid);
            long sumOfPackets = currentPackets + packets;
            mappedPackageData.put(PACKET_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid, sumOfPackets);
        } else {
            mappedPackageData.put(PACKET_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid, packets);
        }
    }

    public static long getInitialReceivedMobilePacket(int uid) {
        if (mappedPackageData.containsKey(PACKET_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid)) {
            return mappedPackageData.get(PACKET_PREFIX + MOBILE_PREFIX + RECEIVED_PREFIX + uid);
        } else {
            return 0;
        }
    }

    public static void clearMappedPackageData() {
        mappedPackageData.clear();
    }

    public static boolean isNewRun() {
        return newRun;
    }

    public static void setNewRun(boolean isNewRun) {
        InitialBucketContainer.newRun = isNewRun;
    }

}

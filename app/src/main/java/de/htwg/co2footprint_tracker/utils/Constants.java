package de.htwg.co2footprint_tracker.utils;

public class Constants {
    public interface PERMISSION {
        int RC_LOCATION_READ_PHONE_STATE_ACCESS_NETWORK_STATE = 69;
        int RC_LOCATION = 420;
    }

    public interface ACTION {
        String PROCESS_LATEST_NETWORK_TRAFFIC = "de.htwg.co2footprint_tracker.action.UPDATE_STATS";
        String PACKAGE_LIST_UPDATED = "de.htwg.co2footprint_tracker.action.PACKAGE_LIST_UPDATED";
        String UPDATE_SERVICE_SCHEDULER = "de.htwg.co2footprint_tracker.action.UPDATE_SERVICE_SCHEDULER_STARTED";
        String RESTART_SCHEDULER_SERVICE = "de.htwg.co2footprint_tracker.action.RESTART_SCHEDULER_SERVICE";
        String START_SERVICE = "START";
        String STOP_SERVICE = "STOP";
    }

    public interface PARAMS {
        String PACKAGE_LIST = "de.htwg.co2footprint_tracker.extra.PACKAGE_LIST";
        String SAVE_STATS_TO_FILE = "de.htwg.co2footprint_tracker.extra.SAVE_STATS_TO_FILE";
    }

    public interface PERSISTENCY {
        String PREFERENCE_STARTING_TIME_KEY = "SAVED_START_TIME";
        long NO_START_TIME_SET = 0L;
        String DEVICE_IS_TABLET = "DEVICE_IS_TABLET";
        int DEVICE_TYPE_NOT_SET = -1;
        int DEVICE_TYPE_TABLET = 0;
        int DEVICE_TYPE_NOT_TABLET = 1;
        String ADMINISTRATION_AREA = "ADMINISTRATION_AREA";
        String SERVICE_STARTED = "STARTED";
        String SERVICE_STOPPED = "STOPPED";
        String TODAY_TOTAL_TOGGLE_STATE = "TODAY_TOTAL_TOGGLE_STATE";
        int TOGGLE_STATE_NOT_SET = -1;
        int TOGGLE_STATE_TODAY = 0;
        int TOGGLE_STATE_TOTAL = 1;
    }

    public interface LOG {
        String TAG = "FootprintTracker";
    }

    public interface ELECTRICITY {
        double VORARLBERG_FACTOR = 0.019528684;
        double APPENZELL_AUSS_FACTOR = 0.014425932;
        double APPENZELL_INN_FACTOR = 0.00223616;
        double SCHAFFHAUSEN_FACTOR = 0.023133478;
        double ST_GALLEN_FACTOR = 0.031782046;
        double THURGAU_FACTOR = 0.010749989;
        double ZUERICH_FACTOR = 0.013780631;
        double BADEN_WUERTTEMBERG_FACTOR = 0.13439065;
        double BAYERN_FACTOR = 0.04230199;
        double LICHTENSTEIN_FACTOR = 0.028816243;
    }
}

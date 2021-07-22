package de.htwg.co2footprint_tracker.utils;

public class Constants {
    public interface PERMISSION {
        int REQUEST_CODE = 1;
        int LOCATION_REQUEST_CODE = 44;
    }

    public interface ACTION {
        String PROCESS_LATEST_NETWORK_TRAFFIC = "de.htwg.co2footprint_tracker.action.UPDATE_STATS";
        String PACKAGE_LIST_UPDATED = "de.htwg.co2footprint_tracker.action.PACKAGE_LIST_UPDATED";
        String UPDATE_SERVICE_SCHEDULER = "de.htwg.co2footprint_tracker.action.UPDATE_SERVICE_SCHEDULER_STARTED";
        String RESTART_SCHEDULER_SERVICE = "de.htwg.co2footprint_tracker.action.RESTART_SCHEDULER_SERVICE";
    }

    public interface PARAMS {
        String PACKAGE_LIST = "de.htwg.co2footprint_tracker.extra.PACKAGE_LIST";
        String SAVE_STATS_TO_FILE = "de.htwg.co2footprint_tracker.extra.SAVE_STATS_TO_FILE";
    }

    public interface PERSISTENCY {
        String PREFERENCE_STARTING_TIME_KEY = "SAVED_START_TIME";
        String DEVICE_IS_TABLET = "DEVICE_IS_TABLET";
        String ADMINISTRATION_AREA = "ADMINISTRATION_AREA";
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

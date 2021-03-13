package de.htwg.co2footprint_tracker.utils;

/**
 * Created by darry on 01/02/2018.
 */

public class Constants {
    public interface PERMISSION {
        int PERMISSION_REQUEST = 1;
    }

    public interface ACTION {
        String ACTION_UPDATE_STATS = "de.htwg.co2footprint_tracker.action.UPDATE_STATS";
        String PACKAGE_LIST_UPDATED = "de.htwg.co2footprint_tracker.action.PACKAGE_LIST_UPDATED";
    }

    public interface PARAMS {
        String PACKAGE_LIST = "de.htwg.co2footprint_tracker.extra.PACKAGE_LIST";
        String SAVE_STATS_TO_FILE = "de.htwg.co2footprint_tracker.extra.SAVE_STATS_TO_FILE";
        String TIMESTAMP = "de.htwg.co2footprint_tracker.extra.TIMESTAMP";
    }

    public interface MISC {
        long MINIMUM_RECOMMENDED_TEST_TIME = 180;
    }

    public interface LOG {
        String TAG = "NetworkStatsLog";
    }
}

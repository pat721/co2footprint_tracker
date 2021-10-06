package de.htwg.co2footprint_tracker.model.ipcc;

import java.util.Map;

public class IpccAdminZoneModel implements IpccModel {

    private Map<String, Double> adminAreaStats;


    @Override
    public Map getCorrespondingMapFromSelfOrNested(String adminArea) {
        return adminAreaStats;
    }
}

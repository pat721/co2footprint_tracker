package de.htwg.co2footprint_tracker.model.ipcc;

import java.util.Map;

public class IpccWorldModel implements IpccModel {

    private Map<String, Double> worldStats;

    private Map<String, IpccModel> countryStatModels;


    @Override
    public Map getCorrespondingMapFromSelfOrNested(String adminArea) {
        Map ipccMap = null;
        for (Map.Entry me : countryStatModels.entrySet()) {
            IpccModel ipccModel = (IpccModel) me.getValue();
            ipccMap = ipccModel.getCorrespondingMapFromSelfOrNested(adminArea);
        }
        return ipccMap == null ? worldStats : ipccMap;
    }



}

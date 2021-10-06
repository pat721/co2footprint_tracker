package de.htwg.co2footprint_tracker.model.ipcc;

import java.util.Map;

public class IpccCountryModel implements IpccModel {

    private Map<String, Double> countryStats;

    private Map<String, IpccModel> adminAreaStatModels;


    @Override
    public Map getCorrespondingMapFromSelfOrNested(String adminArea) {
        Map ipccMap = null;
        for (Map.Entry me : adminAreaStatModels.entrySet()) {
            IpccModel ipccModel = (IpccModel) me.getValue();
            ipccMap = ipccModel.getCorrespondingMapFromSelfOrNested(adminArea);
        }
        return ipccMap == null ? countryStats : ipccMap;
    }
}

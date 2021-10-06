package de.htwg.co2footprint_tracker.helpers;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class IpccTableHelper {


    private static IpccTableHelper ipccTableHelper;
    private JsonObject productionIpccTable;
    private JsonObject operationIpccTable;
    private JsonObject endOfLifeIpccTable;
    private JsonObject strommixTable;


    public IpccTableHelper getInstance() {
        if (ipccTableHelper == null) {
            ipccTableHelper = new IpccTableHelper();
        }
        return ipccTableHelper;
    }

    public IpccTableHelper() {

        String object = FirebaseRemoteConfig.getInstance().getString("ProductionIpccTable");
        Gson gson = new GsonBuilder().create();
        JsonObject json = gson.fromJson(object, JsonObject.class);

        int i = 0;

    }

    public double getIpccValuesFor(String adminArea, String country) {

        double result = 0;

        result += getIpccProductionValues();


        JsonObject world = productionIpccTable.getAsJsonObject("world");

        JsonObject countryJson = world.getAsJsonObject(country);

        if (countryJson != null) {
            JsonObject adminAreaJson = countryJson.getAsJsonObject(adminArea);
        }


        //TODO
        return 0.0;
    }


    private double getIpccProductionValues(boolean isMobilePhone, boolean isWifi) {

        double result = 0.0;
        JsonObject world = productionIpccTable.getAsJsonObject("world");

        JsonObject phoneJson = world.getAsJsonObject("phone");
        JsonObject tabletJson = world.getAsJsonObject("tablet");
        JsonObject basisstationJson = world.getAsJsonObject("basisstation");
        JsonObject corenetworkJson = world.getAsJsonObject("corenetwork");
        JsonObject transportnetworkJson = world.getAsJsonObject("transportnetwork");
        JsonObject datacenterJson = world.getAsJsonObject("datacenter");
        JsonObject homerouterJson = world.getAsJsonObject("homerouter");

        double ipccPhone = phoneJson.getAsJsonObject("ipcc").getAsDouble();
        double ipccTablet = tabletJson.getAsJsonObject("ipcc").getAsDouble();
        double ipccBasisstation = basisstationJson.getAsJsonObject("ipcc").getAsDouble();
        double ipcccorenetwork = corenetworkJson.getAsJsonObject("ipcc").getAsDouble();
        double ipcctransportnetwork = transportnetworkJson.getAsJsonObject("ipcc").getAsDouble();
        double ipccdatacenter = datacenterJson.getAsJsonObject("ipcc").getAsDouble();
        double ipcchomerouter = homerouterJson.getAsJsonObject("ipcc").getAsDouble();

        result += isMobilePhone ? ipccPhone : ipccTablet;
        result += isWifi ? ipcchomerouter : ipccBasisstation;
        result += ipcccorenetwork;
        result += ipcctransportnetwork;
        result += ipccdatacenter;

        return result;
    }

    private double getIpccOperationValues(String adminArea, String country) {


//        "world": {
//            "datacenter": 0.0000001485,
//                    "corenetwork_maintenance": 0.00000834,
//                    "transportnetwork_maintenance": 0.0000618,
//                    "basisstation_maintenance": 0.000771,
//                    "datacenter_maintenance": 0.01299,
//
//                    "germany": {
//                "stromverbrauch_corenetwork": 0.00017,
//                        "stromverbrauch_transportnetwork": 0.00211,
//                        "baden-württemberg": {
//                    "stromverbrauch_basisstationen": 0.0000042621013,
//                            "dslam_energieverbrauch": 0.0015832224,
//                            "electricity_usage_router": 0.0004870029
//                },


        double result = 0.0;
        JsonObject world = operationIpccTable.getAsJsonObject("world");


        String[] operationIpccArray = {"StromBasis", "StromCore", "StromTransport"}; //TODO


        for (String key : operationIpccArray) {

            JsonObject countryJsonObject = world.getAsJsonObject(country);
            if (countryJsonObject != null) { //if country exists

                JsonObject adminAreaJsonObject = countryJsonObject.getAsJsonObject(adminArea);
                if (adminAreaJsonObject != null) {
                    //TODO add to result
                    continue;
                }
                //add to result
                result += adminAreaJsonObject.getAsJsonObject(key).getAsDouble();
                continue;
            }
            result += world.getAsJsonObject(key).getAsDouble();
        }


        return result;
    }


}

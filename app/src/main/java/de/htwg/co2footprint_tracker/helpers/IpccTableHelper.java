package de.htwg.co2footprint_tracker.helpers;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

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

        String object = FirebaseRemoteConfig.getInstance().getString("calculationValues_production");
        Gson gson = new GsonBuilder().create();
        productionIpccTable = gson.fromJson(object, JsonObject.class);

        object = FirebaseRemoteConfig.getInstance().getString("calculationValues_operation");
        operationIpccTable = gson.fromJson(object, JsonObject.class);

        object = FirebaseRemoteConfig.getInstance().getString("CalculationValues_endOfLife");
        endOfLifeIpccTable = gson.fromJson(object, JsonObject.class);


        int i = 0;

    }

    public double getIpccValuesFor(String adminArea, String country) {

        double result = 0;

        result += getIpccProductionValues(true, true);
        result += getIpccOperationValues(adminArea, country, true);
        result += getIpccEndOfLifeValues();


        return result;
    }


    private double getIpccProductionValues(boolean isMobilePhone, boolean isWifi) {

        double result = 0.0;
        JsonObject world = productionIpccTable.getAsJsonObject("world");

        JsonObject phoneJson = world.getAsJsonObject("mobilephone");
        JsonObject tabletJson = world.getAsJsonObject("tablet");
        JsonObject basisstationJson = world.getAsJsonObject("basisstation");
        JsonObject corenetworkJson = world.getAsJsonObject("corenetwork");
        JsonObject transportnetworkJson = world.getAsJsonObject("transportnetwork");
        JsonObject datacenterJson = world.getAsJsonObject("datacenter");
        JsonObject homerouterJson = world.getAsJsonObject("homerouter");

        double ipccPhone = phoneJson.get("ipcc").getAsDouble();
        double ipccTablet = tabletJson.get("ipcc").getAsDouble();
        double ipccBasisstation = basisstationJson.get("ipcc").getAsDouble();
        double ipcccorenetwork = corenetworkJson.get("ipcc").getAsDouble();
        double ipcctransportnetwork = transportnetworkJson.get("ipcc").getAsDouble();
        double ipccdatacenter = datacenterJson.get("ipcc").getAsDouble();
        double ipcchomerouter = homerouterJson.get("ipcc").getAsDouble();

        result += isMobilePhone ? ipccPhone : ipccTablet;
        result += isWifi ? ipcchomerouter : ipccBasisstation;
        result += ipcccorenetwork;
        result += ipcctransportnetwork;
        result += ipccdatacenter;

        return result;
    }


    private double getIpccOperationValues(String adminArea, String country, boolean isWifi) {
        double result = 0.0;
        JsonObject world = operationIpccTable.getAsJsonObject("world");
        JsonObject countryJsonObject = world.getAsJsonObject(country);

        JsonObject adminAreaJsonObject = null;
        if (countryJsonObject != null) {
            adminAreaJsonObject = countryJsonObject.getAsJsonObject(adminArea);
        }


        String[] operationIpccArray = {"datacenter", "corenetwork_maintenance", "datacenter_maintenance", "stromverbrauch_corenetwork", "stromverbrauch_transportnetwork"};


        ArrayList<String> operationIpccList = new ArrayList(Arrays.asList(operationIpccArray));


        if (isWifi) {
            operationIpccList.add("electricity_usage_router");
        } else {
            operationIpccList.add("basisstation_maintenance");
            operationIpccList.add("stromverbrauch_basisstationen");
        }

        for (String key : operationIpccList) {

            if (countryJsonObject != null) { //if country exists

                if (adminAreaJsonObject != null) {
                    JsonElement keyJsonElement = adminAreaJsonObject.get(key);
                    if (keyJsonElement != null) {
                        result += keyJsonElement.getAsDouble();
                        continue;
                    }
                }

                JsonElement keyJsonElement = countryJsonObject.get(key);
                if (keyJsonElement != null) {
                    result += keyJsonElement.getAsDouble();
                    continue;
                }
            }
            result += world.get(key).getAsDouble();
        }


        return result;
    }


    private double getIpccEndOfLifeValues() {
        double result = 0.0;


        return result;
    }


}

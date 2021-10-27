package de.htwg.co2footprint_tracker.helpers;

import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.DEVICE_TYPE_NOT_TABLET;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

import de.htwg.co2footprint_tracker.MainActivity;

public class IpccTableHelper {


    private static IpccTableHelper ipccTableHelper;
    private final JsonObject productionIpccTable;
    private final JsonObject operationIpccTable;
    private final JsonObject endOfLifeIpccTable;
    private final JsonObject electricityMix;


    public IpccTableHelper() {

        String object = FirebaseRemoteConfig.getInstance().getString("calculationValues_production");
        Gson gson = new GsonBuilder().create();
        productionIpccTable = gson.fromJson(object, JsonObject.class);

        object = FirebaseRemoteConfig.getInstance().getString("calculationValues_operation");
        operationIpccTable = gson.fromJson(object, JsonObject.class);

        object = FirebaseRemoteConfig.getInstance().getString("calculationValues_endOfLife");
        endOfLifeIpccTable = gson.fromJson(object, JsonObject.class);

        object = FirebaseRemoteConfig.getInstance().getString("calculationValues_electricityMix");
        electricityMix = gson.fromJson(object, JsonObject.class);

    }

    public static IpccTableHelper getInstance() {
        if (ipccTableHelper == null) {
            ipccTableHelper = new IpccTableHelper();
        }
        return ipccTableHelper;
    }

    public double getIpccValuesFor(String adminArea, String countryISOCode, boolean isWifi) {

        double result = 0;

        boolean isMobilePhone = PreferenceManagerHelper.getDeviceType(MainActivity.getWeakInstanceActivity()) == DEVICE_TYPE_NOT_TABLET;

        result += getIpccProductionValues(isMobilePhone, isWifi);
        result += getIpccOperationValues(adminArea, countryISOCode, isWifi);
        result += getIpccEndOfLifeValues(isMobilePhone, countryISOCode);

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


    private double getIpccOperationValues(String adminArea, String countryISOCode, boolean isWifi) {
        double result = 0.0;
        JsonObject world = operationIpccTable.getAsJsonObject("world");
        JsonObject countryJsonObject = world.getAsJsonObject(countryISOCode);

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


    private double getIpccEndOfLifeValues(boolean isMobilePhone, String countryISOCode) {
        JsonObject world = endOfLifeIpccTable.getAsJsonObject("world");
        JsonObject countryJsonObject = world.getAsJsonObject(countryISOCode);
        String key = isMobilePhone ? "mobilephone" : "tablet";

        if (countryJsonObject != null) { //if country exists

            JsonElement keyJsonElement = countryJsonObject.get(key);
            if (keyJsonElement != null) {
                return keyJsonElement.getAsDouble();
            }

        }
        return world.get(key).getAsDouble();
    }

    public double getElectricityFactor(String adminArea, String countryISOCode) {
        JsonObject world = electricityMix.getAsJsonObject("world");
        JsonObject countryJsonObject = world.getAsJsonObject(countryISOCode);

        if (countryJsonObject != null) {
            JsonElement adminAreaJson = countryJsonObject.get(adminArea);
            if (adminAreaJson != null) {
                return adminAreaJson.getAsDouble();
            }
        } else {
            return world.get("default").getAsDouble();
        }
        return countryJsonObject.get("default").getAsDouble();
    }
}

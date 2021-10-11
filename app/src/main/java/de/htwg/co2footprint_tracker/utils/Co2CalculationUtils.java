package de.htwg.co2footprint_tracker.utils;

import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.DEVICE_TYPE_NOT_TABLET;

import android.content.Context;
import android.util.Log;

import java.util.HashSet;
import java.util.Locale;

import de.htwg.co2footprint_tracker.MainActivity;
import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.helpers.IpccTableHelper;
import de.htwg.co2footprint_tracker.helpers.PreferenceManagerHelper;
import de.htwg.co2footprint_tracker.model.MainCardModel;

public class Co2CalculationUtils {
    private static final double ENERGY_IMPACT_SMARTPHONE = 0.000107689;
    private static final double AVERAGE_CONSUMPTION_ACCESS_NETWORK_AND_CPE = 52;
    private static final double AVERAGE_ENERGY_INTESITY_OF_LONG_HAUL_AND_METRO = 0.052;
    private static final double ELECTRICITY_CONSUMPTION_BY_DATACENTERS = 0.000000000072;
    private static final double KHW_TO_CO2_CONVERSION_VALUE = 515.46;

    public static MainCardModel calculateMainCardData(HashSet<Integer> applicationUidSet, Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        long totalReceivedBytes = 0;
        double totalEnergyConsumption = 0;

        for (Integer uid : applicationUidSet) {
            totalReceivedBytes += databaseHelper.getTotalReceivedBytesForPackage(uid);
            totalEnergyConsumption += databaseHelper.getTotalEnergyConsumptionForPackage(uid);
        }

        return new MainCardModel(totalReceivedBytes, totalEnergyConsumption);
    }


    /**
     * @param time: minutes
     * @return energy consumption in g co2
     */
    public double calculateTotalEnergyConsumption(double time, String adminArea, String countryISOCode, long bytes, boolean isWifi) {

        double energyConsumption = calculateSmartphoneEnergyConsumption(time);
        double electricityFactor = IpccTableHelper.getInstance().getElectricityFactor(adminArea.toLowerCase(), countryISOCode.toLowerCase());
        double lifeCycleFactor = IpccTableHelper.getInstance().getIpccValuesFor(adminArea.toLowerCase(), countryISOCode.toLowerCase(), isWifi);

        double returnVal = lifeCycleFactor * bytesToGB(bytes);
        returnVal += energyConsumption * electricityFactor;

        Log.e(Constants.LOG.TAG, "got returnVAl: " + returnVal);

        return returnVal*1000;
    }


    /**
     * @param time: minutes
     * @return energy consumption in kWh
     */
    public double calculateSmartphoneEnergyConsumption(double time) {
        return time * ENERGY_IMPACT_SMARTPHONE;
    }

    private double bytesToGB(long bytes) {
        return bytes / 1024.0 / 1024.0 / 1024.0;
    }


}

package de.htwg.co2footprint_tracker.utils;

import android.content.Context;

import java.util.HashSet;

import de.htwg.co2footprint_tracker.database.DatabaseHelper;
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
     * @param gb:   gb
     * @return energy consumption in kWh
     */
    private double calculateTotalEnergyConsumption(double time, double gb) {
        double total;
        total = calculateSmartphoneEnergyConsumption(time);
        total += calculateInternetEnergyConsumption(gb);
        total += calculateDataCenterEnergyConsumption(gb);
        total += calculateDslamAndCpeEnergyConsumption();

        return total;
    }

    /**
     * @param time:  minutes
     * @param bytes: bytes
     * @return energy consumption in g co2
     */
    public double calculateTotalEnergyConsumption(double time, long bytes, String adminArea) {
        double mb = bytesToGB(bytes);

        double returnVal = calculateTotalEnergyConsumption(time, mb) * KHW_TO_CO2_CONVERSION_VALUE * getAdminAreaCalculationFactor(adminArea);
        return returnVal;
    }

    public double calculateSmartphoneEnergyConsumption(double time) {
        return time * ENERGY_IMPACT_SMARTPHONE;
    }

    public double calculateInternetEnergyConsumption(double megabyte) {
        return megabyte * AVERAGE_ENERGY_INTESITY_OF_LONG_HAUL_AND_METRO;
    }

    public double calculateDataCenterEnergyConsumption(double gigabyte) {
        return gigabyte * 1000000000 * ELECTRICITY_CONSUMPTION_BY_DATACENTERS;
    }

    public double calculateDslamAndCpeEnergyConsumption() {
        return AVERAGE_CONSUMPTION_ACCESS_NETWORK_AND_CPE / 1000;
    }

    private double bytesToGB(long bytes) {
        return bytes / 1024.0 / 1024.0 / 1024.0;
    }


    public double getAdminAreaCalculationFactor(String adminArea) {

        //TODO Swich-Case for different locations
        return 1;
    }

}

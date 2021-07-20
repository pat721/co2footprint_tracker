package de.htwg.co2footprint_tracker.utils;

import android.content.Context;
import android.util.Log;

import java.util.HashSet;

import de.htwg.co2footprint_tracker.database.DatabaseHelper;
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
        double energyConsumption = calculateTotalEnergyConsumption(time, mb);
        double returnVal = 0;

        if (adminArea == null) { //base case
            Log.e(Constants.LOG.TAG, "calculated c02 with null");
            return calculateTotalEnergyConsumption(time, mb) * KHW_TO_CO2_CONVERSION_VALUE;
        }

        switch (adminArea) {
            case "Baden-Württemberg":
                returnVal = Constants.ELECTRICITY.BADEN_WUERTTEMBERG_FACTOR * energyConsumption;
                break;
            case "Bayern":
                returnVal = Constants.ELECTRICITY.BAYERN_FACTOR * energyConsumption;
                break;
            case "Thurgau": //thurgau
                returnVal = Constants.ELECTRICITY.THURGAU_FACTOR * energyConsumption;
                break;
            case "Vorarlberg": //vorarlberg
                returnVal = Constants.ELECTRICITY.VORARLBERG_FACTOR * energyConsumption;
                break;
            case "Appenzell Innerrhoden":
                returnVal = Constants.ELECTRICITY.APPENZELL_INN_FACTOR * energyConsumption;
                break;
            case "Appenzell Ausserrhoden":
                returnVal = Constants.ELECTRICITY.APPENZELL_AUSS_FACTOR * energyConsumption;
                break;
            case "Zürich":
                returnVal = Constants.ELECTRICITY.ZUERICH_FACTOR * energyConsumption;
                break;
            case "Schaffhausen":
                returnVal = Constants.ELECTRICITY.SCHAFFHAUSEN_FACTOR * energyConsumption;
                break;
            case "Sankt Gallen":
                returnVal = Constants.ELECTRICITY.ST_GALLEN_FACTOR * energyConsumption;
                break;
            case "Schaan": //lichtenstein
            case "Balzers":
            case "Ruggell":
            case "Triesen":
                returnVal = Constants.ELECTRICITY.LICHTENSTEIN_FACTOR * energyConsumption;
                break;
            default:
                returnVal = calculateTotalEnergyConsumption(time, mb) * KHW_TO_CO2_CONVERSION_VALUE;
        }

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


}

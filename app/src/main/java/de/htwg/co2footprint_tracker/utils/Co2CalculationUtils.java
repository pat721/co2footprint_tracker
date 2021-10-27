package de.htwg.co2footprint_tracker.utils;

import android.util.Log;

import de.htwg.co2footprint_tracker.helpers.IpccTableHelper;

public class Co2CalculationUtils {
    private static final double ENERGY_IMPACT_SMARTPHONE = 0.000107689;

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

        return returnVal * 1000;
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

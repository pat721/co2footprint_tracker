package de.htwg.co2footprint_tracker.utils;

public class Co2CalculatorHelper {


    private static final double ENERGY_IMPACT_SMARTPHONE = 0.000107689;
    private static final double AVERAGE_CONSUMPTION_ACCESS_NETWORK_AND_CPE = 52;
    private static final double AVERAGE_ENGERY_INTESITY_OF_LONG_HAUL_AND_METRO = 0.052;
    private static final double ELECTRICITY_CONSUMPTION_BY_DATACENTERS = 0.072;


    /**
     * @param time: minutes
     * @param gigabyte: gb
     * @return energy consumption in kWh
     */
    public double calculateTotalEnergyConsumption(double time, double gigabyte) {
        double total;
        total = calculateSmartphoneEnergyConsumption(time);
        total += calculateInternetEnergyConsumption(gigabyte);
        total += calculateDataCenterEnergyConsumption(time);
        total += calculateDslamAndCpeEnergyConsumption();

        return total;
    }


    /**
     * @param time:  minutes
     * @param bytes: bytes
     * @return energy consumption in kWh
     */
    public double calculateTotalEnergyConsumption(double time, long bytes) {
        return calculateTotalEnergyConsumption(time, bytesToGb(bytes));
    }

    public double calculateSmartphoneEnergyConsumption(double time) {
        return time * ENERGY_IMPACT_SMARTPHONE;
    }

    public double calculateInternetEnergyConsumption(double gigabyte) {
        return gigabyte * AVERAGE_ENGERY_INTESITY_OF_LONG_HAUL_AND_METRO;
    }

    public double calculateDataCenterEnergyConsumption(double time) {
        return time * ELECTRICITY_CONSUMPTION_BY_DATACENTERS;
    }

    public double calculateDslamAndCpeEnergyConsumption() {
        return AVERAGE_CONSUMPTION_ACCESS_NETWORK_AND_CPE / 1000;
    }

    private double bytesToGb(long bytes) {
        return bytes / 1024.0 / 1024.0 / 1024.0;
    }

}

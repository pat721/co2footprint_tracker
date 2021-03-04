package de.htwg.co2footprint_tracker.utils;

public class Co2CalculatorHelper {


    public static final double ENERGY_IMPACT_SMARTPHONE = 0.000107689;
    public static final double AVERAGE_CONSUMPTION_ACCESS_NETWORK_AND_CPE = 52;
    public static final double AVERAGE_ENGERY_INTESITY_OF_LONG_HAUL_AND_METRO = 0.052;
    public static final double ELECTRICITY_CONSUMPTION_BY_DATACENTERS = 0.072;


    /**
     * @param time: minutes
     * @param gigabyte: gb
     * @return energy consumption in kWh
     * */
    public double calculateTotalEnergyConsumption(double time, double gigabyte) {

        double total;

        total = calculateSmartphoneEnergyConsumption(time);
        total += calculateInternetEnergyConsumption(gigabyte);
        total += calculateDataCenterEnergyConsumption(time);
        total += calculateDslamAndCpeEnergyConsumption();

        return total;
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

}

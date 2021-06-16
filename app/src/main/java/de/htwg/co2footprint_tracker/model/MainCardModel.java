package de.htwg.co2footprint_tracker.model;

import de.htwg.co2footprint_tracker.utils.UnitUtils;

public class MainCardModel {

    private long totalReceivedBytes;
    private double totalEnergyConsumption;

    public MainCardModel(long totalReceivedBytes, double totalEnergyConsumption) {
        this.totalReceivedBytes = totalReceivedBytes;
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public String getTotalReceivedBytes() {
        return UnitUtils.humanReadableByteCountSI(totalReceivedBytes);
    }

    public String getTotalEnergyConsumption() {
        return totalEnergyConsumption + " g";
    }
}

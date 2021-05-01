package de.htwg.co2footprint_tracker.model;

import lombok.Data;

@Data
public class MainCardModel {

    private long totalReceivedBytes;
    private long totalEnergyConsumption;

    public MainCardModel(long totalReceivedBytes, long totalEnergyConsumption) {
        this.totalReceivedBytes = totalReceivedBytes;
        this.totalEnergyConsumption = totalEnergyConsumption;
    }
}

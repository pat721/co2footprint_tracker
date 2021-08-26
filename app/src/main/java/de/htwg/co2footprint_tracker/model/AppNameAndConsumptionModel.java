package de.htwg.co2footprint_tracker.model;

public class AppNameAndConsumptionModel {

    private String name;
    private double energyConsumption;

    public AppNameAndConsumptionModel() {
        //default constructor, nop
    }

    public AppNameAndConsumptionModel(double energyConsumption, String name) {
        this.name = name;
        this.energyConsumption = energyConsumption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }
}

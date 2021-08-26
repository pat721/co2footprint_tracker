package de.htwg.co2footprint_tracker.model;

import android.graphics.drawable.Drawable;

import de.htwg.co2footprint_tracker.utils.UnitUtils;

public class Consumer {

    private Drawable logo;
    private String name;
    private double energyConsumption;

    public Consumer(double energyConsumption, String name, Drawable logo) {
        this.logo = logo;
        this.name = name;
        this.energyConsumption = energyConsumption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnergyConsumption() {
        return UnitUtils.RoundTo2Decimals(energyConsumption) + " g co2";
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }
}

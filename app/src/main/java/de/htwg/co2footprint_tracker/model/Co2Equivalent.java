package de.htwg.co2footprint_tracker.model;

import android.graphics.drawable.Drawable;

import lombok.Data;

public class Co2Equivalent {
    public Co2Equivalent(String value, String label, Drawable image) {
        this.value = value;
        this.label = label;
        this.image = image;
    }

    private String value;
    private String label;
    private Drawable image;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}

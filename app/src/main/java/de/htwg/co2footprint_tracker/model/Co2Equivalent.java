package de.htwg.co2footprint_tracker.model;

import java.util.ArrayList;
import java.util.List;

import de.htwg.co2footprint_tracker.R;

public class Co2Equivalent {
    private String value;
    private String label;
    private int image;
    public Co2Equivalent(String value, String label, int image) {
        this.value = value;
        this.label = label;
        this.image = image;
    }

    public static List<Co2Equivalent> getEquivalents() {
        List<Co2Equivalent> co2Equivalents = new ArrayList<>();
        co2Equivalents.add(new Co2Equivalent("1.01", "km driven", R.drawable.ic_signal_wifi_3_bar_black_24dp));
        co2Equivalents.add(new Co2Equivalent("1.55", "km flown", R.drawable.baseline_sync_alt_black_24dp));
        co2Equivalents.add(new Co2Equivalent("10.75", "hours lit", R.drawable.baseline_eco_black_24dp));
        co2Equivalents.add(new Co2Equivalent("12.75", "hours netflix", R.drawable.outline_settings_24));

        return co2Equivalents;
    }

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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

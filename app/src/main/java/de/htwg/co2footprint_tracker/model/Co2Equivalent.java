package de.htwg.co2footprint_tracker.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.utils.UnitUtils;

public class Co2Equivalent {
    private String value;
    private String label;
    private String description;
    private String co2;
    private int image;

    public Co2Equivalent(String value, String label, String description, String co2, int image) {
        this.value = value;
        this.label = label;
        this.description = description;
        this.co2 = co2;
        this.image = image;
    }

    public static List<Co2Equivalent> getEquivalents(Context ctx) {

        double co2 = 0;
        if (ctx != null) {
            DatabaseHelper db = DatabaseHelper.getInstance(ctx);
            co2 = db.getTotalEnergyConsumption();
        }

        //TODO get consuption for today and total toggle

        List<Co2Equivalent> co2Equivalents = new ArrayList<>();
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 139), "km driven", "The use of your Smartphone generated %s g of CO2 in total. With this amount of produced CO2 you could have driven %s km by car.", UnitUtils.RoundTo2Decimals(co2), R.drawable.car));
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 201), "km flown", "The use of your Smartphone generated %s g of CO2 in total. With this amount of produced CO2 you could have flown %s km by plane.", UnitUtils.RoundTo2Decimals(co2), R.drawable.plane));
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 20), "hours lit", "The use of your Smartphone generated %s g of CO2 in total. With this amount of produced CO2 you could have lit %s a 40 Watt light bulb.", UnitUtils.RoundTo2Decimals(co2), R.drawable.bulb));
        //https://www.livemint.com/news/world/your-netflix-habit-has-a-carbon-footprint-but-not-a-big-one-11623368952984.html
        // 1h netfix ~ 100g co2
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 100), "hours netflix", "The use of your Smartphone generated %s g of CO2 in total. With this amount of produced CO2 you could have enjoyed %s hours of netflix and chill.", UnitUtils.RoundTo2Decimals(co2), R.drawable.netflix));
        // avg g CO2e/kWh in EU 2019: 255
        // avg smartphone battery capacity 2500 mAh, 3,7V = 0,00925 kWh
        // 0,00925 kWh * 255 g CO2e/kWh = 2,35875
        // X g CO2e / 2,35875 g CO2e = X Times
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 2.35875), "times charged", "The use of your Smartphone generated %s g of CO2 in total. You could have charged your smartphone %s times and produce the same amount of CO2.", UnitUtils.RoundTo2Decimals(co2), R.drawable.charge));
        //1 baum nimmt 200k g co2 auf
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 200000), "trees planted", "The use of your Smartphone generated %s g of CO2 in total. You would need to plant %s trees to compensate this amount of CO2.", UnitUtils.RoundTo2Decimals(co2), R.drawable.tree));

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

    private static String buildCo2String(double co2, double quotient) {
        return UnitUtils.RoundTo2Decimals(co2 / quotient);
    }

    public String getDescription() {
        return String.format(description, co2, value);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCo2() {
        return co2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }
}

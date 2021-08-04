package de.htwg.co2footprint_tracker.model;

import java.util.ArrayList;
import java.util.List;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.utils.UnitUtils;

public class Co2Equivalent {
    private String value;
    private String label;
    private String description;
    private int image;

    public Co2Equivalent(String value, String label, String description, int image) {
        this.value = value;
        this.label = label;
        this.description = description;
        this.image = image;
    }

    public static List<Co2Equivalent> getEquivalents() {

        DatabaseHelper db = DatabaseHelper.getInstanceObject();
        double co2 = 0;

        if (db != null){
             co2 = db.getTotalEnergyConsumption();
        }

        //TODO get consuption for today and total toggle

        List<Co2Equivalent> co2Equivalents = new ArrayList<>();
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 139), "km driven", "The use of your Smartphone generated 214.91 g of CO2 today. With this amount of produced CO2 you could have traveled 0.45 km by car.", R.drawable.car));
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 201), "km flown","The use of your Smartphone generated 214.91 g of CO2 today. With this amount of produced CO2 you could have traveled 0.45 km by car.", R.drawable.plane));
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 20), "hours lit","The use of your Smartphone generated 214.91 g of CO2 today. With this amount of produced CO2 you could have traveled 0.45 km by car.", R.drawable.bulb));
        //https://www.livemint.com/news/world/your-netflix-habit-has-a-carbon-footprint-but-not-a-big-one-11623368952984.html
        // 1h netfix ~ 100g co2
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 100), "hours netflix","The use of your Smartphone generated 214.91 g of CO2 today. With this amount of produced CO2 you could have traveled 0.45 km by car.", R.drawable.netflix));
        // avg g CO2e/kWh in EU 2019: 255
        // 0,00925 kWh * 255 g CO2e/kWh = 2,35875
        // X g CO2e / 2,35875 g CO2e = X Times
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 2.35875), "times charged","The use of your Smartphone generated 214.91 g of CO2 today. With this amount of produced CO2 you could have traveled 0.45 km by car.", R.drawable.charge));
        //1 baum nimmt 200k g co2 auf
        co2Equivalents.add(new Co2Equivalent(buildCo2String(co2, 200000), "trees planted","The use of your Smartphone generated 214.91 g of CO2 today. With this amount of produced CO2 you could have traveled 0.45 km by car.", R.drawable.tree));

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

    private static String buildCo2String(double co2, double quotient){
        return UnitUtils.RoundTo2Decimals(co2 / quotient);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

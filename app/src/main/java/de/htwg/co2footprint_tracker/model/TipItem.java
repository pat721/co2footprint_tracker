package de.htwg.co2footprint_tracker.model;

import java.util.ArrayList;
import java.util.List;

import de.htwg.co2footprint_tracker.R;

public class TipItem {
    private String title;
    private String description;

    public TipItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

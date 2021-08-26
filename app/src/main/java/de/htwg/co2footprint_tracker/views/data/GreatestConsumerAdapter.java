package de.htwg.co2footprint_tracker.views.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.model.Consumer;

public class GreatestConsumerAdapter extends ArrayAdapter<Consumer> {

    public GreatestConsumerAdapter(Context context, ArrayList<Consumer> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Consumer consumer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.greatest_consumer_list, parent, false);
        }

        ImageView ivLogo = (ImageView) convertView.findViewById(R.id.icon);
        TextView tbName = (TextView) convertView.findViewById(R.id.name);
        TextView tvConsumption = (TextView) convertView.findViewById(R.id.consumption);

        // Populate the data into the template view using the data object

        ivLogo.setImageDrawable(consumer.getLogo());
        tbName.setText(consumer.getName());
        tvConsumption.setText(consumer.getEnergyConsumption());

        return convertView;

    }

}
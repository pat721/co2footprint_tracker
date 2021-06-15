package de.htwg.co2footprint_tracker.views.tips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.model.TipItem;
import lombok.Getter;


public class TipsRecyclerViewAdapter extends RecyclerView.Adapter<TipItemViewHolder> {

    @Getter
    private ArrayList<TipItem> items;
    private TipItemViewHolder.ClickListener listener;

    public TipsRecyclerViewAdapter(TipItemViewHolder.ClickListener listener, ArrayList<TipItem> items) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TipItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tip, parent, false);
        return new TipItemViewHolder(view, this.listener);
    }

    /**
     * Sets the new items to the recycler view and triggers the UI refresh.
     */
    public void setItems(ArrayList<TipItem> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    /**
     * Returns an item at a selected position.
     */
    public TipItem getItemAtPosition(int position) {
        return this.items.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TipItemViewHolder holder, int position) {
        holder.renderItem(this.getItemAtPosition(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
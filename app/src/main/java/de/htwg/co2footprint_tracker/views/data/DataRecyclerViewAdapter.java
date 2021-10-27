package de.htwg.co2footprint_tracker.views.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.htwg.co2footprint_tracker.databinding.Co2EquivalenceCardBinding;
import de.htwg.co2footprint_tracker.model.Co2Equivalent;

public class DataRecyclerViewAdapter extends RecyclerView.Adapter<DataRecyclerViewAdapter.ViewHolder> {

    private final List<Co2Equivalent> co2Equivalents;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    DataRecyclerViewAdapter(Context context, List<Co2Equivalent> co2Equivalents) {
        this.mInflater = LayoutInflater.from(context);
        this.co2Equivalents = co2Equivalents;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        Co2EquivalenceCardBinding binding = Co2EquivalenceCardBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Co2Equivalent co2Equivalent = co2Equivalents.get(position);
        holder.bind(co2Equivalent);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return co2Equivalents.size();
    }

    // convenience method for getting data at click position
    public Co2Equivalent getItem(int id) {
        return co2Equivalents.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Co2EquivalenceCardBinding binding;
        CardView view;

        ViewHolder(Co2EquivalenceCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Co2Equivalent co2Equivalent) {
            binding.setCo2equivalent(co2Equivalent);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getBindingAdapterPosition());
        }
    }
}
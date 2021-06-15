package de.htwg.co2footprint_tracker.views.tips;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Locale;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.model.TipItem;

/**
 * Holds the view for a history item.
 */
public class TipItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ClickListener listener;
    private View container;

    /**
     * Constructor that creates a new instance of the current class.
     */
    public TipItemViewHolder(
            @NonNull View itemView,
            @NonNull ClickListener listener
    ) {
        super(itemView);
        this.listener = listener;

        this.container = itemView.findViewById(R.id.itemContainer);
        this.container.setOnClickListener(this);
    }

    /**
     * Renders the item.
     */
    public void renderItem(TipItem item) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

      /*  ((AppCompatTextView) this.container.findViewById(R.id.timestamp))
                .setText(dateFormat.format(new Date(Long.parseLong(item.getDateCreated()))));

        ((AppCompatTextView) this.container.findViewById(R.id.title))
                .setText(this.container.getContext().getString(R.string.history_sequential_id, item.getSequentialId()));*/
    }

    @Override
    public void onClick(View v) {
        if (this.listener != null) {
            this.listener.onItemClicked(getAdapterPosition());
        }
    }

    public interface ClickListener {
        void onItemClicked(int position);
    }
}

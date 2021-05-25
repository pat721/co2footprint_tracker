package de.htwg.co2footprint_tracker.views.tips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.databinding.FragmentTipBinding;
import de.htwg.co2footprint_tracker.model.TipItem;

/**
 * A fragment representing a list of Items.
 */
public class TipsFragment extends Fragment implements TipItemViewHolder.ClickListener {

    private FragmentTipBinding binding;
    private TipsRecyclerViewAdapter adapter;

    /**
     * Returns a new instance of the tips fragment.
     */
    public static TipsFragment getInstance() {
        return new TipsFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tip,
                container,
                false
        );

        this.adapter = new TipsRecyclerViewAdapter(this, new ArrayList<>());
        this.binding.recyclerView.setAdapter(this.adapter);

        this.loadTips();
        return binding.getRoot();
    }

    private void loadTips() {


        // TODO: Load Tips from cloud or so
        // then => renderTips with the fetched tips
    }

    private void renderTips(ArrayList<TipItem> tipItems) {
        this.adapter.setItems(tipItems);
    }

    @Override
    public void onItemClicked(int position) {
        // mach was
    }
}
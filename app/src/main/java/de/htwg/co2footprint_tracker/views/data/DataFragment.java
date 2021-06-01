package de.htwg.co2footprint_tracker.views.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.databinding.FragmentDataBinding;
import de.htwg.co2footprint_tracker.helpers.PackageHelper;
import de.htwg.co2footprint_tracker.model.Co2Equivalent;
import de.htwg.co2footprint_tracker.model.MainCardModel;

public class DataFragment extends Fragment {
    
    /**
     * Returns a new instance of the tips fragment.
     */
    public static DataFragment getInstance() {
        return new DataFragment();
    }

    FragmentDataBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_data,
                container,
                false
        );
        binding.co2EquivalentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        DataRecyclerViewAdapter dataRecyclerViewAdapter = new DataRecyclerViewAdapter(getContext(), Co2Equivalent.getEquivalents());
        binding.co2EquivalentList.setAdapter(dataRecyclerViewAdapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        HashSet<Integer> applicationUids = PackageHelper.getApplicationUids(getContext());
        final long totalReceivedBytes = databaseHelper.getTotalReceivedBytes(applicationUids);
        final double totalEnergyConsumption = databaseHelper.getTotalEnergyConsumption(applicationUids);
        binding.setMainCardModel(new MainCardModel(totalReceivedBytes, totalEnergyConsumption));
        super.onViewCreated(view, savedInstanceState);
    }
}
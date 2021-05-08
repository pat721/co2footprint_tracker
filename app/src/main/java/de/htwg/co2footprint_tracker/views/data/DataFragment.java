package de.htwg.co2footprint_tracker.views.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.HashSet;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.databinding.FragmentDataBinding;
import de.htwg.co2footprint_tracker.helpers.PackageHelper;
import de.htwg.co2footprint_tracker.model.MainCardModel;

public class DataFragment extends Fragment {

    /**
     * Returns a new instance of the tips fragment.
     */
    public static DataFragment getInstance() {
        return new DataFragment();
    }


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        FragmentDataBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_data,
                container,
                false
        );

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        HashSet<Integer> applicationUids = PackageHelper.getApplicationUids(getContext());
        final long totalReceivedBytes = databaseHelper.getTotalReceivedBytes(applicationUids);
        final double totalEnergyConsumption = databaseHelper.getTotalEnergyConsumption(applicationUids);
        binding.setMainCardModel(new MainCardModel(totalReceivedBytes, totalEnergyConsumption));

        return binding.getRoot();
    }
}
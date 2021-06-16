package de.htwg.co2footprint_tracker.views.data;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.HashSet;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.databinding.FragmentDataBinding;
import de.htwg.co2footprint_tracker.helpers.PackageHelper;
import de.htwg.co2footprint_tracker.model.Co2Equivalent;
import de.htwg.co2footprint_tracker.model.MainCardModel;
import de.htwg.co2footprint_tracker.utils.Constants;

public class DataFragment extends Fragment {

    FragmentDataBinding binding;
    DatabaseHelper databaseHelper;

    /**
     * Returns a new instance of the data fragment.
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
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_data,
                container,
                false
        );
        binding.co2EquivalentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        DataRecyclerViewAdapter dataRecyclerViewAdapter = new DataRecyclerViewAdapter(getContext(), Co2Equivalent.getEquivalents());
        binding.co2EquivalentList.setAdapter(dataRecyclerViewAdapter);

        binding.modeToggle.toggle.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.today_toggle:
                    Log.e(Constants.LOG.TAG, "Toggle switched to: today");
                    bindTodayData();
                    break;
                case R.id.total_toggle:
                    Log.e(Constants.LOG.TAG, "Toggle switched to: total");
                    bindTotalData();
                    break;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        databaseHelper = DatabaseHelper.getInstance(getContext());
        bindTodayData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindTodayData() {
        final long todayReceivedBytes = databaseHelper.getReceivedBytesForToday();
        final double todayEnergyConsumption = databaseHelper.getEnergyConsumptionForToday();
        binding.setMainCardModel(new MainCardModel(todayReceivedBytes, todayEnergyConsumption));
    }

    private void bindTotalData() {
        final long totalReceivedBytes = databaseHelper.getTotalReceivedBytes();
        final double totalEnergyConsumption = databaseHelper.getTotalEnergyConsumption();
        binding.setMainCardModel(new MainCardModel(totalReceivedBytes, totalEnergyConsumption));
    }
}
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

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import de.htwg.co2footprint_tracker.MainActivity;
import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.databinding.EquivalentBottomSheetBinding;
import de.htwg.co2footprint_tracker.databinding.FragmentDataBinding;
import de.htwg.co2footprint_tracker.helpers.PreferenceManagerHelper;
import de.htwg.co2footprint_tracker.helpers.TrackingHelper;
import de.htwg.co2footprint_tracker.helpers.UiHelper;
import de.htwg.co2footprint_tracker.model.Co2Equivalent;
import de.htwg.co2footprint_tracker.model.Consumer;
import de.htwg.co2footprint_tracker.model.MainCardModel;
import de.htwg.co2footprint_tracker.utils.Constants;

public class DataFragment extends Fragment {

    FragmentDataBinding binding;
    DatabaseHelper databaseHelper;
    List<Co2Equivalent> co2Equivalents;
    ViewGroup container;

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


        this.container = container; //a little hack to be able to access the container on refresh

        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        co2Equivalents = Co2Equivalent.getEquivalents(db.getTotalEnergyConsumption()); //create initial list to prevent crashing


//        ----------------------

        ArrayList<Consumer> consumers = db.getTopConsumingApps(getContext());

        if(consumers.size() > 2){
            binding.first.setConsumer(consumers.get(0));
            binding.second.setConsumer(consumers.get(1));
            binding.third.setConsumer(consumers.get(2));
        }
 //       GreatestConsumerAdapter adapter = new GreatestConsumerAdapter(getContext(), consumers);
//        binding.topConsumerList.setAdapter(adapter);


//        ----------------------
        DataRecyclerViewAdapter dataRecyclerViewAdapter = getDataRecyclerViewAdapter(container);
        binding.co2EquivalentList.setAdapter(dataRecyclerViewAdapter);

        binding.modeToggle.toggle.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.today_toggle:
                    Log.e(Constants.LOG.TAG, "Toggle switched to: today");
                    bindTodayData();
                    bindTodayList();
                    break;
                case R.id.total_toggle:
                    Log.e(Constants.LOG.TAG, "Toggle switched to: total");
                    bindTotalData();
                    bindTotalList();
                    break;
            }
        });

        UiHelper.getInstance().changeStartStopButtonAccordingToCurrentState(binding.startStopButton, getActivity());
        binding.startStopButton.setOnClickListener((v) -> {

            TrackingHelper th = TrackingHelper.getInstance();
            if (PreferenceManagerHelper.getStartTime(getActivity().getApplicationContext()) == 0L) {
                //th.startTracking(getContext());
                MainActivity.getWeakInstanceActivity().startTracking();
            } else {
                //th.stopTracking(getContext());
                MainActivity.getWeakInstanceActivity().stopTracking();
            }
            UiHelper.getInstance().changeStartStopButtonAccordingToCurrentState(binding.startStopButton, getActivity());
        });


        return binding.getRoot();
    }


    @NonNull
    private DataRecyclerViewAdapter getDataRecyclerViewAdapter(@Nullable ViewGroup container) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        binding.co2EquivalentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        DataRecyclerViewAdapter dataRecyclerViewAdapter = new DataRecyclerViewAdapter(getContext(), co2Equivalents);
        dataRecyclerViewAdapter.setClickListener((view, position) -> {

            EquivalentBottomSheetBinding equivalentBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.equivalent_bottom_sheet, container, false);
            equivalentBinding.setCo2Equivalent(co2Equivalents.get(position));

            sheetDialog.setContentView(equivalentBinding.getRoot());
            sheetDialog.show();
        });
        return dataRecyclerViewAdapter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        databaseHelper = DatabaseHelper.getInstance(getContext());
        bindTodayData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindTodayData() {
        final long todayReceivedBytes = databaseHelper.getTotalBytesForToday();
        final double todayEnergyConsumption = databaseHelper.getEnergyConsumptionForToday();
        binding.setMainCardModel(new MainCardModel(todayReceivedBytes, todayEnergyConsumption));

    }

    private void bindTotalData() {
        final long totalReceivedBytes = databaseHelper.getTotalReceivedBytes();
        final double totalEnergyConsumption = databaseHelper.getTotalEnergyConsumption();
        binding.setMainCardModel(new MainCardModel(totalReceivedBytes, totalEnergyConsumption));
    }

    private void bindTodayList() {
        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        co2Equivalents = Co2Equivalent.getEquivalents(db.getEnergyConsumptionForToday());
        binding.co2EquivalentList.setAdapter(getDataRecyclerViewAdapter(container));
    }


    private void bindTotalList() {
        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        co2Equivalents = Co2Equivalent.getEquivalents(db.getTotalEnergyConsumption());
        binding.co2EquivalentList.setAdapter(getDataRecyclerViewAdapter(container));
    }
}
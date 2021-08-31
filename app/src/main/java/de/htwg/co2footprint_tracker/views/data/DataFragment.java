package de.htwg.co2footprint_tracker.views.data;

import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.NO_START_TIME_SET;
import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.TOGGLE_STATE_NOT_SET;
import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.TOGGLE_STATE_TODAY;
import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.TOGGLE_STATE_TOTAL;

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

import java.util.List;

import de.htwg.co2footprint_tracker.MainActivity;
import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.databinding.EquivalentBottomSheetBinding;
import de.htwg.co2footprint_tracker.databinding.FragmentDataBinding;
import de.htwg.co2footprint_tracker.helpers.PreferenceManagerHelper;
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
        databaseHelper = DatabaseHelper.getInstance(getActivity());
        co2Equivalents = Co2Equivalent.getEquivalents(databaseHelper.getTotalEnergyConsumption()); //create initial list to prevent crashing

        if (PreferenceManagerHelper.getTodayTotalToggleState(getContext()) == TOGGLE_STATE_NOT_SET) {
            PreferenceManagerHelper.setTodayTotalToggleState(getContext(), TOGGLE_STATE_TODAY);
        }

        DataRecyclerViewAdapter dataRecyclerViewAdapter = getDataRecyclerViewAdapter(container);
        binding.co2EquivalentList.setAdapter(dataRecyclerViewAdapter);

        binding.modeToggle.toggle.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.today_toggle:
                    PreferenceManagerHelper.setTodayTotalToggleState(getContext(), TOGGLE_STATE_TODAY);
                    Log.e(Constants.LOG.TAG, "Toggle switched to: today");
                    break;
                case R.id.total_toggle:
                    PreferenceManagerHelper.setTodayTotalToggleState(getContext(), TOGGLE_STATE_TOTAL);
                    Log.e(Constants.LOG.TAG, "Toggle switched to: total");
                    break;
            }
            bindDataAccordingToToggleStatus();
        });

        UiHelper.getInstance().changeStartStopButtonAccordingToCurrentState(binding.startStopButton, getActivity());
        binding.startStopButton.setOnClickListener((v) -> {
            if (PreferenceManagerHelper.getStartTime(getActivity().getApplicationContext()) == NO_START_TIME_SET) {
                MainActivity.getWeakInstanceActivity().startTracking();
            } else {
                MainActivity.getWeakInstanceActivity().stopTracking();
            }
            UiHelper.getInstance().changeStartStopButtonAccordingToCurrentState(binding.startStopButton, getActivity());
        });

        bindDataAccordingToToggleStatus();
        return binding.getRoot();
    }

    private void bindDataAccordingToToggleStatus() {
        int toggleState = PreferenceManagerHelper.getTodayTotalToggleState(getContext());
        switch (toggleState) {
            case TOGGLE_STATE_TODAY:
                bindTodayData();
                break;
            case TOGGLE_STATE_TOTAL:
                bindTotalData();
                break;
        }
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
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindTodayData() {
        //Set total toggle checked to prevent ui bugs on refresh
        binding.modeToggle.todayToggle.setChecked(true);

        //Bind data usage & consumption
        final long todayReceivedBytes = databaseHelper.getTotalBytesForToday();
        final double todayEnergyConsumption = databaseHelper.getEnergyConsumptionForToday();
        binding.setMainCardModel(new MainCardModel(todayReceivedBytes, todayEnergyConsumption));

        //Bind top consumers list
        List<Consumer> consumers = databaseHelper.getTopConsumingAppsForToday(getContext());
        if (consumers.size() > 2) {
            binding.first.setConsumer(consumers.get(0));
            binding.second.setConsumer(consumers.get(1));
            binding.third.setConsumer(consumers.get(2));
        }

        //Bind Equals-List
        co2Equivalents = Co2Equivalent.getEquivalents(databaseHelper.getEnergyConsumptionForToday());
        binding.co2EquivalentList.setAdapter(getDataRecyclerViewAdapter(container));
    }

    private void bindTotalData() {
        //Set total toggle checked to prevent ui bugs on refresh
        binding.modeToggle.totalToggle.setChecked(true);

        //Bind data usage & consumption
        final long totalReceivedBytes = databaseHelper.getTotalReceivedBytes();
        final double totalEnergyConsumption = databaseHelper.getTotalEnergyConsumption();
        binding.setMainCardModel(new MainCardModel(totalReceivedBytes, totalEnergyConsumption));

        //Bind top consumers list
        List<Consumer> consumers = databaseHelper.getTopConsumingApps(getContext());
        if (consumers.size() > 2) {
            binding.first.setConsumer(consumers.get(0));
            binding.second.setConsumer(consumers.get(1));
            binding.third.setConsumer(consumers.get(2));
        }

        //Bind Equals-List
        co2Equivalents = Co2Equivalent.getEquivalents(databaseHelper.getTotalEnergyConsumption());
        binding.co2EquivalentList.setAdapter(getDataRecyclerViewAdapter(container));
    }
}
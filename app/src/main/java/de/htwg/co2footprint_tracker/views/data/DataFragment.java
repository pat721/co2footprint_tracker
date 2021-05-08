package de.htwg.co2footprint_tracker.views.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.databinding.FragmentDataBinding;

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
        return binding.getRoot();
    }
}
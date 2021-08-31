package de.htwg.co2footprint_tracker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import de.htwg.co2footprint_tracker.databinding.DataProtectionBottomSheetBinding;
import de.htwg.co2footprint_tracker.databinding.FragmentAboutBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    FragmentAboutBinding binding;


    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of the about fragment.
     */
    public static AboutFragment getInstance() {
        return new AboutFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                R.layout.fragment_about,
                container,
                false
        );

        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());

        sheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
            // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
            FrameLayout bottomSheet =  dialogc.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });


        String privacyPolicy = FirebaseRemoteConfig.getInstance().getString("privacy_policy");

        binding.licenseButton.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), OssLicensesMenuActivity.class));
        });

        binding.dataProtectionButton.setOnClickListener(view -> {
            DataProtectionBottomSheetBinding bottomSheetBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.data_protection_bottom_sheet, container, false);
            bottomSheetBinding.dataProtection.setText(privacyPolicy);

            sheetDialog.setContentView(bottomSheetBinding.getRoot());
            sheetDialog.show();
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}
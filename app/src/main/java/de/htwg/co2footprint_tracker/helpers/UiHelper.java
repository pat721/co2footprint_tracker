package de.htwg.co2footprint_tracker.helpers;

import android.app.Activity;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import de.htwg.co2footprint_tracker.R;

public class UiHelper {


    private static UiHelper uiHelper;

    public static UiHelper getInstance() {
        if (uiHelper == null) {
            uiHelper = new UiHelper();
        }
        return uiHelper;
    }

    public void changeStartStopButtonAccordingToCurrentState(Button button, Activity activity) {
        if (PreferenceManagerHelper.getStartTime(activity.getApplicationContext()) == 0L) {
            button.setText("Tap to start tracking");
            button.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.test_stopped_background));
        } else {
            button.setText("Tracking is active!");
            button.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.test_running_background));
        }
    }

}

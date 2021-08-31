package de.htwg.co2footprint_tracker.helpers;

import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.NO_START_TIME_SET;

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
        if (PreferenceManagerHelper.getStartTime(activity.getApplicationContext()) == NO_START_TIME_SET) {
            button.setText(R.string.tap_to_start_tracking);
            button.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.test_stopped_background));
        } else {
            button.setText(R.string.tracking_active_button_text);
            button.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.test_running_background));
        }
    }

}

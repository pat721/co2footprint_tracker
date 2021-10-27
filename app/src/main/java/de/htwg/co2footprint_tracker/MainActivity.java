package de.htwg.co2footprint_tracker;

import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.DEVICE_TYPE_NOT_SET;
import static de.htwg.co2footprint_tracker.utils.Constants.PERSISTENCY.NO_START_TIME_SET;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.lang.ref.WeakReference;

import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.databinding.ActivityMainBinding;
import de.htwg.co2footprint_tracker.helpers.LocationHelper;
import de.htwg.co2footprint_tracker.helpers.PermissionHelper;
import de.htwg.co2footprint_tracker.helpers.PreferenceManagerHelper;
import de.htwg.co2footprint_tracker.model.InitialBucketContainer;
import de.htwg.co2footprint_tracker.services.NetworkStatsExecutorService;
import de.htwg.co2footprint_tracker.utils.Constants;
import de.htwg.co2footprint_tracker.views.data.DataFragment;
import de.htwg.co2footprint_tracker.views.tips.TipsFragment;

public class MainActivity extends AppCompatActivity {

    private static WeakReference<MainActivity> weakActivity;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean isInForeground = false;
    private ActivityMainBinding binding;

    public static MainActivity getWeakInstanceActivity() {
        return weakActivity.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        weakActivity = new WeakReference<>(MainActivity.this);
        isInForeground = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        this.binding.bottomNavigation.setSelectedItemId(R.id.data);
        navigateToFragment(DataFragment.getInstance(), false);

        //TODO rm depricated method? maybe getOnFocusChangeListener can do that
        this.binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {

            int currentlyActiveView = binding.bottomNavigation.getSelectedItemId();

            if (item.getItemId() == R.id.data && currentlyActiveView != R.id.data) {
                navigateToFragment(DataFragment.getInstance(), true);
                return true;
            }

            if (item.getItemId() == R.id.tips && currentlyActiveView != R.id.tips) {
                navigateToFragment(TipsFragment.getInstance(), true);
                return true;
            }

            if (item.getItemId() == R.id.about && currentlyActiveView != R.id.about) {
                navigateToFragment(AboutFragment.getInstance(), true);
                return true;
            }

            return false;
        });

        if (PreferenceManagerHelper.getDeviceType(this) == DEVICE_TYPE_NOT_SET) {
            PreferenceManagerHelper.setDeviceType(this);
        }

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onResume() {
        isInForeground = true;
        getSupportActionBar().hide();
        PermissionHelper ph = PermissionHelper.getInstance();
        ph.processPermissionHandling();
        LocationHelper.getInstance().updateCurrentAdminArea();
        refreshUi();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isInForeground = false;
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();

        if (menuItem == R.id.menu_start_tracking) {
            //if no time is stored there is no test running
            if (PreferenceManagerHelper.getStartTime(this) == NO_START_TIME_SET) {
                startTracking();
            } else {
                Toast.makeText(this, R.string.tracking_already_running, Toast.LENGTH_LONG).show();
                Log.e(Constants.LOG.TAG, "Tried to start test even tho the test is already running!");
            }
            return true;
        } else if (menuItem == R.id.menu_stop_tracking) {
            stopTracking();
        } else if (menuItem == R.id.menu_purge_db) {
            DatabaseHelper.getInstance(this).purgeDB();
            Toast.makeText(this, R.string.clearing_database, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void stopTracking() {
        foregroundServiceAction(Constants.ACTION.STOP_SERVICE);
        PreferenceManagerHelper.clearStoredStartTime(this);
        InitialBucketContainer.setNewRun(true);
        InitialBucketContainer.clearMappedPackageData();
    }

    public void startTracking() {
        PreferenceManagerHelper.setStartTime(this);
        Toast.makeText(this, R.string.tracking_was_started, Toast.LENGTH_LONG).show();
        foregroundServiceAction(Constants.ACTION.START_SERVICE);
    }

    public void navigateToFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment);

        if (addToBackStack) {
            ft.addToBackStack(fragment.getClass().getName());
        }
        ft.commit();
    }

    private void foregroundServiceAction(String action) {

        if (PreferenceManagerHelper.getServiceState(this).equals(Constants.PERSISTENCY.SERVICE_STOPPED)
                && action.equals(Constants.ACTION.STOP_SERVICE)) {
            return;
        }

        Intent foregroundServiceIntent = new Intent(this, NetworkStatsExecutorService.class);
        foregroundServiceIntent.setAction(action);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(foregroundServiceIntent);
        } else {
            startService(foregroundServiceIntent);
        }
        Log.e(Constants.LOG.TAG, "service started with action: '" + action + "'");
    }


    public void refreshUi() {
        if (isInForeground && this.binding.bottomNavigation.getSelectedItemId() == R.id.data) {
            navigateToFragment(DataFragment.getInstance(), false);
        }
    }
}

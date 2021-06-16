package de.htwg.co2footprint_tracker;

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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.databinding.ActivityMainBinding;
import de.htwg.co2footprint_tracker.helpers.LocationHelper;
import de.htwg.co2footprint_tracker.helpers.PermissionHelper;
import de.htwg.co2footprint_tracker.helpers.PreferenceManagerHelper;
import de.htwg.co2footprint_tracker.model.InitialBucketContainer;
import de.htwg.co2footprint_tracker.services.UpdateServiceSchedulerService;
import de.htwg.co2footprint_tracker.utils.Constants;
import de.htwg.co2footprint_tracker.views.data.DataFragment;
import de.htwg.co2footprint_tracker.views.tips.TipsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        this.binding.bottomNavigation.setSelectedItemId(R.id.data);
        navigateToFragment(DataFragment.getInstance());

        this.binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.data:
                        navigateToFragment(DataFragment.getInstance());
                        return true;
                    case R.id.tips:
                        navigateToFragment(TipsFragment.getInstance());
                        return true;
                }
                return false;
            }
        });

        if (PreferenceManagerHelper.getDeviceType(this) == -1) {
            PreferenceManagerHelper.setDeviceType(this);
        }

        LocationHelper.getInstance(MainActivity.this).updateCurrentAdminArea();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();

        PermissionHelper ph = new PermissionHelper(this);
        ph.processPermissionHandling();
        LocationHelper.getInstance(MainActivity.this).updateCurrentAdminArea();
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
            if (PreferenceManagerHelper.getStartTime(this) == 0L) {
                startTracking();
            } else {
                Toast.makeText(this, "Test already running!", Toast.LENGTH_LONG).show();
                Log.e(Constants.LOG.TAG, "Tried to start test even tho the test is already running!");
            }
            return true;
        } else if (menuItem == R.id.menu_stop_tracking) {
            stopTracking();
        } else if (menuItem == R.id.menu_purge_db) {
            DatabaseHelper.getInstance(this).purgeDB();
            Toast.makeText(this, "clearing database", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void stopTracking() {
        stopService(new Intent(this, UpdateServiceSchedulerService.class));
        PreferenceManagerHelper.clearStoredStartTime(this);
        InitialBucketContainer.setNewRun(true);
        InitialBucketContainer.clearMappedPackageData();
    }

    private void startTracking() {
        PreferenceManagerHelper.setStartTime(this);
        Toast.makeText(this, "Tracking started!", Toast.LENGTH_LONG).show();
        Log.e(Constants.LOG.TAG, "creating intent....");
        Intent updateSchedulerIntent = new Intent(this, UpdateServiceSchedulerService.class);
        updateSchedulerIntent.setAction(Constants.ACTION.UPDATE_SERVICE_SCHEDULER);
        Log.e(Constants.LOG.TAG, "intent created, starting service...");
        startService(updateSchedulerIntent);
        Log.e(Constants.LOG.TAG, "service started");
    }

    public void navigateToFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

}

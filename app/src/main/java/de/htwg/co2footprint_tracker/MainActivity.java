package de.htwg.co2footprint_tracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

import de.htwg.co2footprint_tracker.database.DatabaseHelper;
import de.htwg.co2footprint_tracker.helpers.Constants;
import de.htwg.co2footprint_tracker.helpers.TimingHelper;
import de.htwg.co2footprint_tracker.model.InitialBucketContainer;
import de.htwg.co2footprint_tracker.model.Package;
import de.htwg.co2footprint_tracker.services.UpdateServiceSchedulerService;

import static de.htwg.co2footprint_tracker.helpers.StringUtils.humanReadableByteCountSI;
import static de.htwg.co2footprint_tracker.helpers.TimingHelper.getTestDurationInMins;
import static de.htwg.co2footprint_tracker.utils.PackageHelper.getPackagesData;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Package> packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.data);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tips:
                        startActivity(new Intent(getApplicationContext(),
                                Tips.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.data:
                        return true;
                }
                return false;
            }
        });
        packageList = getPackagesData(this);
        updateUi();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();

        if (!hasPermissions()) {
            requestPermissions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_start_test) {
            if (TimingHelper.getIsTestRunning(this)) {
                Toast.makeText(this, "Test is already running.  Please stop current test first", Toast.LENGTH_LONG).show();
            } else {
                TimingHelper.setStartTime(this);
                TimingHelper.setIsTestRunning(this, true);
                Toast.makeText(this, "Started test at " + TimingHelper.getStartTimeForUI(this), Toast.LENGTH_LONG).show();
                Log.e(Constants.LOG.TAG, "creating intent....");
                Intent updateSchedulerIntent = new Intent(this, UpdateServiceSchedulerService.class);
                updateSchedulerIntent.putExtra(Constants.PARAMS.PACKAGE_LIST, packageList);
                updateSchedulerIntent.setAction(Constants.ACTION.UPDATE_SERVICE_SCHEDULER_STARTED);
                Log.e(Constants.LOG.TAG, "intent created, starting service...");
                startService(updateSchedulerIntent);
                Log.e(Constants.LOG.TAG, "service started");
            }
            return true;
        } else if (id == R.id.menu_update_stats) {
            updateUi();
            if (!TimingHelper.getIsTestRunning(this)) {
                Toast.makeText(this, "There is no test running", Toast.LENGTH_LONG).show();
            } else {
                long testDurationInMins = getTestDurationInMins(System.currentTimeMillis(), this);
                if (testDurationInMins <= Constants.MISC.MINIMUM_RECOMMENDED_TEST_TIME)
                    Toast.makeText(this, getString(R.string.minimum_duration_error), Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.menu_stop_test) {
            if (!TimingHelper.getIsTestRunning(this)) {
                Toast.makeText(this, "There is no test running", Toast.LENGTH_LONG).show();
            } else {
                TimingHelper.setIsTestRunning(this, false);
                stopService(new Intent(this, UpdateServiceSchedulerService.class));
                long testDurationInMins = getTestDurationInMins(System.currentTimeMillis(), this);
                if (testDurationInMins < Constants.MISC.MINIMUM_RECOMMENDED_TEST_TIME) {
                    Toast.makeText(this, getString(R.string.minimum_duration_error), Toast.LENGTH_LONG).show();
                }
            }
            InitialBucketContainer.setNewRun(true);
            InitialBucketContainer.clearMappedPackageData();
        } else if (id == R.id.menu_purge_db) {
            DatabaseHelper.getInstance(this).clearDb();
            Toast.makeText(this, "clearing database", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateUi() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);

        long totalReceivedBytes = 0;
        double totalEnergyConsumption = 0;

        for (Package packet : packageList) {
            Cursor cursor = databaseHelper.getTotalsForPackage(packet.getPackageUid());
            if (cursor.moveToFirst()) {
                packet.setReceivedBytesWifi(cursor.getLong(0));
                packet.setReceivedBytesMobile(cursor.getLong(1));

                long receivedBytes = cursor.getLong(2);
                totalReceivedBytes += receivedBytes;
                packet.setReceivedBytesTotal(receivedBytes);

                packet.setReceivedPacketsWifi(cursor.getLong(3));
                packet.setReceivedPacketsMobile(cursor.getLong(4));
                packet.setReceivedPacketsTotal(cursor.getLong(5));

                double energyConsumption = cursor.getDouble(6);
                totalEnergyConsumption += energyConsumption;
                packet.setEnergyConsumption(energyConsumption);
            }
        }
        Collections.sort(packageList);

        final TextView dataUsage = findViewById(R.id.data_usage_value);
        final TextView co2equivalent = findViewById(R.id.co2_equivalent_value);

        co2equivalent.setText(totalEnergyConsumption + " g");
        dataUsage.setText(humanReadableByteCountSI(totalReceivedBytes));
    }

    //////////////////////////////////// Permissions ////////////////////////////////////////////

    private void requestPermissions() {
        if (!hasPermissionToReadNetworkHistory()) {
            return;
        }
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats();
        }
    }

    private boolean hasPermissions() {
        return hasPermissionToReadNetworkHistory() && hasPermissionToReadPhoneStats();
    }

    private boolean hasPermissionToReadPhoneStats() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestPhoneStateStats() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                Constants.PERMISSION.PERMISSION_REQUEST);
    }

    private boolean hasPermissionToReadNetworkHistory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        Toast.makeText(this, "Usage Stats permission is required", Toast.LENGTH_LONG).show();
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                android.os.Process.myUid(), getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        if (getIntent().getExtras() != null) {
                            intent.putExtras(getIntent().getExtras());
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        requestReadNetworkHistoryAccess();
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestReadNetworkHistoryAccess() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }
}

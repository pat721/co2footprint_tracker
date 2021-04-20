package de.htwg.co2footprint_tracker.utils;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.TimeUnit;

public class UiRefresher implements Runnable {

    private RecyclerView.Adapter packageAdapter;

    public UiRefresher(RecyclerView.Adapter packageAdapter) {
        this.packageAdapter = packageAdapter;
    }

    @Override
    public void run() {

        for (int j = 0; j <= 100; j++) {
            packageAdapter.notifyDataSetChanged();
            for (int i = 5; i >= 0; i--) {
                Log.e(Constants.LOG.TAG, "Updating Ui in " + i + " seconds");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

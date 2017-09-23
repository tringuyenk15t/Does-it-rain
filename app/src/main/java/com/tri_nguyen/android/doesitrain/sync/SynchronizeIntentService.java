package com.tri_nguyen.android.doesitrain.sync;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;

import com.tri_nguyen.android.doesitrain.utils.LocationUtils;
import com.tri_nguyen.android.doesitrain.utils.WeatherSyncTask;

/**
 * Created by Tri Nguyen on 9/21/2017.
 */

public class SynchronizeIntentService extends IntentService {

    public SynchronizeIntentService() {
        super("SyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LocationUtils locationUtils = new LocationUtils(getApplicationContext());
        Location location = locationUtils.getLocation();

        WeatherSyncTask.syncWeather(this, location);
    }
}

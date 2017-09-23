package com.tri_nguyen.android.doesitrain.sync;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.tri_nguyen.android.doesitrain.utils.LocationUtils;
import com.tri_nguyen.android.doesitrain.utils.WeatherSyncTask;

/**
 * Created by Tri Nguyen on 9/21/2017.
 */
public class SynchronizeJobService extends JobService{
    private static final String TAG = SynchronizeJobService.class.getSimpleName();
    private AsyncTask mAsyncTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        LocationUtils locationUtils = new LocationUtils(getApplicationContext());
        final Location location = locationUtils.getLocation();

        mAsyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                WeatherSyncTask.syncWeather(getApplicationContext(), location);
                jobFinished(job, false);
                Log.e(TAG, " Job has been executed!");
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        mAsyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mAsyncTask != null){
            mAsyncTask.cancel(true);
        }
        //retry if conditions are met again
        return true;
    }
}


package com.tri_nguyen.android.doesitrain.sync;

import android.content.Context;
import android.content.Intent;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.tri_nguyen.android.doesitrain.data.DaoMaster;
import com.tri_nguyen.android.doesitrain.data.WeatherDpHelper;
import com.tri_nguyen.android.doesitrain.data.WeatherInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tri Nguyen on 9/21/2017.
 */

public class SynchronizeUtils {

    public static final int FETCHING_INTERVAL_HOURS = 4;
    public static final int FETCHING_INTERVAL_SECONDS =  (int) TimeUnit.HOURS.toSeconds(FETCHING_INTERVAL_HOURS);
    public static final int FETCHING_FLEXTIME_SECOND = FETCHING_INTERVAL_SECONDS / 3;

    // unique tag to identify job
    public static final String FETCHING_JOB_TAG = "fetching_weather_tag";
    //keep track of what the job has started on.
    private static boolean sInitialized;

    /**
     * Schedules a repeating sync weather data using FirebaseJobDispatcher
     * @param context
     */
     public static void scheduleWeatherFetching(Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job job = dispatcher.newJobBuilder()
                .setService(SynchronizeJobService.class)
                .setTag(FETCHING_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(FETCHING_INTERVAL_SECONDS,
                        FETCHING_INTERVAL_SECONDS + FETCHING_FLEXTIME_SECOND ))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(job);
    }

    /**
     *
     * @param context
     */
    synchronized public static void initializeWeatherData(Context context){
        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (sInitialized) return;
        sInitialized = true;

        scheduleWeatherFetching(context);

        DaoMaster daoMaster = new DaoMaster(new WeatherDpHelper(context).getWritableDatabase());
        List<WeatherInfo> forecastList = WeatherDpHelper.getAllWeatherData(daoMaster);

        if(forecastList != null || forecastList.size() ==0){
             startImmediateSync(context);
        }
    }

    public static void startImmediateSync (Context context){
        Intent synchronizeIntentService = new Intent(context, SynchronizeIntentService.class);
        context.startService(synchronizeIntentService);
    }
}

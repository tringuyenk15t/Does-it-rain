package com.tri_nguyen.android.doesitrain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.tri_nguyen.android.doesitrain.R;
import com.tri_nguyen.android.doesitrain.data.CallbackEvent;
import com.tri_nguyen.android.doesitrain.data.DaoMaster;
import com.tri_nguyen.android.doesitrain.data.WeatherDpHelper;
import com.tri_nguyen.android.doesitrain.data.weather_pojo.WeatherResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tri Nguyen on 9/21/2017.
 */

public class WeatherSyncTask {

    synchronized public static void syncWeather(final Context context,final Location location){

        //get settings variables for fetching weather data
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        //check for current location
        if(location == null){return;}

        //if weather has been fetched in the same day, we don't need to do it anymore
        long latestSyncDate = sharedPreferences.getLong("SYNC_DATE",0); // 0 mean 00.00.00 1/1/1970
        if(DateTimeUtils.getDayName(context, latestSyncDate).equals(context.getResources().getString(R.string.today))){
           return;
        }

        String cnt = sharedPreferences.getString(
                context.getString(R.string.pref_cnt_key), context.getString(R.string.pref_cnt_default_value));
        OpenWeatherService openWeatherService =
                NetworkUtils.createService(OpenWeatherService.class);

        Log.e("Location: ", location.toString());
        Call<WeatherResponse> call = openWeatherService.getForecast(location.getLatitude(), location.getLongitude(), cnt);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                DaoMaster daoMaster = new DaoMaster(new WeatherDpHelper(context).getWritableDatabase());
                WeatherDpHelper.clearWeatherData(daoMaster);

                //convert response body into WeatherResponse POJO
                WeatherResponse weatherResponse = response.body();

                //insert weather data into db
                WeatherDpHelper.weatherBulkInsert(weatherResponse.getWeatherItem(), daoMaster);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                long synTime = System.currentTimeMillis();
                editor.putLong("SYNC_DATE", synTime);
                editor.commit();

                // callback main activity to update forecast list
                EventBus.getDefault().post(new CallbackEvent("update_ui"));

                NotificationUtils.showNotification(context, synTime);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("Network Error: ", "Failed of fetching forecast data!");
                Toast.makeText(context, "Network Error: Failed of fetching forecast data!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

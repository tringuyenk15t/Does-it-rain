package com.tri_nguyen.android.doesitrain.utils;

import com.tri_nguyen.android.doesitrain.data.weather.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Tri Nguyen on 8/17/2017.
 */

public interface OpenWeatherService {

    @GET("data/2.5/forecast/daily?lat=11.3210361&lon=106.0816538&mode=json&units=metric")
    Call<WeatherResponse> getForecast(@Query("cnt") String cnt);
}

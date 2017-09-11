package com.tri_nguyen.android.doesitrain.utils;

import com.tri_nguyen.android.doesitrain.data.weather_pojo.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tri Nguyen on 8/17/2017.
 */

public interface OpenWeatherService {
    @GET("data/2.5/forecast/daily?mode=json&units=metric")
    Call<WeatherResponse> getForecast(@Query("lat") double lat, @Query("lon") double lon , @Query("cnt") String cnt);
}

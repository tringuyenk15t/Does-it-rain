package com.tri_nguyen.android.doesitrain.data;

import android.provider.BaseColumns;

/**
 * Created by Tri Nguyen on 8/24/2017.
 */

public class WeatherContract {

    // To prevent someone from accidentally instantiating the contract class
    private WeatherContract(){}

    public static class WeatherEntry implements BaseColumns{

        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_DATE = "date";
        /* Weather ID as returned by API, used to identify the icon to be used */
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_WEATHER_DESCRIPTION = "weather_description";
        //icon id of openweathermap api, it can be used to replace local weather icon
        public static final String COLUMN_WEATHER_ICON = "weather_icon";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        public static final String COLUMN_WIND_DIRECTION = "wind_direction";
        public static final String COLUMN_RAIN = "rain";
    }
}

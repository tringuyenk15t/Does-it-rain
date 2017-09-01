package com.tri_nguyen.android.doesitrain.data.weather_pojo;

import android.database.Cursor;

import com.tri_nguyen.android.doesitrain.data.WeatherContract;

/**
 * Created by Tri Nguyen on 8/26/2017.
 */

public class CustomWeatherModel {
    private int date, weatherId, humidity, direction;
    private String description, iconId;
    private double minTemp, maxTemp, pressure, speed, rain;

    public CustomWeatherModel(){}

    public CustomWeatherModel(WeatherItem weatherItem){
        date = weatherItem.getDt();
        weatherId = weatherItem.getWeather().get(0).getId();
        description = weatherItem.getWeather().get(0).getDescription();
        iconId = weatherItem.getWeather().get(0).getIcon();
        maxTemp = weatherItem.getTemp().getMax();
        minTemp = weatherItem.getTemp().getMin();
        pressure = weatherItem.getPressure();
        humidity = weatherItem.getHumidity();
        speed = weatherItem.getSpeed();
        direction = weatherItem.getDeg();
        if(weatherItem.getRain() != null){
            rain = weatherItem.getRain();
        }else{
            rain = -1.0;
        }
    }

    public CustomWeatherModel(Cursor cursor){
        date = cursor.getInt(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_DATE));
        weatherId = cursor.getInt(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID));
        description = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION));
        iconId = cursor.getString(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON));
        maxTemp = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
        minTemp = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
        pressure = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_PRESSURE));
        humidity = cursor.getInt(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_HUMIDITY));
        speed = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED));
        direction = cursor.getInt(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION));

        rain = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherContract.WeatherEntry.COLUMN_RAIN));
    }


    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }
}

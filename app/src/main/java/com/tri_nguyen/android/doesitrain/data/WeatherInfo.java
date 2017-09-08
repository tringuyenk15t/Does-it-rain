package com.tri_nguyen.android.doesitrain.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Tri Nguyen on 8/31/2017.
 */
@Entity
public class WeatherInfo {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private long date;
    private int weatherId;
    private String weatherDescription;
    private String weatherIcon;
    private double maxTemperature;
    private double minTemperature;
    private double pressure;
    private int humidity;
    private double windSpeed;
    private double windDirection;

    @Property
    private double rain;

    @Generated(hash = 1516899839)
    public WeatherInfo(Long id, long date, int weatherId, String weatherDescription,
            String weatherIcon, double maxTemperature, double minTemperature,
            double pressure, int humidity, double windSpeed, double windDirection,
            double rain) {
        this.id = id;
        this.date = date;
        this.weatherId = weatherId;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.rain = rain;
    }

    @Generated(hash = 477552911)
    public WeatherInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getWeatherId() {
        return this.weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherDescription() {
        return this.weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIcon() {
        return this.weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public double getMaxTemperature() {
        return this.maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return this.minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getPressure() {
        return this.pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return this.windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return this.windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getRain() {
        return this.rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }
    
}

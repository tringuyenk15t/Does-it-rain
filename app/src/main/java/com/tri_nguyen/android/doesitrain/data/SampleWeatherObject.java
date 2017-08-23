package com.tri_nguyen.android.doesitrain.data;

/**
 * Created by Tri Nguyen on 8/7/2017.
 */

public class SampleWeatherObject {

    private String date;
    private String weatherSummary;
    private String highTemp;
    private String lowTemp;

    private int scrID;


    public SampleWeatherObject(String date, String weatherSummary, String highTemp, String lowTemp, int scrID) {
        this.date = date;
        this.weatherSummary = weatherSummary;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.scrID = scrID;
    }


    public String getDate() {
        return date;
    }

    public String getWeatherSummary() {
        return weatherSummary;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public int getScrID() {
        return scrID;
    }
}

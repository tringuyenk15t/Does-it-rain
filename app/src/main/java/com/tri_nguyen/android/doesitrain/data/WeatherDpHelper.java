package com.tri_nguyen.android.doesitrain.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tri_nguyen.android.doesitrain.data.weather_model.CustomWeatherModel;
import com.tri_nguyen.android.doesitrain.data.weather_model.WeatherItem;
import com.tri_nguyen.android.doesitrain.data.weather_model.WeatherResponse;

import java.util.List;

/**
 * Created by Tri Nguyen on 8/24/2017.
 */

public class WeatherDpHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Weather.db";

    public WeatherDpHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Crate weather table
        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + "(" +
                WeatherContract.WeatherEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeatherContract.WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION + " TEXT NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON + " TEXT, " +
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_HUMIDITY + " INTEGER NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION + " INTEGER NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_RAIN + " REAL NOT NULL, "

                +" UNIQUE (" + WeatherContract.WeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST" + WeatherContract.WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * Insert forecast list into local SQLite database
     * @param db - writable database
     * @param response - weather info need to be inserted
     */
    public static int insertNewForecast(SQLiteDatabase db, WeatherResponse response){
        ContentValues values = new ContentValues();
        List<WeatherItem> forecastList = response.getWeatherItem();
        int columnInserted = 0;

        for(int i = 0; i < response.getWeatherItem().size(); i++){
            values.clear();
            WeatherItem item = forecastList.get(i);
            CustomWeatherModel model = new CustomWeatherModel(item);

            values.put(WeatherContract.WeatherEntry.COLUMN_DATE, model.getDate());
            values.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, model.getWeatherId());
            values.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, model.getDescription());
            values.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, model.getIconId());
            values.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, model.getMaxTemp());
            values.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, model.getMinTemp());
            values.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, model.getPressure());
            values.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, model.getHumidity());
            values.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, model.getSpeed());
            values.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, model.getDirection());
            values.put(WeatherContract.WeatherEntry.COLUMN_RAIN, model.getRain());

            long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME,null,values);
            if(_id != -1){columnInserted++;}
        }
        return columnInserted;
    }

}

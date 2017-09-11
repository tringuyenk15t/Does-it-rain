package com.tri_nguyen.android.doesitrain.data;

import android.content.Context;
import android.util.Log;

import com.tri_nguyen.android.doesitrain.data.weather_pojo.WeatherItem;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by Tri Nguyen on 8/24/2017.
 */

public class WeatherDpHelper extends DaoMaster.DevOpenHelper{
    public static final String FORECAST_ID = "forecast_id";

    public static final String DATABASE_NAME = "Weather.db";

    public WeatherDpHelper(Context context) {
        super(context, DATABASE_NAME);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        Log.d("DEBUG", "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);
        //To modify database we should uncomment below
        /*switch (oldVersion) {
            case 1:
            case 2:
                //db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN "
                // + UserDao.Properties.Name.columnName + " TEXT DEFAULT 'DEFAULT_VAL'");
        }*/
    }

    public static void weatherBulkInsert(List<WeatherItem> weatherItems, DaoMaster daoMaster){
        WeatherInfoDao weatherInfoDao = daoMaster.newSession().getWeatherInfoDao();

        for (int i = 0; i< weatherItems.size(); i++){
            WeatherInfo weatherInfo = new WeatherInfo();

            // convert datetime from seconds to milliseconds before inserting into db
            weatherInfo.setDate(weatherItems.get(i).getDt() * 1000L);
            weatherInfo.setWeatherId(weatherItems.get(i).getWeather().get(0).getId());
            weatherInfo.setWeatherDescription(weatherItems.get(i).getWeather().get(0).getDescription());
            weatherInfo.setWeatherIcon(weatherItems.get(i).getWeather().get(0).getIcon());
            weatherInfo.setMaxTemperature(weatherItems.get(i).getTemp().getMax());
            weatherInfo.setMinTemperature(weatherItems.get(i).getTemp().getMin());
            weatherInfo.setHumidity(weatherItems.get(i).getHumidity());
            weatherInfo.setPressure(weatherItems.get(i).getPressure());
            weatherInfo.setWindSpeed(weatherItems.get(i).getSpeed());
            weatherInfo.setWindDirection(weatherItems.get(i).getDeg());

            if (weatherItems.get(i).getRain() == null){
                weatherInfo.setRain(0.0);
            }else{
                weatherInfo.setRain(weatherItems.get(i).getRain());
            }
            weatherInfoDao.insert(weatherInfo);
        }
    }

    public static List<WeatherInfo> getAllWeatherData(DaoMaster daoMaster){
        WeatherInfoDao weatherInfoDao = daoMaster.newSession().getWeatherInfoDao();
        Query<WeatherInfo> query = weatherInfoDao.queryBuilder().build();
        List<WeatherInfo> forecastList = query.list();

        return  forecastList;
    }

    public static void clearWeatherData(DaoMaster daoMaster){
        WeatherInfoDao weatherInfoDao = daoMaster.newSession().getWeatherInfoDao();
        weatherInfoDao.deleteAll();
    }

    public static WeatherInfo getWeatherById(DaoMaster daoMaster, long id){
        WeatherInfoDao weatherInfoDao = daoMaster.newSession().getWeatherInfoDao();
        WeatherInfo info = weatherInfoDao.queryBuilder().
                where(WeatherInfoDao.Properties.Id.eq(id)).unique();
        return info;
    }
}
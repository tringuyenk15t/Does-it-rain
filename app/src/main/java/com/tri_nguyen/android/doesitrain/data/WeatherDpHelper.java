package com.tri_nguyen.android.doesitrain.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tri_nguyen.android.doesitrain.data.weather_pojo.CustomWeatherModel;
import com.tri_nguyen.android.doesitrain.data.weather_pojo.WeatherItem;
import com.tri_nguyen.android.doesitrain.data.weather_pojo.WeatherResponse;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Created by Tri Nguyen on 8/24/2017.
 */

public class WeatherDpHelper extends DaoMaster.DevOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Weather.db";

    public WeatherDpHelper(Context context) {
        super(context, DATABASE_NAME);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        Log.d("DEBUG", "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);
        //code block that help us to modify database will.
//        switch (oldVersion) {
//            case 1:
//            case 2:
//                //db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN "
//                // + UserDao.Properties.Name.columnName + " TEXT DEFAULT 'DEFAULT_VAL'");
//        }
    }


}
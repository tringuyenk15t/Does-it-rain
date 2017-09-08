package com.tri_nguyen.android.doesitrain.data;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Tri Nguyen on 8/24/2017.
 */

public class WeatherDpHelper extends DaoMaster.DevOpenHelper{
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


}
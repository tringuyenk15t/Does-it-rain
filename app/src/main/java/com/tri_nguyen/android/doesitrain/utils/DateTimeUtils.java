package com.tri_nguyen.android.doesitrain.utils;

import android.content.Context;
import android.text.format.DateUtils;

import com.tri_nguyen.android.doesitrain.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tri Nguyen on 8/17/2017.
 */

public class DateTimeUtils {

    //TODO need to update this method to display "today, tomorrow"
    //consider time condition .
    public static String convertDateTimeToString(long dt){
        Date date = new Date(dt * 1000L);
        String dateInString = new SimpleDateFormat("EEE, MMM d").format(date);
        return dateInString;
    }


}

package com.tri_nguyen.android.doesitrain.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.tri_nguyen.android.doesitrain.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tri Nguyen on 8/17/2017.
 */

public class DateTimeUtils {
    /**
     * convert dt to readable string
     * @param dt - date time in milliseconds
     * @return dateInString - readable date time in String
     */
    public static String convertDateTimeToString(Context context, long dt){

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dt);

        String fullDateFormat = context.getResources().getString(R.string.format_full_friendly_date);

        String day = new SimpleDateFormat("d").format(calendar.getTime());
        String month = new SimpleDateFormat("MMM").format(calendar.getTime());
        String dayName = getDayName(context, calendar.getTimeInMillis());

        String dateInString = String.format(fullDateFormat,dayName,month,day);
        return dateInString;
    }

    public static String getDayName(Context context, long providedDate){
        String dayName;
        long daysFromEpochToProvidedDate = elapsedDaysSinceEpoch(providedDate);
        long daysFromEpochToToday = elapsedDaysSinceEpoch(System.currentTimeMillis());

        int dayNum = (int) (daysFromEpochToProvidedDate - daysFromEpochToToday);
        switch (dayNum){
            case 0:
                dayName = context.getResources().getString(R.string.today);
                break;
            case 1:
                dayName = context.getResources().getString(R.string.tomorrow);
                break;
            default:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
                dayName = simpleDateFormat.format(providedDate);
                break;
        }
        return dayName;
    }

    /**
     * This method will return the local time midnight for the provided normalized UTC date.
     * @param normalizedUtcDate UTC time at midnight for a given date. This number comes from the
     * database
     * @return The local date corresponding to the given normalized UTC date
     */
    private static long getLocalMidnightFromNormalizedUtcDate(long normalizedUtcDate) {
        /* The timeZone object will provide us the current user's time zone offset */
        TimeZone timeZone = TimeZone.getDefault();
        //This offset, in milliseconds, when added to a UTC date time, will produce the local
        //time.
        long gmtOffset = timeZone.getOffset(normalizedUtcDate);
        long localMidnightMillis = normalizedUtcDate - gmtOffset;
        return localMidnightMillis;
    }

    /**
     * This method returns the number of days since the epoch (January 01, 1970, 12:00 Midnight UTC)
     * in UTC time from the current date.
     * @param utcDate - utcDate A date in milliseconds in UTC time.
     * @return The number of days from the epoch to the date argument.
     */
    private static long elapsedDaysSinceEpoch(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }


    public static String convertFullDateTimeToString(Context context, long dt){

        SimpleDateFormat format = new SimpleDateFormat("mm:HH MM/dd/yyyy");

        String dateInString = format.format(dt);
        return dateInString;
    }
}

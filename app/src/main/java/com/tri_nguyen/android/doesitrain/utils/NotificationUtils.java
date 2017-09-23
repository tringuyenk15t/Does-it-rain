package com.tri_nguyen.android.doesitrain.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;


import com.tri_nguyen.android.doesitrain.MainActivity;
import com.tri_nguyen.android.doesitrain.R;

/**
 * Created by Tri Nguyen on 9/23/2017.
 */

public class NotificationUtils {

    public static final int MAIN_ACTIVITY_PENDING_INTENT_ID = 1717;
    public static final int MAIN_ACTIVITY_NOTIFICATION_ID = 1718;

    public static void showNotification(Context context, long syncTime){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.art_rain)
                .setSmallIcon(R.drawable.art_rain)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("DoesItRain Notification!")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setContentIntent(createMainActivityIntent(context))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage(context, syncTime)));

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(MAIN_ACTIVITY_NOTIFICATION_ID, builder.build());
    }

    private static PendingIntent createMainActivityIntent(Context context){
        Intent startMainActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                MAIN_ACTIVITY_PENDING_INTENT_ID,
                startMainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static String notificationMessage(Context context, long syncTime){
        String fullDateInString = DateTimeUtils.convertFullDateTimeToString(context, syncTime);
        return String.format(context.getString(R.string.notification_text_format), fullDateInString);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.art_rain);
        return largeIcon;
    }
}

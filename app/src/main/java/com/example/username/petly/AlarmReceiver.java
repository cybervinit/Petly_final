package com.example.username.petly;


import com.example.username.myapplication.R;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.NotificationCompat;

/**
 * This class handles the alarm request, and creates a notification at the specified time.
 */
public class AlarmReceiver extends BroadcastReceiver {


    private static final String DEFAULT = "";
    static int number_of_notifications = 0;

    /**
     * This method receives the pending intent and handles what happens with it (creates notification).
     */
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("NotiMessage", Context.MODE_PRIVATE);
        String notiMessage = sharedPreferences.getString("msg", DEFAULT);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        v.vibrate(1000);


        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);


        Notification notification = builder.setContentTitle("Reminder: Petly")
                .setContentText(notiMessage)
                .setTicker("Petly Notification")
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.mipmap.ic_launcher, "Open Petly", pendingIntent)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
        if (NotificationHolder.notificationDetails.size() > 0) {
            NotificationHolder.deleteSpecificNotification(number_of_notifications);
        }

        number_of_notifications++;

    }
}

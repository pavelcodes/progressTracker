package com.example.pavelplakhotny_c196;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CourseBroadcast extends BroadcastReceiver {
    public CourseBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "courseAlert")
                .setSmallIcon(R.drawable.alert_course_icon)
                .setContentTitle("Course Alert")
                .setContentText("Check in on your course")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());

    }
}

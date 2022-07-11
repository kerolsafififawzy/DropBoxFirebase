package com.kerolsme.dropboxfirebase.Utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.kerolsme.dropboxfirebase.R;

public class App extends Application {

    public static final String ID_CHANNEL = "LOADING" ;

    @Override
    public void onCreate() {
        super.onCreate();
        CreateChannel();
    }

    public void CreateChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(ID_CHANNEL, getPackageName(), NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription("no sound");
            notificationChannel.setShowBadge(false);
            notificationChannel.enableVibration(false);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(getApplicationContext().getResources().getColor(R.color.mColorPrimary));
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
}

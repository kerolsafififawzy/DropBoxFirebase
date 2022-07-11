package com.kerolsme.dropboxfirebase.Utils;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.kerolsme.dropboxfirebase.Utils.App.ID_CHANNEL;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.kerolsme.dropboxfirebase.Activity.MainActivity;
import com.kerolsme.dropboxfirebase.BroadCast_Utilts.CloseNotification;
import com.kerolsme.dropboxfirebase.R;

public class NotificationCustom  {


     public static final int NOTIFICATION_ID = 1;
     private final Context context;
     private final NotificationCompat.Builder builder;
     private final NotificationManager notificationManager ;


     public NotificationCustom (Context context) {
        this.context = context;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context,ID_CHANNEL);
            notificationManager = context.getSystemService(NotificationManager.class);
        }else{
            builder = new NotificationCompat.Builder(context);
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
    }

    public NotificationCompat.Builder CreateNotification (String  Action) {
          Intent Cancel = new Intent(context, CloseNotification.class);
          Cancel.setAction(Action);
          PendingIntent pendingCancel = PendingIntent.getBroadcast(context,2,Cancel,PendingIntent.FLAG_CANCEL_CURRENT);

       return builder.setContentTitle("File Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(IntentMain())
                .setAutoCancel(false)
                .setProgress(100,0,true)
                .addAction(0,"Cancel",pendingCancel);

    }

    public void onProgress (int progress) {
        builder.setProgress(100,progress,false);
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public void onResultLoading (String Text) {
                 builder.clearActions();
                 builder.setContentTitle(Text)
                .setContentText(Text)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(IntentMain())
                .setProgress(0,0,false)
                .setAutoCancel(true);
                 notificationManager.notify(NOTIFICATION_ID,builder.build());

    }

    private PendingIntent IntentMain ()
    {
        Intent start = new Intent(context.getApplicationContext(), MainActivity.class);
        return PendingIntent.getActivity(context,1,start,PendingIntent.FLAG_CANCEL_CURRENT);
    }


}

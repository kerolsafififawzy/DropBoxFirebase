package com.kerolsme.dropboxfirebase.Service;

import static com.kerolsme.dropboxfirebase.Utils.NotificationCustom.NOTIFICATION_ID;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.storage.StorageTask;
import com.kerolsme.dropboxfirebase.Activity.MainActivity;
import com.kerolsme.dropboxfirebase.Utils.NotificationCustom;
import com.kerolsme.dropboxfirebase.Utils.CloudDatabase;

public class Upload extends Service {

    private boolean isFinish = false;
    private StorageTask mStrongTask;
    private NotificationCustom notificationCustom;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationCustom = new NotificationCustom(getApplicationContext());
        Uri uri = intent.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
              startForeground(NOTIFICATION_ID, notificationCustom.CreateNotification("CancelUpload").build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        }else startForeground(NOTIFICATION_ID, notificationCustom.CreateNotification("CancelUpload").build());

        CloudDatabase cloudDatabase = new CloudDatabase(getApplicationContext());
        cloudDatabase.FileSave(uri, new CloudDatabase.CloudResultProgress() {
            @Override
            public void onError(Throwable throwable) {
                isFinish = true;
                stopForeground(true);
                notificationCustom.onResultLoading("Error");
                stopSelf();
            }

            @Override
            public void onSucceed() {
                isFinish = true;
                stopForeground(true);
                notificationCustom.onResultLoading("Succeed");
                stopSelf();
            }

            @Override
            public void onProgress(int progress) {
                if (!isFinish) {notificationCustom.onProgress(progress);}
            }

            @Override
            public void StorageFirebase(StorageTask upload) {
                mStrongTask = upload;
            }
        });
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (!isFinish && mStrongTask != null) {
                isFinish = true;
                mStrongTask.cancel();
                notificationCustom.getNotificationManager().cancel(NOTIFICATION_ID);
        }
        super.onDestroy();
    }

}




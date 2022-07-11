package com.kerolsme.dropboxfirebase.Service;

import static com.kerolsme.dropboxfirebase.Utils.NotificationCustom.NOTIFICATION_ID;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.kerolsme.dropboxfirebase.Utils.NotificationCustom;

import java.io.File;
import java.io.IOException;

public class Download  extends Service {

    private boolean isFinish ;
    private FileDownloadTask fileDownloadTask;
    private NotificationCustom notificationCustom;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getStringExtra("name");
        String ex = intent.getStringExtra("ex");
        String data = intent.getStringExtra("FilePath");
        Log.e("TAG", "onStartCommand: " + data );
        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference httpsReference = storageRef.getReferenceFromUrl(data);
        notificationCustom = new NotificationCustom(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
              startForeground(NOTIFICATION_ID, notificationCustom.CreateNotification("CancelDownload").build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        }else startForeground(NOTIFICATION_ID, notificationCustom.CreateNotification("CancelDownload").build());

            File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),name);
            Log.e("TAG", "onStartCommand: " + localFile.getPath() );
            fileDownloadTask = httpsReference.getFile(localFile);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    isFinish = true;
                    stopForeground(true);
                    notificationCustom.onResultLoading("Succeed");
                    stopSelf();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    isFinish = true;
                    stopForeground(true);
                    notificationCustom.onResultLoading("onError");
                    stopSelf();
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    if (!isFinish) notificationCustom.onProgress((int) progress);
                }
            });

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (!isFinish && fileDownloadTask != null){
            isFinish = true;
            fileDownloadTask.cancel();
            notificationCustom.getNotificationManager().cancel(NOTIFICATION_ID);
        }
        super.onDestroy();

    }

}

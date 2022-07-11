package com.kerolsme.dropboxfirebase.BroadCast_Utilts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kerolsme.dropboxfirebase.Service.Download;
import com.kerolsme.dropboxfirebase.Service.Upload;

public class CloseNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("CancelDownload")) {
            Intent FinishDownload = new Intent(context, Download.class);
            context.stopService(FinishDownload);
        }else  {
            Intent FinishUpload = new Intent(context, Upload.class);
            context.stopService(FinishUpload);
        }
    }


}

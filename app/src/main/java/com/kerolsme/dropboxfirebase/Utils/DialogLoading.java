package com.kerolsme.dropboxfirebase.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.kerolsme.dropboxfirebase.Models.FileModel;
import com.kerolsme.dropboxfirebase.R;


public class DialogLoading {

    private final Dialog dialog;

    public DialogLoading(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.alter_custom,null,false);
        AlertDialog.Builder alertDialog  = new AlertDialog.Builder(context);
        alertDialog.setView(view);
        dialog = alertDialog.create();
    }

    public void SHOW (CloudDatabase cloudDatabase , FileModel fileModel) {
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        cloudDatabase.Delete(fileModel.getKey(),fileModel.getData(), new CloudDatabase.CloudResult() {
            @Override
            public void onError(Throwable throwable) {
                Cancel();
            }

            @Override
            public void onSucceed() {
                Cancel();
            }
        });

    }
    public void Cancel () {
        dialog.dismiss();
    }

    }



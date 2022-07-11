package com.kerolsme.dropboxfirebase.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.helper.widget.Layer;

import com.kerolsme.dropboxfirebase.Models.FileModel;
import com.kerolsme.dropboxfirebase.Models.UserModel;
import com.kerolsme.dropboxfirebase.R;

import java.net.UnknownServiceException;

public class DialogRename {

    private final Activity activity;
    public DialogRename (Activity activity ){
        this.activity = activity;
    }



    public void Show (GetText getText) {
        View view = LayoutInflater.from(activity).inflate(R.layout.alter_edit,null,false);
        EditText editText = view.findViewById(R.id.GetFileName);
        AlertDialog.Builder builder =new  AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle("Rename");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (editText.getText().toString().isEmpty()){
                    Toast.makeText(activity, "Empty", Toast.LENGTH_SHORT).show();
                }else {
                    getText.Get(editText.getText().toString());
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


    public interface GetText {
        void Get(String s);
    }






}

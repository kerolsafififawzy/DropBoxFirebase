package com.kerolsme.dropboxfirebase.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kerolsme.dropboxfirebase.Models.FileModel;
import com.kerolsme.dropboxfirebase.R;
import com.kerolsme.dropboxfirebase.Service.Download;
import com.kerolsme.dropboxfirebase.Service.Upload;
import com.kerolsme.dropboxfirebase.Utils.CloudDatabase;
import com.kerolsme.dropboxfirebase.Utils.DialogLoading;
import com.kerolsme.dropboxfirebase.Utils.DialogRename;

import java.util.Objects;

public class BottomCustom  extends BottomSheetDialogFragment implements View.OnClickListener {


    private CloudDatabase cloudDatabase;
    private FileModel  fileModel;
    private DialogRename dialogRename;

    public BottomCustom() {

    }

    public BottomCustom (FileModel fileModel) {
        this.fileModel = fileModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView Download = view.findViewById(R.id.Export);
        TextView Rename = view.findViewById(R.id.Rename);
        TextView Delete = view.findViewById(R.id.Delete);
        dialogRename = new DialogRename(getActivity());
        cloudDatabase = new CloudDatabase(getActivity());
        Download.setOnClickListener(this);
        Rename.setOnClickListener(this);
        Delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Export){
            Intent intent = new Intent(getActivity(), Download.class);
            intent.putExtra("name",fileModel.getName());
            intent.putExtra("ex",fileModel.getMimeType());
            intent.putExtra("FilePath",fileModel.getData());
            requireActivity().startService(intent);
            dismiss();
        } else if (view.getId() == R.id.Rename) {
            dialogRename.Show(new DialogRename.GetText() {
                @Override
                public void Get(String s) {
                    cloudDatabase.RenameFile(fileModel.getKey(),s+ "." + fileModel.getMimeType(), new CloudDatabase.CloudResult() {
                        @Override
                        public void onError(Throwable throwable) {

                        }

                        @Override
                        public void onSucceed() {
                            dismiss();
                        }
                    });
                }
            });
        }else{
            DialogLoading dialogCustom = new DialogLoading(getActivity());
            dialogCustom.SHOW(cloudDatabase,fileModel);
            dismiss();

        }

    }
}

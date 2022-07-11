package com.kerolsme.dropboxfirebase.Mvvm;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kerolsme.dropboxfirebase.Models.FileModel;

import java.util.ArrayList;

public class FilesMvvm extends ViewModel {

    MutableLiveData<ArrayList<FileModel>> arrayListMutableLiveData = new MutableLiveData<>();



    public FilesMvvm () {
        super();
        setArrayListMutableLiveData();
    }


    private void setArrayListMutableLiveData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()).child("Files").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<FileModel> arrayList = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    FileModel fileModel = item.getValue(FileModel.class);
                    fileModel.setKey(item.getKey());
                    arrayList.add(fileModel);
                }
                arrayListMutableLiveData.postValue(arrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public MutableLiveData<ArrayList<FileModel>> getArrayListMutableLiveData() {
        return arrayListMutableLiveData;
    }
}


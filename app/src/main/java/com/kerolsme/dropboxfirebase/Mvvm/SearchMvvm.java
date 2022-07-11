package com.kerolsme.dropboxfirebase.Mvvm;

import android.util.Log;

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

public class SearchMvvm extends ViewModel {


    private MutableLiveData<ArrayList<FileModel>> arrayListMutableLiveData= new MutableLiveData<>();

    public SearchMvvm() {
        super();
    }


    public void Search(String Filename) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()).child("Files")
                .orderByChild("name").startAt(Filename).endAt(Filename + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 ArrayList<FileModel> arrayList = new ArrayList<>();
                for (DataSnapshot it : snapshot.getChildren()){
                    FileModel fileModel = it.getValue(FileModel.class);
                    fileModel.setKey(it.getKey());
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


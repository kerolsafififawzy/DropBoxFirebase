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

public class HomeMvvm extends ViewModel {


    public MutableLiveData<ArrayList<FileModel>> arrayListMutableLiveData = new MutableLiveData<ArrayList<FileModel>>();

    public HomeMvvm () {
        super();
        setArrayListMutableLiveData();
    }
    public void setArrayListMutableLiveData () {
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference(mAuth.getUid())
                .child("Files").limitToLast(5).addValueEventListener(new ValueEventListener() {
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

package com.kerolsme.dropboxfirebase.Mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kerolsme.dropboxfirebase.Models.UserModel;

import java.text.ParsePosition;
import java.util.ArrayList;

public class AccountMvvm  extends AndroidViewModel {

    MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();

    public AccountMvvm(@NonNull Application application) {
        super(application);
        setData();
    }


    private void setData () {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()).child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModelMutableLiveData.postValue(snapshot.getValue(UserModel.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    public MutableLiveData<UserModel> getUserModelMutableLiveData() {
        return userModelMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}

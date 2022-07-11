package com.kerolsme.dropboxfirebase.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.kerolsme.dropboxfirebase.Activity.Login;
import com.kerolsme.dropboxfirebase.Activity.MainActivity;
import com.kerolsme.dropboxfirebase.Activity.Premium;
import com.kerolsme.dropboxfirebase.Models.UserModel;
import com.kerolsme.dropboxfirebase.Mvvm.AccountMvvm;
import com.kerolsme.dropboxfirebase.R;
import com.kerolsme.dropboxfirebase.Utils.CloudDatabase;
import com.kerolsme.dropboxfirebase.Utils.Converter;
import com.kerolsme.dropboxfirebase.Utils.DialogRename;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Set;

public class Account  extends Fragment implements View.OnClickListener {


    private TextView UsedSize ;
    private TextView LimitedSize;
    private TextView SignOut;
    private TextView GetPremium;
    private ImageView UserEdit;
    private ImageView UserImage;
    private boolean isNetwork;
    private View Loading;
    private View ViewMain;
    private UserModel userModel;
    private CloudDatabase cloudDatabase;
    private TextView UserName;

    public Account () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        inti(view);
        cloudDatabase = new CloudDatabase(getActivity());
        AccountMvvm account =new ViewModelProvider(requireActivity()).get(AccountMvvm.class);
        account.getUserModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                Loading.setVisibility(View.GONE);
                SetData(userModel);
            }
        });

        SignOut.setOnClickListener(this);
        GetPremium.setOnClickListener(this);
        UserEdit.setOnClickListener(this);
    }

    public void  inti (View view) {
        ViewMain = view.findViewById(R.id.ViewMain);
        UserImage = view.findViewById(R.id.UserImage);
        UsedSize =  view.findViewById(R.id.Used);
        LimitedSize = view.findViewById(R.id.Limited);
        SignOut = view.findViewById(R.id.SignOut);
        GetPremium = view.findViewById(R.id.AccountPlus);
        UserEdit = view.findViewById(R.id.UserEdit);
        Loading = view.findViewById(R.id.loading_);
        UserName = view.findViewById(R.id.UserName);
    }

    public void SetData (UserModel mUserModel) {
        userModel = mUserModel;
        if (mUserModel.isPremium()){GetPremium.setVisibility(View.GONE);}else GetPremium.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "" + mUserModel.isPremium(), Toast.LENGTH_SHORT).show();
        UsedSize.setText(Converter.formatSize(mUserModel.getSize()));
        LimitedSize.setText(Converter.formatSize(mUserModel.getLimitedSize()));
        Picasso.get().load(mUserModel.getPhotoUrl()).into(UserImage);
        UserName.setText(mUserModel.getUserName());

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.AccountPlus) {

            Intent intent = new Intent(getActivity(),Premium.class);
            startActivity(intent);

        }else if (view.getId() == R.id.SignOut){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            getActivity().finish();
        }else {

            if (userModel != null) {
                new DialogRename(getActivity()).Show(new DialogRename.GetText() {
                    @Override
                    public void Get(String s) {
                        cloudDatabase.RenameUser(s, new CloudDatabase.CloudResult() {
                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onSucceed() {

                            }
                        });
                    }
                });
            }


        }



    }
}

package com.kerolsme.dropboxfirebase.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.kerolsme.dropboxfirebase.Adapter.FilesAdapter;
import com.kerolsme.dropboxfirebase.Models.FileModel;
import com.kerolsme.dropboxfirebase.Mvvm.HomeMvvm;
import com.kerolsme.dropboxfirebase.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private View Empty;
    private View Loading;

    public HomeFragment () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Empty = view.findViewById(R.id.empty_);
        Loading = view.findViewById(R.id.loading_);
        RecyclerView ListItems = view.findViewById(R.id.listHome);
        FilesAdapter filesAdapter = new FilesAdapter(new ArrayList<>(),getActivity().getSupportFragmentManager());
        ListItems.setAdapter(filesAdapter);
        HomeMvvm homeMvvm = new ViewModelProvider(this).get(HomeMvvm.class);
        homeMvvm.getArrayListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<FileModel>>() {
            @Override
            public void onChanged(ArrayList<FileModel> arrayList) {
                if (!arrayList.isEmpty()){
                 Loading.setVisibility(View.GONE);
                 Empty.setVisibility(View.GONE);
                 filesAdapter.setArrayList(arrayList);
                }else {
                    Loading.setVisibility(View.GONE);
                    Empty.setVisibility(View.VISIBLE);
                }
                Log.e("TAG", "onChanged: " + "jerols" );

            }
        });
    }

}


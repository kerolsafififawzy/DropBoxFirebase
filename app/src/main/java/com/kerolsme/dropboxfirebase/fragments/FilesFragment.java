package com.kerolsme.dropboxfirebase.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.kerolsme.dropboxfirebase.Adapter.FilesAdapter;
import com.kerolsme.dropboxfirebase.Models.FileModel;
import com.kerolsme.dropboxfirebase.Mvvm.FilesMvvm;
import com.kerolsme.dropboxfirebase.R;

import java.util.ArrayList;

public class FilesFragment extends Fragment {

    private View Empty;
    private View Loading;
    public FilesFragment () {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.files_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Empty = view.findViewById(R.id.empty_);
        Loading = view.findViewById(R.id.loading_);
        RecyclerView recyclerView = view.findViewById(R.id.listFiles);
        FilesAdapter filesAdapter = new FilesAdapter(new ArrayList<FileModel>(),getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(filesAdapter);
        ReadData(filesAdapter);

    }

    private void  ReadData(FilesAdapter filesAdapter)  {

               FilesMvvm  filesMvvm = new ViewModelProvider(requireActivity()).get(FilesMvvm.class);
                filesMvvm.getArrayListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<FileModel>>() {
                    @Override
                    public void onChanged(ArrayList<FileModel> arrayList) {
                        Log.e("TAG", "onChanged: " + "ketols" );
                        if (!arrayList.isEmpty()) {
                            Loading.setVisibility(View.GONE);
                            Empty.setVisibility(View.GONE);
                            filesAdapter.setArrayList(arrayList);
                        } else {
                            Loading.setVisibility(View.GONE);
                            Empty.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

}


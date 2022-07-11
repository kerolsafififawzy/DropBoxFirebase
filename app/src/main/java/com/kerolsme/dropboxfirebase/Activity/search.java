package com.kerolsme.dropboxfirebase.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.firebase.database.DatabaseReference;
import com.kerolsme.dropboxfirebase.Adapter.FilesAdapter;
import com.kerolsme.dropboxfirebase.Models.FileModel;
import com.kerolsme.dropboxfirebase.Mvvm.SearchMvvm;
import com.kerolsme.dropboxfirebase.R;

import java.util.ArrayList;

public class search extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private DatabaseReference databaseReference;
    private SearchMvvm searchMvvm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        RecyclerView recyclerView = findViewById(R.id.listItems);
        FilesAdapter filesAdapter = new FilesAdapter(new ArrayList<>(),getSupportFragmentManager());
        recyclerView.setAdapter(filesAdapter);
        searchMvvm = new ViewModelProvider(this).get(SearchMvvm.class);
        searchMvvm.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<FileModel>>() {
            @Override
            public void onChanged(ArrayList<FileModel> arrayList) {
                filesAdapter.setArrayList(arrayList);
            }
        });
        //searchView.setQuery(searchMvvm.getS(),false);

        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
           searchMvvm.Search(newText);
        }
        return true;
    }

}
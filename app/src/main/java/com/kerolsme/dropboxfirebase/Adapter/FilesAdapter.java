package com.kerolsme.dropboxfirebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kerolsme.dropboxfirebase.Models.FileModel;
import com.kerolsme.dropboxfirebase.R;
import com.kerolsme.dropboxfirebase.Utils.Converter;
import com.kerolsme.dropboxfirebase.fragments.BottomCustom;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolderCustom> {
    private ArrayList<FileModel> arrayList;
    private FragmentManager context;
    public FilesAdapter (ArrayList<FileModel> arrayList , FragmentManager context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolderCustom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCustom(LayoutInflater.from(parent.getContext()).inflate(R.layout.files,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCustom holder, int position) {
        FileModel Item = arrayList.get(position);
        Picasso.get().load(Item.getData()).into(holder.FileImage);
        holder.FileSize.setText(Converter.formatSize(Item.getSize()));
        holder.FileName.setText(Item.getName());
        holder.FileDate.setText( " - " + Converter.getTimeAgo(Item.getDate()));
        if (position == arrayList.size() - 1 ) {
             holder.layoutParams.setMargins(0,0,0,150);
        }else {
            holder.layoutParams.setMargins(0,0,0,0);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomCustom bottomCustom = new BottomCustom(Item);
                bottomCustom.show(context,bottomCustom.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderCustom extends RecyclerView.ViewHolder {
         TextView FileName;
         TextView FileSize;
         TextView FileDate;
         ImageView FileImage;
         RecyclerView.LayoutParams layoutParams;

        public ViewHolderCustom(@NonNull View itemView) {
            super(itemView);
            FileName = itemView.findViewById(R.id.FileName);
            FileDate = itemView.findViewById(R.id.FileData);
            FileSize = itemView.findViewById(R.id.FileSize);
            FileImage = itemView.findViewById(R.id.FileImage);
            layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();

        }
    }

    public void setArrayList(ArrayList<FileModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
}

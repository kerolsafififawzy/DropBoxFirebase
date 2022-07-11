package com.kerolsme.dropboxfirebase.Models;

import java.lang.ref.PhantomReference;

public class FileModel {

    private String data;
    private String name;
    private Long date;
    private long size;
    private String mimeType;
    private String key;

    public FileModel () {

    }

    public FileModel (String data , String name , Long date , long size , String mimeType , String key) {
        this.date = date;
        this.name = name;
        this.data = data;
        this.mimeType = mimeType;
        this.size = size ;
        this.key = key;
    }

    public FileModel (String data , String name , Long date , long size , String mimeType) {
        this.data = data;
        this.date = date;
        this.name = name;
        this.mimeType = mimeType;
        this.size = size ;
    }


    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getDate() {
        return date;
    }

    public long getSize() {
        return size;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

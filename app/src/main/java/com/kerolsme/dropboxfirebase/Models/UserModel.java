package com.kerolsme.dropboxfirebase.Models;

import android.service.autofill.FillEventHistory;

import com.kerolsme.dropboxfirebase.R;

public class UserModel {
    
    private String email ;
    private String uid;
    private String userName;
    public String photoUrl;
    private boolean premium;
    private Long size;
    private Long limitedSize;



    public UserModel () {


    }


   public UserModel (String email , String uid , String userName , String photoUrl ,boolean premium,Long size , Long limitedSize) {
       this.premium = premium;
       this.size = size;
       this.limitedSize = limitedSize;
       this.email = email;
       this.photoUrl = photoUrl;
       this.uid = uid;
       this.userName = userName;
   }


    public String getEmail() {
        return email;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public String getUid() {
        return uid;
    }
    public String getUserName() {
        return userName;
    }
    public Long getLimitedSize() {return limitedSize;}
    public Long getSize() {return size;}
    public boolean isPremium() {return premium;}

}

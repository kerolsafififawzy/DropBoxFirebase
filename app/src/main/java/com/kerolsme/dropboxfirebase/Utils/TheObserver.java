package com.kerolsme.dropboxfirebase.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TheObserver {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TheObserver (Context context) {
        sharedPreferences = context.getSharedPreferences("App",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void SaveSize(Long Size) {
        editor.putLong("size",Size);
        editor.apply();
    }
    public long getSize() {
        return sharedPreferences.getLong("size",0);
    }

    public void setLimitedSize (long limited) {
        editor.putLong("limited",limited);
        editor.apply();
    }

    public long getLimitedSize () {
        return sharedPreferences.getLong("limited",2147483648L);
    }
}

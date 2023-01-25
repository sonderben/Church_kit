package com.churchkit.churchkit.database;

import android.content.Context;
import android.os.Build;

import androidx.room.TypeConverter;

import com.churchkit.churchkit.R;

public class ChurchKitConverter {

    Context context;

    public ChurchKitConverter(Context context) {
        this.context = context;
    }


    @TypeConverter
    public int resourceNameToIdentifier(String resourceName){
            return  context.getResources().getIdentifier(resourceName,"color", "com.churchkit.churchkit");
    }

    @TypeConverter
    public String resourceIdentifierToName(int resourceIdentifier){
        return context.getResources().getResourceEntryName(resourceIdentifier);
    }

}

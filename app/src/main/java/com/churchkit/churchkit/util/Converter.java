package com.churchkit.churchkit.util;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class Converter {
    @TypeConverter
    public static Calendar toDate(Long dateLong){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateLong);
        return date;
    }

    @TypeConverter
    public static Long fromDate(Calendar date){
        return  date.getTimeInMillis();
    }
}

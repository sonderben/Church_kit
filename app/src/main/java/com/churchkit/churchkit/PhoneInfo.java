package com.churchkit.churchkit;

import android.os.Build;

public class PhoneInfo {
    public static String manufacturer = Build.MANUFACTURER;
    public static String model = Build.MODEL;
    public static String version = Build.VERSION.RELEASE;
    public static String device = Build.DEVICE;


    @Override
    public String toString() {
        return "PhoneInfo{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", version='" + version + '\'' +
                ", device='" + device + '\'' +
                '}';
    }
}

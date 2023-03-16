package com.churchkit.churchkit.ui.util;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Util {
    public static DisplayMetrics getScreenDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int getColorByPosition(int position){
        System.out.println("position: "+position);

        switch (position){
            case 1:return Color.WHITE;
            case 2:return Color.argb(210,  136,  190, 1);
            case 3:return Color.argb(110,166,90,1);
            case 4:return Color.argb( 31,  138,  112, 1);
            case 5:return Color.argb( 191,  219,  56, 1);
            case 6:return Color.argb( 252,  115,  0, 1);

            case 7:return Color.argb( 114,  134,  211, 1);
            case 8:return Color.argb( 255,  212,  149, 1);
            case 9:return Color.argb( 255,  66,  110, 1);
            case 10:return Color.argb( 215,  233,  185, 1);
            default:
                return Color.WHITE;
        }
    }
}

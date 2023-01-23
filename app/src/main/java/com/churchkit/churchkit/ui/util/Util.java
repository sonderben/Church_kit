package com.churchkit.churchkit.ui.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Util {
    public static DisplayMetrics getScreenDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}

package com.churchkit.churchkit.util;

import android.content.Context;
import android.os.PowerManager;

public class CkWakeLock {
    private static PowerManager.WakeLock wakeLock ;

    public static long TEN_MINUTES = 10 * 1000 * 60;

    public static PowerManager.WakeLock getInstance(Context context){
        if (wakeLock == null){
            PowerManager powerManager = (PowerManager) context.getSystemService( Context.POWER_SERVICE );
            wakeLock = powerManager.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, "MiApp:EncenderPantalla");
        }
        return  wakeLock;
    }
}

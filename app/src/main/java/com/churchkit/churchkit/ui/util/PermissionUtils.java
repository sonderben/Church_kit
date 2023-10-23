package com.churchkit.churchkit.ui.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    // Identificador de solicitud de permiso (puede ser cualquier número)
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;


    public static boolean hasWriteExternalStoragePermission(Activity activity) {








        int permissionResult = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionResult == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }


    public static void requestWriteExternalStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
        );
    }

    // Manejar la respuesta de la solicitud de permiso
    public static boolean handlePermissionResult(
            int requestCode, String[] permissions, int[] grantResults, int permissionCode) {
        if (requestCode == permissionCode) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true; // Permiso otorgado
            } else {
                return false; // Permiso denegado
            }
        }
        return false; // Cualquier otro requestCode
    }


}



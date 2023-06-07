package com.churchkit.churchkit.util;


import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;

public class InternetBroadcastReceiver extends BroadcastReceiver {

    public interface OnInternetStatusChange{
        void change(boolean isConnected);
    }
    OnInternetStatusChange onInternetStatusChange;

    public void  setOnInternetStatusChange(OnInternetStatusChange onInternetStatusChange){
        this.onInternetStatusChange = onInternetStatusChange;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnectedToInternet(context)) {
            if (onInternetStatusChange!=null)
                onInternetStatusChange.change(true);
        } else {
            if (onInternetStatusChange!=null)
                onInternetStatusChange.change(false);
        }
    }

    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

            return activeNetwork != null  && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}

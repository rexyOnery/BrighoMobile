package com.ovede.brigho.web;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ovede.brigho.MainActivity;

public class DetectConnection {
    private static final String TAG = DetectConnection.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean InternetAvailable(MainActivity context){
//        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
//                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//        if (info == null){
//            return false;
//        }else{
//            return true;
//
//        }

        NetworkCapabilities nc = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return netInfo.isConnected();
            }catch (Exception e){
                return false;
            }
        }else{
            final ConnectivityManager connManager =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(null != connManager) {
                NetworkInfo[] allNetworks = connManager.getAllNetworkInfo();
                if(null != allNetworks) {
                    for(NetworkInfo info: allNetworks) {
                        if ( info.getState() == NetworkInfo.State.CONNECTED ) {
                           return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}

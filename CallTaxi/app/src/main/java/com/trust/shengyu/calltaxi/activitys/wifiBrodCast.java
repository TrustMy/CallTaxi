package com.trust.shengyu.calltaxi.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.trust.shengyu.calltaxi.tools.L;

/**
 * Created by Trust on 2017/8/2.
 */

public class wifiBrodCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
            //signal strength changed
        }
        else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){//wifi连接上与否
//            System.out.println("网络状态改变");
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(info.getState().equals(NetworkInfo.State.DISCONNECTED)){
                System.out.println("wifi网络连接断开");
                Toast.makeText(context,"wifi网络连接断开",Toast.LENGTH_LONG).show();
            }
            else if(info.getState().equals(NetworkInfo.State.CONNECTED)){

                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                //获取当前wifi名称
                System.out.println("连接到网络wifi " + wifiInfo.getSSID()+"|wifiInfo.getBSSID():"+wifiInfo.getBSSID());
                Toast.makeText(context,"连接wifi",Toast.LENGTH_LONG).show();
            }

        }
         if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){//wifi打开与否
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

            if(wifistate == WifiManager.WIFI_STATE_DISABLED){
                System.out.println("系统关闭wifi");
                L.d("系统关闭wifi");
            }
            else if(wifistate == WifiManager.WIFI_STATE_ENABLED){
                System.out.println("系统开启wifi");
                L.d("系统开启wifi");
            }
        }
    }
}

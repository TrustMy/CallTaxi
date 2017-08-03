package com.trust.shengyu.calltaxidriver.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Trust on 2017/8/3.
 */

public class TrustServer extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void initMqtt(){

    }



}

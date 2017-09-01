package com.trust.shengyu.rentalcar;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.MapView;
import com.jakewharton.rxbinding2.view.RxView;
import com.trust.shengyu.rentalcar.tools.AndroidPermissionTool;
import com.trust.shengyu.rentalcar.tools.L;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class BaseActivity extends AppCompatActivity {
    protected MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
    }
    protected void initPermission(){
        AndroidPermissionTool androidPermissionTool = new AndroidPermissionTool();
        androidPermissionTool.checkPermission(this);
    }





    //--------------------在设置的时间内只响应1次,其他过滤---------------------------------------------------------------------
    protected void  baseSetOnClick(final View v){
        RxView.clicks(v).throttleFirst(Config.ClickTheInterval, TimeUnit.SECONDS).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        baseClickResult(v);
                    }
                });
    }

    //----------------------动态申请权限结果---------------------------------------------------
    boolean status = false;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if(grantResults.length > 0 ){
                    for (int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            status = false;
                            break;
                        }else{
                            status = true;
                        }
                    }
                    if (status) {
                        L.d("全部通过");
                        PermissionSuccess();
                    }else{
                        L.d("全部或一部分被拒绝");
                        PermissionError();
                    }
                }
                break;
        }
    }

    protected void PermissionSuccess(){
        L.d("PermissionSuccess");
    }

    protected void PermissionError(){
        L.d("PermissionError");
    }
}

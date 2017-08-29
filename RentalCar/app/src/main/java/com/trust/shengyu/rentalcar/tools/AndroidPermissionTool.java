package com.trust.shengyu.rentalcar.tools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


/**
 * Created by Trust on 2017/7/23.
 */

public class AndroidPermissionTool extends Activity{
    public void checkPermission(Activity activity){

        int version = Integer.valueOf(Build.VERSION.SDK );
        L.d("version :"+Build.VERSION.SDK_INT);
        if(version >= 23){
            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE ,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_PHONE_STATE ,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_FINE_LOCATION},1);
                }else {
                    if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.READ_PHONE_STATE ,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }else{
                        if(ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.READ_PHONE_STATE ,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else{

                                if(ContextCompat.checkSelfPermission(activity,Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(activity,
                                            new String[]{Manifest.permission.READ_PHONE_STATE ,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.CAMERA},1);
                                }else{

                                    L.d("permission success");
                                }
                        }
                    }
                }
            }
        }else {

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    L.d("执行操作");
                }else{
                    L.e("拒绝权限");
                }

                break;
        }
    }
}

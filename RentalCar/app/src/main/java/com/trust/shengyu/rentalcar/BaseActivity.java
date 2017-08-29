package com.trust.shengyu.rentalcar;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trust.shengyu.rentalcar.tools.AndroidPermissionTool;
import com.trust.shengyu.rentalcar.tools.L;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPermission();
    }

    protected void initPermission(){
        AndroidPermissionTool androidPermissionTool = new AndroidPermissionTool();
        androidPermissionTool.checkPermission(this);
    }

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

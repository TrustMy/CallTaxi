package com.trust.shengyu.rentalcarclient.activitys.checkinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;


public class CheckInfoActivity extends BaseActivity {
    private static final int CHOODE_PHOTO = 2;
    private static final int CHOODE_PHOTO1 = 3;
    private static final int CHOODE_PHOTO2= 4;
    private static final int CHOODE_PHOTO3 = 5;
    private ImageView testlog,testlog1,testlog2,testlog3;
    TrustTools trustTools;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_info);
        testlog = (ImageView) findViewById(R.id.testlog);
        testlog1 = (ImageView) findViewById(R.id.testlog1);
        testlog2 = (ImageView) findViewById(R.id.testlog2);
        testlog3 = (ImageView) findViewById(R.id.testlog3);
        trustTools = new TrustTools<>();
    }


    public void test(View view){
            openAlbum(CHOODE_PHOTO);
    }

    public void test1(View view){
        openAlbum(CHOODE_PHOTO1);
    }

    public void test2(View view){
        openAlbum(CHOODE_PHOTO2);
    }

    public void test3(View view){
        openAlbum(CHOODE_PHOTO3);
    }

    /**
     * 打开本地相册
     * @param requestCode
     */
    private void openAlbum(int requestCode) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,requestCode);
    }

TrustTools tru = new TrustTools();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOODE_PHOTO:
                if (resultCode == RESULT_OK){

                 Bitmap bitmap =    trustTools.getImages(data,this);
                    if (bitmap == null) {
                        L.e("获取图片失败");
                    }else{
                        testlog.setImageBitmap(bitmap);
                    }
                }
                break;

            case CHOODE_PHOTO1:
                if (resultCode == RESULT_OK){

                    Bitmap bitmap =    trustTools.getImages(data,this);
                    if (bitmap == null) {
                        L.e("获取图片失败");
                    }else{
                        testlog1.setImageBitmap(bitmap);
//                        TrustTools.bitmapCompression(bitmap);
                    }

                }
                break;
            case CHOODE_PHOTO2:
                if (resultCode == RESULT_OK){

                    Bitmap bitmap =     trustTools.getImages(data,this);
                    if (bitmap == null) {
                        L.e("获取图片失败");
                    }else{
                        testlog2.setImageBitmap(bitmap);
                    }
                }
                break;
            case CHOODE_PHOTO3:
                if (resultCode == RESULT_OK){
//                    20160710_113834   .jpg   /storage/9016-4EF8/DCIM/Camera/
                    Bitmap bitmap =  trustTools .getImages(data,this);
                    if (bitmap == null) {
                        L.e("获取图片失败");
                    }else{
                        testlog3.setImageBitmap(bitmap);
                    }
                }
                break;

            default:
                L.e("default");
                break;
        }
    }














}

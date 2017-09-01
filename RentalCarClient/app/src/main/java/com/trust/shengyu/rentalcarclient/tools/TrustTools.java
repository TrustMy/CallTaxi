package com.trust.shengyu.rentalcarclient.tools;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Trust on 2017/8/7.
 */

public class TrustTools <T extends View> {
    private static  TrustTools trustTools;

    public static  TrustTools getTrustTools(){
        if (trustTools == null) {
            trustTools = new TrustTools();
        }
        return trustTools;
    }

    /**
     * 倒计时显示
     * @param value
     * @param time
     */
    public  void  Countdown(final T value , int time){
        final int count = time;
        Observable.interval(0,1, TimeUnit.SECONDS).take(count+1).map(new Function<Long, Object>() {
            @Override
            public Object apply(@NonNull Long aLong) throws Exception {
                L.d("定时器: count:"+count+"|aLong:"+aLong);
                return count-aLong;
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        value.setEnabled(false);//不可点击
                    }
                }).observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        checkT(value,o+"秒");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        value.setEnabled(true);//不可点击
                        checkT(value,"获取验证码");
                    }
                });
    }

    private void checkT(T v,String msg){
        if(v instanceof Button){
            ((Button)v).setText(msg);
        }else if(v instanceof TextView){
            ((TextView)v).setText(msg);
        }
    }

    /**
     * 自定义时间时间倒计时
     * @param time
     */
    public  TrustTools setCountdown( int time){
        Observable
                .fromArray(100)
                .subscribeOn(Schedulers.io())
                .timer(time*1000,TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ObservableTransformer<Object, Object>() {

                    @Override
                    public ObservableSource<Object> apply(Observable<Object> upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        countdownCallBack.callBackCountDown();
                    }
                });

        return this;
    }


    public interface CountdownCallBack{
        void callBackCountDown();
    }
    public CountdownCallBack countdownCallBack;

    public void setCountdownCallBack(CountdownCallBack countdownCallBack){
        this.countdownCallBack = countdownCallBack;
    }


    //-----------------------时间--------------------------------------

    /**
     * 获取系统时间
     * @return
     */
    public static String  getSystemTimeString(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = new Date(System.currentTimeMillis());//获取当前时间
        String systemTime = formatter.format(dateTime);
        return systemTime;
    }

    /**
     * 获取系统时间 data
     * @return
     */
    public static long getSystemTimeData(){
        return System.currentTimeMillis();
    }

    /**
     * 省略小数点后几位
     * @param v  小数
     * @param scale  几位
     * @return
     */
    public   static   double   round(double v,int   scale){
        if(scale<0){
            throw   new   IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b   =   new   BigDecimal(Double.toString(v));
        BigDecimal   one   =   new   BigDecimal("1");
        return   b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取app当前版本
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String resultAppVersion(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();

        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),
                0);
        return packInfo.versionName;
    }


    //-------------------获取相册里面的相片------------------------------------------------------------------

    /**
     * 得到图片的bitmap
     * @param data
     * @param context
     * @return
     */
    public Bitmap getImages(Intent data , Context context){
        Bitmap bitmap = null;
        if(Build.VERSION.SDK_INT >=19){
            //4.4以上
            bitmap =  handlerImageOnKikat(data,context);
        }else{
            //4.4一下
            bitmap =  handlerImageBeforeKiKat(data ,context);
        }

        return bitmap;
    }

    /**
     * 4.4以上获取
     * @param data
     * @param context
     * @return
     */
    private Bitmap handlerImageOnKikat(Intent data , Context context){
        String imagePath = null;
        Uri url = data.getData();
        if(DocumentsContract.isDocumentUri(context,url)){
            //如果是document类型的url,通过document ID 处理
            String docId = DocumentsContract.getDocumentId(url);
            if("com.android.providers.media.documents".equals(url.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection
                        ,context);
            }else if("com.android.providers.downloads.documents".equals(url.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null,context);
            }

        }else if("content".equalsIgnoreCase(url.getScheme())){
            //如果是content类型Uri 使用普通方式
            imagePath = getImagePath(url,null,context);
        } else if ("file".equalsIgnoreCase(url.getScheme())) {
            //如果是file类型的uri 直接获取图片路径
            imagePath = url.getPath();
        }
        return displayImage(imagePath);
    }

    /**
     * 4.4一下获取
     * @param data
     * @param context
     * @return
     */
    private Bitmap handlerImageBeforeKiKat(Intent data , Context context){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null ,context);
        return displayImage(imagePath);
    }



    private String getImagePath(Uri uri,String  selection ,Context context){
        String path = null;
        //通过Uri和selection 获取真实图片路径
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return  path;
    }

    private Bitmap displayImage(String imagePath) {
        L.d("图片路径:"+imagePath);
        if (imagePath == null) {
            L.e("找不到这个文件!");
            return null;
        }else{

            return   bitmapCompressionRotate(imagePath);
        }
    }

    //-----------------------图片压缩--------------------------------------------------------------


    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }



    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param bitmap
     * @return
     */
    public static Bitmap bitmapCompression(Bitmap bitmap){
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 2;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio);
        canvas.drawBitmap(bitmap, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
        L.d("bitmap:"+(bitmap.getByteCount() / 1024 )+"KB"
                +"|bitmap2 大小:"+(result.getByteCount() / 1024 )+"KB");
        return result;
    }

    /**
     * bitmap 压缩,旋转,保存
     * @param fileName
     * @param filePath
     * @return
     */
    public static Bitmap bitmapCompressionRotate(String fileName,String filePath){
        try{
            int quality = 80;
            Bitmap bitmap = null;
            //
            File f = new File(filePath,fileName + ".jpg");
            int rotate = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(filePath);
                int result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                switch(result) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    default:
                        rotate =  0;
                        break;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                L.e("读取img 错误:"+e.toString());
            }
            L.d("rotate :"+rotate);
            // 1:compress bitmap
            try {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f), null, o);
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1;
                while (true) {
                    int VARIETY_SIZE = (rotate==90 || rotate==270)?height_tmp:width_tmp;
                    if (VARIETY_SIZE / 2 <= 600){
                        if(VARIETY_SIZE>600 && VARIETY_SIZE-600>300){
                        }else{
                            break;
                        }
                    }
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }
                // decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                bitmap =  BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
                int max = width_tmp>height_tmp?width_tmp:height_tmp;
                int min = width_tmp>height_tmp?height_tmp:width_tmp;
                int value = (int)((float)max*10/min);
                if(value>15){
                    quality = 80;
                }else{
                    quality = 90;
                }
            } catch (FileNotFoundException e) {
                System.gc();
            } catch(OutOfMemoryError e){
                System.gc();
                e.printStackTrace();
            }
            // 2:rotate bitmap
            if(f.exists()){
                f.delete();
            }

            if(rotate>0){
                Matrix mtx = new Matrix();
                mtx.postRotate(rotate);
                try{
                    Bitmap roateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), mtx, true);
                    if(roateBitmap!=null && roateBitmap!=bitmap){
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                        bitmap = roateBitmap;
                    }
                }catch(OutOfMemoryError e){
                    System.gc();
                    e.printStackTrace();
                }
            }




            /*
            压缩后的bitmap  保存到指定的路径
            // 3:save bitmap
            FileOutputStream fileOutputStream = new FileOutputStream(f.getPath());
            BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            bitmap.recycle();
            os.flush();
            os.close();
            */

            System.gc();
            return bitmap;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }




    /**
     * bitmap 压缩,旋转,保存

     * @return
     */
    public static Bitmap bitmapCompressionRotate(String path){
        try{
            int quality = 80;
            Bitmap bitmap = null;
            //
            File f = new File(path);
            int rotate = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(path);
                int result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                switch(result) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    default:
                        rotate =  0;
                        break;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                L.e("读取img 错误:"+e.toString());
            }
            L.d("rotate :"+rotate);
            // 1:compress bitmap
            try {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f), null, o);
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1;
                while (true) {
                    int VARIETY_SIZE = (rotate==90 || rotate==270)?height_tmp:width_tmp;
                    if (VARIETY_SIZE / 2 <= 600){
                        if(VARIETY_SIZE>600 && VARIETY_SIZE-600>300){
                        }else{
                            break;
                        }
                    }
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }
                // decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                bitmap =  BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
                int max = width_tmp>height_tmp?width_tmp:height_tmp;
                int min = width_tmp>height_tmp?height_tmp:width_tmp;
                int value = (int)((float)max*10/min);
                if(value>15){
                    quality = 80;
                }else{
                    quality = 90;
                }
            } catch (FileNotFoundException e) {
                System.gc();
            } catch(OutOfMemoryError e){
                System.gc();
                e.printStackTrace();
            }
            // 2:rotate bitmap
            if(f.exists()){
                f.delete();
            }
            if(rotate>0){
                Matrix mtx = new Matrix();
                mtx.postRotate(rotate);
                try{
                    Bitmap roateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), mtx, true);
                    if(roateBitmap!=null && roateBitmap!=bitmap){
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                        bitmap = roateBitmap;
                    }
                }catch(OutOfMemoryError e){
                    System.gc();
                    e.printStackTrace();
                }
            }


            /*
            压缩后的bitmap  保存到指定的路径
            // 3:save bitmap
            FileOutputStream fileOutputStream = new FileOutputStream(f.getPath());
            BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            bitmap.recycle();
            os.flush();
            os.close();
            */

            System.gc();
            return bitmap;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }



    //--------------------------------------------------------------------------------------------

}

package com.trust.shengyu.rentalcarclient.tools.gdgps;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.trust.shengyu.rentalcarclient.Config;
import com.trust.shengyu.rentalcarclient.tools.L;

/**
 * Created by Trust on 2017/5/16.
 */

public class Positioning {
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private LatLonPoint date;
//    private PositionCallBack positionCallBack;

    public Positioning() {
//        this.positionCallBack = positionCallBack;
        init();
    }

    public void init(){
        //初始化定位
        mLocationClient = new AMapLocationClient(Config.context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        mLocationOption.setHttpTimeOut(20000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
    }

    public void startGps(){
        //启动定位
        mLocationClient.startLocation();
    }


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
//                date = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
//                positionCallBack.positionCallBack(date);
                positioningListener.onLocationChanged(aMapLocation);
                L.d("定位成功");
            } else {
                L.e("定位失败:"+aMapLocation.getErrorCode());
                positioningListener.onLocationError(aMapLocation);
            }
        }
    };

    public interface PositioningListener{
        void onLocationChanged (AMapLocation aMapLocation);
        void onLocationError(AMapLocation aMapLocation);
    }

    public PositioningListener positioningListener;

    public void setPostitioningListener(PositioningListener positioningListener){
        this.positioningListener = positioningListener;
    }

}

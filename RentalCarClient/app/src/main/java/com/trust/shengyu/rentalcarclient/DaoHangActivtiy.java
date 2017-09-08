package com.trust.shengyu.rentalcarclient;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.trust.shengyu.rentalcarclient.activitys.mainmap.ErrorInfo;
import com.trust.shengyu.rentalcarclient.activitys.mainmap.TTSController;
import com.trust.shengyu.rentalcarclient.tools.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trust on 2017/9/1.
 */

public class DaoHangActivtiy extends AppCompatActivity implements AMapNaviListener, AMapNaviViewListener {
    protected AMapNaviView mAMapNaviView;
    protected AMapNavi mAMapNavi;
    protected TTSController mTtsManager;
    protected NaviLatLng mEndLatlng = new NaviLatLng(31.366701,121.318896);
    protected NaviLatLng mStartLatlng = new NaviLatLng(31.248546,121.44352);
    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    protected List<NaviLatLng> mWayPointList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //实例化语音引擎
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();

        //
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);

        //设置模拟导航的行车速度
        mAMapNavi.setEmulatorNaviSpeed(75);
        sList.add(mStartLatlng);
        eList.add(mEndLatlng);
        L.d("onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        mTtsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mTtsManager.destroy();
    }

    @Override
    public void onInitNaviFailure() {
        L.d("onInitNaviFailure");
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess() {
        //初始化成功
        L.d("onInitNaviSuccess");
    }

    @Override
    public void onStartNavi(int type) {
        //开始导航回调
        L.d("onStartNavi");
    }

    @Override
    public void onTrafficStatusUpdate() {
        //
        L.d("onTrafficStatusUpdate");
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        NaviLatLng coord = location.getCoord();
        //当前位置回调
        mStartLatlng.setLatitude(coord.getLatitude());
        mStartLatlng.setLongitude(coord.getLongitude());
        L.d("onLocationChange");
    }

    @Override
    public void onGetNavigationText(int type, String text) {
        //播报类型和播报文字回调
        L.d("onGetNavigationText");
    }

    @Override
    public void onGetNavigationText(String s) {
        L.d("onGetNavigationText");
    }

    @Override
    public void onEndEmulatorNavi() {
        //结束模拟导航
        L.d("onEndEmulatorNavi");
    }

    @Override
    public void onArriveDestination() {
        //到达目的地
        L.d("onArriveDestination");
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        L.d("onCalculateRouteFailure");
        L.d("路线计算失败：错误码=" + errorInfo + ",Error Message= " + ErrorInfo.getError(errorInfo));
        L.d("onCalculateRouteFailure");
        L.d("onCalculateRouteFailure");
        L.d("onCalculateRouteFailure");
        //路线计算失败
        Log.e("dm", "--------------------------------------------");
        Log.i("dm", "路线计算失败：错误码=" + errorInfo + ",Error Message= " + ErrorInfo.getError(errorInfo));
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
        Toast.makeText(this, "errorInfo：" + errorInfo + ",Message：" + ErrorInfo.getError(errorInfo), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
        L.d("onReCalculateRouteForYaw");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
        L.d("onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        //到达途径点
        L.d("onArrivedWayPoint");
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        //GPS开关状态回调
        L.d("onGpsOpenStatus");
    }

    @Override
    public void onNaviSetting() {
        //底部导航设置点击回调
        L.d("onNaviSetting");
    }

    @Override
    public void onNaviMapMode(int isLock) {
        //地图的模式，锁屏或锁车
        L.d("onNaviMapMode");
    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {
        //转弯view的点击回调
        L.d("onInitNaviFailure");
    }

    @Override
    public void onNextRoadClick() {
        //下一个道路View点击回调
        L.d("onInitNaviFailure");
    }


    @Override
    public void onScanViewButtonClick() {
        //全览按钮点击回调
        L.d("onInitNaviFailure");
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
        //过时
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        //导航过程中的信息更新，请看NaviInfo的具体说明
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        //已过时
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //已过时
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示转弯回调
    }

    @Override
    public void hideCross() {
        //隐藏转弯回调
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        //显示车道信息

    }

    @Override
    public void hideLaneInfo() {
        //隐藏车道信息
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //多路径算路成功回调
    }

    @Override
    public void notifyParallelRoad(int i) {
        if (i == 0) {
            Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在主辅路过渡");
            return;
        }
        if (i == 1) {
            Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在主路");
            return;
        }
        if (i == 2) {
            Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在辅路");
        }
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        //更新交通设施信息
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        //更新巡航模式的统计信息
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        //更新巡航模式的拥堵信息
    }

    @Override
    public void onPlayRing(int i) {

    }


    @Override
    public void onLockMap(boolean isLock) {
        //锁地图状态发生变化时回调
    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("wlx", "导航页面加载成功");
        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


}

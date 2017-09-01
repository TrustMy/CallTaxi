package com.trust.shengyu.rentalcarclient.activitys.mainmap;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MainMapTestActivity extends BaseActivity {
    private MapView mapView;
    private AMap aMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map_test);
        mapView = (MapView) bindView(this,R.id.mainmaptest_map,R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        /*
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
//以下三种模式从5.1.0版本开始提供
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
           */



        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }


    private ConcurrentMap<Integer, TraceOverlay> mOverlayList = new ConcurrentHashMap<Integer, TraceOverlay>();
    private int mSequenceLineID = 1000;
    private List<TraceLocation> mTraceList;
    private LBSTraceClient mTraceClient;
    private int mCoordinateType = LBSTraceClient.TYPE_AMAP;
    /**
     * 调起一次轨迹纠偏
     */
    public void traceGrasp(){
        TraceOverlay mTraceOverlay = new TraceOverlay(aMap);
        mOverlayList.put(mSequenceLineID, mTraceOverlay);
        List<LatLng> mapList = traceLocationToMap(mTraceList);
        mTraceOverlay.setProperCamera(mapList);
        mTraceClient = new LBSTraceClient(this.getApplicationContext());
        mTraceClient.queryProcessedTrace(mSequenceLineID, mTraceList,
                mCoordinateType,traceListener );
    }

    private TraceListener traceListener = new TraceListener() {

        /**
         * 轨迹纠偏失败回调
         */
        @Override
        public void onRequestFailed(int lineID, String errorInfo) {

        }


        /**
         * 轨迹纠偏过程回调
         */
        @Override
        public void onTraceProcessing(int lineID, int index, List<LatLng> segments) {

        }


        /**
         * 轨迹纠偏结束回调
         */
        @Override
        public void onFinished(int lineID, List<LatLng> linepoints, int distance,
                               int watingtime) {

        }
    };

    /**
     * 轨迹纠偏点转换为地图LatLng
     *
     * @param traceLocationList
     * @return
     */
    public List<LatLng> traceLocationToMap(List<TraceLocation> traceLocationList) {
        List<LatLng> mapList = new ArrayList<LatLng>();
        for (TraceLocation location : traceLocationList) {
            LatLng latlng = new LatLng(location.getLatitude(),
                    location.getLongitude());
            mapList.add(latlng);
        }
        return mapList;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}

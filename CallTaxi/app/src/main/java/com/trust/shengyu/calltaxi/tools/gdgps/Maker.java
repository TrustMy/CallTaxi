package com.trust.shengyu.calltaxi.tools.gdgps;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;


import java.util.ArrayList;

/**
 * Created by Trust on 2017/5/16.
 */

public class Maker {

    public static void showMaker (AMap map, LatLng data ,String title,String msg){
        map.addMarker(new MarkerOptions().
                position(data).
                title(title).snippet(msg)).showInfoWindow();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(data.latitude,data.longitude),//新的中心点坐标
                500, //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
    }

    public static void showMaker(AMap map, LatLng data){
        map.addMarker(new MarkerOptions().
                position(data));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(data.latitude,data.longitude),//新的中心点坐标
                500, //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
    }
    public static void showMakerGif(AMap aMap, LatLng data , int [] imgs , int size){

        MarkerOptions markerOptions = new MarkerOptions();
        ArrayList<BitmapDescriptor> ml = new ArrayList<>();


        for (int i = 0; i < imgs.length; i++) {
            ml.add(BitmapDescriptorFactory.fromResource(imgs[i]));
        }

        markerOptions.icons(ml);
        markerOptions.position(data);
        markerOptions.period(3);

        aMap.addMarker(markerOptions).showInfoWindow();
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(data.latitude,data.longitude),//新的中心点坐标
                size, //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
    }
}

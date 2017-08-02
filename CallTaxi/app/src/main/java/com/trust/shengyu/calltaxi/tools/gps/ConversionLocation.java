package com.trust.shengyu.calltaxi.tools.gps;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.tools.L;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trust on 2017/5/19.
 */

public class ConversionLocation implements GeocodeSearch.OnGeocodeSearchListener {
    private GeocodeSearch geocoderSearch;

    public ConversionLocation() {
        geocoderSearch = new GeocodeSearch(Config.context);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }



    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                L.e("addressName:"+addressName);
                addressListener.getAddress(true,addressName);
            } else {
               L.e("没有搜索到");
            }
        } else {
            L.e("错误吗:"+rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void setAddressListener(ConversionLocationAddressListener conversionLocationAddressListener){
        this.addressListener = conversionLocationAddressListener;
    }


    public interface ConversionLocationAddressListener{
        void getAddress(boolean status,String msg);
        void getAddressList(boolean status,TripAddress tripAddress);
    }

    public ConversionLocationAddressListener addressListener;

    public class TripAddress{
        private String startName,endName;

        public TripAddress(String startName, String endName) {
            this.startName = startName;
            this.endName = endName;
        }

        public String getStartName() {
            return startName;
        }

        public void setStartName(String startName) {
            this.startName = startName;
        }

        public String getEndName() {
            return endName;
        }

        public void setEndName(String endName) {
            this.endName = endName;
        }
    }
}

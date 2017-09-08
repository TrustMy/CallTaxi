package com.trust.shengyu.rentalcarclient.tools.beans.oldbeans;

import com.amap.api.maps.model.LatLng;

/**
 * Created by Trust on 2017/9/6.
 */

public class TakeTheCarBean {
    private LatLng dataLatLng;
    private String info;

    public TakeTheCarBean(LatLng dataLatLng, String info) {
        this.dataLatLng = dataLatLng;
        this.info = info;
    }

    public LatLng getDataLatLng() {
        return dataLatLng;
    }

    public void setDataLatLng(LatLng dataLatLng) {
        this.dataLatLng = dataLatLng;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

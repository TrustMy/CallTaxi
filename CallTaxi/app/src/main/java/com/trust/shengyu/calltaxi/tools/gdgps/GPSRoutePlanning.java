package com.trust.shengyu.calltaxi.tools.gdgps;


import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.gdgps.gpsconfig.WalkRouteOverlay;


/**
 * Created by Trust on 2016/12/17.
 */
public class GPSRoutePlanning implements RouteSearch.OnRouteSearchListener{

    private RouteSearch routeSearch;
    private Context context = Config.context;
    private RouteSearch.FromAndTo fromAndTol;
    private LatLonPoint startLat;
    private LatLonPoint endLat;
    private AMap aMap;




//    private AMapNavi mAMapNavi;


    public GPSRoutePlanning( LatLonPoint start, LatLonPoint end, AMap aMap) {

        this.startLat = start;
        this.endLat = end;
        this.aMap = aMap;

        fromAndTol = new RouteSearch.FromAndTo(this.startLat,this.endLat);
        init();
    }

    private void init() {
        routeSearch = new RouteSearch(context);
        routeSearch.setRouteSearchListener(this);

        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTol, 1);
        routeSearch.calculateWalkRouteAsyn(query);//开始算路



//        //获取AMapNavi实例
//        mAMapNavi = AMapNavi.getInstance(context);
//        //添加监听回调，用于处理算路成功
//        mAMapNavi.addAMapNaviListener(this);


    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        L.i("onBusRouteSearched");
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        L.i("onDriveRouteSearched");
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {


       if (i == 3003) {
//           T.showToast(context, "距离车辆位置过长!");
        }else if(i ==3001)
        {
//            T.showToast(context,"附近搜不到路!");
        }else if(i == 3002)
        {
//            T.showToast(context,"路线计算失败!");
        }else if(i == 3000)
        {
//            T.showToast(context,"不在中国陆地范围内!");
        }else
        {

            WalkPath walkPath = walkRouteResult.getPaths().get(0);


            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(context,
                    aMap, walkPath, walkRouteResult.getStartPos(),
                    walkRouteResult.getTargetPos());
            walkRouteOverlay.removeFromMap();
            walkRouteOverlay.addToMap();
            walkRouteOverlay.zoomToSpan();
        }




    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        L.d("onRideRouteSearched");
    }

    public void setFromAndTo(LatLonPoint start , LatLonPoint end){
        this.startLat = start;
        this.endLat = end;
    }


}

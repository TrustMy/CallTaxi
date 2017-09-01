package com.trust.shengyu.rentalcarclient.tools.gdgps.routeplan;

import com.amap.api.maps.AMap;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.trust.shengyu.rentalcarclient.Config;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.gdgps.overlay.DrivingRouteOverlay;
import com.trust.shengyu.rentalcarclient.tools.gdgps.util.AMapUtil;

/**
 * Created by Trust on 2017/8/3.
 */

public class RoutePlan implements RouteSearch.OnRouteSearchListener {
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private AMap mAmap;
    public RoutePlan(AMap aMap) {
        mRouteSearch = new RouteSearch(Config.context);
        mRouteSearch.setRouteSearchListener(this);
        mAmap = aMap;
    }
    public DrivingRouteOverlay drivingRouteOverlay;
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult( LatLonPoint mStartPoint , LatLonPoint mEndPoint ) {
        if (mStartPoint == null) {
//            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
//            ToastUtil.show(mContext, "终点未设置");
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);

            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询

    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mAmap.clear();
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);

                    drivingRouteOverlay = new DrivingRouteOverlay(
                            Config.context, mAmap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();

                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";

                    double taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    L.d("打车需要:"+taxiCost+"元");
                    routePlanListener.result(taxiCost);
                } else if (result != null && result.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }


    public interface onRoutePlanListener{
        void result(Object money);
    }

    private onRoutePlanListener routePlanListener;

    public void setOnRoutePlanListener (onRoutePlanListener routePlanListener){
        this.routePlanListener = routePlanListener;
    }
}

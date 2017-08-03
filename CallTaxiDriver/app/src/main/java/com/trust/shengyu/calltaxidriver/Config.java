package com.trust.shengyu.calltaxidriver;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Trust on 2017/8/1.
 */

public class Config {
    public static Context context ;
    public static Activity activity;
    public static boolean needAdd = true;
    public static boolean noAdd = false;
    public static String token;

    public final static int SUCCESS = 0;
    public final static int ERROR = 1;




    //-----------------------基本配置参数-------------------------------

    public static int ClickTheInterval = 2;//点击间隔 防止连续点击
}

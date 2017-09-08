package com.trust.shengyu.rentalcarclient;

/**
 * Created by Trust on 2017/9/4.
 */

public interface RentalcarClientConfig {
    //user 参数
    String PHONE = Config.PHONE;



    //红包状态
    int USED = 0,UNUSED = 1,EXPIRED = 2;  // 已使用   未使用  已过期


    String MSG_USER_NAME = "请输入姓名!";
    String MSG_USER_ID_NAME = "请输入身份证号!";
    String MSG_DRIVER_TIME = "请选择日期!";
    String MSG_CHOOSE_IMG = "请选择图片!";


    int USER_STATUS_NOT_CERTIFIED = 0;//用户未认证
    int USER_STATUS_CERTIFIED_ING = 1;//用户认证中
    int USER_STATUS_CERTIFIED_SUCCESS = 2;//用户认证过
    int USER_STATUS_CERTIFIED_ERROR = 3;//用户认证未通过

    //url
    String URL_SERVER = Config.SERVER_URL;//主url
    String URL_REGISTER = Config.REGISTER;//注册
    String URL_VERIFICATION_CODE = Config.VERIFICATION_CODE;//验证码
    String URL_RETRIEVE_THE_PASSWORD = Config.RETRIEVE_THE_PASSWORD;//找回密码
    String URL_LOGIN = Config.LOGIN;//登录
    String URL_GET_USER_INFO = Config.GET_USER_INFO;//获取用户信息
    String URL_IMPROVE_USER_INFO = Config.IMPROVE_USER_INFO;//用户信息完善
    String URL_GET_USER_COUPON = Config.GET_USER_COUPON;//用户优惠劵

    //url tag
    int TAG_URL_REGISTER = 0x00001;//注册
    int TAG_URL_VERIFICATION_CODE = 0x00002;//验证码
    int TAG_URL_RETRIEVE_THE_PASSWORD = 0x00003;//找回密码
    int TAG_URL_LOGIN = 0x00004;//登录
    int TAG_URL_GET_USER_INFO = 0x00005;//获取用户信息
    int TAG_URL_IMPROVE_USER_INFO = 0x00006;//用户信息完善
    int TAG_URL_GET_USER_COUPON = 0x00007;//用户优惠劵


}

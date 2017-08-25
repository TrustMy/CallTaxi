package com.trust.shengyu.calltaxidriver.tools.beans;

import java.io.Serializable;

/**
 * Created by Trust on 2017/8/25.
 */

public class SelectOrdersBean extends Bean implements Serializable {

    /**
     * status : 1
     * info : 订单查询成功
     * code :
     * content : {"uid":205,"orderNo":"201708251343240001","locationLat":31.232185,"locationLng":121.413141,"startAddress":"上海市普陀区顺义路10号靠近普陀长风开业园区","endAddress":"天安门","customer":"5001","driver":"1001","receiveTime":"Aug 25, 2017 1:40:31 PM","status":2,"estimateDuration":2000,"estimatesAmount":4521.1,"createTime":"Aug 25, 2017 1:43:25 PM","updateTime":"Aug 25, 2017 1:43:28 PM"}
     */

    private int status;
    private String info;
    private String code;
    private ContentBean content;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }

    public ContentBean getContent() {
        return content;
    }

    public static class ContentBean implements Serializable{
        /**
         * uid : 205
         * orderNo : 201708251343240001
         * locationLat : 31.232185
         * locationLng : 121.413141
         * startAddress : 上海市普陀区顺义路10号靠近普陀长风开业园区
         * endAddress : 天安门
         * customer : 5001
         * driver : 1001
         * receiveTime : Aug 25, 2017 1:40:31 PM
         * status : 2
         * estimateDuration : 2000
         * estimatesAmount : 4521.1
         * createTime : Aug 25, 2017 1:43:25 PM
         * updateTime : Aug 25, 2017 1:43:28 PM
         */

        private int uid;
        private String orderNo;
        private double locationLat;
        private double locationLng;
        private String startAddress;
        private String endAddress;
        private String customer;
        private String driver;
        private String receiveTime;
        private int status;
        private int estimateDuration;
        private double estimatesAmount;
        private String createTime;
        private String updateTime;

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public void setLocationLat(double locationLat) {
            this.locationLat = locationLat;
        }

        public void setLocationLng(double locationLng) {
            this.locationLng = locationLng;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public void setEndAddress(String endAddress) {
            this.endAddress = endAddress;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setEstimateDuration(int estimateDuration) {
            this.estimateDuration = estimateDuration;
        }

        public void setEstimatesAmount(double estimatesAmount) {
            this.estimatesAmount = estimatesAmount;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUid() {
            return uid;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public double getLocationLat() {
            return locationLat;
        }

        public double getLocationLng() {
            return locationLng;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public String getEndAddress() {
            return endAddress;
        }

        public String getCustomer() {
            return customer;
        }

        public String getDriver() {
            return driver;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public int getStatus() {
            return status;
        }

        public int getEstimateDuration() {
            return estimateDuration;
        }

        public double getEstimatesAmount() {
            return estimatesAmount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }
    }
}

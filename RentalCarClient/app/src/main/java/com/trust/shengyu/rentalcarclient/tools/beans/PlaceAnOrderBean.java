package com.trust.shengyu.rentalcarclient.tools.beans;

/**
 * Created by Trust on 2017/8/17.
 */

public class PlaceAnOrderBean extends Bean {

    /**
     * status : 1
     * info : 订单预定成功
     * code :
     * content : {"uid":42,"orderNo":"201708171354510004","startAddress":"上海市普陀区顺义路10号靠近普陀长风开业园区","endAddress":"上海市普陀区长风新村街道上海外国语大学夜大学十分部福兴村附近","startLat":31.230086,"startLng":121.417475,"endLat":31.230087,"endLng":121.417465,"customer":"client_789","status":0,"estimateDuration":2000,"estimatesAmount":1.4000000000000001,"createTime":"Aug 17, 2017 1:54:51 PM"}
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

    public static class ContentBean  {
        /**
         * uid : 42
         * orderNo : 201708171354510004
         * startAddress : 上海市普陀区顺义路10号靠近普陀长风开业园区
         * endAddress : 上海市普陀区长风新村街道上海外国语大学夜大学十分部福兴村附近
         * startLat : 31.230086
         * startLng : 121.417475
         * endLat : 31.230087
         * endLng : 121.417465
         * customer : client_789
         * status : 0
         * estimateDuration : 2000
         * estimatesAmount : 1.4000000000000001
         * createTime : Aug 17, 2017 1:54:51 PM
         */

        private int uid;
        private String orderNo;
        private String startAddress;
        private String endAddress;
        private double startLat;
        private double startLng;
        private double endLat;
        private double endLng;
        private String customer;
        private int status;
        private int estimateDuration;
        private double estimatesAmount;
        private String createTime;

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public void setEndAddress(String endAddress) {
            this.endAddress = endAddress;
        }

        public void setStartLat(double startLat) {
            this.startLat = startLat;
        }

        public void setStartLng(double startLng) {
            this.startLng = startLng;
        }

        public void setEndLat(double endLat) {
            this.endLat = endLat;
        }

        public void setEndLng(double endLng) {
            this.endLng = endLng;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
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

        public int getUid() {
            return uid;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public String getEndAddress() {
            return endAddress;
        }

        public double getStartLat() {
            return startLat;
        }

        public double getStartLng() {
            return startLng;
        }

        public double getEndLat() {
            return endLat;
        }

        public double getEndLng() {
            return endLng;
        }

        public String getCustomer() {
            return customer;
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
    }
}

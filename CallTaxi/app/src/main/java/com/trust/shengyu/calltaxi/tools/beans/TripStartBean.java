package com.trust.shengyu.calltaxi.tools.beans;

/**
 * Created by Trust on 2017/8/23.
 */

public class TripStartBean {

    /**
     * status : 1
     * info : 司机开始订单
     * type : 5
     * content : {"driver":{"uid":4,"cellPhone":"11111111111","password":"111111","driverId":1001,"nickName":"9b6d78GK3B","sex":0,"token":"p99JLa5AV+thOomEZMkKe3ObVfoE+KK6noXEoL6H85ZJyfCJeAbpqawjSeWUUneZ","regTime":"Aug 21, 2017 6:53:52 PM","status":1,"onlineState":3,"updateTime":"Aug 23, 2017 1:27:30 PM"},"order":{"uid":61,"orderNo":"201708231327370019","locationLat":31.232185,"locationLng":121.413141,"startAddress":"上海市普陀区顺义路10号靠近普陀长风开业园区","endAddress":"王府井(地铁站)","startLat":31.23255166666667,"startLng":121.412505,"customer":"5001","driver":"1001","receiveTime":"Aug 23, 2017 1:24:48 PM","startTime":"Aug 23, 2017 1:25:03 PM","status":3,"estimateDuration":2000,"estimatesAmount":4514.1,"createTime":"Aug 23, 2017 1:27:38 PM","updateTime":"Aug 23, 2017 1:28:00 PM"}}
     */

    private int status;
    private String info;
    private int type;
    private ContentBean content;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getType() {
        return type;
    }

    public ContentBean getContent() {
        return content;
    }

    public static class ContentBean {
        /**
         * driver : {"uid":4,"cellPhone":"11111111111","password":"111111","driverId":1001,"nickName":"9b6d78GK3B","sex":0,"token":"p99JLa5AV+thOomEZMkKe3ObVfoE+KK6noXEoL6H85ZJyfCJeAbpqawjSeWUUneZ","regTime":"Aug 21, 2017 6:53:52 PM","status":1,"onlineState":3,"updateTime":"Aug 23, 2017 1:27:30 PM"}
         * order : {"uid":61,"orderNo":"201708231327370019","locationLat":31.232185,"locationLng":121.413141,"startAddress":"上海市普陀区顺义路10号靠近普陀长风开业园区","endAddress":"王府井(地铁站)","startLat":31.23255166666667,"startLng":121.412505,"customer":"5001","driver":"1001","receiveTime":"Aug 23, 2017 1:24:48 PM","startTime":"Aug 23, 2017 1:25:03 PM","status":3,"estimateDuration":2000,"estimatesAmount":4514.1,"createTime":"Aug 23, 2017 1:27:38 PM","updateTime":"Aug 23, 2017 1:28:00 PM"}
         */

        private DriverBean driver;
        private OrderBean order;

        public void setDriver(DriverBean driver) {
            this.driver = driver;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public DriverBean getDriver() {
            return driver;
        }

        public OrderBean getOrder() {
            return order;
        }

        public static class DriverBean {
            /**
             * uid : 4
             * cellPhone : 11111111111
             * password : 111111
             * driverId : 1001
             * nickName : 9b6d78GK3B
             * sex : 0
             * token : p99JLa5AV+thOomEZMkKe3ObVfoE+KK6noXEoL6H85ZJyfCJeAbpqawjSeWUUneZ
             * regTime : Aug 21, 2017 6:53:52 PM
             * status : 1
             * onlineState : 3
             * updateTime : Aug 23, 2017 1:27:30 PM
             */

            private int uid;
            private String cellPhone;
            private String password;
            private int driverId;
            private String nickName;
            private int sex;
            private String token;
            private String regTime;
            private int status;
            private int onlineState;
            private String updateTime;

            public void setUid(int uid) {
                this.uid = uid;
            }

            public void setCellPhone(String cellPhone) {
                this.cellPhone = cellPhone;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public void setDriverId(int driverId) {
                this.driverId = driverId;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public void setRegTime(String regTime) {
                this.regTime = regTime;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setOnlineState(int onlineState) {
                this.onlineState = onlineState;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getUid() {
                return uid;
            }

            public String getCellPhone() {
                return cellPhone;
            }

            public String getPassword() {
                return password;
            }

            public int getDriverId() {
                return driverId;
            }

            public String getNickName() {
                return nickName;
            }

            public int getSex() {
                return sex;
            }

            public String getToken() {
                return token;
            }

            public String getRegTime() {
                return regTime;
            }

            public int getStatus() {
                return status;
            }

            public int getOnlineState() {
                return onlineState;
            }

            public String getUpdateTime() {
                return updateTime;
            }
        }

        public static class OrderBean {
            /**
             * uid : 61
             * orderNo : 201708231327370019
             * locationLat : 31.232185
             * locationLng : 121.413141
             * startAddress : 上海市普陀区顺义路10号靠近普陀长风开业园区
             * endAddress : 王府井(地铁站)
             * startLat : 31.23255166666667
             * startLng : 121.412505
             * customer : 5001
             * driver : 1001
             * receiveTime : Aug 23, 2017 1:24:48 PM
             * startTime : Aug 23, 2017 1:25:03 PM
             * status : 3
             * estimateDuration : 2000
             * estimatesAmount : 4514.1
             * createTime : Aug 23, 2017 1:27:38 PM
             * updateTime : Aug 23, 2017 1:28:00 PM
             */

            private int uid;
            private String orderNo;
            private double locationLat;
            private double locationLng;
            private String startAddress;
            private String endAddress;
            private double startLat;
            private double startLng;
            private String customer;
            private String driver;
            private String receiveTime;
            private String startTime;
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

            public void setStartLat(double startLat) {
                this.startLat = startLat;
            }

            public void setStartLng(double startLng) {
                this.startLng = startLng;
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

            public void setStartTime(String startTime) {
                this.startTime = startTime;
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

            public double getStartLat() {
                return startLat;
            }

            public double getStartLng() {
                return startLng;
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

            public String getStartTime() {
                return startTime;
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
}

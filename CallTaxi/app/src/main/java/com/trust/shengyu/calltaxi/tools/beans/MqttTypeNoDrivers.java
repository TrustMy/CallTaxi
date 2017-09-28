package com.trust.shengyu.calltaxi.tools.beans;

/**
 * Created by Trust on 2017/9/11.
 */

public class MqttTypeNoDrivers extends Bean{

    /**
     * status : 0
     * info : 没有匹配到合适司机
     * type : 1
     * content : {"order":{"uid":286,"orderNo":"201709111646470031","locationLat":31.232186,"locationLng":121.4132,"startAddress":"上海市普陀区顺义路454号靠近普陀长风开业园区","endAddress":"哦纱玳设计中心","customer":"5001","status":0,"estimateDuration":2000,"estimatesAmount":18.1,"createTime":"Sep 11, 2017 4:46:47 PM"},"customer":{"uid":1,"nickName":"3B7o5IB0D0","customerId":5001,"sex":0,"cellPhone":"13892929789","password":"1111","status":0,"token":"MS8gxxR9lhjv/jEvcCx6RVVUHdG4c9/n/i4xNP49bH5lHU59Dj4KkLMA6Sq2QIOv","regTime":"Sep 7, 2017 10:36:59 AM","updateTime":"Sep 11, 2017 4:46:27 PM"}}
     */

    private int status;
    private String info;
    private int type;
    private ContentBean content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * order : {"uid":286,"orderNo":"201709111646470031","locationLat":31.232186,"locationLng":121.4132,"startAddress":"上海市普陀区顺义路454号靠近普陀长风开业园区","endAddress":"哦纱玳设计中心","customer":"5001","status":0,"estimateDuration":2000,"estimatesAmount":18.1,"createTime":"Sep 11, 2017 4:46:47 PM"}
         * customer : {"uid":1,"nickName":"3B7o5IB0D0","customerId":5001,"sex":0,"cellPhone":"13892929789","password":"1111","status":0,"token":"MS8gxxR9lhjv/jEvcCx6RVVUHdG4c9/n/i4xNP49bH5lHU59Dj4KkLMA6Sq2QIOv","regTime":"Sep 7, 2017 10:36:59 AM","updateTime":"Sep 11, 2017 4:46:27 PM"}
         */

        private OrderBean order;
        private CustomerBean customer;

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public CustomerBean getCustomer() {
            return customer;
        }

        public void setCustomer(CustomerBean customer) {
            this.customer = customer;
        }

        public static class OrderBean {
            /**
             * uid : 286
             * orderNo : 201709111646470031
             * locationLat : 31.232186
             * locationLng : 121.4132
             * startAddress : 上海市普陀区顺义路454号靠近普陀长风开业园区
             * endAddress : 哦纱玳设计中心
             * customer : 5001
             * status : 0
             * estimateDuration : 2000
             * estimatesAmount : 18.1
             * createTime : Sep 11, 2017 4:46:47 PM
             */

            private int uid;
            private String orderNo;
            private double locationLat;
            private double locationLng;
            private String startAddress;
            private String endAddress;
            private String customer;
            private int status;
            private int estimateDuration;
            private double estimatesAmount;
            private String createTime;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public double getLocationLat() {
                return locationLat;
            }

            public void setLocationLat(double locationLat) {
                this.locationLat = locationLat;
            }

            public double getLocationLng() {
                return locationLng;
            }

            public void setLocationLng(double locationLng) {
                this.locationLng = locationLng;
            }

            public String getStartAddress() {
                return startAddress;
            }

            public void setStartAddress(String startAddress) {
                this.startAddress = startAddress;
            }

            public String getEndAddress() {
                return endAddress;
            }

            public void setEndAddress(String endAddress) {
                this.endAddress = endAddress;
            }

            public String getCustomer() {
                return customer;
            }

            public void setCustomer(String customer) {
                this.customer = customer;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getEstimateDuration() {
                return estimateDuration;
            }

            public void setEstimateDuration(int estimateDuration) {
                this.estimateDuration = estimateDuration;
            }

            public double getEstimatesAmount() {
                return estimatesAmount;
            }

            public void setEstimatesAmount(double estimatesAmount) {
                this.estimatesAmount = estimatesAmount;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }

        public static class CustomerBean {
            /**
             * uid : 1
             * nickName : 3B7o5IB0D0
             * customerId : 5001
             * sex : 0
             * cellPhone : 13892929789
             * password : 1111
             * status : 0
             * token : MS8gxxR9lhjv/jEvcCx6RVVUHdG4c9/n/i4xNP49bH5lHU59Dj4KkLMA6Sq2QIOv
             * regTime : Sep 7, 2017 10:36:59 AM
             * updateTime : Sep 11, 2017 4:46:27 PM
             */

            private int uid;
            private String nickName;
            private int customerId;
            private int sex;
            private String cellPhone;
            private String password;
            private int status;
            private String token;
            private String regTime;
            private String updateTime;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getCustomerId() {
                return customerId;
            }

            public void setCustomerId(int customerId) {
                this.customerId = customerId;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getCellPhone() {
                return cellPhone;
            }

            public void setCellPhone(String cellPhone) {
                this.cellPhone = cellPhone;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getRegTime() {
                return regTime;
            }

            public void setRegTime(String regTime) {
                this.regTime = regTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}

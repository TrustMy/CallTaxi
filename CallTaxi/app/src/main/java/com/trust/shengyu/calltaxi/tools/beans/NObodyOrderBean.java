package com.trust.shengyu.calltaxi.tools.beans;

/**
 * Created by Trust on 2017/8/24.
 */

public class NObodyOrderBean  {

    /**
     * status : 0
     * info : 没有匹配到合适司机
     * type : 1
     * content : {"customer":{"uid":14,"nickName":"W2J27RJC97","customerId":5001,"sex":0,"cellPhone":"13892929789","password":"111111","status":0,"token":"MS8gxxR9lhjv/jEvcCx6RdsjbSZXhF5uxgkxX3n+iHR/aME7NbaVpYjSDXyFODSK","regTime":"Aug 22, 2017 3:22:42 PM","updateTime":"Aug 24, 2017 10:17:25 AM"},"order":{"uid":101,"orderNo":"201708241017440000","locationLat":31.232185,"locationLng":121.413141,"startAddress":"上海市普陀区顺义路10号靠近普陀长风开业园区","endAddress":"天安门","customer":"5001","status":0,"estimateDuration":2000,"estimatesAmount":4521.1,"createTime":"Aug 24, 2017 10:17:44 AM"}}
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
         * customer : {"uid":14,"nickName":"W2J27RJC97","customerId":5001,"sex":0,"cellPhone":"13892929789","password":"111111","status":0,"token":"MS8gxxR9lhjv/jEvcCx6RdsjbSZXhF5uxgkxX3n+iHR/aME7NbaVpYjSDXyFODSK","regTime":"Aug 22, 2017 3:22:42 PM","updateTime":"Aug 24, 2017 10:17:25 AM"}
         * order : {"uid":101,"orderNo":"201708241017440000","locationLat":31.232185,"locationLng":121.413141,"startAddress":"上海市普陀区顺义路10号靠近普陀长风开业园区","endAddress":"天安门","customer":"5001","status":0,"estimateDuration":2000,"estimatesAmount":4521.1,"createTime":"Aug 24, 2017 10:17:44 AM"}
         */

        private CustomerBean customer;
        private OrderBean order;

        public void setCustomer(CustomerBean customer) {
            this.customer = customer;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public CustomerBean getCustomer() {
            return customer;
        }

        public OrderBean getOrder() {
            return order;
        }

        public static class CustomerBean {
            /**
             * uid : 14
             * nickName : W2J27RJC97
             * customerId : 5001
             * sex : 0
             * cellPhone : 13892929789
             * password : 111111
             * status : 0
             * token : MS8gxxR9lhjv/jEvcCx6RdsjbSZXhF5uxgkxX3n+iHR/aME7NbaVpYjSDXyFODSK
             * regTime : Aug 22, 2017 3:22:42 PM
             * updateTime : Aug 24, 2017 10:17:25 AM
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

            public void setUid(int uid) {
                this.uid = uid;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public void setCustomerId(int customerId) {
                this.customerId = customerId;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public void setCellPhone(String cellPhone) {
                this.cellPhone = cellPhone;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public void setRegTime(String regTime) {
                this.regTime = regTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getUid() {
                return uid;
            }

            public String getNickName() {
                return nickName;
            }

            public int getCustomerId() {
                return customerId;
            }

            public int getSex() {
                return sex;
            }

            public String getCellPhone() {
                return cellPhone;
            }

            public String getPassword() {
                return password;
            }

            public int getStatus() {
                return status;
            }

            public String getToken() {
                return token;
            }

            public String getRegTime() {
                return regTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }
        }

        public static class OrderBean {
            /**
             * uid : 101
             * orderNo : 201708241017440000
             * locationLat : 31.232185
             * locationLng : 121.413141
             * startAddress : 上海市普陀区顺义路10号靠近普陀长风开业园区
             * endAddress : 天安门
             * customer : 5001
             * status : 0
             * estimateDuration : 2000
             * estimatesAmount : 4521.1
             * createTime : Aug 24, 2017 10:17:44 AM
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
}

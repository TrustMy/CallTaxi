package com.trust.shengyu.calltaxi.tools.beans;

/**
 * Created by Trust on 2017/8/22.
 */

public class UserBean {


    /**
     * status : 1
     * info : 信息获取成功
     * code :
     * content : {"uid":1,"nickName":"3B7o5IB0D0","customerId":5001,"sex":0,"cellPhone":"13892929789","password":"1111","status":0,"token":"MS8gxxR9lhjv/jEvcCx6RQCHLLlmXy7gINsK/riTa6plHU59Dj4KkLMA6Sq2QIOv","regTime":"Sep 7, 2017 10:36:59 AM","updateTime":"Sep 12, 2017 10:07:35 AM"}
     */

    private int status;
    private String info;
    private String code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * uid : 1
         * nickName : 3B7o5IB0D0
         * customerId : 5001
         * sex : 0
         * cellPhone : 13892929789
         * password : 1111
         * status : 0
         * token : MS8gxxR9lhjv/jEvcCx6RQCHLLlmXy7gINsK/riTa6plHU59Dj4KkLMA6Sq2QIOv
         * regTime : Sep 7, 2017 10:36:59 AM
         * updateTime : Sep 12, 2017 10:07:35 AM
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

package com.trust.shengyu.calltaxidriver.tools.beans;

/**
 * Created by Trust on 2017/8/22.
 */

public class DriverInformation {

    /**
     * status : 1
     * info : 信息获取成功
     * code :
     * content : {"uid":5,"cellPhone":"22222222222","password":"111111","driverId":1002,"nickName":"G22939w03a","sex":0,"token":"sCXELFe89TAM9cqRIn+Yt9lKUjBef8sz23t0H/aMvj3Hr4Ofp7GUDpl9YnAJ/E76","regTime":"Aug 21, 2017 6:54:12 PM","status":0,"updateTime":"Aug 22, 2017 10:16:47 AM"}
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

    public static class ContentBean {
        /**
         * uid : 5
         * cellPhone : 22222222222
         * password : 111111
         * driverId : 1002
         * nickName : G22939w03a
         * sex : 0
         * token : sCXELFe89TAM9cqRIn+Yt9lKUjBef8sz23t0H/aMvj3Hr4Ofp7GUDpl9YnAJ/E76
         * regTime : Aug 21, 2017 6:54:12 PM
         * status : 0
         * updateTime : Aug 22, 2017 10:16:47 AM
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

        public String getUpdateTime() {
            return updateTime;
        }
    }
}

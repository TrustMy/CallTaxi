package com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans;

/**
 * Created by Trust on 2017/9/8.
 */

public class ResultUserInfoBean {


    /**
     * status : 1
     * info : 用户信息获取成功
     * code : 300003
     * content : {"uid":1,"cellPhone":"15221664507","customerId":1001,"name":"张三","idCard":"111111111111","faceCard":"222222222","reverseCard":"3333333","driverLicense":"3333333","attachment":"","licenseNumber":"","archiveNumber":"","licenseTime ":"","area":"","detailAddress":"","nickName":"流","gender":0,"headPortrait":"3333","license":"","description":"133333","regTime":"Sep 6, 2017 2:22:56 PM","status":2,"onlineState":0,"appVersion":"3.0.5","appUpdateVersion":"Sep 6, 2017 11:34:14 PM","updateTime":"Sep 6, 2017 11:34:14 PM"}
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
         * cellPhone : 15221664507
         * customerId : 1001
         * name : 张三
         * idCard : 111111111111
         * faceCard : 222222222
         * reverseCard : 3333333
         * driverLicense : 3333333
         * attachment :
         * licenseNumber :
         * archiveNumber :
         * licenseTime  :
         * area :
         * detailAddress :
         * nickName : 流
         * gender : 0
         * headPortrait : 3333
         * license :
         * description : 133333
         * regTime : Sep 6, 2017 2:22:56 PM
         * status : 2
         * onlineState : 0
         * appVersion : 3.0.5
         * appUpdateVersion : Sep 6, 2017 11:34:14 PM
         * updateTime : Sep 6, 2017 11:34:14 PM
         */

        private int uid;
        private String cellPhone;
        private String customerId;
        private String name;
        private String idCard;
        private String faceCard;
        private String reverseCard;
        private String driverLicense;
        private String attachment;
        private String licenseNumber;
        private String archiveNumber;
        private String licenseTime;
        private String area;
        private String detailAddress;
        private String nickName;
        private int gender;
        private String headPortrait;
        private String license;
        private String description;
        private String regTime;
        private int status;
        private int onlineState;
        private String appVersion;
        private String appUpdateVersion;
        private String updateTime;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getCellPhone() {
            return cellPhone;
        }

        public void setCellPhone(String cellPhone) {
            this.cellPhone = cellPhone;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getFaceCard() {
            return faceCard;
        }

        public void setFaceCard(String faceCard) {
            this.faceCard = faceCard;
        }

        public String getReverseCard() {
            return reverseCard;
        }

        public void setReverseCard(String reverseCard) {
            this.reverseCard = reverseCard;
        }

        public String getDriverLicense() {
            return driverLicense;
        }

        public void setDriverLicense(String driverLicense) {
            this.driverLicense = driverLicense;
        }

        public String getAttachment() {
            return attachment;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public String getLicenseNumber() {
            return licenseNumber;
        }

        public void setLicenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
        }

        public String getArchiveNumber() {
            return archiveNumber;
        }

        public void setArchiveNumber(String archiveNumber) {
            this.archiveNumber = archiveNumber;
        }

        public String getLicenseTime() {
            return licenseTime;
        }

        public void setLicenseTime(String licenseTime) {
            this.licenseTime = licenseTime;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getHeadPortrait() {
            return headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getRegTime() {
            return regTime;
        }

        public void setRegTime(String regTime) {
            this.regTime = regTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOnlineState() {
            return onlineState;
        }

        public void setOnlineState(int onlineState) {
            this.onlineState = onlineState;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getAppUpdateVersion() {
            return appUpdateVersion;
        }

        public void setAppUpdateVersion(String appUpdateVersion) {
            this.appUpdateVersion = appUpdateVersion;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}

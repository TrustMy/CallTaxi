package com.trust.shengyu.rentalcarclient.tools.beans;

/**
 * Created by Trust on 2017/8/22.
 */

public class UserBean {


    /**
     * uid : 14
     * nickName : W2J27RJC97
     * customerId : 5001
     * sex : 0
     * cellPhone : 13892929789
     * password : 111111
     * status : 0
     * token : MS8gxxR9lhjv/jEvcCx6RaitSTj+pqr1rMQu/Fw/nKaZQwbJ2iTCS0H6GqH2QMgp
     * regTime : Aug 22, 2017 3:22:42 PM
     * updateTime : Aug 22, 2017 5:05:26 PM
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

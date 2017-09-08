package com.trust.shengyu.rentalcarclient.tools.beans.oldbeans;

/**
 * Created by Trust on 2017/8/8.
 */

public class OrderHistoryBean {
    private long orderTime;
    private int money,tripTime,tripMileage;
    private String licensePlate;

    public OrderHistoryBean(long orderTime, int money, int tripTime, int tripMileage, String licensePlate) {
        this.orderTime = orderTime;
        this.money = money;
        this.tripTime = tripTime;
        this.tripMileage = tripMileage;
        this.licensePlate = licensePlate;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getTripTime() {
        return tripTime;
    }

    public void setTripTime(int tripTime) {
        this.tripTime = tripTime;
    }

    public int getTripMileage() {
        return tripMileage;
    }

    public void setTripMileage(int tripMileage) {
        this.tripMileage = tripMileage;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}

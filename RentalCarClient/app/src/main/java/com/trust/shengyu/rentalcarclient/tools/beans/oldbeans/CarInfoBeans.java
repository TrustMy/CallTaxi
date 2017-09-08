package com.trust.shengyu.rentalcarclient.tools.beans.oldbeans;

/**
 * Created by Trust on 2017/9/6.
 */

public class CarInfoBeans {
    private String carNum,carModels,carMileage,carElectricity;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CarInfoBeans(String carNum, String carModels, String carMileage, String carElectricity,boolean status) {
        this.carNum = carNum;
        this.carModels = carModels;
        this.carMileage = carMileage;
        this.carElectricity = carElectricity;
        this.status = status;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarModels() {
        return carModels;
    }

    public void setCarModels(String carModels) {
        this.carModels = carModels;
    }

    public String getCarMileage() {
        return carMileage;
    }

    public void setCarMileage(String carMileage) {
        this.carMileage = carMileage;
    }

    public String getCarElectricity() {
        return carElectricity;
    }

    public void setCarElectricity(String carElectricity) {
        this.carElectricity = carElectricity;
    }
}

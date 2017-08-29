package com.trust.shengyu.rentalcar.tools.gsons;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Trust on 2017/8/29.
 */

public class DataBeaas implements Serializable {

    /**
     * status : ok
     * message : 登陆成功
     * data : [{"id":"1","age":"15","sex":"女"},{"id":"1","age":"15","sex":"男"}]
     */

    private String status;
    private String message;
    private List<DataBean> data;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        /**
         * id : 1
         * age : 15
         * sex : 女
         */

        private String id;
        private String age;
        private String sex;

        public void setId(String id) {
            this.id = id;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getId() {
            return id;
        }

        public String getAge() {
            return age;
        }

        public String getSex() {
            return sex;
        }
    }
}

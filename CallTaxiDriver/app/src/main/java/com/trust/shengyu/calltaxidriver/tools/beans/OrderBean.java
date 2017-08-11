package com.trust.shengyu.calltaxidriver.tools.beans;

/**
 * Created by Trust on 2017/8/8.
 */

public class OrderBean extends Bean {

    /**
     * msg : {"taxiCast":14,"startName":"上海市普陀区顺义路10号靠近普陀长风开业园区","endName":"上海市普陀区长风新村街道白玉路10号福兴村附近"}
     * status : true
     * type : 1
     */

    private MsgBean msg;
    private boolean status;
    private int type;

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public boolean getStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

    public static class MsgBean {
        /**
         * taxiCast : 14
         * startName : 上海市普陀区顺义路10号靠近普陀长风开业园区
         * endName : 上海市普陀区长风新村街道白玉路10号福兴村附近
         */

        private int taxiCast;
        private String startName;
        private String endName;

        public void setTaxiCast(int taxiCast) {
            this.taxiCast = taxiCast;
        }

        public void setStartName(String startName) {
            this.startName = startName;
        }

        public void setEndName(String endName) {
            this.endName = endName;
        }

        public int getTaxiCast() {
            return taxiCast;
        }

        public String getStartName() {
            return startName;
        }

        public String getEndName() {
            return endName;
        }
    }
}

package com.mxkj.yuanyintang.mainui.newapp.myself;

import java.io.Serializable;

public class TimeLimitPreferential implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"surplus_time":80133}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * surplus_time : 80133
         */

        private int surplus_time;

        public int getSurplus_time() {
            return surplus_time;
        }

        public void setSurplus_time(int surplus_time) {
            this.surplus_time = surplus_time;
        }
    }
}

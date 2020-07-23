package com.mxkj.yuanyintang.mainui.myself.my_income.bean;

import java.util.List;

/**
 * Created by zuixia on 2018/5/23.
 */

public class CashHistoryBean {
    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements com.chad.library.adapter.base.entity.MultiItemEntity {
        /**
         * id : 24
         * fish_num : 10
         * status : 0
         * create_time : 1526468273
         * remark : 农业银行 (11111222) qq123123
         * reason :
         * examine_time :
         * status_desc : 队列中
         */

        private int id;
        private float fish_num;
        private int status;
        private String create_time;
        private String remark;
        private String reason;
        private String examine_time;
        private String status_desc;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getFish_num() {
            return fish_num;
        }

        public void setFish_num(float fish_num) {
            this.fish_num = fish_num;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getExamine_time() {
            return examine_time;
        }

        public void setExamine_time(String examine_time) {
            this.examine_time = examine_time;
        }

        public String getStatus_desc() {
            return status_desc;
        }

        public void setStatus_desc(String status_desc) {
            this.status_desc = status_desc;
        }

        @Override
        public int getItemType() {
            if (status==0){
                return 0;//能取消
            }else{
                return 1;
            }
        }
    }
}

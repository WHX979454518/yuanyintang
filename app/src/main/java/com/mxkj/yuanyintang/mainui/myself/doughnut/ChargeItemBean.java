package com.mxkj.yuanyintang.mainui.myself.doughnut;

import com.mxkj.yuanyintang.mainui.home.homebaseadapter.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zuixia on 2018/5/16.
 */

public class ChargeItemBean implements Serializable {


    /**
     * code : 200
     * msg : success
     * data : {"lists":[{"id":5,"type":1,"coin_num":2000,"price":20,"discount_price":18,"title":"20-2000-an","remark":"xxxx","end_time":"","start_time":""},{"id":4,"type":1,"coin_num":1000,"price":10,"discount_price":9,"title":"10-1000-an","remark":"22","end_time":"","start_time":""}],"setting":{"first_charge":"100","first_remark":"首冲奖励50"}}
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
         * lists : [{"id":5,"type":1,"coin_num":2000,"price":20,"discount_price":18,"title":"20-2000-an","remark":"xxxx","end_time":"","start_time":""},{"id":4,"type":1,"coin_num":1000,"price":10,"discount_price":9,"title":"10-1000-an","remark":"22","end_time":"","start_time":""}]
         * setting : {"first_charge":"100","first_remark":"首冲奖励50"}
         */




        private SettingBean setting;
        private List<ListsBean> lists;

        public SettingBean getSetting() {
            return setting;
        }

        public void setSetting(SettingBean setting) {
            this.setting = setting;
        }

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }


        public static class SettingBean {
            /**
             * first_charge : 100
             * first_remark : 首冲奖励50
             */
            private List<LastPayInfoBean> last_pay_info;

            public List<LastPayInfoBean> getLast_pay_info() {
                return last_pay_info;
            }

            public void setLast_pay_info(List<LastPayInfoBean> last_pay_info) {
                this.last_pay_info = last_pay_info;
            }
            private int first_charge;
            private String first_remark;

            public static class LastPayInfoBean implements Serializable{
                /**
                 * type : 1
                 * desc : 微信
                 * is_last : 0
                 */

                private int type;
                private String desc;
                private int is_last;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public int getIs_last() {
                    return is_last;
                }

                public void setIs_last(int is_last) {
                    this.is_last = is_last;
                }
            }


            public int getFirst_charge() {
                return first_charge;
            }

            public void setFirst_charge(int first_charge) {
                this.first_charge = first_charge;
            }

            public String getFirst_remark() {
                return first_remark;
            }

            public void setFirst_remark(String first_remark) {
                this.first_remark = first_remark;
            }
        }

        public static class ListsBean implements MultiItemEntity {
            /**
             * id : 5
             * type : 1
             * coin_num : 2000
             * price : 20
             * discount_price : 18
             * title : 20-2000-an
             * remark : xxxx
             * end_time :
             * start_time :
             */

            private int id;
            private int type;
            private int first_charge;
            private int coin_num;
            private float price;
            private float discount_price;
            private String title;
            private String remark;
            private String end_time;
            private String start_time;
            private boolean isChecked=false;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public int getFirst_charge() {
                return first_charge;
            }

            public void setFirst_charge(int first_charge) {
                this.first_charge = first_charge;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getCoin_num() {
                return coin_num;
            }

            public void setCoin_num(int coin_num) {
                this.coin_num = coin_num;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public float getDiscount_price() {
                return discount_price;
            }

            public void setDiscount_price(float discount_price) {
                this.discount_price = discount_price;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            @Override
            public int getItemType() {
                return 0;
            }
        }

    }
}

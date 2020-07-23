package com.mxkj.yuanyintang.mainui.myself.bean;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/9.
 */

public class MyDoughnutBean {


    /**
     * code : 200
     * msg : success
     * data : {"data":[{"month":"2018-05","month_data":[{"id":460401,"coin_num":1,"reason":"自动登录领取","remark":"WEB端登录","log_at":1,"create_time":"2018-05-25 11:07:17"}]}],"remark":"每日登陆获得1甜甜圈\r\n一次投喂获得1甜甜圈\r\n一次打赏扣除1甜甜圈","my_coin_counts":992007,"today_is_show":0,"today_coin":0,"discount_surplus_second":0}
     */

    private int code;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * data : [{"month":"2018-05","month_data":[{"id":460401,"coin_num":1,"reason":"自动登录领取","remark":"WEB端登录","log_at":1,"create_time":"2018-05-25 11:07:17"}]}]
         * remark : 每日登陆获得1甜甜圈
         一次投喂获得1甜甜圈
         一次打赏扣除1甜甜圈
         * my_coin_counts : 992007
         * today_is_show : 0
         * today_coin : 0
         * discount_surplus_second : 0
         */

        private String remark;
        private String help_url;
        private int my_coin_counts;
        private int today_is_show;
        private int today_coin;
        private int discount_surplus_second;
        private List<DataBean> data;

        public String getHelp_url() {
            return help_url;
        }

        public void setHelp_url(String help_url) {
            this.help_url = help_url;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getMy_coin_counts() {
            return my_coin_counts;
        }

        public void setMy_coin_counts(int my_coin_counts) {
            this.my_coin_counts = my_coin_counts;
        }

        public int getToday_is_show() {
            return today_is_show;
        }

        public void setToday_is_show(int today_is_show) {
            this.today_is_show = today_is_show;
        }

        public int getToday_coin() {
            return today_coin;
        }

        public void setToday_coin(int today_coin) {
            this.today_coin = today_coin;
        }

        public int getDiscount_surplus_second() {
            return discount_surplus_second;
        }

        public void setDiscount_surplus_second(int discount_surplus_second) {
            this.discount_surplus_second = discount_surplus_second;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * month : 2018-05
             * month_data : [{"id":460401,"coin_num":1,"reason":"自动登录领取","remark":"WEB端登录","log_at":1,"create_time":"2018-05-25 11:07:17"}]
             */

            private String month;
            private List<MonthDataBean> month_data;

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public List<MonthDataBean> getMonth_data() {
                return month_data;
            }

            public void setMonth_data(List<MonthDataBean> month_data) {
                this.month_data = month_data;
            }


            public static class MonthDataBean {
                /**
                 * id : 460401
                 * coin_num : 1
                 * reason : 自动登录领取
                 * remark : WEB端登录
                 * log_at : 1
                 * create_time : 2018-05-25 11:07:17
                 */
                public int getItemType() {
                    return itemType;
                }

                public void setItemType(int itemType) {
                    this.itemType = itemType;
                }

                private int itemType;
                private int id;
                private int coin_num;
                private String reason;
                private String month;
                private String remark;
                private int log_at;
                private String create_time;

                public String getMonth() {
                    return month;
                }

                public void setMonth(String month) {
                    this.month = month;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCoin_num() {
                    return coin_num;
                }

                public void setCoin_num(int coin_num) {
                    this.coin_num = coin_num;
                }

                public String getReason() {
                    return reason;
                }

                public void setReason(String reason) {
                    this.reason = reason;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public int getLog_at() {
                    return log_at;
                }

                public void setLog_at(int log_at) {
                    this.log_at = log_at;
                }

                public String getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(String create_time) {
                    this.create_time = create_time;
                }
            }
        }
    }
}

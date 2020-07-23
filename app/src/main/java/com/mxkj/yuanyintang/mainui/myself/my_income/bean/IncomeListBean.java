package com.mxkj.yuanyintang.mainui.myself.my_income.bean;

import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean;

import java.util.List;

/**
 * Created by zuixia on 2018/5/22.
 */

public class IncomeListBean {

    /**
     * code : 200
     * msg : success
     * data : [{"month":"2018-05","month_total":0,"month_data":[{"id":177,"from_type":"3","from_id":123,"create_time":1526542034,"coin_num":1,"uid":50966,"fish_num":-0.01,"reason":"充值到甜甜圈","remark":"兑换1个甜甜圈","month":"2018-05","cash_info":{"status":1,"status_desc":"体现成功","icon":""}}]}]
     */

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

    public static class DataBean {
        /**
         * month : 2018-05
         * month_total : 0
         * month_data : [{"id":177,"from_type":"3","from_id":123,"create_time":1526542034,"coin_num":1,"uid":50966,"fish_num":-0.01,"reason":"充值到甜甜圈","remark":"兑换1个甜甜圈","month":"2018-05","cash_info":{"status":1,"status_desc":"体现成功","icon":""}}]
         */

        private String month;
        private float month_total;
        private List<MonthDataBean> month_data;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public float getMonth_total() {
            return month_total;
        }

        public void setMonth_total(float month_total) {
            this.month_total = month_total;
        }

        public List<MonthDataBean> getMonth_data() {
            return month_data;
        }

        public void setMonth_data(List<MonthDataBean> month_data) {
            this.month_data = month_data;
        }

        public static class MonthDataBean {
            /**
             * id : 177
             * from_type : 3
             * from_id : 123
             * create_time : 1526542034
             * coin_num : 1
             * uid : 50966
             * fish_num : -0.01
             * reason : 充值到甜甜圈
             * remark : 兑换1个甜甜圈
             * month : 2018-05
             * cash_info : {"status":1,"status_desc":"体现成功","icon":""}
             */

            private int id;
            private float month_total;
            private int from_type;
            private int from_id;
            private String create_time;
            private int coin_num;
            private int uid;
            private float fish_num;
            private String reason;
            private String remark;
            private String month;
            private CashInfoBean cash_info;
            private int itemType;
            /**
             * head_info : {"ext":"png","w":"300","h":"300","size":"45684","is_long":"0","md5":"34e634fd2a08ab7f4c3c639fdcf944a6","link":"http://testapi.imxkj.com//image/34e634fd2a08ab7f4c3c639fdcf944a6/3"}
             */

            private HomeBean.ImgpicInfoBean head_info;


            public float getMonth_total() {
                return month_total;
            }

            public void setMonth_total(float month_total) {
                this.month_total = month_total;
            }

            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getFrom_type() {
                return from_type;
            }

            public void setFrom_type(int from_type) {
                this.from_type = from_type;
            }

            public int getFrom_id() {
                return from_id;
            }

            public void setFrom_id(int from_id) {
                this.from_id = from_id;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getCoin_num() {
                return coin_num;
            }

            public void setCoin_num(int coin_num) {
                this.coin_num = coin_num;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public float getFish_num() {
                return fish_num;
            }

            public void setFish_num(float fish_num) {
                this.fish_num = fish_num;
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

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public CashInfoBean getCash_info() {
                return cash_info;
            }

            public void setCash_info(CashInfoBean cash_info) {
                this.cash_info = cash_info;
            }

            public HomeBean.ImgpicInfoBean getHead_info() {
                return head_info;
            }

            public void setHead_info(HomeBean.ImgpicInfoBean head_info) {
                this.head_info = head_info;
            }

            public static class CashInfoBean {
                /**
                 * status : 1
                 * status_desc : 体现成功
                 * icon :
                 */

                private int status;
                private String status_desc;
                private String icon;

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getStatus_desc() {
                    return status_desc;
                }

                public void setStatus_desc(String status_desc) {
                    this.status_desc = status_desc;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }
            }

            public static class HeadInfoBean {
                /**
                 * ext : png
                 * w : 300
                 * h : 300
                 * size : 45684
                 * is_long : 0
                 * md5 : 34e634fd2a08ab7f4c3c639fdcf944a6
                 * link : http://testapi.imxkj.com//image/34e634fd2a08ab7f4c3c639fdcf944a6/3
                 */

                private String ext;
                private String w;
                private String h;
                private String size;
                private String is_long;
                private String md5;
                private String link;

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public String getW() {
                    return w;
                }

                public void setW(String w) {
                    this.w = w;
                }

                public String getH() {
                    return h;
                }

                public void setH(String h) {
                    this.h = h;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getIs_long() {
                    return is_long;
                }

                public void setIs_long(String is_long) {
                    this.is_long = is_long;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }
            }
        }
    }
}

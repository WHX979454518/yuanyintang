package com.mxkj.yuanyintang.mainui.myself.my_income.bean;

import java.io.Serializable;

/**
 * Created by zuixia on 2018/5/21.
 */

public class IncomeHomeBean implements Serializable{


    /**
     * code : 200
     * msg : success
     * data : {"week_fish":0,"fish_counts":0,"cash_counts":0,"can_apply_cash":0,"can_exchange_coin":0,"bank_info":"","exchange_coin_desc":"兑换甜甜圈的说明","cash_desc":"提现的说明","fish_desc":"小鱼干的说明","apply_cash_desc":"申请提现的说明","fish_to_coin_rate":100,"fish_to_money_rate":1,"min_exchange_coin_num":1,"min_cash_money_num":10,"help_url":"http://wap.demo.com/helpcenter?id="}
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

    public static class DataBean implements Serializable{
        /**
         * week_fish : 0
         * fish_counts : 0
         * cash_counts : 0
         * can_apply_cash : 0
         * can_exchange_coin : 0
         * bank_info :
         * exchange_coin_desc : 兑换甜甜圈的说明
         * cash_desc : 提现的说明
         * fish_desc : 小鱼干的说明
         * apply_cash_desc : 申请提现的说明
         * fish_to_coin_rate : 100
         * fish_to_money_rate : 1
         * min_exchange_coin_num : 1
         * min_cash_money_num : 10
         * help_url : http://wap.demo.com/helpcenter?id=
         */

        private int week_fish;
        private int  is_get_fish;

        private float fish_counts;
        private int cash_counts;
        private int can_apply_cash;
        private int can_exchange_coin;
        private String exchange_coin_desc;
        private String cash_desc;
        private String fish_desc;
        private String apply_cash_desc;
        private int fish_to_coin_rate;
        private float fish_to_money_rate;
        private int min_exchange_coin_num;
        private int min_cash_money_num;
        private String help_url;

        public int getIs_get_fish() {
            return is_get_fish;
        }

        public void setIs_get_fish(int is_get_fish) {
            this.is_get_fish = is_get_fish;
        }

        /**
         * bank_info : {"id":4,"card_user":"qq123123","card_num":"11111222","bank_name":"农业银行","icon":"0e60393614c18b2003557d08622139c8"}
         */
        private BankInfoBean bank_info;

        public int getWeek_fish() {
            return week_fish;
        }

        public void setWeek_fish(int week_fish) {
            this.week_fish = week_fish;
        }

        public float getFish_counts() {
            return fish_counts;
        }

        public void setFish_counts(float fish_counts) {
            this.fish_counts = fish_counts;
        }

        public int getCash_counts() {
            return cash_counts;
        }

        public void setCash_counts(int cash_counts) {
            this.cash_counts = cash_counts;
        }

        public int getCan_apply_cash() {
            return can_apply_cash;
        }

        public void setCan_apply_cash(int can_apply_cash) {
            this.can_apply_cash = can_apply_cash;
        }

        public int getCan_exchange_coin() {
            return can_exchange_coin;
        }

        public void setCan_exchange_coin(int can_exchange_coin) {
            this.can_exchange_coin = can_exchange_coin;
        }

        public String getExchange_coin_desc() {
            return exchange_coin_desc;
        }

        public void setExchange_coin_desc(String exchange_coin_desc) {
            this.exchange_coin_desc = exchange_coin_desc;
        }

        public String getCash_desc() {
            return cash_desc;
        }

        public void setCash_desc(String cash_desc) {
            this.cash_desc = cash_desc;
        }

        public String getFish_desc() {
            return fish_desc;
        }

        public void setFish_desc(String fish_desc) {
            this.fish_desc = fish_desc;
        }

        public String getApply_cash_desc() {
            return apply_cash_desc;
        }

        public void setApply_cash_desc(String apply_cash_desc) {
            this.apply_cash_desc = apply_cash_desc;
        }

        public int getFish_to_coin_rate() {
            return fish_to_coin_rate;
        }

        public void setFish_to_coin_rate(int fish_to_coin_rate) {
            this.fish_to_coin_rate = fish_to_coin_rate;
        }

        public float getFish_to_money_rate() {
            return fish_to_money_rate;
        }

        public void setFish_to_money_rate(float fish_to_money_rate) {
            this.fish_to_money_rate = fish_to_money_rate;
        }

        public int getMin_exchange_coin_num() {
            return min_exchange_coin_num;
        }

        public void setMin_exchange_coin_num(int min_exchange_coin_num) {
            this.min_exchange_coin_num = min_exchange_coin_num;
        }

        public int getMin_cash_money_num() {
            return min_cash_money_num;
        }

        public void setMin_cash_money_num(int min_cash_money_num) {
            this.min_cash_money_num = min_cash_money_num;
        }

        public String getHelp_url() {
            return help_url;
        }

        public void setHelp_url(String help_url) {
            this.help_url = help_url;
        }

        public BankInfoBean getBank_info() {
            return bank_info;
        }

        public void setBank_info(BankInfoBean bank_info) {
            this.bank_info = bank_info;
        }

        public static class BankInfoBean implements Serializable{
            /**
             * id : 4
             * card_user : qq123123
             * card_num : 11111222
             * bank_name : 农业银行
             * icon : 0e60393614c18b2003557d08622139c8
             */

            private int id;
            private String card_user;
            private String card_num;
            private String bank_name;
            private String icon;
            private IconInfoBean icon_info;

            public IconInfoBean getIcon_info() {
                return icon_info;
            }

            public void setIcon_info(IconInfoBean icon_info) {
                this.icon_info = icon_info;
            }

            public static class IconInfoBean implements Serializable{
                /**
                 * ext : jpg
                 * w : 1080
                 * h : 1080
                 * size : 330492
                 * is_long : 0
                 * md5 : c7adcb987e5224301258c6f7efb2d53e
                 * link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1
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




            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCard_user() {
                return card_user;
            }

            public void setCard_user(String card_user) {
                this.card_user = card_user;
            }

            public String getCard_num() {
                return card_num;
            }

            public void setCard_num(String card_num) {
                this.card_num = card_num;
            }

            public String getBank_name() {
                return bank_name;
            }

            public void setBank_name(String bank_name) {
                this.bank_name = bank_name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}

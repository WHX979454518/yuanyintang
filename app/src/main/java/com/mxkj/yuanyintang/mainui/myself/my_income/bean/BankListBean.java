package com.mxkj.yuanyintang.mainui.myself.my_income.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zuixia on 2018/5/23.
 */

public class BankListBean implements Serializable{


    /**
     * code : 200
     * msg : success
     * data : [{"id":1,"name":"农业银行","icon":"0e60393614c18b2003557d08622139c8","icon_link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","icon_info":{"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1"}},{"id":2,"name":"工商银行","icon":"0e60393614c18b2003557d08622139c8","icon_link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","icon_info":{"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1"}},{"id":3,"name":"人民银行","icon":"0e60393614c18b2003557d08622139c8","icon_link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","icon_info":{"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1"}},{"id":4,"name":"成都银行","icon":"0e60393614c18b2003557d08622139c8","icon_link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","icon_info":{"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1"}}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * name : 农业银行
         * icon : 0e60393614c18b2003557d08622139c8
         * icon_link : http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1
         * icon_info : {"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1"}
         */

        private int id;
        private int user_card_id;
        private String card_number;
        private String name;
        private String bank_user_name;
        private String icon;
        private String icon_link;
        private IconInfoBean icon_info;

        public int getUser_card_id() {
            return user_card_id;
        }

        public void setUser_card_id(int user_card_id) {
            this.user_card_id = user_card_id;
        }

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public String getBank_user_name() {
            return bank_user_name;
        }

        public void setBank_user_name(String bank_user_name) {
            this.bank_user_name = bank_user_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon_link() {
            return icon_link;
        }

        public void setIcon_link(String icon_link) {
            this.icon_link = icon_link;
        }

        public IconInfoBean getIcon_info() {
            return icon_info;
        }

        public void setIcon_info(IconInfoBean icon_info) {
            this.icon_info = icon_info;
        }

        public static class IconInfoBean implements Serializable{
            /**
             * ext : png
             * w : 300
             * h : 300
             * size : 79407
             * is_long : 0
             * md5 : 0e60393614c18b2003557d08622139c8
             * link : http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1
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

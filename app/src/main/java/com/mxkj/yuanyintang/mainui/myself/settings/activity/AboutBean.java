package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import java.io.Serializable;

public class AboutBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"about_sinaweibo":"@源音塘音乐","about_email":"help@imxkj.com","about_feedback_qq_group":"858222916","about_wx_mp":"源音塘音乐"}
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
         * about_sinaweibo : @源音塘音乐
         * about_email : help@imxkj.com
         * about_feedback_qq_group : 858222916
         * about_wx_mp : 源音塘音乐
         */

        private String about_sinaweibo;
        private String about_email;
        private String about_feedback_qq_group;
        private String about_wx_mp;

        public String getAbout_sinaweibo() {
            return about_sinaweibo;
        }

        public void setAbout_sinaweibo(String about_sinaweibo) {
            this.about_sinaweibo = about_sinaweibo;
        }

        public String getAbout_email() {
            return about_email;
        }

        public void setAbout_email(String about_email) {
            this.about_email = about_email;
        }

        public String getAbout_feedback_qq_group() {
            return about_feedback_qq_group;
        }

        public void setAbout_feedback_qq_group(String about_feedback_qq_group) {
            this.about_feedback_qq_group = about_feedback_qq_group;
        }

        public String getAbout_wx_mp() {
            return about_wx_mp;
        }

        public void setAbout_wx_mp(String about_wx_mp) {
            this.about_wx_mp = about_wx_mp;
        }
    }
}

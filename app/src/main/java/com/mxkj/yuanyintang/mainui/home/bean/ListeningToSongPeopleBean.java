package com.mxkj.yuanyintang.mainui.home.bean;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/19.
 */

public class ListeningToSongPeopleBean {

    /**
     * code : 200
     * msg : success
     * data : [{"uid":31065,"nickname":"YY ","head":"bf8aa22297870df989701a527ecda1a9f15fed70","signature":"嚯嚯","day":0,"is_music":3,"relation":0,"head_link":"http://yyt.demo.com/image/bf8aa22297870df989701a527ecda1a9f15fed70"},{"uid":31065,"nickname":"YY ","head":"bf8aa22297870df989701a527ecda1a9f15fed70","signature":"嚯嚯","day":0,"is_music":3,"relation":0,"head_link":"http://yyt.demo.com/image/bf8aa22297870df989701a527ecda1a9f15fed70"}]
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
         * uid : 31065
         * nickname : YY
         * head : bf8aa22297870df989701a527ecda1a9f15fed70
         * signature : 嚯嚯
         * day : 0
         * is_music : 3
         * relation : 0
         * head_link : http://yyt.demo.com/image/bf8aa22297870df989701a527ecda1a9f15fed70
         */

        private int uid;
        private String nickname;
        private String head;
        private String signature;
        private int day;
        private int is_music;
        private int relation;
        private String head_link;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }
    }
}

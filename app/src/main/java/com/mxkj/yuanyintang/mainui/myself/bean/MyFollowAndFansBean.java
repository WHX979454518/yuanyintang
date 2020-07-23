package com.mxkj.yuanyintang.mainui.myself.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/9.
 */

public class MyFollowAndFansBean implements Serializable {


    /**
     * code : 200
     * msg : success
     * data : [{"id":31233,"nickname":"源音塘825887","sex":"","head":"3d3d27527715c56204c6e063e34883ae60384a97","member_type":1,"is_music":0,"signature":"","is_relation":0,"head_link":"http://demoapi.yuanyintang.com/image/3d3d27527715c56204c6e063e34883ae60384a97"}]
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
         * id : 31233
         * nickname : 源音塘825887
         * sex :
         * head : 3d3d27527715c56204c6e063e34883ae60384a97
         * member_type : 1
         * is_music : 0
         * signature :
         * is_relation : 0
         * head_link : http://demoapi.yuanyintang.com/image/3d3d27527715c56204c6e063e34883ae60384a97
         */

        private int id;
        private String nickname;
        private String sex;
        private String head;
        private int member_type;
        private int is_music;
        private String signature;
        private int is_relation;
        private String head_link;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getMember_type() {
            return member_type;
        }

        public void setMember_type(int member_type) {
            this.member_type = member_type;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getIs_relation() {
            return is_relation;
        }

        public void setIs_relation(int is_relation) {
            this.is_relation = is_relation;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }
    }
}

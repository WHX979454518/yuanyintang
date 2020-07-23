package com.mxkj.yuanyintang.mainui.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/9/22.
 */

public class MusicListMusicIanBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : [{"id":31236,"nickname":"源音塘497067","day":-28800,"sex":0,"head":"3d3d27527715c56204c6e063e34883ae60384a97","member_type":2,"is_music":3,"age":47,"music":{"item_id":4065,"title":"红尘客栈啥啥啥啥啥啥"},"head_link":"http://yyt.demo.com/image/3d3d27527715c56204c6e063e34883ae60384a97"}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 31236
         * nickname : 源音塘497067
         * day : -28800
         * sex : 0
         * head : 3d3d27527715c56204c6e063e34883ae60384a97
         * member_type : 2
         * is_music : 3
         * age : 47
         * music : {"item_id":4065,"title":"红尘客栈啥啥啥啥啥啥"}
         * head_link : http://yyt.demo.com/image/3d3d27527715c56204c6e063e34883ae60384a97
         */

        private int id;
        private int is_relation;
        private String nickname;
        private int day;
        private int fans_num;
        private int sex;
        private String head;
        private int member_type;
        private int is_music;
        private int age;
        private boolean isPlaying;
        private MusicBean music;
        private String head_link;
        private Boolean check = false;
        private String title;
        private int item_id;

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public int getFans_num() {
            return fans_num;
        }

        public void setFans_num(int fans_num) {
            this.fans_num = fans_num;
        }

        public int getIs_relation() {
            return is_relation;
        }

        public void setIs_relation(int is_relation) {
            this.is_relation = is_relation;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public Boolean getCheck() {
            return check;
        }

        public void setCheck(Boolean check) {
            this.check = check;
        }

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

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public MusicBean getMusic() {
            return music;
        }

        public void setMusic(MusicBean music) {
            this.music = music;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public static class MusicBean implements Serializable {
            /**
             * item_id : 4065
             * title : 红尘客栈啥啥啥啥啥啥
             */

            private int item_id;
            private String title;

            public int getItem_id() {
                return item_id;
            }

            public void setItem_id(int item_id) {
                this.item_id = item_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}

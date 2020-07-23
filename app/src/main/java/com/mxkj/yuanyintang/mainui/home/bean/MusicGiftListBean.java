package com.mxkj.yuanyintang.mainui.home.bean;

import java.util.List;

/**
 * Created by zuixia on 2018/5/17.
 */

public class MusicGiftListBean {

    /**
     * code : 200
     * msg : success
     * data : [{"id":50963,"nickname":"4月","head":"3d3d27527715c56204c6e063e34883ae60384a97","sex":0,"is_music":0,"gift_name":"礼物4","icon":"c7adcb987e5224301258c6f7efb2d53e","head_link":"http://api.demo.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3","head_info":"","icon_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3","icon_info":{"ext":"","w":"","h":"","size":"","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}},{"id":50963,"nickname":"4月","head":"3d3d27527715c56204c6e063e34883ae60384a97","sex":0,"is_music":0,"gift_name":"222","icon":"c7adcb987e5224301258c6f7efb2d53e","head_link":"http://api.demo.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3","head_info":"","icon_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3","icon_info":{"ext":"","w":"","h":"","size":"","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}}]
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
         * id : 50963
         * nickname : 4月
         * head : 3d3d27527715c56204c6e063e34883ae60384a97
         * sex : 0
         * is_music : 0
         * gift_name : 礼物4
         * icon : c7adcb987e5224301258c6f7efb2d53e
         * head_link : http://api.demo.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3
         * head_info :
         * icon_link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
         * icon_info : {"ext":"","w":"","h":"","size":"","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}
         */

        private int id;
        private String nickname;
        private String head;
        private int sex;
        private int is_music;
        private String gift_name;
        private String icon;
        private String head_link;
        private String head_info;
        private String icon_link;
        private IconInfoBean icon_info;

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

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public String getHead_info() {
            return head_info;
        }

        public void setHead_info(String head_info) {
            this.head_info = head_info;
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

        public static class IconInfoBean {
            /**
             * ext :
             * w :
             * h :
             * size :
             * is_long : 0
             * md5 : c7adcb987e5224301258c6f7efb2d53e
             * link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
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

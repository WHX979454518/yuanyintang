package com.mxkj.yuanyintang.mainui.newapp.message;

import com.mxkj.yuanyintang.mainui.home.homebaseadapter.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by zuixia on 2018/8/23.
 */

public class MygiftListBean {
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

    public static class DataBean implements MultiItemEntity {
        /**
         * id : 278
         * uid : 50874
         * imgpic : 9527181c9258ee280cc525ae5d2f070a
         * icon : c7adcb987e5224301258c6f7efb2d53e
         * coin_num : 300
         * gift_name : 礼物4
         * imgpic_link : http://api.demo.com//image/9527181c9258ee280cc525ae5d2f070a/1
         * imgpic_info : {"ext":"png","w":"300","h":"300","size":"122629","is_long":"0","md5":"9527181c9258ee280cc525ae5d2f070a","link":"http://api.demo.com//image/9527181c9258ee280cc525ae5d2f070a/1"}
         * icon_link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1
         * icon_info : {"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1"}
         */

        private int id;
        private int uid;
        private String imgpic;
        private String nickname;
        private String create_time;
        private String icon;
        private int coin_num;
        private String gift_name;
        private String imgpic_link;
        private ImgpicInfoBean imgpic_info;
        private String icon_link;
        private IconInfoBean icon_info;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getCoin_num() {
            return coin_num;
        }

        public void setCoin_num(int coin_num) {
            this.coin_num = coin_num;
        }

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
        }

        public String getImgpic_link() {
            return imgpic_link;
        }

        public void setImgpic_link(String imgpic_link) {
            this.imgpic_link = imgpic_link;
        }

        public ImgpicInfoBean getImgpic_info() {
            return imgpic_info;
        }

        public void setImgpic_info(ImgpicInfoBean imgpic_info) {
            this.imgpic_info = imgpic_info;
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

        @Override
        public int getItemType() {
            return 0;
        }

        public static class ImgpicInfoBean {
            /**
             * ext : png
             * w : 300
             * h : 300
             * size : 122629
             * is_long : 0
             * md5 : 9527181c9258ee280cc525ae5d2f070a
             * link : http://api.demo.com//image/9527181c9258ee280cc525ae5d2f070a/1
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

        public static class IconInfoBean {
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
    }
}

package com.mxkj.yuanyintang.mainui.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/27.
 */

public class ContainSongBean {

    /**
     * code : 200
     * msg : success
     * data : [{"id":771,"title":"法实施地方","counts":14000007,"comment":1,"total":50,"imgpic":"96ee2899bd16ba7c9c64d937a64365cb","original":"","uid":31085,"nickname":"坎坎坷坷","is_song":0,"imgpic_link":"http://demoapi.yuanyintang.com/image/96ee2899bd16ba7c9c64d937a64365cb","original_link":""}]
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
         * id : 771
         * title : 法实施地方
         * counts : 14000007
         * comment : 1
         * total : 50
         * imgpic : 96ee2899bd16ba7c9c64d937a64365cb
         * original :
         * uid : 31085
         * nickname : 坎坎坷坷
         * is_song : 0
         * imgpic_link : http://demoapi.yuanyintang.com/image/96ee2899bd16ba7c9c64d937a64365cb
         * original_link :
         */

        private int id;
        private String title;
        private int counts;
        private int comment;
        private int total;
        private String imgpic;
        private String original;
        private int uid;
        private String nickname;
        private int is_song;
//        private String imgpic_link;
        private String original_link;


        private ImgpicInfoBean imgpic_info;
        public ImgpicInfoBean getImgpic_info() {
            return imgpic_info;
        }

        public void setImgpic_info(ImgpicInfoBean imgpic_info) {
            this.imgpic_info = imgpic_info;
        }
        public static class ImgpicInfoBean implements Serializable {
            /**
             * ext :
             * w :
             * h :
             * size : 330492
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


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

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

        public int getIs_song() {
            return is_song;
        }

        public void setIs_song(int is_song) {
            this.is_song = is_song;
        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }

//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }

        public String getOriginal_link() {
            return original_link;
        }

        public void setOriginal_link(String original_link) {
            this.original_link = original_link;
        }
    }
}

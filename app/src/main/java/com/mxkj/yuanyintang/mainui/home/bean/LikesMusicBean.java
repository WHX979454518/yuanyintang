package com.mxkj.yuanyintang.mainui.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/20.
 */

public class LikesMusicBean {


    /**
     * code : 200
     * msg : success
     * data : [{"id":3077,"title":"月照杨花舞","counts":124,"imgpic":"b84016ca03b695f7bc0ecb199425330e","playtime":"","uid":31085,"nickname":"坎坎坷坷","category":20,"song_id":647,"collection":1,"imgpic_link":"http://yyt.demo.com/image/b84016ca03b695f7bc0ecb199425330e"},{"id":4061,"title":"意义在哪里","counts":61,"imgpic":"1730917e595c4c37c69e4b8f28cce0ed","playtime":"03:52","uid":31085,"nickname":"坎坎坷坷","category":20,"song_id":647,"collection":1,"imgpic_link":"http://yyt.demo.com/image/1730917e595c4c37c69e4b8f28cce0ed"},{"id":4062,"title":"我怀念的你","counts":45,"imgpic":"1740b8a089a4a19c8e11ba9787b6642a","playtime":"03:45","uid":31085,"nickname":"坎坎坷坷","category":20,"song_id":647,"collection":1,"imgpic_link":"http://yyt.demo.com/image/1740b8a089a4a19c8e11ba9787b6642a"},{"id":3066,"title":"111","counts":1035,"imgpic":"9a627aafc2e4c3f128bb02e4bba85d2d","playtime":"04:32","uid":31130,"nickname":"柔柔弱弱","category":7,"song_id":647,"collection":1,"imgpic_link":"http://yyt.demo.com/image/9a627aafc2e4c3f128bb02e4bba85d2d"},{"id":4058,"title":"love me for me","counts":75,"imgpic":"a7565c329329d6f3410af3ee2a3b46c3","playtime":"","uid":31144,"nickname":"食发鬼设法","category":20,"song_id":647,"collection":1,"imgpic_link":"http://yyt.demo.com/image/a7565c329329d6f3410af3ee2a3b46c3"},{"id":3805,"title":"告白气球","counts":55,"imgpic":"","playtime":"","uid":31085,"nickname":"坎坎坷坷","category":20,"song_id":647,"collection":1}]
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
         * id : 3077
         * title : 月照杨花舞
         * counts : 124
         * imgpic : b84016ca03b695f7bc0ecb199425330e
         * playtime :
         * uid : 31085
         * nickname : 坎坎坷坷
         * category : 20
         * song_id : 647
         * collection : 1
         * imgpic_link : http://yyt.demo.com/image/b84016ca03b695f7bc0ecb199425330e
         */

        private int id;
        private String title;
        private int counts;
        private String imgpic;
        private String playtime;
        private int uid;
        private String nickname;
        private int category;
        private int song_id;
        private int collection;
//        private String imgpic_link;
        private int comment;


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



        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
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

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
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

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getSong_id() {
            return song_id;
        }

        public void setSong_id(int song_id) {
            this.song_id = song_id;
        }

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }
//
//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }
    }
}

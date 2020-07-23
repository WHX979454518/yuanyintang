package com.mxkj.yuanyintang.mainui.home.bean;

import java.io.Serializable;
import java.util.List;

public class MusicIanSongSheetBean implements Serializable{

    /**
     * code : 200
     * msg : success
     * data : [{"id":2743,"type":0,"title":"【燃向】30首超燃节奏，易燃易爆炸！","counts":67,"total":30,"share":0,"imgpic":"35bcc344b5f5b0ca7adf0be3c52cc9046d8f77cb","uid":36490,"nickname":"一个古风工作室","imgpic_link":"https://api.yuanyintang.com/image/35bcc344b5f5b0ca7adf0be3c52cc9046d8f77cb"},{"id":2624,"type":0,"title":"1","counts":0,"total":1,"share":0,"imgpic":"b752f5ec04f8f0297b8192cd435ae7f311fe257c","uid":36490,"nickname":"一个古风工作室","imgpic_link":"https://api.yuanyintang.com/image/b752f5ec04f8f0297b8192cd435ae7f311fe257c"}]
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
         * id : 2743
         * type : 0
         * title : 【燃向】30首超燃节奏，易燃易爆炸！
         * counts : 67
         * total : 30
         * share : 0
         * imgpic : 35bcc344b5f5b0ca7adf0be3c52cc9046d8f77cb
         * uid : 36490
         * nickname : 一个古风工作室
         * imgpic_link : https://api.yuanyintang.com/image/35bcc344b5f5b0ca7adf0be3c52cc9046d8f77cb
         */

        private int id;
        private int type;
        private String title;
        private int counts;
        private String counts_text;
        private int total;
        private int share;
        private String imgpic;
        private int uid;
        private String nickname;
//        private String imgpic_link;
        private Boolean isCollection = false;
        private int status;
        private int is_private;
        private int deal_type;

        public int getDeal_type() {
            return deal_type;
        }

        public void setDeal_type(int deal_type) {
            this.deal_type = deal_type;
        }

        public int getIs_private() {
            return is_private;
        }

        public void setIs_private(int is_private) {
            this.is_private = is_private;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCounts_text() {
            return counts_text;
        }

        public void setCounts_text(String counts_text) {
            this.counts_text = counts_text;
        }

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


        public Boolean getCollection() {
            return isCollection;
        }

        public void setCollection(Boolean collection) {
            isCollection = collection;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getShare() {
            return share;
        }

        public void setShare(int share) {
            this.share = share;
        }

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
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

//        public String getImgpic_link() {
//            return imgpic_link;
//        }
//
//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }
    }
}

package com.mxkj.yuanyintang.mainui.home.bean;

import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/9/30.
 */

public class MusicIanPondBean {


    /**
     * code : 200
     * msg : success
     * data : [{"id":1463,"title":"#秋日祭#比赛  求收听  投票","content":"讲真，见证友情了嗷嗷","hashtag":[{"id":5,"title":"听说大佬很多哟"},{"id":19,"title":"青春"},{"id":72,"title":"新歌发布"}],"imglist":"389255b28add53c3291bc02341eede5a4b8943a8","create_time":"09-09","update_time":"09-12","thcount":1,"recommend":0,"uid":36490,"nickname":"一个古风工作室","head":"65348d73086937ee73ea5ebbd531fb91eac9361d","member_type":2,"is_music":3,"imglist_link":["https://api.yuanyintang.com/image/389255b28add53c3291bc02341eede5a4b8943a8"],"head_link":"https://api.yuanyintang.com/image/65348d73086937ee73ea5ebbd531fb91eac9361d"}]
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
         * id : 1463
         * title : #秋日祭#比赛  求收听  投票
         * content : 讲真，见证友情了嗷嗷
         * hashtag : [{"id":5,"title":"听说大佬很多哟"},{"id":19,"title":"青春"},{"id":72,"title":"新歌发布"}]
         * imglist : 389255b28add53c3291bc02341eede5a4b8943a8
         * create_time : 09-09
         * update_time : 09-12
         * thcount : 1
         * recommend : 0
         * uid : 36490
         * nickname : 一个古风工作室
         * head : 65348d73086937ee73ea5ebbd531fb91eac9361d
         * member_type : 2
         * is_music : 3
         * imglist_link : ["https://api.yuanyintang.com/image/389255b28add53c3291bc02341eede5a4b8943a8"]
         * head_link : https://api.yuanyintang.com/image/65348d73086937ee73ea5ebbd531fb91eac9361d
         */

        private int id;
        private String title;
        private String content;
        private String imglist;
        private String create_time;
        private String update_time;
        private int thcount;
        private int recommend;
        private int uid;
        private String nickname;
        private String head;
        private int member_type;
        private int is_music;
        private String head_link;
        private int hits;
        private List<HashtagBean> hashtag;
//        private List<String> imglist_link;
        private List<HomeBean.ImgpicInfoBean> imglist_info;

        public static class ImglistInfoBean implements Serializable {
            /**
             * ext : jpg
             * w : 200
             * h : 200
             * size : 9329
             * is_long : 0
             * link : http://api.demo.com//image/ce81fba58689fa96ab2ad9860a6b2461/3
             * md5 : ce81fba58689fa96ab2ad9860a6b2461
             */

            private String ext;
            private String w;
            private String h;
            private String size;
            private String is_long;
            private String link;
            private String md5;

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

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
            }
        }

        public List<HomeBean.ImgpicInfoBean> getImglist_info() {
            return imglist_info;
        }

        public void setImglist_info(List<HomeBean.ImgpicInfoBean> imglist_info) {
            this.imglist_info = imglist_info;
        }


        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImglist() {
            return imglist;
        }

        public void setImglist(String imglist) {
            this.imglist = imglist;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getThcount() {
            return thcount;
        }

        public void setThcount(int thcount) {
            this.thcount = thcount;
        }

        public int getRecommend() {
            return recommend;
        }

        public void setRecommend(int recommend) {
            this.recommend = recommend;
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

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public List<HashtagBean> getHashtag() {
            return hashtag;
        }

        public void setHashtag(List<HashtagBean> hashtag) {
            this.hashtag = hashtag;
        }

//        public List<String> getImglist_link() {
//            return imglist_link;
//        }
//
//        public void setImglist_link(List<String> imglist_link) {
//            this.imglist_link = imglist_link;
//        }

        public static class HashtagBean {
            /**
             * id : 5
             * title : 听说大佬很多哟
             */

            private int id;
            private String title;

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
        }
    }
}

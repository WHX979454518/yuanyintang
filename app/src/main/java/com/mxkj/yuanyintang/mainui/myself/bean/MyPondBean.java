package com.mxkj.yuanyintang.mainui.myself.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/10.
 */

public class MyPondBean implements Serializable {


    /**
     * code : 200
     * msg : success
     * data : {"count":{"all":2,"fail":0,"auditing":2,"online":0,"draft":0},"pond":[{"id":294,"title":"222","content":"111","hashtag":[{"id":5,"title":"原创1"},{"id":209,"title":"测试二"}],"imglist":"4b57806f82c4bc98da4803e77689e1657f3dcf92","nickname":"你好","create_time":"2小时前","update_time":"2小时前","thcount":0,"recommend":0,"status":1,"remark":"","uid":31083,"member_type":1,"imglist_link":["http://yyt.demo.com/image/4b57806f82c4bc98da4803e77689e1657f3dcf92"]},{"id":295,"title":"222","content":"111","hashtag":[{"id":5,"title":"原创1"},{"id":216,"title":"111"},{"id":217,"title":"1c"}],"imglist":"4b57806f82c4bc98da4803e77689e1657f3dcf92","nickname":"你好","create_time":"2小时前","update_time":"2小时前","thcount":0,"recommend":0,"status":1,"remark":"","uid":31083,"member_type":1,"imglist_link":["http://yyt.demo.com/image/4b57806f82c4bc98da4803e77689e1657f3dcf92"]}]}
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

    public static class DataBean implements Serializable {
        /**
         * count : {"all":2,"fail":0,"auditing":2,"online":0,"draft":0}
         * pond : [{"id":294,"title":"222","content":"111","hashtag":[{"id":5,"title":"原创1"},{"id":209,"title":"测试二"}],"imglist":"4b57806f82c4bc98da4803e77689e1657f3dcf92","nickname":"你好","create_time":"2小时前","update_time":"2小时前","thcount":0,"recommend":0,"status":1,"remark":"","uid":31083,"member_type":1,"imglist_link":["http://yyt.demo.com/image/4b57806f82c4bc98da4803e77689e1657f3dcf92"]},{"id":295,"title":"222","content":"111","hashtag":[{"id":5,"title":"原创1"},{"id":216,"title":"111"},{"id":217,"title":"1c"}],"imglist":"4b57806f82c4bc98da4803e77689e1657f3dcf92","nickname":"你好","create_time":"2小时前","update_time":"2小时前","thcount":0,"recommend":0,"status":1,"remark":"","uid":31083,"member_type":1,"imglist_link":["http://yyt.demo.com/image/4b57806f82c4bc98da4803e77689e1657f3dcf92"]}]
         */

        private CountBean count;
        private List<PondBean> pond;

        public CountBean getCount() {
            return count;
        }

        public void setCount(CountBean count) {
            this.count = count;
        }

        public List<PondBean> getPond() {
            return pond;
        }

        public void setPond(List<PondBean> pond) {
            this.pond = pond;
        }

        public static class CountBean implements Serializable {
            /**
             * all : 2
             * fail : 0
             * auditing : 2
             * online : 0
             * draft : 0
             */

            private int all = 0;
            private int fail = 0;
            private int auditing = 0;
            private int online = 0;
            private int draft = 0;

            public int getAll() {
                return all;
            }

            public void setAll(int all) {
                this.all = all;
            }

            public int getFail() {
                return fail;
            }

            public void setFail(int fail) {
                this.fail = fail;
            }

            public int getAuditing() {
                return auditing;
            }

            public void setAuditing(int auditing) {
                this.auditing = auditing;
            }

            public int getOnline() {
                return online;
            }

            public void setOnline(int online) {
                this.online = online;
            }

            public int getDraft() {
                return draft;
            }

            public void setDraft(int draft) {
                this.draft = draft;
            }
        }

        public static class PondBean implements Serializable {
            /**
             * id : 294
             * title : 222
             * content : 111
             * hashtag : [{"id":5,"title":"原创1"},{"id":209,"title":"测试二"}]
             * imglist : 4b57806f82c4bc98da4803e77689e1657f3dcf92
             * nickname : 你好
             * create_time : 2小时前
             * update_time : 2小时前
             * thcount : 0
             * recommend : 0
             * status : 1
             * remark :
             * uid : 31083
             * member_type : 1
             * imglist_link : ["http://yyt.demo.com/image/4b57806f82c4bc98da4803e77689e1657f3dcf92"]
             */

            private int id;
            private String title;
            private String content;
            private String imglist;
            private String nickname;
            private String create_time;
            private String update_time;
            private int thcount;
            private int recommend;
            private int status;
            private String remark;
            private int uid;
            private int member_type;
            private List<HashtagBean> hashtag;
//            private List<String> imglist_link;

            private List<ImglistInfoBean> imglist_info;
            public static class ImglistInfoBean implements Serializable  {
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
            public List<ImglistInfoBean> getImglist_info() {
                return imglist_info;
            }
            public void setImglist_info(List<ImglistInfoBean> imglist_info) {
                this.imglist_info = imglist_info;
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getMember_type() {
                return member_type;
            }

            public void setMember_type(int member_type) {
                this.member_type = member_type;
            }

            public List<HashtagBean> getHashtag() {
                return hashtag;
            }

            public void setHashtag(List<HashtagBean> hashtag) {
                this.hashtag = hashtag;
            }

//            public List<String> getImglist_link() {
//                return imglist_link;
//            }
//
//            public void setImglist_link(List<String> imglist_link) {
//                this.imglist_link = imglist_link;
//            }

            public static class HashtagBean implements Serializable {
                /**
                 * id : 5
                 * title : 原创1
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
}

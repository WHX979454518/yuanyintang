package com.mxkj.yuanyintang.mainui.pond.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mxkj.yuanyintang.mainui.home.data.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/9/27.
 */

public class PondBean implements Serializable{

    /**
     * code : 200
     * msg : success
     * data : [{"id":3,"title":"222221","content":"111","hashtag":[{"id":208,"title":"测试一"},{"id":209,"title":"测试二"}],"imglist":"111","create_time":"05-23","update_time":"07-20","thcount":50,"recommend":1,"uid":31047,"nickname":"","head":"","member_type":"","imglist_link":["http://yyt.demo.com/image/111"],"imglist_info": [{
     "ext": "jpg",
     "w": "200",
     "h": "200",
     "size": "9329",
     "is_long": "0",
     "link": "http:\/\/api.demo.com\/\/image\/ce81fba58689fa96ab2ad9860a6b2461\/3",
     "md5": "ce81fba58689fa96ab2ad9860a6b2461"
     }]}]
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

    public static class DataBean implements Serializable, MultiItemEntity {
        private int id;
        private String title;
        private String content;
        private String imglist;
        private String create_time;
        private String update_time;
        private int thcount;
        private int hits;
        private String hits_text;
        private int recommend;
        private int uid;
        private int is_music;
        private String nickname;
        private int agrees;
        private String head;
        private String head_link;
        private String member_type;
        private List<HashtagBean> hashtag;
//        private List<String> imglist_link;
        private List<ImglistInfoBean> imglist_info;

        private int is_agree;
        private String agrees_text = "0";
        private String thcount_text = "0";
        private String share_counts_text = "0";
        private int is_self;
        private String share_url;

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public int getIs_self() {
            return is_self;
        }

        public void setIs_self(int is_self) {
            this.is_self = is_self;
        }

        public String getAgrees_text() {
            return agrees_text;
        }

        public void setAgrees_text(String agrees_text) {
            this.agrees_text = agrees_text;
        }

        public String getThcount_text() {
            return thcount_text;
        }

        public void setThcount_text(String thcount_text) {
            this.thcount_text = thcount_text;
        }

        public String getShare_counts_text() {
            return share_counts_text;
        }

        public void setShare_counts_text(String share_counts_text) {
            this.share_counts_text = share_counts_text;
        }

        public int getIs_agree() {
            return is_agree;
        }

        public void setIs_agree(int is_agree) {
            this.is_agree = is_agree;
        }

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


        public int getAgrees() {
            return agrees;
        }

        public void setAgrees(int agrees) {
            this.agrees = agrees;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        private boolean isRecommend;

        public boolean isRecommend() {
            return isRecommend;
        }

        public void setRecommend(boolean recommend) {
            isRecommend = recommend;
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

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
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

        public String getMember_type() {
            return member_type;
        }

        public void setMember_type(String member_type) {
            this.member_type = member_type;
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

        @Override
        public int getItemType() {
           /* if (hashtag != null && hashtag.size() > 0) {
                return Constant.PondItemType.TYPE_TAG;
            } else*/
           if (imglist_info != null) {
                if (1 < imglist_info.size()) {
                    return Constant.PondItemType.TYPE_POND_MUTI_IMG;
                } else if (imglist_info.size() == 1) {
                    return Constant.PondItemType.TYPE_POND_SINGLE_IMG;
                } else if (imglist_info.size() == 0) {
                    return Constant.PondItemType.TYPE_POND_NO_IMG;
                }
            } else if (imglist_info == null) {
                return Constant.PondItemType.TYPE_POND_NO_IMG;
            }
            return 0;
        }

        public static class HashtagBean implements Serializable{
            /**
             * id : 208
             * title : 测试一
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

package com.mxkj.yuanyintang.mainui.pond.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mxkj.yuanyintang.mainui.home.data.Constant;

import java.io.Serializable;
import java.util.List;

public class PondBeanNew implements Serializable{

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

    public static class DataBean implements Serializable{
        private SeoBean seo;
        private List<DataListBean> data_list;

        public SeoBean getSeo() {
            return seo;
        }

        public void setSeo(SeoBean seo) {
            this.seo = seo;
        }

        public List<DataListBean> getData_list() {
            return data_list;
        }

        public void setData_list(List<DataListBean> data_list) {
            this.data_list = data_list;
        }

        public static class SeoBean {
            /**
             * keywords : 源音塘|原创|二次元音乐|满汉女神|加入音乐人|Vocaloid|动漫游戏|古风|三次元|翻唱歌曲|二次元|音乐人|咕噜吧啦|幻音
             * title : 源音塘音乐 — 全新的二次元音乐社区
             * description : 源音塘是全新的以二次元音乐为主的音乐社区。这里有让耳朵怀孕的丰富良曲、极富魅力的音乐人和偶尔破次元的音乐同好。每天,故事和音乐都在这里
             */

            private String keywords;
            private String title;
            private String description;

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class DataListBean implements Serializable, MultiItemEntity {
            private int id;
            private String title;
            private String content;
            private String imglist;
            private String create_time;
            private String update_time;
            private int thcount;
            private int hits;
            private int recommend;
            private int uid;
            private String nickname;
            private String head;
            private int is_music;
            private int member_type;
            private int floor;
            private String hits_text;
            private String head_link;
            private HeadInfoBean head_info;
            private List<HashtagBean> hashtag;
            private List<String> imglist_link;
            private List<ImglistInfoBean> imglist_info;

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

            public int getHits() {
                return hits;
            }

            public void setHits(int hits) {
                this.hits = hits;
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

            public int getIs_music() {
                return is_music;
            }

            public void setIs_music(int is_music) {
                this.is_music = is_music;
            }

            public int getMember_type() {
                return member_type;
            }

            public void setMember_type(int member_type) {
                this.member_type = member_type;
            }

            public int getFloor() {
                return floor;
            }

            public void setFloor(int floor) {
                this.floor = floor;
            }

            public String getHits_text() {
                return hits_text;
            }

            public void setHits_text(String hits_text) {
                this.hits_text = hits_text;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public HeadInfoBean getHead_info() {
                return head_info;
            }

            public void setHead_info(HeadInfoBean head_info) {
                this.head_info = head_info;
            }

            public List<HashtagBean> getHashtag() {
                return hashtag;
            }

            public void setHashtag(List<HashtagBean> hashtag) {
                this.hashtag = hashtag;
            }

            public List<String> getImglist_link() {
                return imglist_link;
            }

            public void setImglist_link(List<String> imglist_link) {
                this.imglist_link = imglist_link;
            }

            public List<ImglistInfoBean> getImglist_info() {
                return imglist_info;
            }

            public void setImglist_info(List<ImglistInfoBean> imglist_info) {
                this.imglist_info = imglist_info;
            }

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


            public static class HeadInfoBean {
                /**
                 * ext : png
                 * w : 300
                 * h : 300
                 * size : 89825
                 * is_long : 0
                 * md5 : 59ef1fb87804dcea7b066a98ca709bdfb0c70c53
                 * link : https://api.yuanyintang.com/image/59ef1fb87804dcea7b066a98ca709bdfb0c70c53/3
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

            public static class HashtagBean {
                /**
                 * id : 64
                 * title : 搞事情委员会
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

            public static class ImglistInfoBean {
                /**
                 * ext : jpg
                 * w : 1080
                 * h : 10452
                 * size : 4705782
                 * is_long : 1
                 * link : https://api.yuanyintang.com/image/1b6178f2a30df00913dc133294c52ce9/3
                 * md5 : 1b6178f2a30df00913dc133294c52ce9
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
        }
    }
}

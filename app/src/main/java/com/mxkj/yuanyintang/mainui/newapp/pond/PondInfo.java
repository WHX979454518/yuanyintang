package com.mxkj.yuanyintang.mainui.newapp.pond;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.mxkj.yuanyintang.mainui.dynamic.bean.DynamicBean;
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean;

import java.io.Serializable;
import java.util.List;

public class PondInfo implements Serializable {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
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
             * title : 源音塘音乐 — 全新的二次元音乐社区
             * keywords : 源音塘|原创|二次元音乐|满汉女神|加入音乐人|Vocaloid|动漫游戏|古风|三次元|翻唱歌曲|二次元|音乐人|咕噜吧啦|幻音
             * description : 源音塘是全新的以二次元音乐为主的音乐社区。这里有让耳朵怀孕的丰富良曲、极富魅力的音乐人和偶尔破次元的音乐同好。每天,故事和音乐都在这里
             */

            private String title;
            private String keywords;
            private String description;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class DataListBean implements MultiItemEntity, Serializable {


            @Override
            public int getItemType() {
                return 0;
            }

            /**
             * id : 1448
             * title : 不说
             * source : 1
             * content : 不说
             * hashtag : [{"id":3,"title":"古风"},{"id":274,"title":"阴阳师SAMA"}]
             * agrees : 0
             * place_desc :
             * share_counts : 0
             * imglist : []
             * create_time : 27分钟前
             * update_time : 25分钟前
             * thcount : 0
             * hits : 83
             * recommend : 0
             * uid : 50340
             * nickname : 郑君婵
             * head : 3d3d27527715c56204c6e063e34883ae60384a97
             * is_music : 0
             * item_type : 0
             * item_id : 0
             * member_type : 1
             * floor : 1
             * distance : -1
             * item_info : {}
             * is_relation : 0
             * is_agree : 0
             * agrees_text : 0
             * share_counts_text : 0
             * hits_text : 83
             * head_link : http://testapi.demo.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3
             * head_info :
             * imglist_link : ["http://testapi.demo.com//image/47bc8e2481fb784a5bd19946f7ca34e0/3"]
             * imglist_info : []
             */


            private int id;

            private int is_self;

            private boolean isPlaying;
            private String title;
            private String share_topic_detail = "";
            private int source;
            private String content;
            private int agrees;
            private String place_desc;
            private int share_counts;
            private String create_time;
            private String update_time;
            private int thcount;
            private int hits;
            private int uid;
            private String nickname;
            private String head;
            private int is_music;
            private int item_type;
            private int item_id;
            private int member_type;
            private int floor;
            private String distance = "";
            private ItemInfoBean item_info;
            private String share_url;
            private int is_relation;
            private int is_agree;
            private String agrees_text = "0";
            private String thcount_text = "0";
            private String share_counts_text = "0";
            private String hits_text;
            private String head_link;
            private HomeBean.ImgpicInfoBean head_info;
            private List<HashtagBean> hashtag;
            private List<String> imglist_link;
            private List<DynamicBean.DataBean.ImglistInfoBean> imglist_info;

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

            public String getShare_topic_detail() {
                return share_topic_detail;
            }

            public void setShare_topic_detail(String share_topic_detail) {
                this.share_topic_detail = share_topic_detail;
            }

            public boolean isPlaying() {
                return isPlaying;
            }

            public void setPlaying(boolean playing) {
                isPlaying = playing;
            }

            public String getThcount_text() {
                return thcount_text;
            }

            public void setThcount_text(String thcount_text) {
                this.thcount_text = thcount_text;
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

            public int getSource() {
                return source;
            }

            public void setSource(int source) {
                this.source = source;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getAgrees() {
                return agrees;
            }

            public void setAgrees(int agrees) {
                this.agrees = agrees;
            }

            public String getPlace_desc() {
                return place_desc;
            }

            public void setPlace_desc(String place_desc) {
                this.place_desc = place_desc;
            }

            public int getShare_counts() {
                return share_counts;
            }

            public void setShare_counts(int share_counts) {
                this.share_counts = share_counts;
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

            public int getItem_type() {
                return item_type;
            }

            public void setItem_type(int item_type) {
                this.item_type = item_type;
            }

            public int getItem_id() {
                return item_id;
            }

            public void setItem_id(int item_id) {
                this.item_id = item_id;
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

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public ItemInfoBean getItem_info() {
                return item_info;
            }

            public void setItem_info(ItemInfoBean item_info) {
                this.item_info = item_info;
            }

            public int getIs_relation() {
                return is_relation;
            }

            public void setIs_relation(int is_relation) {
                this.is_relation = is_relation;
            }

            public int getIs_agree() {
                return is_agree;
            }

            public void setIs_agree(int is_agree) {
                this.is_agree = is_agree;
            }

            public String getAgrees_text() {
                return agrees_text;
            }

            public void setAgrees_text(String agrees_text) {
                this.agrees_text = agrees_text;
            }

            public String getShare_counts_text() {
                return share_counts_text;
            }

            public void setShare_counts_text(String share_counts_text) {
                this.share_counts_text = share_counts_text;
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

            public HomeBean.ImgpicInfoBean getHead_info() {
                return head_info;
            }

            public void setHead_info(HomeBean.ImgpicInfoBean head_info) {
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

            public List<DynamicBean.DataBean.ImglistInfoBean> getImglist_info() {
                return imglist_info;
            }

            public void setImglist_info(List<DynamicBean.DataBean.ImglistInfoBean> imglist_info) {
                this.imglist_info = imglist_info;
            }

            public static class ItemInfoBean {
                private int id;
                private int status;
                private int is_private ;
                private String title;
                private String video;
                private String imgpic;
                private int uid;
                private int music_type;
                private String nickname;
                private String mv;
                private int item_type;
                private String video_link;
                private VideoInfoBean video_info;
                private String imgpic_link;
                private HomeBean.ImgpicInfoBean imgpic_info;
                private HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean.MvInfoBean mv_info;



                private String activityAlias;
                private String sinaDescription;
                private String sub_title;
                private String topicContent;
                private String url;


                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getIs_private() {
                    return is_private;
                }

                public void setIs_private(int is_private) {
                    this.is_private = is_private;
                }

                public String getActivityAlias() {
                    return activityAlias;
                }

                public void setActivityAlias(String activityAlias) {
                    this.activityAlias = activityAlias;
                }

                public String getSinaDescription() {
                    return sinaDescription;
                }

                public void setSinaDescription(String sinaDescription) {
                    this.sinaDescription = sinaDescription;
                }

                public String getSub_title() {
                    return sub_title;
                }

                public void setSub_title(String sub_title) {
                    this.sub_title = sub_title;
                }

                public String getTopicContent() {
                    return topicContent;
                }

                public void setTopicContent(String topicContent) {
                    this.topicContent = topicContent;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getMv() {
                    return mv;
                }

                public void setMv(String mv) {
                    this.mv = mv;
                }

                public int getItem_type() {
                    return item_type;
                }

                public void setItem_type(int item_type) {
                    this.item_type = item_type;
                }

                public HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean.MvInfoBean getMv_info() {
                    return mv_info;
                }

                public void setMv_info(HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean.MvInfoBean mv_info) {
                    this.mv_info = mv_info;
                }

                public int getMusic_type() {
                    return music_type;
                }

                public void setMusic_type(int music_type) {
                    this.music_type = music_type;
                }

                /**
                 * counts : 9
                 * total : 3
                 * counts_text : 9
                 */



                private int counts;
                private int total;
                private String counts_text;
                /**
                 * content : 呃呃呃正文正文正文正文正文
                 * imgpic_info : {"ext":"","w":"","h":"","size":"15811","is_long":"0","md5":"0b7389d8c6a0a877c2c2d57f44474e3b","link":"http://api.demo.com//image/0b7389d8c6a0a877c2c2d57f44474e3b/1"}
                 */

                private String content;
                @SerializedName("imgpic_info")
                private ImgpicInfoBean imgpic_infoX;

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

                public String getVideo() {
                    return video;
                }

                public void setVideo(String video) {
                    this.video = video;
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

                public String getVideo_link() {
                    return video_link;
                }

                public void setVideo_link(String video_link) {
                    this.video_link = video_link;
                }

                public VideoInfoBean getVideo_info() {
                    return video_info;
                }

                public void setVideo_info(VideoInfoBean video_info) {
                    this.video_info = video_info;
                }

                public String getImgpic_link() {
                    return imgpic_link;
                }

                public void setImgpic_link(String imgpic_link) {
                    this.imgpic_link = imgpic_link;
                }

                public HomeBean.ImgpicInfoBean getImgpic_info() {
                    return imgpic_info;
                }

                public void setImgpic_info(HomeBean.ImgpicInfoBean imgpic_info) {
                    this.imgpic_info = imgpic_info;
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

                public String getCounts_text() {
                    return counts_text;
                }

                public void setCounts_text(String counts_text) {
                    this.counts_text = counts_text;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public ImgpicInfoBean getImgpic_infoX() {
                    return imgpic_infoX;
                }

                public void setImgpic_infoX(ImgpicInfoBean imgpic_infoX) {
                    this.imgpic_infoX = imgpic_infoX;
                }

                public static class VideoInfoBean {
                    /**
                     * ext : mp3
                     * size : 8600503
                     * playtime : 03:34
                     * bitrate : 320
                     * md5 : e9abeb99d90dc2c0aef929bd3bc98aad395ea943
                     * link : http://testapi.imxkj.com//music/e9abeb99d90dc2c0aef929bd3bc98aad395ea943.mp3?log_at=1
                     */

                    private String ext;
                    private String size;
                    private String playtime;
                    private String bitrate;
                    private String md5;
                    private String link;

                    public String getExt() {
                        return ext;
                    }

                    public void setExt(String ext) {
                        this.ext = ext;
                    }

                    public String getSize() {
                        return size;
                    }

                    public void setSize(String size) {
                        this.size = size;
                    }

                    public String getPlaytime() {
                        return playtime;
                    }

                    public void setPlaytime(String playtime) {
                        this.playtime = playtime;
                    }

                    public String getBitrate() {
                        return bitrate;
                    }

                    public void setBitrate(String bitrate) {
                        this.bitrate = bitrate;
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

                public static class ImgpicInfoBean {
                    /**
                     * ext :
                     * w :
                     * h :
                     * size : 15811
                     * is_long : 0
                     * md5 : 0b7389d8c6a0a877c2c2d57f44474e3b
                     * link : http://api.demo.com//image/0b7389d8c6a0a877c2c2d57f44474e3b/1
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

            public static class HashtagBean {
                /**
                 * id : 3
                 * title : 古风
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

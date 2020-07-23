package com.mxkj.yuanyintang.mainui.home.bean;

import com.google.gson.annotations.SerializedName;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.entity.MultiItemEntity;
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/9/25.
 */

public class MusicIndex implements Serializable {
    public static class ItemInfoListBean implements MultiItemEntity, Serializable {
        public String itemType;
        public int tagT;
        private int id;
        private String title;
        private String video;
        private String counts_text;


        public String getCounts_text() {
            return counts_text;
        }

        public void setCounts_text(String counts_text) {
            this.counts_text = counts_text;
        }

        private int counts;
        private int comment;
        private int shares;
        private int downloads;
        private int category;
        private int coin;
        private int collects;
        private int event_id;
        private String tag;
        private int song;
        private String imgpic;
        private String original;
        private String lyrics;
        private String intro="";
        private int create_time;
        private int uid;
        private String nickname;
        private String head;
        private int sex;
        private int member_type;
        private int type;
        private int is_music;
        private int fans_num;
        private int attention_num;
        private int ips_num;
        private String signature;
        private int status;
        private int collection;
        private int agrees;
        private int is_agree;
        private int isCollection;
        private String event_alias;
        private String event_head;
        private String event_img;
        private String lyrics_data;
        private String share_url;
        private int relation;
        private int music_type;
        private String playtime;
        private String bitrate;
        private String second;

        @Override
        public String toString() {
            return "ItemInfoListBean{" +
                    "itemType='" + itemType + '\'' +
                    ", tagT=" + tagT +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", video='" + video + '\'' +
                    ", counts_text='" + counts_text + '\'' +
                    ", counts=" + counts +
                    ", comment=" + comment +
                    ", shares=" + shares +
                    ", downloads=" + downloads +
                    ", category=" + category +
                    ", coin=" + coin +
                    ", collects=" + collects +
                    ", event_id=" + event_id +
                    ", tag='" + tag + '\'' +
                    ", song=" + song +
                    ", imgpic='" + imgpic + '\'' +
                    ", original='" + original + '\'' +
                    ", lyrics='" + lyrics + '\'' +
                    ", intro='" + intro + '\'' +
                    ", create_time=" + create_time +
                    ", uid=" + uid +
                    ", nickname='" + nickname + '\'' +
                    ", head='" + head + '\'' +
                    ", sex=" + sex +
                    ", member_type=" + member_type +
                    ", type=" + type +
                    ", is_music=" + is_music +
                    ", fans_num=" + fans_num +
                    ", attention_num=" + attention_num +
                    ", ips_num=" + ips_num +
                    ", signature='" + signature + '\'' +
                    ", status=" + status +
                    ", collection=" + collection +
                    ", agrees=" + agrees +
                    ", is_agree=" + is_agree +
                    ", isCollection=" + isCollection +
                    ", event_alias='" + event_alias + '\'' +
                    ", event_head='" + event_head + '\'' +
                    ", event_img='" + event_img + '\'' +
                    ", lyrics_data='" + lyrics_data + '\'' +
                    ", share_url='" + share_url + '\'' +
                    ", relation=" + relation +
                    ", music_type=" + music_type +
                    ", playtime='" + playtime + '\'' +
                    ", bitrate='" + bitrate + '\'' +
                    ", second='" + second + '\'' +
                    ", video_info=" + video_info +
                    ", member=" + member +
                    ", imgpic_info=" + imgpic_info +
                    ", head_link='" + head_link + '\'' +
                    ", event_head_link='" + event_head_link + '\'' +
                    ", event_img_link='" + event_img_link + '\'' +
                    ", song_all=" + song_all +
                    ", tags=" + tags +
                    ", likes=" + likes +
                    ", related=" + related +
                    ", music=" + music +
                    ", recommend=" + recommend +
                    ", remark='" + remark + '\'' +
                    ", pondList=" + pondList +
                    '}';
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public int getTagT() {
            return tagT;
        }

        public void setTagT(int tagT) {
            this.tagT = tagT;
        }

        public int getMusic_type() {
            return music_type;
        }

        public void setMusic_type(int music_type) {
            this.music_type = music_type;
        }

        //        private String video_link;
//        private String imgpic_link;
        private MusicInfo.DataBean.VideoInfoBean video_info;

        private List<MemberBeanX> member;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<MemberBeanX> getMember() {
            return member;
        }

        public void setMember(List<MemberBeanX> member) {
            this.member = member;
        }


        public static class MemberBeanX implements Serializable {
            /**
             * music_type : 唱见
             * member : [{"uid":50523,"nickname":"yyyy","music_type":"唱见","head":"3443102d221701fefdcd555b192952c4","is_music":"0","head_link":"http://api.demo.com//image/3443102d221701fefdcd555b192952c4/3","head_info":{"ext":"png","w":"300","h":"300","size":"191282","is_long":"0","md5":"3443102d221701fefdcd555b192952c4","link":"http://api.demo.com//image/3443102d221701fefdcd555b192952c4/3"}},{"uid":50526,"nickname":"源音塘429832","music_type":"唱见","head":"f1f6b2f1f49e3cffa93fc306b0ac6dd0","is_music":"0","head_link":"http://api.demo.com//image/f1f6b2f1f49e3cffa93fc306b0ac6dd0/3","head_info":{"ext":"png","w":"300","h":"300","size":"91075","is_long":"0","md5":"f1f6b2f1f49e3cffa93fc306b0ac6dd0","link":"http://api.demo.com//image/f1f6b2f1f49e3cffa93fc306b0ac6dd0/3"}}]
             */

            private String music_type;
            private List<MemberBean> member;

            public String getMusic_type() {
                return music_type;
            }

            public void setMusic_type(String music_type) {
                this.music_type = music_type;
            }

            public List<MemberBean> getMember() {
                return member;
            }

            public void setMember(List<MemberBean> member) {
                this.member = member;
            }

            public static class MemberBean implements Serializable {
                /**
                 * uid : 50523
                 * nickname : yyyy
                 * music_type : 唱见
                 * head : 3443102d221701fefdcd555b192952c4
                 * is_music : 0
                 * head_link : http://api.demo.com//image/3443102d221701fefdcd555b192952c4/3
                 * head_info : {"ext":"png","w":"300","h":"300","size":"191282","is_long":"0","md5":"3443102d221701fefdcd555b192952c4","link":"http://api.demo.com//image/3443102d221701fefdcd555b192952c4/3"}
                 */

                private int uid;
                private String nickname;
                private String music_type;
                private String head;
                private String is_music;
                private String head_link;
                private HomeBean.ImgpicInfoBean head_info;

                public int getUid() {
                    return uid;
                }

                public void setUid(int uid) {
                    this.uid = uid;
                }

                public String getNickname() {
                    return nickname == null ? "" : nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getMusic_type() {
                    return music_type;
                }

                public void setMusic_type(String music_type) {
                    this.music_type = music_type;
                }

                public String getHead() {
                    return head;
                }

                public void setHead(String head) {
                    this.head = head;
                }

                public String getIs_music() {
                    return is_music;
                }

                public void setIs_music(String is_music) {
                    this.is_music = is_music;
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

                public static class HeadInfoBean implements Serializable {
                    /**
                     * ext : png
                     * w : 300
                     * h : 300
                     * size : 191282
                     * is_long : 0
                     * md5 : 3443102d221701fefdcd555b192952c4
                     * link : http://api.demo.com//image/3443102d221701fefdcd555b192952c4/3
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

        public static class VideoInfoBean implements Serializable {

            /**
             * ext : mp3
             * size : 18302306
             * playtime : 07:37
             * bitrate : 320
             * md5 : 4b1b917652c542247565aa5ace5a7a5c
             * link : http://api.demo.com//music/4b1b917652c542247565aa5ace5a7a5c/1
             */

            private String ext;
            private int size;
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

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
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

        public static class MvInfoBean implements Serializable{
            /**
             * ext : m4a
             * size : 445069
             * playtime : 00:05
             * bitrate : 597
             * height :640
             * width:360
             * fps :25
             * md5 : 3707d269505bc28be2864f5d87e7343b
             * link : http:\/\/testapi.imxkj.com\/\/video\/3707d269505bc28be2864f5d87e7343b.mp4?log_at=3
             */
            private String ext;
            private int size;
            private String playtime;
            private String bitrate;
            private int height;
            private int width;
            private int fps;
            private String md5;
            private String link;

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
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

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getFps() {
                return fps;
            }

            public void setFps(int fps) {
                this.fps = fps;
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


        public MusicInfo.DataBean.VideoInfoBean getVideo_info() {
            return video_info;
        }

        public void setVideo_info(MusicInfo.DataBean.VideoInfoBean video_info) {
            this.video_info = video_info;
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

        private String head_link;
        private String event_head_link;
        private String event_img_link;
        private List<SongAllBean> song_all;
        private List<TagsBean> tags;
        private List<LikesBean> likes;
        private List<RelatedBean> related;
        private List<MusicBean> music;
        private List<RecommendBean> recommend;
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public int getItemType() {
            if ("listening".equals(itemType)) {
                return Constant.MUSIC_TYPE_LISTENING;
            } else if ("synopsis".equals(itemType)) {
                return Constant.MUSIC_TYPE_SYNOPSIS;
            } else if ("song".equals(itemType)) {
                return Constant.MUSIC_TYPE_SONG;
            } else if ("recommendSong".equals(itemType)) {
                return Constant.MUSIC_TYPE_RECOMMEND_SONG;
            } else if ("pond".equals(itemType)) {
                return Constant.MUSIC_TYPE_POND;
            } else if ("music".equals(itemType)) {
                return Constant.MUSIC_TYPE_MUSIC;
            } else if ("recommend".equals(itemType)) {
                return Constant.MUSIC_TYPE_RECOMMEND;
            } else if ("giftItem".equals(itemType)) {
                return Constant.MUSIC_TYPE_GIFT_LIST;
            } else if ("relatedPartnerView".equals(itemType)) {
                return Constant.MUSIC_TYPE_RELATED_PARTNER;
            }
            return Constant.TYPE_OTHER;
        }

        public int getIs_agree() {
            return is_agree;
        }

        public void setIs_agree(int is_agree) {
            this.is_agree = is_agree;
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

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
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

        public int getShares() {
            return shares;
        }

        public void setShares(int shares) {
            this.shares = shares;
        }

        public int getAgrees() {
            return agrees;
        }

        public void setAgrees(int agrees) {
            this.agrees = agrees;
        }

        public int getDownloads() {
            return downloads;
        }

        public void setDownloads(int downloads) {
            this.downloads = downloads;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getCollects() {
            return collects;
        }

        public void setCollects(int collects) {
            this.collects = collects;
        }

        public int getEvent_id() {
            return event_id;
        }

        public void setEvent_id(int event_id) {
            this.event_id = event_id;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getSong() {
            return song;
        }

        public void setSong(int song) {
            this.song = song;
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

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

        public int getFans_num() {
            return fans_num;
        }

        public void setFans_num(int fans_num) {
            this.fans_num = fans_num;
        }

        public int getAttention_num() {
            return attention_num;
        }

        public void setAttention_num(int attention_num) {
            this.attention_num = attention_num;
        }

        public int getIps_num() {
            return ips_num;
        }

        public void setIps_num(int ips_num) {
            this.ips_num = ips_num;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
        }

        public int getIsCollection() {
            return isCollection;
        }

        public void setIsCollection(int isCollection) {
            this.isCollection = isCollection;
        }

        public String getEvent_alias() {
            return event_alias;
        }

        public void setEvent_alias(String event_alias) {
            this.event_alias = event_alias;
        }

        public String getEvent_head() {
            return event_head;
        }

        public void setEvent_head(String event_head) {
            this.event_head = event_head;
        }

        public String getEvent_img() {
            return event_img;
        }

        public void setEvent_img(String event_img) {
            this.event_img = event_img;
        }

        public String getLyrics_data() {
            return lyrics_data;
        }

        public void setLyrics_data(String lyrics_data) {
            this.lyrics_data = lyrics_data;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
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

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

//        public String getVideo_link() {
//            return video_link;
//        }
//
//        public void setVideo_link(String video_link) {
//            this.video_link = video_link;
//        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }
//
//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public String getEvent_head_link() {
            return event_head_link;
        }

        public void setEvent_head_link(String event_head_link) {
            this.event_head_link = event_head_link;
        }

        public String getEvent_img_link() {
            return event_img_link;
        }

        public void setEvent_img_link(String event_img_link) {
            this.event_img_link = event_img_link;
        }

        public List<SongAllBean> getSong_all() {
            return song_all;
        }

        public void setSong_all(List<SongAllBean> song_all) {
            this.song_all = song_all;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<LikesBean> getLikes() {
            return likes;
        }

        public void setLikes(List<LikesBean> likes) {
            this.likes = likes;
        }


        public List<RelatedBean> getRelated() {
            return related;
        }

        public void setRelated(List<RelatedBean> related) {
            this.related = related;
        }

        public List<MusicBean> getMusic() {
            return music;
        }

        public void setMusic(List<MusicBean> music) {
            this.music = music;
        }

        public List<RecommendBean> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<RecommendBean> recommend) {
            this.recommend = recommend;
        }

        public static class SongAllBean implements Serializable {
            /**
             * id : 643
             * title : 风格
             * counts : 14
             * comment : 0
             * total : 5
             * imgpic : 9a627aafc2e4c3f128bb02e4bba85d2d
             * uid : 31144
             * nickname : 食发鬼设法
             * imgpic_link : http://yyt.demo.com/image/9a627aafc2e4c3f128bb02e4bba85d2d
             */

            private int id;
            private String title;
            private int counts;
            private int comment;
            private int total;
            private String imgpic;
            private int uid;
            private String nickname;
//            private String imgpic_link;


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

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }

        }

        public static class TagsBean implements Serializable {
            /**
             * id : 303
             * title : 一巷江湖
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

        public static class LikesBean implements Serializable {

            /**
             * id : 3726
             * title : 月照杨花舞
             * imgpic :
             * uid : 31085
             * nickname : 坎坎坷坷
             * counts : 106
             * video : 55abc176caa21cb040e5d088414992e5
             * playtime :
             * collection : 0
             * video_link : http://yyt.demo.com/music//music/55abc176caa21cb040e5d088414992e5
             */

            private int id;
            private String title;
            private String imgpic;
            private int uid;
            private String nickname;
            private int counts;
            private String video;
            private String playtime;
            private int collection;
            //            private String video_link;
//            private String imgpic_link;
            private MusicInfo.DataBean.VideoInfoBean video_info;

            public static class VideoInfoBean implements Serializable {

                /**
                 * ext : mp3
                 * size : 18302306
                 * playtime : 07:37
                 * bitrate : 320
                 * md5 : 4b1b917652c542247565aa5ace5a7a5c
                 * link : http://api.demo.com//music/4b1b917652c542247565aa5ace5a7a5c/1
                 */

                private String ext;
                private int size;
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

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
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

            public MusicInfo.DataBean.VideoInfoBean getVideo_info() {
                return video_info;
            }

            public void setVideo_info(MusicInfo.DataBean.VideoInfoBean video_info) {
                this.video_info = video_info;
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


//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }

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

            public int getCounts() {
                return counts;
            }

            public void setCounts(int counts) {
                this.counts = counts;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getPlaytime() {
                return playtime;
            }

            public void setPlaytime(String playtime) {
                this.playtime = playtime;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
            }

//            public String getVideo_link() {
//                return video_link;
//            }
//
//            public void setVideo_link(String video_link) {
//                this.video_link = video_link;
//            }
        }

        List<PondInfo.DataBean.DataListBean> pondList;

        public List<PondInfo.DataBean.DataListBean> getPondList() {
            return pondList;
        }

        public void setPondList(List<PondInfo.DataBean.DataListBean> pondList) {
            this.pondList = pondList;
        }

        public static class RelatedBean implements Serializable {
            private int uid;
            private int is_music;
            private int day;
            private int relation;
            private String nickname;
            private String head_link;
            private String signature;

            public RelatedBean() {
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getIs_music() {
                return is_music;
            }

            public void setIs_music(int is_music) {
                this.is_music = is_music;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getRelation() {
                return relation;
            }

            public void setRelation(int relation) {
                this.relation = relation;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }
        }

        public static class MusicBean implements Serializable {


            /**
             * sid : 5119
             * id : 4066
             * title : 搁浅
             * counts : 15
             * comment : 2
             * imgpic : 523ad2ab47d894e035c90a352b9bc821
             * original : dcc2c90ca196dd3ae14fc200d8b8b741
             * song_id : 771
             * playtime : 04:00
             * uid : 31085
             * nickname : 坎坎坷坷
             * video : 088d8dd13bce31bd1c724cadaafab126
             * up_time : 1508496934
             * collection : 0
             * imgpic_link : http://yyt.demo.com/image/523ad2ab47d894e035c90a352b9bc821
             * original_link : http://yyt.demo.com/image/dcc2c90ca196dd3ae14fc200d8b8b741
             * video_link : http://yyt.demo.com/music//music/088d8dd13bce31bd1c724cadaafab126
             */

            private int sid;
            private int id;
            private String title;
            private int counts;
            private int comment;
            private String imgpic;
            private String original;
            private int music_type;
            private int song_id;
            private String playtime="";
            private int uid;
            private String video;
            private int up_time;
            private String mv;
            private int status;
            private int is_collection;
            private int collection;
            private String sex;
            private String nickname;
            private String head;
            private String counts_text;
            private String comment_text;
            private String imgpic_link;
            private ImgpicInfoBean imgpicinfo;
            private String original_link;
            private String video_link;
            private VideoInfoBean videoinfo;
            private MvInfoBean mvinfo;


            private Boolean check;
            private boolean ischeck;
            private Boolean single_selection;
            private boolean isLocalMusic;

            @Override
            public String toString() {
                return "MusicBean{" +
                        "sid=" + sid +
                        ", id=" + id +
                        ", title='" + title + '\'' +
                        ", counts=" + counts +
                        ", comment=" + comment +
                        ", imgpic='" + imgpic + '\'' +
                        ", original='" + original + '\'' +
                        ", music_type=" + music_type +
                        ", song_id=" + song_id +
                        ", playtime='" + playtime + '\'' +
                        ", uid=" + uid +
                        ", video='" + video + '\'' +
                        ", up_time=" + up_time +
                        ", mv='" + mv + '\'' +
                        ", status=" + status +
                        ", is_collection=" + is_collection +
                        ", collection=" + collection +
                        ", sex='" + sex + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", head='" + head + '\'' +
                        ", counts_text='" + counts_text + '\'' +
                        ", comment_text='" + comment_text + '\'' +
                        ", imgpic_link='" + imgpic_link + '\'' +
                        ", imgpicinfo=" + imgpicinfo +
                        ", original_link='" + original_link + '\'' +
                        ", video_link='" + video_link + '\'' +
                        ", videoinfo=" + videoinfo +
                        ", mvinfo=" + mvinfo +
                        ", check=" + check +
                        ", ischeck=" + ischeck +
                        ", single_selection=" + single_selection +
                        ", isLocalMusic=" + isLocalMusic +
                        ", mvInfoBean=" + mvInfoBean +
                        ", isPlaying=" + isPlaying +
                        ", image_info=" + image_info +
                        ", video_info=" + video_info +
                        ", mv_info=" + mv_info +
                        ", imgpic_info=" + imgpic_info +
                        '}';
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public MusicBean() {
            }

            public int getIs_collection() {
                return is_collection;
            }

            public void setIs_collection(int is_collection) {
                this.is_collection = is_collection;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getCounts_text() {
                return counts_text;
            }

            public void setCounts_text(String counts_text) {
                this.counts_text = counts_text;
            }

            public String getComment_text() {
                return comment_text;
            }

            public void setComment_text(String comment_text) {
                this.comment_text = comment_text;
            }

            public String getImgpic_link() {
                return imgpic_link;
            }

            public void setImgpic_link(String imgpic_link) {
                this.imgpic_link = imgpic_link;
            }

            public ImgpicInfoBean getImgpicinfo() {
                return imgpicinfo;
            }

            public void setImgpicinfo(ImgpicInfoBean imgpicinfo) {
                this.imgpicinfo = imgpicinfo;
            }

            public String getVideo_link() {
                return video_link;
            }

            public void setVideo_link(String video_link) {
                this.video_link = video_link;
            }

            public VideoInfoBean getVideoinfo() {
                return videoinfo;
            }

            public void setVideoinfo(VideoInfoBean videoinfo) {
                this.videoinfo = videoinfo;
            }

            public MvInfoBean getMvinfo() {
                return mvinfo;
            }

            public void setMvinfo(MvInfoBean mvinfo) {
                this.mvinfo = mvinfo;
            }

            //            private MvInfoBean mvInfoBean;
            private MusicInfo.DataBean.MvInfoBean mvInfoBean;

            public MusicInfo.DataBean.MvInfoBean getMvInfoBean() {
                return mvInfoBean;
            }

            public void setMvInfoBean(MusicInfo.DataBean.MvInfoBean mvInfoBean) {
                this.mvInfoBean = mvInfoBean;
            }

            public String getMv() {
                return mv;
            }

            public void setMv(String mv) {
                this.mv = mv;
            }

            public int getMusic_type() {
                return music_type;
            }

            public void setMusic_type(int music_type) {
                this.music_type = music_type;
            }

            public boolean isLocalMusic() {
                return isLocalMusic;
            }

            public void setLocalMusic(boolean localMusic) {
                isLocalMusic = localMusic;
            }

            public Boolean getLocalMusic() {
                return isLocalMusic;
            }

            public void setLocalMusic(Boolean localMusic) {
                isLocalMusic = localMusic;
            }

            boolean isPlaying;

            public boolean isPlaying() {
                return isPlaying;
            }

            public void setPlaying(boolean playing) {
                isPlaying = playing;
            }

            public boolean isIscheck() {
                return ischeck;
            }

            public void setIscheck(boolean ischeck) {
                this.ischeck = ischeck;
            }

            public Boolean getCheck() {
                return check == null ? false : check;
            }

            public void setCheck(Boolean check) {
                this.check = check;
            }

            public Boolean getSingle_selection() {
                if (null == single_selection) {
                    return false;
                }
                return single_selection;
            }

            public void setSingle_selection(Boolean single_selection) {
                this.single_selection = single_selection;
            }


            private MusicInfo.DataBean.ImgpicInfoBean image_info;

            public MusicInfo.DataBean.ImgpicInfoBean getImage_info() {
                return image_info;
            }

            public void setImage_info(MusicInfo.DataBean.ImgpicInfoBean image_info) {
                this.image_info = image_info;
            }

            private MusicInfo.DataBean.VideoInfoBean video_info;


            private MusicInfo.DataBean.MvInfoBean mv_info;

            public MusicInfo.DataBean.MvInfoBean getMv_info() {
                return mv_info;
            }

            public void setMv_info(MusicInfo.DataBean.MvInfoBean mv_info) {
                this.mv_info = mv_info;
            }

            //            private HomeBean.MvInfoBean mv_info;
//
//            public HomeBean.MvInfoBean getMv_info() {
//                return mv_info;
//            }
//
//            public void setMv_info(HomeBean.MvInfoBean mv_info) {
//                this.mv_info = mv_info;
//            }



            public static class VideoInfoBean implements Serializable {

                /**
                 * ext : mp3
                 * size : 18302306
                 * playtime : 07:37
                 * bitrate : 320
                 * md5 : 4b1b917652c542247565aa5ace5a7a5c
                 * link : http://api.demo.com//music/4b1b917652c542247565aa5ace5a7a5c/1
                 */

                private String ext;
                private int size;
                private String playtime;
                private String bitrate;
                private String md5;
                private String link;

                public VideoInfoBean() {
                }

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
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

            public MusicInfo.DataBean.VideoInfoBean getVideo_info() {
                return video_info;
            }

            public void setVideo_info(MusicInfo.DataBean.VideoInfoBean video_info) {
                this.video_info = video_info;
            }

            private HomeBean.ImgpicInfoBean imgpic_info;

            public HomeBean.ImgpicInfoBean getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(HomeBean.ImgpicInfoBean imgpic_info) {
                this.imgpic_info = imgpic_info;
            }



            public static class MvInfoBean implements Serializable{
                /**
                 * ext : m4a
                 * size : 445069
                 * playtime : 00:05
                 * bitrate : 597
                 * height :640
                 * width:360
                 * fps :25
                 * md5 : 3707d269505bc28be2864f5d87e7343b
                 * link : http:\/\/testapi.imxkj.com\/\/video\/3707d269505bc28be2864f5d87e7343b.mp4?log_at=3
                 */
                private String ext;
                private int size;
                private String playtime;
                private String bitrate;
                private int height;
                private int width;
                private int fps;
                private String md5;
                private String link;


                @Override
                public String toString() {
                    return "MvInfoBean{" +
                            "ext='" + ext + '\'' +
                            ", size=" + size +
                            ", playtime='" + playtime + '\'' +
                            ", bitrate='" + bitrate + '\'' +
                            ", height=" + height +
                            ", width=" + width +
                            ", fps=" + fps +
                            ", md5='" + md5 + '\'' +
                            ", link='" + link + '\'' +
                            '}';
                }

                public MvInfoBean() {
                }

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
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

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getFps() {
                    return fps;
                }

                public void setFps(int fps) {
                    this.fps = fps;
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

                public ImgpicInfoBean() {
                }

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


            public int getSid() {
                return sid;
            }

            public void setSid(int sid) {
                this.sid = sid;
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

            public int getSong_id() {
                return song_id;
            }

            public void setSong_id(int song_id) {
                this.song_id = song_id;
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

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public int getUp_time() {
                return up_time;
            }

            public void setUp_time(int up_time) {
                this.up_time = up_time;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
            }

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }

            public String getOriginal_link() {
                return original_link;
            }

            public void setOriginal_link(String original_link) {
                this.original_link = original_link;
            }

//            public String getVideo_link() {
//                return video_link;
//            }
//
//            public void setVideo_link(String video_link) {
//                this.video_link = video_link;
//            }
        }

        public static class RecommendBean implements Serializable {

            /**
             * id : 209
             * title : 2222311132sd
             * imgpic : fb1855c447ace5ebc1ab47f9a2f9f3a292b24c64
             * counts : 0
             * uid : 31075
             * nickname : 源音塘853554
             * imgpic_link : http://yyt.demo.com/image/fb1855c447ace5ebc1ab47f9a2f9f3a292b24c64
             */


            private int id;
            private String title;
            private int counts;
            private int uid;
            private String nickname;
            private HomeBean.ImgpicInfoBean imgpic_info;
            /**
             * show_type : 1
             * show_desc : 新歌速递
             * uid :
             * id :
             */

            private int show_type;
            private String show_desc;

            public RecommendBean() {
            }

            public HomeBean.ImgpicInfoBean getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(HomeBean.ImgpicInfoBean imgpic_info) {
                this.imgpic_info = imgpic_info;
            }

            public int getShow_type() {
                return show_type;
            }

            public void setShow_type(int show_type) {
                this.show_type = show_type;
            }

            public String getShow_desc() {
                return show_desc;
            }

            public void setShow_desc(String show_desc) {
                this.show_desc = show_desc;
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

                public ImgpicInfoBean() {
                }

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

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }
        }


    }

}

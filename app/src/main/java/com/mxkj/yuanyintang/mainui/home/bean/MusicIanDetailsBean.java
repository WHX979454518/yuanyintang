package com.mxkj.yuanyintang.mainui.home.bean;

import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/9/28.
 */

public class MusicIanDetailsBean {

    /**
     * song : [{"id":567,"type":0,"title":"大幅度发过火","counts":3,"total":1,"share":0,"imgpic":"45f2722d9fb5324112adffa4cd3f80f55130f020","uid":31123,"nickname":"栀子丶","imgpic_link":"http://www.tp.com/image/45f2722d9fb5324112adffa4cd3f80f55130f020"}]
     * release : [{"id":3057,"title":"111","counts":1,"imgpic":"9eafc6f557e4aa2b3d468be2737f332df400a1a5","uid":31123,"nickname":"栀子丶","video":"b0d7d2cfae66ed449686d0678531bb778b1ab633","playtime":"04:26","collection":0,"imgpic_link":"http://www.tp.com/image/9eafc6f557e4aa2b3d468be2737f332df400a1a5","video_link":"http://yyt.demo.com/music/b0d7d2cfae66ed449686d0678531bb778b1ab633"}]
     * collection : [{"id":564,"title":"哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦","counts":0,"total":1,"imgpic":"9b0de9257a0854250692162e3ebc30e116812623","uid":31123,"nickname":"君禅是不是傻呀，哈哈哈哈","imgpic_link":"http://www.tp.com/image/9b0de9257a0854250692162e3ebc30e116812623"}]
     * topic : [{"id":824,"title":"让他和人","content":"任天野","hashtag":[{"id":4,"title":"原创"},{"id":5,"title":"原创1"}],"imglist":"9eafc6f557e4aa2b3d468be2737f332df400a1a5","create_time":"08-28","update_time":"08-28","thcount":0,"recommend":0,"uid":31123,"nickname":"栀子丶","head":"f06141013e338279e2a7464b0296f36c9b54a150","member_type":1,"is_music":1,"imglist_link":["http://www.tp.com/image/9eafc6f557e4aa2b3d468be2737f332df400a1a5"],"head_link":"http://www.tp.com/image/f06141013e338279e2a7464b0296f36c9b54a150"}]
     * box : {"song_id":1,"id":1,"uid":30513,"create_time":123213,"title":"焕卿最喜欢的音乐","nickname":"焕卿","imgpic":"423432423423423","type":1,"imgpic_link":"http://yyt.demo.com/image/423432423423423"}
     */

    private BoxBean box;
    private List<SongBean> song;
    private List<ReleaseBean> release;
    private List<CollectionBean> collection;
    private List<TopicBean> topic;

    public BoxBean getBox() {
        return box;
    }

    public void setBox(BoxBean box) {
        this.box = box;
    }

    public List<SongBean> getSong() {
        return song;
    }

    public void setSong(List<SongBean> song) {
        this.song = song;
    }

    public List<ReleaseBean> getRelease() {
        return release;
    }

    public void setRelease(List<ReleaseBean> release) {
        this.release = release;
    }

    public List<CollectionBean> getCollection() {
        return collection;
    }

    public void setCollection(List<CollectionBean> collection) {
        this.collection = collection;
    }

    public List<TopicBean> getTopic() {
        return topic;
    }

    public void setTopic(List<TopicBean> topic) {
        this.topic = topic;
    }

    public static class BoxBean {


        /**
         * nickname : 食发鬼设法
         * id : 862
         * uid : 31144
         * title : 食发鬼设法喜欢的歌曲
         * imgpic : 1740b8a089a4a19c8e11ba9787b6642a
         * status : 1
         * create_time : 1508227744
         * is_rec : 0
         * sort : 1
         * counts : 8
         * tag :
         * remark :
         * collection : 0
         * agrees : 0
         * share : 0
         * fans_num : 303
         * ips_num : 1141
         * signature : 666
         * attention_num : 2000
         * type : 1
         * comment : 0
         * head : 5f05a96e34555e1a6de3d4bdbd2e1adf
         * sex : 0
         * member_type : 2
         * total : 53
         * is_music : 3
         * original :
         * imgpic_type : 0
         * imgpic_link : http://yyt.demo.com/image/1740b8a089a4a19c8e11ba9787b6642a
         * head_link : http://yyt.demo.com/image/5f05a96e34555e1a6de3d4bdbd2e1adf
         * original_link :
         */

        private String nickname;
        private int id;
        private int uid;
        private String title;
        private String imgpic;
        private int status;
        private int create_time;
        private int is_rec;
        private int sort;
        private int counts;
        private String tag;
        private String remark;
        private int collection;
        private int agrees;
        private int share;
        private int fans_num;
        private int ips_num;
        private String signature;
        private int attention_num;
        private int type;
        private int comment;
        private String head;
        private int sex;
        private int member_type;
        private int total;
        private int is_music;
        private String original;
        private int imgpic_type;
//        private String imgpic_link;
        private String head_link;
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


        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getIs_rec() {
            return is_rec;
        }

        public void setIs_rec(int is_rec) {
            this.is_rec = is_rec;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
        }

        public int getAgrees() {
            return agrees;
        }

        public void setAgrees(int agrees) {
            this.agrees = agrees;
        }

        public int getShare() {
            return share;
        }

        public void setShare(int share) {
            this.share = share;
        }

        public int getFans_num() {
            return fans_num;
        }

        public void setFans_num(int fans_num) {
            this.fans_num = fans_num;
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

        public int getAttention_num() {
            return attention_num;
        }

        public void setAttention_num(int attention_num) {
            this.attention_num = attention_num;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
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

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public int getImgpic_type() {
            return imgpic_type;
        }

        public void setImgpic_type(int imgpic_type) {
            this.imgpic_type = imgpic_type;
        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }

//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public String getOriginal_link() {
            return original_link;
        }

        public void setOriginal_link(String original_link) {
            this.original_link = original_link;
        }
    }

    public static class SongBean {
        /**
         * id : 567
         * type : 0
         * title : 大幅度发过火
         * counts : 3
         * total : 1
         * share : 0
         * imgpic : 45f2722d9fb5324112adffa4cd3f80f55130f020
         * uid : 31123
         * nickname : 栀子丶
         * imgpic_link : http://www.tp.com/image/45f2722d9fb5324112adffa4cd3f80f55130f020
         */

        private int id;
        private int type;
        private String title;
        private int counts;
        private int total;
        private int share;
        private String imgpic;
        private int uid;
        private String nickname;
//        private String imgpic_link;


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

//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }
    }

    public static class ReleaseBean {
        /**
         * id : 3057
         * title : 111
         * counts : 1
         * imgpic : 9eafc6f557e4aa2b3d468be2737f332df400a1a5
         * uid : 31123
         * nickname : 栀子丶
         * video : b0d7d2cfae66ed449686d0678531bb778b1ab633
         * playtime : 04:26
         * collection : 0
         * imgpic_link : http://www.tp.com/image/9eafc6f557e4aa2b3d468be2737f332df400a1a5
         * video_link : http://yyt.demo.com/music/b0d7d2cfae66ed449686d0678531bb778b1ab633
         */

        private int id;
        private String title;
        private int counts;
        private String imgpic;
        private int uid;
        private String nickname;
        private String video;
        private String playtime;
        private int collection;
//        private String imgpic_link;
//        private String video_link;
private MusicInfo.DataBean.VideoInfoBean video_info;
        public static class VideoInfoBean implements Serializable{

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

//        public String getImgpic_link() {
//            return imgpic_link;
//        }

//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }

//        public String getVideo_link() {
//            return video_link;
//        }
//
//        public void setVideo_link(String video_link) {
//            this.video_link = video_link;
//        }
    }

    public static class CollectionBean {
        /**
         * id : 564
         * title : 哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦哦
         * counts : 0
         * total : 1
         * imgpic : 9b0de9257a0854250692162e3ebc30e116812623
         * uid : 31123
         * nickname : 君禅是不是傻呀，哈哈哈哈
         * imgpic_link : http://www.tp.com/image/9b0de9257a0854250692162e3ebc30e116812623
         */

        private int id;
        private String title;
        private int counts;
        private int total;
        private String imgpic;
        private int uid;
        private String nickname;
//        private String imgpic_link;


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

//        public String getImgpic_link() {
//            return imgpic_link;
//        }

//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }
    }

    public static class TopicBean {
        /**
         * id : 824
         * title : 让他和人
         * content : 任天野
         * hashtag : [{"id":4,"title":"原创"},{"id":5,"title":"原创1"}]
         * imglist : 9eafc6f557e4aa2b3d468be2737f332df400a1a5
         * create_time : 08-28
         * update_time : 08-28
         * thcount : 0
         * recommend : 0
         * uid : 31123
         * nickname : 栀子丶
         * head : f06141013e338279e2a7464b0296f36c9b54a150
         * member_type : 1
         * is_music : 1
         * imglist_link : ["http://www.tp.com/image/9eafc6f557e4aa2b3d468be2737f332df400a1a5"]
         * head_link : http://www.tp.com/image/f06141013e338279e2a7464b0296f36c9b54a150
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
        private List<HashtagBean> hashtag;
//        private List<String> imglist_link;

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
             * id : 4
             * title : 原创
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

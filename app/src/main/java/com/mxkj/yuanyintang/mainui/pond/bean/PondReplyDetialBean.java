package com.mxkj.yuanyintang.mainui.pond.bean;

import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/30.
 */

public class PondReplyDetialBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"id":1093,"title":"","content":"给哥哥看看离婚后鼓励鼓励考虑考虑里捡垃圾","agrees":0,"hashtag":"","music_id":4095,"imglist":"e516453c485a07d8988fdada61b3b50d,1ca4439d4ad0f241457026bfcbc452f7,d54d317e5b3a7dd93b9f5a825fa91e72,da28b76f8532bb49e6ac465f3eecc45b,7dee180673b0930e47ec3b3edae1506e","create_time":"37分钟前","thcount":0,"hits":1,"pid":1087,"recommend":0,"uid":31103,"nickname":"LLLLL","head":"45ebf7c0cbc416152894b0d8bd19d08a","sex":1,"signature":"11","fans_num":11,"ips_num":862,"is_music":1,"member_type":1,"choice":0,"music_title":"同标签4少两个歌曲名：get out","music_uid":31085,"music_nickname":"坎坎坷坷","imgpic":"8e2a65bc669813eb6e128c153143ce9b","video":"3746b193b3e29721e1cd2feaabc0f2a0","is_agree":0,"share_topic_discuss":"http://app.yuanyintang.com/topic/detail?id=1093","relation":4,"is_vote":0,"is_vote_id":"","imglist_link":["http://yyt.demo.com/image/e516453c485a07d8988fdada61b3b50d","http://yyt.demo.com/image/1ca4439d4ad0f241457026bfcbc452f7","http://yyt.demo.com/image/d54d317e5b3a7dd93b9f5a825fa91e72","http://yyt.demo.com/image/da28b76f8532bb49e6ac465f3eecc45b","http://yyt.demo.com/image/7dee180673b0930e47ec3b3edae1506e"],"head_link":"http://yyt.demo.com/image/45ebf7c0cbc416152894b0d8bd19d08a","imgpic_link":"http://yyt.demo.com/image/8e2a65bc669813eb6e128c153143ce9b","video_link":"http://yyt.demo.com//music/3746b193b3e29721e1cd2feaabc0f2a0"}
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

    public static class DataBean implements Serializable{
        /**
         * id : 1093
         * title :
         * content : 给哥哥看看离婚后鼓励鼓励考虑考虑里捡垃圾
         * agrees : 0
         * hashtag :
         * music_id : 4095
         * imglist : e516453c485a07d8988fdada61b3b50d,1ca4439d4ad0f241457026bfcbc452f7,d54d317e5b3a7dd93b9f5a825fa91e72,da28b76f8532bb49e6ac465f3eecc45b,7dee180673b0930e47ec3b3edae1506e
         * create_time : 37分钟前
         * thcount : 0
         * hits : 1
         * pid : 1087
         * recommend : 0
         * uid : 31103
         * nickname : LLLLL
         * head : 45ebf7c0cbc416152894b0d8bd19d08a
         * sex : 1
         * signature : 11
         * fans_num : 11
         * ips_num : 862
         * is_music : 1
         * member_type : 1
         * choice : 0
         * music_title : 同标签4少两个歌曲名：get out
         * music_uid : 31085
         * music_nickname : 坎坎坷坷
         * imgpic : 8e2a65bc669813eb6e128c153143ce9b
         * video : 3746b193b3e29721e1cd2feaabc0f2a0
         * is_agree : 0
         * share_topic_discuss : http://app.yuanyintang.com/topic/detail?id=1093
         * relation : 4
         * is_vote : 0
         * is_vote_id :
         * imglist_link : ["http://yyt.demo.com/image/e516453c485a07d8988fdada61b3b50d","http://yyt.demo.com/image/1ca4439d4ad0f241457026bfcbc452f7","http://yyt.demo.com/image/d54d317e5b3a7dd93b9f5a825fa91e72","http://yyt.demo.com/image/da28b76f8532bb49e6ac465f3eecc45b","http://yyt.demo.com/image/7dee180673b0930e47ec3b3edae1506e"]
         * head_link : http://yyt.demo.com/image/45ebf7c0cbc416152894b0d8bd19d08a
         * imgpic_link : http://yyt.demo.com/image/8e2a65bc669813eb6e128c153143ce9b
         * video_link : http://yyt.demo.com//music/3746b193b3e29721e1cd2feaabc0f2a0
         */

        private int id;
        private String title;
        private String content;
        private int agrees;
        private String hashtag;
        private int music_id;
        private String imglist;
        private String create_time;
        private int thcount;
        private int hits;
        private int pid;
        private int recommend;
        private int uid;
        private String nickname;
        private String head;
        private int sex;
        private String signature;
        private int fans_num;
        private int ips_num;
        private int is_music;
        private int member_type;
        private int choice;
        private String music_title;
        private int music_uid;
        private String music_nickname;
        private String imgpic;
        private String video;
        private int is_agree;
        private String share_topic_discuss;
        private int relation;
        private int is_vote;
        private String is_vote_id;
        private String head_link;
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

        public int getAgrees() {
            return agrees;
        }

        public void setAgrees(int agrees) {
            this.agrees = agrees;
        }

        public String getHashtag() {
            return hashtag;
        }

        public void setHashtag(String hashtag) {
            this.hashtag = hashtag;
        }

        public int getMusic_id() {
            return music_id;
        }

        public void setMusic_id(int music_id) {
            this.music_id = music_id;
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

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
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

        public int getChoice() {
            return choice;
        }

        public void setChoice(int choice) {
            this.choice = choice;
        }

        public String getMusic_title() {
            return music_title;
        }

        public void setMusic_title(String music_title) {
            this.music_title = music_title;
        }

        public int getMusic_uid() {
            return music_uid;
        }

        public void setMusic_uid(int music_uid) {
            this.music_uid = music_uid;
        }

        public String getMusic_nickname() {
            return music_nickname;
        }

        public void setMusic_nickname(String music_nickname) {
            this.music_nickname = music_nickname;
        }

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public int getIs_agree() {
            return is_agree;
        }

        public void setIs_agree(int is_agree) {
            this.is_agree = is_agree;
        }

        public String getShare_topic_discuss() {
            return share_topic_discuss;
        }

        public void setShare_topic_discuss(String share_topic_discuss) {
            this.share_topic_discuss = share_topic_discuss;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }

        public int getIs_vote() {
            return is_vote;
        }

        public void setIs_vote(int is_vote) {
            this.is_vote = is_vote;
        }

        public String getIs_vote_id() {
            return is_vote_id;
        }

        public void setIs_vote_id(String is_vote_id) {
            this.is_vote_id = is_vote_id;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }
//
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

//        public List<String> getImglist_link() {
//            return imglist_link;
//        }
//
//        public void setImglist_link(List<String> imglist_link) {
//            this.imglist_link = imglist_link;
//        }
    }
}

package com.mxkj.yuanyintang.mainui.myself.bean;

import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/9.
 */

public class MyCollectionBean {


    /**
     * code : 200
     * msg : success
     * data : [{"id":4058,"title":"love me for me","imgpic":"a7565c329329d6f3410af3ee2a3b46c3","video":"77ddd29f099593353680175d505a594f","counts":72,"comment":0,"song_id":879,"playtime":"","uid":31144,"nickname":"食发鬼设法","collection":1,"imgpic_link":"http://www.1.com/image/a7565c329329d6f3410af3ee2a3b46c3","video_link":"http://yyt.demo.com//music/77ddd29f099593353680175d505a594f"},{"id":4059,"title":"幻宠大陆伴奏","imgpic":"a7565c329329d6f3410af3ee2a3b46c3","video":"7ae21d9d527b360b1287e11e4af216f3","counts":100023,"comment":1,"song_id":879,"playtime":"03:02","uid":31066,"nickname":"一一樱","collection":1,"imgpic_link":"http://www.1.com/image/a7565c329329d6f3410af3ee2a3b46c3","video_link":"http://yyt.demo.com//music/7ae21d9d527b360b1287e11e4af216f3"},{"id":3066,"title":"111","imgpic":"9a627aafc2e4c3f128bb02e4bba85d2d","video":"de0781c17b6db367830358b7f84e33f6","counts":1033,"comment":4,"song_id":879,"playtime":"04:32","uid":31130,"nickname":"柔柔弱弱","collection":1,"imgpic_link":"http://www.1.com/image/9a627aafc2e4c3f128bb02e4bba85d2d","video_link":"http://yyt.demo.com//music/de0781c17b6db367830358b7f84e33f6"},{"id":4062,"title":"我怀念的你","imgpic":"1740b8a089a4a19c8e11ba9787b6642a","video":"bf8f4d6810201078aa5798b2f1bc5dca","counts":43,"comment":2,"song_id":879,"playtime":"03:45","uid":31085,"nickname":"坎坎坷坷","collection":1,"imgpic_link":"http://www.1.com/image/1740b8a089a4a19c8e11ba9787b6642a","video_link":"http://yyt.demo.com//music/bf8f4d6810201078aa5798b2f1bc5dca"},{"id":4061,"title":"意义在哪里","imgpic":"1730917e595c4c37c69e4b8f28cce0ed","video":"89cba74497cd5f5d65383cfc2ed9ce70","counts":59,"comment":1,"song_id":879,"playtime":"03:52","uid":31085,"nickname":"坎坎坷坷","collection":1,"imgpic_link":"http://www.1.com/image/1730917e595c4c37c69e4b8f28cce0ed","video_link":"http://yyt.demo.com//music/89cba74497cd5f5d65383cfc2ed9ce70"}]
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
         * id : 4058
         * title : love me for me
         * imgpic : a7565c329329d6f3410af3ee2a3b46c3
         * video : 77ddd29f099593353680175d505a594f
         * counts : 72
         * comment : 0
         * song_id : 879
         * playtime :
         * uid : 31144
         * nickname : 食发鬼设法
         * collection : 1
         * imgpic_link : http://www.1.com/image/a7565c329329d6f3410af3ee2a3b46c3
         * video_link : http://yyt.demo.com//music/77ddd29f099593353680175d505a594f
         */

        private int id;
        private int sid;
        private String title;
        private String imgpic;
        private String video;
        private int counts;
        private int comment;
        private int song_id;
        private String playtime;
        private int uid;
        private int music_type;
        private int isdown;
        private int status;
        private int is_collection;

        private String nickname;
        private int collection;
//        private String imgpic_link;
//        private String video_link;
        private Boolean check;
        private MusicInfo.DataBean.VideoInfoBean video_info;

        private Boolean playing;

        public Boolean getPlaying() {
            return playing;
        }

        public void setPlaying(Boolean playing) {
            this.playing = playing;
        }

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
        private Boolean single_selection;


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

        public int getMusic_type() {
            return music_type;
        }

        public void setMusic_type(int music_type) {
            this.music_type = music_type;
        }

        public int getIsdown() {
            return isdown;
        }

        public void setIsdown(int isdown) {
            this.isdown = isdown;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_collection() {
            return is_collection;
        }

        public void setIs_collection(int is_collection) {
            this.is_collection = is_collection;
        }

        public Boolean getCheck() {
            if (null == check) {
                return false;
            }
            return check;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
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
}

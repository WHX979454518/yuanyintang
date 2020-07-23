package com.mxkj.yuanyintang.mainui.myself.bean;


import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/12.
 */

public class PlayerHistoryBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : [{"id":4059,"title":"幻宠大陆伴奏","counts":100023,"comment":1,"playtime":"03:02","update_time":1509536904,"pid":235,"uid":31066,"nickname":"一一樱","video":"7ae21d9d527b360b1287e11e4af216f3","imgpic":"a7565c329329d6f3410af3ee2a3b46c3","collection":0,"video_link":"http://yyt.demo.com//music/7ae21d9d527b360b1287e11e4af216f3","imgpic_link":"http://yyt.demo.com/image/a7565c329329d6f3410af3ee2a3b46c3"},{"id":4062,"title":"我怀念的你","counts":43,"comment":2,"playtime":"03:45","update_time":1509535885,"pid":398,"uid":31085,"nickname":"坎坎坷坷","video":"bf8f4d6810201078aa5798b2f1bc5dca","imgpic":"1740b8a089a4a19c8e11ba9787b6642a","collection":0,"video_link":"http://yyt.demo.com//music/bf8f4d6810201078aa5798b2f1bc5dca","imgpic_link":"http://yyt.demo.com/image/1740b8a089a4a19c8e11ba9787b6642a"},{"id":4095,"title":"同标签4少两个歌曲名：get out","counts":10,"comment":3,"playtime":"03:48","update_time":1509345270,"pid":397,"uid":31085,"nickname":"坎坎坷坷","video":"3746b193b3e29721e1cd2feaabc0f2a0","imgpic":"8e2a65bc669813eb6e128c153143ce9b","collection":0,"video_link":"http://yyt.demo.com//music/3746b193b3e29721e1cd2feaabc0f2a0","imgpic_link":"http://yyt.demo.com/image/8e2a65bc669813eb6e128c153143ce9b"},{"id":4057,"title":"16号爱人","counts":20,"comment":0,"playtime":"","update_time":1509345250,"pid":396,"uid":31144,"nickname":"食发鬼设法","video":"03737086d1d2ab629093e02843b99579","imgpic":"1e1352c61ff88990a111077d36852bdf","collection":0,"video_link":"http://yyt.demo.com//music/03737086d1d2ab629093e02843b99579","imgpic_link":"http://yyt.demo.com/image/1e1352c61ff88990a111077d36852bdf"},{"id":4073,"title":"卓玛","counts":13,"comment":0,"playtime":"04:55","update_time":1508988083,"pid":305,"uid":31126,"nickname":"小一","video":"fa33f9156428e3892285326549615878","imgpic":"8ce5bd7a79d1a3a4daac3225cb591e9c","collection":0,"video_link":"http://yyt.demo.com//music/fa33f9156428e3892285326549615878","imgpic_link":"http://yyt.demo.com/image/8ce5bd7a79d1a3a4daac3225cb591e9c"},{"id":4091,"title":"告白告白告白告白告白告白告白告白告白告白","counts":69,"comment":3,"playtime":"04:28","update_time":1508808866,"pid":298,"uid":31087,"nickname":"名字在长遇见小学生也不怕啦啦哈","video":"7cadc2459793ecaa65cb0e64f27454ab","imgpic":"53544bc2326b80430046b0abda7f8ac3","collection":0,"video_link":"http://yyt.demo.com//music/7cadc2459793ecaa65cb0e64f27454ab","imgpic_link":"http://yyt.demo.com/image/53544bc2326b80430046b0abda7f8ac3"},{"id":3806,"title":"以后的以后","counts":18,"comment":0,"playtime":"","update_time":1508765919,"pid":238,"uid":31065,"nickname":"YY ","video":"d6cade0cf047fde2ca9b946724c91035","imgpic":"","collection":0,"video_link":"http://yyt.demo.com//music/d6cade0cf047fde2ca9b946724c91035"},{"id":4076,"title":"还记得1111111111111","counts":11,"comment":1,"playtime":"04:10","update_time":1508764122,"pid":233,"uid":31168,"nickname":"有动态的账号","video":"089d945e1ca229f159e2636f20d68025","imgpic":"1562742b774f8318990d3e95dc1f7479","collection":0,"video_link":"http://yyt.demo.com//music/089d945e1ca229f159e2636f20d68025","imgpic_link":"http://yyt.demo.com/image/1562742b774f8318990d3e95dc1f7479"},{"id":3084,"title":"Suite","counts":45,"comment":4,"playtime":"","update_time":1508144468,"pid":244,"uid":31085,"nickname":"坎坎坷坷","video":"368d6b434dfdc0752b5240f719015845","imgpic":"afa61e64d183eeec01c0f8abc0274c99","collection":0,"video_link":"http://yyt.demo.com//music/368d6b434dfdc0752b5240f719015845","imgpic_link":"http://yyt.demo.com/image/afa61e64d183eeec01c0f8abc0274c99"}]
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
         * id : 4059
         * title : 幻宠大陆伴奏
         * counts : 100023
         * comment : 1
         * playtime : 03:02
         * update_time : 1509536904
         * pid : 235
         * uid : 31066
         * nickname : 一一樱
         * video : 7ae21d9d527b360b1287e11e4af216f3
         * imgpic : a7565c329329d6f3410af3ee2a3b46c3
         * collection : 0
         * video_link : http://yyt.demo.com//music/7ae21d9d527b360b1287e11e4af216f3
         * imgpic_link : http://yyt.demo.com/image/a7565c329329d6f3410af3ee2a3b46c3
         */

        private int id;
        private int song_id;
        private String title;
        private String song_title;
        private int counts;
        private int status;
        private int comment;
        private String playtime;
        private int update_time;
        private int pid;
        private int uid;
        private String nickname;
        private String video;
        private String imgpic;
        private int collection;

        private int music_type;
        private int isdown;

//        private String video_link;
//        private String imgpic_link;
        private Boolean check;
        private VideoInfoBean video_info;
        private ImgpicInfoBean imgpic_info;

        public VideoInfoBean getVideo_info() {
            return video_info;
        }

        public void setVideo_info(VideoInfoBean video_info) {
            this.video_info = video_info;
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

        @Override
        public String toString() {
            return "DataBean{" +
                    "video_info=" + video_info +
                    '}';
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


        private Boolean single_selection;

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

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
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

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
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

//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }

        public int getSong_id() {
            return song_id;
        }

        public void setSong_id(int song_id) {
            this.song_id = song_id;
        }

        public String getSong_title() {
            return song_title;
        }

        public void setSong_title(String song_title) {
            this.song_title = song_title;
        }
    }
}

package com.mxkj.yuanyintang.mainui.myself.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/24.
 */

public class MyReleaseBean implements Serializable {


    /**
     * code : 200
     * msg : success
     * data : [{"id":4077,"title":"旋涡2","video":"c06b03b78ba7b0137b381f0f1b3461ff","counts":71,"coin":0,"comment":1,"updata":"","create_time":"10-16","update_time":"10-25","collects":1,"playtime":"03:57","tag":[{"id":109,"title":"插曲"},{"id":269,"title":"全明星"}],"nickname":"LLLLL","imgpic":"85000760755caa0147b4fdd7e8c7d36b","original":"3c4da3d51a53bd70a22a9f718dae1f5c","music_type":0,"isdown":1,"downloads":0,"reason":"","admin_status":2,"status":1,"video_link":"http://yyt.demo.com//music/c06b03b78ba7b0137b381f0f1b3461ff","imgpic_link":"http://yyt.demo.com/image/85000760755caa0147b4fdd7e8c7d36b","original_link":"http://yyt.demo.com/image/3c4da3d51a53bd70a22a9f718dae1f5c"},{"id":4072,"title":"一事无成","video":"21254254913348f22ca9018ebf5292aa","counts":10,"coin":0,"comment":18,"updata":"","create_time":"10-11","update_time":"10-27","collects":1,"playtime":"03:10","tag":[{"id":106,"title":"男声"}],"nickname":"LLLLL","imgpic":"96ee2899bd16ba7c9c64d937a64365cb","original":"","music_type":1,"isdown":1,"downloads":0,"reason":"","admin_status":2,"status":1,"video_link":"http://yyt.demo.com//music/21254254913348f22ca9018ebf5292aa","imgpic_link":"http://yyt.demo.com/image/96ee2899bd16ba7c9c64d937a64365cb"},{"id":4070,"title":"愚公移山","video":"8eb19c59721a0fbf3fd6836710775a49","counts":50,"coin":0,"comment":0,"updata":"","create_time":"10-11","update_time":"10-11","collects":0,"playtime":"03:26","tag":[{"id":160,"title":"成精系列"},{"id":165,"title":"电音"},{"id":260,"title":"全明星"},{"id":261,"title":"中国风"}],"nickname":"LLLLL","imgpic":"a3fcd6e85fc1e1fe0c0fd0c1640d2250","original":"4a2dc38f43819048e79e518335df00ba","music_type":1,"isdown":1,"downloads":0,"reason":"","admin_status":4,"status":0,"video_link":"http://yyt.demo.com//music/8eb19c59721a0fbf3fd6836710775a49","imgpic_link":"http://yyt.demo.com/image/a3fcd6e85fc1e1fe0c0fd0c1640d2250","original_link":"http://yyt.demo.com/image/4a2dc38f43819048e79e518335df00ba"},{"id":4069,"title":"最爱最爱最爱最爱最爱最爱最爱最爱最爱最爱","video":"59a7ff3a8e9d468a3b639f7cad507fda","counts":0,"coin":0,"comment":0,"updata":"","create_time":"10-11","update_time":"10-11","collects":0,"playtime":"04:25","tag":[{"id":160,"title":"成精系列"},{"id":165,"title":"电音"},{"id":260,"title":"全明星"},{"id":261,"title":"中国风"}],"nickname":"LLLLL","imgpic":"a3fcd6e85fc1e1fe0c0fd0c1640d2250","original":"4a2dc38f43819048e79e518335df00ba","music_type":1,"isdown":1,"downloads":0,"reason":"","admin_status":4,"status":0,"video_link":"http://yyt.demo.com//music/59a7ff3a8e9d468a3b639f7cad507fda","imgpic_link":"http://yyt.demo.com/image/a3fcd6e85fc1e1fe0c0fd0c1640d2250","original_link":"http://yyt.demo.com/image/4a2dc38f43819048e79e518335df00ba"},{"id":4068,"title":"枫","video":"e655e8434bd2e4a1bc9546ddb2547ccd","counts":45,"coin":0,"comment":12,"updata":"","create_time":"10-11","update_time":"10-27","collects":1,"playtime":"04:35","tag":[{"id":112,"title":"原创曲"},{"id":269,"title":"全明星"}],"nickname":"LLLLL","imgpic":"acdcfbe29e90933bcc6ae479ab42f918","original":"cbe1edb0b54d1c5c1127b51c6bf88f2e","music_type":1,"isdown":1,"downloads":0,"reason":"","admin_status":2,"status":1,"video_link":"http://yyt.demo.com//music/e655e8434bd2e4a1bc9546ddb2547ccd","imgpic_link":"http://yyt.demo.com/image/acdcfbe29e90933bcc6ae479ab42f918","original_link":"http://yyt.demo.com/image/cbe1edb0b54d1c5c1127b51c6bf88f2e"}]
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
        /**
         * id : 4077
         * title : 旋涡2
         * video : c06b03b78ba7b0137b381f0f1b3461ff
         * counts : 71
         * coin : 0
         * comment : 1
         * updata :
         * create_time : 10-16
         * update_time : 10-25
         * collects : 1
         * playtime : 03:57
         * tag : [{"id":109,"title":"插曲"},{"id":269,"title":"全明星"}]
         * nickname : LLLLL
         * imgpic : 85000760755caa0147b4fdd7e8c7d36b
         * original : 3c4da3d51a53bd70a22a9f718dae1f5c
         * music_type : 0
         * isdown : 1
         * downloads : 0
         * reason :
         * admin_status : 2
         * status : 1
         * video_link : http://yyt.demo.com//music/c06b03b78ba7b0137b381f0f1b3461ff
         * imgpic_link : http://yyt.demo.com/image/85000760755caa0147b4fdd7e8c7d36b
         * original_link : http://yyt.demo.com/image/3c4da3d51a53bd70a22a9f718dae1f5c
         */

        private int id;
        private String title;
        private String mv;
        private String video;
        private int counts;
        private int coin;
        private int comment;
        private int can_delete;
        private int apply_off_status;
        private String updata;
        private String create_time;
        private String update_time;
        private int collects;
        private String playtime;
        private String nickname;
        private String imgpic;
        private String original;
        private int music_type;
        private int isdown;
        private int downloads;
        private String reason;
        private int admin_status;
        private int status;
        private int uid;
        //        private String video_link;
//        private String imgpic_link;
        private String original_link;
        private List<TagBean> tag;
        private List<MvInfoBean> mv_info;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", mv='" + mv + '\'' +
                    ", video='" + video + '\'' +
                    ", counts=" + counts +
                    ", coin=" + coin +
                    ", comment=" + comment +
                    ", can_delete=" + can_delete +
                    ", apply_off_status=" + apply_off_status +
                    ", updata='" + updata + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", update_time='" + update_time + '\'' +
                    ", collects=" + collects +
                    ", playtime='" + playtime + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", imgpic='" + imgpic + '\'' +
                    ", original='" + original + '\'' +
                    ", music_type=" + music_type +
                    ", isdown=" + isdown +
                    ", downloads=" + downloads +
                    ", reason='" + reason + '\'' +
                    ", admin_status=" + admin_status +
                    ", status=" + status +
                    ", original_link='" + original_link + '\'' +
                    ", tag=" + tag +
                    ", mv_info=" + mv_info +
                    ", video_info=" + video_info +
                    ", imgpic_info=" + imgpic_info +
                    '}';
        }

        public String getMv() {
            return mv;
        }

        public void setMv(String mv) {
            this.mv = mv;
        }

        public List<MvInfoBean> getMv_info() {
            return mv_info;
        }

        public void setMv_info(List<MvInfoBean> mv_info) {
            this.mv_info = mv_info;
        }

        public int getCan_delete() {
            return can_delete;
        }

        public void setCan_delete(int can_delete) {
            this.can_delete = can_delete;
        }

        public int getApply_off_status() {
            return apply_off_status;
        }

        public void setApply_off_status(int apply_off_status) {
            this.apply_off_status = apply_off_status;
        }

        private MusicInfo.DataBean.VideoInfoBean video_info;

        @Override
        public int getItemType() {

            switch (admin_status) {
                case -1://未上线
                    switch (can_delete) {
                        case 0://不能删除
                            return 10;
                        case 1:
                            return 11;
                    }
                case 0://未上线
                    switch (can_delete) {
                        case 0://不能删除
                            return 10;
                        case 1:
                            return 11;
                    }
                case 1://审核中
                    return 1;

                case 2://已上线
                    switch (apply_off_status) {
                        case 0:
                            return 20;
                        case 1:
                            return 21;

                        case 2:
                            return 22;
                    }
                case 4://草稿
                    return 4;

            }
            return 0;
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

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public String getUpdata() {
            return updata;
        }

        public void setUpdata(String updata) {
            this.updata = updata;
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

        public int getCollects() {
            return collects;
        }

        public void setCollects(int collects) {
            this.collects = collects;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public int getDownloads() {
            return downloads;
        }

        public void setDownloads(int downloads) {
            this.downloads = downloads;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public int getAdmin_status() {
            return admin_status;
        }

        public void setAdmin_status(int admin_status) {
            this.admin_status = admin_status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public String getOriginal_link() {
            return original_link;
        }

        public void setOriginal_link(String original_link) {
            this.original_link = original_link;
        }

        public List<TagBean> getTag() {
            return tag;
        }

        public void setTag(List<TagBean> tag) {
            this.tag = tag;
        }

        public static class TagBean implements Serializable {
            /**
             * id : 109
             * title : 插曲
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
        public static class MvInfoBean implements Serializable {
            /**
             "ext": "m4a",
             "size": 2018850,
             "playtime": "00:04",
             "bitrate": "3902",
             "height": 480,
             "width": 640,
             "fps": 25,
             "md5": "01c28596df7cf5564746e338495a0120",
             "link": "http:\/\/testapi.imxkj.com\/\/video\/01c28596df7cf5564746e338495a0120.mp4?log_at=3"
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
    }
}

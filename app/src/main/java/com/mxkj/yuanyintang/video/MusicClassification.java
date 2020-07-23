package com.mxkj.yuanyintang.video;

import java.io.Serializable;
import java.util.List;

public class MusicClassification implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"data_list":[{"id":43257,"title":"窃香（剧情版）","imgpic":"5aab20996b4406c4fa40e1fab58530c2","comment":1,"counts":7715,"playtime":"","uid":93244,"nickname":"晃儿","video":"F6625902162A7F371D52C55C909FABF7","dayhits":184,"weekhits":326,"monthhits":423,"music_type":1,"mv":"2f7a493679a95608e9d2087dba979a81","is_collection":0,"collection":0,"song_id":0,"imgpic_link":"https://api.yuanyintang.com/image/5aab20996b4406c4fa40e1fab58530c2/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"218213","is_long":"0","md5":"5aab20996b4406c4fa40e1fab58530c2","link":"https://api.yuanyintang.com/image/5aab20996b4406c4fa40e1fab58530c2/3"},"comment_text":"1","counts_text":"7715","video_link":"http://api.yuanyintang.com/music/F6625902162A7F371D52C55C909FABF7.mp3?log_at=3","video_info":{"ext":"mp3","size":"9982627","playtime":"04:07","bitrate":"322","md5":"F6625902162A7F371D52C55C909FABF7","link":"http://api.yuanyintang.com/music/F6625902162A7F371D52C55C909FABF7.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":146671773,"playtime":"04:08","bitrate":"4728","height":1080,"width":1920,"fps":25,"md5":"2f7a493679a95608e9d2087dba979a81","link":"https://api.yuanyintang.com/video/2f7a493679a95608e9d2087dba979a81.mp4?log_at=3"},"collection_text":"0"},{"id":43195,"title":"秦楼月","imgpic":"0ed6329a7399162346ebf656fe63e757","comment":0,"counts":34,"playtime":"","uid":37933,"nickname":"裂天","video":"921A8DA91AC6267FEF6EC467F90B003D","dayhits":1,"weekhits":1,"monthhits":1,"music_type":1,"mv":"","is_collection":0,"collection":0,"song_id":0,"imgpic_link":"https://api.yuanyintang.com/image/0ed6329a7399162346ebf656fe63e757/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"152848","is_long":"0","md5":"0ed6329a7399162346ebf656fe63e757","link":"https://api.yuanyintang.com/image/0ed6329a7399162346ebf656fe63e757/3"},"comment_text":"0","counts_text":"34","video_link":"http://api.yuanyintang.com/music/921A8DA91AC6267FEF6EC467F90B003D.mp3?log_at=3","video_info":{"ext":"mp3","size":"9906607","playtime":"04:07","bitrate":"320","md5":"921A8DA91AC6267FEF6EC467F90B003D","link":"http://api.yuanyintang.com/music/921A8DA91AC6267FEF6EC467F90B003D.mp3?log_at=3"},"mv_info":{},"collection_text":"0"}],"seo":{}}
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

    public static class DataBean {
        /**
         * data_list : [{"id":43257,"title":"窃香（剧情版）","imgpic":"5aab20996b4406c4fa40e1fab58530c2","comment":1,"counts":7715,"playtime":"","uid":93244,"nickname":"晃儿","video":"F6625902162A7F371D52C55C909FABF7","dayhits":184,"weekhits":326,"monthhits":423,"music_type":1,"mv":"2f7a493679a95608e9d2087dba979a81","is_collection":0,"collection":0,"song_id":0,"imgpic_link":"https://api.yuanyintang.com/image/5aab20996b4406c4fa40e1fab58530c2/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"218213","is_long":"0","md5":"5aab20996b4406c4fa40e1fab58530c2","link":"https://api.yuanyintang.com/image/5aab20996b4406c4fa40e1fab58530c2/3"},"comment_text":"1","counts_text":"7715","video_link":"http://api.yuanyintang.com/music/F6625902162A7F371D52C55C909FABF7.mp3?log_at=3","video_info":{"ext":"mp3","size":"9982627","playtime":"04:07","bitrate":"322","md5":"F6625902162A7F371D52C55C909FABF7","link":"http://api.yuanyintang.com/music/F6625902162A7F371D52C55C909FABF7.mp3?log_at=3"},"mv_info":{"ext":"m4a","size":146671773,"playtime":"04:08","bitrate":"4728","height":1080,"width":1920,"fps":25,"md5":"2f7a493679a95608e9d2087dba979a81","link":"https://api.yuanyintang.com/video/2f7a493679a95608e9d2087dba979a81.mp4?log_at=3"},"collection_text":"0"},{"id":43195,"title":"秦楼月","imgpic":"0ed6329a7399162346ebf656fe63e757","comment":0,"counts":34,"playtime":"","uid":37933,"nickname":"裂天","video":"921A8DA91AC6267FEF6EC467F90B003D","dayhits":1,"weekhits":1,"monthhits":1,"music_type":1,"mv":"","is_collection":0,"collection":0,"song_id":0,"imgpic_link":"https://api.yuanyintang.com/image/0ed6329a7399162346ebf656fe63e757/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"152848","is_long":"0","md5":"0ed6329a7399162346ebf656fe63e757","link":"https://api.yuanyintang.com/image/0ed6329a7399162346ebf656fe63e757/3"},"comment_text":"0","counts_text":"34","video_link":"http://api.yuanyintang.com/music/921A8DA91AC6267FEF6EC467F90B003D.mp3?log_at=3","video_info":{"ext":"mp3","size":"9906607","playtime":"04:07","bitrate":"320","md5":"921A8DA91AC6267FEF6EC467F90B003D","link":"http://api.yuanyintang.com/music/921A8DA91AC6267FEF6EC467F90B003D.mp3?log_at=3"},"mv_info":{},"collection_text":"0"}]
         * seo : {}
         */

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
        }

        public static class DataListBean {
            /**
             * id : 43257
             * title : 窃香（剧情版）
             * imgpic : 5aab20996b4406c4fa40e1fab58530c2
             * comment : 1
             * counts : 7715
             * playtime :
             * uid : 93244
             * nickname : 晃儿
             * video : F6625902162A7F371D52C55C909FABF7
             * dayhits : 184
             * weekhits : 326
             * monthhits : 423
             * music_type : 1
             * mv : 2f7a493679a95608e9d2087dba979a81
             * is_collection : 0
             * collection : 0
             * song_id : 0
             * imgpic_link : https://api.yuanyintang.com/image/5aab20996b4406c4fa40e1fab58530c2/3
             * imgpic_info : {"ext":"png","w":"300","h":"300","size":"218213","is_long":"0","md5":"5aab20996b4406c4fa40e1fab58530c2","link":"https://api.yuanyintang.com/image/5aab20996b4406c4fa40e1fab58530c2/3"}
             * comment_text : 1
             * counts_text : 7715
             * video_link : http://api.yuanyintang.com/music/F6625902162A7F371D52C55C909FABF7.mp3?log_at=3
             * video_info : {"ext":"mp3","size":"9982627","playtime":"04:07","bitrate":"322","md5":"F6625902162A7F371D52C55C909FABF7","link":"http://api.yuanyintang.com/music/F6625902162A7F371D52C55C909FABF7.mp3?log_at=3"}
             * mv_info : {"ext":"m4a","size":146671773,"playtime":"04:08","bitrate":"4728","height":1080,"width":1920,"fps":25,"md5":"2f7a493679a95608e9d2087dba979a81","link":"https://api.yuanyintang.com/video/2f7a493679a95608e9d2087dba979a81.mp4?log_at=3"}
             * collection_text : 0
             */

            private int id;
            private String title;
            private String imgpic;
            private int comment;
            private int counts;
            private String playtime;
            private int uid;
            private String nickname;
            private String video;
            private int dayhits;
            private int weekhits;
            private int monthhits;
            private int music_type;
            private String mv;
            private int is_collection;
            private int collection;
            private int song_id;
            private String imgpic_link;
            private ImgpicInfoBean imgpic_info;
            private String comment_text;
            private String counts_text;
            private String video_link;
            private VideoInfoBean video_info;
            private MvInfoBean mv_info;
            private String collection_text;


            private Boolean check;
            private boolean ischeck;
            private Boolean single_selection;
            private boolean isLocalMusic;
            boolean isPlaying;

            public boolean isPlaying() {
                return isPlaying;
            }

            public void setPlaying(boolean playing) {
                isPlaying = playing;
            }

            public Boolean getCheck() {
                return check;
            }

            public void setCheck(Boolean check) {
                this.check = check;
            }

            public boolean isIscheck() {
                return ischeck;
            }

            public void setIscheck(boolean ischeck) {
                this.ischeck = ischeck;
            }

            public Boolean getSingle_selection() {
                return single_selection;
            }

            public void setSingle_selection(Boolean single_selection) {
                this.single_selection = single_selection;
            }

            public boolean isLocalMusic() {
                return isLocalMusic;
            }

            public void setLocalMusic(boolean localMusic) {
                isLocalMusic = localMusic;
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

            public String getImgpic() {
                return imgpic;
            }

            public void setImgpic(String imgpic) {
                this.imgpic = imgpic;
            }

            public int getComment() {
                return comment;
            }

            public void setComment(int comment) {
                this.comment = comment;
            }

            public int getCounts() {
                return counts;
            }

            public void setCounts(int counts) {
                this.counts = counts;
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

            public int getDayhits() {
                return dayhits;
            }

            public void setDayhits(int dayhits) {
                this.dayhits = dayhits;
            }

            public int getWeekhits() {
                return weekhits;
            }

            public void setWeekhits(int weekhits) {
                this.weekhits = weekhits;
            }

            public int getMonthhits() {
                return monthhits;
            }

            public void setMonthhits(int monthhits) {
                this.monthhits = monthhits;
            }

            public int getMusic_type() {
                return music_type;
            }

            public void setMusic_type(int music_type) {
                this.music_type = music_type;
            }

            public String getMv() {
                return mv;
            }

            public void setMv(String mv) {
                this.mv = mv;
            }

            public int getIs_collection() {
                return is_collection;
            }

            public void setIs_collection(int is_collection) {
                this.is_collection = is_collection;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
            }

            public int getSong_id() {
                return song_id;
            }

            public void setSong_id(int song_id) {
                this.song_id = song_id;
            }

            public String getImgpic_link() {
                return imgpic_link;
            }

            public void setImgpic_link(String imgpic_link) {
                this.imgpic_link = imgpic_link;
            }

            public ImgpicInfoBean getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(ImgpicInfoBean imgpic_info) {
                this.imgpic_info = imgpic_info;
            }

            public String getComment_text() {
                return comment_text;
            }

            public void setComment_text(String comment_text) {
                this.comment_text = comment_text;
            }

            public String getCounts_text() {
                return counts_text;
            }

            public void setCounts_text(String counts_text) {
                this.counts_text = counts_text;
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

            public MvInfoBean getMv_info() {
                return mv_info;
            }

            public void setMv_info(MvInfoBean mv_info) {
                this.mv_info = mv_info;
            }

            public String getCollection_text() {
                return collection_text;
            }

            public void setCollection_text(String collection_text) {
                this.collection_text = collection_text;
            }

            public static class ImgpicInfoBean {
                /**
                 * ext : png
                 * w : 300
                 * h : 300
                 * size : 218213
                 * is_long : 0
                 * md5 : 5aab20996b4406c4fa40e1fab58530c2
                 * link : https://api.yuanyintang.com/image/5aab20996b4406c4fa40e1fab58530c2/3
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

            public static class VideoInfoBean {
                /**
                 * ext : mp3
                 * size : 9982627
                 * playtime : 04:07
                 * bitrate : 322
                 * md5 : F6625902162A7F371D52C55C909FABF7
                 * link : http://api.yuanyintang.com/music/F6625902162A7F371D52C55C909FABF7.mp3?log_at=3
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

            public static class MvInfoBean {
                /**
                 * ext : m4a
                 * size : 146671773
                 * playtime : 04:08
                 * bitrate : 4728
                 * height : 1080
                 * width : 1920
                 * fps : 25
                 * md5 : 2f7a493679a95608e9d2087dba979a81
                 * link : https://api.yuanyintang.com/video/2f7a493679a95608e9d2087dba979a81.mp4?log_at=3
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
}

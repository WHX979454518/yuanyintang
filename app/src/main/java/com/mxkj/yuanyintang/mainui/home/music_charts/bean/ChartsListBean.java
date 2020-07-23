package com.mxkj.yuanyintang.mainui.home.music_charts.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zuixia on 2018/4/19.
 */

public class ChartsListBean implements Serializable {
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
         * billboard_type : 2
         * date : 2018-04-20
         * class_info : {"id":4,"title":"古风","imgpic":"009d7c2c8205b6e66e2497776f69e690","type":2,"border_bg_color":"70bf40","icon":"c7adcb987e5224301258c6f7efb2d53e","music_class_id":6,"sub_title":"你喜欢的音乐上线了","imgpic_link":"http://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"174467","is_long":"0","md5":"009d7c2c8205b6e66e2497776f69e690","link":"http://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/3"},"icon_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3","icon_info":{"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}}
         * share_info : {"url":"class=4&type=1&billboard_type=2","title":"","content":""}
         * data_list : [{"title":"【糕药】山有木兮","head":"91eba73702be30bec3286913dd620c58a4542175","is_music":1,"video":"c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c","imgpic":"0e60393614c18b2003557d08622139c8","music_id":6187,"score_total":4,"nickname":"糕药","uid":41525,"score":0,"comment":4,"counts":48,"coin":25,"grow_status":1,"head_link":"http://api.demo.com//image/91eba73702be30bec3286913dd620c58a4542175/3","head_info":"","video_link":"http://api.demo.com//music/c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c.mp3?log_at=3","video_info":{"ext":"mp3","size":9805398,"playtime":"04:05","bitrate":"320","md5":"c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c","link":"http://api.demo.com//music/c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c.mp3?log_at=3"},"imgpic_link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/3","imgpic_info":{"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/3"},"comment_text":4,"counts_text":48}]
         */

        private int billboard_type;
        private String date;
        private ClassInfoBean class_info;
        private ShareInfoBean share_info;
        private List<DataListBean> data_list;
        /**
         * my_score : {"member_id":50539,"nickname":"源音塘518548","grow_status":0,"signature":"66","head":"3d3d27527715c56204c6e063e34883ae60384a97","sex":0,"is_music":0,"score":3,"total":2000,"is_self":0,"total_desc":"2000","head_link":"http://api.demo.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3"}
         */

        private MyScoreBean my_score;
        /**
         * toggle_info : {"id":13,"title":"收益榜单","imgpic":"bf619d9f6694960296d3135082ed7653d9f3af9c","type":4,"border_bg_color":"F00F00","icon":"c7adcb987e5224301258c6f7efb2d53e","music_class_id":0,"sub_title":"你喜欢的音乐上线了","share_title":"收益","share_content":"8888888888","share_icon":"c7adcb987e5224301258c6f7efb2d53e","imgpic_link":"http://api.demo.com//image/bf619d9f6694960296d3135082ed7653d9f3af9c/3","imgpic_info":"","icon_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3","icon_info":{"ext":"","w":"","h":"","size":"","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}}
         */

        private ToggleInfoBean toggle_info;


        public int getBillboard_type() {
            return billboard_type;
        }

        public void setBillboard_type(int billboard_type) {
            this.billboard_type = billboard_type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ClassInfoBean getClass_info() {
            return class_info;
        }

        public void setClass_info(ClassInfoBean class_info) {
            this.class_info = class_info;
        }

        public ShareInfoBean getShare_info() {
            return share_info;
        }

        public void setShare_info(ShareInfoBean share_info) {
            this.share_info = share_info;
        }

        public List<DataListBean> getData_list() {
            return data_list;
        }

        public void setData_list(List<DataListBean> data_list) {
            this.data_list = data_list;
        }

        public MyScoreBean getMy_score() {
            return my_score;
        }

        public void setMy_score(MyScoreBean my_score) {
            this.my_score = my_score;
        }

        public ToggleInfoBean getToggle_info() {
            return toggle_info;
        }

        public void setToggle_info(ToggleInfoBean toggle_info) {
            this.toggle_info = toggle_info;
        }

        public static class ClassInfoBean {
            /**
             * id : 4
             * title : 古风
             * imgpic : 009d7c2c8205b6e66e2497776f69e690
             * type : 2
             * border_bg_color : 70bf40
             * icon : c7adcb987e5224301258c6f7efb2d53e
             * music_class_id : 6
             * sub_title : 你喜欢的音乐上线了
             * imgpic_link : http://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/3
             * imgpic_info : {"ext":"png","w":"300","h":"300","size":"174467","is_long":"0","md5":"009d7c2c8205b6e66e2497776f69e690","link":"http://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/3"}
             * icon_link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
             * icon_info : {"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}
             */

            private int id;
            private String title;
            private String imgpic;
            private int type;
            private String border_bg_color;
            private String icon;
            private int music_class_id;
            private String sub_title;
            private String imgpic_link;
            private ImgpicInfoBean imgpic_info;
            private String icon_link;
            private IconInfoBean icon_info;

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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getBorder_bg_color() {
                return border_bg_color;
            }

            public void setBorder_bg_color(String border_bg_color) {
                this.border_bg_color = border_bg_color;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getMusic_class_id() {
                return music_class_id;
            }

            public void setMusic_class_id(int music_class_id) {
                this.music_class_id = music_class_id;
            }

            public String getSub_title() {
                return sub_title;
            }

            public void setSub_title(String sub_title) {
                this.sub_title = sub_title;
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

            public String getIcon_link() {
                return icon_link;
            }

            public void setIcon_link(String icon_link) {
                this.icon_link = icon_link;
            }

            public IconInfoBean getIcon_info() {
                return icon_info;
            }

            public void setIcon_info(IconInfoBean icon_info) {
                this.icon_info = icon_info;
            }

            public static class ImgpicInfoBean {
                /**
                 * ext : png
                 * w : 300
                 * h : 300
                 * size : 174467
                 * is_long : 0
                 * md5 : 009d7c2c8205b6e66e2497776f69e690
                 * link : http://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/3
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

            public static class IconInfoBean {
                /**
                 * ext : jpg
                 * w : 1080
                 * h : 1080
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
        }

        public static class ShareInfoBean {
            /**
             * url : class=4&type=1&billboard_type=2
             * title :
             * content :
             */

            private String url;
            private String title;
            private String content;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
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
        }

        public static class DataListBean implements MultiItemEntity {
            /**
             * title : 【糕药】山有木兮
             * head : 91eba73702be30bec3286913dd620c58a4542175
             * is_music : 1
             * video : c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c
             * imgpic : 0e60393614c18b2003557d08622139c8
             * music_id : 6187
             * score_total : 4
             * nickname : 糕药
             * uid : 41525
             * score : 0
             * comment : 4
             * counts : 48
             * coin : 25
             * grow_status : 1
             * head_link : http://api.demo.com//image/91eba73702be30bec3286913dd620c58a4542175/3
             * head_info :
             * video_link : http://api.demo.com//music/c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c.mp3?log_at=3
             * video_info : {"ext":"mp3","size":9805398,"playtime":"04:05","bitrate":"320","md5":"c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c","link":"http://api.demo.com//music/c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c.mp3?log_at=3"}
             * imgpic_link : http://api.demo.com//image/0e60393614c18b2003557d08622139c8/3
             * imgpic_info : {"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/3"}
             * comment_text : 4
             * counts_text : 48
             */

            int tag;

            public int getTag() {
                return tag;
            }

            public void setTag(int tag) {
                this.tag = tag;
            }

            private String title;
            private String head;
            private int is_music;
            private String video;
            private String imgpic;
            private String ttq_total_desc;
            private String score_total_desc;
            private int music_id;
            private String nickname;
            private int uid;
            private int score;
            private int ttq_total;
            private int reward_counts;
            private int comment;
            private int counts;
            private String counts_text;
            private int coin;
            private int grow_status;
            private String head_link;
            private String head_info;
            private String video_link;
            private VideoInfoBean video_info;
            private String imgpic_link;
            private ImgpicInfoBeanX imgpic_info;
            private String comment_text;

            private boolean isPlaying;
            public boolean isPlaying() {
                return isPlaying;
            }
            public void setPlaying(boolean playing) {
                isPlaying = playing;
            }


            public String getTtq_total_desc() {
                return ttq_total_desc;
            }

            @Override
            public String toString() {
                return "DataListBean{" +
                        "tag=" + tag +
                        ", title='" + title + '\'' +
                        ", head='" + head + '\'' +
                        ", is_music=" + is_music +
                        ", video='" + video + '\'' +
                        ", imgpic='" + imgpic + '\'' +
                        ", ttq_total_desc='" + ttq_total_desc + '\'' +
                        ", score_total_desc='" + score_total_desc + '\'' +
                        ", music_id=" + music_id +
                        ", nickname='" + nickname + '\'' +
                        ", uid=" + uid +
                        ", score=" + score +
                        ", ttq_total=" + ttq_total +
                        ", reward_counts=" + reward_counts +
                        ", comment=" + comment +
                        ", counts=" + counts +
                        ", counts_text='" + counts_text + '\'' +
                        ", coin=" + coin +
                        ", grow_status=" + grow_status +
                        ", head_link='" + head_link + '\'' +
                        ", head_info='" + head_info + '\'' +
                        ", video_link='" + video_link + '\'' +
                        ", video_info=" + video_info +
                        ", imgpic_link='" + imgpic_link + '\'' +
                        ", imgpic_info=" + imgpic_info +
                        ", comment_text='" + comment_text + '\'' +
                        ", isTopThree=" + isTopThree +
                        '}';
            }

            public void setTtq_total_desc(String ttq_total_desc) {
                this.ttq_total_desc = ttq_total_desc;
            }

            public String getScore_total_desc() {
                return score_total_desc;
            }

            public void setScore_total_desc(String score_total_desc) {
                this.score_total_desc = score_total_desc;
            }

            private boolean isTopThree;


            public void setTtq_total(int ttq_total) {
                this.ttq_total = ttq_total;
            }

            public int getReward_counts() {
                return reward_counts;
            }

            public void setReward_counts(int reward_counts) {
                this.reward_counts = reward_counts;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

            public int getMusic_id() {
                return music_id;
            }

            public void setMusic_id(int music_id) {
                this.music_id = music_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
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

            public int getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getGrow_status() {
                return grow_status;
            }

            public void setGrow_status(int grow_status) {
                this.grow_status = grow_status;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public String getHead_info() {
                return head_info;
            }

            public void setHead_info(String head_info) {
                this.head_info = head_info;
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

            public ImgpicInfoBeanX getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(ImgpicInfoBeanX imgpic_info) {
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
//
            public void setCounts_text(String counts_text) {
                this.counts_text = counts_text;
            }

            @Override
            public int getItemType() {
                if (isTopThree == true) {
                    return 2;
                } else {
                    return 1;
                }
            }

            public static class VideoInfoBean {
                /**
                 * ext : mp3
                 * size : 9805398
                 * playtime : 04:05
                 * bitrate : 320
                 * md5 : c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c
                 * link : http://api.demo.com//music/c0f2dbbc8553e7a8c5808c742babb5a05aaf6b0c.mp3?log_at=3
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

            public static class ImgpicInfoBeanX {
                /**
                 * ext : png
                 * w : 300
                 * h : 300
                 * size : 79407
                 * is_long : 0
                 * md5 : 0e60393614c18b2003557d08622139c8
                 * link : http://api.demo.com//image/0e60393614c18b2003557d08622139c8/3
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

        public static class MyScoreBean {
            /**
             * member_id : 50539
             * nickname : 源音塘518548
             * grow_status : 0
             * signature : 66
             * head : 3d3d27527715c56204c6e063e34883ae60384a97
             * sex : 0
             * is_music : 0
             * score : 3
             * total : 2000
             * is_self : 0
             * total_desc : 2000
             * head_link : http://api.demo.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3
             */

            private int member_id;
            private String nickname;
            private int grow_status;
            private String signature;
            private String head;
            private int sex;
            private int is_music;
            private int score;
            private int total;
            private int is_self;
            private String total_desc;
            private String head_link;

            public int getMember_id() {
                return member_id;
            }

            public void setMember_id(int member_id) {
                this.member_id = member_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getGrow_status() {
                return grow_status;
            }

            public void setGrow_status(int grow_status) {
                this.grow_status = grow_status;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
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

            public int getIs_music() {
                return is_music;
            }

            public void setIs_music(int is_music) {
                this.is_music = is_music;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getIs_self() {
                return is_self;
            }

            public void setIs_self(int is_self) {
                this.is_self = is_self;
            }

            public String getTotal_desc() {
                return total_desc;
            }

            public void setTotal_desc(String total_desc) {
                this.total_desc = total_desc;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }
        }

        public static class ToggleInfoBean {
            /**
             * id : 13
             * title : 收益榜单
             * imgpic : bf619d9f6694960296d3135082ed7653d9f3af9c
             * type : 4
             * border_bg_color : F00F00
             * icon : c7adcb987e5224301258c6f7efb2d53e
             * music_class_id : 0
             * sub_title : 你喜欢的音乐上线了
             * share_title : 收益
             * share_content : 8888888888
             * share_icon : c7adcb987e5224301258c6f7efb2d53e
             * imgpic_link : http://api.demo.com//image/bf619d9f6694960296d3135082ed7653d9f3af9c/3
             * imgpic_info :
             * icon_link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
             * icon_info : {"ext":"","w":"","h":"","size":"","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}
             */

            private int id;
            private String title;
            private String imgpic;
            private int type;
            private String border_bg_color;
            private String icon;
            private int music_class_id;
            private String sub_title;
            private String share_title;
            private String share_content;
            private String share_icon;
            private String imgpic_link;
            private String imgpic_info;
            private String icon_link;
            private ClassInfoBean.IconInfoBean icon_info;

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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getBorder_bg_color() {
                return border_bg_color;
            }

            public void setBorder_bg_color(String border_bg_color) {
                this.border_bg_color = border_bg_color;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getMusic_class_id() {
                return music_class_id;
            }

            public void setMusic_class_id(int music_class_id) {
                this.music_class_id = music_class_id;
            }

            public String getSub_title() {
                return sub_title;
            }

            public void setSub_title(String sub_title) {
                this.sub_title = sub_title;
            }

            public String getShare_title() {
                return share_title;
            }

            public void setShare_title(String share_title) {
                this.share_title = share_title;
            }

            public String getShare_content() {
                return share_content;
            }

            public void setShare_content(String share_content) {
                this.share_content = share_content;
            }

            public String getShare_icon() {
                return share_icon;
            }

            public void setShare_icon(String share_icon) {
                this.share_icon = share_icon;
            }

            public String getImgpic_link() {
                return imgpic_link;
            }

            public void setImgpic_link(String imgpic_link) {
                this.imgpic_link = imgpic_link;
            }

            public String getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(String imgpic_info) {
                this.imgpic_info = imgpic_info;
            }

            public String getIcon_link() {
                return icon_link;
            }

            public void setIcon_link(String icon_link) {
                this.icon_link = icon_link;
            }
        }
    }
}

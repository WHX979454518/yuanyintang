package com.mxkj.yuanyintang.mainui.myself.bean;

import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/30.
 */

public class NearbyPeopleBean {


    /**
     * code : 200
     * msg : success
     * data : [{"uid":50457,"nickname":"小义哥哥?","head":"597ed8f4535f73284182cbd0415c67f6706359a7","sex":0,"signature":"搞原唱","day":47,"is_music":0,"distance":"17.87m","relation":0,"to_relation":0,"music":{"music_id":6840,"music_title":"【原创】The Liar","video":"525da79bc59fb80c51932bc932c4f544e852035c","imgpic":"315ec85b4e7605082d13a6e1b6e38c31713bcac4","video_link":"http://api.yuanyintang.com//music/525da79bc59fb80c51932bc932c4f544e852035c","imgpic_link":"http://demoapi.yuanyintang.com/image/315ec85b4e7605082d13a6e1b6e38c31713bcac4"},"head_link":"http://demoapi.yuanyintang.com/image/597ed8f4535f73284182cbd0415c67f6706359a7"},{"uid":4,"nickname":"调皮的小萝莉","head":"204e6048e3e19e8d4cf1ad2cc168da2e86020ce6","sex":0,"signature":"ｔｒｙｙｃｆｌ","day":29,"is_music":1,"distance":"17.9m","relation":0,"to_relation":0,"music":{"music_id":4621,"music_title":"2014~唱于你听（片段）","video":"99c8101b88695f40add0021d0968264b17e2d84a","imgpic":"c6229bf7a99ac29b6e1b8e4a5c004596de38a127","video_link":"http://api.yuanyintang.com//music/99c8101b88695f40add0021d0968264b17e2d84a","imgpic_link":"http://demoapi.yuanyintang.com/image/c6229bf7a99ac29b6e1b8e4a5c004596de38a127"},"head_link":"http://demoapi.yuanyintang.com/image/204e6048e3e19e8d4cf1ad2cc168da2e86020ce6"},{"uid":11280,"nickname":"宝贝彩の美丽","head":"3e34aa8a45c02f015a160707bb750611","sex":1,"signature":"大多数人想要改造这个天下，但却罕见人想改造自己。","day":13,"is_music":0,"distance":"18.13m","relation":0,"to_relation":0,"music":{"music_id":6625,"music_title":"【大圣归来】绝尘赋","video":"0b4633b5a26842a643d79802728f4f4d6a38f38f","imgpic":"dd5ab7e6631a7433fd98abc02455752a4d465090","video_link":"http://api.yuanyintang.com//music/0b4633b5a26842a643d79802728f4f4d6a38f38f","imgpic_link":"http://demoapi.yuanyintang.com/image/dd5ab7e6631a7433fd98abc02455752a4d465090"},"head_link":"http://demoapi.yuanyintang.com/image/3e34aa8a45c02f015a160707bb750611"}]
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
         * uid : 50457
         * nickname : 小义哥哥?
         * head : 597ed8f4535f73284182cbd0415c67f6706359a7
         * sex : 0
         * signature : 搞原唱
         * day : 47
         * is_music : 0
         * distance : 17.87m
         * relation : 0
         * to_relation : 0
         * music : {"music_id":6840,"music_title":"【原创】The Liar","video":"525da79bc59fb80c51932bc932c4f544e852035c","imgpic":"315ec85b4e7605082d13a6e1b6e38c31713bcac4","video_link":"http://api.yuanyintang.com//music/525da79bc59fb80c51932bc932c4f544e852035c","imgpic_link":"http://demoapi.yuanyintang.com/image/315ec85b4e7605082d13a6e1b6e38c31713bcac4"}
         * head_link : http://demoapi.yuanyintang.com/image/597ed8f4535f73284182cbd0415c67f6706359a7
         */




        private int uid;
        private String nickname;
        private String head;
        private int sex;
        private String signature;
        private int day;
        private int is_music;
        private String distance;
        private int relation;
        private int to_relation;
        private MusicBean music;
        private String head_link;
        private List<String> tag;

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

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }

        public int getTo_relation() {
            return to_relation;
        }

        public void setTo_relation(int to_relation) {
            this.to_relation = to_relation;
        }

        public MusicBean getMusic() {
            return music;
        }

        public void setMusic(MusicBean music) {
            this.music = music;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public List<String> getTag() {
            return tag;
        }

        public void setTag(List<String> tag) {
            this.tag = tag;
        }

        public static class MusicBean {
            /**
             * music_id : 6840
             * music_title : 【原创】The Liar
             * video : 525da79bc59fb80c51932bc932c4f544e852035c
             * imgpic : 315ec85b4e7605082d13a6e1b6e38c31713bcac4
             * video_link : http://api.yuanyintang.com//music/525da79bc59fb80c51932bc932c4f544e852035c
             * imgpic_link : http://demoapi.yuanyintang.com/image/315ec85b4e7605082d13a6e1b6e38c31713bcac4
             */

            private int music_id;
            private String music_title;
            private String video;
            private String imgpic;
//            private String video_link;
//            private String imgpic_link;
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


            public int getMusic_id() {
                return music_id;
            }

            public void setMusic_id(int music_id) {
                this.music_id = music_id;
            }

            public String getMusic_title() {
                return music_title;
            }

            public void setMusic_title(String music_title) {
                this.music_title = music_title;
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

//            public String getVideo_link() {
//                return video_link;
//            }
//
//            public void setVideo_link(String video_link) {
//                this.video_link = video_link;
//            }

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

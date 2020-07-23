package com.mxkj.yuanyintang.mainui.search;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/10.
 */

public class MusicSearchResultBean implements Serializable{

    /**
     * code : 200
     * msg : success
     * data : {"count":{"musicCount":28,"musicianCount":2,"songCount":4,"topicCount":0},"data":[{"id":2130,"title":"【G.P】2017朴灿烈亮相五周年原创纪念曲<宿愿>","counts":46,"uid":23600,"nickname":"简木哥哥_","imgpic":"26cbd99defb354c892bafb1a14b8f1a96c5bf069","imgpic_link":"http://www.yyt_new.com/image/26cbd99defb354c892bafb1a14b8f1a96c5bf069"}]}
     */

    private int code;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX implements Serializable{
        /**
         * count : {"musicCount":28,"musicianCount":2,"songCount":4,"topicCount":0}
         * data : [{"id":2130,"title":"【G.P】2017朴灿烈亮相五周年原创纪念曲<宿愿>","counts":46,"uid":23600,"nickname":"简木哥哥_","imgpic":"26cbd99defb354c892bafb1a14b8f1a96c5bf069","imgpic_link":"http://www.yyt_new.com/image/26cbd99defb354c892bafb1a14b8f1a96c5bf069"}]
         */

        private CountBean count;
        private List<DataBean> data;

        public CountBean getCount() {
            return count;
        }

        public void setCount(CountBean count) {
            this.count = count;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class CountBean implements Serializable{
            /**
             * musicCount : 28
             * musicianCount : 2
             * songCount : 4
             * topicCount : 0
             */

            private int musicCount;
            private int musicianCount;
            private int songCount;
            private int topicCount;

            public int getMusicCount() {
                return musicCount;
            }

            public void setMusicCount(int musicCount) {
                this.musicCount = musicCount;
            }

            public int getMusicianCount() {
                return musicianCount;
            }

            public void setMusicianCount(int musicianCount) {
                this.musicianCount = musicianCount;
            }

            public int getSongCount() {
                return songCount;
            }

            public void setSongCount(int songCount) {
                this.songCount = songCount;
            }

            public int getTopicCount() {
                return topicCount;
            }

            public void setTopicCount(int topicCount) {
                this.topicCount = topicCount;
            }
        }

        public static class DataBean implements Serializable{
            /**
             * id : 2130
             * title : 【G.P】2017朴灿烈亮相五周年原创纪念曲<宿愿>
             * counts : 46
             * uid : 23600
             * nickname : 简木哥哥_
             * imgpic : 26cbd99defb354c892bafb1a14b8f1a96c5bf069
             * imgpic_link : http://www.yyt_new.com/image/26cbd99defb354c892bafb1a14b8f1a96c5bf069
             */

            private int id;
            private String title;
            private int counts;
            private int uid;
            private String nickname;
            private String imgpic;
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

            public String getImgpic() {
                return imgpic;
            }

            public void setImgpic(String imgpic) {
                this.imgpic = imgpic;
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

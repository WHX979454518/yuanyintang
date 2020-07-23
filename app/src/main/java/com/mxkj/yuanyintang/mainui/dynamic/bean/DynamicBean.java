package com.mxkj.yuanyintang.mainui.dynamic.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.mainui.home.data.Constant;

import java.io.Serializable;
import java.util.List;

public class DynamicBean implements Serializable {
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

    public static class DataBean implements MultiItemEntity {

        public static class NewMsgBean {
            private String userIcon;
            private int msgNum;

            public String getUserIcon() {
                return userIcon;
            }

            public void setUserIcon(String userIcon) {
                this.userIcon = userIcon;
            }

            public int getMsgNum() {
                return msgNum;
            }

            public void setMsgNum(int msgNum) {
                this.msgNum = msgNum;
            }
        }

        private int id;
        private String depict;
        private int comment;
        private int agrees;
        private String create_time;
        private int uid;
        private String nickname;
        private int sex;
        private String head;
        private int member_type;
        private int is_relation;
        private int hits;
        private int push;
        private boolean isPlaying;
        private NewMsgBean newMsgBean;

        public NewMsgBean getNewMsgBean() {
            return newMsgBean;
        }

        public void setNewMsgBean(NewMsgBean newMsgBean) {
            this.newMsgBean = newMsgBean;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public int getPush() {
            return push;
        }

        public void setPush(int push) {
            this.push = push;
        }

        public int getRoute() {
            return push;
        }

        public void setRoute(int route) {
            this.push = route;
        }

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        private int is_music;
        private MusicBean music;
        private TopicBean topic;
        private SongBean song;
        private TopicReplyBean topic_reply;
        private int is_agree;
        private String head_link;
//        private List<String> imglist_link;

        private List<ImglistInfoBean> imglist_info;

        public static class ImglistInfoBean implements Serializable {
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

        public SongBean getSong() {
            return song;
        }

        public void setSong(SongBean song) {
            this.song = song;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDepict() {
            return depict;
        }

        public void setDepict(String depict) {
            this.depict = depict;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public int getAgrees() {
            return agrees;
        }

        public void setAgrees(int agrees) {
            this.agrees = agrees;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

        public int getIs_relation() {
            return is_relation;
        }

        public void setIs_relation(int is_relation) {
            this.is_relation = is_relation;
        }

        public MusicBean getMusic() {
            return music;
        }

        public void setMusic(MusicBean music) {
            this.music = music;
        }

        public TopicBean getTopic() {
            return topic;
        }

        public void setTopic(TopicBean topic) {
            this.topic = topic;
        }

        public TopicReplyBean getTopic_reply() {
            return topic_reply;
        }

        public void setTopic_reply(TopicReplyBean topic_reply) {
            this.topic_reply = topic_reply;
        }

        public int getIs_agree() {
            return is_agree;
        }

        public void setIs_agree(int is_agree) {
            this.is_agree = is_agree;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

//        public List<String> getImglist_link() {
//            return imglist_link;
//        }
//
//        public void setImglist_link(List<String> imglist_link) {
//            this.imglist_link = imglist_link;
//        }

        @Override
        public int getItemType() {
            if (newMsgBean != null) {
                return Constant.DynamicItemType.TYPE_NEW_MSG;
            } else if (music != null && music.getId() != 0 && imglist_info != null && imglist_info.size() > 0) {//有音乐
                return Constant.DynamicItemType.TYPE_MUSIC_IMG;//音乐图片
            } else if (music.getId() != 0 && imglist_info == null) {
                return Constant.DynamicItemType.TYPE_TEXT_MUSIC;//文本音乐
            } else if (music.getId() != 0 && imglist_info != null && imglist_info.size() == 0) {
                return Constant.DynamicItemType.TYPE_TEXT_MUSIC;//文本音乐
            } else if (music.getId() == 0 && topic.getId() == 0 && topic_reply.getId() == 0 && music.getVideo_info() == null && imglist_info != null && imglist_info.size() > 0) {
                return Constant.DynamicItemType.TYPE_TEXT_IMG;//文本  图片
            } else if (topic.getId() != 0 && imglist_info != null && imglist_info.size() > 0) {
                return Constant.DynamicItemType.TYPE_TOPIC_IMG;//话题 图片
            } else if (topic.getId() != 0 && imglist_info == null) {
                return Constant.DynamicItemType.TYPE_TOPIC_NOIMG;//话题不带图片
            } else if (topic.getId() != 0 && imglist_info != null && imglist_info.size() == 0) {
                return Constant.DynamicItemType.TYPE_TOPIC_NOIMG;//话题不带图片
            } else if (topic_reply.getContent() != null && imglist_info == null) {
                return Constant.DynamicItemType.TYPE_TOPIC_NOIMG;
            } else if (topic_reply.getContent() != null && imglist_info != null && imglist_info.size() > 0) {
                return Constant.DynamicItemType.TYPE_TOPIC_IMG;
            } else if (song != null && song.getTitle() != null && imglist_info == null) {
                return Constant.DynamicItemType.TYPE_TEXT_ALBUM;//歌单不带图
            } else if (song != null && song.getTitle() != null && imglist_info != null && imglist_info.size() == 0) {
                return Constant.DynamicItemType.TYPE_TEXT_ALBUM;//歌单不带图
            } else if (song != null && song.getTitle() != null && imglist_info != null && imglist_info.size() > 0) {
                return Constant.DynamicItemType.TYPE_IMG_ALBUM;//歌单带图
            } else {
                return Constant.DynamicItemType.TYPE_TEXT;//只有文字
            }
        }

    }


    public static class SongBean implements Serializable {
        /**
         * id	int	歌单id
         * title	int	歌单名称
         * imgpic_link	int	歌单图片
         * counts	int	歌单播放次数
         * total	int	歌单音乐总数
         * uid	int	上传者id
         * nickname
         */

        int id;
        String title;
//        String imgpic_link;


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


        int counts;
        int total;
        int uid;
        String nickname;

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

//        public String getImgpic_link() {
//            return imgpic_link;
//        }
//
//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }

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
    }


    public static class MusicBean implements Serializable {
        /**
         * id : 2120
         * title : 我的歌声里
         * imgpic : 2f221a1477bbfc4e2842960c557f15f05870a4ba
         * uid : 22770
         * nickname : Iose
         * video_link : http://api.yuanyintang.com/music/b8157c88f48f6229c6f65a7c161ebaa75a4df9f0
         * imgpic_link : http://yyt.demo.com/image/2f221a1477bbfc4e2842960c557f15f05870a4ba
         */

        private int id;
        private String title;
        private String imgpic;
        private int uid;
        private String nickname;
        private boolean isPlaying = false;

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        //        private String video_link;
//        private String imgpic_link;
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


        public int getSong_id() {
            return song_id;
        }

        public void setSong_id(int song_id) {
            this.song_id = song_id;
        }

        private int song_id;

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
    }

    public static class TopicBean implements Serializable {
        /**
         * id : 3
         * title : 222221
         * content : 111
         * imgpic : 111
         * imgpic_link : http://yyt.demo.com/image/111
         */

        private int id;
        private String title;
        private String content;
        private String imgpic;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }
//
//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }
    }

    public static class TopicReplyBean implements Serializable {
        /**
         * id : 3
         * content : 111
         * imgpic : 111
         * imgpic_link : http://yyt.demo.com/image/111
         */

        private int id;

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        private int pid;
        private String content;
        private String imgpic;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }
//
//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }
    }
}

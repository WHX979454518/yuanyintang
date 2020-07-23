package com.mxkj.yuanyintang.mainui.pond.bean;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.myself.settings.SettingActivity;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.io.Serializable;
import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.data.Constant.PondAllCommentItemType.IMG;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.PondAllCommentItemType.MUSIC;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.PondAllCommentItemType.MUSIC_IMG;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.PondAllCommentItemType.TEXT;

/**
 * Created by LiuJie on 2017/10/11.
 */

public class PondCommentBean implements Serializable {


    /**
     * code : 200
     * msg : success
     * data : [{"id":1063,"content":"梵蒂冈的发","agrees":0,"music_id":0,"imglist":[],"nickname":"食发鬼设法","create_time":"10-20","thcount":0,"uid":31144,"head":"5f05a96e34555e1a6de3d4bdbd2e1adf","sex":0,"is_music":1,"member_type":2,"is_agree":0,"com_count":3,"com_lists":[{"id":475,"content":"xvSDVdvS","agrees":0,"create_time":"09-01","uid":343,"nickname":"loveyouL","sex":1,"head":"f06141013e338279e2a7464b0296f36c9b54a150","member_type":1,"is_music":0,"to_uid":0,"to_nickname":"","to_sex":"","fid":1063,"is_agree":0,"head_link":"http://www.1.com/image/f06141013e338279e2a7464b0296f36c9b54a150"},{"id":474,"content":"11111","agrees":0,"create_time":"09-01","uid":31083,"nickname":"你好呵呵呵呵呵呵哈哈哈哈呵呵呵","sex":0,"head":"bf3ca10cffe33cc72d5c7a7ebbb4f3c7","member_type":1,"is_music":0,"to_uid":0,"to_nickname":"","to_sex":"","fid":1063,"is_agree":0,"head_link":"http://www.1.com/image/bf3ca10cffe33cc72d5c7a7ebbb4f3c7"},{"id":473,"content":"11111","agrees":0,"create_time":"09-01","uid":31083,"nickname":"你好呵呵呵呵呵呵哈哈哈哈呵呵呵","sex":0,"head":"bf3ca10cffe33cc72d5c7a7ebbb4f3c7","member_type":1,"is_music":0,"to_uid":0,"to_nickname":"","to_sex":"","fid":1063,"is_agree":0,"head_link":"http://www.1.com/image/bf3ca10cffe33cc72d5c7a7ebbb4f3c7"}],"head_link":"http://www.1.com/image/5f05a96e34555e1a6de3d4bdbd2e1adf"}]
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

    public static class DataBean implements MultiItemEntity, Serializable {
        /**
         * id : 1063
         * content : 梵蒂冈的发
         * agrees : 0
         * music_id : 0
         * imglist : []
         * nickname : 食发鬼设法
         * create_time : 10-20
         * thcount : 0
         * uid : 31144
         * head : 5f05a96e34555e1a6de3d4bdbd2e1adf
         * sex : 0
         * is_music : 1
         * member_type : 2
         * is_agree : 0
         * com_count : 3
         * com_lists : [{"id":475,"content":"xvSDVdvS","agrees":0,"create_time":"09-01","uid":343,"nickname":"loveyouL","sex":1,"head":"f06141013e338279e2a7464b0296f36c9b54a150","member_type":1,"is_music":0,"to_uid":0,"to_nickname":"","to_sex":"","fid":1063,"is_agree":0,"head_link":"http://www.1.com/image/f06141013e338279e2a7464b0296f36c9b54a150"},{"id":474,"content":"11111","agrees":0,"create_time":"09-01","uid":31083,"nickname":"你好呵呵呵呵呵呵哈哈哈哈呵呵呵","sex":0,"head":"bf3ca10cffe33cc72d5c7a7ebbb4f3c7","member_type":1,"is_music":0,"to_uid":0,"to_nickname":"","to_sex":"","fid":1063,"is_agree":0,"head_link":"http://www.1.com/image/bf3ca10cffe33cc72d5c7a7ebbb4f3c7"},{"id":473,"content":"11111","agrees":0,"create_time":"09-01","uid":31083,"nickname":"你好呵呵呵呵呵呵哈哈哈哈呵呵呵","sex":0,"head":"bf3ca10cffe33cc72d5c7a7ebbb4f3c7","member_type":1,"is_music":0,"to_uid":0,"to_nickname":"","to_sex":"","fid":1063,"is_agree":0,"head_link":"http://www.1.com/image/bf3ca10cffe33cc72d5c7a7ebbb4f3c7"}]
         * head_link : http://www.1.com/image/5f05a96e34555e1a6de3d4bdbd2e1adf
         */

        private int id;
        private int pid;
        private String content;
        private String floor;

        public String getFloor() {
            return "| # "+floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        private int agrees;
        private int music_id;
        //        private List<String> imglist_info;
        private String nickname;
        private String create_time;
        private int thcount;
        private int uid;
        private String head;
        private int sex;
        private int is_music;
        private String music_uid;
        private String music_title;
        private String music_nickname;
        private int member_type;
        private int is_agree;
        private int com_count;
        private int hits;
        private String head_link;

        //        private String imgpic_link;
        private List<String> imglist;
        private List<ComListsBean> com_lists;
        private List<ImglistInfoBean> imglist_info;

        private boolean isPlaying;

        private int is_self;

        public int getIs_self() {
            return is_self;
        }

        public void setIs_self(int is_self) {
            this.is_self = is_self;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }


        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
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

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public String getMusic_uid() {
            return music_uid;
        }

        public void setMusic_uid(String music_uid) {
            this.music_uid = music_uid;
        }

        public String getMusic_title() {
            return music_title;
        }

        public void setMusic_title(String music_title) {
            this.music_title = music_title;
        }

        public String getMusic_nickname() {
            return music_nickname;
        }

        public void setMusic_nickname(String music_nickname) {
            this.music_nickname = music_nickname;
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

        public int getAgrees() {
            return agrees;
        }

        public void setAgrees(int agrees) {
            this.agrees = agrees;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public int getMember_type() {
            return member_type;
        }

        public void setMember_type(int member_type) {
            this.member_type = member_type;
        }

        public int getIs_agree() {
            return is_agree;
        }

        public void setIs_agree(int is_agree) {
            this.is_agree = is_agree;
        }

        public int getCom_count() {
            return com_count;
        }

        public void setCom_count(int com_count) {
            this.com_count = com_count;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public List<String> getImglist() {
            return imglist;
        }

        public void setImglist(List<String> imglist) {
            this.imglist = imglist;
        }

        public List<ComListsBean> getCom_lists() {
            return com_lists;
        }

        public void setCom_lists(List<ComListsBean> com_lists) {
            this.com_lists = com_lists;
        }

        @Override
        public int getItemType() {

            if (music_id != 0 && imglist_info != null && imglist_info.size() > 0) {//有音乐和图片
                return MUSIC_IMG;
            }else if (music_id != 0 && imglist_info == null) {//只有音乐
                return MUSIC;
            } else if (music_id != 0 && imglist_info != null && imglist_info.size() == 0) {//只有音乐
                return MUSIC;
            } else if (music_id == 0 && imglist_info != null && imglist_info.size() > 0) {//只有图片
                return IMG;
            } else if (music_id == 0 && imglist_info == null) {//只有文字
                return TEXT;
            } else if (music_id == 0 && imglist_info != null && imglist_info.size() == 0) {//只有文字
                return TEXT;
            }
            return -1;
        }

        public static class ComListsBean implements Serializable {
            private int id;
            private String content;
            private int agrees;
            private String create_time;
            private int uid;
            private String nickname;
            private int sex;
            private String head;
            private int member_type;
            private int is_music;
            private int to_uid;
            private String to_nickname;
            private int to_sex;
            private int fid;
            private int pid;
            private int is_agree;
            private String head_link;

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
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

            public int getIs_music() {
                return is_music;
            }

            public void setIs_music(int is_music) {
                this.is_music = is_music;
            }

            public int getTo_uid() {
                return to_uid;
            }

            public void setTo_uid(int to_uid) {
                this.to_uid = to_uid;
            }

            public String getTo_nickname() {
                return to_nickname;
            }

            public void setTo_nickname(String to_nickname) {
                this.to_nickname = to_nickname;
            }

            public int getTo_sex() {
                return to_sex;
            }

            public void setTo_sex(int to_sex) {
                this.to_sex = to_sex;
            }

            public int getFid() {
                return fid;
            }

            public void setFid(int fid) {
                this.fid = fid;
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
        }
    }
}

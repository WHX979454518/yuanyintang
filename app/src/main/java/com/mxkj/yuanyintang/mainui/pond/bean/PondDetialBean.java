package com.mxkj.yuanyintang.mainui.pond.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/11.
 */

public class PondDetialBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"id":990,"title":"东风浩荡条回复的回复","content":"的防守打法多付多多付多付多付多多多多多多多多多多多多多多多多多多多多","agrees":0,"hashtag":"3","music_id":0,"imglist":"8ce384db6734e72a7f982261a3feab57,5fe10ea9035c17153954243754d93d22,84b34d068b5ed55e94378425fc2082b8,8706b84c9f6ac73dd7f06533ccc261ca,6d794e8300314cd4e937709bb6c36a8f,8971dd24996a850e5d0de4356d86a718,a22a182bcbf05f8ffb37a6338a40d220,058c23404d23004078caf0f192acde1a,949ebc2164f95a3ff7097094506c567a","create_time":"昨天","thcount":0,"hits":4,"pid":0,"recommend":[],"uid":31144,"nickname":"食发鬼设法","head":"5f05a96e34555e1a6de3d4bdbd2e1adf","sex":0,"signature":"666","fans_num":303,"ips_num":1050,"is_music":3,"choice":1,"is_agree":0,"tag":[{"id":3,"title":"小花"}],"share_topic_detail":"http://app.yuanyintang.com/topic/discuss-detail?id=990","relation":0,"question_name":"1","hide":1,"votetype":2,"sumvotenum":0,"vote":[{"id":941,"optionname":"1","votenum":0},{"id":942,"optionname":"1","votenum":0},{"id":943,"optionname":"1","votenum":0}],"is_vote":0,"is_vote_id":"","imglist_link":["http://yyt.demo.com/image/8ce384db6734e72a7f982261a3feab57","http://yyt.demo.com/image/5fe10ea9035c17153954243754d93d22","http://yyt.demo.com/image/84b34d068b5ed55e94378425fc2082b8","http://yyt.demo.com/image/8706b84c9f6ac73dd7f06533ccc261ca","http://yyt.demo.com/image/6d794e8300314cd4e937709bb6c36a8f","http://yyt.demo.com/image/8971dd24996a850e5d0de4356d86a718","http://yyt.demo.com/image/a22a182bcbf05f8ffb37a6338a40d220","http://yyt.demo.com/image/058c23404d23004078caf0f192acde1a","http://yyt.demo.com/image/949ebc2164f95a3ff7097094506c567a"],"head_link":"http://yyt.demo.com/image/5f05a96e34555e1a6de3d4bdbd2e1adf"}
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

    public static class DataBean implements MultiItemEntity, Serializable {
        /**
         * id : 990
         * title : 东风浩荡条回复的回复
         * content : 的防守打法多付多多付多付多付多多多多多多多多多多多多多多多多多多多多
         * agrees : 0
         * hashtag : 3
         * music_id : 0
         * imglist : 8ce384db6734e72a7f982261a3feab57,5fe10ea9035c17153954243754d93d22,84b34d068b5ed55e94378425fc2082b8,8706b84c9f6ac73dd7f06533ccc261ca,6d794e8300314cd4e937709bb6c36a8f,8971dd24996a850e5d0de4356d86a718,a22a182bcbf05f8ffb37a6338a40d220,058c23404d23004078caf0f192acde1a,949ebc2164f95a3ff7097094506c567a
         * create_time : 昨天
         * thcount : 0
         * hits : 4
         * pid : 0
         * recommend : []
         * uid : 31144
         * nickname : 食发鬼设法
         * head : 5f05a96e34555e1a6de3d4bdbd2e1adf
         * sex : 0
         * signature : 666
         * fans_num : 303
         * ips_num : 1050
         * is_music : 3
         * choice : 1
         * is_agree : 0
         * tag : [{"id":3,"title":"小花"}]
         * share_topic_detail : http://app.yuanyintang.com/topic/discuss-detail?id=990
         * relation : 0
         * question_name : 1
         * hide : 1
         * votetype : 2
         * sumvotenum : 0
         * vote : [{"id":941,"optionname":"1","votenum":0},{"id":942,"optionname":"1","votenum":0},{"id":943,"optionname":"1","votenum":0}]
         * is_vote : 0
         * is_vote_id :
         * imglist_link : ["http://yyt.demo.com/image/8ce384db6734e72a7f982261a3feab57","http://yyt.demo.com/image/5fe10ea9035c17153954243754d93d22","http://yyt.demo.com/image/84b34d068b5ed55e94378425fc2082b8","http://yyt.demo.com/image/8706b84c9f6ac73dd7f06533ccc261ca","http://yyt.demo.com/image/6d794e8300314cd4e937709bb6c36a8f","http://yyt.demo.com/image/8971dd24996a850e5d0de4356d86a718","http://yyt.demo.com/image/a22a182bcbf05f8ffb37a6338a40d220","http://yyt.demo.com/image/058c23404d23004078caf0f192acde1a","http://yyt.demo.com/image/949ebc2164f95a3ff7097094506c567a"]
         * head_link : http://yyt.demo.com/image/5f05a96e34555e1a6de3d4bdbd2e1adf
         */

        private int id;
        private int item_type;
        private String title;
        private String place_desc;
        private String distance;
        private String content;
        private int agrees;
        private String hashtag;
        private int music_id;
        private int music_uid;
        private String music_title;
        private String music_nickname;
        private String create_time;
        private int thcount;
        private int hits;
        private int pid;
        private int uid;
        private String nickname;
        private String head;
        private int sex;
        private int is_recommend;
        private String signature;
        private int fans_num;
        private int ips_num;
        private int is_music;
        private int choice;
        private int is_agree;
        private String share_topic_detail;
        private int relation;
        private String question_name;
        private int hide;
        private int votetype;
        private int sumvotenum;
        private int is_vote;
        private String is_vote_id;
        private String head_link;
        //        private String imgpic_link;
        private List<RecommendBean> recommend;
        private List<TagBean> tag;
        private List<VoteBean> vote;
        List<PondCommentBean.DataBean> hot_ommenList;
        List<PondCommentBean.DataBean> all_ommenList;
        private List<ImglistInfoBean> imglist_info;

        public int getItem_type() {
            return item_type;
        }

        public void setItem_type(int item_type) {
            this.item_type = item_type;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getPlace_desc() {
            return place_desc;
        }

        public void setPlace_desc(String place_desc) {
            this.place_desc = place_desc;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        private ImgpicInfoBean imgpic_info;
        /**
         * item_info : {"id":1608,"title":"未闻花名ED（口琴&吉他伴奏版）","video":"db042237021d2461044100cddae4072204d1b2ed","imgpic":"3258ca07f94863aebcdb0d52a55d5ae0fb095aee","uid":22770,"nickname":"桜君","video_link":"http://testapi.imxkj.com//music/db042237021d2461044100cddae4072204d1b2ed.mp3?log_at=3","video_info":{"ext":"mp3","size":"8800409","playtime":"06:06","bitrate":"192","md5":"db042237021d2461044100cddae4072204d1b2ed","link":"http://testapi.imxkj.com//music/db042237021d2461044100cddae4072204d1b2ed.mp3?log_at=3"},"imgpic_link":"http://testapi.imxkj.com//image/3258ca07f94863aebcdb0d52a55d5ae0fb095aee/3","imgpic_info":{"ext":"jpg","w":"960","h":"640","size":"59291","is_long":"0","md5":"3258ca07f94863aebcdb0d52a55d5ae0fb095aee","link":"http://testapi.imxkj.com//image/3258ca07f94863aebcdb0d52a55d5ae0fb095aee/3"}}
         */

        private ItemInfoBean item_info;

        public ImgpicInfoBean getImgpic_info() {
            return imgpic_info;
        }

        public void setImgpic_info(ImgpicInfoBean imgpic_info) {
            this.imgpic_info = imgpic_info;
        }

        public ItemInfoBean getItem_info() {
            return item_info;
        }

        public void setItem_info(ItemInfoBean item_info) {
            this.item_info = item_info;
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

        public static class MvInfoBean {
            /**
             * ext : m4a
             * size : 173539259
             * playtime : 04:06
             * bitrate : 5642
             * height : 720
             * width : 1280
             * fps : 30
             * md5 : e7d53798a20fee90e6c75901ad537227
             * link : https://api.yuanyintang.com/video/e7d53798a20fee90e6c75901ad537227.mp4?log_at=3
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

            @Override
            public String toString() {
                return "MvInfoBean{" +
                        "ext='" + ext + '\'' +
                        ", size=" + size +
                        ", playtime='" + playtime + '\'' +
                        ", bitrate='" + bitrate + '\'' +
                        ", height=" + height +
                        ", width=" + width +
                        ", fps=" + fps +
                        ", md5='" + md5 + '\'' +
                        ", link='" + link + '\'' +
                        '}';
            }

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

        public List<ImglistInfoBean> getImglist_info() {
            return imglist_info;
        }

        public void setImglist_info(List<ImglistInfoBean> imglist_info) {
            this.imglist_info = imglist_info;
        }
//        public String getImgpic_link() {
//            return imgpic_link;
//        }

//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }

        public List<PondCommentBean.DataBean> getHot_ommenList() {
            return hot_ommenList;
        }

        public void setHot_ommenList(List<PondCommentBean.DataBean> hot_ommenList) {
            this.hot_ommenList = hot_ommenList;
        }

        public List<PondCommentBean.DataBean> getAll_ommenList() {
            return all_ommenList;
        }

        public void setAll_ommenList(List<PondCommentBean.DataBean> all_ommenList) {
            this.all_ommenList = all_ommenList;
        }

        @Override
        public int getItemType() {
            if (all_ommenList != null && all_ommenList.size() > 10 && hot_ommenList != null && hot_ommenList.size() > 0) {
                return 1;
            } else if (all_ommenList != null && all_ommenList.size() > 0) {
                return 2;
            } else if (nickname != null) {
                return 3;
            }
            return 0;
        }

        public static class RecommendBean implements MultiItemEntity, Serializable {
            /**
             * id : 236
             * title : 源音塘啦啦啦啦22252
             * imglist :
             * thcount : 0
             */

            private int id;
            private String title;
            private String imglist;

//            public List<String> getImglist_link() {
//                return imglist_link;
//            }
//
//            public void setImglist_link(List<String> imglist_link) {
//                this.imglist_link = imglist_link;
//            }

            //            private List<String> imglist_link;
            private int thcount;
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

            public String getImglist() {
                return imglist;
            }

            public void setImglist(String imglist) {
                this.imglist = imglist;
            }

            public int getThcount() {
                return thcount;
            }

            public void setThcount(int thcount) {
                this.thcount = thcount;
            }

            @Override
            public int getItemType() {
                return 0;
            }
        }

        public int getMusic_uid() {
            return music_uid;
        }

        public void setMusic_uid(int music_uid) {
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

        public int getChoice() {
            return choice;
        }

        public void setChoice(int choice) {
            this.choice = choice;
        }

        public int getIs_agree() {
            return is_agree;
        }

        public void setIs_agree(int is_agree) {
            this.is_agree = is_agree;
        }

        public String getShare_topic_detail() {
            return share_topic_detail;
        }

        public void setShare_topic_detail(String share_topic_detail) {
            this.share_topic_detail = share_topic_detail;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }

        public String getQuestion_name() {
            return question_name;
        }

        public void setQuestion_name(String question_name) {
            this.question_name = question_name;
        }

        public int getHide() {
            return hide;
        }

        public void setHide(int hide) {
            this.hide = hide;
        }

        public int getVotetype() {
            return votetype;
        }

        public void setVotetype(int votetype) {
            this.votetype = votetype;
        }

        public int getSumvotenum() {
            return sumvotenum;
        }

        public void setSumvotenum(int sumvotenum) {
            this.sumvotenum = sumvotenum;
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

        public List<RecommendBean> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<RecommendBean> recommend) {
            this.recommend = recommend;
        }

        public List<TagBean> getTag() {
            return tag;
        }

        public void setTag(List<TagBean> tag) {
            this.tag = tag;
        }

        public List<VoteBean> getVote() {
            return vote;
        }

        public void setVote(List<VoteBean> vote) {
            this.vote = vote;
        }

//        public List<String> getImglist_link() {
//            return imglist_link;
//        }
//
//        public void setImglist_link(List<String> imglist_link) {
//            this.imglist_link = imglist_link;
//        }

        public static class TagBean implements Serializable {
            /**
             * id : 3
             * title : 小花
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

        public static class VoteBean implements Serializable {
            /**
             * id : 941
             * optionname : 1
             * votenum : 0
             */

            private boolean isChecked = false;
            private int id;
            private String optionname;
            private int votenum;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOptionname() {
                return optionname;
            }

            public void setOptionname(String optionname) {
                this.optionname = optionname;
            }

            public int getVotenum() {
                return votenum;
            }

            public void setVotenum(int votenum) {
                this.votenum = votenum;
            }
        }

        public static class ItemInfoBean {
            /**
             * id : 1608
             * title : 未闻花名ED（口琴&吉他伴奏版）
             * video : db042237021d2461044100cddae4072204d1b2ed
             * imgpic : 3258ca07f94863aebcdb0d52a55d5ae0fb095aee
             * uid : 22770
             * nickname : 桜君
             * video_link : http://testapi.imxkj.com//music/db042237021d2461044100cddae4072204d1b2ed.mp3?log_at=3
             * video_info : {"ext":"mp3","size":"8800409","playtime":"06:06","bitrate":"192","md5":"db042237021d2461044100cddae4072204d1b2ed","link":"http://testapi.imxkj.com//music/db042237021d2461044100cddae4072204d1b2ed.mp3?log_at=3"}
             * imgpic_link : http://testapi.imxkj.com//image/3258ca07f94863aebcdb0d52a55d5ae0fb095aee/3
             * imgpic_info : {"ext":"jpg","w":"960","h":"640","size":"59291","is_long":"0","md5":"3258ca07f94863aebcdb0d52a55d5ae0fb095aee","link":"http://testapi.imxkj.com//image/3258ca07f94863aebcdb0d52a55d5ae0fb095aee/3"}
             */

            @SerializedName("id")
            private int id;
            private int item_type;
            @SerializedName("title")
            private String title;
            private String video;
            private String imgpic;
            @SerializedName("uid")
            private int uid;
            @SerializedName("nickname")
            private String nickname;
            private String video_link;
            private VideoInfoBean video_info;
            private String imgpic_link;
            @SerializedName("imgpic_info")
            private ImglistInfoBean imgpic_info;

            private boolean isPlaying;

            private String sub_title;
            private String url;

            private int status;
            private int is_private;

            private MvInfoBean mv_info;

            public MvInfoBean getMv_info() {
                return mv_info;
            }

            public void setMv_info(MvInfoBean mv_info) {
                this.mv_info = mv_info;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getIs_private() {
                return is_private;
            }

            public void setIs_private(int is_private) {
                this.is_private = is_private;
            }

            public String getSub_title() {
                return sub_title;
            }

            public void setSub_title(String sub_title) {
                this.sub_title = sub_title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public boolean isPlaying() {
                return isPlaying;
            }

            public void setPlaying(boolean playing) {
                isPlaying = playing;
            }

            public int getItem_type() {
                return item_type;
            }

            public void setItem_type(int item_type) {
                this.item_type = item_type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public ImglistInfoBean getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(ImglistInfoBean imgpic_info) {
                this.imgpic_info = imgpic_info;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String gettitle() {
                return title;
            }

            public void settitle(String title) {
                this.title = title;
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

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getnickname() {
                return nickname;
            }

            public void setnickname(String nickname) {
                this.nickname = nickname;
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

            public ImglistInfoBean getimgpic_info() {
                return imgpic_info;
            }

            public void setimgpic_info(ImglistInfoBean imgpic_info) {
                this.imgpic_info = imgpic_info;
            }

            public static class VideoInfoBean {
                /**
                 * ext : mp3
                 * size : 8800409
                 * playtime : 06:06
                 * bitrate : 192
                 * md5 : db042237021d2461044100cddae4072204d1b2ed
                 * link : http://testapi.imxkj.com//music/db042237021d2461044100cddae4072204d1b2ed.mp3?log_at=3
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
        }
    }
}

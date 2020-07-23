package com.mxkj.yuanyintang.mainui.home.bean;

import com.google.gson.annotations.SerializedName;
import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/12/15.
 */

public class MyReleaseDetial {

    /**
     * code : 200
     * msg : success
     * data : {"id":8170,"title":"测试","video":"615d96d228341a84a59a6d77af8b1497","counts":0,"shares":0,"downloads":0,"nickname":"稻城雪融的时候","status":0,"category":6,"coin":0,"comment":0,"collects":0,"uid":37940,"create_time":1513327231,"imgpic":"70ad9ebbc91b2c88b90b175a569a721b","lyrics":"测试","intro":"测试","lrc":"测试","isdown":0,"music_type":1,"tag":[{"id":219,"title":"古风"},{"id":333,"title":"合唱"}],"hot":0,"new":0,"sort":0,"update_time":1513327231,"song":0,"admin_status":1,"updata":"","publish":0,"head":"07ca7475a300f35878251da368cecc239df9d6a0","event_id":0,"sex":0,"signature":"辗转两三个城市，四五个职场","fans_num":5,"ips_num":7,"attention_num":41,"member_type":1,"reason":"","hitstime":0,"monthhits":0,"weekhits":0,"dayhits":0,"is_music":1,"cover_zt":0,"cover_lx":0,"accompany":"","vip":0,"original":"","actual_comment":"","playtime":"01:48","vote_count":"","activity_status":0,"activity_serial":1,"activity_count":0,"log_at":3,"catename":"国风","video_link":"http://api.yuanyintang.com/music/615d96d228341a84a59a6d77af8b1497","imgpic_link":"http://api.yuanyintang.com/image/70ad9ebbc91b2c88b90b175a569a721b","head_link":"http://api.yuanyintang.com/image/07ca7475a300f35878251da368cecc239df9d6a0"}
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
         * id : 8170
         * title : 测试
         * video : 615d96d228341a84a59a6d77af8b1497
         * counts : 0
         * shares : 0
         * downloads : 0
         * nickname : 稻城雪融的时候
         * status : 0
         * category : 6
         * coin : 0
         * comment : 0
         * collects : 0
         * uid : 37940
         * create_time : 1513327231
         * imgpic : 70ad9ebbc91b2c88b90b175a569a721b
         * lyrics : 测试
         * intro : 测试
         * lrc : 测试
         * isdown : 0
         * music_type : 1
         * tag : [{"id":219,"title":"古风"},{"id":333,"title":"合唱"}]
         * hot : 0
         * new : 0
         * sort : 0
         * update_time : 1513327231
         * song : 0
         * admin_status : 1
         * updata :
         * publish : 0
         * head : 07ca7475a300f35878251da368cecc239df9d6a0
         * event_id : 0
         * sex : 0
         * signature : 辗转两三个城市，四五个职场
         * fans_num : 5
         * ips_num : 7
         * attention_num : 41
         * member_type : 1
         * reason :
         * hitstime : 0
         * monthhits : 0
         * weekhits : 0
         * dayhits : 0
         * is_music : 1
         * cover_zt : 0
         * cover_lx : 0
         * accompany :
         * vip : 0
         * original :
         * actual_comment :
         * playtime : 01:48
         * vote_count :
         * activity_status : 0
         * activity_serial : 1
         * activity_count : 0
         * log_at : 3
         * catename : 国风
         * video_link : http://api.yuanyintang.com/music/615d96d228341a84a59a6d77af8b1497
         * imgpic_link : http://api.yuanyintang.com/image/70ad9ebbc91b2c88b90b175a569a721b
         * head_link : http://api.yuanyintang.com/image/07ca7475a300f35878251da368cecc239df9d6a0
         */

        private int id;
        private String title;
        private String video;
        private String mv;
        private int counts;
        private int shares;
        private int downloads;
        private String nickname;
        private int status;
        private int category;
        private int coin;
        private int comment;
        private int collects;
        private int uid;
        private int create_time;
        private String imgpic;private String category_title;
        private String lyrics;
        private String intro;
        private String lrc;
        private int isdown;
        private int music_type;
        private String mv_filename;
        private String mv_size;

        public String getMv_filename() {
            return mv_filename;
        }

        public void setMv_filename(String mv_filename) {
            this.mv_filename = mv_filename;
        }

        public String getMv_size() {
            return mv_size;
        }

        public void setMv_size(String mv_size) {
            this.mv_size = mv_size;
        }

        public String getMv() {
            return mv;
        }

        public void setMv(String mv) {
            this.mv = mv;
        }

        public String getCategory_title() {
            return category_title;
        }

        public void setCategory_title(String category_title) {
            this.category_title = category_title;
        }

        private int hot;
        @SerializedName("new")
        private int newX;
        private int sort;
        private int update_time;
        private int song;
        private int admin_status;
        private String updata;
        private int publish;
        private String head;
        private int event_id;
        private int sex;
        private String signature;
        private int fans_num;
        private int ips_num;
        private int attention_num;
        private int member_type;
        private String reason;
        private int hitstime;
        private int monthhits;
        private int weekhits;
        private int dayhits;
        private int is_music;
        private int cover_zt;
        private int cover_lx;
        private String accompany;
        private int vip;
        private String original;
        private String actual_comment;
        private String playtime;
        private String vote_count;
        private int activity_status;
        private int activity_serial;
        private int activity_count;
        private int log_at;
        private List<MemberBeanX> member;
        private String catename;
        //        private String video_link;
//        private String imgpic_link;
        private String head_link;
        private String size;
        private String filename;
        private List<TagBean> tag;
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

        public List<MemberBeanX> getMember() {
            return member;
        }

        public void setMember(List<MemberBeanX> member) {
            this.member = member;
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

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
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

        public int getShares() {
            return shares;
        }

        public void setShares(int shares) {
            this.shares = shares;
        }

        public int getDownloads() {
            return downloads;
        }

        public void setDownloads(int downloads) {
            this.downloads = downloads;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
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

        public int getCollects() {
            return collects;
        }

        public void setCollects(int collects) {
            this.collects = collects;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public int getIsdown() {
            return isdown;
        }

        public void setIsdown(int isdown) {
            this.isdown = isdown;
        }

        public int getMusic_type() {
            return music_type;
        }

        public void setMusic_type(int music_type) {
            this.music_type = music_type;
        }

        public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public int getNewX() {
            return newX;
        }

        public void setNewX(int newX) {
            this.newX = newX;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public int getSong() {
            return song;
        }

        public void setSong(int song) {
            this.song = song;
        }

        public int getAdmin_status() {
            return admin_status;
        }

        public void setAdmin_status(int admin_status) {
            this.admin_status = admin_status;
        }

        public String getUpdata() {
            return updata;
        }

        public void setUpdata(String updata) {
            this.updata = updata;
        }

        public int getPublish() {
            return publish;
        }

        public void setPublish(int publish) {
            this.publish = publish;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getEvent_id() {
            return event_id;
        }

        public void setEvent_id(int event_id) {
            this.event_id = event_id;
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

        public int getAttention_num() {
            return attention_num;
        }

        public void setAttention_num(int attention_num) {
            this.attention_num = attention_num;
        }

        public int getMember_type() {
            return member_type;
        }

        public void setMember_type(int member_type) {
            this.member_type = member_type;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public int getHitstime() {
            return hitstime;
        }

        public void setHitstime(int hitstime) {
            this.hitstime = hitstime;
        }

        public int getMonthhits() {
            return monthhits;
        }

        public void setMonthhits(int monthhits) {
            this.monthhits = monthhits;
        }

        public int getWeekhits() {
            return weekhits;
        }

        public void setWeekhits(int weekhits) {
            this.weekhits = weekhits;
        }

        public int getDayhits() {
            return dayhits;
        }

        public void setDayhits(int dayhits) {
            this.dayhits = dayhits;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        public int getCover_zt() {
            return cover_zt;
        }

        public void setCover_zt(int cover_zt) {
            this.cover_zt = cover_zt;
        }

        public int getCover_lx() {
            return cover_lx;
        }

        public void setCover_lx(int cover_lx) {
            this.cover_lx = cover_lx;
        }

        public String getAccompany() {
            return accompany;
        }

        public void setAccompany(String accompany) {
            this.accompany = accompany;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getActual_comment() {
            return actual_comment;
        }

        public void setActual_comment(String actual_comment) {
            this.actual_comment = actual_comment;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getVote_count() {
            return vote_count;
        }

        public void setVote_count(String vote_count) {
            this.vote_count = vote_count;
        }

        public int getActivity_status() {
            return activity_status;
        }

        public void setActivity_status(int activity_status) {
            this.activity_status = activity_status;
        }

        public int getActivity_serial() {
            return activity_serial;
        }

        public void setActivity_serial(int activity_serial) {
            this.activity_serial = activity_serial;
        }

        public int getActivity_count() {
            return activity_count;
        }

        public void setActivity_count(int activity_count) {
            this.activity_count = activity_count;
        }

        public int getLog_at() {
            return log_at;
        }

        public void setLog_at(int log_at) {
            this.log_at = log_at;
        }

        public String getCatename() {
            return catename;
        }

        public void setCatename(String catename) {
            this.catename = catename;
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

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public List<TagBean> getTag() {
            return tag;
        }

        public void setTag(List<TagBean> tag) {
            this.tag = tag;
        }

        public static class TagBean {
            /**
             * id : 219
             * title : 古风
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

        public static class MemberBeanX {
            /**
             * music_type : 呵呵呵呵
             * member : [{"music_type":"呵呵呵呵","nickname":"13170556481","uid":16516,"head":"3d3d27527715c56204c6e063e34883ae60384a97","is_music":"0","head_link":"http://testapi.imxkj.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3","head_info":{"ext":"jpg","w":"330","h":"333","size":"15105","is_long":"0","md5":"3d3d27527715c56204c6e063e34883ae60384a97","link":"http://testapi.imxkj.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3"}},{"music_type":"呵呵呵呵","nickname":"345qwe","uid":30143,"head":"3d3d27527715c56204c6e063e34883ae60384a97","is_music":"0","head_link":"http://testapi.imxkj.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3","head_info":{"ext":"jpg","w":"330","h":"333","size":"15105","is_long":"0","md5":"3d3d27527715c56204c6e063e34883ae60384a97","link":"http://testapi.imxkj.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3"}}]
             */

            private String music_type;
            private List<MemberBean> member;

            public String getMusic_type() {
                return music_type;
            }

            public void setMusic_type(String music_type) {
                this.music_type = music_type;
            }

            public List<MemberBean> getMember() {
                return member;
            }

            public void setMember(List<MemberBean> member) {
                this.member = member;
            }

            public static class MemberBean implements Serializable {
                /**
                 * music_type : 呵呵呵呵
                 * nickname : 13170556481
                 * uid : 16516
                 * head : 3d3d27527715c56204c6e063e34883ae60384a97
                 * is_music : 0
                 * head_link : http://testapi.imxkj.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3
                 * head_info : {"ext":"jpg","w":"330","h":"333","size":"15105","is_long":"0","md5":"3d3d27527715c56204c6e063e34883ae60384a97","link":"http://testapi.imxkj.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3"}
                 */

                private String music_type;
                private String nickname;
                private int uid;
                private String is_music;
                private String head_link;

                public String getMusic_type() {
                    return music_type;
                }

                public void setMusic_type(String music_type) {
                    this.music_type = music_type;
                }

                public String getIs_music() {
                    return is_music;
                }

                public void setIs_music(String is_music) {
                    this.is_music = is_music;
                }

                public String getHead_link() {
                    return head_link;
                }

                public void setHead_link(String head_link) {
                    this.head_link = head_link;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nicknameX) {
                    this.nickname = nicknameX;
                }

                public int getUid() {
                    return uid;
                }

                public void setUid(int uidX) {
                    this.uid = uidX;
                }

            }
        }
    }
}

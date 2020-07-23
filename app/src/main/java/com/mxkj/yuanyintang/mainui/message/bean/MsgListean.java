package com.mxkj.yuanyintang.mainui.message.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.mxkj.yuanyintang.mainui.home.data.Constant;

import java.io.Serializable;
import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.AgreeItemType.AGREE_MY_COMMENT_FOR_ALBUM;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.AgreeItemType.AGREE_MY_COMMENT_FOR_MUSIC;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.AgreeItemType.AGREE_MY_COMMENT_FOR_POND;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MYMUSIC;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_ALBUM;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_COMMENT_FOR_ALBUM;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_COMMENT_FOR_MUSIC;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_COMMENT_FOR_POND;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.CommentMsgItemType.REPLY_MY_POND;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.SystemMsgItemType.SYS_MSG;

/**
 * Created by LiuJie on 2018/3/7.
 */

public class MsgListean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"count":{"notice_msg_count":0,"system_msg_count":0,"comment_msg_count":0},"data":[{"id":176,"title":"这个跳转音乐详情","body":"这个跳转音乐详情这个跳转音乐详情这个跳转音乐详情","type":1,"from_type":1,"create_time":1520472614,"imgpic":"","urlmodel":"musicDetails","app_url":"","web_url":"","from_id":22,"fid":0,"from_uid":"","status":1,"item_id":0,"create_time_desc":"4小时前","imgpic_link":"","imgpic_info":{"ext":"","w":"","h":"","size":"43659","is_long":"0","md5":"","link":"http://api.demo.com//image//3"}},{"id":175,"title":"这个是跳转 url","body":"这个是跳转 url这个是跳转 url这个是跳转 url这个是跳转 url","type":1,"from_type":1,"create_time":1520472592,"imgpic":"c7adcb987e5224301258c6f7efb2d53e","urlmodel":"activity","app_url":"wap.baidu.com","web_url":"www.baidu.com","from_id":0,"fid":0,"from_uid":"","status":1,"item_id":0,"create_time_desc":"4小时前","imgpic_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3","imgpic_info":{"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}},{"id":174,"title":"这个是带土的公告","body":"这个是带土的公告内容","type":1,"from_type":1,"create_time":1520472560,"imgpic":"c7adcb987e5224301258c6f7efb2d53e","urlmodel":"","app_url":"","web_url":"","from_id":0,"fid":0,"from_uid":"","status":1,"item_id":0,"create_time_desc":"4小时前","imgpic_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3","imgpic_info":{"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}},{"id":173,"title":"这个是一个纯公告的消息","body":"这个是一个纯公告的消息的内容这个是一个纯公告的消息的内容这个是一个纯公告的消息的内容这个是一个纯公告的消息的内容这个是一个纯公告的消息的内容","type":1,"from_type":1,"create_time":1520472375,"imgpic":"","urlmodel":"","app_url":"","web_url":"","from_id":0,"fid":0,"from_uid":"","status":1,"item_id":0,"create_time_desc":"4小时前","imgpic_link":"","imgpic_info":{"ext":"","w":"","h":"","size":"43659","is_long":"0","md5":"","link":"http://api.demo.com//image//3"}}]}
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

    public static class DataBeanX {
        /**
         * count : {"notice_msg_count":0,"system_msg_count":0,"comment_msg_count":0}
         * data : [{"id":176,"title":"这个跳转音乐详情","body":"这个跳转音乐详情这个跳转音乐详情这个跳转音乐详情","type":1,"from_type":1,"create_time":1520472614,"imgpic":"","urlmodel":"musicDetails","app_url":"","web_url":"","from_id":22,"fid":0,"from_uid":"","status":1,"item_id":0,"create_time_desc":"4小时前","imgpic_link":"","imgpic_info":{"ext":"","w":"","h":"","size":"43659","is_long":"0","md5":"","link":"http://api.demo.com//image//3"}},{"id":175,"title":"这个是跳转 url","body":"这个是跳转 url这个是跳转 url这个是跳转 url这个是跳转 url","type":1,"from_type":1,"create_time":1520472592,"imgpic":"c7adcb987e5224301258c6f7efb2d53e","urlmodel":"activity","app_url":"wap.baidu.com","web_url":"www.baidu.com","from_id":0,"fid":0,"from_uid":"","status":1,"item_id":0,"create_time_desc":"4小时前","imgpic_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3","imgpic_info":{"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}},{"id":174,"title":"这个是带土的公告","body":"这个是带土的公告内容","type":1,"from_type":1,"create_time":1520472560,"imgpic":"c7adcb987e5224301258c6f7efb2d53e","urlmodel":"","app_url":"","web_url":"","from_id":0,"fid":0,"from_uid":"","status":1,"item_id":0,"create_time_desc":"4小时前","imgpic_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3","imgpic_info":{"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}},{"id":173,"title":"这个是一个纯公告的消息","body":"这个是一个纯公告的消息的内容这个是一个纯公告的消息的内容这个是一个纯公告的消息的内容这个是一个纯公告的消息的内容这个是一个纯公告的消息的内容","type":1,"from_type":1,"create_time":1520472375,"imgpic":"","urlmodel":"","app_url":"","web_url":"","from_id":0,"fid":0,"from_uid":"","status":1,"item_id":0,"create_time_desc":"4小时前","imgpic_link":"","imgpic_info":{"ext":"","w":"","h":"","size":"43659","is_long":"0","md5":"","link":"http://api.demo.com//image//3"}}]
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


        public static class CountBean {
            /**
             * notice_msg_count : 0
             * system_msg_count : 0
             * comment_msg_count : 0
             */

            private int notice_msg_count;
            private int system_msg_count;
            private int comment_msg_count;

            public int getNotice_msg_count() {
                return notice_msg_count;
            }

            public void setNotice_msg_count(int notice_msg_count) {
                this.notice_msg_count = notice_msg_count;
            }

            public int getSystem_msg_count() {
                return system_msg_count;
            }

            public void setSystem_msg_count(int system_msg_count) {
                this.system_msg_count = system_msg_count;
            }

            public int getComment_msg_count() {
                return comment_msg_count;
            }

            public void setComment_msg_count(int comment_msg_count) {
                this.comment_msg_count = comment_msg_count;
            }
        }

        public static class DataBean implements MultiItemEntity {
            /**
             * id : 176
             * title : 这个跳转音乐详情
             * body : 这个跳转音乐详情这个跳转音乐详情这个跳转音乐详情
             * type : 1
             * from_type : 1
             * create_time : 1520472614
             * imgpic :
             * urlmodel : musicDetails
             * app_url :
             * web_url :
             * from_id : 22
             * fid : 0
             * from_uid :
             * status : 1
             * item_id : 0
             * create_time_desc : 4小时前
             * imgpic_link :
             * imgpic_info : {"ext":"","w":"","h":"","size":"43659","is_long":"0","md5":"","link":"http://api.demo.com//image//3"}
             */

            private int id;
            private String title;
            private String body;
            private int type;
            private int from_type;
            private int create_time;
            private String imgpic;
            private String urlmodel;
            private String app_url;
            private String web_url;
            private int from_id;
            private int fid;
            private int from_uid;
            private int status;
            private int item_id;
            private String create_time_desc;
            private String imgpic_link;
            private ImgpicInfoBean imgpic_info;
            private AgreeInfoBean agree_info;
            private FromInfoBean from_info;
            private UserInfoBean user_info;
            private ItemInfoBean item_info;
            private ReplyApiDescBean reply_api_desc;

            public ItemInfoBean getItem_info() {
                return item_info;
            }

            public void setItem_info(ItemInfoBean item_info) {
                this.item_info = item_info;
            }

            public ReplyApiDescBean getReply_api_desc() {
                return reply_api_desc;
            }

            public void setReply_api_desc(ReplyApiDescBean reply_api_desc) {
                this.reply_api_desc = reply_api_desc;
            }

            public static class ItemInfoBean {
                /**
                 * id : 1308
                 * title : 会哈手说切让肉上
                 * uid : 37940
                 * content : 1144444
                 * create_time : 1520576096
                 * agrees : 0
                 * nickname : 稻城雪融的时候
                 * counts : 3
                 * imglist :
                 */

                private int id;
                private String title;
                private int uid;
                private String content;
                private int create_time;
                private int agrees;
                private String nickname;
                private int counts;
                private int TAG;
                private List<ImglistInfoBean> imglist_info;

                public int getTAG() {
                    return TAG;
                }

                public void setTAG(int TAG) {
                    this.TAG = TAG;
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

                public int getUid() {
                    return uid;
                }

                public void setUid(int uid) {
                    this.uid = uid;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getAgrees() {
                    return agrees;
                }

                public void setAgrees(int agrees) {
                    this.agrees = agrees;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public int getCounts() {
                    return counts;
                }

                public void setCounts(int counts) {
                    this.counts = counts;
                }

//                public String getImglist() {
//                    return imglist;
//                }
//
//                public void setImglist(String imglist) {
//                    this.imglist = imglist;
//                }
            }

            @Override
            public int getItemType() {
                /**
                 * 布局类型，
                 * */
                if (type == 1 && from_type == 1) {//TODO  公告消息(4种)  urlmodel：后台定义的，page跳页面，activity跳网页（一般是活动）
                    if (imgpic_info != null && !TextUtils.isEmpty(imgpic_info.getLink())) {
                        //有图片
                        return Constant.MsgCenterItem.BroadMsgItemType.HAVE_IMG;
                    } else if (imgpic_info != null && TextUtils.isEmpty(imgpic_info.getLink())) {
                        //没有图片
                        return Constant.MsgCenterItem.BroadMsgItemType.NO_IMG;
                    } else {
                        //没有图片
                        return Constant.MsgCenterItem.BroadMsgItemType.NO_IMG;
                    }
                } else if (type == 2) {
                    return SYS_MSG;
                } else if (type == 3) {//TODO 回复评论消息
//                    评论消息下面分4种：from_type 含义：1.单曲的评论2.歌单的评价3.池塘的评论 （1级评论）4.池塘的回复（2级评论）
                    if (from_type == 1) {
                        if (from_info != null && fid > 0) {
//                            别人回复了我对某个音乐的评论
                            return REPLY_MY_COMMENT_FOR_MUSIC;
                        } else if (from_info != null && fid == 0) {
//                            别人评论了我的歌曲
                            return REPLY_MYMUSIC;
                        }

                    } else if (from_type == 2) {
                        if (from_info != null && fid > 0) {//
//                            别人回复了我对某个歌单的评论
                            return REPLY_MY_COMMENT_FOR_ALBUM;
                        } else if (from_info != null && fid == 0) {
//                            别人评论了我的歌单
                            return REPLY_MY_ALBUM;
                        }
                    } else if (from_type == 3) {
                        if (from_info != null && fid > 0) {//
//                            别人回复了我对某个池塘的评论
                            return REPLY_MY_COMMENT_FOR_POND;
                        } else if (from_info != null && fid == 0) {
//                            别人评论了我的池塘
                            return REPLY_MY_POND;
                        }
                    } else if (from_type == 4) {
                        if (from_info != null && fid > 0) {//
//                            别人回复了我对某个池塘的评论
                            return REPLY_MY_COMMENT_FOR_POND;
                        }
                    }
                } else if (type == 4) {//TODO 评论消息（点了赞）
//                    1.歌单点赞2.单曲评价点赞3.歌单评价点赞4.池塘评论（1级）点赞5.池塘回复（2级）点赞
                    if (from_type == 1) {
                        if (from_info != null) {//
                            return AGREE_MY_COMMENT_FOR_MUSIC;
                        }
                    } else if (from_type == 2) {
                        if (from_info != null) {//
                            return AGREE_MY_COMMENT_FOR_ALBUM;
                        } else if (from_info != null) {
//                            return AGREE_MY_ALBUM;
                        }
                    } else if (from_type == 3) {
                        if (from_info != null) {//
                            return AGREE_MY_COMMENT_FOR_POND;
                        } else if (from_info != null) {
//                            return AGREE_MY_POND;
                        }
                    } else if (from_type == 4) {
                        if (from_info != null) {//
                            return AGREE_MY_COMMENT_FOR_POND;
                        }
                    }
                }
                return 0;
            }

            public AgreeInfoBean getAgree_info() {
                return agree_info;
            }

            public void setAgree_info(AgreeInfoBean agree_info) {
                this.agree_info = agree_info;
            }

            public FromInfoBean getFrom_info() {
                return from_info;
            }

            public void setFrom_info(FromInfoBean from_info) {
                this.from_info = from_info;
            }

            public UserInfoBean getUser_info() {
                return user_info;
            }

            public void setUser_info(UserInfoBean user_info) {
                this.user_info = user_info;
            }

            public static class ReplyApiDescBean {
                /**
                 * api_desc : （池塘回复接口）topcom
                 * fid : 1151
                 * pid : 625
                 */

                private String api_desc;
                @SerializedName("fid")
                private int fid;
                private int pid;

                public String getApi_desc() {
                    return api_desc;
                }

                public void setApi_desc(String api_desc) {
                    this.api_desc = api_desc;
                }

                public int getFid() {
                    return fid;
                }

                public void setFid(int fid) {
                    this.fid = fid;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }
            }

            public class UserInfoBean {

                /**
                 * id : 44758
                 * nickname : 源音sadasd
                 * head : 6c055c617f3ebd31cdd4a24759a409ecad24252a
                 * is_music : 0
                 * head_link : http://api.demo.com//image/6c055c617f3ebd31cdd4a24759a409ecad24252a/1
                 * head_info :
                 */

                private int id;
                private String nickname;
                private String head;
                private int is_music;
                private String head_link;
                private String head_info;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
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

                public int getIs_music() {
                    return is_music;
                }

                public void setIs_music(int is_music) {
                    this.is_music = is_music;
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
            }

            public static class AgreeInfoBean {

                /**
                 * agree_type : 4
                 * item_id : 79474
                 * status : 0
                 */

                private int agree_type;
                private int item_id;
                private int status;
                private int agree_num;

                public int getAgree_num() {
                    return agree_num;
                }

                public void setAgree_num(int agree_num) {
                    this.agree_num = agree_num;
                }

                public int getAgree_type() {
                    return agree_type;
                }

                public void setAgree_type(int agree_type) {
                    this.agree_type = agree_type;
                }

                public int getItem_id() {
                    return item_id;
                }

                public void setItem_id(int item_id) {
                    this.item_id = item_id;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }
            }


            public static class FromInfoBean {

                /**
                 * create_time : 1519717002
                 * id : 346
                 * uid : 50782
                 * content : =-0-=0=
                 * status : 1
                 * fid : 1
                 * agrees
                 * nickname
                 */

                private int create_time;
                private int is_delete;
                private int id;
                private int uid;
                private String content;
                private String nickname;
                private int status;
                private int agrees;
                private String agrees_text;
                private int fid;
                private int pid;

                public String getAgrees_text() {
                    return agrees_text;
                }

                public void setAgrees_text(String agrees_text) {
                    this.agrees_text = agrees_text;
                }

                public int getIs_delete() {
                    return is_delete;
                }

                public void setIs_delete(int is_delete) {
                    this.is_delete = is_delete;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public int getAgrees() {
                    return agrees;
                }

                public void setAgrees(int agrees) {
                    this.agrees = agrees;
                }

                private ReplyInfoBean reply_info;

                public ReplyInfoBean getReply_info() {
                    return reply_info;
                }

                public void setReply_info(ReplyInfoBean reply_info) {
                    this.reply_info = reply_info;
                }

                public static class ReplyInfoBean {


                    /**
                     * id : 79474
                     * uid : 44758
                     * content : The fact I can get it
                     * create_time : 1520410585
                     * agrees : 1
                     * nickname : 源音sadasd
                     */

                    private int id;
                    private int is_delete;
                    private int uid;
                    private String content;
                    private int create_time;
                    private int agrees;
                    private String nickname;

                    public int getIs_delete() {
                        return is_delete;
                    }

                    public void setIs_delete(int is_delete) {
                        this.is_delete = is_delete;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getUid() {
                        return uid;
                    }

                    public void setUid(int uid) {
                        this.uid = uid;
                    }

                    public String getContent() {
                        return content;
                    }

                    public void setContent(String content) {
                        this.content = content;
                    }

                    public int getCreate_time() {
                        return create_time;
                    }

                    public void setCreate_time(int create_time) {
                        this.create_time = create_time;
                    }

                    public int getAgrees() {
                        return agrees;
                    }

                    public void setAgrees(int agrees) {
                        this.agrees = agrees;
                    }

                    public String getNickname() {
                        return nickname;
                    }

                    public void setNickname(String nickname) {
                        this.nickname = nickname;
                    }
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getUid() {
                    return uid;
                }

                public void setUid(int uid) {
                    this.uid = uid;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getFid() {
                    return fid;
                }

                public void setFid(int fid) {
                    this.fid = fid;
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

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getFrom_type() {
                return from_type;
            }

            public void setFrom_type(int from_type) {
                this.from_type = from_type;
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

            public String getUrlmodel() {
                return urlmodel;
            }

            public void setUrlmodel(String urlmodel) {
                this.urlmodel = urlmodel;
            }

            public String getApp_url() {
                return app_url;
            }

            public void setApp_url(String app_url) {
                this.app_url = app_url;
            }

            public String getWeb_url() {
                return web_url;
            }

            public void setWeb_url(String web_url) {
                this.web_url = web_url;
            }

            public int getFrom_id() {
                return from_id;
            }

            public void setFrom_id(int from_id) {
                this.from_id = from_id;
            }

            public int getFid() {
                return fid;
            }

            public void setFid(int fid) {
                this.fid = fid;
            }

            public int getFrom_uid() {
                return from_uid;
            }

            public void setFrom_uid(int from_uid) {
                this.from_uid = from_uid;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getItem_id() {
                return item_id;
            }

            public void setItem_id(int item_id) {
                this.item_id = item_id;
            }

            public String getCreate_time_desc() {
                return create_time_desc;
            }

            public void setCreate_time_desc(String create_time_desc) {
                this.create_time_desc = create_time_desc;
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

            public static class ImgpicInfoBean implements Serializable {
                /**
                 * ext :
                 * w :
                 * h :
                 * size : 43659
                 * is_long : 0
                 * md5 :
                 * link : http://api.demo.com//image//3
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
    }
}

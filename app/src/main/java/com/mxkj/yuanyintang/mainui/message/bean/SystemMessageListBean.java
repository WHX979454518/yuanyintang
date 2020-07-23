package com.mxkj.yuanyintang.mainui.message.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/13.
 */



/**
 *
 * 接口修修改后这个只用来展示动态消息
 *
 * */

public class SystemMessageListBean implements Serializable{

    /**
     * code : 200
     * msg : success
     * data : {"count":{"systemCount":21,"dynamicCount":2,"commentCount":4},"data":[{"id":10120163,"rec_type":37997,"rec_id":37997,"type":11,"nickname":"Wai wang","sex":1,"head":"4617102b898f10c474dfb3eadba3274db9266874","vip":0,"user_id":41160,"title":"你的圈子评论被回复了","content":"你的圈子评论被回复了，，我们的生活是一","status":0,"pdate":0,"create_time":"1小时前","item_id":3290,"obj_type":3,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":3290,"uid":35045,"title":"忍术一直被封印，不曾被使用。或者，使用此术的人，已经离我们而去，留下永恒的传说。火影里的禁术，犹如一股巨大的"},"head_link":"http://demoapi.yuanyintang.com/image/4617102b898f10c474dfb3eadba3274db9266874"},{"id":10120157,"rec_type":37997,"rec_id":37997,"type":11,"nickname":"源音塘545337","sex":0,"head":"ab705cd2b02272ff9c7f579be8bd8368","vip":0,"user_id":37986,"title":"你的圈子评论被回复了","content":"你的圈子评论被回复了，啊啊啊啊啊啊啊","status":0,"pdate":0,"create_time":"1小时前","item_id":3290,"obj_type":3,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":3290,"uid":35045,"title":"忍术一直被封印，不曾被使用。或者，使用此术的人，已经离我们而去，留下永恒的传说。火影里的禁术，犹如一股巨大的"},"head_link":"http://demoapi.yuanyintang.com/image/ab705cd2b02272ff9c7f579be8bd8368"},{"id":10120083,"rec_type":37997,"rec_id":37997,"type":11,"nickname":"稻城雪融的时候","sex":1,"head":"07ca7475a300f35878251da368cecc239df9d6a0","vip":0,"user_id":37940,"title":"你的圈子评论被回复了","content":"你的圈子评论被回复了，[:4][:3][:14]","status":0,"pdate":0,"create_time":"7小时前","item_id":3290,"obj_type":3,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":3290,"uid":35045,"title":"忍术一直被封印，不曾被使用。或者，使用此术的人，已经离我们而去，留下永恒的传说。火影里的禁术，犹如一股巨大的"},"head_link":"http://demoapi.yuanyintang.com/image/07ca7475a300f35878251da368cecc239df9d6a0"},{"id":10119704,"rec_type":37997,"rec_id":37997,"type":11,"nickname":"稻城雪融的时候","sex":1,"head":"07ca7475a300f35878251da368cecc239df9d6a0","vip":0,"user_id":37940,"title":"你的圈子被评论了","content":"你的圈子【你牛规规矩矩】被评论了","status":0,"pdate":0,"create_time":"昨天","item_id":3247,"obj_type":3,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":3247,"uid":37997,"title":"用户新增了歌单"},"head_link":"http://demoapi.yuanyintang.com/image/07ca7475a300f35878251da368cecc239df9d6a0"},{"id":1185652,"rec_type":0,"rec_id":37997,"type":0,"nickname":"茶荼","sex":0,"head":"c54923ea23db03110ee30b806d52da6c4460b38d","vip":0,"user_id":36916,"title":"你的音乐评论被回复了","content":"茶荼回复了你的评论","status":1,"pdate":1509353925,"create_time":"10-11","item_id":5124,"obj_type":1,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":5124,"uid":36916,"title":"【茶荼】なきむし爱哭鬼"},"head_link":"http://demoapi.yuanyintang.com/image/c54923ea23db03110ee30b806d52da6c4460b38d"}]}
     */

    private int code;
    private String msg;
    private DataBeanXX data;

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

    public DataBeanXX getData() {
        return data;
    }

    public void setData(DataBeanXX data) {
        this.data = data;
    }

    public static class DataBeanXX implements Serializable{
        /**
         * count : {"systemCount":21,"dynamicCount":2,"commentCount":4}
         * data : [{"id":10120163,"rec_type":37997,"rec_id":37997,"type":11,"nickname":"Wai wang","sex":1,"head":"4617102b898f10c474dfb3eadba3274db9266874","vip":0,"user_id":41160,"title":"你的圈子评论被回复了","content":"你的圈子评论被回复了，，我们的生活是一","status":0,"pdate":0,"create_time":"1小时前","item_id":3290,"obj_type":3,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":3290,"uid":35045,"title":"忍术一直被封印，不曾被使用。或者，使用此术的人，已经离我们而去，留下永恒的传说。火影里的禁术，犹如一股巨大的"},"head_link":"http://demoapi.yuanyintang.com/image/4617102b898f10c474dfb3eadba3274db9266874"},{"id":10120157,"rec_type":37997,"rec_id":37997,"type":11,"nickname":"源音塘545337","sex":0,"head":"ab705cd2b02272ff9c7f579be8bd8368","vip":0,"user_id":37986,"title":"你的圈子评论被回复了","content":"你的圈子评论被回复了，啊啊啊啊啊啊啊","status":0,"pdate":0,"create_time":"1小时前","item_id":3290,"obj_type":3,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":3290,"uid":35045,"title":"忍术一直被封印，不曾被使用。或者，使用此术的人，已经离我们而去，留下永恒的传说。火影里的禁术，犹如一股巨大的"},"head_link":"http://demoapi.yuanyintang.com/image/ab705cd2b02272ff9c7f579be8bd8368"},{"id":10120083,"rec_type":37997,"rec_id":37997,"type":11,"nickname":"稻城雪融的时候","sex":1,"head":"07ca7475a300f35878251da368cecc239df9d6a0","vip":0,"user_id":37940,"title":"你的圈子评论被回复了","content":"你的圈子评论被回复了，[:4][:3][:14]","status":0,"pdate":0,"create_time":"7小时前","item_id":3290,"obj_type":3,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":3290,"uid":35045,"title":"忍术一直被封印，不曾被使用。或者，使用此术的人，已经离我们而去，留下永恒的传说。火影里的禁术，犹如一股巨大的"},"head_link":"http://demoapi.yuanyintang.com/image/07ca7475a300f35878251da368cecc239df9d6a0"},{"id":10119704,"rec_type":37997,"rec_id":37997,"type":11,"nickname":"稻城雪融的时候","sex":1,"head":"07ca7475a300f35878251da368cecc239df9d6a0","vip":0,"user_id":37940,"title":"你的圈子被评论了","content":"你的圈子【你牛规规矩矩】被评论了","status":0,"pdate":0,"create_time":"昨天","item_id":3247,"obj_type":3,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":3247,"uid":37997,"title":"用户新增了歌单"},"head_link":"http://demoapi.yuanyintang.com/image/07ca7475a300f35878251da368cecc239df9d6a0"},{"id":1185652,"rec_type":0,"rec_id":37997,"type":0,"nickname":"茶荼","sex":0,"head":"c54923ea23db03110ee30b806d52da6c4460b38d","vip":0,"user_id":36916,"title":"你的音乐评论被回复了","content":"茶荼回复了你的评论","status":1,"pdate":1509353925,"create_time":"10-11","item_id":5124,"obj_type":1,"item_type":3,"feedback_status":"","feedback_id":0,"data":{"id":5124,"uid":36916,"title":"【茶荼】なきむし爱哭鬼"},"head_link":"http://demoapi.yuanyintang.com/image/c54923ea23db03110ee30b806d52da6c4460b38d"}]
         */

        private CountBean count;
        private List<DataBeanX> data;

        public CountBean getCount() {
            return count;
        }

        public void setCount(CountBean count) {
            this.count = count;
        }

        public List<DataBeanX> getData() {
            return data;
        }

        public void setData(List<DataBeanX> data) {
            this.data = data;
        }

        public static class CountBean implements Serializable{
            /**
             * systemCount : 21
             * dynamicCount : 2
             * commentCount : 4
             */

            private int systemCount;
            private int dynamicCount;
            private int commentCount;

            public int getSystemCount() {
                return systemCount;
            }

            public void setSystemCount(int systemCount) {
                this.systemCount = systemCount;
            }

            public int getDynamicCount() {
                return dynamicCount;
            }

            public void setDynamicCount(int dynamicCount) {
                this.dynamicCount = dynamicCount;
            }

            public int getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
            }
        }

        public static class DataBeanX implements Serializable{
            /**
             * id : 10120163
             * rec_type : 37997
             * rec_id : 37997
             * type : 11
             * nickname : Wai wang
             * sex : 1
             * head : 4617102b898f10c474dfb3eadba3274db9266874
             * vip : 0
             * user_id : 41160
             * title : 你的圈子评论被回复了
             * content : 你的圈子评论被回复了，，我们的生活是一
             * status : 0
             * pdate : 0
             * create_time : 1小时前
             * item_id : 3290
             * obj_type : 3
             * item_type : 3
             * feedback_status :
             * feedback_id : 0
             * data : {"id":3290,"uid":35045,"title":"忍术一直被封印，不曾被使用。或者，使用此术的人，已经离我们而去，留下永恒的传说。火影里的禁术，犹如一股巨大的"}
             * head_link : http://demoapi.yuanyintang.com/image/4617102b898f10c474dfb3eadba3274db9266874
             */

            private int id;
            private int rec_type;
            private int rec_id;
            private int type;
            private String nickname;
            private int sex;
            private String head;
            private int vip;
            private int user_id;
            private String title;
            private String content;
            private int status;
            private int pdate;
            private String create_time;
            private int item_id;
            private int obj_type;
            private int item_type;
            private String feedback_status;
            private int feedback_id;
            private DataBean data;
            private String head_link;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getRec_type() {
                return rec_type;
            }

            public void setRec_type(int rec_type) {
                this.rec_type = rec_type;
            }

            public int getRec_id() {
                return rec_id;
            }

            public void setRec_id(int rec_id) {
                this.rec_id = rec_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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

            public int getVip() {
                return vip;
            }

            public void setVip(int vip) {
                this.vip = vip;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getPdate() {
                return pdate;
            }

            public void setPdate(int pdate) {
                this.pdate = pdate;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getItem_id() {
                return item_id;
            }

            public void setItem_id(int item_id) {
                this.item_id = item_id;
            }

            public int getObj_type() {
                return obj_type;
            }

            public void setObj_type(int obj_type) {
                this.obj_type = obj_type;
            }

            public int getItem_type() {
                return item_type;
            }

            public void setItem_type(int item_type) {
                this.item_type = item_type;
            }

            public String getFeedback_status() {
                return feedback_status;
            }

            public void setFeedback_status(String feedback_status) {
                this.feedback_status = feedback_status;
            }

            public int getFeedback_id() {
                return feedback_id;
            }

            public void setFeedback_id(int feedback_id) {
                this.feedback_id = feedback_id;
            }

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public static class DataBean implements Serializable{
                /**
                 * id : 3290
                 * uid : 35045
                 * title : 忍术一直被封印，不曾被使用。或者，使用此术的人，已经离我们而去，留下永恒的传说。火影里的禁术，犹如一股巨大的
                 */

                private int id;
                private int uid;
                private String title;

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

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }
        }
    }
}

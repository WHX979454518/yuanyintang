package com.mxkj.yuanyintang.mainui.message.bean;

/**
 * Created by LiuJie on 2017/11/12.
 */

public class MessageEMMDataBean {

    /**
     * content : 你的圈子【[:2][:4][:16][:14][:16][:3][:2][:2]】被评论了
     * create_time : 1510383157
     * data : {"id":3348,"title":"用户新增了池塘","uid":38076}
     * item_id : 3348
     * obj_type : 3
     * title : 你的圈子被评论了
     */

    private String content;
    private int create_time;
    private DataBean data;
    private String item_id;
    private String obj_type;
    private String title;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getObj_type() {
        return obj_type;
    }

    public void setObj_type(String obj_type) {
        this.obj_type = obj_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class DataBean {
        /**
         * id : 3348
         * title : 用户新增了池塘
         * uid : 38076
         */

        private int id;
        private String title;
        private int uid;

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
    }
}

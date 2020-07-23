package com.mxkj.yuanyintang.mainui.home.bean;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/28.
 */

public class SearchRecommendBean {


    /**
     * code : 200
     * msg : 返回推荐
     * data : [{"id":3578,"title":"爱你一万年"},{"id":3622,"title":"y"},{"id":40,"title":"1"},{"id":865,"title":"满汉女神"},{"id":2344,"title":"S"},{"id":3849,"title":"坎坷"},{"id":84,"title":"3"},{"id":3600,"title":"哈哈"},{"id":3769,"title":"李白"},{"id":153,"title":"了悟"}]
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
         * id : 3578
         * title : 爱你一万年
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
}

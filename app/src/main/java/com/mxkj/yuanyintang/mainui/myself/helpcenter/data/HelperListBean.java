package com.mxkj.yuanyintang.mainui.myself.helpcenter.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zuixia on 2018/4/17.
 */

public class HelperListBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : [{"id":79,"title":"申请首页推荐","alias":"sdsads"},{"id":77,"title":"我是音乐人","alias":""},{"id":78,"title":"我不是音乐人","alias":""},{"id":82,"title":"r认证状态跳转","alias":"realnamehelp"},{"id":80,"title":"详情页的V ","alias":"Vmusicianhelp"},{"id":81,"title":"歌词问号","alias":"lyricsnotwork"}]
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

    public static class DataBean implements  Serializable{
        /**
         * id : 79
         * title : 申请首页推荐
         * alias : sdsads
         */

        private int id;
        private String title;
        private String content;
        private String alias;
        private String description;
        private String share_url;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }
}

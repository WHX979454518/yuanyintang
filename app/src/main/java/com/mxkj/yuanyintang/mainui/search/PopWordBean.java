package com.mxkj.yuanyintang.mainui.search;

import java.util.List;

/**
 * Created by LiuJie on 2017/11/21.
 */

public class PopWordBean {


    /**
     * code : 200
     * msg : success
     * data : [{"title":"1211","nickname":"sss11"},{"title":"2","nickname":"yyytt"}]
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
         * title : 1211
         * nickname : sss11
         */

        private String title;
        private String nickname;

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
    }
}

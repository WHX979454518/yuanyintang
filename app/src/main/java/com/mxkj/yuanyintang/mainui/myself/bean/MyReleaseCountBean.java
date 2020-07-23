package com.mxkj.yuanyintang.mainui.myself.bean;

/**
 * Created by LiuJie on 2017/10/30.
 */

public class MyReleaseCountBean {

    /**
     * code : 200
     * msg : success
     * data : {"all":2,"ing":0,"ok":2,"draft":0,"no":0}
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
         * all : 2
         * ing : 0
         * ok : 2
         * draft : 0
         * no : 0
         */

        private int all;
        private int ing;
        private int ok;
        private int draft;
        private int no;

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }

        public int getIng() {
            return ing;
        }

        public void setIng(int ing) {
            this.ing = ing;
        }

        public int getOk() {
            return ok;
        }

        public void setOk(int ok) {
            this.ok = ok;
        }

        public int getDraft() {
            return draft;
        }

        public void setDraft(int draft) {
            this.draft = draft;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }
    }
}

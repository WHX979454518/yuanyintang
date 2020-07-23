package com.mxkj.yuanyintang.mainui.search;

import java.util.List;

public class HotSearch {

    /**
     * code : 400
     * msg : 常搜索
     * data : [{"id":1,"title":"王力宏","url":"www.baidu.com","status":1},{"id":2,"title":"周杰伦","url":"www.xinlang.com","status":1},{"id":3,"title":"薛子谦","url":"www.google.com","status":1},{"id":4,"title":"bigbang","url":"www.handa.com","status":1}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 王力宏
         * url : www.baidu.com
         * status : 1
         */

        private int id;
        private String title;
        private String url;
        private int status;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

package com.mxkj.yuanyintang.mainui.pond.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/9/28.
 */

public class ChoosePlayListTagBean implements Serializable{

    /**
     * code : 200
     * msg : success
     * data : [{"id":14,"title":"场景","alias":"tag","tags":[{"id":6957,"title":"夜晚","code":"tag","selected":0},{"id":6958,"title":"学习","code":"tag","selected":0},{"id":6960,"title":"清晨","code":"tag","selected":0},{"id":6962,"title":"工作","code":"tag","selected":0},{"id":6964,"title":"休息","code":"tag","selected":0},{"id":6965,"title":"游戏","code":"tag","selected":0},{"id":6968,"title":"运动","code":"tag","selected":0},{"id":6970,"title":"旅行","code":"tag","selected":0},{"id":6973,"title":"独处","code":"tag","selected":0}]},{"id":15,"title":"情感","alias":"tag","tags":[{"id":6974,"title":"放松","code":"tag","selected":0},{"id":6975,"title":"快乐","code":"tag","selected":0},{"id":6976,"title":"伤感","code":"tag","selected":0},{"id":6977,"title":"治愈","code":"tag","selected":0},{"id":6978,"title":"安静","code":"tag","selected":0},{"id":6979,"title":"怀旧","code":"tag","selected":0},{"id":6980,"title":"兴奋","code":"tag","selected":0},{"id":6981,"title":"浪漫","code":"tag","selected":0}]},{"id":16,"title":"主题","alias":"tag","tags":[{"id":6982,"title":"ACG","code":"tag","selected":0},{"id":6983,"title":"国风","code":"tag","selected":0},{"id":6984,"title":"VC","code":"tag","selected":0},{"id":6985,"title":"广播剧","code":"tag","selected":0},{"id":6986,"title":"三次元","code":"tag","selected":0}]},{"id":"","title":"","alias":"custom","tags":[]}]
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
         * id : 14
         * title : 场景
         * alias : tag
         * tags : [{"id":6957,"title":"夜晚","code":"tag","selected":0},{"id":6958,"title":"学习","code":"tag","selected":0},{"id":6960,"title":"清晨","code":"tag","selected":0},{"id":6962,"title":"工作","code":"tag","selected":0},{"id":6964,"title":"休息","code":"tag","selected":0},{"id":6965,"title":"游戏","code":"tag","selected":0},{"id":6968,"title":"运动","code":"tag","selected":0},{"id":6970,"title":"旅行","code":"tag","selected":0},{"id":6973,"title":"独处","code":"tag","selected":0}]
         */

        private int id;
        private String title;
        private String alias;
        private List<TagsBean> tags;

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

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class TagsBean {
            /**
             * id : 6957
             * title : 夜晚
             * code : tag
             * selected : 0
             */

            private int id;
            private String title;
            private String code;
            private int selected;

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

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }
        }
    }
}

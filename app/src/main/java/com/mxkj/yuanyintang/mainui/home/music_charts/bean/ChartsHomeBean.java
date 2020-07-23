package com.mxkj.yuanyintang.mainui.home.music_charts.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zuixia on 2018/4/19.
 */

public class ChartsHomeBean implements Serializable{


    /**
     * code : 200
     * msg : success
     * data : [{"id":4,"title":"古风","imgpic":"bf619d9f6694960296d3135082ed7653d9f3af9c","type":"2","border_bg_color":"FFFFFF","icon":"bf619d9f6694960296d3135082ed7653d9f3af9c","imgpic_link":"http://api.demo.com//image/bf619d9f6694960296d3135082ed7653d9f3af9c/1","imgpic_info":{"ext ":"","link":" http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"},"icon_link":"http://api.demo.com//image/bf619d9f6694960296d3135082ed7653d9f3af9c/1","icon_info":""}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 4
         * title : 古风
         * imgpic : bf619d9f6694960296d3135082ed7653d9f3af9c
         * type : 2
         * border_bg_color : FFFFFF
         * icon : bf619d9f6694960296d3135082ed7653d9f3af9c
         * imgpic_link : http://api.demo.com//image/bf619d9f6694960296d3135082ed7653d9f3af9c/1
         * imgpic_info : {"ext ":"","link":" http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3"}
         * icon_link : http://api.demo.com//image/bf619d9f6694960296d3135082ed7653d9f3af9c/1
         * icon_info :
         */




        private int id;
        private String title;
        private String imgpic;
        private int type;
        private String border_bg_color;
        private String icon;
        private String imgpic_link;
        private ImgpicInfoBean imgpic_info;
        private String icon_link;
        private String sub_title;
        private String share_title;
        private String share_content;
        private String share_icon;
        /**
         * toggle_class : {"id":14,"title":"贡献榜单","type":3}
         */

        private DataBean toggle_class;

//        private String icon_info;

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

        public String getImgpic() {
            return imgpic;
        }

        public void setImgpic(String imgpic) {
            this.imgpic = imgpic;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getBorder_bg_color() {
            return border_bg_color;
        }

        public void setBorder_bg_color(String border_bg_color) {
            this.border_bg_color = border_bg_color;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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

        public String getIcon_link() {
            return icon_link;
        }

        public void setIcon_link(String icon_link) {
            this.icon_link = icon_link;
        }

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getShare_content() {
            return share_content;
        }

        public void setShare_content(String share_content) {
            this.share_content = share_content;
        }

        public String getShare_icon() {
            return share_icon;
        }

        public void setShare_icon(String share_icon) {
            this.share_icon = share_icon;
        }

        public DataBean getToggle_class() {
            return toggle_class;
        }

        public void setToggle_class(DataBean toggle_class) {
            this.toggle_class = toggle_class;
        }

        public static class ImgpicInfoBean implements Serializable{
            /**
             * ext  :
             * link :  http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
             */

            private String ext;
            private String link;

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
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

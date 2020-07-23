package com.mxkj.yuanyintang.mainui.home.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zuixia on 2018/5/18.
 */

public class MemberGiftListBean {


    /**
     * code : 200
     * msg : success
     * data : {"counts":28,"data_list":[{"id":50781,"nickname":"lwqeoowq26","head":"c7adcb987e5224301258c6f7efb2d53e","sex":1,"is_music":0,"operation_id":50781,"sum":13000,"counts":12,"imglist":"0e60393614c18b2003557d08622139c8,d353a80163d3196287c44fb291931288","head_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1","head_info":{"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1"},"counts_text":12,"imglist_link":["http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","http://api.demo.com//image/d353a80163d3196287c44fb291931288/1"],"imglist_info":[{"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","md5":"0e60393614c18b2003557d08622139c8"},{"ext":"jpg","w":"748","h":"728","size":"83161","is_long":"0","link":"http://api.demo.com//image/d353a80163d3196287c44fb291931288/1","md5":"d353a80163d3196287c44fb291931288"}]}],"counts_text":28,"profit_billboard_config":{"billboard_type":4,"class_id":13,"type":4}}
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
         * counts : 28
         * data_list : [{"id":50781,"nickname":"lwqeoowq26","head":"c7adcb987e5224301258c6f7efb2d53e","sex":1,"is_music":0,"operation_id":50781,"sum":13000,"counts":12,"imglist":"0e60393614c18b2003557d08622139c8,d353a80163d3196287c44fb291931288","head_link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1","head_info":{"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1"},"counts_text":12,"imglist_link":["http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","http://api.demo.com//image/d353a80163d3196287c44fb291931288/1"],"imglist_info":[{"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","md5":"0e60393614c18b2003557d08622139c8"},{"ext":"jpg","w":"748","h":"728","size":"83161","is_long":"0","link":"http://api.demo.com//image/d353a80163d3196287c44fb291931288/1","md5":"d353a80163d3196287c44fb291931288"}]}]
         * counts_text : 28
         * profit_billboard_config : {"billboard_type":4,"class_id":13,"type":4}
         */

        private int counts;
        private int counts_text;
        private ProfitBillboardConfigBean profit_billboard_config;
        private List<DataListBean> data_list;

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public int getCounts_text() {
            return counts_text;
        }

        public void setCounts_text(int counts_text) {
            this.counts_text = counts_text;
        }

        public ProfitBillboardConfigBean getProfit_billboard_config() {
            return profit_billboard_config;
        }

        public void setProfit_billboard_config(ProfitBillboardConfigBean profit_billboard_config) {
            this.profit_billboard_config = profit_billboard_config;
        }

        public List<DataListBean> getData_list() {
            return data_list;
        }

        public void setData_list(List<DataListBean> data_list) {
            this.data_list = data_list;
        }

        public static class ProfitBillboardConfigBean implements Serializable{
            /**
             * billboard_type : 4
             * class_id : 13
             * type : 4
             */

            private int billboard_type;
            private int class_id;
            private int type;
            /**
             * toggle_class : {"type":3,"id":14,"title":"贡献榜单"}
             */

            private ToggleClassBean toggle_class;


            public int getBillboard_type() {
                return billboard_type;
            }

            public void setBillboard_type(int billboard_type) {
                this.billboard_type = billboard_type;
            }

            public int getClass_id() {
                return class_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public ToggleClassBean getToggle_class() {
                return toggle_class;
            }

            public void setToggle_class(ToggleClassBean toggle_class) {
                this.toggle_class = toggle_class;
            }

            public static class ToggleClassBean implements Serializable{
                /**
                 * type : 3
                 * id : 14
                 * title : 贡献榜单
                 */

                @SerializedName("type")
                private int typeX;
                private int id;
                private String title;

                public int getTypeX() {
                    return typeX;
                }

                public void setTypeX(int typeX) {
                    this.typeX = typeX;
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
            }
        }

        public static class DataListBean {
            /**
             * id : 50781
             * nickname : lwqeoowq26
             * head : c7adcb987e5224301258c6f7efb2d53e
             * sex : 1
             * is_music : 0
             * operation_id : 50781
             * sum : 13000
             * counts : 12
             * imglist : 0e60393614c18b2003557d08622139c8,d353a80163d3196287c44fb291931288
             * head_link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1
             * head_info : {"ext":"jpg","w":"1080","h":"1080","size":"330492","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1"}
             * counts_text : 12
             * imglist_link : ["http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","http://api.demo.com//image/d353a80163d3196287c44fb291931288/1"]
             * imglist_info : [{"ext":"png","w":"300","h":"300","size":"79407","is_long":"0","link":"http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","md5":"0e60393614c18b2003557d08622139c8"},{"ext":"jpg","w":"748","h":"728","size":"83161","is_long":"0","link":"http://api.demo.com//image/d353a80163d3196287c44fb291931288/1","md5":"d353a80163d3196287c44fb291931288"}]
             */

            private int id;
            private int is_self;
            private String nickname;
            private String head;
            private int sex;
            private int is_music;
            private int operation_id;
            private int sum;
            private int counts;
            private String imglist;
            private String head_link;
            private HeadInfoBean head_info;
            private int counts_text;
            private List<String> imglist_link;
            private List<ImglistInfoBean> imglist_info;

            public int getIs_self() {
                return is_self;
            }

            public void setIs_self(int is_self) {
                this.is_self = is_self;
            }

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

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public int getIs_music() {
                return is_music;
            }

            public void setIs_music(int is_music) {
                this.is_music = is_music;
            }

            public int getOperation_id() {
                return operation_id;
            }

            public void setOperation_id(int operation_id) {
                this.operation_id = operation_id;
            }

            public int getSum() {
                return sum;
            }

            public void setSum(int sum) {
                this.sum = sum;
            }

            public int getCounts() {
                return counts;
            }

            public void setCounts(int counts) {
                this.counts = counts;
            }

            public String getImglist() {
                return imglist;
            }

            public void setImglist(String imglist) {
                this.imglist = imglist;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public HeadInfoBean getHead_info() {
                return head_info;
            }

            public void setHead_info(HeadInfoBean head_info) {
                this.head_info = head_info;
            }

            public int getCounts_text() {
                return counts_text;
            }

            public void setCounts_text(int counts_text) {
                this.counts_text = counts_text;
            }

            public List<String> getImglist_link() {
                return imglist_link;
            }

            public void setImglist_link(List<String> imglist_link) {
                this.imglist_link = imglist_link;
            }

            public List<ImglistInfoBean> getImglist_info() {
                return imglist_info;
            }

            public void setImglist_info(List<ImglistInfoBean> imglist_info) {
                this.imglist_info = imglist_info;
            }

            public static class HeadInfoBean {
                /**
                 * ext : jpg
                 * w : 1080
                 * h : 1080
                 * size : 330492
                 * is_long : 0
                 * md5 : c7adcb987e5224301258c6f7efb2d53e
                 * link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1
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

            public static class ImglistInfoBean {
                /**
                 * ext : png
                 * w : 300
                 * h : 300
                 * size : 79407
                 * is_long : 0
                 * link : http://api.demo.com//image/0e60393614c18b2003557d08622139c8/1
                 * md5 : 0e60393614c18b2003557d08622139c8
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
        }
    }
}

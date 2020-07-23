package com.mxkj.yuanyintang.mainui.pond.bean;

import java.util.List;

public class PondTagListBean {


    /**
     * code : 200
     * msg : success
     * data : [{"id":314,"title":"rrrr","pid":0,"head":"3714fbd163b2e5c32bdb752fd92d972f","tag":[{"id":317,"title":"rrrr111","pid":314,"head":"60eb65a1cdfcc6737ffac65cbc680cdc","head_link":"http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1","head_info":{"ext":"","w":"","h":"","size":"10306","is_long":"0","md5":"60eb65a1cdfcc6737ffac65cbc680cdc","link":"http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1"}}],"head_link":"http://api.demo.com//image/3714fbd163b2e5c32bdb752fd92d972f/1","head_info":{"ext":"","w":"","h":"","size":"24993","is_long":"0","md5":"3714fbd163b2e5c32bdb752fd92d972f","link":"http://api.demo.com//image/3714fbd163b2e5c32bdb752fd92d972f/1"}},{"id":256,"title":"测试测试","pid":0,"head":"0a54a01adfceefd69003afd98e93cce73df60ab4","head_link":"http://api.demo.com//image/0a54a01adfceefd69003afd98e93cce73df60ab4/1","head_info":""},{"id":222,"title":"88","pid":0,"head":""},{"id":3,"title":"小花","pid":0,"head":""},{"id":2,"title":"了悟","pid":0,"head":""},{"id":1,"title":"满汉","pid":0,"head":"","tag":[{"id":316,"title":"满汉111","pid":1,"head":"","head_link":"","head_info":""},{"id":315,"title":"标签","pid":1,"head":"ef9e2cf5a3c0e540f3ea1df6108cba30","head_link":"http://api.demo.com//image/ef9e2cf5a3c0e540f3ea1df6108cba30/1","head_info":{"ext":"","w":"","h":"","size":"23215","is_long":"0","md5":"ef9e2cf5a3c0e540f3ea1df6108cba30","link":"http://api.demo.com//image/ef9e2cf5a3c0e540f3ea1df6108cba30/1"}}]}]
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
         * id : 314
         * title : rrrr
         * pid : 0
         * head : 3714fbd163b2e5c32bdb752fd92d972f
         * tag : [{"id":317,"title":"rrrr111","pid":314,"head":"60eb65a1cdfcc6737ffac65cbc680cdc","head_link":"http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1","head_info":{"ext":"","w":"","h":"","size":"10306","is_long":"0","md5":"60eb65a1cdfcc6737ffac65cbc680cdc","link":"http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1"}}]
         * head_link : http://api.demo.com//image/3714fbd163b2e5c32bdb752fd92d972f/1
         * head_info : {"ext":"","w":"","h":"","size":"24993","is_long":"0","md5":"3714fbd163b2e5c32bdb752fd92d972f","link":"http://api.demo.com//image/3714fbd163b2e5c32bdb752fd92d972f/1"}
         */

        private int id;
        private String title;
        private int pid;
        private String head;
        private String head_link;
        private HeadInfoBean head_info;
        private List<TagBean> tag;

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

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
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

        public List<TagBean> getTag() {
            return tag;
        }

        public void setTag(List<TagBean> tag) {
            this.tag = tag;
        }

        public static class HeadInfoBean {
            /**
             * ext :
             * w :
             * h :
             * size : 24993
             * is_long : 0
             * md5 : 3714fbd163b2e5c32bdb752fd92d972f
             * link : http://api.demo.com//image/3714fbd163b2e5c32bdb752fd92d972f/1
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

        public static class TagBean {
            /**
             * id : 317
             * title : rrrr111
             * pid : 314
             * head : 60eb65a1cdfcc6737ffac65cbc680cdc
             * head_link : http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1
             * head_info : {"ext":"","w":"","h":"","size":"10306","is_long":"0","md5":"60eb65a1cdfcc6737ffac65cbc680cdc","link":"http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1"}
             */

            private int id;
            private String title;
            private int pid;
            private String head;
            private String head_link;
            private HeadInfoBeanX head_info;

            @Override
            public String toString() {
                return "TagBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", pid=" + pid +
                        ", head='" + head + '\'' +
                        ", head_link='" + head_link + '\'' +
                        ", head_info=" + head_info +
                        '}';
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

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public HeadInfoBeanX getHead_info() {
                return head_info;
            }

            public void setHead_info(HeadInfoBeanX head_info) {
                this.head_info = head_info;
            }

            public static class HeadInfoBeanX {
                /**
                 * ext :
                 * w :
                 * h :
                 * size : 10306
                 * is_long : 0
                 * md5 : 60eb65a1cdfcc6737ffac65cbc680cdc
                 * link : http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1
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
        }
    }
}

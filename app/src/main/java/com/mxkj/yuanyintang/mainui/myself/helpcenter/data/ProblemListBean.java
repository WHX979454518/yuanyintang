package com.mxkj.yuanyintang.mainui.myself.helpcenter.data;

import java.io.Serializable;
import java.util.List;

public class ProblemListBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : [{"id":1,"title":"快速入门","imgpic":"f0aaf41bc618a1be7fbcd91d364e479a","pid":3,"code":"kuaisurumen","imgpic_link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3","imgpic_info":{"ext":"","w":"","h":"","size":"2573","is_long":"0","md5":"f0aaf41bc618a1be7fbcd91d364e479a","link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3"}},{"id":5,"title":"导航一","imgpic":"f0aaf41bc618a1be7fbcd91d364e479a","pid":0,"code":"","imgpic_link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3","imgpic_info":{"ext":"","w":"","h":"","size":"2573","is_long":"0","md5":"f0aaf41bc618a1be7fbcd91d364e479a","link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3"}},{"id":6,"title":"导航二","imgpic":"f0aaf41bc618a1be7fbcd91d364e479a","pid":0,"code":"","imgpic_link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3","imgpic_info":{"ext":"","w":"","h":"","size":"2573","is_long":"0","md5":"f0aaf41bc618a1be7fbcd91d364e479a","link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3"}},{"id":8,"title":"导航二级1","imgpic":"f0aaf41bc618a1be7fbcd91d364e479a","pid":5,"code":"","imgpic_link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3","imgpic_info":{"ext":"","w":"","h":"","size":"2573","is_long":"0","md5":"f0aaf41bc618a1be7fbcd91d364e479a","link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3"}},{"id":12,"title":"导航⑤","imgpic":"f0aaf41bc618a1be7fbcd91d364e479a","pid":0,"code":"","imgpic_link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3","imgpic_info":{"ext":"","w":"","h":"","size":"2573","is_long":"0","md5":"f0aaf41bc618a1be7fbcd91d364e479a","link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3"}},{"id":13,"title":"导航6","imgpic":"f0aaf41bc618a1be7fbcd91d364e479a","pid":0,"code":"","imgpic_link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3","imgpic_info":{"ext":"","w":"","h":"","size":"2573","is_long":"0","md5":"f0aaf41bc618a1be7fbcd91d364e479a","link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3"}},{"id":14,"title":"导航6导航6","imgpic":"f0aaf41bc618a1be7fbcd91d364e479a","pid":0,"code":"","imgpic_link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3","imgpic_info":{"ext":"","w":"","h":"","size":"2573","is_long":"0","md5":"f0aaf41bc618a1be7fbcd91d364e479a","link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3"}}]
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
         * id : 1
         * title : 快速入门
         * imgpic : f0aaf41bc618a1be7fbcd91d364e479a
         * pid : 3
         * code : kuaisurumen
         * imgpic_link : http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3
         * imgpic_info : {"ext":"","w":"","h":"","size":"2573","is_long":"0","md5":"f0aaf41bc618a1be7fbcd91d364e479a","link":"http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3"}
         */

        private int id;
        private String title;
        private String imgpic;
        private int pid;
        private String code;
        private String imgpic_link;
        private ImgpicInfoBean imgpic_info;

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

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public static class ImgpicInfoBean {
            /**
             * ext :
             * w :
             * h :
             * size : 2573
             * is_long : 0
             * md5 : f0aaf41bc618a1be7fbcd91d364e479a
             * link : http://testapi.imxkj.com//image/f0aaf41bc618a1be7fbcd91d364e479a/3
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

package com.mxkj.yuanyintang.mainui.myself.bean;

import java.io.Serializable;
import java.util.List;


public class MySongListBean implements Serializable{


    /**
     * code : 200
     * msg : success
     * data : {"count":{"song":10,"scollection":3},"data":[{"id":2,"title":"123213","counts":0,"total":1,"uid":523,"imgpic":123123123213213,"nickname":"??背立?","imgpic_link":"http://yyt.demo.com/image/123123123213213"}]}
     */

    private int code;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX implements Serializable{
        /**
         * count : {"song":10,"scollection":3}
         * data : [{"id":2,"title":"123213","counts":0,"total":1,"uid":523,"imgpic":123123123213213,"nickname":"??背立?","imgpic_link":"http://yyt.demo.com/image/123123123213213"}]
         */

        private CountBean count;
        private List<DataBean> data;

        public CountBean getCount() {
            return count;
        }

        public void setCount(CountBean count) {
            this.count = count;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class CountBean implements Serializable{
            /**
             * song : 10
             * scollection : 3
             */

            private int song;
            private int scollection;

            public int getSong() {
                return song;
            }

            public void setSong(int song) {
                this.song = song;
            }

            public int getScollection() {
                return scollection;
            }

            public void setScollection(int scollection) {
                this.scollection = scollection;
            }
        }

        public static class DataBean implements Serializable{
            /**
             * id : 2
             * title : 123213
             * counts : 0
             * total : 1
             * uid : 523
             * imgpic : 123123123213213
             * nickname : ??背立?
             * imgpic_link : http://yyt.demo.com/image/123123123213213
             */

            private int id;
            private String title;
            private int counts;
            private int total;
            private int uid;
            private String imgpic;
            private String nickname;
//            private String imgpic_link;
            private Boolean check = false;


            private ImgpicInfoBean imgpic_info;
            public ImgpicInfoBean getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(ImgpicInfoBean imgpic_info) {
                this.imgpic_info = imgpic_info;
            }
            public static class ImgpicInfoBean implements Serializable {
                /**
                 * ext :
                 * w :
                 * h :
                 * size : 330492
                 * is_long : 0
                 * md5 : c7adcb987e5224301258c6f7efb2d53e
                 * link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
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



            public Boolean getCheck() {
                return check;
            }

            public void setCheck(Boolean check) {
                this.check = check;
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

            public int getCounts() {
                return counts;
            }

            public void setCounts(int counts) {
                this.counts = counts;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getImgpic() {
                return imgpic;
            }

            public void setImgpic(String imgpic) {
                this.imgpic = imgpic;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }
        }
    }
}

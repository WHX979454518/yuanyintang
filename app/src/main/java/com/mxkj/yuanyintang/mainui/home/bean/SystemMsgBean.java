package com.mxkj.yuanyintang.mainui.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/12/25.
 */

public class SystemMsgBean {

    /**
     * code : 200
     * msg : success
     * data : [{"id":8,"title":"源音塘次元音乐会","imgpic":"80a92caeac808f0c793de9443c41677c54ce3446","create_time":1511160041,"state":-1,"type":0,"url":"https://www.yuanyintang.com/ciyuan2017","start_time":1511591400,"end_time":1511609400,"imgpic_link":"http://testapi.yuanyintang.com/image/80a92caeac808f0c793de9443c41677c54ce3446"},{"id":7,"title":"第十四届 \u201c牛牛仔\u201d杯外语歌王大赛","imgpic":"195f85d69f9167163f5cf408f1b6f069ce11d5b9","create_time":1510023276,"state":-1,"type":0,"url":"https://www.yuanyintang.com/topic/detail?id=2502","start_time":1509465600,"end_time":1511236800,"imgpic_link":"http://testapi.yuanyintang.com/image/195f85d69f9167163f5cf408f1b6f069ce11d5b9"},{"id":6,"title":"筑梦之路.源音塘校园行音乐大赛","imgpic":"d3819cefc69e0126edd2330584afa11ad2e3876b","create_time":1509433482,"state":-1,"type":0,"url":"https://www.yuanyintang.com/2017school/vote","start_time":1509379200,"end_time":1511355600,"imgpic_link":"http://testapi.yuanyintang.com/image/d3819cefc69e0126edd2330584afa11ad2e3876b"},{"id":4,"title":"幻宠大陆主题曲翻唱大赛9月13日-10月17日","imgpic":"3e735df0953bd14d12af69162c0a2ca33c7a799f","create_time":1505128790,"state":-1,"type":0,"url":"https://www.yuanyintang.com/pets2017","start_time":1505232000,"end_time":1508255999,"imgpic_link":"http://testapi.yuanyintang.com/image/3e735df0953bd14d12af69162c0a2ca33c7a799f"},{"id":3,"title":"源音塘秋日祭音乐会  9月4日-10月11日","imgpic":"b0684344ee2d186fd08c60eda25e1749fccb5229","create_time":1504165663,"state":-1,"type":0,"url":"https://www.yuanyintang.com/autumnvote2017","start_time":1504504800,"end_time":1507737599,"imgpic_link":"http://testapi.yuanyintang.com/image/b0684344ee2d186fd08c60eda25e1749fccb5229"},{"id":2,"title":"源音塘首届夏日音乐会","imgpic":"f5b0d4f8c027d53e07e5976161cabd9925a7b96b","create_time":1504016440,"state":-1,"type":0,"url":"http://www.yuanyintang.com/bulletin?id=122","start_time":1494259200,"end_time":1497974399,"imgpic_link":"http://testapi.yuanyintang.com/image/f5b0d4f8c027d53e07e5976161cabd9925a7b96b"},{"id":1,"title":"源音塘音乐英雄评选赛粉丝福利抽奖","imgpic":"9218827f56c4c91419b8a8d5398fa841819582b6","create_time":1504016397,"state":-1,"type":0,"url":"http://www.yuanyintang.com/bulletin?id=121","start_time":1489939200,"end_time":1491494399,"imgpic_link":"http://testapi.yuanyintang.com/image/9218827f56c4c91419b8a8d5398fa841819582b6"}]
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
         * id : 8
         * title : 源音塘次元音乐会
         * imgpic : 80a92caeac808f0c793de9443c41677c54ce3446
         * create_time : 1511160041
         * state : -1
         * type : 0
         * url : https://www.yuanyintang.com/ciyuan2017
         * start_time : 1511591400
         * end_time : 1511609400
         * imgpic_link : http://testapi.yuanyintang.com/image/80a92caeac808f0c793de9443c41677c54ce3446
         */

        private int id;
        private String title;
        private String imgpic;
        private int create_time;
        private int state;
        private int type;
        private String url;
        private int start_time;
        private int end_time;
//        private String imgpic_link;


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

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }
//
//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }
    }
}

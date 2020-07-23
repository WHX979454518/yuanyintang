package com.mxkj.yuanyintang.widget.emoji;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/9.
 */

public class FaceBean {

    /**
     * code : 200
     * msg : success
     * data : [{"id":3,"emoji":"[:mdb]","title":"","imgpic":"1ff29d183f7535598a634c111350c0b7b9662de1","imgpic_link":"http://dev.yytnew.com/image/1ff29d183f7535598a634c111350c0b7b9662de1"}]
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
         * id : 3
         * emoji : [:mdb]
         * title :
         * imgpic : 1ff29d183f7535598a634c111350c0b7b9662de1
         * imgpic_link : http://dev.yytnew.com/image/1ff29d183f7535598a634c111350c0b7b9662de1
         */

        private int id;
        private String emoji;
        private String title;
        private String imgpic;
        private String imgpic_link;


//        private ImgpicInfoBean imgpic_info;
//        public ImgpicInfoBean getImgpic_info() {
//            return imgpic_info;
//        }
//
//        public void setImgpic_info(ImgpicInfoBean imgpic_info) {
//            this.imgpic_info = imgpic_info;
//        }
//        public static class ImgpicInfoBean implements Serializable {
//            /**
//             * ext :
//             * w :
//             * h :
//             * size : 330492
//             * is_long : 0
//             * md5 : c7adcb987e5224301258c6f7efb2d53e
//             * link : http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/3
//             */
//
//            private String ext;
//            private String w;
//            private String h;
//            private String size;
//            private String is_long;
//            private String md5;
//            private String link;
//
//            public String getExt() {
//                return ext;
//            }
//
//            public void setExt(String ext) {
//                this.ext = ext;
//            }
//
//            public String getW() {
//                return w;
//            }
//
//            public void setW(String w) {
//                this.w = w;
//            }
//
//            public String getH() {
//                return h;
//            }
//
//            public void setH(String h) {
//                this.h = h;
//            }
//
//            public String getSize() {
//                return size;
//            }
//
//            public void setSize(String size) {
//                this.size = size;
//            }
//
//            public String getIs_long() {
//                return is_long;
//            }
//
//            public void setIs_long(String is_long) {
//                this.is_long = is_long;
//            }
//
//            public String getMd5() {
//                return md5;
//            }
//
//            public void setMd5(String md5) {
//                this.md5 = md5;
//            }
//
//            public String getLink() {
//                return link;
//            }
//
//            public void setLink(String link) {
//                this.link = link;
//            }
//        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmoji() {
            return emoji;
        }

        public void setEmoji(String emoji) {
            this.emoji = emoji;
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

        public String getImgpic_link() {
            return imgpic_link;
        }

        public void setImgpic_link(String imgpic_link) {
            this.imgpic_link = imgpic_link;
        }
    }
}

package com.mxkj.yuanyintang.mainui.search;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;
public class SheetBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : [{"nickname":"泡泡Oo","id":3,"uid":2,"title":"132131","imgpic":"123123123213","status":1,"create_time":1490786630,"is_rec":1,"sort":11,"counts":0,"tag":"","remark":"sdfdsfsd","collection":0,"agree":0,"share":0,"imgpic_link":"http://yyt.demo.com/image/123123123213"},{"nickname":"泡泡Oo","id":1,"uid":2,"title":"123123","imgpic":"423432423423423","status":1,"create_time":1490786630,"is_rec":1,"sort":10,"counts":0,"tag":"","remark":"大大的","collection":0,"agree":0,"share":0,"imgpic_link":"http://yyt.demo.com/image/423432423423423"},{"nickname":"泡泡Oo","id":2,"uid":2,"title":"123213","imgpic":"123123123213213","status":1,"create_time":1490786630,"is_rec":1,"sort":10,"counts":0,"tag":"","remark":"sfsdfsd","collection":0,"agree":0,"share":0,"imgpic_link":"http://yyt.demo.com/image/123123123213213"},{"nickname":"泡泡Oo","id":4,"uid":2,"title":"adasda","imgpic":"","status":1,"create_time":1499064312,"is_rec":0,"sort":1,"counts":0,"tag":"","remark":"sdfds","collection":0,"agree":0,"share":0},{"nickname":"泡泡Oo","id":6,"uid":2,"title":"adasdaasda","imgpic":"","status":1,"create_time":1499064485,"is_rec":0,"sort":1,"counts":0,"tag":"","remark":"df","collection":0,"agree":0,"share":0},{"nickname":"泡泡Oo","id":7,"uid":2,"title":"新建","imgpic":"124124242343543534","status":1,"create_time":1499070100,"is_rec":0,"sort":1,"counts":0,"tag":"","remark":"","collection":0,"agree":0,"share":0,"imgpic_link":"http://yyt.demo.com/image/124124242343543534"},{"nickname":"泡泡Oo","id":9,"uid":2,"title":"新建歌单333","imgpic":"","status":1,"create_time":1499076443,"is_rec":0,"sort":1,"counts":0,"tag":"","remark":"","collection":0,"agree":0,"share":0},{"nickname":"泡泡Oo","id":10,"uid":2,"title":"新建歌","imgpic":"","status":1,"create_time":1499076523,"is_rec":0,"sort":1,"counts":0,"tag":"","remark":"asdhajkshdksahdkashdkashdkah","collection":0,"agree":0,"share":0}]
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

    public static class DataBean implements Serializable, MultiItemEntity {
        /**
         * nickname : 泡泡Oo
         * id : 3
         * uid : 2
         * title : 132131
         * imgpic : 123123123213
         * status : 1
         * create_time : 1490786630
         * is_rec : 1
         * sort : 11
         * counts : 0
         * tag :
         * remark : sdfdsfsd
         * collection : 0
         * agree : 0
         * share : 0
         * imgpic_link : http://yyt.demo.com/image/123123123213
         */



        public boolean isLike=false;//判断是不是用户收藏

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isChecked=false;


        private String nickname;
        private int id;
        private int uid;
        private String title;
        private String imgpic;
        private int status;
        private int create_time;
        private int is_rec;
        private int sort;
        private int counts;
        private String tag;
        private String remark;
        private int collection;
        private int agree;
        private int share;
//        private String imgpic_link;
        private int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

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


        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getIs_rec() {
            return is_rec;
        }

        public void setIs_rec(int is_rec) {
            this.is_rec = is_rec;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
        }

        public int getAgree() {
            return agree;
        }

        public void setAgree(int agree) {
            this.agree = agree;
        }

        public int getShare() {
            return share;
        }

        public void setShare(int share) {
            this.share = share;
        }

//        public String getImgpic_link() {
//            return imgpic_link;
//        }

//        public void setImgpic_link(String imgpic_link) {
//            this.imgpic_link = imgpic_link;
//        }

        @Override
        public int getItemType() {
            if (isLike==true){
                return 1;
            }else {
                return 0;
            }
        }
    }
}

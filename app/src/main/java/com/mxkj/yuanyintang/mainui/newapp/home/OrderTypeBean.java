package com.mxkj.yuanyintang.mainui.newapp.home;

import com.mxkj.yuanyintang.mainui.newapp.pond.PondImageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2018/8/8.
 */

public class OrderTypeBean implements Serializable {
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
        private List<WhereBean> where;
        private List<OrderBean> order;
        /**
         * cate_img : {"id":177,"title":"laichitasaiqx","alias":"where_topic","imgpic":"5b45d1eb1c7529a2032295528cc63669","url":"","item_type":1,"content":"上传图片：\r\n上传图片：\r\n上传图片：\r\n上传图片：\r\n上传图片：","place":"","code":"where_topic","to_code":"app","imgpic_link":"http://testapi.imxkj.com//image/5b45d1eb1c7529a2032295528cc63669/3","imgpic_info":{"ext":"","w":"","h":"","size":"20124","is_long":"0","md5":"5b45d1eb1c7529a2032295528cc63669","link":"http://testapi.imxkj.com//image/5b45d1eb1c7529a2032295528cc63669/3"}}
         */

        private CateImgBean cate_img;

        private List<BgImgBean> bg_img;

        public List<WhereBean> getWhere() {
            return where;
        }


        public List<BgImgBean> getBg_img() {
            return bg_img;
        }

        public void setBg_img(List<BgImgBean> bg_img) {
            this.bg_img = bg_img;
        }

        public void setWhere(List<WhereBean> where) {
            this.where = where;
        }

        public List<OrderBean> getOrder() {
            return order;
        }

        public void setOrder(List<OrderBean> order) {
            this.order = order;
        }

        public CateImgBean getCate_img() {
            return cate_img;
        }

        public void setCate_img(CateImgBean cate_img) {
            this.cate_img = cate_img;
        }

        public static class WhereBean {
            /**
             * id : 33
             * title : 情感
             * field : tag
             * list : [{"id":451,"title":"歌单情感2"},{"id":450,"title":"歌单情感1"}]
             */

            private int id;
            private String title;
            private String field;
            private boolean isChecked;
            private List<ListBean> list;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
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

            public WhereBean setTitle(String title) {
                this.title = title;
                return this;
            }

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * id : 451
                 * title : 歌单情感2
                 */

                private int id=0;
                private String title;
                private boolean isChecked=false;

                public boolean isChecked() {
                    return isChecked;
                }

                public void setChecked(boolean checked) {
                    isChecked = checked;
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

        public static class OrderBean {
            /**
             * field : id-desc
             * title : 默认排序
             */

            private String field;
            private String title;

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class CateImgBean {
            /**
             * id : 177
             * title : laichitasaiqx
             * alias : where_topic
             * imgpic : 5b45d1eb1c7529a2032295528cc63669
             * url :
             * item_type : 1
             * content : 上传图片：
             上传图片：
             上传图片：
             上传图片：
             上传图片：
             * place :
             * code : where_topic
             * to_code : app
             * imgpic_link : http://testapi.imxkj.com//image/5b45d1eb1c7529a2032295528cc63669/3
             * imgpic_info : {"ext":"","w":"","h":"","size":"20124","is_long":"0","md5":"5b45d1eb1c7529a2032295528cc63669","link":"http://testapi.imxkj.com//image/5b45d1eb1c7529a2032295528cc63669/3"}
             */

            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String place;
            private String code;
            private String to_code;
            private String imgpic_link;
            private ImgpicInfoBean imgpic_info;

            @Override
            public String toString() {
                return "CateImgBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", alias='" + alias + '\'' +
                        ", imgpic='" + imgpic + '\'' +
                        ", url='" + url + '\'' +
                        ", item_type=" + item_type +
                        ", content='" + content + '\'' +
                        ", place='" + place + '\'' +
                        ", code='" + code + '\'' +
                        ", to_code='" + to_code + '\'' +
                        ", imgpic_link='" + imgpic_link + '\'' +
                        ", imgpic_info=" + imgpic_info +
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

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getImgpic() {
                return imgpic;
            }

            public void setImgpic(String imgpic) {
                this.imgpic = imgpic;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getItem_type() {
                return item_type;
            }

            public void setItem_type(int item_type) {
                this.item_type = item_type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPlace() {
                return place;
            }

            public void setPlace(String place) {
                this.place = place;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getTo_code() {
                return to_code;
            }

            public void setTo_code(String to_code) {
                this.to_code = to_code;
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
                 * size : 20124
                 * is_long : 0
                 * md5 : 5b45d1eb1c7529a2032295528cc63669
                 * link : http://testapi.imxkj.com//image/5b45d1eb1c7529a2032295528cc63669/3
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

        public static class BgImgBean {
            /**
             * id : 177
             * title : laichitasaiqx
             * alias : where_topic
             * imgpic : 5b45d1eb1c7529a2032295528cc63669
             * url :
             * item_type : 1
             * content : 上传图片：
             上传图片：
             上传图片：
             上传图片：
             上传图片：
             * place :
             * code : where_topic
             * to_code : app
             * imgpic_link : http://testapi.imxkj.com//image/5b45d1eb1c7529a2032295528cc63669/3
             * imgpic_info : {"ext":"","w":"","h":"","size":"20124","is_long":"0","md5":"5b45d1eb1c7529a2032295528cc63669","link":"http://testapi.imxkj.com//image/5b45d1eb1c7529a2032295528cc63669/3"}
             */

            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String place;
            private String code;
            private String to_code;
            private String imgpic_link;
            private ImgpicInfoBean imgpic_info;

            @Override
            public String toString() {
                return "BgImgBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", alias='" + alias + '\'' +
                        ", imgpic='" + imgpic + '\'' +
                        ", url='" + url + '\'' +
                        ", item_type=" + item_type +
                        ", content='" + content + '\'' +
                        ", place='" + place + '\'' +
                        ", code='" + code + '\'' +
                        ", to_code='" + to_code + '\'' +
                        ", imgpic_link='" + imgpic_link + '\'' +
                        ", imgpic_info=" + imgpic_info +
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

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getImgpic() {
                return imgpic;
            }

            public void setImgpic(String imgpic) {
                this.imgpic = imgpic;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getItem_type() {
                return item_type;
            }

            public void setItem_type(int item_type) {
                this.item_type = item_type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPlace() {
                return place;
            }

            public void setPlace(String place) {
                this.place = place;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getTo_code() {
                return to_code;
            }

            public void setTo_code(String to_code) {
                this.to_code = to_code;
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
                 * size : 20124
                 * is_long : 0
                 * md5 : 5b45d1eb1c7529a2032295528cc63669
                 * link : http://testapi.imxkj.com//image/5b45d1eb1c7529a2032295528cc63669/3
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

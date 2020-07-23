package com.mxkj.yuanyintang.mainui.newapp.pond;

import java.io.Serializable;
import java.util.List;

public class PondImageBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"cate_img":{"id":520,"title":"附近有小可爱出现，快来勾搭","alias":"near","imgpic":"16ae8daf983e603e93d2d1025ee136b5","url":"","item_type":1,"content":"别害羞，我只是想认识可爱的你","place":"池塘","code":"where_topic","to_code":"app","imgpic_link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3","imgpic_info":{"ext":"","w":"","h":"","size":"109406","is_long":"0","md5":"16ae8daf983e603e93d2d1025ee136b5","link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3"}},"bg_img":[{"id":520,"title":"附近有小可爱出现，快来勾搭","alias":"near","imgpic":"16ae8daf983e603e93d2d1025ee136b5","url":"","item_type":1,"content":"别害羞，我只是想认识可爱的你","place":"池塘","code":"where_topic","to_code":"app","imgpic_link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3","imgpic_info":{"ext":"","w":"","h":"","size":"109406","is_long":"0","md5":"16ae8daf983e603e93d2d1025ee136b5","link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3"}},{"id":519,"title":"一见倾心念念不忘","alias":"follow","imgpic":"d6df1eb8233d9dd23839d481160e6e54","url":"","item_type":1,"content":"喜欢TA就要随时关注","place":"池塘","code":"where_topic","to_code":"app","imgpic_link":"https://api.yuanyintang.com/image/d6df1eb8233d9dd23839d481160e6e54/3","imgpic_info":{"ext":"","w":"","h":"","size":"106298","is_long":"0","md5":"d6df1eb8233d9dd23839d481160e6e54","link":"https://api.yuanyintang.com/image/d6df1eb8233d9dd23839d481160e6e54/3"}},{"id":418,"title":"围观不够嗨，快来与同好们畅所欲言","alias":"where_topic","imgpic":"afc3c1b7a08ae6771eb648256a75c3bd","url":"","item_type":1,"content":"围观不够嗨，快来与同好们畅所欲言\r\n每日快乐源泉，从灌水池塘开始","place":"池塘","code":"where_topic","to_code":"app","imgpic_link":"https://api.yuanyintang.com/image/afc3c1b7a08ae6771eb648256a75c3bd/3","imgpic_info":{"ext":"","w":"","h":"","size":"98971","is_long":"0","md5":"afc3c1b7a08ae6771eb648256a75c3bd","link":"https://api.yuanyintang.com/image/afc3c1b7a08ae6771eb648256a75c3bd/3"}}],"where":[{"id":0,"field":"tag_class","title":"热门","list":[{"id":0,"title":"推荐"},{"id":726,"title":"动漫"},{"id":725,"title":"招募"},{"id":722,"title":"音乐人"},{"id":705,"title":"游戏"},{"id":666,"title":"活动"},{"id":662,"title":"技能"}]}],"order":[{"field":"id-desc","title":"热门"},{"field":"_follow-desc","title":"关注"},{"field":"create_time-desc","title":"最新"},{"field":"thcount-desc","title":"评论最多"},{"field":"update_time-desc","title":"刚更新"}]}
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
         * cate_img : {"id":520,"title":"附近有小可爱出现，快来勾搭","alias":"near","imgpic":"16ae8daf983e603e93d2d1025ee136b5","url":"","item_type":1,"content":"别害羞，我只是想认识可爱的你","place":"池塘","code":"where_topic","to_code":"app","imgpic_link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3","imgpic_info":{"ext":"","w":"","h":"","size":"109406","is_long":"0","md5":"16ae8daf983e603e93d2d1025ee136b5","link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3"}}
         * bg_img : [{"id":520,"title":"附近有小可爱出现，快来勾搭","alias":"near","imgpic":"16ae8daf983e603e93d2d1025ee136b5","url":"","item_type":1,"content":"别害羞，我只是想认识可爱的你","place":"池塘","code":"where_topic","to_code":"app","imgpic_link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3","imgpic_info":{"ext":"","w":"","h":"","size":"109406","is_long":"0","md5":"16ae8daf983e603e93d2d1025ee136b5","link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3"}},{"id":519,"title":"一见倾心念念不忘","alias":"follow","imgpic":"d6df1eb8233d9dd23839d481160e6e54","url":"","item_type":1,"content":"喜欢TA就要随时关注","place":"池塘","code":"where_topic","to_code":"app","imgpic_link":"https://api.yuanyintang.com/image/d6df1eb8233d9dd23839d481160e6e54/3","imgpic_info":{"ext":"","w":"","h":"","size":"106298","is_long":"0","md5":"d6df1eb8233d9dd23839d481160e6e54","link":"https://api.yuanyintang.com/image/d6df1eb8233d9dd23839d481160e6e54/3"}},{"id":418,"title":"围观不够嗨，快来与同好们畅所欲言","alias":"where_topic","imgpic":"afc3c1b7a08ae6771eb648256a75c3bd","url":"","item_type":1,"content":"围观不够嗨，快来与同好们畅所欲言\r\n每日快乐源泉，从灌水池塘开始","place":"池塘","code":"where_topic","to_code":"app","imgpic_link":"https://api.yuanyintang.com/image/afc3c1b7a08ae6771eb648256a75c3bd/3","imgpic_info":{"ext":"","w":"","h":"","size":"98971","is_long":"0","md5":"afc3c1b7a08ae6771eb648256a75c3bd","link":"https://api.yuanyintang.com/image/afc3c1b7a08ae6771eb648256a75c3bd/3"}}]
         * where : [{"id":0,"field":"tag_class","title":"热门","list":[{"id":0,"title":"推荐"},{"id":726,"title":"动漫"},{"id":725,"title":"招募"},{"id":722,"title":"音乐人"},{"id":705,"title":"游戏"},{"id":666,"title":"活动"},{"id":662,"title":"技能"}]}]
         * order : [{"field":"id-desc","title":"热门"},{"field":"_follow-desc","title":"关注"},{"field":"create_time-desc","title":"最新"},{"field":"thcount-desc","title":"评论最多"},{"field":"update_time-desc","title":"刚更新"}]
         */

        private CateImgBean cate_img;
        private List<BgImgBean> bg_img;
        private List<WhereBean> where;
        private List<OrderBean> order;

        public CateImgBean getCate_img() {
            return cate_img;
        }

        public void setCate_img(CateImgBean cate_img) {
            this.cate_img = cate_img;
        }

        public List<BgImgBean> getBg_img() {
            return bg_img;
        }

        public void setBg_img(List<BgImgBean> bg_img) {
            this.bg_img = bg_img;
        }

        public List<WhereBean> getWhere() {
            return where;
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

        public static class CateImgBean {
            /**
             * id : 520
             * title : 附近有小可爱出现，快来勾搭
             * alias : near
             * imgpic : 16ae8daf983e603e93d2d1025ee136b5
             * url :
             * item_type : 1
             * content : 别害羞，我只是想认识可爱的你
             * place : 池塘
             * code : where_topic
             * to_code : app
             * imgpic_link : https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3
             * imgpic_info : {"ext":"","w":"","h":"","size":"109406","is_long":"0","md5":"16ae8daf983e603e93d2d1025ee136b5","link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3"}
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
                 * size : 109406
                 * is_long : 0
                 * md5 : 16ae8daf983e603e93d2d1025ee136b5
                 * link : https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3
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
             * id : 520
             * title : 附近有小可爱出现，快来勾搭
             * alias : near
             * imgpic : 16ae8daf983e603e93d2d1025ee136b5
             * url :
             * item_type : 1
             * content : 别害羞，我只是想认识可爱的你
             * place : 池塘
             * code : where_topic
             * to_code : app
             * imgpic_link : https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3
             * imgpic_info : {"ext":"","w":"","h":"","size":"109406","is_long":"0","md5":"16ae8daf983e603e93d2d1025ee136b5","link":"https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3"}
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
            private ImgpicInfoBeanX imgpic_info;

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

            public ImgpicInfoBeanX getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(ImgpicInfoBeanX imgpic_info) {
                this.imgpic_info = imgpic_info;
            }

            public static class ImgpicInfoBeanX {
                /**
                 * ext :
                 * w :
                 * h :
                 * size : 109406
                 * is_long : 0
                 * md5 : 16ae8daf983e603e93d2d1025ee136b5
                 * link : https://api.yuanyintang.com/image/16ae8daf983e603e93d2d1025ee136b5/3
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

        public static class WhereBean {
            /**
             * id : 0
             * field : tag_class
             * title : 热门
             * list : [{"id":0,"title":"推荐"},{"id":726,"title":"动漫"},{"id":725,"title":"招募"},{"id":722,"title":"音乐人"},{"id":705,"title":"游戏"},{"id":666,"title":"活动"},{"id":662,"title":"技能"}]
             */

            private int id;
            private String field;
            private String title;
            private List<ListBean> list;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

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

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * id : 0
                 * title : 推荐
                 */

                private int id;
                private String title;

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
             * title : 热门
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
    }
}

package com.mxkj.yuanyintang.mainui.pond.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/12/8.
 */

public class PondTagBanner implements Serializable{


    /**
     * code : 200
     * msg : 加载成功！
     * data : {"shuffling":[{"id":148,"title":"哈哈哈","alias":"112544","imgpic":"7faf99d402104c64f639ddc0e448ec1a","url":"2649","content":"","item_type":1,"imgpic_link":"http://testapi.yuanyintang.com//image/7faf99d402104c64f639ddc0e448ec1a"},{"id":150,"title":"1","alias":"1","imgpic":"e1e2e4c4ab933b61aa2121f62b550198","url":"","content":"","item_type":1,"imgpic_link":"http://testapi.yuanyintang.com//image/e1e2e4c4ab933b61aa2121f62b550198"}],"tag":{"tag":[{"id":280,"title":"国人首翻","head":"dd66a7047334339ca532197dba70ab72","times":3,"head_link":"http://testapi.yuanyintang.com//image/dd66a7047334339ca532197dba70ab72"},{"id":283,"title":"今日最佳神评论","head":"8f7216e11ed2c472b1501c9b41d4d8d7","times":9,"head_link":"http://testapi.yuanyintang.com//image/8f7216e11ed2c472b1501c9b41d4d8d7"},{"id":281,"title":"花间提壶","head":"7fbb2b494fecddd7ed23a559b599e3a6","times":2,"head_link":"http://testapi.yuanyintang.com//image/7fbb2b494fecddd7ed23a559b599e3a6"}],"tag_img":{"id":153,"imgpic":"a8b404777749dc9a6f00400b6a76af55","alias":"topic_img_tag","imgpic_link":"http://testapi.yuanyintang.com//image/a8b404777749dc9a6f00400b6a76af55"},"pond_img":{"id":154,"imgpic":"74c43f35f9106f2f0de6f1c5bfe7d7b5","alias":"topic_img_hot","imgpic_link":"http://testapi.yuanyintang.com//image/74c43f35f9106f2f0de6f1c5bfe7d7b5"}}}
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

    public static class DataBean implements Serializable{
        /**
         * shuffling : [{"id":148,"title":"哈哈哈","alias":"112544","imgpic":"7faf99d402104c64f639ddc0e448ec1a","url":"2649","content":"","item_type":1,"imgpic_link":"http://testapi.yuanyintang.com//image/7faf99d402104c64f639ddc0e448ec1a"},{"id":150,"title":"1","alias":"1","imgpic":"e1e2e4c4ab933b61aa2121f62b550198","url":"","content":"","item_type":1,"imgpic_link":"http://testapi.yuanyintang.com//image/e1e2e4c4ab933b61aa2121f62b550198"}]
         * tag : {"tag":[{"id":280,"title":"国人首翻","head":"dd66a7047334339ca532197dba70ab72","times":3,"head_link":"http://testapi.yuanyintang.com//image/dd66a7047334339ca532197dba70ab72"},{"id":283,"title":"今日最佳神评论","head":"8f7216e11ed2c472b1501c9b41d4d8d7","times":9,"head_link":"http://testapi.yuanyintang.com//image/8f7216e11ed2c472b1501c9b41d4d8d7"},{"id":281,"title":"花间提壶","head":"7fbb2b494fecddd7ed23a559b599e3a6","times":2,"head_link":"http://testapi.yuanyintang.com//image/7fbb2b494fecddd7ed23a559b599e3a6"}],"tag_img":{"id":153,"imgpic":"a8b404777749dc9a6f00400b6a76af55","alias":"topic_img_tag","imgpic_link":"http://testapi.yuanyintang.com//image/a8b404777749dc9a6f00400b6a76af55"},"pond_img":{"id":154,"imgpic":"74c43f35f9106f2f0de6f1c5bfe7d7b5","alias":"topic_img_hot","imgpic_link":"http://testapi.yuanyintang.com//image/74c43f35f9106f2f0de6f1c5bfe7d7b5"}}
         */

        private TagBeanX tag;
        private List<ShufflingBean> shuffling;

        public TagBeanX getTag() {
            return tag;
        }

        public void setTag(TagBeanX tag) {
            this.tag = tag;
        }

        public List<ShufflingBean> getShuffling() {
            return shuffling;
        }

        public void setShuffling(List<ShufflingBean> shuffling) {
            this.shuffling = shuffling;
        }

        public static class TagBeanX implements Serializable{
            /**
             * tag : [{"id":280,"title":"国人首翻","head":"dd66a7047334339ca532197dba70ab72","times":3,"head_link":"http://testapi.yuanyintang.com//image/dd66a7047334339ca532197dba70ab72"},{"id":283,"title":"今日最佳神评论","head":"8f7216e11ed2c472b1501c9b41d4d8d7","times":9,"head_link":"http://testapi.yuanyintang.com//image/8f7216e11ed2c472b1501c9b41d4d8d7"},{"id":281,"title":"花间提壶","head":"7fbb2b494fecddd7ed23a559b599e3a6","times":2,"head_link":"http://testapi.yuanyintang.com//image/7fbb2b494fecddd7ed23a559b599e3a6"}]
             * tag_img : {"id":153,"imgpic":"a8b404777749dc9a6f00400b6a76af55","alias":"topic_img_tag","imgpic_link":"http://testapi.yuanyintang.com//image/a8b404777749dc9a6f00400b6a76af55"}
             * pond_img : {"id":154,"imgpic":"74c43f35f9106f2f0de6f1c5bfe7d7b5","alias":"topic_img_hot","imgpic_link":"http://testapi.yuanyintang.com//image/74c43f35f9106f2f0de6f1c5bfe7d7b5"}
             */

            private TagImgBean tag_img;
            private PondImgBean pond_img;
            private List<TagBean> tag;

            public TagImgBean getTag_img() {
                return tag_img;
            }

            public void setTag_img(TagImgBean tag_img) {
                this.tag_img = tag_img;
            }

            public PondImgBean getPond_img() {
                return pond_img;
            }

            public void setPond_img(PondImgBean pond_img) {
                this.pond_img = pond_img;
            }

            public List<TagBean> getTag() {
                return tag;
            }

            public void setTag(List<TagBean> tag) {
                this.tag = tag;
            }

            public static class TagImgBean implements Serializable {
                /**
                 * id : 153
                 * imgpic : a8b404777749dc9a6f00400b6a76af55
                 * alias : topic_img_tag
                 * imgpic_link : http://testapi.yuanyintang.com//image/a8b404777749dc9a6f00400b6a76af55
                 */

                private int id;
                private String imgpic;
                private String alias;
//                private String imgpic_link;


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

                public String getImgpic() {
                    return imgpic;
                }

                public void setImgpic(String imgpic) {
                    this.imgpic = imgpic;
                }

                public String getAlias() {
                    return alias;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
                }

//                public String getImgpic_link() {
//                    return imgpic_link;
//                }
//
//                public void setImgpic_link(String imgpic_link) {
//                    this.imgpic_link = imgpic_link;
//                }
            }

            public static class PondImgBean implements Serializable {
                /**
                 * id : 154
                 * imgpic : 74c43f35f9106f2f0de6f1c5bfe7d7b5
                 * alias : topic_img_hot
                 * imgpic_link : http://testapi.yuanyintang.com//image/74c43f35f9106f2f0de6f1c5bfe7d7b5
                 */

                private int id;
                private String imgpic;
                private String alias;
//                private String imgpic_link;


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

                public String getImgpic() {
                    return imgpic;
                }

                public void setImgpic(String imgpic) {
                    this.imgpic = imgpic;
                }

                public String getAlias() {
                    return alias;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
                }

//                public String getImgpic_link() {
//                    return imgpic_link;
//                }
//
//                public void setImgpic_link(String imgpic_link) {
//                    this.imgpic_link = imgpic_link;
//                }
            }

            public static class TagBean implements MultiItemEntity,Serializable {
                /**
                 * id : 280
                 * title : 国人首翻
                 * head : dd66a7047334339ca532197dba70ab72
                 * times : 3
                 * head_link : http://testapi.yuanyintang.com//image/dd66a7047334339ca532197dba70ab72
                 */

                private int id;
                private String title;
                private String head;
                private int times;
                private String head_link;

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

                public String getHead() {
                    return head;
                }

                public void setHead(String head) {
                    this.head = head;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public String getHead_link() {
                    return head_link;
                }

                public void setHead_link(String head_link) {
                    this.head_link = head_link;
                }

                @Override
                public int getItemType() {
                    return 1;
                }
            }
        }

        public static class ShufflingBean {
            /**
             * id : 148
             * title : 哈哈哈
             * alias : 112544
             * imgpic : 7faf99d402104c64f639ddc0e448ec1a
             * url : 2649
             * content :
             * item_type : 1
             * imgpic_link : http://testapi.yuanyintang.com//image/7faf99d402104c64f639ddc0e448ec1a
             */

            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private String content;
            private int item_type;
//            private String imgpic_link;


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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getItem_type() {
                return item_type;
            }

            public void setItem_type(int item_type) {
                this.item_type = item_type;
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

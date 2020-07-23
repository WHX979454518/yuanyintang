package com.mxkj.yuanyintang.mainui.newapp.home;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.AlbumBeanTYPE;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.BANNERTYPE;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.CoverBeanTYPE;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.FOURTYPE;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.GuessBeanTYPE;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.MusicianBeanTYPE;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.MusicianBeanTYPENew;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.OriginalBeanTYPE;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.RecomendBeanTYPE;
import static com.mxkj.yuanyintang.mainui.home.data.Constant.NewHomeType.SysMsgType;

public class HomeBean implements MultiItemEntity,Serializable {
    BannerBean bannerBean;
    TypeBean typeBean;
    SysMsgBean sysMsgBean;
    AlbumBean albumBean;
    YxyRecomendBean recomendBean;
    TypeMusicListBean typeCoverListBean;
    TypeMusicListBean typeOriListBean;
    HomeMusicianBean musicianBean;
    GuessBean guessBean;
    /**
     * cate_img : {"id":147,"title":"猜你喜欢","alias":"guess_you_like","imgpic":"d880f8b9a10da3b0f949c8c13d5e2886","url":"","item_type":1,"content":"","place":"","code":"guess_you_like","to_code":"app","imgpic_link":"http://testapi.demo.com//image/d880f8b9a10da3b0f949c8c13d5e2886/3","imgpic_info":{"ext":"","w":"","h":"","size":"10028","is_long":"0","md5":"d880f8b9a10da3b0f949c8c13d5e2886","link":"http://testapi.demo.com//image/d880f8b9a10da3b0f949c8c13d5e2886/3"}}
     */

    private CateImgBean cate_img;


    @Override
    public int getItemType() {
        if (bannerBean != null) {
            return BANNERTYPE;
        } else if (typeBean != null) {
            return FOURTYPE;
        } else if (albumBean != null) {
            return AlbumBeanTYPE;
        } else if (recomendBean != null) {
            return RecomendBeanTYPE;
        } else if (typeCoverListBean != null) {
            return CoverBeanTYPE;
        } else if (typeOriListBean != null) {
            return OriginalBeanTYPE;
        } else if (musicianBean != null&&musicianBean.type.equals("recommend_musician")) {
            return MusicianBeanTYPE;
        } else if (musicianBean != null && musicianBean.type.equals("newest_musician")) {
            return MusicianBeanTYPENew;
        } else if (guessBean != null) {
            return GuessBeanTYPE;
        } else if (sysMsgBean != null) {
            return SysMsgType;
        }
        return -1;
    }

    public CateImgBean getCate_img() {
        return cate_img;
    }

    public void setCate_img(CateImgBean cate_img) {
        this.cate_img = cate_img;
    }

    /*banner*/
    public static class BannerBean implements Serializable{
        public List<HomeIndex.ItemInfoListBean.ShufflingBean> shuffling = new ArrayList<>();

        public List<HomeIndex.ItemInfoListBean.ShufflingBean> getShuffling() {
            return shuffling;
        }

        public void setShuffling(List<HomeIndex.ItemInfoListBean.ShufflingBean> shuffling) {
            this.shuffling = shuffling;
        }
    }

    /*四个分类*/
    public static class TypeBean implements Serializable {
        private TodayRecommendsBean today_recommends;
        private MusicCategoryBean music_category;
        private BillboardBean billboard;
        private NearbyBean nearby;

        public TodayRecommendsBean getToday_recommends() {
            return today_recommends;
        }

        public void setToday_recommends(TodayRecommendsBean today_recommends) {
            this.today_recommends = today_recommends;
        }

        public MusicCategoryBean getMusic_category() {
            return music_category;
        }

        public void setMusic_category(MusicCategoryBean music_category) {
            this.music_category = music_category;
        }

        public BillboardBean getBillboard() {
            return billboard;
        }

        public void setBillboard(BillboardBean billboard) {
            this.billboard = billboard;
        }

        public NearbyBean getNearby() {
            return nearby;
        }

        public void setNearby(NearbyBean nearby) {
            this.nearby = nearby;
        }

        public static class TodayRecommendsBean {

            private List<MusicsBean> musics;
            private CateImgBean cate_img;

            public CateImgBean getCate_img() {
                return cate_img;
            }

            public void setCate_img(CateImgBean cate_img) {
                this.cate_img = cate_img;
            }

            public List<MusicsBean> getMusics() {
                return musics;
            }

            public void setMusics(List<MusicsBean> musics) {
                this.musics = musics;
            }

            public static class CateImgBean implements Serializable{
                /**
                 * id : 154
                 * title : 今日推荐
                 * alias : today_recommends
                 * imgpic : 82c07ef549856077179356e133254e99
                 * url :
                 * item_type : 1
                 * content :
                 * place :
                 * imgpic_link : http://api.demo.com//image/82c07ef549856077179356e133254e99/1
                 * imgpic_info : {"ext":"","w":"","h":"","size":"19424","is_long":"0","md5":"82c07ef549856077179356e133254e99","link":"http://api.demo.com//image/82c07ef549856077179356e133254e99/1"}
                 */

                private int id;
                private String title;
                private String alias;
                private String imgpic;
                private String url;
                private int item_type;
                private String content;
                private String place;
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
            }

            public static class MusicsBean implements Serializable{

                private int id;
                private String title;
                private int music_type;
                private String mv;
                private String imgpic;
                private int counts;
                private String playtime;
                private int uid;
                private String nickname;
                private String video;
                private String dayhits;
                private int total;
                private int collection;
                private MvInfoBean mv_info;
                private String imgpic_link;
                private String imgpic_info;
                private String counts_text;
                private String video_link;
                private String video_info;
                private int collection_text;

                public int getMusic_type() {
                    return music_type;
                }

                public void setMusic_type(int music_type) {
                    this.music_type = music_type;
                }

                public String getMv() {
                    return mv;
                }

                public void setMv(String mv) {
                    this.mv = mv;
                }

                public String getDayhits() {
                    return dayhits;
                }

                public void setDayhits(String dayhits) {
                    this.dayhits = dayhits;
                }

                public int getTotal() {
                    return total;
                }

                public void setTotal(int total) {
                    this.total = total;
                }

                public MvInfoBean getMv_info() {
                    return mv_info;
                }

                public void setMv_info(MvInfoBean mv_info) {
                    this.mv_info = mv_info;
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

                public int getCounts() {
                    return counts;
                }

                public void setCounts(int counts) {
                    this.counts = counts;
                }

                public String getPlaytime() {
                    return playtime;
                }

                public void setPlaytime(String playtime) {
                    this.playtime = playtime;
                }

                public int getUid() {
                    return uid;
                }

                public void setUid(int uid) {
                    this.uid = uid;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getVideo() {
                    return video;
                }

                public void setVideo(String video) {
                    this.video = video;
                }

                public int getCollection() {
                    return collection;
                }

                public void setCollection(int collection) {
                    this.collection = collection;
                }

                public String getImgpic_link() {
                    return imgpic_link;
                }

                public void setImgpic_link(String imgpic_link) {
                    this.imgpic_link = imgpic_link;
                }

                public String getImgpic_info() {
                    return imgpic_info;
                }

                public void setImgpic_info(String imgpic_info) {
                    this.imgpic_info = imgpic_info;
                }

                public String getCounts_text() {
                    return counts_text;
                }

                public void setCounts_text(String counts_text) {
                    this.counts_text = counts_text;
                }

                public String getVideo_link() {
                    return video_link;
                }

                public void setVideo_link(String video_link) {
                    this.video_link = video_link;
                }

                public String getVideo_info() {
                    return video_info;
                }

                public void setVideo_info(String video_info) {
                    this.video_info = video_info;
                }

                public int getCollection_text() {
                    return collection_text;
                }

                public void setCollection_text(int collection_text) {
                    this.collection_text = collection_text;
                }
            }
        }

        public static class MvInfoBean implements Serializable {
            /**
             "ext":"m4a",
             "size":445069,
             "playtime":"00:05",
             "bitrate":"597",
             "height":640,
             "width":360,
             "fps":25,
             "md5":"3707d269505bc28be2864f5d87e7343b",
             "link":"http://testapi.imxkj.com//video/3707d269505bc28be2864f5d87e7343b.mp4?log_at=3"
             */
            private String ext;
            private int size;
            private String playtime;
            private String bitrate;
            private int height;
            private int width;
            private int fps;
            private String md5;
            private String link;

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getPlaytime() {
                return playtime;
            }

            public void setPlaytime(String playtime) {
                this.playtime = playtime;
            }

            public String getBitrate() {
                return bitrate;
            }

            public void setBitrate(String bitrate) {
                this.bitrate = bitrate;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getFps() {
                return fps;
            }

            public void setFps(int fps) {
                this.fps = fps;
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

        public static class MusicCategoryBean implements Serializable{
            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String place;
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
        }

        public static class BillboardBean implements Serializable{
            /**
             * id : 151
             * title : 排行榜
             * alias : billboard
             * imgpic : c5f5979af74d48fee53b799be6aba5be
             * url :
             * item_type : 1
             * content :
             * place :
             * imgpic_link : http://api.demo.com//image/c5f5979af74d48fee53b799be6aba5be/1
             * imgpic_info : {"ext":"","w":"","h":"","size":"9288","is_long":"0","md5":"c5f5979af74d48fee53b799be6aba5be","link":"http://api.demo.com//image/c5f5979af74d48fee53b799be6aba5be/1"}
             */

            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String place;
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

        }

        public static class NearbyBean implements Serializable{
            /**
             * id : 150
             * title : 附近的人
             * alias : naerby
             * imgpic : 60eb65a1cdfcc6737ffac65cbc680cdc
             * url :
             * item_type : 1
             * content :
             * place :
             * imgpic_link : http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1
             * imgpic_info : {"ext":"","w":"","h":"","size":"10306","is_long":"0","md5":"60eb65a1cdfcc6737ffac65cbc680cdc","link":"http://api.demo.com//image/60eb65a1cdfcc6737ffac65cbc680cdc/1"}
             */

            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String place;
            private String imgpic_link;
            private HomeBean.ImgpicInfoBean imgpic_info;

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

            public String getImgpic_link() {
                return imgpic_link;
            }

            public void setImgpic_link(String imgpic_link) {
                this.imgpic_link = imgpic_link;
            }

            public HomeBean.ImgpicInfoBean getImgpic_info() {
                return imgpic_info;
            }

            public void setImgpic_info(HomeBean.ImgpicInfoBean imgpic_info) {
                this.imgpic_info = imgpic_info;
            }

            public static class ImgpicInfoBean implements Serializable{
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

    /*系统消息*/
    public static class SysMsgBean implements Serializable{
        private List<SystemAfficheBean> system_affiche;
        public List<SystemAfficheBean> getSystem_affiche() {
            return system_affiche;
        }
        public void setSystem_affiche(List<SystemAfficheBean> system_affiche) {
            this.system_affiche = system_affiche;
        }
        public static class SystemAfficheBean {
            /**
             * affiche_type : 1
             * type : activity
             * id : 7
             * url : http:/www.baidu.com
             * text : 2018音乐会将会如期在某某
             */

            private String affiche_type;
            private String type;
            private int id;
            private String url;
            private String text;

            public String getAffiche_type() {
                return affiche_type;
            }

            public void setAffiche_type(String affiche_type) {
                this.affiche_type = affiche_type;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }

    /*歌单*/
    public static class AlbumBean implements Serializable{
        private List<MusicIndex.ItemInfoListBean.RecommendBean> recommend;

        public void setRecommend(List<MusicIndex.ItemInfoListBean.RecommendBean> recommend) {
            this.recommend = recommend;
        }

        public List<MusicIndex.ItemInfoListBean.RecommendBean> getRecommend() {
            return recommend;
        }

        private CateImgBean cate_img;

        public CateImgBean getCate_img() {
            return cate_img;
        }

        public void setCate_img(CateImgBean cate_img) {
            this.cate_img = cate_img;
        }

        public static class CateImgBean implements Serializable{
            /**
             * id : 154
             * title : 今日推荐
             * alias : today_recommends
             * imgpic : 82c07ef549856077179356e133254e99
             * url :
             * item_type : 1
             * content :
             * place :
             * imgpic_link : http://api.demo.com//image/82c07ef549856077179356e133254e99/1
             * imgpic_info : {"ext":"","w":"","h":"","size":"19424","is_long":"0","md5":"82c07ef549856077179356e133254e99","link":"http://api.demo.com//image/82c07ef549856077179356e133254e99/1"}
             */

            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String place;
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

            public static class ImgpicInfoBean implements Serializable{
                /**
                 * ext :
                 * w :
                 * h :
                 * size : 19424
                 * is_long : 0
                 * md5 : 82c07ef549856077179356e133254e99
                 * link : http://api.demo.com//image/82c07ef549856077179356e133254e99/1
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

    /*源小伊+一张banner*/
    public static class YxyRecomendBean implements Serializable{
        private List<YxyNoticeBean> yxy_notice;
        private List<HomeAdBean> home_ad;
        private int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public List<YxyNoticeBean> getYxy_notice() {
            return yxy_notice;
        }

        public void setYxy_notice(List<YxyNoticeBean> yxy_notice) {
            this.yxy_notice = yxy_notice;
        }

        public List<HomeAdBean> getHome_ad() {
            return home_ad;
        }

        public void setHome_ad(List<HomeAdBean> home_ad) {
            this.home_ad = home_ad;
        }

        public static class YxyNoticeBean implements Serializable{
            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String place;
            private String imgpic_link;
            private ImgpicInfoBean imgpic_info;

            @Override
            public String toString() {
                return "YxyNoticeBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", alias='" + alias + '\'' +
                        ", imgpic='" + imgpic + '\'' +
                        ", url='" + url + '\'' +
                        ", item_type=" + item_type +
                        ", content='" + content + '\'' +
                        ", place='" + place + '\'' +
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

        }

        public static class HomeAdBean implements Serializable{
            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;//
            private String content;
            private String place;
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
        }
    }

    /*原创或翻唱*/
    public static class TypeMusicListBean implements Serializable{
        private int index;
        private List<TypeMusicBean> type_music;

        public List<TypeMusicBean> getType_music() {
            return type_music;
        }

        public void setType_music(List<TypeMusicBean> type_music) {
            this.type_music = type_music;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public static class TypeMusicBean implements Serializable{
            private int id;
            private String title;
            private String imgpic;
            private String alias;
            private int selected;
            private String imgpic_link;
            private ImgpicInfoBean imgpic_info;
            private List<MusicBean> music;

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

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
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

            public List<MusicBean> getMusic() {
                return music;
            }

            public void setMusic(List<MusicBean> music) {
                this.music = music;
            }

            public static class MusicBean implements Serializable{
                /**
                 * id : 7390
                 * title : 1
                 * imgpic : 8ca25c25061556320fa628b544c129fb
                 * comment : 2
                 * counts : 122
                 * playtime :
                 * uid : 50863
                 * nickname : 皮麦郎1
                 * video : 8cdd1a74699ec80aff590e8a6763f734
                 * dayhits : 25
                 * weekhits : 25
                 * monthhits : 25
                 * music_type : 1
                 * song_id : 0
                 * is_collection : 0
                 * imgpic_link : http://testapi.demo.com//image/8ca25c25061556320fa628b544c129fb/3
                 * imgpic_info : {"ext":"jpg","w":"742","h":"750","size":"65434","is_long":"0","md5":"8ca25c25061556320fa628b544c129fb","link":"http://testapi.demo.com//image/8ca25c25061556320fa628b544c129fb/3"}
                 * comment_text : 2
                 * counts_text : 122
                 * video_link : http://testapi.imxkj.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=3
                 * video_info : {"ext":"mp3","size":"5245950","playtime":"02:09","bitrate":"324","md5":"8cdd1a74699ec80aff590e8a6763f734","link":"http://testapi.imxkj.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=3"}
                 *"mv_info": { "ext": "m4a", "size": 445069,"playtime": "00:05","bitrate": "597","height": 640,"width": 360,"fps": 25,"md5": "3707d269505bc28be2864f5d87e7343b","link": "http:\/\/testapi.imxkj.com\/\/video\/3707d269505bc28be2864f5d87e7343b.mp4?log_at=3"},
                 * dayhits_text : 25
                 * weekhits_text : 25
                 * monthhits_text : 25
                 */

                private int id;
                private String title;
                private String imgpic;
                private int comment;
                private int counts;
                private String playtime;
                private int uid;
                private String nickname;
                private String video;
                private int dayhits;
                private int weekhits;
                private int monthhits;
                private int music_type;

                private String mv;
                private int song_id;
                private int is_collection;
                private String imgpic_link;
                private ImgpicInfoBean imgpic_info;
                private int comment_text;
                private String counts_text;
                private String video_link;
                private VideoInfoBean video_info;
                private MvInfoBean mv_info;

                //              private int dayhits_text;
//              private int weekhits_text;
//              private int monthhits_text;
                private boolean isPlaying;

                public MvInfoBean getMv_info() {
                    return mv_info;
                }

                public void setMv_info(MvInfoBean mv_info) {
                    this.mv_info = mv_info;
                }

                public String getMv() {
                    return mv;
                }

                public void setMv(String mv) {
                    this.mv = mv;
                }

                @Override
                public String toString() {
                    return "{" +
                            "id=" + id +
                            ", title='" + title + '\'' +
                            ", imgpic='" + imgpic + '\'' +
                            ", comment=" + comment +
                            ", counts=" + counts +
                            ", playtime='" + playtime + '\'' +
                            ", uid=" + uid +
                            ", nickname='" + nickname + '\'' +
                            ", video='" + video + '\'' +
                            ", dayhits=" + dayhits +
                            ", weekhits=" + weekhits +
                            ", monthhits=" + monthhits +
                            ", music_type=" + music_type +
                            ", mv='" + mv + '\'' +
                            ", song_id=" + song_id +
                            ", is_collection=" + is_collection +
                            ", imgpic_link='" + imgpic_link + '\'' +
                            ", imgpic_info=" + imgpic_info +
                            ", comment_text=" + comment_text +
                            ", counts_text='" + counts_text + '\'' +
                            ", video_link='" + video_link + '\'' +
                            ", video_info=" + video_info +
                            ", isPlaying=" + isPlaying +
                            '}';
                }

                public boolean isPlaying() {
                    return isPlaying;
                }

                public void setPlaying(boolean playing) {
                    isPlaying = playing;
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

                public int getComment() {
                    return comment;
                }

                public void setComment(int comment) {
                    this.comment = comment;
                }

                public int getCounts() {
                    return counts;
                }

                public void setCounts(int counts) {
                    this.counts = counts;
                }

                public String getPlaytime() {
                    return playtime;
                }

                public void setPlaytime(String playtime) {
                    this.playtime = playtime;
                }

                public int getUid() {
                    return uid;
                }

                public void setUid(int uid) {
                    this.uid = uid;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getVideo() {
                    return video;
                }

                public void setVideo(String video) {
                    this.video = video;
                }

                public int getDayhits() {
                    return dayhits;
                }

                public void setDayhits(int dayhits) {
                    this.dayhits = dayhits;
                }

                public int getWeekhits() {
                    return weekhits;
                }

                public void setWeekhits(int weekhits) {
                    this.weekhits = weekhits;
                }

                public int getMonthhits() {
                    return monthhits;
                }

                public void setMonthhits(int monthhits) {
                    this.monthhits = monthhits;
                }

                public int getMusic_type() {
                    return music_type;
                }

                public void setMusic_type(int music_type) {
                    this.music_type = music_type;
                }

                public int getSong_id() {
                    return song_id;
                }

                public void setSong_id(int song_id) {
                    this.song_id = song_id;
                }

                public int getIs_collection() {
                    return is_collection;
                }

                public void setIs_collection(int is_collection) {
                    this.is_collection = is_collection;
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

                public int getComment_text() {
                    return comment_text;
                }

                public void setComment_text(int comment_text) {
                    this.comment_text = comment_text;
                }

                public String getCounts_text() {
                    return counts_text;
                }

                public void setCounts_text(String counts_text) {
                    this.counts_text = counts_text;
                }

                public String getVideo_link() {
                    return video_link;
                }

                public void setVideo_link(String video_link) {
                    this.video_link = video_link;
                }

                public VideoInfoBean getVideo_info() {
                    return video_info;
                }

                public void setVideo_info(VideoInfoBean video_info) {
                    this.video_info = video_info;
                }

//                public int getDayhits_text() {
//                    return dayhits_text;
//                }

//                public void setDayhits_text(int dayhits_text) {
//                    this.dayhits_text = dayhits_text;
//                }

//                public int getWeekhits_text() {
//                    return weekhits_text;
//                }

//                public void setWeekhits_text(int weekhits_text) {
//                    this.weekhits_text = weekhits_text;
//                }

//                public int getMonthhits_text() {
//                    return monthhits_text;
//                }
//
//                public void setMonthhits_text(int monthhits_text) {
//                    this.monthhits_text = monthhits_text;
//                }

                public static class ImgpicInfoBeanX implements Serializable{
                    /**
                     * ext : jpg
                     * w : 742
                     * h : 750
                     * size : 65434
                     * is_long : 0
                     * md5 : 8ca25c25061556320fa628b544c129fb
                     * link : http://testapi.demo.com//image/8ca25c25061556320fa628b544c129fb/3
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

                public static class VideoInfoBean implements Serializable{
                    /**
                     * ext : mp3
                     * size : 5245950
                     * playtime : 02:09
                     * bitrate : 324
                     * md5 : 8cdd1a74699ec80aff590e8a6763f734
                     * link : http://testapi.imxkj.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=3
                     */

                    private String ext;
                    private String size;
                    private String playtime;
                    private String bitrate;
                    private String md5;
                    private String link;

                    public String getExt() {
                        return ext;
                    }

                    public void setExt(String ext) {
                        this.ext = ext;
                    }

                    public String getSize() {
                        return size;
                    }

                    public void setSize(String size) {
                        this.size = size;
                    }

                    public String getPlaytime() {
                        return playtime;
                    }

                    public void setPlaytime(String playtime) {
                        this.playtime = playtime;
                    }

                    public String getBitrate() {
                        return bitrate;
                    }

                    public void setBitrate(String bitrate) {
                        this.bitrate = bitrate;
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

                public static class MvInfoBean implements Serializable{
                    /**
                     * ext : m4a
                     * size : 445069
                     * playtime : 00:05
                     * bitrate : 597
                     * height :640
                     * width:360
                     * fps :25
                     * md5 : 3707d269505bc28be2864f5d87e7343b
                     * link : http:\/\/testapi.imxkj.com\/\/video\/3707d269505bc28be2864f5d87e7343b.mp4?log_at=3
                     */

                    private String ext;
                    private int size;
                    private String playtime;
                    private String bitrate;
                    private int height;
                    private int width;
                    private int fps;
                    private String md5;
                    private String link;


                    @Override
                    public String toString() {
                        return "MvInfoBean{" +
                                "ext='" + ext + '\'' +
                                ", size=" + size +
                                ", playtime='" + playtime + '\'' +
                                ", bitrate='" + bitrate + '\'' +
                                ", height=" + height +
                                ", width=" + width +
                                ", fps=" + fps +
                                ", md5='" + md5 + '\'' +
                                ", link='" + link + '\'' +
                                '}';
                    }

                    public String getExt() {
                        return ext;
                    }

                    public void setExt(String ext) {
                        this.ext = ext;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }

                    public String getPlaytime() {
                        return playtime;
                    }

                    public void setPlaytime(String playtime) {
                        this.playtime = playtime;
                    }

                    public String getBitrate() {
                        return bitrate;
                    }

                    public void setBitrate(String bitrate) {
                        this.bitrate = bitrate;
                    }

                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                    }

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }

                    public int getFps() {
                        return fps;
                    }

                    public void setFps(int fps) {
                        this.fps = fps;
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

    /*音乐人*/

    public static class HomeMusicianBean implements Serializable{
        private String type;//new  和  hot
        private List<MusicianBean> musician;
        private CateImgBean cate_img;

        public static class CateImgBean implements Serializable{
            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String place;
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
        }

        public CateImgBean getCate_img() {
            return cate_img;
        }

        public void setCate_img(CateImgBean cate_img) {
            this.cate_img = cate_img;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<MusicianBean> getMusician() {
            return musician;
        }

        public void setMusician(List<MusicianBean> musician) {
            this.musician = musician;
        }

        public static class MusicianBean implements MultiItemEntity, Serializable{
            private int fans_num = 0;
            private int id;
            private int is_music;
            private int member_type;
            private String nickname;
            private int sex;
            private int is_relation;
            private String head_link;
            private ImgpicInfoBean head_info;

            public int getFans_num() {
                return fans_num;
            }

            public void setFans_num(int fans_num) {
                this.fans_num = fans_num;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_music() {
                return is_music;
            }

            public void setIs_music(int is_music) {
                this.is_music = is_music;
            }

            public int getMember_type() {
                return member_type;
            }

            public void setMember_type(int member_type) {
                this.member_type = member_type;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public int getRelation() {
                return is_relation;
            }

            public void setRelation(int relation) {
                this.is_relation = relation;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public ImgpicInfoBean getHead_info() {
                return head_info;
            }

            public void setHead_info(ImgpicInfoBean head_info) {
                this.head_info = head_info;
            }

            @Override
            public int getItemType() {
                return 0;
            }

            public static class HeadInfoBean {
                private String link;

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }
            }
        }
    }

    public static class CateImgBean implements Serializable{
        /**
         * id : 147
         * title : 猜你喜欢
         * alias : guess_you_like
         * imgpic : d880f8b9a10da3b0f949c8c13d5e2886
         * url :
         * item_type : 1
         * content :
         * place :
         * code : guess_you_like
         * to_code : app
         * imgpic_link : http://testapi.demo.com//image/d880f8b9a10da3b0f949c8c13d5e2886/3
         * imgpic_info : {"ext":"","w":"","h":"","size":"10028","is_long":"0","md5":"d880f8b9a10da3b0f949c8c13d5e2886","link":"http://testapi.demo.com//image/d880f8b9a10da3b0f949c8c13d5e2886/3"}
         */

        private int id;
        private int day;
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

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
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
    }


    /*猜你喜欢*/
    public static class GuessBean implements Serializable{
        private List<MusicIndex.ItemInfoListBean.RecommendBean> guessList;
        private CateImgBean cate_img;

        public List<MusicIndex.ItemInfoListBean.RecommendBean> getGuessList() {
            return guessList;
        }

        public void setGuessList(List<MusicIndex.ItemInfoListBean.RecommendBean> guessList) {
            this.guessList = guessList;
        }

        public CateImgBean getCate_img() {
            return cate_img;
        }

        public void setCate_img(CateImgBean cate_img) {
            this.cate_img = cate_img;
        }
    }

    public static class ImgpicInfoBean implements Serializable{
        /**
         * ext :
         * w :
         * h :
         * size : 19424
         * is_long : 0
         * md5 : 82c07ef549856077179356e133254e99
         * link : http://api.demo.com//image/82c07ef549856077179356e133254e99/1
         */

        private String ext;
        private String w;
        private String h;
        private String size;
        private String is_long;
        private String md5;
        private String link;

        @Override
        public String toString() {
            return "ImgpicInfoBean{" +
                    "ext='" + ext + '\'' +
                    ", w='" + w + '\'' +
                    ", h='" + h + '\'' +
                    ", size='" + size + '\'' +
                    ", is_long='" + is_long + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }

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

    public static class MvInfoBean implements Serializable {
        /**
         * ext : m4a
         * size : 445069
         * playtime : 00:05
         * bitrate : 597
         * height :640
         * width:360
         * fps :25
         * md5 : 3707d269505bc28be2864f5d87e7343b
         * link : http:\/\/testapi.imxkj.com\/\/video\/3707d269505bc28be2864f5d87e7343b.mp4?log_at=3
         */
        private String ext;
        private int size;
        private String playtime;
        private String bitrate;
        private int height;
        private int width;
        private int fps;
        private String md5;
        private String link;

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getBitrate() {
            return bitrate;
        }

        public void setBitrate(String bitrate) {
            this.bitrate = bitrate;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getFps() {
            return fps;
        }

        public void setFps(int fps) {
            this.fps = fps;
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

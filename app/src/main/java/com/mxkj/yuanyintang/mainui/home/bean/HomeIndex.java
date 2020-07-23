package com.mxkj.yuanyintang.mainui.home.bean;


import com.google.gson.annotations.SerializedName;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：admin on 2017/3/28 14:18.
 */

public class HomeIndex implements Serializable{

    /**
     * code : 0
     * itemInfoList : [{"itemType":"topBanner","module":"topBanner","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/s720x322_jfs/t4903/41/12296166/85214/15205dd6/58d92373N127109d8.jpg!q70.jpg","clickUrl":"男装超级品牌类日"},{"imageUrl":"https://img1.360buyimg.com/da/jfs/t4309/113/2596274814/85129/a59c5f5e/58d4762cN72d7dd75.jpg","clickUrl":"京东春茶季"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s720x322_jfs/t4675/88/704144946/137165/bbbe8a4e/58d3a160N621fc59c.jpg!q70.jpg","clickUrl":"装出新高度"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s720x322_jfs/t4627/177/812580410/198036/24a79c26/58d4f1e9N5b9fc5ee.jpg!q70.jpg","clickUrl":"宝宝出行利器"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s720x322_jfs/t3097/241/9768114398/78418/47e4335e/58d8a637N6f178fbd.jpg!q70.jpg","clickUrl":"慕尼黑新品上市"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s720x322_jfs/t4282/364/2687292678/87315/e4311cd0/58d4d923N24a2f5eb.jpg!q70.jpg","clickUrl":"空调让你百试不爽"},{"imageUrl":"https://img1.360buyimg.com/da/jfs/t4162/171/1874609280/92523/a1206b3f/58c7a832Nc8582e81.jpg","clickUrl":"奥妙全新上市"},{"imageUrl":"https://img1.360buyimg.com/da/jfs/t4387/338/1185667042/56822/bcd7fc3d/58d9e139Nadf09c53.jpg","clickUrl":"吸尘品类直降"}]},{"itemType":"iconList","module":"iconList","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t4477/120/1165061315/6371/82dfb453/58d9a09dNe4ba11e3.png","clickUrl":"京东超市","itemTitle":"京东超市"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3286/167/1907269933/15789/da204cbe/57d53f16Nf3431cbd.png","clickUrl":"全球购","itemTitle":"全球购"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3208/285/1806438443/12227/e35aa8d/57d5407cN0d6adf20.png","clickUrl":"服装城","itemTitle":"服装城"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3079/222/1812395993/14681/29321e2c/57d54122N700d9c1b.png","clickUrl":"京东生鲜","itemTitle":"京东生鲜"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3664/294/1570684882/19292/8b63dd4d/582adfbcNf82877de.png","clickUrl":"京东到家","itemTitle":"京东到家"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3118/92/1836879034/12255/981e942a/57d54204N32b71c87.png","clickUrl":"充值中心","itemTitle":"充值中心"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3424/278/301037516/11616/98748707/58096edbNcd05f66b.png","clickUrl":"惠赚钱","itemTitle":"惠赚钱"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3202/244/9578875998/14975/afaba260/58d4ee32N29ed1055.png","clickUrl":"领券","itemTitle":"领券"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3199/169/1818813995/12570/62402b0d/57d54364Needc47cd.png","clickUrl":"物流查询","itemTitle":"物流查询"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s80x80_jfs/t3211/295/1824792746/12749/a74e2524/57d543ebN25337ef2.png","clickUrl":"我的关注","itemTitle":"我的关注"}]},{"itemType":"newUser","module":"newUser","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t3229/183/8420242660/31884/d556597b/58c24709Nf44579e2.jpg!q70.jpg","clickUrl":"新用户专享","itemTitle":"京东超市","itemSubTitle":"","itemRecommendedLanguage":"","itemSubscript":"","itemBackgroundImageUrl":""},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t3169/299/6474778567/28114/54ee4d66/58a6d4b3N69c6e565.jpg!q70.jpg","clickUrl":"新人红包"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4444/167/618381657/13640/b412722b/58d2a204N83cc6dbd.jpg!q70.jpg","clickUrl":"9.9包邮"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4303/36/2607357917/11737/a2500194/58d3ef41N8458849c.jpg!q70.jpg","clickUrl":"衣食住行"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t3862/351/2286895626/41651/b9430342/58a6d991Ne9c82f29.jpg!q70.jpg","clickUrl":"首单满99新人礼包"}]},{"itemType":"jdBulletin","module":"jdBulletin","itemContentList":[{"clickUrl":"男装超品类日，跨店3件7折！","itemTitle":"热","itemSubTitle":"男装超品类日，跨店3件7折！"},{"clickUrl":"运动大牌春季特惠，满800减100！","itemTitle":"抢","itemSubTitle":"运动大牌春季特惠，满800减100！"},{"clickUrl":"美妆护肤，领券满199元减100！","itemTitle":"大促","itemSubTitle":"美妆护肤，领券满199元减100！"}]},{"itemType":"jdSpikeHeader","module":"jdSpike","itemContentList":[{"clickUrl":"秒杀专场","itemTitle":"16点场","itemSubTitle":"莫负春光进来逛逛","itemRecommendedLanguage":"50000"}]},{"itemType":"jdSpikeContent","module":"jdSpike","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t4186/44/1480543850/122209/11be676e/58c2676aN66a9c5ed.jpg!q70.jpg","clickUrl":"福临门","itemTitle":"73.9","itemSubTitle":"119.9","itemSubscript":"好货"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t799/288/51305470/57357/7bfb28a2/54f52c83Nb6c2cef5.jpg!q70.jpg","clickUrl":"刮胡刀","itemTitle":"299","itemSubTitle":"538","itemSubscript":"品质"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t2158/275/1115377524/647696/6c177b0/56792777N23d7cda0.png!q70.jpg","clickUrl":"ipad","itemTitle":"3199","itemSubTitle":"3688","itemSubscript":"经典"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t3790/31/64611920/414303/ae44859b/57fde07dN0c1bae84.jpg!q70.jpg","clickUrl":"美女玩具","itemTitle":"99","itemSubTitle":"158"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t3208/354/1454144017/140641/d876f6d2/57cd3936N647f9d2d.jpg!q70.jpg","clickUrl":"男裤","itemTitle":"199","itemSubTitle":"338"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t4582/132/1058213405/68736/4f05024/58d8595fN6d4f8092.jpg!q70.jpg","clickUrl":"自行车","itemTitle":"599","itemSubTitle":"1138"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t3847/3/2992821407/161733/b17f17ef/58748cb0N721e7a20.jpg!q70.jpg","clickUrl":"皮鞋","itemTitle":"159","itemSubTitle":"435"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t2023/299/2364635879/258423/c2d89f21/56cfc313Nc798c4c9.jpg!q70.jpg","clickUrl":"减肥","itemTitle":"399","itemSubTitle":"638"}]},{"itemType":"showEvent","module":"showEvent","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t5011/52/11315246/33679/3af01dc4/58d914ccN6070524e.jpg!q70.jpg","clickUrl":"品牌折扣"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4261/164/2985229152/21008/6cee35d7/58d93ef1Nf05dc380.jpg!q70.jpg","clickUrl":"主会场"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4678/353/1126087781/29490/bd75b7c9/58d91540N13ccd112.jpg!q70.jpg","clickUrl":"尖货折扣"}]},{"itemType":"findGoodStuff","module":"findGoodStuff","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t4573/49/77501525/294199/65cf62f/58c93bb7N6ee68c13.jpg!q70.jpg","clickUrl":"发现好货"},{"imageUrl":"https://m.360buyimg.com/mobilecms/s220x220_jfs/t4459/60/496148966/255134/5fac7ade/58d0bcdcNe426a111.jpg!q70.jpg","clickUrl":"优品专辑"}]},{"itemType":"type_211","module":"ranking","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t3190/88/5826160627/22963/564f1153/58806ba0Nf975c799.jpg!q70.jpg","clickUrl":"排行榜","itemTitle":"排行榜","itemSubTitle":"网罗全民挚爱","itemRecommendedLanguage":"人气火爆","itemSubscript":"","itemBackgroundImageUrl":""},{"imageUrl":"https://m.360buyimg.com//mobilecms/s276x276_jfs/t3127/283/6476517920/291766/84b2d798/58a7bf8cNf4c925d5.jpg!q70.jpg","clickUrl":"品牌头条","itemTitle":"品牌头条","itemSubTitle":"大牌很任性"},{"imageUrl":"https://m.360buyimg.com//mobilecms/s276x276_jfs/t3214/176/5823546732/128532/624229f6/5881b1e3N6fe68d4c.jpg!q70.jpg","clickUrl":"闪购","itemTitle":"闪购","itemSubTitle":"品牌特卖"}]},{"itemType":"type_Title","module":"loveLife","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t2878/152/3484829401/8909/e1cf0ca/578de36bNae7bb54a.png!q70.jpg","itemBackgroundImageUrl":"https://st.360buyimg.com/m/images/index/floor-tit.png"}]},{"itemType":"type_22","module":"loveLife_item1","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4390/306/810593954/43649/db7da1a3/58d4f31dN7f95f121.jpg!q70.jpg","clickUrl":"玩3c","itemTitle":"玩3C","itemSubTitle":"抢iPhone7红","itemRecommendedLanguage":"12期免息 燃"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t3277/14/9826379985/16115/3bb30dc7/58d8ce10Nc69a898e.jpg!q70.jpg","clickUrl":"京东家电","itemTitle":"京东家电","itemSubTitle":"800元现金券"}]},{"itemType":"type_22","module":"loveLife_item2","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4663/320/926911638/15321/7cdb9777/58d66630N3aa7e836.jpg!q70.jpg","clickUrl":"京东超市","itemTitle":"京东超市","itemSubTitle":"纸品二免一"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4516/339/787269831/33006/b2d29efe/58d5008dN359de233.jpg!q70.jpg","clickUrl":"爱家","itemTitle":"爱家","itemSubTitle":"居家物199-100","itemRecommendedLanguage":"家居小商品节"}]},{"itemType":"type_1111","module":"loveLife_item3","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4675/199/1080554759/28689/4d53c00e/58d88d51N32dfc9e7.png!q70.jpg","clickUrl":"爱宝宝","itemTitle":"爱宝宝","itemSubTitle":"满199减50","itemRecommendedLanguage":"人气火爆","itemSubscript":"玩大牌","itemBackgroundImageUrl":""},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4273/172/3010816439/22923/3096939d/58d8b505N70640765.jpg!q70.jpg","clickUrl":"爱美丽","itemTitle":"爱美丽","itemSubTitle":"YSL黑管唇釉"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4426/51/1099039770/16697/134acadd/58d8d2e1N3b849fd9.jpg!q70.jpg","clickUrl":"爱吃","itemTitle":"爱吃","itemSubTitle":"领券99减50","itemSubscript":"春茶节"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4543/232/1163237356/8249/e2e2069c/58d9c2ecN771ca3f0.jpg!q70.jpg","clickUrl":"爱逛","itemTitle":"爱逛","itemSubTitle":"满399减100","itemSubscript":"男装节"}]},{"itemType":"type_middleBanner","module":"loveLife_item4","itemContentList":[{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4849/174/66501445/26867/afb327e2/58da04c2Ne94c71a9.jpg!q70.jpg","clickUrl":"onPlus"},{"imageUrl":"https://m.360buyimg.com/mobilecms/jfs/t4474/169/1143877107/79140/ed5bef21/58d8cfcfN7f38aa2b.jpg!q70.jpg","clickUrl":"京东金融"}]}]
     */

    public String code;
    public List<ItemInfoListBean> itemInfoList;
    /**
     * cate_img : {"alias":"topic","title":"热门池塘","imgpic":"441c089d6b9f1f32ba02e49ac1f4c514","id":111,"url":"1","place":"池塘","imgpic_link":"http://demoapi.yuanyintang.com/image/441c089d6b9f1f32ba02e49ac1f4c514"}
     */

    private CateImgBean cate_img;

    public CateImgBean getCate_img() {
        return cate_img;
    }

    public void setCate_img(CateImgBean cate_img) {
        this.cate_img = cate_img;
    }


    // FIXME generate failure  field _$Billboard16
    public static class ItemInfoListBean implements MultiItemEntity, Serializable {

        public String itemType;
        private int tag;
        private String cate_img;
        private int item;

        /**
         * billboard : {"billboards":[{"id":1,"cate_img":"","title":"","imgpic":"bf619d9f6694960296d3135082ed7653d9f3af9c","border_color":"FFFFFF","imgpic_link":"http://api.demo.com//image/bf619d9f6694960296d3135082ed7653d9f3af9c/1","imgpic_info":{"ext":"png","w":"480","h":"768","size":"309737","is_long":"0","md5":"22e20b1b3e5329d7deb99112d530a9a7","link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1"},"music":{"id":7387,"title":"改名了ii","video":"8cdd1a74699ec80aff590e8a6763f734","imgpic":"22e20b1b3e5329d7deb99112d530a9a7","intro":"8888","video_link":"http://api.demo.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=1","video_info":{"ext":"mp3","size":5245950,"playtime":"02:09","bitrate":"324","md5":"8cdd1a74699ec80aff590e8a6763f734","link":"http://api.demo.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=1"},"imgpic_link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1","imgpic_info":{"ext":"png","w":"480","h":"768","size":"309737","is_long":"0","md5":"22e20b1b3e5329d7deb99112d530a9a7","link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1"}}}],"cate_img":{"id":65,"title":"排行榜","imgpic":"1b8ef65106379bfe32cedca5428a0219","url":"","place":"","alias":"billboard","imgpic_link":"http://api.demo.com//image/1b8ef65106379bfe32cedca5428a0219/1","imgpic_info":{"ext":"png","w":"480","h":"768","size":"309737","is_long":"0","md5":"22e20b1b3e5329d7deb99112d530a9a7","link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1"}}}
         */


        public int getItem() {
            return item;
        }

        public void setItem(int item) {
            this.item = item;
        }

        public String getCate_img() {
            return cate_img;
        }

        public void setCate_img(String cate_img) {
            this.cate_img = cate_img;
        }

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        @Override
        public int getItemType() {
            if ("topBanner".equals(itemType)) {
                return Constant.TYPE_TOP_BANNER;
            } else if ("recommendMusic".equals(itemType)) {
                return Constant.TYPE_RECOMMEND_MUSIC;
            } else if ("recommendMusicIan".equals(itemType)) {
                return Constant.TYPE_RECOMMEND_MUSICIAN;
            } else if ("vocaloid".equals(itemType)) {
                return Constant.TYPE_VOCALOID;
            } else if ("pond".equals(itemType)) {
                return Constant.TYPE_POND;
            } else if ("systemMsg".equals(itemType)) {
                return Constant.TYPE_SYSYEM_MSG;
            } else if ("lottery".equals(itemType)) {
                return Constant.TYPE_LOTTERY;
            } else if ("charts".equals(itemType)) {
                return Constant.TYPE_CHARTS;
            }
            return Constant.TYPE_OTHER;
        }

        private MusicianBeanX musician;
        private SongBeanX song;
        private List<ShufflingBean> shuffling = new ArrayList<>();
        private List<MusicBeanX> music;
        private List<CatemusicBean> catemusic = new ArrayList<>();
        private List<CustomlistBean> customlist = new ArrayList<>();
        private List<MusicnewBean> musicnew = new ArrayList<>();
        private List<TopicBean> topic = new ArrayList<>();
        private List<LotteryBean> lotteryList = new ArrayList();
        private List<SystemMsgBean> systemMsgBeanList = new ArrayList();
        private BillboardBean billboardBean;

        public BillboardBean getBillboardBean() {
            return billboardBean;
        }

        public void setBillboardBean(BillboardBean billboardBean) {
            this.billboardBean = billboardBean;
        }

        public List<SystemMsgBean> getSystemMsgBeanList() {
            return systemMsgBeanList;
        }

        public void setSystemMsgBeanList(List<SystemMsgBean> systemMsgBeanList) {
            this.systemMsgBeanList = systemMsgBeanList;
        }

        public List<LotteryBean> getLotteryList() {
            return lotteryList;
        }

        public void setLotteryList(List<LotteryBean> lotteryList) {
            this.lotteryList = lotteryList;
        }

        public MusicianBeanX getMusicianX() {
            return musician;
        }

        public void setMusician(MusicianBeanX musician) {
            this.musician = musician;
        }

        public SongBeanX getSong() {
            return song;
        }

        public void setSong(SongBeanX song) {
            this.song = song;
        }

        public List<ShufflingBean> getShuffling() {
            return shuffling;
        }

        public void setShuffling(List<ShufflingBean> shuffling) {
            this.shuffling = shuffling;
        }

        public List<MusicBeanX> getMusic() {
            return music;
        }

        public void setMusic(List<MusicBeanX> music) {
            this.music = music;
        }

        public List<CatemusicBean> getCatemusic() {
            return catemusic;
        }

        public void setCatemusic(List<CatemusicBean> catemusic) {
            this.catemusic = catemusic;
        }

        public List<CustomlistBean> getCustomlist() {
            return customlist;
        }

        public void setCustomlist(List<CustomlistBean> customlist) {
            this.customlist = customlist;
        }

        public List<MusicnewBean> getMusicnew() {
            return musicnew;
        }

        public void setMusicnew(List<MusicnewBean> musicnew) {
            this.musicnew = musicnew;
        }

        public List<TopicBean> getTopic() {
            return topic;
        }

        public void setTopic(List<TopicBean> topic) {
            this.topic = topic;
        }

        public static class MusicianBeanX implements Serializable {
            /**
             * musician : [{"id":25920,"nickname":"Dewa季枳然","sex":1,"head":"2226f2f29b5e3bf1877a0f7a0bde5eafd7b35ccb","music":{"id":1983,"title":"御龙铭千古","imgpic":"3dc7ef6338d2bb1d836d050a4e0b08f2d0e3c5c1","counts":1582,"uid":25920,"nickname":"Dewa季枳然","video":"d85659487296102d72fc235123ba9fdd44834700","playtime":"05:02","collection":0,"imgpic_link":"http://yyt.demo.com/image/3dc7ef6338d2bb1d836d050a4e0b08f2d0e3c5c1","video_link":"http://yyt.demo.com/music/d85659487296102d72fc235123ba9fdd44834700"},"head_link":"http://yyt.demo.com/image/2226f2f29b5e3bf1877a0f7a0bde5eafd7b35ccb"},{"id":37933,"nickname":"裂天","sex":1,"head":"8ea0d9089c3873d4c9804bc9221762d2c6458c67","music":{"id":4809,"title":"九张机","imgpic":"afa77c10fd1b53ce3c64e0143333a0249d5781ad","counts":1060,"uid":37933,"nickname":"裂天","video":"4c849fdbb7ff77430861b2b8a7a7a13a3dd47dd0","playtime":"03:40","collection":0,"imgpic_link":"http://yyt.demo.com/image/afa77c10fd1b53ce3c64e0143333a0249d5781ad","video_link":"http://yyt.demo.com/music/4c849fdbb7ff77430861b2b8a7a7a13a3dd47dd0"},"head_link":"http://yyt.demo.com/image/8ea0d9089c3873d4c9804bc9221762d2c6458c67"},{"id":37909,"nickname":"祈Inory","sex":0,"head":"ddee0a96a6de3d547ca7c056499b65b37e289e53","music":{"id":5378,"title":"時間は窓の向こう側","imgpic":"9e9c3c3094fb67223b49aaed44589ed0b9cc44af","counts":896,"uid":37909,"nickname":"祈Inory","video":"f96ebfe9b9e21610ca449517414970bf26838847","playtime":"05:02","collection":0,"imgpic_link":"http://yyt.demo.com/image/9e9c3c3094fb67223b49aaed44589ed0b9cc44af","video_link":"http://yyt.demo.com/music/f96ebfe9b9e21610ca449517414970bf26838847"},"head_link":"http://yyt.demo.com/image/ddee0a96a6de3d547ca7c056499b65b37e289e53"},{"id":22721,"nickname":"十二律音乐联盟","sex":1,"head":"a57e6790378fb5663545034da697fa2f5951bf6c","music":{"id":2845,"title":"魂大侠【魂斗罗手游】-十二律","imgpic":"f5421b807dd0f52b0eceed110591130d340d427a","counts":1049,"uid":22721,"nickname":"十二律音乐联盟","video":"05d3b3f93693304000ac1bccb02fac945d206429","playtime":"02:26","collection":0,"imgpic_link":"http://yyt.demo.com/image/f5421b807dd0f52b0eceed110591130d340d427a","video_link":"http://yyt.demo.com/music/05d3b3f93693304000ac1bccb02fac945d206429"},"head_link":"http://yyt.demo.com/image/a57e6790378fb5663545034da697fa2f5951bf6c"},{"id":37280,"nickname":"根小八","sex":1,"head":"2606118444cc7e0a52fe70ad70897e53d0bb607f","music":{"id":4387,"title":"天篷","imgpic":"2d263e40db7042e21404b17069be220457b19741","counts":394,"uid":37280,"nickname":"根小八","video":"013d83dcacb58c80bba5dc02cae0804abe6df6b0","playtime":"03:20","collection":0,"imgpic_link":"http://yyt.demo.com/image/2d263e40db7042e21404b17069be220457b19741","video_link":"http://yyt.demo.com/music/013d83dcacb58c80bba5dc02cae0804abe6df6b0"},"head_link":"http://yyt.demo.com/image/2606118444cc7e0a52fe70ad70897e53d0bb607f"}]
             * cate_img : {"id":65,"title":"首页图片配置","imgpic":"af0b2a95be66a1a834ce57951a948861a3592905","url":"","imgpic_link":"http://yyt.demo.com/image/af0b2a95be66a1a834ce57951a948861a3592905"}
             */

            private CateImgBean cate_img;
            private List<MusicianBean> musician = new ArrayList<>();

            public CateImgBean getCate_img() {
                return cate_img;
            }

            public void setCate_img(CateImgBean cate_img) {
                this.cate_img = cate_img;
            }

            public List<MusicianBean> getMusician() {
                return musician;
            }

            public void setMusician(List<MusicianBean> musician) {
                this.musician = musician;
            }

            public static class CateImgBean implements Serializable {
                /**
                 * id : 65
                 * title : 首页图片配置
                 * imgpic : af0b2a95be66a1a834ce57951a948861a3592905
                 * url :
                 * imgpic_link : http://yyt.demo.com/image/af0b2a95be66a1a834ce57951a948861a3592905
                 */

                private int id;
                private String title;
                private String imgpic;
                private String url;
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

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

//                public String getImgpic_link() {
//                    return imgpic_link;
//                }
//
//                public void setImgpic_link(String imgpic_link) {
//                    this.imgpic_link = imgpic_link;
//                }
            }

            public static class MusicianBean implements Serializable {
                /**
                 * id : 25920
                 * nickname : Dewa季枳然
                 * sex : 1
                 * head : 2226f2f29b5e3bf1877a0f7a0bde5eafd7b35ccb
                 * music : {"id":1983,"title":"御龙铭千古","imgpic":"3dc7ef6338d2bb1d836d050a4e0b08f2d0e3c5c1","counts":1582,"uid":25920,"nickname":"Dewa季枳然","video":"d85659487296102d72fc235123ba9fdd44834700","playtime":"05:02","collection":0,"imgpic_link":"http://yyt.demo.com/image/3dc7ef6338d2bb1d836d050a4e0b08f2d0e3c5c1","video_link":"http://yyt.demo.com/music/d85659487296102d72fc235123ba9fdd44834700"}
                 * head_link : http://yyt.demo.com/image/2226f2f29b5e3bf1877a0f7a0bde5eafd7b35ccb
                 */

                private int id;
                private String nickname;
                private int sex;
                private String head;
                private MusicBean music;
                private String head_link;
                private int is_music;
                private Boolean check;

                public Boolean getCheck() {
                    if (null == check) {
                        return false;
                    }
                    return check;
                }

                public void setCheck(Boolean check) {
                    this.check = check;
                }

                public int getIs_music() {
                    return is_music;
                }

                public void setIs_music(int is_music) {
                    this.is_music = is_music;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
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

                public String getHead() {
                    return head;
                }

                public void setHead(String head) {
                    this.head = head;
                }

                public MusicBean getMusic() {
                    return music;
                }

                public void setMusic(MusicBean music) {
                    this.music = music;
                }

                public String getHead_link() {
                    return head_link;
                }

                public void setHead_link(String head_link) {
                    this.head_link = head_link;
                }

                public static class MusicBean implements Serializable {
                    /**
                     * id : 1983
                     * title : 御龙铭千古
                     * imgpic : 3dc7ef6338d2bb1d836d050a4e0b08f2d0e3c5c1
                     * counts : 1582
                     * uid : 25920
                     * nickname : Dewa季枳然
                     * video : d85659487296102d72fc235123ba9fdd44834700
                     * playtime : 05:02
                     * collection : 0
                     * imgpic_link : http://yyt.demo.com/image/3dc7ef6338d2bb1d836d050a4e0b08f2d0e3c5c1
                     * video_link : http://yyt.demo.com/music/d85659487296102d72fc235123ba9fdd44834700
                     */

                    private int id;
                    private String title;
                    private String imgpic;
                    private int counts;
                    private int uid;
                    private String nickname;
                    private String video;
                    private String playtime;
                    private int collection;
                    //private String imgpic_link;
//                    private String video_link;
                    private MusicInfo.DataBean.VideoInfoBean video_info;

                    public static class VideoInfoBean implements Serializable {

                        /**
                         * ext : mp3
                         * size : 18302306
                         * playtime : 07:37
                         * bitrate : 320
                         * md5 : 4b1b917652c542247565aa5ace5a7a5c
                         * link : http://api.demo.com//music/4b1b917652c542247565aa5ace5a7a5c/1
                         */

                        private String ext;
                        private int size;
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

                    public MusicInfo.DataBean.VideoInfoBean getVideo_info() {
                        return video_info;
                    }

                    public void setVideo_info(MusicInfo.DataBean.VideoInfoBean video_info) {
                        this.video_info = video_info;
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

                    public String getPlaytime() {
                        return playtime;
                    }

                    public void setPlaytime(String playtime) {
                        this.playtime = playtime;
                    }

                    public int getCollection() {
                        return collection;
                    }

                    public void setCollection(int collection) {
                        this.collection = collection;
                    }

//                    public String getImgpic_link() {
//                        return imgpic_link;
//                    }
//
//                    public void setImgpic_link(String imgpic_link) {
//                        this.imgpic_link = imgpic_link;
//                    }

//                    public String getVideo_link() {
//                        return video_link;
//                    }
//
//                    public void setVideo_link(String video_link) {
//                        this.video_link = video_link;
//                    }
                }
            }
        }

        public static class MusicBeanX implements Serializable {
            private int id;
            private String title;
            //            private String imgpic_link;
            private String url;


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

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class SystemMsgBean implements Serializable {
            /**
             * {
             * "id": 3,
             * "type": "2",
             * "jump_type": "web",
             * "url": "http://www.yujianni.top/news/comp_artinfo.html?id=101",
             * "config_id": 0,
             * "imgpic": "c7adcb987e5224301258c6f7efb2d53e",
             * "start_time": "2018-02-02 08:00:00",
             * "end_time": "2018-03-31 08:00:00",
             * "text": "12423432424",
             * "imgpic_link": "http://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e"
             * }
             */
            private int id;
            private int config_id;
            private String type;
            private String jump_type;
            private String url;
            private String imgpic;
            private String imgpic_link;
            private String start_time;
            private String end_time;
            private String text;


//            private ImgpicInfoBean imgpic_info;
//            public ImgpicInfoBean getImgpic_info() {
//                return imgpic_info;
//            }

            //            public void setImgpic_info(ImgpicInfoBean imgpic_info) {
//                this.imgpic_info = imgpic_info;
//            }
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

            public int getConfig_id() {
                return config_id;
            }

            public void setConfig_id(int config_id) {
                this.config_id = config_id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getJump_type() {
                return jump_type;
            }

            public void setJump_type(String jump_type) {
                this.jump_type = jump_type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
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

            //
            public void setImgpic_link(String imgpic_link) {
                this.imgpic_link = imgpic_link;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }


        public static class SongBeanX implements Serializable {
            /**
             * song : [{"id":534,"title":"【自荐歌单Ⅰ】这些古风有没有惊艳你的时光\u2014秋日祭","counts":3809,"imgpic":"747be53850c36b096703509761a8a3479f2f2c16","uid":36534,"nickname":"阿喵雅","imgpic_link":"http://yyt.demo.com/image/747be53850c36b096703509761a8a3479f2f2c16"},{"id":11,"title":"日翻专业推荐师","counts":708,"imgpic":"4bf14dfb79ce0f3607689c72f244c904eef43054","uid":32366,"nickname":"机器人不是机器人是人","imgpic_link":"http://yyt.demo.com/image/4bf14dfb79ce0f3607689c72f244c904eef43054"},{"id":930,"title":"二十四节气系列","counts":862,"imgpic":"278074b680b723af5a0ff2d74059a57cbdd52eff","uid":36947,"nickname":"子归","imgpic_link":"http://yyt.demo.com/image/278074b680b723af5a0ff2d74059a57cbdd52eff"},{"id":187,"title":"荷笙-超美日翻合集","counts":1067,"imgpic":"fff976424063496a8f2c28e1ae170c86f2fd1a45","uid":666,"nickname":"源音塘搞事情委员会","imgpic_link":"http://yyt.demo.com/image/fff976424063496a8f2c28e1ae170c86f2fd1a45"},{"id":448,"title":"这些让我们忘了原唱的翻唱","counts":978,"imgpic":"313d57757c2f1e3371d1a66a6c90f7d9b60dbc29","uid":36534,"nickname":"阿喵雅","imgpic_link":"http://yyt.demo.com/image/313d57757c2f1e3371d1a66a6c90f7d9b60dbc29"},{"id":2250,"title":"《九张机》29个版本大全","counts":75,"imgpic":"2bf20854d7d5b1c933dc2f5c9c49d09589e92d39","uid":36782,"nickname":"文浩","imgpic_link":"http://yyt.demo.com/image/2bf20854d7d5b1c933dc2f5c9c49d09589e92d39"}]
             * cate_img : {"id":100,"title":"首页图片配置","imgpic":"5cffbd9c97c844d07d2305e765db036f7b57668b","url":"","imgpic_link":"http://yyt.demo.com/image/5cffbd9c97c844d07d2305e765db036f7b57668b"}
             */

            private CateImgBeanX cate_img;
            private List<SongBean> song;

            public CateImgBeanX getCate_img() {
                return cate_img;
            }

            public void setCate_img(CateImgBeanX cate_img) {
                this.cate_img = cate_img;
            }

            public List<SongBean> getSong() {
                return song;
            }

            public void setSong(List<SongBean> song) {
                this.song = song;
            }

            public static class CateImgBeanX implements Serializable {
                /**
                 * id : 100
                 * title : 首页图片配置
                 * imgpic : 5cffbd9c97c844d07d2305e765db036f7b57668b
                 * url :
                 * imgpic_link : http://yyt.demo.com/image/5cffbd9c97c844d07d2305e765db036f7b57668b
                 */

                private int id;
                private String title;
                private String imgpic;
                private String url;
                private String imgpic_link;


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

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getImgpic_link() {
                    return imgpic_link;
                }

                public void setImgpic_link(String imgpic_link) {
                    this.imgpic_link = imgpic_link;
                }
            }

            public static class SongBean implements Serializable {
                /**
                 * id : 534
                 * title : 【自荐歌单Ⅰ】这些古风有没有惊艳你的时光—秋日祭
                 * counts : 3809
                 * imgpic : 747be53850c36b096703509761a8a3479f2f2c16
                 * uid : 36534
                 * nickname : 阿喵雅
                 * imgpic_link : http://yyt.demo.com/image/747be53850c36b096703509761a8a3479f2f2c16
                 */

                private int id;
                private String title;
                private int counts;
                private String imgpic;
                private String counts_text;
                private int uid;
                private String nickname;
                private String imgpic_link;
                private Boolean check;


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
                    if (null == check) {
                        return false;
                    }
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
                public String getCounts_text() {
                    return counts_text;
                }

                public void setCounts_text(String counts_text) {
                    this.counts_text = counts_text;
                }

                public void setCounts(int counts) {
                    this.counts = counts;
                }

                public String getImgpic() {
                    return imgpic;
                }

                public void setImgpic(String imgpic) {
                    this.imgpic = imgpic;
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

                //
                public String getImgpic_link() {
                    return imgpic_link;
                }

                public void setImgpic_link(String imgpic_link) {
                    this.imgpic_link = imgpic_link;
                }
            }
        }

        public static class ShufflingBean implements Serializable {
            /**
             * id : 107
             * title : 源音塘秋日祭音乐会
             * alias : activity
             * imgpic : 3194248a8acb66ff065ce56e3ede69566c938bf9
             * url : http://app.yuanyintang.com/autumnvote2017
             * item_type : 2
             * content : 无音乐不梦想，属于你的闪光时刻！让创作不孤单，让音乐更音乐，让世界听到你的声音！自源音塘“音乐人培养计划”上线以来，源酱与音乐人朋友们在多个领域尝试共同突破。也许我们现在还不是那个完美的自己，但是有了你们，源酱有勇气大胆的不断前行。源音塘秋日祭音乐会正式启动！丰富大赛奖励、海量曝光机会、商业合作推荐，源酱在这里等着你！
             * imgpic_link : http://yyt.demo.com/image/3194248a8acb66ff065ce56e3ede69566c938bf9
             */

            private int id;
            private String title;
            private String alias;
            private String imgpic;
            private String url;
            private int item_type;
            private String content;
            private String imgpic_link;


            private ImgpicInfoBean imgpic_info;

            public ImgpicInfoBean getImgpic_info() {
                return imgpic_info;
            }

            @Override
            public String toString() {
                return "ShufflingBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", alias='" + alias + '\'' +
                        ", imgpic='" + imgpic + '\'' +
                        ", url='" + url + '\'' +
                        ", item_type=" + item_type +
                        ", content='" + content + '\'' +
                        ", imgpic_link='" + imgpic_link + '\'' +
                        ", imgpic_info=" + imgpic_info +
                        '}';
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

            public String getImgpic_link() {
                return imgpic_link;
            }

            public void setImgpic_link(String imgpic_link) {
                this.imgpic_link = imgpic_link;
            }
        }

        public static class CatemusicBean implements Serializable {
            /**
             * id : 20
             * title : Vocaloid
             * alias : Vocaloid
             * pid : 0
             * sort : 0
             * status : 1
             * imgpic : bd6b6236c27e8a505e4660cbf77ddeddcc71b8a1
             * music_img :
             * music : []
             * imgpic_link : http://yyt.demo.com/image/bd6b6236c27e8a505e4660cbf77ddeddcc71b8a1
             */

            private int id;
            private String title;
            private String alias;
            private int pid;
            private int sort;
            private int status;
            private String imgpic;
            private String music_img;
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


            private List<CatemusicBeanX> music = new ArrayList<>();

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

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getImgpic() {
                return imgpic;
            }

            public void setImgpic(String imgpic) {
                this.imgpic = imgpic;
            }

            public String getMusic_img() {
                return music_img;
            }

            public void setMusic_img(String music_img) {
                this.music_img = music_img;
            }

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }

            public List<CatemusicBeanX> getMusic() {
                return music;
            }

            public void setMusic(List<CatemusicBeanX> music) {
                this.music = music;
            }

            public static class CatemusicBeanX implements Serializable {
                private int id;
                private String title;
                private int counts;
                //                private String imgpic_link;
                private String uid;
                private String nickname;
                private Boolean check;

                private boolean isPlaying;

                public boolean isPlaying() {
                    return isPlaying;
                }

                public void setPlaying(boolean playing) {
                    isPlaying = playing;
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


                public Boolean getCheck() {
                    if (check == null) {
                        return false;
                    }
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

//                public String getImgpic_link() {
//                    return imgpic_link;
//                }
//
//                public void setImgpic_link(String imgpic_link) {
//                    this.imgpic_link = imgpic_link;
//                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }
            }
        }

        public static class CustomlistBean implements Serializable {
            /**
             * id : 3992
             * title : 半阙词【哆啦】
             * imgpic : ded93b3bcd0600bbf0d957772c7f011966d1b426
             * counts : 362
             * uid : 36674
             * nickname : 哆啦
             * video : e0af64f0a7e90d7975cf09b6a011783303c307af
             * playtime : 05:23
             * collection : 0
             * imgpic_link : http://yyt.demo.com/image/ded93b3bcd0600bbf0d957772c7f011966d1b426
             * video_link : http://yyt.demo.com/music/e0af64f0a7e90d7975cf09b6a011783303c307af
             */

            private int id;
            private String title;
            private String imgpic;
            private int counts;
            private int uid;
            private String nickname;
            private String video;
            private String playtime;
            private int collection;
            //            private String imgpic_link;
//            private String video_link;
            private MusicInfo.DataBean.VideoInfoBean video_info;

            public static class VideoInfoBean implements Serializable {

                /**
                 * ext : mp3
                 * size : 18302306
                 * playtime : 07:37
                 * bitrate : 320
                 * md5 : 4b1b917652c542247565aa5ace5a7a5c
                 * link : http://api.demo.com//music/4b1b917652c542247565aa5ace5a7a5c/1
                 */

                private String ext;
                private int size;
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

            public MusicInfo.DataBean.VideoInfoBean getVideo_info() {
                return video_info;
            }

            public void setVideo_info(MusicInfo.DataBean.VideoInfoBean video_info) {
                this.video_info = video_info;
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

            public String getPlaytime() {
                return playtime;
            }

            public void setPlaytime(String playtime) {
                this.playtime = playtime;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
            }

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }

//            public String getVideo_link() {
//                return video_link;
//            }
//
//            public void setVideo_link(String video_link) {
//                this.video_link = video_link;
//            }
        }

        public static class MusicnewBean implements Serializable {
            /**
             * id : 5681
             * title : 《大鱼》大鱼海棠印象曲
             * imgpic : 965748235c49b69ced2e895135c04dc03b6a1543
             * counts : 207
             * uid : 40619
             * nickname : 一颗大柠檬
             * video : 290c34ab04ff89d951da6f30bed889970f3a36b8
             * playtime : 05:20
             * collection : 0
             * imgpic_link : http://yyt.demo.com/image/965748235c49b69ced2e895135c04dc03b6a1543
             * video_link : http://yyt.demo.com/music/290c34ab04ff89d951da6f30bed889970f3a36b8
             */

            private int id;
            private String title;
            private String imgpic;
            private int counts;
            private int uid;
            private String nickname;
            private String video;
            private String playtime;
            private int collection;
            //            private String imgpic_link;
//            private String video_link;
            private MusicInfo.DataBean.VideoInfoBean video_info;

            public static class VideoInfoBean implements Serializable {

                /**
                 * ext : mp3
                 * size : 18302306
                 * playtime : 07:37
                 * bitrate : 320
                 * md5 : 4b1b917652c542247565aa5ace5a7a5c
                 * link : http://api.demo.com//music/4b1b917652c542247565aa5ace5a7a5c/1
                 */

                private String ext;
                private int size;
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

            public MusicInfo.DataBean.VideoInfoBean getVideo_info() {
                return video_info;
            }

            public void setVideo_info(MusicInfo.DataBean.VideoInfoBean video_info) {
                this.video_info = video_info;
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

            public String getPlaytime() {
                return playtime;
            }

            public void setPlaytime(String playtime) {
                this.playtime = playtime;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
            }

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }

//            public String getVideo_link() {
//                return video_link;
//            }
//
//            public void setVideo_link(String video_link) {
//                this.video_link = video_link;
//            }
        }

        public static class TopicBean implements Serializable {

            /**
             * thcount : 8
             * hashtag : [{"title":"简介简介简介简介","id":255},{"title":"111111111111","id":263},{"title":"222222222222","id":264}]
             * nickname : 源音塘160944
             * head : 3d3d27527715c56204c6e063e34883ae60384a97
             * imglist : 27b611ff31df7b11c5a65f0c375f1cf9,afa61e64d183eeec01c0f8abc0274c99,3cec12c50c55755dcb915fba55f26a0b,b262f3ed7b07f27f4778204babfe5a17,c8af8a6bac443a0f095e82b088366afb,68c70ef957bb013834867c397287fb65,27d0b765fe7745bb0d2b473df975fcca,0867d34ce0cef8156b5464046a84c868,da21c54b56c3e1d255097af3f0bc475d
             * head_link : http://yyt.demo.com/image/3d3d27527715c56204c6e063e34883ae60384a97
             * title : 哈哈哈哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或
             * is_music : 0
             * id : 1065
             * member_type : 1
             * imglist_link : ["http://yyt.demo.com/image/27b611ff31df7b11c5a65f0c375f1cf9","http://yyt.demo.com/image/afa61e64d183eeec01c0f8abc0274c99","http://yyt.demo.com/image/3cec12c50c55755dcb915fba55f26a0b","http://yyt.demo.com/image/b262f3ed7b07f27f4778204babfe5a17","http://yyt.demo.com/image/c8af8a6bac443a0f095e82b088366afb","http://yyt.demo.com/image/68c70ef957bb013834867c397287fb65","http://yyt.demo.com/image/27d0b765fe7745bb0d2b473df975fcca","http://yyt.demo.com/image/0867d34ce0cef8156b5464046a84c868","http://yyt.demo.com/image/da21c54b56c3e1d255097af3f0bc475d"]
             * uid : 31234
             * hits : 12
             * create_time : 10-20
             */

            private int thcount;
            private String nickname;
            private String head;
            private String imglist;
            private String head_link;
            private String title;
            private int is_music;
            private int id;
            private int member_type;
            private int uid;
            private int hits;
            private String create_time;
            private List<HashtagBean> hashtag;
            private List<String> imglist_link;
            private List<ImglistInfoBean> imglist_info;

            public static class ImglistInfoBean implements Serializable {
                /**
                 * ext : jpg
                 * w : 200
                 * h : 200
                 * size : 9329
                 * is_long : 0
                 * link : http://api.demo.com//image/ce81fba58689fa96ab2ad9860a6b2461/3
                 * md5 : ce81fba58689fa96ab2ad9860a6b2461
                 */

                private String ext;
                private String w;
                private String h;
                private String size;
                private String is_long;
                private String link;
                private String md5;

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

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }
            }

            public List<ImglistInfoBean> getImglist_info() {
                return imglist_info;
            }

            public void setImglist_info(List<ImglistInfoBean> imglist_info) {
                this.imglist_info = imglist_info;
            }

            public int getThcount() {
                return thcount;
            }

            public void setThcount(int thcount) {
                this.thcount = thcount;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getImglist() {
                return imglist;
            }

            public void setImglist(String imglist) {
                this.imglist = imglist;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getIs_music() {
                return is_music;
            }

            public void setIs_music(int is_music) {
                this.is_music = is_music;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMember_type() {
                return member_type;
            }

            public void setMember_type(int member_type) {
                this.member_type = member_type;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getHits() {
                return hits;
            }

            public void setHits(int hits) {
                this.hits = hits;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public List<HashtagBean> getHashtag() {
                return hashtag;
            }

            public void setHashtag(List<HashtagBean> hashtag) {
                this.hashtag = hashtag;
            }

            public List<String> getImglist_link() {
                return imglist_link;
            }

            public void setImglist_link(List<String> imglist_link) {
                this.imglist_link = imglist_link;
            }

            public static class HashtagBean implements Serializable {
                /**
                 * title : 简介简介简介简介
                 * id : 255
                 */

                private String title;
                private int id;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }
            }
        }

        public static class LotteryBean implements Serializable {
            private int id;
            private int config_id;
            private String type;
            private String jump_type;
            private String url;
            private String imgpic;
            //            private String imgpic_link;
            private String start_time;
            private String end_time;
            private String text;


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

            public int getConfig_id() {
                return config_id;
            }

            public void setConfig_id(int config_id) {
                this.config_id = config_id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getJump_type() {
                return jump_type;
            }

            public void setJump_type(String jump_type) {
                this.jump_type = jump_type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getImgpic() {
                return imgpic;
            }

            public void setImgpic(String imgpic) {
                this.imgpic = imgpic;
            }

//            public String getImgpic_link() {
//                return imgpic_link;
//            }
//
//            public void setImgpic_link(String imgpic_link) {
//                this.imgpic_link = imgpic_link;
//            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }


        public static class BillboardBean implements Serializable {
            /**
             * billboards : [{"id":1,"title":"\r\n全站甜甜圈榜","imgpic":"bf619d9f6694960296d3135082ed7653d9f3af9c","border_color":"FFFFFF","music":{"id":7387,"title":"改名了ii","video":"8cdd1a74699ec80aff590e8a6763f734","imgpic":"22e20b1b3e5329d7deb99112d530a9a7","intro":"8888","video_link":"http://api.demo.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=1","video_info":{"ext":"mp3","size":5245950,"playtime":"02:09","bitrate":"324","md5":"8cdd1a74699ec80aff590e8a6763f734","link":"http://api.demo.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=1"},"imgpic_link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1","imgpic_info":{"ext":"png","w":"480","h":"768","size":"309737","is_long":"0","md5":"22e20b1b3e5329d7deb99112d530a9a7","link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1"}},"imgpic_link":"http://api.demo.com//image/bf619d9f6694960296d3135082ed7653d9f3af9c/1","imgpic_info":{"ext":"png","w":"480","h":"768","size":"309737","is_long":"0","md5":"22e20b1b3e5329d7deb99112d530a9a7","link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1"}}]
             * cate_img : {"id":65,"title":"排行榜","imgpic":"1b8ef65106379bfe32cedca5428a0219","url":"","place":"","alias":"billboard","imgpic_link":"http://api.demo.com//image/1b8ef65106379bfe32cedca5428a0219/1","imgpic_info":{"ext":"png","w":"480","h":"768","size":"309737","is_long":"0","md5":"22e20b1b3e5329d7deb99112d530a9a7","link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1"}}
             */

            @SerializedName("cate_img")
            private CateImgBean cate_imgX;
            private List<BillboardsBean> billboards;

            public CateImgBean getCate_imgX() {
                return cate_imgX;
            }

            public void setCate_imgX(CateImgBean cate_imgX) {
                this.cate_imgX = cate_imgX;
            }

            public List<BillboardsBean> getBillboards() {
                return billboards;
            }

            public void setBillboards(List<BillboardsBean> billboards) {
                this.billboards = billboards;
            }

            public static class CateImgBean implements Serializable {
                /**
                 * id : 65
                 * title : 排行榜
                 * imgpic : 1b8ef65106379bfe32cedca5428a0219
                 * url :
                 * place :
                 * alias : billboard
                 * imgpic_link : http://api.demo.com//image/1b8ef65106379bfe32cedca5428a0219/1
                 * imgpic_info : {"ext":"png","w":"480","h":"768","size":"309737","is_long":"0","md5":"22e20b1b3e5329d7deb99112d530a9a7","link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1"}
                 */

                private int id;
                private String title;
                private String imgpic;
                private String url;
                private String place;
                private String alias;
                private String imgpic_link;
                private ImgpicInfoBean imgpic_info;

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

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getPlace() {
                    return place;
                }

                public void setPlace(String place) {
                    this.place = place;
                }

                public String getAlias() {
                    return alias;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
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

            public static class BillboardsBean implements Serializable, com.chad.library.adapter.base.entity.MultiItemEntity {

                /**
                 * id : 1
                 * type : 1
                 * title : 全站甜甜圈榜
                 * imgpic : c7adcb987e5224301258c6f7efb2d53e
                 * border_bg_color : f69f00
                 * icon : c7adcb987e5224301258c6f7efb2d53e
                 * music_class_id : 0
                 * music : {"title":"锦鲤抄","head":"0e60393614c18b2003557d08622139c8","is_music":3,"video":"6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a","imgpic":"009d7c2c8205b6e66e2497776f69e690","nickname":"孤城?白帝","id":7334,"uid":50448,"comment":2,"counts":612,"coin":42,"ttq_total":"25","head_link":"https://api.demo.com//image/0e60393614c18b2003557d08622139c8/1","head_info":{"ext":"","w":"","h":"","size":"","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"https://api.demo.com//image/0e60393614c18b2003557d08622139c8/1"},"video_link":"https://api.demo.com//music/6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a.mp3?log_at=1","video_info":{"ext":"wav","size":"43146266","playtime":"04:04","bitrate":"1411","md5":"6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a","link":"http://api.demo.com//music/6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a.mp3?log_at=1"},"imgpic_link":"https://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/1","imgpic_info":{"ext":"","w":"","h":"","size":"","is_long":"0","md5":"009d7c2c8205b6e66e2497776f69e690","link":"https://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/1"},"comment_text":2,"counts_text":612}
                 * imgpic_link : https://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1
                 * imgpic_info : {"ext":"","w":"","h":"","size":"","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"https://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1"}
                 * icon_link : https://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1
                 * icon_info : {"ext":"","w":"","h":"","size":"","is_long":"0","md5":"c7adcb987e5224301258c6f7efb2d53e","link":"https://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1"}
                 */

                private int id;
                private int type;
                private String title;
                private String imgpic;
                private String border_bg_color;
                private String icon;
                private int music_class_id;
                private MusicBean music;
                private String imgpic_link;
                private ImgpicInfoBeanX imgpic_info;
                private String icon_link;
                private IconInfoBean icon_info;
                /**
                 * member : {"id":6450,"score":1,"total":1700,"member_id":35,"nickname":"KuRy颜酱","sex":0,"signature":"新浪微博@KuRy颜酱||粉丝群489812653~","head":"7882a19e44b919c22207ec153029385aa5acb7a1","is_music":3,"grow_status":1,"head_link":"http://api.demo.com//image/7882a19e44b919c22207ec153029385aa5acb7a1/1"}
                 */


                private MemberBean member;
                /**
                 * toggle_class : {"id":14,"title":"贡献榜单","type":3}
                 */

                private BillboardsBean toggle_class;


                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
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

                public String getBorder_bg_color() {
                    return border_bg_color;
                }

                public void setBorder_bg_color(String border_bg_color) {
                    this.border_bg_color = border_bg_color;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public int getMusic_class_id() {
                    return music_class_id;
                }

                public void setMusic_class_id(int music_class_id) {
                    this.music_class_id = music_class_id;
                }

                public MusicBean getMusic() {
                    return music;
                }

                public void setMusic(MusicBean music) {
                    this.music = music;
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

                public String getIcon_link() {
                    return icon_link;
                }

                public void setIcon_link(String icon_link) {
                    this.icon_link = icon_link;
                }

                public IconInfoBean getIcon_info() {
                    return icon_info;
                }

                public void setIcon_info(IconInfoBean icon_info) {
                    this.icon_info = icon_info;
                }

                public MemberBean getMember() {
                    return member;
                }

                public void setMember(MemberBean member) {
                    this.member = member;
                }

                @Override
                public int getItemType() {
                    if (type==1||type==2){//music
                        return Constant.HomeCharts.MUSIC;
                    }else if (type==3||type==4){//
                        return Constant.HomeCharts.INCOME;
                    }
                    return 0;
                }

                public BillboardsBean getToggle_class() {
                    return toggle_class;
                }

                public void setToggle_class(BillboardsBean toggle_class) {
                    this.toggle_class = toggle_class;
                }

                public static class MusicBean implements Serializable{
                    /**
                     * title : 锦鲤抄
                     * head : 0e60393614c18b2003557d08622139c8
                     * is_music : 3
                     * video : 6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a
                     * imgpic : 009d7c2c8205b6e66e2497776f69e690
                     * nickname : 孤城?白帝
                     * id : 7334
                     * uid : 50448
                     * comment : 2
                     * counts : 612
                     * coin : 42
                     * ttq_total : 25
                     * head_link : https://api.demo.com//image/0e60393614c18b2003557d08622139c8/1
                     * head_info : {"ext":"","w":"","h":"","size":"","is_long":"0","md5":"0e60393614c18b2003557d08622139c8","link":"https://api.demo.com//image/0e60393614c18b2003557d08622139c8/1"}
                     * video_link : https://api.demo.com//music/6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a.mp3?log_at=1
                     * video_info : {"ext":"wav","size":"43146266","playtime":"04:04","bitrate":"1411","md5":"6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a","link":"http://api.demo.com//music/6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a.mp3?log_at=1"}
                     * imgpic_link : https://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/1
                     * imgpic_info : {"ext":"","w":"","h":"","size":"","is_long":"0","md5":"009d7c2c8205b6e66e2497776f69e690","link":"https://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/1"}
                     * comment_text : 2
                     * counts_text : 612
                     */

                    private String title;
                    private String head;
                    private int is_music;
                    private String video;
                    private String imgpic;
                    private String nickname;
                    private int id;
                    private int uid;
                    private int comment;
                    private int counts;
                    private int coin;
                    private String ttq_total;
                    private String head_link;
                    private HeadInfoBean head_info;
                    private String video_link;
                    private VideoInfoBean video_info;
                    private String imgpic_link;
                    private ImgpicInfoBean imgpic_info;
                    private String comment_text;
                    private String counts_text;

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

                    public int getIs_music() {
                        return is_music;
                    }

                    public void setIs_music(int is_music) {
                        this.is_music = is_music;
                    }

                    public String getVideo() {
                        return video;
                    }

                    public void setVideo(String video) {
                        this.video = video;
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

                    public int getCoin() {
                        return coin;
                    }

                    public void setCoin(int coin) {
                        this.coin = coin;
                    }

                    public String getTtq_total() {
                        return ttq_total;
                    }

                    public void setTtq_total(String ttq_total) {
                        this.ttq_total = ttq_total;
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

                    public String getComment_text() {
                        return comment_text;
                    }

                    public void setComment_text(String comment_text) {
                        this.comment_text = comment_text;
                    }

                    public String getCounts_text() {
                        return counts_text;
                    }

                    public void setCounts_text(String counts_text) {
                        this.counts_text = counts_text;
                    }

                    public static class HeadInfoBean {
                        /**
                         * ext :
                         * w :
                         * h :
                         * size :
                         * is_long : 0
                         * md5 : 0e60393614c18b2003557d08622139c8
                         * link : https://api.demo.com//image/0e60393614c18b2003557d08622139c8/1
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

                    public static class VideoInfoBean {
                        /**
                         * ext : wav
                         * size : 43146266
                         * playtime : 04:04
                         * bitrate : 1411
                         * md5 : 6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a
                         * link : http://api.demo.com//music/6fa63ceef76e7610c89d31e33bc14e9c42a7fa7a.mp3?log_at=1
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

                    public static class ImgpicInfoBean {
                        /**
                         * ext :
                         * w :
                         * h :
                         * size :
                         * is_long : 0
                         * md5 : 009d7c2c8205b6e66e2497776f69e690
                         * link : https://api.demo.com//image/009d7c2c8205b6e66e2497776f69e690/1
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

                public static class ImgpicInfoBeanX {
                    /**
                     * ext :
                     * w :
                     * h :
                     * size :
                     * is_long : 0
                     * md5 : c7adcb987e5224301258c6f7efb2d53e
                     * link : https://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1
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

                public static class IconInfoBean {
                    /**
                     * ext :
                     * w :
                     * h :
                     * size :
                     * is_long : 0
                     * md5 : c7adcb987e5224301258c6f7efb2d53e
                     * link : https://api.demo.com//image/c7adcb987e5224301258c6f7efb2d53e/1
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

                public static class MemberBean {
                    /**
                     * id : 6450
                     * score : 1
                     * total : 1700
                     * member_id : 35
                     * nickname : KuRy颜酱
                     * sex : 0
                     * signature : 新浪微博@KuRy颜酱||粉丝群489812653~
                     * head : 7882a19e44b919c22207ec153029385aa5acb7a1
                     * is_music : 3
                     * grow_status : 1
                     * head_link : http://api.demo.com//image/7882a19e44b919c22207ec153029385aa5acb7a1/1
                     */

                    private int id;
                    private int score;
                    private int total;
                    private int member_id;
                    private String nickname;
                    private int sex;
                    private String signature;
                    private String head;
                    private int is_music;
                    private int grow_status;
                    private String head_link;
                    private HeadInfoBean head_info;

                    public HeadInfoBean getHead_info() {
                        return head_info;
                    }

                    public void setHead_info(HeadInfoBean head_info) {
                        this.head_info = head_info;
                    }

                    public static class HeadInfoBean {
                        /**
                         * ext :
                         * w :
                         * h :
                         * size :
                         * is_long : 0
                         * md5 : 0e60393614c18b2003557d08622139c8
                         * link : https://api.demo.com//image/0e60393614c18b2003557d08622139c8/1
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

                    public void setId(int idX) {
                        this.id = idX;
                    }

                    public int getScore() {
                        return score;
                    }

                    public void setScore(int score) {
                        this.score = score;
                    }

                    public int getTotal() {
                        return total;
                    }

                    public void setTotal(int total) {
                        this.total = total;
                    }

                    public int getMember_id() {
                        return member_id;
                    }

                    public void setMember_id(int member_id) {
                        this.member_id = member_id;
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

                    public String getSignature() {
                        return signature;
                    }

                    public void setSignature(String signature) {
                        this.signature = signature;
                    }

                    public String getHead() {
                        return head;
                    }

                    public void setHead(String head) {
                        this.head = head;
                    }

                    public int getIs_music() {
                        return is_music;
                    }

                    public void setIs_music(int is_music) {
                        this.is_music = is_music;
                    }

                    public int getGrow_status() {
                        return grow_status;
                    }

                    public void setGrow_status(int grow_status) {
                        this.grow_status = grow_status;
                    }

                    public String getHead_link() {
                        return head_link;
                    }

                    public void setHead_link(String head_link) {
                        this.head_link = head_link;
                    }
                }
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

            public static class MusicBean implements Serializable {
                /**
                 * id : 7387
                 * title : 改名了ii
                 * video : 8cdd1a74699ec80aff590e8a6763f734
                 * imgpic : 22e20b1b3e5329d7deb99112d530a9a7
                 * intro : 8888
                 * video_link : http://api.demo.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=1
                 * video_info : {"ext":"mp3","size":5245950,"playtime":"02:09","bitrate":"324","md5":"8cdd1a74699ec80aff590e8a6763f734","link":"http://api.demo.com//music/8cdd1a74699ec80aff590e8a6763f734.mp3?log_at=1"}
                 * imgpic_link : http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1
                 * imgpic_info : {"ext":"png","w":"480","h":"768","size":"309737","is_long":"0","md5":"22e20b1b3e5329d7deb99112d530a9a7","link":"http://api.demo.com//image/22e20b1b3e5329d7deb99112d530a9a7/1"}
                 */

                private int id;
                private String title;
                private String video;
                private String imgpic;
                private String intro;
                private String video_link;
                private VideoInfoBean video_info;
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

                public String getVideo() {
                    return video;
                }

                public void setVideo(String video) {
                    this.video = video;
                }

                public String getImgpic() {
                    return imgpic;
                }

                public void setImgpic(String imgpic) {
                    this.imgpic = imgpic;
                }

                public String getIntro() {
                    return intro;
                }

                public void setIntro(String intro) {
                    this.intro = intro;
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

                public static class VideoInfoBean implements Serializable {

                    /**
                     * ext : mp3
                     * size : 18302306
                     * playtime : 07:37
                     * bitrate : 320
                     * md5 : 4b1b917652c542247565aa5ace5a7a5c
                     * link : http://api.demo.com//music/4b1b917652c542247565aa5ace5a7a5c/1
                     */

                    private String ext;
                    private int size;
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

                public String getImgpic_link() {
                    return imgpic_link;
                }

                public void setImgpic_link(String imgpic_link) {
                    this.imgpic_link = imgpic_link;
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

                public ImgpicInfoBean getImgpic_info() {
                    return imgpic_info;
                }

                public void setImgpic_info(ImgpicInfoBean imgpic_info) {
                    this.imgpic_info = imgpic_info;
                }
            }
        }
    }

    public static class CateImgBean {
        /**
         * alias : topic
         * title : 热门池塘
         * imgpic : 441c089d6b9f1f32ba02e49ac1f4c514
         * id : 111
         * url : 1
         * place : 池塘
         * imgpic_link : http://demoapi.yuanyintang.com/image/441c089d6b9f1f32ba02e49ac1f4c514
         */

        private String alias;
        private String title;
        private String imgpic;
        private int id;
        private String url;
        private String place;
        private String imgpic_link;


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


        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
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
    }
}

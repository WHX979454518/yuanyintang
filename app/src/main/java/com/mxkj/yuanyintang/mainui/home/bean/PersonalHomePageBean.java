package com.mxkj.yuanyintang.mainui.home.bean;

import java.io.Serializable;
import java.util.List;

public class PersonalHomePageBean implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"id":91721,"source":2,"mobile":"150****4551","nickname":"小三真子","easemob":0,"sex":1,"signature":"接作曲作词编曲","head":"964fa411b4c8d39a023cb49bd8858c30","back":"95aedf87a3ec22279d8c21a34a7b0e0dddbd5947","email":"1*********@qq.com","province":1,"city":34,"apptoken":"","token":"","waptoken":"","reg_time":1533801812,"reg_ip":"","ips_num":78089,"tip":"","qq":"","day":"1998-01-01","last_login_time":1535364126,"last_login_ip":"2101216894","is_auth":3,"is_music":0,"incode":"","tag":"93,83","mycode":"ZJ5NKW","coin_counts":4,"x":"","y":"","auth_state":2,"distinction":1,"share_counts":0,"log_num":4,"log_at":1,"fans_num":11,"member_type":1,"today_coin":0,"comment_counts":2,"fish":0.35,"interest_tag":[],"identity_tag":[{"id":93,"title":"唱见","code":"custom","selected":1},{"id":83,"title":"混音","code":"custom","selected":1}],"day_txt":"01-01","age":"21","province_text":"北京市","city_text":"北京市","bind":[],"count":{"releaseCount":82,"collectionCount":0,"topicCount":7,"songCount":0,"musicCount":0,"mycodeCount":0,"playlogcount":0,"attention":0,"coin_counts":4,"collection":0,"fans":11,"topicCount_text":"7","attention_text":"0","coin_counts_text":"4","collection_text":"0","fans_text":"11"},"attention_num":0,"share_mycode_url":"https://www.yuanyintang.com/invite?code=ZJ5NKW","share_mycode_url_link":"https://api.yuanyintang.com/files/image/incode/code/ZJ5NKW","realname":"小***","number":"111************111","is_relation":0,"to_relation":0,"share_url":"https://wap.yuanyintang.com/singer/91721","billboard_list":[{"type":2,"billboard_type":4,"score":8,"class_id":11,"toggle_info":{"class_id":10}}],"constellation":"摩羯座","seo":{"title":"小三真子 - 源音塘","description":"接作曲作词编曲 - 源音塘","keywords":"小三真子|接作曲作词编曲"},"is_self":0,"head_link":"https://api.yuanyintang.com/image/964fa411b4c8d39a023cb49bd8858c30/3","head_info":{"ext":"","w":"","h":"","size":"23093","is_long":"0","md5":"964fa411b4c8d39a023cb49bd8858c30","link":"https://api.yuanyintang.com/image/964fa411b4c8d39a023cb49bd8858c30/3"},"back_link":"https://api.yuanyintang.com/image/95aedf87a3ec22279d8c21a34a7b0e0dddbd5947/3","back_info":{"ext":"jpg","w":"1920","h":"360","size":"185178","is_long":"0","md5":"95aedf87a3ec22279d8c21a34a7b0e0dddbd5947","link":"https://api.yuanyintang.com/image/95aedf87a3ec22279d8c21a34a7b0e0dddbd5947/3"},"ips_num_text":"7.81万","coin_counts_text":"4","share_counts_text":"0","fans_num_text":"11","fish_text":"0.35"}
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
         * id : 91721
         * source : 2
         * mobile : 150****4551
         * nickname : 小三真子
         * easemob : 0
         * sex : 1
         * signature : 接作曲作词编曲
         * head : 964fa411b4c8d39a023cb49bd8858c30
         * back : 95aedf87a3ec22279d8c21a34a7b0e0dddbd5947
         * email : 1*********@qq.com
         * province : 1
         * city : 34
         * apptoken :
         * token :
         * waptoken :
         * reg_time : 1533801812
         * reg_ip :
         * ips_num : 78089
         * tip :
         * qq :
         * day : 1998-01-01
         * last_login_time : 1535364126
         * last_login_ip : 2101216894
         * is_auth : 3
         * is_music : 0
         * incode :
         * tag : 93,83
         * mycode : ZJ5NKW
         * coin_counts : 4
         * x :
         * y :
         * auth_state : 2
         * distinction : 1
         * share_counts : 0
         * log_num : 4
         * log_at : 1
         * fans_num : 11
         * member_type : 1
         * today_coin : 0
         * comment_counts : 2
         * fish : 0.35
         * interest_tag : []
         * identity_tag : [{"id":93,"title":"唱见","code":"custom","selected":1},{"id":83,"title":"混音","code":"custom","selected":1}]
         * day_txt : 01-01
         * age : 21
         * province_text : 北京市
         * city_text : 北京市
         * bind : []
         * count : {"releaseCount":82,"collectionCount":0,"topicCount":7,"songCount":0,"musicCount":0,"mycodeCount":0,"playlogcount":0,"attention":0,"coin_counts":4,"collection":0,"fans":11,"topicCount_text":"7","attention_text":"0","coin_counts_text":"4","collection_text":"0","fans_text":"11"}
         * attention_num : 0
         * share_mycode_url : https://www.yuanyintang.com/invite?code=ZJ5NKW
         * share_mycode_url_link : https://api.yuanyintang.com/files/image/incode/code/ZJ5NKW
         * realname : 小***
         * number : 111************111
         * is_relation : 0
         * to_relation : 0
         * share_url : https://wap.yuanyintang.com/singer/91721
         * billboard_list : [{"type":2,"billboard_type":4,"score":8,"class_id":11,"toggle_info":{"class_id":10}}]
         * constellation : 摩羯座
         * seo : {"title":"小三真子 - 源音塘","description":"接作曲作词编曲 - 源音塘","keywords":"小三真子|接作曲作词编曲"}
         * is_self : 0
         * head_link : https://api.yuanyintang.com/image/964fa411b4c8d39a023cb49bd8858c30/3
         * head_info : {"ext":"","w":"","h":"","size":"23093","is_long":"0","md5":"964fa411b4c8d39a023cb49bd8858c30","link":"https://api.yuanyintang.com/image/964fa411b4c8d39a023cb49bd8858c30/3"}
         * back_link : https://api.yuanyintang.com/image/95aedf87a3ec22279d8c21a34a7b0e0dddbd5947/3
         * back_info : {"ext":"jpg","w":"1920","h":"360","size":"185178","is_long":"0","md5":"95aedf87a3ec22279d8c21a34a7b0e0dddbd5947","link":"https://api.yuanyintang.com/image/95aedf87a3ec22279d8c21a34a7b0e0dddbd5947/3"}
         * ips_num_text : 7.81万
         * coin_counts_text : 4
         * share_counts_text : 0
         * fans_num_text : 11
         * fish_text : 0.35
         */

        private int id;
        private int source;
        private String mobile;
        private String nickname;
        private int easemob;
        private int sex;
        private String signature;
        private String head;
        private String back;
        private String email;
        private int province;
        private int city;
        private String apptoken;
        private String token;
        private String waptoken;
        private int reg_time;
        private String reg_ip;
        private int ips_num;
        private String tip;
        private String qq;
        private String day;
        private int last_login_time;
        private String last_login_ip;
        private int is_auth;
        private int is_music;
        private String incode;
        private String tag;
        private String mycode;
        private int coin_counts;
        private String x;
        private String y;
        private int auth_state;
        private int distinction;
        private int share_counts;
        private int log_num;
        private int log_at;
        private int fans_num;
        private int member_type;
        private int today_coin;
        private int comment_counts;
        private double fish;
        private String day_txt;
        private String age;
        private String province_text;
        private String city_text;
        private CountBean count;
        private int attention_num;
        private String share_mycode_url;
        private String share_mycode_url_link;
        private String realname;
        private String number;
        private int is_relation;
        private int to_relation;
        private String share_url;
        private String constellation;
        private SeoBean seo;
        private int is_self;
        private String head_link;
        private HeadInfoBean head_info;
        private String back_link;
        private BackInfoBean back_info;
        private String ips_num_text;
        private String coin_counts_text;
        private String share_counts_text;
        private String fans_num_text;
        private String fish_text;
        private List<?> interest_tag;
        private List<IdentityTagBean> identity_tag;
        private List<?> bind;
        private List<BillboardListBean> billboard_list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getEasemob() {
            return easemob;
        }

        public void setEasemob(int easemob) {
            this.easemob = easemob;
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

        public String getBack() {
            return back;
        }

        public void setBack(String back) {
            this.back = back;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public String getApptoken() {
            return apptoken;
        }

        public void setApptoken(String apptoken) {
            this.apptoken = apptoken;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getWaptoken() {
            return waptoken;
        }

        public void setWaptoken(String waptoken) {
            this.waptoken = waptoken;
        }

        public int getReg_time() {
            return reg_time;
        }

        public void setReg_time(int reg_time) {
            this.reg_time = reg_time;
        }

        public String getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(String reg_ip) {
            this.reg_ip = reg_ip;
        }

        public int getIps_num() {
            return ips_num;
        }

        public void setIps_num(int ips_num) {
            this.ips_num = ips_num;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(int last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public int getIs_auth() {
            return is_auth;
        }

        public void setIs_auth(int is_auth) {
            this.is_auth = is_auth;
        }

        public int getIs_music() {
            return is_music;
        }

        public void setIs_music(int is_music) {
            this.is_music = is_music;
        }

        public String getIncode() {
            return incode;
        }

        public void setIncode(String incode) {
            this.incode = incode;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getMycode() {
            return mycode;
        }

        public void setMycode(String mycode) {
            this.mycode = mycode;
        }

        public int getCoin_counts() {
            return coin_counts;
        }

        public void setCoin_counts(int coin_counts) {
            this.coin_counts = coin_counts;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public int getAuth_state() {
            return auth_state;
        }

        public void setAuth_state(int auth_state) {
            this.auth_state = auth_state;
        }

        public int getDistinction() {
            return distinction;
        }

        public void setDistinction(int distinction) {
            this.distinction = distinction;
        }

        public int getShare_counts() {
            return share_counts;
        }

        public void setShare_counts(int share_counts) {
            this.share_counts = share_counts;
        }

        public int getLog_num() {
            return log_num;
        }

        public void setLog_num(int log_num) {
            this.log_num = log_num;
        }

        public int getLog_at() {
            return log_at;
        }

        public void setLog_at(int log_at) {
            this.log_at = log_at;
        }

        public int getFans_num() {
            return fans_num;
        }

        public void setFans_num(int fans_num) {
            this.fans_num = fans_num;
        }

        public int getMember_type() {
            return member_type;
        }

        public void setMember_type(int member_type) {
            this.member_type = member_type;
        }

        public int getToday_coin() {
            return today_coin;
        }

        public void setToday_coin(int today_coin) {
            this.today_coin = today_coin;
        }

        public int getComment_counts() {
            return comment_counts;
        }

        public void setComment_counts(int comment_counts) {
            this.comment_counts = comment_counts;
        }

        public double getFish() {
            return fish;
        }

        public void setFish(double fish) {
            this.fish = fish;
        }

        public String getDay_txt() {
            return day_txt;
        }

        public void setDay_txt(String day_txt) {
            this.day_txt = day_txt;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getProvince_text() {
            return province_text;
        }

        public void setProvince_text(String province_text) {
            this.province_text = province_text;
        }

        public String getCity_text() {
            return city_text;
        }

        public void setCity_text(String city_text) {
            this.city_text = city_text;
        }

        public CountBean getCount() {
            return count;
        }

        public void setCount(CountBean count) {
            this.count = count;
        }

        public int getAttention_num() {
            return attention_num;
        }

        public void setAttention_num(int attention_num) {
            this.attention_num = attention_num;
        }

        public String getShare_mycode_url() {
            return share_mycode_url;
        }

        public void setShare_mycode_url(String share_mycode_url) {
            this.share_mycode_url = share_mycode_url;
        }

        public String getShare_mycode_url_link() {
            return share_mycode_url_link;
        }

        public void setShare_mycode_url_link(String share_mycode_url_link) {
            this.share_mycode_url_link = share_mycode_url_link;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getIs_relation() {
            return is_relation;
        }

        public void setIs_relation(int is_relation) {
            this.is_relation = is_relation;
        }

        public int getTo_relation() {
            return to_relation;
        }

        public void setTo_relation(int to_relation) {
            this.to_relation = to_relation;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public SeoBean getSeo() {
            return seo;
        }

        public void setSeo(SeoBean seo) {
            this.seo = seo;
        }

        public int getIs_self() {
            return is_self;
        }

        public void setIs_self(int is_self) {
            this.is_self = is_self;
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

        public String getBack_link() {
            return back_link;
        }

        public void setBack_link(String back_link) {
            this.back_link = back_link;
        }

        public BackInfoBean getBack_info() {
            return back_info;
        }

        public void setBack_info(BackInfoBean back_info) {
            this.back_info = back_info;
        }

        public String getIps_num_text() {
            return ips_num_text;
        }

        public void setIps_num_text(String ips_num_text) {
            this.ips_num_text = ips_num_text;
        }

        public String getCoin_counts_text() {
            return coin_counts_text;
        }

        public void setCoin_counts_text(String coin_counts_text) {
            this.coin_counts_text = coin_counts_text;
        }

        public String getShare_counts_text() {
            return share_counts_text;
        }

        public void setShare_counts_text(String share_counts_text) {
            this.share_counts_text = share_counts_text;
        }

        public String getFans_num_text() {
            return fans_num_text;
        }

        public void setFans_num_text(String fans_num_text) {
            this.fans_num_text = fans_num_text;
        }

        public String getFish_text() {
            return fish_text;
        }

        public void setFish_text(String fish_text) {
            this.fish_text = fish_text;
        }

        public List<?> getInterest_tag() {
            return interest_tag;
        }

        public void setInterest_tag(List<?> interest_tag) {
            this.interest_tag = interest_tag;
        }

        public List<IdentityTagBean> getIdentity_tag() {
            return identity_tag;
        }

        public void setIdentity_tag(List<IdentityTagBean> identity_tag) {
            this.identity_tag = identity_tag;
        }

        public List<?> getBind() {
            return bind;
        }

        public void setBind(List<?> bind) {
            this.bind = bind;
        }

        public List<BillboardListBean> getBillboard_list() {
            return billboard_list;
        }

        public void setBillboard_list(List<BillboardListBean> billboard_list) {
            this.billboard_list = billboard_list;
        }

        public static class CountBean {
            /**
             * releaseCount : 82
             * collectionCount : 0
             * topicCount : 7
             * songCount : 0
             * musicCount : 0
             * mycodeCount : 0
             * playlogcount : 0
             * attention : 0
             * coin_counts : 4
             * collection : 0
             * fans : 11
             * topicCount_text : 7
             * attention_text : 0
             * coin_counts_text : 4
             * collection_text : 0
             * fans_text : 11
             */

            private int releaseCount;
            private int collectionCount;
            private int topicCount;
            private int songCount;
            private int musicCount;
            private int mycodeCount;
            private int playlogcount;
            private int attention;
            private int coin_counts;
            private int collection;
            private int fans;
            private String topicCount_text;
            private String attention_text;
            private String coin_counts_text;
            private String collection_text;
            private String fans_text;

            public int getReleaseCount() {
                return releaseCount;
            }

            public void setReleaseCount(int releaseCount) {
                this.releaseCount = releaseCount;
            }

            public int getCollectionCount() {
                return collectionCount;
            }

            public void setCollectionCount(int collectionCount) {
                this.collectionCount = collectionCount;
            }

            public int getTopicCount() {
                return topicCount;
            }

            public void setTopicCount(int topicCount) {
                this.topicCount = topicCount;
            }

            public int getSongCount() {
                return songCount;
            }

            public void setSongCount(int songCount) {
                this.songCount = songCount;
            }

            public int getMusicCount() {
                return musicCount;
            }

            public void setMusicCount(int musicCount) {
                this.musicCount = musicCount;
            }

            public int getMycodeCount() {
                return mycodeCount;
            }

            public void setMycodeCount(int mycodeCount) {
                this.mycodeCount = mycodeCount;
            }

            public int getPlaylogcount() {
                return playlogcount;
            }

            public void setPlaylogcount(int playlogcount) {
                this.playlogcount = playlogcount;
            }

            public int getAttention() {
                return attention;
            }

            public void setAttention(int attention) {
                this.attention = attention;
            }

            public int getCoin_counts() {
                return coin_counts;
            }

            public void setCoin_counts(int coin_counts) {
                this.coin_counts = coin_counts;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
            }

            public int getFans() {
                return fans;
            }

            public void setFans(int fans) {
                this.fans = fans;
            }

            public String getTopicCount_text() {
                return topicCount_text;
            }

            public void setTopicCount_text(String topicCount_text) {
                this.topicCount_text = topicCount_text;
            }

            public String getAttention_text() {
                return attention_text;
            }

            public void setAttention_text(String attention_text) {
                this.attention_text = attention_text;
            }

            public String getCoin_counts_text() {
                return coin_counts_text;
            }

            public void setCoin_counts_text(String coin_counts_text) {
                this.coin_counts_text = coin_counts_text;
            }

            public String getCollection_text() {
                return collection_text;
            }

            public void setCollection_text(String collection_text) {
                this.collection_text = collection_text;
            }

            public String getFans_text() {
                return fans_text;
            }

            public void setFans_text(String fans_text) {
                this.fans_text = fans_text;
            }
        }

        public static class SeoBean {
            /**
             * title : 小三真子 - 源音塘
             * description : 接作曲作词编曲 - 源音塘
             * keywords : 小三真子|接作曲作词编曲
             */

            private String title;
            private String description;
            private String keywords;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }
        }

        public static class HeadInfoBean {
            /**
             * ext :
             * w :
             * h :
             * size : 23093
             * is_long : 0
             * md5 : 964fa411b4c8d39a023cb49bd8858c30
             * link : https://api.yuanyintang.com/image/964fa411b4c8d39a023cb49bd8858c30/3
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

        public static class BackInfoBean {
            /**
             * ext : jpg
             * w : 1920
             * h : 360
             * size : 185178
             * is_long : 0
             * md5 : 95aedf87a3ec22279d8c21a34a7b0e0dddbd5947
             * link : https://api.yuanyintang.com/image/95aedf87a3ec22279d8c21a34a7b0e0dddbd5947/3
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

        public static class IdentityTagBean {
            /**
             * id : 93
             * title : 唱见
             * code : custom
             * selected : 1
             */

            private int id;
            private String title;
            private String code;
            private int selected;

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

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }
        }

        public static class BillboardListBean {
            /**
             * type : 2
             * billboard_type : 4
             * score : 8
             * class_id : 11
             * toggle_info : {"class_id":10}
             */

            private int type;
            private int billboard_type;
            private int score;
            private int class_id;
            private ToggleInfoBean toggle_info;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getBillboard_type() {
                return billboard_type;
            }

            public void setBillboard_type(int billboard_type) {
                this.billboard_type = billboard_type;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getClass_id() {
                return class_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public ToggleInfoBean getToggle_info() {
                return toggle_info;
            }

            public void setToggle_info(ToggleInfoBean toggle_info) {
                this.toggle_info = toggle_info;
            }

            public static class ToggleInfoBean {
                /**
                 * class_id : 10
                 */

                private int class_id;

                public int getClass_id() {
                    return class_id;
                }

                public void setClass_id(int class_id) {
                    this.class_id = class_id;
                }
            }
        }
    }
}

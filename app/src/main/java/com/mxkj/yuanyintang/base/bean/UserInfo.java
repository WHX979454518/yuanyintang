package com.mxkj.yuanyintang.base.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;


public class UserInfo implements Serializable {

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

    public static class DataBean implements Serializable, MultiItemEntity {
        public int getTo_relation() {
            return to_relation;
        }
        public void setTo_relation(int to_relation) {
            this.to_relation = to_relation;
        }

        /**
         * id : 50963
         * source : 0
         * mobile :
         * nickname : 4月
         * easemob : 1
         * sex : 0
         * signature :
         * head : 3d3d27527715c56204c6e063e34883ae60384a97
         * back : 95aedf87a3ec22279d8c21a34a7b0e0dddbd5947
         * email :
         * province : 1
         * city : 34
         * reg_time : 1524555037
         * reg_ip : 192.168.1.50
         * ips_num : 0
         * tip :
         * qq :
         * day : 2018-04-24
         * last_login_time : 1534487256
         * last_login_ip : 192.168.1.50
         * is_auth : 1
         * is_music : 0
         * incode :
         * tag :
         * mycode : YPX2EJ
         * coin_counts : 299409
         * x : 104.0647
         * y : 30.548356
         * auth_state : 0
         * distinction : 0
         * share_counts : 0
         * log_num : 24
         * log_at : 1
         * fans_num : 0
         * today_coin : 0
         * comment_counts : 0
         * fish : 10000
         * interest_tag : [{"id":0,"title":"翻唱","code":"interest_music_type","selected":1},{"id":1,"title":"原创 ","code":"interest_music_type","selected":1},{"id":5,"title":"动漫游戏","code":"interest_music_class","selected":1},{"id":6,"title":"古风","code":"interest_music_class","selected":1},{"id":7,"title":"三次元","code":"interest_music_class","selected":0},{"id":19,"title":"Vocaloid","code":"interest_music_class","selected":0},{"id":22,"title":"活动作品","code":"interest_music_class","selected":0},{"id":222,"title":"招募","code":"interest_tag_class","selected":0},{"id":256,"title":"游戏","code":"interest_tag_class","selected":0},{"id":314,"title":"技能GET","code":"interest_tag_class","selected":0}]
         * identity_tag : [{"id":420,"title":"身份的标签1","code":"identity","selected":0},{"id":421,"title":"身份的标签2","code":"identity","selected":0},{"id":438,"title":"shenf1","code":"identity","selected":0},{"id":442,"title":"音乐人","code":"musician","selected":0},{"id":462,"title":"身份标签1","code":"identity","selected":0},{"id":463,"title":"身份标签2","code":"identity","selected":0},{"id":464,"title":"音乐人1","code":"musician","selected":0}]
         * day_txt : 04-24
         * province_text : 北京市
         * city_text : 北京市
         * bind : [{"id":1746,"type":1,"nickname":"4月","name":"4月","head":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIjHnUTMRMZ9XwWLePLVtzotc3J58AibbibbVoggz3KicPDSLVoibJNMVibj0Iw1cjQ8c460tqq5A1WprQ/132","openid":"o1tB2wZd1MUtHsfn2rzZTacz26So","head_link":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIjHnUTMRMZ9XwWLePLVtzotc3J58AibbibbVoggz3KicPDSLVoibJNMVibj0Iw1cjQ8c460tqq5A1WprQ/132"}]
         * count : {"attention":39,"fans":0,"coin_counts":299409,"playlogCount":16,"releaseCount":0,"collection":2,"collectionCount":0,"topicCount":0,"songCount":1,"mycodeCount":0,"musicCount":0,"collection_text":"2"}
         * attention_num : 39
         * share_mycode_url : http://wap.demo.com/invite?code=YPX2EJ
         * share_mycode_url_link : http://api.demo.com/files/image/incode/code/YPX2EJ
         * number :
         * realname :
         * logintoken : E9AC87689FA17174884C47BD0359F69A
         * device_token : AkmtXhbQ03XuiDON_Xla3WVrQlC-R9iiZXmxgamHpD7B
         * head_link : http://testapi.demo.com//image/3d3d27527715c56204c6e063e34883ae60384a97/3
         * head_info :
         * back_link : http://testapi.demo.com//image/95aedf87a3ec22279d8c21a34a7b0e0dddbd5947/3
         * back_info :
         * share_counts_text : 0
         */

        private int id;
        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
        private int is_relation;
        private int to_relation;
        private int source;
        private String age;
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
        private double x;
        private double y;
        private int auth_state;
        private int distinction;
        private int share_counts;
        private int log_num;
        private int log_at;
        private int fans_num;
        private int today_coin;
        private int comment_counts;
        private int fish;
        private int play_log_counts;
        private String day_txt;
        private String province_text;
        private String city_text;
        private CountBean count;
        private int attention_num;
        private int release_num;
        private String share_mycode_url;
        private String share_mycode_url_link;
        private String number;
        private String realname;
        private String logintoken;
        private String device_token;
        private String head_link;
        private String head_info;
        private String back_link;
        private String back_info;
        private String share_counts_text;

        private String is_self;

        private List<InterestTagBean> interest_tag;
        private List<IdentityTagBean> identity_tag;
        private List<BindBean> bind;

        private List<MusicBean> music;

        public List<MusicBean> getMusic() {
            return music;
        }

        public void setMusic(List<MusicBean> music) {
            this.music = music;
        }

        public String getIs_self() {
            return is_self;
        }

        public void setIs_self(String is_self) {
            this.is_self = is_self;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String  age) {
            this.age = age;
        }

        public int getRelease_num() {
            return release_num;
        }

        public void setRelease_num(int release_num) {
            this.release_num = release_num;
        }

        public int getPlay_log_counts() {
            return play_log_counts;
        }

        public void setPlay_log_counts(int play_log_counts) {
            this.play_log_counts = play_log_counts;
        }

        public int getIs_relation() {
            return is_relation;
        }

        public void setIs_relation(int is_relation) {
            this.is_relation = is_relation;
        }

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

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
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

        public int getFish() {
            return fish;
        }

        public void setFish(int fish) {
            this.fish = fish;
        }

        public String getDay_txt() {
            return day_txt;
        }

        public void setDay_txt(String day_txt) {
            this.day_txt = day_txt;
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

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getLogintoken() {
            return logintoken;
        }

        public void setLogintoken(String logintoken) {
            this.logintoken = logintoken;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }

        public String getHead_link() {
            return head_link;
        }

        public void setHead_link(String head_link) {
            this.head_link = head_link;
        }

        public String getHead_info() {
            return head_info;
        }

        public void setHead_info(String head_info) {
            this.head_info = head_info;
        }

        public String getBack_link() {
            return back_link;
        }

        public void setBack_link(String back_link) {
            this.back_link = back_link;
        }

        public String getBack_info() {
            return back_info;
        }

        public void setBack_info(String back_info) {
            this.back_info = back_info;
        }

        public String getShare_counts_text() {
            return share_counts_text;
        }

        public void setShare_counts_text(String share_counts_text) {
            this.share_counts_text = share_counts_text;
        }

        public List<InterestTagBean> getInterest_tag() {
            return interest_tag;
        }

        public void setInterest_tag(List<InterestTagBean> interest_tag) {
            this.interest_tag = interest_tag;
        }

        public List<IdentityTagBean> getIdentity_tag() {
            return identity_tag;
        }

        public void setIdentity_tag(List<IdentityTagBean> identity_tag) {
            this.identity_tag = identity_tag;
        }

        public List<BindBean> getBind() {
            return bind;
        }

        public void setBind(List<BindBean> bind) {
            this.bind = bind;
        }

        @Override
        public int getItemType() {
            return 0;
        }

        public static class CountBean implements Serializable {
            /**
             * attention : 39
             * fans : 0
             * coin_counts : 299409
             * playlogCount : 16
             * releaseCount : 0
             * collection : 2
             * collectionCount : 0
             * topicCount : 0
             * songCount : 1
             * mycodeCount : 0
             * musicCount : 0
             * collection_text : 2
             */

            private int attention;
            private int fans;
            private int coin_counts;
            private int playlogCount=0;
            private int releaseCount;
            private int collection;
            private int collectionCount;
            private int topicCount;
            private int songCount;
            private int mycodeCount;
            private int musicCount;
            private String collection_text;

            public int getAttention() {
                return attention;
            }

            public void setAttention(int attention) {
                this.attention = attention;
            }

            public int getFans() {
                return fans;
            }

            public void setFans(int fans) {
                this.fans = fans;
            }

            public int getCoin_counts() {
                return coin_counts;
            }

            public void setCoin_counts(int coin_counts) {
                this.coin_counts = coin_counts;
            }

            public int getPlaylogCount() {
                return playlogCount;
            }

            public void setPlaylogCount(int playlogCount) {
                this.playlogCount = playlogCount;
            }

            public int getReleaseCount() {
                return releaseCount;
            }

            public void setReleaseCount(int releaseCount) {
                this.releaseCount = releaseCount;
            }

            public int getCollection() {
                return collection;
            }

            public void setCollection(int collection) {
                this.collection = collection;
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

            public int getMycodeCount() {
                return mycodeCount;
            }

            public void setMycodeCount(int mycodeCount) {
                this.mycodeCount = mycodeCount;
            }

            public int getMusicCount() {
                return musicCount;
            }

            public void setMusicCount(int musicCount) {
                this.musicCount = musicCount;
            }

            public String getCollection_text() {
                return collection_text;
            }

            public void setCollection_text(String collection_text) {
                this.collection_text = collection_text;
            }
        }

        public static class InterestTagBean implements Serializable {
            /**
             * id : 0
             * title : 翻唱
             * code : interest_music_type
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

        public static class IdentityTagBean implements Serializable {
            /**
             * id : 420
             * title : 身份的标签1
             * code : identity
             * selected : 0
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

        public static class BindBean implements Serializable {
            /**
             * id : 1746
             * type : 1
             * nickname : 4月
             * name : 4月
             * head : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIjHnUTMRMZ9XwWLePLVtzotc3J58AibbibbVoggz3KicPDSLVoibJNMVibj0Iw1cjQ8c460tqq5A1WprQ/132
             * openid : o1tB2wZd1MUtHsfn2rzZTacz26So
             * head_link : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIjHnUTMRMZ9XwWLePLVtzotc3J58AibbibbVoggz3KicPDSLVoibJNMVibj0Iw1cjQ8c460tqq5A1WprQ/132
             */

            private int id;
            private int type;
            private String nickname;
            private String name;
            private String head;
            private String openid;
            private String head_link;

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

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getOpenid() {
                return openid;
            }

            public void setOpenid(String openid) {
                this.openid = openid;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }
        }

        public static class MusicBean implements Serializable {
            /**
             * item_id : 1746
             * title : "【翻唱】若当来世—《狐妖小红娘》OP"
             */

            private int item_id;
            private String title;

            @Override
            public String toString() {
                return "MusicBean{" +
                        "item_id=" + item_id +
                        ", title='" + title + '\'' +
                        '}';
            }

            public int getItem_id() {
                return item_id;
            }

            public void setItem_id(int item_id) {
                this.item_id = item_id;
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

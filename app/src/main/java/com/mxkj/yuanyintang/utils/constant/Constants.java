package com.mxkj.yuanyintang.utils.constant;

import android.os.Environment;

import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.io.File;
import java.io.Serializable;

/**
 * Created by LiuJie on 2017/6/8.
 */
public class Constants implements Serializable {
    public static class User {
        public static final String IS_LOGIN = "is_login";//是否登录过
        public static final String USER_ID = "user_id";//自己的id
        public static final String USER_JSON = "user_json";
        public static final String MUSIC_KBP = "music_KBPS";//下载码率
        public static final String USER_DEVICE_TOKEN = "deviceToken";//token
        public static final String SHOW_GUIDE_PREFIX = "show_guide_on_view_";
        public static final String USER_LOGIN_TOKEN = "user_login_token";
        public static final String MUSIC_DIR_PRIVATE = "MUSIC_DIR_PRIVATE";
        public static String FIRST_TIME = "first_time";
    }

    public static class Other {
        public static final String NOT_WIFI_NET_DOWNLOAD_FILE = "openNetDownload";//没有wifi，不能下载
        public static final String UP_APK_TIME = "up_apk_time";//更新apk时间，一天内不显示弹窗
        public static final String IS_SETTING_MSG_SPEAKER = "_is_setting_msg_speaker";//
        public static final String IS_SETTING_MSG_VIBRATE = "_is_setting_msg_vibrate";//设置消息震动
        public static final String IS_SETTING_MSG_SOUND = "_is_setting_msg_sound";//设置消息声音
        public static final String IS_SETTING_MSG_NOTIFICATION = "_is_setting_msg_notification";//设置通知栏
        public static final String IS_SETTING_MSG_HELLO = "_is_setting_msg_hello";//打招呼状态显示
        public static final String IS_NIGHT_NOT_DISTURB_MSG = "_is_night_not_disturb_msg";//夜间免打扰设置
        public static final String IS_NOT_CONCERN_NOT_DISTURB_MSG = "_is_not_concern_not_disturb_msg";//未关注人收入消息盒子
        public static final String MSG_HELL_CONTENT = "_msg_hell_content";//打招呼的消息内容
        public static final String WIDTH = "width";//宽度
        public static final String CHARTS_BEAN = "charts_bean";
        public static String UMENG_DEVICEKEY = "umeng_device_key";
        public static String NEED_RE_LAUNCH = "need_re_launch";
        public static final String SPLASH_PIC_DATA = "START_IMG_JJSON";
        public static final String EM_LOGIN = "EM_LOGIN";
        public static java.lang.String YXY_CHAT_HISTOTY = "YXY_CHAT_HISTOTY";
        public static java.lang.String WX_APP_ID = "wx67591dc5fc8029ac";

        public static Boolean getIsSettingMsgSpeaker() {
            return CacheUtils.INSTANCE.getBoolean(MainApplication.Companion.getApplication(), IS_SETTING_MSG_SPEAKER, false);
        }

        public static Boolean getIsSettingMsgVibrate() {
            return CacheUtils.INSTANCE.getBoolean(MainApplication.Companion.getApplication(), IS_SETTING_MSG_VIBRATE, false);
        }

        public static Boolean getIsSettingMsgSound() {
            return CacheUtils.INSTANCE.getBoolean(MainApplication.Companion.getApplication(), IS_SETTING_MSG_SOUND, false);
        }

        public static Boolean getIsSettingMsgNotification() {
            return CacheUtils.INSTANCE.getBoolean(MainApplication.Companion.getApplication(), IS_SETTING_MSG_NOTIFICATION, false);
        }

        public static Boolean getIsSettingMsgHello() {
            return CacheUtils.INSTANCE.getBoolean(MainApplication.Companion.getApplication(), IS_SETTING_MSG_HELLO, false);
        }
    }

    public static class Message {

    }

    public static class LAST_PLAY {
        public static String BEAN_STR = "beanStr";
        public static String POSITION = "position";




    }


    public static class DataManager {
        public static final String CITY_DATA_JSON = "_city_data_json";//获取城市
        public static final String HOME_DATA_JSON = "_home_data_json";//缓存首页HomeFragment
        public static final String HOME_BANNER_DATA_JSON = "_home_banner_data_json";//缓存首页bannerHomeFragment
        public static final String HOME_SYSTEM_MSG_DATA_JSON = "_home_system_msg_data_json";//缓存首页bannerHomeFragment
        public static final String HOME_SONG_DATA_JSON = "_home_song_data_json";//缓存首頁歌单HomeFragment
        public static final String HOME_MUSICIAN_DATA_JSON = "_home_musician_data_json";//缓存首页音乐人HomeFragment
        public static final String HOME_MUSIC_DATA_JSON = "_home_music_data_json";//缓存首页音乐HomeFragment
        public static final String HOME_POND_DATA_JSON = "_home_pond_data_json";//缓存首页池塘HomeFragment
        public static final String SEARCH_HOT_DATA_JSON = "_search_hot_data_json";//缓存搜索热词
        public static final String POND_DATA_JSON = "_pond_data_json";//缓存池塘PondFragment
        public static final String POND_HOT_TAG_DATA_JSON = "_pond_hot_tag_data_json";//缓存池塘热门标签PondFragment
        public static final String POND_BANNER_DATA_JSON = "_pond_banner_data_json";//缓存池塘bannerPondFragment
        public static final String DYNAMIC_DATA_JSON = "_dynamic_data_json";//缓存动态DynamicFragment
        public static final String MUSIC_DATA_JSON = "_music_data_json";//音乐详情MusicDetailsActivity

    }

    public static class Path {
        public static final String APP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "com"
                + File.separator
                + "mxkj"
                + File.separator
                + "yuanyintang";

        public static final String APP_CACHE_PATH = MainApplication.Companion.getApplication().getExternalCacheDir()
                + File.separator
                + "com"
                + File.separator
                + "mxkj"
                + File.separator
                + "yuanyintang";
    }

    public class EaseConstant {
        public static final String ACCOUNT_REMOVED = "account_removed";
        public static final String ACCOUNT_CONFLICT = "conflict";
        public static final String ACCOUNT_FORBIDDEN = "user_forbidden";
        public static final String ACCOUNT_KICKED_BY_CHANGE_PASSWORD = "kicked_by_change_password";
        public static final String ACCOUNT_KICKED_BY_OTHER_DEVICE = "kicked_by_another_device";
        public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
        public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

        public static final String MESSAGE_TYPE_RECALL = "message_recall";


        public static final int CHATTYPE_SINGLE = 1;
        public static final int CHATTYPE_GROUP = 2;
        public static final int CHATTYPE_CHATROOM = 3;

        public static final String EXTRA_CHAT_TYPE = "chatType";
        public static final String EXTRA_USER_ID = "userId";
        public static final String EXTRA_OTHER_NAME = "otherName";
        public static final String EXTRA_OTHER_AVATAR = "otherAvatar";
    }


}

package com.mxkj.yuanyintang.net

import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.utils.file.CacheUtils

object ApiAddress {
//    const val BASE_URL = "http://testapi.imxkj.com"
//    const val BASE_URL = "http://api.demo.com"
    const val BASE_URL = "https://api.yuanyintang.com"
    val H5_BASE_URL = CacheUtils.getString(MainApplication.context, "web_url", "https://app.yuanyintang.com/")
    const val GET_TOKEN = "/v2/api/com/auth"
    const val GET_HOME = "/v2/api/home/app"
    const val GET_HOME_ACTIVITY = "3/v2/api/activity"
    const val GET_DYNAMIC = "/v2/api/circle"//动态列表
    const val GET_OBSERVALABLE = "/v2/api/circle/getAttention"//动态列表
    const val GET_PUBLIC_TAG = "/v2/api/com/tag"//标签获取
    const val GET_POND = "/v2/api/pond"//池塘
    const val GET_FOLLOW_POND = "/v2/api/pond/follow"//池塘
    const val GET_NEAR_POND = "/v2/api/pond/nearby"//池塘
    const val GET_PUBLIC_MUSIC = "/v2/api/music"
    const val GET_PUBLIC_MUSICIAN = "/v2/api/musician"
    const val GET_ONCLICKNUM = "/v2/api/com/onClicknum"//banner 点击增加次数
    const val GET_STARTADDCOUNT = "/v2/api/com/startAddCount"//点击启动图增加次数
    const val GET_PUBLIC_MUSIC_SONG = "/v2/api/song"
    //    public static final String GET_CODE = "/v2/api/code";//获取验证码
    const val GET_CODE = "/v2/api/code/sendcode"//获取验证码
    const val QUICK_LOGIN = "/v2/api/user/explogin"//快速登录
    const val MUST_BIND_MOBILE = "/v2/api/member.member/bind_mobile"//强制绑定手机

    const val REGIST = "/v2/api/user/reg"//注册
    const val GET_MUSIC_DETAILS = "/v2/api/music/"//音乐详情
    const val AVATAR = "/v2/api/member/member/avatar"//修改头像

    const val PWD_LOGIN = "/v2/api/user/login"
    const val THIRD_INFO = "/v2/api/connect/info"//第三方信息
    //    public static final String IS_BIND = "/v2/api/user/binds";//是否绑定
    const val IS_BIND = "/v2/api/user/loginthird"//是否绑定
    const val BIND_THIRD_UN = "/v2/api/user/third"//为登录绑定
    const val LOGIN_BY_OPEN_ID = "/v2/api/user/openidlogin"

    const val BANNER = "/v2/api/com/picture"//轮播
    const val POND_BANNER_TAG = "/v2/api/home/topic_nav"//轮播
    const val POND_HOT_TAG = "/v2/api/pond/tags"//池塘页面热门标签，带图片
    const val PLAYLIST_LABLE = "/v2/api/song/tag" //歌单标签
    const val GET_SONG_SHEET_DETAILS = "/v2/api/song/"//歌单详情
    const val GET_MUSICIAN_DETAILS = "/v2/api/musician/"//音乐人详情
    const val GET_MEMBER_ATTENTION = "/v2/api/member.member/attention"//我的关注
    const val GET_MEMBER_FANS = "/v2/api/member.member/fans"//我的粉丝
    const val GET_MEMBER_COIN = "/v2/api/member.coin/get_list"//我的甜甜圈列表
    const val GET_TIMELIMIT = "/v2/api/com/discount_surplus_time"//x限时优惠
    const val GET_MEMBER_COLLECTION = "/v2/api/member.member/collection"//我的收藏
    const val POST_MEMBER_DELCOLLECT = "/v2/api/member/delcollect"//取消收藏
    const val GET_MEMBER_SONG = "/v2/api/member.song"//我的歌单
    const val GET_MEMBER_SCOLLECTION = "/v2/api/member.song/scollection"//我收藏歌单
    const val GET_MEMBER_POND = "/v2/api/member.pond"//我的池塘
    //    public static final String GET_MEMBER = "/v2/api/member";//修改会员信息
    const val GET_MEMBER = "/v2/api/member.member/renew"//修改会员信息


    const val GET_VERSION = "/v2/api/com/version"//APP更新
    const val GET_FEEDBACK = "/v2/api/feedback"//举报
    const val MV_PLAYER_MV = "/v2/api/music/play_mv"//mv播放次数
    const val LOCA_UPLOAD = "/files/file/upload"//没有登录上传文件到本地

    const val CIRCLE_DETIAL_DATA = "/v2/api/circle/"//动态详情
    const val ADD_COMMENT = "/v2/api/member/comment" //评论列表和添加新评论
    //mv的评论
    const val ADD_MVCOMMENT = "/v2/api/member/comment" //mv的评论
    //mv的详情
    const val MVDETAILS = "v2/api/music" //mv的评论

    const val GET_FACE = "/v2/api/com/face" //获取
    const val PUBLISH_TOPIC = "/v2/api/member/pond"//发布池塘;
    const val SEARCH = "/v2/api/search"//搜索;
    const val ALL_POND_COMMENT = "/v2/api/pond/"
    const val AGREE = "/v2/api/agree/add"
    const val FOLLOW = "/v2/api/member/relation/follow"//关注

    const val GET_MUSIC_SONG = "/v2/api/music/song"
    const val GET_MUSIC_COLLECT = "/v2/api/member.music/collect"
    const val GET_RELATION_FOLLOW = "/v2/api/member/relation/follow"
    const val GET_MEMBER_COORDINATE = "/v2/api/member/coordinate"
    const val GET_SONG_COLLECT = "/v2/api/song/collect"//取消收藏歌单
    const val GET_MUSICDRAFT = "/v2/api/member/musicdraft"//发布音乐列表
    const val GET_MESSAGE = "/v2/api/message"//消息
    const val GET_CITY = "/v2/api/ajax/city"//获取城市
    const val POST_S_ADD_SONG = "/v2/api/song/add"//添加到一个歌单
    const val POST_ADD_SONG = "/v2/api/song/batch"//添加到多个歌单
    const val POST_NEW_SONG = "/v2/api/song"//新建歌单
    const val PUBLIC_SONG_LIST = "/v2/api/member.song/set_public"//公开歌单


    const val POST_NEWList_SONG = "/v2/api/member/song"//新建歌单

    const val POST_DOWN_KEY = "/v2/api/music/getDownKey"//下载
    const val GET_DOWN_BIT = "/v2/api/music/getBit/"//判断是否可下载
    const val POST_AGREE_ADD = "/v2/api/agree/add"//点赞或者取消
    const val GET_MUSIC_RELATED = "/v2/api/music/related"//正在听这首歌的人
    const val MUSIC_CATE = "/v2/api/music/cate"
    const val PUBLISH_MUSIC = "/v2/api/member/musicdraft"//fault音乐
    const val WORKS_TYPE = "/v2/api/music/category"//获取作品类型
    const val REAL_INFO_AUTH = "/v2/api/member/certification"//实名认证;
    const val SEARCH_RECOMMEND_LISTS = "/v2/api/search/lists"//搜索推荐
    const val MUSICIAN_INFO = "/v2/api/musician/info"//音乐人信息
    const val MUSICDRAFT_COUNT = "/v2/api/member.musicdraft/count"//我的音乐数量统计
    const val MEMBER_NEARBY = "/v2/api/member.member/nearby"//附近的人
    //    public static final String SONG_PLAYS = "/v2/api/member/song/plays/";//歌单播放列表
    const val SONG_PLAYS = "/v2/api/song/music/"//歌单播放列表
    const val MUSIC_COIN = "/v2/api/member.music/coin"//音樂投幣
    const val MUSIC_HISTORY = "/v2/api/member.playlist"//播放历史
    const val MUSIC_DEL_HISTORY = "/v2/api/member/playlist/del"//清除历史播放
    const val MUSIC_DOWN = "/v2/api/music/down"//音乐多下载
//    const val MUSIC_DOWN = "/v2/api/music/getDownKey"//音乐下载
    const val MESSAGE_CLICKSHOW = "/v2/api/message/clickshow/"//标记消息
    const val MSG_HELLO = "/v2/api/member/hello"//打招呼
    const val START_IMG = "/v2/api/com/getStartImg"//启动页

    const val SHARE_COUNT = "/v2/api/share"//分享次数统计
    const val AUTH_INCODE = "/v2/api/user/incode"//验证邀请码
    const val GIFT_DIALOG = "/apiv2/Lottery/lottery_dialog"//随机弹框
    const val IMG_ADDRESS = "/iamge/"//访问图片  :sha1/log_at/w/h/type/quality/limit?format=1
    const val GET_ACTIVITY = "/v2/api/activity/getActivity"//获取正在进行的活动列表
    const val GET_HELP_LIST = "/v2/api/help/hot"
    const val GET_HELP_PROBLEMLIST = "/v2/api/help/all_class"//问题分类类列表
    const val GET_HELP_PROBLEMLISTDETAILS = "/v2/api/help/class_list"//获取分类下的帮助列表
    const val GET_HELP_PROBLEMLISTDETAILSDETAILS = "/v2/api/help/read"//获取分类下的帮助列表详情
    const val GET_CHARTS_HOME_LIST = "/v2/api/billboard/getclass"
    const val GET_CHARTSNEW_HOME_LIST = "/v2/api/billboard/getclass"
    const val GET_CHARTS_ITEM_LIST = "/v2/api/billboard/getlist"
    const val GET_REWARD_INFO = "/v2/api/member.coin/getrewardinfo"
    const val GO_REWARD = "/v2/api/member.music/rewardcoin"
    const val GET_HELP_MSG = "/v2/api/help/getconfig"
    const val SEARCH_HELP = "/v2/api/help/keyword"
    const val GET_DEGREE = "/v2/api/busy/getdegree"


    const val GET_ROBOT_ANSWER = "/v2/api/help/ask"
    const val NO_MOBILE_GO_BIND = "/v2/api/member.member/addbind"
    const val APPLY_DOWN_LINE = "/v2/api/member.musicdraft/apply_downline"
    //我的作品删除
    const val DEL_REPLEASE = "/v2/api/member.musicdraft/delete_music"
    //我的作品撤回申请
    const val Back_REPLEASE = "/v2/api/member.musicdraft/revoke_examine"
    const val GIFT_LIST = "/v2/api/com/gift"
    const val GIVE_GIFT = "/v2/api/member.music/give_gift"
    const val CHECK_GIFT = "/v2/api/member.music/check_gift"
    const val CHECK_ITEM = "/v2/api/order.order_cate"
    const val CREATE_ORDER = "/v2/api/order.order"
    const val GET_MUSIC_GIFT_LIST = "/v2/api/music/get_gift"

    const val TODAY_RECOMMEND = "/v2/api/home/today_recommends"//今日推荐
    const val NEW_RECOMMEND = "/v2/api/home/newest_music"//推荐
    const val ORDER_WHERE = "/v2/api/com/get_where"//页面排序

}

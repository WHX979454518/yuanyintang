package com.mxkj.yuanyintang.mainui.home.data;

/**
 * Created by admin on 2017/2/22.
 */
public class Constant {
    //布局参照首页依次往下判断布局
    public static final int TYPE_TOP_BANNER = 0xff01;
    public static final int TYPE_RECOMMEND_MUSIC = 0xff02;
    public static final int TYPE_RECOMMEND_MUSICIAN = 0xff03;
    public static final int TYPE_VOCALOID = 0xff04;
    //    public static final int TYPE_ANCIENTRY = 0xff05;
//    public static final int TYPE_ANIME_OR_GAME = 0xff06;
//    public static final int TYPE_THREE_D = 0xff07;
    public static final int TYPE_POND = 0xff07;
    public static final int TYPE_OTHER = 0xff09;
    public static final int TYPE_SYSYEM_MSG = 0xff10;
    public static final int TYPE_CHARTS = 0xff11;

    public static final int MUSIC_TYPE_LISTENING = 0xff50;
    public static final int MUSIC_TYPE_LABEL = 0xff51;
    public static final int MUSIC_TYPE_SYNOPSIS = 0xff52;
    public static final int MUSIC_TYPE_SONG = 0xff53;
    public static final int MUSIC_TYPE_RECOMMEND_SONG = 0xff54;
    public static final int MUSIC_TYPE_POND = 0xff55;
    public static final int MUSIC_TYPE_MUSIC = 0xff56;
    public static final int MUSIC_TYPE_RECOMMEND = 0xff57;
    public static final int TYPE_LOTTERY = 0xff58;
    public static final int MUSIC_TYPE_GIFT_LIST = 0xff59;
    public static final int MUSIC_TYPE_RELATED_PARTNER = 0xff60;

    public static class PondItemType {
        public static final int TYPE_TAG = 0xff01;
        public static final int TYPE_POND_SINGLE_IMG = 0xff02;//1
        public static final int TYPE_POND_MUTI_IMG = 0xff03;//1
        public static final int TYPE_POND_NO_IMG = 0xff04;  //0
    }  //动态布局类型


    public static class DynamicItemType {
        public static final int TYPE_TEXT_IMG = 0xff01;
        public static final int TYPE_TEXT_MUSIC = 0xff02;
        public static final int TYPE_MUSIC_IMG = 0xff03;
        public static final int TYPE_TOPIC_NOIMG = 0xff04;
        public static final int TYPE_TOPIC_IMG = 0xff05;
        public static final int TYPE_TEXT = 0xff06;
        public static final int TYPE_TEXT_ALBUM = 0xff07;
        public static final int TYPE_IMG_ALBUM = 0xff08;
        public static final int TYPE_NEW_MSG = 1024;
    } //动态布局类型

    /*
    * 池塘评论
    * */
    public static class PondAllCommentItemType {
        public static final int MUSIC_IMG = 0xff01;
        public static final int MUSIC = 0xff02;//1
        public static final int IMG = 0xff03;//1
        public static final int TEXT = 0xff04;  //0
//        public static final int ISSELF = 0xff05;  //0
    }



  public static class HomeCharts {
        public static final int MUSIC = 0xff01;
        public static final int INCOME = 0xff02;
    }




    /*
       * 消息中心
       * */
    public static class MsgCenterItem {
        public static class BroadMsgItemType {//公告消息
            public static final int HAVE_IMG = 0xff01;
            public static final int NO_IMG = 0xff02;
        }

        public static class SystemMsgItemType {//系统
            public static final int SYS_MSG = 0xff03;
        }

        public static class CommentMsgItemType {//评论消息
            public static final int REPLY_MYMUSIC = 0xff04;
            public static final int REPLY_MY_COMMENT_FOR_MUSIC = 0xff05;
            public static final int REPLY_MY_ALBUM = 0xff06;
            public static final int REPLY_MY_COMMENT_FOR_ALBUM = 0xff07;
            public static final int REPLY_MY_POND = 0xff08;
            public static final int REPLY_MY_COMMENT_FOR_POND = 0xff09;
        }

        public static class AgreeItemType {//点赞消息
            public static final int AGREE_MY_COMMENT_FOR_MUSIC = 0xff10;
            public static final int AGREE_MY_COMMENT_FOR_ALBUM = 0xff11;
            public static final int AGREE_MY_COMMENT_FOR_POND = 0xff12;
        }
    }

/*
*
* 动态消息列表布局类型
* */

    public static class CircleMsgItemType {
        public static final int COMMENT_DYNAMIC = 0xff13;
        public static final int REPLY_COMMENT_FOR_DYNAMIC = 0xff14;
        public static final int AGREE_MY_DYNAMIC = 0xff15;
        public static final int AGREE_MY_COMMENT = 0xff16;
        public static final int AGREE_MY_REPLY = 0xff17;
        public static final int TV_HISTORY_MSG = 0xff18;
    }

    public static class IncomeListItemType {
        public static final int TITLE = 0xff01;
        public static final int CONTENT = 0xff02;
    }

     public static class NewHomeType {
        public static final int BANNERTYPE = 0xff01;
        public static final int FOURTYPE = 0xff02;
        public static final int AlbumBeanTYPE = 0xff03;
        public static final int RecomendBeanTYPE = 0xff04;
        public static final int OriginalBeanTYPE = 0xff05;
        public static final int CoverBeanTYPE = 0xff06;
        public static final int MusicianBeanTYPE = 0xff07;
        public static final int MusicianBeanTYPENew = 0xff11;
        public static final int GuessBeanTYPE = 0xff08;
        public static final int SysMsgType = 0xff10;

    }





}


package com.mxkj.yuanyintang.mainui.emotionkeyboard.utils;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.mxkj.yuanyintang.database.DBManager;
import com.mxkj.yuanyintang.widget.emoji.FaceBean;

import java.util.List;

public class EmotionUtils {

    /**
     * 表情类型标志符
     */
    public static final String EMOTION_CLASSIC_TYPE = "";//经典表情

    /**
     * key-表情文字;
     * value-表情图片资源
     */
    public static ArrayMap<String, String> EMPTY_MAP;
    public static ArrayMap<String, String> EMOTION_CLASSIC_MAP;


    static {
//        EMPTY_MAP = new ArrayMap<>();
//        EMOTION_CLASSIC_MAP = new ArrayMap<>();
////		"[:" + emotionName + "]"
////        String emojiJson = CacheUtils.getString("emojiJson");
////        FaceBean faceBean = JSON.parseObject(emojiJson, FaceBean.class);
//        new DBManager();
//        if (faceBean!=null){
//            List<FaceBean.DataBean> faceList = faceBean.getData();
//            for (int i = 0; i < faceList.size(); i++) {
//                final FaceBean.DataBean dataBean = faceList.get(i);
//                EMOTION_CLASSIC_MAP.put("[:" + dataBean.getEmoji() + "]", dataBean.getImgpic_info().getLink());
//            }
//        }
    }

    /**
     * 根据名称获取当前表情图标R值
     *
     * @param context
     * @param EmotionType 表情类型标志符
     * @param imgName     名称
     * @return
     */
    public static String getImgByName(Context context, String EmotionType, String imgName) {
        String emojiLink = null;
        //预留  不同类型的表情
//        switch (EmotionType) {
//            case EMOTION_CLASSIC_TYPE:
//                break;
//            default:
//                LogUtils.e("the emojiMap is null!! Handle Yourself ");
//                break;
//        }

        EMPTY_MAP = new ArrayMap<>();
        EMOTION_CLASSIC_MAP = new ArrayMap<>();
//		"[:" + emotionName + "]"
//        String emojiJson = CacheUtils.getString("emojiJson");
//        FaceBean faceBean = JSON.parseObject(emojiJson, FaceBean.class);
        DBManager dbManager = new DBManager(context);
        List<FaceBean.DataBean> faceList = dbManager.queryEmoji();
        if (faceList != null) {
            for (int i = 0; i < faceList.size(); i++) {
                final FaceBean.DataBean dataBean = faceList.get(i);
                EMOTION_CLASSIC_MAP.put("[:" + dataBean.getEmoji() + "]", dataBean.getImgpic_link());//这里拿到的地址是本地文件的地址
            }
        }
        emojiLink = EMOTION_CLASSIC_MAP.get(imgName);
        return emojiLink == null ? null : emojiLink;
    }

    /**
     * 根据类型获取表情数据
     *
     * @param EmotionType
     * @return
     */
    public static ArrayMap<String, Integer> getEmojiMap(String EmotionType, Context context) {
        ArrayMap EmojiMap = null;
        EMOTION_CLASSIC_MAP = new ArrayMap<>();
        DBManager dbManager = new DBManager(context);
        List<FaceBean.DataBean> faceList = dbManager.queryEmoji();
        if (faceList != null) {
            for (int i = 0; i < faceList.size(); i++) {
                final FaceBean.DataBean dataBean = faceList.get(i);
                try {
                    EMOTION_CLASSIC_MAP.put("[:" + dataBean.getEmoji() + "]", dataBean.getImgpic_link());//这里拿到的地址是本地文件的地址
                } catch (RuntimeException e) {
                }

            }
        }
//                break;
//            default:
//                EmojiMap = EMPTY_MAP;
//                break;
//        }
        EmojiMap = EMOTION_CLASSIC_MAP;
        return EmojiMap;
    }
}

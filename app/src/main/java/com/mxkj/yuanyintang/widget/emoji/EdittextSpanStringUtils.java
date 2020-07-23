package com.mxkj.yuanyintang.widget.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/27.
 */

public class EdittextSpanStringUtils {
    public static void getEmotionContent(final Context context, final View tv, String source) {
        SpannableString spannableString = new SpannableString(source);
//        String regexEmotion = "/\\[:(.+?)\\]/g";
//        Pattern patternEmotion = Pattern.compile(regexEmotion);
//        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
//        while (matcherEmotion.find()) {
        String emojiJson = CacheUtils.INSTANCE.getString(context,"emojiJson");
        FaceBean faceBean = JSON.parseObject(emojiJson, FaceBean.class);
        List<FaceBean.DataBean> faceList = faceBean.getData();
        for (int i = 0; i < faceList.size(); i++) {
            final FaceBean.DataBean dataBean = faceList.get(i);
            if (source.contains(dataBean.getEmoji())) {
                source.replace(dataBean.getEmoji(), "");
                ImageLoader.with(context)
                        .url(dataBean.getImgpic_link())
                        .asBitmap(new SingleConfig.BitmapListener() {
                            @Override
                            public void onSuccess(Bitmap bitmap) {
                                ImageSpan imageSpan = new ImageSpan(bitmap);
//                      SpannableString spannableString = new SpannableString("[:"+dataBean.getEmoji()+"]");
                                SpannableString spannableString = new SpannableString(dataBean.getEmoji());
                                spannableString.setSpan(imageSpan, 0, dataBean.getEmoji().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                if (tv instanceof TextView) {
                                    ((TextView) tv).append(spannableString);
                                } else if (tv instanceof EditText) {
                                    ((EditText) tv).append(spannableString);
                                }
                            }

                            @Override
                            public void onFail() {

                            }
                        });

            }
        }
    }
//        return spannableString;
//    }
}

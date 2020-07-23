//package com.mxkj.yuanyintang.ui.emotionkeyboard.utils;
//
//import android.content.Context;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.mxkj.yuanyintang.ui.emotionkeyboard.emotionkeyboardview.emoji_text.emoji.AnimatedGifDrawable;
//import com.mxkj.yuanyintang.ui.emotionkeyboard.emotionkeyboardview.emoji_text.emoji.AnimatedImageSpan;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.FileCallBack;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.lang.ref.WeakReference;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableSource;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.Call;
//
//public class SpanStringUtils {
//    public static void getEmotionContent(String emotion_map_type, final Context context, final View tv, final String source, final SpannableStringCallback callback) {
//        final SpannableString mValue = SpannableString.valueOf(source);
//        Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
//        Matcher localMatcher = EMOTION_URL.matcher(mValue);
//        while (localMatcher.find()) {
//            String str2 = localMatcher.group(0);
//            final int k = localMatcher.start();
//            final int m = localMatcher.end();
//            final String imgLink = EmotionUtils.getImgByName(context, "", str2);
//            if (imgLink != null) {
//                Log.e("TAG", "getEmotionContent: ====" + imgLink);
//                Observable.just(imgLink).flatMap(new Function<String, ObservableSource<InputStream>>() {
//                    @Override
//                    public ObservableSource<InputStream> apply(String urlStr) throws Exception {
//                        if (urlStr != null) {
//                            InputStream inputStream = null;
//                            if (urlStr != null) {
//                                File file = new File(urlStr);
//                                if (file.exists()) {
//                                    inputStream = new FileInputStream(file);
//                                    if (inputStream != null) {
//                                        return Observable.just(inputStream).doOnError(new Consumer<Throwable>() {
//                                            @Override
//                                            public void accept(@NonNull Throwable throwable) throws Exception {
//
//                                            }
//                                        });
//                                    }
//                                }
//
//                            }
//
//                        }
//                        return null;
//                    }
//                }).observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(new Consumer<InputStream>() {
//                            @Override
//                            public void accept(InputStream inputStream) throws Exception {
//                                if (inputStream != null) {
//                                    WeakReference<AnimatedImageSpan> localImageSpanRef = new WeakReference<AnimatedImageSpan>(new AnimatedImageSpan(new AnimatedGifDrawable(inputStream, new AnimatedGifDrawable.UpdateListener() {
//                                        @Override
//                                        public void update() {
//                                            tv.postInvalidate();
//                                        }
//                                    })));
//                                    AnimatedImageSpan animatedImageSpan = localImageSpanRef.get();
//                                    if (animatedImageSpan != null) {
//                                        Log.e("TAG", "accept:===== ");
//                                        mValue.setSpan(animatedImageSpan, k, m, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                                        if (tv instanceof TextView) {
//                                            ((TextView) tv).setText(mValue);
//                                        }
//                                        if (tv instanceof EditText) {
//                                            ((EditText) tv).setText(mValue);
//                                        }
//                                    }
//                                }
//                            }
//                        });
//            }
//        }
//        if (tv instanceof TextView) {
//            ((TextView) tv).setText(mValue);
//        }
//        if (tv instanceof EditText) {
//            ((EditText) tv).setText(mValue);
//        }
//    }
//
//    public interface SpannableStringCallback {
//        void getSpannableString(SpannableString spannableString);
//    }
//}

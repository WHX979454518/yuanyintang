package com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.comment.Comment;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.LogUtils;
import com.mxkj.yuanyintang.utils.uiutils.Toast;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import okhttp3.Headers;

public class EmotionKeyboard {
    private static final String SHARE_PREFERENCE_NAME = "EmotionKeyboard";
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";
    public static final String COMMENT_TYPE = "comment_type";
    public static final String COMMENT_ITEM_ID = "comment_item_id";
    public static final String COMMENT_PID = "comment_pid";
    public static final String COMMENT_FID = "comment_fid";
    public static final String COMMENT_SUCCESS = "comment_success";
    public static final String COMMENT_SUCCESS_POND = "comment_success_pond";
    public static final String COMMENT_SUCCESS_JSON = "comment_success_json";
    public static final String IS_POND = "comment_pond";
    private Activity mActivity;
    private InputMethodManager mInputManager;//软键盘管理类
    private SharedPreferences sp;
    public View mEmotionLayout;//表情布局
    private EditText mEditText;//
    private View mContentView;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪
    private int comment_pid;
    private int comment_type;
    private int comment_fid;
    private int comment_item_id;
    private boolean is_pond = false;

    public EmotionKeyboard() {

    }

    /**
     * 外部静态调用
     *
     * @param activity
     * @return
     */
    public static EmotionKeyboard with(Activity activity) {
        EmotionKeyboard emotionInputDetector = new EmotionKeyboard();
        emotionInputDetector.mActivity = activity;
        emotionInputDetector.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        emotionInputDetector.sp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return emotionInputDetector;
    }

    /**
     * 绑定内容view，此view用于固定bar的高度，防止跳闪
     *
     * @param contentView
     * @return
     */
    public EmotionKeyboard bindToContent(View contentView) {
        mContentView = contentView;
        return this;
    }

    /**
     * 绑定编辑框
     *
     * @param editText
     * @return
     */
    public EmotionKeyboard bindToEditText(EditText editText) {
        mEditText = editText;
        mEditText.requestFocus();
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && mEmotionLayout.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout(true);//隐藏表情布局，显示软件盘

                    //软件盘显示后，释放内容高度
                    mEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    }, 100);
                }
                return false;
            }
        });
        return this;
    }

    /**
     * 绑定表情按钮
     *
     * @param emotionButton
     * @return
     */
    public EmotionKeyboard bindToEmotionButton(View emotionButton) {
        emotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmotionLayout.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                    unlockContentHeightDelayed();//软件盘显示后，释放内容高度
                } else {
                    if (isSoftInputShown()) {//同上
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showEmotionLayout();//两者都没显示，直接显示表情布局
                    }
                }
            }
        });
        return this;
    }

    /**
     * 绑定发送按钮
     *
     * @param emotionButton
     * @return
     */
    public EmotionKeyboard bindToSendButton(View emotionButton) {
        emotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.create(mActivity);
                if (!CacheUtils.INSTANCE.getBoolean(mActivity, Constants.User.IS_LOGIN, false)) {
                    mActivity.startActivity(new Intent(mActivity, LoginRegMainPage.class));
                }else {
                    if (is_pond == true) {
                        Log.e("TAG", "onClick: is_pond" + is_pond + "---comment_fid==" + comment_fid + "---comment_pid==" + comment_pid);
                        NetWork.INSTANCE.addPondCommentReply(mActivity, comment_fid, comment_pid, mEditText.getText().toString(), new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                mEditText.getText().clear();
                                hideSoftInput();
                                Intent intent = new Intent(COMMENT_SUCCESS);
                                intent.putExtra(COMMENT_SUCCESS_JSON, resultData);
                                intent.putExtra(COMMENT_PID, comment_pid);
                                intent.putExtra(IS_POND, true);
                                mActivity.sendBroadcast(intent);
                            }

                            @Override
                            public void doError(String msg) {

                            }

                            @Override
                            public void doResult() {

                            }
                        });
                    } else {
                        NetWork.INSTANCE.addComment(mActivity, comment_type, comment_item_id, mEditText.getText().toString(), comment_pid, comment_fid, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                mEditText.getText().clear();
                                hideSoftInput();
                                Intent intent = new Intent(COMMENT_SUCCESS);
                                intent.putExtra(COMMENT_SUCCESS_JSON, resultData);
                                intent.putExtra(COMMENT_PID, comment_pid);
                                mActivity.sendBroadcast(intent);
                            }

                            @Override
                            public void doError(String msg) {
                                Log.e("TAG", "doError: " + msg);
                            }

                            @Override
                            public void doResult() {

                            }
                        });
                    }
                }
//                if (CacheUtils.INSTANCE.getBoolean(mActivity,Constants.User.IS_LOGIN, false) == true) {
//                    if (is_pond == true) {
//                        Log.e("TAG", "onClick: is_pond" + is_pond + "---comment_fid==" + comment_fid + "---comment_pid==" + comment_pid);
//                        NetWork.INSTANCE.addPondCommentReply(mActivity, comment_fid, comment_pid, mEditText.getText().toString(), new NetWork.TokenCallBack() {
//                            @Override
//                            public void doNext(String resultData, Headers headers) {
//                                mEditText.getText().clear();
//                                hideSoftInput();
//                                Intent intent = new Intent(COMMENT_SUCCESS);
//                                intent.putExtra(COMMENT_SUCCESS_JSON, resultData);
//                                intent.putExtra(COMMENT_PID, comment_pid);
//                                intent.putExtra(IS_POND, true);
//                                mActivity.sendBroadcast(intent);
//                            }
//
//                            @Override
//                            public void doError(String msg) {
//
//                            }
//
//                            @Override
//                            public void doResult() {
//
//                            }
//                        });
//                    } else {
//                        NetWork.INSTANCE.addComment(mActivity, comment_type, comment_item_id, mEditText.getText().toString(), comment_pid, comment_fid, new NetWork.TokenCallBack() {
//                            @Override
//                            public void doNext(String resultData, Headers headers) {
//                                mEditText.getText().clear();
//                                hideSoftInput();
//                                Intent intent = new Intent(COMMENT_SUCCESS);
//                                intent.putExtra(COMMENT_SUCCESS_JSON, resultData);
//                                intent.putExtra(COMMENT_PID, comment_pid);
//                                mActivity.sendBroadcast(intent);
//                            }
//
//                            @Override
//                            public void doError(String msg) {
//                                Log.e("TAG", "doError: " + msg);
//                            }
//
//                            @Override
//                            public void doResult() {
//
//                            }
//                        });
//                    }
//                } else {
//                    mActivity.startActivity(new Intent(mActivity, LoginRegMainPage.class));
//                }

            }
        });
        return this;
    }

    /**
     * 设置表情内容布局
     *
     * @param emotionView
     * @return
     */
    public EmotionKeyboard setEmotionView(View emotionView) {
        mEmotionLayout = emotionView;
        return this;
    }

    public EmotionKeyboard build() {
        //设置软件盘的模式：SOFT_INPUT_ADJUST_RESIZE  这个属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        //从而方便我们计算软件盘的高度
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //隐藏软件盘
        hideSoftInput();
        return this;
    }

    /**
     * 点击返回键时先隐藏表情布局
     *
     * @return
     */
    public boolean interceptBackPress() {
        if (mEmotionLayout.isShown()) {
            hideEmotionLayout(false);
            return true;
        }
        return false;
    }

    public void showEmotionLayout() {
        /**
         * s重新设置显示高度  避免过高
         *
         * */
        hideSoftInput();
        ViewGroup.LayoutParams layoutParams = mEmotionLayout.getLayoutParams();
        layoutParams.height = 380;
        mEmotionLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    public void hideEmotionLayout(boolean showSoftInput) {
        if (mEmotionLayout.isShown()) {
            mEmotionLayout.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
//        ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
//                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        mEditText.requestFocus();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mEditText, 0);
            }
        });
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 是否显示软件盘
     *
     * @return
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }

        if (softInputHeight < 0) {
            LogUtils.w("EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0) {
            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
     *
     * @return
     */
    public int getKeyBoardHeight() {
        return sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 787);

    }

    /**
     * 设置一些参数，用于回复评论什么的
     * private int commentType;
     * private int Id;
     * private int pid;//我要回复这条评论
     * private int fid;
     */
    public void sendComment(Bundle args) {
        comment_type = args.getInt(COMMENT_TYPE, 0);
        comment_item_id = args.getInt(COMMENT_ITEM_ID, 0);
        comment_fid = args.getInt(COMMENT_FID, 0);
        comment_pid = args.getInt(COMMENT_PID, 0);
        is_pond = args.getBoolean(IS_POND, false);
    }

    @NonNull
    public Comment.DataBean.SonBean setSonBean(String response) {
        Comment.DataBean.SonBean sonBean;
        com.alibaba.fastjson.JSONObject jsonObject1 = JSON.parseObject(response);
        sonBean = jsonObject1.getObject("data", Comment.DataBean.SonBean.class);
        return sonBean;
    }

    @NonNull
    public Comment.DataBean setCommentBean(String response) {
        Comment.DataBean sonBean;
        com.alibaba.fastjson.JSONObject jsonObject1 = JSON.parseObject(response);
        sonBean = jsonObject1.getObject("data", Comment.DataBean.class);
        return sonBean;
    }


}

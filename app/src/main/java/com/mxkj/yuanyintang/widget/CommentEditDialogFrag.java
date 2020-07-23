package com.mxkj.yuanyintang.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.comment.Comment;
import com.mxkj.yuanyintang.mainui.comment.CommentSuccessReceiver;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_FID;
import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_ITEM_ID;
import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_PID;
import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS;
import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.IS_POND;

@SuppressLint("ValidFragment")
public class CommentEditDialogFrag extends DialogFragment implements View.OnClickListener {
    private boolean isPond = false;
    @Nullable
    public Window window;
    public View mFilterView;
    public EditText et_pinglun;
    public Button btn_comment;
    private int commentType;
    private int Id;
    private int pid;//我要回复这条评论
    private int fid;
    public successCallBack callBack;
    private ImageView img_show_emoji;
    public EmotionMainFragment emotionMainFragment;

    private CommentSuccessReceiver commentSuccessReceiver;
    private IntentFilter filter;
    String hintText;

    public CommentEditDialogFrag(String hintText, int commentType, int id, int pid, int fid, boolean isPond, successCallBack callBack) {
        this.commentType = commentType;
        this.callBack = callBack;
        this.Id = id;
        this.pid = pid;
        this.fid = fid;
        this.isPond = isPond;
        this.hintText = hintText;
    }

    public CommentEditDialogFrag(String hintText, int commentType, int id, int pid, int fid, successCallBack callBack) {
        this.commentType = commentType;
        this.hintText = hintText;
        this.fid = fid;

        this.callBack = callBack;
        this.Id = id;
        this.pid = pid;
    }

    public void setEtHint(String hintText) {
        if (emotionMainFragment != null && emotionMainFragment.bar_edit_text != null) {
            emotionMainFragment.bar_edit_text.setHint(hintText);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, R.anim.slide_in_bottom);
    }

    @SuppressLint("RtlHardcoded")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        window = getDialog().getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        mFilterView = inflater.inflate(R.layout.layout_send_comment_pond, ((ViewGroup) window.findViewById(android.R.id.content)), false);//需要用android.R.id.content这个view
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(-1, -2);
        setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        et_pinglun = mFilterView.findViewById(R.id.et_pinglun);
        btn_comment = mFilterView.findViewById(R.id.btn_comment);
        img_show_emoji = mFilterView.findViewById(R.id.img_show_emoji);
        btn_comment.setOnClickListener(this);
        img_show_emoji.setOnClickListener(this);
        registerCommentReceiver();
        initEmotionMainFragment();
        return mFilterView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        window.setAttributes(windowParams);

        if (emotionMainFragment.bar_edit_text != null) {
            emotionMainFragment.bar_edit_text.setHint("回复 " + hintText);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        et_pinglun.setFocusable(true);
        et_pinglun.setFocusableInTouchMode(true);
        et_pinglun.requestFocus();
        showSoftInput();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (et_pinglun != null) {
            hideSoftInput(et_pinglun.getWindowToken());
            et_pinglun.getText().clear();
        }
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_comment:
                if (getActivity()!=null&& CacheUtils.INSTANCE.getBoolean(getActivity(),Constants.User.IS_LOGIN, false) == true) {
                    NetWork.INSTANCE.addComment(getActivity(), commentType, Id, et_pinglun.getText().toString(), pid, fid, new NetWork.TokenCallBack() {

                        @Override
                        public void doNext(String resultData, Headers headers) {
                            et_pinglun.getText().clear();
                            hideSoftInput(et_pinglun.getWindowToken());
                            dismiss();
                            callBack.callBack(resultData);
                            hideSoftInput(et_pinglun.getWindowToken());
                        }

                        @Override
                        public void doError(String msg) {
                            Log.e("TAG", "doError: " + msg);
                        }

                        @Override
                        public void doResult() {

                        }
                    });
                    break;
                } else {
                    getActivity().startActivity(new Intent(getActivity(), QuickLoginActivityNew.class));
                }
            case R.id.img_show_emoji:
//                EmojiDialogFrag dialogForComment = new EmojiDialogFrag(new EmojiDialogFrag.EmojiClickCallback() {
//                    @Override
//                    public void onEmojiClick(final FaceBean.DataBean dataBean) {
//                        Glide.with(getActivity()).load(dataBean.getImgpic_info().getLink()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                ImageSpan imageSpan = new ImageSpan(resource);
//                                String emojiRep = "[:" + dataBean.getEmoji() + "]";
//                                SpannableString spannableString = new SpannableString(emojiRep);
//                                spannableString.setSpan(imageSpan, 0, emojiRep.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                et_pinglun.append(spannableString);
//                            }
//                        });
//                    }
//                });
//                dialogForComment.show(getFragmentManager(), "emoji");
                break;
        }
    }

    public interface successCallBack {
        void callBack(String json);
    }

    public void hideSoftInput(@Nullable IBinder token) {
        if (token != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) et_pinglun.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(token,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
            et_pinglun.setFocusable(false);
        }
    }

    public void showSoftInput() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) et_pinglun.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }, 100);
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

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true);
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, false);
        bundle.putInt(COMMENT_ITEM_ID, Id);
        bundle.putInt(COMMENT_PID, pid);
        bundle.putInt(COMMENT_FID, fid);
        bundle.putBoolean(IS_POND, isPond);
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(et_pinglun);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void registerCommentReceiver() {
        commentSuccessReceiver = new CommentSuccessReceiver(new CommentSuccessReceiver.SuccessCallBack() {
            @Override
            public void callBack(String json, Intent intent) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).setSnackBar("发表成功", "", R.drawable.icon_success);
                    callBack.callBack(json);
                }
            }
        });
        filter = new IntentFilter(COMMENT_SUCCESS);
        getActivity().registerReceiver(commentSuccessReceiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(commentSuccessReceiver);
    }
}

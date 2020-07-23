package com.mxkj.yuanyintang.mainui.web;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.GiiGiftSuccessActivity;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.extraui.gift.BotomGiftListDialog;
import com.mxkj.yuanyintang.extraui.gift.CheckBean;
import com.mxkj.yuanyintang.extraui.gift.ConfirmGiveGiftDialog;
import com.mxkj.yuanyintang.extraui.gift.FirstChargeDialog;
import com.mxkj.yuanyintang.mainui.comment.Comment;
import com.mxkj.yuanyintang.mainui.comment.CommentSuccessReceiver;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment;
import com.mxkj.yuanyintang.mainui.home.bean.MemberGiftListBean;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.myself.doughnut.ChargeDoughnutActivity;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.HttpUtils;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.SaveMessage;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.HexagonImageView;
import com.mxkj.yuanyintang.widget.Panel;
import com.umeng.analytics.MobclickAgent;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import wendu.webviewjavascriptbridge.WVJBWebView;

public class CommonWebview extends StandardUiActivity {
    public String TAG = "CommonWebview";
    @BindView(R.id.webView)
    WVJBWebView webView;
    private Handler handler = new Handler();
    public MusicBean musicBean;
    private String headerType;//右上角显示提交还是更多图标
    private String title;//网页标题
    private String url;
    private String shareContent;
    private String shareImg;
    public boolean showDialog;
    public String dialogTitle, dialogContent, sureBtnText, cancelBtnText;
    H5Interface h5Interface;


    FrameLayout fl_emotionview_main;
    LinearLayout root_layout;

    @Override
    public int setLayoutId() {
        return R.layout.activity_webview4_h5;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        fl_emotionview_main = findViewById(R.id.fl_emotionview_main);
        root_layout = findViewById(R.id.root_layout);
    }

    @Override
    protected void initData() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initEvent() {
        if (getIntent() != null) {
            title = getIntent().getStringExtra("title");
            shareContent = getIntent().getStringExtra("content");
            shareImg = getIntent().getStringExtra("img");
            headerType = getIntent().getStringExtra("headerType");
            if (title != null) {
                setWebTitle(title, headerType);
            }
        }

        registerCommentReceiver();
        initPannel();


        initJsBridge();
        initWebview();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initWebview() {
        final WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBlockNetworkImage(true);
        settings.setJavaScriptEnabled(true);
//        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        url = getIntent().getStringExtra("url");
        if (url != null) {
            Log.e(TAG, "initWebview: " + url);
//            webView.loadUrl(url);
//            String  cook= CacheUtils.INSTANCE.getString(CommonWebview.this, "token", "");
            Map<String,String> map=new HashMap<>();
//            map.put("logintoken",CacheUtils.INSTANCE.getString(CommonWebview.this, "token"));
            map.put("logintoken",SaveMessage.huoquLogintoken(CommonWebview.this));
            webView.loadUrl(url,map);

            try {
                PackageManager manager = this.getPackageManager();
                PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                String version = info.versionName;
                webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + "/yuanyintang/"+version);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            webView.loadUrl("https://iask.sina.com.cn/b/4220195.html");
//            JsBridge--->Test Html
//            webView.loadUrl("file:///android_asset/test.html");
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                settings.setBlockNetworkImage(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);

            }
        });
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void initJsBridge() {
        webView.registerHandler("runAdroidApi", new WVJBWebView.WVJBHandler() {

            @Override
            public void handler(final Object dataObj, final WVJBWebView.WVJBResponseCallback callback) {
                if (h5Interface == null) {
                    h5Interface = new H5Interface();
                }
                UserInfo infoBean = null;
                String userInfo = CacheUtils.INSTANCE.getString(CommonWebview.this, Constants.User.USER_JSON);
                if (!TextUtils.isEmpty(userInfo)) {
                    infoBean = JSON.parseObject(userInfo, UserInfo.class);
                }
                String loginToken = "";
                if (infoBean != null && infoBean.getData() != null) {
                    loginToken = infoBean.getData().getLogintoken();
                }
                String paramsForWeb = "{\"logintoken\": \"" + loginToken + "\",\"logAt\":\"3\",\"token\":\"" + CacheUtils.INSTANCE.getString(CommonWebview.this, "token") + "\"} ";
                h5Interface.funAndroid(CommonWebview.this, dataObj.toString());
                callback.onResult(paramsForWeb);
            }
        });

        webView.callHandler("init", null, new WVJBWebView.WVJBResponseCallback() {
            @Override
            public void onResult(Object data) {
                Log.e(TAG, "Java received response:--- " + data.toString());
            }
        });


    }

    public void setWebTitle(final String title, String headerType) {
        if (title != null) {
            this.title = title;
            Log.e(TAG, "setWebTitle: ====" + title);
            setTitleText(title);
        }
        if (!TextUtils.isEmpty(headerType) && headerType.equals("submit")) {//提交按钮，活动页提交信息
            showRightButton();
            setRightButton("提交", null, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webView.loadUrl("javascript:submit()");
                    showDialog = false;
                }
            });
        } else if (!TextUtils.isEmpty(headerType) && headerType.equals("none")) {
            findViewById(R.id.rightButton).setVisibility(View.INVISIBLE);
        } else {
            showRightButton();
            Resources resources = getResources();
            Drawable drawable = resources.getDrawable(R.drawable.icon_more_black);
            setRightButton("", drawable, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog = false;
                    if (musicBean == null) {
                        musicBean = new MusicBean();
                        MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                        shareDataBean.setType("web");
                        if (title != null) {
                            shareDataBean.setTitle(title);
                            shareDataBean.setNickname(title);
                        }
                        if (shareContent != null) {
                            shareDataBean.setTopicContent(shareContent);
                        } else {
                            shareDataBean.setTopicContent(title);
                        }
                        if (shareImg != null) {
                            shareDataBean.setWebImgUrl(shareImg);
                        }
                        shareDataBean.setShareUrl(url);
                        musicBean.setShareDataBean(shareDataBean);
                    }
                    ShareBottomDialog shareBottomDialog = new ShareBottomDialog(CommonWebview.this, musicBean);
                    shareBottomDialog.show();
                }
            });
        }
    }

    @OnClick({R.id.leftButton, R.id.rightButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                if (showDialog == true) {
                    showCancleDialog();
                } else {
                    if (webView.canGoBack()) {
                        webView.goBack();//返回上一页面
                    } else {
                        finish();
                    }
                }
                break;
            case R.id.rightButton:

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (showDialog == true) {
                showCancleDialog();
                return true;
            } else {
                if (webView.canGoBack()) {
                    webView.goBack();//返回上一页面
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showCancleDialog() {
        MaterialDialog(TextUtils.isEmpty(dialogContent) ? "是否退出？" : dialogContent, null, null, new MaterialDialogBtnClickListener() {
            @Override
            public void onBtnClick(int code) {
                if (code == 1) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
        if (CacheUtils.INSTANCE.getBoolean(this, "needReload", false) == true) {
            webView.reload();
            CacheUtils.INSTANCE.setBoolean(this, "needReload", false);
        }
        webView.reload();
        webView.resumeTimers();

        if (isReGiveGift && gift_id != 0) {
            checkStatus(gift_id);
        }
    }

    @Override
    protected void onDestroy() {
        setConfigCallback(null);
        if (webView != null) {
            webView.pauseTimers();
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }


        unregisterReceiver(commentSuccessReceiver);
        super.onDestroy();
    }

    /**
     * 初始化分享的信息
     *
     * @param musicBean
     */

    public void setShareInfo(MusicBean musicBean) {
        if (musicBean != null) {
            this.musicBean = musicBean;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);
            if (null == configCallback) {
                return;
            }
            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
        }
    }

    /**
     * 调用前端方法
     */
    public void reSetVoteNum() {
        webView.reload();
//        HashMap<String, String> data = new HashMap<String, String>() {{
//            put("greetingFromJava", "Hi there, JS!");
//        }};
//        webView.callHandler("runWapApi", new JSONObject(data), new WVJBWebView.WVJBResponseCallback() {
//            @Override
//            public void onResult(Object data) {
//                Log.e(TAG, "分享统计参数: " + data);
//                Toast.create(CommonWebview.this).show(data.toString());
//            }
//        });
    }

    /**
     * 送礼物
     */
    private String musci_id = "";
    private BotomGiftListDialog giftListDialog = null;
    private Boolean isReGiveGift= false;
    private int gift_id= 0;
    private MemberGiftListBean.DataBean.ProfitBillboardConfigBean profit_billboard_config = null;
    private ImageView img_gift;
    private Panel rightPanel2;
    private TextView bck;
    private HexagonImageView switcher_head_icon;
    private void initPannel() {
        img_gift = findViewById(R.id.img_gift);
        rightPanel2 = findViewById(R.id.rightPanel2);
        bck = findViewById(R.id.bck);
        switcher_head_icon = findViewById(R.id.switcher_head_icon);
        AnimationDrawable animationDrawable = (AnimationDrawable) img_gift.getDrawable();
        animationDrawable.start();
        rightPanel2.setOnPanelListener(new Panel.OnPanelListener() {
            @Override
            public void onPanelClosed(Panel panel) {
                bck.setVisibility(View.GONE);
            }

            @Override
            public void onPanelOpened(Panel panel) {
                MobclickAgent.onEvent(CommonWebview.this, "player_gift");

                getGiftList();
                bck.setVisibility(View.VISIBLE);
                bck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rightPanel2.setOpen(false, true);
                        bck.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

    private void getGiftList() {
        rightPanel2.setVisibility(View.VISIBLE);
        //tv_musician_name.text = MediaService.bean?.nickname
        Glide.with(CommonWebview.this).load(MediaService.bean.getHead_link()).into(switcher_head_icon);
    }

    public void showBotomGiftDialog(String music_id) {
        rightPanel2.setOpen(false, false);
        BotomGiftListDialog giftListDialog = new BotomGiftListDialog();
        if(MediaService.bean == null){
//            giftListDialog.getMusic_id(MediaService.bean.getId());
        }
            giftListDialog.show(getSupportFragmentManager(), "giftListDialog");
        musci_id = music_id;
    }

    public void checkStatus(int gift_id) {
        this.gift_id = gift_id;
        HttpParams params = new HttpParams();
        Log.e("urlid",""+musci_id);
        params.put("music_id", musci_id.toString() + "");
        params.put("gift_id", gift_id+"");
        NetWork.INSTANCE.checkGift(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                CheckBean checkBean = JSON.parseObject(resultData, CheckBean.class);
                int code = checkBean.getCode();
                final CheckBean.DataBean data = checkBean.getData();
                switch (code){
                        case 0:
                            if (isReGiveGift) {
                                return;
                            }
                            isReGiveGift = false;
                            if(null!=data.getOrder_cate()&&null!=data.getOrder_cate().getSetting()&&data.getOrder_cate().getSetting().getFirst_charge()>0){
                                data.getOrder_cate().getSetting();
                                FirstChargeDialog.Companion.newInstance().showDialog(CommonWebview.this, new FirstChargeDialog.onBtClick() {
                                    @Override
                                    public void onConfirm() {
                                        startActivity(new Intent(CommonWebview.this, ChargeDoughnutActivity.class));
                                    }

                                    @Override
                                    public void onCancle() {
                                        //rechargeDialog(data);
                                    }
                                });
                            }else {
                                //rechargeDialog(data);
                            }
                            break;
                        case 1:
                            giveGiftDialog(data);
                            break;
                }
            }

            @Override
            public void doError(String msg) {
                           }

            @Override
            public void doResult() {

            }


        });
    }
//    /*充值*/
//    public void rechargeDialog(CheckBean.DataBean dataBean) {
//        if (dataBean.getOrder_cate() == null || dataBean.getOrder_cate().getLists() == null) {
//            return;
//        }
//        CheckBean.DataBean.OrderCateBean.ListsBean dLlists = dataBean.getOrder_cate().getLists();
//        for(int i = 0 ; i < dLlists; i++) {
//            system.out.println(list.get(i));
//        }
//    }
    public void giveGiftDialog(final CheckBean.DataBean dataBean){
//        if (null==dataBean.getIcon_info()) {
//            return;
//        }
        String content = "为《" + StringUtils.isEmpty(dataBean.getMusic_title()) + "》送";
        if (isReGiveGift) {
            content = "继续为《" + StringUtils.isEmpty(dataBean.getMusic_title()) + "》送";
            isReGiveGift = false;
        }
        String imurl = "";
        if(null==dataBean.getIcon_info()){
            imurl="";
        }else {
            imurl= dataBean.getIcon_info().getLink();
        }
        ConfirmGiveGiftDialog.Companion.newInstance()
                .content(content)
                .giftName(dataBean.getName())
                .giftUrl(imurl)
                .tips("剩余：" + dataBean.getMy_coin() + "甜甜圈")
                .showDialog(CommonWebview.this, new ConfirmGiveGiftDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        HttpParams params = new HttpParams();
                        params.put("music_id", musci_id);
                        params.put("gift_id", dataBean.getGift_id()+ "");
                        params.put("source", "source");
                        NetWork.INSTANCE.giveGift(CommonWebview.this, params, new NetWork.TokenCallBack() {

                            @Override
                            public void doNext(String resultData, Headers headers) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("GIFT_BEAN", dataBean);
                                Intent intent = new Intent(CommonWebview.this, GiiGiftSuccessActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            };

                            @Override
                            public void doError(String msg) {

                            }

                            @Override
                            public void doResult() {

                            }
                        });
                    }

                    @Override
                    public void onCancle() {

                    }
                });
}

    /**
     * 初始化表情面板
     */
    private EmotionMainFragment emotionMainFragment= null;
    private TextView et_pinglun;
    private Button btn_comment;
    private CommentSuccessReceiver commentSuccessReceiver = null;
    private IntentFilter filter = null;
    public void initEmotionMainFragment(final int id) {
        et_pinglun = findViewById(R.id.et_pinglun);
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, false);
        bundle.putInt(EmotionKeyboard.COMMENT_TYPE, 8);
        bundle.putInt(EmotionKeyboard.COMMENT_ITEM_ID, id);
        bundle.putInt(EmotionKeyboard.COMMENT_PID, 0);
        bundle.putInt(EmotionKeyboard.COMMENT_FID, 0);
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(et_pinglun);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }
    public void initEmotionMainFragmenthide(final int id) {
        et_pinglun = findViewById(R.id.et_pinglun);
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, false);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, true);
        bundle.putInt(EmotionKeyboard.COMMENT_TYPE, 8);
        bundle.putInt(EmotionKeyboard.COMMENT_ITEM_ID, id);
        bundle.putInt(EmotionKeyboard.COMMENT_PID, 0);
        bundle.putInt(EmotionKeyboard.COMMENT_FID, 0);
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(et_pinglun);
        emotionMainFragment.isHidden();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         */
        if (!emotionMainFragment.isInterceptBackPress()) {
            finish();
        }
        super.onBackPressed();
    }
    private void registerCommentReceiver() {
        commentSuccessReceiver = new CommentSuccessReceiver(new CommentSuccessReceiver.SuccessCallBack() {
            @Override
            public void callBack(@NotNull String json, @NotNull Intent intent) {
                setSnackBar("发表成功", "", R.drawable.icon_success);
                if(null!=json){
                    webView.reload();
                    fl_emotionview_main.setVisibility(View.GONE);
                    root_layout.setVisibility(View.GONE);
                    //                if (intent.getIntExtra(EmotionKeyboard.COMMENT_PID, 0) == 0) {
//                    Comment.DataBean commentBean;
//                    JSONObject jsonObject1 = JSON.parseObject(json);
//                    commentBean = jsonObject1.getObject("data", Comment.DataBean.class);
//                    //dataList.add(0, commentBean);
//                    //adapter.setNewData(dataList);
//                } else {
//                    int pid = intent.getIntExtra(EmotionKeyboard.COMMENT_PID, 0);
//                    JSONObject jsonObject1 = JSON.parseObject(json);
//                    if (jsonObject1 != null) {
//                        JSONObject data = jsonObject1.getJSONObject("data");
//                        String jsonString = data.toJSONString();
//                        Comment.DataBean.SonBean sonBean = JSON.parseObject(jsonString,Comment.DataBean.SonBean.class);
//                        for (i in dataList.indices) {
//                            val dateBean = dataList[i]
//                            if (dateBean.id == sonBean!!.fid) {
//                                var son:ArrayList<Comment.DataBean.SonBean>? = dateBean.son
//                                if (son == null) {
//                                    son = ArrayList<Comment.DataBean.SonBean>()
//                                }
//                                if (sonBean != null) {
//                                    son.add(0, sonBean)
//                                    for (i1 in 0 until son.size - 1) {
//                                        for (j in son.size - 1 downTo i1 + 1) {
//                                            if (son[j].id == son[i1].id) {
//                                                son.removeAt(j)
//                                            }
//                                        }
//                                    }
//                                }
////                                dateBean.son = son
////                                dataList[i] = dateBean
////                                adapter.notifyDataSetChanged()
//                                break;
//                            }
//                        }
//                    }
//                }
                }else {

                }
            }
        });
        filter = new IntentFilter(EmotionKeyboard.COMMENT_SUCCESS);
        registerReceiver(commentSuccessReceiver, filter);
    }
}

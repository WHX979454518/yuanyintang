package com.mxkj.yuanyintang.base.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.hyphenate.chat.EMClient;
import com.linsh.lshutils.others.NetUtils;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.base.dialog.CoinDialog;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.utils.screenshoot.ScreenShootDialog;
import com.mxkj.yuanyintang.utils.screenshoot.ScreenshotDetectionDelegate;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.widget.ViewLoading;
import com.mxkj.yuanyintang.widget.snackbar.Prompt;
import com.mxkj.yuanyintang.widget.snackbar.TSnackbar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
//import com.umeng.message.PushAgent;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public abstract class BaseActivity extends RxAppCompatActivity implements ScreenshotDetectionDelegate.ScreenshotDetectionListener {
    public static String TAG = "BaseActivity";
    public ViewLoading viewLoading;
    public LotterRecriver lotterRecriver;
    public IntentFilter lotterFilter;
    public DiaLogBuilder diaLogBuilder;
    public MaterialDialog materialDialog;
    private final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009;
    private ScreenshotDetectionDelegate screenshotDetectionDelegate = new ScreenshotDetectionDelegate(this, this);
    public CoinDialog coinDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkReadExternalStoragePermission();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lotterFilter = new IntentFilter();
        lotterFilter.addAction("showOutLoginDialog");
        lotterFilter.addAction("hideOutLoginDialog");
//        lotterFilter.addAction("hideCoinDialog");
//        lotterFilter.addAction("showCoinDialog");
        lotterRecriver = new LotterRecriver();
        registerReceiver(lotterRecriver, lotterFilter);
        PushAgent.getInstance(this).onAppStart();
        if (!NetUtils.isConnected(this)) {
            setSnackBar("请检查网络连接", "", R.drawable.icon_fails);
        }
        // 如果登录过，APP 长期在后台再进的时候也可能会导致加载到内存的群组和会话为空，
        // 可以在主页面的oncreate 里也加上这两句代码，当然，更好的办法应该是放在程序的开屏页
        //如果不提前载入，会导致首次点击消息中心进入消息中心后页面空白（环信消息记录必须在进入聊天界面之前加入到内存中）
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
        unregisterReceiver(lotterRecriver);
        if (null != viewLoading && viewLoading.isShowing()) {
            viewLoading.dismiss();
            viewLoading = null;
        } else {
            viewLoading = null;
        }
        System.gc();
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(getLocalClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.Companion.getApplication().setCurrentActivity(this);
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(getLocalClassName());
    }


    @Override
    protected void onStart() {
        super.onStart();
        screenshotDetectionDelegate.startScreenshotDetection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        screenshotDetectionDelegate.stopScreenshotDetection();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showReadExternalStoragePermissionDeniedMessage();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onScreenCaptured(String path) {
        ScreenShootDialog.newInstance().showDialog(this, path);
    }

    @Override
    public void onScreenCapturedWithDeniedPermission() {
    }

    private void checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadExternalStoragePermission();
        }
    }

    private void requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
    }

    private void showReadExternalStoragePermissionDeniedMessage() {
    }

    /**
     * 跳转页面
     *
     * @param mClass 目标页面
     */
    protected void goActivity(Class<?> mClass) {
        goActivity(mClass, null);
    }

    /**
     * 跳转页面带参数
     *
     * @param mClass 目标页面
     * @param bundle 参数
     */
    public void goActivity(Class<?> mClass, Bundle bundle) {
        Intent intent = new Intent(this, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 带页面回调跳转，不带参数
     *
     * @param mClass      目标页面
     * @param requestCode 请求code
     */
    protected void goActivityForResult(Class<?> mClass, int requestCode) {
        goActivityForResult(mClass, null, requestCode);
    }

    /**
     * 带页面回调跳转，带参数
     *
     * @param mClass      目标页面
     * @param bundle      参数
     * @param requestCode 请求码
     */

    protected void goActivityForResult(Class<?> mClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 页面顶部的消息提示
     */
    public void setSnackBar(String msg, final String action, int iconId) {
        if (TextUtils.isEmpty(msg)) return;
        final ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
        TSnackbar snackBar = TSnackbar.make(viewGroup, msg, TSnackbar.LENGTH_SHORT, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
        snackBar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackBar.setPromptThemBackground(Prompt.SUCCESS);
        snackBar.addIcon(iconId);
        snackBar.setBackgroundColor(Color.WHITE);
        snackBar.show();
    }

    protected UserInfo getUserInfo() {
        String string = CacheUtils.INSTANCE.getString(this, Constants.User.USER_JSON);
        UserInfo userInfo = null;
        if (!string.isEmpty()) {
            userInfo = JSON.parseObject(string, UserInfo.class);
        }
        return userInfo;
    }

    /**
     * 通用的二次确认提示框
     * 这个是之前刘杰留下的第三方的一个，我没删，我看有些地方在用
     * 我自己在base.dialog这个包下面按照UI设计的样式封装了一个通用的BaseConfirmDialog，建议用这个
     */
    public void MaterialDialog(String content, String tvBtnOk, String tvBtncancle, final MaterialDialogBtnClickListener dialogBtnClickListener) {
        if (materialDialog == null || (materialDialog != null && !materialDialog.isShowing())) {
            materialDialog = new MaterialDialog(this);
            materialDialog.content(content).btnText(tvBtncancle == null ? "取消" : tvBtncancle, tvBtnOk == null ? "确定" : tvBtnOk)
                    .titleTextSize(16)
                    .titleTextColor(ContextCompat.getColor(this, R.color.color_14_text))
                    .contentTextColor(ContextCompat.getColor(this, R.color.color_66_text))
                    .contentTextSize(14)
                    .btnTextSize(14)
                    .btnTextColor(ContextCompat.getColor(this, R.color.base_red), ContextCompat.getColor(this, R.color.base_red))
                    .showAnim(null)//
                    .dismissAnim(null)//
                    .show();
            materialDialog.setOnBtnClickL(
                    new OnBtnClickL() {//left btn click listener
                        @Override
                        public void onBtnClick() {
                            materialDialog.dismiss();
                            if (null != dialogBtnClickListener) {
                                dialogBtnClickListener.onBtnClick(0);
                            }
                        }
                    },
                    new OnBtnClickL() {//right btn click listener
                        @Override
                        public void onBtnClick() {
                            materialDialog.dismiss();
                            if (null != dialogBtnClickListener) {
                                dialogBtnClickListener.onBtnClick(1);
                            }
                        }
                    }
            );
        }
    }

    public interface MaterialDialogBtnClickListener {
        void onBtnClick(int code);
    }

    public void showLoadingView() {
        if (null == viewLoading) {
            viewLoading = new ViewLoading(this) {
                @Override
                public void loadCancel() {

                }
            };
            viewLoading.show();
        } else {
            viewLoading.show();
        }
    }

    public void hideLoadingView() {
        if (null != viewLoading) {
            viewLoading.dismiss();
        }
    }

    private void clearReferences() {
        Activity currActivity = MainApplication.Companion.getApplication().getCurrentActivity();
        if (this.equals(currActivity))
            MainApplication.Companion.getApplication().setCurrentActivity(null);
    }

    public class LotterRecriver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "showCoinDialog":
                        coinDialog = CoinDialog.Companion.newInstance();
                        coinDialog.showDialog(BaseActivity.this);
                        break;
                    case "hideCoinDialog":
                        if (coinDialog != null) {
                            coinDialog.dismiss();
                        }
                }
            }
        }
    }


    public void showCoinDialog() {
        coinDialog = CoinDialog.Companion.newInstance();
        coinDialog.showDialog(this);
    }
}

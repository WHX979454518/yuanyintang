package com.mxkj.yuanyintang.mainui.login_regist;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.TitleBarActivity;
import com.mxkj.yuanyintang.net.NetWork;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class UserNoticeActivity extends TitleBarActivity {
    @BindView(R.id.webView)
    WebView webView;

    @Override
    public View setContentLayout() {
        View view = View.inflate(this, R.layout.activity_user_notice, null);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initWebView();

        String code = getIntent().getStringExtra("code");
        setData(code);
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.getSettings().setMixedContentMode(webView.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);  //注意6.0权限
        }
    }

    private void setData(String code) {
        NetWork.INSTANCE.getUserNotice(this, code, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                JSONObject jsonObject = JSON.parseObject(resultData);
                JSONObject data = jsonObject.getJSONObject("data");
                String title = data.getString("title");
                String content = data.getString("content");
                setTitleText(title);
                webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    @OnClick(R.id.leftButton)
    public void onViewClicked() {
        finish();
    }
}

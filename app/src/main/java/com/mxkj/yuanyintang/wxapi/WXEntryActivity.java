package com.mxkj.yuanyintang.wxapi;


//import com.umeng.weixin.callback.WXCallbackActivity;

import android.os.Bundle;

import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            finish();
        }
    }

}

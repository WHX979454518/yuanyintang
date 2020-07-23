package com.mxkj.yuanyintang.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Keep;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

public class SophixStubApplication extends SophixApplication {
    private String currentVersion;

    @Keep
    @SophixEntry(MainApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        try {
            currentVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setContext(this)
                .setAppVersion(currentVersion)
                .setAesKey(null)
                .setEnableDebug(true)
//                .setUsingEnhance()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {

                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {

                        }
                    }
                }).initialize();
    }
}


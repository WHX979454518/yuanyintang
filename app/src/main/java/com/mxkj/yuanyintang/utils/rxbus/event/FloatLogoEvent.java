package com.mxkj.yuanyintang.utils.rxbus.event;

/**
 * Created by LiuJie on 2017/11/18.
 */

public class FloatLogoEvent {
    private String url;

    public FloatLogoEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.mxkj.yuanyintang.utils.rxbus.event;

/**
 * Created by LiuJie on 2017/10/30.
 */

public class MusicIanEvent {

    public MusicIanEvent(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

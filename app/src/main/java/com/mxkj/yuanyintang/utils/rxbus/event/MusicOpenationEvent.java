package com.mxkj.yuanyintang.utils.rxbus.event;

/**
 * Created by LiuJie on 2017/11/3.
 */

public class MusicOpenationEvent {

    private int position;
    private int type;

    public MusicOpenationEvent(int position, int type) {
        this.position = position;
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

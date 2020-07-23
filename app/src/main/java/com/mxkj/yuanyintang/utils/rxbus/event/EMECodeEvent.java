package com.mxkj.yuanyintang.utils.rxbus.event;

/**
 * Created by LiuJie on 2017/11/2.
 */

public class EMECodeEvent {

    private int code;
    //200,登录成功
    //201,账号错误
    //202,其他账号登录
    //203,本地服务器验证失效
    //100,收到消息
    //1 刷新消息
    //997 关闭页面跳转到池塘

    public EMECodeEvent(int code) {
        this.code = code;
    }

    public EMECodeEvent() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

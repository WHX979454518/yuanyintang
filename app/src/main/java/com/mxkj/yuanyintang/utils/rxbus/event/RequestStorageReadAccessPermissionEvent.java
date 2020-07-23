package com.mxkj.yuanyintang.utils.rxbus.event;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/30 下午11:23
 */
public class RequestStorageReadAccessPermissionEvent {

    private boolean success;

    public RequestStorageReadAccessPermissionEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

}

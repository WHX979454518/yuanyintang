package com.mxkj.yuanyintang.musicplayer.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by LiuJie on 2017/11/13.
 */

public class OnTouchUtil {
    static float sX, sY, eX, eY;

    public static void setONTouchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sX = motionEvent.getX();
                        sY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        eX = motionEvent.getX();
                        eY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL:
                        float v = Math.abs(eX - sX) - Math.abs(eY - sY);
                        if (v > 0) {
                            return false;
                        } else {
                            return true;
                        }
                }
                return false;
            }
        });
    }
}

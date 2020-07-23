package com.mxkj.yuanyintang.musicplayer.play_control;

import android.content.Context;
import android.util.Log;

import com.mxkj.yuanyintang.database.DBManager;
import com.mxkj.yuanyintang.base.bean.MusicInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.playList;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.setRandomPlayList;

public class PlayList {
    private static DBManager dbManager;

    /**
     * 添加到列表
     */
    public static void addToList(final Context context, final MusicInfo.DataBean dataBean) {

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                try {
//                    if (dbManager == null) {
                    dbManager = new DBManager(context);
//                }
                if (dataBean == null) return;
                dbManager.addToCurrentList(dataBean);
                if (playList == null) playList = new ArrayList<>();
                playList.add(dataBean);
                    for (int i = 0; i < playList.size(); i++) {
                        for (int j = playList.size(); j > i; j--) {
                            if (playList.get(j).getId() == playList.get(i).getId()) {
                                playList.remove(j);
                            }
                        }
                    }
                    Log.e("playListsize",""+playList.size());

                } catch (RuntimeException ex) {

                }

            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });

    }
    /**
     * 获取列表
     */
    public static List<MusicInfo.DataBean> getList(Context context) {
        DBManager dbManager = new DBManager(context);
        List<MusicInfo.DataBean> list = dbManager.query(1);
        return list;
    }

    /**
     * 从列表删除某首歌(通过id从数据库删同时从列表删)
     */
    public static void deleCurrMusic(Context context, MusicInfo.DataBean dataBean) {
        DBManager dbManager = new DBManager(context);
        dbManager.deleteMusic(dataBean);
        for (int i = 0; i < playList.size(); i++) {
            if (playList.size() > i && playList.get(i).getId() == dataBean.getId()) {
                playList.remove(i);
            }
        }
        setRandomPlayList();
    }
}

package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd;

import android.content.Context;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zuixia on 2018/4/20.
 */

public class SongDao {
    private Dao<Song, Integer> songDaope;
    private SongHelper songHelper;

    public SongDao(Context context) {
        try {
            songHelper = SongHelper.getHelper(context);
            songDaope = songHelper.getDao(Song.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一条数据
     */
    public void add(Song song) {
        try {
            songDaope.create(song);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //根据id（主键）删除一条数据
    public void deleteHelper(int id) {
        try {
            songHelper.getsongDao().deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //删除数据
    public void deleteAllHelper() {
        try {
            songHelper.getsongDao().delete(listHelper());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //根据id（主键）更新一条数据
    public void updateHelper(Song Song, int id) {
        try {
            Song.setId(id);
            songHelper.getsongDao().update(Song);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //返回list
    public void listHelper(Song Song, int id) {
        try {
            Song.setId(id);
            List<Song> songs = songHelper.getsongDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //返回list
    public List<Song> listHelper() {
        List<Song> songs = null;
        try {
            songs = songHelper.getsongDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs==null?new ArrayList<Song>():songs;
    }

    public void orderList(final String order, final SongDao.RefreshDataLisener refreshDataLisener) {
        Observable.fromIterable(listHelper())
                .toSortedList(new Comparator<Song>() {
                    @Override
                    public int compare(Song Song, Song Song2) {
                        if (TextUtils.equals("DESC", order)) {
                            return Song2.getId() - Song.getId();
                        } else {
                            return Song.getId() - Song2.getId();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Song>>() {
                    @Override
                    public void accept(@NonNull List<Song> song) throws Exception {
                        refreshDataLisener.refreshData(song);
                    }
                });
    }

    public interface RefreshDataLisener {
        void refreshData(List<Song> Song);
    }
}

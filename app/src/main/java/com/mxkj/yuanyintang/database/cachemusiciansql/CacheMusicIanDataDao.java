package com.mxkj.yuanyintang.database.cachemusiciansql;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LiuJie on 2017/12/5.
 */

public class CacheMusicIanDataDao {
    private Dao<CacheMusicIanDataBean, Integer> cacheMusicIanDataBeen;
    private CacheMusicIanDataBaseHelper cacheMusicIanDataBaseHelper;

    public CacheMusicIanDataDao(Context context) {
        try {
            cacheMusicIanDataBaseHelper = CacheMusicIanDataBaseHelper.getHelper(context);
            cacheMusicIanDataBeen = cacheMusicIanDataBaseHelper.getDao(CacheMusicIanDataBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一条数据
     *
     * @param user
     */
    public void add(CacheMusicIanDataBean user) {
        try {
            cacheMusicIanDataBeen.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否有数据
     * key
     * values
     */
    public Boolean isQueryData(String key, Object values) {
        try {
            List<CacheMusicIanDataBean> cacheMusicIanDataBeanList = cacheMusicIanDataBaseHelper.getMusicIanDataBeanIntegerDao().queryForEq(key, values);
            if (cacheMusicIanDataBeanList.size() > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否有数据
     * key
     * values
     */
    public List<CacheMusicIanDataBean> isQueryDataList(String key, Object values) {
        List<CacheMusicIanDataBean> cacheMusicIanDataBeanList = null;
        try {
            cacheMusicIanDataBeanList = cacheMusicIanDataBaseHelper.getMusicIanDataBeanIntegerDao().queryForEq(key, values);
            if (cacheMusicIanDataBeanList.size() > 0) {
                return cacheMusicIanDataBeanList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cacheMusicIanDataBeanList;
    }

    //根据id（主键）删除一条数据
    public void deleteHelper(int id) {
        try {
            cacheMusicIanDataBaseHelper.getMusicIanDataBeanIntegerDao().deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //删除数据
    public void deleteAllHelper() {
        try {
            cacheMusicIanDataBaseHelper.getMusicIanDataBeanIntegerDao().delete(listHelper());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //根据id（主键）更新一条数据
    public void updateHelper(CacheMusicIanDataBean cacheMusicIanDataBean, int id) {
        try {
            cacheMusicIanDataBean.setId(id);
            cacheMusicIanDataBaseHelper.getMusicIanDataBeanIntegerDao().update(cacheMusicIanDataBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //返回list
    public List<CacheMusicIanDataBean> listHelper(CacheMusicIanDataBean cacheMusicIanDataBean, int id) {
        List<CacheMusicIanDataBean> users = null;
        try {
            cacheMusicIanDataBean.setId(id);
            users = cacheMusicIanDataBaseHelper.getMusicIanDataBeanIntegerDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static final String TAG = "CacheMusicIanDataDao";

    //返回list
    public List<CacheMusicIanDataBean> listHelper() {
        List<CacheMusicIanDataBean> users = null;
        try {
            users = cacheMusicIanDataBaseHelper.getMusicIanDataBeanIntegerDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<CacheMusicIanDataBean> getCacheDataList(String key) {
        List<CacheMusicIanDataBean> musicIanDataBeanList = isQueryDataList("key", key);
        if (null != musicIanDataBeanList && musicIanDataBeanList.size() > 0) {
            return musicIanDataBeanList;
        }
        return null;
    }

    public CacheMusicIanDataBean getCacheData(String key) {
        CacheMusicIanDataBean data = null;
        List<CacheMusicIanDataBean> musicIanDataBeanList = isQueryDataList("key", key);
        if (null != musicIanDataBeanList && musicIanDataBeanList.size() > 0) {
            if (!TextUtils.isEmpty(musicIanDataBeanList.get(0).getData())) {
                data = musicIanDataBeanList.get(0);
            }
        }
        return data;
    }

    public void setCacheData(String resultData, String key, Long endTime) {
        if (!TextUtils.isEmpty(resultData)) {
            if (isQueryData("key", key)) {
                List<CacheMusicIanDataBean> cacheMusicIanDataBeanList = listHelper();
                for (int i = 0; i < cacheMusicIanDataBeanList.size(); i++) {
                    if (TextUtils.equals(key, cacheMusicIanDataBeanList.get(i).getKey())) {
                        CacheMusicIanDataBean musicIanDataBean = cacheMusicIanDataBeanList.get(i);
                        musicIanDataBean.setData(resultData)
                                .setEndtime(endTime);
                        updateHelper(musicIanDataBean, musicIanDataBean.getId());
                    }
                }
            } else {
                add(new CacheMusicIanDataBean().setData(resultData).setKey(key));
            }
        }
    }

    public void orderList(final String order, final RefreshDataLisener refreshDataLisener) {
        Observable.fromIterable(listHelper())
                .toSortedList(new Comparator<CacheMusicIanDataBean>() {
                    @Override
                    public int compare(CacheMusicIanDataBean cacheMusicIanDataBean, CacheMusicIanDataBean cacheMusicIanDataBean2) {
                        if (TextUtils.equals("DESC", order)) {
                            return cacheMusicIanDataBean2.getId() - cacheMusicIanDataBean.getId();
                        } else {
                            return cacheMusicIanDataBean.getId() - cacheMusicIanDataBean2.getId();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<CacheMusicIanDataBean>>() {
                    @Override
                    public void accept(@NonNull List<CacheMusicIanDataBean> cacheMusicIanDataBeen) throws Exception {
                        refreshDataLisener.refreshData(cacheMusicIanDataBeen);
                    }
                });
    }

    public interface RefreshDataLisener {
        void refreshData(List<CacheMusicIanDataBean> cacheMusicIanDataBeen);
    }

}

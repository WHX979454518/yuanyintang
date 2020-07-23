package com.mxkj.yuanyintang.database.updatafilesql;

import android.content.Context;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LiuJie on 2017/10/11.
 */

public class UpDataFileDao {
    private Context context;
    private Dao<UpDataFileBean, Integer> songHistoryDaope;
    private UpDataFileDatabaseHelper upDataFileDatabaseHelper;

    public UpDataFileDao(Context context) {
        this.context = context;
        try {
            upDataFileDatabaseHelper = UpDataFileDatabaseHelper.getHelper(context);
            songHistoryDaope = upDataFileDatabaseHelper.getDao(UpDataFileBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一条数据
     *
     * @param user
     */
    public void add(UpDataFileBean user) {
        try {
            songHistoryDaope.create(user);
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
            List<UpDataFileBean> upDataFileBeanList = upDataFileDatabaseHelper.getSongHistoryDao().queryForEq(key, values);
            if (upDataFileBeanList.size() > 0) {
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
    public List<UpDataFileBean> isQueryDataList(String key, Object values) {
        List<UpDataFileBean> upDataFileBeanList = null;
        try {
            upDataFileBeanList = upDataFileDatabaseHelper.getSongHistoryDao().queryForEq(key, values);
            if (upDataFileBeanList.size() > 0) {
                return upDataFileBeanList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upDataFileBeanList;
    }

    //根据id（主键）删除一条数据
    public void deleteHelper(int id) {
        try {
            upDataFileDatabaseHelper.getSongHistoryDao().deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //删除数据
    public void deleteAllHelper() {
        try {
            upDataFileDatabaseHelper.getSongHistoryDao().delete(listHelper());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //根据id（主键）更新一条数据
    public void updateHelper(UpDataFileBean upDataFileBean, int id) {
        try {
            upDataFileBean.setId(id);
            upDataFileDatabaseHelper.getSongHistoryDao().update(upDataFileBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //返回list
    public void listHelper(UpDataFileBean upDataFileBean, int id) {
        try {
            upDataFileBean.setId(id);
            List<UpDataFileBean> users = upDataFileDatabaseHelper.getSongHistoryDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //返回list
    public List<UpDataFileBean> listHelper() {
        List<UpDataFileBean> users = null;
        try {
            users = upDataFileDatabaseHelper.getSongHistoryDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void orderList(final String order, final UpDataFileDao.RefreshDataLisener refreshDataLisener) {
        Observable.fromIterable(listHelper())
                .toSortedList(new Comparator<UpDataFileBean>() {
                    @Override
                    public int compare(UpDataFileBean upDataFileBean, UpDataFileBean upDataFileBean2) {
                        if (TextUtils.equals("DESC", order)) {
                            return upDataFileBean2.getId() - upDataFileBean.getId();
                        } else {
                            return upDataFileBean.getId() - upDataFileBean2.getId();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<UpDataFileBean>>() {
                    @Override
                    public void accept(@NonNull List<UpDataFileBean> songHistoryBeen) throws Exception {
                        refreshDataLisener.refreshData(songHistoryBeen);
                    }
                });
    }

    public interface RefreshDataLisener {
        void refreshData(List<UpDataFileBean> upDataFileBean);
    }


    /**
     * 更新下载数据库状态为true
     */
    public void upSuccessDataKey(Object key) {
        try {
            List<UpDataFileBean> upDataFileBeanList = upDataFileDatabaseHelper.getSongHistoryDao().queryForEq("tastId", key);
            if (upDataFileBeanList.size() > 0) {
                UpDataFileBean upDataFileBean = upDataFileBeanList.get(0);
                upDataFileBean.setSuccess(true);
                updateHelper(upDataFileBean, upDataFileBean.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

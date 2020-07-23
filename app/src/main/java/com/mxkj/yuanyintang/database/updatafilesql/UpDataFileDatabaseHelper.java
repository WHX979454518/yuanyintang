package com.mxkj.yuanyintang.database.updatafilesql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by LiuJie on 2017/10/11.
 */

public class UpDataFileDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "sqlite_yyt_updatafile.db";

    private Dao<UpDataFileBean, Integer> upDataFileDao;

    private UpDataFileDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, UpDataFileBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "UpDataFileDatabaseHelpe";

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        更新表，使用ormlite提供的TableUtils.dropTable(connectionSource, User.class, true);进行删除操作~
//                删除完成后，别忘了，创建操作：onCreate(database, connectionSource);
        Log.d(TAG, "onUpgrade: ---------->" + oldVersion + "------->" + newVersion);
        try {
            TableUtils.dropTable(connectionSource, UpDataFileBean.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static UpDataFileDatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized UpDataFileDatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (UpDataFileDatabaseHelper.class) {
                if (instance == null)
                    instance = new UpDataFileDatabaseHelper(context);
            }
        }

        return instance;
    }

    /**
     * 获得userDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<UpDataFileBean, Integer> getSongHistoryDao() throws SQLException {
        if (upDataFileDao == null) {
            upDataFileDao = getDao(UpDataFileBean.class);
        }
        return upDataFileDao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        upDataFileDao = null;
    }


}

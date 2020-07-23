package com.mxkj.yuanyintang.database.cachemusiciansql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mxkj.yuanyintang.database.messagesql.MessageBean;
import com.mxkj.yuanyintang.database.messagesql.SystemMessageDatabaseHelper;

import java.sql.SQLException;

/**
 * Created by LiuJie on 2017/12/5.
 */

public class CacheMusicIanDataBaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "sqlite_yyt_cache_musician.db";
    /**
     * MessageDao ，每张表对于一个
     */
    private Dao<CacheMusicIanDataBean, Integer> musicIanDataBeanIntegerDao;

    private CacheMusicIanDataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CacheMusicIanDataBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        更新表，使用ormlite提供的TableUtils.dropTable(connectionSource, User.class, true);进行删除操作~
//                删除完成后，别忘了，创建操作：onCreate(database, connectionSource);
        try {
            TableUtils.dropTable(connectionSource, CacheMusicIanDataBean.class, true);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static CacheMusicIanDataBaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized CacheMusicIanDataBaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (CacheMusicIanDataBaseHelper.class) {
                if (instance == null)
                    instance = new CacheMusicIanDataBaseHelper(context);
            }
        }

        return instance;
    }

    /**
     * 获得CacheMusicIanDataBean
     *
     * @return
     * @throws SQLException
     */
    public Dao<CacheMusicIanDataBean, Integer> getMusicIanDataBeanIntegerDao() throws SQLException {
        if (musicIanDataBeanIntegerDao == null) {
            musicIanDataBeanIntegerDao = getDao(CacheMusicIanDataBean.class);
        }
        return musicIanDataBeanIntegerDao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        musicIanDataBeanIntegerDao = null;
    }
}

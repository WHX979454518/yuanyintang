package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

/**
 * Created by LiuJie on 2017/10/11.
 */

public class SongHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "sqlite_yyt_scanlist.db";
    /**
     * MessageDao ，每张表对于一个
     */
    private Dao<Song, Integer> songDao;

    private SongHelper(Context context) {
        super(context, TABLE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Song.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        更新表，使用ormlite提供的TableUtils.dropTable(connectionSource, User.class, true);进行删除操作~
//                删除完成后，别忘了，创建操作：onCreate(database, connectionSource);
        try {
            TableUtils.dropTable(connectionSource, Song.class, true);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SongHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized SongHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (SongHelper.class) {
                if (instance == null)
                    instance = new SongHelper(context);
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
    public Dao<Song, Integer> getsongDao() throws SQLException {
        if (songDao == null) {
            songDao = getDao(Song.class);
        }
        return songDao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        songDao = null;
    }

}

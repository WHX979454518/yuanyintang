package com.mxkj.yuanyintang.mainui.myself.helpcenter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
//import com.mxkj.yuanyintang.main_ui.myself.my_release.up_load_music.scan_sd.ChatHistoryHelper;

import java.sql.SQLException;

/**
 * Created by zuixia on 2018/4/24.
 */

public class ChatHistoryHelper extends OrmLiteSqliteOpenHelper {
    private static final String DataBase_NAME = "sqlite_yyt_chat_history.db";
    private static ChatHistoryHelper mDatabaseHelper = null;
    private Dao<ChatHistoryBean, Integer> mParentDao = null;
    private Dao<ChatHistoryBean.MutiTextBean, Integer> childDao = null;
    private final static int DataBase_VERSION = 1;

    public ChatHistoryHelper(Context context, String databaseName,
                             SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, DataBase_NAME, factory, DataBase_VERSION);
    }

    public static ChatHistoryHelper getInstance(Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new ChatHistoryHelper(context, DataBase_NAME,
                    null, DataBase_VERSION);
        }

        return mDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, ChatHistoryBean.class);
            TableUtils.createTableIfNotExists(connectionSource, ChatHistoryBean.MutiTextBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // 删除旧的数据库表。  
            TableUtils.dropTable(connectionSource, ChatHistoryBean.class, true);
            TableUtils.dropTable(connectionSource, ChatHistoryBean.MutiTextBean.class, true);

            // 重新创建新版的数据库。  
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<ChatHistoryBean.MutiTextBean, Integer> getchildDao() {
        if (childDao == null) {
            try {
                childDao = getDao(ChatHistoryBean.MutiTextBean.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }

        return childDao;
    }

    public Dao<ChatHistoryBean, Integer> getClassDao() {
        if (mParentDao == null) {
            try {
                mParentDao = getDao(ChatHistoryBean.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }

        return mParentDao;
    }

    @Override
    public void close() {
        super.close();
        mParentDao = null;
        childDao = null;
    }
}

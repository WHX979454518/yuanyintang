package com.mxkj.yuanyintang.database.songhistorysql

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

import java.sql.SQLException

class SongHistoryDatabaseHelper constructor(context: Context) : OrmLiteSqliteOpenHelper(context, TABLE_NAME, null, 1) {
    /**
     * MessageDao ，每张表对于一个
     */
    internal lateinit var songHistoryDao: Dao<SongHistoryBean, Int>

    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable(connectionSource, SongHistoryBean::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    override fun onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        //        更新表，使用ormlite提供的TableUtils.dropTable(connectionSource, User.class, true);进行删除操作~
        //                删除完成后，别忘了，创建操作：onCreate(database, connectionSource);
        try {
            TableUtils.dropTable<SongHistoryBean, Any>(connectionSource, SongHistoryBean::class.java, true)
            onCreate(database, connectionSource)

        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    /**
     * 获得userDao
     *
     * @return
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun getSongHistoryDao(): Dao<SongHistoryBean, Int> {
        if (songHistoryDao == null) {
            songHistoryDao = getDao(SongHistoryBean::class.java)
        }
        return songHistoryDao
    }

    /**
     * 释放资源
     */
    override fun close() {
        super.close()
    }

    companion object {
        private val TABLE_NAME = "sqlite_yyt_songhistory.db"

        private var instance: SongHistoryDatabaseHelper? = null

        /**
         * 单例获取该Helper
         *
         * @param context
         * @return
         */
        @Synchronized
        fun getHelper(context: Context): SongHistoryDatabaseHelper? {
            if (instance == null) {
                synchronized(SongHistoryDatabaseHelper::class.java) {
                    if (instance == null)
                        instance = SongHistoryDatabaseHelper(context)
                }
            }
            return instance
        }
    }

}

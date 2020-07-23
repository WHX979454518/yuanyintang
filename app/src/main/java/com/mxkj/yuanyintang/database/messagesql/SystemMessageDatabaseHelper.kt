package com.mxkj.yuanyintang.database.messagesql


import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

import java.sql.SQLException

class SystemMessageDatabaseHelper private constructor(context: Context) : OrmLiteSqliteOpenHelper(context, TABLE_NAME, null, 1) {
    /**
     * MessageDao ，每张表对于一个
     */
    internal lateinit var messageDao: Dao<MessageBean, Int>
    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable(connectionSource, MessageBean::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    override fun onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        //        更新表，使用ormlite提供的TableUtils.dropTable(connectionSource, User.class, true);进行删除操作~
        //                删除完成后，别忘了，创建操作：onCreate(database, connectionSource);
        try {
            TableUtils.dropTable<MessageBean, Any>(connectionSource, MessageBean::class.java, true)
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
    fun getMessageDao(): Dao<MessageBean, Int>? {
        if (messageDao == null) {
            messageDao = getDao(MessageBean::class.java)
        }
        return messageDao
    }

    /**
     * 释放资源
     */
    override fun close() {
        super.close()
    }

    companion object {
        private val TABLE_NAME = "sqlite_yyt_message.db"
        private var instance: SystemMessageDatabaseHelper? = null
        /**
         * 单例获取该Helper
         *
         * @param context
         * @return
         */
        @Synchronized
        fun getHelper(context: Context): SystemMessageDatabaseHelper? {
            if (instance == null) {
                synchronized(SystemMessageDatabaseHelper::class.java) {
                    if (instance == null)
                        instance = SystemMessageDatabaseHelper(context)
                }
            }
            return instance
        }
    }

}

package com.mxkj.yuanyintang.database.messagesql

import android.content.Context
import android.text.TextUtils

import com.j256.ormlite.dao.Dao

import java.sql.SQLException
import java.util.Comparator

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class MessageDao(private val context: Context) {
    private lateinit var messageDaoOpe: Dao<MessageBean, Int>
    private lateinit var messageDatabaseHelper: SystemMessageDatabaseHelper
    init {
        try {
            messageDatabaseHelper = SystemMessageDatabaseHelper.getHelper(context)!!
            messageDaoOpe = messageDatabaseHelper!!.getDao(MessageBean::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    /**
     * 增加一条数据
     *
     * @param user
     */
    fun add(user: MessageBean) {
        try {
            messageDaoOpe!!.create(user)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //根据id（主键）删除一条数据
    fun deleteHelper(id: Int) {
        try {
            messageDatabaseHelper!!.messageDao.deleteById(id)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //删除数据
    fun deleteAllHelper() {
        try {
            messageDatabaseHelper!!.messageDao.delete(listHelper())
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //根据id（主键）更新一条数据
    fun updateHelper(messageBean: MessageBean, id: Int) {
        try {
            messageBean.id = id
            messageDatabaseHelper!!.messageDao.update(messageBean)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //返回list
    fun listHelper(messageBean: MessageBean, id: Int) {
        try {
            messageBean.id = id
            val users = messageDatabaseHelper!!.messageDao.queryForAll()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //返回list
    fun listHelper(): List<MessageBean>? {
        var users: List<MessageBean>? = null
        try {
            users = messageDatabaseHelper!!.messageDao.queryForAll()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return users
    }

    fun orderList(order: String, refreshDataLisener: RefreshDataLisener) {
        Observable.fromIterable(listHelper()!!)
                .toSortedList { messageBean, messageBean2 ->
                    if (TextUtils.equals("DESC", order)) {
                        messageBean2.id - messageBean.id
                    } else {
                        messageBean.id - messageBean2.id
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe { messageBeen -> refreshDataLisener.refreshData(messageBeen) }
    }

    interface RefreshDataLisener {
        fun refreshData(messageBeen: List<MessageBean>)
    }

    companion object {

        private val TAG = "MessageDao"
    }
}

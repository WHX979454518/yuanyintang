package com.mxkj.yuanyintang.database.songhistorysql

import android.content.Context
import android.text.TextUtils

import com.j256.ormlite.dao.Dao

import java.sql.SQLException
import java.util.Comparator

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class SongHistoryDao(context: Context) {
    lateinit var songHistoryDaope: Dao<SongHistoryBean, Int>
    lateinit var songHistoryDatabaseHelper: SongHistoryDatabaseHelper
    init {
        try {
            songHistoryDatabaseHelper = SongHistoryDatabaseHelper.getHelper(context)!!
            songHistoryDaope = songHistoryDatabaseHelper!!.getDao(SongHistoryBean::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    /**
     * 增加一条数据
     *
     * @param user
     */
    fun add(user: SongHistoryBean) {
        try {
            songHistoryDaope!!.create(user)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //根据id（主键）删除一条数据
    fun deleteHelper(id: Int) {
        try {
            songHistoryDatabaseHelper.songHistoryDao.deleteById(id)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //删除数据
    fun deleteAllHelper() {
        try {
            songHistoryDatabaseHelper!!.songHistoryDao.delete(listHelper())
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //根据id（主键）更新一条数据
    fun updateHelper(songHistoryBean: SongHistoryBean, id: Int) {
        try {
            songHistoryBean.setId(id)
            songHistoryDatabaseHelper!!.songHistoryDao.update(songHistoryBean)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //返回list
    fun listHelper(songHistoryBean: SongHistoryBean, id: Int) {
        try {
            songHistoryBean.setId(id)
            val users = songHistoryDatabaseHelper!!.songHistoryDao.queryForAll()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //返回list
    fun listHelper(): List<SongHistoryBean>? {
        var users: List<SongHistoryBean>? = null
        try {
            users = songHistoryDatabaseHelper!!.songHistoryDao.queryForAll()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return users
    }

    fun orderList(order: String, refreshDataLisener: SongHistoryDao.RefreshDataLisener) {
        Observable.fromIterable(listHelper()!!)
                .toSortedList { songHistoryBean, songHistoryBean2 ->
                    if (TextUtils.equals("DESC", order)) {
                        songHistoryBean2.getId() - songHistoryBean.getId()
                    } else {
                        songHistoryBean.getId() - songHistoryBean2.getId()
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe { songHistoryBeen -> refreshDataLisener.refreshData(songHistoryBeen) }
    }

    interface RefreshDataLisener {
        fun refreshData(songHistoryBean: List<SongHistoryBean>)
    }
}

package com.mxkj.yuanyintang.utils.file

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import android.view.View
import com.mxkj.yuanyintang.base.MainApplication

import com.mxkj.yuanyintang.utils.constant.Constants

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.StreamCorruptedException

object CacheUtils {
    /**
     * logintoken、登录状态标记、accesstoken存在IMPORT这个下面
     * 其他缓存存在MXKJ下面，用户清理缓存的时候IMPORT这个sp可以和其他缓存信息一起直接删除，不影响登录状态和token
     */
    private const val SP_NAME = "MXKJ"
    private const val SP_IMPORT_NAME = "IMPORT"
    private var sp: SharedPreferences? = null

    private fun getSp(context: Context, key: String?): SharedPreferences? {
        sp = if (key == null || key == Constants.User.IS_LOGIN || key == Constants.User.USER_JSON || key == Constants.User.USER_LOGIN_TOKEN || key == "token"|| key == "home"|| key == "remark"|| key == "devicekey") {
            context.getSharedPreferences(SP_IMPORT_NAME, Context.MODE_PRIVATE)
        } else {
            context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        }
        return sp
    }

    /**
     * 获取boolean 数据
     *
     * @param context
     * @param key
     * @return 如果没有值，返回false
     */
    fun getBoolean(context: Context?, key: String): Boolean {
        context?.let {
            val sp = getSp(context, key)
            return sp!!.getBoolean(key, false)
        }
        return false
    }

    fun clearSpData(context: Context) {
        val sp = getSp(context, "CLEAR")
        sp?.edit()?.clear()?.apply()
    }


    /**
     * 获取boolean 数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    fun getBoolean(context: Context?, key: String,
                   defValue: Boolean): Boolean {
        context?.let {
            val sp = getSp(context, key)
            return sp!!.getBoolean(key, defValue)
        }
        return false
    }

    fun getBoolean(context: Context, view: View, defValue: Boolean): Boolean {
        val sp = getSp(context, "BOOLEAN")

        return sp?.getBoolean(generateUniqId(view), defValue) ?: false
    }

    private fun generateUniqId(v: View): String {
        return Constants.User.SHOW_GUIDE_PREFIX + v.id
    }

    /**
     * 存boolean缓存
     *
     * @param view
     * @param value
     */
    fun setBoolean(context: Context, view: View, value: Boolean) {
        val sp = getSp(context, "BOOLEAN")
        val editor = sp!!.edit()
        editor.putBoolean(generateUniqId(view), value)
        editor.commit()

    }

    /**
     * 存boolean缓存
     *
     * @param context
     * @param key
     * @param value
     */
    fun setBoolean(context: Context?, key: String, value: Boolean) {
        context?.let {
            val sp = getSp(context, key)
            val editor = sp!!.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

    }

    /**
     * 获取String 数据
     *
     * @param context
     * @param key
     * @return 如果没有值，返回null
     */
    fun getString(context: Context?, key: String?): String? {
        context?.let {
            Log.e(key+"__getdata","");
            val sp = getSp(context, key)
            return sp!!.getString(key, null)
        }
        return null

    }

    /**
     * 获取String 数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    fun getString(context: Context?, key: String, defValue: String?): String? {
        context?.let {

            val sp = getSp(context, key)
            return CacheUtils.sp?.getString(key, defValue)
        }
        return null
    }

    fun getLong(context: Context, key: String, defValue: Long): Long {
        val sp = getSp(context, key)
        return sp?.getLong(key, defValue)!!
    }

    /**
     * 存String缓存
     *
     * @param context
     * @param key
     * @param value
     */
    fun setString(context: Context?, key: String?, value: String?) {
        context?.let {
            val sp = getSp(context, key)
            val editor = sp?.edit()
//            Log.e(key+"__data",value);
            editor?.putString(key, value + "")
            editor?.commit()
        }

    }

    /**
     * 获取int 数据
     *
     * @param context
     * @param key
     * @return 如果没有值，返回-1
     */
    fun getInt(context: Context, key: String): Int {
        val sp = getSp(context, key)
        return sp!!.getInt(key, -1)
    }

    /**
     * 获取int 数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    fun getInt(context: Context?, key: String, defValue: Int): Int {
        context?.let {
            val sp = getSp(context, key)
            return sp!!.getInt(key, defValue)
        }
        return 0
    }

    /**
     * 存int缓存
     *
     * @param context
     * @param key
     * @param value
     */
    fun setInt(context: Context, key: String, value: Int) {
        val sp = getSp(context, key)
        val editor = sp!!.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun setLong(context: Context, key: String, value: Long?) {
        val sp = getSp(context, key)
        val editor = sp!!.edit()
        editor.putLong(key, value!!)
        editor.commit()
    }

    @Throws(IOException::class)
    fun SceneList2String(SceneList: List<*>): String {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        val byteArrayOutputStream = ByteArrayOutputStream()
        // 然后将得到的字符数据装载到ObjectOutputStream
        val objectOutputStream = ObjectOutputStream(
                byteArrayOutputStream)
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList)
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        val SceneListString = String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT))
        // 关闭objectOutputStream
        objectOutputStream.close()
        return SceneListString
    }

    @Throws(StreamCorruptedException::class, IOException::class, ClassNotFoundException::class)
    fun String2SceneList(SceneListString: String): List<*> {
        val mobileBytes = Base64.decode(SceneListString.toByteArray(),
                Base64.DEFAULT)
        val byteArrayInputStream = ByteArrayInputStream(
                mobileBytes)
        val objectInputStream = ObjectInputStream(
                byteArrayInputStream)
        val SceneList = objectInputStream.readObject() as List<*>
        objectInputStream.close()
        return SceneList
    }
}

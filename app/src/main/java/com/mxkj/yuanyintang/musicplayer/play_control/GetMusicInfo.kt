package com.mxkj.yuanyintang.musicplayer.play_control

import android.content.Context
import android.util.Log

import com.alibaba.fastjson.JSON
import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.net.ApiAddress
import com.mxkj.yuanyintang.net.HttpUtils
import com.mxkj.yuanyintang.net.NetWork

import okhttp3.Headers

object GetMusicInfo {
    /**
     * 播放音乐时获取详情（同时播放次数会加一）
     */
    fun playById(context: Context, musicId: Int, song_id: Int, setBeanData: SetBeanData) {
//        val url = ApiAddress.BASE_URL + "/v2/api/music/play?id=" + musicId
        val url = ApiAddress.BASE_URL + "/v2/api/music/play/" + musicId
        val params = HttpParams()
//        params.put("song_id", song_id.toString() + "")
        params.put("song_id", song_id.toString() + "")
        getData(context, url, params, setBeanData)
    }

    /**
     * 只会请求音乐详情，不加播放次数
     */
    fun getMusicDetial(context: Context, musicId: Int, setBeanData: SetBeanData) {
        NetWork.getMusicDetails(context, musicId.toString() + "", 0L, object : NetWork.TokenCallBack {
            override fun doNext(resultData: String, headers: Headers?) {
                val jsonObject = JSON.parseObject(resultData)
                val data = jsonObject.getJSONObject("data") ?: return
                val dataStr = data.toJSONString()
                val infoListBean = JSON.parseObject(dataStr, MusicInfo.DataBean::class.java)
                if (infoListBean != null) {
                    setBeanData.setBeanData(infoListBean)
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    private fun getData(context: Context, url: String, params: HttpParams, setBeanData: SetBeanData) {
        HttpUtils.request(false, "get", context, url, params, object : HttpUtils.DoNext() {
            override fun doNext(responseString: String, headers: Headers) {
                Log.e("reslt_data",responseString)
                val jObject = JSON.parseObject(responseString)
                val code = jObject.getInteger("code")
                val dataBean = JSON.parseObject(jObject.getJSONObject("data").toJSONString(), MusicInfo.DataBean::class.java)
                if (dataBean != null) {
                    if (code == 501) {//"抱歉，该歌曲已下架！
                    } else {

                        setBeanData.setBeanData(dataBean)
                    }
                }
            }

            override fun doError(msg: String) {

            }

            override fun doResult() {

            }
        })
    }

    interface SetBeanData {
        fun setBeanData(dataBean: MusicInfo.DataBean?)
    }

}

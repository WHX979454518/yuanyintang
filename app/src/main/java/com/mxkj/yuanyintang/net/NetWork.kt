package com.mxkj.yuanyintang.net

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject

import com.lzy.okgo.model.HttpParams
import com.mxkj.yuanyintang.R.id.action
import com.mxkj.yuanyintang.base.MainApplication
import com.mxkj.yuanyintang.mainui.web.H5Interface
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil
import com.mxkj.yuanyintang.utils.constant.Constants
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.mxkj.yuanyintang.utils.image.IMGCompressUtils
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager
import com.umeng.socialize.bean.SHARE_MEDIA

import okhttp3.Headers
import java.io.File

object NetWork {
    private const val TAG = "NetWork"
    private fun getSecretRequest(needCache: Boolean, requestMethod: String, context: Context, api: String, params: HttpParams??, callBack: TokenCallBack) {
        HttpUtils.request(needCache, requestMethod, context, ApiAddress.BASE_URL + api, params, object : HttpUtils.DoNext() {
            @SuppressLint("WrongConstant")
            override fun doNext(responseString: String, headers: Headers?) {
                callBack.doNext(responseString, headers)
            }

            override fun doError(msg: String) {
                Log.e("TAG", "ERRor!!!!!!!!!")
                callBack.doError(msg)
            }

            override fun doResult() {
                callBack.doResult()
            }
        })
    }

    /**
     * 带缓存的请求
     *
     * 1 判断cacheKey是否为空 如果为空则为普通请求。
     *2 判断指定cacheKey 是否有数据 如果没有数据则执行请求并且根据请求结果缓存数据
     * 3 有数据则先调用callBack.doNext（）返回数据
     * 4 执行线程池发送请求 再次调用doNext
     *
     * @author  fenghao
     *@param cacheKey: String
     */
    private fun getSecretRequest(cacheKey: String, requestMethod: String, context: Context, api: String, params: HttpParams??, callBack: TokenCallBack) {
        if(cacheKey.isNullOrEmpty()){
            getSecretRequest(false,requestMethod,context,api,params,callBack)
            return
        }
        val responseString = CacheUtils.getString(context, cacheKey)
        if (responseString.isNullOrEmpty()) {
            HttpUtils.request(false, requestMethod, context, ApiAddress.BASE_URL + api, params, object : HttpUtils.DoNext() {
                @SuppressLint("WrongConstant")
                override fun doNext(responseString: String, headers: Headers?) {
                    callBack.doNext(responseString, headers)
                    CacheUtils.setString(context,cacheKey,responseString)
                }
                override fun doError(msg: String) {
                    Log.e("TAG", "ERRor!!!!!!!!!")
                    callBack.doError(msg)
                }
                override fun doResult() {
                    callBack.doResult()
                }
            })
        } else {
            callBack.doNext(responseString!!,null)
            ThreadPoolManager.getInstance().execute(object:Runnable{
                override fun run() {
                    HttpUtils.request(false, requestMethod, context, ApiAddress.BASE_URL + api, params, object : HttpUtils.DoNext() {
                        @SuppressLint("WrongConstant")
                        override fun doNext(responseString: String, headers: Headers?) {
                            callBack.doNext(responseString, headers)
                            Log.e(cacheKey,"pageCount")
                            CacheUtils.setString(context,cacheKey,responseString)
                        }
                        override fun doError(msg: String) {
                            Log.e("TAG", "ERRor!!!!!!!!!")
                            callBack.doError(msg)
                        }
                        override fun doResult() {
                            callBack.doResult()
                        }
                    })
                }
            })

        }
    }

    /**
     * 获取home
     */
    fun getHome(cacheKey: String, context: Context, callBack: TokenCallBack) {
        getSecretRequest(cacheKey, "get", context, ApiAddress.GET_HOME, null, callBack)
    }

    /**
     * 获取home公告消息
     */
    fun getHomeSystemMsg(needCache: Boolean, context: Context, callBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(needCache, "get", context, ApiAddress.GET_HOME_ACTIVITY, params, callBack)
    }

    /**
     * 获取"世界"
     */
    fun getDynamicWorld(page: Int, isRefresh: Boolean, context: Context, endTime: Long, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        params.put("row", 10.toString())
        if (page == 1) {
            params.put("update_time", endTime)
        }
        getSecretRequest(page == 1 && !isRefresh, "get", context, ApiAddress.GET_DYNAMIC, params, callBack)
    }

    /**
     * 获取"观测者"
     */
    fun getDynamicObserval(page: Int, isRefresh: Boolean, context: Context, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        params.put("row", 10.toString())
        getSecretRequest(page == 1 && !isRefresh, "get", context, ApiAddress.GET_OBSERVALABLE, params, callBack)
    }

    /**
     * 获取"用户动态"
     */
    fun getDynamicObserval(page: Int, isRefresh: Boolean, context: Context, uid: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("uid", uid)
        params.put("p", page.toString())
        params.put("row", 15)
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_DYNAMIC, params, callBack)
    }


    /**
     * 获取池塘
     */

    fun getPond(cacheKey: String, type: String, context: Context, params: HttpParams?, callBack: TokenCallBack) {
        Log.e("TAG", "池塘参数$params")
        val url = when (type) {
            "follow" -> ApiAddress.GET_FOLLOW_POND
            "near" -> ApiAddress.GET_NEAR_POND
            else -> ApiAddress.GET_POND
        }

        getSecretRequest(cacheKey, "get", context, url, params, callBack)
    }

    /**
     * 获取池塘banner\标签
     * /v2/api/home/topic_nav
     */
    fun getPondBanner(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.POND_BANNER_TAG, params, callBack)
    }

    /**
     * 获取标签
     */
    fun getPublicTag(pid: Int, item_id: Int, p: Int, context: Context, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("pid", pid.toString())
        params.put("item_id", item_id.toString())
        params.put("p", p.toString())
        params.put("row", 100)
        params.put("order", "times-desc")
        getSecretRequest(false, "get", context, ApiAddress.GET_PUBLIC_TAG, params, callBack)
    }

    /**
     * 获取音乐列表
     */
    fun getPublicMusic(params: HttpParams?, context: Context, callBack: TokenCallBack) {
        Log.e("TAG", "获取音乐列表==" + params.toString())
        getSecretRequest(false, "get", context, ApiAddress.GET_PUBLIC_MUSIC, params, callBack)
    }
    /**
    * 获取标签（新的）
    */
    fun getPublicTagTag(pid: String, context: Context, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("alias", pid)
        getSecretRequest(false, "get", context, ApiAddress.GET_PUBLIC_TAG, params, callBack)
    }

    /**
     * 获取音乐人列表
     */
    fun getPublicMusicIan(tag: Int, tagStr: String, p: Int, context: Context, order: String, callBack: TokenCallBack) {
        val params = HttpParams()
        if (tagStr != null && tag != 0) {
            params.put(tagStr, tag.toString())
        }
        params.put("p", p.toString())
        params.put("order", "publish-desc")
        getSecretRequest(false, "get", context, ApiAddress.GET_PUBLIC_MUSICIAN, params, callBack)
    }
    /**
     * 点击启动图增加次数
     */
    fun getStartAddCount(id: String, context: Context,callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("id", id)
        getSecretRequest(false, "get", context, ApiAddress.GET_STARTADDCOUNT, params, callBack)
    }
    /**
     * banner 点击增加次数
     */
    fun getOnClicknum(id: Int, context: Context,callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("id", id)
        getSecretRequest(false, "get", context, ApiAddress.GET_ONCLICKNUM, params, callBack)
    }
    /**
     * 获取歌单列表
     */
    fun getPublicMusicSong(tag: Int, p: Int, context: Context, order: String?, callBack: TokenCallBack) {
        val params = HttpParams()
        var url = ApiAddress.GET_PUBLIC_MUSIC_SONG
        if (tag != 0) {
            params.put("tag", tag.toString())
        }
        when (order) {
            "recommend" -> {
                params.put("row", 5)
                url = "/v2/api/home/recommend_song"
            }
            "guess" -> {
                params.put("row", 6)
                url = "/v2/api/home/guess_you_like"
            }
            else -> {
                params.put("row", 24)
                params.put("order", order ?: "")
            }
        }
        params.put("p", p.toString())
        params.put("order", "counts-desc")
        getSecretRequest(false, "get", context, url, params, callBack)
    }

    /**
     * 获取验证码
     * reg注册,login登录,find找回密码，pwd修改密码，unbind解除绑定，sms快速登录/绑定
     */
    fun getCode(third_log_type: String?, type: String?, byWhat: String?, mobile: String?, context: Context, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("type", type)
        if (!TextUtils.isEmpty(byWhat)) {
            params.put("choose", byWhat)
        }
        if (!TextUtils.isEmpty(mobile)) {
            params.put("name", mobile)
        }
        if (!TextUtils.isEmpty(third_log_type)) {
            params.put("third_log_type", third_log_type)
        }
        Log.e(TAG, "getCode: $params")
        getSecretRequest(false, "get", context, ApiAddress.GET_CODE, params, callBack)
    }

    /**
     * 快速登录\绑定手机\强制绑定手机
     */

    fun bindMobile(must: Boolean, inviteCode: String, mobile: String, code: String, context: Context, callBack: TokenCallBack) {

        val params = HttpParams()
        if (!TextUtils.isEmpty(inviteCode)) {
            params.put("incode", inviteCode)
        }
        params.put("device_token", MainApplication.pushAgent.registrationId)
        params.put("device_finger", MainApplication.pushAgent.registrationId)
        params.put("reg_at", "android")
        val api: String
        params.put("mobile", mobile)
        api = if (must) {
            params.put("bind_code", code)
            ApiAddress.MUST_BIND_MOBILE
        } else {
            params.put("thirdcode", code)
            ApiAddress.QUICK_LOGIN
        }
        Log.e(TAG, "bindMobile: $params")
        getSecretRequest(false, "post", context, api, params, callBack)
    }

    /**
     * 注册
     * name	是	string	邮箱或者手机号
     * password	是	string	密码
     * nickname	否	string	昵称
     * code	是	string	验证码
     * mycode	否	string	邀请码
     */
    fun Regist(context: Context, params: HttpParams, callBack: TokenCallBack) {
        Log.e(TAG, "Regist: ===$params")
        params.put("device_token", MainApplication.pushAgent.registrationId)
        params.put("device_finger", MainApplication.pushAgent.registrationId)
        params.put("reg_at", "android")
        getSecretRequest(false, "post", context, ApiAddress.REGIST, params, callBack)
    }

    /**
     * 修改头像
     */
    fun changeIcon(context: Context, hash: String, callback: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.AVATAR, HttpParams("head", hash), callback)
    }

    /**
     * 账号密码登录
     */
    fun PwdLogin(context: Context, name: String, pwd: String, canshu: String, callBack: TokenCallBack) {
        val params = HttpParams()
//        if(!canshu.equals("")){
////            val title = jsobj!!.getString("title")
////            val headerType = jsobj.getString("headerType")
//            val json_test:JSONObject
//            json_test= JSONObject.parseObject(canshu)
//            val key: MutableSet<String> = json_test.keys
//            val value: MutableCollection<Any> = json_test.values
//            Log.e("gggg",""+key)
//            Log.e("gggg",""+value)
//            params.put(key.toString(),value.toString())
//        }
        if(null!=canshu&&!canshu.equals("")&&canshu.equals("{}")){
            if(canshu.equals("{}")){

            }else{
                val jsobj = H5Interface.getJsobj(params.toString())
                if(null!=jsobj!!.getString("reg_from")||jsobj!!.getString("reg_from")==""){
                    val reg_from = jsobj!!.getString("reg_from")
                    params.put("reg_from",reg_from)
                }
            }

        }



        params.put("loginname", name)
        params.put("password", pwd)
        val registrationId = MainApplication.pushAgent.registrationId
        params.put("device_token", registrationId)
        params.put("device_finger", registrationId)
        Log.e(TAG, "PwdLogin==: $params")
        getSecretRequest(false, "post", context, ApiAddress.PWD_LOGIN, params, callBack)
    }

    /**
     * 音乐详情
     * id	是	string
     */
    fun getMusicDetails(context: Context, id: String, endTime: Long, callBack: TokenCallBack) {
        val params = HttpParams()
        if (endTime > 0) {
            params.put("update_time", endTime)
        }
        getSecretRequest(false, "get", context, ApiAddress.GET_MUSIC_DETAILS + id, params, callBack)
    }

    /**
     * 验证第三方账号
     */
    fun IsBind(context: Context, platform: SHARE_MEDIA, data: Map<String, String>, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("device_token", MainApplication.pushAgent.registrationId)
        params.put("device_finger", MainApplication.pushAgent.registrationId)
        when (platform) {
            SHARE_MEDIA.QQ -> params.put("type", 2.toString())
            SHARE_MEDIA.WEIXIN -> params.put("type", 1.toString())
            SHARE_MEDIA.SINA -> params.put("type", 3.toString())
        }
        params.put("openid", data["uid"])
        getSecretRequest(false, "post", context, ApiAddress.IS_BIND, params, callBack)
    }

    fun canBind(context: Context, platform: SHARE_MEDIA, data: Map<String, String>, callBack: TokenCallBack) {
        val params = HttpParams()
        when (platform) {
            SHARE_MEDIA.QQ -> params.put("type", 2.toString())
            SHARE_MEDIA.WEIXIN -> params.put("type", 1.toString())
            SHARE_MEDIA.SINA -> params.put("type", 3.toString())
        }
        params.put("openid", data["uid"])
        getSecretRequest(false, "get", context, "/v2/api/member.connect/third", params, callBack)
    }

    /**
     * 绑定第三方账号
     */
    fun bindThird(context: Context, params: HttpParams, callBack: TokenCallBack) {
        params.put("device_token", MainApplication.pushAgent.registrationId)
        params.put("reg_at", "android")
        getSecretRequest(false, "post", context, ApiAddress.BIND_THIRD_UN, params, callBack)
    }

    fun loginByOpenId(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.LOGIN_BY_OPEN_ID, params, callBack)
    }

    /**
     * 获取池塘首页，带图片的热门标签
     */
    fun getPondHotTag(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.POND_HOT_TAG, params, callBack)
    }
    /**
     * 获取歌单的标签
     */
    fun getPlayListTag(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.PLAYLIST_LABLE, params, callBack)
    }

    /**
     * 歌单详情
     * id	是	string
     */
    fun getSongSheetDetails(cacheKey: String,context: Context, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(cacheKey, "get", context, ApiAddress.GET_SONG_SHEET_DETAILS + id, params, callBack)
    }
    fun getSongSheetDetails(context: Context, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(true, "get", context, ApiAddress.GET_SONG_SHEET_DETAILS + id, params, callBack)
    }


    /**
     * 歌单详情
     * id	是	string
     */
    fun getSongList(context: Context, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("id", id + "")
        getSecretRequest(true, "get", context, ApiAddress.SONG_PLAYS, params, callBack)
    }

    /**
     * 音乐人详情
     * id	是	string
     */
    fun getMusicianDetails(context: Context, id: String, row: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("row", row)
        getSecretRequest(false, "get", context, ApiAddress.GET_MUSICIAN_DETAILS + id, params, callBack)
    }

    /**
     * 音乐人发布的音乐
     * id	是	string
     */
    fun getMusicianMusic(context: Context, page: Int, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        params.put("row", 100)
        params.put("order", "counts-desc")
        Log.e(TAG, "getMusicianMusic: -------$params")
        getSecretRequest(false, "get", context, ApiAddress.GET_MUSICIAN_DETAILS + id + "/release", params, callBack)
    }

    /**
     * 音乐人歌单
     * id	是	string
     */
    fun getMusicianSong(context: Context, page: Int, row: Int, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        if (row != 0) {
            params.put("row", row.toString())
        }
        getSecretRequest(false, "get", context, "/v2/api/musician/$id/song", params, callBack)
    }

    /**
     * 音乐人歌单
     * id	是	string
     */
    fun getMusicianTopic(context: Context, page: Int, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        params.put("row", 15)
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MUSICIAN_DETAILS + id + "/topic", params, callBack)
    }

    /**
     * 音乐人歌单
     * id	是	string
     */
    fun getMusicianSongCollection(context: Context, page: Int, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        getSecretRequest(false, "get", context, ApiAddress.GET_MUSICIAN_DETAILS + id + "/collection", params, callBack)
    }

    /**
     * 音乐人音乐列表
     * id	是	string
     */
    fun getMusicianMusicCollection(context: Context, page: Int, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MUSICIAN_DETAILS + id + "/collect", params, callBack)
    }

    /**
     * 我的关注
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认10
     */
    fun getMemberAttention(context: Context, page: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MEMBER_ATTENTION, params, callBack)
    }

    /**
     * 我的关注
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认10
     */
    fun getMemberFans(context: Context, page: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MEMBER_FANS, params, callBack)
    }

    /**
     * 我的甜甜圈
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认10
     */
    fun getMemberCoin(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.GET_MEMBER_COIN, params, callBack)
    }

    /**
     * 限时优惠
     */
    fun getTimeLimit(context: Context,callBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(false, "get", context, ApiAddress.GET_TIMELIMIT, params, callBack)
    }


    /**
     * 我的收藏
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认10
     */
    fun getMemberCollection(context: Context, page: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MEMBER_COLLECTION, params, callBack)
    }

    /**
     * 删除收藏
     * id	是	int	音乐id,多条记录用逗号分隔
     */
    fun getMemberDelCollect(context: Context, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("id", id)
        getSecretRequest(false, "post", context, ApiAddress.POST_MEMBER_DELCOLLECT, params, callBack)
    }

    /**
     * 我的歌单
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认10
     */
    fun getMemberSong(context: Context, page: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MEMBER_SONG, params, callBack)
    }

    /**
     * 删除歌单
     * id	是	string	歌单id
     */
    fun delCollectionSong(context: Context, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(false, "delete", context, "/v2/api/member/song/$id", params, callBack)
    }

    /**
     * 我的歌单
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认10
     */
    fun getMemberScollection(context: Context, page: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString())
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MEMBER_SCOLLECTION, params, callBack)
    }

    /**
     * 我的池塘
     * status	是	int	未上线0/1审核中/2已审核/3草稿箱
     */
    fun getMemberPond(context: Context, status: String, p: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        if (!TextUtils.isEmpty(status)) {
            params.put("status", status)
        }
        params.put("p", p)
        getSecretRequest(false, "get", context, ApiAddress.GET_MEMBER_POND, params, callBack)
    }


    /**
     * 删除池塘
     * status	是	int	未上线0/1审核中/2已审核/3草稿箱
     */
    fun getMemberDeletePond(context: Context, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(false, "delete", context, "/v2/api/member/pond/$id", params, callBack)
    }

    /**
     * 手机app下载
     * versions	是	int	版本号
     * type	是	int	类型：1 android 2 ios
     */
    fun getVersions(context: Context, versions: String, callBack: TokenCallBack) {


        val params = HttpParams()
        params.put("versions", versions)
        params.put("type", 1)
        getSecretRequest(false, "get", context, ApiAddress.GET_VERSION, params, callBack)
    }

    /**
     * 获取举报类型
     * id	是	int	类型:1音乐，2歌单，3圈子，4话题or话题一级评论，5评论，8举报反馈，29话题二级评论
     */
    fun getFeedback(context: Context, id: String, callBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(false, "get", context, ApiAddress.GET_FEEDBACK + "/" + id, params, callBack)
    }

    /**
     * mv播放次数
     * id	音乐id
     */
    fun getMvnumber(context: Context, id: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("id", id)
        getSecretRequest(false, "get", context, ApiAddress.MV_PLAYER_MV, params, callBack)
    }



    /**
     * 没有登录上传文件搭配本地
     * file	文件流
     */
    fun localupload(context: Context, file: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("file", file)
        getSecretRequest(false, "post", context, ApiAddress.LOCA_UPLOAD, params, callBack)
    }

    /**
     * 举报提交
     * item_id	是	string	举报选项id
     * title	否	string	意见反馈专用（电话或邮箱 ）
     * content	是	string	内容
     * filelist	否	string	附件多个用逗号分隔
     * qq	是	string	QQ号
     * pid	是	举报的池塘、音乐、歌单的id
     */
    fun postFeedback(context: Context, item_id: String, title: String, content: String, filelist: String, qq: String, pid: Int, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("item_id", item_id)
        if (!TextUtils.isEmpty(title)) {
            params.put("title", title)
        }
        params.put("content", content)
        if (!TextUtils.isEmpty(filelist)) {
            params.put("filelist", filelist)
        }
        if (!TextUtils.isEmpty(qq)) {
            params.put("qq", qq)
        }
        params.put("pid", pid)
        getSecretRequest(false, "post", context, ApiAddress.GET_FEEDBACK, params, callBack)
    }

    fun suggest(context: Context, params: HttpParams, callBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/feedback/add", params, callBack)
    }

    /**
     * 点赞
     * 1代表音乐,2歌单，3圈子评论，4,评论，5，池塘， 6代表池塘评论
     */
    fun agree(mContext: Context, params: HttpParams, callBack: TokenCallBack) {
        params.put("device_token", MainApplication.pushAgent.registrationId)
        params.put("device_finger", MainApplication.pushAgent.registrationId)
        getSecretRequest(false, "post", mContext, ApiAddress.AGREE, params, callBack)
    }

    /**
     * 关注
     */
    fun follow(mContext: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", mContext, ApiAddress.FOLLOW, params, callBack)
    }

    /**
     * 发动态
     */
    fun publishDynamic(mContext: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", mContext, "/v2/api/member.pond/share", params, callBack)
    }

    /**
     * 删除动态
     */
    fun deleDynamic(c: Context, id: Int, callBack: TokenCallBack) {
        getSecretRequest(false, "delete", c, "/v2/api/circle/$id", null, callBack)
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(context: Context, params: HttpParams?, url: String, callBack: TokenCallBack) {
        getSecretRequest(false, "get", context, url, params, callBack)
    }

    /**
     * 解绑
     */
    fun unBindThird(c: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", c, "/v2/api/member/unthird", params, callBack)
    }

    /**
     * 手机绑定验证
     */
    fun bindPhoneAuth(c: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", c, "/v2/api/member/find", params, callBack)
    }

    /**
     * 绑定新账号
     */
    fun bindNewMobile(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member/bind", params, callBack)
    }

    fun changePwd(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member/pwd", params, callBack)
    }

    /**
     * 获取音乐分类
     */

    fun getMusicType(c: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "get", c, ApiAddress.MUSIC_CATE, params, callBack)
    }

    /**
     * 发布音乐
     */
    fun releaseMusic(c: Context, musicId: Int, params: HttpParams?, callBack: TokenCallBack) {
        if (musicId != 0) {
            getSecretRequest(false, "put", c, ApiAddress.PUBLISH_MUSIC + "/" + musicId, params, callBack)
        } else {
            getSecretRequest(false, "post", c, ApiAddress.PUBLISH_MUSIC, params, callBack)
        }
    }
    /**
     * 获取作品类型
     */
    fun worksType(c: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "get", c, ApiAddress.WORKS_TYPE, params, callBack)
    }

    fun RealInfoAuth(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.REAL_INFO_AUTH, params, callBack)
    }

    /**
     * 回复池塘
     */
    fun replyTopic(c: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", c, "/v2/api/pond/reply", params, tokenCallBack)
    }

    /**
     * 池塘评论的子评论列表
     */
    fun getPondReplySon(c: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {

        getSecretRequest(false, "get", c, "/v2/api/topcom", params, tokenCallBack)
    }

    fun deleComment(c: Context, id: Int, tokenCallBack: TokenCallBack) {
        Log.e(TAG, "deleComment: $id")
        getSecretRequest(false, "delete", c, "/v2/api/comment/$id", null, tokenCallBack)
    }

    fun deleTopicComment(c: Context, id: Int, tokenCallBack: TokenCallBack) {
        Log.e(TAG, "deleComment: $id")
        getSecretRequest(false, "delete", c, "/v2/api/pond/$id", null, tokenCallBack)
    }

    fun deleTopicSonComment(c: Context, id: Int, tokenCallBack: TokenCallBack) {
        Log.e(TAG, "deleComment: $id")
        getSecretRequest(false, "delete", c, "/v2/api/topcom/$id", null, tokenCallBack)
    }

    fun delePond(context: Context, id: Int, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "delete", context, "/v2/api/member/pond/$id", null, tokenCallBack)
    }

    /**
     * 回复池塘评论
     */
    fun addPondCommentReply(c: Context, fid: Int, pid: Int, s: String, tokenCallBack: TokenCallBack) {
        val params = HttpParams()
        if (fid != 0) {
            params.put("fid", fid.toString() + "")
        }
        if (pid != 0) {
            params.put("pid", pid.toString() + "")
        }
        params.put("content", s + "")
        Log.e(TAG, "addPondCommentReply: $params")
        getSecretRequest(false, "post", c, "/v2/api/topcom", params, tokenCallBack)
    }

    fun pondVote(mContext: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", mContext, "/v2/api/pond/vote", params, tokenCallBack)
    }

    fun getRecommendUser(c: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", c, "/v2/api/member.member/hot", null, tokenCallBack)
    }

    fun followAll(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member/relation/followAll", params, tokenCallBack)
    }

    fun searchPopWord(context: Context, s: String, word: String, i: Int, tokenCallBack: TokenCallBack) {
        val pa = HttpParams()
        pa.put("keyword", word + "")
        getSecretRequest(false, "get", context, "/v2/api/search/keyword", pa, tokenCallBack)
    }

    fun changePwdAuth(c: Context, params2: HttpParams, tokenCallBack1: TokenCallBack) {
        getSecretRequest(false, "post", c, "/v2/api/member/user/pwd", params2, tokenCallBack1)
    }

    fun sayHi(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member/hello", params, tokenCallBack)
    }

    /**
     * 用户协议
     */
    fun getUserNotice(context: Context, code: String, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/com/document", HttpParams("code", code), tokenCallBack)
    }

    fun addSelfInfo(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member.member/profile", params, tokenCallBack)
    }

    fun bindThirdCout(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member/thirdbind", params, tokenCallBack)
    }

    fun authStatus(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/member/certification", null, tokenCallBack)
    }

    /**
     * 后台新加的接口，审核失败，点击再次提交时请求，会把后台的状态改了。骚操作。。。。。。
     */
    fun changeAuthStatus(context: Context, tokenCallBack: TokenCallBack) {

        getSecretRequest(false, "get", context, "/v2/api/member.certification/status", null, tokenCallBack)
    }

    ///v2/api/musicdraft
    fun getMyReleaseDetial(context: Context, id: Int, tokenCallBack: TokenCallBack) {
        getSecretRequest(true, "get", context, "/v2/api/member/musicdraft/$id", null, tokenCallBack)
    }

    fun authIncode(context: Context, myCode: String, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/user/incode", HttpParams("mycode", myCode), tokenCallBack)
    }

    /**
     * 随机礼物弹框的信息
     */
    fun getGiftDialogInfo(context: Context, dialog_url: String, tokenCallBack: TokenCallBack) {
        val replace = dialog_url.replace(ApiAddress.BASE_URL, "")
        getSecretRequest(false, "post", context, replace, null, tokenCallBack)
    }

    /**
     * 判断是否已经上传文件
     */
    fun isUploaded(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member.checkfile/app", params, tokenCallBack)
    }

    fun getStsToken(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/member.checkfile/gettoken", null, tokenCallBack)
    }

    fun outLogin(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/user/logout", null, tokenCallBack)

    }

    fun pushCount(context: Context, msg_id: String, tokenCallBack: TokenCallBack) {


        getSecretRequest(false, "get", context, "/v2/api/push?msg_id=$msg_id", null, tokenCallBack)
    }

    fun getMsgList(context: Context, page: Int, type: Int, tokenCallBack: TokenCallBack) {
        val params = HttpParams()
        params.put("type", type.toString() + "")
        params.put("p", page.toString() + "")
        if (!CacheUtils.getBoolean(context, Constants.User.IS_LOGIN, false)) {
            params.put("create_time", CacheUtils.getLong(context, Constants.User.FIRST_TIME, 0L))
        }
        Log.e(TAG, "获取消息列表-----: $params")
        getSecretRequest(false, "get", context, "/v2/api/msg", params, tokenCallBack)
    }

    fun getUnreadMsgList(type: Int, private_msg_ids: Int, create_time: Int, context: Context, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("type", type.toString())
        params.put("private_msg_ids", private_msg_ids.toString())
        if (!CacheUtils.getBoolean(context, Constants.User.IS_LOGIN, false)) {
            params.put("create_time", CacheUtils.getLong(context, Constants.User.FIRST_TIME, 0L))
        }
        Log.e(TAG, "获取唯独消息列表-----: $params")
        getSecretRequest(false, "get", context, "/v2/api/msg", params, callBack)
    }

    fun getActivity(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.GET_ACTIVITY, null, tokenCallBack)
    }

    fun getHelpList(context: Context, page: Int, row: Int, tokenCallBack: TokenCallBack) {
        val params = HttpParams()
        params.put("p", page.toString() + "")
        params.put("row", row.toString() + "")
        getSecretRequest(false, "get", context, ApiAddress.GET_HELP_LIST, HttpParams("p", page.toString() + ""), tokenCallBack)
    }

//    问题分类列表
    fun getHelpProblemList(context: Context, tokenCallBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(false, "get", context, ApiAddress.GET_HELP_PROBLEMLIST,null, tokenCallBack)
    }
    //    获取分类下的帮助列表
    fun getHelpProblemListDetails(context: Context, class_id: String, tokenCallBack: TokenCallBack) {
        val params = HttpParams()
        params.put("class_id", class_id)
        getSecretRequest(false, "get", context, ApiAddress.GET_HELP_PROBLEMLISTDETAILS,params, tokenCallBack)
    }
    //    获取分类下的帮助列表的详情页面
    fun getHelpProblemListDetailsDetails(context: Context, class_id: String, tokenCallBack: TokenCallBack) {
        val params = HttpParams()
        params.put("id", class_id)
        getSecretRequest(false, "get", context, ApiAddress.GET_HELP_PROBLEMLISTDETAILSDETAILS,params, tokenCallBack)
    }

    fun getChartsHomeList(context: Context, page: Int, tokenCallBack: TokenCallBack) {
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_CHARTS_HOME_LIST, HttpParams("p", page.toString() + ""), tokenCallBack)
    }
    //新改动的排行榜
    fun getChartsNewHomeList(context: Context, page: Int, tokenCallBack: TokenCallBack) {
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_CHARTSNEW_HOME_LIST, HttpParams("p", page.toString() + ""), tokenCallBack)
    }

    fun getrewardinfo(mContext: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", mContext, ApiAddress.GET_REWARD_INFO, null, tokenCallBack)

    }

    fun goReward(mContext: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", mContext, ApiAddress.GO_REWARD, params, tokenCallBack)
    }

    fun getHelpMsg(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.GET_HELP_MSG, null, tokenCallBack)

    }

    fun searchHelp(context: Context, s: String, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.SEARCH_HELP, HttpParams("keyword", s), tokenCallBack)
    }

    fun getCodeNoLogin(params: HttpParams?, context: Context, tokenCallBack: TokenCallBack) {
        Log.e(TAG, "getCodeNoLogin: $params")
        getSecretRequest(false, "get", context, "/v2/api/send_code", params, tokenCallBack)
    }

    fun checklogin(context: Context, paramsAuth: HttpParams, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/user/checklogin", paramsAuth, tokenCallBack)
    }

    //关于我们
    fun aboutContent(context: Context, paramsAuth: HttpParams, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/com/get_about", paramsAuth, tokenCallBack)
    }


    fun getDegree(context: Context, alias: String, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.GET_DEGREE, HttpParams("alias", alias), tokenCallBack)
    }

    fun askRobot(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.GET_ROBOT_ANSWER, params, tokenCallBack)
    }

    fun noMoile_GoBind(params: HttpParams?, context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.NO_MOBILE_GO_BIND, params, tokenCallBack)
    }

    fun applyDownLine(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.APPLY_DOWN_LINE, params, tokenCallBack)
    }

    //我的作品删除
    fun deleRelease(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.DEL_REPLEASE, params, tokenCallBack)

    }

    //我的作品撤回申请
    fun backRelease(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.Back_REPLEASE, params, tokenCallBack)

    }

    fun readAllMsg(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/msg/allshow", params, tokenCallBack)

    }

    fun getGiftList(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.GIFT_LIST, null, tokenCallBack)
    }

    fun giveGift(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.GIVE_GIFT, params, tokenCallBack)
    }

    fun checkGift(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.CHECK_GIFT, params, tokenCallBack)
    }

    fun getChargeItem(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.CHECK_ITEM, params, tokenCallBack)

    }

    fun creatOrder(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.CREATE_ORDER, params, tokenCallBack)
    }

    fun getOrderInfo(context: Context, payUrl: String, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, payUrl, null, tokenCallBack)
    }

    fun getMusiGiftList(mContext: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        Log.e(TAG, "getMusiGiftList: $params")
        getSecretRequest(false, "get", mContext, ApiAddress.GET_MUSIC_GIFT_LIST, params, tokenCallBack)
    }

    fun getMemberGiftList(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/member/get_gift", params, tokenCallBack)
    }

    fun getMyIncomeHome(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member.fish/index", null, tokenCallBack)

    }

    fun getIncomehistoryList(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/member.fish/fish_log", params, tokenCallBack)
    }

    fun exchange(context: Context, num: Float, tokenCallBack: TokenCallBack) {
        val params = HttpParams("fish_num", num.toString() + "")
        getSecretRequest(false, "post", context, "/v2/api/member.fish/exchange_coin", params, tokenCallBack)
    }

    fun getBankList(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/com/bank", null, tokenCallBack)
    }

    fun applyCash(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member.fish/apply_cash", params, tokenCallBack)
    }

    fun addCard(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member.fish/add_bank", params, tokenCallBack)
    }

    fun getCashHistory(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/member.fish/cash_log", params, tokenCallBack)
    }

    fun cancelCash(activity: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", activity, "/v2/api/member.fish/cancel_apply", params, tokenCallBack)
    }

    fun getPartnerList(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {

        getSecretRequest(false, "get", context, "/v2/api/member.musicdraft/buddy", params, tokenCallBack)

    }

    fun searchPartner(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/member.musicdraft/nickname", params, tokenCallBack)

    }

    fun check_msg(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        Log.e(TAG, "check_msg:--- ")
        getSecretRequest(false, "post", context, "/v2/api/member.easemob/check_msg", params, tokenCallBack)
    }

    fun delFromSongSheet(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        Log.e(TAG, "delFromSongSheet:---- $params")
        getSecretRequest(false, "get", context, "/v2/api/member/song/del", params, tokenCallBack)
    }

    interface TokenCallBack {
        fun doNext(resultData: String, headers: Headers?)

        fun doError(msg: String)

        fun doResult()
    }

    /**
     * 动态详情
     */
    fun getDynamicDetial(context: Context, url: String, callBack: TokenCallBack) {
        getSecretRequest(true, "get", context, url, null, callBack)
    }

    /**
     * 获取评论列表  评论类型 1:音乐 2:歌单 3:圈子 4:评论 5:池塘
     * order  热门评论agrees desc
     * fid 主评论id
     */
    fun getCommentList(needCache: Boolean, c: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(needCache, "get", c, "/v2/api/comment", params, callBack)
    }
    fun getCommentListList(needCache: Boolean, c: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(needCache, "get", c, "v2/api/comment?", params, callBack)
    }

    //mv音乐详情
    fun mvdetails(c: Context, item_id: Int,callBack: TokenCallBack) {
        val params = HttpParams()
        getSecretRequest(false, "get", c, "/v2/api/music/"+item_id, params, callBack)
    }


    fun addMvComment(c: Context, item_id: Int, type: Int, content: String, callBack: TokenCallBack) {
        val params = HttpParams()
        params.put("item_id",item_id)
        params.put("type",type)
        params.put("content",content)
        getSecretRequest(false, "post", c, ApiAddress.ADD_MVCOMMENT, params, callBack)
    }

    /**
     * 添加评论
     * 评论类型 1:音乐 2:歌单 3:圈子 4:评论 5:池塘
     */
    fun addComment(c: Context, commentType: Int, Id: Int, content: String, pid: Int, fid: Int, callBack: NetWork.TokenCallBack) {
        val params = HttpParams()

        if(commentType==8){
            params.put("content", content)
            params.put("pid", Id.toString() + "")
            getSecretRequest(false, "post", c, "/v2/api/member.comment/reply" + "", params, callBack)
        }else{
            if (commentType != 0 && commentType != 5) {
                params.put("type", commentType.toString() + "")
            }
            if (Id != 0) {
                params.put("item_id", Id.toString() + "") //getArguments().getInt("id")+"";
            }
            params.put("content", content)
            if (pid != 0) {
                params.put("pid", pid.toString() + "")
            }
            if (fid != 0) {
                params.put("fid", fid.toString() + "")
            }
            if (commentType != 0) {
                getSecretRequest(false, "post", c, ApiAddress.ADD_COMMENT, params, callBack)
            } else {
                getSecretRequest(false, "post", c, "/v2/api/member.comment/reply" + "", params, callBack)
            }
        }


    }

    /**
     * 添加评论
     * 评论类型 1:音乐 2:歌单 3:圈子 4:评论 5:池塘
     */


    /**
     * 获取表情
     * /v2/api/com/face
     */

    fun getFace(c: Context, params: HttpParams??, callBack: TokenCallBack) {
        getSecretRequest(false, "get", c, ApiAddress.GET_FACE, params, callBack)
    }

    fun SearchMusic(c: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "get", c, ApiAddress.SEARCH, params, callBack)
    }

    /**
     * 发布池塘
     */
    fun publishTopic(c: Context, ststus: Int, pondId: Int, params: HttpParams?, callBack: TokenCallBack) {
        Log.e(TAG, "publishTopic:=== $ststus")
        if (ststus == 3) {//草稿
            getSecretRequest(false, "put", c, "/v2/api/member/pond/$pondId", params, callBack)
        } else {
            getSecretRequest(false, "post", c, ApiAddress.PUBLISH_TOPIC, params, callBack)
        }
    }

    fun getAllPondComment(needCache: Boolean, c: Context, pid: Int, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(needCache, "get", c, ApiAddress.ALL_POND_COMMENT + pid + "/comments", params, callBack)
    }

    fun getPondDetialInfo(c: Context, pid: Int, callBack: TokenCallBack) {
        Log.e(TAG, "/v2/api/pond/$pid")
        getSecretRequest(true, "get", c, "/v2/api/pond/$pid", HttpParams("id", pid.toString() + ""), callBack)
    }

    fun getPondReplyDetialInfo(c: Context, fid: Int, callBack: TokenCallBack) {
        Log.e(TAG, "/v2/api/pond/getcomment$fid")
        getSecretRequest(true, "get", c, "/v2/api/pond/getcomment", HttpParams("fid", fid.toString() + ""), callBack)
    }

    fun getHotSearchWords(c: Context, callBack: TokenCallBack) {
        getSecretRequest(false, "get", c, "/v2/api/search/lists", null, callBack)
    }

    /**
     * 搜索
     */
    fun search(c: Context, type: String, s_title: String?, page: Int, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("p", page.toString() + "")
        param.put("type", type + "")
        if (type == "2") {//搜索用户
            param.put("order", "dynamic_time-desc")
        }
        if (s_title != null) {
            param.put("keyword", s_title)
        }
        getSecretRequest(false, "get", c, "/v2/api/search", param, callBack)
    }

    /**
     * item_type	是	int	消息类型 1：CommentsListActivity 2：动态消息 3：评论消息
     */
    fun getMessage(c: Context, item_type: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("item_type", item_type)
        getSecretRequest(false, "get", c, ApiAddress.GET_MESSAGE, param, callBack)
    }

    /**
     * pid	是	int	城市pid
     * type	是	int	0分级读取，1全部读取
     */
    fun getCity(c: Context, type: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("type", type)
        getSecretRequest(false, "get", c, ApiAddress.GET_CITY, param, callBack)
    }

    /**
     * title	是	string	歌单名
     * imgpic	否	string	歌单图片
     * imgpic_type	否	int	1为会员上传封面 ,0系统给封面
     * remark	否	string	简介
     * tag	是	json	标签例子：[{“id”:0,”title”:”\u6d4b\u8bd5\u4e00”},{“id”:0,”title”:”\u6d4b\u8bd5\u4e8c”},{“id”:0,”title”:”\u6d4b\u8bd5\u4e8c”}]
     * music	是	string	歌单音乐例子：3,4,5
     */
    fun getEditSong(c: Context, item_id: String, title: String, imgpic: String, imgpic_type: Int, remark: String, tag: String, music: String, edit: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("title", title)
        param.put("imgpic", imgpic)
        param.put("imgpic_type", imgpic_type)
        param.put("remark", remark)
        param.put("tag", tag)
        param.put("music", music)
        param.put("edit", edit)
        getSecretRequest(false, "put", c, "/v2/api/member/song/$item_id", param, callBack)
    }
    fun getEditSong(c: Context, item_id: String, title: String, imgpic: String, imgpic_type: Int, remark: String, tag: String, music: String, edit: String, is_private: String,callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("title", title)
        param.put("imgpic", imgpic)
        param.put("imgpic_type", imgpic_type)
        param.put("remark", remark)
        param.put("tag", tag)
        param.put("music", music)
        param.put("edit", edit)
        param.put("is_private", is_private)
        getSecretRequest(false, "put", c, "/v2/api/member/song/$item_id", param, callBack)
    }

    /**
     * 新建歌单
     * title	是	string	歌单名称
     * remark	否	string	歌单介绍
     * item_id	否	int	音乐id
     */
    fun getNewSong(c: Context, title: String, remark: String, music_id: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("title", title)
        param.put("remark", remark)
        param.put("item_id", music_id)
        getSecretRequest(false, "get", c, ApiAddress.POST_NEW_SONG, param, callBack)
    }

    /**
     * 新建歌单
     * title	是	string	歌单名称
     * remark	否	string	歌单介绍
     * item_id	否	int	音乐id
     */
    fun getNewSong(c: Context, title: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("title", title)
        getSecretRequest(false, "post", c, ApiAddress.POST_NEW_SONG, param, callBack)
    }


    fun getNewSongList(c: Context, title: String, is_private: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("title", title)
        param.put("is_private", is_private)
        getSecretRequest(false, "post", c, ApiAddress.POST_NEWList_SONG, param, callBack)
    }
    fun getNewPlaylist(c: Context, music_id: String,title: String, is_private: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("music", music_id)
        param.put("title", title)
        param.put("is_private", is_private)
        getSecretRequest(false, "post", c, ApiAddress.POST_NEWList_SONG, param, callBack)
    }
    /**
     * 新建歌单
     * title	是	string	歌单名称
     * item_id	否	int	音乐id
     */
    fun gethomeNewSong(c: Context, title: String, item_id: String,is_private: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("title", title)
        param.put("item_id", item_id)
        param.put("is_private", is_private)
        getSecretRequest(false, "post", c, ApiAddress.POST_NEW_SONG, param, callBack)
    }

    fun getNewSong(c: Context, title: String, item_id: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("title", title)
        param.put("item_id", item_id)
        getSecretRequest(false, "post", c, ApiAddress.POST_NEW_SONG, param, callBack)
    }
    /**
     * song_id	是	int	歌单id
     * music_id	是	int	音乐id多个ID用,逗号分隔
     */
    fun historicalrecordpostAddSong(c: Context, song_id: String, music_id: String, isMultiSelect: Boolean, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("song_id", song_id)
        param.put("music_id", music_id)
        var url = ""
        Log.e(TAG, "添加歌单:--- $param")

        val buffer2 = StringBuffer()
        buffer2.append(music_id)
        Log.e("nnnn", "选中的musicis长度" + buffer2.length)
        if (buffer2.length > 4) {

        }

        if (isMultiSelect&&buffer2.length > 4) {
            url = ApiAddress.POST_S_ADD_SONG
        } else {
            url = ApiAddress.POST_ADD_SONG
        }
//        if (isMultiSelect) {
//            url = ApiAddress.POST_ADD_SONG
//        } else {
//            url = ApiAddress.POST_S_ADD_SONG
//        }
        getSecretRequest(false, "post", c, url, param, callBack)
    }
    fun postAddSong(c: Context, song_id: String, music_id: String, isMultiSelect: Boolean, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("song_id", song_id)
        param.put("music_id", music_id)
        var url = ""
        Log.e(TAG, "添加歌单:--- $param")

        val buffer2 = StringBuffer()
        buffer2.append(music_id)
        Log.e("nnnn", "选中的musicis长度" + buffer2.length)
        if (buffer2.length > 4) {

        }

        if (isMultiSelect) {
            url = ApiAddress.POST_ADD_SONG
        } else {
            url = ApiAddress.POST_S_ADD_SONG
        }
        getSecretRequest(false, "post", c, url, param, callBack)
    }
    /**
     * id	是	int	音乐id
     * type	否	int	下载音质 默认1， 0：源文件 1：高清音质 2：普通音质
     */
    fun postDownLoadFile(c: Context, music_id: String, type: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("id", music_id)
        param.put("type", type)
        getSecretRequest(false, "post", c, ApiAddress.POST_DOWN_KEY, param, callBack)
    }

    /**
     * type	是	int	点赞类型 1代表音乐,2歌单，3圈子评论，4,评论，5，池塘， 6代表池塘评论
     * item_id	是	int	点赞类型对应的id
     */
    fun postAgreeAdd(c: Context, type: String, item_id: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("type", type)
        param.put("item_id", item_id)
        param.put("device_token", MainApplication.pushAgent.registrationId)
        param.put("device_finger", MainApplication.pushAgent.registrationId)
        Log.e(TAG, "点赞参数==: $param")
        getSecretRequest(false, "post", c, ApiAddress.POST_AGREE_ADD, param, callBack)
    }

    /**
     * 判断音乐是否可下载
     */
    fun getDownBit(c: Context, music_id: String, callBack: TokenCallBack) {
        val param = HttpParams()
        getSecretRequest(false, "get", c, ApiAddress.GET_DOWN_BIT + music_id, param, callBack)
    }

    /**
     * id	是	int	音乐id
     * duration	是	int	音乐时长（秒）
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认20
     */
    fun getMusicRelated(c: Context, id: Int, duration: Int, p: Int, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("id", id.toString())
        param.put("duration", duration.toString())
        param.put("p", p.toString())
        getSecretRequest(false, "get", c, ApiAddress.GET_MUSIC_RELATED, param, callBack)
    }

    /**
     * 相关歌单-包含该歌曲的歌单
     * id	是	int	音乐id
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认20
     */
    fun getMusicSong(c: Context, id: String, p: Int, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("id", id)
        param.put("p", p.toString())
        getSecretRequest(p == 1, "get", c, ApiAddress.GET_MUSIC_SONG, param, callBack)
    }

    /**
     * 公开歌单
     */
    fun publicSongList(c: Context, id: String,callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("id", id)
        getSecretRequest(false, "post", c, ApiAddress.PUBLIC_SONG_LIST, param, callBack)
    }

    /**
     * 收藏歌曲
     */
    fun collectMusic(context: Context, id: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("id", id)
        getSecretRequest(false, "post", context, ApiAddress.GET_MUSIC_COLLECT, param, callBack)
    }

    /**
     * 关注|取消关注接口
     * id	是	int	关注or取消关注用户的id
     */
    fun getRelationFollow(context: Context, id: String, callBack: TokenCallBack) {


        val param = HttpParams()
        param.put("id", id)
        getSecretRequest(false, "get", context, ApiAddress.GET_RELATION_FOLLOW, param, callBack)
    }


    /**
     * 收藏|取消收藏歌单
     * id	是	int	歌单id
     */
    fun getSongCollect(context: Context, id: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("id", id)
        getSecretRequest(false, "post", context, ApiAddress.GET_SONG_COLLECT, param, callBack)
    }

    /**
     * 我的发布列表
     */
    fun getMyRelease(context: Context, id: String, page: Int, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("p", page)
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MUSICIAN_DETAILS + id + "/release", param, callBack)
    }

    /**
     * 我的音乐列表
     * admin_status	否	int	全部（无参数） 1代表审核中 2代表审核通过 -1代表审核失败 0代表下线 4代表草稿
     * p	否	int	页数，默认1
     * row	否	int	每页条数，默认20
     * order	否	int	id-desc默认counts-desc播放最多 create_time-desc刚刚发布 update_time-desc更新时间,rand()-desc随机显示
     */
    fun getMusicdraft(context: Context, admin_status: String, page: Int, order: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("admin_status", admin_status)
        param.put("p", page)
        param.put("order", order)
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_MUSICDRAFT, param, callBack)
    }

    /**
     * 会员修改
     * sex	是	int	性别
     * day	是	int	生日
     * province	是	int	省
     * city	是	int	市
     * qq	否	string	qq号
     * signature	是	string	个性签名
     * nickname	否	string	昵称
     * head	否	string	头像
     * identity_tag 身份标签
     */
    fun getMember(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.GET_MEMBER, params, callBack)

    }

    /**
     * 收藏音乐歌单详情
     */
    fun getMusicianDetailsCollection(context: Context, id: String, callBack: TokenCallBack) {
        val param = HttpParams()
        getSecretRequest(false, "get", context, ApiAddress.GET_MUSICIAN_DETAILS + id + "/box", param, callBack)
    }


    /**
     * 搜索
     */
    fun getSearchRecommend(context: Context, endTime: Long?, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("update_time", endTime!!)
        getSecretRequest(false, "get", context, ApiAddress.SEARCH_RECOMMEND_LISTS, param, callBack)
    }

    /**
     * 音乐人信息
     * uid	是	int	音乐人id
     */
    fun getMusicIanInfo(context: Context, uid: String, callBack: TokenCallBack) {


        val param = HttpParams()
        param.put("uid", uid)
        getSecretRequest(false, "get", context, ApiAddress.MUSICIAN_INFO, param, callBack)
    }

    /**
     * 音乐人信息
     * uid	是	int	音乐人id
     */
    fun getMusicIanInfo(cacheKey: String,context: Context, uid: String, endTime: Long?, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("uid", uid)
        param.put("update_time", endTime!!)
        Log.d(TAG, "getMusicIanInfo: -------->" + endTime!!)
        getSecretRequest(cacheKey, "get", context, ApiAddress.MUSICIAN_INFO, param, callBack)
    }

    /**
     * 我的音乐数量统计
     * uid	是	int	音乐人id
     */
    fun getMusicIanInfo(context: Context, callBack: TokenCallBack) {
        val param = HttpParams()
        getSecretRequest(false, "get", context, ApiAddress.MUSICDRAFT_COUNT, param, callBack)
    }

    /**
     * 附近的人
     * x	是	double	经度
     * y	是	double	纬度
     * type	否	int	更多 1-只看异性 2-只看在线
     */
    fun getMemberNearby(context: Context, lat: Double?, longitude: Double?, page: Int, type: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("x", longitude!!)
        param.put("y", lat!!)
        param.put("p", page)
        if (!TextUtils.isEmpty(type)) {
            param.put("type", type)
        }
        getSecretRequest(false, "get", context, ApiAddress.MEMBER_NEARBY, param, callBack)
    }

    /**
     * 音樂投幣
     */
    fun getMusicCoin(context: Context, id: String, callBack: TokenCallBack) {


        val param = HttpParams()
        param.put("id", id)
        getSecretRequest(false, "get", context, ApiAddress.MUSIC_COIN, param, callBack)
    }

    /**
     * 播放历史
     * int p,
     */
    fun getMusicHistory(context: Context, row: Int, callBack: TokenCallBack) {
        val param = HttpParams()
        //        param.put("p", p);
        param.put("row", row)
        getSecretRequest(false, "get", context, ApiAddress.MUSIC_HISTORY, param, callBack)
    }

    /**
     * 删除播放历史
     */
    fun getDelHistory(context: Context, id: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("id", id)
        getSecretRequest(false, "delete", context, ApiAddress.MUSIC_DEL_HISTORY, param, callBack)
    }

    /**
     * 找回密码
     */

    fun authChgPwd(context: Context, params: HttpParams?, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/user/find", params, tokenCallBack)
    }

    /**
     * id	是	int	音乐id
     * type	是	int	下载音质，默认1低清，2高清
     */
    fun getMusicDown(context: Context, id: String, type: String, callBack: TokenCallBack) {
        val param = HttpParams()

        param.put("id", id)
        param.put("type", type)
        getSecretRequest(false, "get", context, ApiAddress.MUSIC_DOWN, param, callBack)
    }

    /**
     * id	是	int	站内信id
     * type	是	int	类型 2：评论消息 1：CommentsListActivityor动态消息
     */
    fun getMessageShow(context: Context, id: String, type: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("type", type)
        getSecretRequest(false, "get", context, ApiAddress.MESSAGE_CLICKSHOW + id, param, callBack)
    }

    /**
     * is_hello	是	int	0关闭，1打开
     * hello	是	string	打招呼内容，最多255字符
     * disturb	是	int	0关闭，1打开 夜间免打扰
     * concern	是	int	0关闭，1打开 未关注消息是否放入消息盒子
     */
    fun getMessageHello(context: Context, is_hello: Int, hello: String, callBack: TokenCallBack) {
        val param = HttpParams()
        param.put("is_hello", is_hello)
        param.put("hello", hello)
        getSecretRequest(false, "post", context, ApiAddress.MSG_HELLO, param, callBack)
    }

    /**
     * song_id	否	int	歌单id 默认null
     * up_time	否	int	歌曲更新时间 默认null
     */
    fun getStartImg(context: Context, callBack: TokenCallBack) {
        getSecretRequest(false, "get", context, ApiAddress.START_IMG, null, callBack)
    }

    /**
     * 统计分享次数
     */
    fun shareCount(context: Context, param: HttpParams, callBack: TokenCallBack) {
        getSecretRequest(false, "post", context, ApiAddress.SHARE_COUNT, param, callBack)
    }

    fun getChartsList(context: Context, page: Int, param: HttpParams, callBack: TokenCallBack) {
        Log.e(TAG, "getChartsList: $param")
        getSecretRequest(page == 1, "get", context, ApiAddress.GET_CHARTS_ITEM_LIST, param, callBack)
    }

    fun getTodatRecommend(cacheKey: String,context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(cacheKey, "get", context, ApiAddress.TODAY_RECOMMEND, params, callBack)
    }
    fun getTodatRecommend(context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(true, "get", context, ApiAddress.TODAY_RECOMMEND, null, callBack)
    }


    /**
     * 新歌速递
     */
    fun getNewRecommend(cacheKey: String,context: Context, params: HttpParams?, callBack: TokenCallBack) {
        getSecretRequest(cacheKey, "get", context, ApiAddress.NEW_RECOMMEND, params, callBack)
    }

    fun getOrderType(context: Context, alias: String, callBack: TokenCallBack) {
        var param = HttpParams()
        param.put("alias", alias)
        getSecretRequest(false, "get", context, ApiAddress.ORDER_WHERE, param, callBack)
    }

    fun saveInteTag(context: Context, params: HttpParams, tokenCallBack: NetWork.TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api/member.tag/save_interest_tag", params, tokenCallBack)

    }

    fun saveIdenTag(context: Context, params: HttpParams, tokenCallBack: NetWork.TokenCallBack) {
        getSecretRequest(false, "post", context, "/v2/api//member.tag/save_identity_tag", params, tokenCallBack)

    }

    fun getMyGiftList(context: Context, params: HttpParams, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/member.gift", params, tokenCallBack)

    }

    fun getPondTags(context: Context, tokenCallBack: TokenCallBack) {
        getSecretRequest(false, "get", context, "/v2/api/pond/get_tag", null, tokenCallBack)
    }

}

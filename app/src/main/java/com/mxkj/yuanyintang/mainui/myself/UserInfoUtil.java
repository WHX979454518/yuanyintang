package com.mxkj.yuanyintang.mainui.myself;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import okhttp3.Headers;
public class UserInfoUtil {
    private static UserInfo userInfoBean;
    static HttpParams params = new HttpParams();
    public static void getUserInfoById(int uid, final Context context, final UserCallBack callBack) {
        String url = "/v2/api/musician/info";
        params.put("uid", uid + "");
        if (uid == 0) {//获取个人信息，登录状态下
            params = new HttpParams();
            url = "/v2/api/member.member/read";
        }
        NetWork.INSTANCE.getUserInfo(context, params, url, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                userInfoBean = JSON.parseObject(resultData, UserInfo.class);
//                CacheUtils.INSTANCE.setString(context, Constants.User.USER_ID, userInfoBean.getData().getId() + "");
                CacheUtils.INSTANCE.setString(context, Constants.User.USER_JSON, resultData);
                if (userInfoBean != null) {
                    callBack.doNext(userInfoBean);
                }
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    public static void getUserInfoByJson(String s, final UserCallBack callBack) {
        userInfoBean = null;
        String json = CacheUtils.INSTANCE.getString(MainApplication.Companion.getApplication(), Constants.User.USER_JSON);
        if (!TextUtils.isEmpty(json)&&!json.equals("null")) {
             userInfoBean = JSON.parseObject(json, UserInfo.class);

        }
        if (userInfoBean == null) return;
        callBack.doNext(userInfoBean);
    }

    public interface UserCallBack {
        void doNext(UserInfo infoBean);
    }
}

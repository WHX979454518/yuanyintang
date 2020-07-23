package com.mxkj.yuanyintang.net;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;

import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.DeleteRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.base.activity.BaseActivity;

import com.mxkj.yuanyintang.utils.app.RxDeviceTool;
import com.mxkj.yuanyintang.utils.app.UpDataApkTools;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.io.IOException;


import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtils {
    private static HttpHeaders headers;
    @NonNull
    private static Handler handler = new Handler();

    public static void request(final boolean needCache, @NonNull final String requstMethod, @NonNull final Context context, final String url, @Nullable final HttpParams params, @NonNull final DoNext doNext) {
        headers = SetRequestHeaders.getHeader(context);
        if (requstMethod.equals("get")) {
            GetRequest request = OkGo.get(url).headers(headers);
            if (params != null) {
                request.params(params);
            }
            request.execute(new AbsCallback() {
                @Override
                public void onSuccess(com.lzy.okgo.model.Response response) {
                    Response rawResponse = response.getRawResponse();
                    handleResult(params, null, rawResponse, url, context, doNext);
                }

                @Override
                public Object convertResponse(Response response) throws Throwable {
                    return response;
                }

                @Override
                public void onCacheSuccess(com.lzy.okgo.model.Response response) {
                    super.onCacheSuccess(response);
                }

                @Override
                public void onError(com.lzy.okgo.model.Response response) {
                    super.onError(response);

                    doNext.doError("你访问的资源不存在哟~~");
                }
            });
        } else if (requstMethod.equals("post")) {
            final PostRequest request = OkGo.post(url).headers(HttpUtils.headers);
            if (params != null) {
                request.params(params);
            }

            request.execute(new AbsCallback() {
                @Override
                public void onSuccess(com.lzy.okgo.model.Response response) {
                    Response rawResponse = response.getRawResponse();
                    handleResult(params, null, rawResponse, url, context, doNext);
                }

                @Override
                public Object convertResponse(Response response) throws Throwable {
                    return response;
                }
            });
        } else if (requstMethod.equals("put")) {
            PutRequest request = OkGo.put(url).headers(HttpUtils.headers);
            if (params != null) {
                request.params(params);
            }

            request.execute(new AbsCallback<Response>() {
                @Override
                public Response convertResponse(Response response) throws Throwable {
                    return response;
                }

                @Override
                public void onCacheSuccess(com.lzy.okgo.model.Response response) {
                    super.onCacheSuccess(response);
                    Response rawResponse = response.getRawResponse();
                    handleResult(params, null, rawResponse, url, context, doNext);
                }

                @Override
                public void onSuccess(com.lzy.okgo.model.Response<Response> response) {
                    Response body = response.getRawResponse();
                    handleResult(params, null, body, url, context, doNext);

                }

            });
        } else if (TextUtils.equals("delete", requstMethod)) {
            DeleteRequest request = OkGo.delete(url).headers(HttpUtils.headers);
            if (params != null) {
                request.params(params);
            }
            request.execute(new AbsCallback<Response>() {
                @Override
                public void onSuccess(com.lzy.okgo.model.Response<Response> response) {
                    Response body = response.getRawResponse();
                    handleResult(params, null, body, url, context, doNext);
                }

                @Override
                public void onCacheSuccess(com.lzy.okgo.model.Response response) {
                    super.onCacheSuccess(response);
                    Response rawResponse = response.getRawResponse();
                    handleResult(params, null, rawResponse, url, context, doNext);
                }

                @Override
                public Response convertResponse(Response response) throws Throwable {
                    return response;
                }
            });
        }
    }

    private static void handleResult(final HttpParams params, final String responseStr, final Response response, final String url, final Context context, final DoNext doNext) {
        SaveDataRunaable saveDataRunaable = new SaveDataRunaable(context, responseStr, response, url, params, doNext);
        saveDataRunaable.run();
        System.gc();
    }

    private static void onResponseError(DoNext doNext, String string, Context context) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(string);
            if (jsonObject != null) {
                String msg = jsonObject.optString("msg");
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).setSnackBar(msg, "", R.drawable.icon_fails);
                    doNext.doError(msg);
                }
            }
        } catch (org.json.JSONException e) {
            Log.e(TAG, "onResponseError: " + e);
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).setSnackBar("请求失败，请稍后再试", "", R.drawable.icon_fails);
                doNext.doError("");
            }
        }
    }

    public static void outLogin(Context context, String responseMsg) {
        CacheUtils.INSTANCE.setBoolean(context, Constants.User.IS_LOGIN, false);
        CacheUtils.INSTANCE.setString(context, Constants.User.USER_JSON, "");
        CacheUtils.INSTANCE.setString(context, Constants.User.USER_LOGIN_TOKEN, "");
    }

    public static abstract class DoNext {

        public DoNext() {
        }

        public void doNext(String responseString, Headers headers) {

        }

        public void doError(String msg) {

        }

        public abstract void doResult();
    }

    private static final String TAG = "HttpUtils";

    public static void getToken(final Context context, final DoNext doNext) {
        if (doNext != null) {
            doNext.doError("");
        }
        if (!TextUtils.isEmpty(CacheUtils.INSTANCE.getString(context, "token", ""))) {
            return;
        }
        HttpParams params = new HttpParams();
        params.put("logat", "3");
        params.put("remark", RxDeviceTool.getBuildMANUFACTURER() + "  " + RxDeviceTool.getBuildBrandModel());
        params.put("devicekey", CacheUtils.INSTANCE.getString(context, Constants.User.USER_DEVICE_TOKEN, ""));
        HttpHeaders header = SetRequestHeaders.getHeader(context);
        String registrationId = MainApplication.Companion.getPushAgent().getRegistrationId();
        String cacheDeviceToken = CacheUtils.INSTANCE.getString(context, Constants.User.USER_DEVICE_TOKEN, "");
        header.put("devicekey", cacheDeviceToken == null ? registrationId : cacheDeviceToken);
        OkGo.get(ApiAddress.INSTANCE.BASE_URL + ApiAddress.GET_TOKEN)
                .headers(header)
                .params(params)
                .execute(new AbsCallback<Object>() {
                    @Override
                    public Object convertResponse(Response response) throws Throwable {
                        return response;
                    }

                    @Override
                    public void onSuccess(final com.lzy.okgo.model.Response<Object> response) {
                        final Response rawResponse = response.getRawResponse();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("http_url", ApiAddress.INSTANCE.BASE_URL + ApiAddress.GET_TOKEN);
                                try {
                                    Headers headers = rawResponse.headers();
                                    if (headers.get("Set-Cookie") != null && headers.get("Set-Cookie") != "") {//判断服务器是否有返回cookie
                                        CacheUtils.INSTANCE.setString(context, "set_Cookie", headers.get("Set-Cookie"));
                                    }
                                    String string = rawResponse.body().string();
                                    Log.e("GET_TOKEN", string);
                                    JSONObject obj = JSON.parseObject(string);
                                    JSONObject jsonObject = obj.getJSONObject("data");
                                    if (jsonObject != null ) {
                                        String token = jsonObject.getString("accesstoken");
                                        String web_url = jsonObject.getString("web_url");
                                        CacheUtils.INSTANCE.setString(context, "token", token);
                                        CacheUtils.INSTANCE.setString(context, "web_url", web_url);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
    }

    private static class SaveDataRunaable implements Runnable {
        Response response;
        Context context;
        String url, responseStr;
        HttpParams params;
        DoNext doNext;
        ResponseBody body;
        String responseMsg = "";
        String string = "";
        Headers headers;
        int code;

        public SaveDataRunaable(Context context, String responseStr, Response response, String url, HttpParams params, DoNext doNext) {
            this.response = response;
            this.context = context;
            this.url = url;
            this.params = params;
            this.doNext = doNext;
            this.responseStr = responseStr;

        }

        @Override
        public void run() {
            try {
                if (responseStr != null) {
                    string = responseStr;
                    code = JSON.parseObject(responseStr).getInteger("code");
                }
                if (response != null) {
                    code = response.code();
                    body = response.body();
                    responseMsg = response.message();
                    string = body.string();
                    headers = response.headers();
                    body.close();
                    if (!TextUtils.isEmpty(string)) {
                        if (code >= 500 || string.contains("<!DOCTYPE")) {
                            return;
                        }
                        System.gc();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //处理返回Headers
                        setResponseHeaders(context, headers);
                        //处理返回body
                        setResponseBody(string);
                        doNext.doResult();
                        System.gc();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
            }
        }

        /**
         * 设置返回的ResponseHeaders
         *
         * @param headers
         */
        private void setResponseHeaders(Context context, Headers headers) {
            if (headers == null) {
                return;
            }
            if (code == 200 && headers.get("X_End_User") != null) {
                if (headers.get("X_End_User").equals("0")) {
                    outLogin(context, "");
                } else {
                    CacheUtils.INSTANCE.setBoolean(context, Constants.User.IS_LOGIN, true);
                }
            }
//            if (headers.get("Set-Cookie") != null && headers.get("Set-Cookie") != "") {//判断服务器是否有返回cookie
//                CacheUtils.INSTANCE.setString(context, "set_Cookie", headers.get("Set-Cookie"));
//            }
            String x_auto_coin = headers.get("X_AUTO_COIN");
            if (x_auto_coin != null) { //处理甜甜圈弹出·
                int coinNum = Integer.valueOf(x_auto_coin);
                if (coinNum > 0) {
                    if (context instanceof BaseActivity) {
                        ((BaseActivity) context).showCoinDialog();
                    }
                }
            }
        }

        /**
         * 解析返回的body部分
         *
         * @param body
         */
        private void setResponseBody(String body) {
            if (body == null || body.isEmpty()) {
                return;
            }
            Log.e("http_url", url);
            switch (code) {
                case 200:
                    doNext.doNext(string, headers);
                    Log.e("data==", string);
                    break;
                case 402:
                    CacheUtils.INSTANCE.setString(context, "token", "");
                    getToken(context, doNext);
                    break;
                case 401:
                    outLogin(context, responseMsg);
                    break;
                case 408:
                    onResponseError(doNext, string, context);
                    UpDataApkTools.getInstance().setContext(context).upDataApkData(false, string);
                    break;
                default:
                    if (code < 500) {
                        onResponseError(doNext, string, context);
                    }
            }
        }
    }


}
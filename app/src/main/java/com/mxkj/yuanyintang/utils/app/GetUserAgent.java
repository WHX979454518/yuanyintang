package com.mxkj.yuanyintang.utils.app;

import android.os.Build;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by LiuJie on 2017/8/23.
 */

public class GetUserAgent {
    private String userAgent;

    public String getUserAgent() {
        if (TextUtils.isEmpty(userAgent)) {
            String webUserAgent = null;
            try {
                Class<?> sysResCls = Class.forName("com.android.internal.R$string");
                Field webUserAgentField = sysResCls.getDeclaredField("web_user_agent");
                Integer resId = (Integer) webUserAgentField.get(null);
                webUserAgent = OkGo.getInstance().getContext().getString(resId);
            } catch (Exception e) {
            }
            if (TextUtils.isEmpty(webUserAgent)) {
                webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/5.0 %sSafari/533.1";
            }

            Locale locale = Locale.getDefault();
            StringBuffer buffer = new StringBuffer();
            final String version = Build.VERSION.RELEASE;
            if (version.length() > 0) {
                buffer.append(version);
            } else {
                buffer.append("1.0");
            }
            buffer.append("; ");
            final String language = locale.getLanguage();
            if (language != null) {
                buffer.append(language.toLowerCase(locale));
                final String country = locale.getCountry();
                if (!TextUtils.isEmpty(country)) {
                    buffer.append("-");
                    buffer.append(country.toLowerCase(locale));
                }
            } else {
                buffer.append("en");
            }
            if ("REL".equals(Build.VERSION.CODENAME)) {
                final String model = Build.MODEL;
                if (model.length() > 0) {
                    buffer.append("; ");
                    buffer.append(model);
                }
            }
            final String id = Build.ID;
            if (id.length() > 0) {
                buffer.append(" Build/");
                buffer.append(id);
            }
            userAgent = String.format(webUserAgent, buffer, "Mobile ");
            return userAgent;
        }
        return userAgent;
    }
}

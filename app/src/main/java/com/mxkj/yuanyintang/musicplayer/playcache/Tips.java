package com.mxkj.yuanyintang.musicplayer.playcache;

/**
 * Created by LiuJie on 2017/8/28.
 */

public class Tips {

    /** *
     *    1、 compile 'org.slf4j:slf4j-android:1.7.21'
     *
     *   2、  Service implements CacheListener
     *
     *   3、 @Override public void onCacheAvailable(File cacheFile, String url, int percentsAvailable)
     *
     *   4、检查缓存的状态
     *      HttpProxyCacheServer proxy = MainApplication.Companion.getProxy(getApplicationContext());
     *      boolean fullyCached = proxy.isCached(url);
     *
     *   5、 HttpProxyCacheServer proxy = MainApplication.Companion.getProxy(getApplicationContext());
     *      String proxyUrl = proxy.getProxyUrl(url);
     *      mediaPlayer.setDataSource(proxyUrl);
     *
     * */


}

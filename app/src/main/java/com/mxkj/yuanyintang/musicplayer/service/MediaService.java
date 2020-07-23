package com.mxkj.yuanyintang.musicplayer.service;
//TODO  播放控制逻辑优化  耳机线控
/**
 * Created by LiuJie on 2017/11/21.
 */

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.HomeActivity;
import com.mxkj.yuanyintang.database.songhistorysql.SongHistoryBean;
import com.mxkj.yuanyintang.database.songhistorysql.SongHistoryDao;
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.config.SingleConfig;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.musicplayer.activity.LockScreenActivity;
import com.mxkj.yuanyintang.musicplayer.play_control.GetMusicInfo;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList;
import com.mxkj.yuanyintang.musicplayer.playcache.CacheListener;
import com.mxkj.yuanyintang.musicplayer.playcache.HttpProxyCacheServer;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.FloatLogoEvent;
import com.mxkj.yuanyintang.utils.rxbus.event.MusicNoFileIoEvent;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.umeng.message.proguard.f;

import org.greenrobot.eventbus.EventBus;

import static com.mxkj.yuanyintang.utils.constant.Constants.LAST_PLAY.BEAN_STR;
import static com.umeng.message.proguard.f.i;

public class MediaService extends Service implements CacheListener {
    public static final String LAST_PLAY_SONG = "last_play_song";
    public static final String LAST_PLAY_SONG_TOTAL_TIME = "last_play_song_dur";
    public static final String LAST_PLAY_SONG_PLAY_TIME = "last_play_song_curr";
    public static IjkMediaPlayer mediaPlayer;
    public playerCtlrecevier re;
    public IntentFilter filter;

    @Nullable
    public static MusicInfo.DataBean bean;//当前播放的歌曲
    //    TODO
    public static int currPlayingMusicId;//当前播放的歌的id
    public static int currPlayingchartsId;//当前播放的排行榜id
    public static int currPlayingalbumId;//当前播放的歌单id

    public static List<MusicInfo.DataBean> playList = new ArrayList<>();//当前播放歌曲列表
    public static List<MusicInfo.DataBean> alreadyPlayList = new ArrayList<>();//已经播放过的
    public static List<MusicInfo.DataBean> nextPlayList = new ArrayList<>();//下一首播放的列表
    public static List<MusicInfo.DataBean> tempList = new ArrayList();

    public static int playModel = 2;//1  单曲  0随机 2列表
    public static int currPlayPos;//当前正在播放歌曲在列表的位置
    public int currLastPlayPos;
    private Boolean isNextPlayer = false;
    private MaterialDialog dialog;
    private AysRunnable aysRunnable;

    public static void setRandomPlayList() {
        removeExt(playList);
        alreadyPlayList.clear();
    }

    @NonNull
    private Timer mTimer;
    @Nullable
    private TimerTask mTimerTask;
    @NonNull
    private Notification notification;
    private RemoteViews contentView;
    private NotificationManager notifyManager;
    @NonNull
    public Handler handler = new Handler();
    private int currCachePercent;
    private File cacheFile;
    private HttpProxyCacheServer proxy;
    private boolean isChgProgross;
    int seekTo;
    private String TAG = "com.mxkj.yuanyintang.service";
    public static final String ACTION_PAUSE = "com.mxkj.pause";
    public static final String ACTION_STOP = "com.mxkj.stop";
    public static final String ACTION_SEEK = "com.mxkj.seek";
    public static final String ACTION_CANCLE = "com.mxkj.cancle";
    public static final String ACTION_PRE = "com.mxkj.pre";
    public static final String ACTION_NEXT = "com.mxkj.next";
    public static final String CHANGE_PLAYING_IMG = "com.mxkj.chenge_playing_img";
    public static final String MEDIA_PLAY_IS_PAUSE = "_media_play_is_pause";
    private Disposable mMusicNoFileIoEvent;

    private boolean mResumeAfterCall = false;


    @Override
    public void onCreate() {
        super.onCreate();
        if (null == re) {
            playList = PlayList.getList(this);
            re = new playerCtlrecevier();
            filter = new IntentFilter();
            filter.addAction(ACTION_PAUSE);//暂停或播放
            filter.addAction(ACTION_STOP);//停止
            filter.addAction(ACTION_SEEK);//拖动进度条
            filter.addAction(ACTION_NEXT);//下一曲
            filter.addAction(ACTION_PRE);//上一曲
            filter.addAction(ACTION_CANCLE);//退出通知栏，停止播放
            filter.addAction(Intent.ACTION_SCREEN_OFF);//锁屏
            filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);//??
            filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);//??
            registerReceiver(re, filter);
        }
        mMusicNoFileIoEvent = RxBus.getDefault().toObservable(MusicNoFileIoEvent.class)
                .subscribeWith(new RxBusSubscriber<MusicNoFileIoEvent>() {
                    @Override
                    protected void onEvent(MusicNoFileIoEvent musicNoFileIoEvent) throws Exception {
                        mediaPlayer.stop();
                    }
                });

        RxBus.getDefault().add(mMusicNoFileIoEvent);


        //电话监听的主要这两句
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tmgr.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        initAudio();
    }

    private void initDiaLog() {
        if (null != MainApplication.Companion.getApplication().getCurrentActivity()) {
            dialog = new MaterialDialog(MainApplication.Companion.getApplication().getCurrentActivity());
            Log.e(TAG, "initDiaLog: 当前activity" + MainApplication.Companion.getApplication().getCurrentActivity());
            dialog.content(
                    "您已关闭数据流量播放功能，是否通过流量播放这首歌曲呢？")
                    .btnText("取消", "确定")
                    .titleTextSize(16)
                    .titleTextColor(ContextCompat.getColor(MainApplication.Companion.getContext(), R.color.color_14_text))
                    .contentTextColor(ContextCompat.getColor(MainApplication.Companion.getContext(), R.color.color_66_text))
                    .contentTextSize(14)
                    .btnTextSize(14)
                    .btnTextColor(ContextCompat.getColor(MainApplication.Companion.getContext(), R.color.base_red), ContextCompat.getColor(MainApplication.Companion.getContext(), R.color.base_red))
                    .showAnim(null)
                    .dismissAnim(null);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnBtnClickL(
                    new OnBtnClickL() {//left btn click listener
                        @Override
                        public void onBtnClick() {
                            dialog.dismiss();
                        }
                    }, new OnBtnClickL() {//right btn click listener
                        @Override
                        public void onBtnClick() {
                            canPlay();
                            dialog.dismiss();
                            //用户用确定用流量播放当前歌曲，就把流量播放开关打开
                            CacheUtils.INSTANCE.setBoolean(getApplicationContext(), "open3gNet", true);
                        }
                    }
            );
        }
    }

    /**
     * 监听缓存进度
     */
    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        this.cacheFile = cacheFile;
        currCachePercent = percentsAvailable;
        setSecondPro(percentsAvailable);
    }

    /**
     * 给播放器进度条设置缓冲进度
     */
    private void setSecondPro(int percentsAvailable) {
        CacheBus eventBus = new CacheBus();
        long duration = mediaPlayer.getDuration();
        int i = (int) (duration * percentsAvailable / 100);
        eventBus.setProgress(i);
        EventBus.getDefault().post(eventBus);
    }

    @Override
    public void onDestroy() {
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tmgr.listen(mPhoneStateListener, 0);

        super.onDestroy();
        RxBus.getDefault().remove(mMusicNoFileIoEvent);
        unregisterReceiver(re);
        if (notifyManager != null) {
            notifyManager.cancel(0);
        }
        MainApplication.Companion.getProxy(getApplication()).unregisterCacheListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(@Nullable final Intent intent, final int flags, int startId) {
        initDiaLog();
        if (null != mediaPlayer) {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        if (intent != null && intent.getSerializableExtra("bean") != null) {
            MusicInfo.DataBean dataBean = (MusicInfo.DataBean) intent.getSerializableExtra("bean");
            bean = dataBean;
            sendBroadcast(new Intent("startplay"));
            CacheUtils.INSTANCE.setString(this, BEAN_STR, JSON.toJSONString(dataBean));
            getPlayPosInList();
            if (bean != null && bean.getVideo_info() != null && bean.getVideo_info().getLink() != null) {
                File file = new File(bean.getVideo_info().getLink());
                if (file.exists()) {
                    play();
                } else {
                    if (NetConnectedUtils.isPhoneNetConnected(getApplicationContext())) {//如果是数据流量
                        if (isCachedMusic(bean)) {
                            initNotificationBar();
                            play();
                        } else {//没有缓存或下载
                            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
                            boolean isLock = false;
                            String MY_PKG_NAME = "com.mxkj.yuanyintang.musicplayer.activity.LockScreenActivity";
                            for (ActivityManager.RunningTaskInfo info : list) {
                                if (TextUtils.equals(MY_PKG_NAME, info.topActivity.getClassName())) {
                                    isLock = true;
                                    break;
                                }
                            }
                            if (!CacheUtils.INSTANCE.getBoolean(getApplicationContext(), "open3gNet", true)) {
                                if (!isLock) {
                                    if (dialog != null && !dialog.isShowing()) {
                                        dialog.show();
                                    }
                                } else {
                                    mediaPlayer.reset();
                                }
                            } else {
                                initNotificationBar();
                                play();
                            }
                        }
                    } else {
                        initNotificationBar();
                        play();
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 在播放的时候更新进度条，暂停播放停止更新
     */
    private void setTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null && isChgProgross == true) {
                    if (mediaPlayer.isPlaying()) {
                        Intent in2 = new Intent();
                        in2.setAction("setplay");
                        sendBroadcast(in2);
                        Intent in3 = new Intent();
                        in3.setAction("dur");
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("dur", (int) mediaPlayer.getDuration());
                        bundle1.putInt("currdur", (int) mediaPlayer.getCurrentPosition());
                        seekTo = (int) mediaPlayer.getCurrentPosition();
                        in3.putExtras(bundle1);
                        sendBroadcast(in3);
                    }
                }
            }
        };
        //开始一个定时任务
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    /**
     * 检查缓存的状态  是否完全缓存当前歌曲
     */
    private HttpProxyCacheServer checkCachedState() {
        HttpProxyCacheServer proxy = MainApplication.Companion.getProxy(getApplicationContext());
        if (bean.getVideo_info() != null) {
            boolean fullyCached = proxy.isCached(bean.getVideo_info().getLink());
            if (fullyCached) {//已缓存，设置缓存进度为100
                try {
                    setSecondPro((int) mediaPlayer.getDuration());
                } catch (RuntimeException e) {
                }
            } else {
                if (cacheFile != null) {
                    cacheFile.deleteOnExit();
                }
            }
        }

        return proxy;
    }

    public void playLocalMusic(MusicIndex.ItemInfoListBean.MusicBean musicBean) {
        if (mediaPlayer == null) {
            mediaPlayer = new IjkMediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        canPlay();
    }


    /**
     * 控制播放逻辑广播接收器
     */
    public class playerCtlrecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: ------------>" + action);
            switch (action) {
                case ACTION_PAUSE:
                    RxBus.getDefault().post(new PlayerMusicRefreshDataEvent());
                    CacheUtils.INSTANCE.setBoolean(getApplicationContext(), MediaService.MEDIA_PLAY_IS_PAUSE, true);
                    if (bean != null) {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            sendBroadcast(new Intent("setpause"));
                            CacheUtils.INSTANCE.setString(getApplicationContext(), LAST_PLAY_SONG, JSON.toJSONString(bean));
                            CacheUtils.INSTANCE.setLong(getApplicationContext(), LAST_PLAY_SONG_TOTAL_TIME, mediaPlayer.getDuration());
                            CacheUtils.INSTANCE.setLong(getApplicationContext(), LAST_PLAY_SONG_PLAY_TIME, mediaPlayer.getCurrentPosition());
                        } else if (mediaPlayer != null) {
                            mediaPlayer.start();
                            sendBroadcast(new Intent("setplay"));
                            isChgProgross = true;
                        }else{
                            startPlay();
                        }
                        initNotificationBar();
                    }
                    break;
                case ACTION_STOP:
                    mediaPlayer.stop();
                    RxBus.getDefault().post(new PlayerMusicRefreshDataEvent());
                    CacheUtils.INSTANCE.setBoolean(getApplicationContext(), MediaService.MEDIA_PLAY_IS_PAUSE, true);
                    break;
                case ACTION_SEEK:
                    Log.e("TAG", "onReceive: ACTION_SEEK");
                    Log.e("TAG", "onReceive: " + intent.getIntExtra(ACTION_SEEK, 0));
                    if (bean != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.seekTo((int) intent.getLongExtra(ACTION_SEEK, 0));
                            CacheUtils.INSTANCE.setBoolean(getApplicationContext(), MediaService.MEDIA_PLAY_IS_PAUSE, false);
                        }
                    }
                    break;
                case ACTION_CANCLE:
                    if (notifyManager != null) {
                        Log.e("TAG", "onReceive: " + "cancle");
                        notifyManager.cancel(0);
                        mediaPlayer.pause();
                        sendBroadcast(new Intent("setpause"));
                        RxBus.getDefault().post(new PlayerMusicRefreshDataEvent());
                        CacheUtils.INSTANCE.setBoolean(getApplicationContext(), MediaService.MEDIA_PLAY_IS_PAUSE, true);
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF://监听锁屏
                    if (CacheUtils.INSTANCE.getBoolean(getApplicationContext(), "openLock", true) == true) {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            Intent lockscreen = new Intent(getApplicationContext(), LockScreenActivity.class);
                            lockscreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(lockscreen);
                        }
                    }
                    break;
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        sendBroadcast(new Intent("setpause"));
                        CacheUtils.INSTANCE.setBoolean(getApplicationContext(), MediaService.MEDIA_PLAY_IS_PAUSE, true);
                    }
                    break;
                case Intent.ACTION_NEW_OUTGOING_CALL://去电
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        sendBroadcast(new Intent("setpause"));
                        CacheUtils.INSTANCE.setBoolean(getApplicationContext(), MediaService.MEDIA_PLAY_IS_PAUSE, true);
                    }
                    break;
                case ACTION_NEXT:
                    playNext();
                    CacheUtils.INSTANCE.setBoolean(getApplicationContext(), MediaService.MEDIA_PLAY_IS_PAUSE, false);
                    break;
                case ACTION_PRE:
                    playPre();
                    CacheUtils.INSTANCE.setBoolean(getApplicationContext(), MediaService.MEDIA_PLAY_IS_PAUSE, false);
                    break;
            }
        }
    }

    /**
     * 状态栏播放器
     */
    public void initNotificationBar() {
        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (bean != null) {
            if (bean.getImgpic_info() != null && !TextUtils.isEmpty(bean.getImgpic_info().getLink())) {//java.lang.IllegalArgumentException: unexpected url:
                ImageLoader.with(getApplicationContext())
                        .override(60, 60)
                        .url(bean.getImgpic_info().getLink())
                        .scale(ScaleMode.CENTER_CROP)
                        .asBitmap(new SingleConfig.BitmapListener() {
                            @Override
                            public void onSuccess(Bitmap bitmap) {
                                contentView = new RemoteViews(getPackageName(),
                                        R.layout.notification_layout);
                                Notification.Builder mNotifyBuilder = new Notification.Builder(getApplicationContext());
                                notification = mNotifyBuilder
                                        .setSmallIcon(R.drawable.tab_music_player)
                                        .setLargeIcon(bitmap)
                                        .build();
                                notification.bigContentView = contentView;
                                notification.contentView = contentView;
                                contentView.setTextViewText(R.id.notification_music_title, bean.getTitle());
                                contentView.setTextViewText(R.id.notification_music_Artist, bean.getNickname());
                                contentView.setImageViewBitmap(R.id.notification_artist_image, bitmap);
                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        contentView.setImageViewResource(R.id.notification_play_button, R.drawable.notify_pause_normal);
                                    } else {
                                        contentView.setImageViewResource(R.id.notification_play_button, R.drawable.play_notify_normal);
                                    }
                                }
                                // 点击跳转到主界面
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("openplayer", true);
                                PendingIntent intent_go = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                contentView.setOnClickPendingIntent(R.id.notification_layout, intent_go);
                                Intent intentPlay = new Intent(ACTION_PAUSE);//新建意图，并设置action标记为"play"，用于接收广播时过滤意图信息
                                PendingIntent pIntentPlay = PendingIntent.getBroadcast(getApplicationContext(), 0,
                                        intentPlay, 0);
                                contentView.setOnClickPendingIntent(R.id.notification_play_button, pIntentPlay);//为play控件注册事件
                                Intent intentNext = new Intent(ACTION_NEXT);
                                PendingIntent pIntentNext = PendingIntent.getBroadcast(getApplicationContext(), 0,
                                        intentNext, 0);
                                contentView.setOnClickPendingIntent(R.id.notification_next_song_button, pIntentNext);
                                Intent intentCancel = new Intent(ACTION_CANCLE);
                                PendingIntent pIntentCancel = PendingIntent.getBroadcast(getApplicationContext(), 0,
                                        intentCancel, 0);
                                contentView.setOnClickPendingIntent(R.id.notification_exit_button, pIntentCancel);
                                notification.flags = notification.FLAG_NO_CLEAR;//设置通知点击或滑动时不被清除
                                notifyManager.notify(0, notification);
                            }

                            @Override
                            public void onFail() {

                            }
                        });
            }
        }
    }

    /**
     * 播放
     */

    public void play() {
        isChgProgross = false;
        startPlay();
        setTimer();
        setOnerrorListener(mediaPlayer);
        setOnCompletionListener(mediaPlayer);
    }

    private void startPlay() {
        Intent intent = new Intent(CHANGE_PLAYING_IMG);
        intent.putExtra("bean", bean);
        sendBroadcast(intent);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                System.gc();
            }
        }
        mediaPlayer = new IjkMediaPlayer();
        try {
            String proxyUrl = null;
            if (bean.getVideo_info() != null) {
                File file = new File(bean.getVideo_info().getLink());
                if (file.exists()) {
                    mediaPlayer.setDataSource(this, Uri.parse(file.getAbsolutePath()));
                } else {
                    proxy = checkCachedState();
                    proxy.registerCacheListener(this, bean.getVideo_info().getLink());
                    proxyUrl = proxy.getProxyUrl(bean.getVideo_info().getLink());
                    String s = TasksManager.getFilePath(getApplicationContext()) + "/" + bean.getTitle() + ".mp3";
                    File file2 = new File(s);
                    if (file2.exists()) {
                        mediaPlayer.setDataSource(file2.getAbsolutePath());
                    } else {
                        mediaPlayer.setDataSource(proxyUrl);
                    }
                }
            }
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new IjkMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    assert mediaPlayer != null;
                    mediaPlayer.start();
                    if (CacheUtils.INSTANCE.getBoolean(getApplicationContext(), "openNotify", true) == true) {
                        initNotificationBar();
                    }
                    if (bean != null && bean.getImgpic_info() != null && bean.getImgpic_info().getLink() != null) {
                        RxBus.getDefault().post(new FloatLogoEvent(bean.getImgpic_info().getLink()));
                        /**
                         * 歌曲开始播放的时候发送事件更新UI
                         * */
                        RxBus.getDefault().post(new PlayerMusicRefreshDataEvent());
                        SongHistoryBean.ImgpicInfoBean imgpicInfoBean = new SongHistoryBean.ImgpicInfoBean();
                        imgpicInfoBean.setLink(bean.getImgpic_info().getLink());
                        SongHistoryBean songHistoryBean = new SongHistoryBean().setCatename(bean.getCatename())
                                .setSong_id(bean.getSong_id())
                                .setContents(bean.getTitle() + "")
                                .setLyrics(bean.getLyrics() + "")
                                .setMusic_id(bean.getId())
                                .setMusic_name(StringUtils.isEmpty(bean.getTitle() + ""))
                                .setMusic_nickname(StringUtils.isEmpty(bean.getNickname()))
                                .setPlaytime(StringUtils.isEmpty(bean.getPlaytime() + ""))
                                .setTime(bean.getUp_time())
                                .setUid(bean.getUid());
                        if (bean.getVideo_info() != null) {
                            songHistoryBean.setVideo_link(bean.getVideo_info().getLink());
                        }
                        songHistoryBean.setImgpic_info(imgpicInfoBean);
                        new SongHistoryDao(MediaService.this).add(songHistoryBean);

                    }
                    if (bean != null && bean.getVideo_info() != null) {
                        Log.e(TAG, "onPrepared:startplay =" + bean.getTitle());
                        isChgProgross = true;
                    }
                }
            });
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException: -------1----->");
        } catch (IOException e) {
            Log.d(TAG, "IOException: -------2----->");
        } catch (Exception e) {
            Log.d(TAG, "Exception: ------3------>");
            e.printStackTrace();
        }
    }

    private void getPlayPosInList() {
        if (bean != null) {
            if (null == nextPlayList) {
                return;
            }
            if (nextPlayList.size() > 0) {
                nextPlayMusic();
            } else {
                if (playModel == 0) {//随机播放
                } else {
                    currLastPlayPos = currPlayPos;
                    if (playList == null) return;
                    for (int i = 0; i < playList.size(); i++) {
                        if (bean.getId() == playList.get(i).getId()) {
                            currPlayPos = i;
                        }
                    }
                }
            }
        }
    }

    /**
     * 播放"下一首播放"
     */
    private void nextPlayMusic() {
        if (null != nextPlayList) {
            nextStartPlayer(nextPlayList.get(nextPlayList.size() - 1));
            nextPlayList.remove(nextPlayList.size() - 1);
        }
    }

    private void setOnCompletionListener(final IjkMediaPlayer mediaPlayer) {
        mediaPlayer.setOnCompletionListener(new IjkMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                isChgProgross = false;
                if (currCachePercent > 96) {//已缓存文件大小。认为本次播放完成。有些歌曲时长比实际播放完后的长度多一两秒
                    sendBroadcast(new Intent(ACTION_NEXT));
                } else if (currCachePercent > 0) {
                    mediaPlayer.reset();
                    HttpProxyCacheServer proxy = MainApplication.Companion.getProxy(getApplicationContext());
                    if (bean.getVideo_info() != null) {
                        String proxyUrl = proxy.getProxyUrl(bean.getVideo_info().getLink());
                        try {
                            mediaPlayer.setDataSource(proxyUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        mediaPlayer.prepareAsync();
                    }catch (IllegalStateException e){

                    }
                    mediaPlayer.setOnPreparedListener(new IjkMediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(IMediaPlayer iMediaPlayer) {
                            mediaPlayer.start();
                            isChgProgross = true;
                            mediaPlayer.seekTo(seekTo);
                            isChgProgross = false;
                        }
                    });
                } else {
                    Log.d(TAG, "onCompletion: -------介个---->");
                    sendBroadcast(new Intent(ACTION_NEXT));
                }
            }
        });
    }

    private void setOnerrorListener(IjkMediaPlayer mediaPlayer) {
        mediaPlayer.setOnErrorListener(new IjkMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                isChgProgross = false;
                return false;
            }
        });
    }

    /**
     * 播放上一曲
     */
    public void playPre() {
        if (playModel == 0) {
            Log.e(TAG, "getRandomBean: 上一曲--" + alreadyPlayList.size() + "------" + playList.size());
            getRandomBean();
            playPreOrNext();
            return;
        }
        if (currPlayPos == 0) {
            currPlayPos = MediaService.playList.size() - 1;
        }
        if (currPlayPos > 0) {
            currPlayPos--;
        }
        if (playModel == 1) {
            currPlayPos++;
        }
        if (playList.size() == 1) {
            currPlayPos = 0;
        }
        if (MediaService.playList.size() > 0 && currPlayPos > MediaService.playList.size()) {
            currPlayPos = MediaService.playList.size() - 1;
        }
        if (MediaService.playList.size() > currPlayPos) {
            playPreOrNext();
        }
    }

    /**
     * 播放下一曲
     */
    public void playNext() {
        if (nextPlayList.size() > 0) {
            nextPlayMusic();
        } else {
            if (playModel == 0) {
                getRandomBean();
                playPreOrNext();
                return;
            }
            if (isNextPlayer) {
                isNextPlayer = false;
                currPlayPos = currLastPlayPos;
            }
            if (currPlayPos < playList.size()) {
                currPlayPos++;
            }
            if (currPlayPos >= playList.size()) {
                currPlayPos = 0;
            }
            if (playList.size() == 1) {
                currPlayPos = 0;
            }
            if (playModel == 1) {
                currPlayPos--;
            }
            if (currPlayPos < 0) {
                currPlayPos = 0;
            }
            playPreOrNext();
        }
    }

    /*获取当前随机播放音乐*/
    private void getRandomBean() {
        if (playList.size() == 0) {
            alreadyPlayList.clear();
            tempList.clear();
            return;
        }
        removeExt(alreadyPlayList);
        removeExt(tempList);
//        1、将播放列表copy一份
//        2、对比播放列表和已播放过的列表，从templist移除已经播放过的
//        3、每播放一首，往已播放列表加进去
        removeTempList();
//        歌曲全部播放了一遍
        Log.e(TAG, "呵呵: 上次播放---" + bean.getTitle());
        if (tempList.size() == 0) {
            alreadyPlayList.clear();
            tempList.clear();
            tempList.addAll(playList);
            removeTempList();
            if (bean != null) {
                for (int i = 0; i < tempList.size(); i++) {
                    if (bean.getId() == tempList.get(i).getId()) {
                        alreadyPlayList.add(tempList.get(i));
                        tempList.remove(i);
                        break;
                    }
                }
            }
        }
//        4、从当前tempList列表随机取出来播放即可
        if (tempList.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(tempList.size());
            bean = tempList.get(index);
            alreadyPlayList.add(bean);
            tempList.remove(bean);
            Log.e(TAG, "getRandomBean: 当前播放---" + bean.getTitle() + "----已播放" + alreadyPlayList.size() + "首" + "---未播放：" + tempList.size() + "首");

        }
    }

    private void removeTempList() {
        if (playList == null) return;
        for (int i = 0; i < playList.size(); i++) {
            for (int j = 0; j < alreadyPlayList.size(); j++) {
                if (alreadyPlayList.get(j).getId() == playList.get(i).getId()) {
                    for (int k = 0; k < tempList.size(); k++) {
                        if (alreadyPlayList.get(j).getId() == tempList.get(k).getId()) {
                            Log.e(TAG, "getRandomBean: 从tempList移除" + tempList.get(k).getTitle());
                            tempList.remove(k);
                        }
                    }
                }
            }
        }
    }

    private static List<MusicInfo.DataBean> removeExt(List<MusicInfo.DataBean> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getId() == list.get(i).getId()) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    public void playPreOrNext() {
        System.gc();
        if (aysRunnable == null) {
            aysRunnable = new AysRunnable();
        }
        aysRunnable.run();
    }

    private void isPlayerModel() {
        if (playList.size() == 0) {
            return;
        }
        if (playModel != 0) {
            if (playList.size() <= currPlayPos) {
                currPlayPos = 0;
            }
            bean = MediaService.playList.get(currPlayPos);
        }
        if (playList.size() == alreadyPlayList.size()) {
            alreadyPlayList.clear();
        }
        sendBroadcast(new Intent("startplay"));
        if (bean != null && bean.getVideo_info() != null && bean.getVideo_info().getLink() != null) {
            if (new File(bean.getVideo_info().getLink()).exists()) {
                play();
                return;
            }
        }
        if (NetConnectedUtils.isPhoneNetConnected(getApplicationContext())) {//如果是数据流量
            Log.e(TAG, "数据流量播放》》》》》》》: ");
            if (isCachedMusic(bean)) {
                Log.e(TAG, "歌曲已缓存或下载》》》》》》》: ");
                canPlay();
            } else {//没有缓存或下载
                Log.e(TAG, "歌曲没缓存》》》》》》: ");
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
                boolean isLock = false;
                String MY_PKG_NAME = "com.mxkj.yuanyintang.musicplayer.activity.LockScreenActivity";
                for (ActivityManager.RunningTaskInfo info : list) {
                    if (TextUtils.equals(MY_PKG_NAME, info.topActivity.getClassName())) {
                        isLock = true;
                        break;
                    }
                }
                if (!CacheUtils.INSTANCE.getBoolean(getApplicationContext(), "open3gNet", true)) {
                    Log.e(TAG, "流量播放开关没开》》》》》》: ");
                    if (!isLock) {
                        Log.e(TAG, "不是锁屏状态》》》》》》: ");
                        if (dialog != null && !dialog.isShowing()) {
                            Log.e(TAG, "不是锁屏状态:------- show");
                            dialog.show();
                        }
                    } else {
                        mediaPlayer.reset();
                    }
                } else {
                    Log.e(TAG, "流量播放被允许》》》》》》: ");
                    canPlay();
                }
            }
        } else {
            canPlay();
        }
    }

    private void canPlay() {
        PlayCtrlUtil.INSTANCE.startServiceToPlay(this, bean);
        if (CacheUtils.INSTANCE.getBoolean(getApplicationContext(), "openNotify", true) == true) {
            initNotificationBar();
        }
    }

    private void nextStartPlayer(MusicInfo.DataBean dataBean) {
        isNextPlayer = true;
        PlayCtrlUtil.INSTANCE.startServiceToPlay(this, dataBean);
        for (int i = 0; i < playList.size(); i++) {
            if (playList.get(i).getId() == dataBean.getId()) {
                alreadyPlayList.add(playList.get(i));
            }
        }
    }

    /**
     * 音乐是否缓存或者已下载
     */
    public boolean isCachedMusic(@Nullable MusicInfo.DataBean bean) {
        if (bean != null && bean.getVideo_info() != null) {
            if (bean.getVideo_info().getLink() != null) {
                HttpProxyCacheServer proxy = MainApplication.Companion.getProxy(getApplicationContext());
                String proxyUrl = proxy.getProxyUrl(bean.getVideo_info().getLink());
                File file = new File(proxyUrl);
                File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/yyt_music/" + bean.getTitle() + ".mp3");
                boolean b = file.exists() || file2.exists();
                return b;
            }
        }
        return false;
    }

    public static void clearData() {
        bean = null;
        playList.clear();
        playList = null;
        nextPlayList.clear();
        nextPlayList = null;
//        randomPlayPosition.clear();
//        randomPlayPosition = null;
    }

    public static List<MusicInfo.DataBean> getPlayList() {
        if (null == playList) {
            return new ArrayList<>();
        }
        return playList;
    }

    public static List<MusicInfo.DataBean> getNextPlayList() {
        if (null == nextPlayList) {
            return new ArrayList<>();
        }
        return nextPlayList;
    }

    public static IjkMediaPlayer getMediaPlayer() {
        if (null == mediaPlayer) {
            mediaPlayer = new IjkMediaPlayer();
        }
        return mediaPlayer;
    }

    public class AysRunnable implements Runnable {
        @Override
        public void run() {
            isPlayerModel();
//            GetMusicInfo.INSTANCE.playById(MainApplication.Companion.getApplication(), bean.getId(), 0, new GetMusicInfo.SetBeanData() {
//                @Override
//                public void setBeanData(MusicInfo.DataBean dataBean) {
//                    bean = dataBean;
//                }
//            });
        }
    }




    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int ringvolume = audioManager
                        .getStreamVolume(AudioManager.STREAM_RING);
                if (ringvolume > 0) {
//                    mResumeAfterCall = (getMusicPlayState() || mResumeAfterCall);
//                    //当电话打进来时，就设置为停止播放状态
//                    getAireMusicPlay().setPlayStatus(false);
                    sendBroadcast(new Intent(ACTION_STOP));
                }
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                // pause the music while a conversation is in progress
                //当去电的时候，就设置为停止播放状态
                sendBroadcast(new Intent(ACTION_STOP));
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                // start playing again
                if (mResumeAfterCall) {
                    //当电话挂断是时，就设置为播放状态
//                    sendBroadcast(new Intent(ACTION_PAUSE));
                    startPlay();
                    mResumeAfterCall = false;
                }
            }
        }
    };
    private AudioManager ams = null;//音频管理器
    //微信，qq通话监听
    private void initAudio() {
        ams = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ams.getMode();//这里getmode返回值为3时代表，接通qq或者微信电话
        ams.requestAudioFocus(mAudioFocusListener, 1, 1);
    }
    private boolean isChangeToPause = false;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            Log.d("qcl111", "focusChange----------" + focusChange);

            if (focusChange == 1) {//视频语音挂断状态
                if (isChangeToPause == true) {
                    if(MediaService.mediaPlayer.isPlaying()){

                    }else {
//                        sendBroadcast(new Intent(ACTION_PAUSE));
                        startPlay();
                    }
                    isChangeToPause = false;
                    Log.e("qcl111", "playResume()" + focusChange);
                }
            } else {//微信或者qq语音视频接通状态
                sendBroadcast(new Intent(ACTION_STOP));
                isChangeToPause = true;
                Log.e("qcl111", "playPause()" + focusChange);
            }
        }
    };
}
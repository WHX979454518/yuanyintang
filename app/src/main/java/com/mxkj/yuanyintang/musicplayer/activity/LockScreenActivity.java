package com.mxkj.yuanyintang.musicplayer.activity;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.database.DBManager;
import com.mxkj.yuanyintang.musicplayer.view.SildingFinishLayout;
import com.mxkj.yuanyintang.utils.app.HandlerUtil;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_NEXT;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PRE;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.playList;

public class LockScreenActivity extends RxAppCompatActivity implements View.OnClickListener {
    private TextView mTime, mDate, mMusicName, mMusicArtsit, mLrc;
    private ImageView pre, play, next, fav;
    private Handler mHandler;
    private SildingFinishLayout mView;
    private ImageView mBack;
    private boolean isFav;
    private IntentFilter filter_duration;
    private PlayCtrlReceiver receiver;
    private boolean isUpdateProUi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_lock_screen);
        mTime = (TextView) findViewById(R.id.lock_time);
        mDate = (TextView) findViewById(R.id.lock_date);
        mMusicName = (TextView) findViewById(R.id.lock_music_name);
        mMusicArtsit = (TextView) findViewById(R.id.lock_music_artsit);
        mLrc = (TextView) findViewById(R.id.lock_music_lrc);
        pre = (ImageView) findViewById(R.id.lock_music_pre);
        play = (ImageView) findViewById(R.id.lock_music_play);
        next = (ImageView) findViewById(R.id.lock_music_next);
        fav = (ImageView) findViewById(R.id.lock_music_fav);
        mView = (SildingFinishLayout) findViewById(R.id.lock_root);
        mBack = (ImageView) findViewById(R.id.lock_background);
        mView.setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

            @Override
            public void onSildingFinish() {
                finish();
            }
        });
        mView.setTouchView(getWindow().getDecorView());
        mHandler = HandlerUtil.getInstance(this);
        mHandler.post(updateRunnable);
        pre.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        fav.setOnClickListener(this);
        registReceiver();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lock_music_pre:
                sendBroadcast(new Intent(ACTION_PRE));
                break;
            case R.id.lock_music_play:
                sendBroadcast(new Intent(ACTION_PAUSE));
                break;
            case R.id.lock_music_next:
                sendBroadcast(new Intent(ACTION_NEXT));
                break;
            case R.id.lock_music_fav:
                if (bean != null) {
                    PlayCtrlUtil.INSTANCE.collectSong(this, bean, new PlayCtrlUtil.ChgCollectCallBack() {
                        @Override
                        public void chgCollect(MusicInfo.DataBean dataBean) {
                            bean = dataBean;
                            sendBroadcast(new Intent("cgCollect"));
                            if (dataBean.getCollection() == 0) {
                                fav.setImageResource(R.drawable.lock_btn_love);
                            } else {
                                fav.setImageResource(R.drawable.lock_btn_loved);
                            }
                            for (int i = 0; i < playList.size(); i++) {
                                if (bean.getId() == playList.get(i).getId()) {
                                    playList.get(i).setCollection(bean.getCollection());
                                }
                            }

                            DBManager dbManager = new DBManager(LockScreenActivity.this);
                            dbManager.upDateMusicInfo(bean);/**/
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
                break;
        }
    }

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm-MM月dd日 E", Locale.CHINESE);
            String date[] = simpleDateFormat.format(new Date()).split("-");
            mTime.setText(date[0]);
            mDate.setText(date[1]);
            mHandler.postDelayed(updateRunnable, 300);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lock", " on resume");
        if (bean != null) {
            setPlayerBaseInfo();
        }
    }

    /**
     * 初始化的时候设置播放器信息
     */
    private void setPlayerBaseInfo() {
        mMusicName.setText(bean.getTitle());
        mMusicArtsit.setText(bean.getNickname());
        try {
            fav.setImageResource(bean.getCollection() == 1 ? R.drawable.lock_btn_loved : R.drawable.lock_btn_love);
            setAlbumImgPic();
        } catch (IllegalStateException e) {
        }
    }

    private void setAlbumImgPic() {
        if (bean == null || bean.getImgpic_info() == null || bean.getImgpic_info().getLink() == null) {
            mBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.login_bg_night));
        } else {
            String url = bean.getImgpic_info().getLink();
            ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
                @Override
                public void animate(View view) {
                    view.setAlpha(0f);
                    ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                    fadeAnim.setDuration(750);
                    fadeAnim.start();
                }
            };

            ImageLoader.with(this)
                    .animate(animationObject)
                    .url(url)
                    .placeHolder(R.drawable.login_bg_night)
                    .error(R.drawable.login_bg_night)
                    .into(mBack);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("lock", " on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("lock", " on stop");
    }

    @Override
    protected void onDestroy() {
//        Intent intent = new Intent();
//        intent.setAction(MediaService.ACTION_LOCK_SCREEN);
//        intent.putExtra("islock", false);
//        sendBroadcast(intent);
//        mHandler.removeCallbacks(updateRunnable);
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }


    /***
     * 广播接收器
     * */
    private class PlayCtrlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case "startplay":
                    if (bean != null) {
                        if (bean.getCollection() == 1) {
                            fav.setImageResource(R.drawable.lock_btn_loved);
                        } else {
                            fav.setImageResource(R.drawable.lock_btn_love);
                        }
                    }
                    setAlbumImgPic();
                    if (bean != null) {
                        mMusicArtsit.setText(bean.getNickname());
                        mMusicName.setText(bean.getTitle());
                    }
                    break;
                case "setpause":
                    play.setImageResource(R.drawable.lock_btn_play);
                    break;
                case "setplay":
                    play.setImageResource(R.drawable.lock_btn_pause);
                    break;
                case "cgCollect":
                    fav.setImageResource(bean.getCollection() == 1 ? R.drawable.lock_btn_loved : R.drawable.lock_btn_love);

                    break;
            }
        }
    }

    /**
     * 播放的广播接收器
     */
    public void registReceiver() {
        filter_duration = new IntentFilter();
        filter_duration.addAction("dur");
        filter_duration.addAction("secondDur");//缓冲进度
        filter_duration.addAction("setpause");
        filter_duration.addAction("setplay");
        filter_duration.addAction("playNext");
        filter_duration.addAction("startplay");
        filter_duration.addAction("playPre");
        filter_duration.addAction("cgCollect");//更改收藏状态
        receiver = new PlayCtrlReceiver();
        registerReceiver(receiver, filter_duration);
    }

}

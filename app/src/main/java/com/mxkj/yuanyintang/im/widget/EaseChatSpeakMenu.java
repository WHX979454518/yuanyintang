package com.mxkj.yuanyintang.im.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.mxkj.yuanyintang.R;
import com.hyphenate.util.PathUtil;
import com.mxkj.yuanyintang.im.widget.audiorecord.AudioPlaybackManager;
import com.mxkj.yuanyintang.im.widget.audiorecord.entity.AudioEntity;
import com.mxkj.yuanyintang.im.widget.audiorecord.util.Cons;
import com.mxkj.yuanyintang.im.widget.audiorecord.util.PaoPaoTips;
import com.mxkj.yuanyintang.im.widget.audiorecord.util.PermissionUtil;
import com.mxkj.yuanyintang.im.widget.audiorecord.util.StringUtil;
import com.mxkj.yuanyintang.im.widget.audiorecord.view.LineWaveVoiceView;
import com.mxkj.yuanyintang.im.widget.audiorecord.view.RecordAudioView;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE;

/**
 * Created by LiuJie on 2017/11/8.
 */

public class EaseChatSpeakMenu extends RelativeLayout implements View.OnClickListener, AudioPlaybackManager.OnPlayingListener {
    private static final String TAG = "EaseChatSpeakMenu";


    public static final long DEFAULT_MAX_RECORD_TIME = 61000;
    public static final long DEFAULT_MIN_RECORD_TIME = 1000;
    protected static final int DEFAULT_MIN_TIME_UPDATE_TIME = 1000;
    private long maxRecordTime = DEFAULT_MAX_RECORD_TIME;
    private long minRecordTime = DEFAULT_MIN_RECORD_TIME;

    private RecordAudioView recordAudioView;
    private TextView tvRecordTips;
    private TextView tv_cancel;
    private TextView tv_confirm;
    private View layout_bottom_view;
    private LineWaveVoiceView mHorVoiceView;
    private ImageView iv_player;
    private ImageView iv_delete;
    private TextView tv_play_time;
    private ImageView iv_is_player;
    private ImageView iv_is_delete;

    private String[] recordStatusDescription;
    private String audioFileName;

    private Timer timer;
    private TimerTask timerTask;
    private Handler mainHandler;
    private long recordTotalTime;
    private Context context;
    private Boolean isPauseMusic = false;
    private int voiceTime = 0;

    public EaseChatSpeakMenu(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public EaseChatSpeakMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public EaseChatSpeakMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.ease_widget_chat_speak_menu, this);
        recordAudioView = (RecordAudioView) findViewById(R.id.iv_recording);
        tvRecordTips = (TextView) findViewById(R.id.record_tips);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_play_time = (TextView) findViewById(R.id.tv_play_time);
        iv_player = (ImageView) findViewById(R.id.iv_player);
        iv_is_player = (ImageView) findViewById(R.id.iv_is_player);
        iv_is_delete = (ImageView) findViewById(R.id.iv_is_delete);
        layout_bottom_view = findViewById(R.id.layout_bottom_view);
        mHorVoiceView = (LineWaveVoiceView) findViewById(R.id.horvoiceview);
        recordStatusDescription = new String[]{
                "按住说话",
                "松手试听",
                "取消发送"
        };
        mainHandler = new Handler();
        recordAudioView.setRecordAudioListener(new RecordAudioView.IRecordAudioListener() {
            @Override
            public boolean onRecordPrepare() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    //检查录音权限
                    if (!PermissionUtil.hasSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                        String[] pp = new String[]{Manifest.permission.RECORD_AUDIO};
                        ActivityCompat.requestPermissions((Activity) context, pp, Cons.PERMISSIONS_REQUEST_AUDIO);
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String onRecordStart() {
                recordTotalTime = 0;
                if (null != timer && null != timerTask) {
                    timer.cancel();
                    timerTask.cancel();
                }
                if (null != mHorVoiceView) {
                    mHorVoiceView.stopRecord();
                }
                initTimer();
                timer.schedule(timerTask, 0, DEFAULT_MIN_TIME_UPDATE_TIME);
                String voiceFileName = getVoiceFileName(EMClient.getInstance().getCurrentUser());
                audioFileName = PathUtil.getInstance().getVoicePath() + "/" + voiceFileName;
                mHorVoiceView.startRecord();
                if (null != MediaService.bean && MediaService.getMediaPlayer().isPlaying()) {
                    if (!isPauseMusic) {
                        isPauseMusic = true;
                        context.sendBroadcast(new Intent(ACTION_PAUSE));
                    }
                }
                return audioFileName;
            }

            @Override
            public boolean onRecordStop(RecordAudioView.RecordSlideStop record) {
                if (isPauseMusic) {
                    isPauseMusic = false;
                    context.sendBroadcast(new Intent(ACTION_PAUSE));
                }
                if (recordTotalTime >= minRecordTime) {
                    timer.cancel();
                    if (record == RecordAudioView.RecordSlideStop.LEFT) {
                        layout_bottom_view.setVisibility(VISIBLE);
                        recordAudioView.setVisibility(GONE);
                        iv_is_player.setVisibility(GONE);
                        iv_is_delete.setVisibility(GONE);
                        iv_player.setVisibility(VISIBLE);
                        tv_play_time.setVisibility(VISIBLE);
                        mHorVoiceView.setVisibility(INVISIBLE);
                    } else if (record == RecordAudioView.RecordSlideStop.RIGHT) {
                        onRecordCancel();
                    } else {
                        sendVoiceMsg();
                    }
                } else {
                    onRecordCancel();
                }
                return false;
            }

            @Override
            public boolean onRecordCancel() {
                if (timer != null) {
                    timer.cancel();
                }
                updateCancelUi();
                return false;
            }

            //左右滑动
            @Override
            public void onSlideTop(RecordAudioView.RecordSlideStop record) {
                if (record == RecordAudioView.RecordSlideStop.LEFT) {

                } else if (record == RecordAudioView.RecordSlideStop.RIGHT) {

                } else {

                }
//        mHorVoiceView.setVisibility(View.INVISIBLE);
//        tvRecordTips.setVisibility(VISIBLE);
            }

            @Override
            public void onFingerPress() {
                if (null != mHorVoiceView && null != tvRecordTips) {
                    mHorVoiceView.setVisibility(View.VISIBLE);
                    tvRecordTips.setVisibility(View.INVISIBLE);
                }
            }
        });
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        iv_player.setOnClickListener(this);
        MediaPlayUtil.getInstance().setPlayOnCompleteListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!TextUtils.isEmpty(audioFileName)) {
                    int duration = AudioPlaybackManager.getDuration(audioFileName) / 1000;
                    tv_play_time.setText(formatTime(duration));
                }
                if (null != iv_player) {
                    iv_player.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ease_icon_trends_msg));
                }
                mHandler.removeCallbacks(runnable3);
                if (null != mHorVoiceView) {
                    mHorVoiceView.stopRecord();
                }
            }
        });
    }

    /**
     * 初始化计时器用来更新倒计时
     */
    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //每隔1000毫秒更新一次ui
                        recordTotalTime += 1000;
                        updateTimerUI();
                    }
                });
            }
        };
    }

    private void updateTimerUI() {
        if (recordTotalTime >= maxRecordTime) {
            recordAudioView.invokeStop();
        } else {
            String string = String.format(" 倒计时 %s ", StringUtil.formatRecordTime(recordTotalTime, maxRecordTime));
            mHorVoiceView.setText(string);
        }
    }

    private void updateCancelUi() {
        mHorVoiceView.setVisibility(View.INVISIBLE);
        tvRecordTips.setVisibility(View.VISIBLE);
        tvRecordTips.setText(recordStatusDescription[0]);
        mHorVoiceView.stopRecord();
        deleteTempFile();
    }

    private void deleteTempFile() {
        //取消录制后删除文件
        if (audioFileName != null) {
            File tempFile = new File(audioFileName);
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + ".amr";
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            bottomStopUi();
        } else if (i == R.id.tv_confirm) {
            bottomStopUi();
            sendVoiceMsg();
        } else if (i == R.id.iv_player) {
            if (!TextUtils.isEmpty(audioFileName)) {
                AudioEntity entity = new AudioEntity();
                entity.setUrl(audioFileName);
                int duration = AudioPlaybackManager.getDuration(audioFileName);
                if (duration <= 0) {
                    PaoPaoTips.showDefault(context, "无权限");
                    File tempFile = new File(audioFileName);
                    if (tempFile.exists()) {
                        tempFile.delete();
                        return;
                    }
                } else {
                    entity.setDuration(duration / 1000);
                    voiceTime = (int) entity.getDuration();
                    tv_play_time.setText(formatTime(voiceTime));
                    if (!MediaPlayUtil.getInstance().isPlaying()) {
                        MediaPlayUtil.getInstance().play(entity.getUrl());
                        mHandler.post(runnable3);
                        iv_player.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_pause_voice));
                    } else {
                        iv_player.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ease_icon_trends_msg));
                        MediaPlayUtil.getInstance().pause();
                        mHandler.removeCallbacks(runnable3);
                    }
                }
            }
        }
    }

    private void bottomStopUi() {
        tvRecordTips.setVisibility(VISIBLE);
        recordAudioView.setVisibility(VISIBLE);
        mHorVoiceView.setVisibility(INVISIBLE);
        iv_player.setVisibility(GONE);
        layout_bottom_view.setVisibility(GONE);
        iv_is_player.setVisibility(VISIBLE);
        iv_is_delete.setVisibility(VISIBLE);
        tv_play_time.setVisibility(GONE);
        MediaPlayUtil.getInstance().stop();
        mHandler.removeCallbacks(runnable3);
    }

    @Override
    public void onStart() {
        invalidate();
    }

    @Override
    public void onStop() {
        invalidate();
        tv_play_time.setVisibility(GONE);
    }

    @Override
    public void onComplete() {
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE) {
            if (null != mHorVoiceView) {
                mHorVoiceView.stopRecord();
            }
            mHandler.removeCallbacks(runnable3);
        }
    }

    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(runnable3, 1000);
            if (voiceTime > 0) {
                if (null != tv_play_time) {
                    tv_play_time.setText(formatTime(voiceTime));
                }
                voiceTime--;
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 倒计时处理
     */
    public static String formatTime(int t) {
        int time = Math.round(t);
        int timeMinute = time / 60;
        int timeSec2 = time % 60;
        String timeSecStr = timeSec2 >= 10 ? "" + timeSec2 : "0" + timeSec2;
        return timeMinute + ":" + timeSecStr;
    }

    public interface SendSpeakOnClickListener {
        void sendVoiceMsg(String fileName, int length);
    }

    private SendSpeakOnClickListener sendSpeakOnClickListener;

    public void setSendSpeakOnClickListener(SendSpeakOnClickListener sendSpeakOnClickListener) {
        this.sendSpeakOnClickListener = sendSpeakOnClickListener;
    }

    private void sendVoiceMsg() {
        if (null != sendSpeakOnClickListener && !TextUtils.isEmpty(audioFileName)) {
            int duration = AudioPlaybackManager.getDuration(audioFileName) / 1000;
            sendSpeakOnClickListener.sendVoiceMsg(audioFileName, Math.round(duration));
        }
    }

}

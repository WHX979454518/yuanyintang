package com.mxkj.yuanyintang.im.widget.audiorecord.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.mxkj.yuanyintang.im.widget.audiorecord.AudioRecordManager;


public class RecordAudioView extends Button {

    private static final String TAG = "RecordAudioView";

    public enum RecordSlideStop {
        LEFT,
        RIGHT,
        CENTER
    }

    private Context context;
    private IRecordAudioListener recordAudioListener;
    private AudioRecordManager audioRecordManager;
    private boolean isCanceled;
    private float downPointY;
    private float downPointX;
    private static final float DEFAULT_SLIDE_HEIGHT_CANCEL = 260;
    private boolean isRecording;


    public RecordAudioView(Context context) {
        super(context);
        initView(context);
    }

    public RecordAudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecordAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        audioRecordManager = AudioRecordManager.getInstance();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        super.onTouchEvent(event);
        if (recordAudioListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setSelected(true);
                    downPointY = event.getY();
                    downPointX = event.getX();
                    recordAudioListener.onFingerPress();
                    startRecordAudio();
                    break;
                case MotionEvent.ACTION_UP:
                    setSelected(false);
                    onFingerUp(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    onFingerMove(event);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isCanceled = true;
                    onFingerUp(event);
                    break;
                default:

                    break;
            }
        }
        return true;
    }

    /**
     * 手指抬起,可能是取消录制也有可能是录制成功
     */
    private void onFingerUp(MotionEvent event) {
        if (isRecording) {
            if (isCanceled) {
                isRecording = false;
                audioRecordManager.cancelRecord();
                recordAudioListener.onRecordCancel();
            } else {
                stopRecordAudio(event);
            }
        }
    }

    /**
     * 手指抬起,可能是取消录制也有可能是录制成功
     */
    private void onFingerUp() {
        if (isRecording) {
            if (isCanceled) {
                isRecording = false;
                audioRecordManager.cancelRecord();
                recordAudioListener.onRecordCancel();
            } else {
                stopRecordAudio();
            }
        }
    }

    private void onFingerMove(MotionEvent event) {
        float currentY = event.getY();
        float currentX = event.getX();
        if ((downPointX - currentX >= DEFAULT_SLIDE_HEIGHT_CANCEL)) {//左边
            recordAudioListener.onSlideTop(RecordSlideStop.LEFT);
        } else if (downPointX - currentX <= -DEFAULT_SLIDE_HEIGHT_CANCEL) {//右边
            recordAudioListener.onSlideTop(RecordSlideStop.RIGHT);
        } else {
            recordAudioListener.onFingerPress();
        }
//        isCanceled = checkCancel(currentY);
//        if (isCanceled) {
//            recordAudioListener.onSlideTop();
//        } else {
//            recordAudioListener.onFingerPress();
//        }
    }


    private boolean checkCancel(float currentY) {
        return downPointY - currentY >= DEFAULT_SLIDE_HEIGHT_CANCEL;
    }

    /**
     * 检查是否ready录制,如果已经ready则开始录制
     */
    private void startRecordAudio() throws RuntimeException {
        boolean isPrepare = recordAudioListener.onRecordPrepare();
        Log.d(TAG, "startRecordAudio: ---------->" + isPrepare);
        if (isPrepare) {
            String audioFileName = recordAudioListener.onRecordStart();
            Log.d(TAG, "startRecordAudio: " + audioFileName);
            //准备就绪开始录制
            try {
                audioRecordManager.init(audioFileName);
                audioRecordManager.startRecord();
                isRecording = true;
            } catch (Exception e) {
                this.recordAudioListener.onRecordCancel();
            }
        }
    }

    /**
     * 停止录音
     */
    private void stopRecordAudio() {
        if (isRecording) {
            isRecording = false;
            audioRecordManager.stopRecord();
            this.recordAudioListener.onRecordStop(RecordSlideStop.CENTER);
        }
    }

    /**
     * 停止录音
     */
    private void stopRecordAudio(MotionEvent event) throws RuntimeException {
        if (isRecording) {
            try {
                isRecording = false;
                audioRecordManager.stopRecord();
                float currentX = event.getX();
                if ((downPointX - currentX >= DEFAULT_SLIDE_HEIGHT_CANCEL)) {//左边
                    this.recordAudioListener.onRecordStop(RecordSlideStop.LEFT);
                } else if (downPointX - currentX <= -DEFAULT_SLIDE_HEIGHT_CANCEL) {//右边
                    this.recordAudioListener.onRecordStop(RecordSlideStop.RIGHT);
                } else {
                    this.recordAudioListener.onRecordStop(RecordSlideStop.CENTER);
                }
            } catch (Exception e) {
                this.recordAudioListener.onRecordCancel();
            }
        }
    }


    /**
     * 需要设置IRecordAudioStatus,来监听开始录音结束录音等操作,并对权限进行处理
     *
     * @param recordAudioListener
     */
    public void setRecordAudioListener(IRecordAudioListener recordAudioListener) {
        this.recordAudioListener = recordAudioListener;
    }

    public void invokeStop() {
        onFingerUp();
    }

    public interface IRecordAudioListener {
        boolean onRecordPrepare();

        String onRecordStart();

        boolean onRecordStop(RecordSlideStop record);

        boolean onRecordCancel();

        void onSlideTop(RecordSlideStop record);

        void onFingerPress();
    }
}

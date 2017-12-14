package com.upup8.ezaudioinputlib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upup8.ezaudioinputlib.manager.EzMediaRecorderManager;
import com.upup8.ezaudioinputlib.R;
import com.upup8.ezaudioinputlib.wiget.EzWaterRippleView;

/**
 * Audio 录音控件
 * EzAudioInputView
 * Created by renwoxing on 2017/12/11.
 */
public class EzAudioInputView extends LinearLayout {

    private static final String TAG = "EzAudioInputView";

    private IEzRecordAudioInputListener audioInputListener;
    private EzMediaRecorderManager mMediaRecorderManager;
    private boolean isCanceled;
    private float downPointY;
    private static final float DEFAULT_SLIDE_HEIGHT_CANCEL = 150;
    private boolean isRecording;
    private EzWaterRippleView mEzWaterRippleView;
    private TextView mEzAudioTitle;

    public EzAudioInputView(Context context) {
        this(context, null);
    }

    public EzAudioInputView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public EzAudioInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.ez_audio_input, this);
        mMediaRecorderManager = EzMediaRecorderManager.getInstance();
        mEzWaterRippleView = findViewById(R.id.ez_audio_wr_btn);
        mEzAudioTitle = findViewById(R.id.ez_tv_audio_title);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.i(TAG, "onTouchEvent");
        super.onTouchEvent(event);
        if (audioInputListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setSelected(true);
                    downPointY = event.getY();
                    audioInputListener.onFingerPress();
                    startRecordAudio();
                    break;
                case MotionEvent.ACTION_UP:
                    setSelected(false);
                    onFingerUp();
                    break;
                case MotionEvent.ACTION_MOVE:
                    onFingerMove(event);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isCanceled = true;
                    onFingerUp();
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
    private void onFingerUp() {
        if (isRecording) {
            if (isCanceled) {
                isRecording = false;
                mMediaRecorderManager.cancelRecord();
                audioInputListener.onRecordCancel();
                mEzWaterRippleView.stop();
                mEzAudioTitle.setText(R.string.ez_audio_btn_default_msg);
            } else {
                stopRecordAudio();
            }
        }
    }

    private void onFingerMove(MotionEvent event) {
        float currentY = event.getY();
        isCanceled = checkCancel(currentY);
        if (isCanceled) {
            mEzAudioTitle.setText(R.string.ez_audio_btn_slide_top_start_msg);
            audioInputListener.onSlideTop();
        } else {
            audioInputListener.onFingerPress();
        }
    }

    private boolean checkCancel(float currentY) {
        return downPointY - currentY >= DEFAULT_SLIDE_HEIGHT_CANCEL;
    }

    /**
     * 检查是否ready录制,如果已经ready则开始录制
     */
    private void startRecordAudio() throws RuntimeException {
        boolean isPrepare = audioInputListener.onRecordPrepare();
        if (isPrepare) {
            String audioFileName = audioInputListener.onRecordStart();
            Log.d(TAG, "startRecordAudio() has prepared.");
            //准备就绪开始录制
            try {
                mMediaRecorderManager.init(audioFileName);
                mMediaRecorderManager.startRecord();
                mEzWaterRippleView.start();
                mEzAudioTitle.setText(R.string.ez_audio_btn_slide_top_msg);
                isRecording = true;
            } catch (Exception e) {
                this.audioInputListener.onRecordCancel();
                mEzWaterRippleView.stop();
                mEzAudioTitle.setText(R.string.ez_audio_btn_default_msg);
            }
        }
    }

    /**
     * 停止录音
     */
    private void stopRecordAudio() throws RuntimeException {
        if (isRecording) {
            Log.d(TAG, "stopRecordAudio()");
            try {
                isRecording = false;
                mMediaRecorderManager.stopRecord();
                this.audioInputListener.onRecordStop();
            } catch (Exception e) {
                this.audioInputListener.onRecordCancel();
            } finally {
                mEzAudioTitle.setText(R.string.ez_audio_btn_default_msg);
                mEzWaterRippleView.stop();
            }
        }
    }

    /**
     * 需要设置IEzRecordAudioListener,来监听开始录音结束录音等操作,并对权限进行处理
     *
     * @param audioInputListener
     */
    public void setEzRecordAudioListener(IEzRecordAudioInputListener audioInputListener) {
        this.audioInputListener = audioInputListener;
    }

    public void invokeStop() {
        onFingerUp();
    }


    public interface IEzRecordAudioInputListener {

        boolean onRecordPrepare();

        String onRecordStart();

        boolean onRecordStop();

        boolean onRecordCancel();

        void onSlideTop();

        void onFingerPress();
    }


}

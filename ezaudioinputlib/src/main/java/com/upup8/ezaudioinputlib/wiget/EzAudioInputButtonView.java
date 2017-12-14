package com.upup8.ezaudioinputlib.wiget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.upup8.ezaudioinputlib.manager.EzMediaRecorderManager;

/**
 * EzAudioInputView
 * Created by renwoxing on 2017/12/10.
 */
@Deprecated
public class EzAudioInputButtonView extends AppCompatButton {

    private static final String TAG = "RecordAudioView";

    private Context mContext;
    private IEzRecordAudioListener recordAudioListener;
    private EzMediaRecorderManager mMediaRecorderManager;
    private boolean isCanceled;
    private float downPointY;
    private static final float DEFAULT_SLIDE_HEIGHT_CANCEL = 150;
    private boolean isRecording;


    public EzAudioInputButtonView(Context context) {
        super(context);
        initView(context);
    }

    public EzAudioInputButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EzAudioInputButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        mMediaRecorderManager = EzMediaRecorderManager.getInstance();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.i(TAG, "onTouchEvent");
        super.onTouchEvent(event);
        if (recordAudioListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setSelected(true);
                    downPointY = event.getY();
                    recordAudioListener.onFingerPress();
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
                recordAudioListener.onRecordCancel();
            } else {
                stopRecordAudio();
            }
        }
    }

    private void onFingerMove(MotionEvent event) {
        float currentY = event.getY();
        isCanceled = checkCancel(currentY);
        if (isCanceled) {
            recordAudioListener.onSlideTop();
        } else {
            recordAudioListener.onFingerPress();
        }
    }

    private boolean checkCancel(float currentY) {
        return downPointY - currentY >= DEFAULT_SLIDE_HEIGHT_CANCEL;
    }

    /**
     * 检查是否ready录制,如果已经ready则开始录制
     */
    private void startRecordAudio() throws RuntimeException {
        boolean isPrepare = recordAudioListener.onRecordPrepare();
        if (isPrepare) {
            String audioFileName = recordAudioListener.onRecordStart();
            Log.d(TAG, "startRecordAudio() has prepared.");
            //准备就绪开始录制
            try {
                mMediaRecorderManager.init(audioFileName);
                mMediaRecorderManager.startRecord();
                isRecording = true;
            } catch (Exception e) {
                this.recordAudioListener.onRecordCancel();
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
                this.recordAudioListener.onRecordStop();
            } catch (Exception e) {
                this.recordAudioListener.onRecordCancel();
            }
        }
    }

    /**
     * 需要设置IEzRecordAudioListener,来监听开始录音结束录音等操作,并对权限进行处理
     *
     * @param recordAudioListener
     */
    public void setEzRecordAudioListener(IEzRecordAudioListener recordAudioListener) {
        this.recordAudioListener = recordAudioListener;
    }

    public void invokeStop() {
        onFingerUp();
    }

    public interface IEzRecordAudioListener {
        boolean onRecordPrepare();

        String onRecordStart();

        boolean onRecordStop();

        boolean onRecordCancel();

        void onSlideTop();

        void onFingerPress();
    }
}

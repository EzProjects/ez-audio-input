package com.upup8.ezaudioinputlib.manager;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * MediaRecorder
 * EzMediaRecorderManager
 * Created by renwoxing on 2017/12/10.
 */
public class EzMediaRecorderManager {

    private static final String TAG = "EzMediaRecorderManager";
    private volatile static EzMediaRecorderManager INSTANCE;
    private MediaRecorder mediaRecorder;
    private String audioFileName;
    private int mMaxDuration = 60;
    private RecordStatus recordStatus = RecordStatus.STOP;

    public enum RecordStatus {
        READY,
        START,
        STOP
    }

    public EzMediaRecorderManager() {
    }

    public static EzMediaRecorderManager getInstance() {
        if (INSTANCE == null) {
            synchronized (EzMediaRecorderManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EzMediaRecorderManager();
                }
            }
        }
        return INSTANCE;
    }

    public void init(String audioFileName) {
        init(audioFileName, 60);
    }

    public void init(String audioFileName, int maxDuration) {
        this.audioFileName = audioFileName;
        this.mMaxDuration = maxDuration;
        recordStatus = RecordStatus.READY;
    }

    public void startRecord() {
        if (recordStatus == RecordStatus.READY) {
            Log.d(TAG, "startRecord():" + audioFileName);
            mediaRecorder = new MediaRecorder();
            // 设置录音的来源（从哪里录音）
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置录音的保存格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            // 设置录音的编码
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 设置录音文件的保存位置
            mediaRecorder.setOutputFile(audioFileName);
            // todo 设置录音最大时长 ，需要显示调用 stop 实现 接口
            // mediaRecorder.setMaxDuration(mMaxDuration);


            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();
            recordStatus = RecordStatus.START;
        } else {
            Log.e(TAG, "startRecord() invoke init first.");
        }
    }

    public void stopRecord() {
        if (recordStatus == RecordStatus.START) {
            Log.d(TAG, "startRecord()");
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            recordStatus = RecordStatus.STOP;
            audioFileName = null;
        } else {
            Log.e(TAG, "startRecord() invoke start first.");
        }
    }

    public void cancelRecord() {
        if (recordStatus == RecordStatus.START) {
            Log.d(TAG, "cancelRecord()");
            String file = audioFileName;
            stopRecord();
            File file1 = new File(file);
            file1.delete();
        } else {
            Log.e(TAG, "startRecord() invoke start first.");
        }
    }

    /**
     * 获得录音的音量，范围 0-32767, 归一化到0 ~ 1
     *
     * @return
     */
    public float getMaxAmplitude() {
        if (recordStatus == RecordStatus.START) {
            return mediaRecorder.getMaxAmplitude() * 1.0f / 32768;
        }
        return 0;
    }


}

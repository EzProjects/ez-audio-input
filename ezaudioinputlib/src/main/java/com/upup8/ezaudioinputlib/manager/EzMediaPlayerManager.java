package com.upup8.ezaudioinputlib.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * MediaPlayer 播放文件
 * <code>
 * //播放录音
 * EzMediaPlayerManager.playSound(getCurrentFilePath(), new MediaPlayer.OnCompletionListener() {
 *
 * @Override public void onCompletion(MediaPlayer mp) {
 * //当播放完了之后 to do something
 * }
 * });
 * </code>
 * EzMediaPlayerManager
 * Created by renwoxing on 2017/12/14.
 */
public class EzMediaPlayerManager {

    private static MediaPlayer mMediaPlayer;
    private static boolean isPause = false;

    public static void playSound(String filePath) {
        playSound(filePath, null);
    }

    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (onCompletionListener != null) {
                mMediaPlayer.setOnCompletionListener(onCompletionListener);
            }


            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 暂停播放
     */
    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPause = true;
        }
    }


    /**
     * 继续播放
     */
    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * 释放资源
     */
    public static void realese() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            //需要重置 内存清理关键
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            isPause = true;
        }
    }
}

package com.upup8.ezaudioinputlib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.upup8.ezaudioinputlib.R;

/**
 * EzAudioInputView
 * Created by renwoxing on 2017/12/11.
 */
public class EzAudioInputView extends LinearLayout {


    public EzAudioInputView(Context context) {
        super(context);
    }

    public EzAudioInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ez_audio_input,this);
    }

    public EzAudioInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }






}

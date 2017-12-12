package com.upup8.ezaudioinput;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.upup8.ezaudioinputlib.view.EzAudioInputView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //EzAudioInputView mEzAudioInputView;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private Context mContext;
    private EzAudioInputView mEzAudioInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEzAudioInputView = findViewById(R.id.ez_audio_input_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext != null && requestAudio(mContext)) {
                    //updateView(InputMode.VOICE);
//                    Snackbar.make(view, " 打开了语音权限", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                } else {
//                    Snackbar.make(view, "需要打开语音权限", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                }
                if (mEzAudioInputView.getVisibility() == View.GONE) {
                    mEzAudioInputView.setVisibility(View.VISIBLE);
                } else {
                    mEzAudioInputView.setVisibility(View.GONE);
                }
            }
        });

        WebView webView = findViewById(R.id.wv_home);
        webView.loadUrl("http://www.hiteacher.com.cn");

        //mEzAudioInputView = (EzAudioInputView) findViewById(R.id.ez_audio_input_btn);
        //mEzAudioInputView.setEzRecordAudioListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public boolean onRecordPrepare() {
        //检查录音权限
//        if(!PermissionUtil.hasSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
//            String[] pp = new String[]{
//                    Manifest.permission.RECORD_AUDIO
//            };
//            ActivityCompat.requestPermissions(this, pp, Cons.PERMISSIONS_REQUEST_AUDIO);
//            return false;
//        }
        return true;
    }

    @Override
    public String onRecordStart() {
        String audioFileName = getApplicationContext().getExternalCacheDir() + File.separator + createAudioName();
        Log.d(TAG, "-------- onRecordStart path: " + audioFileName);
        return audioFileName;
        //return null;
    }

    @Override
    public boolean onRecordStop() {
        Log.d(TAG, "onRecordStop: ");
        return false;
    }

    @Override
    public boolean onRecordCancel() {
        Log.d(TAG, "onRecordCancel: ");
        return false;
    }

    @Override
    public void onSlideTop() {
        Log.d(TAG, "onSlideTop: ");
    }

    @Override
    public void onFingerPress() {
        Log.d(TAG, "onFingerPress: ");
    }

    private String createAudioName() {
        long time = System.currentTimeMillis();
        String fileName = UUID.randomUUID().toString() + time + ".amr";
        return fileName;
    }
    */

    private boolean afterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean requestAudio(Context context) {
        if (afterM()) {
            int hasPermission = context.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

}

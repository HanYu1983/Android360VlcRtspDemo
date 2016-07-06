package com.example.hanyu.testdemo;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by hanyu on 2016/7/5.
 */
public class TestRTSP extends Activity {
    MediaPlayer m = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample2);
        try{
            //final String filepath = "https://storage.googleapis.com/particle-resources/tmp/videoplayback.mp4";
            //final String filepath = "rtsp://quicktime.tc.columbia.edu:554/users/lrf10/movies/sixties.mov";
            final String filepath = "rtsp://mpv.cdn3.bigCDN.com:554/bigCDN/definst/mp4:bigbuckbunnyiphone_400.mp4";

            SurfaceView surfaceview = (SurfaceView)findViewById(R.id.surfaceView);
            final SurfaceHolder surfaceholder = surfaceview.getHolder();
            surfaceholder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        //m.setAudioStreamType(AudioManager.STREAM_MUSIC);//設定型態
                        m.setDataSource(filepath);//路徑
                        m.setDisplay(surfaceholder);//用surfaceholder顯示畫面
                        m.prepareAsync();//要用非同步的準備，UI介面才不會停住
                    } catch (Exception e) {}


                    m.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer arg0) {
                            m.start();//載入完成時播放
                        }});
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });


        } catch (Exception e){
            e.printStackTrace();
        }


    }
}

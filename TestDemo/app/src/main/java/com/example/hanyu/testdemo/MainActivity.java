package com.example.hanyu.testdemo;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDVRLibrary;

import org.videolan.libvlc.LibVLC;

import java.io.IOException;

public class MainActivity extends Activity {

    private MediaPlayer mp = new MediaPlayer();

    private MD360Director director;
    private MDVRLibrary mVRLibrary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //int resID=getResources().getIdentifier("videoplayback.mp4", "raw", getPackageName());
            //String filePath = Environment.getExternalStorageDirectory()+"/yourfolderNAme/yopurfile.mp3";

            /*
            String filePath = "https://storage.googleapis.com/particle-resources/tmp/videoplayback.mp4";
            System.out.println(filePath);
            mp.setDataSource(filePath);
            mp.prepare();
            mp.start();
            */

            //String filePath = "rtsp://quicktime.tc.columbia.edu:554/users/lrf10/movies/sixties.mov";
            String filePath = "https://storage.googleapis.com/particle-resources/tmp/videoplayback.mp4";
            //String filePath = "rtsp://mpv.cdn3.bigCDN.com:554/bigCDN/definst/mp4:bigbuckbunnyiphone_400.mp4";

            mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    System.out.println("onBufferingUpdate:"+percent);
                }
            });
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    System.out.println("onPrepared:");
                    mp.start();
                }
            });
            mp.setDataSource(filePath);
            mp.prepareAsync();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        director = MD360Director.builder().setNearScale(1f).build();

        mVRLibrary = MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_TOUCH)
                .directorFactory(new DirectorFactory())
                .pinchEnabled(true)
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                                ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                        Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                })
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mp.setSurface(surface);
                    }
                })
                .build(R.id.surface_view);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mVRLibrary.handleTouchEvent(event) || super.onTouchEvent(event);
    }

    private class DirectorFactory extends MD360DirectorFactory{
        @Override
        public MD360Director createDirector(int index) {
            return director;
        }
    }
}

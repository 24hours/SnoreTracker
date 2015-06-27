package com.labtf.snoretracker;

/**
 * Created by leeyeongkhang on 12/15/14.
 */
import android.app.Service;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.AudioRecord;
import android.media.AudioFormat;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SnoreAnalyse implements MediaPlayer.OnPreparedListener{
    private String FILE_PATH;
    private MediaPlayer player;
    private boolean prepared, playing = false;
    public final String TAG = "SnoreAnalyse";


    public SnoreAnalyse(String file_path){
        FILE_PATH = file_path;
        player = new MediaPlayer();
        try {
            player.setDataSource(file_path);
            player.setOnPreparedListener(this);
            player.prepareAsync();
        }catch(IOException e){
            Log.e(TAG, "Failed to open " + file_path + " because : " + e.getMessage());
        }

//        File f = new File(Environment.getExternalStorageDirectory() + "/SnoreTracker");
//        if(!f.isDirectory()) {
//            boolean success = false;
//            success = f.mkdir();
//        }
//
//        f = null;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        prepared = true;
        if (playing == true) {
            player.start();
        }
    }

    public void Play(){
        if (prepared == true){
            player.start();
        }
        playing = true;
    }

    public void Stop(){
        if( player.isPlaying() == false){
            player.stop();
        }
    }


    public boolean IsPlaying(){
        return playing;
    }

    public void setOnCompletionListener (MediaPlayer.OnCompletionListener listener){
        player.setOnCompletionListener(listener);
    }
}

package com.labtf.snoretracker;

/**
 * Created by leeyeongkhang on 12/15/14.
 * Play and analyse the recording
 */

import android.media.MediaPlayer;
import android.util.Log;
import java.io.IOException;

public class SnoreAnalyse {
    String FILE_PATH;
    private MediaPlayer player;
    private boolean prepared, playing = false;
    public final String TAG = "SnoreAnalyse";
    private OnSnoreAnalyseChangeListener snalistener;

    public SnoreAnalyse(String file_path, OnSnoreAnalyseChangeListener l){
        FILE_PATH = file_path;
        snalistener = l;
        player = new MediaPlayer();
        try {
            player.setDataSource(file_path);
            player.setOnPreparedListener(new preparedListener());
            player.setOnCompletionListener(new completionListener());
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

    public void Play(){
        if (prepared){
            player.start();
        }
        playing = true;
    }

    @SuppressWarnings("unused")
    public void Stop(){
        if(!player.isPlaying()){
            player.stop();
        }
    }

    public void Pause(){
        if( player.isPlaying()){
            player.pause();
        }
        playing = false;
    }
    public boolean IsPlaying(){
        return playing;
    }

    public int GetDuration(){
        return player.getDuration();
    }

    private class completionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mp) {
            if(snalistener != null){
                snalistener.onCompletePlaying(SnoreAnalyse.this);
            }
        }
    }

    private class preparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            prepared = true;
            if (playing) {
                player.start();
            }

            if(snalistener != null){
                snalistener.onPrepared(SnoreAnalyse.this);
            }
        }
    }

    public interface OnSnoreAnalyseChangeListener {
        void onPrepared(SnoreAnalyse sna);
        void onCompletePlaying(SnoreAnalyse sna);
    }
}

package com.labtf.snoretracker;

import android.app.Service;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import java.io.IOException;
import android.util.Log;

/**
 * Created by leeyeongkhang on 6/25/15.
 */
public class SoundRecorder {
    private MediaRecorder rec;
    private String TAG = "SoundRecorder";
    private boolean recording = false;

    public SoundRecorder(String filepath){
        rec = new MediaRecorder();
        rec.setAudioSource(MediaRecorder.AudioSource.MIC);
        rec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        rec.setOutputFile(filepath);
        rec.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
    }

    public void Record() throws IOException{
        try {
            rec.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed due to ".concat(e.getMessage()));
            throw e;
        }

        rec.start();
        recording = true;
    }

    public void Stop(){
        rec.stop();
        rec.release();
    }

    public boolean isRecording(){
        return recording;
    }
}

package com.labtf.snoretracker;

/**
 * Created by leeyeongkhang on 12/15/14.
 */
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

public class SnoreAnalyse {
    private String path;
    private String filename;
    private boolean isRecording;
    private Thread recordingThread = null;
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_DEFAULT;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_DEFAULT;
    private AudioRecord recorder;
    private MediaRecorder encodedRecorder;
    private int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    private int BytesPerElement = 2; // 2 bytes in 16bit format

    public final String TAG = "SnoreAnalyse";

    public SnoreAnalyse(){
//        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
//                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
//                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        File f = new File(Environment.getExternalStorageDirectory() + "/SnoreTracker");
        if(!f.isDirectory()) {
            boolean success = false;
            success = f.mkdir();
        }

        f = null;

        encodedRecorder = new MediaRecorder();
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        encodedRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        encodedRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        encodedRecorder.setOutputFile(path + "/SnoreTracker/" + "tracker.3gp");
        encodedRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        filename = "/snoretracker2.pcm";
        isRecording = false;
    }

    public String GetFilePath(){
        return path + "/SnoreTracker/" + "tracker.3gp";
    }

    public void StartRecording(){
        //recorder.startRecording();
        isRecording = true;
//        recordingThread = new Thread(new Runnable() {
//            public void run() {
//                //writeAudioDataToFile();
//            }
//        }, "AudioRecorder Thread");
//        recordingThread.start();


        try {
            // This thing will fail, in the rare case where SDCard is mounted as read-only
            // Do not trust any check prior, they are not guarantee even by android itself.
            encodedRecorder.prepare();

        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
            Log.e(TAG, e.toString());
        }

        encodedRecorder.start();

        return;
    }

    public void stopRecording() {
        // stops the recording activity
//        if (recorder != null ) {
//            isRecording = false;
//            recorder.stop();
//            recorder.release();
//            recorder = null;
//            recordingThread = null;
//        }

        encodedRecorder.stop();
        encodedRecorder.reset();
        encodedRecorder.release();
    }

    private byte[] short2byte(short[] sData) {
        int shortArraySize = sData.length;
        byte[] bytes = new byte[shortArraySize * 2];
        for (int i = 0; i < shortArraySize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;

        try {
            os = new FileOutputStream(path+filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BufferElements2Rec);
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

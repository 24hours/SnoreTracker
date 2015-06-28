package com.labtf.snoretracker;
//http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// TODO : Play the sound as seeked by user
// TODO : Add "magnified" area to allow user easily see highlighted area
// TODO : analyse snore

public class MainActivity extends ActionBarActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    static String TAG = "MainActivity";
    static String FOLDER = "SnoreTracker";
    static String extension = "mp4";

    private String FILE_PATH;
    private SnoreAnalyse sna;
    private SoundRecorder sdr;
    private static CircularSeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), FOLDER);
        if(!file.exists() || !file.isDirectory()){
            file.mkdir();
        }

        FILE_PATH = file.getAbsolutePath();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        Log.v(TAG, "Paused State");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new RecorderFragment();
                case 1:
                    return FileFragment.newInstance();
                default:
                    return StatsFragment.newInstance("yo");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public static class RecorderFragment extends Fragment {
        public RecorderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            seekbar = (CircularSeekBar) rootView.findViewById(R.id.seekbar);
            seekbar.setOnSeekBarChangeListener(new CircleSeekBarListener());
            seekbar.setStartAngle(-90);
            seekbar.setProgress(0);
            seekbar.setProgress(50);
            seekbar.highlight(0, 6000, Color.BLUE, 1);
            seekbar.highlight(500, 3000, Color.RED, 2);

            return rootView;
        }

        // event handling
        public class CircleSeekBarListener implements CircularSeekBar.OnCircularSeekBarChangeListener {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                //TextView t = (TextView) ((MainActivity)getActivity()).findViewById(R.id.progress);
                //t.setText(Integer.toString(progress));
                Log.v(TAG, "seeking");
            }

            public void onStopTrackingTouch(CircularSeekBar seekBar) {
            }

            public void onStartTrackingTouch(CircularSeekBar seekBar) {
            }
        }
    }
    private void updateUI(){
        seekbar.setProgress(seekbar.getProgress() + 1);
    }

    public void onPlayClicked(View view){
        String filename = FILE_PATH + "/" + generateFileName() + "." + extension;

        if(sdr != null && sdr.isRecording()){
            sdr.Stop();
            EventHandling(Event.Recording_Stop);
        } else {
            sdr = new SoundRecorder(filename);
            try{
                sdr.Record();
            } catch (IOException e){
                Toast toast = Toast.makeText(getApplicationContext(),
                                            "Failed to Get Microphone",
                                            Toast.LENGTH_LONG);
                toast.show();
            }
            EventHandling(Event.Recording_Start);
        }
    }

    public void onListenClicked(View view){
        if(sna.IsPlaying()) {
            sna.Pause();
            EventHandling(Event.Stop_record);
        } else {
            sna.Play();
            EventHandling(Event.Play_record);
        }
    }

    public String generateFileName(){
        if( isExternalStorageWritable()){
            // if user tap record twice, the original file it will get override.
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm", Locale.US);
            return dateFormat.format(new Date());
        } else{
            return "";
        }
    }

    private void EventHandling(Event e){
        Button playBtn = (Button)findViewById(R.id.play);
        Button ListenBtn = (Button)findViewById(R.id.listen);
        switch(e){
            case Play_record:
                ListenBtn.setText("Pause");
                break;
            case Stop_record:
                ListenBtn.setText("Listen");
                break;
            case Recording_Start:
                playBtn.setText("Stop");
                break;
            case Recording_Stop:
                playBtn.setText("Record");
                sdr = null;
                break;
        }
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void  LoadFile(String filename){
        sna = new SnoreAnalyse(FILE_PATH + "/" +filename);
        sna.Play();
        sna.setOnCompletionListener(new SnaCompletion());

        EventHandling(Event.Play_record);
    }

    public class SnaCompletion implements MediaPlayer.OnCompletionListener{
        public void onCompletion(MediaPlayer mp){
            runOnUiThread(new Runnable() {
                @Override
                public void run() { EventHandling(Event.Stop_record);  }
            });
        }
    }

    public enum Event {
        Play_record,
        Stop_record,
        Recording_Start,
        Recording_Stop,
    }
}

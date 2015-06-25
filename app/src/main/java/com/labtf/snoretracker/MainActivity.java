package com.labtf.snoretracker;
//http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

// TODO : Coloring the Circular bar [highlight] on designated area
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
    private static String TAG = "MainActivity";
    private SnoreAnalyse sna = new SnoreAnalyse();
    private SoundRecorder sdr;
    private static CircularSeekBar seekbar;
    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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
                    return FileFragment.newInstance("yo","ho");
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
        public RecorderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            seekbar = (CircularSeekBar)rootView.findViewById(R.id.seekbar);

            //seekbar.setOnSeekBarChangeListener(new CircleSeekBarListener());
            seekbar.setStartAngle(0);
            seekbar.highlight(0, 6000, Color.BLUE, 1);
            seekbar.highlight(500, 3000, Color.RED, 2);
            return rootView;
        }
    }

    // event handling

    public class CircleSeekBarListener implements CircularSeekBar.OnCircularSeekBarChangeListener {
        @Override
        public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
            TextView t = (TextView)findViewById(R.id.progress);
            t.setText(Integer.toString(progress));
        }

        public void onStopTrackingTouch(CircularSeekBar seekBar){}

        public void onStartTrackingTouch(CircularSeekBar seekBar){}
    }

    private void updateUI(){
        seekbar.setProgress(seekbar.getProgress() + 1);
    }

    public void onPlayClicked(View view){
        if(sdr != null || sdr.isRecording() == true){
            StopRecording();
        } else {
            sdr = new SoundRecorder();
            sdr.Record();
        }

        isRecording = !isRecording;
        Button playBtn = (Button)view.findViewById(R.id.play);
        if(isRecording){
            playBtn.setText("Stop");
        } else {
            playBtn.setText("Record");
        }
    }

    public void onListenClicked(View view){
        if(mPlayer.isPlaying()){
            pause(false);
        } else {
            play(sna.GetFilePath());
        }

        Button playBtn = (Button)view.findViewById(R.id.listen);

        if(mPlayer.isPlaying()){
            playBtn.setText("Pause");
        } else {
            playBtn.setText("Listen");
        }
    }
}

package com.labtf.snoretracker;


import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileFragment extends Fragment {
    private String TAG = "FILE";
    private static String FOLDER = "SnoreTracker";
    private static String FILE_PATH;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileFragment newInstance() {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_file, container, false);
        // Inflate the layout for this fragment
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), FOLDER);
        FILE_PATH = file.getAbsolutePath();

        FileObserver fobsv = new FileObserver(FILE_PATH, FileObserver.CREATE | FileObserver.DELETE) {
            @Override
            public void onEvent(int event, String path) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() { refresh();  }
                });
            };
        };
        fobsv.startWatching();
        ls(rootView);
        return rootView;
    }

    private void refresh(){
//        ListView l = (ListView)getView().findViewById(R.id.listView);
//        l.setAdapter( new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1));
        ls(getView());
    }

    private void ls(View v){
        ListView l = (ListView)v.findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<String>();

        File[] entries = new File(FILE_PATH).listFiles();

        for ( File entry : entries ) {
            if (entry.isDirectory() == false) {
                list.add(entry.getName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, list);
        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                ((MainActivity)getActivity()).LoadFile(item);

                ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
                mViewPager.setCurrentItem(0);
            }

        });
    }

}

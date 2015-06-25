package com.labtf.snoretracker;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by leeyeongkhang on 12/22/14.
 */
public class FileStore {
    private String folderPath;
    private String folderName;
    public static final int EXTERNAL = 1;
    public static final int INTERNAL = 2;
    private int storageType;
    // you are idiot if you change this
    public File fd;

    public FileStore(Context ctx, int storageType){
        this.storageType = storageType;

        switch(this.storageType){
            case INTERNAL:
                folderPath = ctx.getFilesDir().getAbsolutePath();
                break;
            default: // EXTERNAL
//                SetFolder("SnoreTracker");
//                folderPath = ctx.getFilesDir().getAbsolutePath();
//                File file = new File(Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_PICTURES), albumName);
//                if (!file.mkdirs()) {
//                    Log.e(LOG_TAG, "Directory not created");
//                }
        }
        return;
    }

    private FileStore(){}

    public boolean SetFolder(String name){
        folderName = name;
        return false;
    }

    public static boolean CanAccess(String folderPath){
        File f = new File(folderPath);

        return f.canRead() && f.canWrite();
    }

    public boolean CanAccess(){
        File f = new File(folderPath);

        return f.canRead() && f.canWrite();
    }

    public static boolean ExternalExist(){
        FileStore f = new FileStore();
        return f.isExternalStorageWritable() && f.isExternalStorageWritable();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}


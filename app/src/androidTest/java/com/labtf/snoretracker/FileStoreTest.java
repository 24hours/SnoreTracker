package com.labtf.snoretracker;

import android.app.Application;
import android.os.Environment;
import android.test.ApplicationTestCase;

/**
 * Created by leeyeongkhang on 12/22/14.
 */
public class FileStoreTest extends ApplicationTestCase<Application> {
    public FileStoreTest() {
        super(Application.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testDummy() {
        assertNotNull("All is well 1", 1);
    }

    public void testPrecondition(){
        FileStore f;
        f = new FileStore(getContext(), FileStore.INTERNAL);
        assertNotNull("File store should be created", f);
        f = null;
        assertNull("Impossible case", f);
        f = new FileStore(getContext(), FileStore.EXTERNAL);
        assertNotNull("File store should be created", f);
    }

    public void testWritable(){
        assertEquals("Can't access root folder", false, FileStore.CanAccess("/"));
        assertEquals("Read/Write to internal folder", true, FileStore.CanAccess(getContext().getFilesDir().getAbsolutePath()));
        assertEquals("Read/write to external storage", true, FileStore.CanAccess(Environment.getExternalStorageDirectory().getAbsolutePath()));
    }
}

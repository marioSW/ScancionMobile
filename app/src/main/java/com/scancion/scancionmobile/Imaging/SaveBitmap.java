package com.scancion.scancionmobile.Imaging;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 *
 * Created by Isuri Chandrathilaka on 9/7/2016.
 */
public class SaveBitmap {

    private String fname;
    private File myDir;

    public void savebitmap(String TAG, Bitmap bm){
        String root = Environment.getExternalStorageDirectory().toString();
        myDir = new File(root + "/req_images");
        if(!myDir.isDirectory()) {//boolean directoryRes=myDir.mkdirs();
            Log.d(TAG, "Creating new Directory req_images");
            if(!myDir.mkdir()){
                Log.d(TAG, "Directory req_images was not created");
            }

        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        Log.i(TAG, "" + file.getName());
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e(TAG,"Exception with file output stream");
            e.printStackTrace();
        }

    }

    /**
     * Get the Directory that was created and where the image is stored
     * @return
     */
    public File getMyDir() {
        return myDir;
    }

    /**
     * Get the name of the file that was created (the image files name)
     * @return String name
     */
    public String getFname() {
        return fname;
    }

}

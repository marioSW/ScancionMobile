package com.scancion.scancionmobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.scancion.scancionmobile.Imaging.CVProcessing;
import com.scancion.scancionmobile.Imaging.Processing;
import com.scancion.scancionmobile.Imaging.SaveBitmap;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "DBG_" + MainActivity.class.getName();
    private final int requestCode = 20;
    Uri uriSavedImage;

    private File fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent= new Intent(view.getContext(),ShowCameraActivity2.class);
                //startActivity(intent);

                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/req_images");
                if(!myDir.isDirectory()) {//boolean directoryRes=myDir.mkdirs();
                    Log.d(TAG, "Creating new Directory req_images");
                    if(!myDir.mkdir()){
                        Log.d(TAG, "Directory req_images was not created");
                    }

                }
                /*String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                folder specofics
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), timeStamp);
                boolean makeOrCreateDir=imagesFolder.mkdirs();

                if(makeOrCreateDir){
                File image = new File(imagesFolder, "PHOTO_" + timeStamp + ".jpg");
                System.out.println(image);
                uriSavedImage = Uri.fromFile(image); */
                startActivityForResult(photoCaptureIntent, requestCode);
                //Snackbar.make(view, "Image recieved    "+fileName.getName() , Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                //System.out.println(uriSavedImage);
            }
        });
    }

    public SaveBitmap save;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Save initialized listner");

        if(this.requestCode == requestCode && resultCode == RESULT_OK) {

            Log.d(TAG, "Save initialized RESULT_OK");

            Bitmap bitmap,bmgray1,bmgray2,
                    bmgray3,bmthresh1,bmthresh2,
                    bmthresh3,bmthresh4,bmthresh5,
                    bmthresh6,bmthresh7 = null;
            //Uri imageUri = data.getData();

                bitmap = (Bitmap) data.getExtras().get("data");
                //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                //SaveBitmap.savebitmap(TAG, bitmap);
                //Graysccale functions
                //bmgray2 = Processing.toGrayscaleTwo(bitmap);
                //SaveBitmap.savebitmap(TAG, bmgray2);

                save= new SaveBitmap();
                bmthresh1 =Processing.toGrayscaleTwo(bitmap);
                //bmthresh2=CVProcessing.globalThresholding(bitmap); CV PROCCESSING HAS AN ERROR
                save.savebitmap(TAG, bmthresh1);
                Intent intent = new Intent(this.getApplicationContext(),DisplayClass.class);
                intent.putExtra("IMAGE_LOCATION_NAME",save.getFname());
                intent.putExtra("IMAGE_LOCATION_DIRECTORY",save.getMyDir());
                startActivity(intent);


        }
        else if (resultCode == RESULT_CANCELED) {
            // User cancelled the image capture
            Log.d(TAG, "Save initialized RESULT_CANCELED");
        }
        else {
            // Image capture failed, advise user
            Log.d(TAG, "Save initialized CAPTURE FAILED");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get the file name from the saved file
     * @return File file name
     */
    public File getFileName() {
        return fileName;
    }

    /**
     * Set the fileName
     * @param fileName
     */
    public void setFileName(File fileName) {
        this.fileName = fileName;
    }
}

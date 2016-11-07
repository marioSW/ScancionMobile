package com.scancion.scancionmobile;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ImageSegmentationActivity extends AppCompatActivity {

    private final String TAG="DBG_" + ImageSegmentationActivity.class.getName();

    String imageName;
    TextView out;
    ImageView iv;
    String root = Environment.getExternalStorageDirectory().toString();
    String datapath = "";

    File myDir = new File(root + "/req_images");
    private TessBaseAPI mTess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        imageName=i.getStringExtra("IMAGE_LOCATION_NAME");

        setContentView(R.layout.activity_image_segmentation);

        out = (TextView) findViewById(R.id.out_txt);
        iv = (ImageView) findViewById(R.id.imageView);
        //if(imageName != null){}
        //imView = (ImageView) findViewById(R.id.view_edited_image);
        File imgFile= new File(myDir,imageName);

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Intent intent = new Intent(this.getApplicationContext(), DisplayClass.class);
        if(myBitmap != null) {
            //Utils.bitmapToMat(myBitmap, mRgba);
            iv.setImageBitmap(myBitmap);
            datapath= getFilesDir()+"/tesseract/";
            checkFile(new File(datapath+"tessdata/"));


            String lang = "eng";
            Log.d(TAG,"Initializing Teseract API");
            /*mTess = new TessBaseAPI();
            mTess.init(datapath, lang);

            mTess.end();*/
            /*mTess.setImage(myBitmap);
            String OCRresult = mTess.getUTF8Text();
            out.setText(OCRresult);*/

        }
        else{
            //out.setText("MYBITMAP IS NULL!!!!!");
            Log.e(TAG, "MYBITMAP IS NULL!!!!!");


        }

        //startActivity(intent);

    }

    /**
     *
     * @throws IOException
     */
    private void copyFile(){

            //location we want the file to be at
            String filepath = datapath + "/tessdata/";

            //get access to AssetManager
            AssetManager assetManager = getAssets();

        try {
            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();
        } catch(Exception ex){Log.v(TAG,"And IO Exception took place");}

    }

    /**
     *
     * @param dir
     * @throws IOException
     */
    private void checkFile(File dir){
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFile();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);

            if (datafile.exists()) {
                copyFile();
            }
        }
    }

    public void processImage(Bitmap image){
        mTess.setImage(image);
        String OCRresult = mTess.getUTF8Text();
        out.setText(OCRresult);
    }
    /*
    private Scalar CONTOUR_COLOR;
    public Mat detectLanguage(){
        CONTOUR_COLOR = new Scalar(255);
        MatOfKeyPoint keypoint = new MatOfKeyPoint();
        List<KeyPoint> listpoint = new ArrayList<KeyPoint>();
        KeyPoint kpoint = new KeyPoint();
        Mat mask = Mat.zeros(mRgba.size(), CvType.CV_8UC1);
        int rectanx1;
        int rectany1;
        int rectanx2;
        int rectany2;

        //
        Scalar zeos = new Scalar(0, 0, 0);
        List<MatOfPoint> contour1 = new ArrayList<MatOfPoint>();
        List<MatOfPoint> contour2 = new ArrayList<MatOfPoint>();
        Mat kernel = new Mat(1, 50, CvType.CV_8UC1, Scalar.all(255));
        Mat morbyte = new Mat();
        Mat hierarchy = new Mat();

        Rect rectan2 = new Rect();//
        Rect rectan3 = new Rect();//
        int imgsize = mRgba.height() * mRgba.width();

            FeatureDetector detector = FeatureDetector
                    .create(FeatureDetector.MSER);
            detector.detect(mGray, keypoint);
            listpoint = keypoint.toList();
            //
            for (int ind = 0; ind < listpoint.size(); ind++) {
                kpoint = listpoint.get(ind);
                rectanx1 = (int) (kpoint.pt.x - 0.5 * kpoint.size);
                rectany1 = (int) (kpoint.pt.y - 0.5 * kpoint.size);
                // rectanx2 = (int) (kpoint.pt.x + 0.5 * kpoint.size);
                // rectany2 = (int) (kpoint.pt.y + 0.5 * kpoint.size);
                rectanx2 = (int) (kpoint.size);
                rectany2 = (int) (kpoint.size);
                if (rectanx1 <= 0)
                    rectanx1 = 1;
                if (rectany1 <= 0)
                    rectany1 = 1;
                if ((rectanx1 + rectanx2) > mGray.width())
                    rectanx2 = mGray.width() - rectanx1;
                if ((rectany1 + rectany2) > mGray.height())
                    rectany2 = mGray.height() - rectany1;
                Rect rectant = new Rect(rectanx1, rectany1, rectanx2, rectany2);
                Mat roi = new Mat(mask, rectant);
                roi.setTo(CONTOUR_COLOR);

            }

            Imgproc.morphologyEx(mask, morbyte, Imgproc.MORPH_DILATE, kernel);
            Imgproc.findContours(morbyte, contour2, hierarchy,
                    Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
            for (int ind = 0; ind < contour2.size(); ind++) {
                rectan3 = Imgproc.boundingRect(contour2.get(ind));
                if (rectan3.area() > 0.5 * imgsize || rectan3.area() < 100
                        || rectan3.width / rectan3.height < 2) {
                    Mat roi = new Mat(morbyte, rectan3);
                    roi.setTo(zeos);

                } else
                    Imgproc.rectangle(mRgba, rectan3.br(), rectan3.tl(),
                            CONTOUR_COLOR);
            }
        return mRgba;
    }
    */
}

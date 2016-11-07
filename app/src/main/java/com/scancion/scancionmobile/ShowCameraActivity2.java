package com.scancion.scancionmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static com.scancion.scancionmobile.R.layout.show_camera_2;

public class ShowCameraActivity2 extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    // Used for logging success or failure messages
    private static final String TAG = "OCVSample::Activity";
    private Mat mGray, mRgba;
    // These variables are used (at the moment) to fix camera orientation from 270degree to 0degre
    Mat mRgbaF;
    Mat mRgbaT;
    Mat mByte;
    private JavaCameraView mOpenCvCameraView;
    private Mat mIntermediateMat;
    Button button;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this){
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_camera_2);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        mOpenCvCameraView = (JavaCameraView) this.findViewById(R.id.show_camera_2);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        button = (Button) findViewById(R.id.show_camera_2_button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DisplayClass.class);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onCameraViewStarted(int width, int height) {

        /*mIntermediateMat = new Mat();
        mGrey = new Mat(height, width, CvType.CV_8UC4);
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);*/

        mGray = new Mat(height, width, CvType.CV_8UC4);
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mByte = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);


    }

    @Override
    public void onCameraViewStopped()  {
        mRgba.release();
    }

    boolean isProcess=true;
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        int res = Resources.getSystem().getConfiguration().orientation;
        //Rotate mRgba 90 degrees
        if(res == 1) {
            Core.transpose(mRgba, mRgbaT);
            Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0, 0, 0);
            Core.flip(mRgbaF, mRgba, 1);
            mRgba = inputFrame.rgba();
            mGray = inputFrame.gray();

        } else {
            mRgba = inputFrame.rgba();
            mGray = inputFrame.gray();
        }
        //mRgba = detectLanguage();
        return mRgba;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);

        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        isProcess=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    private Scalar CONTOUR_COLOR;
    public Mat detectLanguage(){
        CONTOUR_COLOR = new Scalar(255);
        MatOfKeyPoint keypoint = new MatOfKeyPoint();
        List<KeyPoint> listpoint = new ArrayList<KeyPoint>();
        KeyPoint kpoint = new KeyPoint();
        Mat mask = Mat.zeros(mGray.size(), CvType.CV_8UC1);
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

        if (isProcess) {
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
        return mRgba;
    }

}

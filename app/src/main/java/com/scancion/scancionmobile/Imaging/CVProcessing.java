package com.scancion.scancionmobile.Imaging;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


/**
 * Created by Isuri Chandrathilaka on 9/8/2016.
 */
public class CVProcessing {
    static final String TAG = "DBG_" + CVProcessing.class.getName();

    public static Bitmap grayscaling(Bitmap bm1){
        long startTime = System.nanoTime();

        Bitmap bm = Bitmap.createBitmap(bm1);
        Mat tmp = new Mat(bm.getWidth(), bm.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bm, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(tmp, bm);
        long stopTime = System.nanoTime();
        System.out.println("Time Grayscale cv = "+ (stopTime - startTime));
        return bm;
    }
    public static Bitmap globalThresholding(Bitmap bm1){
        long startTime = System.nanoTime();

        Bitmap bm = Bitmap.createBitmap(bm1);
        Mat tmp = new Mat(bm.getWidth(), bm.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bm, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(tmp,tmp,127,255, Imgproc.THRESH_BINARY);
        Utils.matToBitmap(tmp, bm);
        long stopTime = System.nanoTime();
        System.out.println("Time globalthresh cv = "+ (stopTime - startTime));
        return bm;
    }
    public static Bitmap otsuThresholding(Bitmap bm1){
        long startTime = System.nanoTime();

        Bitmap bm = Bitmap.createBitmap(bm1);
        Mat tmp = new Mat(bm.getWidth(), bm.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bm, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(tmp, tmp, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        Utils.matToBitmap(tmp, bm);
        long stopTime = System.nanoTime();
        System.out.println("Time otsu cv = "+ (stopTime - startTime));
        return bm;
    }
    public static Bitmap adaptiveThresholding(Bitmap bm1){
        long startTime = System.nanoTime();

        Bitmap bm = Bitmap.createBitmap(bm1);
        Mat tmp = new Mat(bm.getWidth(), bm.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bm, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.adaptiveThreshold(tmp, tmp, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);
        Utils.matToBitmap(tmp, bm);
        long stopTime = System.nanoTime();
        System.out.println("Time adaptGause cv = "+ (stopTime - startTime));
        return bm;
    }
    public static Bitmap adaptiveThresholdingMod(Bitmap bm1){
        long startTime = System.nanoTime();

        Bitmap bm = Bitmap.createBitmap(bm1);
        Mat tmp = new Mat(bm.getWidth(), bm.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bm, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.adaptiveThreshold(tmp, tmp, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 999, 40);
        Utils.matToBitmap(tmp, bm);
        long stopTime = System.nanoTime();
        System.out.println("Time adaptmean cv = "+ (stopTime - startTime));
        return bm;
    }

}

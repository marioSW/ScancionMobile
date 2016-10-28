package com.scancion.scancionmobile.Imaging;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.scancion.scancionmobile.MainActivity;

/**
 * Created by Isuri Chandrathilaka on 6/14/2016.
 */
public class Processing {
    static final String TAG = "DBG_" + MainActivity.class.getName();


    public static Bitmap createContrast(Bitmap src, double value) {
    // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;
        //Set the double value to 50 on method call. For Example createContrast(Bitmap src, 50)
    }


    //GrayScaling Algorithms

    public static Bitmap toGrayscaleOne(Bitmap bmpOriginal){
        long startTime = System.nanoTime();
        Log.d(TAG, "inside to grayscale");
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        long stopTime = System.nanoTime();
        System.out.println("Time Grayscale 1 = "+ (stopTime - startTime));
        return bmpGrayscale;
    }


    public static Bitmap toGrayscaleTwo(Bitmap src) {

        long startTime = System.nanoTime();
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        long stopTime = System.nanoTime();
        System.out.println("Time Grayscale 2 = "+ (stopTime - startTime));
        return bmOut;
        //Execution time slower
        //Tends to get App stuck
    }

    //Unprocessed - Uses canvas functions
    public void toGrayscaleThree(Bitmap original){
        long startTime = System.nanoTime();
        float oneThird = 1/3f;
        float[] mat = new float[]{
                oneThird, oneThird, oneThird, 0, 0,
                oneThird, oneThird, oneThird, 0, 0,
                oneThird, oneThird, oneThird, 0, 0,
                0, 0, 0, 1, 0,};

        int width, height;
        height = original.getHeight();
        width = original.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(mat);
        Paint paint = new Paint();
        Canvas c = new Canvas(bmpGrayscale);
        paint.setColorFilter(filter);
        c.drawBitmap(original, 0, 0, paint);

        /*float[] mat = new float[]{
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0, 0, 0, 1, 0,};*/
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
    }


    //Threasholding Techniques
    public static Bitmap thresholdingOne(Bitmap src)
    {
        long startTime = System.nanoTime();
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap anythingBmap = Bitmap.createBitmap(width, height, src.getConfig());
        int threshold = 120;
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){

                int pixel = src.getPixel(x, y);
                int gray = Color.red(pixel);
                if(gray < threshold){
                    anythingBmap.setPixel(x, y, 0xFF000000);
                } else{
                    anythingBmap.setPixel(x, y, 0xFFFFFFFF);
                }
            }
        }
        long stopTime = System.nanoTime();
        System.out.println("Time thresh1 = "+ (stopTime - startTime));
        return anythingBmap;
    }

    //uses grayscaleTwo algorithm
    public static Bitmap thresholdingTwo(Bitmap src) {

        long startTime = System.nanoTime();
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);

                if (gray > 128)
                    gray = 255;
                else
                    gray = 0;
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        long stopTime = System.nanoTime();
        System.out.println("Time Thresh 2 = "+ (stopTime - startTime));
        return bmOut;
        //Execution time slower
        //Tends to get App stuck
    }


    //Threshold Function -unprocessed-
    //Threshold function ends

}

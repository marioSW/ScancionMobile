package com.scancion.scancionmobile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DisplayClass extends AppCompatActivity {



    private WebView wv;
    private String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        wv=(WebView)findViewById(R.id.webView);
        //wv.setWebViewClient(new MyBrowser());

        setURL("http://scancion.freeoda.com/GrammarChecker/anything.php");
       // String URL="http://127.0.0.1:1049/checkDocument?data=the+ball+was+thrown";

        //String URL="http://scancion.freeoda.com/GrammarChecker/demo.html";
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
       // wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);

        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        wv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


        wv.loadUrl(getURL());
        setContentView(R.layout.activity_content_display_class);

       /* try {
            Runtime.getRuntime().exec("cmd /c start C:\\Users\\Dharsha\\Desktop\\sliit doc\\4th year\\semester 2\\CDAP\\atd\\run-lowmem.bat");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }


    public String getURL() {
        return URL;

    }public void setURL(String URL) {
        this.URL = URL;
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



}

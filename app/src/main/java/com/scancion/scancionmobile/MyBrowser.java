package com.scancion.scancionmobile;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Dharsha on 10/22/2016.
 */
public class MyBrowser extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}

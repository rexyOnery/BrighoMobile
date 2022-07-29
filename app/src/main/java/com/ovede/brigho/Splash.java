package com.ovede.brigho;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ovede.brigho.web.Settings;
import com.ovede.brigho.web.WebAppInterface;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Splash extends AppCompatActivity {
    private WebView mainWebView;
    private String SPLASH_URL = Settings.SpotOn_Splash;
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mainWebView = findViewById(R.id.splash_webview);
        mainWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        mainWebView.setWebViewClient(new Splash.MyWebViewClient());
        mainWebView.loadUrl(SPLASH_URL);

        // configuracion del webview
        mainWebView.getSettings().setJavaScriptEnabled(true);
        //Enable and setup JS localStorage
        mainWebView.getSettings().setDomStorageEnabled(true);
        int SPLASH_TIME_OUT = 5000;
        new Handler() {
            public void postDelayed(Runnable runnable, int splash_time_out) {
            }

            @Override
            public void publish(LogRecord record) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        }.postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    private class MyWebViewClient extends WebViewClient {

        // permite la navegacion dentro del webview
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //Overriding webview URLs for API 23+ [suggested by github.com/JakePou]
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            SPLASH_URL = request.getUrl().toString();
            return  url_actions(request.getUrl().toString());
        }

    }

    public boolean url_actions(String url){
        aswm_view(url);
        return true;
    }

    void aswm_view(String url) {
        mainWebView.loadUrl(url);
    }
}

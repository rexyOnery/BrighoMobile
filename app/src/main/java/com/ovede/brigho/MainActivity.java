package com.ovede.brigho;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;

import com.ovede.brigho.web.DetectConnection;
import com.ovede.brigho.web.JavaBridge;
import com.ovede.brigho.web.Settings;
import com.ovede.brigho.web.WebAppInterface;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.ovede.brigho.databinding.ActivityMainBinding;

import android.view.ViewTreeObserver;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Timer;
import java.util.TimerTask;

import com.ovede.brigho.MessageDialog;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class MainActivity extends AppCompatActivity {
    private final static int FILECHOOSER_RESULTCODE = 1;
    static boolean ASWP_OFFLINE		= Settings.ASWP_OFFLINE;
    private final String HOME_URL = Settings.SpotOn_Home;
    private static final String ASWV_URL     		= Settings.ASWV_URL;

    ProgressDialog progressDialog;
    int asw_error_counter = 0;
    private WebView mainWebView;
    private ValueCallback<Uri[]> mUploadMessage;
    static boolean ASWP_CERT_VERIFICATION 	= Settings.ASWP_CERT_VERI;
    private String CURR_URL					= ASWV_URL;
    private SecureRandom random = new SecureRandom();
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    private FingerprintManager fingerprintManager;
    private Timer timer;
    Handler handler;
    Runnable r;


    //Getting host name
    public static String aswm_host(String url){
        if (url == null || url.length() == 0) {
            return "";
        }
        int dslash = url.indexOf("//");
        if (dslash == -1) {
            dslash = 0;
        } else {
            dslash += 2;
        }
        int end = url.indexOf('/', dslash);
        end = end >= 0 ? end : url.length();
        int port = url.indexOf(':', dslash);
        end = (port > 0 && port < end) ? port : end;
        Log.w("URL Host: ",url.substring(dslash, end));
        return url.substring(dslash, end);
    }

    public static int aswm_fcm_id(){
        //Date now = new Date();
        //Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                MessageDialog.MessageDialog(MainActivity.this,"You have been inactive for the past 3 minutes. Please log in again to continue.");
                //Toast.makeText(MainActivity.this, "user is inactive from last 5 minutes",Toast.LENGTH_SHORT).show();
            }
        };
        startHandler();


        // instanciamos el webview
        mainWebView = findViewById(R.id.webview);
        //this to pass data from jsScript to Android
        mainWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // establecemos el cliente interno para que la navegacion no se salga de la aplicacion
        mainWebView.setWebViewClient(new MyWebViewClient());

        // establecemos el cliente chrome para seleccionar archivos
        mainWebView.setWebChromeClient(new MyWebChromeClient());

        // configuracion del webview
        mainWebView.getSettings().setJavaScriptEnabled(true);
        //Enable and setup JS localStorage
        mainWebView.getSettings().setDomStorageEnabled(true);
        //The Bridge
        mainWebView.addJavascriptInterface(new JavaBridge(this), "JavaBridge");

        try {
            if (DetectConnection.InternetAvailable(MainActivity.this)) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String dash = extras.getString("EXTRA_USER_AUTHENTICATED");
                    try {
                        //assert dash != null;
                        if (dash.equals("yes")) {
                            mainWebView.loadUrl("http://mobile.brigho.com/seller");
                        } else {
                            mainWebView.loadUrl(HOME_URL);
                        }
                    } catch (Exception e) {
                        mainWebView.loadUrl(HOME_URL);
                    }

                } else {
                    mainWebView.loadUrl(HOME_URL);
                }
            } else {
                mainWebView.loadUrl(ASWV_URL);
            }
        }catch (Exception err){
            mainWebView.loadUrl(ASWV_URL);
        }

        final String theArgumentYouWantToPass = "Akili and the bees";
        mainWebView.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url){
                //mainWebView.loadUrl("javascript:CallJav('" + theArgumentYouWantToPass + "')");
                /* progressDialog.dismiss();*/
            }
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // get_location();
                /*progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("loading..."); // Setting Message
                //progressDialog.setTitle("ProgressDialog"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);*/
            }

            /*public void onPageFinished(WebView view, String url) {
                findViewById(R.id.msw_welcome).setVisibility(View.GONE);
                findViewById(R.id.msw_view).setVisibility(View.VISIBLE);
            }*/
            //For android below API 23
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                /*Toast.makeText(getApplicationContext(), getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                Log.d("ERROR_TAG: ",description);*/
                view.loadUrl("about:blank");
                aswm_view("file:///android_asset/app-404.html", false, asw_error_counter);
            }

            //Overriding webview URLs
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                CURR_URL = url;
                Log.d("URL",url);
                return url_actions(view, url);
            }

            //Overriding webview URLs for API 23+ [suggested by github.com/JakePou]
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                CURR_URL = request.getUrl().toString();
                return url_actions(view, request.getUrl().toString());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if(ASWP_CERT_VERIFICATION) {
                    super.onReceivedSslError(view, handler, error);
                } else {
                    handler.proceed(); // Ignore SSL certificate errors
                }
            }

        });

    }


    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();
        startHandler();
    }
    public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 180000);
    }

    //Reloading current page
    public void pull_fresh(){
        aswm_view(HOME_URL,false, asw_error_counter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean url_actions(WebView view, String url){
        boolean a = true;
        Log.d("URL: ",url);
        // show toast error if not connected to the network
        if (!ASWP_OFFLINE && !DetectConnection.InternetAvailable(MainActivity.this)) {

            MessageDialog.NetworkDialog(MainActivity.this, getString(R.string.check_connection));
            //Toast.makeText(getApplicationContext(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();

            // use this in a hyperlink to redirect back to default URL :: href="refresh:android"
        }else if(TextUtils.isEmpty(url))
        {
            //mainWebView.loadUrl("javascript:CallJav('just from call')");
        }
        else if (url.startsWith("refresh:")) {
            String ref_sch = (Uri.parse(url).toString()).replace("refresh:","");
            CURR_URL = ref_sch;
            /*if(ref_sch.matches("URL")){
                CURR_URL = ASWV_URL;
            }*/
            pull_fresh();


            // use this to open your apps page on google play store app :: href="rate:android"
        } else if (url.startsWith("rate:")) {
            final String app_package = getPackageName(); //requesting app package name from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app_package)));
            } catch (ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + app_package)));
            }

            // sharing content from your webview to external apps :: href="share:URL" and remember to place the URL you want to share after share:___
        } else if (url.startsWith("exit:")) {
            aswm_exit();

            // getting location for offline files
        } else if (url.startsWith("offloc:")) {
            String offloc = ASWV_URL+"?loc=";
            aswm_view(offloc,false, asw_error_counter);
            Log.d("OFFLINE LOC REQ",offloc);

            // creating firebase notification for offline files
        } else if (url.startsWith("activity:")) {
            Log.d("OFFLINE_FCM_TOKEN",url);
            aswm_view(url,false, asw_error_counter);
            return true;
            // opening external URLs in android default web browser
        } else {//if (ASWP_EXTURL && !aswm_host(url).equals(ASWV_HOST)) {
            aswm_view(url,false, asw_error_counter);

            // else return false for no special action
        } /*else {
            a = false;
        }*/
        return true;
    }

    public void aswm_exit(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // ====================
    // Web clients classes
    // ====================

    //Opening URLs inside webview with request
    void aswm_view(String url, Boolean tab, int error_counter) {
        if(error_counter > 2){
            asw_error_counter = 0;
            aswm_exit();
        }else {
            if (url.contains("?phonecall")) { // check to see whether the url already has query parameters and handle appropriately.

                final String theArgumentYouWantToPass = "processing";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    fingerprintManager =
                            (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                    //Check whether the device has a fingerprint sensor//
                    //try {
                        if (fingerprintManager.isHardwareDetected()) {

                            if(fingerprintManager.hasEnrolledFingerprints()) {
                                int startIndex = url.indexOf("=") + 1;
                                String userPhone = url.substring(startIndex);

                                Intent intent = new Intent(this, FingerprintLoginActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("EXTRA_USER_PHONE", userPhone);
                                intent.putExtras(extras);
                                startActivity(intent);
                            }else{
                                MessageDialog.FingerDialog(MainActivity.this,"No fingerprint enrolled on the device. Please enroll a fingerprint to continue!");
                                //Toast.makeText(getApplicationContext(), "No fingerprint enrolled on the device.   enroll a fingerprint to continue!", Toast.LENGTH_SHORT).show();
                                mainWebView.loadUrl("javascript:CallJav('" + theArgumentYouWantToPass + "')");
                            }
                        } else {
                            MessageDialog.FingerDialog(MainActivity.this,"Fingerprint sensor not detected!");
                            //Toast.makeText(getApplicationContext(), "Fingerprint sensor not detected!", Toast.LENGTH_SHORT).show();
                            mainWebView.loadUrl("javascript:CallJav('" + theArgumentYouWantToPass + "')");
                        }
                    /*}catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Fingerprint sensor not detected!", Toast.LENGTH_SHORT).show();
                        mainWebView.loadUrl("javascript:CallJav('" + theArgumentYouWantToPass + "')");
                    }*/
                }
            }else {
                mainWebView.loadUrl(url);
            }
        }
    }


    /*--- actions based on URL structure ---*/


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
            CURR_URL = request.getUrl().toString();
            return url_actions(view, request.getUrl().toString());
        }

    }

    private class MyWebChromeClient extends WebChromeClient {

        // maneja la accion de seleccionar archivos
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {

            // asegurar que no existan callbacks
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }

            mUploadMessage = filePathCallback;

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*"); // set MIME type to filter

            MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FILECHOOSER_RESULTCODE );

            return true;
        }
    }

    public void onBackPressed(){

        if (mainWebView.canGoBack()) {
            mainWebView.goBack();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Exit Brigho");
            dialog.setMessage("Brigho has nothing to go back, so what next?");
            dialog.setPositiveButton("EXIT ME", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.setNegativeButton("STAY HERE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
    }
    private void GoForward() {
        if (mainWebView.canGoForward()) {
            mainWebView.goForward();
        } else {
            Toast.makeText(this, "Can't go further!", Toast.LENGTH_SHORT).show();
        }
    }

    public class Callback extends WebViewClient {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }
}
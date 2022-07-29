package com.ovede.brigho.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.ovede.brigho.MainActivity;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Intent intent = new Intent(mContext, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_UserID", toast);
        extras.putString("EXTRA_TOKEN", "token");
        intent.putExtras(extras);
        mContext.startActivity(intent);
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}

package com.ovede.brigho.web;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ovede.brigho.MainActivity;

public class JavaBridge {
    Activity parentActivity;
    public JavaBridge(Activity activity) {
        parentActivity = activity;
    }

    public void launchNewActvity(String toast){
        Intent intent = new Intent(parentActivity, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_UserID", toast);
        extras.putString("EXTRA_TOKEN", "token");
        intent.putExtras(extras);
        parentActivity.startActivity(intent);
    }
}

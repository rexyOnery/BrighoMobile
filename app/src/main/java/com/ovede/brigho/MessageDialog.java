package com.ovede.brigho;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;

public class MessageDialog {
    private static AlertDialog alertDialog;
    static HashMap<String, String> params = new HashMap<>();
    private static String text;
    private static Context activitycoordinator;

    public static void MessageDialog(final android.content.Context _activity, final String _text) {
        text = _text;
        activitycoordinator = _activity;
        alertDialog = createCallDialog(callClickMessageListener(), _activity);
        alertDialog.show();
    }

    public static void NetworkDialog(final android.content.Context _activity, final String _text) {
        text = _text;
        activitycoordinator = _activity;
        alertDialog = createNetworkDialog(callClickListener(), _activity);
        alertDialog.show();
    }

    public static void FingerDialog(final android.content.Context _activity, final String _text) {
        text = _text;
        activitycoordinator = _activity;
        alertDialog = createFingerprintDialog(callClickListener(), _activity);
        alertDialog.show();
    }

    public static AlertDialog createNetworkDialog(final DialogInterface.OnClickListener callClickListener,
                                               final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setIcon(R.drawable.ic_baseline_network_check_24);
        alertDialogBuilder.setTitle("Network Error!");
        alertDialogBuilder.setPositiveButton("OK", callClickListener);
        alertDialogBuilder.setCancelable(false);

        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.message_dialog, null);
        final TextView contact =  dialogView.findViewById(R.id.message_dialog_text);
        contact.setText(text);
        alertDialogBuilder.setView(dialogView);

        return alertDialogBuilder.create();

    }
    public static AlertDialog createCallDialog(final DialogInterface.OnClickListener callClickMessageListener,
                                               final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setIcon(R.drawable.ic_baseline_timer_off_24);
        alertDialogBuilder.setTitle("Time Out!");
        alertDialogBuilder.setPositiveButton("OK", callClickMessageListener);
        alertDialogBuilder.setCancelable(false);

        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.message_dialog, null);
        final TextView contact =  dialogView.findViewById(R.id.message_dialog_text);
        contact.setText(text);
        alertDialogBuilder.setView(dialogView);

        return alertDialogBuilder.create();

    }


    public static AlertDialog createFingerprintDialog(final DialogInterface.OnClickListener callClickListener,
                                               final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setIcon(R.drawable.ic_baseline_finger_print_24);
        alertDialogBuilder.setTitle("Fingerprint Error!");
        alertDialogBuilder.setPositiveButton("OK", callClickListener);
        alertDialogBuilder.setCancelable(false);

        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.message_dialog, null);
        final TextView contact =  dialogView.findViewById(R.id.message_dialog_text);
        contact.setText(text);
        alertDialogBuilder.setView(dialogView);

        return alertDialogBuilder.create();

    }

    private static DialogInterface.OnClickListener callClickMessageListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent myIntent = new Intent(activitycoordinator, MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_DASH", "login");
                myIntent.putExtras(extras);
                activitycoordinator.startActivity(myIntent);
                alertDialog.dismiss();
            }
        };
    }

    private static DialogInterface.OnClickListener callClickListener() {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        };
    }
}


package com.jordanheim.jerseytrackerapp;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

/**
 * Class to determine if the app is able to send SMS
 * messages to the user when a jersey quantity gets
 * to 0 based on their preferences
 *
 * NOTE: SMS manager is not used, but rather toast messages are used to
 * show the user that the jersey quantity is low
 */
public class TextNotificationAlert
{
    public static AlertDialog doubleButton(final JerseyListActivity context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("APP SMS Permission Needed")
                .setIcon(R.drawable.text_notif)
                .setCancelable(false)
                .setMessage("JerseyTracker SMS feature needs persmission")
                .setPositiveButton("Enable", (dialog, arg1) -> {
                    Toast.makeText(context, "SMS Alerts Enabled", Toast.LENGTH_LONG).show();
                    JerseyListActivity.allowSendSMS();
                    dialog.cancel();
                })
                .setNegativeButton("Disable", (dialog, arg1) -> {
                    Toast.makeText(context, "SMS Alerts Disabled", Toast.LENGTH_LONG).show();
                    JerseyListActivity.denySendSMS();
                    dialog.cancel();
                });

        return builder.create();
    }

}

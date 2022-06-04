package org.beyondlogic.ble;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class ShowAlertDialogs {

    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    public ShowAlertDialogs(Context context) {
        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
    }

    public void Dismiss() {
        dialog.dismiss();
    }

    public void ShowAppAboutDialog(Context context) {
        dialog.dismiss();
        builder.setTitle(R.string.about_title);
        builder.setMessage(context.getString(R.string.about_message) + BuildConfig.VERSION_NAME);
        builder.setPositiveButton(R.string.about_ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(null, null);
        dialog = builder.create();
        dialog.show();
    }

    public void ShowAppExitDialog(Context context) {
        dialog.dismiss();
        builder.setTitle(R.string.exit_title);
        builder.setMessage(R.string.exit_message);
        builder.setPositiveButton(R.string.exit_ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setNegativeButton(R.string.exit_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
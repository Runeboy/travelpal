package rune.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import rune.logging.Logger;

/**
 * Helper class for common UI notifications
 */
public class MessageBoxExpert {

    // Fields

    private static final Logger log = new Logger(MessageBoxExpert.class);

    private static int mDialogCount = 0;

    private static String TAG_PREFIX = "DIALOG_";

    // Methods

    public static Dialog makeDialogwithOkButton(String title, String message, DialogInterface.OnClickListener onClick, Context context) {
        AlertDialog.Builder build = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", onClick);

//        if (onClick != null)
//            build = build.setPositiveButton("OK")
        return build .create();
    }

    public static void showMessageWithOkButton(String title , String message, Context context) {
        makeDialogwithOkButton(title, message, null, context).show();
    }

    public static void showMessageFragmentWithOkButton(final String title , final String message, final Activity activity) {
        log.d("Showing message fragment dialog: {0}:{1}", title, message);

        String nextDialogTag =  TAG_PREFIX + mDialogCount++;

        new OkDialogFragment().initialize(title, message).show(activity.getFragmentManager(), nextDialogTag);

    }

}

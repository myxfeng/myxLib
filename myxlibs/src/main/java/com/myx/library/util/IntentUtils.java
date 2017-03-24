package com.myx.library.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class IntentUtils {
    public static void callTelephone(Activity activity, String address) {
        Uri uri = Uri.parse("tel:" + address);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(it);
    }

    public static void sendEmail(String shareSubject, String shareContent, final Activity context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        i.putExtra(Intent.EXTRA_TEXT, shareContent);
        i.setType("application/xhtml+xml");
        try {
            context.startActivity(Intent.createChooser(i, shareSubject));
        } catch (ActivityNotFoundException e) {
        }
    }

    /**
     * @param url
     * @param title
     * @param context
     */
    public static void callSystemBrowser(String url, String title, Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return;
        }
        // }
    }
    /**
     * @param url
     * @param choosertxt
     * @param context
     */
    public static void callBrowserDownload(String url, String choosertxt,Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        Intent chooser = Intent.createChooser(i, choosertxt + "");
        context.startActivity(chooser);
    }

    /**
     * @param context
     * @param contentUri
     */
    public static void galleryAddPic(Context context, Uri contentUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}

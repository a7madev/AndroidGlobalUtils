package me.a7madev.androidglobalutils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public class GlobalFileUtils {

    public static final String TAG = GlobalFileUtils.class.getSimpleName();

    /**
     * Open File using intent
     * @param context  The context to use. Use application or activity context
     * @param file file object to be opened
     */
    public static void openFileIntent(Context context, File file) {
        if(context != null && file.exists()) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), getMimeType(context, Uri.fromFile(file)));
                context.startActivity(intent);
            } catch (Exception e) {
                GlobalUtils.logThis(TAG, "openFileIntent Exception", e);
            }
        }
    }

    /**
     * Get File Mime Type
     * @param context  The context to use. Use application or activity context
     * @param uri File uri
     * @return Mime type string
     */
    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        try {
            if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = context.getContentResolver();
                mimeType = cr.getType(uri);
            } else {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                        fileExtension.toLowerCase());
            }
        } catch (Exception e) {
            GlobalUtils.logThis(TAG, "getMimeType Exception", e);
        }
        return mimeType;
    }

}

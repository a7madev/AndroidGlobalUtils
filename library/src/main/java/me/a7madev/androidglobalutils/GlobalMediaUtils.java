package me.a7madev.androidglobalutils;

import android.net.Uri;

public class GlobalMediaUtils {

    public static final String TAG = GlobalMediaUtils.class.getSimpleName();

    public static boolean isImage(String url) {
        String mimeType = GlobalFileUtils.getMimeType(null, Uri.parse(url));
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideo(String url) {
        String mimeType = GlobalFileUtils.getMimeType(null, Uri.parse(url));
        return mimeType != null && mimeType.startsWith("video");
    }
}
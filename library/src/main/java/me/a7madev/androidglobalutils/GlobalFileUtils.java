package me.a7madev.androidglobalutils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;

public class GlobalFileUtils {

    public static final String TAG = GlobalFileUtils.class.getSimpleName();

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

    /**
     * Open File using intent
     * @param context  The context to use. Use application or activity context
     * @param openFile file object to be opened
     */

    public static void openFileIntent(Context context, File openFile) {
        if(context != null && openFile.exists()) {
            try {
                Intent intent = getFileIntent(context, openFile);
                context.startActivity(intent);
            } catch (Exception e) {
                GlobalUtils.logThis(TAG, "openFileIntent Exception", e);
            }
        }
    }

    /**
     * Return Intent to open any files
     * @param context  The context to use. Use application or activity context
     * @param openFile file object to be opened
     */
    public static Intent getFileIntent(Context context, File openFile) {
        Intent fileIntent = null;
        if(context != null && openFile.exists()) {
            try {
                fileIntent = new Intent(Intent.ACTION_VIEW);
                fileIntent.setDataAndType(Uri.fromFile(openFile), getMimeType(context, Uri.fromFile(openFile)));
            } catch (Exception e) {
                GlobalUtils.logThis(TAG, "getFileIntent Exception", e);
            }
        }
        return fileIntent;
    }


    /**
     * Return list of files in a directory
     * @param dir  directory as File
     * @param acceptExtensions include all extensions to be listed: .png, .jpg, .mp4
     * @param includeDirectory include directory in the list?
     * @return Array list of files
     */
    public static ArrayList<File> getFilesListFromDirectory(File dir, String[] acceptExtensions, boolean includeDirectory) {
        ArrayList<File> fileList = new ArrayList<>();
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File aListFile : listFile) {
                if (aListFile.isDirectory()) {
                    if(includeDirectory){
                        fileList.add(aListFile);
                    }
                    getFilesListFromDirectory(aListFile, acceptExtensions, includeDirectory);
                } else {
                    if(acceptExtensions != null && acceptExtensions.length > 0){
                        for (String ext : acceptExtensions){
                            if (aListFile.getName().endsWith(ext)) {
                                fileList.add(aListFile);
                            }
                        }
                    }
                }
            }
        }
        return fileList;
    }
}

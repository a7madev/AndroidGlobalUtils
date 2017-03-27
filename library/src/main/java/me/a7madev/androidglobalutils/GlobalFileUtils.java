package me.a7madev.androidglobalutils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
     * @return File Intent
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
        if(dir != null && dir.exists()) {
            File listFile[] = dir.listFiles();
            if (listFile != null && listFile.length > 0) {
                for (File aListFile : listFile) {
                    if (aListFile.isDirectory()) {
                        if (includeDirectory) {
                            fileList.add(aListFile);
                        }
                        getFilesListFromDirectory(aListFile, acceptExtensions, includeDirectory);
                    } else {
                        if (acceptExtensions != null && acceptExtensions.length > 0) {
                            for (String ext : acceptExtensions) {
                                if (aListFile.getName().endsWith(ext)) {
                                    fileList.add(aListFile);
                                }
                            }
                        }
                    }
                }
            }
        }
        return fileList;
    }

    /**
     * Get image or video thumbnail from a file
     * @param context Context
     * @param file Image
     * @return Bitmap bitmap object
     */
    public static Bitmap getMediaThumbnailFromFile(Context context, File file) {

        Bitmap thumbBitmap;

        // get video thumbnail
        thumbBitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);

        // get image thumbnail
        if(thumbBitmap == null){
            thumbBitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
        }

        // create a thumbnail
        if(thumbBitmap == null){
            File image = new File(Uri.fromFile(file).getPath());

            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(image.getPath(), bounds);
            if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
                return null;

            int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                    : bounds.outWidth;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = originalSize / 256;
            return BitmapFactory.decodeFile(image.getPath(), opts);
        }
        return thumbBitmap;
    }

    /**
     * Delete a file
     * @param file File to be deleted
     * @return boolean file is deleted?
     */
    public static boolean deleteFile(File file) {
        boolean fileDeleted = false;
        try {
            if(file != null && file.exists()){
                fileDeleted = file.delete();
            }
        } catch (Exception e) {
            GlobalUtils.logThis(TAG, "deleteFile Exception", e);
        }
        return fileDeleted;
    }

    /**
     * Get file name from url
     * @param url link
     * @return String file name
     */
    public static String getFileNameFromURL(String url) {
        if(url != null && !url.isEmpty()){
            return url.substring(url.lastIndexOf('/') + 1, url.length());
        }
        return "unnamed";
    }


    /**
     * Get Storage Directory, if doesnt exist, return download directory
     * @param directoryName Your directory name
     * @return File File object
     */
    public static File getStorageDirectory(String directoryName) {
        File downloadsFolder = null, appFolder = null;
        boolean success = false;

        try {

            downloadsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

            appFolder = new File(Environment.getExternalStorageDirectory(), "/" + directoryName);

            if (!appFolder.exists()) {
                if(appFolder.mkdirs()){
                    success = true;
                }
            }else{
                success = true;
            }

        } catch (Exception e) {
            GlobalUtils.logThis(TAG, "getStorageDirectory ActivityNotFoundException", e);
        }

        if (success) {
            return appFolder;
        } else {
            return downloadsFolder;
        }
    }

    /**
     * Checks if external storage is available for read and write
     * @return Boolean
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Checks if external storage is available to at least read
     * @return Boolean
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
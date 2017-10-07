package me.a7madev.androidglobalutils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import static me.a7madev.androidglobalutils.GlobalUtils.logThis;

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
            logThis(TAG, "getMimeType Exception", e);
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
                logThis(TAG, "openFileIntent Exception", e);
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
                logThis(TAG, "getFileIntent Exception", e);
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
            logThis(TAG, "deleteFile Exception", e);
        }
        return fileDeleted;
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
            logThis(TAG, "getStorageDirectory ActivityNotFoundException", e);
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

    /**
     * Get File Name From URL
     * @param url String
     * @return File Name String
     */
    public static String getFileNameFromURL(String url) {
        try {
            String link1Decoded = URLDecoder.decode(url, "UTF-8");
            return link1Decoded.substring(link1Decoded.lastIndexOf('/') + 1);
        } catch (UnsupportedEncodingException e) {
            logThis(TAG, "getFileNameFromURL UnsupportedEncodingException", e);
            return url;
        }
    }

    /**
     * Accept Image From File
     * @param acceptedImageFileExtensions String[]
     * @param file File
     * @return boolean is valid or not
     */
    public static boolean acceptImageFromFile(String[] acceptedImageFileExtensions, File file) {
        try {
            for (String extension : acceptedImageFileExtensions)
            {
                if (file.getName().toLowerCase().endsWith(extension))
                {
                    return true;
                }
            }
        } catch (Exception e) {
            logThis(TAG, "acceptImageFromFile Exception", e);
        }
        return false;
    }

    /**
     * Accept Video From File
     * @param acceptedVideoFileExtensions String[]
     * @param file File
     * @return boolean is valid or not
     */
    public static boolean acceptVideoFromFile(String[] acceptedVideoFileExtensions, File file) {
        try {
            for (String extension : acceptedVideoFileExtensions)
            {
                if (file.getName().toLowerCase().endsWith(extension))
                {
                    return true;
                }
            }
        } catch (Exception e) {
            logThis(TAG, "acceptVideoFromFile Exception", e);
        }
        return false;
    }

    /**
     * Accept Image From Path
     * @param acceptedImageFileExtensions String[]
     * @param path String
     * @return boolean is valid or not
     */
    public static boolean acceptImageFromFilePath(String[] acceptedImageFileExtensions, String path) {
        try {
            for (String extension : acceptedImageFileExtensions)
            {
                if (path.toLowerCase().endsWith(extension))
                {
                    return true;
                }
            }
        } catch (Exception e) {
            logThis(TAG, "acceptImageFromFilePath Exception", e);
        }
        return false;
    }

    /**
     * Accept Video From Path
     * @param acceptedVideoFileExtensions String[]
     * @param path String
     * @return boolean is valid or not
     */
    public static boolean acceptVideoFromFilePath(String[] acceptedVideoFileExtensions, String path) {
        try {
            for (String extension : acceptedVideoFileExtensions)
            {
                if (path.toLowerCase().endsWith(extension))
                {
                    return true;
                }
            }
        } catch (Exception e) {
            logThis(TAG, "acceptVideoFromFilePath Exception", e);
        }
        return false;
    }

    /**
     * Get URI From File Path
     * @param context Context
     * @param filePath String
     * @param applicationID String (BuildConfig.APPLICATION_ID)
     * @return Uri
     */
    public static Uri getURIFromFilePath(Context context, String filePath, String applicationID) {
        Uri uri = null;
        if(GlobalUtils.validateText(filePath)) {
            try {
                uri = FileProvider.getUriForFile(context, applicationID + ".provider", new File(filePath));
            } catch (Exception e) {
                logThis(TAG, "getURIFromFilePath Exception", e);
                uri = null;
            }
            if(uri == null){
                try {
                    uri = Uri.fromFile(new File(filePath));
                } catch (Exception e) {
                    logThis(TAG, "getURIFromFilePath Exception", e);
                }
            }
        }
        return uri;
    }

    /**
     * Get share file intent
     * @param context  The context to use. Use application or activity context
     * @param file file object to be opened
     * @return Intent
     */
    public static Intent getShareFileIntent(Context context, File file) {
        Intent shareIntent = null;
        try {
            Uri fileURI = Uri.fromFile(file);
            shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileURI);
            shareIntent.setType(GlobalFileUtils.getMimeType(context, fileURI));
        } catch (Exception e) {
            logThis(TAG, "getShareFileIntent Exception", e);
        }
        return shareIntent;
    }

    /**
     * Opens share file intent
     * @param context  The context to use. Use application or activity context
     * @param file file object to be opened
     * @param shareDialogMessage Show message appears in share dialog
     */
    public static void openShareFileIntent(Context context, File file, String shareDialogMessage) {
        try {
            context.startActivity(Intent.createChooser(getShareFileIntent(context, file), shareDialogMessage));
        } catch (Exception e) {
            logThis(TAG, "openShareFileIntent Exception", e);
        }
    }

    /**
     * Get Mime Type From File URL
     * @param context  The context to use. Use application or activity context
     * @param uri File URI
     * @return String mime type
     */
    public static String getMimeTypeFromFileURI(Context context, Uri uri) {
        String mimeType = null;
        try {
            if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = context.getContentResolver();
                mimeType = cr.getType(uri);
            } else {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            }
        } catch (Exception e) {
            logThis(TAG, "getMimeTypeByURI Exception", e);
        }
        return mimeType;
    }

    public static String getMimeTypeFromFile(File file) {
        String type = null;
        try {
            final String url = file.toString();
            final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
            }
            if (type == null) {
                type = "*/*"; // fallback type. You might set it to */*
            }
        } catch (Exception e) {
            logThis(TAG, "getMimeTypeFromFile Exception", e);
        }
        return type;
    }

    public static String getPathFromFileURI(Context context, Uri uri) {
        String stringPath = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null)
                return null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            stringPath = cursor.getString(column_index);
            cursor.close();
        } catch (IllegalArgumentException e) {
            logThis(TAG, "getPathFromFileURI Exception", e);
        }
        return stringPath;
    }

}
package me.a7madev.androidglobalutils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class GlobalUtils {

    public static final String TAG = GlobalUtils.class.getSimpleName();

    /**
     * Is build in debug config or release
     * @return  boolean true (debug), false (release)
     */
    public static boolean isDebug(){
        return BuildConfig.DEBUG;
    }

    /**
     * Log message and exception
     * @param TAG Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param error An exception to log. Use null if there is no error
     */
    public static void logThis(String TAG, String message, Throwable error){
        if(message != null) { // validate message
            if (error != null) { // validate throwable error
                Log.e(TAG, message, error);
            } else { // if no error just show a message
                Log.d(TAG, message);
            }
        }
    }

    /**
     * Make a standard toast that just contains a text view.
     * @param context  The context to use. Use application or activity context
     * @param message The text to show.
     * @param duration How long to display the message. Toast.LENGTH_SHORT OR Toast.LENGTH_LONG
     * @param inDebugOnly Show the toast message in debug config only
     */
    public static void showToast(Context context, String message, int duration, boolean inDebugOnly){

        // validate duration
        if(duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG){
            duration = Toast.LENGTH_SHORT;
        }

        // validate message
        if(message != null) {
            if (inDebugOnly && isDebug()) { // show only in debug config
                Toast.makeText(context, message, duration).show();
            } else if (!inDebugOnly) { // show in debug and release configs
                Toast.makeText(context, message, duration).show();
            }
        }
    }

    /**
     * Check if Internet is available and connected
     * @param context  The context to use. Use application or activity context
     * @param TAG Used to identify the source of a log message.
     * @return true if Internet is connected
     */
    public static boolean checkForInternetConnection(Context context, String TAG) {

        boolean internetIsAvailable = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            //validate internet connection
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable()) {
                internetIsAvailable = true;
            }
        } catch (Exception e) {
            logThis(TAG, "checkForInternetConnection Exception " + e.getMessage(), e);
        }

        return internetIsAvailable;
    }


    /**
     * Create a new typeface from font file.
     * @param context  The context to use. Use application or activity context
     * @param fontName  The file name of the font data in the assets directory (fonts/Roboto-Light.ttf)
     * @return The new typeface.
     */
    public static Typeface getTypeFace(Context context, String fontName){
        Typeface typeface = null;
        if(context != null){
            typeface = Typeface.createFromAsset(context.getAssets(), fontName);
        }
        return typeface;
    }

    /**
     * Get an array of heterogeneous values.
     * @param context  The context to use. Use application or activity context
     * @param resID  Resource id (R.array.name_array)
     * @return The TypedArray
     */
    public static TypedArray getTypedArrayResource(Context context, int resID) {
        TypedArray typedArray = null;
        try {
            if(context != null) {
                try {
                    typedArray = context.getResources().obtainTypedArray(resID);
                } catch (Resources.NotFoundException e) {
                    logThis(TAG, "getAppResources Resources.NotFoundException", e);
                }
            }
        } catch (Exception e) {
            logThis(TAG, "getTypedArrayResource Exception", e);
        }
        return typedArray;
    }


    /**
     * Return the string array associated with a particular resource ID.
     * @param context  The context to use. Use application or activity context
     * @param arrayResId  Resource id (R.array.name_array)
     * @return The string array associated with the resource.
     */
    public static String[] getStringArray(Context context, int arrayResId) {
        return context.getResources().getStringArray(arrayResId);
    }

    /**
     * Return the int array associated with a particular resource ID.
     * @param context  The context to use. Use application or activity context
     * @param arrayResId  Resource id (R.array.name_array)
     * @return The integer array associated with the resource.
     */
    public static int[] getIntArray(Context context, int arrayResId) {
        return context.getResources().getIntArray(arrayResId);
    }
}

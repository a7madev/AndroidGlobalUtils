package me.a7madev.androidglobalutils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GlobalUtils implements GlobalUtilsInterface {

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
            try {
                if (inDebugOnly && isDebug()) { // show only in debug config
                    Toast.makeText(context, message, duration).show();
                } else if (!inDebugOnly) { // show in debug and release configs
                    Toast.makeText(context, message, duration).show();
                }
            } catch (Exception e) {
                logThis(TAG, "showToast Exception", e);
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

    /**
     * Convert String Array to Array List
     * @param stringArray  String Array
     * @return String Array List
     */
    public static ArrayList<String> convertStringArrayToArrayList(String[] stringArray) {
        if(stringArray != null && stringArray.length > 0){
            return new ArrayList<>(Arrays.asList(stringArray));
        }else{
            return null;
        }
    }

    /**
     * Get content from clipboard
     * @param context  The context to use. Use application or activity context
     * @return String clipboard content
     */
    public static String getContentFromClipboard(Context context) {

        // initialize paste data string
        String pasteData = "";

        // get data from clip board
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            pasteData = item.getText().toString();
        } catch (Exception e) {
            logThis(TAG, "getContentFromClipboard Exception", e);
        }

        return pasteData;
    }

    /**
     * Request Multiple Permissions (Android M+)
     * @param activity  Activity reference
     * @param permissionsList  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
     *                         + Add permission to manifest.xml "permission android:name="android.permission.READ_EXTERNAL_STORAGE"
     * @param requestCode  Application specific request code to match with a result reported to
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestMultiplePermissions(Activity activity, String[] permissionsList, int requestCode) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // init permissions list
                List<String> permissions = new ArrayList<>();

                // loop through permissions
                for (String permission : permissionsList) {
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(permission);
                    }
                }

                // if permissions list is not empty, request permission
                if (!permissions.isEmpty()) {
                    activity.requestPermissions(permissions.toArray(new String[permissions.size()]), requestCode);
                }
            }
        } catch (Exception e) {
            logThis(TAG, "requestMultiplePermissions Exception", e);
        }
    }

    /**
     * Initialize Progress Dialog
     * @param context  The context to use. Use application or activity context
     * @param message String: progress dialog message
     * @param isCancelable boolean: is cancelable
     * @param isIndeterminate boolean: is indeterminate
     * @return  ProgressDialog Progress Dialog object
     */
    public static ProgressDialog initProgressDialog(Context context, String message, boolean isCancelable, boolean isIndeterminate) {
        ProgressDialog loadingDialog = null;
        try {
            if(context != null) {
                if (message == null) {
                    message = "Loading. Please wait...";
                }

                loadingDialog = new ProgressDialog(context);
                loadingDialog.setCancelable(isCancelable);
                loadingDialog.setMessage(message);
                loadingDialog.setIndeterminate(isIndeterminate);
            }
        } catch (Exception e) {
            logThis(TAG, "initProgressDialog Exception", e);
        }
        return loadingDialog;
    }

    /**
     * Dismiss Progress Dialog
     * @param progressDialog  Progress Dialog object
     */
    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        // dismiss the loading dialog
        try {
            if(progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            logThis(TAG, "dismissProgressDialog Exception", e);
        }
    }

    /**
     * Get Bitmap by Resource ID
     * @param context  The context to use. Use application or activity context
     * @param resID  resource ID: R.drawable.image
     * @return Bitmap bitmap object
     */
    public static Bitmap getBitmapByResourceID(Context context, int resID) {
        return BitmapFactory.decodeResource(context.getResources(), resID);
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
     * Get share text intent
     * @param stringTitle Title
     * @param stringContent Content
     * @return Intent
     */
    public static Intent getShareTextIntent(String stringTitle, String stringContent) {
        Intent sendIntent = null;
        try {
            sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, stringContent);
            sendIntent.putExtra(Intent.EXTRA_TITLE, stringTitle);
            sendIntent.setType("text/plain");
        } catch (Exception e) {
            logThis(TAG, "getShareTextIntent Exception", e);
        }
        return sendIntent;
    }

    /**
     * Open share text intent
     * @param context  The context to use. Use application or activity context
     * @param stringTitle Title
     * @param stringContent Content
     * @param shareDialogTitle Show message appears in share dialog
     */
    public static void openShareTextIntent(Context context, String stringTitle, String stringContent, String shareDialogTitle) {
        try {
            context.startActivity(Intent.createChooser(getShareTextIntent(stringTitle, stringContent), shareDialogTitle));
        } catch (Exception e) {
            logThis(TAG, "openShareTextIntent Exception", e);
        }
    }

    /**
     * Get call intent
     * @param phoneNumber Phone number
     * @return  Intent
     */
    public static Intent getCallIntent(String phoneNumber) {
        Intent callIntent = null;
        try {
            callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(phoneNumber));
        } catch (ActivityNotFoundException e) {
            logThis(TAG, "getCallIntent ActivityNotFoundException", e);
        }
        return callIntent;
    }

    /**
     * Open call intent
     * @param activity  Fragment activity
     * @param phoneNumber Phone number
     * @param codeRequest Permission request (onActivityResult) if required
     */
    public static void openCallIntent(Activity activity, String phoneNumber, int codeRequest) {
        try {
            Intent callIntent = getCallIntent(phoneNumber);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android M+ Check Permission
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    activity.startActivity(callIntent);
                }else{
                    requestMultiplePermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, codeRequest);
                }
            }else{ // Android Pre-M
                activity.startActivity(callIntent);
            }
        } catch (ActivityNotFoundException e) {
            logThis(TAG, "openCallIntent ActivityNotFoundException", e);
        }
    }

    /**
     * Get URL Intent
     * @param url Website Link
     * @return Intent
     */
    public static Intent getURLIntent(String url) {
        Intent intent = null;
        try {
            if(url != null && !url.isEmpty()){
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            }
        } catch (Exception e) {
            logThis(TAG, "getURLIntent Exception", e);
        }
        return intent;
    }

    /**
     * Open URL Intent
     * @param context  The context to use. Use application or activity context
     * @param link Website Link
     */
    public static void openURLIntent(Context context, String link) {
        try {
            context.startActivity(getURLIntent(link));
        } catch (Exception e) {
            logThis(TAG, "Unable to open website!", e);
        }
    }

    /**
     * Get Email Intent
     * @param emailAddress To Email Address
     * @param emailSubject Email Subject
     * @return Intent
     */
    public static Intent getnEmailIntent(String emailAddress, String emailSubject) {
        Intent emailIntent = null;
        try {
            emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
            emailIntent.setType("message/rfc822");
        } catch (Exception e) {
            logThis(TAG, "getnEmailIntent Exception", e);
        }
        return emailIntent;
    }

    /**
     * Open Email Intent
     * @param context  The context to use. Use application or activity context
     * @param emailAddress To Email Address
     * @param emailSubject Email Subject
     * @param dialogMessage Dialog message (Example: Choose an email client:)
     */
    public static void openEmailIntent(Context context, String emailAddress, String emailSubject, String dialogMessage) {
        try {
            context.startActivity(Intent.createChooser(getnEmailIntent(emailAddress, emailSubject), dialogMessage));
        } catch (Exception e) {
            logThis(TAG, "openEmailIntent Exception", e);
        }
    }

    /**
     * Change Activity Theme
     * @param activity  Activity Content
     * @param styleID Style ID: R.style.theme
     */
    public static void changeActivityTheme(Activity activity, int styleID) {
        try {
            activity.setTheme(styleID);
        } catch (Exception e) {
            logThis(TAG, "changeActivityTheme Exception", e);
        }
    }

    /**
     * Load image
     * @param context  Activity Content
     * @param url Image url or null
     * @param placeHolder Image placeholder: R.id.image or 0
     * @param error Image error: R.id.image or 0
     * @param imageView Image View
     */
    public static void loadImage(Context context, String url, int placeHolder, int error, ImageView imageView) {
        try {
            if (context != null) {
                if (imageView != null) {
                    if (url != null && !url.isEmpty()) {
                        if (placeHolder != 0) {
                            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeHolder).into(imageView);
                        } else {
                            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                        }
                    } else {
                        Glide.with(context).load(placeHolder).into(imageView);
                    }
                } else {
                    Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL);
                }
            }
        } catch (Exception e) {
            logThis(TAG, "loadImage Exception", e);
        }
    }

    /**
     * Generate UUID
     * @return UUID String
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Today's Timestamp
     * @return String today timestamp
     */
    public static String todayTimestamp() {
        DateTime dateTimeNow = DateTime.now();
        return createDateTimeString(getTwoDigitsInt(dateTimeNow.getYear()), getTwoDigitsInt(dateTimeNow.getMonthOfYear()), getTwoDigitsInt(dateTimeNow.getDayOfMonth()), true);
    }

    /**
     * Create Date Time String
     * @param year String
     * @param month String
     * @param day String
     * @param midnight boolean, if true returns time 00:00, otherwise 11:59
     * @return String two digits
     */
    public static String createDateTimeString(String year, String month, String day, boolean midnight) {
        String time = "00:00:00";

        if (!midnight) {
            time = "11:59:59";
        }

        return year + "-" + month + "-" + day + "T" + time;
    }

    /**
     * Get Two Digits Integer
     * @param num integer number
     * @return String two digits
     */
    public static String getTwoDigitsInt(int num) {
        return String.format(Locale.US,"%02d", num);
    }

    /**
     * Validate the text
     * @param text String
     * @return boolean if text is not null and not empty
     */
    public static boolean validateText(String text) {
        return text != null && !text.isEmpty();
    }

    /**
     * Get Display Size
     * @param activity Activity
     * @return String Size and SDK Number
     */
    public static String getDisplaySize(Activity activity) {
        Window window;
        if(activity != null){
            window = activity.getWindow();
            Point size = new Point();
            window.getWindowManager().getDefaultDisplay().getSize(size);
            return "Size: + "+ size +", SDK: "+ android.os.Build.VERSION.SDK_INT;
        }else{
            return null;
        }
    }

    /**
     * Get Display Density
     * @param activity Activity
     * @return DisplayMetrics display metrics object
     */
    public static DisplayMetrics getDisplayDensity(Activity activity) {
        if(activity != null){
            return activity.getResources().getDisplayMetrics();
        }else{
            return null;
        }
    }

    /**
     * Convert DP to PX
     * @param context Context
     * @param dp integer dp value
     * @return px integer
     */
    public static int convertDpToPx(Context context, int dp) {
        try {
            return (int) (dp * (context.getResources().getDisplayMetrics().density) + 0.5f);
        } catch (Exception e) {
            logThis(TAG, "convertDpToPx Exception", e);
            return dp;
        }
    }

    /**
     * Convert PX to DP
     * @param context Context
     * @param px float pixel
     * @return dp integer
     */
    public static float convertPxToDp(Context context, float px){
        try {
            return px / ((float)context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        } catch (Exception e) {
            logThis(TAG, "convertPxToDp Exception", e);
            return px;
        }
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
     * Get Calendar Month Display
     * @param month integer
     * @return Month Title String
     */
    public static String getCalendarMonthDisplay(int month) {
        switch (month) {
            case 1:
                return "JANUARY";
            case 2:
                return "FEBRUARY";
            case 3:
                return "MARCH";
            case 4:
                return "APRIL";
            case 5:
                return "MAY";
            case 6:
                return "JUNE";
            case 7:
                return "JULY";
            case 8:
                return "AUGUST";
            case 9:
                return "SEPTEMBER";
            case 10:
                return "OCTOBER";
            case 11:
                return "NOVEMBER";
            case 12:
                return "DECEMBER";
            default:
                return "";
        }
    }

    /**
     * Convert String to Integer
     * @param string text
     * @return integer number
     */
    public static int convertStringToInt(String string) {
        if (string != null && !string.isEmpty()) {
            return Integer.parseInt(string);
        } else {
            return 0;
        }
    }

    /**
     * Is Device Online
     * @param context Context
     * @return boolean if device online or not
     */
    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Convert Double to String
     * @param number double
     * @return Double Number as String
     */
    public static String convertDoubleToString(double number) {
        try {
            return Double.toString(number);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Dismiss Alert Dialog
     * @param alertDialog Alert Dialog Object
     */
    public static void dismissAlertDialog(AlertDialog alertDialog) {
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }

    /**
     * Dismiss Loading (ProgressDialog and SwipeRefreshLayout)
     * @param loadingDialog ProgressDialog
     * @param swipeRefreshLayout SwipeRefreshLayout
     */
    public static void dismissLoading(ProgressDialog loadingDialog, SwipeRefreshLayout swipeRefreshLayout) {
        // dismiss the loading dialog
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

        // dismiss refreshing layout
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Get Color
     * @param context Context
     * @param colorID int R.color.something
     * @return integer
     */
    public static int getColor(Context context, int colorID) {
        try {
            if(context != null){
                return ContextCompat.getColor(context, colorID);
            }else{
                logThis(TAG, "getColor context is null! colorID: " + colorID, null);
            }
        } catch (Exception e) {
            logThis(TAG, "getColor Exception", e);
        }
        return 0;
    }

    /**
     * Get Drawable
     * @param context Context
     * @param drawableID int
     * @return Drawable object
     */
    public static Drawable getDrawable(Context context, int drawableID) {
        try {
            if(context != null){
                return ContextCompat.getDrawable(context, drawableID);
            }else{
                logThis(TAG, "getColor context is null! colorID: " + drawableID, null);
            }
        } catch (Exception e) {
            logThis(TAG, "getDrawable Exception", e);
        }
        return null;
    }

    /**
     * Get App Version Name
     * @param context Context
     * @return String App Version Name
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            logThis(TAG, "getAppVersionName NameNotFoundException", e);
            return "";
        }
    }

    /**
     * Get App Version Code
     * @param context Context
     * @return String App Version Code Number
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * Set Actionbar Title
     * @param appCompatActivity AppCompatActivity
     * @param title String
     */
    public static void setActionBarTitle(AppCompatActivity appCompatActivity, String title) {
        if (appCompatActivity != null && appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Set Actionbar Back Button
     * @param appCompatActivity AppCompatActivity
     */
    public static void setActionBarBackButton(AppCompatActivity appCompatActivity) {
        if (appCompatActivity != null && appCompatActivity.getSupportActionBar() != null){
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Get Today Date Time
     * @param timezone TimeZone
     * @return DateTime Object
     */
    public static DateTime todayDateTime(String timezone) {
        if(timezone != null && !timezone.isEmpty()){
            return DateTime.now(DateTimeZone.forID(timezone));
        }else{
            return DateTime.now(DateTimeZone.forID("Asia/Bahrain"));
        }
    }

    /**
     * Get Today Local Date Time
     * @param timezone TimeZone
     * @return DateTime Object
     */
    public static LocalDateTime todayLocalDateTime(String timezone) {
        if(timezone != null && !timezone.isEmpty()){
            return LocalDateTime.now(DateTimeZone.forID(timezone));
        }else{
            return LocalDateTime.now(DateTimeZone.forID("Asia/Bahrain"));
        }
    }

    /**
     * Validate And Set Alternative String otherwise
     * @param text String
     * @param alternativeText String
     * @return String alternativeText
     */
    public static String validateAndSetAlternativeString(String text, String alternativeText) {
        if(validateText(text)){
            return text;
        }else{
            return alternativeText;
        }
    }

    /**
     * Is Build Flavor
     * @param flavorName String
     * @return boolean is true or not
     */
    public static boolean isBuildFlavor(String flavorName) {
        if(validateText(flavorName)){
            return BuildConfig.FLAVOR.equalsIgnoreCase(flavorName);
        }
        return false;
    }

    /**
     * Generate Now Timestamp
     * @return String generated timestamp
     */
    public static String generateNowTimestamp() {
        try {
            return String.valueOf(System.currentTimeMillis());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Open SMS Intent
     * @param context Context
     * @param number String
     */
    public static void openSMSIntent(Context context, String number) {
        try {
            if(validateText(number)) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.setData(Uri.parse("sms:" + number));
                sendIntent.putExtra("address", number);
                context.startActivity(sendIntent);
            }else{
                showToast(context, "Invalid number!", 0, false);
            }
        } catch (Exception e) {
            showToast(context, "Unable to open messaging application!", 0, false);
            logThis(TAG, "openSMSIntent Exception", e);
        }
    }

    /**
     * Parse To HTML
     * @param source HTML Source
     * @return Spanned HTML
     */
    public static Spanned parseToHTML(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    /**
     * Show Snackbar
     * @param appCompatActivity FragmentActivity
     * @param view View
     * @param text String
     * @param duration Snackbar.LENGTH_LONG or Snackbar.LENGTH_SHORT
     * @param findViewByID int R.id.coordinatorLayout
     */
    public static void showSnackbar(FragmentActivity appCompatActivity, View view, String text, int duration, int findViewByID) {
        if (text != null) {

            // validate duration
            if (duration == 0) {
                duration = Snackbar.LENGTH_LONG;
            } else {
                duration = Snackbar.LENGTH_SHORT;
            }

            View theView = view;
            if (view == null) {
                theView = appCompatActivity.findViewById(findViewByID);
            }

            Snackbar snackbar = Snackbar.make(theView, text, duration);
            snackbar.setAction("", null);
            snackbar.show();
        }
    }

    /**
     * Recurse parents children and children children views
     * @param v View
     * @return List of views
     */
    public static ArrayList<View> getAllChildrenInView(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildrenInView(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    /**
     * Dismiss Search View
     * @param searchView SearchView
     */
    public static void dismissSearchView(SearchView searchView) {
        try {
            if(searchView != null) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            logThis(TAG, "dismissSearchView Exception", e);
        }
    }

    /**
     * Convert Boolean to String
     * @param bool boolean
     * @return String true or false
     */
    public static String convertBooleanToString(boolean bool){
        return bool ? "true" : "false";
    }

    /**
     * Rotate Image
     * @param filePath String
     * @return Bitmap Image Rotated
     */
    public static Bitmap rotateImage(String filePath) {
        try {
            if(validateText(filePath)) {
                BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, bounds);

                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(filePath, opts);
                ExifInterface exif = new ExifInterface(filePath);
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
            }
        } catch (IOException e) {
            logThis(TAG, "rotateImage IOException", e);
        }
        return null;
    }

    /**
     * Convert String to Double
     * @param string String
     * @return double value
     */
    public static double convertStringToDouble(String string) {
        if (string != null && !string.isEmpty()) {
            return Double.parseDouble(string);
        } else {
            return 0.0;
        }
    }

    /**
     * Accept Image
     * @param acceptedImageFileExtensions String[]
     * @param file File
     * @return boolean
     */
    public static boolean acceptImage(String[] acceptedImageFileExtensions, File file) {
        try {
            for (String extension : acceptedImageFileExtensions)
            {
                if (file.getName().toLowerCase().endsWith(extension))
                {
                    return true;
                }
            }
        } catch (Exception e) {
            logThis(TAG, "acceptImage Exception", e);
        }
        return false;
    }

    /**
     * Accept Video
     * @param acceptedVideoFileExtensions String[]
     * @param file File
     * @return boolean
     */
    public static boolean acceptVideo(String[] acceptedVideoFileExtensions, File file) {
        try {
            for (String extension : acceptedVideoFileExtensions)
            {
                if (file.getName().toLowerCase().endsWith(extension))
                {
                    return true;
                }
            }
        } catch (Exception e) {
            logThis(TAG, "acceptVideo Exception", e);
        }
        return false;
    }

    /**
     * Hide Keyboard
     * @param fragmentActivity FragmentActivity
     */
    public static void hideKeyboard(FragmentActivity fragmentActivity) {
        try {
            View view = fragmentActivity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            logThis(TAG, "hideKeyboard Exception", e);
        }
    }

    /**
     * Convert Integer to String
     * @param number integer
     * @return String number
     */
    public static String convertIntToString(int number) {
        String string = null;
        try {
            string = Integer.toString(number);
            if (string.isEmpty()) {
                string = String.valueOf(number);
            }
        } catch (Exception e) {
            logThis(TAG, "convertIntToString Exception", e);
        }
        return string;
    }

    /**
     * Generate Date Time String
     * @param dateTime LocalDateTime
     * @return String date time (Year Month Day)
     */
    public static String generateDateTimeString(LocalDateTime dateTime) {
        return createDateTimeString(getTwoDigitsInt(dateTime.getYear()), getTwoDigitsInt(dateTime.getMonthOfYear()), getTwoDigitsInt(dateTime.getDayOfMonth()), true);
    }

    /**
     * Get Date Time from Timestamp
     * @param timeStamp String
     * @param format String
     * @return LocalDateTime
     */
    public static LocalDateTime getDateTime(String timeStamp, String format) {

        LocalDateTime dateTime = null;

        if (validateText(timeStamp)) {
            if (format != null) {
                dateTime = LocalDateTime.parse(timeStamp, DateTimeFormat.forPattern(format));
            } else {
                dateTime = LocalDateTime.parse(timeStamp);
            }
        }

        return dateTime;
    }

    /**
     * Get Shared Prefs
     * @param context Context
     * @param prefsName String
     * @param className Class String.class, Boolean.class
     * @param key String
     * @return Object
     */
    public static Object getSharedPrefs(Context context, String prefsName, Class className, String key) {

        Object object = null;

        try {
            if (className == String.class) {
                object = initSharedPrefs(context, prefsName).getString(key, null);
            } else if (className == Boolean.class) {
                object = initSharedPrefs(context, prefsName).getBoolean(key, false);
            } else if (className == Integer.class) {
                object = initSharedPrefs(context, prefsName).getInt(key, 0);
            } else if (className == Long.class) {
                object = initSharedPrefs(context, prefsName).getLong(key, 0);
            } else if (className == Float.class) {
                object = initSharedPrefs(context, prefsName).getFloat(key, 0);
            }
        } catch (Exception e) {
            logThis(TAG, "getSharedPrefs Exception", e);
        }

        return object;
    }

    /**
     * Init Shared Prefs
     * @param context Context
     * @param prefsName String
     * @return SharedPreferences
     */
    public static SharedPreferences initSharedPrefs(Context context, String prefsName) {
        return context.getSharedPreferences(prefsName, 0);
    }

    /**
     * Init Shared Prefs
     * @param context Context
     * @param prefsName String
     */
    public static void clearSharedPrefs(Context context, String prefsName) {
        try {
            initSharedPrefs(context, prefsName).edit().clear().apply();
        } catch (Exception e) {
            logThis(TAG, "clearSharedPrefs Exception " + e.getMessage(), e);
        }
    }

    /**
     * Log Intent Extras
     * @param TAG String
     * @param bundle Bundle
     */
    public static void logIntentExtras(String TAG, Bundle bundle) {
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(TAG, String.format("%s %s (%s)", key, value != null ? value.toString() : " _ ", value!= null ? value.getClass().getName() : " _ "));
            }
        }
    }

    /**
     * Save Shared Prefs One
     * @param context Context
     * @param prefsName String
     * @param key String
     * @param value Object
     */
    public static void saveSharedPrefsOne(Context context, String prefsName, String key, Object value) {

        try {
            if (key != null) {
                SharedPreferences.Editor editor = initSharedPrefs(context, prefsName).edit();
                if (value instanceof String) {
                    editor.putString(key, (String) value).apply();
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, (boolean) value).apply();
                } else if (value instanceof Long) {
                    editor.putLong(key, (long) value).apply();
                } else if (value instanceof Integer) {
                    editor.putLong(key, (int) value).apply();
                }else{
                    editor.putString(key, null).apply();
                }
            }
        } catch (Exception e) {
            logThis(TAG, "saveSharedPrefsOne Exception", e);
        }
    }

    /**
     * Save Shared Prefs List
     * @param context Context
     * @param prefsName String
     * @param hashMap Map of String and Object
     */
    public static void saveSharedPrefsList(Context context, String prefsName, Map<String, Object> hashMap) {
        try {
            SharedPreferences.Editor editor = initSharedPrefs(context, prefsName).edit();
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key != null) {

                    if (value instanceof String) {
                        editor.putString(key, (String) value).apply();
                    } else if (value instanceof Boolean) {
                        editor.putBoolean(key, (boolean) value).apply();
                    } else if (value instanceof Long) {
                        editor.putLong(key, (long) value).apply();
                    } else if (value instanceof Integer) {
                        editor.putLong(key, (int) value).apply();
                    }
                }
            }
            editor.apply();
        } catch (Exception e) {
            logThis(TAG, "saveSharedPrefsList Exception", e);
        }
    }


}
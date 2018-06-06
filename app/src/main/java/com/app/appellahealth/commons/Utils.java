package com.app.appellahealth.commons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.app.appellahealth.R;
import com.app.appellahealth.activities.LoginActivity;
import com.google.gson.Gson;

import org.xml.sax.XMLReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static String deviceDetails = "";
    public static String versionName = "";
    public static final int px100dp = (int)getDp(100);

    public static float getDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }


    public static String getVersionName(Context context){
        if(versionName.isEmpty()){
            try {
                versionName = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionName;
    }

    public static boolean LARGE_LOG_ENABLED = true;
    public static boolean LOG_ENABLED = false;
    public static boolean FILE_LOG_ENABLED = false;

    public static class Log {
        public static void e(String tag, String message) {
            if (LOG_ENABLED) {
                android.util.Log.e(tag, message);
            }
        }

        public static void d(String tag, String message) {
            if (LOG_ENABLED) {
                if (LARGE_LOG_ENABLED) {
                    if (message.length() > 4000) {
                        android.util.Log.d(tag, message.substring(0, 4000));
                        d(tag, message.substring(4000));
                    }
                } else {
                    android.util.Log.d(tag, message);
                }
            }
            if(FILE_LOG_ENABLED){
                String time = DateUtils.getFormattedDate("yyyy.MM.dd.HH:mm", System.currentTimeMillis(), DateUtils.TZ.LOC2LOC);
                appendLog(time+"\n"+tag+"::"+message);
            }
        }
    }

    public static void appendLog(String text) {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/"+Utils.class.getPackage().getName());
        dir.mkdir();
        File logFile = new File(dir, "Logs.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
                    true));
            buf.append(text);
            buf.newLine();
            buf.newLine();
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetwork(Context ctx, boolean showError) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting() && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            if (showError) {
                offlineToast(ctx);
            }
            return false;
        }
    }

    public static void offlineToast(final Context context) {
        showToast(context,context.getString(R.string.err_internet));
    }

    public static void showToast(final Context context, final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static String getUUID(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private static String getDeviceDetails(){
        if(deviceDetails.isEmpty()){
            String manufacturer = Build.MANUFACTURER;
            deviceDetails = Build.MODEL + " " + Build.BRAND +" ("
                    + Build.VERSION.RELEASE+")"
                    + " API-" + Build.VERSION.SDK_INT;

            if (deviceDetails.startsWith(manufacturer)) {
                deviceDetails = capitalize(deviceDetails);
            } else {
                deviceDetails = capitalize(manufacturer) + " " + deviceDetails;
            }
        }
        return deviceDetails;
    }

    public static void hideKb(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getMimeType(String url) {

        String type = null;
        String extension = url.substring(url.lastIndexOf(".") + 1);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void openFileURL(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url),getMimeType(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String serialize(Object object) {
        return new Gson().toJson(object);
    }

    public static Object deserialize(String objectString, Type type) {
        return new Gson().fromJson(objectString, type);
    }

    public static Intent getDialCallIntent(String number){
        return new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
    }

    public static Intent getStartCallIntent(String number){
        return new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", number, null));
    }

    public static void openURL(Context context,String url){
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void setHTMLData(TextView textView,String bodyData){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(bodyData,Html.FROM_HTML_MODE_LEGACY,null,new UlTagHandler()),TextView.BufferType.SPANNABLE);
        } else {
            textView.setText(Html.fromHtml(bodyData, null, new UlTagHandler()));
        }
    }

    public static class UlTagHandler implements Html.TagHandler{
        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                              XMLReader xmlReader) {
            if(tag.equals("ul") && !opening) output.append("\n");
            if(tag.equals("li") && opening) output.append("\n\t•");
        }
    }

    public static int getClearFlags(){
        int flags;
        if(Build.VERSION.SDK_INT >= 11) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK;
        } else {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP;
        }
        return flags;
    }

    public static class DateUtils {

        public static String SEP = "'T'";
        public static String SEP_VALUE = "T";

        public static TimeZone tzLOC = TimeZone.getDefault();
        public static TimeZone tzUTC = TimeZone.getTimeZone("GMT");

        public static enum TZ{
            UTC2UTC(tzUTC,tzUTC),UTC2LOC(tzUTC,tzLOC),LOC2UTC(tzLOC,tzUTC),LOC2LOC(tzLOC,tzLOC);
            private TimeZone source,dest;
            private TZ(TimeZone source, TimeZone dest) {
                this.source = source;
                this.dest = dest;
            }
        }

        private static SimpleDateFormat targetFormat = new SimpleDateFormat();
        private static SimpleDateFormat originalFormat = new SimpleDateFormat();
        private static String formattedDate = "";

        public static final String TIMESTAMPZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_FORMAT1 = "yyyy-MM-dd";
        public static final String DATE_FORMAT2 = "yyyy-MM-dd (EEE)";
        public static final String DATE_FORMAT3 = "EEEE MMM dd, yyyy";
        public static final String DATE_FORMAT4 = "MMMM,yyyy";
        public static final String DATE_FORMAT5 = "dd(EEEE)";
        public static final String DATE_FORMAT6 = "dd/MM";
        public static final String DATE_FORMAT7 = "MMM d, yyyy";
        public static final String TIME_FORMAT1 = "HH:mm";
        public static final String TIME_FORMAT2 = "hh:mm aa";
        public static final String TIME_FORMAT3 = "HH:mm:ss";
        public static final String TIME_FORMAT4 = "hhaa";
        public static final String TIME_FORMAT5 = "h:mm a";
        public static final String DATETIME_FORMAT1 = "dd(EEE) HH:mm";
        public static final String DATETIME_FORMAT2 = DATE_FORMAT3+" "+TIME_FORMAT2;
        public static final String DATETIME_FORMAT3 = "dd:mm"+"\n"+"yyyy";
        public static final String DATETIME_FORMAT4 = DATE_FORMAT7+" "+TIME_FORMAT5;
        public static final String DAY_FORMAT1 = "dd";

        public static Date getDateObject(String pattern, String value){
            Date parsedDate = null;
            try {
                parsedDate = (new SimpleDateFormat(pattern)).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return parsedDate;
        }

        public static String getFormattedDate(String targetPattern,
                                              String existingPattern, String existingValue, TZ tz) {

/*
            if(true){
                return Utils.getFormattedTime(existingValue);
            }
*/

            System.out.println(existingValue);
            formattedDate = existingValue;

            originalFormat.applyPattern(existingPattern);
            targetFormat.applyPattern(targetPattern);

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());

            symbols.setAmPmStrings(new String[]{"AM", "PM"});
            targetFormat.setDateFormatSymbols(symbols);

            originalFormat.setTimeZone(tz.source);
            targetFormat.setTimeZone(tz.dest);

            formattedDate = targetFormat.format(originalFormat
                    .parse(existingValue, new ParsePosition(0)));
            return formattedDate;
        }

        public static String getFormattedDate(String targetPattern,
                                              long existingValueMillis, TZ tz) {
            System.out.println(existingValueMillis+"");


/*
            originalFormat.applyPattern(TIMESTAMP_FORMAT);
            targetFormat.applyPattern(targetPattern);

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());

            symbols.setAmPmStrings(new String[]{"AM", "PM"});
            targetFormat.setDateFormatSymbols(symbols);

            originalFormat.setTimeZone(tz.source);
            targetFormat.setTimeZone(tz.dest);

            String existingValue = originalFormat.format(new Date(existingValueMillis));
            formattedDate = existingValue;


            try {
                formattedDate = targetFormat.format(originalFormat
                        .parse(existingValue));
            } catch (ParseException e) {
                e.printStackTrace();
            }

*/


            targetFormat.applyPattern(targetPattern);

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());

            symbols.setAmPmStrings(new String[]{"AM", "PM"});
            targetFormat.setDateFormatSymbols(symbols);

            targetFormat.setTimeZone(tz.dest);

            formattedDate = targetFormat.format(new Date(existingValueMillis));

            return formattedDate;
        }
    }

    public static void openVideo(Context context, String videoPath){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4");
        context.startActivity(intent);
    }

    public static void logout(Activity context) {
        Prefs prefs = new Prefs(context.getApplicationContext());
        prefs.editPrefs();
        prefs.putBoolean(Prefs.KEY_is_logged_in, false);
        prefs.commitPrefs();
        navigateToLogin(context);
    }

    public static void navigateToLogin(Activity context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.setFlags(Utils.getClearFlags());
        context.startActivity(loginIntent);
        context.finish();
    }



}

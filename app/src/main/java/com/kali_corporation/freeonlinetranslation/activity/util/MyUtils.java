package com.kali_corporation.freeonlinetranslation.activity.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings.Secure;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.kali_corporation.freeonlinetranslation.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MyUtils {
    public static Bitmap cropBit;
    public static Uri cropUri;
    public static Editor editor;
    public static SharedPreferences pref;

    public MyUtils(Context cn) {
        pref = cn.getSharedPreferences(cn.getPackageName(), 0);
        editor = pref.edit();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        boolean z = false;
        inImage.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        try {
            return Uri.parse(Media.insertImage(inContext.getContentResolver(), inImage, "Title", null));
        } catch (Exception e) {
            Log.e("AAA", e.toString());
//            return z;         //--change
            return null;
        }
    }

    public static Bitmap getBitmapResize(Context cn, Bitmap mainBitmap, int lw, int lh) {
        int newheight;
        int newwidth;
        int layoutwidth = lw;
        int layoutheight = lh;
        int imagewidth = mainBitmap.getWidth();
        int imageheight = mainBitmap.getHeight();
        if (imagewidth >= imageheight) {
            newwidth = layoutwidth;
            newheight = (newwidth * imageheight) / imagewidth;
            if (newheight > layoutheight) {
                newwidth = (layoutheight * newwidth) / newheight;
                newheight = layoutheight;
            }
        } else {
            newheight = layoutheight;
            newwidth = (newheight * imagewidth) / imageheight;
            if (newwidth > layoutwidth) {
                newheight = (newheight * layoutwidth) / newwidth;
                newwidth = layoutwidth;
            }
        }
        return Bitmap.createScaledBitmap(mainBitmap, newwidth, newheight, true);
    }

    public static boolean checknotificationaccess(Context cn) {
        boolean b;
        try {
            b = Secure.getString(cn.getContentResolver(), "enabled_notification_listeners").contains(cn.getPackageName());
        } catch (Exception e) {
            b = false;
        }
        if (!b) {
            Toast(cn, cn.getString(R.string.app_name) + " Select and Give Permission", 1);
            cn.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
        return b;
    }

    public static String dtformt(Long str) {
        return time_msg(str.longValue(), System.currentTimeMillis(), 1000).toString();
    }

    private static CharSequence time_msg(long j, long j2, long j3) {
        StringBuilder stringBuilder = new StringBuilder();
        Resources.getSystem();
        Integer obj = j2 >= j ? Integer.valueOf(1) : null;
        long abs = Math.abs(j2 - j);
        if (abs < 60000 && j3 < 60000) {
            long abs2 = abs / 1000;
            if (abs2 <= 10) {
                stringBuilder.append("Just Now");
            } else if (obj != null) {
                stringBuilder.append(abs2);
                stringBuilder.append("s");
            } else {
                stringBuilder.append("Just Now");
            }
        } else if (abs < 3600000 && j3 < 3600000) {
            long abs3 = abs / 60000;
            if (obj != null) {
                stringBuilder.append(abs3);
                stringBuilder.append("m");
            } else {
                stringBuilder.append("Just Now");
            }
        } else if (abs < 86400000 && j3 < 86400000) {
            long abs4 = abs / 3600000;
            if (obj != null) {
                stringBuilder.append(abs4);
                stringBuilder.append("h");
            } else {
                stringBuilder.append("Just Now");
            }
        } else if (abs >= 604800000 || j3 >= 604800000) {
            stringBuilder.append(DateUtils.formatDateRange(null, j, j, 0));
        } else {
            stringBuilder.append(DateUtils.getRelativeTimeSpanString(j, j2, j3));
        }
        return stringBuilder.toString();
    }

    public static String getContactName(Context context, String phoneNumber) {
        Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)), new String[]{"display_name"}, null, null, null);
        if (cursor == null) {
            return phoneNumber;
        }
        String contactName = phoneNumber;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex("display_name"));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    public static boolean generatetxt(Context context, String path, String sFileName, String sBody) {
        try {
            File root = new File(path);
            if (!root.exists()) {
                root.mkdirs();
            }
            FileWriter writer = new FileWriter(new File(root, sFileName));
            writer.append(sBody);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void sharetextonWhatsApp(Context cn, String txt) {
        Intent whatsappIntent = new Intent("android.intent.action.SEND");
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra("android.intent.extra.TEXT", txt);
        try {
            cn.startActivity(whatsappIntent);
        } catch (ActivityNotFoundException e) {
            Toast(cn, "Whatsapp have not been installed.");
        }
    }

    public static void share(Context cn) {
        String shareBody = "https://play.google.com/store/apps/details?id=" + cn.getPackageName();
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra("android.intent.extra.TEXT", shareBody);
        cn.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void rate(Context cn) {
        try {
            cn.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + cn.getPackageName())));
        } catch (ActivityNotFoundException e) {
            cn.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + cn.getPackageName())));
        }
    }

    public static LayoutParams getParamsR(Context cn, int width, int height) {
        return new LayoutParams((cn.getResources().getDisplayMetrics().widthPixels * width) / 1080, (cn.getResources().getDisplayMetrics().heightPixels * height) / 1920);
    }

    public static LinearLayout.LayoutParams getParamsL(Context cn, int width, int height) {
        return new LinearLayout.LayoutParams((cn.getResources().getDisplayMetrics().widthPixels * width) / 1080, (cn.getResources().getDisplayMetrics().heightPixels * height) / 1920);
    }

    public static void copyClipboard(Context cn, String txt) {
        ((ClipboardManager) cn.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("copy", txt));
        Toast(cn, "Copy Message...");
    }

    public static String getNumbers(String string) {
        return string.replaceAll("[^0-9]", "");
    }

    public static Bitmap getMask(Context mContext, Bitmap userimageBitmap, int rs) {
        Bitmap maskBitmap = BitmapFactory.decodeResource(mContext.getResources(), rs);
        int w = mContext.getResources().getDisplayMetrics().widthPixels;
        int m = (w * 15) / 1080;
        Bitmap userimageBitmap2 = Bitmap.createScaledBitmap(userimageBitmap, (w / 3) - (m * 2), (w / 3) - (m * 2), true);
        Bitmap maskBitmap2 = Bitmap.createScaledBitmap(maskBitmap, (w / 3) - (m * 2), (w / 3) - (m * 2), true);
        Bitmap result = Bitmap.createBitmap(maskBitmap2.getWidth(), maskBitmap2.getHeight(), Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint mPaint = new Paint(1);
        mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        mCanvas.drawBitmap(userimageBitmap2, 0.0f, 0.0f, null);
        mCanvas.drawBitmap(maskBitmap2, 0.0f, 0.0f, mPaint);
        mPaint.setXfermode(null);
        return result;
    }

    public static void copyFileUsingStream(File source, String folderName, String filename) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            String path = Environment.getExternalStorageDirectory() + "/MyStatus/" + folderName;
            File f = new File(path);
            if (!f.exists()) {
                f.mkdir();
            }
            File fd = new File(path + "/" + filename);
            InputStream is2 = new FileInputStream(source);
            try {
                OutputStream os2 = new FileOutputStream(fd);
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int length = is2.read(buffer);
                        if (length > 0) {
                            os2.write(buffer, 0, length);
                        } else {
                            is2.close();
                            os2.close();
                            return;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    os = os2;
                    is = is2;
                    is.close();
                    os.close();
                    throw th;
                }
            } catch (Throwable th2) {
                th2 = th2;
                is = is2;
                is.close();
                os.close();
                throw th2;
            }
        } catch (Throwable th3) {
            th3 = th3;
            is.close();
            os.close();
            try {
                throw th3;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static ArrayList<File> getListFiles(File parentDir) {
        File[] files;
        ArrayList<File> inFiles = new ArrayList<>();
        if (parentDir.exists()) {
            for (File file : parentDir.listFiles()) {
                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public static ArrayList<String> getListFiles(String path) {
        File[] files;
        ArrayList<String> inFiles = new ArrayList<>();
        File parentDir = new File(path);
        if (parentDir.exists()) {
            for (File file : parentDir.listFiles()) {
                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file.getPath()));
                } else {
                    inFiles.add(file.getPath());
                }
            }
        }
        return inFiles;
    }

    public static ArrayList<String> getFolderfiles(String path) {
        File[] files;
        ArrayList<String> inFiles = new ArrayList<>();
        File parentDir = new File(path);
        if (parentDir.exists()) {
            for (File file : parentDir.listFiles()) {
                if (!file.isDirectory()) {
                    inFiles.add(file.getPath());
                }
            }
        }
        return inFiles;
    }

    public static String getFileExtension(String path) {
        String name = new File(path).getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String[] permission) {
        boolean b = true;
        for (String p : permission) {
            if (ActivityCompat.checkSelfPermission(context, p) == 0) {
                b = true;
            } else {
                b = false;
            }
            if (!b) {
                break;
            }
        }
        if (!b) {
            ActivityCompat.requestPermissions((Activity) context, permission, 101);
        }
        return b;
    }

    public static void Toast(Context cn, String msg) {
        Toast(cn, msg, 0);
    }

    public static void Toast(Context cn, String msg, int lenght) {
        Toast.makeText(cn, msg, lenght).show();
    }

    public static long gettimeskip(int min) {
        Calendar c = Calendar.getInstance();
        long timeInMillis = c.getTimeInMillis();
        c.add(12, min);
        c.set(13, 0);
        return c.getTimeInMillis();
    }

    public static String gettimestring(long mili, String pattern) {
        return new SimpleDateFormat(pattern).format(Long.valueOf(mili));
    }

    public static long getdatefromstring(String pattern, String sdate) throws ParseException {
        return new SimpleDateFormat(pattern).parse(sdate).getTime();
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            try {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isNetworkConnected(Context cn) {
        boolean z;
        ConnectivityManager cm = (ConnectivityManager) cn.getSystemService("connectivity");
        if (cm.getActiveNetworkInfo() != null) {
            z = true;
        } else {
            z = false;
        }
        if (!Boolean.valueOf(z).booleanValue()) {
            Toast(cn, "Need Internet Connection");
        }
        if (cm.getActiveNetworkInfo() != null) {
            return true;
        }
        return false;
    }

    public static Uri getBitmapUri(Context cn, Bitmap cropBit2) {
        cropBit2.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(Media.insertImage(cn.getContentResolver(), cropBit2, "Title", null));
    }
}

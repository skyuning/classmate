package com.example.classmate.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    @SuppressWarnings("deprecation")
    public static String uri2Path(Activity activity, Uri uri) {
        String[] columns = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, columns, null, null, null);
        if (cursor == null)
            return null;

        try {
            int idx = cursor.getColumnIndexOrThrow(columns[0]);
            if (cursor.moveToFirst())
                return cursor.getString(idx);
            else
                return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isEmptyString(CharSequence s) {
        if (s.equals("null"))
            return true;
        if (TextUtils.isEmpty(s))
            return true;
        return false;
    }

    public static String md5(String s) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10)
                hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }
    
    // maxWidth 必须小于等于 maxHeight
    // http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    public static Bitmap getThumb(String filePath, int maxWidth, int maxHeight) {
        // 获取Bitmap的Size信息
        // JustDecodeBounds
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(new FileInputStream(new File(filePath)), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        int scale = 1;

        int outWidth = 0;
        int outHeight = 0;
        if (options.outWidth <= options.outHeight) {
            outWidth = options.outWidth;
            outHeight = options.outHeight;
        } else {
            outWidth = options.outHeight;
            outHeight = options.outWidth;
        }
        while (outWidth / scale > maxWidth || outHeight / scale > maxHeight)
            scale *= 2;

        options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        try {
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(new File(filePath)), null, options);
            return bmp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void hideSoftInput(Activity activity) {
        View curfocus = activity.getCurrentFocus();
        if (null == curfocus)
            return;
                
        IBinder windowToken = curfocus.getWindowToken();
        if (null == windowToken)
            return;
        
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

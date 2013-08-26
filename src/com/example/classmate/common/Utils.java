package com.example.classmate.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

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
}

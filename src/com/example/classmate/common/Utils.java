package com.example.classmate.common;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Utils {

    @SuppressWarnings("deprecation")
    public static String uri2Path(Activity activity, Uri uri) {
        String imagePath = uri.getPath();
        if (uri.toString().startsWith("content://")) {
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            if (cursor != null) {
                try {
                    int idx = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        imagePath = cursor.getString(idx);
                    }
                } catch (IllegalArgumentException e) {
                }
            }
        }
        return imagePath;
    }
}

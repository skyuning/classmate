package com.example.classmate.common;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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
}

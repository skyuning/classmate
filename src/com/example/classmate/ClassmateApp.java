package com.example.classmate;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ClassmateApp extends Application {
    public static LruCache<String, Bitmap> sImageCache;
    static {
        sImageCache = new LruCache<String, Bitmap>(4 * 1000 * 1000);
    }
}

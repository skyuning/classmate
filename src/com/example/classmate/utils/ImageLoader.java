package com.example.classmate.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.example.classmate.ClassmateApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoader {
    
    private static int loadingImageResId = android.R.drawable.stat_notify_sync;
    private static int faildImageResId = android.R.drawable.ic_menu_report_image;
    
    public static void loadImage(final Context context, final ImageView iv) {
        String path = (String) iv.getTag();
        Bitmap bm = ClassmateApp.sImageCache.get(path);
        if (null != bm) {
            iv.setImageBitmap(bm);
        } else {
            iv.setImageResource(loadingImageResId);
            asyncLoadImage(context, iv);
        }
    }
    
    public static void asyncLoadImage(final Context context, final ImageView iv) {
        final String path = (String) iv.getTag();
                
        AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {
            
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    URL imgUrl = new URL(path);
                    URLConnection conn = imgUrl.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    return bm;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (! path.equals(iv.getTag()))
                    return;
                if (null == result) {
                    result = BitmapFactory.decodeResource(context.getResources(), faildImageResId);
                    iv.setImageBitmap(result);
                } else {
                    iv.setImageBitmap(result);
                }
                ClassmateApp.sImageCache.put(path, result);
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

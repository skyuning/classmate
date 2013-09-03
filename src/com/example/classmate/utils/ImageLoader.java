package com.example.classmate.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.Executor;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;

import com.example.classmate.ClassmateApp;
import com.example.classmate.common.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ImageLoader {
    
    private static String imageDir = "image/";
    private static int loadingImageResId = android.R.drawable.stat_notify_sync;
    private static int faildImageResId = android.R.drawable.ic_menu_report_image;
    
//    private static final int CORE_POOL_SIZE = 5;
//    private static final int MAXIMUM_POOL_SIZE = 128;
//    private static final int KEEP_ALIVE = 1;
//    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
//        private final AtomicInteger mCount = new AtomicInteger(1);
//        public Thread newThread(Runnable r) {
//            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
//        }
//    };
//    private static final BlockingQueue<Runnable> sPoolWorkQueue =
//            new LinkedBlockingQueue<Runnable>(10);
//    private static final Executor THREAD_POOL_EXECUTOR
//            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
//                    TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    
    private static LruCache<String, Bitmap> sImageCache;
    static {
        sImageCache = new LruCache<String, Bitmap>(4 * 1000 * 1000);
    }
    
    public static String getImageDir() {
        String absImageDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + ClassmateApp.storageRootDir + imageDir;
        File dir = new File(absImageDir);
        if (! dir.exists())
            dir.mkdirs();
        return absImageDir;
    }
    
    public static void loadImage(final Context context, final ImageView iv) {
        String url = (String) iv.getTag();
        Object object = sImageCache.get(url);
        if (object != null) {
            iv.setImageBitmap((Bitmap) object);
            return;
        }
        
        String localPath = getImageDir() + Utils.md5(url) + ".png";
        Bitmap bm = BitmapFactory.decodeFile(localPath);
        if (bm != null) {
            iv.setImageBitmap(bm);
            sImageCache.put(url, bm);
            Log.d("local image", localPath);
            return;
        }
        
        iv.setImageResource(loadingImageResId);
        loadImageFromNet(context, iv);
    }
    
    public static void loadImageFromNet(final Context context, final ImageView iv) {
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
                if (result == null) {
                    result = BitmapFactory.decodeResource(context.getResources(), faildImageResId);
                    iv.setImageBitmap(result);
                    LayoutParams params = (LayoutParams) iv.getLayoutParams();
                    params.height = LayoutParams.WRAP_CONTENT;
                    iv.setLayoutParams(params);
                    return;
                } 
                iv.setImageBitmap(result);
                sImageCache.put(path, result);
                String url = (String) iv.getTag();
                String localPath = getImageDir() + Utils.md5(url);
                try {
                    File myCaptureFile = new File(localPath + ".png");
                    myCaptureFile.createNewFile();
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(myCaptureFile));
                    result.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    bos.flush();
                    bos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        asyncTask.execute();
    }
}

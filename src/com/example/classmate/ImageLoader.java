package com.example.classmate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

public class ImageLoader {
    public static void loadImage(final ImageView iv) {
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
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (null == result)
                    return;
                if (! path.equals(iv.getTag()))
                    return;
                
                iv.setImageBitmap(result);
                iv.setVisibility(View.VISIBLE);
            }
        };
        asyncTask.execute();
    }
}

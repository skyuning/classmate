package com.example.classmate;

import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Gallery;

public class AlbumActivity extends Activity {

    @ViewInject(id = R.id.gallery)
    private Gallery mGallery;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.album, menu);
        return true;
    }

}

package com.example.classmate;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class WelcomeActivity extends Activity {

    private AsyncTask<Void, Void, Void> mAsyncTask;
    
    @ViewInject(id = android.R.id.content)
    private View contentView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ViewAnnotation.bind(this, this);
        
        mAsyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                onPostLoading();
            }
        };
        mAsyncTask.execute();
        
        contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncTask.cancel(true);
                onPostLoading();
            }
        });
    }

    private void onPostLoading() {
        SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
        String token = sp.getString("token", null);
        if (token != null)
            startActivity(new Intent(this, MainActivity.class));
        else
            startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

}

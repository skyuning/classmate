package com.example.classmate.common;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {
    
    private static Toast sToast;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showToast(String text) {
        if (null == sToast)
            sToast = new Toast(this);
        sToast.setText(text);
        sToast.show();
    }
}

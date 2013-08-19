package com.example.classmate.common;

import android.app.Activity;
import android.widget.Toast;

public class BaseActivity extends Activity {
    
    private static Toast sToast;

    public void showToast(String text) {
        if (null == sToast)
            sToast = new Toast(this);
        sToast.setText(text);
        sToast.show();
    }
}

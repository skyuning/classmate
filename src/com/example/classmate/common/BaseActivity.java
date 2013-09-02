package com.example.classmate.common;

import com.example.classmate.R;
import com.example.classmate.utils.WindowAttr;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {
    
    private String mTitle;
    private FrameLayout mTitleBar;
    private TextView mTitleTextView;
    private ImageButton mRightImgBtn;
    private Button mRightBtn;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowAttr anno = getClass().getAnnotation(WindowAttr.class);
        if (anno != null)
            mTitle = anno.title();
    }

    @Override
    public void setContentView(int layoutResID) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.setContentView(layoutResID);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        initTitleBar();
    }
    
    private void initTitleBar() {
        mTitleBar = (FrameLayout) findViewById(R.id.title_bar);
        mTitleTextView = (TextView) mTitleBar.findViewById(R.id.title);
        mRightImgBtn = (ImageButton) mTitleBar.findViewById(R.id.right_img_btn);
        mRightBtn = (Button) mTitleBar.findViewById(R.id.right_btn);
        setRightImgBtn(-1, null);
        setRightBtn(null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTitleTextView.setText(mTitle);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void setRightImgBtn(int resId, OnClickListener listener) {
        if (resId == -1) {
            mRightImgBtn.setVisibility(View.GONE);
            mRightImgBtn.setOnClickListener(null);
        } else {
            mRightImgBtn.setVisibility(View.VISIBLE);
            mRightImgBtn.setImageResource(resId);
            mRightImgBtn.setOnClickListener(listener);
        }
    }
    
    protected void setRightBtn(String text, OnClickListener listener) {
        if (text == null) {
            mRightBtn.setVisibility(View.GONE);
            mRightBtn.setOnClickListener(null);
        } else {
            mRightBtn.setVisibility(View.VISIBLE);
            mRightBtn.setText(text);
            mRightBtn.setOnClickListener(listener);
        }
    }
}

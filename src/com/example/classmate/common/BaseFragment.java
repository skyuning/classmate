package com.example.classmate.common;

import com.example.classmate.R;
import com.example.classmate.utils.WindowAttr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseFragment extends Fragment {

    private String mTitle = "no title";
    private FrameLayout mTitleFrame;
    private FrameLayout mTitleBar;
    private TextView mTitleTextView;
    private ImageButton mRightImgBtn;
    private Button mRightBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        WindowAttr anno = getClass().getAnnotation(WindowAttr.class);
        if (anno != null)
            mTitle = anno.title();
        initTitleBar();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    private void initTitleBar() {
        mTitleFrame = (FrameLayout) getActivity().findViewById(R.id.title_bar_frame);
        mTitleBar = (FrameLayout) getActivity().getLayoutInflater().inflate(R.layout.title_bar, null);
        mTitleFrame.addView(mTitleBar);
        mTitleTextView = (TextView) mTitleBar.findViewById(R.id.title);
        mRightImgBtn = (ImageButton) mTitleBar.findViewById(R.id.right_img_btn);
        mRightBtn = (Button) mTitleBar.findViewById(R.id.right_btn);
        setRightImgBtn(-1, null);
        setRightBtn(null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTitleTextView.setText(mTitle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTitleFrame.removeView(mTitleBar);
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

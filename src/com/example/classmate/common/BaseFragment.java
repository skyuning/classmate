package com.example.classmate.common;

import com.example.classmate.MainActivity;
import com.example.classmate.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseFragment extends Fragment {
    
    private String mTitle = "no title";
//    private FrameLayout mTitleFrame;
//    private FrameLayout mTitleBar;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
//        mTitleFrame = (FrameLayout) getActivity().findViewById(R.id.title_frame);
//        mTitleBar = (FrameLayout) getActivity().getLayoutInflater().inflate(R.layout.title_bar, null);
//        mTitleFrame.addView(mTitleBar);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        TextView titleView = (TextView) mTitleBar.findViewById(R.id.title);
//        titleView.setText(mTitle);
        getActivity().setTitle(mTitle);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mTitleFrame.removeView(mTitleBar);
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
    
    protected void setTitle(String title) {
        mTitle = title;
    }
    
//    protected ImageButton getRightImgBtn() {
//        ImageButton rightImgBtn = (ImageButton) mTitleBar.findViewById(R.id.right_img_btn);
//        return rightImgBtn;
//    }
}

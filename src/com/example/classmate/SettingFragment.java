package com.example.classmate;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.utils.WindowAttr;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

@WindowAttr(title = "设置")
public class SettingFragment extends BaseFragment implements OnClickListener {

    @ViewInject(id = R.id.profile)
    private View mProfileView;

    @ViewInject(id = R.id.album)
    private View mAlbumView;

    @ViewInject(id = R.id.about)
    private View mAboutView;

    @ViewInject(id = R.id.update)
    private View mUpdateView;

    @ViewInject(id = R.id.feekback)
    private View mFeedbackView;

    @ViewInject(id = R.id.copyright)
    private View mCopyrightView;
    
    @ViewInject(id = R.id.logout)
    private Button mLogoutBtn;

    private LinearLayout mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_more, null);
        ViewAnnotation.bind(mLayout, this);
        mProfileView.setOnClickListener(this);
        mAlbumView.setOnClickListener(this);
        mAboutView.setOnClickListener(this);
        mUpdateView.setOnClickListener(this);
        mFeedbackView.setOnClickListener(this);
        mCopyrightView.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);

        return mLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.profile:
            startActivity(new Intent(getActivity(), UserProfileActivity.class));
            break;
            
        case R.id.album:
            startActivity(new Intent(getActivity(), AlbumActivity.class));
            break;
            
        case R.id.logout:
            getActivity().getSharedPreferences("session", Context.MODE_PRIVATE)
            .edit().clear().commit();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            break;

        default:
            break;
        }
    }
}

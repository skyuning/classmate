package com.example.classmate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallback;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.requests.ListRequest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public abstract class BaseListFragment extends BaseFragment implements
        OnItemClickListener, OnScrollListener {

    @ViewInject(id = R.id.listview)
    protected ListView mListView;

    private LinearLayout mLayout;
    protected List<JSONObject> mData;
    protected CommonAdapter mAdapter;
    protected int mPage;
    private boolean mIsLoading;
    private View mLoadingFooter;
    private View mHeaderView;

    private TextView mTimeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_classmate_list, null);
        ViewAnnotation.bind(mLayout, this);

        // header
        mHeaderView = inflater.inflate(R.layout.classmate_list_header, null);
        mTimeView = (TextView) mHeaderView.findViewById(R.id.time);
        mTimeView.setText(calcTime());
        mListView.addHeaderView(mHeaderView, null, false);

        // footer
        mLoadingFooter = inflater.inflate(R.layout.list_footer_loading, null);
        View loadingView = mLoadingFooter.findViewById(R.id.loading);
        Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotate);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        loadingView.startAnimation(rotateAnimation);
        mListView.addFooterView(mLoadingFooter);

        mData = new ArrayList<JSONObject>();
        mAdapter = getAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        mIsLoading = false;
        mPage = 0;

        return mLayout;
    }
    
    private String calcTime() {
        SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd");
        try {
            Date start = formater.parse("1998-07-01");
            Date end = new Date();
            int year = end.getYear() - start.getYear();
            
            Calendar cal = Calendar.getInstance();
            if (end.getMonth() > start.getMonth()) {
                cal.set(end.getYear() + 1900, start.getMonth(), start.getDate());
            } else {
                cal.set(end.getYear() + 1990 - 1, start.getMonth(), start.getDate());
                year--;
            }
            start = cal.getTime();
            int day = (int) ((end.getTime() - start.getTime()) / 24 / 3600 / 1000);
            
            String time = String.format("已毕业%d年%d天", year, day);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void loadOnePageData(int page) {
        ListRequest request = getRequest();
        request.setPage(page);
        XHttpCallback callback = new XHttpCallbacks.DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                @SuppressWarnings("unchecked")
                List<JSONObject> data = (List<JSONObject>) result.data;
                if (data.isEmpty()) {
                    mLoadingFooter.setVisibility(View.INVISIBLE);
                    mListView.setOnScrollListener(null);
                } else {
                    mData.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }
                mIsLoading = false;
            };

            @Override
            public void onFaild(AHttpResult result) {
                mIsLoading = false;
                // Toast.makeText(getActivity(), result.e.getMessage(),
                // Toast.LENGTH_LONG).show();
            }
        };
        XHttpClient.sendRequest(request, callback);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (view.getLastVisiblePosition() < view.getCount() - 1)
            return;
        if (mIsLoading)
            return;

        mPage++;
        mIsLoading = true;
        loadOnePageData(mPage);
        Log.d("debug", "load page " + mPage);
    }

    protected abstract CommonAdapter getAdapter();

    protected abstract ListRequest getRequest();
}
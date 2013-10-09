package com.example.classmate;

import java.util.List;

import org.json.JSONObject;
import org.xframe.http.XHttpCallback;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.CommonAdapter;
import com.example.classmate.requests.ListRequest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

public class ListLayout extends FrameLayout implements
        OnItemClickListener, OnScrollListener {

    protected ListView mListView;
    private LayoutInflater mInflater;
    private List<JSONObject> mData;
    private CommonAdapter mAdapter;
    private ListRequest mRequest;
    private int mPage;
    private boolean mIsLoading;
    private View mLoadingFooter;
    
    public ListLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    public ListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListLayout(Context context) {
        super(context);
        init();
    }
    
    private void init() {
        mInflater = LayoutInflater.from(getContext());
        
        // init ListView
        mListView = new ListView(getContext());
        addView(mListView);
        mListView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mListView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
        
        // init footer
        mLoadingFooter = mInflater.inflate(R.layout.list_footer_loading, null);
        View loadingView = mLoadingFooter.findViewById(R.id.loading);
        Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        loadingView.startAnimation(rotateAnimation);
        mListView.addFooterView(mLoadingFooter, null, false);

        mListView.setOnItemClickListener(this);
        
        mIsLoading = false;
        mPage = 0;
    }
    
    public void start() {
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
    }
    
    public void restart() {
        mIsLoading = false;
        mPage = 0;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        mListView.setOnScrollListener(this);
    }
    
    public ListView getListView() {
        return mListView;
    }

    private void loadOnePageData(int page) {
        mRequest.setPage(page);
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
        XHttpClient.sendRequest(mRequest, callback);
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

    protected void setAdapter(CommonAdapter adapter) {
        mAdapter = adapter;
        mData = (List<JSONObject>) mAdapter.getData();
    };

    protected void setRequest(ListRequest request) {
        mRequest = request;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
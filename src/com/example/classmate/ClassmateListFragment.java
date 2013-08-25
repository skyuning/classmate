package com.example.classmate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;
import org.xframe.http.XHttpRequest;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.requests.ListRequest;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ClassmateListFragment extends BaseFragment {

    @ViewInject(id = R.id.listview)
    private ListView mListView;

    private LinearLayout mLayout;
    private List<JSONObject> mData;
    private ClassmateAdapter mAdapter;
    private int mPage;
    private boolean mIsLoading;
    private View mLoadingFooter;
    private View mHeaderView;
    private TextView mTimeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setTitle("同学们");

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_classmate_list, null);
        ViewAnnotation.bind(mLayout, this);

        mData = new ArrayList<JSONObject>();
        mAdapter = new ClassmateAdapter(getActivity(), mData);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        mListView.setOnScrollListener(new _OnScrollListener());

        // header
        mHeaderView = inflater.inflate(R.layout.classmate_list_header, null);
        mTimeView = (TextView) mHeaderView.findViewById(R.id.time);
        mListView.addHeaderView(mHeaderView);

        // footer
        mListView.addFooterView(inflater.inflate(R.layout.listitem_wraper, null));
        mLoadingFooter = inflater.inflate(R.layout.list_footer_loading, null);
        View loadingView = mLoadingFooter.findViewById(R.id.loading);
        Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotate);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        loadingView.startAnimation(rotateAnimation);
        mListView.addFooterView(mLoadingFooter);

        mListView.setOnScrollListener(new _OnScrollListener());
        mListView.setAdapter(mAdapter);

        mIsLoading = false;
        mPage = 0;

        return mLayout;
    }

    private void loadOnePageData(int page) {
        XHttpRequest listClassmate = new ListRequest(getActivity(), "user",
                page);
        XHttpClient.sendRequest(listClassmate,
                new XHttpCallbacks.DefaultHttpCallback() {
                    @Override
                    public void onSuccess(AHttpResult result) {
                        @SuppressWarnings("unchecked")
                        List<JSONObject> data = (List<JSONObject>) result.data;
                        if (data.isEmpty())
                            mLoadingFooter.setVisibility(View.INVISIBLE);
                        mData.addAll(data);
                        mAdapter.notifyDataSetChanged();
                        mIsLoading = false;
                    };

                    @Override
                    public void onFaild(AHttpResult result) {
                        mIsLoading = false;
                        // Toast.makeText(getActivity(), result.e.getMessage(),
                        // Toast.LENGTH_LONG).show();
                    }
                });
    }

    private class _OnScrollListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                int visibleItemCount, int totalItemCount) {
            if (mListView.getFooterViewsCount() == 0)
                return;
            if (view.getLastVisiblePosition() < view.getCount() - 1)
                return;
            if (mIsLoading)
                return;

            mPage++;
            mIsLoading = true;
            loadOnePageData(mPage);
            Log.d("debug", "load page " + mPage);
        }
    }

    private class _OnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            Intent intent = new Intent(getActivity(),
                    ClassmateDetailActivity.class);
            // Classmate classmate = (Classmate)
            // parent.getItemAtPosition(position);
            // JSONObject jo = JSONUtils.java2JsonObject(classmate, new
            // JSONObject());
            JSONObject jo = (JSONObject) parent.getItemAtPosition(position);
            intent.putExtra("classmate_detail", jo.toString());
            startActivity(intent);
        }
    }

    private static class ClassmateAdapter extends CommonAdapter {

        public ClassmateAdapter(Context context, List<JSONObject> data) {
            super(context, data);
            // TODO Auto-generated constructor stub
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();

            JSONObject item = (JSONObject) getItem(position);
            String name = item.optString("u_name");
            String phone = item.optString("u_cellphone");
            holder.name.setText(name);
            holder.phone.setText(phone);
            return v;
        }

        @Override
        protected Object newViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected int getResId() {
            return R.layout.listitem_classmate;
        }

        private static class ViewHolder {
            @ViewInject(id = R.id.name)
            TextView name;

            @ViewInject(id = R.id.phone)
            TextView phone;
        }
    }
}

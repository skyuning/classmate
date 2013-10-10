package com.example.classmate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.data.Holiday;
import com.example.classmate.requests.HolidayListRequest;
import com.example.classmate.utils.WindowAttr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@WindowAttr(title = "纪念日")
public class HolidaysFragment extends BaseFragment implements OnClickListener {
    
    private static final int REQUEST_ADD_HOLIDAY = 11;

    @ViewInject(id = R.id.listview)
    private ListView mListView;

    private LinearLayout mLayout;
    private List<Holiday> mData;
    private CommemorationAdapter mAdapter;
    private View mLoadingFooter;
    private int mPage;
    private boolean mIsLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_holiday_list, null);
        ViewAnnotation.bind(mLayout, this);

        // footer
        mLoadingFooter = inflater.inflate(R.layout.list_footer_loading, null);
        View loadingView = mLoadingFooter.findViewById(R.id.loading);
        Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotate);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        loadingView.startAnimation(rotateAnimation);
        mListView.addFooterView(mLoadingFooter);

        mData = new ArrayList<Holiday>();
        mAdapter = new CommemorationAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        mListView.setOnScrollListener(new _OnScrollListener());

        mIsLoading = false;
        mPage = 0;
        
        return mLayout;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        setRightImgBtn(R.drawable.icon_write, this);
    }

    private void loadOnePageData(int page) {
        XHttpClient.sendRequest(new HolidayListRequest(getActivity(), page),
                new XHttpCallbacks.DefaultHttpCallback() {
            
            @Override
            public void onPostExecute(AHttpResult result) {
                mIsLoading = false;
            }
            
            @Override
            public void onSuccess(AHttpResult result) {
                @SuppressWarnings("unchecked")
                List<Holiday> data = (List<Holiday>) result.data;
                
                if (data.isEmpty()) {
                    mLoadingFooter.setVisibility(View.INVISIBLE);
                    mListView.setOnScrollListener(null);
                } else {
                    mData.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }
            };
        });
    }

    private class _OnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
             Intent intent = new Intent(getActivity(), HolidayDetailActivity.class);
             intent.putExtra("holiday",
                     (Serializable) parent.getItemAtPosition(position));
             startActivity(intent);
        }
    }

    private static class CommemorationAdapter extends CommonAdapter {

        public CommemorationAdapter(Context context, List<Holiday> mData) {
            super(context, mData);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View mainView = null;
            if (convertView == null) {
                mainView = createMainView(position, parent);
                FrameLayout wraper = new FrameLayout(mContext);
                wraper.setPadding(20, 20, 20, 20);
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                wraper.setLayoutParams(params);
                wraper.addView(mainView);
                convertView = wraper;
            } else {
                mainView = convertView.findViewById(R.id.main);
            }
            
            Holiday item = (Holiday) getItem(position);
            ViewHolder holder = (ViewHolder) mainView.getTag();
            
            if (item.category == 1) {
                mainView.setBackgroundResource(R.drawable.bg_listitem_holiday_birthday);
                holder.icon.setImageResource(R.drawable.holiday_birthday);
            } else if (item.category == 2) {
                mainView.setBackgroundResource(R.drawable.bg_listitem_holiday_festival);
                holder.icon.setImageResource(R.drawable.holiday_festival);
            } else if (item.category == 3) {
                mainView.setBackgroundResource(R.drawable.bg_listitem_holiday_activity);
                holder.icon.setImageResource(R.drawable.holiday_activity);
            }

            holder.title.setText(item.title);
            holder.date.setText(item.date);
            return convertView;
        }

        @Override
        protected Object newViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected int getResId() {
            return R.layout.listitem_holiday;
        }

        private static class ViewHolder {
            @ViewInject(id = R.id.icon)
            ImageView icon;

            @ViewInject(id = R.id.title)
            TextView title;

            @ViewInject(id = R.id.datetime)
            TextView date;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_img_btn) {
            Intent intent = new Intent(getActivity(), AddHolidayActivity.class);
            startActivityForResult(intent, REQUEST_ADD_HOLIDAY);
        }
    }
    
    private class _OnScrollListener implements OnScrollListener {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_ADD_HOLIDAY)
            return;
        if (resultCode != Activity.RESULT_OK)
            return;
        if (data == null)
            return;
        Holiday holiday = (Holiday) data.getSerializableExtra("holiday");
        if (holiday == null)
            return;
        mData.add(holiday);
        mAdapter.notifyDataSetChanged();
    }
}
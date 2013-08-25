package com.example.classmate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xframe.annotation.JSONUtils;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.data.Holiday;
import com.example.classmate.requests.HolidayListRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class HolidaysFragment extends BaseFragment {

    @ViewInject(id = R.id.listview)
    private ListView mListView;

    private LinearLayout mLayout;
    private List<Holiday> mData;
    private CommemorationAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setTitle("纪念日");

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_holiday_list, null);
        ViewAnnotation.bind(mLayout, this);

        mData = new ArrayList<Holiday>();
        mListView.addFooterView(inflater.inflate(R.layout.listitem_wraper, null));
        mAdapter = new CommemorationAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        loadOnePageData(1);

        return mLayout;
    }

    private void loadOnePageData(int page) {
        XHttpClient.sendRequest(new HolidayListRequest(getActivity(), page),
                new XHttpCallbacks.DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                @SuppressWarnings("unchecked")
                List<Holiday> data = (List<Holiday>) result.data;
                mData.addAll(data);
                mAdapter.notifyDataSetChanged();
            };
        });
    }

    private class _OnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
             Intent intent = new Intent(getActivity(), HolidayDetailActivity.class);
             Holiday holiday = (Holiday) parent.getItemAtPosition(position);
             JSONObject jo = JSONUtils.java2JsonObject(holiday, new JSONObject());
             intent.putExtra("holiday_detail", jo.toString());
             startActivity(intent);
        }
    }

    private static class CommemorationAdapter extends CommonAdapter {

        public CommemorationAdapter(Context context, List<Holiday> mData) {
            super(context, mData);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();

            Holiday item = (Holiday) getItem(position);
            holder.title.setText(item.title);
            holder.desc.setText(item.desc);
            holder.date.setText(item.date);
            return v;
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
            @ViewInject(id = R.id.title)
            TextView title;

            @ViewInject(id = R.id.desc)
            TextView desc;

            @ViewInject(id = R.id.datetime)
            TextView date;
        }
    }
}
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
import com.example.classmate.utils.WindowAttr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@WindowAttr(title = "纪念日")
public class HolidaysFragment extends BaseFragment {

    @ViewInject(id = R.id.listview)
    private ListView mListView;

    private LinearLayout mLayout;
    private List<Holiday> mData;
    private CommemorationAdapter mAdapter;
    private View mHeaderView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setTitle("纪念日");

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_holiday_list, null);
        ViewAnnotation.bind(mLayout, this);

        // header
        mHeaderView = inflater.inflate(R.layout.classmate_list_header, null);
        mListView.addHeaderView(mHeaderView);

        mData = new ArrayList<Holiday>();
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
            if ((position % 3) == 0)
                mainView.setBackgroundResource(R.drawable.bg_listitem_holiday_1);
            else if ((position % 3) == 1)
                mainView.setBackgroundResource(R.drawable.bg_listitem_holiday_2);
            else if ((position % 3) == 2)
                mainView.setBackgroundResource(R.drawable.bg_listitem_holiday_3);
            
            ViewHolder holder = (ViewHolder) mainView.getTag();

            Holiday item = (Holiday) getItem(position);
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
            @ViewInject(id = R.id.title)
            TextView title;

            @ViewInject(id = R.id.datetime)
            TextView date;
        }
    }
}
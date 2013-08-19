package com.example.classmate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xframe.annotation.JSONUtils;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.classmate.common.BaseActivity;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.common.Conf;
import com.example.classmate.data.News;
import com.example.classmate.requests.ListRequest;
import com.example.classmate.utils.ImageLoader;

public class NewsDetailActivity extends BaseActivity {

    @ViewInject(id = R.id.listview)
    private ListView mListView;
    private News mNews;

    private List<JSONObject> mData;
    private ReviewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        try {
            String detail = getIntent().getStringExtra("news_detail");
            mNews = JSONUtils.json2JavaObject(new JSONObject(detail),
                    new News());
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("数据错误");
            return;
        }

        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        iv.setTag(Conf.IMAGE_ROOT + mNews.newsPhoto);
        ImageLoader.loadImage(this, iv);
        mListView.addHeaderView(iv);

        mData = new ArrayList<JSONObject>();
        mAdapter = new ReviewAdapter(this, mData);
        mListView.setAdapter(mAdapter);

        loadOnePageData(1);
    }

    private void loadOnePageData(int page) {
        ListRequest request = new ListRequest(this, "review", page);
        request.addParam("newsid", mNews.newsId);
        XHttpClient.sendRequest(request, new XHttpCallbacks.DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                @SuppressWarnings("unchecked")
                List<JSONObject> data = (List<JSONObject>) result.data;
                mData.addAll(data);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mAdapter.getCount() - 1);
            };
        });
    }

    private class ReviewAdapter extends CommonAdapter {

        public ReviewAdapter(Context context, List<?> data) {
            super(context, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();

            JSONObject item = (JSONObject) getItem(position);
            holder.text.setText(item.toString());
            return v;
        }

        @Override
        protected int getResId() {
            return android.R.layout.simple_list_item_1;
        }

        private class ViewHolder {
            @ViewInject(id = android.R.id.text1)
            TextView text;
        }
    }
}

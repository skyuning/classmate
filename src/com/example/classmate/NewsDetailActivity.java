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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.example.classmate.common.AddReviewRequest;
import com.example.classmate.common.BaseActivity;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.common.Conf;
import com.example.classmate.data.News;
import com.example.classmate.data.News.Review;
import com.example.classmate.requests.ListRequest;
import com.example.classmate.utils.ImageLoader;

public class NewsDetailActivity extends BaseActivity {

    @ViewInject(id = R.id.listview)
    private ListView mListView;
    private News mNews;

    private List<JSONObject> mData;
    private ReviewAdapter mAdapter;
    private View mHeaderView;
    private View mFooterView;
    private EditText mEditText;
    private Button mSubmitBtn;
    
    private int mPage;
    private boolean mIsLoading;

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

        mHeaderView = getLayoutInflater().inflate(R.layout.review_header, null);
        ImageView iv = (ImageView) mHeaderView.findViewById(R.id.image);
        iv.setTag(Conf.IMAGE_ROOT + mNews.newsPhoto);
        ImageLoader.loadImage(this, iv);
        mListView.addHeaderView(mHeaderView, null, false);
        
        mFooterView = getLayoutInflater().inflate(R.layout.review_footer, null);
        mEditText = (EditText) mFooterView.findViewById(R.id.edit);
        mSubmitBtn = (Button) mFooterView.findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(new OnSubmitBtnClickListener());
        mListView.addFooterView(mFooterView, null, false);
        mEditText.clearFocus();

        mData = new ArrayList<JSONObject>();
        mAdapter = new ReviewAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new _OnScrollListener());

        mPage = 0;
        mIsLoading = false;
    }
    
    private class OnSubmitBtnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            AddReviewRequest request = new AddReviewRequest(
                    NewsDetailActivity.this, mNews.newsId, mEditText.getText() .toString());
            XHttpClient.sendRequest(request, new XHttpCallbacks.DebugHttpCallback(NewsDetailActivity.this));
        }
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
    
    private class _OnScrollListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                int visibleItemCount, int totalItemCount) {
            if ((view.getLastVisiblePosition() == view.getCount() - 1) && ! mIsLoading) {
                mPage++;
                mIsLoading = true;
                loadOnePageData(mPage);
            }
        }
    }

    private class ReviewAdapter extends CommonAdapter {

        public ReviewAdapter(Context context, List<?> data) {
            super(context, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();

            Review review = JSONUtils.json2JavaObject((JSONObject) getItem(position), new Review());
            holder.info.setText(review.info);
            holder.date.setText(review.date);
            if (TextUtils.isEmpty(review.photo))
                holder.photo.setVisibility(View.GONE);
            else {
                holder.photo.setVisibility(View.VISIBLE);
                String imgUrl = Conf.IMAGE_ROOT + review.photo;
                holder.photo.setTag(imgUrl);
                ImageLoader.loadImage(mContext, holder.photo);
            }
            return v;
        }

        @Override
        protected int getResId() {
            return R.layout.listitem_review;
        }

        private class ViewHolder {
            @ViewInject(id = R.id.photo)
            ImageView photo;
            
            @ViewInject(id = R.id.date)
            TextView date;
            
            @ViewInject(id = R.id.info)
            TextView info;
        }
    }
}

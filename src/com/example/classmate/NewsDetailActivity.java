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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classmate.common.AddReviewRequest;
import com.example.classmate.common.BaseActivity;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.common.Conf;
import com.example.classmate.data.News;
import com.example.classmate.data.News.Review;
import com.example.classmate.requests.ListRequest;
import com.example.classmate.utils.ImageLoader;

public class NewsDetailActivity extends BaseActivity {

    @ViewInject(id = R.id.list_layout)
    private ListLayout mListLayout;
    private News mNews;

    private View mHeaderView;
    private View mFooterView;
    private EditText mEditText;
    private Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        String detail = getIntent().getStringExtra("news_detail");
        JSONObject jo = null;
        try {
            jo = new JSONObject(detail);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("数据错误");
            return;
        }
        mNews = JSONUtils.json2JavaObject(jo, new News());

        mHeaderView = getLayoutInflater().inflate(R.layout.review_header, null);
        ViewHolder holder = new ViewHolder();
        ViewAnnotation.bind(mHeaderView, holder);
        renderItem(jo, holder);
        mListLayout.getListView().addHeaderView(mHeaderView, null, false);

        mFooterView = getLayoutInflater().inflate(R.layout.review_footer, null);
        mEditText = (EditText) mFooterView.findViewById(R.id.edit);
        mSubmitBtn = (Button) mFooterView.findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(new OnSubmitBtnClickListener());
        mListLayout.getListView().addFooterView(mFooterView, null, false);
        mEditText.clearFocus();

        ListRequest request = new ListRequest(this, "review", 0);
        request.addParam("newsid", mNews.newsId);
        mListLayout.setRequest(request);
        mListLayout.setAdapter(new ReviewAdapter(this,
                new ArrayList<JSONObject>()));

        mListLayout.start();
    }

    private class OnSubmitBtnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            AddReviewRequest request = new AddReviewRequest(
                    NewsDetailActivity.this, mNews.newsId, mEditText.getText()
                            .toString());
            XHttpClient.sendRequest(request,
                    new XHttpCallbacks.DebugHttpCallback(
                            NewsDetailActivity.this));
        }
    }

    private class ReviewAdapter extends CommonAdapter {

        public ReviewAdapter(Context context, List<?> data) {
            super(context, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View mainView = null;
            if (convertView == null) {
                mainView = createMainView(position, parent);
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_wraper, null);
                ViewGroup wraper = (ViewGroup) convertView.findViewById(R.id.wraper);
                wraper.addView(mainView);
            } else {
                mainView = convertView.findViewById(R.id.main);
            }
            if (position % 2 == 0)
                mainView.setBackgroundResource(R.drawable.bg_listitem_1);
            else
                mainView.setBackgroundResource(R.drawable.bg_listitem_2);
            if (position == getCount() - 1)
                convertView.findViewById(R.id.bottom).setVisibility(View.VISIBLE);
            else
                convertView.findViewById(R.id.bottom).setVisibility(View.GONE);

            
            ViewHolder holder = (ViewHolder) mainView.getTag();

            Review review = JSONUtils.json2JavaObject(
                    (JSONObject) getItem(position), new Review());
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
            return convertView;
        }

        @Override
        protected int getResId() {
            return R.layout.listitem_review;
        }

        private class ViewHolder {
            @ViewInject(id = R.id.photo)
            ImageView photo;

            @ViewInject(id = R.id.birth)
            TextView date;

            @ViewInject(id = R.id.info)
            TextView info;
        }
    }

    private void renderItem(JSONObject item, ViewHolder holder) {
        String info = item.optString("newsinfo");
        String photoUrl = item.optString("newsphoto");
        int reviewNum = item.optInt("reviewnum");
        holder.info.setText(info + "\n" + photoUrl);
        holder.reviewNum.setText(String.format("%d条评论", reviewNum));

        if (TextUtils.isEmpty(photoUrl) || "null".equals(photoUrl))
            holder.photo.setVisibility(View.GONE);
        else {
            holder.photo.setVisibility(View.VISIBLE);
            String imgUrl = Conf.IMAGE_ROOT + photoUrl;
            holder.photo.setTag(imgUrl);
            ImageLoader.loadImage(this, holder.photo);
        }
    }

    private class ViewHolder {
        @ViewInject(id = R.id.news_photo)
        ImageView photo;

        @ViewInject(id = R.id.news_info)
        TextView info;

        @ViewInject(id = R.id.review_num)
        TextView reviewNum;
    }
}

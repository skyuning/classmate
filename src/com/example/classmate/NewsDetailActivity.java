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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classmate.common.AddReviewRequest;
import com.example.classmate.common.BaseActivity;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.common.Conf;
import com.example.classmate.common.Utils;
import com.example.classmate.data.News;
import com.example.classmate.requests.ListRequest;
import com.example.classmate.utils.ImageLoader;
import com.example.classmate.utils.WindowAttr;

@WindowAttr(title = "新鲜事详情")
public class NewsDetailActivity extends BaseActivity {

    @ViewInject(id = R.id.list_layout)
    private ListLayout mListLayout;
    private News mNews;

    private View mHeaderView;
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

        mHeaderView = getLayoutInflater().inflate(R.layout.news_detail_header, null);
        ViewHolder holder = new ViewHolder();
        ViewAnnotation.bind(mHeaderView, holder);
        renderHeader(jo, holder);
        mListLayout.getListView().addHeaderView(mHeaderView, null, false);

        mEditText = (EditText) findViewById(R.id.edit);
        mSubmitBtn = (Button) findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(new OnSubmitBtnClickListener());
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
            XHttpClient.sendRequest(request, new XHttpCallbacks.DefaultHttpCallback());
        }
    }

    private class ReviewAdapter extends CommonAdapter {

        public ReviewAdapter(Context context, List<?> data) {
            super(context, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View mainView = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) mainView.getTag();

            JSONObject item = (JSONObject) getItem(position);
            holder.info.setText(item.optString("cinfo"));
            holder.date.setText(item.optString("cdate"));
            holder.name.setText(item.optString("u_name") + ":");
            String photoUrl = item.optString("u_photo");
            if (Utils.isEmptyString(photoUrl))
                holder.photo.setVisibility(View.INVISIBLE);
            else {
                holder.photo.setVisibility(View.VISIBLE);
                String imgUrl = Conf.IMAGE_ROOT + photoUrl;
                holder.photo.setTag(imgUrl);
                ImageLoader.loadImage(mContext, holder.photo);
            }
            return mainView;
        }

        @Override
        protected int getResId() {
            return R.layout.listitem_review;
        }

        private class ViewHolder {
            @ViewInject(id = R.id.photo)
            ImageView photo;

            @ViewInject(id = R.id.datetime)
            TextView date;

            @ViewInject(id = R.id.info)
            TextView info;
            
            @ViewInject(id = R.id.name)
            TextView name;
        }
    }

    private void renderHeader(JSONObject item, ViewHolder holder) {
        String info = item.optString("newsinfo");
        String photoUrl = item.optString("newsphoto");
        int reviewNum = item.optInt("reviewnum");
        holder.info.setText(info);
        holder.reviewNum.setText(String.format("%d条评论", reviewNum));
        holder.name.setText(item.optString("unmae"));
        holder.datetime.setText(item.optString("newsdate"));

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
        @ViewInject(id = R.id.photo)
        ImageView photo;

        @ViewInject(id = R.id.info)
        TextView info;
        
        @ViewInject(id = R.id.name)
        TextView name;
        
        @ViewInject(id = R.id.datetime)
        TextView datetime;

        @ViewInject(id = R.id.review_num)
        TextView reviewNum;
    }
}

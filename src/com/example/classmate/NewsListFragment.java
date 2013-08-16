package com.example.classmate;

import java.util.ArrayList;
import java.util.List;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.common.Conf;
import com.example.classmate.data.News;
import com.example.classmate.requests.NewsListRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NewsListFragment extends BaseFragment {
    
    @ViewInject(id = R.id.listview)
    private ListView mListView;
    
    private ImageButton mPublishNewsBtn;

    private LinearLayout mLayout;
    private List<News> mData;
    private NewsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setTitle("新鲜事");

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_news_list, null);
        ViewAnnotation.bind(mLayout, this);
        
        mPublishNewsBtn = getRightImgBtn();
        mPublishNewsBtn.setVisibility(View.VISIBLE);
        mPublishNewsBtn.setImageResource(android.R.drawable.ic_menu_edit);
        mPublishNewsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublishNewsActivity.class);
                startActivity(intent);
            }
        });

        mData = new ArrayList<News>();
        mAdapter = new NewsAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        loadOnePageData(1);

        return mLayout;
    }

    private void loadOnePageData(int page) {
        XHttpClient.sendRequest(new NewsListRequest(page), new XHttpCallbacks.DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                @SuppressWarnings("unchecked")
                List<News> data = (List<News>) result.data;
                mData.addAll(data);
                mAdapter.notifyDataSetChanged();
            };
        });
    }

    private class _OnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // Intent intent = new Intent(getActivity(),
            // ClassmateInfoActivity.class);
            // JSONObject item = (JSONObject)
            // parent.getItemAtPosition(position);
            // intent.putExtra("classmate_info", item.toString());
            // startActivity(intent);
        }
    }

    private class NewsAdapter extends CommonAdapter {

        public NewsAdapter(Context context, List<News> mData) {
            super(context, mData);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();

            News item = (News) getItem(position);
            holder.info.setText(item.info);
            holder.reviewNum.setText(String.format("%d条评论", item.reviewNum));
            
            if (TextUtils.isEmpty(item.newsPhoto))
                holder.photo.setVisibility(View.GONE);
            else {
                String imgUrl = Conf.IMAGE_ROOT + item.newsPhoto;
                holder.photo.setTag(imgUrl);
                ImageLoader.loadImage(holder.photo);
            }
            
            return v;
        }

        @Override
        protected Object newViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected int getResId() {
            return R.layout.listitem_news;
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
}
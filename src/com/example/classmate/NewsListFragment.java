package com.example.classmate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.common.Conf;
import com.example.classmate.requests.ListRequest;
import com.example.classmate.utils.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NewsListFragment extends BaseFragment {
    
    @ViewInject(id = R.id.listview)
    private ListView mListView;
    
    private LinearLayout mLayout;
    private List<JSONObject> mData;
    private NewsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setTitle("新鲜事");

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_news_list, null);
        ViewAnnotation.bind(mLayout, this);
        
        mData = new ArrayList<JSONObject>();
        mAdapter = new NewsAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        loadOnePageData(1);

        return mLayout;
    }

    private void loadOnePageData(int page) {
        XHttpClient.sendRequest(new ListRequest(getActivity(), "news", page),
                new XHttpCallbacks.DefaultHttpCallback() {
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

    private class _OnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
             JSONObject item = (JSONObject) parent.getItemAtPosition(position);
             intent.putExtra("news_detail", item.toString());
             startActivity(intent);
        }
    }

    private class NewsAdapter extends CommonAdapter {

        public NewsAdapter(Context context, List<JSONObject> mData) {
            super(context, mData);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();

            JSONObject item = (JSONObject) getItem(position);
            String info = item.optString("info");
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
                ImageLoader.loadImage(getActivity(), holder.photo);
            }
            
            return v;
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
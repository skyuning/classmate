package com.example.classmate;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.common.Utils;
import com.example.classmate.data.News;
import com.example.classmate.data.News.Review;
import com.example.classmate.requests.AddNewsRequest;
import com.example.classmate.requests.NewsListRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsListFragment extends BaseFragment {
    
    private static final int REQUEST_CHOOSE_PHOTO = 11;

    @ViewInject(id = R.id.listview)
    private ListView mListView;
    
    @ViewInject(id = R.id.button1)
    private Button mButton;

    private LinearLayout mLayout;
    private List<News> mData;
    private NewsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setTitle("纪念日");

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_news_list, null);
        ViewAnnotation.bind(mLayout, this);
        
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
            }
        });

        mData = new ArrayList<News>();
        mAdapter = new NewsAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        loadOnePageData(1);

        return mLayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_CHOOSE_PHOTO) && (resultCode == Activity.RESULT_OK)) {
            String path = Utils.uri2Path(getActivity(), data.getData());
            File f = new File(path);
            Toast.makeText(getActivity(), "" + f.length(), Toast.LENGTH_LONG).show();
            try {
                XHttpClient.sendRequest(new AddNewsRequest(path, "hahaha"), new XHttpCallbacks.DebugHttpCallback(getActivity()));
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private static class NewsAdapter extends CommonAdapter {

        public NewsAdapter(Context context, List<News> mData) {
            super(context, mData);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();

            News item = (News) getItem(position);
            holder.photo.setImageResource(R.drawable.ic_launcher);
            holder.more.setText(String.format("共%d条评论", item.reviewNum));
            
            if (null != item.reviewList) {
                holder.reviews.removeAllViews();
                for (Review review : item.reviewList) {
                    TextView textView = new TextView(mContext);
                    textView.setText(review.info);
                    holder.reviews.addView(textView);
                }
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

        private static class ViewHolder {
            @ViewInject(id = R.id.photo)
            ImageView photo;
            
            @ViewInject(id = R.id.reviews)
            LinearLayout reviews;

            @ViewInject(id = R.id.more)
            TextView more;
        }
    }
}
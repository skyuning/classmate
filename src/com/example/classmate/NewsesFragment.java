package com.example.classmate;

import java.util.List;

import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import com.example.classmate.common.CommonAdapter;
import com.example.classmate.common.Conf;
import com.example.classmate.requests.ListRequest;
import com.example.classmate.utils.ImageLoader;
import com.example.classmate.utils.WindowAttr;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

@WindowAttr(title = "新鲜事")
public class NewsesFragment extends BaseListFragment implements OnClickListener {

    @Override
    public void onResume() {
        super.onResume();
        setRightImgBtn(R.drawable.write, this);
    }
    
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PublishNewsActivity.class);
        startActivity(intent);
    }

    private class NewsAdapter extends CommonAdapter {

        public NewsAdapter(Context context, List<JSONObject> mData) {
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

            ViewHolder holder = (ViewHolder) mainView.getTag();

            JSONObject item = (JSONObject) getItem(position);
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
                ImageLoader.loadImage(getActivity(), holder.photo);
            }

            return convertView;
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

    @Override
    protected CommonAdapter getAdapter() {
        return new NewsAdapter(getActivity(), mData);
    }

    @Override
    protected ListRequest getRequest() {
        ListRequest request = new ListRequest(getActivity(), "news", 0);
        return request;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        JSONObject jo = (JSONObject) parent.getItemAtPosition(position);
        intent.putExtra("news_detail", jo.toString());
        startActivity(intent);
    }
}
package com.example.classmate;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.xframe.annotation.JSONUtils;
import org.xframe.annotation.JSONUtils.JSONDict;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;
import org.xframe.http.XHttpRequest;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CommemorationListFragment extends BaseFragment {

    @ViewInject(id = R.id.listview)
    private ListView mListView;

    private LinearLayout mLayout;
    private List<Commemoration> mData;
    private CommemorationAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setTitle("纪念日");

        mLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_classmate_list, null);
        ViewAnnotation.bind(mLayout, this);

        mData = new ArrayList<Commemoration>();
        mAdapter = new CommemorationAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        loadOnePageData(1);

        return mLayout;
    }

    private void loadOnePageData(int page) {
        XHttpClient.sendRequest(new _Request(), new XHttpCallbacks.DebugHttpCallback(getActivity()) {
            @Override
            public void onSuccess(AHttpResult result) {
                List<Commemoration> data = (List<Commemoration>) result.data;
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

    private class _Request extends XHttpRequest {

        @Override
        public Object handleResponse(HttpResponse response, String content)
                throws Exception {
            List<Commemoration> data = new ArrayList<Commemoration>();
            content = "[{\"date\":1331481600000,\"description\":\"韩梅梅生日啦\",\"holidayid\":\"2\",\"title\":\"韩梅梅生日\",\"userid\":0},"
                    + "{\"date\":1355241600000,\"description\":\"李雪生日啦\",\"    holidayid\":\"3\",\"title\":\"李雪生日\",\"userid\":0},"
                    + "{\"category\":\"纪念日\",\"date\":1364745600000,\"description\":\"毕业10周年纪念日\",\"holidayid\":\"1\",\"photourl\":\"http://localhost\",\"title\":\"毕业纪念日\",\"userid\":1}]";

            JSONArray ja = new JSONArray(content);

            for (int i = 0; i < ja.length(); i++) {
                try {
                    Commemoration commemoration = new Commemoration();
                    JSONUtils.json2JavaObject(ja.getJSONObject(i),
                            commemoration);
                    data.add(commemoration);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return data;
        }

        @Override
        protected String buildUrl() {
            String url = "http://192.168.1.10:8080/Classmate/app/holiday?action=list&page=1&size=3";
            return url;
        }

    }

    private static class CommemorationAdapter extends CommonAdapter {

        public CommemorationAdapter(Context context, List<Commemoration> mData) {
            super(context, mData);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();

            Commemoration item = (Commemoration) getItem(position);
            holder.title.setText(item.title);
            holder.desc.setText(item.desc);
            holder.date.setText(item.date);
            return convertView;
        }

        @Override
        protected Object newViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected int getResId() {
            return R.layout.listitem_notify;
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

    private class Commemoration {
        @JSONDict(name = "date", defVal = "")
        public String date;

        @JSONDict(name = "description", defVal = "")
        public String desc;

        @JSONDict(name = "title", defVal = "")
        public String title;
    }
}
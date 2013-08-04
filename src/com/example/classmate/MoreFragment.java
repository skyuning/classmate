package com.example.classmate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.classmate.common.BaseFragment;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreFragment extends BaseFragment {

    @ViewInject(id = R.id.listview)
    private ListView mListView;
    
    private LinearLayout mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        setTitle("更多");
        
        mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_classmate_list, null);
        ViewAnnotation.bind(mLayout, this);
        
        JSONArray data = null;
        try {
            data = getListData();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
            return mLayout;
        }
        
        _Adapter adapter = new _Adapter(getActivity(), data);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        
        return mLayout;
    }

    private JSONArray getListData() throws JSONException {
        String jsonData = "{'classmates': ["
                + "{ 'name': 'skyun', 'sex': '男', 'phone': '13171741551', 'age': 26 },"
                + "{ 'name': 'jerry', 'sex': '男', 'phone': 'xxxxxxxxxxx', 'age': 26 },"
                + "{ 'name': 'gangz', 'sex': '男', 'phone': 'xxxxxxxxxxx', 'age': 26 }"
                + "]}";

        JSONObject jo = new JSONObject(jsonData);
        JSONArray ja = jo.getJSONArray("classmates");
        return ja;
    }
    
    private class _OnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent = new Intent(getActivity(), ClassmateInfoActivity.class);
//            JSONObject item = (JSONObject) parent.getItemAtPosition(position);
//            intent.putExtra("classmate_info", item.toString());
//            startActivity(intent);
        }
    }

    private static class _Adapter extends BaseAdapter {
        
        private Context mContext;
        private JSONArray mData;
        
        public _Adapter(Context context, JSONArray data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.length();
        }

        @Override
        public Object getItem(int position) {
            return mData.opt(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_classmate, null);
                holder = new ViewHolder();
                ViewAnnotation.bind(convertView, holder);
                convertView.setTag(holder);
            } else {
               holder = (ViewHolder) convertView.getTag();
            }
            
            JSONObject item = (JSONObject) getItem(position);
            holder.name.setText(item.optString("name"));
            holder.phone.setText(item.optString("phone"));
            return convertView;
        }
        
        private static class ViewHolder {
            @ViewInject(id = R.id.name)
            TextView name;
            
            @ViewInject(id = R.id.phone)
            TextView phone;
        }
    }
}

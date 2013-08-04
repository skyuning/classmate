package com.example.classmate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.xframe.annotation.JSONUtils;
import org.xframe.annotation.JSONUtils.JSONDict;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import com.example.classmate.common.BaseFragment;

import android.content.Context;
import android.os.AsyncTask;
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

public class ClassmateListFragment extends BaseFragment {

    @ViewInject(id = R.id.listview)
    private ListView mListView;
    
    private LinearLayout mLayout;
    private List<Classmate> mData;
    private ClassmateAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        setTitle("同学们");
        
        mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_classmate_list, null);
        ViewAnnotation.bind(mLayout, this);
        
        mData = new ArrayList<Classmate>();
        mAdapter = new ClassmateAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
//        loadOnePageData(1);
//        loadOnePageData(2);
        
        return mLayout;
//        JSONArray data = null;
//        try {
//            data = getListData();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
//            return mLayout;
//        }
    }

    private void loadOnePageData(int page) {
        AsyncTask<Void, Void, JSONArray> asyncTask = new AsyncTask<Void, Void, JSONArray>() {
            @Override
            protected JSONArray doInBackground(Void... params) {
                String url = "http://192.168.1.10:8080/Classmate/app/user.jsp?action=list&page=1&size=9";
                HttpGet get = new HttpGet(url);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response;
                try {
                    response = client.execute(get);
                    String content = EntityUtils.toString(response.getEntity());
                    JSONArray ja = new JSONArray(content);
                    return ja;
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(JSONArray result) {
                if (result == null)
                    return;
                
                for (int i=0; i<result.length(); i++) {
                    try {
                        Classmate classmate = new Classmate();
                        JSONUtils.json2JavaObject(result.getJSONObject(i), classmate);
                        mData.add(classmate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        asyncTask.execute();
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

    private static class ClassmateAdapter extends BaseAdapter {
        
        private Context mContext;
        private List<Classmate> mData;
        
        public ClassmateAdapter(Context context, List<Classmate> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
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
            
            Classmate item = (Classmate) getItem(position);
            holder.name.setText(item.name);
            holder.phone.setText(item.phone);
            return convertView;
        }
        
        private static class ViewHolder {
            @ViewInject(id = R.id.name)
            TextView name;
            
            @ViewInject(id = R.id.phone)
            TextView phone;
        }
    }
    
    private class Classmate {
        @JSONDict(name = "u_address", defVal = "")
        public String address;
        
        @JSONDict(name = "u_cellphone", defVal = "")
        public String phone;
        
        @JSONDict(name = "u_city", defVal = "")
        public String city;
        
        @JSONDict(name = "u_email", defVal = "")
        public String email;
        
        @JSONDict(name = "u_name", defVal = "")
        public String name;
        
        @JSONDict(name = "u_qq", defVal = "")
        public String qq;
        
        @JSONDict(name = "u_status", defVal = "")
        public String status;
        
        @JSONDict(name = "u_weibo", defVal = "")
        public String weibo;
        
        @JSONDict(name = "u_weixin", defVal = "")
        public String weixin;
        
        @JSONDict(name = "u_work", defVal = "")
        public String work;
    }
}

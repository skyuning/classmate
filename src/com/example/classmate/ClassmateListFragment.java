package com.example.classmate;

import java.util.ArrayList;
import java.util.List;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;
import org.xframe.http.XHttpRequest;

import com.example.classmate.common.BaseFragment;
import com.example.classmate.common.CommonAdapter;
import com.example.classmate.data.Classmate;
import com.example.classmate.requests.ClassmateListRequest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
        super.onCreateView(inflater, container, savedInstanceState);
        
        setTitle("同学们");
        
        mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_classmate_list, null);
        ViewAnnotation.bind(mLayout, this);
        
        mData = new ArrayList<Classmate>();
        mAdapter = new ClassmateAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new _OnItemClickListener());
        loadOnePageData(1);
//        loadOnePageData(2);
        
        return mLayout;
    }

    private void loadOnePageData(int page) {
        XHttpRequest listClassmate = new ClassmateListRequest(page);
        XHttpClient.sendRequest(listClassmate, new XHttpCallbacks.DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                @SuppressWarnings("unchecked")
                List<Classmate> data = (List<Classmate>) result.data;
                mData.addAll(data);
                mAdapter.notifyDataSetChanged();
            };
            
            @Override
            public void onFaild(AHttpResult result) {
//                Toast.makeText(getActivity(), result.e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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

    private static class ClassmateAdapter extends CommonAdapter {
        
        public ClassmateAdapter(Context context, List<?> data) {
            super(context, data);
            // TODO Auto-generated constructor stub
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) v.getTag();
            
            Classmate item = (Classmate) getItem(position);
            holder.name.setText(item.name);
            holder.phone.setText(item.phone);
            return v;
        }
        
        @Override
        protected Object newViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected int getResId() {
            return R.layout.listitem_classmate;
        }
        
        private static class ViewHolder {
            @ViewInject(id = R.id.name)
            TextView name;
            
            @ViewInject(id = R.id.phone)
            TextView phone;
        }
    }
}

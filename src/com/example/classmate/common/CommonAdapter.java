package com.example.classmate.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.xframe.annotation.ViewAnnotation;

import com.example.classmate.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class CommonAdapter extends BaseAdapter {
    
    protected Context mContext;
    private List<?> mData;

    public CommonAdapter(Context context, List<?> data) {
        mContext = context;
        mData = data;
    }
    
    public List<?> getData() {
        return mData;
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
        if (convertView == null)
            convertView = createMainView(position, parent);
        return convertView;
    }
    
    protected View createMainView(int position, ViewGroup parent) {
        ListView lv = (ListView) parent;
        lv.setDividerHeight(0);
        
        View mainView = null;
        
        Object holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mainView =  inflater.inflate(getResId(), null);
        holder = newViewHolder();
        ViewAnnotation.bind(mainView, holder);
        mainView.setTag(holder);
        mainView.setId(R.id.main);

        return mainView;
    }

    protected Object newViewHolder() {
        Class<?>[] clazzs = getClass().getDeclaredClasses();
        Class<?> holderClazz = null;
        for (Class<?> clazz : clazzs) {
            if (clazz.getSimpleName() == "ViewHolder") {
                holderClazz = clazz;
                break;
            }
        }
        if (null == holderClazz)
            return null;
        
        Object holder = null;
        try {
            Constructor<?> constructor = holderClazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            holder = constructor.newInstance(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return holder;
    }

    protected int getResId() { return 0; }
}

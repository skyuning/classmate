package com.example.classmate.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.xframe.annotation.ViewAnnotation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter extends BaseAdapter {
    
    protected Context mContext;
    private List<?> mData;

    public CommonAdapter(Context context, List<?> data) {
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
        Object holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(getResId(), null);
            holder = newViewHolder();
            ViewAnnotation.bind(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = convertView.getTag();
        }
        return convertView;
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

    protected abstract int getResId();
}

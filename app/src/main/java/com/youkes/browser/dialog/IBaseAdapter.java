package com.youkes.browser.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * com.yuntongxun.ecdemo.common.dialog in ECDemo_Android
 * Created by Jorstin on 2015/4/18.
 */
public abstract class IBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> data;
    protected LayoutInflater mLayoutInflater;
    private boolean mNotifyOnChange = true;

    public IBaseAdapter(Context ctx) {
        mContext = ctx;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = new ArrayList<T>();
    }

    public IBaseAdapter(Context ctx ,List<T> data) {
        this(ctx);
        this.data = data;
    }

    public View inflateView(int resource) {
        return mLayoutInflater.inflate(resource , null);
    }


    public List<T> getData() {
        return data;
    }

    public void replace(int position , T t) {
        data.remove(position);
        data.add(position , t);
        if(!mNotifyOnChange) {
            return ;
        }
        notifyDataSetChanged();
    }

    public void insert(int position , T t) {
        data.add(position, t);
        if(!mNotifyOnChange) {
            return ;
        }
        notifyDataSetChanged();
    }

    public void addAll(int position , Collection<T> t) {
        data.addAll(position, t);
        if(!mNotifyOnChange) {
            return ;
        }
        notifyDataSetChanged();
    }

    public void add(T t) {
        data.add(t);
        if(!mNotifyOnChange) {
            return ;
        }
        notifyDataSetChanged();
    }

    public void setData(Collection<T> t) {
        data.clear();
        addData(t);
    }

    public void addData(T[] ts) {
        if(ts == null || ts.length == 0) {
            return ;
        }
        for(int i = 0 ; i < ts.length ; i++) {
            data.add(ts[i]);
        }
        if(!mNotifyOnChange) {
            return ;
        }
        notifyDataSetChanged();
    }

    public void clear(boolean notify) {
        data.clear();
        if(!mNotifyOnChange) {
            return ;
        }
        notifyDataSetChanged();
    }

    public void addData(Collection<T> t) {
        addData(t, this.mNotifyOnChange);
    }

    public void addData(Collection<T> t ,boolean notify) {
        data.addAll(t);
        if(!mNotifyOnChange) {
            return ;
        }
        notifyDataSetChanged();
    }

    public  void reset() {
        clear(this.mNotifyOnChange);
    }

    public void remove(int position) {
        data.remove(position);
        if(!mNotifyOnChange) {
            return ;
        }
        notifyDataSetChanged();
    }

    public void addOnly(T t) {
        data.add(t);
    }

    public T removeOnly(int position) {
        return data.remove(position);
    }

    public void removeOnly(T t) {
        data.remove(t);
    }

    public void unNofity() {
        this.mNotifyOnChange = false;
    }

    public boolean hasDataAndRemove(T t) {
        boolean remove = data.remove(t);
        if(mNotifyOnChange) {
            notifyDataSetChanged();
        }
        return remove;
    }

    public Context getContext() {
        return mContext;
    }

    public int getPosition(T t) {
        return data.indexOf(t);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mNotifyOnChange = true;
    }
}

/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.youkes.browser.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 自定义数据适配器
 * Created by Jorstin on 2015/3/18.
 */
public abstract class CommAdapter extends BaseAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if(isNullContentView(convertView, itemViewType)) {
            convertView = buildViewByType(position, parent, itemViewType);
        }
        bindData(convertView, position, itemViewType);
        return convertView;
    }

    /**
     * 判断是否为空，重复使用
     * @param convertView
     * @param itemViewType
     * @return
     */
    protected boolean isNullContentView(View convertView , int itemViewType) {
        return convertView == null;
    }

    /**
     * 需要实现方法，子类根据提供的Item类型返回对应的View
     * @param position
     * @param parent
     * @param itemViewType
     * @return
     */
    protected abstract View buildViewByType( int position ,ViewGroup parent , int itemViewType);

    /**
     * 绑定数据
     * @param convertView
     * @param position
     * @param itemViewType
     */
    protected abstract void bindData(View convertView , int position , int itemViewType);
}


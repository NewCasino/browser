package com.youkes.browser.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.youkes.browser.utils.DensityUtil;


public class PopupMenuListView extends SuperListView {

    private Context mContext;

    /**
     * @param context
     */
    public PopupMenuListView(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * @param context
     * @param attrs
     */
    public PopupMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(measureWidth()
                        + getPaddingLeft() + getPaddingRight(),
                View.MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    /**
     * 计算宽度
     * @return
     */
    private int measureWidth() {
        int maxWidth = 0;
        View convertView = null;
        for(int i = 0 ; i < getAdapter().getCount() ; i ++) {
            convertView = getAdapter().getView(i, convertView, this);
            if(convertView == null) {
                continue;
            }
            convertView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            if(convertView.getMeasuredWidth() <= maxWidth) {
                continue;
            }
            maxWidth = convertView.getMeasuredWidth();
        }

        int max = DensityUtil.dip2px(112.0F);
        if(maxWidth < max) {
            maxWidth = max;
        }
        return maxWidth;
    }

}


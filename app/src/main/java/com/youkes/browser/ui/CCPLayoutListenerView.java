package com.youkes.browser.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;


public class CCPLayoutListenerView extends FrameLayout {


    private OnCCPViewLayoutListener mOnLayoutListener;
    private OnCCPViewSizeChangedListener mOnSizeChangedListener;
    /**
     * @param context
     */
    public CCPLayoutListenerView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CCPLayoutListenerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(this.mOnLayoutListener != null ) {
            this.mOnLayoutListener.onViewLayout();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);

    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(this.mOnSizeChangedListener != null ) {
            this.mOnSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    public void setOnLayoutListener(OnCCPViewLayoutListener onLayoutListener) {
        this.mOnLayoutListener = onLayoutListener;
    }

    public void setOnSizeChangedListener(
            OnCCPViewSizeChangedListener onSizeChangedListener) {
        this.mOnSizeChangedListener = onSizeChangedListener;
    }


    public interface OnCCPViewLayoutListener {
        void onViewLayout();
    }

    public interface OnCCPViewSizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh) ;
    }
}


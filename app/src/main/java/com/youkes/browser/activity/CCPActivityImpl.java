package com.youkes.browser.activity;

import android.view.View;

/**
 * Created by Jorstin on 2015/3/18.
 */
public class CCPActivityImpl extends CCPActivityBase {

    final private AppBaseActivity mActivity;

    public CCPActivityImpl(AppBaseActivity activity) {
        mActivity  = activity;
    }

    @Override
    protected void onInit() {
        mActivity.onActivityInit();
    }

    @Override
    protected int getLayoutId() {
        return mActivity.getLayoutId();
    }

    @Override
    protected View getContentLayoutView() {
        return null;
    }

    @Override
    protected String getClassName() {
        return mActivity.getClass().getName();
    }

    @Override
    protected void onBaseContentViewAttach(View contentView) {
        mActivity.onBaseContentViewAttach(contentView);
    }
}

package com.youkes.browser.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.youkes.browser.R;
import com.youkes.browser.ui.CCPLayoutListenerView;
import com.youkes.browser.ui.TopBarView;
import com.youkes.browser.ui.VerticalImageSpan;
import com.youkes.browser.utils.AudioManagerTools;
import com.youkes.browser.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


public abstract class CCPActivityBase {

    private FragmentActivity mActionBarActivity;

    private AudioManager mAudioManager ;

    /**
     * CCPActivity root view
     */
    private View mContentView;

    private LayoutInflater mLayoutInflater;

    /**
     * CCPActivity root View container
     */
    private FrameLayout mContentFrameLayout;

    /**
     * Manager dialog.
     */
    private List<Dialog> mAppDialogCache ;

    /**
     * The volume of music
     */
    private int mMusicMaxVolume;
    private View mBaseLayoutView;
    private View mTransLayerView;

    public CharSequence mTitleText;
    /**
     * The client mute, do not accept message Notification
     */
    private VerticalImageSpan mMuteIcon;

    /**
     * 标题
     */
    private View mTopBarView;
    /**
     * Whether the mute of receive new message
     */
    private boolean isMute = false;

    public void init(Context context , FragmentActivity activity)  {
        mActionBarActivity = activity;
        onInit();

        mAudioManager = AudioManagerTools.getInstance().getAudioManager();
        mMusicMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        int layoutId = getLayoutId();
        mLayoutInflater = LayoutInflater.from(mActionBarActivity);
        mBaseLayoutView = mLayoutInflater.inflate(R.layout.ccp_activity, null);
        mTransLayerView = mBaseLayoutView.findViewById(R.id.ccp_trans_layer);
        LinearLayout mRootView = (LinearLayout) mBaseLayoutView.findViewById(R.id.ccp_root_view);
        mContentFrameLayout = (FrameLayout) mBaseLayoutView.findViewById(R.id.ccp_content_fl);

        if(getTitleLayout() != -1) {
            mTopBarView = mLayoutInflater.inflate(getTitleLayout() , null);
            mRootView.addView(mTopBarView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        if (layoutId != -1) {

            mContentView = getContentLayoutView();
            if(mContentView == null) {
                mContentView = mLayoutInflater.inflate(getLayoutId(), null);
            }
            mRootView.addView(mContentView,LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }

        onBaseContentViewAttach(mBaseLayoutView);

        CCPLayoutListenerView listenerView = (CCPLayoutListenerView) mActionBarActivity.findViewById(R.id.ccp_content_fl);
        if (listenerView != null && mActionBarActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) {
            listenerView.setOnSizeChangedListener(new CCPLayoutListenerView.OnCCPViewSizeChangedListener() {
                @Override
                public void onSizeChanged(int w, int h, int oldw, int oldh) {
                    LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "oldh - h = " + (oldh - h));
                }
            });

        }
    }


    /**
     * hideTitleView
     */
    public final void hideTitleView() {
        LogUtil.d(LogUtil.getLogUtilsTag(AppBaseActivity.class), "hideTitleView hasTitle :" + (mTopBarView != null? true: false));
        if(mTopBarView != null) {
            mTopBarView.setVisibility(View.GONE);
        }
    }

    /**
     * showTitleView
     */
    public final void showTitleView() {
        LogUtil.d(LogUtil.getLogUtilsTag(AppBaseActivity.class), "showTitleView hasTitle :" + (mTopBarView != null? true: false));
        if(mTopBarView != null) {
            mTopBarView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * isTitleShowing
     * @return
     */
    public final boolean isTitleShowing() {
        LogUtil.d(LogUtil.getLogUtilsTag(AppBaseActivity.class), "isTitleShowing hasTitle :" + (mTopBarView != null? true: false));
        if(mTopBarView == null) {
            return  false;
        }

        return mTopBarView.getVisibility() == View.VISIBLE;
    }

    /**
     * The height of acitonBar
     * @return
     */
    public final int getActionBarHeight() {
        if(mTopBarView == null) {
            return 0;
        }

        return mTopBarView.getHeight();
    }

    /**
     *
     * @return
     */
    public View getActivityLayoutView() {
        return mContentView;
    }

    public View getContentView() {
        return mBaseLayoutView;
    }

    /**
     *
     * @param visiable
     */
    public void setActionBarVisiable(int visiable) {
        if(mTopBarView == null) {
            return ;
        }
        if(visiable == View.VISIBLE) {
            showTitleView();
            return ;
        }
        hideTitleView();
    }

    /**
     *
     * @return
     */
    public FragmentActivity getFragmentActivity() {
        return mActionBarActivity;
    }


    /**
     *
     * @param contentDescription
     */
    public final void setActionContentDescription(CharSequence contentDescription) {
        if(TextUtils.isEmpty(contentDescription)) {
            return;
        }
        String description = mActionBarActivity.getString(R.string.common_enter_activity) + contentDescription;
        mActionBarActivity.getWindow().getDecorView().setContentDescription(description);
    }


    /**
     *
     */
    public void displaySoftKeyboard() {
        final FragmentActivity activity = mActionBarActivity;
        // Display the soft keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = activity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * hide input method.
     */
    public void hideSoftKeyboard(View view) {
        if (view == null) {
            return;
        }


        InputMethodManager inputMethodManager = (InputMethodManager) mActionBarActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            IBinder localIBinder = view.getWindowToken();
            if (localIBinder != null)
                inputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
        }
    }

    /**
     * hide inputMethod
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mActionBarActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null ) {
            View localView = mActionBarActivity.getCurrentFocus();
            if(localView != null && localView.getWindowToken() != null ) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    /**
     *
     * @return
     */
    public int getStreamMaxVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     *
     * @return
     */
    public int getStreamVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 设置ActionBar标题
     * @param title
     */
    public final void setActionBarTitle(CharSequence title) {
        if(mTopBarView == null) {
            return;
        }

        mTitleText = title;
        if(mTopBarView instanceof TopBarView) {
            ((TopBarView) mTopBarView ).setTitle(title!= null ?title.toString():"");
        }
        setActionContentDescription(title);
    }

    /**
     *
     * @return
     */
    public final CharSequence getActionBarTitle() {
        return mTitleText;
    }

    /**
     *
     * @return
     */
    public final TopBarView getTitleBar() {
        return (TopBarView)mTopBarView;
    }


    /**
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
                && mAudioManager != null) {
            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            if(streamVolume >= mMusicMaxVolume) {
                //LogUtil.d(LogUtil.getLogUtilsTag(BaseFragment.class), "has set the max volume");
                return true;
            }

            int mean = mMusicMaxVolume / 7;
            if(mean == 0) {
                mean = 1;
            }

            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    streamVolume + mean, AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
        }
        if((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
                && mAudioManager != null) {
            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int mean = mMusicMaxVolume / 7;
            if(mean == 0) {
                mean = 1;
            }

            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    streamVolume - mean, AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
            return true;
        }
        return false;
    }

    /**
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
			/*if(mOverFlowAction != null && mOverFlowAction.isEnabled()) {
				callMenuCallback(mOverFlowMenuItem, mOverFlowAction);
				return true;
			}*/
        }
        return false;
    }

    public void onResume(){

    }

    public void onPause(){

    }

    /**
     *
     */
    public void onDestroy() {
        releaseDialogList();
        mAudioManager = null;
        mTopBarView = null;
    }

    /**
     *
     */
    private void releaseDialogList() {
        if(mAppDialogCache == null) {
            return;
        }

        for(Dialog dialog : mAppDialogCache) {
            if(dialog == null || !dialog.isShowing()) {
                continue;
            }
            dialog.dismiss();
        }
        mAppDialogCache.clear();
        mAppDialogCache = null;
    }


    /**
     * 子类重载该方法自定义标题布局文件
     * @return
     */
    public int getTitleLayout() {
        return R.layout.ec_title_view_base;
    }

    public TopBarView getTopBarView() {
        if(mTopBarView instanceof TopBarView) {
            return (TopBarView) mTopBarView;
        }
        return null;
    }

    protected abstract void onInit();

    /**
     * The sub Activity implement, set the Ui Layout
     * @return
     */
    protected abstract int getLayoutId();
    protected abstract View getContentLayoutView();
    protected abstract String getClassName();

    /**
     *
     */
    protected abstract void onBaseContentViewAttach(View contentView);

    public void addDialog(Dialog dialog) {
        if(dialog == null) {
            return;
        }

        if(mAppDialogCache == null) {
            mAppDialogCache = new ArrayList<Dialog>();
        }
        mAppDialogCache.add(dialog);
    }

    /**
     *
     */
    protected SpannableString buildActionTitle() {
        int dimensionPixelSize = mActionBarActivity.getResources().getDimensionPixelSize(R.dimen.BigTextSize);

        int mutIndex = 0;
        String format = "%s";
        if(isMute) {
            format = format + " #";
            mutIndex += 2;
        }

        SpannableString spannableString = new SpannableString(mTitleText);
        //EmoticonUtil.getTextFormat(mActionBarActivity, String.format(format, new Object[]{mTitleText}), dimensionPixelSize);
        if(isMute) {
            if(mMuteIcon == null) {
                mMuteIcon = getTitleIconTips(dimensionPixelSize, R.drawable.chat_mute_notify_title_icon);
            }
            int length = spannableString.length() - mutIndex + 1;
            spannableString.setSpan(mMuteIcon, length, length + 1, SpannableStringBuilder.SPAN_POINT_MARK);
        }
        return spannableString;
    }

    /**
     *
     * @param padding
     * @param iconRes
     * @return
     */
    private VerticalImageSpan getTitleIconTips(int padding , int iconRes) {
        Drawable drawable = mActionBarActivity.getResources().getDrawable(iconRes);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
        imageSpan.setPadding((drawable.getIntrinsicHeight() - padding) / 2);
        return imageSpan;
    }

    /**
     *
     * @param mute
     */
    public CharSequence setMute(boolean mute) {
        isMute = mute;
        return buildActionTitle();
    }


    /**
     *
     */
    public final void invalidateActionMenu() {
        mActionBarActivity.supportInvalidateOptionsMenu();
    }
}

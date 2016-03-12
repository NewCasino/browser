package com.youkes.browser.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


import com.umeng.analytics.MobclickAgent;
import com.youkes.browser.ApiAction;
import com.youkes.browser.R;
import com.youkes.browser.ui.TopBarView;
import com.youkes.browser.utils.LogUtil;


import java.util.Iterator;
import java.util.Set;

public abstract class AppBaseActivity extends FragmentActivity implements
		GestureDetector.OnGestureListener {

	private static final String TAG = AppBaseActivity.class.getSimpleName();
	public boolean hasLeftTextBtn() {
		return false;
	}
	/**
	 * 初始化应用ActionBar
	 */
	private CCPActivityBase mBaseActivity = new CCPActivityImpl(this);
	/**
	 * 初始化广播接收器
	 */
	private InternalReceiver internalReceiver;
	private GestureDetector mGestureDetector = null;
	private boolean mIsHorizontalScrolling = false;
	private int mScrollLimit = 0;
	private boolean mIsChildScrolling = false;
	private int mMinExitScrollX = 0;

	public boolean hasSearch(){
		return false;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//PushAgent.getInstance(this).onAppStart();
		MobclickAgent.openActivityDurationTrack(false);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
		);
		mBaseActivity.init(getBaseContext(), this);
		onActivityInit();
		LogUtil.d(TAG, "checktask onCreate:" + super.getClass().getSimpleName()
				+ "#0x" + super.hashCode() + ", taskid:" + getTaskId()
				+ ", task:" + new ActivityTaskUtils(this));
		abstracrRegist();
		getTopBarView().showSearch(hasSearch());

	}


	protected void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction(ApiAction.ACTION_KICK_OFF);
		for (String action : actionArray) {
			intentfilter.addAction(action);
		}
		if (internalReceiver == null) {
			internalReceiver = new InternalReceiver();
		}
		registerReceiver(internalReceiver, intentfilter);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		try {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				reset();
			}
			if (!isEnableRightSlideGesture()) {
				return super.dispatchTouchEvent(ev);
			}
			Set<View> views = getReturnInvalidAreaView();
			if (views != null && views.size() > 0) {
				Iterator<View> iterator = views.iterator();
				while (iterator.hasNext()) {
					View view = iterator.next();
					if (view != null) {
						Rect invalidArea = getReturnInvalidArea(view);
						if (invalidArea != null
								&& invalidArea.contains((int) ev.getX(),
										(int) ev.getY())) {
							reset();
							mIsChildScrolling = true;
							return super.dispatchTouchEvent(ev);
						}
					}
				}
			}

			if (mGestureDetector == null) {
				mGestureDetector = new GestureDetector(this, this);
			}
			boolean handler = mGestureDetector.onTouchEvent(ev);
			if ((ev.getAction() == MotionEvent.ACTION_UP)
					|| (ev.getAction() == MotionEvent.ACTION_CANCEL)) {
				reset();
				if (this.mIsChildScrolling == true) {
					this.mIsChildScrolling = false;
				}
			}
			if (handler) {
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			boolean dispatchTouchEvent = super.dispatchTouchEvent(ev);
			return handler | dispatchTouchEvent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * The sub Activity implement, set the Ui Layout
	 * 
	 * @return
	 */
	protected abstract int getLayoutId();

	protected void onActivityInit() {
		// CCPAppManager.setContext(this);
	}

	/**
	 * 如果子界面需要拦截处理注册的广播 需要实现该方法
	 * 
	 * @param context
	 * @param intent
	 */
	protected void handleReceiver(Context context, Intent intent) {
		// 广播处理
		if (intent == null) {
			return;
		}

	}

	public void onBaseContentViewAttach(View contentView) {
		setContentView(contentView);
	}

	public FragmentActivity getActionBarActivityContext() {
		return mBaseActivity.getFragmentActivity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);


		mBaseActivity.onPause();

	}

	@Override
	protected void onResume() {
		// HSCoreService
		super.onResume();

		MobclickAgent.onPageStart(this.getClass().getName());

		MobclickAgent.onResume(this);


		mBaseActivity.onResume();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogUtil.d(TAG, "checktask onCreate:" + super.getClass().getSimpleName()
				+ "#0x" + super.hashCode() + ", taskid:" + getTaskId()
				+ ", task:" + new ActivityTaskUtils(this));
		super.onDestroy();
		mBaseActivity.onDestroy();
		try {
			unregisterReceiver(internalReceiver);
		} catch (Exception e) {
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mBaseActivity.onKeyDown(keyCode, event)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (mBaseActivity.onKeyUp(keyCode, event)) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void hideSoftKeyboard() {
		mBaseActivity.hideSoftKeyboard();
		
	}

	/**
	 * 跳转
	 * 
	 * @param clazz
	 * @param intent
	 */
	protected void startCCPActivity(Class<? extends Activity> clazz,
			Intent intent) {
		intent.setClass(this, clazz);
		startActivity(intent);
	}




	// Internal calss.
	private class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null || intent.getAction() == null) {
				return;
			}
			handleReceiver(context, intent);
		}
	}

	public Activity getActivitContext() {
		if (getParent() != null) {
			return getParent();
		}
		return null;
	}

	public TopBarView getTopBarView() {
		return mBaseActivity.getTopBarView();
	}

	/**
	 * 设置ActionBar标题
	 * 
	 * @param resid
	 */
	public void setActionBarTitle(int resid) {
		mBaseActivity.setActionBarTitle(getString(resid));
	}

	/**
	 * 设置ActionBar标题
	 * 
	 * @param text
	 */
	public void setActionBarTitle(CharSequence text) {
		mBaseActivity.setActionBarTitle(text);
	}

	/**
	 * 返回ActionBar 标题
	 * 
	 * @return
	 */
	public final CharSequence getActionBarTitle() {
		return mBaseActivity.getActionBarTitle();
	}

	/**
	 * #getLayoutId()
	 * 
	 * @return
	 */
	public View getActivityLayoutView() {
		return mBaseActivity.getActivityLayoutView();
	}

	/**
     *
     */
	public final void showTitleView() {
		mBaseActivity.showTitleView();
	}

	/**
     *
     */
	public final void hideTitleView() {
		mBaseActivity.hideTitleView();
	}

	public boolean isEnableRightSlideGesture() {
		return false;
	}

	protected Set<View> getReturnInvalidAreaView() {
		return null;
	}

	private Rect getReturnInvalidArea(View view) {
		if (view == null)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		Rect rect = new Rect();
		rect.left = location[0];
		rect.top = location[1];
		rect.right = (rect.left + view.getRight() - view.getLeft());
		rect.bottom = (rect.top + view.getBottom() - view.getTop());
		return rect;
	}

	private void reset() {
		this.mIsHorizontalScrolling = false;
		this.mScrollLimit = 0;
	}

	private boolean isCannotHorizontalScroll() {
		return (this.mScrollLimit >= 5);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (!(isEnableRightSlideGesture())) {
			return false;
		}
		if (isCannotHorizontalScroll()) {
			return false;
		}
		if ((!(this.mIsHorizontalScrolling))
				&& (Math.abs(2.0F * distanceY) > Math.abs(distanceX))) {
			this.mScrollLimit = (1 + this.mScrollLimit);
			return false;
		}
		this.mIsHorizontalScrolling = true;
		if (e1 == null || e2 == null) {
			return false;
		}
		float f1 = 0.0F;
		if (!(this.mIsChildScrolling)) {
			if (e1 != null) {
				f1 = e1.getX();
			}
			float f2 = 0.0F;
			if (e2 != null) {
				f2 = e2.getX();
			}
			if (f1 - f2 < getMinExitScrollX()) {
				this.mScrollLimit = 5;
				close();
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param mute
	 */
	public SpannableString setNewMessageMute(boolean mute) {
		mBaseActivity.setMute(mute);
		return mBaseActivity.buildActionTitle();
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	public void abstracrRegist() {

	}

	private int getMinExitScrollX() {
		if (this.mMinExitScrollX == 0) {
			this.mMinExitScrollX = (int) (getResources().getInteger(
					R.integer.min_exit_scroll_factor)
					* getWidthPixels() / 100.0F);
			this.mMinExitScrollX = (-this.mMinExitScrollX);
		}
		return this.mMinExitScrollX;
	}

	public int getWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}

	public void close() {
		finish();
	}
}

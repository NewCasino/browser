
package com.youkes.browser.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youkes.browser.R;
import com.youkes.browser.utils.DensityUtil;
import com.youkes.browser.utils.GlideUtil;
import com.youkes.browser.utils.URLUtil;


public class TopBarView extends LinearLayout {
	public static final int SHOW_SUTITLE = 2;
	private Context mContext;
	private ImageView mLeftButton;
	private TextView mMiddleButton;
	private TextView mMiddleSub;
	private ImageView mRightButton;
	private TextView mLeftText;
	private TextView mRightText;
	private OnClickListener mClickListener;

	private boolean mArrowUp = true;

	/**
	 * @param context
	 */
	public TopBarView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}


	ImageView searchBtn=null;

	public void showSearch(boolean b){
		if(searchBtn!=null){
			if(!b) {
				searchBtn.setVisibility(View.GONE);
			}else{
				searchBtn.setVisibility(View.VISIBLE);
			}
		}
	}

	ImageView mTitleIcon=null;

	public TextView getFinishBtn() {
		return finishBtn;
	}

	public void disableFinishBtn() {
		finishBtn.setBackgroundResource(R.drawable.btn_style_green_disable);
	}
	public void enableFinishBtn() {
		finishBtn.setBackgroundResource(R.drawable.btn_style_green);
	}
	public void showFinishBtn() {
		finishBtn.setVisibility(View.VISIBLE);
	}
	public void hideFinishBtn() {
		finishBtn.setVisibility(View.GONE);
	}
	TextView finishBtn=null;

	TextView textLeftBtn=null;
	private void initView() {
		setOrientation(LinearLayout.HORIZONTAL);
		// setBackgroundResource(R.drawable.actionbar_bg);
		setBackgroundDrawable(new ColorDrawable(mContext.getResources()
				.getColor(R.color.top_bar_color)));
		LayoutInflater.from(getContext()).inflate(R.layout.common_view_top_bar,
				this, true);

		textLeftBtn = (TextView) findViewById(R.id.text_left_btn);

		mLeftButton = (ImageView) findViewById(R.id.btn_left);

		mLeftButton = (ImageView) findViewById(R.id.btn_left);
		mTitleIcon = (ImageView) findViewById(R.id.title_icon);
		mMiddleButton = (TextView) findViewById(R.id.btn_middle);
		mMiddleSub = (TextView) findViewById(R.id.btn_middle_sub);
		mRightButton = (ImageView) findViewById(R.id.btn_right);
		mLeftText = (TextView) findViewById(R.id.text_left);
		mRightText = (TextView) findViewById(R.id.text_right);
		searchBtn = (ImageView) findViewById(R.id.btn_search);
		finishBtn = (TextView) findViewById(R.id.finish_btn);

	}


	public void showLeftTextBtn(boolean show){
		if(show) {
			textLeftBtn.setVisibility(View.VISIBLE);
		}else{
			textLeftBtn.setVisibility(View.GONE);
		}


	}



	public void showTitleIcon(boolean show){
		if(show) {
			mTitleIcon.setVisibility(View.VISIBLE);
		}else{
			mTitleIcon.setVisibility(View.GONE);
		}


	}


	public void displayIcon(String img){
		if(mTitleIcon==null) {
			return;
		}


		if(URLUtil.isValideUrl(img)) {
			mTitleIcon.setVisibility(View.VISIBLE);
			GlideUtil.displayImage(img, mTitleIcon);
			//GlideUtil.displayImage(img, mTitleIcon, ImageLoadOptions.getListOptions());
		}else{
			mTitleIcon.setVisibility(View.GONE);
		}

	}

	public void hideIcon(){
		if(mTitleIcon!=null) {

			mTitleIcon.setVisibility(View.GONE);
		}else{

		}

	}


	private void setMiddleSubTitle(int type, String title, String subTitle,
			OnClickListener onClickListener) {
		if (type == 1) {
			setOnClickListener(onClickListener);
		}
		setTitle(title);
		if (TextUtils.isEmpty(subTitle) || type == 2) {
			mMiddleSub.setVisibility(View.GONE);
			return;
		}
		mMiddleSub.setText(subTitle);
		mMiddleSub.setVisibility(View.VISIBLE);
		mMiddleSub.setOnClickListener(onClickListener);
	}

	/**
	 * 显示正在加载Progressba
	 */
	public void showTopProgressbar() {
		mRightButton.setVisibility(View.GONE);
		mRightText.setVisibility(View.GONE);
		((RelativeLayout) findViewById(R.id.top_progressbar))
				.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置TopBarView 右边按钮的背景
	 *
	 * @param resId
	 */
	public void setRightButtonRes(int resId) {
		if (resId == -1) {
			mRightButton.setVisibility(View.GONE);
			return;
		}
		int padding = getContext().getResources().getDimensionPixelSize(
				R.dimen.btn_topbar_paddingHorizontal);
		mRightButton.setImageResource(resId);
		mRightButton.setPadding(padding, 0, padding, 0);
	}

	/**
	 * 设置右边按钮的显示文字
	 *
	 * @param text
	 */
	public void setRightButtonText(String text) {
		if (text == null) {
			mRightText.setVisibility(View.GONE);
			return;
		}
		mRightText.setText(text);
	}

	/**
	 * 设置TopBarView 顶部更新提示是否显示
	 *
	 * @param show
	 */
	public void setTopbarUpdatePoint(boolean show) {
		View mTopbarUpdatePoint = findViewById(R.id.topbar_update_point);
		if (show) {
			mTopbarUpdatePoint.setVisibility(View.VISIBLE);
			return;
		}
		mTopbarUpdatePoint.setVisibility(View.GONE);
	}

	/**
	 * 设置TopBarView 右侧按钮的显示
	 */
	public void setRightVisible() {
		mRightButton.setVisibility(View.VISIBLE);
		mRightText.setVisibility(View.VISIBLE);
		((RelativeLayout) findViewById(R.id.top_progressbar))
				.setVisibility(View.GONE);
	}

	/**
	 * 设置TopBarView RightPoint是否显示
	 *
	 * @param show
	 */
	public void setTopbarRightPoint(boolean show) {
		View mTopbarRightPoint = findViewById(R.id.right_point);
		if (show) {
			mTopbarRightPoint.setVisibility(View.VISIBLE);
			return;
		}
		mTopbarRightPoint.setVisibility(View.GONE);
	}

	/**
	 * @return the mLeftButton
	 */
	public ImageView getLeftButton() {
		return mLeftButton;
	}

	/**
	 * @return the mRightButton
	 */
	public ImageView getRightButton() {
		return mRightButton;
	}

	/**
	 * @return the mLeftText
	 */
	public TextView getLeftText() {
		return mLeftText;
	}


	public TextView getLeftTextButton() {
		return textLeftBtn;
	}

	/**
	 * @return the mRightText
	 */
	public TextView getRightText() {
		return mRightText;
	}

	public void setFront() {
		bringToFront();
	}

	/**
	 * 显示up 或者Down 的图标
	 *
	 * @param up
	 * @param arrow
	 */
	public void setMiddleBtnArrowUp(boolean up, boolean arrow) {
		if (mArrowUp == up && !arrow) {
			return;
		}

		mArrowUp = up;
		int id = R.drawable.common_top_bar_arrow_down;
		if (mArrowUp) {
			id = R.drawable.common_top_bar_arrow_up;
		}
		Drawable upDownDrawable = mContext.getResources().getDrawable(id);
		upDownDrawable.setBounds(0, 0, upDownDrawable.getIntrinsicWidth(),
				upDownDrawable.getIntrinsicHeight());
		mMiddleButton.setCompoundDrawablePadding(DensityUtil.dip2px(5.0F));
		mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
				upDownDrawable, null);
	}

	/**
	 * 设置MiddleButton 的padding
	 *
	 * @param padding
	 */
	public void setMiddleBtnPadding(int padding) {
		if (mMiddleButton == null) {
			return;
		}
		mMiddleButton.setPadding(padding, 0, padding, 0);
	}

	/**
	 * 右侧按钮是否可用
	 *
	 * @param enabled
	 */
	public void setRightBtnEnable(boolean enabled) {
		mRightButton.setEnabled(enabled);
		mRightText.setEnabled(enabled);

		if (!enabled) {
			//99b991
			mRightText.setTextColor(Color.rgb(141, 177, 132));
		}else{
			mRightText.setTextColor(Color.rgb(255, 255, 255));
		}

	}

	/**
	 * 设置TopBarView 标题
	 *
	 * @param title
	 */
	public void setTitle(CharSequence title) {
		if (TextUtils.isEmpty(title)) {
			mMiddleButton.setVisibility(View.INVISIBLE);
			return;
		}
		mMiddleButton.setText(title);
		mMiddleButton.setVisibility(View.VISIBLE);
		mMiddleButton.setOnClickListener(mClickListener);

		doSetTouchDelegate();
	}

	/**
	 * 设置TopBarView 标题
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		if (TextUtils.isEmpty(title)) {
			mMiddleButton.setVisibility(View.INVISIBLE);
			return;
		}
		mMiddleButton.setText(title);
		mMiddleButton.setVisibility(View.VISIBLE);
		mMiddleButton.setOnClickListener(mClickListener);

		doSetTouchDelegate();
	}

	// 设置标题的可点击范围
	private void doSetTouchDelegate() {
		final TextView middleBtn = mMiddleButton;
		post(new Runnable() {

			@Override
			public void run() {
				Rect rect = new Rect();
				rect.left = (middleBtn.getWidth() / 4);
				rect.right = (3 * middleBtn.getWidth() / 4);
				rect.top = 0;
				rect.bottom = middleBtn.getHeight();
				middleBtn.setTouchDelegate(new TouchDelegate(rect, /*
																	 * TopBarView.
																	 * this
																	 */
						mMiddleSub));
			}
		});
	}

	/**
	 * 设置标题的背景
	 *
	 * @param resId
	 */
	public void setTitleDrawable(int resId) {
		if (resId == -1) {
			mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(resId), null, null, null);
			return;
		}
		mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
				null);
	}

	/**
	 *
	 * @param type
	 * @param leftResid
	 * @param rightResid
	 * @param titleRes
	 * @param l
	 */
	public void setTopBarToStatus(int type, int leftResid, int rightResid,
			int titleRes, OnClickListener l) {
		String str = "";
		if (titleRes != -1) {
			str = getResources().getString(titleRes);
		}
		setTopBarToStatus(type, leftResid, rightResid, null, null, str, "", l);
	}

	/**
	 * 设置纯图片的按钮TopBarView
	 *
	 * @param type
	 * @param leftResid
	 * @param rightResid
	 * @param title
	 * @param l
	 */
	public void setTopBarToStatus(int type, int leftResid, int rightResid,
			String title, OnClickListener l) {
		setTopBarToStatus(type, leftResid, rightResid, null, null, title, "", l);
	}

	/**
	 * 重载方法，设置返回、标题、右侧Action按钮
	 *
	 * @param type
	 * @param leftResid
	 * @param rightText
	 * @param title
	 * @param l
	 */
	public void setTopBarToStatus(int type, int leftResid, String rightText,
			String title, OnClickListener l) {
		setTopBarToStatus(type, leftResid, -1, null, rightText, title, "", l);
	}


	/**
	 * 设置TopBarView 属性
	 *
	 * @param type
	 *            类型
	 * @param leftResid
	 *            左边按钮背景
	 * @param rightResid
	 *            右边按钮背景
	 * @param leftText
	 *            左边按钮文字
	 * @param rightText
	 *            右边按钮文字
	 * @param title
	 *            标题文字
	 * @param subTitle
	 *            子标题文字
	 * @param l
	 */
	public void setTopBarToStatus(int type, int leftResid, int rightResid,
			String leftText, String rightText, String title, String subTitle,
			OnClickListener l) {
		mClickListener = l;
		findViewById(R.id.common_top_wrapper)
				.setOnClickListener(mClickListener);
		int padding = getContext().getResources().getDimensionPixelSize(
				R.dimen.btn_topbar_paddingHorizontal);
		if (leftResid <= 0 || leftText != null) {
			mLeftButton.setVisibility(View.GONE);
			if (leftText != null) {
				mLeftButton.setVisibility(View.GONE);
				mLeftText.setText(leftText);
				mLeftText.setVisibility(View.VISIBLE);
				mLeftText.setOnClickListener(l);
			} else {
				mLeftText.setVisibility(View.GONE);
			}

			if (leftResid > 0) {
				mLeftText.setBackgroundResource(leftResid);
				mLeftText.setPadding(padding, 0, padding, 0);
			}
		} else {
			mLeftButton.setImageResource(leftResid);
			mLeftButton.setPadding(padding, 0, padding, 0);
			mLeftButton.setVisibility(View.VISIBLE);
			mLeftButton.setOnClickListener(l);
		}

		if (rightResid <= 0 || rightText != null) {
			mRightButton.setVisibility(View.GONE);

			if (rightText != null) {
				mRightButton.setVisibility(View.GONE);
				mRightText.setText(rightText);
				mRightText.setVisibility(View.VISIBLE);
				mRightText.setOnClickListener(l);
			} else {
				mRightText.setVisibility(View.GONE);
			}

			if (rightResid > 0) {
				mRightText.setBackgroundResource(rightResid);
				mRightText.setPadding(padding, 0, padding, 0);
			}

		} else {
			mRightButton.setImageResource(rightResid);
			mRightButton.setPadding(padding, 0, padding, 0);
			mRightButton.setVisibility(View.VISIBLE);
			mRightButton.setOnClickListener(l);
		}

		setMiddleSubTitle(type, title, subTitle, l);

		searchBtn.setClickable(true);
		searchBtn.setOnClickListener(mClickListener);
	}

	public void setFinishText(String s) {
		this.finishBtn.setText(s);
	}

	public void enableRightBtn(int resId, String text, OnClickListener listener) {
		mRightText.setVisibility(View.VISIBLE);
		mRightText.setBackgroundResource(resId);
		mRightText.setText(text);
		mRightText.setOnClickListener(listener);
	}
}

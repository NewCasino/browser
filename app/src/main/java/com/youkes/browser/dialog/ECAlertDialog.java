
package com.youkes.browser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.youkes.browser.R;
import com.youkes.browser.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ECAlertDialog extends Dialog implements View.OnClickListener {

    public static final String TAG = "com.youkes.browser.AlertDialog";
    /**左边按钮*/
    public static final int BUTTON_NEGATIVE = 0;
    /**中间按钮*/
    public static final int BUTTON_NEUTRAL = 1;
    /**右边按钮*/
    public static final int BUTTON_POSITIVE = 2;
    private boolean mDismiss = true;
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = false;
    private List<Button> mButtons;
    /**对话框标题*/
    private View mLayoutTitle;
    /**对话框内容*/
    private ViewGroup mLayoutContent;
    /**对话框按钮*/
    private View mLayoutButton;

    /**
     * @param context
     */
    public ECAlertDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        super.setContentView(R.layout.common_dialog_generic);
        initView();
    }

    /**
     *
     */
    private void initView() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        mButtons = new ArrayList<Button>();
        Button leftBtn = (Button) findViewById(R.id.dilaog_button1);
        leftBtn.setOnClickListener(this);
        mButtons.add(leftBtn);
        Button middleBtn = (Button) findViewById(R.id.dilaog_button2);
        middleBtn.setOnClickListener(this);
        mButtons.add(middleBtn);
        Button rightBtn = (Button) findViewById(R.id.dilaog_button3);
        rightBtn.setOnClickListener(this);
        mButtons.add(rightBtn);
        mLayoutTitle = findViewById(R.id.dialog_layout_title);
        mLayoutContent = ((ViewGroup) findViewById(R.id.dialog_layout_content));
        mLayoutButton = findViewById(R.id.dialog_layout_button);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setTitle(R.string.dialog_title_alert);
    }

    public static ECAlertDialog buildAlert(Context ctx, int message,int button , OnClickListener listener) {
        return buildAlert(ctx, ctx.getString(message) , ctx.getString(button), listener);
    }

    public static ECAlertDialog buildAlert(Context ctx, String message,int button , OnClickListener listener) {
        return buildAlert(ctx,message , ctx.getString(button), listener);
    }
    
    public static ECAlertDialog buildAlert(Context ctx, int resId, OnClickListener listener) {
        return buildAlert(ctx, resId, R.string.dialog_btn_cancel, R.string.dialog_btn_confim, null, listener);
    }

    public static ECAlertDialog buildAlert(Context ctx, int resId,
                                           OnClickListener negativeClickListener,
                                           OnClickListener positive) {
        return buildAlert(ctx, ctx.getString(resId), ctx.getString(R.string.dialog_btn_cancel), ctx.getString(R.string.dialog_btn_confim), negativeClickListener, positive);
    }

    public static ECAlertDialog buildAlert(Context ctx, CharSequence message,
                                           OnClickListener listener) {
        return buildAlert(ctx, message, ctx.getString(R.string.dialog_btn_cancel), ctx.getString(R.string.dialog_btn_confim), null, listener);
    }

    
    
    public static ECAlertDialog buildAlert(Context ctx, CharSequence message,
                                           OnClickListener negativeClickListener,
                                           OnClickListener positive) {
        return buildAlert(ctx, message, ctx.getString(R.string.dialog_btn_cancel), ctx.getString(R.string.dialog_btn_confim), negativeClickListener, positive);
    }

    public static ECAlertDialog buildAlert(Context ctx, int message,
                                           int leftBtnText, int rightText,
                                           OnClickListener negativeClickListener,
                                           OnClickListener positive) {
        return buildAlert(ctx, ctx.getString(message), ctx.getString(leftBtnText), ctx.getString(rightText), negativeClickListener, positive);
    }

    /**
     * 创建对话框
     * @param ctx 上下文
     * @param message 对话框内容
     * @param leftBtnText 取消按钮文本
     * @param rightText 确定按钮文本
     * @param negativeClickListener
     * @param positive
     * @return
     */
    public static ECAlertDialog buildAlert(Context ctx, CharSequence message,
                                           CharSequence leftBtnText, CharSequence rightText,
                                           OnClickListener negativeClickListener,
                                           OnClickListener positive) {
        ECAlertDialog dialog = new ECAlertDialog(ctx);
        dialog.setMessage(message);
        dialog.setButton(BUTTON_NEGATIVE, leftBtnText, negativeClickListener);
        dialog.setButton(BUTTON_POSITIVE, rightText, positive);
        return dialog;
    }

    /**
     * 创建只有一个按钮的对话框
     * @param ctx
     * @param message
     * @param text
     * @param positive
     * @return
     */
    public static ECAlertDialog buildAlert(Context ctx, CharSequence message,CharSequence text,
                                           OnClickListener positive) {
        ECAlertDialog dialog = new ECAlertDialog(ctx);
        dialog.setMessage(message);
        dialog.setButton(BUTTON_NEGATIVE, text, positive);
        return dialog;
    }

    public static ECAlertDialog buildPositiveAlert(Context ctx , int resId , OnClickListener listener) {
        return buildPositiveAlert(ctx, ctx.getString(resId), listener);
    }

    /**
     *
     * @param ctx
     * @param message
     * @param listener
     * @return
     */
    public static ECAlertDialog buildPositiveAlert(Context ctx , CharSequence message , OnClickListener listener) {
        ECAlertDialog dialog = new ECAlertDialog(ctx);
        dialog.setMessage(message);
        dialog.setButton(BUTTON_POSITIVE, ctx.getString(R.string.dialog_btn_confim), listener);
        return dialog;
    }

    /**
     * 设置对话框按钮
     * @param id
     * @param resId
     * @param listener
     * @return
     */
    public final Button setButton(int id , int resId , OnClickListener listener) {
        return setButton(resId, getContext().getString(resId), listener);
    }

    /**
     * 设置按钮
     * @param id 按钮号
     * @param text 按钮显示文本
     * @param listener
     * @return
     */
    public final Button setButton(int id , CharSequence text , OnClickListener listener) {
        Button button = mButtons.get(id);
        button.setText(text);
        button.setVisibility(View.VISIBLE);
        setButtonTag(id, listener);
        mLayoutButton.setVisibility(View.VISIBLE);
        return button;
    }

    public final ECAlertDialog setButtonTag(int id , OnClickListener listener) {
        Button button = mButtons.get(id);
        button.setTag(listener);
        return this;
    }

    public final void setMessage(int resId) {
        setMessage(getContext().getString(resId));
    }

    /**
     * 设置对话框显示文本
     * @param text
     */
    public final void setMessage(CharSequence text) {
        ((TextView)findViewById(R.id.dialog_tv_message)).setText(text);
    }

    public final void setTitleNormalColor() {
        ((TextView)findViewById(R.id.dialog_tv_title)).setTextColor(getContext().getResources().getColor(R.color.text_content));
    }

    /**
     * 设置标题是否可见
     * @param visibility
     */
    public final void setTitleVisibility(int visibility) {
        mLayoutTitle.setVisibility(visibility);
    }

    /**
     * 设置内容显示区域
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public final void setContentPadding(int left, int top, int right, int bottom) {
        if (left < 0) {
            left = mLayoutContent.getPaddingLeft();
        }
        if (top < 0) {
            top = mLayoutContent.getPaddingRight();
        }
        if (right < 0) {
            right = mLayoutContent.getPaddingTop();
        }
        if (bottom < 0) {
            bottom = mLayoutContent.getPaddingBottom();
        }
        Drawable localDrawable = mLayoutContent.getBackground();
        mLayoutContent.setPadding(left, top, right, bottom);
        mLayoutContent.setBackgroundDrawable(localDrawable);
    }

    public final View getContent() {
        return mLayoutContent;
    }

    /**
     * 点击按钮不销毁对话框
     */
    public void setDismissFalse() {
        mDismiss = false;
    }

    /**
     *
     * @param view
     * @return
     */
    private int getViewLocation(View view) {
        for(int i = 0 ; i < mButtons.size() ; i ++) {
            if(mButtons.get(i) == view) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClick(View v) {
        OnClickListener clickListener = (OnClickListener) v.getTag();
        if(clickListener != null) {
            clickListener.onClick(this, getViewLocation(v));
        }
        if(mDismiss) {
            dismiss();
            return ;
        }
        // mDismiss = true;
    }

    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable = flag;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        mCanceledOnTouchOutside = cancel;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d(TAG, "onTouchEvent");
        if(mCancelable && mCanceledOnTouchOutside && event.getAction() == MotionEvent.ACTION_DOWN) {
            cancel();
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置自定义View
     */
    public final void setContentView(int resource) {
        setContentView(getLayoutInflater().inflate(resource, null));
    }

    public void setContentView(View contentView) {
        if (mLayoutContent.getChildCount() > 0) {
            mLayoutContent.removeAllViews();
        }
        mLayoutContent.addView(contentView);
    }

    public void setContentView(View child, ViewGroup.LayoutParams params) {
        if (mLayoutContent.getChildCount() > 0) {
            mLayoutContent.removeAllViews();
        }
        mLayoutContent.addView(child, params);
    }

    /**
     * 设置对话框标题
     */
    public void setTitle(int title) {
        setTitle(getContext().getString(title));
    }

    /**
     * 设置对话框标题
     */
    public void setTitle(CharSequence text) {
        if ((text == null) || (TextUtils.isEmpty(text.toString()))) {
        	((TextView) findViewById(R.id.dialog_tv_title)).setText("");
        	((TextView) findViewById(R.id.dialog_tv_title)).setVisibility(View.GONE);
            mLayoutContent.setVisibility(View.VISIBLE);
            setTitleVisibility(View.VISIBLE);
            return;
        }
        ((TextView) findViewById(R.id.dialog_tv_title)).setText(text);
        mLayoutContent.setVisibility(View.VISIBLE);
        setTitleVisibility(View.VISIBLE);
    }

    public void show() {
        super.show();

        int i = 0;
        Button btn = null;
        Iterator<Button> iterator = mButtons.iterator();
        while (iterator.hasNext()) {
            Button button = iterator.next();
            if(button.getVisibility() != View.VISIBLE) {
                continue;
            }
            ++i;
            btn = button;
        }
        if (i == 1) {
            btn.setBackgroundResource(R.drawable.btn_dialog_single);
        }
        if (i == 2) {
            btn.setSelected(true);
            ((ViewGroup.MarginLayoutParams)(this.mButtons.get(0)).getLayoutParams()).rightMargin = 1;
        }
        if (i == 3)  {
            btn.setSelected(true);
            ((ViewGroup.MarginLayoutParams)(this.mButtons.get(2)).getLayoutParams()).leftMargin = 1;
            ((ViewGroup.MarginLayoutParams)(this.mButtons.get(0)).getLayoutParams()).rightMargin = 1;
        }

    }


}

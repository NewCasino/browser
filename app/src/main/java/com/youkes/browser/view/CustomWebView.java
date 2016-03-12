package com.youkes.browser.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.youkes.browser.R;


import java.lang.reflect.Method;

/**
 * A convenient extension of WebView.
 */
public class CustomWebView extends WebView {

	private Context context;

	// override all other constructor to avoid crash
	public CustomWebView(Context context) {
		super(context);
		this.context = context;

		this.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
				//menuInfo.
				//ToastUtil.showMessage("info:");
			}
		});

	}

	private void enterTextSelection() {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1)
			return;

	}


	public class WebAppInterface {
		Context mContext;

		WebAppInterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void getText(String text) {
			// put selected text into clipdata
			ClipboardManager clipboard = (ClipboardManager)
					mContext.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("simple text", text);
			clipboard.setPrimaryClip(clip);
			// gives the toast for selected text
			Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
		}
	}

	// setting custom action bar
	private ActionMode mActionMode;
	private ActionMode.Callback mSelectActionModeCallback;
	private GestureDetector mDetector;

	// this will over ride the default action bar on long press
	@Override
	public ActionMode startActionMode(ActionMode.Callback callback) {
		ViewParent parent = getParent();
		if (parent == null) {
			return null;
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			String name = callback.getClass().toString();
			if (name.contains("SelectActionModeCallback")) {
				mSelectActionModeCallback = callback;
				mDetector = new GestureDetector(context,
						new CustomGestureListener());
			}
		}
		CustomActionModeCallback mActionModeCallback = new CustomActionModeCallback();
		return parent.startActionModeForChild(this, mActionModeCallback);
	}

	private class CustomActionModeCallback implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mActionMode = mode;
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.menu_main, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			mode.finish();
			return false;

		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				clearFocus();
			} else {
				if (mSelectActionModeCallback != null) {
					mSelectActionModeCallback.onDestroyActionMode(mode);
				}
				mActionMode = null;
			}
		}
	}

	private void getSelectedData() {

		String js = "(function getSelectedText() {" +
				"var txt;" +
				"if (window.getSelection) {" +
				"txt = window.getSelection().toString();" +
				"} else if (window.document.getSelection) {" +
				"txt = window.document.getSelection().toString();" +
				"} else if (window.document.selection) {" +
				"txt = window.document.selection.createRange().text;" +
				"}" +
				"JSInterface.getText(txt);" +
				"})()";
		// calling the js function
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			evaluateJavascript("javascript:" + js, null);
		} else {
			loadUrl("javascript:" + js);
		}
	}

	private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (mActionMode != null) {
				mActionMode.finish();
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Send the event to our gesture detector
		// If it is implemented, there will be a return value
		if (mDetector != null)
			mDetector.onTouchEvent(event);
		// If the detected gesture is unimplemented, send it to the superclass
		return super.onTouchEvent(event);
	}

}





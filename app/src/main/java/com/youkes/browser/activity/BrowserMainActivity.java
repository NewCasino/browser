package com.youkes.browser.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.youkes.browser.R;
import com.youkes.browser.preference.PreferenceManager;

@SuppressWarnings("deprecation")
public class BrowserMainActivity extends BrowserActivity {

	CookieManager mCookieManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void updateCookiePreference() {
		mCookieManager = CookieManager.getInstance();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			CookieSyncManager.createInstance(this);
		}

		mCookieManager.setAcceptCookie(PreferenceManager.getInstance().getCookiesEnabled());
		super.updateCookiePreference();

	}

	@Override
	public synchronized void initializeTabs() {

		restoreOrNewTab();

		closeDrawers();
		// if incognito mode use newTab(null, true); instead
	}



	@Override
	protected void onNewIntent(Intent intent) {
		handleNewIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!isFinished) {
			saveOpenTabs();
		}

	}

	@Override
	public void updateHistory(String title, String url) {
		super.updateHistory(title, url);
		addItemToHistory(title, url);
	}

	@Override
	public boolean isIncognito() {
		return false;
	}



	@Override
	public void closeActivity() {
		closeDrawers();
		this.finish();
		//moveTaskToBack(true);
	}
}

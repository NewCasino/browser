
package com.youkes.browser.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebIconDatabase;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewDatabase;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.VideoView;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.youkes.browser.MainApp;
import com.youkes.browser.R;
import com.youkes.browser.config.BrowserType;
import com.youkes.browser.config.ServerConfig;
import com.youkes.browser.constant.BookmarkPage;
import com.youkes.browser.constant.Constants;
import com.youkes.browser.constant.HistoryPage;
import com.youkes.browser.controller.BrowserController;
import com.youkes.browser.database.BookmarkDatabase;
import com.youkes.browser.database.BookmarkManager;
import com.youkes.browser.database.HistoryDatabase;
import com.youkes.browser.database.HistoryItem;
import com.youkes.browser.dialog.ECListDialog;

import com.youkes.browser.object.ClickHandler;
import com.youkes.browser.object.DrawerArrowDrawable;
import com.youkes.browser.object.SearchAdapter;
import com.youkes.browser.open.QQShareApi;
import com.youkes.browser.open.WeixinShareApi;
import com.youkes.browser.preference.PreferenceManager;
import com.youkes.browser.preference.PreferenceUtils;
import com.youkes.browser.site.SiteHotActivity;
import com.youkes.browser.utils.ImageSaveUtil;
import com.youkes.browser.utils.LinkType;
import com.youkes.browser.utils.LinkUtils;
import com.youkes.browser.utils.SoftKeyboardUtil;
import com.youkes.browser.utils.StringUtils;
import com.youkes.browser.utils.ToastUtil;
import com.youkes.browser.utils.Utils;
import com.youkes.browser.view.AnimatedProgressBar;
import com.youkes.browser.view.HtmlPage;
import com.youkes.browser.view.LightningView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class BrowserActivity extends ThemableActivity implements BrowserController, OnClickListener,ViewTreeObserver.OnScrollChangedListener {

	// Layout
	private DrawerLayout mDrawerLayout;
	private FrameLayout  mBrowserFrame;
	private FullscreenHolder mFullscreenContainer;
	private ListView mDrawerListLeft, mDrawerListRight;
	private LinearLayout mDrawerLeft, mDrawerRight;
	private LinearLayout mUiLayout, mToolbarLayout;
	private RelativeLayout mSearchBar;

	// List
	private final List<LightningView> mWebViews = new ArrayList<>();
	private List<HistoryItem> mBookmarkList;
	//private LightningView getCurrentWebView();

	GestureDetector detector = null;

	private AnimatedProgressBar mProgressBar;
	private AutoCompleteTextView mSearch;
	private ImageView mArrowImage;
	private VideoView mVideoView;
	private View mCustomView, mVideoProgressView;

	// Adapter
	private BookmarkViewAdapter mBookmarkAdapter;
	private LightningViewAdapter mTitleAdapter;
	private SearchAdapter mSearchAdapter;

	// Callback
	private ClickHandler mClickHandler;
	private CustomViewCallback mCustomViewCallback;
	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> mFilePathCallback;

	// Context
	private Activity mActivity;

	// Native
	private boolean mSystemBrowser = false, mIsNewIntent = false, mFullScreen, mColorMode,
			mDarkTheme;
	private int mOriginalOrientation, mBackgroundColor, mIdGenerator;
	private String mSearchText, mUntitledTitle, mHomepage, mCameraPhotoPath;

	// Storage
	private HistoryDatabase mHistoryDatabase;
	private BookmarkDatabase mBookmarkManager;
	private PreferenceManager mPreferences;

	// Image
	private Bitmap mDefaultVideoPoster, mWebpageBitmap;
	private final ColorDrawable mBackground = new ColorDrawable();
	private Drawable mDeleteIcon, mRefreshIcon, mCopyIcon, mIcon;
	private DrawerArrowDrawable mArrowDrawable;

	// Helper
	private boolean mI2PHelperBound;
	private boolean mI2PProxyInitialized;

	// Constant
	private static final int API = android.os.Build.VERSION.SDK_INT;
	private static final LayoutParams MATCH_PARENT = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);
	private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(StringUtils.isEmpty(PreferenceUtils.getUserId())){
			PreferenceUtils.setAnonymous();
		}

		isFinished=false;
		initialize();
		detector = new GestureDetector(this,gestureListener);
		//setHasOptionsMenu(true);

		hideTabsPanel();
		hideSettingPanel();



		if(mDrawerLayout!=null) {
			mDrawerLayout.closeDrawers();
		}

	}


	private GestureDetector.OnGestureListener gestureListener=new GestureDetector.OnGestureListener() {
		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			//Toast.makeText(BrowserActivity.this, "tap up", Toast.LENGTH_SHORT).show();
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			//Toast.makeText(BrowserActivity.this, "e1.x"+e1.getX()+" e2.x"+e2.getX(), Toast.LENGTH_SHORT).show();

			return false;

		}

		@Override
		public void onLongPress(MotionEvent e) {


			LightningView w=getCurrentWebView();
			BrowserActivity.this.onLongPress();
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			if(showTabs){
				//return browserTabsFragment.flingTabsView(e1,e2,velocityX,velocityY);
				return true;
				//return browserTabsFragment.flingTabsView( e1,  e2,  velocityX,  velocityY);
			}
			float screenX=MainApp.getInstance().getScreenWidth();

			if (e1.getX() - e2.getX() > screenX/8.0f) {
				if(e1.getX()>screenX-screenX/32.0f){

					//Toast.makeText(BrowserActivity.this, "向左手势", Toast.LENGTH_SHORT).show();
					return true;
				}


			} else if (e2.getX() - e1.getX() > screenX/8.0f) {
				if(e1.getX()<screenX/32.0f) {
					//Toast.makeText(BrowserActivity.this, "向右手势", Toast.LENGTH_SHORT).show();
					return true;
				}
			}


			//Toast.makeText(BrowserActivity.this, "e1.x"+e1.getX()+" e2.x"+e2.getX(), Toast.LENGTH_LONG).show();

			return false;

		}
	};




	TextView windowsText=null;


	View settingPanel=null;
	boolean showSetting=false;

	View tabsPanel=null;
	boolean showTabs=false;

	void flipTabsPanel(){
		if(tabsPanel==null){
			return;
		}
		if(!showTabs){
			showTabsWindow();
			tabsPanel.setVisibility(View.VISIBLE);
		}else{
			tabsPanel.setVisibility(View.GONE);
		}
		showTabs=!showTabs;

		tabsPanel.setVisibility(View.GONE);
	}

	private void showTabsWindow() {

		//browserTabsFragment.clearAll();
		//browserTabsFragment.addTabBitmapList(mWebViews, getCurrentWebView());
	}

	void hideTabsPanel(){
		if(tabsPanel==null){
			return;
		}
		showTabs=false;
		tabsPanel.setVisibility(View.GONE);
	}

	void flipSettingPanel(){
		if(settingPanel==null){
			return;
		}
		if(!showSetting){
			settingPanel.setVisibility(View.VISIBLE);
		}else{
			settingPanel.setVisibility(View.GONE);
		}
		showSetting=!showSetting;
	}

	void hideSettingPanel(){
		if(settingPanel==null){
			return;
		}
		showSetting=false;
		settingPanel.setVisibility(View.GONE);
	}


	void refreshWebPage(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				runRefreshWebPage();
				// mDemoSlider.removeAllSliders();
			}
		}, 200);
	}


	@Override
	public void onScrollChanged() {
		LightningView webview = getCurrentWebView();
		if (webview == null) {
			return;
		}
		if (webview.getScrollY() == 0) {
			//swipeLayout.setEnabled(true);
		} else {
			//swipeLayout.setEnabled(false);
		}
	}

	void runRefreshWebPage() {
		LightningView webview = getCurrentWebView();
		if (webview != null) {
			webview.reload();
		}

		//swipeLayout.setRefreshing(false);
	}

	Button fullscreenCancelBtn=null;
	View shortcutToolbar=null;
	//SwipeRefreshLayout swipeLayout =null;
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private synchronized void initialize() {
		setContentView(R.layout.activity_browser_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.browser_toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();

		mPreferences = PreferenceManager.getInstance();
		mDarkTheme = true;
		mActivity = this;
		mWebViews.clear();


		mClickHandler = new ClickHandler(this);
		mBrowserFrame = (FrameLayout) findViewById(R.id.browser_views);
		mToolbarLayout = (LinearLayout) findViewById(R.id.toolbar_layout);


		//swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);


		/*
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				runRefreshWebPage();
			}
		});
	*/

		settingPanel = findViewById(R.id.setting_panel);
		tabsPanel = findViewById(R.id.tabs_panel);

		// initialize background ColorDrawable
		mBackground.setColor(((ColorDrawable) mToolbarLayout.getBackground()).getColor());

		mUiLayout = (LinearLayout) findViewById(R.id.ui_layout);
		mProgressBar = (AnimatedProgressBar) findViewById(R.id.progress_view);

		View forwardView=findViewById(R.id.forward_action);
		forwardView.setOnClickListener(this);

		View backwardView=findViewById(R.id.backward_action);
		backwardView.setOnClickListener(this);



		View editView=findViewById(R.id.edit_action);
		editView.setOnClickListener(this);

		View fullscreenView=findViewById(R.id.fullscreen_action);
		fullscreenView.setOnClickListener(this);

		View shareWebPageView=findViewById(R.id.share_web_page_action);
		shareWebPageView.setOnClickListener(this);


		fullscreenCancelBtn=(Button)findViewById(R.id.fullscreen_cancel);
		fullscreenCancelBtn.setOnClickListener(this);


		shortcutToolbar=findViewById(R.id.shortcut_toolbar);
		if(PreferenceUtils.isUserVisitor()){
			editView.setVisibility(View.GONE);
		}else{
			editView.setVisibility(View.VISIBLE);
		}

		View favorateView=findViewById(R.id.favorite_action);
		favorateView.setOnClickListener(this);

		RelativeLayout newTab = (RelativeLayout) findViewById(R.id.new_tab_button);

		mDrawerLeft = (LinearLayout) findViewById(R.id.left_drawer);
		// Drawer stutters otherwise
		mDrawerLeft.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListLeft = (ListView) findViewById(R.id.left_drawer_list);
		mDrawerRight = (LinearLayout) findViewById(R.id.right_drawer);
		mDrawerRight.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		mDrawerListRight = (ListView) findViewById(R.id.right_drawer_list);

		windowsText=(TextView) findViewById(R.id.windows_text);

		setNavigationDrawerWidth();
		mDrawerLayout.setDrawerListener(new DrawerLocker());

		mWebpageBitmap = Utils.getWebpageBitmap(getResources(), mDarkTheme);

		mHomepage = mPreferences.getHomepage();

		mTitleAdapter = new LightningViewAdapter(this, R.layout.tab_list_item, mWebViews);
		mDrawerListLeft.setAdapter(mTitleAdapter);
		mDrawerListLeft.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerListLeft.setOnItemLongClickListener(new DrawerItemLongClickListener());

		mDrawerListRight.setOnItemClickListener(new BookmarkItemClickListener());
		mDrawerListRight.setOnItemLongClickListener(new BookmarkItemLongClickListener());

		mHistoryDatabase = HistoryDatabase.getInstance(getApplicationContext());

		// set display options of the ActionBar
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.toolbar_content);

		View v = actionBar.getCustomView();
		LayoutParams lp = v.getLayoutParams();
		lp.width = LayoutParams.MATCH_PARENT;
		v.setLayoutParams(lp);

		mArrowDrawable = new DrawerArrowDrawable(this);
		mArrowImage = (ImageView) actionBar.getCustomView().findViewById(R.id.arrow);
		// Use hardware acceleration for the animation
		mArrowImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		mArrowImage.setImageDrawable(mArrowDrawable);
		LinearLayout arrowButton = (LinearLayout) actionBar.getCustomView().findViewById(
				R.id.arrow_button);
		arrowButton.setOnClickListener(this);



		RelativeLayout back = (RelativeLayout) findViewById(R.id.action_back);
		back.setOnClickListener(this);

		RelativeLayout winV = (RelativeLayout) findViewById(R.id.action_windows);
		winV.setOnClickListener(this);

		RelativeLayout tabCloseV = (RelativeLayout) findViewById(R.id.action_tab_close);
		tabCloseV.setOnClickListener(this);

		RelativeLayout setV = (RelativeLayout) findViewById(R.id.action_settings);
		setV.setOnClickListener(this);

		RelativeLayout forward = (RelativeLayout) findViewById(R.id.action_forward);
		forward.setOnClickListener(this);


		RelativeLayout closeBtn = (RelativeLayout) findViewById(R.id.close_button);
		closeBtn.setOnClickListener(this);

		View clearTab=findViewById(R.id.clear_tab_btn);
		clearTab.setOnClickListener(this);

		View settingsBtn =  findViewById(R.id.settings_btn);
		settingsBtn.setOnClickListener(this);

		View historyBtn=findViewById(R.id.history_btn);
		historyBtn.setOnClickListener(this);

		View addBookmarkBtn=findViewById(R.id.add_bookmark_btn);
		addBookmarkBtn.setOnClickListener(this);
		View addBookmarkView=findViewById(R.id.add_bookmark_action);
		addBookmarkView.setOnClickListener(this);

		View shareBtn=findViewById(R.id.share_btn);
		shareBtn.setOnClickListener(this);

		View editBtn=findViewById(R.id.edit_btn);
		editBtn.setOnClickListener(this);

		if(PreferenceUtils.isUserVisitor()){
			editBtn.setVisibility(View.GONE);
		}else{
			editBtn.setVisibility(View.VISIBLE);
		}

		// create the search EditText in the ToolBar
		mSearch = (AutoCompleteTextView) actionBar.getCustomView().findViewById(R.id.search);
		mUntitledTitle = getString(R.string.untitled);
		mBackgroundColor = getResources().getColor(R.color.primary_color);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			mDeleteIcon = getResources().getDrawable(R.drawable.ic_action_delete);
			mRefreshIcon = getResources().getDrawable(R.drawable.ic_action_refresh);
			mCopyIcon = getResources().getDrawable(R.drawable.ic_action_copy);
		} else {
			Theme theme = getTheme();
			mDeleteIcon = getResources().getDrawable(R.drawable.ic_action_delete, theme);
			mRefreshIcon = getResources().getDrawable(R.drawable.ic_action_refresh, theme);
			mCopyIcon = getResources().getDrawable(R.drawable.ic_action_copy, theme);
		}

		int iconBounds = Utils.convertDpToPixels(24);
		mDeleteIcon.setBounds(0, 0, iconBounds, iconBounds);
		mRefreshIcon.setBounds(0, 0, iconBounds, iconBounds);
		mCopyIcon.setBounds(0, 0, iconBounds, iconBounds);
		mIcon = mRefreshIcon;
		SearchClass search = new SearchClass();
		mSearch.setCompoundDrawables(null, null, mRefreshIcon, null);
		mSearch.setOnKeyListener(search.new KeyListener());
		mSearch.setOnFocusChangeListener(search.new FocusChangeListener());
		mSearch.setOnEditorActionListener(search.new EditorActionListener());
		mSearch.setOnTouchListener(search.new TouchListener());

		mSystemBrowser = getSystemBrowser();
		Thread initialize = new Thread(new Runnable() {

			@Override
			public void run() {
				mBookmarkManager = BookmarkDatabase.getInstance(mActivity.getApplicationContext());
				mBookmarkList = mBookmarkManager.getLastItems(PreferenceUtils.getUserId(),0,1000);
				int order=0;
				if (mBookmarkList.size() == 0 && !PreferenceUtils.isBookmarkInited()) {
					int size=BookmarkManager.DEFAULT_BOOKMARKS.length;
					for (int i=size-1;i>=0;i--) {
						String[] array=BookmarkManager.DEFAULT_BOOKMARKS[i];
						HistoryItem bookmark = new HistoryItem(array[0], array[1]);
						bookmark.setOrder(order++);
						mBookmarkManager.addBookmarkItem(PreferenceUtils.getUserId(), array[0], array[1]);
						removeBookmarkUrl(bookmark.getUrl());
						mBookmarkList.add(0,bookmark);
					}
					//Collections.sort(mBookmarkList, new SortIgnoreCase());
					PreferenceUtils.setBookmarkInited();
				}
				mBookmarkAdapter = new BookmarkViewAdapter(mActivity, R.layout.bookmark_list_item,
						mBookmarkList);
				mDrawerListRight.setAdapter(mBookmarkAdapter);
				initializeSearchSuggestions(mSearch);
			}

		});
		initialize.run();

		newTab.setOnClickListener(this);
		newTab.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				return true;
			}

		});

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_right_shadow, GravityCompat.END);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_left_shadow, GravityCompat.START);

		initializeTabs();

		if (API <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
		}

		checkForProxy();

		//mDrawerRight.setVisibility(View.GONE);
		//mDrawerLeft.setVisibility(View.GONE);

	}



	private class SearchClass {

		public class KeyListener implements OnKeyListener {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

				switch (arg1) {
					case KeyEvent.KEYCODE_ENTER:
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
						searchTheWeb(mSearch.getText().toString());
						LightningView v=getCurrentWebView();
						if (v != null) {
							v.requestFocus();
						}
						return true;
					default:
						break;
				}
				return false;
			}

		}

		public class EditorActionListener implements OnEditorActionListener {
			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				// hide the keyboard and search the web when the enter key
				// button is pressed
				if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE
						|| actionId == EditorInfo.IME_ACTION_NEXT
						|| actionId == EditorInfo.IME_ACTION_SEND
						|| actionId == EditorInfo.IME_ACTION_SEARCH
						|| (arg2.getAction() == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
					searchTheWeb(mSearch.getText().toString());
					LightningView v=getCurrentWebView();
					if (v != null) {
						v.requestFocus();
					}
					return true;
				}
				return false;
			}
		}

		public class FocusChangeListener implements OnFocusChangeListener {
			@Override
			public void onFocusChange(View v, final boolean hasFocus) {
				LightningView wv=getCurrentWebView();
				if (!hasFocus && wv != null) {
					if (wv.getProgress() < 100) {
						setIsLoading();
					} else {
						setIsFinishedLoading();
					}
					updateUrl(wv.getUrl(), true);
				} else if (hasFocus) {
					String url = wv.getUrl();
					if (url == null || url.startsWith(Constants.FILE)) {
						mSearch.setText("");
					} else {
						mSearch.setText(url);
					}

					if(ServerConfig.isSearchUrl(url)){
						mSearch.setText(ServerConfig.getSearchWord(url));
					}

					((AutoCompleteTextView) v).selectAll(); // Hack to make sure
															// the text gets
															// selected
					mIcon = mCopyIcon;
					mSearch.setCompoundDrawables(null, null, mCopyIcon, null);
				}
				final Animation anim = new Animation() {

					@Override
					protected void applyTransformation(float interpolatedTime, Transformation t) {
						if (!hasFocus) {
							mArrowDrawable.setProgress(1.0f - interpolatedTime);
						} else {
							mArrowDrawable.setProgress(interpolatedTime);
						}
					}

					@Override
					public boolean willChangeBounds() {
						return true;
					}

				};
				anim.setDuration(300);
				anim.setInterpolator(new DecelerateInterpolator());
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						if (!hasFocus) {
							mArrowDrawable.setProgress(0.0f);
						} else {
							mArrowDrawable.setProgress(1.0f);
						}
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

				});
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mArrowImage.startAnimation(anim);
					}

				}, 100);

				if (!hasFocus) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
				}
			}
		}

		public class TouchListener implements OnTouchListener {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mSearch.getCompoundDrawables()[2] != null) {
					boolean tappedX = event.getX() > (mSearch.getWidth()
							- mSearch.getPaddingRight() - mIcon.getIntrinsicWidth());
					if (tappedX) {
						if (event.getAction() == MotionEvent.ACTION_UP) {
							if (mSearch.hasFocus()) {
								ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
								ClipData clip = ClipData.newPlainText("label", mSearch.getText()
										.toString());
								clipboard.setPrimaryClip(clip);
								Utils.showToast(
										mActivity,
										mActivity.getResources().getString(
												R.string.message_text_copied));
							} else {
								refreshOrStop();
							}
						}
						return true;
					}
				}
				return false;
			}

		}
	}

	private class DrawerLocker implements DrawerListener {

		@Override
		public void onDrawerClosed(View v) {

			if (v == mDrawerRight) {
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mDrawerLeft);
			} else {
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mDrawerRight);
			}

		}

		@Override
		public void onDrawerOpened(View v) {

			if (v == mDrawerRight) {
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerLeft);
			} else {
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
			}

		}

		@Override
		public void onDrawerSlide(View v, float arg) {
		}

		@Override
		public void onDrawerStateChanged(int arg) {
		}

	}

	/*
	 * If Orbot/Tor or I2P is installed, prompt the user if they want to enable
	 * proxying for this session
	 */
	private void checkForProxy() {
		boolean useProxy = mPreferences.getUseProxy();


		final boolean orbotInstalled = false;
		boolean orbotChecked = mPreferences.getCheckedForTor();
		boolean orbot = orbotInstalled && !orbotChecked;

		boolean i2pInstalled = false;
		boolean i2pChecked = mPreferences.getCheckedForI2P();
		boolean i2p = i2pInstalled && !i2pChecked;

		// TODO Is the idea to show this per-session, or only once?
		if (!useProxy && (orbot || i2p)) {
			if (orbot) mPreferences.setCheckedForTor(true);
			if (i2p) mPreferences.setCheckedForI2P(true);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			if (orbotInstalled && i2pInstalled) {
				String[] proxyChoices = this.getResources().getStringArray(R.array.proxy_choices_array);
				builder.setTitle(getResources().getString(R.string.http_proxy))
						.setSingleChoiceItems(proxyChoices, mPreferences.getProxyChoice(),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										mPreferences.setProxyChoice(which);
									}
								})
						.setNeutralButton(getResources().getString(R.string.action_ok),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if (mPreferences.getUseProxy())
											initializeProxy();
									}
								});
			} else {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								mPreferences.setProxyChoice(orbotInstalled ?
										Constants.PROXY_ORBOT : Constants.PROXY_I2P);
								initializeProxy();
								break;
							case DialogInterface.BUTTON_NEGATIVE:
								mPreferences.setProxyChoice(Constants.NO_PROXY);
								break;
						}
					}
				};

				builder.setMessage(orbotInstalled ? R.string.use_tor_prompt : R.string.use_i2p_prompt)
						.setPositiveButton(R.string.yes, dialogClickListener)
						.setNegativeButton(R.string.no, dialogClickListener);
			}
			builder.show();
		}
	}

	/*
	 * Initialize WebKit Proxying
	 */
	private void initializeProxy() {
		String host;
		int port;

		switch (mPreferences.getProxyChoice()) {
			case Constants.NO_PROXY:
				// We shouldn't be here
				return;

			case Constants.PROXY_ORBOT:

				break;

			case Constants.PROXY_I2P:

				break;

			default:
				host = mPreferences.getProxyHost();
				port = mPreferences.getProxyPort();
		}

		try {

		} catch (Exception e) {
			Log.d(Constants.TAG, "error enabling web proxying", e);
		}

	}

	public boolean isProxyReady() {
		if (mPreferences.getProxyChoice() == Constants.PROXY_I2P) {

		}

		return true;
	}

	private boolean isTablet() {
		return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	private void setNavigationDrawerWidth() {

		int width = getResources().getDisplayMetrics().widthPixels - Utils.convertDpToPixels(56);
		int maxWidth;
		if (isTablet()) {
			maxWidth = Utils.convertDpToPixels(320);
		} else {
			maxWidth = Utils.convertDpToPixels(300);
		}
		if (width > maxWidth) {
			DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerLeft
					.getLayoutParams();
			params.width = maxWidth;
			mDrawerLeft.setLayoutParams(params);
			mDrawerLeft.requestLayout();
			DrawerLayout.LayoutParams paramsRight = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerRight
					.getLayoutParams();
			paramsRight.width = maxWidth;
			mDrawerRight.setLayoutParams(paramsRight);
			mDrawerRight.requestLayout();
		} else {
			DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerLeft
					.getLayoutParams();
			params.width = width;
			mDrawerLeft.setLayoutParams(params);
			mDrawerLeft.requestLayout();
			DrawerLayout.LayoutParams paramsRight = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerRight
					.getLayoutParams();
			paramsRight.width = width;
			mDrawerRight.setLayoutParams(paramsRight);
			mDrawerRight.requestLayout();
		}

	}

	/*
	 * Override this class
	 */
	public synchronized void initializeTabs() {

	}

	public void restoreOrNewTab() {
		mIdGenerator = 0;

		int type=getIntent().getIntExtra("type",0);
		if(type== BrowserType.Search_Page){
			openSearchTab();
			return;
		}
		int selectTabIndex=mPreferences.getSelectedIndex();
		String url = getIntent().getStringExtra("url");
		if (url!=null&&(url.startsWith(Constants.HTTP)||url.startsWith(Constants.HTTPS))) {
			newTab(url, true);
			selectTabIndex=0;

		}


		if (mPreferences.getRestoreLostTabsEnabled()) {
			String mem = mPreferences.getMemoryUrl();
			mPreferences.setMemoryUrl("");
			String[] array = Utils.getArray(mem);
			int count = 0;
			for (String urlString : array) {
				if (urlString.length() > 0) {
					if ( mWebViews.size() >= ServerConfig.BrowserTabMax) {
						break;
					}
					newTab(urlString, true);
					count++;
				}
			}

		}



		if(selectTabIndex>=0&&selectTabIndex<mWebViews.size()) {
			LightningView lv0 = mWebViews.get(selectTabIndex);
			if(url!=null) {
				lv0.loadUrl(url);
			}

			if(lv0!=null) {
				showTab(lv0);
				if (selectTabIndex < mDrawerRight.getChildCount()) {
					mDrawerListLeft.setItemChecked(selectTabIndex, true);
				}
			}

		}



		if(mWebViews.size()==0) {
			newTab(Constants.Default_Page, true);
		}


	}

	private void openSearchTab() {
		//ToastUtil.showMessage("type:opensearch");
		newTab(null,true);
		LightningView v=getCurrentWebView();
		if(v!=null) {
			//openBookmarkPage(v.getWebView());
		}

		if(mSearch!=null) {
			//mSearch.requestFocus();
		}

	}

	public void initializePreferences() {
		if (mPreferences == null) {
			mPreferences = PreferenceManager.getInstance();
		}
		mFullScreen = mPreferences.getFullScreenEnabled();
		mColorMode = mPreferences.getColorModeEnabled();
		mColorMode &= !mDarkTheme;
		LightningView wv=getCurrentWebView();
		if (!isIncognito() && !mColorMode && !mDarkTheme && mWebpageBitmap != null) {
			changeToolbarBackground(mWebpageBitmap);
		} else if (!isIncognito() && wv != null && !mDarkTheme
				&& wv.getFavicon() != null) {
			changeToolbarBackground(wv.getFavicon());
		}

		if (mFullScreen && mBrowserFrame.findViewById(R.id.toolbar_layout) == null) {
			mUiLayout.removeView(mToolbarLayout);
			//mBrowserFrame.addView(mToolbarLayout);
			mToolbarLayout.bringToFront();
		} else if (mBrowserFrame.findViewById(R.id.toolbar_layout) != null) {
			//mBrowserFrame.removeView(mToolbarLayout);
			//mUiLayout.addView(mToolbarLayout, 0);
		}
		if (mPreferences.getHideStatusBarEnabled()) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		switch (mPreferences.getSearchChoice()) {

			case 0:
				mSearchText = Constants.BAIDU_SEARCH;
				break;
			case 1:
				mSearchText = Constants.BING_SEARCH;
				break;
		}

		updateCookiePreference();
		if (mPreferences.getUseProxy()) {
			initializeProxy();
		} else {
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
			mI2PProxyInitialized = false;
		}
	}

	/*
	 * Override this if class overrides BrowserActivity
	 */
	public void updateCookiePreference() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (mSearch.hasFocus()) {
				searchTheWeb(mSearch.getText().toString());
			}
		} else if ((keyCode == KeyEvent.KEYCODE_MENU) && (Build.VERSION.SDK_INT <= 16)
				&& (Build.MANUFACTURER.compareTo("LGE") == 0)) {
			// Workaround for stupid LG devices that crash
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU) && (Build.VERSION.SDK_INT <= 16)
				&& (Build.MANUFACTURER.compareTo("LGE") == 0)) {
			// Workaround for stupid LG devices that crash
			openOptionsMenu();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	protected boolean isFinished=false;
	private void browserExit() {
		SoftKeyboardUtil.hideSoftKeyboard(this);
		isFinished=true;
		saveOpenTabs();

		if(mDrawerLayout!=null) {
			mDrawerLayout.closeDrawers();
		}


		for(LightningView v:mWebViews){

				if (v != null) {
					v.onDestroy();
				}

		}
		this.finish();

	}

	/**
	 * refreshes the underlying list of the Bookmark adapter since the bookmark
	 * adapter doesn't always change when notifyDataChanged gets called.
	 */
	private void notifyBookmarkDataSetChanged() {
		mBookmarkAdapter.clear();
		mBookmarkAdapter.addAll(mBookmarkList);
		mBookmarkAdapter.notifyDataSetChanged();
	}

	/**
	 * method that shows a dialog asking what string the user wishes to search
	 * for. It highlights the text entered.
	 */
	private void findInPage() {
		final AlertDialog.Builder finder = new AlertDialog.Builder(mActivity);
		finder.setTitle(getResources().getString(R.string.action_find));
		final EditText getHome = new EditText(this);
		getHome.setHint(getResources().getString(R.string.search_hint));
		finder.setView(getHome);
		finder.setPositiveButton(getResources().getString(R.string.search_hint),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String query = getHome.getText().toString();
						if (query.length() > 0)
							showSearchInterfaceBar(query);
					}
				});
		finder.show();
	}

	private void showSearchInterfaceBar(String text) {

		LightningView wv=getCurrentWebView();
		if(wv==null){
			return;
		}


		mSearchBar = (RelativeLayout) findViewById(R.id.search_bar);
		mSearchBar.setVisibility(View.VISIBLE);

		TextView tw = (TextView) findViewById(R.id.search_query);
		tw.setText("'" + text + "'");

		ImageButton up = (ImageButton) findViewById(R.id.button_next);
		up.setOnClickListener(this);

		ImageButton down = (ImageButton) findViewById(R.id.button_back);
		down.setOnClickListener(this);

		ImageButton quit = (ImageButton) findViewById(R.id.button_quit);
		quit.setOnClickListener(this);
	}

	private void showCloseDialog(final int position) {
		/*
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity,
				android.R.layout.simple_dropdown_item_1line);
		adapter.add(mActivity.getString(R.string.close_tab));
		adapter.add(mActivity.getString(R.string.close_other_tabs));
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						deleteTab(position);
						break;
					case 1:
						closeOtherTabs(position);
						break;
					default:
						break;
				}
			}
		});
		builder.show();
		*/
	}

	private void closeOtherTabs(int position) {
		if(mWebViews==null){
			return;
		}
		int tabCnt=mWebViews.size();
		if (position >= tabCnt) {
			position=0;

		}

		for(int i=tabCnt;i<tabCnt;i++){
			if(i!=position){
				//deleteTab();
			}
		}

	}

	/**
	 * The click listener for ListView in the navigation drawer
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mIsNewIntent = false;
			showTab(mWebViews.get(position));
		}

	}

	/**
	 * long click listener for Navigation Drawer
	 */
	private class DrawerItemLongClickListener implements ListView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
			showCloseDialog(position);
			return true;
		}
	}

	private class BookmarkItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			LightningView wv=getCurrentWebView();
			if(wv==null){
				return;
			}


			wv.loadUrl(mBookmarkList.get(position).getUrl());

			// keep any jank from happening when the drawer is closed after the
			// URL starts to load
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
			}, 150);
		}
	}

	private class BookmarkItemLongClickListener implements ListView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(mActivity.getResources().getString(R.string.action_bookmarks));
			builder.setMessage(getResources().getString(R.string.dialog_bookmark))
					.setCancelable(true)
					.setPositiveButton(getResources().getString(R.string.action_new_tab),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									newTab(mBookmarkList.get(position).getUrl(), true);
									mDrawerLayout.closeDrawers();
								}
							})
					.setNegativeButton(getResources().getString(R.string.action_delete),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (mBookmarkManager.deleteBookmarkItem(
											PreferenceUtils.getUserId(),
											mBookmarkList.get(position)
													.getUrl())) {
										mBookmarkList.remove(position);
										notifyBookmarkDataSetChanged();
										mSearchAdapter.refreshBookmarks();
										openBookmarks();
									}
								}
							});

					/*
					.setNeutralButton(getResources().getString(R.string.action_edit_top),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									editBookmark(position);
								}
							});
							*/

			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}
	}

	/**
	 * Takes in the id of which bookmark was selected and shows a dialog that
	 * allows the user to rename and change the url of the bookmark
	 *
	 * @param id
	 *            which id in the list was chosen
	 */
	public synchronized void editBookmark(final int id) {

	}

	protected LightningView getLightningView(View v){
		for(int i=0;i<mWebViews.size();i++){
			LightningView lv=mWebViews.get(i);
			if(lv!=null&&lv.getWebView()==v){
				return lv;
			}
		}
		return null;
	}



	/**
	 * displays the WebView contained in the LightningView Also handles the
	 * removal of previous views
	 *
	 * @param view
	 *            the LightningView to show
	 */
	private synchronized void showTab(LightningView view) {

		if (view == null) {
			return;
		}


		// Set the background color so the color mode color doesn't show through
		mBrowserFrame.setBackgroundColor(mBackgroundColor);


		LightningView lv=getCurrentWebView();
		if (lv != null) {
			lv.setForegroundTab(false);
			lv.onPause();
		}

		mBrowserFrame.removeAllViews();//
		WebView wv = view.getWebView();
		mBrowserFrame.addView(wv, MATCH_PARENT);

		view.setForegroundTab(true);
		if (view.getWebView() != null) {
			updateUrl(view.getUrl(), true);
			updateProgress(view.getProgress());
		} else {
			updateUrl("", true);
			updateProgress(0);
		}






		//mBrowserFrame.setDisplayedChild(index);


		// Remove browser frame background to reduce overdraw
		mBrowserFrame.setBackgroundColor(0);
		view.requestFocus();
		view.onResume();

		// Use a delayed handler to make the transition smooth
		// otherwise it will get caught up with the showTab code
		// and cause a janky motion
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mDrawerLayout.closeDrawers();
			}
		}, 150);


		findViewById(R.id.browser_bottom_bar).invalidate();


	}



	/**
	 * creates a new tab with the passed in URL if it isn't null
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	public void handleNewIntent(Intent intent) {

		String url = null;
		if (intent != null) {
			url = intent.getDataString();
		}
		int num = 0;

		int type=0;
		if (intent != null && intent.getExtras() != null) {
			num = intent.getExtras().getInt(getPackageName() + ".Origin");
			//type=intent.getExtras().getInt("type");
			//ToastUtil.showMessage("this type "+type);
		}
		if (num == 1) {
			LightningView lv=getCurrentWebView();
			lv.loadUrl(url);
		} else if (url != null) {
			if (url.startsWith(Constants.FILE)) {
				Utils.showToast(this, getResources().getString(R.string.message_blocked_local));
				url = null;
			}
			newTab(url, true);
			mIsNewIntent = true;
		}
	}

	@Override
	public void closeEmptyTab() {
		LightningView lv=getCurrentWebView();
		if (lv != null && lv.getWebView().copyBackForwardList().getSize() == 0) {
			closeCurrentTab();
		}
	}

	private void closeCurrentTab() {
		LightningView lv=getCurrentWebView();
		//ToastUtil.showMessage("close current tab");
		// don't delete the tab because the browser will close and mess stuff up
		if(mDrawerLeft!=null) {
			int s=mWebViews.size();
				if(s>1) {
					deleteTab(getSelectedTabIndex());
					//getSelectedTabIndex()+1)+"/"+
				windowsText.setText(String.valueOf(mWebViews.size()));
			}else if(s==1){
				//ToastUtil.showMessage(getString(R.string.last_tab_not_close));
				openBookmarkPage(lv.getWebView());
			}

		}

	}

	ArrayList<LightningView> copyWebviewList(){
		ArrayList<LightningView> tmpList=new ArrayList<LightningView>();
		for(int i=0;i<mWebViews.size();i++){
			tmpList.add(i,mWebViews.get(i));
		}
		return tmpList;
	}

	void closeAllTab(){
		int s=mWebViews.size();
		LightningView lv=getCurrentWebView();


		for (int i=0;i<s;i++){
			LightningView reference = mWebViews.get(i);
			if(reference!=null&&reference!=lv) {
				reference.onDestroy();
			}
		}
		mWebViews.clear();
		mWebViews.add(lv);
		openBookmarkPage(lv.getWebView());
		windowsText.setText(String.valueOf(1));
		hideTabsPanel();
		hideSettingPanel();

	}


	@SuppressWarnings("deprecation")
	@Override
	public void onTrimMemory(int level) {
		if (level > TRIM_MEMORY_MODERATE && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			Log.d(Constants.TAG, "Low Memory, Free Memory");
			for (LightningView view : mWebViews) {
				view.getWebView().freeMemory();
			}
		}
	}

	//private String currentUrl="";
	protected synchronized boolean newTab(String url, boolean show) {

		// Limit number of tabs for limited version of app
		if ( mWebViews.size() >= ServerConfig.BrowserTabMax) {
			Utils.showToast(this, this.getString(R.string.max_tabs));
			return false;
		}
		mIsNewIntent = false;
		LightningView startingTab = new LightningView(mActivity, url, mDarkTheme);
		startingTab.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					onWebViewTouchUp(event);
				}
				return BrowserActivity.this.detector.onTouchEvent(event);
			}
		});

		startingTab.getWebView().getViewTreeObserver().addOnScrollChangedListener(this);

		startingTab.setWebViewAction(new LightningView.WebViewAction() {
			@Override
			public void OnLinkClicked(LightningView v, String url) {
				showTab(v);
			}

			@Override
			public void OnBackClicked(LightningView v, String url) {
				showTab(v);
			}

		});
		if (mIdGenerator == 0) {
			startingTab.resumeTimers();
		}
		mIdGenerator++;
		mWebViews.add(startingTab);



		mTitleAdapter.notifyDataSetChanged();
		if (show) {
			mDrawerListLeft.setItemChecked(mWebViews.size() - 1, true);
			showTab(startingTab);
		}
		windowsText.setText(String.valueOf(mWebViews.size()));
		if(url!=null) {
			if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0) {
				mSearch.setText(url);
			}
		}

		LightningView curview=getCurrentWebView();
		if(curview!=null) {
			//MobclickAgent.onPageEnd(this.getClass().getName() + ":" +curview.getUrl());
		}

		//MobclickAgent.onPageStart(this.getClass().getName() + ":" +url);

		//browserTabsFragment.addTabBitmap(getCurrentWebView().getScreenShot());
		return true;
	}

	int getSelectedTabIndex(){


		LightningView lv=getCurrentWebView();
		for(int i=0;i<mWebViews.size();i++){
			LightningView v=mWebViews.get(i);
			if(v==lv){
				return i;
			}
		}
		return 0;

	}
	int getTabIndex(LightningView lv){
		for(int i=0;i<mWebViews.size();i++){
			LightningView v=mWebViews.get(i);
			if(v==lv){
				return i;
			}
		}
		return 0;
	}

	public LightningView getCurrentWebView(){
		if(mBrowserFrame==null||mBrowserFrame.getChildCount()!=1){
			return null;
		}
		LightningView lv=getLightningView(mBrowserFrame.getChildAt(0));
		return lv;
	}


	void onWebViewTouchUp(MotionEvent event){
		hideSettingPanel();
		//ToastUtil.showMessage("touch up:"+event.getX()+":"+event.getY());
	}

	//d
	private synchronized void deleteTab(int position) {
		if (position >= mWebViews.size()) {
			return;
		}
		if(mWebViews.size()<=1){
			//LightningView lv=getCurrentWebView();
			//openBookmarkPage(lv.getWebView());
			ToastUtil.showMessage(getString(R.string.last_tab_not_close));
			return;
		}

		int current = getSelectedTabIndex();
		LightningView reference = mWebViews.get(position);
		if (reference == null) {
			return;
		}
		if (reference.getUrl() != null && !reference.getUrl().startsWith(Constants.FILE)
				&& !isIncognito()) {
			mPreferences.setSavedUrl(reference.getUrl());
		}
		boolean isShown = reference.isShown();
		if (isShown) {
			mBrowserFrame.setBackgroundColor(mBackgroundColor);
		}
		if (current > position) {
			mWebViews.remove(position);
			mDrawerListLeft.setItemChecked(current - 1, true);
			reference.onDestroy();
		} else if (mWebViews.size() > position + 1) {
			if (current == position) {
				showTab(mWebViews.get(position + 1));
				mWebViews.remove(position);
				mDrawerListLeft.setItemChecked(position, true);
			} else {
				mWebViews.remove(position);
			}

			reference.onDestroy();
		} else if (mWebViews.size() > 1) {
			if (current == position) {
				showTab(mWebViews.get(position - 1));
				mWebViews.remove(position);
				mDrawerListLeft.setItemChecked(position - 1, true);
			} else {
				mWebViews.remove(position);
			}

			reference.onDestroy();
		} else {
			if (getCurrentWebView().getUrl() == null || getCurrentWebView().getUrl().startsWith(Constants.FILE)
					|| getCurrentWebView().getUrl().equals(mHomepage)) {
				closeActivity();
			} else {
				mWebViews.remove(position);
				if (mPreferences.getClearCacheExit() && getCurrentWebView() != null && !isIncognito()) {
					getCurrentWebView().clearCache(true);
					Log.d(Constants.TAG, "Cache Cleared");

				}
				if (mPreferences.getClearHistoryExitEnabled() && !isIncognito()) {
					clearHistory();
					Log.d(Constants.TAG, "History Cleared");

				}
				if (mPreferences.getClearCookiesExitEnabled() && !isIncognito()) {
					clearCookies();
					Log.d(Constants.TAG, "Cookies Cleared");

				}
				reference.pauseTimers();
				reference.onDestroy();

				mTitleAdapter.notifyDataSetChanged();
				finish();

			}
		}
		mTitleAdapter.notifyDataSetChanged();

		if (mIsNewIntent && isShown) {
			mIsNewIntent = false;
			closeActivity();
		}

		Log.d(Constants.TAG, "deleted tab");
		windowsText.setText(String.valueOf(mWebViews.size()));
	}


	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showCloseDialog(getSelectedTabIndex());
		}
		return true;
	}

	private void closeBrowser() {
		mBrowserFrame.setBackgroundColor(mBackgroundColor);
		if (mPreferences.getClearCacheExit() && getCurrentWebView() != null && !isIncognito()) {
			getCurrentWebView().clearCache(true);
			Log.d(Constants.TAG, "Cache Cleared");

		}
		if (mPreferences.getClearHistoryExitEnabled() && !isIncognito()) {
			clearHistory();
			Log.d(Constants.TAG, "History Cleared");

		}
		if (mPreferences.getClearCookiesExitEnabled() && !isIncognito()) {
			clearCookies();
			Log.d(Constants.TAG, "Cookies Cleared");

		}
		
		for (int n = 0; n < mWebViews.size(); n++) {
			if (mWebViews.get(n) != null) {
				mWebViews.get(n).onDestroy();
			}
		}
		mWebViews.clear();
		mTitleAdapter.notifyDataSetChanged();
		finish();
	}

	@SuppressWarnings("deprecation")
	public void clearHistory() {
		this.deleteDatabase(HistoryDatabase.DATABASE_NAME);
		WebViewDatabase m = WebViewDatabase.getInstance(this);
		m.clearFormData();
		m.clearHttpAuthUsernamePassword();
		if (API < 18) {
			m.clearUsernamePassword();
			WebIconDatabase.getInstance().removeAllIcons();
		}
		if (mSystemBrowser) {
			try {
				//Browser.
				//Browser.clearHistory(getContentResolver());
			} catch (NullPointerException ignored) {
			}
		}
		Utils.trimCache(this);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void clearCookies() {
		// TODO Break out web storage deletion into its own option/action
		// TODO clear web storage for all sites that are visited in Incognito mode
		WebStorage storage = WebStorage.getInstance();
		storage.deleteAllData();
		CookieManager c = CookieManager.getInstance();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			c.removeAllCookies(null);
		} else {
			CookieSyncManager.createInstance(this);
			c.removeAllCookie();
		}
	}

	@Override
	public void onBackPressed() {
		if(showTabs==true){
			hideTabsPanel();
			return;
		}
		if(showSetting==true){
			hideSettingPanel();
			return;
		}

		if (mDrawerLayout.isDrawerOpen(mDrawerLeft)) {
			mDrawerLayout.closeDrawer(mDrawerLeft);
			return;
		} else if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
			mDrawerLayout.closeDrawer(mDrawerRight);
			return;
		}

		if (getCurrentWebView() != null) {
				Log.d(Constants.TAG, "onBackPressed");
				if (mSearch.hasFocus()) {
					getCurrentWebView().requestFocus();
				} else if (getCurrentWebView().canGoBack()) {
					if (!getCurrentWebView().isShown()) {
						onHideCustomView();
					} else {
						getCurrentWebView().goBack();
					}
				} else {
					ToastUtil.showMessage(getString(R.string.browser_no_back));
					//deleteTab(mDrawerListLeft.getCheckedItemPosition());
				}
		} else {
			Log.e(Constants.TAG, "This shouldn't happen ever");
				super.onBackPressed();
			}

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(Constants.TAG, "onPause");

		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);

		if (getCurrentWebView() != null) {
			getCurrentWebView().pauseTimers();
			getCurrentWebView().onPause();
		}
	}

	public void saveOpenTabs() {
		if (mPreferences.getRestoreLostTabsEnabled()) {
			String s = "";

			for (int n = 0; n < mWebViews.size(); n++) {
				if (mWebViews.get(n).getUrl() != null) {
					s = s + mWebViews.get(n).getUrl() + "|$|SEPARATOR|$|";
				}
			}


			//only restore current url

			if(getCurrentWebView()!=null) {
				//LightningView lv=getCurrentWebView();
				int selectedIndex=getSelectedTabIndex();
				//s = s + getCurrentWebView().getUrl() + "|$|SEPARATOR|$|";
				mPreferences.setSelectedIndex(selectedIndex);
			}




			mPreferences.setMemoryUrl(s);
		}

		saveWebViewScreenShots();
	}

	private void saveWebViewScreenShots() {
		for (int n = 0; n < mWebViews.size(); n++) {
			if (mWebViews.get(n).getUrl() != null) {
				LightningView view=mWebViews.get(n);

				view.saveScreenShot(n);

				//s = s + mWebViews.get(n).getUrl() + "|$|SEPARATOR|$|";
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		mI2PHelperBound = false;
	}

	@Override
	protected void onDestroy() {
		Log.d(Constants.TAG, "onDestroy");
		if (mHistoryDatabase != null) {
			mHistoryDatabase.close();
		}
		super.onDestroy();

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mPreferences.getProxyChoice() == Constants.PROXY_I2P) {
			// Try to bind to I2P Android

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
		Log.d(Constants.TAG, "onResume");
		if (mSearchAdapter != null) {
			mSearchAdapter.refreshPreferences();
			mSearchAdapter.refreshBookmarks();
		}
		if (getCurrentWebView() != null) {
			getCurrentWebView().resumeTimers();
			getCurrentWebView().onResume();

			mHistoryDatabase = HistoryDatabase.getInstance(getApplicationContext());
			mBookmarkList = mBookmarkManager.getLastItems(PreferenceUtils.getUserId(),0,1000);
			notifyBookmarkDataSetChanged();
		}
		initializePreferences();
		if (mWebViews != null) {
			for (int n = 0; n < mWebViews.size(); n++) {
				if (mWebViews.get(n) != null) {
					mWebViews.get(n).initializePreferences(this);
				} else {
					mWebViews.remove(n);
				}
			}
		}

		supportInvalidateOptionsMenu();
	}

	/**
	 * searches the web for the query fixing any and all problems with the input
	 * checks if it is a search, url, etc.
	 */
	void searchTheWeb(String query) {
		if (query.equals("")) {
			return;
		}
		String SEARCH = mSearchText;
		query = query.trim();
		getCurrentWebView().stopLoading();

		if (query.startsWith("www.")) {
			query = Constants.HTTP + query;
		} else if (query.startsWith("ftp.")) {
			query = "ftp://" + query;
		}

		boolean containsPeriod = query.contains(".");
		boolean isIPAddress = (TextUtils.isDigitsOnly(query.replace(".", ""))
				&& (query.replace(".", "").length() >= 4) && query.contains("."));
		boolean aboutScheme = query.contains("about:");
		boolean validURL = (query.startsWith("ftp://") || query.startsWith(Constants.HTTP)
				|| query.startsWith(Constants.FILE) || query.startsWith(Constants.HTTPS))
				|| isIPAddress;
		boolean isSearch = ((query.contains(" ") || !containsPeriod) && !aboutScheme);

		if (isIPAddress
				&& (!query.startsWith(Constants.HTTP) || !query.startsWith(Constants.HTTPS))) {
			query = Constants.HTTP + query;
		}

		if (isSearch) {
			try {
				query = URLEncoder.encode(query, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			getCurrentWebView().loadUrl(SEARCH + query);
		} else if (!validURL) {
			getCurrentWebView().loadUrl(Constants.HTTP + query);
		} else {
			getCurrentWebView().loadUrl(query);
		}
	}

	public class LightningViewAdapter extends ArrayAdapter<LightningView> {

		final Context context;
		ColorMatrix colorMatrix;
		ColorMatrixColorFilter filter;
		Paint paint;
		final int layoutResourceId;
		List<LightningView> data = null;
		final CloseTabListener mExitListener;

		public LightningViewAdapter(Context context, int layoutResourceId, List<LightningView> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			this.mExitListener = new CloseTabListener();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View row = convertView;
			LightningViewHolder holder;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new LightningViewHolder();
				holder.txtTitle = (TextView) row.findViewById(R.id.textTab);
				holder.favicon = (ImageView) row.findViewById(R.id.faviconTab);
				holder.exit = (ImageView) row.findViewById(R.id.deleteButton);
				holder.exit.setTag(position);
				row.setTag(holder);
			} else {
				holder = (LightningViewHolder) row.getTag();
			}

			holder.exit.setTag(position);
			holder.exit.setOnClickListener(mExitListener);

			ViewCompat.jumpDrawablesToCurrentState(holder.exit);

			LightningView web = data.get(position);
			holder.txtTitle.setText(web.getTitle());
			if (web.isForegroundTab()) {
				holder.txtTitle.setTextAppearance(context, R.style.boldText);
			} else {
				holder.txtTitle.setTextAppearance(context, R.style.normalText);
			}

			Bitmap favicon = web.getFavicon();
			if (web.isForegroundTab()) {

				holder.favicon.setImageBitmap(favicon);
				if (!isIncognito() && mColorMode)
					changeToolbarBackground(favicon);
			} else {
				Bitmap grayscaleBitmap = Bitmap.createBitmap(favicon.getWidth(),
						favicon.getHeight(), Bitmap.Config.ARGB_8888);

				Canvas c = new Canvas(grayscaleBitmap);
				if (colorMatrix == null || filter == null || paint == null) {
					paint = new Paint();
					colorMatrix = new ColorMatrix();
					colorMatrix.setSaturation(0);
					filter = new ColorMatrixColorFilter(colorMatrix);
					paint.setColorFilter(filter);
				}

				c.drawBitmap(favicon, 0, 0, paint);
				holder.favicon.setImageBitmap(grayscaleBitmap);
			}
			return row;
		}

		class LightningViewHolder {
			TextView txtTitle;
			ImageView favicon;
			ImageView exit;
		}
	}

	private class CloseTabListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			deleteTab((int) v.getTag());
		}

	}

	private void changeToolbarBackground(Bitmap favicon) {

	}

	public static boolean isColorTooDark(int color) {
		final byte RED_CHANNEL = 16;
		final byte GREEN_CHANNEL = 8;
		//final byte BLUE_CHANNEL = 0;

		int r = ((int) ((float) (color >> RED_CHANNEL & 0xff) * 0.3f)) & 0xff;
		int g = ((int) ((float) (color >> GREEN_CHANNEL & 0xff) * 0.59)) & 0xff;
		int b = ((int) ((float) (color & 0xff) * 0.11)) & 0xff;
		int gr = (r + g + b) & 0xff;
		int gray = gr + (gr << GREEN_CHANNEL) + (gr << RED_CHANNEL);

		return gray < 0x727272;
	}

	public static int mixTwoColors(int color1, int color2, float amount) {
		final byte ALPHA_CHANNEL = 24;
		final byte RED_CHANNEL = 16;
		final byte GREEN_CHANNEL = 8;
		//final byte BLUE_CHANNEL = 0;

		final float inverseAmount = 1.0f - amount;

		int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) + ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
		int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) + ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
		int b = ((int) (((float) (color1 & 0xff) * amount) + ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

		return 0xff << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b;
	}

	public class BookmarkViewAdapter extends ArrayAdapter<HistoryItem> {

		final Context context;
		List<HistoryItem> data = null;
		final int layoutResourceId;

		public BookmarkViewAdapter(Context context, int layoutResourceId, List<HistoryItem> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			BookmarkViewHolder holder;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new BookmarkViewHolder();
				holder.txtTitle = (TextView) row.findViewById(R.id.textBookmark);
				holder.favicon = (ImageView) row.findViewById(R.id.faviconBookmark);
				row.setTag(holder);
			} else {
				holder = (BookmarkViewHolder) row.getTag();
			}

			HistoryItem web = data.get(position);
			holder.txtTitle.setText(web.getTitle());
			holder.favicon.setImageBitmap(mWebpageBitmap);
			if (web.getBitmap() == null) {
				getImage(holder.favicon, web);
			} else {
				holder.favicon.setImageBitmap(web.getBitmap());
			}
			return row;
		}

		class BookmarkViewHolder {
			TextView txtTitle;
			ImageView favicon;
		}
	}

	private void getImage(ImageView image, HistoryItem web) {
		new DownloadImageTask(image, web).execute(web.getUrl());
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		final ImageView bmImage;
		final HistoryItem mWeb;

		public DownloadImageTask(ImageView bmImage, HistoryItem web) {
			this.bmImage = bmImage;
			this.mWeb = web;
		}

		protected Bitmap doInBackground(String... urls) {
			String url = urls[0];
			Bitmap mIcon = null;
			// unique path for each url that is bookmarked.
			String hash = String.valueOf(Utils.getDomainName(url).hashCode());
			File image = new File(mActivity.getCacheDir(), hash + ".png");
			String urldisplay;
			try {
				urldisplay = Utils.getProtocol(url) + getDomainName(url) + "/favicon.ico";
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return mWebpageBitmap;
			}
			// checks to see if the image exists
			if (!image.exists()) {
				try {
					// if not, download it...
					URL urlDownload = new URL(urldisplay);
					HttpURLConnection connection = (HttpURLConnection) urlDownload.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream in = connection.getInputStream();

					if (in != null) {
						mIcon = BitmapFactory.decodeStream(in);
					}
					// ...and cache it
					if (mIcon != null) {
						FileOutputStream fos = new FileOutputStream(image);
						mIcon.compress(Bitmap.CompressFormat.PNG, 100, fos);
						fos.flush();
						fos.close();
						Log.d(Constants.TAG, "Downloaded: " + urldisplay);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// if it exists, retrieve it from the cache
				mIcon = BitmapFactory.decodeFile(image.getPath());
			}
			if (mIcon == null) {

			}
			if (mIcon == null) {
				return mWebpageBitmap;
			} else {
				return mIcon;
			}
		}

		protected void onPostExecute(Bitmap result) {
			Bitmap fav = Utils.padFavicon(result);
			bmImage.setImageBitmap(fav);
			mWeb.setBitmap(fav);
			notifyBookmarkDataSetChanged();
		}
	}

	static String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		if (domain == null) {
			return url;
		}
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	@Override
	public void updateUrl(String url, boolean shortUrl) {
		if(ServerConfig.isSearchUrl(url)){
			mSearch.setText(ServerConfig.getSearchWord(url));
			return;
		}

		if (url == null || mSearch == null || mSearch.hasFocus()) {
			return;
		}

		if(url!=null&&!url.equals("")) {
			mSearch.setText(url);
		}

		if (shortUrl && !url.startsWith(Constants.FILE)) {
			switch (mPreferences.getUrlBoxContentChoice()) {
				case 0: // Default, show only the domain
					//url = url.replaceFirst(Constants.HTTP, "");
					if (getCurrentWebView() != null && !getCurrentWebView().getTitle().isEmpty()) {
						mSearch.setText(getCurrentWebView().getTitle());
					}else {
						url = Utils.getDomainName2(url);
						mSearch.setText(url);
					}
					break;
				case 1: // URL, show the entire URL
					mSearch.setText(url);
					break;
				case 2: // Title, show the page's title
					if (getCurrentWebView() != null && !getCurrentWebView().getTitle().isEmpty()) {
						mSearch.setText(getCurrentWebView().getTitle());
					} else {
						mSearch.setText(mUntitledTitle);
					}
					break;
			}



		}

		if (url.startsWith(Constants.FILE)) {
			mSearch.setText("");
			return;
		}


		if(ServerConfig.isSearchUrl(url)){
			mSearch.setText(ServerConfig.getSearchWord(url));
		}else{


		}
	}

	@Override
	public void updateProgress(int n) {
		if (n >= 100) {
			setIsFinishedLoading();

		} else {
			if(getCurrentWebView()!=null) {
				//mSearch.setText(getCurrentWebView().getUrl());
				setIsLoading();
			}

		}
		mProgressBar.setProgress(n);

		LightningView lv=getCurrentWebView();
		if(!lv.canGoBack()){
			((ImageView)findViewById(R.id.btn_back)).setImageResource(R.drawable.ic_action_back);
		}else{
			((ImageView)findViewById(R.id.btn_back)).setImageResource(R.drawable.ic_action_back_dark);
		}
		if(!lv.canGoForward()) {
			((ImageView) findViewById(R.id.btn_forward)).setImageResource(R.drawable.ic_action_forward);
		}else{
			((ImageView) findViewById(R.id.btn_forward)).setImageResource(R.drawable.ic_action_forward_dark);
		}

	}

	@Override
	public void updateHistory(final String title, final String url) {

	}

	public void addItemToHistory(final String title, final String url) {
		Runnable update = new Runnable() {
			@Override
			public void run() {
				if (isSystemBrowserAvailable() && mPreferences.getSyncHistoryEnabled()) {
					try {

						//Browser.updateVisitedHistory(getContentResolver(), url, true);
					} catch (NullPointerException ignored) {
					}
				}
				try {
					if (mHistoryDatabase == null) {
						mHistoryDatabase = HistoryDatabase.getInstance(mActivity);
					}
					mHistoryDatabase.visitHistoryItem(PreferenceUtils.getUserId(),url, title);
				} catch (IllegalStateException e) {
					Log.e(Constants.TAG, "IllegalStateException in updateHistory");
				} catch (NullPointerException e) {
					Log.e(Constants.TAG, "NullPointerException in updateHistory");
				} catch (SQLiteException e) {
					Log.e(Constants.TAG, "SQLiteException in updateHistory");
				}
			}
		};
		if (url != null && !url.startsWith(Constants.FILE)) {
			new Thread(update).start();
		}
	}

	public boolean isSystemBrowserAvailable() {
		return mSystemBrowser;
	}

	public boolean getSystemBrowser() {
		Cursor c = null;
		String[] columns = new String[] { "url", "title" };
		boolean browserFlag;
		try {

			//Uri bookmarks = Browser.BOOKMARKS_URI;
			//c = getContentResolver().query(bookmarks, columns, null, null, null);
		} catch (SQLiteException | IllegalStateException | NullPointerException e) {
			e.printStackTrace();
		}

		if (c != null) {
			Log.d("Browser", "System Browser Available");
			browserFlag = true;
		} else {
			Log.e("Browser", "System Browser Unavailable");
			browserFlag = false;
		}
		if (c != null) {
			c.close();
		}
		mPreferences.setSystemBrowserPresent(browserFlag);
		return browserFlag;
	}

	/**
	 * method to generate search suggestions for the AutoCompleteTextView from
	 * previously searched URLs
	 */
	private void initializeSearchSuggestions(final AutoCompleteTextView getUrl) {

		getUrl.setThreshold(1);
		getUrl.setDropDownWidth(-1);
		getUrl.setDropDownAnchor(R.id.toolbar_layout);
		getUrl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			}

		});

		getUrl.setSelectAllOnFocus(true);
		mSearchAdapter = new SearchAdapter(mActivity, mDarkTheme, isIncognito());
		getUrl.setAdapter(mSearchAdapter);
	}

	@Override
	public boolean isIncognito() {
		return false;
	}

	/**
	 * function that opens the HTML history page in the browser
	 */
	private void openHistory() {
		// use a thread so that history retrieval doesn't block the UI
		Thread history = new Thread(new Runnable() {

			@Override
			public void run() {

				String url=HistoryPage.getHistoryPage(PreferenceUtils.getUserId(),mActivity);
				newTab(url,true);
				//getCurrentWebView().loadUrl();
				mSearch.setText("");
			}

		});
		history.run();
	}

	private static final int OPEN_BOOKMARKS_HISTORY_ACTIVITY = 0;
	private static final int OPEN_DOWNLOADS_ACTIVITY = 1;
	private static final int OPEN_FILE_CHOOSER_ACTIVITY = 2;
	public static final int Open_Browser_Input = 3;

	private static final int Open_Url = 4;
	private void openHotSiteActivity() {

		Intent intent=new Intent(this, SiteHotActivity.class);
		startActivityForResult(intent, Open_Url);


	}

	private void openHistoryActivity() {

		//openHistory();




	}

	private void openSiteHotActivity() {

		//openHistory();
		Intent intent=new Intent(this, SiteHotActivity.class);
		startActivityForResult(intent, Open_Url);

	}




	/**
	 * helper function that opens the bookmark drawer
	 */
	private void openBookmarks() {

		if (mDrawerLayout.isDrawerOpen(mDrawerLeft)) {
			mDrawerLayout.closeDrawers();
		}
		mDrawerLayout.openDrawer(mDrawerRight);

	}

	public void closeDrawers() {
		mDrawerLayout.closeDrawers();
	}

	@Override
	/**
	 * open the HTML bookmarks page, parameter view is the WebView that should show the page
	 */
	public void openBookmarkPage(WebView view) {
		StringBuilder bookmarkBuilder = new StringBuilder();
		bookmarkBuilder.append(BookmarkPage.HEADING);
		Iterator<HistoryItem> iter = mBookmarkList.iterator();
		HistoryItem helper;
		while (iter.hasNext()) {
			helper = iter.next();
			bookmarkBuilder.append(BookmarkPage.PART1);
			bookmarkBuilder.append(helper.getUrl());
			bookmarkBuilder.append(BookmarkPage.PART2);
			//bookmarkBuilder.append(URLUtil.getHost(helper.getUrl()) + "/favicon.ico");
			bookmarkBuilder.append(helper.getUrl());
			bookmarkBuilder.append(BookmarkPage.PART3);
			bookmarkBuilder.append(helper.getTitle());
			bookmarkBuilder.append(BookmarkPage.PART4);
		}
		bookmarkBuilder.append(BookmarkPage.END);
		File bookmarkWebPage = new File(mActivity.getFilesDir(), BookmarkPage.FILENAME);
		try {
			FileWriter bookWriter = new FileWriter(bookmarkWebPage, false);
			bookWriter.write(bookmarkBuilder.toString());
			bookWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		view.loadUrl(Constants.FILE + bookmarkWebPage);
	}

	@Override
	public void update() {
		//mSearch.setText(getCurrentWebView().getName());
		mTitleAdapter.notifyDataSetChanged();
	}

	@Override
	/**
	 * opens a file chooser
	 * param ValueCallback is the message from the WebView indicating a file chooser
	 * should be opened
	 */
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		mUploadMessage = uploadMsg;
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");
		startActivityForResult(Intent.createChooser(i, getString(R.string.title_file_chooser)), 1);
	}

	@Override
	/**
	 * used to allow uploading into the browser
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (API < Build.VERSION_CODES.LOLLIPOP) {
			if (requestCode == 1) {
				if (null == mUploadMessage) {
					return;
				}
				Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
				mUploadMessage.onReceiveValue(result);
				mUploadMessage = null;

			}
		}


		 if (requestCode == Open_Url) {
			if (intent != null) {
				Bundle b = intent.getExtras();

				if (b != null) {
					String url=b.getString(Constants.EXTRA_ID_URL);
					getCurrentWebView().loadUrl(url);
					//newTab(url, true);
				}
			}
			return;

		}
		if (requestCode != 1 || mFilePathCallback == null) {
			super.onActivityResult(requestCode, resultCode, intent);
			return;
		}

		Uri[] results = null;

		// Check that the response is a good one
		if (resultCode == Activity.RESULT_OK) {
			if (intent == null) {
				// If there is not data, then we may have taken a photo
				if (mCameraPhotoPath != null) {
					results = new Uri[] { Uri.parse(mCameraPhotoPath) };
				}
			} else {
				String dataString = intent.getDataString();
				if (dataString != null) {
					results = new Uri[] { Uri.parse(dataString) };
				}
			}
		}

		mFilePathCallback.onReceiveValue(results);
		mFilePathCallback = null;
	}

	@Override
	public void showFileChooser(ValueCallback<Uri[]> filePathCallback) {
		if (mFilePathCallback != null) {
			mFilePathCallback.onReceiveValue(null);
		}
		mFilePathCallback = filePathCallback;

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = Utils.createImageFile();
				takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
			} catch (IOException ex) {
				// Error occurred while creating the File
				Log.e(Constants.TAG, "Unable to create Image File", ex);
			}

			// Continue only if the File was successfully created
			if (photoFile != null) {
				mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			} else {
				takePictureIntent = null;
			}
		}

		Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
		contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
		contentSelectionIntent.setType("image/*");

		Intent[] intentArray;
		if (takePictureIntent != null) {
			intentArray = new Intent[] { takePictureIntent };
		} else {
			intentArray = new Intent[0];
		}

		Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
		chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
		chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

		mActivity.startActivityForResult(chooserIntent, 1);
	}

	@Override
	/**
	 * handles long presses for the browser, tries to get the
	 * url of the item that was clicked and sends it (it can be null)
	 * to the click handler that does cool stuff with it
	 */
	public void onLongPress() {
		if (mClickHandler == null) {
			mClickHandler = new ClickHandler(mActivity);
		}
		Message click = mClickHandler.obtainMessage();
		if (click != null) {
			click.setTarget(mClickHandler);
			getCurrentWebView().getWebView().requestFocusNodeHref(click);
		}
	}

	@Override
	public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
		if (view == null) {
			return;
		}
		if (mCustomView != null && callback != null) {
			callback.onCustomViewHidden();
			return;
		}
		try {
			view.setKeepScreenOn(true);
		} catch (SecurityException e) {
			Log.e(Constants.TAG, "WebView is not allowed to keep the screen on");
		}
		mOriginalOrientation = getRequestedOrientation();
		FrameLayout decor = (FrameLayout) getWindow().getDecorView();
		mFullscreenContainer = new FullscreenHolder(this);
		mCustomView = view;
		mFullscreenContainer.addView(mCustomView, COVER_SCREEN_PARAMS);
		decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
		setFullscreen(true);
		getCurrentWebView().setVisibility(View.GONE);
		if (view instanceof FrameLayout) {
			if (((FrameLayout) view).getFocusedChild() instanceof VideoView) {
				mVideoView = (VideoView) ((FrameLayout) view).getFocusedChild();
				mVideoView.setOnErrorListener(new VideoCompletionListener());
				mVideoView.setOnCompletionListener(new VideoCompletionListener());
			}
		}
		mCustomViewCallback = callback;
	}

	@Override
	public void onHideCustomView() {
		if (mCustomView == null || mCustomViewCallback == null || getCurrentWebView() == null) {
			return;
		}
		Log.d(Constants.TAG, "onHideCustomView");
		getCurrentWebView().setVisibility(View.VISIBLE);
		try {
			mCustomView.setKeepScreenOn(false);
		} catch (SecurityException e) {
			Log.e(Constants.TAG, "WebView is not allowed to keep the screen on");
		}
		setFullscreen(mPreferences.getHideStatusBarEnabled());
		FrameLayout decor = (FrameLayout) getWindow().getDecorView();
		if (decor != null) {
			decor.removeView(mFullscreenContainer);
		}

		if (API < 19) {
			try {
				mCustomViewCallback.onCustomViewHidden();
			} catch (Throwable ignored) {

			}
		}
		mFullscreenContainer = null;
		mCustomView = null;
		if (mVideoView != null) {
			mVideoView.setOnErrorListener(null);
			mVideoView.setOnCompletionListener(null);
			mVideoView = null;
		}
		setRequestedOrientation(mOriginalOrientation);
	}

	private class VideoCompletionListener implements MediaPlayer.OnCompletionListener,
			MediaPlayer.OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			return false;
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			onHideCustomView();
		}

	}

	/**
	 * turns on fullscreen mode in the app
	 *
	 * @param enabled
	 *            whether to enable fullscreen or not
	 */
	public void setFullscreen(boolean enabled) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (enabled) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
			if (mCustomView != null) {
				mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			} else {
				mBrowserFrame.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			}
		}
		win.setAttributes(winParams);

		if(mToolbarLayout!=null){
			mToolbarLayout.setVisibility(View.GONE);
		}

		if(shortcutToolbar!=null){
			shortcutToolbar.setVisibility(View.GONE);
		}
		if(fullscreenCancelBtn!=null){
			fullscreenCancelBtn.setVisibility(View.VISIBLE);
		}

	}


	void fullscreenCancel(){
		if(mToolbarLayout!=null){
			mToolbarLayout.setVisibility(View.VISIBLE);
		}

		if(shortcutToolbar!=null){
			shortcutToolbar.setVisibility(View.VISIBLE);
		}
		if(fullscreenCancelBtn!=null){
			fullscreenCancelBtn.setVisibility(View.GONE);
		}
	}
	/**
	 * a class extending FramLayout used to display fullscreen videos
	 */
	static class FullscreenHolder extends FrameLayout {

		public FullscreenHolder(Context ctx) {
			super(ctx);
			setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
		}

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouchEvent(MotionEvent evt) {
			return true;
		}

	}

	@Override
	/**
	 * a stupid method that returns the bitmap image to display in place of
	 * a loading video
	 */
	public Bitmap getDefaultVideoPoster() {
		if (mDefaultVideoPoster == null) {
			mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(),
					android.R.drawable.ic_media_play);
		}
		return mDefaultVideoPoster;
	}

	@SuppressLint("InflateParams")
	@Override
	/**
	 * dumb method that returns the loading progress for a video
	 */
	public View getVideoLoadingProgressView() {
		if (mVideoProgressView == null) {
			LayoutInflater inflater = LayoutInflater.from(this);
			mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
		}
		return mVideoProgressView;
	}

	@Override
	/**
	 * handles javascript requests to create a new window in the browser
	 */
	public void onCreateWindow(boolean isUserGesture, Message resultMsg) {
		if (resultMsg == null) {
			return;
		}
		if (newTab("", true)) {
			WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
			transport.setWebView(getCurrentWebView().getWebView());
			resultMsg.sendToTarget();
		}
	}

	@Override
	/**
	 * returns the Activity instance for this activity,
	 * very helpful when creating things in other classes... I think
	 */
	public Activity getActivity() {
		return mActivity;
	}

	/**
	 * it hides the action bar, seriously what else were you expecting
	 */
	@Override
	public void hideActionBar() {
		if (mFullScreen) {
			if (mBrowserFrame.findViewById(R.id.toolbar_layout) == null) {
				mUiLayout.removeView(mToolbarLayout);
				//mBrowserFrame.addView(mToolbarLayout);
				mToolbarLayout.bringToFront();
				Log.d(Constants.TAG, "Move view to browser frame");
			}
			if (mToolbarLayout.getVisibility() != View.GONE) {

				Animation hide = AnimationUtils.loadAnimation(mActivity, R.anim.slide_up);
				hide.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						mToolbarLayout.setVisibility(View.GONE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

				});
				mToolbarLayout.startAnimation(hide);
				Log.d(Constants.TAG, "Hide");
			}
		}
	}

	@Override
	public void toggleActionBar() {
		if (mFullScreen) {
			if (mToolbarLayout.getVisibility() != View.VISIBLE) {
				showActionBar();
			} else {
				hideActionBar();
			}
		}
	}

	@Override
	/**
	 * obviously it shows the action bar if it's hidden
	 */
	public void showActionBar() {
		if (mFullScreen) {
			if (mBrowserFrame.findViewById(R.id.toolbar_layout) == null) {
				mUiLayout.removeView(mToolbarLayout);
				//mBrowserFrame.addView(mToolbarLayout);
				mToolbarLayout.bringToFront();
				Log.d(Constants.TAG, "Move view to browser frame");
			}
			if (mToolbarLayout.getVisibility() != View.VISIBLE) {
				Animation show = AnimationUtils.loadAnimation(mActivity, R.anim.slide_down);
				show.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						mToolbarLayout.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

				});
				mToolbarLayout.startAnimation(show);
				Log.d(Constants.TAG, "Show");
			}

		}

	}

	@Override
	/**
	 * handles a long click on the page, parameter String url
	 * is the url that should have been obtained from the WebView touch node
	 * thingy, if it is null, this method tries to deal with it and find a workaround
	 */
	public void longClickPage(final String url) {
		HitTestResult result = null;

		LightningView lv=getCurrentWebView();

		if(lv==null){
			return;
		}
		if(lv.getUrl().indexOf("http")<0){
			longClickPageFiles(url);
			return;
		}
		if (lv.getWebView() != null) {
			result = getCurrentWebView().getWebView().getHitTestResult();
		}


		if (url != null) {
			if (result != null) {
				if (result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE
						|| result.getType() == HitTestResult.IMAGE_TYPE) {

					final String imageUrl = result.getExtra();
					onImageLongClick(url,imageUrl);

				} else {

					final String extraLink = result.getExtra();
					//result.
					//ToastUtil.showMessage("ex:"+extraLink+" url:"+url);
					onLinkLongClick(url);
				}
			} else {
				onLinkLongClick(url);
			}

		} else if (result != null) {
			if (result.getExtra() != null) {
				final String newUrl = result.getExtra();
				if (result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE
						|| result.getType() == HitTestResult.IMAGE_TYPE) {
					onImageLongClick(newUrl,newUrl);
					//onImageLongClickExtraUrl(newUrl);
				} else {
					onLinkLongClick(newUrl);
				}

			}

		}

	}

	//
	private void longClickPageFiles(String url) {

		File bookmarkWebPage = new File(mActivity.getFilesDir(), BookmarkPage.FILENAME);
		String bookmarkUrl=Constants.FILE + bookmarkWebPage.toString();
		LightningView lv=getCurrentWebView();
		if(lv==null){
			return;
		}
		String webUrl=lv.getUrl();
		if(webUrl.equalsIgnoreCase(bookmarkUrl)){
			openBookmarkUrlLongClick(url);
			return;
		}

		File historyWebPage = new File(this.getFilesDir(), "history.html");
		String historyUrl= Constants.FILE + historyWebPage;
		if(webUrl.equalsIgnoreCase(historyUrl)){
			openHistoryUrlLongClick(url);
			return;
		}
	}

	void openBookmarkUrlLongClick(final String url){
		LightningView lv = getCurrentWebView();
		HitTestResult result = lv.getWebView().getHitTestResult();
		if (url != null) {
			onBookmarkLongClick(url);
		} else if (result != null) {
			if (result.getExtra() != null) {
				final String newUrl = result.getExtra();
				onBookmarkLongClick(newUrl);
			}

		}
	}

	void openHistoryUrlLongClick(final String url){
		LightningView lv = getCurrentWebView();
		HitTestResult result = lv.getWebView().getHitTestResult();
		if (url != null) {
			onHistoryLongClick(url);
		} else if (result != null) {
			if (result.getExtra() != null) {
				final String newUrl = result.getExtra();
				onHistoryLongClick(newUrl);
			}

		}
	}

	private void onImageLongClickExtraUrl(final String newUrl) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        newTab(newUrl, true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        getCurrentWebView().loadUrl(newUrl);
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        if (API > 8) {
                            Utils.downloadFile(mActivity, newUrl,
									getCurrentWebView().getUserAgent(), "attachment", false);
                        }
                        break;
                }
            }
        };

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
		builder.setTitle(newUrl.replace(Constants.HTTP, ""))
                .setMessage(getResources().getString(R.string.dialog_image))
                .setPositiveButton(getResources().getString(R.string.action_new_tab),
						dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.action_open),
						dialogClickListener)
                .setNeutralButton(getResources().getString(R.string.action_download),
						dialogClickListener).show();
	}

	private void onLinkLongClick2(final String url) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        newTab(url, false);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        getCurrentWebView().loadUrl(url);
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", url);
                        clipboard.setPrimaryClip(clip);
                        break;
                }
            }
        };

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
		builder.setTitle(url)
                .setMessage(getResources().getString(R.string.dialog_link))
                .setPositiveButton(getResources().getString(R.string.action_new_tab),
						dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.action_open),
						dialogClickListener)
                .setNeutralButton(getResources().getString(R.string.action_copy),
						dialogClickListener).show();
	}


	private void onLinkLongClick(final String url) {
		this.currentClickUrl =url;
		this.currentClickImageUrl="";
		ECListDialog dialog = getLinkMenuDialog();
		dialog.show();
	}


	private void onBookmarkLongClick(final String url) {
		ECListDialog dialog = getBookmarkMenuDialog(url);
		dialog.show();
	}

	private void onHistoryLongClick(final String url) {
		ECListDialog dialog = getHistoryMenuDialog(url);
		dialog.show();
	}

	ECListDialog bookMarkMenuDlg=null;
	private ECListDialog getBookmarkMenuDialog(final String url) {
		bookmarkUrl=url;
		if(bookMarkMenuDlg!=null){
			return bookMarkMenuDlg;
		}

		bookMarkMenuDlg = new ECListDialog(this,
				R.array.webview_bookmark_link);
		bookMarkMenuDlg.setTitle(getString(R.string.bookmark));


		bookMarkMenuDlg.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onBookmarkMenuClicked(d, position);
			}
		});
		return bookMarkMenuDlg;

	}


	private String currentClickUrl ="";
	private String currentClickImageUrl ="";
	private void onImageLongClick(final String url,final String imageUrl) {
		this.currentClickUrl =url;
		this.currentClickImageUrl=imageUrl;
		ECListDialog dialog = getImageMenuDialog();
		dialog.show();
	}


	ECListDialog linkMenuDlg=null;
	private ECListDialog getLinkMenuDialog() {
		if(linkMenuDlg!=null){
			return linkMenuDlg;
		}

		linkMenuDlg = new ECListDialog(this,
				R.array.webview_action_link);
		linkMenuDlg.setTitle(getString(R.string.link));
		linkMenuDlg.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onLinkMenuClicked(d, position);
			}
		});
		return linkMenuDlg;

	}


	void deleteBookmarkUrl(final String url) {
		//ToastUtil.showMessage(bookmarkUrl);
		if (url == null) {
			return;
		}
		mBookmarkManager.deleteBookmarkItem(PreferenceUtils.getUserId(), url);
		removeBookmarkUrl(url);

		mSearchAdapter.refreshBookmarks();
		openBookmarkPage(getCurrentWebView().getWebView());

	}


	ECListDialog historyMenuDlg=null;
	String historyUrl=null;
	private ECListDialog getHistoryMenuDialog(final String url) {
		historyUrl=url;

		if(historyMenuDlg!=null){
			return historyMenuDlg;
		}

		historyMenuDlg = new ECListDialog(this,
				R.array.webview_history_link);
		historyMenuDlg.setTitle(getString(R.string.history));

		historyMenuDlg.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onHistoryMenuClicked(d, position);
			}
		});
		return historyMenuDlg;

	}

	private void onHistoryMenuClicked(Dialog d, int position) {
		switch (position) {
			case 0:
				deleteHistory(historyUrl);
				break;
		}
	}



	void deleteHistory(final String url) {
		//ToastUtil.showMessage(url);
		if(url==null){
			return;
		}
		HistoryDatabase.getInstance(this).deleteHistoryItem(PreferenceUtils.getUserId(), url);
		//getCurrentWebView().loadUrl("javascript:removeUrl('"+url+"')");

		Thread history = new Thread(new Runnable() {

			@Override
			public void run() {
				String url=HistoryPage.getHistoryPage(PreferenceUtils.getUserId(),mActivity);
				getCurrentWebView().loadUrl(url);
			}

		});
		history.run();

	}

	void removeBookmarkUrl(String url){
		for(int i=0;i<mBookmarkList.size();i++){
			HistoryItem history=mBookmarkList.get(i);
			if(history.getUrl().equalsIgnoreCase(url)){
				mBookmarkList.remove(i);
				break;
			}
		}
	}

	String bookmarkUrl=null;
	private void onBookmarkMenuClicked(Dialog d, int position) {
		switch (position) {
			case 0:
				deleteBookmarkUrl(bookmarkUrl);
				//bookmarkUrl
				//newTab(currentClickUrl, true);
				break;

		}
	}

	private void onLinkMenuClicked(Dialog d, int position) {
		switch (position) {
			case 0:
				newTab(currentClickUrl, true);
				break;
			case 1:
				if(getCurrentWebView()!=null) {
					getCurrentWebView().loadUrl(currentClickUrl);
				}
				break;
			case 2:
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("label", currentClickUrl);
				clipboard.setPrimaryClip(clip);
				Utils.showToast(mActivity,
						mActivity.getResources().getString(R.string.message_link_copied));
				break;
		}
	}

	ECListDialog visitorImageDlg=null;
	private ECListDialog getVisitorImageDialog() {
		if (visitorImageDlg != null) {
			return visitorImageDlg;
		}


		visitorImageDlg = new ECListDialog(this,
				R.array.webview_action_image_visitor);
		visitorImageDlg.setTitle(getString(R.string.share));
		visitorImageDlg.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onImageVisitorMenuClicked(d, position);
			}
		});
		return visitorImageDlg;
	}


	private void onImageVisitorMenuClicked(Dialog d, int position) {

		switch (position) {
			case 0:
				openImageMenuSocial();
				break;
			case 1:
				saveImage(currentClickImageUrl);
				break;
		}
	}

	ECListDialog imageMenuDlg=null;
	ECListDialog imageMenuGoodsDlg=null;

	private void onTopbarShareClick() {

		this.currentClickUrl =getCurrentWebView().getUrl();
		this.currentClickImageUrl="";

		getLinkShareMenuDialog().show();


	}



	ECListDialog linkShareMenuDlg=null;
	private ECListDialog getLinkShareMenuDialog() {
		if(PreferenceUtils.isUserVisitor()){
			return getImageMenuDialogSocial();
		}else {

		}

		if(linkShareMenuDlg!=null){
			return linkShareMenuDlg;
		}


		linkShareMenuDlg = new ECListDialog(this,
				R.array.webview_link_actions);
		linkShareMenuDlg.setTitle(getString(R.string.share));
		linkShareMenuDlg.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onLinkShareMenuClicked(d, position);
			}
		});

		return linkShareMenuDlg;

	}

	private void onLinkShareMenuClicked(Dialog d, int position) {

		switch (position) {
			case 0:
				shareImageCircle(currentClickImageUrl);
				break;
			case 1:
				shareToTopicCircle(this.currentClickImageUrl);
				break;
			case 2:
				openImageMenuSocial();
				break;
		}
	}



	private ECListDialog getImageMenuDialog() {
		if(PreferenceUtils.isUserVisitor()){
			return getVisitorImageDialog();
		}
		LightningView lv=getCurrentWebView();
		String url=lv.getUrl();
		if(LinkUtils.getLinkType(url)== LinkType.Type_Goods){
			return getImageMenuDialogGoods();
		}

		if(HtmlPage.getLinkType(url)== LinkType.Type_News){
			return getImageMenuDialogNews();
		}


		if(imageMenuDlg!=null){
			return imageMenuDlg;
		}


		imageMenuDlg = new ECListDialog(this,
				R.array.webview_image_actions);
		imageMenuDlg.setTitle(getString(R.string.share));
		imageMenuDlg.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onImageMenuClicked(d, position);
			}
		});
		return imageMenuDlg;

	}

	private ECListDialog getImageMenuDialogGoods() {


		if(imageMenuGoodsDlg !=null){
			return imageMenuGoodsDlg;
		}


		imageMenuGoodsDlg = new ECListDialog(this,
				R.array.webview_action_image_goods);
		imageMenuGoodsDlg.setTitle(getString(R.string.share));
		imageMenuGoodsDlg.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onImageGoodsMenuClicked(d, position);
			}
		});
		return imageMenuGoodsDlg;

	}

	ECListDialog imgNewsDlg =null;
	private ECListDialog getImageMenuDialogNews() {
		if (imgNewsDlg != null) {
			return imgNewsDlg;
		}


		imgNewsDlg = new ECListDialog(this,
				R.array.webview_action_image_news);
		imgNewsDlg.setTitle(getString(R.string.share));
		imgNewsDlg.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onImageNewsMenuClicked(d, position);
			}
		});
		return imgNewsDlg;

	}



	ECListDialog imageMenuDlgSocial=null;
	private ECListDialog getImageMenuDialogSocial() {
		if(imageMenuDlgSocial!=null){
			return imageMenuDlgSocial;
		}

		imageMenuDlgSocial = new ECListDialog(this,
				R.array.webview_image_actions_social);
		imageMenuDlgSocial.setTitle(getString(R.string.share));
		imageMenuDlgSocial.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onImageMenuSocialClicked(d, position);
			}
		});

		return imageMenuDlgSocial;

	}

	private void onImageMenuSocialClicked(Dialog d, int position) {
		String url = this.getCurrentWebView().getUrl();
		String title = this.getCurrentWebView().getTitle();
		switch (position) {
			case 0:
				shareWeixinFriend(url, title, this.currentClickImageUrl);
				break;
			case 1:
				shareWeixinCircle(url, title, this.currentClickImageUrl);
				break;
			case 2:
				shareQQFriend(url, title, this.currentClickImageUrl);
				break;
			case 3:
				shareQQZone(url, title, this.currentClickImageUrl);
				break;

		}

	}


	private void shareQQFriend(String url,String title,String imageUrl){

		QQShareApi.shareWebPageQQFriend(this, url, title, "", imageUrl, new IUiListener() {
			@Override
			public void onComplete(Object o) {

			}

			@Override
			public void onError(UiError uiError) {

			}

			@Override
			public void onCancel() {

			}
		});
	}

	private void shareQQZone(String url,String title,String imageUrl){
		QQShareApi.shareToQzone(this, url, title, "", imageUrl, new IUiListener() {
			@Override
			public void onComplete(Object o) {

			}

			@Override
			public void onError(UiError uiError) {

			}

			@Override
			public void onCancel() {

			}
		});
	}


	private void shareWeixinFriend(String url,String title,String imageUrl){
		WeixinShareApi.shareWebPage(url, title, "", imageUrl, false);
	}

	private void shareWeixinCircle(String url,String title,String imageUrl){
		WeixinShareApi.shareWebPage(url, title, "", imageUrl, true);
	}


	ECListDialog imageMenuDlgMore=null;
	private ECListDialog getImageMenuDialogMore() {
		if(imageMenuDlgMore!=null){
			return imageMenuDlgMore;
		}

		imageMenuDlgMore = new ECListDialog(this,
				R.array.webview_image_actions_more);
		imageMenuDlgMore.setTitle(getString(R.string.share));
		imageMenuDlgMore.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
			@Override
			public void onDialogItemClick(Dialog d, int position) {
				onImageMenuMoreClicked(d, position);
			}
		});
		return imageMenuDlgMore;

	}

	private void onImageMenuMoreClicked(Dialog d, int position) {

		switch (position) {
			case 0:
				createTopicCircle(this.currentClickImageUrl);
				break;
			case 1:
				shareToTopicCircle(this.currentClickImageUrl);
				break;
			case 2:
				replyTopic(this.currentClickImageUrl);
				break;
			case 3:
				ECListDialog dialog = getImageMenuDialog();
				dialog.show();
				break;
		}

	}

	private void replyTopic(String img) {

	}

	private void shareToTopicCircle(String img) {


	}

	private void createTopicCircle(String img) {
		String url = this.getCurrentWebView().getUrl();
		String title = this.getCurrentWebView().getTitle();

	}


	public void shareImageFavorite(final String img) {




	}


	private void onShareImageFavoritePage(String url,String title, String img, HtmlPage page) {


	}


	public void shareImageCircle(final String img) {




	}

	private void onShareImageCirclePage(String url,String title, String img, HtmlPage page) {


	}

	private void onImageMenuClicked(Dialog d, int position) {
		switch (position) {
			case 0:
				shareImageFavorite(currentClickImageUrl);
				break;
			case 1:
				shareImagePublic(currentClickImageUrl);
				break;
			case 2:
				shareImageCircle(currentClickImageUrl);
				break;
			case 3:
				shareToTopicCircle(this.currentClickImageUrl);

				break;
			case 4:
				openImageMenuSocial();
				break;
			case 5:
				saveImage(currentClickImageUrl);
				break;
		}
	}

	private void onImageGoodsMenuClicked(Dialog d, int position) {
		switch (position) {

			case 0:
				shareImagePublic(currentClickImageUrl);
				break;
			case 1:
				shareImageCircle(currentClickImageUrl);
				break;


			case 2:
				shareImageGoods(currentClickImageUrl);
				break;

			case 3:
				saveImage(currentClickImageUrl);
				break;

			case 4:
				openImageMenuMore();
				break;
		}
	}


	private void onImageNewsMenuClicked(Dialog d, int position) {
		switch (position) {

			case 0:
				shareImagePublic(currentClickImageUrl);
				break;
			case 1:
				shareImageCircle(currentClickImageUrl);
				break;


			case 2:
				shareImageNews(currentClickImageUrl);
				break;

			case 3:
				saveImage(currentClickImageUrl);
				break;

			case 4:
				openImageMenuMore();
				break;
		}
	}



	private void openImageMenuSocial() {
		ECListDialog dialog = getImageMenuDialogSocial();
		dialog.show();
	}

	private void openImageMenuMore() {
		ECListDialog dialog = getImageMenuDialogMore();
		dialog.show();
	}


	public void shareImageGoods(String img) {

	}

	public void shareImageNews(final String img) {

	}

	public void shareImagePublic(String img) {


	}

	private void saveImage(String img) {
		ImageSaveUtil.saveImage(img);
	}

	/***
	 * 功能：用线程保存图片
	 *
	 *
	 */


	private void onImageLongClick1(final String url) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						newTab(url, false);
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						getCurrentWebView().loadUrl(url);
						break;

					case DialogInterface.BUTTON_NEUTRAL:
						if (API > 8) {
							Utils.downloadFile(mActivity, url,
									getCurrentWebView().getUserAgent(), "attachment", false);
						}
						break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
		builder.setTitle(url.replace(Constants.HTTP, ""))
				.setMessage(getResources().getString(R.string.dialog_image))
				.setPositiveButton(getResources().getString(R.string.action_new_tab),
						dialogClickListener)
				.setNegativeButton(getResources().getString(R.string.action_open),
						dialogClickListener)
				.setNeutralButton(getResources().getString(R.string.action_download),
						dialogClickListener).show();
	}


	/**
	 * This method lets the search bar know that the page is currently loading
	 * and that it should display the stop icon to indicate to the user that
	 * pressing it stops the page from loading
	 */
	public void setIsLoading() {
		if (!mSearch.hasFocus()) {
			mIcon = mDeleteIcon;
			mSearch.setCompoundDrawables(null, null, mDeleteIcon, null);
		}
	}

	boolean pageLoaded=false;
	/**
	 * This tells the search bar that the page is finished loading and it should
	 * display the refresh icon
	 */
	public void setIsFinishedLoading() {
		if (!mSearch.hasFocus()) {
			mIcon = mRefreshIcon;
			mSearch.setCompoundDrawables(null, null, mRefreshIcon, null);
		}
		pageLoaded=true;
	}

	/**
	 * handle presses on the refresh icon in the search bar, if the page is
	 * loading, stop the page, if it is done loading refresh the page.
	 *
	 * See setIsFinishedLoading and setIsLoading for displaying the correct icon
	 */
	public void refreshOrStop() {
		if (getCurrentWebView() != null) {
			if(mIcon==mRefreshIcon){
				getCurrentWebView().reload();
			}else if(mIcon==mDeleteIcon){
				stopLoadingPage();
			}
			/*
			if (getCurrentWebView().getProgress() < 100) {
				stopLoadingPage();

			} else {
				getCurrentWebView().reload();
			}
			*/
		}
	}

	private void stopLoadingPage() {
		getCurrentWebView().stopLoading();

		setIsFinishedLoading();
		mProgressBar.setProgress(100);
	}

	// Override this, use finish() for Incognito, moveTaskToBack for Main
	public void closeActivity() {
		finish();
	}

	public class SortIgnoreCase implements Comparator<HistoryItem> {

		public int compare(HistoryItem o1, HistoryItem o2) {
			return o1.getTitle().toLowerCase(Locale.getDefault())
					.compareTo(o2.getTitle().toLowerCase(Locale.getDefault()));
		}

	}

	/*
	@Override
	public int getMenu() {
		return R.menu.main;
	}

*/

	@Override
	public void onClick(View v) {
		if(v.getId()!=R.id.action_settings) {
			hideSettingPanel();
		}

		switch (v.getId()) {
			case R.id.edit_action:
			case R.id.edit_btn:
				startEditActivity();
				break;
			case R.id.fullscreen_action:
				setFullscreen(true);
				break;

			case R.id.fullscreen_cancel:
				fullscreenCancel();
				break;
			case R.id.share_web_page_action:
				onTopbarShareClick();
				break;
			case R.id.backward_action:
			case R.id.action_back:
				if (getCurrentWebView() != null) {
					if (getCurrentWebView().canGoBack()) {
						getCurrentWebView().goBack();

					} else {
						ToastUtil.showMessage(getString(R.string.browser_no_back));
						//deleteTab(mDrawerListLeft.getCheckedItemPosition());
					}
				}
				break;
			case R.id.forward_action:
			case R.id.action_forward:
				if (getCurrentWebView() != null) {
					if (getCurrentWebView().canGoForward()) {
						getCurrentWebView().goForward();
					}else{
						ToastUtil.showMessage(getString(R.string.browser_no_forward));
					}
				}
				break;

			case R.id.action_windows:
				flipTabsPanel();
				break;
			case R.id.action_tab_close:
				hideTabsPanel();
				break;

			case R.id.action_settings:
				startSettingActivity();
				break;
			case R.id.arrow_button:
				browserExit();
				break;
			case R.id.new_tab_button:
				newTab(ServerConfig.getHomeUrl(), true);
				break;
			case R.id.button_next:
				getCurrentWebView().getWebView().findNext(false);
				break;
			case R.id.button_back:
				getCurrentWebView().getWebView().findNext(true);
				break;
			case R.id.button_quit:
				getCurrentWebView().getWebView().clearMatches();
				mSearchBar.setVisibility(View.GONE);
				break;

			case R.id.close_button:
				//newTab(null, true);
				closeCurrentTab();
				break;
			case R.id.clear_tab_btn:
				closeAllTab();
				break;
			case R.id.settings_btn:
				openSettings();
				break;



			case R.id.history_btn:
				openHistoryActivity();
				break;

			case R.id.favorite_action:
				openSiteHotActivity();
				break;
			case R.id.add_bookmark_btn:
			case R.id.add_bookmark_action:
				addCurrentUrlToBookmark();
				break;

			case R.id.share_btn:
				shareWebLink();
				break;

		}
	}

	private void startEditActivity() {



		LightningView lv=getCurrentWebView();
		final String url = lv.getUrl();
		final String title = lv.getWebTitle();

		lv.parsePage(new LightningView.HtmlPageParseListener() {
			@Override
			public void OnHtmlPageParsed(HtmlPage page) {
				onStartEditHtmlPage(page);
			}
		});



	}


	public void onStartEditHtmlPage(HtmlPage page){

	}

	private void shareWebLink() {
		LightningView lv = getCurrentWebView();
		if (lv == null) {
			return;
		}

		String url = lv.getUrl();
		if (url == null || url.startsWith(Constants.FILE)) {
			return;
		}



	}


	private  void addCurrentUrlToBookmark(){
		LightningView mCurrentView=getCurrentWebView();
		if (!mCurrentView.getUrl().startsWith(Constants.FILE)) {
			HistoryItem bookmark = new HistoryItem(mCurrentView.getUrl(),
					mCurrentView.getTitle());
			if (mBookmarkManager.addBookmarkItem(PreferenceUtils.getUserId(),mCurrentView.getUrl(),
					mCurrentView.getTitle())) {
				removeBookmarkUrl(mCurrentView.getUrl());
				mBookmarkList.add(0,bookmark);
				//Collections.sort(mBookmarkList, new SortIgnoreCase());
				notifyBookmarkDataSetChanged();
				mSearchAdapter.refreshBookmarks();
			}
		}
		ToastUtil.showMessage(getString(R.string.bookmark_added));
	}

	private void openSettings() {

	}

	private void startSettingActivity() {
		flipSettingPanel();
		/*
		Intent intent = new Intent(this,BrowserTab2Activity.class);
		startActivity(intent);
		*/
	}
}

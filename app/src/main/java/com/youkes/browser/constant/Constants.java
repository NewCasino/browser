
package com.youkes.browser.constant;

import android.content.Context;
import android.os.Environment;

import com.youkes.browser.R;

public final class Constants {

	private Constants() {
	}

	public static final boolean FULL_VERSION = true;
	
	public static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
	public static final String MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.4; en-us; Nexus 4 Build/JOP24G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
	public static final String YAHOO_SEARCH = "https://search.yahoo.com/search?p=";
	public static final String GOOGLE_SEARCH = "https://www.google.com/search?client=lightning&ie=UTF-8&oe=UTF-8&q=";
	public static final String BING_SEARCH = "https://www.bing.com/search?q=";
	public static final String DUCK_SEARCH = "https://duckduckgo.com/?t=lightning&q=";
	public static final String DUCK_LITE_SEARCH = "https://duckduckgo.com/lite/?t=lightning&q=";
	public static final String STARTPAGE_MOBILE_SEARCH = "https://startpage.com/do/m/mobilesearch?language=english&query=";
	public static final String STARTPAGE_SEARCH = "https://startpage.com/do/search?language=english&query=";
	public static final String ASK_SEARCH = "http://www.ask.com/web?qsrc=0&o=0&l=dir&qo=lightningBrowser&q=";
	public static final String HOMEPAGE = "about:home";
	public static final String HOME_Bookmark = "about:bookmarks";
	public static final String BAIDU_SEARCH = "https://www.baidu.com/s?wd=";

	public static final String Default_Page = "https://m.baidu.com/";

	public static final String YANDEX_SEARCH = "https://yandex.ru/yandsearch?lr=21411&text=";
	public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory()
			.toString();
	public static final String JAVASCRIPT_INVERT_PAGE = "javascript:(function(){var e='img {-webkit-filter: invert(100%);'+'-moz-filter: invert(100%);'+'-o-filter: invert(100%);'+'-ms-filter: invert(100%); }',t=document.getElementsByTagName('head')[0],n=document.createElement('style');if(!window.counter){window.counter=1}else{window.counter++;if(window.counter%2==0){var e='html {-webkit-filter: invert(0%); -moz-filter: invert(0%); -o-filter: invert(0%); -ms-filter: invert(0%); }'}}n.type='text/css';if(n.styleSheet){n.styleSheet.cssText=e}else{n.appendChild(document.createTextNode(e))}t.appendChild(n)})();";
	public static final String JAVASCRIPT_TEXT_REFLOW = "javascript:document.getElementsByTagName('body')[0].style.width=window.innerWidth+'px';";

	public static final String LOAD_READING_URL = "ReadingUrl";

	public static final String SEPARATOR = "\\|\\$\\|SEPARATOR\\|\\$\\|";
	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";
	public static final String FILE = "file://";
	public static final String FOLDER = "folder://";
	public static final String TAG = "Lightning";

	// These should match the order of @array/proxy_choices_array
	public static final int NO_PROXY = 0;
	public static final int PROXY_ORBOT = 1;
	public static final int PROXY_I2P = 2;
	public static final int PROXY_MANUAL = 3;


	public static final String Web_Title = "title";
	public static final String Web_Url = "url";



	public static final String EXTRA_ID_NEW_TAB = "EXTRA_ID_NEW_TAB";
	public static final String EXTRA_ID_URL = "EXTRA_ID_URL";

	public static final String EXTRA_ID_BOOKMARK_ID = "EXTRA_ID_BOOKMARK_ID";
	public static final String EXTRA_ID_BOOKMARK_URL = "EXTRA_ID_BOOKMARK_URL";
	public static final String EXTRA_ID_BOOKMARK_TITLE = "EXTRA_ID_BOOKMARK_TITLE";

	public static final String EXTRA_SAVED_URL = "EXTRA_SAVED_URL";

	public static final int BOOKMARK_THUMBNAIL_WIDTH_FACTOR = 70;
	public static final int BOOKMARK_THUMBNAIL_HEIGHT_FACTOR = 60;

	/**
	 * Specials urls.
	 */
	public static final String URL_ABOUT_BLANK = "https://m.baidu.com";
	public static final String URL_ABOUT_START = "https://m.baidu.com";
	public static final String URL_ACTION_SEARCH = "action:search?q=";
	public static final String URL_GOOGLE_MOBILE_VIEW = "http://www.google.com/gwt/x?u=%s";
	public static final String URL_GOOGLE_MOBILE_VIEW_NO_FORMAT = "http://www.google.com/gwt/x?u=";

	/**
	 * Search urls.
	 */
	public static String URL_SEARCH_GOOGLE = "http://www.google.com/search?ie=UTF-8&sourceid=navclient&gfns=1&q=%s";

	public static String URL_Search_Baidu = "http://m.baidu.com/s?word=%s";



	public static String URL_SEARCH_WIKIPEDIA = "http://en.wikipedia.org/w/index.php?search=%s&go=Go";

	/**
	 * User agents.
	 */
	public static String USER_AGENT_DEFAULT = "";
	public static String USER_AGENT_DESKTOP = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Chrome/7.0.517.44 Safari/534.7";

	/**
	 * Preferences.
	 */
	public static final String PREFERENCES_GENERAL_HOME_PAGE = "GeneralHomePage";
	public static final String PREFERENCES_GENERAL_SEARCH_URL = "GeneralSearchUrl";
	public static final String PREFERENCES_GENERAL_SWITCH_TABS_METHOD = "GeneralSwitchTabMethod";
	public static final String PREFERENCES_GENERAL_BARS_DURATION = "GeneralBarsDuration";
	public static final String PREFERENCES_GENERAL_BUBBLE_POSITION = "GeneralBubblePosition";
	public static final String PREFERENCES_SHOW_FULL_SCREEN = "GeneralFullScreen";
	public static final String PREFERENCES_GENERAL_HIDE_TITLE_BARS = "GeneralHideTitleBars";
	public static final String PREFERENCES_SHOW_TOAST_ON_TAB_SWITCH = "GeneralShowToastOnTabSwitch";

	public static final String PREFERENCES_UI_VOLUME_KEYS_BEHAVIOUR = "GeneralVolumeKeysBehaviour";

	public static final String PREFERENCES_DEFAULT_ZOOM_LEVEL = "DefaultZoomLevel";

	public static final String PREFERENCES_BROWSER_HISTORY_SIZE = "BrowserHistorySize";
	public static final String PREFERENCES_BROWSER_ENABLE_JAVASCRIPT = "BrowserEnableJavascript";
	public static final String PREFERENCES_BROWSER_ENABLE_IMAGES = "BrowserEnableImages";
	public static final String PREFERENCES_BROWSER_USE_WIDE_VIEWPORT = "BrowserUseWideViewPort";
	public static final String PREFERENCES_BROWSER_LOAD_WITH_OVERVIEW = "BrowserLoadWithOverview";
	public static final String PREFERENCES_BROWSER_ENABLE_FORM_DATA = "BrowserEnableFormData";
	public static final String PREFERENCES_BROWSER_ENABLE_PASSWORDS = "BrowserEnablePasswords";
	public static final String PREFERENCES_BROWSER_ENABLE_COOKIES = "BrowserEnableCookies";
	public static final String PREFERENCES_BROWSER_USER_AGENT = "BrowserUserAgent";
	public static final String PREFERENCES_BROWSER_ENABLE_PLUGINS_ECLAIR = "BrowserEnablePluginsEclair";
	public static final String PREFERENCES_BROWSER_ENABLE_PROXY_SETTINGS = "BrowserEnableProxySettings";
	public static final String PREFERENCES_BROWSER_ENABLE_PLUGINS = "BrowserEnablePlugins";
	public static final String PREFERENCES_BROWSER_RESTORE_LAST_PAGE = "PREFERENCES_BROWSER_RESTORE_LAST_PAGE";

	public static final String PREFERENCES_PRIVACY_CLEAR_CACHE_ON_EXIT = "PrivacyClearCacheOnExit";

	public static final String PREFERENCES_ADBLOCKER_ENABLE = "AdBlockerEnable";

	public static final String PREFERENCES_BOOKMARKS_SORT_MODE = "BookmarksSortMode";

	public static final String PREFERENCES_LAST_VERSION_CODE = "LastVersionCode";

	public static final String PREFERENCES_START_PAGE_SHOW_SEARCH = "StartPageEnableSearch";
	public static final String PREFERENCES_START_PAGE_SHOW_BOOKMARKS = "StartPageEnableBookmarks";
	public static final String PREFERENCES_START_PAGE_SHOW_HISTORY = "StartPageEnableHistory";
	public static final String PREFERENCES_START_PAGE_BOOKMARKS_LIMIT = "StartPageBookmarksLimit";
	public static final String PREFERENCES_START_PAGE_HISTORY_LIMIT = "StartPageHistoryLimit";

	public static final String PREFERENCE_USE_WEAVE = "PREFERENCE_USE_WEAVE";
	public static final String PREFERENCE_WEAVE_SERVER = "PREFERENCE_WEAVE_SERVER";
	public static final String PREFERENCE_WEAVE_USERNAME = "PREFERENCE_WEAVE_USERNAME";
	public static final String PREFERENCE_WEAVE_PASSWORD = "PREFERENCE_WEAVE_PASSWORD";
	public static final String PREFERENCE_WEAVE_KEY = "PREFERENCE_WEAVE_KEY";
	public static final String PREFERENCE_WEAVE_LAST_SYNC_DATE = "PREFERENCE_WEAVE_LAST_SYNC_DATE";

	public static final String WEAVE_AUTH_TOKEN_SCHEME = "{\"secret\":\"%s\",\"password\":\"%s\",\"username\":\"%s\",\"server\":\"%s\"}";

	public static final String WEAVE_DEFAULT_SERVER = "https://auth.services.mozilla.com/";

	public static final String PREFERENCE_BOOKMARKS_DATABASE = "PREFERENCE_BOOKMARKS_DATABASE";

	/**
	 * Methods.
	 */

	/**
	 * Initialize the search url "constants", which depends on the user local.
	 * @param context The current context.
	 */
	public static void initializeConstantsFromResources(Context context) {
		URL_SEARCH_GOOGLE = context.getResources().getString(R.string.Constants_SearchUrlGoogle);
		URL_SEARCH_WIKIPEDIA = context.getResources().getString(R.string.Constants_SearchUrlWikipedia);
	}
}

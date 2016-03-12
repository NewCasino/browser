package com.youkes.browser.database;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.Browser;
import android.widget.Toast;

import com.youkes.browser.R;
import com.youkes.browser.constant.Constants;
import com.youkes.browser.preference.PreferenceManager;
import com.youkes.browser.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

public class BookmarkManager {

	private final Context mContext;
	private static final String TITLE = "title";
	private static final String URL = "url";
	private static final String FOLDER = "folder";
	private static final String ORDER = "order";
	private static final String FILE_BOOKMARKS = "bookmarks.dat";
	private static SortedMap<String, Integer> mBookmarkMap = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);
	private static BookmarkManager mInstance;

	public static BookmarkManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new BookmarkManager(context);
		}
		return mInstance;
	}

	private BookmarkManager(Context context) {
		mContext = context;
		mBookmarkMap = getBookmarkUrls();
	}

	/**
	 * This method adds the the HistoryItem item to permanent bookmark storage
	 * 
	 * @param item
	 */
	private synchronized boolean addBookmark(HistoryItem item) {
		File bookmarksFile = new File(mContext.getFilesDir(), FILE_BOOKMARKS);
		if (item.getUrl() == null || mBookmarkMap.containsKey(item.getUrl())) {
			return false;
		}
		try {
			BufferedWriter bookmarkWriter = new BufferedWriter(new FileWriter(bookmarksFile, true));
			JSONObject object = new JSONObject();
			object.put(TITLE, item.getTitle());
			object.put(URL, item.getUrl());
			object.put(FOLDER, item.getFolder());
			object.put(ORDER, item.getOrder());

			bookmarkWriter.write(object.toString());
			bookmarkWriter.newLine();
			bookmarkWriter.close();
			mBookmarkMap.put(item.getUrl(), 1);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * This method adds the list of HistoryItems to permanent bookmark storage
	 * 
	 * @param list
	 */
	private synchronized void addBookmarkList(List<HistoryItem> list) {
		File bookmarksFile = new File(mContext.getFilesDir(), FILE_BOOKMARKS);
		try {
			BufferedWriter bookmarkWriter = new BufferedWriter(new FileWriter(bookmarksFile, true));
			for (HistoryItem item : list) {
				if (item.getUrl() != null && !mBookmarkMap.containsKey(item.getUrl())) {
					JSONObject object = new JSONObject();
					object.put(TITLE, item.getTitle());
					object.put(URL, item.getUrl());
					object.put(FOLDER, item.getFolder());
					object.put(ORDER, item.getOrder());
					bookmarkWriter.write(object.toString());
					bookmarkWriter.newLine();
					mBookmarkMap.put(item.getUrl(), 1);
				}
			}
			bookmarkWriter.close();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method deletes the bookmark with the given url
	 * 
	 * @param url
	 */
	private synchronized boolean deleteBookmark(String url) {
		List<HistoryItem> list;
		if (url == null) {
			return false;
		}
		mBookmarkMap.remove(url);
		list = getBookmarks(false);
		File bookmarksFile = new File(mContext.getFilesDir(), FILE_BOOKMARKS);
		boolean bookmarkDeleted = false;
		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(bookmarksFile, false));
			for (HistoryItem item : list) {
				if (!item.getUrl().equalsIgnoreCase(url)) {
					JSONObject object = new JSONObject();
					object.put(TITLE, item.getTitle());
					object.put(URL, item.getUrl());
					object.put(FOLDER, item.getFolder());
					object.put(ORDER, item.getOrder());
					fileWriter.write(object.toString());
					fileWriter.newLine();
				} else {
					bookmarkDeleted = true;
				}
			}
			fileWriter.close();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return bookmarkDeleted;
	}

	/**
	 * This method exports the stored bookmarks to a text file in the device's
	 * external download directory
	 */
	private synchronized void exportBookmarks() {
		List<HistoryItem> bookmarkList = getBookmarks(true);
		File bookmarksExport = new File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				"BookmarksExport.txt");
		int counter = 0;
		while (bookmarksExport.exists()) {
			counter++;
			bookmarksExport = new File(
					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
					"BookmarksExport-" + counter + ".txt");
		}
		try {
			BufferedWriter bookmarkWriter = new BufferedWriter(new FileWriter(bookmarksExport,
					false));
			for (HistoryItem item : bookmarkList) {
				JSONObject object = new JSONObject();
				object.put(TITLE, item.getTitle());
				object.put(URL, item.getUrl());
				object.put(FOLDER, item.getFolder());
				object.put(ORDER, item.getOrder());
				bookmarkWriter.write(object.toString());
				bookmarkWriter.newLine();
			}
			bookmarkWriter.close();
			Toast.makeText(
					mContext,
					mContext.getString(R.string.bookmark_export_path) + " "
							+ bookmarksExport.getPath(), Toast.LENGTH_SHORT).show();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method returns a list of all stored bookmarks
	 * 
	 * @return
	 */
	private synchronized List<HistoryItem> getBookmarks(boolean sort) {
		List<HistoryItem> bookmarks = new ArrayList<>();
		File bookmarksFile = new File(mContext.getFilesDir(), FILE_BOOKMARKS);
		try {
			BufferedReader bookmarksReader = new BufferedReader(new FileReader(bookmarksFile));
			String line;
			while ((line = bookmarksReader.readLine()) != null) {
				JSONObject object = new JSONObject(line);
				HistoryItem item = new HistoryItem();
				item.setTitle(object.getString(TITLE));
				item.setUrl(object.getString(URL));
				item.setFolder(object.getString(FOLDER));
				item.setOrder(object.getInt(ORDER));
				item.setImageId(R.drawable.ic_bookmark);
				bookmarks.add(item);
			}
			bookmarksReader.close();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		if (sort) {
			//Collections.sort(bookmarks, new SortIgnoreCase());
		}
		return bookmarks;
	}

	/**
	 * This method returns a list of bookmarks located in the specified folder
	 * 
	 * @param folder
	 * @return
	 */
	private synchronized List<HistoryItem> getBookmarksFromFolder(String folder) {
		List<HistoryItem> bookmarks = new ArrayList<>();
		File bookmarksFile = new File(mContext.getFilesDir(), FILE_BOOKMARKS);
		try {
			BufferedReader bookmarksReader = new BufferedReader(new FileReader(bookmarksFile));
			String line;
			while ((line = bookmarksReader.readLine()) != null) {
				JSONObject object = new JSONObject(line);
				if (object.getString(FOLDER).equals(folder)) {
					HistoryItem item = new HistoryItem();
					item.setTitle(object.getString(TITLE));
					item.setUrl(object.getString(URL));
					item.setFolder(object.getString(FOLDER));
					item.setOrder(object.getInt(ORDER));
					item.setImageId(R.drawable.ic_bookmark);
					bookmarks.add(item);
				}
			}
			bookmarksReader.close();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return bookmarks;
	}

	/**
	 * Method is used internally for searching the bookmarks
	 * 
	 * @return
	 */
	private synchronized SortedMap<String, Integer> getBookmarkUrls() {
		SortedMap<String, Integer> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		File bookmarksFile = new File(mContext.getFilesDir(), FILE_BOOKMARKS);
		try {
			BufferedReader bookmarksReader = new BufferedReader(new FileReader(bookmarksFile));
			String line;
			while ((line = bookmarksReader.readLine()) != null) {
				JSONObject object = new JSONObject(line);
				map.put(object.getString(URL), 1);
			}
			bookmarksReader.close();
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * This method returns a list of all folders
	 * 
	 * @return
	 */
	private synchronized List<HistoryItem> getFolders() {
		List<HistoryItem> folders = new ArrayList<>();
		SortedMap<String, Integer> folderMap = new TreeMap<>(
				String.CASE_INSENSITIVE_ORDER);
		File bookmarksFile = new File(mContext.getFilesDir(), FILE_BOOKMARKS);
		try {
			BufferedReader bookmarksReader = new BufferedReader(new FileReader(bookmarksFile));
			String line;
			while ((line = bookmarksReader.readLine()) != null) {
				JSONObject object = new JSONObject(line);
				String folderName = object.getString(FOLDER);
				if (!folderName.isEmpty() && !folderMap.containsKey(folderName)) {
					HistoryItem item = new HistoryItem();
					item.setTitle(folderName);
					item.setUrl(Constants.FOLDER + folderName);
					folderMap.put(folderName, 1);
					folders.add(item);
				}
			}
			bookmarksReader.close();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return folders;
	}

	/**
	 * This method imports all bookmarks that are included in the device's
	 * permanent bookmark storage
	 */
	private synchronized void importBookmarksFromBrowser(Context context) {
		/*
		if (PreferenceManager.getInstance().getSystemBrowserPresent()) {

			List<HistoryItem> bookmarkList = new ArrayList<>();
			String[] columns = new String[] { Browser.BookmarkColumns.TITLE,
					Browser.BookmarkColumns.URL };
			String selection = Browser.BookmarkColumns.BOOKMARK + " = 1";
			Cursor cursor = mContext.getContentResolver().query(Browser.BOOKMARKS_URI, columns,
					selection, null, null);

			String title, url;
			int number = 0;
			if (cursor.moveToFirst()) {
				do {
					title = cursor.getString(0);
					url = cursor.getString(1);
					if (title.isEmpty()) {
						title = Utils.getDomainName(url);
					}
					number++;
					bookmarkList.add(new HistoryItem(url, title));
				} while (cursor.moveToNext());
			}

			cursor.close();
			addBookmarkList(bookmarkList);

			Utils.showToast(mContext,
					number + " " + mContext.getResources().getString(R.string.message_import));
		} else {
			Utils.createInformativeDialog(context,
					mContext.getResources().getString(R.string.title_error), mContext
							.getResources().getString(R.string.dialog_import_error));
		}
		*/

	}

	/**
	 * This method imports the bookmarks from a backup file that is located on
	 * external storage
	 * 
	 * @param dir
	 * @param file
	 */
	private synchronized void importBookmarksFromFile(File file, Context context) {
		if (file == null) {
			return;
		}
		List<HistoryItem> list = new ArrayList<>();
		try {
			BufferedReader bookmarksReader = new BufferedReader(new FileReader(file));
			String line;
			int number = 0;
			while ((line = bookmarksReader.readLine()) != null) {
				JSONObject object = new JSONObject(line);
				HistoryItem item = new HistoryItem();
				item.setTitle(object.getString(TITLE));
				item.setUrl(object.getString(URL));
				item.setFolder(object.getString(FOLDER));
				item.setOrder(object.getInt(ORDER));
				list.add(item);
				number++;
			}
			bookmarksReader.close();
			addBookmarkList(list);
			Utils.showToast(mContext,
					number + " " + mContext.getResources().getString(R.string.message_import));
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			Utils.createInformativeDialog(context,
					mContext.getResources().getString(R.string.title_error), mContext
							.getResources().getString(R.string.import_bookmark_error));
		}
	}

	/**
	 * This method overwrites the entire bookmark file with the list of
	 * bookmarks. This is useful when an edit has been made to one or more
	 * bookmarks in the list
	 * 
	 * @param list
	 */
	private synchronized void overwriteBookmarks(List<HistoryItem> list) {
		File bookmarksFile = new File(mContext.getFilesDir(), FILE_BOOKMARKS);
		try {
			BufferedWriter bookmarkWriter = new BufferedWriter(new FileWriter(bookmarksFile, false));
			for (HistoryItem item : list) {
				JSONObject object = new JSONObject();
				object.put(TITLE, item.getTitle());
				object.put(URL, item.getUrl());
				object.put(FOLDER, item.getFolder());
				object.put(ORDER, item.getOrder());
				bookmarkWriter.write(object.toString());
				bookmarkWriter.newLine();
			}
			bookmarkWriter.close();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	private class SortIgnoreCase implements Comparator<HistoryItem> {

		public int compare(HistoryItem o1, HistoryItem o2) {
			if (o1 == null || o2 == null || o1.getTitle() == null || o2.getTitle() == null) {
				return 0;
			}
			return o1.getTitle().toLowerCase(Locale.getDefault())
					.compareTo(o2.getTitle().toLowerCase(Locale.getDefault()));
		}

	}

	private static final String[] Youkes = {"http://youkes.com/", "优克斯"};
	private static final String[] Baidu = {"https://m.baidu.com/", "百度"};
	private static final String[] Sina = {"http://sina.cn/", "新浪网"};
	private static final String[] QQ3G = {"http://info.3g.qq.com/", "腾讯网"};
	private static final String[] Sohu = {"http://m.sohu.com/", "搜狐网"};
	private static final String[] Netease = {"http://3g.163.com/", "网易"};
	private static final String[] Ifeng = {"http://i.ifeng.com/", "凤凰网"};

	private static final String[] Youku = {"http://www.youku.com/", "优酷视频"};
	private static final String[] sohuTv = {"http://m.tv.sohu.com/", "搜狐视频"};
	private static final String[] Iqiyi = {"http://m.iqiyi.com/", "爱奇艺视频"};
	private static final String[] Tudou = {"http://www.tudou.com/", "土豆视频"};

	private static final String[] Qidan = {"http://m.qidian.com/", "起点阅读"};
	private static final String[] Hongxiu = {"http://m.hongxiu.com/", "红袖阅读"};

	private static final String[] Kugou = {"http://m.kugou.com/", "酷狗音乐"};
	private static final String[] Kuwo = {"http://m.kuwo.cn/", "酷我音乐"};
	private static final String[] BaiduMusic = {"http://music.baidu.com/", "百度音乐"};
	private static final String[] BaiduBaike = {"https://baike.baidu.com/", "百度百科"};
	private static final String[] BaiduTieba = {"http://tieba.baidu.com/", "百度贴吧"};
	private static final String[] BaiduMap = {"http://map.baidu.com/", "百度地图"};


	private static final String[] eastmoney = {"http://wap.eastmoney.com/", "东方财富"};
	private static final String[] hexun = {"http://m.hexun.com/", "和讯"};
	private static final String[] sinafinance = {"http://finance.sina.cn/", "新浪财经"};

	private static final String[] Taobao = {"https://m.taobao.com/", "淘宝"};
	private static final String[] JD = {"http://m.jd.com/", "京东"};
	private static final String[] Vipcom = {"http://m.vip.com/", "唯品会"};
	private static final String[] Meituan = {"http://i.meituan.com/", "美团"};
	private static final String[] dianping = {"http://m.dianping.com/", "大众点评"};
	private static final String[] tongcheng58 = {"http://m.58.com/", "58同城"};
	private static final String[] ganji = {"http://ganji.com", "赶集"};
	private static final String[] Ctrip = {"http://m.ctrip.com/", "携程"};

	private static final String[] Hupu = {"http://m.hupu.com/", "虎扑"};
	private static final String[] cnBing = {"http://cn.bing.com/", "必应"};
	private static final String[] Tianya = {"http://www.tianya.cn/", "天涯社区"};
	private static final String[] Qiushibaike = {"http://www.qiushibaike.com/", "糗事百科"};

	private static final String[] rayli = {"http://m.rayli.com.cn/", "瑞丽"};
	private static final String[] Tiexue = {"http://m.tiexue.net/", "铁血军事"};


	private static final String[] Hao123 = {"http://m.hao123.com/", "Hao123"};
	private static final String[] Nav2345 = {"http://m.2345.com/", "2345导航"};
	private static final String[] Sogou123 = {"http://123.sogou.com", "搜狗导航"};
	private static final String[] Bing123 = {"http://123.msn.com", "微软导航"};
	private static final String[] Hao360 = {"http://hao.360.cn/", "360导航"};

	private static final String[] Huaban = {"http://huaban.com/", "花瓣"};

	private static final String[] Toutiao = {"http://toutiao.eastday.com/", "头条新闻"};
	private static final String[] JingriToutiao = {"http://m.toutiao.com/", "今日头条"};
	private static final String[] Game7K7K = {"http://www.7k7k.com/", "7K7K小游戏"};
	private static final String[] Game17173 = {"http://m.17173.com/", "17173游戏"};

	private static final String[] Meipai = {"http://www.meipai.com/", "美拍"};

	private static final String[] YidianZhixun = {"http://www.yidianzixun.com/", "一点资讯"};









	private static final String[] FACEBOOK = {"https://www.facebook.com/", "Facebook"};
	private static final String[] TWITTER = {"https://twitter.com", "Twitter"};
	private static final String[] GOOGLE = {"https://www.google.com/", "Google"};
	private static final String[] YAHOO = {"https://www.yahoo.com/", "Yahoo"};

	public static final String[][] DEFAULT_BOOKMARKS = {
			Youkes,
			Baidu,
			Sina,
			//
			Sohu,
			Netease,
			QQ3G,
			Ifeng,
			JingriToutiao,
			cnBing,
			Youku,
			sohuTv,
			Iqiyi,
			Tudou,

			YidianZhixun,
			Hongxiu,

			Kugou,
			Qidan,
			Kuwo,
			BaiduMusic,
			BaiduBaike,
			BaiduTieba,
			BaiduMap,



			eastmoney,
			hexun,
			sinafinance,

			Taobao ,
			JD,
			Vipcom,
			Meituan,
			dianping,
			tongcheng58,
			ganji,
			Ctrip,

			Hupu,

			Tianya,
			Qiushibaike,

			rayli,
			Tiexue ,


			Hao123,
			Nav2345,
			Sogou123,
			Bing123,
			Hao360,

			Huaban,

			Toutiao ,
			Game7K7K ,
			Game17173



	};
}

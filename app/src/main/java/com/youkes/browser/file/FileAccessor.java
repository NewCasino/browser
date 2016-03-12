package com.youkes.browser.file;

import android.os.Environment;
import android.text.TextUtils;


import com.youkes.browser.MainApp;
import com.youkes.browser.R;
import com.youkes.browser.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileAccessor {

	public static final String TAG = FileAccessor.class.getName();
	public static String EXTERNAL_STOREPATH = getExternalStorePath();

	public static final String Share_Image_Dir = getExternalStorePath()
			+ "/youkes/browser/image";

	public static final String Image_Root_Dir = getExternalStorePath()
			+ "/youkes/browser/image";

	public static final String Image_Cache_Dir = getExternalStorePath()
			+ "/youkes/browser/image";


	public static final String All_ROOT_DIR = getExternalStorePath()
			+ "/youkes";

	public static final String APP_ROOT_DIR = getExternalStorePath()
			+ "/youkes/browser";

	public static final String LOCAL_PATH = APP_ROOT_DIR + "/config.txt";

	public static final String Apk_File = getExternalStorePath()
			+ "/youkes/browser/apk";

	public static final String Video_Download = getExternalStorePath()
			+ "/youkes/browser/video_down";

	public static final String Video_Thumb = getExternalStorePath()
			+ "/youkes/browser/video_thumb";

	public static final String Image_Download = getExternalStorePath()
			+ "/youkes/browser/image_down";


	public static final String File_Down = getExternalStorePath()
			+ "/youkes/browser/file_down";

	/**
	 * 初始化应用文件夹目录
	 */
	public static void initFileAccess() {

		
		File allRootDir = new File(All_ROOT_DIR);
		if (!allRootDir.exists()) {
			allRootDir.mkdir();
		}
		
		
		File rootDir = new File(APP_ROOT_DIR);
		if (!rootDir.exists()) {
			rootDir.mkdir();
		}

		

		File imageRootDir = new File(Image_Root_Dir);
		if (!imageRootDir.exists()) {
			imageRootDir.mkdir();
		}

		File imageCacheDir = new File(Image_Cache_Dir);
		if (!imageCacheDir.exists()) {
			imageCacheDir.mkdir();
		}



		File imgDownDir = new File(Image_Download);
		if (!imgDownDir.exists()) {
			imgDownDir.mkdir();
		}

		File videoDownDir = new File(Video_Download);
		if (!videoDownDir.exists()) {
			videoDownDir.mkdir();
		}

		File videoThumbDir = new File(Video_Thumb);
		if (!videoThumbDir.exists()) {
			videoThumbDir.mkdir();
		}




		File apkDir = new File(Apk_File);
		if (!apkDir.exists()) {
			apkDir.mkdir();
		}

		File fileDownDir = new File(File_Down);
		if (!fileDownDir.exists()) {
			fileDownDir.mkdir();
		}



	}

	public static String getAppKey() {
		if (isExistExternalStore()) {
			String content = readContentByFile(LOCAL_PATH);
			if (content != null) {
				try {
					String result = content.split(",")[0];
					if (result != null && result.contains("appkey=")) {
						return result.replace("appkey=", "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String readContentByFile(String path) {
		BufferedReader reader = null;
		String line = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				StringBuilder sb = new StringBuilder();
				reader = new BufferedReader(new FileReader(file));
				while ((line = reader.readLine()) != null) {
					sb.append(line.trim());
				}
				return sb.toString().trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}




	/**
	 * 返回图片存放目录
	 * 
	 * @return
	 */
	public static File getImagePathName() {
		if (!isExistExternalStore()) {
			ToastUtil.showMessage(R.string.media_ejected);
			return null;
		}

		File directory = new File(Image_Download);
		if (!directory.exists() && !directory.mkdirs()) {
			ToastUtil.showMessage("Path to file could not be created");
			return null;
		}

		return directory;
	}

	/**
	 * 获取文件名
	 * 
	 * @param pathName
	 * @return
	 */
	public static String getFileName(String pathName) {

		int start = pathName.lastIndexOf("/");
		if (start != -1) {
			return pathName.substring(start + 1, pathName.length());
		}
		return pathName;

	}

	/**
	 * 外置存储卡的路径
	 * 
	 * @return
	 */
	public static String getExternalStorePath() {
		if (isExistExternalStore()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	public static String getExternalPublicImageStorePath() {
		if (isExistExternalStore()) {
			return Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES).getAbsolutePath();
		}
		return null;
	}

	/**
	 * 是否有外存卡
	 * 
	 * @return
	 */
	public static boolean isExistExternalStore() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * /data/data/com.youkes.bluetooth/files
	 * 
	 * @return
	 */
	public static String getAppContextPath() {
		return MainApp.getInstance().getFilesDir().getAbsolutePath();
	}



	/**
	 * 
	 * @param filePaths
	 */
	public static void delFiles(ArrayList<String> filePaths) {
		for (String url : filePaths) {
			if (!TextUtils.isEmpty(url))
				delFile(url);
		}
	}

	public static boolean delFile(String filePath) {
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return true;
		}

		return file.delete();
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getSecondLevelDirectory(String fileName) {
		if (TextUtils.isEmpty(fileName) || fileName.length() < 4) {
			return null;
		}

		String sub1 = fileName.substring(0, 2);
		String sub2 = fileName.substring(2, 4);
		return sub1 + File.separator + sub2;
	}

	/**
	 * 
	 * @param root
	 * @param srcName
	 * @param destName
	 */
	public static void renameTo(String root, String srcName, String destName) {
		if (TextUtils.isEmpty(root) || TextUtils.isEmpty(srcName)
				|| TextUtils.isEmpty(destName)) {
			return;
		}

		File srcFile = new File(root + srcName);
		File newPath = new File(root + destName);

		if (srcFile.exists()) {
			srcFile.renameTo(newPath);
		}
	}

	public static File getTackPicFilePath() {
		File localFile = new File(getExternalStorePath()
				+ "/youkes/share/.tempchat", "temp.jpg");
		if ((!localFile.getParentFile().exists())
				&& (!localFile.getParentFile().mkdirs())) {

			localFile = null;
		}
		return localFile;
	}
}

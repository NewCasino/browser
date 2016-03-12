package com.youkes.browser.utils;

import android.util.Log;


import com.youkes.browser.file.FileAccessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class LogUtil {

	private static boolean isPrint = true;
	private static boolean isDebug = false;

	public static final String TAG = "youkes_share";
	public static final String MSG = "log msg is null.";
	public static final String LOCAL_POWER_PATH = FileAccessor
			.getExternalStorePath() + "/" + "youkes_browser_log.txt";

	private static List<String> logList;

	static {
		// isPrint = ECApplication.getInstance().getLoggingSwitch();
	}

	public static void v(String tag, String msg) {
		print(Log.VERBOSE, tag, msg);
	}

	public static void v(String msg) {
		v(TAG, msg);
	}

	public static void d(String tag, String msg) {
		print(Log.DEBUG, tag, msg);
		print(isDebug, msg);
	}

	public static void d(String msg) {
		d(TAG, msg);
	}

	public static void i(String tag, String msg) {
		print(Log.INFO, tag, msg);
	}

	public static void i(String msg) {
		i(TAG, msg);
	}

	public static void w(String tag, String msg) {
		print(Log.WARN, tag, msg);
	}

	public static void w(String msg) {
		w(TAG, msg);
	}

	public static void e(String tag, String msg) {
		print(Log.ERROR, tag, msg);
	}

	public static void e(String msg) {
		e(TAG, msg);
	}

	private static void print(int mode, final String tag, String msg) {
		if (!isPrint) {
			return;
		}
		if (msg == null) {
			Log.e(tag, MSG);
			return;
		}
		switch (mode) {
		case Log.VERBOSE:
			Log.v(tag, msg);
			break;
		case Log.DEBUG:
			Log.d(tag, msg);
			break;
		case Log.INFO:
			Log.i(tag, msg);
			break;
		case Log.WARN:
			Log.w(tag, msg);
			break;
		case Log.ERROR:
			Log.e(tag, msg);
			break;
		default:
			Log.d(tag, msg);
			break;
		}
	}

	private static void print(boolean flag, String msg) {
		if (flag && logList != null) {
			logList.add(msg);
		}
	}

	public static void setState(boolean flag) {
		if (flag) {
			if (logList == null) {
				logList = new ArrayList<String>();
			} else {
				logList.clear();
			}
		} else {
			if (logList != null) {
				logList.clear();
				logList = null;
			}
		}

		isDebug = flag;
	}

	public static synchronized void appendByBufferedWriter(String content) {
		if (!isPrint) {
			return;
		}

		BufferedWriter bw = null;
		try {
			File file = new File(LOCAL_POWER_PATH);
			if (!file.exists()) {
				file.createNewFile();
			}
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			bw.write(content);
			bw.flush();
			bw.close();
			LogUtil.d("[FileAccessor - appendContent] append finished.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
				bw = null;
			}
		}
	}

	public static void printFile(InputStream is, String path) {
		FileOutputStream fos = null;
		byte[] temp = null;
		try {
			if (isPrint) {
				File file = new File(path);
				if (!file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				temp = new byte[1024];
				int i = 0;
				while ((i = is.read(temp)) > -1) {
					if (i < temp.length) {
						byte[] b = new byte[i];
						System.arraycopy(temp, 0, b, 0, b.length);
						fos.write(b);
					} else {
						fos.write(temp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fos = null;
			}
			temp = null;
		}
	}

	public static void printErrorStackTrace(Exception ex) {
		File f = new File("/sdcard/Hisun/error.log");
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			ex.printStackTrace(new PrintStream(f));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getLogUtilsTag(Class<? extends Object> clazz) {
		return LogUtil.TAG + "." + clazz.getSimpleName();
	}
}

package com.youkes.browser.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;


import com.youkes.browser.MainApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class BitmapUtil {

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Bitmap loadFromFile(String filename) {
		try {
			File f = new File(filename);
			if (!f.exists()) {
				return null;
			}
			Bitmap tmp = BitmapFactory.decodeFile(filename);
			// tmp = setExifInfo(filename, tmp);
			return tmp;
		} catch (Exception e) {
			return null;
		}

	}



	static int MaxK = 128;

	public static byte[] compressImageBytes(String filename) {
		File file = new File(filename);
		long len = file.length();
		Bitmap image = null;

		if (len < 1024 * 512) {
			image = loadFromFile(filename);
		} else {
			image = loadScaleFile(filename);
		}

		if (image == null) {
			return null;
		}

		//
		image = setExifInfo(filename, image);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

		double size = (double) baos.toByteArray().length;// / 1024.0 > MaxK
		if (size > 1024 * 1024) {
			image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
		} else if (size > 1024 * 512) {
			image.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		} else if (size > 1024 * 256) {
			image.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		} else if (size > 1024 * 128) {
			image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		} else {

		}

		return baos.toByteArray();
	}

	public static Bitmap loadBitmap(String imgpath) {
		return BitmapFactory.decodeFile(imgpath);
	}

	public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
		if (!adjustOritation) {
			return loadBitmap(imgpath);
		} else {
			Bitmap bm = loadBitmap(imgpath);
			return bm;// setExifInfo(imgpath, bm);
		}
	}

	private static Bitmap setExifInfo(String imgpath, Bitmap bm) {
		
		int digree = 0;
		ExifInterface exif = null;
		try {

			exif = new ExifInterface(imgpath);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}
		if (exif != null) {
			// 读取图片中相机方向信息
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			// 计算旋转角度
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
		}
		if (digree != 0) {
			// 旋转图片
			Matrix m = new Matrix();
			m.postRotate(digree);
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					m, true);
		}
		return bm;
	}

	
	public static byte[] getBitmap(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		return baos.toByteArray();

	}
	
	
	public static byte[] compressBitmap(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ((double) baos.toByteArray().length / 1024.0 > 128.0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG,options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			if (options > 1) {
				options -= 1;
			} else if (options > 0.5) {
				options -= 0.5;
			} else if (options > 0.1) {
				options -= 0.1;
			} else {
				break;
			}
		}
		return baos.toByteArray();

	}

	public static Bitmap loadScaleFile(String filename) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = loadBitmap(filename, true);// BitmapFactory.decodeFile(filename,
													// newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		float hh = MainApp.getInstance().getScreenHeight();
		float ww = MainApp.getInstance().getScreenWidth();

		if (w > h) {
			hh = MainApp.getInstance().getScreenWidth();//
			ww = MainApp.getInstance().getScreenHeight();//
		}

		// bitmap.c
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = calculateInSampleSize(w, h, (int) ww, (int) hh);
		Bitmap bitmapNew = BitmapFactory.decodeFile(filename, opt);
		// bitmapNew = setExifInfo(filename, bitmapNew);
		return scaleBitmap(bitmapNew, ww, hh);

	}

	public static Bitmap loadScaleFile2(String filename) {

		// return loadFromFile(filename);

		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filename, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		float hh = MainApp.getInstance().getScreenHeight();
		float ww = MainApp.getInstance().getScreenWidth();

		if (w > h) {
			hh = MainApp.getInstance().getScreenWidth();//
			ww = MainApp.getInstance().getScreenHeight();//
		}
		if (w <= ww || h <= hh) {
			return bitmap;
		}
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) {
			be = 1;
		}
		newOpts.inSampleSize = be;// 设置采样率
		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		// newOpts.inSampleSize = calculateInSampleSize(newOpts, 480, 800);

		bitmap = BitmapFactory.decodeFile(filename, newOpts);
		return scaleBitmap(bitmap, ww, hh);
		// */
	}

	private static Bitmap scaleBitmap(Bitmap bitmap, float ww, float hh) {
		double scaleWidth = 1;
		double scaleHeight = 1;
		int widthOri = bitmap.getWidth();
		int heightOri = bitmap.getHeight();

		if (widthOri > heightOri && widthOri > ww) {
			scaleWidth = 1.0 / (double) (widthOri / ww);
			scaleHeight = scaleWidth;
		} else if (widthOri < heightOri && heightOri > hh) {
			scaleHeight = 1.0 / (double) (heightOri / hh);
			scaleWidth = scaleHeight;
		}

		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale((float) scaleWidth, (float) scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	private static int calculateInSampleSize(int width, int height,
			int reqWidth, int reqHeight) {

		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	public Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 128) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG,options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		// bitmap.
		return bitmap;

	}

	public static Bitmap getBitmap(String imagePath) {
		if (!(imagePath.length() > 5)) {

			return null;

		}

		File cache_file = new File(new File(
				Environment.getExternalStorageDirectory(), "xxxx"),
				"cachebitmap");

		cache_file = new File(cache_file, getMD5(imagePath));

		if (cache_file.exists()) {

			return BitmapFactory.decodeFile(getBitmapCache(imagePath));

		} else {

			try {

				URL url = new URL(imagePath);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();

				conn.setConnectTimeout(5000);

				if (conn.getResponseCode() == 200) {

					InputStream inStream = conn.getInputStream();

					File file = new File(new File(
							Environment.getExternalStorageDirectory(), "xxxx"),
							"cachebitmap");

					if (!file.exists())
						file.mkdirs();

					file = new File(file, getMD5(imagePath));

					FileOutputStream out = new FileOutputStream(file);

					byte buff[] = new byte[1024];

					int len = 0;

					while ((len = inStream.read(buff)) != -1) {

						out.write(buff, 0, len);

					}

					out.close();

					inStream.close();

					return BitmapFactory.decodeFile(getBitmapCache(imagePath));

				}

			} catch (Exception e) {
			}

		}

		return null;
	}

	public static String getBitmapCache(String url) {

		File file = new File(new File(
				Environment.getExternalStorageDirectory(), "xxxx"),
				"cachebitmap");

		file = new File(file, getMD5(url));

		if (file.exists()) {

			return file.getAbsolutePath();

		}

		return null;

	}

	// 加密为MD5

	public static String getMD5(String content) {

		try {

			MessageDigest digest = MessageDigest.getInstance("MD5");

			digest.update(content.getBytes());

			return getHashString(digest);

		} catch (Exception e) {

		}

		return null;

	}

	private static String getHashString(MessageDigest digest) {

		StringBuilder builder = new StringBuilder();

		for (byte b : digest.digest()) {

			builder.append(Integer.toHexString((b >> 4) & 0xf));

			builder.append(Integer.toHexString(b & 0xf));

		}

		return builder.toString().toLowerCase();

	}

	/*
	 * public Bitmap compressImage(Bitmap image) { ByteArrayOutputStream baos =
	 * new ByteArrayOutputStream(); image.compress(Bitmap.CompressFormat.JPEG,
	 * 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中 int options = 100; while
	 * (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
	 * baos.reset();// 重置baos即清空baos image.compress(Bitmap.CompressFormat.JPEG,
	 * options, baos);// 这里压缩options%，把压缩后的数据存放到baos中 options -= 10;// 每次都减少10 }
	 * ByteArrayInputStream isBm = new
	 * ByteArrayInputStream(baos.toByteArray());//
	 * 把压缩后的数据baos存放到ByteArrayInputStream中 Bitmap bitmap =
	 * BitmapFactory.decodeStream(isBm, null, null);//
	 * 把ByteArrayInputStream数据生成图片 return bitmap; }
	 */

}

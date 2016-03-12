package com.youkes.browser.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;

public class ImageUtil {

	public static Bitmap getBitmap(String src) {

		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
			
		} catch (IOException e) {
			return null;
		}

	}

	
	public static Bitmap getBitmap2(String imagePath) {

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

					if (!file.exists()) {
						file.mkdirs();
					}

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

	/**
	 * 
	 * 获取缓存
	 * 
	 * @param url
	 * 
	 * @return
	 */

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


	public static ArrayList<String> processImageDisplayUrl(
			ArrayList<String> imgs) {
			ArrayList<String> ret=new ArrayList<String>();
			
		for(int i=0;i<imgs.size();i++){
			String img=imgs.get(i);
			if(img.indexOf("http")==0){
				ret.add(img);
			}else{
				ret.add("file://"+img);
			}
		}
		return ret;
	}

}

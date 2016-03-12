package com.youkes.browser.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.annotation.NonNull;


import com.youkes.browser.MainApp;
import com.youkes.browser.file.FileAccessor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class ImageSaveUtil {

	// save jpg
	private static String saveImage(byte[] bytes) {
		Context context= MainApp.getInstance().getApplicationContext();
		if (bytes == null) {
			return "";
		}

		File dir = new File(FileAccessor.APP_ROOT_DIR, "image");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		long t = new Date().getTime();

		String filename = t + ".jpg";
		String path = FileAccessor.Share_Image_Dir + "/" + filename;

		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(path));
			bos.write(bytes);
			bos.flush();
			bos.close();

			//addImageToGallery(path, context);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return path;

	}

	public static void saveBitmapPng(final Bitmap bmp, final String filepath) {
		new Thread() {
			@Override
			public void run() {
				saveBitmapPngInner(bmp,filepath);
			}
		}.start();
	}

	private static void saveBitmapPngInner(Bitmap bmp,String filepath) {

		if(bmp==null){
			return;
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filepath);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
			// PNG is a lossless format, the compression factor (100) is ignored
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	private static void addImageToGallery(final String filePath,
			final Context context,String contentType) {

		ContentValues values = new ContentValues();

		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
		values.put(Images.Media.MIME_TYPE, contentType);
		values.put(MediaStore.MediaColumns.DATA, filePath);

		context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI,
				values);
	}

	//save to jpg only but if
	public static void saveImage(String downImgUrl) {

		ToastUtil.showMessage("下载图片:"+downImgUrl);
		if(downImgUrl.indexOf("http://")==0||downImgUrl.indexOf("https://")==0){
			new SaveImage(downImgUrl).execute();
			return;
		}else{

		}
	}

	private static class SaveImage extends AsyncTask<String, Void, String> {

		String downImgUrl = "";

		public SaveImage(String img) {
			downImgUrl = img;
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			try {
				String path = ImageSaveUtil.saveImageInner( downImgUrl);
				result = "图片已保存至：" + path;
			} catch (Exception e) {
				result = "保存失败！" + e.getLocalizedMessage();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			ToastUtil.showMessage(result);
		}
	}

	@NonNull
	private static String saveImageInner(String downImgUrl) {
		Context context= MainApp.getInstance().getApplicationContext();
		File dir = new File(FileAccessor.Image_Download);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String path = "";



		InputStream inputStream = null;
		try {
			URL url = new URL(downImgUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(20000);
			int respCode=conn.getResponseCode();
			//if (conn.getResponseCode() == 200) {
				inputStream = conn.getInputStream();
			//}
			byte[] buffer = new byte[4096];
			int len = 0;
			String contentType=conn.getContentType();
			String ext= ContentTypeUtil.getExt(conn.getContentType());

			long t = new Date().getTime();
			String filename = t + ext;
			 path = FileAccessor.Image_Download + "/" + filename;

			File file = new File(path);

			FileOutputStream outStream = new FileOutputStream(file);
			while ((len = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			addImageToGallery(path, context,contentType);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return path;
	}
}

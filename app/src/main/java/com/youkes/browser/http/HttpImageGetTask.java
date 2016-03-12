package com.youkes.browser.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpImageGetTask extends AsyncTask<String, Void, Bitmap> {
	private OnImageDownloadCompleted listener;

	public HttpImageGetTask(OnImageDownloadCompleted listener) {
		this.listener = listener;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		try {
			return downloadUrl(urls[0]);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		listener.onBitmapCompleted(result);
	}

	private Bitmap downloadUrl(String myurl) throws IOException {

		try {
			URL url = new URL(myurl);
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

}

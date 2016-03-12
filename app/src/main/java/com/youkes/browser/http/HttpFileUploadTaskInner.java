package com.youkes.browser.http;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class HttpFileUploadTaskInner extends AsyncTask<String, Void, String> {

	private OnTaskCompleted listener;
	List<NameValuePair> params;
	File uploadFile = null;

	public HttpFileUploadTaskInner(OnTaskCompleted listener,
								   List<NameValuePair> params, File f) {
		this.listener = listener;
		this.params = params;
		this.uploadFile = f;

	}

	@Override
	protected String doInBackground(String... urls) {

		return downloadUrl(urls[0]);

	}

	@Override
	protected void onPostExecute(String result) {
		listener.onTaskCompleted(result);
	}

	public String readIt(InputStream stream, int len) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] data = new char[len];
		StringBuilder buffer = new StringBuilder(len * 10);
		int size;

		size = reader.read(data, 0, data.length);
		while (size > 0) {
			String str = new String(data, 0, size);
			buffer.append(str);
			size = reader.read(data, 0, data.length);
		}
		return buffer.toString();

	}

	private String downloadUrl(String postUrl) {
		String attachmentName = "file";
		String attachmentFileName = this.uploadFile.getName();// "bitmap.bmp";
		String crlf = "\r\n";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		try {
			// Setup the request:
			HttpURLConnection httpUrlConnection = null;
			URL url;

			url = new URL(postUrl);

			httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setDoOutput(true);

			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
			httpUrlConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream request = new DataOutputStream(
					httpUrlConnection.getOutputStream());

			int len = params.size();
			for (int i = 0; i < len; i++) {

				NameValuePair pair = params.get(i);
				String name = pair.getName();
				String val = pair.getValue();

				request.writeBytes(twoHyphens + boundary + lineEnd);
				request.writeBytes("Content-Disposition: form-data; name=\""
						+ name + "\"" + lineEnd);
				request.writeBytes("Content-Type: text/plain;charset=UTF-8"
						+ lineEnd);
				request.writeBytes("Content-Length: " + val.length() + lineEnd);
				request.writeBytes(lineEnd);
				String encVal = URLEncoder.encode(val, "UTF-8");
				request.writeBytes(encVal);
				request.writeBytes(lineEnd);

			}

			request.writeBytes(twoHyphens + boundary + crlf);
			request.writeBytes("Content-Disposition: form-data; name=\""
					+ attachmentName + "\";filename=\"" + attachmentFileName
					+ "\"" + crlf);
			request.writeBytes(crlf);

			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024 * 50;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			int count = 0;

			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				request.write(buffer, 0, length);
				count = count + buffer.length;
				// Log.v(TAG, "文件总大小=="+getByte(new
				// File(imgfilestring)).length+"    每次上传的length=="+length+"    上传进度="+cout);
			}

			request.writeBytes(crlf);
			request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

			// Flush output buffer:
			request.flush();
			request.close();

			// Get response:

			InputStream responseStream = new BufferedInputStream(
					httpUrlConnection.getInputStream());
			BufferedReader responseStreamReader = new BufferedReader(
					new InputStreamReader(responseStream));
			String line = "";

			StringBuilder stringBuilder = new StringBuilder();
			while ((line = responseStreamReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}

			responseStreamReader.close();
			String respStr = stringBuilder.toString();

			// Close response stream:

			responseStream.close();

			// Close the connection:

			httpUrlConnection.disconnect();

			fStream.close();

			return respStr;

		} catch (IOException e) {
			return "{'api':'/error/network',status:4,msg:'网络错误，请检查网络连接',ex:"
					+ e.getMessage() + "}";
		}

	}

}

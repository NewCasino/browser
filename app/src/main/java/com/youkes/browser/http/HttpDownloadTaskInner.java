package com.youkes.browser.http;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import com.youkes.browser.MainApp;
import com.youkes.browser.utils.ContentTypeUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xuming on 2015/9/27.
 */
public class HttpDownloadTaskInner extends AsyncTask<String, Integer, String> {


    boolean paused=false;
    public void pause() {
        paused=true;
    }
    public void resume() {
        paused=false;
    }

    public interface DownloadListener{
        void onPreExecute();
        void onProgressUpdate(Integer... progress);
        void onPostExecute();
    }

    DownloadListener downloadListener=null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) MainApp.getContext().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        if(downloadListener!=null){
            downloadListener.onPreExecute();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        if(downloadListener!=null){
            downloadListener.onProgressUpdate(progress);
        }
        /*
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
        */
    }


    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        if(downloadListener!=null){
            downloadListener.onPostExecute();
        }
    }


    //private Context context;
    private PowerManager.WakeLock mWakeLock;
    private String filePath="";
    public String getContentType() {
        return contentType;
    }

    private String contentType="";
        public HttpDownloadTaskInner(DownloadListener listener,String filepath) {
            this.downloadListener=listener;
            this.filePath=filepath;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept","*/*");
                connection.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
                connection.setRequestProperty("Connection", "Keep-Alive");//Connection:keep-alive
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                this.contentType=connection.getContentType();
                String ext= ContentTypeUtil.getExt(contentType);
                if(this.downloadListener!=null){

                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(filePath);
                byte data[] = new byte[4096];
                int total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    //if (fileLength > 0) { // only if total length is known
                        publishProgress(total,fileLength);
                   // }
                    output.write(data, 0, count);

                    while (paused){
                        Thread.sleep(1000);
                    }
                }

            } catch (Exception e) {
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;

        }

}

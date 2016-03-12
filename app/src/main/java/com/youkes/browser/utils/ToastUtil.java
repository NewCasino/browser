package com.youkes.browser.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.youkes.browser.MainApp;

public class ToastUtil {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();
    public static void showMessage(final String msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }
    public static void showMessageLong(final String msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }

    /**
     * 根据设置的文本显示
     * @param msg
     */
    public static void showMessage(final int msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个文本并且设置时长
     * @param msg
     * @param len
     */
    public static void showMessage(final CharSequence msg, final int len) {
        if (msg == null || msg.equals("")) {
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) { //加上同步是为了每个toast只要有机会显示出来
                    if (toast != null) {
                        //toast.cancel();
                        toast.setText(msg);
                        toast.setDuration(len);
                    } else {
                        toast = Toast.makeText(MainApp.getInstance().getApplicationContext(), msg, len);
                    }
                    toast.show();
                }
            }
        });
    }

    /**
     * 资源文件方式显示文本
     * @param msg
     * @param len
     */
    public static void showMessage(final int msg, final int len) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        //toast.cancel();
                        toast.setText(msg);
                        toast.setDuration(len);
                    } else {
                        toast = Toast.makeText(MainApp.getInstance().getApplicationContext(), msg, len);
                    }
                    toast.show();
                }
            }
        });
    }
}

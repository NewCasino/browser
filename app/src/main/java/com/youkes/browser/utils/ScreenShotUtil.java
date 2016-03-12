package com.youkes.browser.utils;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.youkes.browser.MainApp;
/**
 * Created by xuming on 2015/10/22.
 */
public class ScreenShotUtil {
    public static Bitmap capture(View view, float width, float height, boolean scroll, Bitmap.Config config) {
        if (!view.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }
        if(width==0){
            width= MainApp.getInstance().getScreenWidth();
        }
        if(height==0){
            height= MainApp.getInstance().getScreenHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, config);
        bitmap.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap);
        int left = view.getLeft();
        int top = view.getTop();
        if (scroll) {
            left = view.getScrollX();
            top = view.getScrollY();
        }
        int status = canvas.save();
        canvas.translate(-left, -top);
        float scale = width / view.getWidth();
        canvas.scale(scale, scale, left, top);
        view.draw(canvas);
        canvas.restoreToCount(status);
        Paint alphaPaint = new Paint();
        alphaPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0f, 0f, 1f, height, alphaPaint);
        canvas.drawRect(width - 1f, 0f, width, height, alphaPaint);
        canvas.drawRect(0f, 0f, width, 1f, alphaPaint);
        canvas.drawRect(0f, height - 1f, width, height, alphaPaint);
        canvas.setBitmap(null);
        return bitmap;
    }


}

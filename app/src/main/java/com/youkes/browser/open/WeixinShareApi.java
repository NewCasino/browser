package com.youkes.browser.open;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youkes.browser.MainApp;
import com.youkes.browser.R;
import com.youkes.browser.utils.BitmapUtil;
import com.youkes.browser.utils.ImageUtil;


/**
 * Created by xuming on 2016/3/1.
 */
public class WeixinShareApi {

    public static final String APP_ID="wx3dbd0cb597df16fe";
    private static IWXAPI api=null;


    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static void shareWebPage(final String url,final String title,final String desc,final String img,final boolean timelined) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doShareWebPage(url,title,desc,img,timelined);
            }
        }
        ).start();
    }

    private static void doShareWebPage(final String url,final String title,final String desc,final String img,final boolean timelined) {
        if(api==null) {
            api= WXAPIFactory.createWXAPI(MainApp.getContext(), APP_ID);
        }

        Bitmap bitmap= ImageUtil.getBitmap(img);
        Bitmap thumb =null;
        if(bitmap!=null){
            thumb=Bitmap.createScaledBitmap(bitmap, 64, 64, true);
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;

        if(thumb!=null) {
            msg.thumbData = BitmapUtil.bmpToByteArray(thumb, true);
        }else{
            Bitmap thumbIcon = BitmapFactory.decodeResource(MainApp.getInstance().getResources(), R.drawable.icon);
            msg.thumbData = BitmapUtil.bmpToByteArray(thumbIcon, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = timelined ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }
    /*

        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = text;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        api.sendReq(req);


        WXImageObject imgObj = new WXImageObject();
        imgObj.imageUrl = url;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);


        WXMusicObject music = new WXMusicObject();
        //music.musicUrl = "http://www.baidu.com";
        music.musicUrl="http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
        //music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = "Music Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
        msg.description = "Music Album Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";

        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);


        WXVideoObject video = new WXVideoObject();
        video.videoUrl = "http://www.baidu.com";

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = "Video Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
        msg.description = "Video Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);


        video.videoLowBandUrl = "http://www.qq.com";

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = "Video Title";
        msg.description = "Video Description";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);


    }
    */

}

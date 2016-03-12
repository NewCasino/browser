package com.youkes.browser.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.youkes.browser.MainApp;
import com.youkes.browser.R;

/**
 * Created by xuming on 2016/1/12.
 */
public class GlideUtil {

    /*

     */
    public static String UserAgent="Mozilla/5.0 (Linux; U; Android 2.1; en-us; GT-I9000 Build/ECLAIR) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2";
    public static void displayImageReferer(String url,ImageView imageView,String referer) {
        if(url==null){
            return;
        }
        LazyHeaders.Builder builder=new LazyHeaders.Builder().addHeader("User-Agent", UserAgent);
        if(referer!=null){
            builder.addHeader("Referer", referer);
        }

        /*
                .addHeader("key2", new LazyHeaderFactory() {
                    @Override
                    public String buildHeader() {
                        String expensiveAuthHeader = computeExpensiveAuthHeader();
                        return expensiveAuthHeader;
                    }
                })
                */

        GlideUrl glideUrl = new GlideUrl(url,builder.build());

        Glide.with(MainApp.getContext())
                .load(glideUrl)
                .placeholder(R.drawable.pictures_no)
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }


    public static void displayImage(String url,ImageView imageView) {


        Glide.with(MainApp.getContext())
                .load(url)

                .placeholder(R.drawable.pictures_no)
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }

    private static String computeExpensiveAuthHeader() {
        return "";
    }

    public static void displayImageGif(String url,ImageView imageView) {

        Glide.with(MainApp.getContext())
                .load(url)
                .thumbnail(0.2f)
                .placeholder(R.drawable.pictures_no)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }

    public static void displayImageBitmap(String url,ImageView imageView) {

        Glide.with(MainApp.getContext())
                .load(url)
                .asBitmap()
                .thumbnail(0.2f)
                .placeholder(R.drawable.pictures_no)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }


    public static void displayImageBitmapCaptcha(String url, ImageView imageView) {

        Glide.with(MainApp.getContext())
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

    }

    public static void displayImageThumb(String url,ImageView imageView) {

        Glide.with(MainApp.getContext())
                .load(url)
                .asBitmap()
                .thumbnail(0.2f)
                .placeholder(R.drawable.pictures_no)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }

}

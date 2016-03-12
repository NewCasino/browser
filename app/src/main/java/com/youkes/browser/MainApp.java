package com.youkes.browser;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



import com.youkes.browser.database.BookmarkDatabase;
import com.youkes.browser.database.HistoryDatabase;
import com.youkes.browser.file.FileAccessor;
import com.youkes.browser.preference.PreferenceUtils;



public class MainApp extends Application {

    private static MainApp instance;
    public static Context getContext() {
        return context;
    }
    private static Context context;
    /**
     * 单例，返回一个实例
     * @return
     */
    public static MainApp getInstance() {
        if (instance == null) {

        }
        return instance;
    }



    public  boolean hasNetwork() {
        if(context==null){
            return  false;
        }
        android.net.ConnectivityManager cManager = (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取应用程序版本名称
     * @return
     */
    public String getVersion() {
        String version = "0.0.0";
        if(context == null) {
            return version;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }


    /**
     * 获取应用版本号
     * @return 版本号
     */
    public int getVersionCode() {
        int code = 1;
        if(context == null) {
            return code;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return code;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;

        setChattingContactId();
        initImageLoader();
        
        FileAccessor.initFileAccess();
        initDatabases();


    }



    private void initDatabases() {

    }


    public boolean isNetworkAvailable() {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
	}
    
    /**
     * 保存当前的聊天界面所对应的联系人、方便来消息屏蔽通知
     */
    private void setChattingContactId() {

    }

    private void initImageLoader() {

        /*
        Md5FileNameGenerator ng=new Md5FileNameGenerator();
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), FileAccessor.Image_Cache_Dir_Name);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .imageDownloader(new CustomImageDownloader(context))
                .threadPoolSize(1)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                // .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(ng)
                        // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiscCache(cacheDir ,null ,ng))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // .writeDebugLogs() // Remove for release app
                .build();//开始构建
        
        ImageLoader.getInstance().init(config);
        */
    }

    /**
     * 返回配置文件的日志开关
     * @return
     */
    public boolean getLoggingSwitch() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
            boolean b = appInfo.metaData.getBoolean("LOGGING");

            return b;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }



    public float getScreenHeight() {
		return getBaseContext().getResources().getDisplayMetrics().heightPixels;

	}

	public float getScreenWidth() {
		return getBaseContext().getResources().getDisplayMetrics().widthPixels;

	}
	
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    public int fromDPToPix( int dp) {
        return Math.round(getDensity() * dp);
    }

    float density=0;
    public float getDensity() {
        Context context= getContext();
        if (density <= 0){
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }






    public void logout() {
        PreferenceUtils.setAnonymous();
        //AccountApi.getInstance().setLogout();
        release();
    }


    public static void release() {
        HistoryDatabase.reset();
        BookmarkDatabase.reset();
    }



}

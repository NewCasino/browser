package com.youkes.browser.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;


import com.youkes.browser.MainApp;

import junit.framework.Assert;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HelpUtils {
    public static final String TAG = "youkes_share.Utils";
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
    public static boolean inNativeAllocAccessError = false;
    /**当前SDK版本号*/
    private static int mSdkint = -1;
    /**
     * 计算语音文件的时间长度
     * @param file
     * @return
     */
    public static int calculateVoiceTime(String file) {
        File _file = new File(file);

        //ChattingVoiceMeta.retrive(file);
        //ToastUtil.showMessage();
        if(!_file.exists()) {
            return 0;
        }
        // 650个字节就是1s
        int duration = (int) Math.ceil(_file.length() / 650) ;

        if(duration > 60) {
            return 60;
        }

        if(duration < 1) {
            return 1;
        }
        return duration;
    }

    public static int mediaDuration(Context context,String path){
        MediaPlayer mp = MediaPlayer.create(context, Uri.parse(path));
        if(mp==null){
            return 0;
        }
        int duration = mp.getDuration();
        mp.release();
        return duration;
    }
    private static MessageDigest md = null;
    public static String md5(final String c) {
        if (md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        if (md != null) {
            md.update(c.getBytes());
            return byte2hex(md.digest());
        }
        return "";
    }

    public static String byte2hex(byte b[]) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = (new StringBuffer(String.valueOf(hs))).toString();
        }

        return hs.toUpperCase();
    }

    /**
     * 将集合转换成字符串，用特殊字符做分隔符
     * @param srcList 转换前集合
     * @param separator 分隔符
     * @return
     */
    public static String listToString(List<String> srcList, String separator) {
        if (srcList == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < srcList.size(); ++i)
            if (i == srcList.size() - 1) {
                sb.append(((String) srcList.get(i)).trim());
            } else {
                sb.append(((String) srcList.get(i)).trim() + separator);
            }
        return sb.toString();
    }

    /**
     * SDK版本号
     * @return
     */
    public static int getSdkint() {
        if(mSdkint < 0) {
            mSdkint = Build.VERSION.SDK_INT;
        }
        return mSdkint;
    }

    /**
     * Java文件操作 获取文件扩展名
     * Get the file extension, if no extension or file name
     *
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 返回文件名
     * @param pathName
     * @return
     */
    public static String getFilename(String pathName) {
        File file = new File(pathName);
        if(!file.exists()) {
            return "";
        }
        return file.getName();
    }

    /**
     * 过滤字符串为空
     * @param str
     * @return
     */
    public static String nullAsNil(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * 将字符串转换成整型，如果为空则返回默认值
     * @param str 字符串
     * @param def 默认值
     * @return
     */
    public static int getInt(String str, int def) {
        try {
            if (str == null) {
                return def;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 删除号码中的所有非数字
     *
     * @param str
     * @return
     */
    public static String filterUnNumber(String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("+86")) {
            str = str.substring(3, str.length());
        }

        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        // 替换与模式匹配的所有字符（即非数字的字符将被""替换）
        //对voip造成的负数号码，做处理
        if(str.startsWith("-")){
            return "-"+m.replaceAll("").trim();
        }else{
            return m.replaceAll("").trim();
        }

    }

    /**
     *
     * @param c
     * @return
     */
    public static boolean characterChinese(char c) {
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock
                .of(c);
        return ((unicodeBlock != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
                && (unicodeBlock != Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS)
                && (unicodeBlock != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
                && (unicodeBlock != Character.UnicodeBlock.GENERAL_PUNCTUATION)
                && (unicodeBlock != Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION)
                && (unicodeBlock != Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS));
    }

    public static String getGroupShortId(String id) {
        /*if(TextUtils.isEmpty(id) || !id.startsWith("G")) {
            return id;
        }
        return id.substring(id.length() - 6 , id.length());*/
        return id;
    }

    public static final String PHONE_PREFIX = "+86";
    /**
     * 去除+86
     * @param phoneNumber
     * @return
     */
    public static String formatPhone(String phoneNumber) {
        if (phoneNumber == null) {
            return "";
        }
        if (phoneNumber.startsWith(PHONE_PREFIX)) {
            return phoneNumber.substring(PHONE_PREFIX.length()).trim();
        }
        return phoneNumber.trim();
    }

    /**
     *
     * @param userData
     * @return
     */
    public static String getFileNameFormUserdata(String userData) {
        if(TextUtils.isEmpty(userData) || "null".equals(userData)) {
            return "";
        }
        return userData.substring(userData.indexOf("fileName=") + "fileName=".length(), userData.length());
    }

    static MediaPlayer mediaPlayer = null;
    public static void playNotifycationMusic (Context context ,String voicePath) throws IOException  {
        //paly music ...
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(voicePath);
        if(mediaPlayer == null ) {
            mediaPlayer = new MediaPlayer();
        }
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                fileDescriptor.getLength());
        mediaPlayer.prepare();
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /**
     * @param context
     * @param intent
     * @param appPath
     * @return
     */
    public static String resolvePhotoFromIntent(Context context , Intent intent , String appPath) {
        if(context == null || intent == null || appPath == null) {
            LogUtil.e(LogUtil.getLogUtilsTag(HelpUtils.class), "resolvePhotoFromIntent fail, invalid argument");
            return null;
        }
        Uri uri = Uri.parse(intent.toURI());
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        try {

            String pathFromUri = null;
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                // if it is a picasa image on newer devices with OS 3.0 and up
                if(uri.toString().startsWith("content://com.google.android.gallery3d")) {
                    // Do this in a background thread, since we are fetching a large image from the web
                    pathFromUri =  saveBitmapToLocal(appPath, createChattingImageByUri(intent.getData()));
                } else {
                    // it is a regular local image file
                    pathFromUri =  cursor.getString(columnIndex);
                }
                cursor.close();
                LogUtil.d(TAG, "photo from resolver, path: " + pathFromUri);
                return pathFromUri;
            } else {

                if(intent.getData() != null) {
                    pathFromUri = intent.getData().getPath();
                    if(new File(pathFromUri).exists()) {
                        LogUtil.d(TAG, "photo from resolver, path: " + pathFromUri);
                        return pathFromUri;
                    }
                }

                // some devices (OS versions return an URI of com.android instead of com.google.android
                if((intent.getAction() != null) && (!(intent.getAction().equals("inline-data")))){
                    // use the com.google provider, not the com.android provider.
                    // Uri.parse(intent.getData().toString().replace("com.android.gallery3d","com.google.android.gallery3d"));
                    pathFromUri =  saveBitmapToLocal(appPath, (Bitmap)intent.getExtras().get("data"));
                    LogUtil.d(TAG, "photo from resolver, path: " + pathFromUri);
                    return pathFromUri;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        LogUtil.e(TAG, "resolve photo from intent failed ");
        return null;
    }


    /**
     *
     * @param uri
     * @return
     */
    public static Bitmap createChattingImageByUri(Uri uri) {
        return createChattingImage(0, null, null, uri, 0.0F, 400, 800);
    }

    /**
     *
     * @param resource
     * @param path
     * @param b
     * @param uri
     * @param dip
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createChattingImage(int resource , String path , byte[] b , Uri uri ,float dip , int width , int height ) {
        if(width <= 0 || height <= 0) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        int outWidth = 0;
        int outHeight = 0;
        int sampleSize = 0;
        try {

            do {
                if(dip != 0.0F) {
                    options.inDensity = (int)(160.0F * dip);
                }
                options.inJustDecodeBounds = true;
                decodeMuilt(options, b, path, uri, resource);
                //
                outWidth = options.outWidth;
                outHeight = options.outHeight;

                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                if(outWidth <= width || outHeight <= height){
                    sampleSize = 0;
                    setInNativeAlloc(options);
                    Bitmap decodeMuiltBitmap = decodeMuilt(options, b, path, uri, resource);
                    return decodeMuiltBitmap;
                } else {
                    options.inSampleSize = (int)Math.max(outWidth / width, outHeight / height);
                    sampleSize = options.inSampleSize;
                }
            } while (sampleSize != 0);

        } catch (IncompatibleClassChangeError e) {
            e.printStackTrace();
            throw ((IncompatibleClassChangeError)new IncompatibleClassChangeError("May cause dvmFindCatchBlock crash!").initCause(e));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            BitmapFactory.Options catchOptions = new BitmapFactory.Options();
            if (dip != 0.0F) {
                catchOptions.inDensity = (int) (160.0F * dip);
            }
            catchOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            if(sampleSize != 0) {
                catchOptions.inSampleSize = sampleSize;
            }
            setInNativeAlloc(catchOptions);
            try {
                return decodeMuilt(options, b, path, uri, resource);
            } catch (IncompatibleClassChangeError twoE) {
                twoE.printStackTrace();
                throw ((IncompatibleClassChangeError)new IncompatibleClassChangeError("May cause dvmFindCatchBlock crash!").initCause(twoE));
            } catch (Throwable twoThrowable) {
                twoThrowable.printStackTrace();
            }
        }

        return null;
    }

    /**
     * save image from uri
     * @param outPath
     * @param bitmap
     * @return
     */
    public static String saveBitmapToLocal(String outPath , Bitmap bitmap) {
        try {
            String imagePath = outPath + HelpUtils.md5(DateFormat.format("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()).toString()) + ".jpg";
            File file = new File(imagePath);
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.close();
            LogUtil.d(TAG, "photo image from data, path:" + imagePath);
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveBitmapToLocal(File file , Bitmap bitmap) {
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.close();
            LogUtil.d(TAG, "photo image from data, path:" + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param options
     * @param data
     * @param path
     * @param uri
     * @param resource
     * @return
     */
    public static Bitmap decodeMuilt(BitmapFactory.Options options , byte[] data , String path , Uri uri , int resource) {
        try {

            if(!checkByteArray(data) && TextUtils.isEmpty(path) && uri == null && resource <= 0) {
                return null;
            }

            if(checkByteArray(data)) {
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            }

            if (uri != null){
                InputStream inputStream = MainApp.getContext().getContentResolver().openInputStream(uri);
                Bitmap localBitmap = BitmapFactory.decodeStream(inputStream,null, options);
                inputStream.close();
                return localBitmap;
            }

            if(resource > 0) {
                return BitmapFactory.decodeResource(MainApp.getContext().getResources(), resource);
            }
            return BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setInNativeAlloc(BitmapFactory.Options options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && !inNativeAllocAccessError) {
            try {
                BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
                return ;
            } catch (Exception e) {
                inNativeAllocAccessError = true;
            }
        }
    }

    public static boolean checkByteArray(byte[] b) {
        return b != null && b.length > 0;
    }

    public static Bitmap getSuitableBitmap(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            LogUtil.e(TAG, "filepath is null or nil");
            return null;
        }

        if(!new File(filePath).exists()) {
            LogUtil.e(TAG, "getSuitableBmp fail, file does not exist, filePath = " + filePath);
            return null;
        }
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap decodeFile = BitmapFactory.decodeFile(filePath );
            if(decodeFile != null) {
                decodeFile.recycle();
            }

            if ((options.outWidth <= 0) || (options.outHeight <= 0)) {
                LogUtil.e(TAG,  "get bitmap fail, file is not a image file = " + filePath);
                return null;
            }

            int maxWidth = 960;
            int width = 0;
            int height = 0;
            if((options.outWidth <= options.outHeight * 2.0D) && options.outWidth > 480) {
                height = maxWidth;
                width = options.outWidth;
            }
            if ((options.outHeight <= options.outWidth * 2.0D)
                    || options.outHeight <= 480) {
                width = maxWidth;
                height = options.outHeight;
            }

            Bitmap bitmap = extractThumbNail(filePath, width, height, false);
            if(bitmap == null) {
                LogUtil.e(TAG, "getSuitableBmp fail, temBmp is null, filePath = " + filePath);
                return null;
            }
            int degree = readPictureDegree(filePath);
            if(degree != 0) {
                bitmap = degreeBitmap(bitmap, degree);
            }
            return bitmap;
        } catch (Exception e) {
            LogUtil.e(TAG, "decode bitmap err: " + e.getMessage());
            return null;
        }
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     *
     * @param src
     * @param degree
     * @return
     */
    public static Bitmap degreeBitmap(Bitmap src , float degree) {
        if(degree == 0.0F) {
            return src;
        }
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree, src.getWidth() / 2, src.getHeight() / 2);
        Bitmap resultBitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        boolean filter = true;
        if(resultBitmap == null) {
            LogUtil.e(TAG, "resultBmp is null: ");
            filter = true;
        } else {
            filter = false;
        }

        if(resultBitmap != src) {
            src.recycle();
        }
        LogUtil.d(TAG, "filter: " + filter + "  degree:" + degree);
        return resultBitmap;
    }

    /**
     * 得到指定路径图片的options
     * @param srcPath
     * @return Options {@link BitmapFactory.Options}
     */
    public final static BitmapFactory.Options getBitmapOptions(String srcPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath);
        return options;
    }

    /**
     * 压缩发送到服务器的图片
     * @param origPath 原始图片路径
     * @param widthLimit 图片宽度限制
     * @param heightLimit 图片高度限制
     * @param format 图片格式
     * @param quality 图片压缩率
     * @param authorityDir 图片目录
     * @param outPath 图片详细目录
     * @return
     */
    public static boolean createThumbnailFromOrig(String origPath,
                                                  int widthLimit, int heightLimit, Bitmap.CompressFormat format,
                                                  int quality, String authorityDir, String outPath) {
        Bitmap bitmapThumbNail = extractThumbNail(origPath, widthLimit, heightLimit, false);
        if(bitmapThumbNail == null) {
            return false;
        }

        try {
            saveImageFile(bitmapThumbNail, quality, format, authorityDir, outPath);
            return true;
        } catch (IOException e) {
            LogUtil.e(TAG,  "create thumbnail from orig failed: " + outPath);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap extractThumbNail(final String path, final int width, final int height,  final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            LogUtil.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            LogUtil.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                options.inMutable = true;
            }
            LogUtil.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path);
            setInNativeAlloc(options);
            if (bm == null) {
                Log.e(TAG, "bitmap decode failed");
                return null;
            }

            LogUtil.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                LogUtil.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            LogUtil.e(TAG, "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }

    public static void saveImageFile(Bitmap bitmap , int quality ,Bitmap.CompressFormat format , String authorityDir, String outPath) throws IOException {
        if(!TextUtils.isEmpty(authorityDir) && !TextUtils.isEmpty(outPath)) {
            LogUtil.d(TAG, "saving to " + authorityDir + outPath);
            File file = new File(authorityDir);
            if(!file.exists()) {
                file.mkdirs();
            }
            File outfile = new File(file, outPath);
            outfile.createNewFile();

            try {
                FileOutputStream outputStream = new FileOutputStream(outfile);
                bitmap.compress(format, quality, outputStream);
                outputStream.flush();
            } catch (Exception e) {
                LogUtil.e(TAG, "saveImageFile fil=" + e.getMessage());
            }
        }
    }


    private static int mScreenWidth ;
    public static int getImageMinWidth(Context context) {
        if(mScreenWidth <= 0){
            mScreenWidth = DensityUtil.getImageWeidth(context, 1.0F)- DensityUtil.getDisplayMetrics(context, 40F);
            mScreenWidth = mScreenWidth / 4;
        }
        return mScreenWidth;
    }

    public static int getImageMinWidth2(Context context) {
        if(mScreenWidth <= 0){
            mScreenWidth = DensityUtil.getImageWeidth(context, 1.0F)- DensityUtil.getDisplayMetrics(context, 40F);
            mScreenWidth = mScreenWidth / 4;
        }
        return mScreenWidth;
    }

    /**
     * 获取图片被旋转的角度
     * @param filePath
     * @return
     */
    public static int getBitmapDegrees(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            LogUtil.d(TAG, "filePath is null or nil");
            return 0;
        }
        if(!new File(filePath).exists()) {
            LogUtil.d(TAG, "file not exist:" + filePath);
            return 0;
        }
        ExifInterface exifInterface = null;
        try {

            if(Integer.valueOf(Build.VERSION.SDK).intValue() >= 5) {
                exifInterface = new ExifInterface(filePath);
                int attributeInt = - 1;
                if(exifInterface != null) {
                    attributeInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                }

                if(attributeInt != -1) {
                    switch (attributeInt) {
                        case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                        case ExifInterface.ORIENTATION_TRANSPOSE:
                        case ExifInterface.ORIENTATION_TRANSVERSE:
                            return 0;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            return 180;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            return 90;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            return 270;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LogUtil.e(TAG,  "cannot read exif :" + e.getMessage());
        } finally {
            exifInterface = null;
        }
        return 0;
    }

    /**
     * 旋转图片
     * @param srcPath
     * @param degrees
     * @param format
     * @param root
     * @param fileName
     * @return
     */
    public static boolean rotateCreateBitmap(String srcPath , int degrees , Bitmap.CompressFormat format , String root , String fileName) {
        Bitmap decodeFile = BitmapFactory.decodeFile(srcPath);
        if(decodeFile == null) {
            LogUtil.e(TAG, "rotate: create bitmap fialed");
            return false;
        }
        int width = decodeFile.getWidth();
        int height = decodeFile.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, width / 2.0F, height / 2.0F);
        Bitmap createBitmap = Bitmap.createBitmap(decodeFile, 0, 0, width, height, matrix, true);
        decodeFile.recycle();
        try {
            saveImageFile(createBitmap, 60, format, root, fileName);
            return true;
        } catch (Exception e) {
            LogUtil.e(TAG,  "create thumbnail from orig failed: " + fileName);
        }
        return false;
    }

    /**
     * 生成一张缩略图
     * @param bitmap
     * @param paramFloat
     * @return
     */
    public static Bitmap processBitmap(Bitmap bitmap, float paramFloat) {
        Assert.assertNotNull(bitmap);
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        Rect localRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(localRect);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-4144960);
        canvas.drawRoundRect(rectF, paramFloat, paramFloat, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, localRect, localRect, paint);
        bitmap.recycle();
        return resultBitmap;
    }

    /**
     *
     * @param stream
     * @param dip
     * @return
     */
    public static Bitmap decodeStream(InputStream stream , float dip) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if(dip != 0.0F) {
            options.inDensity = (int)(160.0F * dip);
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        setInNativeAlloc(options);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null,options);
            return bitmap;
        } catch (OutOfMemoryError e) {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            setInNativeAlloc(options);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(stream, null,options);
                return bitmap;
            } catch (OutOfMemoryError e2) {
            }
        }
        return null;
    }

    public static String getLastText(String text) {
        if (text == null) {
            return null;
        }
        for (int i = text.length() - 1; i >= 0; --i) {
            int j = text.charAt(i);
            if ((j >= 19968) && (j <= 40869)) {
                return String.valueOf(j);
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public static Paint newPaint() {
        Paint paint = new Paint(1);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        return paint;
    }



    /*
     *
     */
    public static Bitmap newBitmap(int width, int height,Bitmap.Config config) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(width, height,config);
            return bitmap;
        } catch (Throwable localThrowable) {
            LogUtil.e(TAG, localThrowable.getMessage());
        }
        return null;
    }





    public static int compareVersion(String curVer, String serVer) {
        if (curVer.equals(serVer) || serVer == null) {
            return 0;
        }
        String[] version1Array = curVer.split("\\.");
        String[] version2Array = serVer.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen && (diff = Integer.parseInt(getStringNum(version1Array[index])) - Integer.parseInt(getStringNum(version2Array[index]))) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (i >=4 || Integer.parseInt(version1Array[i]) > 0) {
                    //  没有新版本
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    // 有新版本更新
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    private static String getStringNum(String str) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    public static int getScreenWidth(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     *
     * @param context
     * @param id
     * @return
     */
    public static Drawable getDrawables(Context context , int id) {
        Drawable drawable = getResources(context).getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        return drawable;
    }

    /**
     *
     * @Title: getResource
     * @Description: TODO
     * @param context
     * @return Resources
     */
    public static Resources getResources(Context context) {
        return context.getResources();
    }





}

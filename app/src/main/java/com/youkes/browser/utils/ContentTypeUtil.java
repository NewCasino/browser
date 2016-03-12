package com.youkes.browser.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuming on 2015/10/7.
 */
public class ContentTypeUtil {

    private static final Map<String, String> mimeMap;

    static {
        mimeMap=new HashMap<String, String>();
        mimeMap.put("jpg","image/jpeg");
        mimeMap.put("gif","image/gif");
        mimeMap.put("bmp","image/bmp");
        mimeMap.put("png","image/png");

    }

    private static final Map<String, String> extMap;

    static {
        extMap=new HashMap<String, String>();


        extMap.put("image/jpeg", ".jpg");
        extMap.put("image/gif", ".gif");
        extMap.put("image/bmp", ".bmp");
        extMap.put("image/png", ".png");
        extMap.put("image/cis-cod", ".cod");
        extMap.put("image/gif", ".gif");
        extMap.put("image/ief", ".ief");

        extMap.put("image/pipeg", ".jfif");
        extMap.put("image/svg+xml", ".svg");
        extMap.put("image/tiff", ".tif");
        extMap.put("image/x-cmu-raster", ".ras");
        extMap.put("image/x-cmx", ".cmx");
        extMap.put("image/x-icon", ".ico");
        extMap.put("image/x-portable-anymap", ".pnm");
        extMap.put("image/x-portable-bitmap", ".pbmv");
        extMap.put("image/x-portable-graymap", ".pgm");
        extMap.put("image/x-portable-pixmap", ".ppm");
        extMap.put("image/x-rgb", ".rgb");
        extMap.put("image/x-xbitmap", ".xbm");
        extMap.put("image/x-xpixmap", ".xpm");
        extMap.put("image/x-xwindowdump", ".xwd");

        extMap.put("video/mpeg",".mpeg");
        extMap.put("video/quicktime",".mov");
        extMap.put("video/x-la-asf",".lsf");
        extMap.put("video/x-la-asf",".lsx");
        extMap.put("video/x-ms-asf",".asf");
        extMap.put("video/x-ms-asf",".asr");
        extMap.put("video/x-ms-asf",".asx");
        extMap.put("video/x-msvideo",".avi");
        extMap.put("video/x-sgi-movie",".movie");
        extMap.put("video/mp4",".mp4");

    }


    public static String getExt(String contentType) {
        if(contentType==null){
            return "";
        }
        contentType=contentType.toLowerCase().trim().replaceAll(" +", " ");
        if(!extMap.containsKey(contentType)){
            return "";
        }

        String ext = extMap.get(contentType);
        return ext;


    }
}

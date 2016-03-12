
package com.youkes.browser.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.youkes.browser.R;
import com.youkes.browser.constant.Constants;
import com.youkes.browser.database.HistoryItem;
import com.youkes.browser.download.DownloadHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class LinkUtils {

	private static String[] goodsHttpStart = {

			"m.taobao.com",
			"m.tmall.com",
			"m.jd.com",//"item.m.jd.com",
			"www.amazon.cn",
			"m.vip.com",
			"m.dangdang.com",
			"m.suning.com",
			"m.yhd.com",
			"m.gome.com.cn",
			"m.jumei.com",
			"m.yixun.com",
			"m.lefeng.com",
			"www.womai.com",
			"m.newegg.cn",

	};


	private static String[] newsReg = {

			"m.taobao.com",


	};


	public static int getLinkType(String url) {
		for (String s : goodsHttpStart) {
			if (url.indexOf(s) > 0) {
				return LinkType.Type_Goods;
			}
		}


		return LinkType.Type_Unknown;
	}


}


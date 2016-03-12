
package com.youkes.browser.constant;

import com.youkes.browser.MainApp;
import com.youkes.browser.R;

public class BookmarkPage2 {

	public static final String FILENAME = "bookmarks.html";
	public static final String HEADING = "<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta content=\"en-us\" http-equiv=\"Content-Language\" /><meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\"><title>"
			+ MainApp.getContext().getString(R.string.action_bookmarks)
			+
			"</title></head>"
			+"<style>"+
			"body { background: #e1e1e1; max-width:100%; min-height:100%;}"+
			"#content {width:100%; max-width:800px; margin:0 auto; text-align:left;}"
			+".box { vertical-align:middle;" +
			"text-align:center;position:relative;" +
			" display: inline-block; " +
			"height: 64px; width: 64px; " +
			"margin: 10px; " +
			"background-color:#fff;" +
			"box-shadow: 0px 2px 3px rgba( 0, 0, 0, 0.25 );" +
			"font-family: Arial;" +
			"color: #444;" +
			"font-size: 12px;" +
			"-moz-border-radius: 2px;" +
			"-webkit-border-radius: 2px;border-radius: 2px;}" +
			".stuff {height: 64px; width: 64px;vertical-align:middle;text-align:center; display: table-cell;}" +
			"p.ellipses {width:60px; white-space: nowrap; " +
			"overflow: hidden;text-align:center;margin:auto;" +
			" text-overflow: ellipsis; " +
			"-o-text-overflow: ellipsis;" +
			" -ms-text-overflow: ellipsis;}" +
			".box a { width: 100%; height: 100%; position: absolute; left: 0; top: 0;}" +
			"</style><body> <div id=\"content\">";

	public static final String PART1 = "<div class=\"box\"><a href=\"";
	public static final String PART2 = "\" ></a><div class=\"stuff\" >";
	public static final String PART3 = "<p class=\"ellipses\">";
	public static final String PART4 = "</p></div></div>";
	public static final String END = "</div></body></html>";

}


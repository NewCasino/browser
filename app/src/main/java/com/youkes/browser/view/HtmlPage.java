package com.youkes.browser.view;

import com.youkes.browser.utils.LinkType;
import com.youkes.browser.utils.URLUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by xuming on 2015/10/27.
 */
public class HtmlPage {
    public String getHtml() {
        return html;
    }
    public static class HtmlRule{
        public HtmlRule(String urlPatten,String titleSelector, String imgSelector,int type) {
            this.titleSelector = titleSelector;
            this.urlPatten = urlPatten;
            this.imgSelector = imgSelector;
            this.type = type;
        }

        public String urlPatten="";
        public String titleSelector="";
        public String imgSelector="";
        public int type= LinkType.Type_Unknown;

    }

    private static HashMap<String,HtmlRule> hashMap=new HashMap<String,HtmlRule>();
    private static  boolean inited=false;
    static void init() {
        if (inited) {
            return;
        }
        //Pattern p = Pattern.compile("(mouse|cat|dog|wolf|bear|human)");
        hashMap.put("view.inews.qq.com", new HtmlRule("^https?://view.inews.qq.com/a/.+", "#content p.title", "#content img",LinkType.Type_News));
        inited = true;
    }

    private String title="";
    private String img="";
    private  int type=LinkType.Type_Unknown;
    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }
    public static int getLinkType(String url) {
        init();
        String host = URLUtil.getHostOnly(url);
        if (hashMap.containsKey(host)) {
            HtmlRule rule = hashMap.get(host);
            Pattern pattern = Pattern.compile(rule.urlPatten);
            Matcher mc = pattern.matcher(url);
            if (mc.matches()) {
                return rule.type;
            }

        }


        return LinkType.Type_Unknown;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url="";
    private String html="";
    public HtmlPage(String url,String html){
        this.url=url;
        this.html=html;
        parseHtml(url,html);
    }

    private void parseHtml(String url,String html) {
        init();
        String host = URLUtil.getHostOnly(url);
        Document doc = Jsoup.parse(html);
        if (hashMap.containsKey(host)) {

            HtmlRule rule = hashMap.get(host);
            Pattern pattern = Pattern.compile(rule.urlPatten);
            Matcher mc = pattern.matcher(url);
            if (mc.matches()) {

                title = doc.select(rule.titleSelector).first().text();
                img = doc.select(rule.imgSelector).first().attr("src");
                type=rule.type;
                //ToastUtil.showMessage(host + ":" + title);
                return;
            }

        }

        title = doc.select("title").text();
        img = "";
        type = LinkType.Type_Unknown;
        //ToastUtil.showMessage(host + ":" + title);
    }


}

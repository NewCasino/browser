package com.youkes.browser.view;

/**
 * Created by xuming on 2015/11/21.
 */
public class BrowseHtmlParser {

    public static HtmlPage getPage() {
        return page;
    }

    private static HtmlPage page;
    public static void setPage(HtmlPage page) {
        BrowseHtmlParser.page = page;
    }


}

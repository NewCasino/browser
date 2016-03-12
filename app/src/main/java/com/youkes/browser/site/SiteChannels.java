package com.youkes.browser.site;

import com.youkes.browser.ChannelItem;

import java.util.ArrayList;

/**
 * Created by xuming on 2016/2/17.
 */
public class SiteChannels {
        public static ArrayList<ChannelItem> channels= new ArrayList<ChannelItem>();
        static {

            channels.add(new ChannelItem("","1", "历史", 1, 1));

            channels.add(new ChannelItem("","1", "热门", 1, 1));
            channels.add(new ChannelItem("","2", "新闻", 2, 1));
            channels.add(new ChannelItem("","3", "视频", 3, 1));
            channels.add(new ChannelItem("","5", "音乐", 5, 0));
            channels.add(new ChannelItem("","6", "图片", 6, 1));
            channels.add(new ChannelItem("","7", "小说", 7, 0));
            channels.add(new ChannelItem("","8", "漫画", 8, 0));
            channels.add(new ChannelItem("","9", "体育", 9, 0));
            channels.add(new ChannelItem("","10", "汽车", 10, 1));
            channels.add(new ChannelItem("","11", "财经", 11, 1));
            //channels.add(new ChannelItem("","12", "社交", 12, 1));
            //channels.add(new ChannelItem("","13", "生活", 13, 1));
            channels.add(new ChannelItem("","14", "购物", 14, 1));
            channels.add(new ChannelItem("","15", "游戏", 15, 1));
            channels.add(new ChannelItem("","16", "时尚", 16, 1));
        }
}

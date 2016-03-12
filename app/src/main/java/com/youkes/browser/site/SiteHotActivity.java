package com.youkes.browser.site;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.youkes.browser.ChannelItem;
import com.youkes.browser.R;
import com.youkes.browser.activity.AppViewPagerActivity;
import com.youkes.browser.history.UserHistoryListFragment;

import java.util.ArrayList;


public class SiteHotActivity extends AppViewPagerActivity {

	
	@Override
	public String getTitleString() {
		return getString(R.string.history);
	}

	@Override
	public ArrayList<String> getMenuList() {
		ArrayList<String> menus = new ArrayList<>();
		return menus;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}



	protected ArrayList<ChannelItem> getUserChannelLists(){
		return SiteChannels.channels;
	}


	protected Fragment initFragment(String channelName) {
		if(channelName.equals("历史")){
			UserHistoryListFragment f = new UserHistoryListFragment();
			return f;
		}

		if(!channelName.equals("热门")){
			SiteHotListFragment f = new SiteHotListFragment();
			f.setQueryTag(channelName);
			return f;
		}
		SiteHotListFragment f = new SiteHotListFragment();
		return f;
	}


	

}

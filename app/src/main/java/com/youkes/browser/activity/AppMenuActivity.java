package com.youkes.browser.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;


import com.youkes.browser.R;
import com.youkes.browser.utils.ToastUtil;

import java.util.ArrayList;

public class AppMenuActivity extends AppBaseActivity implements OnClickListener {

	public String getTitleString() {
		return "";
	}


	public boolean hasMenu() {
		return true;
	}

	public boolean hasBackBtn() {
		return true;
	}

	public boolean hasSearch() {
		return false;
	}

	public boolean hasLeftTextBtn() {
		return false;
	}

	public boolean hasTitleIcon() {
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int backResId = -1;
		if (hasBackBtn()) {
			backResId = R.drawable.topbar_back_bt;
		}
		int menuResId = -1;
		if (hasMenu()) {
			menuResId = R.drawable.icon_topbar_add;
		}


		if (getMenuList() == null || getMenuList().size() == 0) {
			menuResId = -1;
		}


		getTopBarView().showSearch(hasSearch());

		getTopBarView().showTitleIcon(hasTitleIcon());
		getTopBarView().showLeftTextBtn(hasLeftTextBtn());
		if (hasTitleIcon() && getIcon() != null && !getIcon().equals("")) {
			getTopBarView().displayIcon(getIcon());
		} else {
			getTopBarView().hideIcon();
		}

		getTopBarView().setTopBarToStatus(1, backResId,
				menuResId, getTitleString(), this);

		mOverflowHelper = new OverflowHelper(this);
		initOverflowItems();

		getTopBarView().invalidate();
	}


	public void reloadMenu() {
		mOverflowHelper = new OverflowHelper(this);
		initOverflowItems();

	}


	private OverflowHelper mOverflowHelper;

	private void controlPlusSubMenu() {
		if (mOverflowHelper == null) {
			return;
		}

		if (mOverflowHelper.isOverflowShowing()) {
			mOverflowHelper.dismiss();
			return;
		}

		mOverflowHelper.setOverflowItems(mItems);
		if (menuListener != null) {
			mOverflowHelper.setOnOverflowItemClickListener(menuListener);
			mOverflowHelper.showAsDropDown(findViewById(R.id.btn_right));
		}
	}

	private AdapterView.OnItemClickListener menuListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			controlPlusSubMenu();
			onMenuClick(position);
		}

	};

	private OverflowAdapter.OverflowItem[] mItems = null;

	public ArrayList<String> getMenuList() {
		ArrayList<String> menus = new ArrayList<>();
		return menus;
	}

	protected void onMenuClick(int position) {

	}

	void initOverflowItems() {

		ArrayList<String> menus = getMenuList();
		if (mItems == null) {
			int siz = menus.size();
			if (siz == 0) {
				return;
			}
			mItems = new OverflowAdapter.OverflowItem[siz];
		}
		for (int i = 0; i < mItems.length; i++) {
			mItems[i] = new OverflowAdapter.OverflowItem(menus.get(i));
		}

	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_container;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_left:
				onBackPressed();
				break;

			case R.id.btn_right:
				hideSoftKeyboard();
				controlPlusSubMenu();
				break;

			case R.id.btn_search:
				hideSoftKeyboard();
				onSearchClick();
				break;


		}
	}

	public void onBackPressed() {
		hideSoftKeyboard();
		finish();
	}

	public void onSearchClick(){
		ToastUtil.showMessage("");
	}

	public String getIcon() {
		return "";
	}
}

package com.youkes.browser.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.youkes.browser.MainApp;
import com.youkes.browser.R;
import com.youkes.browser.constant.Constants;
import com.youkes.browser.database.HistoryDatabase;
import com.youkes.browser.database.HistoryItem;
import com.youkes.browser.preference.PreferenceUtils;
import com.youkes.browser.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.HashMap;


public class UserHistoryListFragment extends Fragment {

	protected SwipeRefreshLayout swipeLayout;
	private SwipeListView listView = null;
	private UserHistoryListItemAdapter listAdapter = null;
	TextView emptyView=null;

	private String type="";

	public UserHistoryListFragment() {

	}


	HashMap<String, String> queryMap = new HashMap<String, String>();

	public void loadFirstPage() {
		pageIdx = 0;
		clear();

		HistoryDatabase databaseHandler = HistoryDatabase.getInstance(MainApp.getContext());
		ArrayList<HistoryItem> items=databaseHandler.getLastItems(PreferenceUtils.getUserId(),pageIdx, 24);
		if(items.size()==0){
			return;
		}

		onQueryCompleted(items);



	}

	private void loadItems() {
		HistoryDatabase databaseHandler = HistoryDatabase.getInstance(MainApp.getContext());
		ArrayList<HistoryItem> items=databaseHandler.getLastItems(PreferenceUtils.getUserId(),pageIdx, 24);
		if(items.size()==0){
			return;
		}

		onQueryCompleted(items);
	}

	boolean loadFinished=false;
	protected void onQueryCompleted(ArrayList<HistoryItem> list) {

		if(getActivity()==null){
			return;
		}

		if(list==null||list.size()==0){
			if(list==null){
				//ToastUtil.showMessage(getString(R.string.connect_server_error));
				emptyView.setText(getString(R.string.error_request_pull_refresh));
				if(listAdapter!=null&&listAdapter.getCount()==0) {emptyView.setVisibility(View.VISIBLE);}

			}else{
				//ToastUtil.showMessage(getString(R.string.connect_server_error));
				emptyView.setText(getString(R.string.main_empty_result));
				if(listAdapter!=null&&listAdapter.getCount()==0) {emptyView.setVisibility(View.VISIBLE);}
			}

			loadFinished=true;
			listView.onBottomComplete();
			listView.setHasMore(false);
			swipeLayout.setRefreshing(false);
			return ;
		}

		emptyView.setVisibility(View.GONE);
		listAdapter.addList(list);
		listAdapter.notifyDataSetChanged();
		listView.onBottomComplete();
		swipeLayout.setRefreshing(false);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater
				.inflate(R.layout.activity_circle, container, false);

		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		listView = (SwipeListView) v
				.findViewById(R.id.listview);

		emptyView = (TextView)v.findViewById(R.id.empty_tv);
		emptyView.setVisibility(View.GONE);
		listAdapter = new UserHistoryListItemAdapter(this.getActivity());
		listView.setAdapter(listAdapter);

		listView.setOnBottomListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onLoadMoreStart();
			}
		});

		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						onRefreshStart();
					}
				}, 500);
			}
		});


		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {

				onItemClickStart(arg0, arg1, arg2, arg3);
			}
		});


		loadFirstPage();

		return v;
	}



	protected void onItemClickStart(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {

		HistoryItem item = this.getItem(pos);
		if (item== null) {
			return;
		}
		Intent result = new Intent();
		result.putExtra(Constants.EXTRA_ID_NEW_TAB, false);
		result.putExtra(Constants.EXTRA_ID_URL, item.getUrl());
		if (getActivity().getParent() != null) {
			getActivity().getParent().setResult(Activity.RESULT_OK, result);
		} else {
			getActivity().setResult(Activity.RESULT_OK, result);
		}

		getActivity().finish();

	}

	int pageIdx = 0;

	void onRefreshStart() {
		pageIdx = 0;
		loadFinished=false;
		clear();
		loadItems();
	}

	void onLoadMoreStart() {
		if(loadFinished){
			listView.onBottomComplete();
			return;
		}
		pageIdx++;
		queryMap.put("p", "" + pageIdx);
		loadItems();
	}

	public void clear() {
		if (listAdapter != null) {
			listAdapter.clear();
			listAdapter.notifyDataSetChanged();
		}

	}

	public HistoryItem getItem(int position) {
		int hcnt = listView.getHeaderViewsCount();

		HistoryItem item = (HistoryItem) listAdapter.getItem(position - hcnt);
		return item;
	}

	public void setType(String type) {
		this.type = type;
	}


}

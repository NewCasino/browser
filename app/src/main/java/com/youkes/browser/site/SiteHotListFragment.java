package com.youkes.browser.site;

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

import com.youkes.browser.R;
import com.youkes.browser.constant.Constants;

import com.youkes.browser.http.OnTaskCompleted;
import com.youkes.browser.swipelistview.SwipeListView;


import java.util.ArrayList;
import java.util.HashMap;


public class SiteHotListFragment extends Fragment {

	protected SwipeRefreshLayout swipeLayout;
	private SwipeListView listView = null;
	private SiteListItemAdapter listAdapter = null;
	TextView emptyView=null;

	private String type="";

	public SiteHotListFragment() {

	}

	private String queryTag="";
	HashMap<String, String> queryMap = new HashMap<String, String>();

	public void loadFirstPage() {
		pageIdx = 0;
		clear();


		SiteHotApi.getInstance().query(queryTag,pageIdx,new OnTaskCompleted() {

			@Override
			public void onTaskCompleted(String result) {
				onQueryCompleted(result);

			}

		});

	}

	private void loadItems() {
		SiteHotApi.getInstance().query(queryTag, pageIdx, new OnTaskCompleted() {

			@Override
			public void onTaskCompleted(String result) {
				onQueryCompleted(result);

			}

		});
	}

	boolean loadFinished=false;
	protected void onQueryCompleted(String result) {

		if(getActivity()==null){
			return;
		}
		ArrayList<SiteHotListItem> list = SiteHotListParser.parse(result);

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
		listAdapter = new SiteListItemAdapter(this.getActivity());
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


		search("");

		return v;
	}



	protected void onItemClickStart(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {

		SiteHotListItem item = this.getItem(pos);
		if (item== null) {
			return;
		}
		Intent result = new Intent();
		result.putExtra(Constants.EXTRA_ID_NEW_TAB, false);
		result.putExtra(Constants.EXTRA_ID_URL, item.getSite());
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

	public SiteHotListItem getItem(int position) {
		int hcnt = listView.getHeaderViewsCount();

		SiteHotListItem item = (SiteHotListItem) listAdapter.getItem(position - hcnt);
		return item;
	}

	boolean searching = false;

	public void search(String text) {
		// pageIdx = 0;
		clear();
		pageIdx = 0;

		if (searching) {
			return;
		}

		if(listView==null){
			return;
		}
		searching = true;
		SiteHotApi.getInstance().query(queryTag,pageIdx, new OnTaskCompleted() {

			@Override
			public void onTaskCompleted(String result) {
				searching = false;
				onQueryCompleted(result);

			}

		});
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setQueryTag(String queryTag) {
		this.queryTag = queryTag;
	}
}

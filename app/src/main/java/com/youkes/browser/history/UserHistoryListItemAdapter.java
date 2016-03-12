package com.youkes.browser.history;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.youkes.browser.R;
import com.youkes.browser.database.HistoryItem;

import java.util.ArrayList;


public class UserHistoryListItemAdapter extends BaseAdapter {

	static int resId=R.layout.item_list_site;
	private ArrayList<HistoryItem> list=null;
	private Activity activity=null;

	public UserHistoryListItemAdapter(Activity activity){
		this.activity=activity;
		this.list=new ArrayList<HistoryItem>();
	}


	@Override
	public int getCount() {
		if(list==null){
			return 0;
		}
		return list.size();
	}


	public void addItem(HistoryItem item) {
		list.add(item);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final UserHistoryListItemViewHolder holder;

		HistoryItem item=list.get(position);


		if (convertView == null) {

			convertView = activity.getLayoutInflater().inflate(resId, parent, false);
			holder=new UserHistoryListItemViewHolder(activity ,convertView);

			convertView.setTag(holder);

		} else {
			holder = (UserHistoryListItemViewHolder) convertView.getTag();
		}


		HistoryItem oldItem = holder.getItem();
		if (oldItem != null && oldItem.getUrl().equals(item.getUrl())) {

		} else {
			holder.setItem(item);
		}



		return convertView;


	}


	public void addList(ArrayList<HistoryItem> slist) {

		for(HistoryItem item:slist){
			list.add(item);
		}

	}


	public void clear() {
		list.clear();
		this.notifyDataSetChanged();
	}


	public HistoryItem getItem(int position) {
		return list.get(position);
		
	}
}

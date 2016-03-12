package com.youkes.browser.site;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.youkes.browser.R;


import java.util.ArrayList;


public class SiteListItemAdapter extends BaseAdapter {

	static int resId=R.layout.item_list_site;
	private ArrayList<SiteHotListItem> list=null;
	private Activity activity=null;

	public SiteListItemAdapter(Activity activity){
		this.activity=activity;
		this.list=new ArrayList<SiteHotListItem>();
	}


	@Override
	public int getCount() {
		if(list==null){
			return 0;
		}
		return list.size();
	}


	public void addItem(SiteHotListItem item) {
		list.add(item);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final SiteListItemViewHolder holder;

		SiteHotListItem item=list.get(position);


		if (convertView == null) {

			convertView = activity.getLayoutInflater().inflate(resId, parent, false);
			holder=new SiteListItemViewHolder(activity ,convertView);

			convertView.setTag(holder);

		} else {
			holder = (SiteListItemViewHolder) convertView.getTag();
		}


		SiteHotListItem oldItem = holder.getItem();
		if (oldItem != null && oldItem.get_id().equals(item.get_id())) {

		} else {
			holder.setItem(item);
		}



		return convertView;


	}


	public void addList(ArrayList<SiteHotListItem> slist) {

		for(SiteHotListItem item:slist){
			list.add(item);
		}

	}


	public void clear() {
		list.clear();
		this.notifyDataSetChanged();
	}


	public SiteHotListItem getItem(int position) {
		return list.get(position);
		
	}
}

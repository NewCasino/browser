package com.youkes.browser.history;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youkes.browser.R;
import com.youkes.browser.database.HistoryItem;
import com.youkes.browser.utils.GlideUtil;


public class UserHistoryListItemViewHolder {

	public TextView titleView = null;
	public TextView textView = null;
	public TextView tagsView = null;
	public ImageView imageView = null;


	Activity activity = null;

	public UserHistoryListItemViewHolder(Activity act, View view) {
		this.activity = act;
		View imgView = view.findViewById(R.id.image);
		if (imgView != null) {
			this.imageView = (ImageView) imgView;
		}

		this.titleView = (TextView) view.findViewById(R.id.title);
		this.tagsView = (TextView) view.findViewById(R.id.tags);
		this.textView = (TextView) view.findViewById(R.id.text);

	}


	HistoryItem currentItem = null;
	public void setItem(HistoryItem item) {
		this.currentItem = item;
		String title = item.getTitle();

		if (title.length() > 64) {
			title = title.substring(0, 64);
		}

		String text = item.getUrl();
		if (text != null) {
			text = Html.fromHtml(text).toString();
			text = text.replaceAll("\\s+", "").trim();
		}

		if (text != null && text.length() > 80) {

			text = text.substring(0, 80);
		}

		this.titleView.setText(title);

		if (text == null || text.equals("")) {
			this.textView.setText("");
			textView.setVisibility(View.GONE);

		} else {
			this.textView.setText(text);
			textView.setVisibility(View.VISIBLE);
		}

		tagsView.setVisibility(View.GONE);
		imageView.setVisibility(View.GONE);

	}

	public HistoryItem getItem() {
		return this.currentItem;
	}

}

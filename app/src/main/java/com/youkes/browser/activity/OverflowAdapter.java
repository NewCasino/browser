package com.youkes.browser.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youkes.browser.R;

public class OverflowAdapter extends CommAdapter {

    private Context mContext;
    /**布局加载器*/
    private LayoutInflater mLayoutInflater;
    /**下拉菜单*/
    private OverflowHelper mHelper;
    /**下拉菜单需要显示的数据*/
    private OverflowItem[] mItems;

    /**
     * 构造方法
     * @param helper
     * @param ctx
     */
    public OverflowAdapter(OverflowHelper helper , Context ctx) {
        mHelper = helper;
        mContext = ctx;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 创建一个新的View 并且设置隐藏
     * @return
     */
    private View createView() {
        View view = new View(mContext);
        view.setVisibility(View.GONE);
        return view;
    }

    /**
     * 设置下拉菜单选项图标是否可见
     * @param imageView 图标
     * @param visible
     */
    private void setViewVisibility(ImageView imageView ,boolean visible) {
        imageView.setVisibility(visible?View.VISIBLE:View.GONE);
    }

    /**
     * 设置当前Item是否可用
     * @param enabled
     * @param textView
     * @param parent
     */
    private void setItemEnabled(boolean enabled , TextView textView , ViewGroup parent){
        if(enabled) {
            textView.setTextColor(mHelper.getNormalColor());
            setViewPadding(parent, R.drawable.common_popup_menu_item);
        } else {
            textView.setTextColor(mHelper.getDisabledColor());
            setViewPadding(parent, R.drawable.transparent);
        }
    }

    /**
     * 设置菜单显示的数据
     * @param items
     */
    public void setOverflowItem(OverflowItem[] items) {
        if(items == null || items.length <= 0) {
            return ;
        }
        this.mItems = items;
    }

    @Override
    public int getCount() {
        if(mItems == null) {
            return 0;
        }
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        if(mItems == null || mItems.length <= position)  	{
            return null;
        }
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(isNullorLinearLayout(convertView)) {
            if(!isPositionNullData(position)) {
                convertView = null;
            }
        }
        if(!isPositionNullData(position)) {
            return super.getView(position, convertView, parent);
        }
        return createView();


    }

    /**
     * 初始化Holder
     * @param convertView
     */
    private void findViewId(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.icon = (ImageView) convertView.findViewById(R.id.popup_menu_item_left_image);
        holder.title = (TextView) convertView.findViewById(R.id.popup_menu_item_name);
        holder.root = (ViewGroup) convertView.findViewById(R.id.popup_container);
        holder.point = (ImageView) convertView.findViewById(R.id.popup_menu_image_red);
        convertView.setTag(holder);
    }

    @Override
    protected View buildViewByType(int position, ViewGroup parent,
                                   int itemViewType) {
        View itemView = mLayoutInflater.inflate(R.layout.comm_popup_menu_item, parent, false);
        findViewId(itemView);
        return itemView;
    }

    @Override
    protected void bindData(View convertView, int position, int itemViewType) {
        OverflowItem overflowItem = mItems[position];
        if(overflowItem == null) {
            return ;
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.title.setText(overflowItem.title);
        if(overflowItem.getIcon() > 0) {
            holder.icon.setImageResource(overflowItem.getIcon());
        }
        setViewVisibility(holder.point, false);
        setItemEnabled(overflowItem.enabled, holder.title, holder.root);
    }

    /**
     * 当前位置显示的数据是否为空
     * @param position
     * @return
     */
    private boolean isPositionNullData(int position) {
        return mItems[position] == null;
    }

    /**
     * 布局是否为空或者为LinearLayout
     * @param convertView
     * @return
     */
    private boolean isNullorLinearLayout(View convertView) {
        return (convertView == null) || (convertView instanceof LinearLayout);
    }

    /**
     * 调整位置Padding
     * @param parent
     * @param background
     */
    private void setViewPadding(ViewGroup parent, int background) {
        int[] rect = new int[4];
        rect[0] = parent.getPaddingLeft();
        rect[1] = parent.getPaddingTop();
        rect[2] = parent.getPaddingRight();
        rect[3] = parent.getPaddingBottom();
        parent.setBackgroundResource(background);
        parent.setPadding(rect[0], rect[1], rect[2], rect[3]);
    }

    public class ViewHolder {
        /**图标*/
        public ImageView icon = null;
        /**标题*/
        public TextView title = null;
        /**跟布局*/
        public ViewGroup root = null;
        /**是否有更新*/
        public ImageView point = null;
    }

    /**
     * 菜单Item
     */
    public static class OverflowItem {
        public String title;
        public int icon;
        protected boolean enabled = true;
        protected boolean showUpdate = false;

        public OverflowItem(String title) {
            this.title = title;
            enabled = true;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int resid) {
            icon = resid;
        }


    }

}

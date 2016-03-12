package com.youkes.browser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.youkes.browser.R;

import java.util.Arrays;
import java.util.List;


public class ECListDialog extends ECAlertDialog implements AdapterView.OnItemClickListener {

    private int mCheckIndex;
    private ListView mListView;
    private OnDialogItemClickListener mListener;
    /**
     * @param context
     */
    public ECListDialog(Context context) {
        super(context);
        mCheckIndex = -1;
        mListener = null;
        mListView = null;
        // setTitleNormalColor();
        View contatinView = LayoutInflater.from(context).inflate(R.layout.include_dialog_simplelist ,null);
        setContentView(contatinView);
        setContentPadding(0,0,-1,-1);
        mListView = (ListView) contatinView.findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);
    }

    /**
     *据数组资源文件创建\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     * @param context
     * @param resourceIdArray
     */
    public ECListDialog(Context context , int resourceIdArray) {
        this(context);
        String[] stringArray = context.getResources().getStringArray(resourceIdArray);
        setAdapter(new ListDialogAdapter(getContext(), Arrays.asList(stringArray)));
    }

    /**
     * 根据集合数组创建
     * @param context
     * @param strs
     */
    public ECListDialog(Context context , List<String> strs) {
        this(context);
        setAdapter(new ListDialogAdapter(getContext(), strs));
    }

    public ECListDialog(Context context , List<String> strs  , int checkPosition) {
        this(context ,strs);
        this.mCheckIndex = checkPosition;

    }

    public ECListDialog(Context context , String[] strs , int checkPosition) {
        this(context ,strs);
        this.mCheckIndex = checkPosition;
    }

    public ECListDialog(Context context , String[] strs) {
        this(context);
        setAdapter(new ListDialogAdapter(getContext(), Arrays.asList(strs)));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mListener != null) {
            mListener.onDialogItemClick(this , position);
        }
        dismiss();
    }

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }



    public class ListDialogAdapter extends IBaseAdapter<String> {


        public ListDialogAdapter(Context ctx, List<String> data) {
            super(ctx, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = this.mLayoutInflater.inflate(R.layout.listitem_dialog , null);
            }
            ((TextView) convertView.findViewById(R.id.textview)).setText(getItem(position).toString());
            if(mCheckIndex == position) {
                convertView.findViewById(R.id.imageview).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.imageview).setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    public void setOnDialogItemClickListener(OnDialogItemClickListener l) {
        this.mListener = l;
    }

    public interface OnDialogItemClickListener {
        void onDialogItemClick(Dialog d, int position);
    }
}

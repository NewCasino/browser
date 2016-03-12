
package com.youkes.browser.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;



public class CCPEditText extends EditText {

    public InputConnection miInputConnection;

    /**
     * @param context
     */
    public CCPEditText(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CCPEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CCPEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

        miInputConnection = super.onCreateInputConnection(outAttrs);
        return miInputConnection;
    }

    public InputConnection getInputConnection() {
        return miInputConnection;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean onTextContextMenuItem = super.onTextContextMenuItem(id);
        if(id == android.R.id.paste) {
            // Gets the position of the cursor
            int selectionStart = getSelectionStart();
            setText(/*EmoticonUtil.emoji2CharSequence(getContext(), */getText()/*.toString(), (int) getTextSize(), false)*/);
            setSelection(selectionStart);
        }

        return onTextContextMenuItem;

    }




    public int getTextSelection(String text , int position) {

        if(TextUtils.isEmpty(text)){
            return position;
        }
        return 0;
    }

    @Override
    public Bundle getInputExtras(boolean create) {
        return super.getInputExtras(create);
    }
}

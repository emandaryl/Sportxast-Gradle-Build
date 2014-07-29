package com.sportxast.SportXast.models;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.sportxast.SportXast.interfaces.EditTextImeBackListener;


public class EditTextBackEvent extends EditText
{
	 
	private Context context_;
    private EditTextImeBackListener mOnImeBack;

    public EditTextBackEvent(Context context) {
    	
        super(context);
    }

    public EditTextBackEvent(Context context, AttributeSet attrs) {
    	
        super(context, attrs);
    }

    public EditTextBackEvent(Context context, AttributeSet attrs, int defStyle) {
    	
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
        	   
            if (mOnImeBack != null) 
              mOnImeBack.onImeBack(this, this.getText().toString(), keyCode, event);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
        mOnImeBack = listener;
    }
	
	/*
    //contructor 1 - very important
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //contructor 2 - very important
    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	*/
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode==KeyEvent.KEYCODE_ENTER) 
        {
            // Just ignore the [Enter] key
            return true;
        }
        // Handle all other keys in the default way
        return super.onKeyDown(keyCode, event);
    }
	 
} 
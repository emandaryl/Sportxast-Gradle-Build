package com.sportxast.SportXast.thirdparty_class;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class EditTextBackKeyEvent extends EditText{

	
	View commentView ;
	
	public EditTextBackKeyEvent(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
    public EditTextBackKeyEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextBackKeyEvent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	
    	  if (keyCode == KeyEvent.KEYCODE_BACK) {
    		  try {
    			  commentView.setVisibility(View.GONE);
    		  } catch (Exception e) {
    			  e.printStackTrace();
    		  }
    	}
    	
    	return super.onKeyPreIme(keyCode, event);
    }
    
    public void initCommentView(View commentView){
    	this.commentView = commentView;
    }
    
    public View getCommentView() {
    	return this.commentView;
    }
}

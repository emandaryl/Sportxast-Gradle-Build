package com.sportxast.SportXast.activities2_0; 

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;


public class MyTextView extends EditText
{
    /**contructor 1 - very important**/
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**contructor 2 - very important**/   
    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

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

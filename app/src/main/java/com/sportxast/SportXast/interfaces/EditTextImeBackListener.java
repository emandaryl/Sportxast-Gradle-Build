package com.sportxast.SportXast.interfaces;

import android.view.KeyEvent;

import com.sportxast.SportXast.models.EditTextBackEvent;

public interface EditTextImeBackListener {
    public abstract void onImeBack(EditTextBackEvent ctrl, String text, int keyCode, KeyEvent event);
}

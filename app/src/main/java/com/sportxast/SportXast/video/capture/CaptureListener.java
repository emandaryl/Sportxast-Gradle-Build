package com.sportxast.SportXast.video.capture;

import android.graphics.Bitmap;
import android.widget.FrameLayout;

import com.sportxast.SportXast.models._MediaStorage;

public interface CaptureListener {
	public void setSnapshot(Bitmap snapshot);

	public void videoProcessComplete(String basename, String basename2);
	public void startRecording();

	public FrameLayout getCameraPreviewFrame();

	public void onDeleteCapture(_MediaStorage capture);

	public void onCommentAdd(_MediaStorage capture);

	public void onFavoriteSuccess(_MediaStorage capture);

	public void onTagSuccess(_MediaStorage capture);

	public void hideKeyboard();
	public void updateCurrentCapture();
}
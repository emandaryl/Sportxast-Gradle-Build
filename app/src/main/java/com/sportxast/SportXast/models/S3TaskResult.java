package com.sportxast.SportXast.models;

import android.net.Uri;

public class S3TaskResult {
	String errorMessage = null;
	Uri uri = null;
	String uploadResponse = null;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public void setUploadresponse(String string) {
		// TODO Auto-generated method stub
		uploadResponse = string;
	}
	public String getUploadresponse() {
		// TODO Auto-generated method stub
		return uploadResponse;
	}
}

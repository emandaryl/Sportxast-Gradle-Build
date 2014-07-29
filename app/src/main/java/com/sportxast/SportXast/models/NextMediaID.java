package com.sportxast.SportXast.models;

public class NextMediaID {
	
	private String uploadQueue = "0";
//########################################		
	private String shareUrl;
	private String mediaId;
	private String shareText;
	private String added;
	private String imageServerPath;
	private String videoServerPath;
//########################################	
	
//########################################
	private String eventId;
	//private String coverImage;
	private String largeImageUrl;
	private String videoLocalPath; 
	private String imageLocalPath; 
	
	private String imageLocalFilename;
	private String videoLocalFilename;
//########################################	
	
	private String userLatitude;
	private String userLongitude;
	
	private String mediaDateShortFormat;
	private String mediaDateLongFormat; 
	
	private String videoUploadResponseHeaders;
	private String imageUploadResponseHeaders;
	
	private String twitterCardUrl;
	
	
//########################################
//########################################	 
	public void setImageUploadResponseHeaders(final String imageUploadResponseHeaders) {
		this.imageUploadResponseHeaders = imageUploadResponseHeaders;
	} 
	public String getImageUploadResponseHeaders() {
		return this.imageUploadResponseHeaders;
	} 
//########################################	
	public void setVideoUploadResponseHeaders(final String videoUploadResponseHeaders) {
		this.videoUploadResponseHeaders = videoUploadResponseHeaders;
	} 
	public String getVideoUploadResponseHeaders() {
		return this.videoUploadResponseHeaders;
	} 
//########################################	
	public void setTwitterCardUrl(final String twitterCardUrl) {
		this.twitterCardUrl = twitterCardUrl;
	} 
	public String getTwitterCardUrl() {
		return this.twitterCardUrl;
	} 
//########################################	
	public void setUploadQueue(final String uploadQueue) {
		this.uploadQueue = uploadQueue;
	} 
	public String getUploadQueue() {
		return this.uploadQueue;
	} 
//#############################################
	public void setUserLatitude(final String userLatitude) {
		this.userLatitude = userLatitude;
	} 
	public String getUserLatitude() {
		return this.userLatitude;
	} 
//#############################################
	public void setUserLongitude(final String userLongitude) {
		this.userLongitude = userLongitude;
	} 
	public String getUserLongitude() {
		return this.userLongitude;
	} 
//#############################################
	
	public void setMediaDateShortFormat(final String mediaDateShortFormat) {
		this.mediaDateShortFormat = mediaDateShortFormat;
	} 
	public String getMediaDateShortFormat() {
		return this.mediaDateShortFormat;
	} 
//#############################################
	public void setMediaDateLongFormat(final String mediaDateLongFormat) {
		this.mediaDateLongFormat = mediaDateLongFormat;
	} 
	public String getMediaDateLongFormat() {
		return this.mediaDateLongFormat;
	} 
//#############################################
	
	public void setVideoLocalFilename(final String videoLocalFilename) {
		this.videoLocalFilename = videoLocalFilename;
	} 
	public String getVideoLocalFilename() {
		return this.videoLocalFilename;
	} 
//#############################################
	 
	public void setShareUrl(final String shareUrl) {
		this.shareUrl = shareUrl;
	} 
	public String getShareUrl() {
		return this.shareUrl;
	} 
//#############################################
	public void setMediaId(final String mediaId) {
		this.mediaId = mediaId;
	}
	public String getMediaId() {
		return this.mediaId;
	}

//#############################################
	public void setShareText(final String shareText) {
		this.shareText = shareText;
	}
	public String getShareText() {
		return this.shareText;
	}

//#############################################
	public void setAdded(final String added) {
		this.added = added;
	}
	public String getAdded() {
		return this.added;
	}

//#############################################
	/** set image server path **/
	public void setImageServerPath(final String imageServerPath) {
		this.imageServerPath = imageServerPath;
	}
	public String getImageServerPath() {
		return this.imageServerPath;
	}

//#############################################
	public void setVideoServerPath(final String videoServerPath) {
		this.videoServerPath = videoServerPath;
	}
	public String getVideoServerPath() {
		return this.videoServerPath;
	} 
//#############################################
//#############################################
	
	public void setEventId(final String eventId) {
		this.eventId = eventId;
	}
	public String getEventId() {
		return this.eventId;
	}
//#############################################
 
	public void setImageLocalFilename(final String imageLocalFilename) {
		this.imageLocalFilename = imageLocalFilename;
	}
	public String getImageLocalFilename() {
		return this.imageLocalFilename;
	} 
//#############################################
	public void setLargeImageUrl(final String largeImageUrl) {

		this.largeImageUrl = largeImageUrl;
	}
	public String getLargeImageUrl() {
		return this.largeImageUrl;
	}
//#############################################

	public void setVideoLocalPath(final String videoLocalPath) {
		this.videoLocalPath = videoLocalPath;
	}
	public String getVideoLocalPath() {
		return this.videoLocalPath;
	}
//#############################################
	public void setImageLocalPath(final String imageLocalPath) {
		this.imageLocalPath = imageLocalPath;
	}
	public String getImageLocalPath() {
		return this.imageLocalPath;
	}
  
 
}

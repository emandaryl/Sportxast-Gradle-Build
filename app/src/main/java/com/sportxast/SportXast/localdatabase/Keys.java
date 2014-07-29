package com.sportxast.SportXast.localdatabase;

public class Keys {

    public static class KEY_S3{
        public static final String ACCESS_KEY = "AKIAJIU3VQFBJI2VSQVA";
        public static final String SECRET_KEY = "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";
    }
public static class KEY_HEADER{
		public static final String Content_Encoding = "Content-Encoding";
		public static final String Content_Type = "Content-Type";
		public static final String Date = "Date";
		public static final String Server = "Server";
		public static final String Vary = "Vary";
		public static final String X_DEVICE_ID = "X-DEVICE-ID";
		public static final String X_SESSION_ID = "X-SESSION-ID";
		public static final String X_USER_ID = "X-USER-ID";
		public static final String X_USER_NAME = "X-USER-NAME";
		public static final String transfer_encoding = "transfer-encoding";
		public static final String Connection = "Connection";

	}
	public static class KEY_PROFILE{
		public static final String TABLE_PROFILE = "profile";
		public static final String ID = "id";
		public static final String DEVICE_ID = "deviceId";
		public static final String SESSION_ID = "sessionId";
		public static final String USER_ID = "userId";
		public static final String USER_NAME = "usernameId";
		public static final String EVENT_ID = "eventId";
		public static final String COUNTRY = "";
		public static final String LOCALITY = "";
	}

	public static class KEY_EVENT { 
		public static final String TABLE_EVENTS = "event";
		public static final String ID = "id";
		public static final String SERVERID ="eventId";
		public static final String SPORTID ="sportId";
		public static final String SPORTNAME ="sportName";
		public static final String SPORTFIRSTLETTER ="sportFirstLetter";
		public static final String SPORTLOGO ="sportLogo";
		public static final String TAGS ="tags"; 
		public static final String VENUEADDR ="venueAddr"; 
		public static final String VENUEID ="venueId";
		public static final String VENUELATITUDE ="venueLatitude"; 
		public static final String VENUELONGITUDE ="venueLongitude";
		public static final String VENUENAME ="venueName";
		public static final String FIRSTTEAM = "firstTeam";
		public static final String SECONDTEAM = "secondTeam";
		
		public static final String EVENT_IS_ENDED = "eventIsEnded";
		public static final String EVENT_LATITUDE = "eventLatitude";
		public static final String EVENT_LOCATION = "eventLocation";
		public static final String EVENT_SHARE_COUNT = "eventShareCount";
		public static final String EVENT_START_DATE_SHORT = "eventStartDateShort";
		public static final String EVENT_START_DATE = "eventStartDate";
		public static final String EVENT_LONGITUDE = "eventLongitude";
		public static final String EVENT_FAVORITE_COUNT = "eventFavoriteCount";
		public static final String EVENT_SCORE = "eventScore";
		public static final String EVENT_TEAMS = "eventTeams";
		public static final String EVENT_ID = "eventId";
		public static final String EVENT_FIRST_TEAM = "eventFirstTeam";
		public static final String EVENT_SECOND_TEAM = "eventSecondTeam";
		public static final String EVENT_VIEW_COUNT = "eventViewCount";
		public static final String EVENT_SPORT_ID = "eventSportId";
		public static final String EVENT_NAME = "eventName";
		public static final String EVENT_COMMENT_COUNT = "eventCommentCount";
		public static final String EVENT_IS_OPEN = "eventIsOpen";
		public static final String EVENT_SPORT_NAME = "eventSportName";
		public static final String EVENT_MEDIA_COUNT = "eventMediaCount";
		public static final String EVENT_TAGS = "eventTags";
	}

	public static class KEY_MEDIA_STORAGE{
		public static final String TABLE_MEDIA_STORAGE = "MediaStorage";
		public static final String ID = "id";
		public static final String IMAGE_FILE_NAME = "imageFileName";
		public static final String IMAGE_UPLOAD_RESPONSE_HEADERS = "imageUploadResponseHeaders";
		public static final String IN_QUEUE = "inQueue";
		public static final String IS_DELETED_BY_USER = "isDeletedByUser";
		public static final String IS_FAVORITE = "isFavorite";
		public static final String IS_IMAGE_UPLOADED = "isImageUploaded";
		public static final String IS_SHARED_ON_FACEBOOK = "isSharedOnFacebook";
		public static final String IS_SHARED_ON_MAIL = "isSharedOnMail";
		public static final String IS_SHARED_ON_SMS = "isSharedOnSms";
		public static final String IS_SHARED_ON_TWITTER = "isSharedOnTwitter";
		public static final String IS_UPLOADED = "isUploaded";
		public static final String IS_SAVED = "isSaved";
		public static final String IS_VIDEO_UPLOADED = "isVideoUploaded";
		public static final String MEDIA_ASPECT_RATIO = "mediaAspectRatio";
		public static final String MEDIA_COMMENT = "mediaComment";
		public static final String MEDIA_COVER_PATH = "mediaCoverPath";
		public static final String MEDIA_DATE = "mediaDate";
		public static final String MEDIA_EVENT_ID = "mediaEventID";
		public static final String MEDIA_FILE_KEY = "mediaFileKey";
		public static final String MEDIA_ORIENTATION = "mediaOrientation";
		public static final String MEDIA_PATH = "mediaPath";
		public static final String MEDIA_SERVER_ID = "mediaServerId";
		public static final String MEDIA_SESSION_ID = "mediaSessionID";
		public static final String MEDIA_SHARE_URL = "mediaShareUrl";
		public static final String MEDIA_TAGS = "mediaTags";
		public static final String MEDIA_SHARE_TEXT = "shareText";
		public static final String USER_LATITUDE = "userLatitude";
		public static final String USER_LONGITUDE = "userLongitude";
		public static final String VIDEO_FILE_NAME = "videoFileName";
		public static final String VIDEO_UPLOAD_RESPONSE_HEADERS = "videoUploadResponseHeaders";
		public static final String MODIFIED = "modified";
	}
	
	public static class KEY_MEDIA_QUEUE{
		public static final String TABLE_MEDIA_QUEUE = "MediaQueue";
		public static final String ID = "id";
		public static final String MEDIA_STORAGE_ID = "mediaStorageId";
		public static final String IMAGE_FILE_NAME = "imageFileName";
		public static final String IMAGE_FILE_PATH = "imageFilePath";
		public static final String IMAGE_UPLOAD_RESPONSE_HEADERS = "imageUploadResponseHeaders";
		public static final String IN_QUEUE = "inQueue";
		public static final String IS_IMAGE_UPLOADED = "isImageUploaded";
		public static final String IS_UPLOADED = "isUploaded";
		public static final String IS_VIDEO_UPLOADED = "isVideoUploaded";
		public static final String MEDIA_FILE_KEY = "mediaFileKey";
		public static final String MEDIA_SERVER_ID = "mediaServerId";
		public static final String MEDIA_SERVER_ID_FAILED = "mediaServerIdFailed";
		public static final String VIDEO_FILE_NAME = "videoFileName";
		public static final String VIDEO_FILE_PATH = "videoFilePath";
		public static final String VIDEO_UPLOAD_RESPONSE_HEADERS = "videoUploadResponseHeaders";
	}
	
    public static class share{
        public static final int RESULT_FROM_EMAIL = 1001;
        public static final int RESULT_FROM_TWITTER = 1002;
        public static final int RESULT_FROM_FACEBOOK = 1003;
        public static final int RESULT_FROM_SMS = 1004;
    }

}

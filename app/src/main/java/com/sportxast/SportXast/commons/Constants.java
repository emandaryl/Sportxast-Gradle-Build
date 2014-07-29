package com.sportxast.SportXast.commons;
 
public class Constants {
//	public static final String BASE_URL="http://sportxast.sportxast.com/phone/apiV2/";
//	public static final String BASE_URL="http://sportxast.apps-sportxast.com/phone/apiV2/"; <== link to be used in romania
//	public static final String BASE_URL="https://rc.sportxast.com/phone/apiV2/"; //<= link to be used in RC/phils
	 
//	public static final String BASE_URL="https://test.sportxast.com/phone/apiV2/"; //<= link to be used FOR TEST 
	
	
	//public static final String BASE_URL="https://dev.sportxast.com/phone/apiV2/"; //<= link to be used in RC/phils
 
	public static final String BASE_URL="https://"+GlobalVariablesHolder.APISubDomain+".sportxast.com/phone/apiV2/"; //<= link to be used in RC/phils
	
	public static final int default_tutorial_eventID 			= 1143; 
	public static final int requestCode_Tutorial_Activity 		= 10010;
	public static final int requestCode_Highlight_Activity		= 10011; 
	public static final int requestCode_VideoCapture_Activity	= 10012; 
	public static final int requestCode_Menu_Activity			= 10013;  
	public static final int requestCode_Profile_Activity		= 10014; 
	public static final int requestCode_Create_Activity			= 10015; 
	public static final int requestCode_ChooseASport_Activity	= 10016; 
	public static final int requestCode_ChooseATeam_Activity	= 10017; 
	public static final int requestCode_ChooseAVenue_Activity	= 10018; 
	public static final int requestCode_SportX2_Main			= 10019; 
	public static final int requestCode_VideoPreviewActivity	= 10020; 
	
	public static final int RC_PRES 	= 1002;
	public static final int RC_NEWS 	= 1003;
	public static final int RC_SPEAK 	= 1004;
	public static final int RC_SENT 	= 1005;
	public static final int RC_RP 		= 1006;
	public static final int RC_INTER 	= 1007;
	
	public static final String _MOVIE_DURATION = "";
	 
	public static String sharePrefKey_uploadedHighlights = "blitzkrieg69691a";
	    

	public static String sharePrefKey_uploadedHighlights_self = "cthulhufsmrawrrawrbaby69";
	
	public static String _S3_VIDEO_BUCKET = "S3_VIDEO_BUCKET";
	public static String _S3_IMAGE_BUCKET = "S3_IMAGE_BUCKET";
	//###############################################################################################
	//###############################################################################################
	 
	public static final String EXTRA_EVENT_ID 	= "eventId";
	public static final String EXTRA_EVENT_TYPE = "eventType";
	public static final String EXTRA_USER_ID 	= "userId";
	public static final String EXTRA_FULLNAME 	= "fullName";
	public static final String EXTRA_MEDIA_ID 	= "mediaId";
	public static final String EXTRA_USERNAME 	= "username";
	
	public static final String EXTRA_HASHTAG = "hashTag";
	public static final String EXTRA_EVENT_TEAMS = "eventTeams";
	public static final String EXTRA_EVENT_DATE = "eventDate";
	public static final String EXTRA_EVENT_IS_OPEN = "eventIsOpen";
	public static final String EXTRA_IS_TODAY = "isToday";
	public static final String EXTRA_FROM_EVENT_PAGE = "fromEventPage";
	
	public static final String KEY_SUCCESS = "success";
	public static final String KEY_ERROR = "error";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_FOLLOWING = "following";
	
	public static final String SHARED_FOLLOWING = "sportx.user.following";
	public static final String SHARED_FOLLOWERS = "sportx.user.followers";
	
	//###############################################################################################
	//###############################################################################################
	 
	public static final String EXTRA_RESULT_COUNT = "resultCount";
	public static final String EXTRA_LIST = "list";
	public static final String EXTRA_ID = "id";
	public static final String EXTRA_NAME = "name";
	public static final String EXTRA_FACEBOOK_TITLE = "title"; 
	
	public static final String EXPORT_EVENT_TAG = "ExportEventTag";
	public static final String EXPORT_FAN_LIST = "ExportFanList";
	public static final String EXPORT_MEDIA_TAG = "ExportMediaTag";
	
	public static final String TAG_FACEBOOK = "Facebook";
	public static final String SEPARATOR = "##";
	 
	
	}

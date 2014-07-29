package com.sportxast.SportXast.models;

import android.content.Context;

import com.sportxast.SportXast.R;

import org.json.JSONException;
import org.json.JSONObject;

public class _AppSetting {

	
	public String language = "";
	public Labels languageLabels  = new Labels();
	public Settings settings = new Settings();
	


	public static class Labels {
		 
		public String _A_SportXast_highlight_was_shared_by = "";
		public String _A_new_beta_version_is_available = "";
		public String _A_new_event__Label_your_Event = "";
		public String _A_new_version_is_available_in_app_store = "";
		public String _Account = "";
		public String _Add_comment = "";
		public String _Add = "";
		public String _Back = "";
		public String _Cancel = "";
		public String _Capture_watch_and_share_sporting_highlights_in_your_community_with_SportXast = "";
		public String _Capture = "";
		public String _Choose_picture = "";
		public String _Close = "";
		public String _DONE = "";
		public String _Delete_media = "";
		public String _Delete_video = "";
		public String _Edit_Profile = "";
		public String _Edit_Your_Profile = "";
		public String _Enter_your_email = "";
		public String _Enter_your_motto = "";
		public String _Enter_your_name = "";
		public String _Enter_your_username = "";
		public String _Error = "";
		public String _Events = "";
		public String _Facebook = "";
		public String _Failed_to_send_SMS = "";
		public String _From_camera = "";
		public String _From_photos = "";
		public String _Go_to_your_iPhone_Settings__Privacy__Location_Services_to_turn_on = "";
		public String _Highlights = "";
		public String _Home = "";
		public String _Import_from_Facebook = "";
		public String _Import_from_Twitter = "";
		public String _Join_Event = "";
		public String _Label_your_event_so_other_people_may_join_to_capture_watch_and_share_highlights = "";
		public String _New_version_available = "";
		public String _No_internet_connection = "";
		public String _No = "";
		public String _On_your_iPhone_go_to_Settings__Facebook_to_sign_in_or_create_a_new_account = "";
		public String _On_your_iPhone_go_to_Settings__Mail_to_add_an_eMail_account = "";
		public String _On_your_iPhone_go_to_Settings__Twitter_to_sign_in_or_create_a_new_account = "";
		public String _Profile_and_Account = "";
		public String _Profile = "";
		public String _Record = "";
		public String _Register = "";
		public String _Report_Inappropriate = "";
		public String _Report_video_as_innappropiate = "";
		public String _Send = "";
		public String _Settings = "";
		public String _Share = "";
		public String _Skip = "";
		public String _SportxAst_cannot_access_yor_Twitter_info = "";
		public String _To_be_done = "";
		public String _Twitter_Account_Not_Found = "";
		public String _Twitter = "";
		public String _Use_facebook_account = "";
		public String _Use_twitter_account = "";
		public String _View_all = "";
		public String _Yes = "";
		public String _Your_device_doesnt_support_SMS = "";
		public String _comments = "";
		public String _favorites = "";
		public String _video = "";
		public String _No_Highlights = "";
		public String _No_Notifications = "";
		public String _No_Media_Tag = "";
		public String _Add_another_Team = "";
		public String _Add_a_Team = "";
		public String _Sports = "";
		public String _Venues = "";
		public String _Done = "";
		public String _Add_more = "";
		public String _Joined = "";
		public String _Join = "";
		public String _Getting_Location = "";
		public String _Search = "";
		public String _Following = "";
		public String _Follow = "";
		public String _Popular = "";
		public String _Nearby = "";
		public String _Favorites = "";
		public String _Sport = "";
		public String _Venue = "";
		public String _Home_Team = "";
		public String _Other_Team = "";
		public String _Information = "";
		public String _Select_Venue = "";
		public String _Teams = "";
		public String _Create = "";
		public String _Menu = "";
		public String _Followers = "";
		public String _Other = "";
		public String _Edit = "";
		public String _Favorite = "";
		public String _Notifications = "";
		public String _Share_SportXast = "";
		public String _No_Events = "";
		public String _Tags = "";
		public String _Event_Tags = "";
	}

	public static class Settings{
		public String _IS_SECURE_CONNECTION = "";
		public String _MOVIE_DURATION = "";
		public String _MOVIE_DELAY = "";
		public String _AUTOCOMPLETE_START_AFTER = "";
		public String _LOG_LEVEL = "";
		public String _S3_VIDEO_BUCKET = "";
		public String _S3_IMAGE_BUCKET = "";
		public String _S3_VIDEO_UPLOAD_PATH = "";
		public String _S3_IMAGE_UPLOAD_PATH = "";
		public String _S3_STREAM_BUCKET = "";
		public String _APP_SHARE_TEXT = "";
		public String _APP_SHARE_URL = "";
		public String _S3_VIDEO_PUT_URL = "";
		public String _S3_VIDEO_GET_URL = "";
		public String _S3_IMAGE_PUT_URL = "";
		public String _S3_IMAGE_GET_URL = "";
		public String _S3_STREAM_PUT_URL = "";
		public String _S3_STREAM_GET_URL = "";
		public String _EVENT_SEARCH_RADIUS = "";
		public String _SNS_NOTIFICATION_TOPIC = "";
		public String _S3_THUMBNAIL_FORMAT = "";
		public String _S3_AVATAR_UPLOAD_PATH = "";
		public String _S3_PLAYLIST_SEGMENT_DURATION = "";
		public String _SPORT_DEFAULT_TAGS = "";
		public String _APP_SHARE_MAIL_SUBJECT = "";
		public String _MEDIA_SHARE_MAIL_SUBJECT = "";
		public String _CHALKBOARD_TIMEOUT = "";
		public String _STOP_BROADCAST_BY_ANGLE = "";
		public String _STOP_BROADCAST_BY_ANGLE_TIME = "";
		public String _APP_BETA_VERSION = "";
		public String _APP_PRODUCTION_VERSION = "";
		public String _APP_BETA_VERSION_IS_LOCKED = "";
		public String _APP_BETA_VERSION_DOWNLOAD_URL = "";
		public String _APP_PRODUCTION_VERSION_DOWNLOAD_URL = "";
		public String _APP_BETA_VERSION_DOWNLOAD_TEXT = "";
		public String _APP_PRODUCTION_VERSION_DOWNLOAD_TEXT = "";
		public String _LOCATION_MAX_RADIUS = "";
		public String _WEBSITE_BASE_URL = "";
		public String _REDIS_CONN_HOST = "";
		public String _REDIS_CONN_PORT = "";
		public String _REDIS_DB_INDEX = "";
		public String _FETCH_NOTIFICATION_INTERVAL = "";
		public String _ALERT_MESSAGE_NEW_EVENT = "";
		public String _ALERT_MESSAGE_NEW_HIGHLIGHT = "";
		public String _ALERT_MESSAGE_NEW_FAVORITE = "";
		public String _ALERT_MESSAGE_NEW_COMMENT = "";
		public String _ALERT_MESSAGE_NEW_SHARE = "";
		public String _SQS_PUSHNOTIFICATION_QUEUE = "";
		public String _IOS_SNS_APP_ARN = "";
		public String _ALERT_MESSAGE_NEW_FRIEND = "";
		public String _ALERT_MESSAGE_NEW_USER_ON_JOINED_EVENT = "";
		public String _TOOLTIP_TIMEOUT = "";
		public String _SQS_TRANSCODER_JOBSTATUS_QUEUE = "";
		public String _ALERT_MESSAGE_NEW_MEDIA_TAG = "";
		public String _DEFAULT_EVENT_ID = "";
	}

	public String getStringValue (Context context,int stringId){
		return context.getResources().getString(stringId);
	}
	
	public void parseAppSetting(Context context,JSONObject jsonObject){
		
		try {
			language = jsonObject.getString(getStringValue(context,R.string.language));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			JSONObject jsonObject_languageLabels = jsonObject.getJSONObject(getStringValue(context,R.string.languageLabels));
			
			languageLabels._A_SportXast_highlight_was_shared_by = jsonObject_languageLabels.getString(getStringValue(context,R.string._A_SportXast_highlight_was_shared_by));
			languageLabels._A_new_beta_version_is_available = jsonObject_languageLabels.getString(getStringValue(context,R.string._A_new_beta_version_is_available));
			languageLabels._A_new_event__Label_your_Event = jsonObject_languageLabels.getString(getStringValue(context,R.string._A_new_event__Label_your_Event));
			languageLabels._A_new_version_is_available_in_app_store = jsonObject_languageLabels.getString(getStringValue(context,R.string._A_new_version_is_available_in_app_store));
			languageLabels._Account = jsonObject_languageLabels.getString(getStringValue(context,R.string._Account));
			languageLabels._Add_comment = jsonObject_languageLabels.getString(getStringValue(context,R.string._Add_comment));
			languageLabels._Add = jsonObject_languageLabels.getString(getStringValue(context,R.string._Add));
			languageLabels._Back = jsonObject_languageLabels.getString(getStringValue(context,R.string._Back));
			languageLabels._Cancel = jsonObject_languageLabels.getString(getStringValue(context,R.string._Cancel));
			languageLabels._Capture_watch_and_share_sporting_highlights_in_your_community_with_SportXast = jsonObject_languageLabels.getString(getStringValue(context,R.string._Capture_watch_and_share_sporting_highlights_in_your_community_with_SportXast));
			languageLabels._Capture = jsonObject_languageLabels.getString(getStringValue(context,R.string._Capture));
			languageLabels._Choose_picture = jsonObject_languageLabels.getString(getStringValue(context,R.string._Choose_picture));
			languageLabels._Close = jsonObject_languageLabels.getString(getStringValue(context,R.string._Close));
			languageLabels._DONE = jsonObject_languageLabels.getString(getStringValue(context,R.string._DONE));
			languageLabels._Delete_media = jsonObject_languageLabels.getString(getStringValue(context,R.string._Delete_media));
			languageLabels._Delete_video = jsonObject_languageLabels.getString(getStringValue(context,R.string._Delete_video));
			languageLabels._Edit_Profile = jsonObject_languageLabels.getString(getStringValue(context,R.string._Edit_Profile));
			languageLabels._Edit_Your_Profile = jsonObject_languageLabels.getString(getStringValue(context,R.string._Edit_Your_Profile));
			languageLabels._Enter_your_email = jsonObject_languageLabels.getString(getStringValue(context,R.string._Enter_your_email));
			languageLabels._Enter_your_motto = jsonObject_languageLabels.getString(getStringValue(context,R.string._Enter_your_motto));
			languageLabels._Enter_your_name = jsonObject_languageLabels.getString(getStringValue(context,R.string._Enter_your_name));
			languageLabels._Enter_your_username = jsonObject_languageLabels.getString(getStringValue(context,R.string._Enter_your_username));
			languageLabels._Error = jsonObject_languageLabels.getString(getStringValue(context,R.string._Error));
			languageLabels._Events = jsonObject_languageLabels.getString(getStringValue(context,R.string._Events));
			languageLabels._Facebook = jsonObject_languageLabels.getString(getStringValue(context,R.string._Facebook));
			languageLabels._Failed_to_send_SMS = jsonObject_languageLabels.getString(getStringValue(context,R.string._Failed_to_send_SMS));
			languageLabels._From_camera = jsonObject_languageLabels.getString(getStringValue(context,R.string._From_camera));
			languageLabels._From_photos = jsonObject_languageLabels.getString(getStringValue(context,R.string._From_photos));
			languageLabels._Go_to_your_iPhone_Settings__Privacy__Location_Services_to_turn_on = jsonObject_languageLabels.getString(getStringValue(context,R.string._Go_to_your_iPhone_Settings__Privacy__Location_Services_to_turn_on));
			languageLabels._Highlights = jsonObject_languageLabels.getString(getStringValue(context,R.string._Highlights));
			languageLabels._Home = jsonObject_languageLabels.getString(getStringValue(context,R.string._Home));
			languageLabels._Import_from_Facebook = jsonObject_languageLabels.getString(getStringValue(context,R.string._Import_from_Facebook));
			languageLabels._Import_from_Twitter = jsonObject_languageLabels.getString(getStringValue(context,R.string._Import_from_Twitter));
			languageLabels._Join_Event = jsonObject_languageLabels.getString(getStringValue(context,R.string._Join_Event));
			languageLabels._Label_your_event_so_other_people_may_join_to_capture_watch_and_share_highlights = jsonObject.getString(getStringValue(context,R.string._Label_your_event_so_other_people_may_join_to_capture_watch_and_share_highlights));
			languageLabels._New_version_available = jsonObject_languageLabels.getString(getStringValue(context,R.string._New_version_available));
			languageLabels._No_internet_connection = jsonObject_languageLabels.getString(getStringValue(context,R.string._No_internet_connection));
			languageLabels._No = jsonObject_languageLabels.getString(getStringValue(context,R.string._No));
			languageLabels._On_your_iPhone_go_to_Settings__Facebook_to_sign_in_or_create_a_new_account = jsonObject_languageLabels.getString(getStringValue(context,R.string._On_your_iPhone_go_to_Settings__Facebook_to_sign_in_or_create_a_new_account));
			languageLabels._On_your_iPhone_go_to_Settings__Mail_to_add_an_eMail_account = jsonObject_languageLabels.getString(getStringValue(context,R.string._On_your_iPhone_go_to_Settings__Mail_to_add_an_eMail_account));
			languageLabels._On_your_iPhone_go_to_Settings__Twitter_to_sign_in_or_create_a_new_account = jsonObject_languageLabels.getString(getStringValue(context,R.string._On_your_iPhone_go_to_Settings__Twitter_to_sign_in_or_create_a_new_account));
			languageLabels._Profile_and_Account = jsonObject_languageLabels.getString(getStringValue(context,R.string._Profile_and_Account));
			languageLabels._Profile = jsonObject_languageLabels.getString(getStringValue(context,R.string._Profile));
			languageLabels._Record = jsonObject_languageLabels.getString(getStringValue(context,R.string._Record));
			languageLabels._Register = jsonObject_languageLabels.getString(getStringValue(context,R.string._Register));
			languageLabels._Report_Inappropriate = jsonObject_languageLabels.getString(getStringValue(context,R.string._Report_Inappropriate));
			languageLabels._Report_video_as_innappropiate = jsonObject_languageLabels.getString(getStringValue(context,R.string._Report_video_as_innappropiate));
			languageLabels._Send = jsonObject_languageLabels.getString(getStringValue(context,R.string._Send));
			languageLabels._Settings = jsonObject_languageLabels.getString(getStringValue(context,R.string._Settings));
			languageLabels._Share = jsonObject_languageLabels.getString(getStringValue(context,R.string._Share));
			languageLabels._Skip = jsonObject_languageLabels.getString(getStringValue(context,R.string._Skip));
			languageLabels._SportxAst_cannot_access_yor_Twitter_info = jsonObject_languageLabels.getString(getStringValue(context,R.string._SportxAst_cannot_access_yor_Twitter_info));
			languageLabels._To_be_done = jsonObject_languageLabels.getString(getStringValue(context,R.string._To_be_done));
			languageLabels._Twitter_Account_Not_Found = jsonObject_languageLabels.getString(getStringValue(context,R.string._Twitter_Account_Not_Found));
			languageLabels._Twitter = jsonObject_languageLabels.getString(getStringValue(context,R.string._Twitter));
			languageLabels._Use_facebook_account = jsonObject_languageLabels.getString(getStringValue(context,R.string._Use_facebook_account));
			languageLabels._Use_twitter_account = jsonObject_languageLabels.getString(getStringValue(context,R.string._Use_twitter_account));
			languageLabels._View_all = jsonObject_languageLabels.getString(getStringValue(context,R.string._View_all));
			languageLabels._Yes = jsonObject_languageLabels.getString(getStringValue(context,R.string._Yes));
			languageLabels._Your_device_doesnt_support_SMS = jsonObject_languageLabels.getString(getStringValue(context,R.string._Your_device_doesnt_support_SMS));
			languageLabels._comments = jsonObject_languageLabels.getString(getStringValue(context,R.string._comments));
			languageLabels._favorites = jsonObject_languageLabels.getString(getStringValue(context,R.string._favorites));
			languageLabels._video = jsonObject_languageLabels.getString(getStringValue(context,R.string._video));
			languageLabels._No_Highlights = jsonObject_languageLabels.getString(getStringValue(context,R.string._No_Highlights));
			languageLabels._No_Notifications = jsonObject_languageLabels.getString(getStringValue(context,R.string._No_Notifications));
			languageLabels._No_Media_Tag = jsonObject_languageLabels.getString(getStringValue(context,R.string._No_Media_Tag));
			languageLabels._Add_another_Team = jsonObject_languageLabels.getString(getStringValue(context,R.string._Add_another_Team));
			languageLabels._Add_a_Team = jsonObject_languageLabels.getString(getStringValue(context,R.string._Add_a_Team));
			languageLabels._Sports = jsonObject_languageLabels.getString(getStringValue(context,R.string._Sports));
			languageLabels._Venues = jsonObject_languageLabels.getString(getStringValue(context,R.string._Venues));
			languageLabels._Done = jsonObject_languageLabels.getString(getStringValue(context,R.string._Done));
			languageLabels._Add_more = jsonObject_languageLabels.getString(getStringValue(context,R.string._Add_more));
			languageLabels._Joined = jsonObject_languageLabels.getString(getStringValue(context,R.string._Joined));
			languageLabels._Join = jsonObject_languageLabels.getString(getStringValue(context,R.string._Join));
			languageLabels._Getting_Location = jsonObject_languageLabels.getString(getStringValue(context,R.string._Getting_Location));
			languageLabels._Search = jsonObject_languageLabels.getString(getStringValue(context,R.string._Search));
			languageLabels._Following = jsonObject_languageLabels.getString(getStringValue(context,R.string._Following));
			languageLabels._Follow = jsonObject_languageLabels.getString(getStringValue(context,R.string._Follow));
			languageLabels._Popular = jsonObject_languageLabels.getString(getStringValue(context,R.string._Popular));
			languageLabels._Nearby = jsonObject_languageLabels.getString(getStringValue(context,R.string._Nearby));
			languageLabels._Favorites = jsonObject_languageLabels.getString(getStringValue(context,R.string._Favorites));
			languageLabels._Sport = jsonObject_languageLabels.getString(getStringValue(context,R.string._Sport));
			languageLabels._Venue = jsonObject_languageLabels.getString(getStringValue(context,R.string._Venue));
			languageLabels._Home_Team = jsonObject_languageLabels.getString(getStringValue(context,R.string._Home_Team));
			languageLabels._Other_Team = jsonObject_languageLabels.getString(getStringValue(context,R.string._Other_Team));
			languageLabels._Information = jsonObject_languageLabels.getString(getStringValue(context,R.string._Information));
			languageLabels._Select_Venue = jsonObject_languageLabels.getString(getStringValue(context,R.string._Select_Venue));
			languageLabels._Teams = jsonObject_languageLabels.getString(getStringValue(context,R.string._Teams));
			languageLabels._Create = jsonObject_languageLabels.getString(getStringValue(context,R.string._Create));
			languageLabels._Menu = jsonObject_languageLabels.getString(getStringValue(context,R.string._Menu));
			languageLabels._Followers = jsonObject_languageLabels.getString(getStringValue(context,R.string._Followers));
			languageLabels._Other = jsonObject_languageLabels.getString(getStringValue(context,R.string._Other));
			languageLabels._Edit = jsonObject_languageLabels.getString(getStringValue(context,R.string._Edit));
			languageLabels._Favorite = jsonObject_languageLabels.getString(getStringValue(context,R.string._Favorite));
			languageLabels._Notifications = jsonObject_languageLabels.getString(getStringValue(context,R.string._Notifications));
			languageLabels._Share_SportXast = jsonObject_languageLabels.getString(getStringValue(context,R.string._Share_SportXast));
			languageLabels._No_Events = jsonObject_languageLabels.getString(getStringValue(context,R.string._No_Events));
			languageLabels._Tags = jsonObject_languageLabels.getString(getStringValue(context,R.string._Tags));
			languageLabels._Event_Tags = jsonObject_languageLabels.getString(getStringValue(context,R.string._Event_Tags));
			
			} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			
			JSONObject jsonObject_settings = jsonObject.getJSONObject(getStringValue(context,R.string.settings));
			
			settings._IS_SECURE_CONNECTION =  jsonObject_settings.getString(getStringValue(context,R.string._IS_SECURE_CONNECTION));
			settings._MOVIE_DURATION =  jsonObject_settings.getString(getStringValue(context,R.string._MOVIE_DURATION));
			settings._MOVIE_DELAY =  jsonObject_settings.getString(getStringValue(context,R.string._MOVIE_DELAY));
			settings._AUTOCOMPLETE_START_AFTER =  jsonObject_settings.getString(getStringValue(context,R.string._AUTOCOMPLETE_START_AFTER));
			settings._LOG_LEVEL =  jsonObject_settings.getString(getStringValue(context,R.string._LOG_LEVEL));
			settings._S3_VIDEO_BUCKET =  jsonObject_settings.getString(getStringValue(context,R.string._S3_VIDEO_BUCKET));
			settings._S3_IMAGE_BUCKET =  jsonObject_settings.getString(getStringValue(context,R.string._S3_IMAGE_BUCKET));
			settings._S3_VIDEO_UPLOAD_PATH =  jsonObject_settings.getString(getStringValue(context,R.string._S3_VIDEO_UPLOAD_PATH));
			settings._S3_IMAGE_UPLOAD_PATH =  jsonObject_settings.getString(getStringValue(context,R.string._S3_IMAGE_UPLOAD_PATH));
			settings._S3_STREAM_BUCKET =  jsonObject_settings.getString(getStringValue(context,R.string._S3_STREAM_BUCKET));
			settings._APP_SHARE_TEXT  =  jsonObject_settings.getString(getStringValue(context,R.string._APP_SHARE_TEXT));
			settings._APP_SHARE_URL =  jsonObject_settings.getString(getStringValue(context,R.string._APP_SHARE_URL));
			settings._S3_VIDEO_PUT_URL =  jsonObject_settings.getString(getStringValue(context,R.string._S3_VIDEO_PUT_URL));
			settings._S3_VIDEO_GET_URL =  jsonObject_settings.getString(getStringValue(context,R.string._S3_VIDEO_GET_URL));
			settings._S3_IMAGE_PUT_URL =  jsonObject_settings.getString(getStringValue(context,R.string._S3_IMAGE_PUT_URL));
			settings._S3_IMAGE_GET_URL =  jsonObject_settings.getString(getStringValue(context,R.string._S3_IMAGE_GET_URL));
			settings._S3_STREAM_PUT_URL =  jsonObject_settings.getString(getStringValue(context,R.string._S3_STREAM_PUT_URL));
			settings._S3_STREAM_GET_URL =  jsonObject_settings.getString(getStringValue(context,R.string._S3_STREAM_GET_URL));
			settings._EVENT_SEARCH_RADIUS =  jsonObject_settings.getString(getStringValue(context,R.string._EVENT_SEARCH_RADIUS));
			settings._SNS_NOTIFICATION_TOPIC =  jsonObject_settings.getString(getStringValue(context,R.string._SNS_NOTIFICATION_TOPIC));
			settings._S3_THUMBNAIL_FORMAT =  jsonObject_settings.getString(getStringValue(context,R.string._S3_THUMBNAIL_FORMAT));
			settings._S3_AVATAR_UPLOAD_PATH =  jsonObject_settings.getString(getStringValue(context,R.string._S3_AVATAR_UPLOAD_PATH));
			settings._S3_PLAYLIST_SEGMENT_DURATION =  jsonObject_settings.getString(getStringValue(context,R.string._S3_PLAYLIST_SEGMENT_DURATION));
			settings._SPORT_DEFAULT_TAGS =  jsonObject_settings.getString(getStringValue(context,R.string._SPORT_DEFAULT_TAGS));
			settings._APP_SHARE_MAIL_SUBJECT =  jsonObject_settings.getString(getStringValue(context,R.string._APP_SHARE_MAIL_SUBJECT));
			settings._MEDIA_SHARE_MAIL_SUBJECT =  jsonObject_settings.getString(getStringValue(context,R.string._MEDIA_SHARE_MAIL_SUBJECT));
			settings._CHALKBOARD_TIMEOUT =  jsonObject_settings.getString(getStringValue(context,R.string._CHALKBOARD_TIMEOUT));
			settings._STOP_BROADCAST_BY_ANGLE =  jsonObject_settings.getString(getStringValue(context,R.string._STOP_BROADCAST_BY_ANGLE));
			settings._STOP_BROADCAST_BY_ANGLE_TIME =  jsonObject_settings.getString(getStringValue(context,R.string._STOP_BROADCAST_BY_ANGLE_TIME));
			settings._APP_BETA_VERSION =  jsonObject_settings.getString(getStringValue(context,R.string._APP_BETA_VERSION));
			settings._APP_PRODUCTION_VERSION =  jsonObject_settings.getString(getStringValue(context,R.string._APP_PRODUCTION_VERSION));
			settings._APP_BETA_VERSION_IS_LOCKED =  jsonObject_settings.getString(getStringValue(context,R.string._APP_BETA_VERSION_IS_LOCKED));
			settings._APP_BETA_VERSION_DOWNLOAD_URL =  jsonObject_settings.getString(getStringValue(context,R.string._APP_BETA_VERSION_DOWNLOAD_URL));
			settings._APP_PRODUCTION_VERSION_DOWNLOAD_URL =  jsonObject_settings.getString(getStringValue(context,R.string._APP_PRODUCTION_VERSION_DOWNLOAD_URL));
			settings._APP_BETA_VERSION_DOWNLOAD_TEXT =  jsonObject_settings.getString(getStringValue(context,R.string._APP_BETA_VERSION_DOWNLOAD_TEXT));
			settings._APP_PRODUCTION_VERSION_DOWNLOAD_TEXT =  jsonObject_settings.getString(getStringValue(context,R.string._APP_PRODUCTION_VERSION_DOWNLOAD_TEXT));
			settings._LOCATION_MAX_RADIUS =  jsonObject_settings.getString(getStringValue(context,R.string._LOCATION_MAX_RADIUS));
			settings._WEBSITE_BASE_URL =  jsonObject_settings.getString(getStringValue(context,R.string._WEBSITE_BASE_URL));
			settings._REDIS_CONN_HOST =  jsonObject_settings.getString(getStringValue(context,R.string._REDIS_CONN_HOST));
			settings._REDIS_CONN_PORT =  jsonObject_settings.getString(getStringValue(context,R.string._REDIS_CONN_PORT));
			settings._REDIS_DB_INDEX =  jsonObject_settings.getString(getStringValue(context,R.string._REDIS_DB_INDEX));
			settings._FETCH_NOTIFICATION_INTERVAL =  jsonObject_settings.getString(getStringValue(context,R.string._FETCH_NOTIFICATION_INTERVAL));
			settings._ALERT_MESSAGE_NEW_EVENT =  jsonObject_settings.getString(getStringValue(context,R.string._ALERT_MESSAGE_NEW_EVENT));
			settings._ALERT_MESSAGE_NEW_HIGHLIGHT =  jsonObject_settings.getString(getStringValue(context,R.string._ALERT_MESSAGE_NEW_HIGHLIGHT));
			settings._ALERT_MESSAGE_NEW_FAVORITE =  jsonObject_settings.getString(getStringValue(context,R.string._ALERT_MESSAGE_NEW_FAVORITE));
			settings._ALERT_MESSAGE_NEW_COMMENT =  jsonObject_settings.getString(getStringValue(context,R.string._ALERT_MESSAGE_NEW_COMMENT));
			settings._ALERT_MESSAGE_NEW_SHARE =  jsonObject_settings.getString(getStringValue(context,R.string._ALERT_MESSAGE_NEW_SHARE));
			settings._SQS_PUSHNOTIFICATION_QUEUE =  jsonObject_settings.getString(getStringValue(context,R.string._SQS_PUSHNOTIFICATION_QUEUE));
			settings._IOS_SNS_APP_ARN =  jsonObject_settings.getString(getStringValue(context,R.string._IOS_SNS_APP_ARN));
			settings._ALERT_MESSAGE_NEW_FRIEND =  jsonObject_settings.getString(getStringValue(context,R.string._ALERT_MESSAGE_NEW_FRIEND));
			settings._ALERT_MESSAGE_NEW_USER_ON_JOINED_EVENT =  jsonObject_settings.getString(getStringValue(context,R.string._ALERT_MESSAGE_NEW_USER_ON_JOINED_EVENT));
			settings._TOOLTIP_TIMEOUT =  jsonObject_settings.getString(getStringValue(context,R.string._TOOLTIP_TIMEOUT));
			settings._SQS_TRANSCODER_JOBSTATUS_QUEUE =  jsonObject_settings.getString(getStringValue(context,R.string._SQS_TRANSCODER_JOBSTATUS_QUEUE));
			settings._ALERT_MESSAGE_NEW_MEDIA_TAG =  jsonObject_settings.getString(getStringValue(context,R.string._ALERT_MESSAGE_NEW_MEDIA_TAG));
			settings._DEFAULT_EVENT_ID =  jsonObject_settings.getString(getStringValue(context,R.string._DEFAULT_EVENT_ID));
			
			
			
		} catch (JSONException e) {
			// TODO: handle exception
		}
		
		
		
		
	}
}

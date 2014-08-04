package com.sportxast.SportXast.commons;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.Global_Data.Coordinate;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models.EventParcel;
import com.sportxast.SportXast.models.LanguageLabels;
import com.sportxast.SportXast.models.NextMediaID;
import com.sportxast.SportXast.models.Settings;
import com.sportxast.SportXast.models._EventLists.EventLists;
import com.sportxast.SportXast.models._EventLists.EventSport;
import com.sportxast.SportXast.models._EventLists.FavoriteCountWithMedia;
import com.sportxast.SportXast.models._EventLists.MediaTagWithHighlight;
import com.sportxast.SportXast.models._EventLists.MediaWithPlayer;
import com.sportxast.SportXast.models._EventLists.Team;
import com.sportxast.SportXast.models._EventLists.ThumbnailImage;
import com.sportxast.SportXast.models._MediaLists;
import com.sportxast.SportXast.models._MediaLists.Comments;
import com.sportxast.SportXast.models._MediaLists.MediaList;
import com.sportxast.SportXast.models._MediaLists.Tag;
import com.sportxast.SportXast.models._MediaLists.User;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;
import com.sportxast.SportXast.models._MediaStorage;
import com.sportxast.SportXast.models._SearchHashtag;
import com.sportxast.SportXast.models._SearchUser;
import com.sportxast.SportXast.thirdparty_class.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
  
public class CommonFunctions_1 {
	  
	public static GPSTracker gpsTracker; 
	//public static SharedPreferences sharedPreferences;
	
	/*
	public static void initSharedPreferences(Context context){
		sharedPreferences =  context.getSharedPreferences( "com.sportxast.SportXast", Context.MODE_PRIVATE);
	}
	*/
	  
	public static ArrayList<MediaList> FMediaLists; 
	public static ArrayList<NextMediaID> FArrNextMediaID;
	

	public static void promptToEnableGPS(Context context){
		
		// custom dialog
		final android.app.Dialog dialog = new android.app.Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		//final android.app.Dialog dialog = new android.app.Dialog(SportX2_Main.this);
		dialog.setContentView(R.layout.layout_dialog_prompt);
		//dialog.setTitle("Title..."); 
		
		// set the custom dialog components - text, image and button  
		Button prompt_btn_ok = ( Button ) dialog.findViewById(R.id.prompt_btn_ok); 
		prompt_btn_ok.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) { 
				dialog.dismiss();
			}
		});
		 /*
		View view_outside = ( View ) dialog.findViewById(R.id.view_outside); 
		view_outside.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				dialog.dismiss();
			}
		});
		*/
		dialog.show();  
		//########################################################################################################
		//########################################################################################################
 
	}
	
	public static JSONObject reformHighlightJSONObject(_MediaStorage mediaStorage, final String uploadQueue){ 
		/*  FOLLOW THIS FORMAT:
		{	"mediaId":"14489",
			"videoLocalPath":"\/storage\/sdcard0\/Sportx\/video\/20140715125016.mp4",
			"mediaDateShortFormat":"2014-50-15 12:50:28",
			"uploadQueue":"0",
			"added":"new media added",
			"imagePath":"db\/event_media\/2014\/07\/15\/14489.jpg",
			"videoPath":"db\/event_media\/2014\/07\/15\/14489.mp4",
			"userLongitude":"123.9357021",
			"imageLocalFilename":"20140715125016.jpg",
			"videoLocalFilename":"20140715125016.mp4",
			"twitterCardUrl":"http:\/\/goo.gl\/KAYuGz",
			"eventId":"1810",
			"imageLocalPath":"\/storage\/sdcard0\/Sportx\/images\/20140715125016.jpg",
			"largeImageUrl":"",
			"shareUrl":"http:\/\/goo.gl\/9PHyFX",
			"shareText":"Watch this Shuffleboard highlight from Ayala Center in Cebu captured by SportXast",
			"mediaDateLongFormat":"Tue, 15 Jul 2014 12:50:28 +0800",
			"userLatitude":"10.339509", 
			"videoUploadResponseHeaders": "",
			"imageUploadResponseHeaders": ""  }  */ 
		JSONObject newFirstHighlightJSONObject = new JSONObject(); 
		try { 
			newFirstHighlightJSONObject.put("uploadQueue", 			uploadQueue);
			newFirstHighlightJSONObject.put("shareUrl", 			mediaStorage.mediaShareUrl);
			newFirstHighlightJSONObject.put("mediaId", 				mediaStorage.mediaServerId);
			newFirstHighlightJSONObject.put("shareText", 			mediaStorage.shareText);
			newFirstHighlightJSONObject.put("added", 				"");
			newFirstHighlightJSONObject.put("imagePath", 			mediaStorage.imageServerFilePath);
			newFirstHighlightJSONObject.put("videoPath", 			mediaStorage.videoServerFilePath);  
			newFirstHighlightJSONObject.put("eventId", 				mediaStorage.mediaEventID); 
			newFirstHighlightJSONObject.put("imageLocalFilename", 	mediaStorage.imageLocalFilename);
			newFirstHighlightJSONObject.put("largeImageUrl", 		mediaStorage.uploadQueue);
			newFirstHighlightJSONObject.put("videoLocalPath", 		mediaStorage.videoLocalFilePath);
			newFirstHighlightJSONObject.put("imageLocalPath", 		mediaStorage.imageLocalFilePath);
			newFirstHighlightJSONObject.put("videoLocalFilename", 	mediaStorage.videoLocalFilename);  
			newFirstHighlightJSONObject.put("userLatitude", 		mediaStorage.userLatitude);
			newFirstHighlightJSONObject.put("userLongitude", 		mediaStorage.userLongitude); 
			newFirstHighlightJSONObject.put("mediaDateShortFormat", mediaStorage.mediaDateShortFormat);
			newFirstHighlightJSONObject.put("mediaDateLongFormat", 	mediaStorage.mediaDateLongFormat); 
			newFirstHighlightJSONObject.put("videoUploadResponseHeaders", mediaStorage.videoUploadResponseHeaders);
			newFirstHighlightJSONObject.put("imageUploadResponseHeaders", mediaStorage.imageUploadResponseHeaders);  
			newFirstHighlightJSONObject.put("twitterCardUrl",  		mediaStorage.twitterCardUrl); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		return newFirstHighlightJSONObject;  
	}
	    
	public static void updateSharedPreferencesQueueing(Context context, JSONObject newFirstHighlightJSONObject){  
	   SharedPreferences sharedpreferences = context.getSharedPreferences("com.sportxast.SportXast.highlights", Context.MODE_PRIVATE); 
	   if (sharedpreferences.contains( Constants.sharePrefKey_uploadedHighlights ) )
			{ 
		   	String highlightDataInString = sharedpreferences.getString( Constants.sharePrefKey_uploadedHighlights , "");
		   	 
		   	int firstEntryCounter = 0; 
		   	
		   	String[] arrHighlightsData = highlightDataInString.split("\\|\\|"); 
		   	
		   	if( arrHighlightsData.length > 1 ){
		   		
		   		firstEntryCounter = highlightDataInString.indexOf("||") + 2; 
		   		String updated_highlightData = highlightDataInString.substring(firstEntryCounter); 
		   		
		   		Editor editor = sharedpreferences.edit();
		   		editor.putString(Constants.sharePrefKey_uploadedHighlights, newFirstHighlightJSONObject.toString() +"||"+ updated_highlightData); 
		   		editor.commit();  
		   		//Toast.makeText(getApplicationContext(), "NAA PAY DAGHAN BRAD" + json.toString() , Toast.LENGTH_LONG).show();
		   	
		   	}else if( arrHighlightsData.length == 1 ){
		   		 
		   		Editor editor = sharedpreferences.edit();
		   		editor.putString( Constants.sharePrefKey_uploadedHighlights, newFirstHighlightJSONObject.toString() ); 
		   		editor.commit();   
		   	} 
		}
	}
	
	public static int parseToInteger(String s){
		int i = 0;
		try {
			i= Integer.parseInt(s);
		} catch (Exception e) {
			// TODO: handle exception
			i=0; 
		}
		return i;
	}
	 
	/** return example: "2014-07-04 10:35:06||Fri, 04 Jul 2014 02:35:12 GMT" **/
	@SuppressLint("SimpleDateFormat")
	public static String getDateNowFormatted(){ 
		Date dateNow = new Date(); 
		
		//"2014-07-04 10:35:06\",
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-mm-dd hh:mm:ss" );
		String formattedDate = dateFormat.format( dateNow );
		
		/// mediaDateLongFormat: (example) "Fri, 04 Jul 2014 02:35:12 GMT" 
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");
		String formattedDate2 = dateFormat2.format( dateNow );
		 
		return formattedDate +"||"+ formattedDate2;
	}
			 
	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss formate date as string
	 */ 
	public static String getCurrentTimeStamp(){
		
		long unixTime = System.currentTimeMillis() / 1000L;
		
		//String timestamp = String.valueof( (long) System.currentTimeMillis() / 1000L ) ; 
		return String.valueOf( unixTime); 
	 
	}
	
	
	public static Drawable getDrawableIcon(Context context, String logoPath){
		Drawable drawable = null;
		int color = context.getResources().getColor(R.color.uni_orange);
		Mode mMode = Mode.SRC_ATOP;
		try {
			//String logo = FArrEventLists.get(pos).eventSport.sportLogo;
			String logo = logoPath; 
			String packageName = logo.substring(logo.lastIndexOf("sport/"), logo.indexOf(".png")).replace("sport/", "");
			packageName = packageName.replace(" ", "_");
			//Log.e("sportLogo", ":"+FArrEventLists.get(pos).eventSport.sportLogo);
			//Log.e("packageName", ""+packageName);
			
			int id = context.getResources().getIdentifier(packageName, "drawable", context.getPackageName()); 
			
			drawable = context.getResources().getDrawable(id);
			} 
		catch (Exception e) {
			// TODO: handle exception
			drawable = context.getResources().getDrawable(R.drawable.ic_istoday);
		}
		
		drawable.setColorFilter(color,mMode); 
		return drawable;
	}
 
	/** @param arrClassType:
	 	@param 1-eventSport 
	 	@param 2-eventCoverImageThumb 
	 	@param 3-favoriteCountWithMedia 
	 	@param 4-mediaTagWithHighlight
		@param 5-mediaWithPlayer
 		@param 6-eventFans 
 		@param 7-eventTeams  **/
	
	private static String arrayClassItemToJSONString(Object arrClassObject, int arrClassType){
		
		String newJSONString = ""; 
		
		JSONObject newJSONObject = null;
		
		switch (arrClassType) {
		case 0: 
			String[] eventTags = (String[])arrClassObject;
			
			for (int i = 0; i < eventTags.length; i++) { 
				newJSONString = newJSONString + "||" +  eventTags[i].toString(); 
			}
			
			if(newJSONString.trim().length() >= 2){
				newJSONString = newJSONString.substring(2);
			}
			
			 
			break;
		case 1: 
			newJSONObject = new JSONObject(); 
			try {
				newJSONObject.put("sportId", 			((EventSport)arrClassObject ).sportId); 
				newJSONObject.put("sportName", 			((EventSport)arrClassObject ).sportName);
				newJSONObject.put("sportFirstLetter", 	((EventSport)arrClassObject ).sportFirstLetter);
				newJSONObject.put("sportLogo", 			((EventSport)arrClassObject ).sportLogo);
				newJSONObject.put("sportWhiteLogo", 	((EventSport)arrClassObject ).sportWhiteLogo); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			newJSONString = newJSONObject.toString();
			 
			break;
		case 2: 
		@SuppressWarnings("unchecked")
		ArrayList<ThumbnailImage> arrThumbnailimg = ( ArrayList<ThumbnailImage>)arrClassObject;
			 /*
			for (int i = 0; i < arrThumbnailimg.size(); i++) {
				newJSONString = newJSONString + "||"
								+"{\"mediaId\":\""+			arrThumbnailimg.get(i).mediaId  + "\","
								+"\"coverImageThumb\":\""+	arrThumbnailimg.get(i).coverImageThumb + "\","
								+"\"commentCount\":\""+  	arrThumbnailimg.get(i).commentCount + "\","
								+"\"favoriteCount\":\""+ 	arrThumbnailimg.get(i).favoriteCount  + "\""
								+"}";
			}
			
			if(newJSONString.trim().length() >= 2){
				newJSONString = newJSONString.substring(2);
			} 
			*/
			
			for (int i = 0; i < arrThumbnailimg.size(); i++) {
				newJSONObject = new JSONObject(); 
				try {
					newJSONObject.put("mediaId", 		arrThumbnailimg.get(i).mediaId ); 
					newJSONObject.put("coverImageThumb",arrThumbnailimg.get(i).coverImageThumb ); 
					newJSONObject.put("commentCount",  	arrThumbnailimg.get(i).commentCount ); 
					newJSONObject.put("favoriteCount", 	arrThumbnailimg.get(i).favoriteCount ); 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				newJSONString = newJSONString + "||" +  newJSONObject.toString();
				
			}	
			if(newJSONString.trim().length() >= 2){
				newJSONString = newJSONString.substring(2);
			} 
		
			break;
		case 3:  
			@SuppressWarnings("unchecked")
			ArrayList<FavoriteCountWithMedia> arFavoriteCountWithMedia = (ArrayList<FavoriteCountWithMedia>)arrClassObject; 
			for (int i = 0; i < arFavoriteCountWithMedia.size(); i++) {
				newJSONObject = new JSONObject(); 
				try {
					newJSONObject.put("mediaId", 		arFavoriteCountWithMedia.get(i).mediaId ); 
					newJSONObject.put("coverImageThumb",arFavoriteCountWithMedia.get(i).coverImageThumb ); 
					newJSONObject.put("favoriteCount",  arFavoriteCountWithMedia.get(i).favoriteCount );  
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				newJSONString = newJSONString + "||" +  newJSONObject.toString();
			} 
			if(newJSONString.trim().length() >= 2){
				newJSONString = newJSONString.substring(2);
			}
			
			break;
		case 4:
			@SuppressWarnings("unchecked")
			ArrayList<MediaTagWithHighlight> arrmediaTagWithHighlight = (ArrayList<MediaTagWithHighlight>)arrClassObject; 
			for (int i = 0; i < arrmediaTagWithHighlight.size(); i++) {
				newJSONObject = new JSONObject(); 
				try {
					newJSONObject.put("mediaId", 		arrmediaTagWithHighlight.get(i).mediaId ); 
					newJSONObject.put("coverImageThumb",arrmediaTagWithHighlight.get(i).coverImageThumb ); 
					newJSONObject.put("tagName",  		arrmediaTagWithHighlight.get(i).tagName );  
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				newJSONString = newJSONString + "||" +  newJSONObject.toString();
			}
			
			if(newJSONString.trim().length() >= 2){
				newJSONString = newJSONString.substring(2);
			}
			 
			break;
		case 5:
	
			@SuppressWarnings("unchecked")
			ArrayList<MediaWithPlayer> arrMediaWithPlayer  = (ArrayList<MediaWithPlayer>)arrClassObject; 
			for (int i = 0; i < arrMediaWithPlayer.size(); i++) {
				newJSONObject = new JSONObject(); 
				try {
					newJSONObject.put("id", 		arrMediaWithPlayer.get(i).id ); 
					newJSONObject.put("name",		arrMediaWithPlayer.get(i).name ); 
					newJSONObject.put("avatarPath",	arrMediaWithPlayer.get(i).avatarPath );  
					newJSONObject.put("avatarUrl",	arrMediaWithPlayer.get(i).avatarUrl );  
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				newJSONString = newJSONString + "||" +  newJSONObject.toString();
			}
			
			if(newJSONString.trim().length() >= 2){
				newJSONString = newJSONString.substring(2);
			}
			break;
		case 6:
			@SuppressWarnings("unchecked")
			ArrayList<MediaWithPlayer> arrEventFans  = (ArrayList<MediaWithPlayer>)arrClassObject; 
			for (int i = 0; i < arrEventFans.size(); i++) {
				newJSONObject = new JSONObject(); 
				try {
					newJSONObject.put("id", 		arrEventFans.get(i).id ); 
					newJSONObject.put("name",		arrEventFans.get(i).name ); 
					newJSONObject.put("avatarPath",	arrEventFans.get(i).avatarPath );  
					newJSONObject.put("avatarUrl",	arrEventFans.get(i).avatarUrl );  
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				newJSONString = newJSONString + "||" +  newJSONObject.toString();
			}
			
			if(newJSONString.trim().length() >= 2){
				newJSONString = newJSONString.substring(2);
			}
			
			
			break;
		case 7:
			@SuppressWarnings("unchecked")
			ArrayList<Team> arrEventTeams = (ArrayList<Team>)arrClassObject; 
			for (int i = 0; i < arrEventTeams.size(); i++) {
				newJSONObject = new JSONObject(); 
				try {
					newJSONObject.put("name", 		arrEventTeams.get(i).name ); 
					newJSONObject.put("avatarUrl",	arrEventTeams.get(i).avatarUrl );    
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				newJSONString = newJSONString + "||" +  newJSONObject.toString();
			}
			
			if(newJSONString.trim().length() >= 2){
				newJSONString = newJSONString.substring(2);
			}
			
			break;
		default:
			newJSONString = ""; 
			break;
		}
		  
		return newJSONString;
	}
	
	
	/**EventLists to EventParcel**/
	public static EventParcel parseToEventParcel( EventLists latestEvent ){
		EventParcel eventParcel = null;	
		
		/*[0]*/String eventTags_ 				= arrayClassItemToJSONString( latestEvent.eventTags,			 0 );
		/*[1]*/String eventSport_ 				= arrayClassItemToJSONString( latestEvent.eventSport, 			 1 ); 
		/*[2]*/String eventCoverImageThumb_ 	= arrayClassItemToJSONString( latestEvent.eventCoverImageThumb,  2 ); 
		/*[3]*/String favoriteCountWithMedia_ 	= arrayClassItemToJSONString( latestEvent.favoriteCountWithMedia,3 );
		/*[4]*/String mediaTagWithHighlight_ 	= arrayClassItemToJSONString( latestEvent.mediaTagWithHighlight, 4 );
		/*[5]*/String mediaWithPlayer_			= arrayClassItemToJSONString( latestEvent.mediaWithPlayer, 		 5 );
		/*[6]*/String eventFans_ 				= arrayClassItemToJSONString( latestEvent.eventFans, 			 6 );
		/*[7]*/String eventTeams_ 				= arrayClassItemToJSONString( latestEvent.eventTeams, 			 7 );		
		 
	try {   
		eventParcel = new EventParcel( 
						latestEvent.eventId,
						latestEvent.eventName,
						latestEvent.eventLocation,
						latestEvent.eventLatitude,
						latestEvent.eventLongitude,
						latestEvent.eventStartDate,
						latestEvent.eventStartDateShort,
						latestEvent.eventStartDateFormatted,
						latestEvent.eventSportId,
						latestEvent.eventSportName,
						latestEvent.eventIsOpen, 
						/**VALUES: "LIVE" OR "CHECK IN"**/
						latestEvent.eventIsOpenString, 
						latestEvent.eventIsEnded,
						latestEvent.eventInFavorites, 
						eventTags_,
						
						latestEvent.eventMediaCount,
						latestEvent.eventViewCount,
						latestEvent.eventFavoriteCount,
						latestEvent.eventCommentCount,
						latestEvent.eventShareCount,
						latestEvent.eventScore,
						
						latestEvent.eventDistance,
						latestEvent.eventDistanceUnit,
						latestEvent.eventFirstTeam,
						latestEvent.eventSecondTeam,
						
						//latestEvent.eventCoverImageThumb=""; 
						latestEvent.eventTotalMediaCount,
						latestEvent.eventTotalFavoriteCount,
						latestEvent.eventTotalCommentCount,
						
						latestEvent.eventTotalTagCount,
						latestEvent.eventTotalPlayerCount,
						latestEvent.eventTotalFanCount,
						
						eventSport_,
						eventCoverImageThumb_,
						favoriteCountWithMedia_,
						mediaTagWithHighlight_,
						mediaWithPlayer_,
						eventFans_,
						eventTeams_ 
					);  
			String hey = "";
			String x = hey;
			
 		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return eventParcel; 
	}
	
	/**JSON Object to EventParcel**/
	public static EventParcel parseToEventParcel(JSONObject jsonObject, String eventFirstTeam, String eventSecondTeam){ 
		/*
		sample jsonObject:
		{	"message":"Event was saved",
			"error":0,
			"event":
			{	"eventShareMessage":"Watch this Biathlon event from Cebu City Marriott Hotel captured by SportXast",
				"eventBroadcastRadius":10,
				"eventDateTime":"2014-06-25 15:01:14",
				"eventStartDateShort":"06.25",
				"eventLongitude":"123.908067",
				"eventTeams":"swabe",
				"eventId":"1664",
				"eventSport":
				{	"sportName":"Biathlon",
					"sportId":"5",
					"sportTags":[],
					"sportWhiteLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/white\/biathlon.png",
					"sportFirstLetter":"B",
					"sportLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/biathlon.png",
					"sportBroadcastRadius":"10000"
				},
				"eventTotalMediaCount":null,
				"eventFirstTeam":"swabe",
				"eventInFavorites":0,
				"eventTimeZone":"Asia\/Manila 2014-06-25 15:01:14",
				"eventDateDiff":0,
				"eventBroadcastRadiusUnit":"km",
				"eventShareURL":"http:\/\/goo.gl\/ptsJ03",
				"eventIsEnded":0,
				"eventLatitude":"10.318571",
				"eventLocation":"Cebu City Marriott Hotel",
				"eventTotalFavoriteCount":"0",
				"eventStartDate":"2014-06-25",
				"eventStartDateFormatted":"Wed, Jun 25",
				"eventTotalCommentCount":"0",
				"eventIsOpenString":"CHECK IN",
				"eventLocalDateTime":"2014-06-25 15:01:14",
				"eventSportId":"5",
				"eventDistanceUnit":"km",
				"eventDistance":0,
				"eventName":"Biathlon @ Cebu City Marriott Hotel",
				"eventIsOpen":1,
				"eventSportName":"Biathlon",
				"eventTags":["swabe"]
			}
		} 
		*/
		
		EventParcel eventParcel = null;
		//EventParcel eventParcel = new EventParcel();
		JSONObject JSONObject_event = null;
		try {
			JSONObject_event = jsonObject.getJSONObject("event");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JSONObject JSONObject_eventSport = null;
		try {
			JSONObject_eventSport = JSONObject_event.getJSONObject("eventSport");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	  
		try {    
			String eventMediaCount_ = "0";
			if( JSONObject_event.has("eventMediaCount") ){ 
				eventMediaCount_ = JSONObject_event.getString("eventMediaCount");  
				if( eventMediaCount_.trim().equals("null") || eventMediaCount_ == null )
					eventMediaCount_ = "0";
			}  
			 
			String eventTotalMediaCount_ = "0";
			if( JSONObject_event.has("eventTotalMediaCount") ){ 
				eventTotalMediaCount_ = JSONObject_event.getString("eventTotalMediaCount");  
				if( eventTotalMediaCount_.trim().equals("null") || eventTotalMediaCount_ == null )
					eventTotalMediaCount_ = "0";
			} 
			
			String eventViewCount_ = "0";
			if( JSONObject_event.has("eventViewCount") ){ 
				eventViewCount_ = JSONObject_event.getString("eventViewCount");  
				if( eventViewCount_.trim().equals("null") || eventViewCount_ == null )
					eventViewCount_ = "0";
			} 
			
			String eventFavoriteCount_ = "0";
			if( JSONObject_event.has("eventFavoriteCount") ){ 
				eventFavoriteCount_ = JSONObject_event.getString("eventFavoriteCount");  
				if( eventFavoriteCount_.trim().equals("null") || eventFavoriteCount_ == null )
					eventFavoriteCount_ = "0";
			} 
			 
			String eventCommentCount_ = "0";
			if( JSONObject_event.has("eventCommentCount") ){ 
				eventCommentCount_ = JSONObject_event.getString("eventCommentCount");  
				if( eventCommentCount_.trim().equals("null") || eventCommentCount_ == null )
					eventCommentCount_ = "0";
			} 
			
			String eventShareCount_ = "0";
			if( JSONObject_event.has("eventShareCount") ){ 
				eventShareCount_ = JSONObject_event.getString("eventShareCount");  
				if( eventShareCount_.trim().equals("null") || eventShareCount_ == null )
					eventShareCount_ = "0";
			} 
			
			String eventScore_ = "0";
			if( JSONObject_event.has("eventScore") ){ 
				eventScore_ = JSONObject_event.getString("eventScore");  
				if( eventScore_.trim().equals("null") || eventScore_ == null )
					eventScore_ = "0";
			} 
			
			String eventTotalFavoriteCount_ = "0";
			if( JSONObject_event.has("eventTotalFavoriteCount") ){ 
				eventTotalFavoriteCount_ = JSONObject_event.getString("eventTotalFavoriteCount");  
				if( eventTotalFavoriteCount_.trim().equals("null") || eventTotalFavoriteCount_ == null )
					eventTotalFavoriteCount_ = "0";
			}
			
			String eventTotalCommentCount_ = "0";
			if( JSONObject_event.has("eventTotalCommentCount") ){ 
				eventTotalCommentCount_ = JSONObject_event.getString("eventTotalCommentCount");  
				if( eventTotalCommentCount_.trim().equals("null") || eventTotalCommentCount_ == null )
					eventTotalCommentCount_ = "0";
			}
			
			String eventTotalTagCount_ = "0";
			if( JSONObject_event.has("eventTotalTagCount") ){ 
				eventTotalTagCount_ = JSONObject_event.getString("eventTotalTagCount");  
				if( eventTotalTagCount_.trim().equals("null") || eventTotalTagCount_ == null )
					eventTotalTagCount_ = "0";
			}
			
			String eventTotalPlayerCount_ = "0";
			if( JSONObject_event.has("eventTotalPlayerCount") ){ 
				eventTotalPlayerCount_ = JSONObject_event.getString("eventTotalPlayerCount");  
				if( eventTotalPlayerCount_.trim().equals("null") || eventTotalPlayerCount_ == null )
					eventTotalPlayerCount_ = "0";
			}
			
			String eventTotalFanCount_ = "0";
			if( JSONObject_event.has("eventTotalFanCount") ){ 
				eventTotalFanCount_ = JSONObject_event.getString("eventTotalFanCount");  
				if( eventTotalFanCount_.trim().equals("null") || eventTotalFanCount_ == null )
					eventTotalFanCount_ = "0";
			}
			
			String eventSport_ = "";
			if( JSONObject_event.has("eventSport") ){ 
				eventSport_ = JSONObject_event.getString("eventSport");  
				if( eventSport_.trim().equals("null") || eventSport_ == null )
					eventSport_ = "";
			}
			
			String eventCoverImageThumb_ = "";
			if( JSONObject_event.has("eventCoverImageThumb") ){ 
				eventCoverImageThumb_ = JSONObject_event.getString("eventCoverImageThumb");  
				if( eventCoverImageThumb_.trim().equals("null") || eventCoverImageThumb_ == null )
					eventCoverImageThumb_ = "";
			}
			
			String favoriteCountWithMedia_ = "";
			if( JSONObject_event.has("favoriteCountWithMedia") ){ 
				favoriteCountWithMedia_ = JSONObject_event.getString("favoriteCountWithMedia");  
				if( favoriteCountWithMedia_.trim().equals("null") || favoriteCountWithMedia_ == null )
					favoriteCountWithMedia_ = "";
			}
			
			String mediaTagWithHighlight_ = "";
			if( JSONObject_event.has("mediaTagWithHighlight") ){ 
				mediaTagWithHighlight_ = JSONObject_event.getString("mediaTagWithHighlight");  
				if( mediaTagWithHighlight_.trim().equals("null") || mediaTagWithHighlight_ == null )
					mediaTagWithHighlight_ = "";
			}
			
			String mediaWithPlayer_ = "";
			if( JSONObject_event.has("mediaWithPlayer") ){ 
				mediaWithPlayer_ = JSONObject_event.getString("mediaWithPlayer");  
				if( mediaWithPlayer_.trim().equals("null") || mediaWithPlayer_ == null )
					mediaWithPlayer_ = "";
			}
			
			String eventFans_ = "";
			if( JSONObject_event.has("eventFans") ){ 
				eventFans_ = JSONObject_event.getString("eventFans");  
				if( eventFans_.trim().equals("null") || eventFans_ == null )
					eventFans_ = "";
			}
			
			String eventTeams_ = "";
			if( JSONObject_event.has("eventTeams") ){ 
				eventTeams_ = JSONObject_event.getString("eventTeams");  
				if( eventTeams_.trim().equals("null") || eventTeams_ == null )
					eventTeams_ = "";
			}
			
			eventParcel = new EventParcel(  
					JSONObject_event.getString("eventId"),
					JSONObject_event.getString("eventName"),
					JSONObject_event.getString("eventLocation"),
					JSONObject_event.getString("eventLatitude"),
					JSONObject_event.getString("eventLongitude"),
					JSONObject_event.getString("eventStartDate"),
					JSONObject_event.getString("eventStartDateShort"),
					JSONObject_event.getString("eventStartDateFormatted"),
					JSONObject_eventSport.getString("sportId"),
					JSONObject_event.getString("eventSportName"),
					JSONObject_event.getString("eventIsOpen"),
					/**VALUES: "LIVE" OR "CHECK IN"**/
					JSONObject_event.getString("eventIsOpenString"),
					JSONObject_event.getString("eventIsEnded"),
					JSONObject_event.getString("eventInFavorites"), 
					JSONObject_event.getString("eventTags"),
					
					eventMediaCount_,
					eventViewCount_,
					eventFavoriteCount_,
					eventCommentCount_,
					eventShareCount_,
					eventScore_,
					
					JSONObject_event.getString("eventDistance"),
					JSONObject_event.getString("eventDistanceUnit"),
					    
					eventFirstTeam, 
					eventSecondTeam,
					/*
					JSONObject_event.getString("eventFirstTeam"),
					JSONObject_event.getString("eventSecondTeam"),
					*/ 
					eventTotalMediaCount_,  
					eventTotalFavoriteCount_,
					eventTotalCommentCount_,
					
					eventTotalTagCount_,
					eventTotalPlayerCount_,
					eventTotalFanCount_,
					
					eventSport_,
					eventCoverImageThumb_,
					favoriteCountWithMedia_,
					mediaTagWithHighlight_,
					mediaWithPlayer_,
					eventFans_,
					eventTeams_
			); 
			
			String hey = "";
			String x = hey;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 
		return eventParcel;
	}
	
	
	public static JSONObject parseNextMediaIdObject(
			JSONObject jsonObject,
			String eventId, 
			String imageLocalFilename, 
			String largeImageUrl, 
			String videoLocalPath, 
			String imageLocalPath,
			String videoLocalFilename ){ 
		 
		/*sample Jason output:
		 {	"shareUrl":"http:\/\/goo.gl\/1GdDMn",
		 	"mediaId":"11929",
		 	"shareText":"Watch this highlight captured by SportXast",
		 	"added":"new media added",
		 	"imagePath":"db\/event_media\/2014\/06\/02\/11929.jpg",
		 	"videoPath":"db\/event_media\/2014\/06\/02\/11929.mp4" } */
		
		try { 
			jsonObject.put("uploadQueue", 	 	"0");
			jsonObject.put("eventId", 			eventId);
			jsonObject.put("imageLocalFilename", imageLocalFilename);
			
			jsonObject.put("largeImageUrl", 	largeImageUrl);
			jsonObject.put("videoLocalPath",	videoLocalPath);
			jsonObject.put("imageLocalPath",	imageLocalPath); 
			jsonObject.put("videoLocalFilename",videoLocalFilename);  
			
			jsonObject.put("userLatitude",		""+GlobalVariablesHolder.user_Latitude); 
			jsonObject.put("userLongitude", 	""+GlobalVariablesHolder.user_Longitude);  
			 
			String[] dateFormats = CommonFunctions_1.getDateNowFormatted().trim().split("\\|\\|");
			jsonObject.put("mediaDateShortFormat",	dateFormats[0].toString() ); //format should be "2014-07-04 10:35:06"
			jsonObject.put("mediaDateLongFormat",	dateFormats[1].toString() ); //format should be "Fri, 04 Jul 2014 02:35:12 GMT" 
			
			jsonObject.put("videoUploadResponseHeaders", "");
			jsonObject.put("imageUploadResponseHeaders", "");  
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		return jsonObject; 
	}  
 
	public static NextMediaID parseNextMediaIDList( JSONObject jsonObject ){   
		/* 
		newFirstHighlightJSONObject.put("uploadQueue", 			mediaStorage.uploadQueue);
		newFirstHighlightJSONObject.put("shareUrl", 			mediaStorage.mediaShareUrl);
		newFirstHighlightJSONObject.put("mediaId", 				mediaStorage.mediaServerId);
		newFirstHighlightJSONObject.put("shareText", 			mediaStorage.shareText);
		newFirstHighlightJSONObject.put("added", 				"");
		newFirstHighlightJSONObject.put("imagePath", 			mediaStorage.imageServerFilePath);
		newFirstHighlightJSONObject.put("videoPath", 			mediaStorage.videoServerFilePath);  
		newFirstHighlightJSONObject.put("eventId", 				mediaStorage.mediaEventID); 
		newFirstHighlightJSONObject.put("imageLocalFilename", 	mediaStorage.imageLocalFilename);
		newFirstHighlightJSONObject.put("largeImageUrl", 		mediaStorage.uploadQueue);
		newFirstHighlightJSONObject.put("videoLocalPath", 		mediaStorage.videoLocalFilePath);
		newFirstHighlightJSONObject.put("imageLocalPath", 		mediaStorage.imageLocalFilePath);
		newFirstHighlightJSONObject.put("videoLocalFilename", 	mediaStorage.videoLocalFilename);  
		newFirstHighlightJSONObject.put("userLatitude", 		mediaStorage.userLatitude);
		newFirstHighlightJSONObject.put("userLongitude", 		mediaStorage.userLongitude); 
		newFirstHighlightJSONObject.put("mediaDateShortFormat", mediaStorage.mediaDateShortFormat);
		newFirstHighlightJSONObject.put("mediaDateLongFormat", 	mediaStorage.mediaDateLongFormat); 
		newFirstHighlightJSONObject.put("videoUploadResponseHeaders", mediaStorage.videoUploadResponseHeaders);
		newFirstHighlightJSONObject.put("imageUploadResponseHeaders", mediaStorage.imageUploadResponseHeaders);  
		newFirstHighlightJSONObject.put("twitterCardUrl",  		mediaStorage.twitterCardUrl); 
		*/
		
		NextMediaID nextMediaID = new NextMediaID();	
		try { 
			nextMediaID.setUploadQueue(			jsonObject.getString("uploadQueue") ); 
			nextMediaID.setShareUrl(			jsonObject.getString("shareUrl") );
			nextMediaID.setMediaId( 			jsonObject.getString("mediaId")  );
			nextMediaID.setShareText( 			jsonObject.getString("shareText"));
			nextMediaID.setAdded(				jsonObject.getString("added") 	 );
			nextMediaID.setImageServerPath( 	jsonObject.getString("imagePath")); //image server path
			nextMediaID.setVideoServerPath( 	jsonObject.getString("videoPath")); //video server path
		//#######################################################################	 
			
			if(jsonObject.has("eventId")) 
				 nextMediaID.setEventId( 		jsonObject.getString("eventId"));
			else nextMediaID.setEventId("");
		 
			nextMediaID.setImageLocalFilename(	jsonObject.getString("imageLocalFilename"));
			nextMediaID.setLargeImageUrl(		jsonObject.getString("largeImageUrl"));
			nextMediaID.setVideoLocalPath(		jsonObject.getString("videoLocalPath"));
			nextMediaID.setImageLocalPath(  	jsonObject.getString("imageLocalPath")); 
			nextMediaID.setVideoLocalFilename( 	jsonObject.getString("videoLocalFilename"));
			 
			nextMediaID.setUserLatitude( 		jsonObject.getString("userLatitude"));
			nextMediaID.setUserLongitude( 		jsonObject.getString("userLongitude"));
			
			nextMediaID.setMediaDateShortFormat(jsonObject.getString("mediaDateShortFormat"));
			nextMediaID.setMediaDateLongFormat( jsonObject.getString("mediaDateLongFormat"));
	 
			nextMediaID.setVideoUploadResponseHeaders(	jsonObject.getString("videoUploadResponseHeaders"));
			nextMediaID.setImageUploadResponseHeaders(	jsonObject.getString("imageUploadResponseHeaders"));  
			nextMediaID.setTwitterCardUrl(				jsonObject.getString("twitterCardUrl"));
			  
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 
		return nextMediaID; 
	}
	 
	 
	public static ArrayList<EventLists> parseEvents(JSONObject jsonObject){ 
		//ArrayList<EventLists> arrEventList = new ArrayList<_EventLists.EventLists>();
		ArrayList<EventLists> arrEventList = new ArrayList<EventLists>();
		
		//isFirstLoad = false;
		int evenList_length=0;
		 
		try {
			String error   = jsonObject.get("error").toString();
			String message = jsonObject.get("message").toString();
		} catch (JSONException e) {
			e.printStackTrace();
			}
		
		
		JSONArray jsonArray_eventList = new JSONArray();
		
		try {
			String resultCount = ""+jsonObject.get("resultCount");
			String pageCount   = ""+jsonObject.get("pageCount");
			String currentPage = ""+jsonObject.get("currentPage"); 
		
		} catch (JSONException e) {e.printStackTrace();}
		 
		try {
			
			jsonArray_eventList = jsonObject.getJSONArray("nearByEvents");
			evenList_length = jsonArray_eventList.length();
		
		} catch (JSONException e) {e.printStackTrace();}
		
		
		try {
		
			jsonArray_eventList = jsonObject.getJSONArray("eventList");
			evenList_length = jsonArray_eventList.length();
		
		} catch (JSONException e) {e.printStackTrace();}
		
	//	Log.e("events", ""+currentPage+"=="+onPage);
		
			
		
			for (int i = 0; i < evenList_length; i++) {
					JSONObject jsObject_eList = new JSONObject();
					EventLists eLists = new EventLists();
					try {
						 jsObject_eList = jsonArray_eventList.getJSONObject(i);
						 Log.e("jsObject_eList: ", jsObject_eList.toString());
						 Log.e("jsObject_eList: ", jsObject_eList.toString());
						 Log.e("jsObject_eList: ", jsObject_eList.toString());
						 eLists = new EventLists();
						 
						eLists.eventId		 		= ""+jsObject_eList.get("eventId"); 
						String eventName_ 	= ""+jsObject_eList.get("eventName");
						eLists.eventName 			= ""+jsObject_eList.get("eventName");
						
						String eventLoc = ""+jsObject_eList.get("eventLocation");
						eLists.eventLocation 		= ""+jsObject_eList.get("eventLocation");
						eLists.eventLatitude 		= ""+jsObject_eList.get("eventLatitude");
						eLists.eventLongitude 		= ""+jsObject_eList.get("eventLongitude");
						
						String ddate = ""+jsObject_eList.get("eventStartDate");
						eLists.eventStartDate 		= ""+jsObject_eList.get("eventStartDate");
						eLists.eventStartDateShort 	= ""+jsObject_eList.get("eventStartDateShort");
						eLists.eventStartDateFormatted = ""+jsObject_eList.get("eventStartDateFormatted");
						eLists.eventSportId 		= ""+jsObject_eList.get("eventSportId");
						eLists.eventSportName 		= ""+jsObject_eList.get("eventSportName");
						String hey = ""+jsObject_eList.get("eventIsOpen");
						eLists.eventIsOpen 			= ""+jsObject_eList.get("eventIsOpen");
						   
						String isEnded = ""+jsObject_eList.get("eventIsEnded");
						eLists.eventIsEnded  		= ""+jsObject_eList.get("eventIsEnded");
						 
						eLists.eventTotalMediaCount = ""+jsObject_eList.get("eventTotalMediaCount");
						eLists.eventTotalCommentCount= ""+jsObject_eList.get("eventTotalCommentCount");
						eLists.eventTotalFavoriteCount = ""+jsObject_eList.get("eventTotalFavoriteCount");
						eLists.eventTotalTagCount = ""+jsObject_eList.get("eventTotalTagCount");
						eLists.eventTotalPlayerCount = ""+jsObject_eList.get("eventTotalPlayerCount");
						eLists.eventTotalFanCount = ""+jsObject_eList.get("eventTotalFanCount");
						
						eLists.eventMediaCount  = ""+jsObject_eList.get("eventMediaCount");
						eLists.eventViewCount  = ""+jsObject_eList.get("eventViewCount");
						eLists.eventFavoriteCount  = ""+jsObject_eList.get("eventFavoriteCount");
						eLists.eventCommentCount  = ""+jsObject_eList.get("eventCommentCount");
						eLists.eventShareCount  = ""+jsObject_eList.get("eventShareCount");
						eLists.eventScore  = ""+jsObject_eList.get("eventScore");	
						
						
						
					} catch (JSONException e) {e.printStackTrace();}
					
					
					try {
						for (String string : jsonObject.getString("eventTeams").split("-")) {
							Team team = new Team();
							team.name = string;
							eLists.eventTeams.add(team);
						}
					
					} catch (JSONException e) {
						// TODO: handle exception
					}
					
					try {
							eLists.eventFirstTeam = ""+jsObject_eList.get("eventFirstTeam");
					} catch (JSONException e) {}
					
					try {
						eLists.eventSecondTeam = ""+jsObject_eList.get("eventSecondTeam");
					} catch (JSONException e) {}
					
					try {
						
						JSONArray arr_eventCoverImageThumb = jsObject_eList.getJSONArray("eventCoverImageThumb");
						for (int j = 0; j < arr_eventCoverImageThumb.length(); j++) {
							JSONObject object = arr_eventCoverImageThumb.getJSONObject(j);
							ThumbnailImage image = new ThumbnailImage();
							image.mediaId = object.get("mediaId").toString();
							image.coverImageThumb = object.get("coverImageThumb").toString();
							image.favoriteCount = object.get("favoriteCount").toString();
							image.commentCount = object.get("commentCount").toString();
							
							eLists.eventCoverImageThumb.add(image);
						}
						
					//	eLists.eventCoverImageThumb = ""+jsObject_eList.get("eventCoverImageThumb");
					} catch (JSONException e) {}
					
					
					try {
						
						JSONArray arr_favoriteCountWithMedia = jsObject_eList.getJSONArray("favoriteCountWithMedia");
						for (int j = 0; j < arr_favoriteCountWithMedia.length(); j++) {
							JSONObject object = arr_favoriteCountWithMedia.getJSONObject(j);
							FavoriteCountWithMedia countWithMedia = new FavoriteCountWithMedia();
							countWithMedia.mediaId = object.get("mediaId").toString();
							countWithMedia.coverImageThumb = object.get("coverImageThumb").toString();
							countWithMedia.favoriteCount = object.get("favoriteCount").toString();
							
							eLists.favoriteCountWithMedia.add(countWithMedia);
							
						}
						
					} catch (JSONException e) {}

					
					try {
						
						JSONArray arr_mediaTagWithHighlight = jsObject_eList.getJSONArray("mediaTagWithHighlight");
						for (int j = 0; j < arr_mediaTagWithHighlight.length(); j++) {
							JSONObject object = arr_mediaTagWithHighlight.getJSONObject(j);
							MediaTagWithHighlight tagWithHighlight = new MediaTagWithHighlight();
							tagWithHighlight.mediaId = object.get("mediaId").toString();
							tagWithHighlight.coverImageThumb = object.get("coverImageThumb").toString();
							tagWithHighlight.tagName = object.get("tagName").toString();
							
							eLists.mediaTagWithHighlight.add(tagWithHighlight);
							
						}
						
					} catch (JSONException e) {}
					
					
					try {
						
						JSONArray arr_mediaWithPlayer = jsObject_eList.getJSONArray("mediaWithPlayer");
						for (int j = 0; j < arr_mediaWithPlayer.length(); j++) {
							JSONObject object = arr_mediaWithPlayer.getJSONObject(j);
							MediaWithPlayer player = new MediaWithPlayer();
							player.id = object.get("id").toString();
							player.name = object.get("name").toString();
							player.avatarPath = object.get("avatarPath").toString();
							player.avatarUrl = object.get("avatarUrl").toString();
							
							eLists.mediaWithPlayer.add(player);
							
						}
						
					} catch (JSONException e) {}

					
					try {
						
						JSONArray arr_mediaWithPlayer = jsObject_eList.getJSONArray("eventFans");
						for (int j = 0; j < arr_mediaWithPlayer.length(); j++) {
							JSONObject object = arr_mediaWithPlayer.getJSONObject(j);
							MediaWithPlayer player = new MediaWithPlayer();
							player.id = object.get("id").toString();
							player.name = object.get("name").toString();
							player.avatarPath = object.get("avatarPath").toString();
							player.avatarUrl = object.get("avatarUrl").toString();
							
							eLists.eventFans.add(player);
							
						}
						
					} catch (JSONException e) {}
					

					try {
					
						JSONArray jsonArray_eventags = jsObject_eList.getJSONArray("eventTags");
						eLists.eventTags = new String[jsonArray_eventags.length()];
						for (int j = 0; j < jsonArray_eventags.length(); j++) {
							eLists.eventTags[j] = ""+jsonArray_eventags.get(j);
						}

						Log.v("toString_array",""+eLists.eventTags.length);
						
						
						
					} catch (JSONException e) {e.printStackTrace();}
					
					try {
						
						JSONObject jsonObject_evensport = jsObject_eList.getJSONObject("eventSport");
						eLists.eventSport =new EventSport();
						
						eLists.eventSport.sportId = ""+jsonObject_evensport.get("sportId");
						eLists.eventSport.sportName = ""+jsonObject_evensport.get("sportName");
						eLists.eventSport.sportFirstLetter = ""+jsonObject_evensport.get("sportFirstLetter");
						eLists.eventSport.sportLogo = ""+jsonObject_evensport.get("sportLogo");
						eLists.eventSport.sportWhiteLogo = ""+jsonObject_evensport.get("sportWhiteLogo");
							
						
						
						
					} catch (JSONException e) {e.printStackTrace();}
					
					/*
					if(isRefresh){
						eventLists.add(i,eLists);
					}
					else{
						eventLists.add(eLists);
					}
					*/
					
					arrEventList.add(eLists);
					
			} 
			
			return arrEventList;
	
	}

	public static ArrayList<_SearchUser> parseSearchUser(JSONObject jObject_response){
		ArrayList<_SearchUser> UserList = new ArrayList<_SearchUser>();
		//FUserList = new ArrayList<Object>();
		try {
			if(Integer.parseInt(jObject_response.getString("resultCount"))>0){
				JSONArray jArray_user = jObject_response.getJSONArray("list");
				
				for (int i = 0; i < jArray_user.length(); i++) {
					JSONObject jObject_user = jArray_user.getJSONObject(i);
					_SearchUser searchUser = new _SearchUser();
					
					searchUser.userId 		= ""+jObject_user.get("userId");
					searchUser.userName 	= ""+jObject_user.get("userName");
					searchUser.fullName 	= ""+jObject_user.get("fullName");
					searchUser.aboutMe 		= ""+jObject_user.get("aboutMe");
					searchUser.avatarPath 	= ""+jObject_user.get("avatarPath");
					searchUser.type	 		= ""+jObject_user.get("type");
					searchUser.isFollowing 	= ""+jObject_user.get("isFollowing");
				
					UserList.add(searchUser);
					 
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 return UserList;
	}
/*
	public void parseSearchUserORIGINAL(JSONObject jObject_response){
		FUserList = new ArrayList<Object>();
		try {
			if(Integer.parseInt(jObject_response.getString("resultCount"))>0){
				JSONArray jArray_user = jObject_response.getJSONArray("list");
				
				for (int i = 0; i < jArray_user.length(); i++) {
					JSONObject jObject_user = jArray_user.getJSONObject(i);
					_SearchUser searchUser = new _SearchUser();
					
					searchUser.userId 		= ""+jObject_user.get("userId");
					searchUser.userName 	= ""+jObject_user.get("userName");
					searchUser.fullName 	= ""+jObject_user.get("fullName");
					searchUser.aboutMe 		= ""+jObject_user.get("aboutMe");
					searchUser.avatarPath 	= ""+jObject_user.get("avatarPath");
					searchUser.type	 		= ""+jObject_user.get("type");
					searchUser.isFollowing 	= ""+jObject_user.get("isFollowing");
				
					FUserList.add(searchUser);
					 
				}
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FPullToRefreshListView.setAdapter( new SearchUserAdapter(this,"user", FUserList));	 
	}
	*/
	public static ArrayList<_SearchHashtag> parseSearchHashtag(JSONObject jObject_response){
		ArrayList<_SearchHashtag> HashtagList = new ArrayList<_SearchHashtag>();
		try {
			if(Integer.parseInt(jObject_response.getString("resultCount"))>0){
				JSONArray jArray_hashtag = jObject_response.getJSONArray("list");
				
				for (int i = 0; i < jArray_hashtag.length(); i++) {
					JSONObject jObject_hashtag = jArray_hashtag.getJSONObject(i);
					_SearchHashtag searchHashtag = new _SearchHashtag();
					
					searchHashtag.id = ""+jObject_hashtag.get("id");
					searchHashtag.name = ""+jObject_hashtag.get("name");
					searchHashtag.avatarPath = ""+jObject_hashtag.get("avatarPath");
					searchHashtag.type = ""+jObject_hashtag.get("type");
					
					HashtagList.add(searchHashtag); 
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return HashtagList;
	}
	
	/*
	public void parseSearchHashtagORIGNAL(JSONObject jObject_response){
		FHashtagList = new ArrayList<_SearchHashtag>();
		try {
			if(Integer.parseInt(jObject_response.getString("resultCount"))>0){
				JSONArray jArray_hashtag = jObject_response.getJSONArray("list");
				
				for (int i = 0; i < jArray_hashtag.length(); i++) {
					JSONObject jObject_hashtag = jArray_hashtag.getJSONObject(i);
					_SearchHashtag searchHashtag = new _SearchHashtag();
					
					searchHashtag.id = ""+jObject_hashtag.get("id");
					searchHashtag.name = ""+jObject_hashtag.get("name");
					searchHashtag.avatarPath = ""+jObject_hashtag.get("avatarPath");
					searchHashtag.type = ""+jObject_hashtag.get("type");
					
					FHashtagList.add(searchHashtag);
					
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		FPullToRefreshListView.setAdapter( new SearchHashtagAdapter(this, FHashtagList));	
	}
	*/
	public static ArrayList<MediaList> parseMediaList(JSONObject jsonObject){

		ArrayList<MediaList> arrMediaLists = new ArrayList<_MediaLists.MediaList>();
		
		//Log.e("parseMediaList", "parseMediaList"); 
		//isFirstLoad=false;  
		boolean isEmpty = true;
		
			try {
				isEmpty = jsonObject.getJSONArray("mediaList") == null? true:false;
			//	Log.e("isEmpty", isEmpty?"true":"false");
			} catch (JSONException e) {e.printStackTrace();}
 
			    if(isEmpty){ 
					//do nothing
			    	try {
			    		
			    		String error   = jsonObject.getString("error");
						//message = jsonObject.getString("message"); 
					} catch (JSONException e) { 
						e.printStackTrace();
						
					}

					//isEmpty=true;
				}
				else{
					 
					int length = 0;
					JSONArray jsonArray_mediaList = new JSONArray();
					//Log.e("naa", "size : "+jsonArray_mediaList.length());
					
					try {
						JSONArray jsonArray_eventInfo= jsonObject.getJSONArray("eventInfo");
					} catch (JSONException e1) {	e1.printStackTrace();		}
					
					String resultCount = "0";
					String pageCount = "0";
					String currentPage = "0";
					try {
						jsonArray_mediaList	= jsonObject.getJSONArray("mediaList");
						
						resultCount 		= jsonObject.getString("resultCount");
						pageCount 			= ""+jsonObject.get("pageCount");
						currentPage 		= ""+jsonObject.get("currentPage"); 
						isEmpty 			= Integer.parseInt(resultCount) > 0? true:false;
						 
					} catch (JSONException e) {
						e.printStackTrace();
						}
					 
					length = jsonArray_mediaList.length(); 
					for (int i = 0; i < length; i++) {
						MediaList mediaList = new MediaList();
						JSONObject m = new JSONObject();
						try {
							m = jsonArray_mediaList.getJSONObject(i);
						 
						//	Log.e("medialist["+i+"]", ""+m.toString()); 
							mediaList.mediaId 		= m.getString("mediaId");
							mediaList.eventId 		= m.getString("eventId");
							mediaList.sportId 		= m.getString("sportId");
							mediaList.mediaUserId 	= m.getString("mediaUserId");
							mediaList.mediaUserName = m.getString("mediaUserName");
							mediaList.mediaType 	= m.getString("mediaType");
							mediaList.coverImage 	= m.getString("coverImage");
							mediaList.coverImageThumb=m.getString("coverImageThumb");
							mediaList.largeImageUrl = m.getString("largeImageUrl");
							mediaList.mediumImageUrl= m.getString("mediumImageUrl");
							mediaList.smallImageUrl = m.getString("smallImageUrl");
							mediaList.mediaUrl 		= m.getString("mediaUrl");
							mediaList.mediaShortUrl = m.getString("mediaShortUrl");
							mediaList.m3u8Url 		= m.getString("m3u8Url");
							mediaList.vp8Url 		= m.getString("vp8Url");
							mediaList.mp4Url 		= m.getString("mp4Url");
							mediaList.videoLocalPath= m.getString("videoLocalPath");
							mediaList.imageLocalPath= m.getString("imageLocalPath");
							mediaList.mediaShareString= m.getString("mediaShareString");
							mediaList.onDate 		= m.getString("onDate");
							mediaList.age 			= m.getString("age");
							mediaList.viewCount 	= ""+m.get("viewCount");
							mediaList.favoritesCount= m.getString("favoritesCount");
							mediaList.commentsCount = m.getString("commentsCount");
							mediaList.shareCount 	= ""+m.get("shareCount");
							mediaList.score 		= m.getString("score");
							mediaList.currentUserIsOwner = ""+m.get("currentUserIsOwner");
							mediaList.currentUserHasInFavorites =  ""+m.get("currentUserHasInFavorites");
							mediaList.transcoderJobStatus = ""+m.get("transcoderJobStatus");
                            mediaList.twitterCardUrl = "" + m.get("twitterCardUrl");

                            //	aq.cache( m.getString("coverImage"), 0);
						} catch (JSONException e) {
							e.printStackTrace();
							}
						
						JSONObject u =new JSONObject();
						
						try { 
							u = m.getJSONObject("user");
							mediaList.user = new User();
							mediaList.user.userId  = u.getString("userId");
							mediaList.user.avatarPath  = u.getString("avatarPath");
							mediaList.user.avatarUrl  = u.getString("avatarUrl");
							mediaList.user.hasAvatar  = ""+u.get("hasAvatar");
							mediaList.user.avatarCount  = ""+u.get("avatarCount");
							mediaList.user.avatarName  = u.getString("avatarName");
							mediaList.user.fullName  = u.getString("fullName");
							mediaList.user.displayName  = u.getString("displayName");
							mediaList.user.userName  = u.getString("userName");
							mediaList.user.aboutMe  = u.getString("aboutMe");
							mediaList.user.postCount  = u.getString("postCount");
							mediaList.user.favoriteCount  = u.getString("favoriteCount");
							mediaList.user.viewCount  = u.getString("viewCount");
							
							Log.v("user","user :"+ u.toString());
							
						} catch (JSONException e) {e.printStackTrace();}
						 
//						try {
//							JSONObject jsonObject_followCount = u.getJSONObject("followCounts");
//							mediaList.user.followCounts = new FollowCounts();
//							mediaList.user.followCounts.follower = ""+jsonObject_followCount.get("follower");
//							mediaList.user.followCounts.following = ""+jsonObject_followCount.get("following");
//							
//						} catch (JSONException e) {e.printStackTrace();}
						 
						mediaList.comments = new ArrayList<Comments>();
						mediaList.comments = parseComments(m);
						
						mediaList.userInFavorites = new ArrayList<UserInFavorites>();
						mediaList.userInFavorites = parseUserInFavorites(m);
						
						
						if(mediaList.userInFavorites.size() <= 0){
							mediaList.currentUserHasInFavorites = "";
							}
						 
						mediaList.tags = new ArrayList<Tag>();
						mediaList.tags = parseTags(m);
							
						/*
						if(isRefresh){
							arrMediaLists.add(i, mediaList);
						}else{
							arrMediaLists.add(mediaList);
						}	
						*/
						arrMediaLists.add(mediaList);
						
					}
					//Log.v("end", "end");
				}	
		
			    
		return arrMediaLists;	    
		
	}
	
	
	public static ArrayList<MediaList> parseMediaListORIGINALXXXX(JSONObject jsonObject){
		 
		FMediaLists = new ArrayList<_MediaLists.MediaList>();
		
		boolean isEmpty	  = false;
		String errorTitle = "";
		String errMessage = "";
		 
		String resultCount 	= "0";
		String pageCount	= "0";
		String currentPage 	= "0";
		  
	//	Log.e("parseMediaList", "parseMediaList");  
	 
		try {
			isEmpty = jsonObject.getJSONArray("mediaList")==null?true:false;
		//	Log.e("isEmpty", isEmpty?"true":"false");
		} catch (JSONException e) {
			e.printStackTrace();
			}
  
		    if(isEmpty){
				//do nothing
		    	try { 
		    		errorTitle = jsonObject.getString("error");
					errMessage = jsonObject.getString("message");
					}
		    	catch (JSONException e){
		    		e.printStackTrace();
		    		} 
				//isEmpty=true;
			}
			else{
				 
				int length = 0;
				JSONArray jsonArray_mediaList = new JSONArray();
				//Log.e("naa", "size : "+jsonArray_mediaList.length());
				
				try {
					
					JSONArray jsonArray_eventInfo = jsonObject.getJSONArray("eventInfo");
					
					} catch (JSONException e1) {
						e1.printStackTrace();		
						}
				
				try {
					
					jsonArray_mediaList= jsonObject.getJSONArray("mediaList");
					resultCount = jsonObject.getString("resultCount");
					pageCount 	= ""+jsonObject.get("pageCount");
					currentPage = ""+jsonObject.get("currentPage"); 
					isEmpty 	= Integer.parseInt( resultCount ) > 0?true:false;
					
				} catch (JSONException e) {
					e.printStackTrace();
					}
				 
				length = jsonArray_mediaList.length();
				
				//mediaLists = new ArrayList<MediaList>();  
				for (int i = 0; i < length; i++) {
					MediaList mediaList = new MediaList();
					JSONObject m = new JSONObject();
					try {
						m = jsonArray_mediaList.getJSONObject(i);
					 
						mediaList.mediaId 			= m.getString("mediaId");
						mediaList.eventId 			= m.getString("eventId");
						mediaList.sportId 			= m.getString("sportId");
						mediaList.mediaUserId 		= m.getString("mediaUserId");
						mediaList.mediaUserName 	= m.getString("mediaUserName");
						mediaList.mediaType			= m.getString("mediaType");
						mediaList.coverImage		= m.getString("coverImage");
						mediaList.coverImageThumb 	= m.getString("coverImageThumb");
						mediaList.largeImageUrl 	= m.getString("largeImageUrl");
						mediaList.mediumImageUrl	= m.getString("mediumImageUrl");
						mediaList.smallImageUrl 	= m.getString("smallImageUrl");
						mediaList.mediaUrl 			= m.getString("mediaUrl");
						mediaList.mediaShortUrl 	= m.getString("mediaShortUrl");
						mediaList.m3u8Url 			= m.getString("m3u8Url");
						mediaList.vp8Url 			= m.getString("vp8Url");
						mediaList.mp4Url 			= m.getString("mp4Url");
						mediaList.videoLocalPath	= m.getString("videoLocalPath");
						mediaList.imageLocalPath	= m.getString("imageLocalPath");
						mediaList.mediaShareString 	= m.getString("mediaShareString");
						mediaList.onDate 			= m.getString("onDate");
						mediaList.age 				= m.getString("age");
						mediaList.viewCount	 		= ""+m.get("viewCount");
						mediaList.favoritesCount	= m.getString("favoritesCount");
						mediaList.commentsCount 	= m.getString("commentsCount");
						mediaList.shareCount 		= ""+m.get("shareCount");
						mediaList.score 			= m.getString("score");
						mediaList.currentUserIsOwner= ""+m.get("currentUserIsOwner");
						mediaList.currentUserHasInFavorites = ""+m.get("currentUserHasInFavorites");
						mediaList.transcoderJobStatus= ""+m.get("transcoderJobStatus");
                        mediaList.twitterCardUrl = "" + m.get("twitterCardUrl");
						
						//aq.cache( m.getString("coverImage"), 0); 
						
					} catch (JSONException e) {
						e.printStackTrace();
						}
					
					JSONObject u = new JSONObject();
					try {
						u = m.getJSONObject("user");
						mediaList.user = new User();
						mediaList.user.userId		= u.getString("userId");
						mediaList.user.avatarPath 	= u.getString("avatarPath");
						mediaList.user.avatarUrl  	= u.getString("avatarUrl");
						mediaList.user.hasAvatar  	= ""+u.get("hasAvatar");
						mediaList.user.avatarCount  = ""+u.get("avatarCount");
						mediaList.user.avatarName  	= u.getString("avatarName");
						mediaList.user.fullName  	= u.getString("fullName");
						mediaList.user.displayName  = u.getString("displayName");
						mediaList.user.userName  	= u.getString("userName");
						mediaList.user.aboutMe  	= u.getString("aboutMe");
						mediaList.user.postCount  	= u.getString("postCount");
						mediaList.user.favoriteCount= u.getString("favoriteCount");
						mediaList.user.viewCount  	= u.getString("viewCount");
					
						Log.v("user","user :"+ u.toString());
						
					} catch (JSONException e) {
						e.printStackTrace();
						}
					 
						mediaList.comments = new ArrayList<Comments>();
						mediaList.comments = parseComments(m);
			
						mediaList.userInFavorites = new ArrayList<UserInFavorites>();
						mediaList.userInFavorites = parseUserInFavorites(m);
						
						mediaList.tags = new ArrayList<Tag>();
						mediaList.tags = parseTags(m);
						 
						FMediaLists.add(mediaList);
					/*	
					if(isRefresh){
						mediaLists.add(i,mediaList);
					}else{
						mediaLists.add(mediaList);
					}
					
					*/
					
				}
				//Log.v("end", "end");
			}
		    
		    return FMediaLists; 
	}

	
	public static ArrayList<Comments> parseComments(JSONObject json){
		ArrayList<Comments> m_comment = new ArrayList<Comments>();
		
		try {
			JSONArray c = json.getJSONArray("comments");
			for (int j = 0; j < c.length(); j++) {
				JSONObject json_c = c.getJSONObject(j);
				Comments comment = new Comments();
				comment.commentId = json_c.getString("commentId");
				comment.commentParentID = json_c.getString("commentParentID");
				comment.commentParentType = json_c.getString("commentParentType");
				comment.commentBody = json_c.getString("commentBody");
				comment.commentAge = json_c.getString("commentAge");
				comment.commentUserName = json_c.getString("commentUserName");
				comment.commentUserId = json_c.getString("commentUserId");
				comment.commentTags = json_c.getString("commentTags");
			 
				//Log.v("comment", ""+json_c); 
				
				JSONObject json_u = json_c.getJSONObject("user");
			
				comment.user = new User();
				comment.user.userId  = json_u.getString("userId");
				comment.user.avatarPath  = json_u.getString("avatarPath");
				comment.user.avatarUrl  = json_u.getString("avatarUrl");
				comment.user.hasAvatar  = ""+json_u.get("hasAvatar");
				comment.user.avatarCount  = ""+json_u.get("avatarCount");
				comment.user.avatarName  = json_u.getString("avatarName");
				comment.user.fullName  = json_u.getString("fullName");
				comment.user.displayName  = json_u.getString("displayName");
				comment.user.userName  = json_u.getString("userName");
				comment.user.aboutMe  = json_u.getString("aboutMe");
				comment.user.postCount  = json_u.getString("postCount");
				comment.user.favoriteCount  = json_u.getString("favoriteCount");
				comment.user.viewCount  = json_u.getString("viewCount");
				m_comment.add(comment);
			}
		} catch (JSONException e) {e.printStackTrace();}
		
		return m_comment;
	}


	public static ArrayList<UserInFavorites> parseUserInFavorites(JSONObject json){
		
		ArrayList<UserInFavorites> userInFavorites = new ArrayList<UserInFavorites>();
		
		try {
			JSONArray u = json.getJSONArray("userInFavorites");
			
			for (int i = 0; i < u.length(); i++) {
				JSONObject uObject = u.getJSONObject(i);
				UserInFavorites favorites = new UserInFavorites();
				
				if(uObject.has("userId"))
					favorites.userId 		= ""+uObject.get("userId");
				
				if(uObject.has("avatarPath"))
					favorites.avatarPath 	= ""+uObject.get("avatarPath");
				
				if(uObject.has("avatarUrl"))
					favorites.avatarUrl 	= ""+uObject.get("avatarUrl");
				
				if(uObject.has("hasAvatar"))
					favorites.hasAvatar 	= ""+uObject.get("hasAvatar");
				
				if(uObject.has("avatarCount"))
					favorites.avatarCount 	= ""+uObject.get("avatarCount");
				
				if(uObject.has("avatarName"))
					favorites.avatarName 	= ""+uObject.get("avatarName");
				
				if(uObject.has("fullName"))
					favorites.fullName 		= ""+uObject.get("fullName");
				
				if(uObject.has("displayName"))
					favorites.displayName 	= ""+uObject.get("displayName");
				
				if(uObject.has("userName"))
					favorites.userName 		= ""+uObject.get("userName");
				
				if(uObject.has("email"))
					favorites.email 		= ""+uObject.get("email");
				
				if(uObject.has("aboutMe"))
					favorites.aboutMe 		= ""+uObject.get("aboutMe");
				
				if(uObject.has("postCount"))
					favorites.postCount 	= ""+uObject.get("postCount");
				
				if(uObject.has("favoriteCount"))
					favorites.favoriteCount = ""+uObject.get("favoriteCount");
				
				if(uObject.has("viewCount"))
					favorites.viewCount 	= ""+uObject.get("viewCount");
				
				userInFavorites.add(favorites);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userInFavorites;
	}
	
	
	public static ArrayList<Tag> parseTags (JSONObject json){
		ArrayList<Tag> tags = new ArrayList<_MediaLists.Tag>();
	
		try {
			JSONArray t = json.getJSONArray("tags");
			for (int i = 0; i < t.length(); i++) {
				JSONObject tObject =t.getJSONObject(i);
				Tag tag = new Tag();
				
				tag.id	 = ""+tObject.get("id");
				tag.name = ""+tObject.get("name");
			
				tags.add(tag);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return tags;
	}
	
//###############################################################################	
//###############################################################################
	
	
	 
	public static LanguageLabels parseAppSettings_LanguageLabels(JSONObject languageLabels_JSON){  
		
		LanguageLabels languageLabels = new LanguageLabels(); 
		try { 
			languageLabels._A_SportXast_highlight_was_shared_by 	= languageLabels_JSON.getString("id");
			languageLabels._A_new_beta_version_is_available 		= languageLabels_JSON.getString("id");
			languageLabels._A_new_event__Label_your_Event 			= languageLabels_JSON.getString("id");
			languageLabels._A_new_version_is_available_in_app_store = languageLabels_JSON.getString("id");
			languageLabels._Account 								= languageLabels_JSON.getString("id");
			languageLabels._Add_comment 							= languageLabels_JSON.getString("id");
			languageLabels._Add 									= languageLabels_JSON.getString("id");
			languageLabels._Back 									= languageLabels_JSON.getString("id");
			languageLabels._Cancel 									= languageLabels_JSON.getString("id");
			languageLabels._Capture_watch_and_share_sporting_highlights_in_your_community_with_SportXast =languageLabels_JSON.getString("id");
			languageLabels._Capture 								= languageLabels_JSON.getString("id");
			languageLabels._Choose_picture 							= languageLabels_JSON.getString("id");
			languageLabels._Close 									= languageLabels_JSON.getString("id");
			languageLabels._DONE 									= languageLabels_JSON.getString("id");
			languageLabels._Delete_media 							= languageLabels_JSON.getString("id");
			languageLabels._Delete_video 							= languageLabels_JSON.getString("id");
			languageLabels._Edit_Profile 							= languageLabels_JSON.getString("id");
			languageLabels._Edit_Your_Profile 						= languageLabels_JSON.getString("id");
			languageLabels._Enter_your_email 						= languageLabels_JSON.getString("id");
			languageLabels._Enter_your_motto 						= languageLabels_JSON.getString("id");
			languageLabels._Enter_your_name 						= languageLabels_JSON.getString("id");
			languageLabels._Enter_your_username 					= languageLabels_JSON.getString("id");
			languageLabels._Error 									= languageLabels_JSON.getString("id");
			languageLabels._Events 									= languageLabels_JSON.getString("id");
			languageLabels._Facebook 								= languageLabels_JSON.getString("id");
			languageLabels._Failed_to_send_SMS 						= languageLabels_JSON.getString("id");
			languageLabels._From_camera 							= languageLabels_JSON.getString("id");
			languageLabels._From_photos							 	= languageLabels_JSON.getString("id");
			languageLabels._Go_to_your_iPhone_Settings__Privacy__Location_Services_to_turn_on = languageLabels_JSON.getString("id");
			languageLabels._Highlights 								= languageLabels_JSON.getString("id");
			languageLabels._Home								 	= languageLabels_JSON.getString("id");
			languageLabels._Import_from_Facebook 					= languageLabels_JSON.getString("id");
			languageLabels._Import_from_Twitter						= languageLabels_JSON.getString("id");
			languageLabels._Join_Event 								= languageLabels_JSON.getString("id");
			languageLabels._Label_your_event_so_other_people_may_join_to_capture_watch_and_share_highlights =  languageLabels_JSON.getString("id");
			languageLabels._New_version_available 					= languageLabels_JSON.getString("id");
			languageLabels._No_internet_connection 					= languageLabels_JSON.getString("id");
			languageLabels._No 									 	= languageLabels_JSON.getString("id");
			languageLabels._On_your_iPhone_go_to_Settings__Facebook_to_sign_in_or_create_a_new_account =  languageLabels_JSON.getString("id");
			languageLabels._On_your_iPhone_go_to_Settings__Mail_to_add_an_eMail_account =  languageLabels_JSON.getString("id");
			languageLabels._On_your_iPhone_go_to_Settings__Twitter_to_sign_in_or_create_a_new_account =  languageLabels_JSON.getString("id");
			languageLabels._Profile_and_Account 					= languageLabels_JSON.getString("id");
			languageLabels._Profile 								= languageLabels_JSON.getString("id");
			languageLabels._Record 									= languageLabels_JSON.getString("id");
			languageLabels._Register 								= languageLabels_JSON.getString("id");
			languageLabels._Report_Inappropriate 					= languageLabels_JSON.getString("id");
			languageLabels._Report_video_as_innappropiate			= languageLabels_JSON.getString("id");
			languageLabels._Send 									= languageLabels_JSON.getString("id");
			languageLabels._Settings 								= languageLabels_JSON.getString("id");
			languageLabels._Share					 				= languageLabels_JSON.getString("id");
			languageLabels._Skip					 				= languageLabels_JSON.getString("id");
			languageLabels._SportxAst_cannot_access_yor_Twitter_info= languageLabels_JSON.getString("id");
			languageLabels._To_be_done					 			= languageLabels_JSON.getString("id");
			languageLabels._Twitter_Account_Not_Found		  		= languageLabels_JSON.getString("id");
			languageLabels._Twitter 								= languageLabels_JSON.getString("id");
			languageLabels._Use_facebook_account 					= languageLabels_JSON.getString("id");
			languageLabels._Use_twitter_account 					= languageLabels_JSON.getString("id");
			languageLabels._View_all 							 	= languageLabels_JSON.getString("id");
			languageLabels._Yes 					 				= languageLabels_JSON.getString("id");
			languageLabels._Your_device_doesnt_support_SMS 			= languageLabels_JSON.getString("id");
			languageLabels._comments 					 			= languageLabels_JSON.getString("id");
			languageLabels._favorites 								= languageLabels_JSON.getString("id");
			languageLabels._video 							 		= languageLabels_JSON.getString("id");
			languageLabels._No_Highlights					  		= languageLabels_JSON.getString("id");
			languageLabels._No_Notifications					  	= languageLabels_JSON.getString("id");
			languageLabels._No_Media_Tag					  		= languageLabels_JSON.getString("id");
			languageLabels._Add_another_Team 					 	= languageLabels_JSON.getString("id");
			languageLabels._Add_a_Team 						 		= languageLabels_JSON.getString("id");
			languageLabels._Sports 					 				= languageLabels_JSON.getString("_Sports");
			languageLabels._Venues					 			 	= languageLabels_JSON.getString("_Venues");
			languageLabels._Done								 	= languageLabels_JSON.getString("_Done");
			languageLabels._Add_more 					 			= languageLabels_JSON.getString("id");
			languageLabels._Joined								  	= languageLabels_JSON.getString("id");
			languageLabels._Join								  	= languageLabels_JSON.getString("id");
			languageLabels._Getting_Location 						= languageLabels_JSON.getString("id");
			languageLabels._Search 					 				= languageLabels_JSON.getString("id");
			languageLabels._Following				 				= languageLabels_JSON.getString("id");
			languageLabels._Follow 				 					= languageLabels_JSON.getString("id");
			languageLabels._Popular 				 				= languageLabels_JSON.getString("id");
			languageLabels._Nearby				 				 	= languageLabels_JSON.getString("id");
			languageLabels._Favorites 				 				= languageLabels_JSON.getString("id");
			languageLabels._Sport				 					= languageLabels_JSON.getString("id");
			languageLabels._Venue				 				 	= languageLabels_JSON.getString("id");
			languageLabels._Home_Team 				 				= languageLabels_JSON.getString("id");
			languageLabels._Other_Team 				 				= languageLabels_JSON.getString("id");
			languageLabels._Information 				 			= languageLabels_JSON.getString("_Information");
			languageLabels._Select_Venue				 			= languageLabels_JSON.getString("id");
			languageLabels._Teams 				 					= languageLabels_JSON.getString("_Teams");
			languageLabels._Create 				 					= languageLabels_JSON.getString("_Create");
			languageLabels._Menu 				 					= languageLabels_JSON.getString("id");
			languageLabels._Followers 				 				= languageLabels_JSON.getString("id");
			languageLabels._Other 				 					= languageLabels_JSON.getString("id");
			languageLabels._Edit				 				 	= languageLabels_JSON.getString("id");
			languageLabels._Favorite 				 				= languageLabels_JSON.getString("id");
			languageLabels._Notifications				 			= languageLabels_JSON.getString("id");
			languageLabels._Share_SportXast 				 		= languageLabels_JSON.getString("id");
			languageLabels._No_Events				 				= languageLabels_JSON.getString("id");
			languageLabels._Tags 				 					= languageLabels_JSON.getString("id");
			languageLabels._Event_Tags 				 				= languageLabels_JSON.getString("id");  
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return languageLabels; 
	}
	
	public static Settings parseAppSettings_Settings(JSONObject settings_JSON){ 
		Settings settings = new Settings();  
		try {  
		    settings._IS_SECURE_CONNECTION 		= settings_JSON.getString(	"IS_SECURE_CONNECTION");
			settings._MOVIE_DURATION 			= settings_JSON.getString(	"MOVIE_DURATION");
			settings._MOVIE_DELAY 				= settings_JSON.getString(	"MOVIE_DELAY");
			settings._AUTOCOMPLETE_START_AFTER 	= settings_JSON.getString(	"AUTOCOMPLETE_START_AFTER");
			settings._LOG_LEVEL 				= settings_JSON.getString(	"LOG_LEVEL");
			settings._S3_VIDEO_BUCKET 			= settings_JSON.getString(	"S3_VIDEO_BUCKET");
			settings._S3_IMAGE_BUCKET 			= settings_JSON.getString(	"S3_IMAGE_BUCKET");
			settings._S3_VIDEO_UPLOAD_PATH 		= settings_JSON.getString(	"S3_VIDEO_UPLOAD_PATH");
			settings._S3_IMAGE_UPLOAD_PATH 		= settings_JSON.getString(	"S3_IMAGE_UPLOAD_PATH");
			settings._S3_STREAM_BUCKET 			= settings_JSON.getString(	"S3_STREAM_BUCKET");
			settings._APP_SHARE_TEXT 			= settings_JSON.getString(	"APP_SHARE_TEXT");
			settings._APP_SHARE_URL 			= settings_JSON.getString(	"APP_SHARE_URL");
			settings._S3_VIDEO_PUT_URL 			= settings_JSON.getString(	"S3_VIDEO_PUT_URL");
			settings._S3_VIDEO_GET_URL 			= settings_JSON.getString(	"S3_VIDEO_GET_URL");
			settings._S3_IMAGE_PUT_URL 			= settings_JSON.getString(	"S3_IMAGE_PUT_URL");
			settings._S3_IMAGE_GET_URL 			= settings_JSON.getString(	"S3_IMAGE_GET_URL");
			settings._S3_STREAM_PUT_URL 		= settings_JSON.getString(	"S3_STREAM_PUT_URL");
			settings._S3_STREAM_GET_URL 		= settings_JSON.getString(	"S3_STREAM_GET_URL");
			settings._EVENT_SEARCH_RADIUS 		= settings_JSON.getString(	"EVENT_SEARCH_RADIUS");
			settings._SNS_NOTIFICATION_TOPIC	= settings_JSON.getString(	"SNS_NOTIFICATION_TOPIC");
			settings._S3_THUMBNAIL_FORMAT 		= settings_JSON.getString(	"S3_THUMBNAIL_FORMAT");
			settings._S3_AVATAR_UPLOAD_PATH		= settings_JSON.getString(	"S3_AVATAR_UPLOAD_PATH");
			settings._S3_PLAYLIST_SEGMENT_DURATION= settings_JSON.getString("S3_PLAYLIST_SEGMENT_DURATION");
			settings._SPORT_DEFAULT_TAGS 		= settings_JSON.getString(	"SPORT_DEFAULT_TAGS");
			settings._APP_SHARE_MAIL_SUBJECT 	= settings_JSON.getString(	"APP_SHARE_MAIL_SUBJECT");
			settings._MEDIA_SHARE_MAIL_SUBJECT 	= settings_JSON.getString(	"MEDIA_SHARE_MAIL_SUBJECT");
			settings._CHALKBOARD_TIMEOUT 		= settings_JSON.getString(	"CHALKBOARD_TIMEOUT");
			settings._STOP_BROADCAST_BY_ANGLE 	= settings_JSON.getString(	"STOP_BROADCAST_BY_ANGLE");
			settings._STOP_BROADCAST_BY_ANGLE_TIME 	= settings_JSON.getString("STOP_BROADCAST_BY_ANGLE_TIME");
			settings._APP_BETA_VERSION 				= settings_JSON.getString("APP_BETA_VERSION");
			settings._APP_PRODUCTION_VERSION 		= settings_JSON.getString("APP_PRODUCTION_VERSION");
			settings._APP_BETA_VERSION_IS_LOCKED	= settings_JSON.getString("APP_BETA_VERSION_IS_LOCKED");
			settings._APP_BETA_VERSION_DOWNLOAD_URL = settings_JSON.getString("APP_BETA_VERSION_DOWNLOAD_URL");
			settings._APP_PRODUCTION_VERSION_DOWNLOAD_URL = settings_JSON.getString("APP_PRODUCTION_VERSION_DOWNLOAD_URL");
			settings._APP_BETA_VERSION_DOWNLOAD_TEXT = settings_JSON.getString("APP_BETA_VERSION_DOWNLOAD_TEXT");
			settings._APP_PRODUCTION_VERSION_DOWNLOAD_TEXT = settings_JSON.getString("APP_PRODUCTION_VERSION_DOWNLOAD_TEXT");
			settings._LOCATION_MAX_RADIUS 			= settings_JSON.getString("LOCATION_MAX_RADIUS");
			settings._WEBSITE_BASE_URL 				= settings_JSON.getString("WEBSITE_BASE_URL");
			settings._REDIS_CONN_HOST	 			= settings_JSON.getString("REDIS_CONN_HOST");
			settings._REDIS_CONN_PORT 				= settings_JSON.getString("REDIS_CONN_PORT");
			settings._REDIS_DB_INDEX 				= settings_JSON.getString("REDIS_DB_INDEX");
			settings._FETCH_NOTIFICATION_INTERVAL 	= settings_JSON.getString("FETCH_NOTIFICATION_INTERVAL");
			settings._ALERT_MESSAGE_NEW_EVENT 		= settings_JSON.getString("ALERT_MESSAGE_NEW_EVENT");
			settings._ALERT_MESSAGE_NEW_HIGHLIGHT 	= settings_JSON.getString("ALERT_MESSAGE_NEW_HIGHLIGHT");
			settings._ALERT_MESSAGE_NEW_FAVORITE 	= settings_JSON.getString("ALERT_MESSAGE_NEW_FAVORITE");
			settings._ALERT_MESSAGE_NEW_COMMENT 	= settings_JSON.getString("ALERT_MESSAGE_NEW_COMMENT");
			settings._ALERT_MESSAGE_NEW_SHARE 		= settings_JSON.getString("ALERT_MESSAGE_NEW_SHARE");
			settings._SQS_PUSHNOTIFICATION_QUEUE 	= settings_JSON.getString("SQS_PUSHNOTIFICATION_QUEUE");
			settings._IOS_SNS_APP_ARN 				= settings_JSON.getString("IOS_SNS_APP_ARN");
			settings._ALERT_MESSAGE_NEW_FRIEND 		= settings_JSON.getString("ALERT_MESSAGE_NEW_FRIEND");
			settings._ALERT_MESSAGE_NEW_USER_ON_JOINED_EVENT = settings_JSON.getString("ALERT_MESSAGE_NEW_USER_ON_JOINED_EVENT");
			settings._TOOLTIP_TIMEOUT 				= settings_JSON.getString("TOOLTIP_TIMEOUT");
			settings._SQS_TRANSCODER_JOBSTATUS_QUEUE= settings_JSON.getString("SQS_TRANSCODER_JOBSTATUS_QUEUE");
			settings._ALERT_MESSAGE_NEW_MEDIA_TAG 	= settings_JSON.getString("ALERT_MESSAGE_NEW_MEDIA_TAG");
			settings._DEFAULT_EVENT_ID 				= settings_JSON.getString("DEFAULT_EVENT_ID");
			  
			settings._EVENT_SHARE_MAIL_SUBJECT				= settings_JSON.getString("EVENT_SHARE_MAIL_SUBJECT");
			settings._LOCATION_REFRESH_TIMER				= settings_JSON.getString("LOCATION_REFRESH_TIMER");
			settings._APP_BETA_VERSION_DOWNLOAD_TITLE		= settings_JSON.getString("APP_BETA_VERSION_DOWNLOAD_TITLE");
			settings._APP_PRODUCTION_VERSION_DOWNLOAD_TITLE	= settings_JSON.getString("APP_PRODUCTION_VERSION_DOWNLOAD_TITLE");
			settings._DEFAULT_TEXT_CHARACTER_LIMIT			= settings_JSON.getString("DEFAULT_TEXT_CHARACTER_LIMIT");
			settings._DEFAULT_POINTS_PER_TAG				= settings_JSON.getString("DEFAULT_POINTS_PER_TAG");
			settings._DEFAULT_POINTS_PER_FAVORITE			= settings_JSON.getString("DEFAULT_POINTS_PER_FAVORITE");
			settings._DEFAULT_POINTS_PER_COMMENT			= settings_JSON.getString("DEFAULT_POINTS_PER_COMMENT");
			settings._DEFAULT_POINTS_PER_SHARE				= settings_JSON.getString("DEFAULT_POINTS_PER_SHARE");
			settings._DEFAULT_MEDIA_SHARE_TITLE				= settings_JSON.getString("DEFAULT_MEDIA_SHARE_TITLE");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return settings; 
	} 
	 
//###############################################################################	
//###############################################################################
	
	public static double[] getLastKnownLocation(Context mContext) {
	    LocationManager lm = (LocationManager)  mContext.getSystemService(Context.LOCATION_SERVICE);  
	    List<String> providers = lm.getProviders(true);

	    /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
	    Location l = null;

	    for (int i=providers.size()-1; i>=0; i--) {
	            l = lm.getLastKnownLocation(providers.get(i));
	            if (l != null) break;
	    }

	    double[] gps = new double[2];
	    if (l != null) {
	            gps[0] = l.getLatitude();
	            gps[1] = l.getLongitude();
	    }
	    return gps;
	}
	 
	public static void getCurrentLocation(Context context) {
		gpsTracker = new GPSTracker(context);
		Global_Data global_Data = (Global_Data)context.getApplicationContext();

		if (gpsTracker.canGetLocation()) {

			Coordinate coordinate = new Coordinate();
			coordinate.latitude = gpsTracker.getLatitude();
			coordinate.longitude = gpsTracker.getLongitude();

			Log.e("LATITUDE : ", "" + coordinate.latitude);
			Log.e("LONGITUDE : ", "" + coordinate.longitude);
			global_Data.setCoordinate(coordinate);
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gpsTracker.showSettingsAlert();
		}

	}
	
	 
	public static String getIntentExtrasString(Context context,String key){
		String str = "";
		
		try {
			str = ((Activity)context).getIntent().getExtras().getString(key);
		} catch (Exception e) {
			// TODO: handle exception
		str="";
		}
		
		if(str==null) str="";
		
		return str;
	}

	public static int getIntentExtrasInt(Context context,String key){
		int intKey = 0;
		
		try {
			//String str = ((Activity)context).getIntent().getExtras().getString(key);
			int str = ((Activity)context).getIntent().getExtras().getInt(key);
			 
			intKey =str;
			//intKey = Integer.parseInt( str );
			
		} catch (Exception e) {
			// TODO: handle exception
			//str="";
			intKey = 0;
		}
		  
		return intKey;
	}
	
	public static boolean getIntentExtrasBoolean(Context context,String key){
		boolean b =false;
		try {
			b = Boolean.parseBoolean(key);
		} catch (Exception e) {
		// TODO: handle exception
		b = false;
		}
		
		return b;
	}
	
	
	
	public static int getScreenWidth(Context context){
		
		WindowManager w = ((Activity)context).getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		return metrics.widthPixels;
		}
	 
	public static int getScreenHeight(Context context){
		WindowManager w = ((Activity)context).getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		return metrics.heightPixels;
		}
	
	public static int scaleSizeToDP(Context context,int i) {
		float scale = context.getResources().getDisplayMetrics().density;

		WindowManager w = ((Activity)context).getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);

		return (int) (i * scale + 0.5f);
	} 
	
//###################################################################################

	public static int getVersion(Context aContext) {
		try {
			PackageInfo pInfo = aContext.getPackageManager().getPackageInfo(
					aContext.getPackageName(), 0);
			return pInfo.versionCode;
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	
	
}

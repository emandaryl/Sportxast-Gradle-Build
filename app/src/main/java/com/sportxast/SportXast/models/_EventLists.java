package com.sportxast.SportXast.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class _EventLists {

	public boolean isFirstLoad = true;
	public boolean isRefresh = false;
	public ArrayList<EventLists> eventLists = new ArrayList<EventLists>();
	public String resultCount="0";
	public String pageCount="0";
	public String currentPage="0";
	
	
	public String error="";
	public String message="";
	
	public static class EventLists{
		
				public String eventId = "";
				public String eventName = "";
				public String eventLocation = "";
				public String eventLatitude = "";
				public String eventLongitude = "";
				public String eventStartDate = "";
				public String eventStartDateShort = "";
				public String eventStartDateFormatted = "";
				public String eventSportId = "";
				public String eventSportName = "";
				public String eventIsOpen = "";
				
				/**VALUES: "LIVE" OR "CHECK IN"**/
				public String eventIsOpenString = "";
				
				public String eventIsEnded = "";
				public String eventInFavorites = "";
		
				public String[] eventTags = new String[]{};
				
				public String eventMediaCount;
				public String eventViewCount;
				public String eventFavoriteCount;
				public String eventCommentCount;
				public String eventShareCount;
				public String eventScore;
				
				public String eventDistance = "";
				public String eventDistanceUnit = "";
				public String eventFirstTeam="";
				public String eventSecondTeam="";
				
				//public String eventCoverImageThumb="";
		
				public String eventTotalMediaCount= "";
				public String eventTotalFavoriteCount= "";
				public String eventTotalCommentCount= "";
				
				public String eventTotalTagCount="";
				public String eventTotalPlayerCount="";
				public String eventTotalFanCount = "";
				
				public EventSport eventSport = new EventSport();
				
				public ArrayList<ThumbnailImage> eventCoverImageThumb = new ArrayList<ThumbnailImage>();
				public ArrayList<FavoriteCountWithMedia>favoriteCountWithMedia = new ArrayList<FavoriteCountWithMedia>();
				public ArrayList<MediaTagWithHighlight>mediaTagWithHighlight = new ArrayList<MediaTagWithHighlight>();
				public ArrayList<MediaWithPlayer> mediaWithPlayer = new ArrayList<MediaWithPlayer>();
				public ArrayList<MediaWithPlayer> eventFans = new ArrayList<MediaWithPlayer>();
				public ArrayList<Team> eventTeams = new ArrayList<Team>();
				//public ArrayList <MediaList> mediaList = new ArrayList<_MediaLists.MediaList>(); 
	}
	
	public static class Team{
		public String name="";
		public String avatarUrl="";
	}
	
	public static class ThumbnailImage{
		public String mediaId ="";
		public String coverImageThumb = ""; 
		public String commentCount="";
		public String favoriteCount="";
		
	}
	
	public static class FavoriteCountWithMedia
	{
		public String mediaId="";
		public String coverImageThumb="";
		public String favoriteCount="";
	}
	public static class MediaTagWithHighlight
	{
		public String mediaId="";
		public String coverImageThumb="";
		public String tagName="";
		
	}
	
	public static class MediaWithPlayer{
		public String id="";
		public String name="";
		public String avatarPath="";
		public String avatarUrl="";
	}
	

	public static class EventSport{
		public String sportId;
		public String sportName;
		public String sportFirstLetter;
		public String sportLogo;
		public String sportWhiteLogo;
	}
	
	
	public static class HighlightReelMedia{
		public String error 	= "";
		public String message 	= "";
		public String shortUrl 	= "";
		public String url		= "";
	}
	
	public void parseEvents(JSONObject jsonObject){
		isFirstLoad = false;
		int evenList_length=0;
		
		
		try {
			error = jsonObject.get("error").toString();
			message = jsonObject.get("message").toString();
		} catch (JSONException e) {e.printStackTrace();}
		
		 
		JSONArray jsonArray_eventList = new JSONArray();
		
		try {
			resultCount = ""+jsonObject.get("resultCount");
			pageCount = ""+jsonObject.get("pageCount");
			currentPage =""+jsonObject.get("currentPage");
			
		
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
		 
		  //  boolean OpenEventFound = false;
			for (int i = 0; i < evenList_length; i++) {
					JSONObject jsObject_eList = new JSONObject();
					EventLists eLists = new EventLists();
					
					String eventIsOpenString_ = "";
					 
					try {
						jsObject_eList = jsonArray_eventList.getJSONObject(i);
						eLists = new EventLists();
						 
						eLists.eventId 					= ""+jsObject_eList.get("eventId");
						eLists.eventName 				= ""+jsObject_eList.get("eventName");
						eLists.eventLocation 			= ""+jsObject_eList.get("eventLocation");
						eLists.eventLatitude 			= ""+jsObject_eList.get("eventLatitude");
						eLists.eventLongitude 			= ""+jsObject_eList.get("eventLongitude");
						eLists.eventStartDate 			= ""+jsObject_eList.get("eventStartDate");
						eLists.eventStartDateShort 		= ""+jsObject_eList.get("eventStartDateShort");
						eLists.eventStartDateFormatted 	= ""+jsObject_eList.get("eventStartDateFormatted");
						eLists.eventSportId 			= ""+jsObject_eList.get("eventSportId");
						eLists.eventSportName 			= ""+jsObject_eList.get("eventSportName");
						  
						eLists.eventIsOpen 				= ""+jsObject_eList.get("eventIsOpen"); 
						
						if( jsObject_eList.has("eventIsOpenString") ) 
							eventIsOpenString_ = ""+jsObject_eList.get("eventIsOpenString");
						
						eLists.eventIsOpenString 		= eventIsOpenString_;
						/*
						if(eventIsOpenString_.equals("CHECK IN")){
							OpenEventFound = true;
						}
						*/
						
						eLists.eventIsEnded  			= ""+jsObject_eList.get("eventIsEnded"); 
						eLists.eventTotalMediaCount 	= ""+jsObject_eList.get("eventTotalMediaCount");
						eLists.eventTotalCommentCount 	= ""+jsObject_eList.get("eventTotalCommentCount");
						eLists.eventTotalFavoriteCount 	= ""+jsObject_eList.get("eventTotalFavoriteCount");
						eLists.eventTotalTagCount 		= ""+jsObject_eList.get("eventTotalTagCount");
						eLists.eventTotalPlayerCount 	= ""+jsObject_eList.get("eventTotalPlayerCount");
						eLists.eventTotalFanCount 		= ""+jsObject_eList.get("eventTotalFanCount");
						 
						if( jsObject_eList.has("eventMediaCount") ) 
							 	eLists.eventMediaCount  	= ""+jsObject_eList.get("eventMediaCount");
						else	eLists.eventMediaCount 	= "";
						 
						if( jsObject_eList.has("eventViewCount") ) 
								eLists.eventViewCount		= ""+jsObject_eList.get("eventViewCount");
						else 	eLists.eventViewCount 	= "";
						 
						if( jsObject_eList.has("eventFavoriteCount") ) 
								eLists.eventFavoriteCount	= ""+jsObject_eList.get("eventFavoriteCount");
						else 	eLists.eventFavoriteCount 	= "";
						 
						if( jsObject_eList.has("eventCommentCount") ) 
								eLists.eventCommentCount	= ""+jsObject_eList.get("eventCommentCount");
						else 	eLists.eventCommentCount 	= "";
						
						if( jsObject_eList.has("eventShareCount") ) 
								eLists.eventShareCount 	= ""+jsObject_eList.get("eventShareCount");
						else 	eLists.eventShareCount 	= "";
						 
						if( jsObject_eList.has("eventScore") ) 
								eLists.eventScore  		= ""+jsObject_eList.get("eventScore");	
						else 	eLists.eventScore 	= "";
						 
				 
						for (String string : jsonObject.getString("eventTeams").split("-")) {
							Team team = new Team();
							team.name = string;
							eLists.eventTeams.add(team);
						}
					 
						if( jsObject_eList.has("eventFirstTeam") ) 
								eLists.eventFirstTeam 	= ""+jsObject_eList.get("eventFirstTeam");
						else 	eLists.eventFirstTeam 	= "";
						
						if( jsObject_eList.has("eventShareCount") )
			
								eLists.eventSecondTeam	= ""+jsObject_eList.get("eventSecondTeam");
						else 	eLists.eventSecondTeam 	= "";
						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
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
							eLists.eventTags[j] 		= ""+jsonArray_eventags.get(j); 
							
							if(j == 0)
								eLists.eventFirstTeam 	= ""+jsonArray_eventags.get(j);
							else if(j == 1)
								eLists.eventSecondTeam 	= ""+jsonArray_eventags.get(j); 
						}
//					    String toString_array = jsObject_eList.get("eventTags").toString();
//					    toString_array = toString_array.replace("[\"", "");
//					    toString_array = toString_array.replace("\"]", "");
//					    eLists.eventTags =new String[]{};
//					    
//					    eLists.eventTags = toString_array.split("\",\"");
//					   
//						//Log.v("eventtag",""+ toString_array);
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
					
					  
					if(isRefresh){
                        if(hasDuplicate(eLists) == false) {
                            eventLists.add(i,eLists);
                        }
					}
					else{
                        if(hasDuplicate(eLists) == false) {
                            eventLists.add(eLists);
                        }
					}
					 
					if(eventIsOpenString_.equals("CHECK IN")){//if latest event
						//eventIsOpen = true; 
						if(this.FLatestEvent == null)
							 this.FLatestEvent = eLists; 
					}
					 
					
			}
				
				
				
			
	
	}
	
    private EventLists FLatestEvent;
	public EventLists getLatestEvent(){
		return this.FLatestEvent;
	}

    private boolean hasDuplicate(EventLists listObject) {
        if(eventLists.isEmpty() == false) {
            for(int i=0; i<eventLists.size(); i++) {
                if(eventLists.get(i).eventId.equals(listObject.eventId)) {
                    return true;
                }
            }
        }
        return false;
    }

}



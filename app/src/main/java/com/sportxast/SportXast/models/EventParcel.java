package com.sportxast.SportXast.models;

import android.os.Parcel;
import android.os.Parcelable;

public class EventParcel implements Parcelable {
	 
	public String eventId 					= "";
	public String eventName 				= "";
	public String eventLocation 			= "";
	public String eventLatitude 			= "";
	public String eventLongitude 			= "";
	public String eventStartDate 			= "";
	public String eventStartDateShort 		= "";
	public String eventStartDateFormatted 	= "";
	public String eventSportId 				= "";
	public String eventSportName 			= "";
	public String eventIsOpen 				= "";
	
	//VALUES: "LIVE" OR "CHECK IN"
	public String eventIsOpenString 		= "";
	
	public String eventIsEnded 				= "";
	public String eventInFavorites 			= "";

	public String eventTags 				= "";
	
	public String eventMediaCount			= "";
	public String eventViewCount			= "";
	public String eventFavoriteCount		= "";
	public String eventCommentCount			= "";
	public String eventShareCount			= "";
	public String eventScore				= "";
	
	public String eventDistance 			= "";
	public String eventDistanceUnit 		= "";
	public String eventFirstTeam			= "";
	public String eventSecondTeam			= "";
	
	//public String eventCoverImageThumb	= ""; 
	public String eventTotalMediaCount		= "";
	public String eventTotalFavoriteCount	= "";
	public String eventTotalCommentCount	= ""; 
	public String eventTotalTagCount		= "";
	public String eventTotalPlayerCount		= "";
	public String eventTotalFanCount 		= ""; 
	
	
	/**	eventSport class, returns JSON String **/ 
	public String eventSport	 			= "";
	
	/**	is an arraylist, returns JSON String **/ 
	public String eventCoverImageThumb 		= "";
	/**	is an arraylist, returns JSON String **/ 
	public String favoriteCountWithMedia 	= "";
	/**	is an arraylist, returns JSON String **/ 
	public String mediaTagWithHighlight 	= "";
	/**	is an arraylist, returns JSON String **/ 
	public String mediaWithPlayer  			= "";
	/**	is an arraylist, returns JSON String **/ 
	public String eventFans  				= "";
	/**	is an arraylist, returns JSON String **/ 
	public String eventTeams 	 			= ""; 
	/*
	public ArrayList<ThumbnailImage> eventCoverImageThumb 		 	= new ArrayList<ThumbnailImage>();
	public ArrayList<FavoriteCountWithMedia>favoriteCountWithMedia 	= new ArrayList<FavoriteCountWithMedia>();
	public ArrayList<MediaTagWithHighlight>mediaTagWithHighlight 	= new ArrayList<MediaTagWithHighlight>();
	public ArrayList<MediaWithPlayer> mediaWithPlayer 				= new ArrayList<MediaWithPlayer>();
	public ArrayList<MediaWithPlayer> eventFans 					= new ArrayList<MediaWithPlayer>();
	public ArrayList<Team> eventTeams 								= new ArrayList<Team>();
	*/
	 
	/**	  
		@param String eventId 					= "";
		@param String eventName 				= "";
		@param String eventLocation 			= "";
		@param String eventLatitude 			= "";
		@param String eventLongitude 			= "";
		@param String eventStartDate 			= "";
		@param String eventStartDateShort 		= "";
		@param String eventStartDateFormatted 	= "";
		@param String eventSportId 				= "";
		@param String eventSportName 			= "";
		@param String eventIsOpen 				= "";
		
		//VALUES: "LIVE" OR "CHECK IN"
		@param String eventIsOpenString 		= "";
		
		@param String eventIsEnded 				= "";
		@param String eventInFavorites 			= "";
	
		@param String eventTags 				= "";
		
		@param String eventMediaCount			= "";
		@param String eventViewCount			= "";
		@param String eventFavoriteCount		= "";
		@param String eventCommentCount			= "";
		@param String eventShareCount			= "";
		@param String eventScore				= "";
		
		@param String eventDistance 			= "";
		@param String eventDistanceUnit 		= "";
		@param String eventFirstTeam			= "";
		@param String eventSecondTeam			= "";
		
		//@param String eventCoverImageThumb	= ""; 
		@param String eventTotalMediaCount		= "";
		@param String eventTotalFavoriteCount	= "";
		@param String eventTotalCommentCount	= ""; 
		@param String eventTotalTagCount		= "";
		@param String eventTotalPlayerCount		= "";
		@param String eventTotalFanCount 		= ""; 
		 
		@param String eventSport	 			= "";
		@param String eventCoverImageThumb 		= "";
		@param String favoriteCountWithMedia 	= "";
		@param String mediaTagWithHighlight 	= "";
		@param String mediaWithPlayer  			= "";
		@param String eventFans  				= "";
		@param String eventTeams 	 			= ""; 
    		**/
	public EventParcel(
			String eventId,
			String eventName,
			String eventLocation,
			String eventLatitude,
			String eventLongitude,
			String eventStartDate,
			String eventStartDateShort,
			String eventStartDateFormatted,
			String eventSportId,
			String eventSportName,
			String eventIsOpen, 
			/**VALUES: "LIVE" OR "CHECK IN"**/
			String eventIsOpenString, 
			String eventIsEnded,
			String eventInFavorites, 
			String eventTags,
			
			String eventMediaCount,
			String eventViewCount,
			String eventFavoriteCount,
			String eventCommentCount,
			String eventShareCount,
			String eventScore,
			
			String eventDistance,
			String eventDistanceUnit,
			String eventFirstTeam,
			String eventSecondTeam,
			
			//String eventCoverImageThumb="";

			String eventTotalMediaCount,
			String eventTotalFavoriteCount,
			String eventTotalCommentCount,
			
			String eventTotalTagCount,
			String eventTotalPlayerCount,
			String eventTotalFanCount,
			
			String eventSport,
			String eventCoverImageThumb,
			String favoriteCountWithMedia,
			String mediaTagWithHighlight,
			String mediaWithPlayer,
			String eventFans,
			String eventTeams 
			){
		 
		this.eventId 				= eventId;
		this.eventName 				= eventName;
		this.eventLocation 			= eventLocation;
		this.eventLatitude 			= eventLatitude;
		this.eventLongitude 		= eventLongitude;
		this.eventStartDate 		= eventStartDate;
		this.eventStartDateShort 	= eventStartDateShort;
		this.eventStartDateFormatted= eventStartDateFormatted;
		this.eventSportId 			= eventSportId;
		this.eventSportName 		= eventSportName;
		this.eventIsOpen 			= eventIsOpen; 
		/**VALUES: "LIVE" OR "CHECK IN"**/
		this.eventIsOpenString 		= eventIsOpenString; 
		this.eventIsEnded 			= eventIsEnded;
		this.eventInFavorites 		= eventInFavorites; 
		this.eventTags 				= eventTags;
		
		this.eventMediaCount	 	= eventMediaCount;
		this.eventViewCount 		= eventViewCount;
		this.eventFavoriteCount 	= eventFavoriteCount;
		this.eventCommentCount 		= eventCommentCount;
		this.eventShareCount 		= eventShareCount;
		this.eventScore 			= eventScore;
		
		this.eventDistance 			= eventDistance;
		this.eventDistanceUnit 		= eventDistanceUnit;
		this.eventFirstTeam 		= eventFirstTeam;
		this.eventSecondTeam 		= eventSecondTeam;
		
		//this.eventCoverImageThumb="";

		this.eventTotalMediaCount 	= eventTotalMediaCount;
		this.eventTotalFavoriteCount= eventTotalFavoriteCount;
		this.eventTotalCommentCount = eventTotalCommentCount;
		
		this.eventTotalTagCount 	= eventTotalTagCount;
		this.eventTotalPlayerCount 	= eventTotalPlayerCount;
		this.eventTotalFanCount 	= eventTotalFanCount;
		
		this.eventSport 			= eventSport;
		this.eventCoverImageThumb 	= eventCoverImageThumb;
		this.favoriteCountWithMedia = favoriteCountWithMedia;
		this.mediaTagWithHighlight 	= mediaTagWithHighlight;
		this.mediaWithPlayer 		= mediaWithPlayer;
		this.eventFans 				= eventFans;
		this.eventTeams 			= eventTeams;
	}
	  
	//parcel part
	public EventParcel(Parcel in){
		String[] data= new String[38];
	 
		in.readStringArray(data);
		    
		this.eventId 				= data[0];
		this.eventName 				= data[1];
		this.eventLocation 			= data[2];
		this.eventLatitude 			= data[3];
		this.eventLongitude 		= data[4];
		this.eventStartDate 		= data[5];
		this.eventStartDateShort 	= data[6];
		this.eventStartDateFormatted= data[7];
		this.eventSportId 			= data[8];
		this.eventSportName 		= data[9];
		this.eventIsOpen 			= data[10]; 
		/**VALUES: "LIVE" OR "CHECK IN"**/
		this.eventIsOpenString 		= data[11]; 
		this.eventIsEnded 			= data[12];
		this.eventInFavorites 		= data[13]; 
		this.eventTags 				= data[14];
		
		this.eventMediaCount 		= data[15];
		this.eventViewCount 		= data[16];
		this.eventFavoriteCount 	= data[17];
		this.eventCommentCount 		= data[18];
		this.eventShareCount 		= data[19];
		this.eventScore 			= data[20];
		
		this.eventDistance 			= data[21];
		this.eventDistanceUnit 		= data[22];
		this.eventFirstTeam	 		= data[23];
		this.eventSecondTeam 		= data[24];
		
		//this.eventCoverImageThumb="";

		this.eventTotalMediaCount 	= data[25];
		this.eventTotalFavoriteCount= data[26];
		this.eventTotalCommentCount = data[27];
		
		this.eventTotalTagCount 	= data[28];
		this.eventTotalPlayerCount 	= data[29];
		this.eventTotalFanCount 	= data[30];
		
		this.eventSport 			= data[31];
		this.eventCoverImageThumb 	= data[32];
		this.favoriteCountWithMedia = data[33];
		this.mediaTagWithHighlight 	= data[34];
		this.mediaWithPlayer 		= data[35];
		this.eventFans 				= data[36];
		this.eventTeams 			= data[37];
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	 
	@Override
	public void writeToParcel(Parcel dest, int flags) {
	// TODO Auto-generated method stub
	 
		dest.writeStringArray(new String[]{  
				this.eventId,
				this.eventName,
				this.eventLocation,
				this.eventLatitude,
				this.eventLongitude,
				this.eventStartDate,
				this.eventStartDateShort,
				this.eventStartDateFormatted,
				this.eventSportId,
				this.eventSportName,
				this.eventIsOpen, 
				/**VALUES: "LIVE" OR "CHECK IN"**/
				this.eventIsOpenString, 
				this.eventIsEnded,
				this.eventInFavorites, 
				this.eventTags,
				
				this.eventMediaCount,
				this.eventViewCount,
				this.eventFavoriteCount,
				this.eventCommentCount,
				this.eventShareCount,
				this.eventScore,
				
				this.eventDistance,
				this.eventDistanceUnit,
				this.eventFirstTeam,
				this.eventSecondTeam,
				
				//this.eventCoverImageThumb="";

				this.eventTotalMediaCount,
				this.eventTotalFavoriteCount,
				this.eventTotalCommentCount,
				
				this.eventTotalTagCount,
				this.eventTotalPlayerCount,
				this.eventTotalFanCount,
				
				this.eventSport,
				this.eventCoverImageThumb,
				this.favoriteCountWithMedia,
				this.mediaTagWithHighlight,
				this.mediaWithPlayer,
				this.eventFans,
				this.eventTeams
	    		});
	}
	 
	
	public static final Parcelable.Creator<EventParcel> CREATOR = new Parcelable.Creator<EventParcel>() {
	 
		@Override
		public EventParcel createFromParcel(Parcel source) {
		// TODO Auto-generated method stub
		return new EventParcel(source);  //using parcelable constructor
		}
		 
		@Override
		public EventParcel[] newArray(int size) {
		// TODO Auto-generated method stub
		return new EventParcel[size];
		}
	};
	
	 
	}
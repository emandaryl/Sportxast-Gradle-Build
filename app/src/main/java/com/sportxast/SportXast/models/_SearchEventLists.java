package com.sportxast.SportXast.models;

public class _SearchEventLists {

	public Events event  = new Events();
	
	
	public static class Events{
		public String eventId = "";
		public String eventName = "";
		public String eventLocation = "";
		public String eventLatitude = "";
		public String eventLongitude = "";
		public String eventStartDate = "";
		public String eventStartDateShort = "";
		public String eventSportId = "";
		public String eventSportName = "";
		public String eventIsEnded = "";
		public String[] eventTags = new String[]{};
		public eventSport eSport = new eventSport();
		public String eventDistance = "";
		public String eventDistanceUnit = "";
	}
	
	public static class eventSport{
		public String eventSport = "";
		public String sportId = "";
		public String sportName = "";
		public String sportFirstLetter = "";
		public String sportLogo = "";
		public String sportWhiteLogo = "";
		
	}
}

package com.sportxast.SportXast.models;

import java.util.ArrayList;

public class _SportsList {
	public boolean isEmpty = true;
	//jsonArray
	public ArrayList<String> letters = new ArrayList<String>();
	
	//jsonArray
	public ArrayList<SportsInLetter> sports = new ArrayList<SportsInLetter>();
	
	public static class SportsInLetter{
		//jsonArray
		public ArrayList<Sports> sportsInLetter = new ArrayList<_SportsList.Sports>();
	}
	
	public static class Sports{
		
		public String sportId="";
		public String sportName="";
		public String sportFirstLetter="";
		public String sportLogo="";
		public String sportWhiteLogo="";
	}
}

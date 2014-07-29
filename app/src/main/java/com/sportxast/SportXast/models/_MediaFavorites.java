package com.sportxast.SportXast.models;


import com.sportxast.SportXast.models._MediaLists.UserInFavorites;

import java.util.ArrayList;

public class _MediaFavorites {

	
	public ArrayList<UserInFavorites> favoritesList = new ArrayList<UserInFavorites>();
	
	public String resultCount;
	public String pageCount="0";
	public String currentPage="0";
	
	public boolean isFirstLoad = true;
	
}

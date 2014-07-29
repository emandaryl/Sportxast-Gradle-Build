package com.sportxast.SportXast.models;


import com.sportxast.SportXast.models._MediaLists.Tag;

import java.util.ArrayList;

public class _MediaTag {
	
	public ArrayList<Tag> tagList = new ArrayList<Tag>();
	
	public String resultCount;
	public String pageCount="0";
	public String currentPage="0";
	
	public boolean isFirstLoad = true;

}

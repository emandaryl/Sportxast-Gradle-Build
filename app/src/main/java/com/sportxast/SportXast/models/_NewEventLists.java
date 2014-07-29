package com.sportxast.SportXast.models;

import com.sportxast.SportXast.models._MediaLists.User;

import java.util.ArrayList;


public class _NewEventLists {
	
	public ArrayList<NewList> lists = new ArrayList<NewList>();
	public String resultCount = "";
	public String pageCount = "";
	public String currentPage = "";
	
	
   public static class NewList{
	   public String id = "";
	   public String name = "";
	   public User user = new User();
   }
}

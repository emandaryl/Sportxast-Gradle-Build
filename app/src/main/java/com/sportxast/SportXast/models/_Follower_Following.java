package com.sportxast.SportXast.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class _Follower_Following {
	public String resultCount;
	public String pageCount = "0";
	public String currentPage = "0";
	public ArrayList<Follow> Follow = new ArrayList<Follow>();

	public static class Follow {
		public String userId = "";
		public String avatarPath = "";
		public String avatarUrl = "";
		public String hasAvatar = "";
		public String avatarCount = "";
		public String avatarName = "";
		public String fullName = "";
		public String displayName = "";
		public String userName = "";
		public String aboutMe = "";
		public String postCount = "";
		public String favoriteCount = "";
		public String viewCount = "";
		public String isFollowing = "";

	}

	public void parseFollow(JSONObject jsonObject){
		try {
			resultCount =""+ jsonObject.get("resultCount");
			pageCount =""+ jsonObject.get("pageCount");
			currentPage =""+ jsonObject.get("currentPage");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray jsonArray_list = new JSONArray();
		
		try {
			jsonArray_list = jsonObject.getJSONArray("followers");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			jsonArray_list = jsonObject.getJSONArray("following");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			
			for (int i = 0; i < jsonArray_list.length(); i++) {
				JSONObject object = jsonArray_list.getJSONObject(i);
				
				Follow follow = new Follow();
				follow.userId =""+ object.get("userId");
				follow.avatarPath =""+ object.get("avatarPath");
				follow.avatarUrl =""+ object.get("avatarUrl");
				follow.hasAvatar =""+ object.get("hasAvatar");
				follow.avatarCount =""+ object.get("avatarCount");
				follow.avatarName =""+ object.get("avatarName");
				follow.fullName =""+ object.get("fullName");
				follow.displayName =""+ object.get("displayName");
				follow.userName =""+ object.get("userName");
				follow.aboutMe =""+ object.get("aboutMe");
				follow.postCount =""+ object.get("postCount");
				follow.favoriteCount =""+ object.get("favoriteCount");
				follow.viewCount =""+ object.get("viewCount");
				follow.isFollowing =""+ object.get("isFollowing");
				
				Follow.add(follow);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

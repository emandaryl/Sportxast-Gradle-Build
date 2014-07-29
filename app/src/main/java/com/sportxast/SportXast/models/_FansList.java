package com.sportxast.SportXast.models;

import com.sportxast.SportXast.models._MediaLists.FollowCounts;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class _FansList {

	
	public ArrayList<Object> fansList = new ArrayList<Object>(); // object is UserInFavorites
	public String resultCount;
	public String pageCount="0";
	public String currentPage="0";
	
	public void parseFanslist(JSONObject jsonObject){
		try {
			resultCount = jsonObject.getString("resultCount");
			pageCount = ""+jsonObject.get("pageCount");
			currentPage = ""+jsonObject.get("currentPage");
		
		} catch (JSONException e) {e.printStackTrace();}
		
		JSONArray u = new JSONArray();
		try {
			 u= jsonObject.getJSONArray("list");
		} catch (JSONException e) {e.printStackTrace();}
		
			for (int i = 0; i < u.length(); i++) {
				UserInFavorites favorites = new UserInFavorites();
				
				JSONObject uObject = new JSONObject(); 
				
				
				try {
					uObject= u.getJSONObject(i);
				} catch (JSONException e) {e.printStackTrace();}
				
				
				try {
					favorites.userId = ""+uObject.get("userId");
					favorites.displayName = ""+uObject.get("displayName");
					favorites.avatarPath = ""+uObject.get("avatarPath");
					favorites.avatarUrl = ""+uObject.get("avatarUrl");
					favorites.hasAvatar = ""+uObject.get("hasAvatar");
					favorites.avatarCount = ""+uObject.get("avatarCount");
					favorites.avatarName = ""+uObject.get("avatarName");
					favorites.fullName = ""+uObject.get("fullName");
					favorites.userName = ""+uObject.get("userName");
					favorites.email = ""+uObject.get("email");
					favorites.aboutMe = ""+uObject.get("aboutMe");
					favorites.postCount = ""+uObject.get("postCount");
					favorites.favoriteCount = ""+uObject.get("favoriteCount");
					favorites.viewCount = ""+uObject.get("viewCount");
				} catch (JSONException e) {e.printStackTrace();}
				
				    try {
						favorites.isFollowing = ""+uObject.get("isFollowing");
						favorites.isPlayer = ""+uObject.get("isPlayer");
					} catch (JSONException e) {e.printStackTrace();}
				    try {
				    	favorites.followCounts = new FollowCounts();
				    	JSONObject countsobject = uObject.getJSONObject("followCounts");
				    	favorites.followCounts.follower = countsobject.get("follower").toString();
				    	favorites.followCounts.following = countsobject.get("following").toString();
				    	
					} catch (JSONException e) {e.printStackTrace();}    
				    
				    
				fansList.add(favorites);
		
		
			}
	}
}

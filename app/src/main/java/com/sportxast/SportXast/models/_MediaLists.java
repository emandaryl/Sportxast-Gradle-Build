package com.sportxast.SportXast.models;

import android.util.Log;

import com.androidquery.AQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class _MediaLists {
	
	public boolean isFirstLoad = true;
	public boolean isRefresh = false;
	public boolean isEmpty;
	
	public ArrayList<MediaList> mediaLists = new ArrayList<_MediaLists.MediaList>();
	
	public String resultCount;
	public String pageCount="0";
	public String currentPage="0";
	 
	public String error="";
	public String message="";
	public AQuery aq ;
	  
	public static class MediaList{
		
		public String mediaId;
		public String eventId;
		public String sportId;
		public String mediaUserId;
		public String mediaUserName;
		public String mediaType;
		public String coverImage;
		public String coverImageThumb;
		public String largeImageUrl;
		public String mediumImageUrl;
		public String smallImageUrl;
		public String mediaUrl;
		public String mediaShortUrl;
		public String m3u8Url;
		public String vp8Url;
		public String mp4Url;
		public String videoLocalPath;
		public String imageLocalPath;
		public String mediaShareString;
		public String onDate;
		public String age;
		public String viewCount;
		public String favoritesCount;
		public String commentsCount;
		public String shareCount;
		public String twitterCardUrl = "";
		public String score;
		public String currentUserIsOwner;
		public String currentUserHasInFavorites;
		public String transcoderJobStatus;
		
		//public String twitter 
		public User user = new User();
		public  ArrayList<Comments> comments = new ArrayList<Comments>();
		public ArrayList<UserInFavorites> userInFavorites = new ArrayList<UserInFavorites>();
		public ArrayList<Tag> tags = new ArrayList<Tag>(); 
	}
	
	public static class User{
		public String userId="";
		public String avatarPath="";
		public String avatarUrl="";
		public String hasAvatar="";
		public String avatarCount="";
		public String avatarName="";
		public String fullName="";
		public String displayName="";
		public String userName="";
		public String email= "";
		public String aboutMe="";
		public String postCount="";
		public String favoriteCount="";
		public String viewCount="";
		public String isFollowing = "";
		public FollowCounts followCounts = new FollowCounts();
	}
	public static class Comments{
		public String commentId="";
		public String commentParentID="";
		public String commentParentType="";
		public String commentBody="";
		public String commentAge="";
		public String commentUserName="";
		public String commentUserId="";
		public String commentTags="";
		
		public User user = new User();
	}
	
	public static class UserInFavorites{
		public String userId="";;
		public String avatarPath="";
		public String avatarUrl="";
		public String hasAvatar="";
		public String avatarCount="";
		public String avatarName="";
		public String fullName="";
		public String displayName="";
		public String userName="";
		public String email="";
		public String aboutMe="";
		public String postCount="";
		public String favoriteCount="";
		public String viewCount;
		
		public String isFollowing="";
		
		public String isPlayer="";
		
		public FollowCounts followCounts = new FollowCounts();
	}
	public static class Tag{
		public String id="";
		public String name="";
		
	}
	
	public static class FollowCounts{
		public String follower="";
		public String following="";
	}
	
	
	public void parseMediaList(JSONObject jsonObject){
	
		
	//	Log.e("parseMediaList", "parseMediaList");
		
		isFirstLoad=false;
		
		try {
			isEmpty = jsonObject.getJSONArray("mediaList")==null?true:false;
		//	Log.e("isEmpty", isEmpty?"true":"false");
		} catch (JSONException e) {e.printStackTrace();}

				
			
		    if(isEmpty){
				//do nothing
		    	try {
					error = jsonObject.getString("error");
					message = jsonObject.getString("message");
				} catch (JSONException e) {e.printStackTrace();}

				//isEmpty=true;
			}
			else{
				 
				int length = 0;
				JSONArray jsonArray_mediaList = new JSONArray();
				//Log.e("naa", "size : "+jsonArray_mediaList.length());
				
				try {
					JSONArray jsonArray_eventInfo= jsonObject.getJSONArray("eventInfo");
				} catch (JSONException e1) {	e1.printStackTrace();		}
				
				try {
					jsonArray_mediaList= jsonObject.getJSONArray("mediaList");
					resultCount = jsonObject.getString("resultCount");
					pageCount = ""+jsonObject.get("pageCount");
					currentPage = ""+jsonObject.get("currentPage");
					
					isEmpty = Integer.parseInt(resultCount)>0?true:false;
				} catch (JSONException e) {e.printStackTrace();}
				 
				length = jsonArray_mediaList.length();
				
				//mediaLists = new ArrayList<MediaList>();
				
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
				
						aq.cache( m.getString("coverImage"), 0);
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
					
					
//					try {
//						JSONObject jsonObject_followCount = u.getJSONObject("followCounts");
//						mediaList.user.followCounts = new FollowCounts();
//						mediaList.user.followCounts.follower = ""+jsonObject_followCount.get("follower");
//						mediaList.user.followCounts.following = ""+jsonObject_followCount.get("following");
//						
//					} catch (JSONException e) {e.printStackTrace();}
					
				
						mediaList.comments = new ArrayList<Comments>();
						mediaList.comments = parseComments(m);
			
						mediaList.userInFavorites = new ArrayList<UserInFavorites>();
						mediaList.userInFavorites = parseUserInFavorites(m);
						
						mediaList.tags = new ArrayList<Tag>();
						mediaList.tags = parseTags(m);
						
						
					if(isRefresh){
						mediaLists.add(i,mediaList);
					}else{
						mediaLists.add(mediaList);
					}	
					
				}
				//Log.v("end", "end");
			}	
	
	}
	
	public ArrayList<Comments> parseComments(JSONObject json){
		ArrayList<Comments> m_comment = new ArrayList<Comments>();
		
		try {
			JSONArray c = json.getJSONArray("list");
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
	
	public ArrayList<UserInFavorites> parseUserInFavorites(JSONObject json){
		
		ArrayList<UserInFavorites> userInFavorites = new ArrayList<UserInFavorites>();
		
		try {
			JSONArray u = json.getJSONArray("userInFavorites");
			
			for (int i = 0; i < u.length(); i++) {
				JSONObject uObject = u.getJSONObject(i);
				UserInFavorites favorites = new UserInFavorites();
				favorites.userId = ""+uObject.get("userId");
				favorites.avatarPath = ""+uObject.get("avatarPath");
				favorites.avatarUrl = ""+uObject.get("avatarUrl");
				favorites.hasAvatar = ""+uObject.get("hasAvatar");
				favorites.avatarCount = ""+uObject.get("avatarCount");
				favorites.avatarName = ""+uObject.get("avatarName");
				favorites.fullName = ""+uObject.get("fullName");
				favorites.displayName = ""+uObject.get("displayName");
				favorites.userName = ""+uObject.get("userName");
				favorites.email = ""+uObject.get("email");
				favorites.aboutMe = ""+uObject.get("aboutMe");
				favorites.postCount = ""+uObject.get("postCount");
				favorites.favoriteCount = ""+uObject.get("favoriteCount");
				favorites.viewCount = ""+uObject.get("viewCount");
				
				userInFavorites.add(favorites);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userInFavorites;
	}
	
	public ArrayList<Tag> parseTags (JSONObject json){
		ArrayList<Tag> tags = new ArrayList<_MediaLists.Tag>();
	
		try {
			JSONArray t = json.getJSONArray("tags");
			for (int i = 0; i < t.length(); i++) {
				JSONObject tObject =t.getJSONObject(i);
				Tag tag = new Tag();
				
				tag.id = ""+tObject.get("id");
				tag.name = ""+tObject.get("name");
			
				tags.add(tag);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return tags;
	}
	
	
}

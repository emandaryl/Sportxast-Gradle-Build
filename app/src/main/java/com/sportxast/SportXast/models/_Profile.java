package com.sportxast.SportXast.models;

public class _Profile {
	/*
	
	"avatarPath":"db\/user_avatar\/000\/055\/673",
	"aboutMe":"fighting crimes trying to save the world",
	"avatarCount":4,
	"avatarName":"avatar_3.jpg",
	"favoriteCount":"17",
	"avatarUrl":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/055\/673\/avatar_3.jpg",
	"userId":"55673",
	"userName":"jencurativo12",
	"postCount":"257",
	"fullName":"Jennifer Curativo",
	"hasAvatar":1,
	"displayName":"jencurativo12",
	"isFollowing":0,
	"viewCount":"740" 
	*/
	public String userId		= "";
	public String avatarPath	= "";
	public String avatarUrl		= "";
	public String hasAvatar		= "0";
	public String avatarCount	= "";
	public String avatarName	= "";
	public String fullName		= "";
	public String displayName	= "";
	public String userName		= "";
	public String email			= "";
	public String aboutMe		= "";
	public String postCount		= "";
	public String favoriteCount	= "0";
	public String viewCount		= "0";
	public String isFollowing	= "0";
	public String type			= "";
	public followCounts fCounts = new followCounts();
	
	public static class followCounts{
		public String follower;
		public String following;
	}
	
	
}

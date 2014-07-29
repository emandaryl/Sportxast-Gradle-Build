package com.sportxast.SportXast.commons;

import android.content.Context;

import com.sportxast.SportXast.models._EventLists.EventLists;

public class GlobalVariablesHolder {
  
	//public static int EventID_Global = -1; 
	/** default is null **/
	public static EventLists FLatestEvent = null;  
	
	public static boolean alreadyCheckedIntoAnEvent = false;
	
	/** default value SHOULD be 0 **/
	public static double user_Latitude = 0;
	
	/** default value SHOULD be 0 **/
	public static double user_Longitude = 0;
	
	public static boolean firstTimeUseOfVideoCapture = false;
	
	public static String X_DEVICE_ID  	= "";
	public static String X_SESSION_ID 	= ""; 
	public static String X_USER_ID 		= ""; 
	public static String X_USER_NAME 	= "";
	
	public static String X_USERCOUNTRY  = "";
	public static String X_USERLOCALITY	= "";  
	
	public static Context currentActivityContext = null;
	
	/** Flag if RunnableChecker is currently running. returns TRUE or FALSE (DEFAULT: False)**/
	public static boolean threadUploaderIsRunning = false;
	
	
	public static boolean pauseBackgroundService = false; 
	
	/** dev or test -- Default: dev **/
	public static String APISubDomain = "dev";
	 
	public static String S3_VIDEO_BUCKET;
	public static String S3_IMAGE_BUCKET;
	 
	
	/*
	public static int selectedLanguage = 0; //default is: 0 - english
	public static int selectedStudy = -1; 
	//public static final String[] LANG_LISTx = { "English", "Chinese" };

	// public static ArrayList<Study> rootStudies;
	public static ArrayList<String> studyNameList = new ArrayList<String>();
	
	*/
	
}

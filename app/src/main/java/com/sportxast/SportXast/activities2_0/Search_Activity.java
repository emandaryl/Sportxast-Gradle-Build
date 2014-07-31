package com.sportxast.SportXast.activities2_0;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.EventsAdapter;
import com.sportxast.SportXast.adapter2_0.HashTagAdapter;
import com.sportxast.SportXast.adapter2_0.ProfileDataAdapter;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models._EventLists.EventLists;
import com.sportxast.SportXast.models._Profile;
import com.sportxast.SportXast.models._Profile.followCounts;
import com.sportxast.SportXast.models._SearchHashtag;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings("deprecation") 
public class Search_Activity extends Activity{
	/** Header composition **/
	private HeaderUIClass headerUIClass;
	private RelativeLayout sx_header_wrapper; 
 
	/** Coordinates **/
	private Double FCorLatitude;
	private Double FCorLongitude;  
	public int FCurrentTabNumber;
	  
	private RelativeLayout pbLoading_container2; 
	private Button btn_tab_events; 
	private Button btn_tab_fans; 
	private Button btn_tab_tags;

	private ArrayList<_Profile> FArrFansList;
	private ArrayList<_SearchHashtag> FArrTagsList;
	  
	/** 0-Events, 1-Fans, 2-Tags **/
	private int FCurrentTab;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // TODO Auto-generated method stub
	    setContentView(R.layout.layout_search_atan);
	     
	    this.FCorLatitude = GlobalVariablesHolder.user_Latitude;
		this.FCorLongitude= GlobalVariablesHolder.user_Longitude;  
		
		initializeResources();    
	    prepareHeader();  
	    this.btn_tab_events.performClick(); 
	  }
	  
	private EditText edittext_search;
	@SuppressLint("ResourceAsColor")
	private void initializeResources(){  
		this.pbLoading_container2 = (RelativeLayout) findViewById(R.id.pbLoading_container2); 
		   
		this.edittext_search = (EditText) findViewById(R.id.edittext_search);
		this.edittext_search.addTextChangedListener( new GenericTextWatcher( 0 ) ); 
		
		this.btn_tab_events = (Button) findViewById(R.id.btn_tab_events);
		this.btn_tab_events.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initializeCustomListView_EVENTS();
				gatherEventList();   
				updateButtonBackground( 0 ); 
				FCurrentTab = 0;
			}
		});
		   
		this.btn_tab_fans 	= (Button) findViewById(R.id.btn_tab_fans);
		this.btn_tab_fans.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initializeCustomListView_FANS();
				gatherFansList(); 
				updateButtonBackground( 1 ); 
				FCurrentTab = 1;
			}
		}); 
		this.btn_tab_tags	= (Button) findViewById(R.id.btn_tab_tags);  
		this.btn_tab_tags.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initializeCustomListView_TAGS();
				gatherTagsList(); 
				updateButtonBackground( 2 );

				FCurrentTab = 2;
			}
		}); 
		 
		//########################################
		this.btn_tab_events.setBackgroundResource(R.drawable.btn_pnl_white_left1); 
		this.btn_tab_fans.setBackgroundResource(R.drawable.btn_pnl_white_mid1);
		this.btn_tab_tags.setBackgroundResource(R.drawable.btn_pnl_white_right1);
		/*
		this.btn_tab_events.setTextColor(R.color.uni_blue);
		this.btn_tab_fans.setTextColor(R.color.uni_blue);
		this.btn_tab_tags.setTextColor(R.color.uni_blue);*/
		
		this.btn_tab_events.setTextColor(Color.parseColor("#3399cc"));
		this.btn_tab_fans.setTextColor(Color.parseColor("#3399cc"));
		this.btn_tab_tags.setTextColor(Color.parseColor("#3399cc"));
		 
	}

	private class GenericTextWatcher implements TextWatcher{
		private int searchType; 
	     
	    private GenericTextWatcher(final int searchType) { //wordType 0 <== word, 1 <== definition
	       this.searchType = searchType; 
	    }

	    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
	    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	    //	Toast.makeText(getApplicationContext(), this.correctWord, Toast.LENGTH_SHORT).show();
	    }

	    public void afterTextChanged(Editable editable) {
			// TODO Auto-generated method stub 
	    	String stringToSearch = editable.toString().trim();
	    	edittext_search.setSelection(stringToSearch.length());
			     
			//Toast.makeText(getApplicationContext(), "CURRENT TAB YEAH: "+Search_Activity.getInstance().getCurrentTabNumber() , Toast.LENGTH_SHORT).show();
			
			int st = this.searchType; 
			if( stringToSearch.length() > 3){   
				beginSearching();     
			}else{
				
			}  
		}
	}
	
	private void beginSearching(){
		
		switch (FCurrentTab) {
		case 0:
			this.btn_tab_events.performClick();  
			break;
			
		case 1: 
			this.btn_tab_fans.performClick();  
			break;
			     
		case 2: 
			this.btn_tab_tags.performClick(); 
			break;
			
		default:
			break;
		}
		   
		//#############################
		//HIDE KEYBOARD
		((InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext_search.getWindowToken(), 0);
	}
	
	@SuppressLint("ResourceAsColor")
	private void updateButtonBackground(int buttonIndex){
		
		this.btn_tab_events.setBackgroundResource(R.drawable.btn_pnl_white_left1); 
		this.btn_tab_fans.setBackgroundResource(R.drawable.btn_pnl_white_mid1);
		this.btn_tab_tags.setBackgroundResource(R.drawable.btn_pnl_white_right1);
		
		/*
		this.btn_tab_events.setTextColor(R.color.uni_blue);
		this.btn_tab_fans.setTextColor(R.color.uni_blue);
		this.btn_tab_tags.setTextColor(R.color.uni_blue);*/
		
		this.btn_tab_events.setTextColor(Color.parseColor("#3399cc"));
		this.btn_tab_fans.setTextColor(Color.parseColor("#3399cc"));
		this.btn_tab_tags.setTextColor(Color.parseColor("#3399cc"));
		
		switch (buttonIndex) {
		case 0:
			this.btn_tab_events.setBackgroundResource(R.drawable.btn_pnl_blue_left1);
			this.btn_tab_events.setTextColor(Color.WHITE);
			break;
		case 1:
			this.btn_tab_fans.setBackgroundResource(R.drawable.btn_pnl_blue_mid1);
			this.btn_tab_fans.setTextColor(Color.WHITE);
			break;
		case 2:
			this.btn_tab_tags.setBackgroundResource(R.drawable.btn_pnl_blue_right1);
			this.btn_tab_tags.setTextColor(Color.WHITE);
			break;
		default:
			break;
		} 
	}
	
	private ListView FPullToRefreshListView;  
	private ArrayList<EventLists> FArrEventLists; 
	
	private EventsAdapter 			FEventsAdapter;
	private ProfileDataAdapter 		FSearchFanAdapter;
	//private SearchHashtagAdapter	FSearchHashtagAdapter;
	
	private void initializeCustomListView_EVENTS( ) { 
		//FPullToRefreshListView = (ProperListView) findViewById(R.id.ptrListViewHighlight); 
		FPullToRefreshListView = (ListView) findViewById(R.id.ptrListViewHighlight); 
		//FPullToRefreshListView = new PullToRefreshListView(this);
		//FPullToRefreshListView.setCacheColorHint(Color.parseColor("#00000000"));  
		FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT); 
		
		//FPullToRefreshListView.addFooterView(footervw); 
		FPullToRefreshListView.setDivider(null);
		 
		if(FArrEventLists == null){
			FArrEventLists = new ArrayList<EventLists>();
			//EventLists eventLists = new EventLists();
			//FArrEventLists.add(eventLists); 
		}
	  
		FEventsAdapter = new EventsAdapter(Search_Activity.this, FArrEventLists);
		FPullToRefreshListView.setAdapter(FEventsAdapter); 
		//FPullToRefreshListView.setOnScrollListener(onScrollListener);  
	}
	
	private void initializeCustomListView_FANS( ) {  
		if(FPullToRefreshListView != null){
			FPullToRefreshListView.invalidateViews(); 
			FPullToRefreshListView.setAdapter(null);
		}
		 
		FPullToRefreshListView = (ListView) findViewById(R.id.ptrListViewHighlight); 
		//FPullToRefreshListView = new PullToRefreshListView(this);
		//FPullToRefreshListView.setCacheColorHint(Color.parseColor("#00000000")); 
		
		FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT); 
		
		//FPullToRefreshListView.addFooterView(footervw); 
		FPullToRefreshListView.setDivider(null);
		
		if(FArrFansList == null){
			FArrFansList = new ArrayList<_Profile>(); 
			//_Profile searchUser = new _Profile(); 
			//FArrFansList.add(searchUser); 
		}
		 
		FArrFansList.trimToSize(); 
		FSearchFanAdapter = new ProfileDataAdapter(Search_Activity.this, FArrFansList, 3);
		FPullToRefreshListView.setAdapter(FSearchFanAdapter); 
	//	FPullToRefreshListView.setOnScrollListener(onScrollListener);  
	} 
	
	@SuppressWarnings("rawtypes")
	private HashTagAdapter FSearchHashtagAdapter;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initializeCustomListView_TAGS( ) {  
		if(FPullToRefreshListView != null){
			FPullToRefreshListView.invalidateViews(); 
			FPullToRefreshListView.setAdapter(null);
		}
		 
		FPullToRefreshListView = (ListView) findViewById(R.id.ptrListViewHighlight); 
		//FPullToRefreshListView = new PullToRefreshListView(this);
		//FPullToRefreshListView.setCacheColorHint(Color.parseColor("#00000000")); 
		
		FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT); 
		
		//FPullToRefreshListView.addFooterView(footervw); 
		FPullToRefreshListView.setDivider(null);
		
		if(FArrTagsList == null){
			FArrTagsList = new ArrayList<_SearchHashtag>();  
			//_SearchHashtag searchHashtag = new _SearchHashtag(); 
			//FArrTagsList.add(searchHashtag); 
		}
		 
		FArrTagsList.trimToSize(); 
		FSearchHashtagAdapter = new HashTagAdapter(Search_Activity.this, FArrTagsList);
		FPullToRefreshListView.setAdapter(FSearchHashtagAdapter); 
	//	FPullToRefreshListView.setOnScrollListener(onScrollListener);  
	} 
	  
	private void gatherTagsList(){    
		String stringToSearch = edittext_search.getText().toString().trim();
		if(stringToSearch.length() <= 0){
		
			showProgressCover(View.VISIBLE, View.GONE);
			 
			return;
		}
		 
		showProgressCover(View.VISIBLE, View.VISIBLE);
		 
		Async_HttpClient async_HttpClient = new Async_HttpClient(Search_Activity.this);
		async_HttpClient = new Async_HttpClient(this);
		String appendUrl = "";
		RequestParams requestParams = new RequestParams(); 
		  
		/* EVENTS
		https://dev.sportxast.com/phone/apiV2/ExportEvents?latitude=10.319821&longitude=123.905381&page=1&pageSize=20&q=aya&type=search 
		*/ 
		/* FANS
		https://dev.sportxast.com/phone/apiV2/Search?latitude=10.319792&limit=20&longitude=123.905392&page=1&q=lex&type=user
		*/
		
		/* TAGS
		https://dev.sportxast.com/phone/apiV2/Search?latitude=10.319792&limit=20&longitude=123.905392&page=1&q=lex&type=hashtag
		*/ 
		 
		appendUrl = "Search";
		requestParams.put("latitude", 	this.FCorLatitude);
		requestParams.put("longitude", 	this.FCorLongitude);
		requestParams.put("limit",		"20");
		requestParams.put("page", 		"1");
		requestParams.put("q",		 	stringToSearch);
		requestParams.put("type",		"hashtag");
		
		async_HttpClient.GET(appendUrl, requestParams,
				new JsonHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}
  
					@Override
					public void onSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						/* SAMPLE RESPONSE:  
						 {
						 "pageCount":1,
						 "resultCount":"2",
						 "list":[
						 	
						 	{
						 	"type":"tag",
						 	"id":"332",
						 	"avatarPath":"",
						 	"name":"gutter"
						 	},
						 	
						 	{
						 	"type":"tag",
						 	"id":"127",
						 	"avatarPath":"",
						 	"name":"Interception"
						 	}
						 		],
						 "currentPage":"1"
						 } 
						 */
						
						String error = "0";
						try {
							if( response.has("error") )
								error = response.getString("error");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(error.equals("1")){
							showProgressCover(View.VISIBLE, View.GONE); 
							return;
						} 
						//#################################
						String hey = "";
						String ho = hey; 
						 
						JSONArray tagsListJSON = null;
						try {
							if( response.has("list") )
								tagsListJSON = response.getJSONArray("list");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
						if(tagsListJSON != null){
							FArrTagsList = parseTags( tagsListJSON );
						}
						
						FArrTagsList.trimToSize();
						reloadListView( 2, FArrTagsList );   
						
						showProgressCover(View.GONE, View.GONE); 
					}

					@Override
					public void onFailure(int arg0, Header[] arg1,
							byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1, arg2, arg3);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
							super.onFinish(); 
							showProgressCover(View.GONE, View.GONE); 
						}
					}); 
	}
	
	private ArrayList<_SearchHashtag> parseTags( JSONArray tagsListJSON ){ 
		ArrayList<_SearchHashtag> arrTagsList = new ArrayList<_SearchHashtag>(); 
		try { 
			for (int j = 0; j < tagsListJSON.length(); j++) {
				JSONObject profileDataJSON = tagsListJSON.getJSONObject(j);
				/*  	{
						 	"type":"tag",
						 	"id":"332",
						 	"avatarPath":"",
						 	"name":"gutter"
						 	}, */  
				 
				_SearchHashtag searchHashtag = new _SearchHashtag();
				
				searchHashtag.type 		= profileDataJSON.getString("type");
				searchHashtag.id 		= profileDataJSON.getString("id");
				searchHashtag.avatarPath= profileDataJSON.getString("avatarPath");
				searchHashtag.name		= profileDataJSON.getString("name");
				   
				arrTagsList.add(searchHashtag);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			}
		 
		return arrTagsList; 
	}
	 
	private void gatherFansList(){ 

		String stringToSearch = edittext_search.getText().toString().trim();
		if(stringToSearch.length() <= 0){
			
			showProgressCover(View.VISIBLE, View.GONE); 
		 
			return;
		}
		showProgressCover(View.VISIBLE, View.VISIBLE);  
		
		Async_HttpClient async_HttpClient = new Async_HttpClient(Search_Activity.this);
		async_HttpClient = new Async_HttpClient(this);
		String appendUrl = "";
		RequestParams requestParams = new RequestParams(); 
		  
		/* EVENTS
		https://dev.sportxast.com/phone/apiV2/ExportEvents?latitude=10.319821&longitude=123.905381&page=1&pageSize=20&q=aya&type=search 
		*/
		
		/* FANS
		https://dev.sportxast.com/phone/apiV2/Search?latitude=10.319792&limit=20&longitude=123.905392&page=1&q=lex&type=user
		*/
		
		/* TAGS
		https://dev.sportxast.com/phone/apiV2/Search?latitude=10.319792&limit=20&longitude=123.905392&page=1&q=lex&type=hashtag
		*/ 
		 
		appendUrl = "Search";
		requestParams.put("latitude", 	this.FCorLatitude);
		requestParams.put("longitude", 	this.FCorLongitude);
		requestParams.put("limit",		"20");
		requestParams.put("page", 		"1");
		requestParams.put("q",		 	stringToSearch);
		requestParams.put("type",		"user");
		
		async_HttpClient.GET(appendUrl, requestParams,
				new JsonHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}
  
					@Override
					public void onSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						//{"message":"Empty list","error":1}
						
						/* SAMPLE RESPONSE: 
						 {
						 "pageCount":2,
						 "resultCount":"15",
						 "list":[
						 	{
						 		"userName":"brett test",
						 		"fullName":"brett",
						 		"type":"user",
						 		"avatarPath":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/006\/948\/avatar_3.jpg",
						 		"isFollowing":0,
						 		"aboutMe":"","userId":"6948"
						 	},
						 	
						 	{
						 		"userName":"jennTest",
						 		"fullName":"Jenntest ProfilePic",
						 		"type":"user",
						 		"avatarPath":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/055\/601\/avatar_3.jpg",
						 		"isFollowing":0,
						 		"aboutMe":"",
						 		"userId":"55601"
						 	},
						 	
						 	{
						 		"userName":"lexlab",
						 		"fullName":"Lexter Rama Labra",
						 		"type":"user",
						 		"avatarPath":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/076\/923\/avatar_11.jpg",
						 		"isFollowing":0,
						 		"aboutMe":"mottolite",
						 		"userId":"76923"
						 	}
						 		],
						 "currentPage":"1"
						 }  */	
						
						String error = "0";
						try {
							if( response.has("error") )
								error = response.getString("error");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(error.equals("1")){
							showProgressCover(View.VISIBLE, View.GONE); 
							return;
						} 
						//#################################
						String hey = "";
						String ho = hey; 
						
						JSONArray fansListJSON = null;
						try {
							if( response.has("list") )
								fansListJSON = response.getJSONArray("list");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(fansListJSON != null)
							 FArrFansList = parseFans( fansListJSON );
					//	else FArrFansList = new ArrayList<_Profile>();
						FArrFansList.trimToSize();
						reloadListView( 1, FArrFansList );   
				  
						showProgressCover(View.GONE, View.GONE); 
					}

					@Override
					public void onFailure(int arg0, Header[] arg1,
							byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1, arg2, arg3);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
							super.onFinish();
							
							showProgressCover(View.GONE, View.GONE); 
						}
					}); 
	}
	  
	
		
	   
		private ArrayList<_Profile> parseFans( JSONArray fansListJSON ){ 
			ArrayList<_Profile> arrFollowersList = new ArrayList<_Profile>(); 
			try { 
				for (int j = 0; j < fansListJSON.length(); j++) {
					JSONObject profileDataJSON = fansListJSON.getJSONObject(j);
					/* {
				 		"userName":"brett test",
				 		"fullName":"brett",
				 		"type":"user",
				 		"avatarPath":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/006\/948\/avatar_3.jpg",
				 		"isFollowing":0,
				 		"aboutMe":"","userId":"6948"
				 		} */  
					String profUserId = "";
					if( profileDataJSON.has("userId")){
						profUserId = profileDataJSON.getString("userId");  
					}
					
					if( profUserId.equals(GlobalVariablesHolder.X_USER_ID) )
						continue;
					
					_Profile followerProfileData = new _Profile();
					
					followerProfileData.userName		= profileDataJSON.getString("userName");
					followerProfileData.fullName		= profileDataJSON.getString("fullName");  
					followerProfileData.type			= profileDataJSON.getString("type");   
					followerProfileData.avatarPath		= profileDataJSON.getString("avatarPath");
					followerProfileData.isFollowing		= profileDataJSON.getString("isFollowing"); 
					followerProfileData.aboutMe			= profileDataJSON.getString("aboutMe"); 
					
					/*
					followerProfileData.avatarCount		= profileDataJSON.getString("avatarCount"); 
					followerProfileData.avatarName		= profileDataJSON.getString("avatarName");
					followerProfileData.favoriteCount	= profileDataJSON.getString("favoriteCount");
					followerProfileData.avatarUrl		= profileDataJSON.getString("avatarUrl"); 
					followerProfileData.userId			= profileDataJSON.getString("userId"); 
					followerProfileData.postCount		= profileDataJSON.getString("postCount"); 
					followerProfileData.hasAvatar		= profileDataJSON.getString("hasAvatar");
					followerProfileData.displayName		= profileDataJSON.getString("displayName");
					*/
					
					if( profileDataJSON.has("viewCount") )
						followerProfileData.viewCount		= profileDataJSON.getString("viewCount");
					
					followerProfileData.email			= ""; 
					followerProfileData.fCounts 		= new followCounts();
					 
					arrFollowersList.add( followerProfileData );
				}
			} catch (JSONException e) {
				e.printStackTrace();
				}
			
			return arrFollowersList; 
		}
		
	/** parameters must be: View.VISIBLE, View.GONE, View.INVISIBLE 
		 @params **/
	private void showProgressCover( final int showView, final int showWithCover){
		 
		if(pbLoading_container2 != null ){ 
			pbLoading_container2.setVisibility(showView);  
			( (ProgressBar) findViewById(R.id.pbLoading2) ).setVisibility(showWithCover);  
		}
	}
	
	private void gatherEventList(){   
		String stringToSearch = edittext_search.getText().toString().trim();
		if(stringToSearch.length() <= 0){
			showProgressCover(View.VISIBLE, View.GONE); 
		 
			return;
		}
		
		showProgressCover(View.VISIBLE, View.VISIBLE); 
		 
		Async_HttpClient async_HttpClient = new Async_HttpClient(Search_Activity.this);
		async_HttpClient = new Async_HttpClient(this);
		String appendUrl = "";
		RequestParams requestParams = new RequestParams(); 
		  
		/* EVENTS
		https://dev.sportxast.com/phone/apiV2/ExportEvents?latitude=10.319821&longitude=123.905381&page=1&pageSize=20&q=aya&type=search 
		*/
		
		/* FANS
		https://dev.sportxast.com/phone/apiV2/Search?latitude=10.319792&limit=20&longitude=123.905392&page=1&q=lex&type=user
		*/
		
		/* TAGS
		https://dev.sportxast.com/phone/apiV2/Search?latitude=10.319792&limit=20&longitude=123.905392&page=1&q=lex&type=hashtag
		*/ 
		 
		appendUrl = "ExportEvents";
		requestParams.put("latitude", 	this.FCorLatitude);
		requestParams.put("longitude", 	this.FCorLongitude);
		requestParams.put("page",		"1");
		requestParams.put("pageSize", 	"20");
		requestParams.put("q",		 	stringToSearch);
		requestParams.put("type",		"search");
		
		async_HttpClient.GET(appendUrl, requestParams,
				new JsonHttpResponseHandler() { 
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}
  
					@Override
					public void onSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
							
						String error = "0";
						try {
							if( response.has("error") )
								error = response.getString("error");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(error.equals("1")){
							showProgressCover(View.VISIBLE, View.GONE); 
							return;
						} 
						//#################################
						
						FArrEventLists = CommonFunctions_1.parseEvents(response);  
						 
						reloadListView( 0, FArrEventLists );  
						//parseSearchResult( response); 
						showProgressCover(View.GONE, View.GONE); 
					}

					@Override
					public void onFailure(int arg0, Header[] arg1,
							byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1, arg2, arg3);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
							super.onFinish();
							
							showProgressCover(View.GONE, View.GONE); 
						}
					}); 
	}
 
	/** listType: 0 - Events, 1 - fans, 2-tags **/
	@SuppressWarnings("unchecked")  
	public void reloadListView( int listType, Object dataArray  ) {  
		 
		switch (listType) { 
		case 0:
			FEventsAdapter.updateListElements( (ArrayList<EventLists>) dataArray );  
			break;
		case 1: 
			FSearchFanAdapter.updateListElements( (ArrayList<_Profile>) dataArray  ); 
			showProgressCover(View.GONE, View.GONE); 
			break;
		case 2:  
			FSearchHashtagAdapter.updateListElements( (ArrayList<_SearchHashtag>) dataArray  ); 
			showProgressCover(View.GONE, View.GONE); 
			break;	
		default:
			break; 
		} 
		
		//FAdapter.updateListElements( arrMediaLists );  
		FPullToRefreshListView.invalidateViews(); 
		FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT); 
		//FAdapter.notifyDataSetChanged();
		//################################################################  
	}
	
	private void prepareHeader(){ 
		this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
		this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper); 
		this.headerUIClass.setMenuTitle("Search");
	  
		this.headerUIClass.showBackButton(	 true); 
		this.headerUIClass.showAddButton(	 false);  
		this.headerUIClass.showMenuButton(	 false);  
		this.headerUIClass.showRefreshButton(false);  
		this.headerUIClass.showAboutButton(	 false); 
		this.headerUIClass.showSearchButton( false); 
		this.headerUIClass.showDoneButton( 	 false);  
		this.headerUIClass.showCameraButton( false); 
		this.headerUIClass.showMenuTitle(	 true); 
		
		//this.headerUIClass.setMenuTitle(	 false);
		this.headerUIClass.showMenuTitle0(	 false); 
		//this.headerUIClass.setDoneButtonText("Save");
		 
		addHeaderButtonListener(); 
	}

	private void addHeaderButtonListener(){
		this.headerUIClass.setOnHeaderButtonClickedListener(new HeaderUIClass.OnHeaderButtonClickedListener() {
			
			@Override
			public void onSearchClicked() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRefreshClicked() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMenuClicked() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDoneClicked() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onCameraClicked() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBackClicked() {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public void onAddClicked() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAboutClicked() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	 
}

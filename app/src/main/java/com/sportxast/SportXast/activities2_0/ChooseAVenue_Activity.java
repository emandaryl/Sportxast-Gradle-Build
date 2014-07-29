package com.sportxast.SportXast.activities2_0;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.ChooseAVenueListAdapter;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models._Venue;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//import com.sportxast.SportXast.BaseSherlockActivity;

public class ChooseAVenue_Activity extends Activity {
	//TODO : Display available Venue to List
	/** Called when the activity is first created. */
	
	private HeaderUIClass headerUIClass;
	private RelativeLayout sx_header_wrapper;
	
	private Global_Data global_Data;
	private HeaderListView headerListView;
	private EditText edittxt_search;
	
	private Async_HttpClient async_HttpClient;
	private ArrayList<_Venue> lists_venue;
	private ArrayList<_Venue> lists_venue_filtered;
	
	 
	private JSONObject JSONVenueChosen;
	private RelativeLayout pbLoading_container; 
	public void exitActivity( _Venue venueChosen ){  
		this.JSONVenueChosen = new JSONObject();
		
		/*sample Jason outpud:
		 {"shareUrl":"http:\/\/goo.gl\/1GdDMn","mediaId":"11929","shareText":"Watch this highlight captured by SportXast","added":"new media added","imagePath":"db\/event_media\/2014\/06\/02\/11929.jpg","videoPath":"db\/event_media\/2014\/06\/02\/11929.mp4"}
		*/ 
		try {
			this.JSONVenueChosen.put("placeId", 		venueChosen.placeId);
			this.JSONVenueChosen.put("placeName", 		venueChosen.placeName);
			this.JSONVenueChosen.put("placeAddr",		venueChosen.placeAddr);
			this.JSONVenueChosen.put("placeLatitude",	venueChosen.placeLatitude);
			this.JSONVenueChosen.put("placeLongitude",	venueChosen.placeLongitude); 
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//######################################################
    	Intent returnIntent = new Intent();
    	returnIntent.putExtra("JSONVenueChosen", this.JSONVenueChosen.toString() );
    	setResult(RESULT_OK, returnIntent); 
    	//######################################################
		
		finish();
	//	return jsonObject; 
	} 
	/** Coordinates **/
	private Double FCorLatitude;
	private Double FCorLongitude; 

	/** Called when the activity is first created. */ 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_sportslist);
		global_Data = (Global_Data) getApplicationContext();
		
		
		this.FCorLatitude	= GlobalVariablesHolder.user_Latitude;
		this.FCorLongitude	= GlobalVariablesHolder.user_Longitude;
		
	    /*
		initActionBarObjects();
		getActionbar_Menu_Item("Venues");
		*/
		prepareHeader();
		initContentView(); 
	}

	private void prepareHeader(){ 
		
		this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
		this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper); 
		this.headerUIClass.setMenuTitle("Venues");
		this.headerUIClass.showMenuButton(false);
		this.headerUIClass.showBackButton(true);
		this.headerUIClass.showSearchButton(false);
		this.headerUIClass.showDoneButton(false); 
		
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
	
	private RelativeLayout suggested_text_cont;
	private TextView suggested_text;
	 
	/** @category Custom Method
	 * Custom function for initializing content view and declared objects*/
	public void initContentView() {
		
		this.suggested_text_cont = (RelativeLayout) findViewById(R.id.suggested_text_cont); 
		this.suggested_text_cont.setVisibility(View.GONE);
		this.suggested_text 	 = (TextView) suggested_text_cont.findViewById(R.id.suggested_text); 
		this.suggested_text.setTag("");
		this.suggested_text.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				String customVenueName = v.getTag().toString();  
				
				_Venue venue = new _Venue();
				venue.placeId 		= "-69";
				venue.placeName 	= customVenueName;
				venue.placeAddr		= "";
				venue.placeLatitude	= String.valueOf( GlobalVariablesHolder.user_Latitude ); 
				venue.placeLongitude= String.valueOf( GlobalVariablesHolder.user_Longitude ); 
				
				exitActivity( venue ); 
			}
		});
		
		
		pbLoading_container = (RelativeLayout) findViewById( R.id.pbLoading_container ); 
		
		headerListView = (HeaderListView) findViewById(R.id.listvw_sportlists);
		initSearch();
		async_HttpClient = new Async_HttpClient(this);
		fetchData();

	}

	/**
	 * @category Custom Method 
	 * Custom function for initializing edittxt_search and add a TextChangedListener to it to read edittxt_search input event. */
	public void initSearch() {

		edittxt_search = (EditText) findViewById(R.id.edittext_search); 
		edittxt_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				/*
				if (arg0.toString().length() > 0) {
					lists_venue = new ArrayList<_Venue>();
					fetchData(); 
				} else {

				}
				*/
				if (arg0.toString().length() > 0) { 
					getSearchResult(arg0.toString()); 
				} else { 
					headerListView.setAdapter(new ChooseAVenueListAdapter(ChooseAVenue_Activity.this, lists_venue));
				 }
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	@SuppressLint("DefaultLocale")
	public void getSearchResult(String search) {
		int strToSearchLength = search.length();
		 
		lists_venue_filtered = new ArrayList<_Venue>();
		boolean matchIsFound = false;
		
		for (int j = 0; j < 4; j++) {
			 
			for (int i = 0; i < lists_venue.size(); i++) { 
				String placeName_full = lists_venue.get(i).placeName;
				
				String[] sportWordElements = placeName_full.split("\\s+");
				
				if(sportWordElements.length < (j+1) )
					continue;
				 
				String placeName_sub = sportWordElements[j].toString().toLowerCase();
				
				int strToSearchLength_ = strToSearchLength; 
				if(strToSearchLength_ > placeName_sub.length() )
					strToSearchLength_ = placeName_sub.length();
				 
				String placeNameInitialChars = placeName_sub.substring( 0, strToSearchLength_ ).toLowerCase();
				//sportNameInitialChars = sportNameInitialChars.toLowerCase();
	 
				if( placeNameInitialChars.equals(search) ){ 
					lists_venue_filtered.add( (_Venue) lists_venue.get(i) ); 
					//sportsInLetter_.sportsInLetter.add(sports); 
					matchIsFound = true;
				} 
			}
			
			/*
			if(matchIsFound)
				 break;
			*/
		} 
		 
		headerListView.setAdapter(new ChooseAVenueListAdapter(this, lists_venue_filtered)); 
		
		if(matchIsFound == false){ 
			//Toast.makeText(getApplicationContext(), "NAA bai", Toast.LENGTH_SHORT).show();
			headerListView.setVisibility(View.GONE);
			
			suggested_text.setText("Add \""+search+"\"?");
			suggested_text.setTag( search );
			this.suggested_text_cont.setVisibility(View.VISIBLE);
		}else{
			
			//Toast.makeText(getApplicationContext(), "wala bai", Toast.LENGTH_SHORT).show();
			headerListView.setVisibility(View.VISIBLE);
			this.suggested_text_cont.setVisibility(View.GONE);
		}
		
	}


/** @category Custom Method
 * Fetch list of venue from server.**/
	public void fetchData() {
		
		pbLoading_container.setVisibility(View.VISIBLE);
		
		RequestParams requestParams = new RequestParams();
		requestParams.put("q", 			edittxt_search.getText().toString());
		
		requestParams.put("latitude",  String.valueOf( FCorLatitude ) );
		requestParams.put("longitude", String.valueOf( FCorLongitude) ); 

		async_HttpClient.GET("VenueList", requestParams,
				new JsonHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						Log.v("onStart", "onStart"); 
					}

					@Override
					public void onSuccess(JSONArray response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						parseData(response);
						pbLoading_container.setVisibility(View.GONE);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						Log.v("onFinish", "onFinish"); 
					}

					@Override
					public void onFailure(String responseBody, Throwable error) {
						// TODO Auto-generated method stub
						super.onFailure(responseBody, error);
						Log.v("onFailure", "onFailure :" + responseBody + " : " + error); 
					}
				});
	}


/** @category Custom Method 
 *  Parse JSONArray result.
 *  @param (JSONArray) jsonArray_response - the JSONArray result.
 * **/
	public void parseData(JSONArray jsonArray_response) {
		lists_venue = new ArrayList<_Venue>();
		try { 
			for (int i = 0; i < jsonArray_response.length(); i++) {

				JSONObject object 	= jsonArray_response.getJSONObject(i);
				_Venue venue 		= new _Venue(); 
				venue.placeId 		= "" + object.get("placeId");
				venue.placeName 	= "" + object.get("placeName");
				venue.placeAddr 	= "" + object.get("placeAddr");
				venue.placeLatitude = "" + object.get("placeLatitude");
				venue.placeLongitude= "" + object.get("placeLongitude");
				venue.placeType 	= "" + object.get("placeType");

				lists_venue.add(venue); 
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		lists_venue_filtered = lists_venue; 
		headerListView.setAdapter(new ChooseAVenueListAdapter(this, lists_venue_filtered));
	}

}

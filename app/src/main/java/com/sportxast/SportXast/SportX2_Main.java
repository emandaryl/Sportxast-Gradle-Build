package com.sportxast.SportXast;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.activities2_0.Create_Activity;
import com.sportxast.SportXast.activities2_0.EventDetail_Activity;
import com.sportxast.SportXast.activities2_0.Highlight_Activity;
import com.sportxast.SportXast.activities2_0.Menu_Activity;
import com.sportxast.SportXast.activities2_0.Search_Activity_ATAN;
import com.sportxast.SportXast.adapter2_0.EventsAdapter;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Dialog;
import com.sportxast.SportXast.commons.DialogSettings;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models._EventLists;
import com.sportxast.SportXast.models._EventLists.EventLists;
import com.sportxast.SportXast.tasks.Scheduler;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.PullToRefreshListView;
import com.sportxast.SportXast.thirdparty_class.PullToRefreshListView.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
//public class SportX2_Main extends BaseSherlockActivity {
public class SportX2_Main extends Activity { 
	// TODO: Display Events within given latitude and longitude of current
	// device location
	// TODO: Use PullToRefreshListView for ListView

	/**Header components**/
	private HeaderUIClass headerUIClass;
	private RelativeLayout sx_header_wrapper; 
	
	private Global_Data FGlobal_Data;
	//DatabaseHelper DB;
	//private GPSTracker gpsTracker;

	private PullToRefreshListView FPullToRefreshListView;
	View headervw;
	View footervw;
     
	Async_HttpClient async_HttpClient;
	_EventLists eventLists = new _EventLists();
	private EventsAdapter FAdapter;
	
	String FType 	= "";
	String FSinceId = "";
	String FUntilId = "";
 
	boolean isLoad 		= false;
	boolean isRefresh 	= false;
	boolean isPause 	= false;
	  
	private boolean promptAlreadyShown = false;
	 
	/** Coordinates **/
	private Double FCorLatitude;
	private Double FCorLongitude; 
	
	private RelativeLayout pbLoading_container;
	
	/*
	public void gotoVideoCaptureActivity(){ 
		Intent intent = new Intent(SportX2_Main.this, VideoCaptureActivity.class); 
		//intent.putExtra("eventId", FEventId);
		intent.putExtra("callingActivityID", Constants.requestCode_SportX2_Main);
		startActivity(intent);   
	}
	*/
	
	private boolean fromCreateActivity;
	private boolean heyhey = false;
	//private SharedPreferences sharedpreferences; 
	  
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		cancelAlarm(); 
		heyhey = true;
		//handler.removeCallbacks(runnable); 
		super.onDestroy();
	} 
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
 
		if ( !this.IsFromOnCreate ) {
			// TODO refresh once activity is resume
			if (eventLists.eventLists.size() > 0) {
				isRefresh = true;
				FUntilId = "";
				FSinceId = eventLists.eventLists.get(0).eventId;
				gatherEventList();
				//Toast.makeText(getApplicationContext(), "xcv", Toast.LENGTH_LONG).show();
			}
			isPause = false;
			
			this.IsFromOnCreate = false; 
		}
	   
		GlobalVariablesHolder.currentActivityContext = SportX2_Main.this;
		FGlobal_Data.runThreadUploader(SportX2_Main.this); 
		FGlobal_Data.getCurrentLocation();   
	}
	
	private boolean IsFromOnCreate = true;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_event); 
		FGlobal_Data = (Global_Data) getApplicationContext(); 
	    
		//########################################################################## 
		//########################################################################## 
		fromCreateActivity = true;   
		 
		try {
			FType = getIntent().getExtras().getString("type");
			} catch (Exception e) {
				FType = "";
			}   
		 
		this.FCorLatitude	= GlobalVariablesHolder.user_Latitude;
		this.FCorLongitude	= GlobalVariablesHolder.user_Longitude;
		 
		// TODO initialize Actionbar
		//initActionBarObjects(); 
		boolean showBackButton 	= false; 
		boolean showMenuButton 	= false;
		boolean showSearchButton= false;
		String titleText 		= "";
		
		if (FType.length() > 0) {
			showBackButton 	= true; 
			showMenuButton 	= false;
			showSearchButton= false;
			titleText 	   	= FType.substring(0, 1).toUpperCase() + FType.substring(1).toLowerCase(); 
		} else{
			showMenuButton  = true;
			showSearchButton= true;
			titleText 	   	= "Events"; 
		}
		
		prepareHeader( showBackButton, showMenuButton, showSearchButton, titleText ); 
		 
		if( ( (int) GlobalVariablesHolder.user_Latitude  == 0 ) &&
		    ( (int) GlobalVariablesHolder.user_Longitude == 0 ) ){
				CommonFunctions_1.promptToEnableGPS(SportX2_Main.this);
			} 
		else{
			initContentView();    
			gatherEventList();    
			//scheduleAlarm();    
			} 
		
		this.IsFromOnCreate = true; 
	}
	 
	public String convertStreamToString(InputStream is) throws IOException {
      /*
       * To convert the InputStream to String we use the BufferedReader.readLine()
       * method. We iterate until the BufferedReader return null which means
       * there's no more data to read. Each line will appended to a StringBuilder
       * and returned as String.
       */
      if (is != null) {
          StringBuilder sb = new StringBuilder();
          String line;

          try {
              BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
              while ((line = reader.readLine()) != null) {
                  sb.append(line).append("\n");
              }
          } finally {
              is.close();
          }
          return sb.toString();
      } else {        
          return "";
      }
	}	
	
	// #######################################################################################################################	
	   
	
	// #######################################################################################################################	
	 
	private void prepareHeader(boolean showBackButton, boolean showMenuButton, boolean showSearchButton, String titleText){ 
		this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
		this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper); 
		    
		this.headerUIClass.setMenuTitle(  	titleText);  
		this.headerUIClass.showBackButton( 	showBackButton); 
		this.headerUIClass.showAddButton(  	false);  
		this.headerUIClass.showMenuButton(	showMenuButton);  
		this.headerUIClass.showRefreshButton(false);  
		this.headerUIClass.showAboutButton(	false); 
		this.headerUIClass.showSearchButton(showSearchButton); 
		this.headerUIClass.showDoneButton(	false);  
		this.headerUIClass.showCameraButton(false); 
		this.headerUIClass.showMenuTitle(	true );  
		addHeaderButtonListener(); 
	}

	private void addHeaderButtonListener(){
		this.headerUIClass.setOnHeaderButtonClickedListener(new HeaderUIClass.OnHeaderButtonClickedListener() {
			
			@Override
			public void onSearchClicked() {
				// TODO Auto-generated method stub 
				// gatherEventList();
				startActivity(new Intent(SportX2_Main.this, Search_Activity_ATAN.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);      
			}
			
			@Override
			public void onRefreshClicked() {
				// TODO Auto-generated method stub
				startActivity(new Intent(SportX2_Main.this, Menu_Activity.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);      
			}
			
			@Override
			public void onMenuClicked() {
				// TODO Auto-generated method stub
				startActivity( new Intent(SportX2_Main.this, Menu_Activity.class) );
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);      
			}
			/** @category Override onDone- from BaseSherlockActivityclass
			 * custom method from BaseSherlockActivity.Invoked when done button (found
			 * on right side of the action bar) is clicked/pressed. */
			@Override
			public void onDoneClicked() {
				// TODO Auto-generated method stub 
				//saveEventToServer(); 
				//headerUIClass.enableDoneButton(false); 
				//v.setEnabled(false);
			}
			
			@Override
			public void onCameraClicked() {
				// TODO Auto-generated method stub
				
			}
			/** @category Override onBack- from BaseSherlockActivityclass
			 * custom method from BaseSherlockActivity.Invoked when back button (found
			 * on left side of the action bar) is clicked/pressed. */
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
	

	/**@category Override onPause - from Activity class
	 * **/

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		isPause = true;
	}
	/**@category Override onResume - from Activity class
	 * **/

	
	public void scheduleAlarmORIGINAL() {
		return;
	}

	public void scheduleAlarm() {
		// Construct an intent that will execute the AlarmReceiver
		Intent intent = new Intent(getApplicationContext(), Scheduler.class);
		// Create a PendingIntent to be triggered when the alarm goes off
		final PendingIntent pIntent = PendingIntent.getBroadcast(this,
				Scheduler.REQUEST_CODE, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// Setup periodic alarm every 5 seconds
		long firstMillis = System.currentTimeMillis(); // first run of alarm is
														// immediate
		int intervalMillis = 20000; // 5 seconds
		AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
		alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), intervalMillis, pIntent);
	}

	public void cancelAlarm() {
		Intent intent = new Intent(getApplicationContext(), Scheduler.class);
		final PendingIntent pIntent = PendingIntent.getBroadcast(this,
				Scheduler.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pIntent);
	}

	/** @category Override onMenu - from BaseSherlockActivity class
	 * custom method from BaseSherlockActivity.Invoked when menu button (found
	 * on left side of the action bar) is clicked/pressed.
	 * 
	 * */
	/*
	@Override
	public void onMenu(View v) {
		// TODO Auto-generated method stub
		super.onMenu(v);
		startActivity(new Intent(this, Menu_Activity.class));
	}
*/
	/** @category Override onBack - from BaseSherlockActivity class
	 * custom method from BaseSherlockActivity.Invoked when back button (found
	 * on left side of the action bar) is clicked/pressed.
	 * 
	 * */
	/*
	@Override
	public void onBack(View v) {
		// TODO Auto-generated method stub
		super.onBack(v);
		finish();
	}
	 */
	/** @category Override onSearch - from BaseSherlockActivity class
	 * custom method from BaseSherlockActivity.Invoked when search button (found
	 * on right side of the action bar) is clicked/pressed.
	 * 
	 * */
	/*
	@Override
	public void onSearch(View v) {
		// TODO Auto-generated method stub
		super.onSearch(v);

		// gatherEventList();
		startActivity(new Intent(this, Search_Activity.class));
	}
*/
	
	/** @category Custom Method
	 * Custom function for initializing content view and declared objects*/
	public void initContentView() {

		pbLoading_container = (RelativeLayout) findViewById(R.id.pbLoading_container);
		
		async_HttpClient = new Async_HttpClient(this);

		FPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listvw_events);
		headervw = getLayoutInflater().inflate(R.layout.list_header, null);
		footervw = getLayoutInflater().inflate(R.layout.progressbar_small, null);

		if (FType.length() == 0)
			FPullToRefreshListView.addHeaderView(headervw);
		FPullToRefreshListView.addFooterView(footervw);
 
		ArrayList<EventLists> xx =  eventLists.eventLists;
		
		FAdapter = new EventsAdapter(this, eventLists.eventLists);
		FPullToRefreshListView.setAdapter(FAdapter);

		FPullToRefreshListView.setOnScrollListener(onScrollListener);
		FPullToRefreshListView.setOnItemClickListener(onitemListener);
		FPullToRefreshListView.setOnRefreshListener(onRefreshListener); 
	}

	/**@category Custom Method
	 * Called on onCreate method that gets the current location of the device using GPSTracker class.* */
	/*
	public void getCurrentLocation() {
		gpsTracker = new GPSTracker(this);

		if (gpsTracker.canGetLocation()) {

			Coordinate coordinate = new Coordinate();
			coordinate.latitude = gpsTracker.getLatitude();
			coordinate.longitude = gpsTracker.getLongitude();

			Log.e("LATITUDE : ", "" + coordinate.latitude);
			Log.e("LONGITUDE : ", "" + coordinate.longitude);
			FGlobal_Data.setCoordinate(coordinate);
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gpsTracker.showSettingsAlert();
		}

		isLoad = true;
		gatherEventList();
	}
	*/

	/** @category Custom Method
	 * Called after fetching data from api.Set user preference using SharedPreference class * */
	public void setSharedPreference() {
		/*
		ArrayList<ContentValues> arrayList = DB.getAllItem(KEY_PROFILE.TABLE_PROFILE);
		ContentValues item = arrayList.get(0);
		
		
		if (DB.getTableCount(KEY_PROFILE.TABLE_PROFILE) > 0) {
			Editor editor = CommonFunctions_1.sharedPreferences.edit();
			editor.putString(KEY_PROFILE.DEVICE_ID,item.getAsString(KEY_PROFILE.DEVICE_ID));
			editor.putString(KEY_PROFILE.SESSION_ID,item.getAsString(KEY_PROFILE.SESSION_ID));
			editor.putString(KEY_PROFILE.USER_ID,item.getAsString(KEY_PROFILE.USER_ID));
			editor.putString(KEY_PROFILE.USER_NAME,	item.getAsString(KEY_PROFILE.USER_NAME));
			editor.commit();
		}
		
		*/

	}

	/**  @category Custom Method
	 * Called after fetching new data and reload listView adapter to view updated data.* */
	public void reloadListView() {
		FAdapter.notifyDataSetChanged();
		FPullToRefreshListView.invalidateViews();
		FPullToRefreshListView.onRefreshComplete();
	}
 
 /** @category Custom Method
  * Custom method for fetching Events data***/ 
	public void fetchGlobalEvent(){
		RequestParams requestParams = new RequestParams();
		requestParams.put("limit", "0");
		requestParams.put("offset", "");
		
		async_HttpClient.GET("SearchEvents", requestParams, new JsonHttpResponseHandler(){ 
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				
				try {
					JSONArray jsonArray_events= response.getJSONArray("nearByEvents");
					
					if(jsonArray_events.length()>0){
						JSONObject object = jsonArray_events.getJSONObject(0);
						FGlobal_Data.setGlobalEventID(object.getString("eventId"));
					} 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Throwable e,
					JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, e, errorResponse);
			}
			
		});
	}
	
	 
	public void gatherEventList() {
 
		if (eventLists.isFirstLoad || isLoad || isRefresh) {
			eventLists.isFirstLoad = false; 
			
			if(fromCreateActivity){
				pbLoading_container.setVisibility(View.VISIBLE);
				fromCreateActivity = false;
			}
			 
			RequestParams requestParams = new RequestParams(); 
			requestParams.put("pageSize", "20"); 
			requestParams.put("type", 	 FType);
			requestParams.put("sinceId", FSinceId);
			requestParams.put("untilId", FUntilId); 
			
			requestParams.put("latitude",  String.valueOf( FCorLatitude ) );
			requestParams.put("longitude", String.valueOf( FCorLongitude) );
			
			async_HttpClient.GET("ExportEvents", requestParams,
					new JsonHttpResponseHandler() {

						@Override
						public void onStart() { // TODO Auto-generated method stub
							super.onStart(); 
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							// TODO Auto-generated method stub
							super.onSuccess(statusCode, headers, response);
							eventLists.isRefresh = isRefresh;
							eventLists.parseEvents(response);
							//Log.e("events", ""+response.toString());
							
							for (int i = 0; i < headers.length; i++) {
								Log.e("header"," "+ headers[i].toString());
							}
							
							reloadListView();
							
							/*
							if (DB.getTableCount(KEY_PROFILE.TABLE_PROFILE) == 0) {
								// Log.e("DBQuery", "sfsfsdfds");
								ContentValues contentValues = new ContentValues();

								contentValues.put(KEY_PROFILE.DEVICE_ID,	headers[5].getValue());
								contentValues.put(KEY_PROFILE.SESSION_ID, 	headers[6].getValue());
								contentValues.put(KEY_PROFILE.USER_ID, 		headers[7].getValue());
								contentValues.put(KEY_PROFILE.USER_NAME, 	headers[8].getValue()); 
								DB.addItem(KEY_PROFILE.TABLE_PROFILE, contentValues); 
							}
							*/
							
							setSharedPreference();

							isLoad = false;
							isRefresh = false;
							String xheyc = "";
							//################################################### 
							if(promptAlreadyShown == false){
								GlobalVariablesHolder.FLatestEvent = eventLists.getLatestEvent(); 
								 
								if(GlobalVariablesHolder.alreadyCheckedIntoAnEvent){
									
								}else{
									
								  if( eventLists.getLatestEvent() == null ){
									  xheyc  = "wala brad";
								  }else{
									  showCheckIntoDialog(eventLists.getLatestEvent().eventName); 
									  promptAlreadyShown = true;
								  }
								}
								  
							} 
							
							pbLoading_container.setVisibility(View.GONE);	
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							super.onFinish();
							

						}

						@Override
						public void onFailure(Throwable e,
								JSONObject errorResponse) {
							// TODO Auto-generated method stub
							super.onFailure(e, errorResponse);
							
							isLoad = false;
							isRefresh = false;
							FPullToRefreshListView.onRefreshComplete();
						} 
					}); 
		}

	}
	 
	private void showCheckIntoDialog( String eventName ){
		
		DialogSettings dialog = new DialogSettings( SportX2_Main.this, 1, eventName); 
		Dialog.showSettingsDialog(dialog, SportX2_Main.this ); 
		
		/* 
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { 
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (Dialog.getDialogSettingsResult() == 0) { // NO CHANGES,  JUST DISMISS 
				} else if (Dialog.getDialogSettingsResult() == 1) {  
				} else if (Dialog.getDialogSettingsResult() == 11) {
				} else if (Dialog.getDialogSettingsResult() == 2) {
				}
				// TODO Auto-generated method stub
			}
		}); 
		
		*/ 
	}
	 
	
	/**@category Listener 
	 * A scrollListener for listView.  * ***/
	OnScrollListener onScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub 
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub

			final int lastItem = firstVisibleItem + visibleItemCount;
 
			if (eventLists.eventLists.size() > 0) {
				if (lastItem == (totalItemCount - 10) && !isLoad) {
					isLoad = true;
					FSinceId = "";
					FUntilId = eventLists.eventLists.get(eventLists.eventLists.size() - 1).eventId;
					gatherEventList();
				}
			}

			if (isLoad || eventLists.message.length() == 0) {
				footervw.setVisibility(View.VISIBLE);
				if (eventLists.eventLists.size() == 0)
					footervw.setVisibility(View.GONE);
			} else {
				footervw.setVisibility(View.GONE);
			}

		}
	};
	
	/**@category Listener
	 *  A refreshListener for listView.  * ***/
	OnRefreshListener onRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if (eventLists.eventLists.size() > 0) {
				isRefresh = true;
				FUntilId = "";
				FSinceId = eventLists.eventLists.get(0).eventId;
				gatherEventList();
			} 
		}
	};
	
	/**@category Listener
	 *  A itemclicklistener for listView. Invoke when a listcell is click.  * ***/
	OnItemClickListener onitemListener = new OnItemClickListener() { 
		public void onItemClick(android.widget.AdapterView<?> arg0, View arg1,
				int arg2, long arg3) { 
			int at 	  = FType.length() > 0 ? 0 : 1;
			int index = FType.length() > 0 ? 1 : 2;
			
			Intent intent = null; 
			if (arg2 > 1) {  
				
				FGlobal_Data.setEventList(eventLists.eventLists.get(arg2 - index));  
				String eventStatus 		= eventLists.eventLists.get(arg2-index).eventIsOpenString;
			  
				if( eventStatus.equals("CHECK IN") ){  
					intent = new Intent(SportX2_Main.this, Highlight_Activity.class);   
					intent.putExtra("eventParcel", CommonFunctions_1.parseToEventParcel(  eventLists.eventLists.get(arg2-index) ) ); 
				}else{ 
					intent = new Intent(SportX2_Main.this, EventDetail_Activity.class);  
					intent.putExtra("eventParcel", CommonFunctions_1.parseToEventParcel(  eventLists.eventLists.get(arg2-index) ) ); 
				} 
			} else {
				 intent = new Intent(SportX2_Main.this, Create_Activity.class);
				    
			}
			 
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);  
		};
	};
	
	/*
	//ORIGINAL
	OnItemClickListener onitemListener = new OnItemClickListener() {

		public void onItemClick(android.widget.AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {

			int at = type.length() > 0 ? 0 : 1;
			int index = type.length() > 0 ? 1 : 2;

			if (arg2 > 1) {
				
				Toast.makeText(SportX2_Main.this, eventLists.eventLists.get(arg2-index).eventId, Toast.LENGTH_LONG).show();
				
				FGlobal_Data.setEventList(eventLists.eventLists.get(arg2 - index));
				Intent intent = new Intent(SportX2_Main.this, EventDetail_Activity.class);
				startActivity(intent);
				  
			} else {
				Intent intent = new Intent(SportX2_Main.this,
						Create_Activity.class);
				startActivity(intent);
			}

		};
	};
	*/
	
	

}

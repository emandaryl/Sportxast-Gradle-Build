package com.sportxast.SportXast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.localdatabase.Keys.KEY_HEADER;
import com.sportxast.SportXast.localdatabase.Keys.KEY_PROFILE;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
//import com.actionbarsherlock.view.Window;

public class Splash_Activity extends Activity { 
	private Global_Data FGlobal_Data;
	//private DatabaseHelper DB; 
	private ImageView imageView; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    // TODO Auto-generated method stub 
	    
	    FGlobal_Data = (Global_Data)getApplicationContext();
	    //DB = new DatabaseHelper(this); 
	    
	 //   CommonFunctions_1.initSharedPreferences(this); 
	    imageView = new ImageView(this);
	    imageView.setId(R.id.imgview_splash);
	    imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    imageView.setAdjustViewBounds(true);
	    imageView.setScaleType(ScaleType.CENTER_CROP);
	    imageView.setImageResource(R.drawable.ic_splash_image);
	    
	    FGlobal_Data.turnGPSOn(); 
	    setContentView(imageView); 
	    resetGlobalStaticVariable(); 
	    getAppSettings();
	     
	    new CityAsyncTask(Splash_Activity.this, GlobalVariablesHolder.user_Latitude, GlobalVariablesHolder.user_Longitude).execute();  
	}

	private class CityAsyncTask extends AsyncTask<String, String, String> {
	    private Activity act;
	    private double latitude;
	    private double longitude;
	    
	    public CityAsyncTask(Activity act, double latitude, double longitude) {
	        // TODO Auto-generated constructor stub
	        this.act = act;
	        this.latitude = latitude;
	        this.longitude = longitude;
	    } 
	    
	    @Override
	    protected String doInBackground(String... params) {
	        String result = "";
	        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
	        try {
	            List<Address> addresses = geocoder.getFromLocation(this.latitude, this.longitude, 1);
	            Log.e("Addresses", "-->" + addresses);
	            result = addresses.get(0).toString(); 
	            
	            GlobalVariablesHolder.X_USERCOUNTRY  = addresses.get(0).getCountryName();
	            GlobalVariablesHolder.X_USERLOCALITY = addresses.get(0).getLocality();
	        	 
	            String x = result;
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	             
	            GlobalVariablesHolder.X_USERCOUNTRY  = "";
	            GlobalVariablesHolder.X_USERLOCALITY = "";

	        }
	        return result;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        // TODO Auto-generated method stub
	        super.onPostExecute(result); 
	    }
	}
	
	private void resetGlobalStaticVariable(){
		  
		GlobalVariablesHolder.X_DEVICE_ID  = Secure.getString(Splash_Activity.this.getContentResolver(), Secure.ANDROID_ID); 
		GlobalVariablesHolder.FLatestEvent = null;   
		GlobalVariablesHolder.alreadyCheckedIntoAnEvent = false;  
		GlobalVariablesHolder.firstTimeUseOfVideoCapture = false;
		
		GlobalVariablesHolder.threadUploaderIsRunning = false; 	 
		/** default value SHOULD be 0 **/
		//GlobalVariablesHolder.user_Latitude = 10.3199438; 
		/** default value SHOULD be 0 **/
		//GlobalVariablesHolder.user_Longitude= 123.9053596; 
		
		FGlobal_Data.getCurrentLocation();    
		
		if( ( (int) GlobalVariablesHolder.user_Latitude == 0 ) &&
				( (int) GlobalVariablesHolder.user_Longitude == 0 ) ){
			
				Toast.makeText( getApplicationContext(), "WRONG WRONG LOCATION", Toast.LENGTH_LONG ).show();   
				/** default value SHOULD be 0 **/
				 GlobalVariablesHolder.user_Latitude = 10.3199438; 
				/** default value SHOULD be 0 **/
				 GlobalVariablesHolder.user_Longitude= 123.9053596;  
			}  
		/* 
		GlobalVariablesHolder.user_Latitude = 0;
		GlobalVariablesHolder.user_Longitude = 0;
		*/
	}
	        
	
	private void supplyDeviceUserData( Header[] headers ){
		// Log.e("DBQuery", "sfsfsdfds"); 
		
		SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences( "com.sportxast.SportXast", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		
		//Editor editor = CommonFunctions_1.sharedPreferences.edit();
		for (Header header : headers) {
			
			/* 
			[Content-Encoding: gzip, 
			Content-Type: application/json,
			 Date: Tue, 01 Jul 2014 08:25:27 GMT, 
			 Server: nginx, 
			 Vary: Accept-Encoding, 
			 X-DEVICE-ID: 30fafafb52bb6f66f7f932ecc19d4fe7, X-SESSION-ID: 7988753b27077ab847, X-USER-ID: 79887, X-USER-NAME: 79887, transfer-encoding: chunked, Connection: keep-alive]
			*/ 
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_DEVICE_ID)){
				String hey = header.getValue(); 
				GlobalVariablesHolder.X_DEVICE_ID	= header.getValue(); 
				editor.putString(KEY_PROFILE.DEVICE_ID, header.getValue());
			}    
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_USER_ID)){
				String hey = header.getValue();
				
				GlobalVariablesHolder.X_USER_ID	= header.getValue(); 
				editor.putString(KEY_PROFILE.USER_ID,header.getValue());
			}
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_USER_NAME)){
				String hey = header.getValue();
				
				GlobalVariablesHolder.X_USER_NAME	= header.getValue(); 
				editor.putString(KEY_PROFILE.USER_NAME,header.getValue());
			}
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_SESSION_ID)){
				String hey = header.getValue();
				
				GlobalVariablesHolder.X_SESSION_ID	= header.getValue();  
				editor.putString(KEY_PROFILE.SESSION_ID,header.getValue());
			}
			
		}
		    
	  editor.commit();
	  
	}
	      
	private void supplyDeviceUserDataORIGINAL( Header[] headers ){
		
		SharedPreferences sharedpreferences = getSharedPreferences("com.sportxast.SportXast", Context.MODE_PRIVATE); 
		if( checkAppFirstTimeUse() == false){
			  
			GlobalVariablesHolder.X_DEVICE_ID	= sharedpreferences.getString(KEY_PROFILE.DEVICE_ID, ""); 
			GlobalVariablesHolder.X_SESSION_ID 	= sharedpreferences.getString(KEY_PROFILE.SESSION_ID, ""); 
			GlobalVariablesHolder.X_USER_ID  	= sharedpreferences.getString(KEY_PROFILE.USER_ID, ""); 
			GlobalVariablesHolder.X_USER_NAME 	= sharedpreferences.getString(KEY_PROFILE.USER_NAME, "");  
			return;
		}
		 
		//SharedPreferences sharedpreferences = getSharedPreferences("com.sportxast.SportXast", Context.MODE_PRIVATE); 
		//Editor editor = CommonFunctions_1.sharedPreferences.edit();
		Editor editor = sharedpreferences.edit();
		for (Header header : headers) { 
			/* 
			GlobalVariablesHolder.X_DEVICE_ID  	= "";
			GlobalVariablesHolder.X_SESSION_ID 	= ""; 
			GlobalVariablesHolder.X_USER_ID 		= ""; 
			GlobalVariablesHolder.X_USER_NAME 	= ""; 
			*/
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_DEVICE_ID)){
				String hey = header.getValue(); 
				GlobalVariablesHolder.X_DEVICE_ID = header.getValue();
				editor.putString(KEY_PROFILE.DEVICE_ID, header.getValue());
			}    
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_SESSION_ID)){
				String hey = header.getValue(); 
				GlobalVariablesHolder.X_SESSION_ID = header.getValue();
				editor.putString(KEY_PROFILE.SESSION_ID, header.getValue());
			}
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_USER_ID)){
				String hey = header.getValue();
				GlobalVariablesHolder.X_USER_ID  = header.getValue();
				editor.putString(KEY_PROFILE.USER_ID, header.getValue());
			}
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_USER_NAME)){
				String hey = header.getValue(); 
				GlobalVariablesHolder.X_USER_NAME = header.getValue();
				editor.putString(KEY_PROFILE.USER_NAME, header.getValue());
			} 
		} 
		editor.commit();
	}
	    
	/**  @category Custom Method
	 * Called on onCreate method.Fetch application settings data( if success - goto parseSetting()) * */
	public void getAppSettings() {  
		//GlobalVariablesHolder.APISubDomain = "dev"; //<-- subdomain/change this
		GlobalVariablesHolder.APISubDomain = "test"; //<-- subdomain/change this
		 
		Async_HttpClient async_HttpClient2 = new Async_HttpClient(this); 
		async_HttpClient2.GET("Settings", new RequestParams(),
				new JsonHttpResponseHandler() {
			
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub [Content-Encoding: gzip, Content-Type: application/json, Date: Wed, 02 Jul 2014 03:24:27 GMT, Server: nginx, Vary: Accept-Encoding, X-DEVICE-ID: 5234e3ee1971395e58e4283e77d094b4, X-SESSION-ID: 8048653b37b6b7501c, X-USER-ID: 80486, X-USER-NAME: 80486, transfer-encoding: chunked, Connection: keep-alive]
						super.onSuccess(statusCode, headers, response);
						for (int i = 0; i < headers.length; i++) {
							Log.e("header"," "+ headers[i].toString());
						}
						  
						parseSetting(response);  
						//supplyDeviceUserData( headers );  
					    if( checkAppFirstInstall() ){ 
					    	supplyDeviceUserData( headers );
					  /*
							if (DB.getTableCount(KEY_PROFILE.TABLE_PROFILE) == 0) {
								// Log.e("DBQuery", "sfsfsdfds");
								ContentValues contentValues = new ContentValues();
								Editor editor = CommonFunctions_1.sharedPreferences.edit();
								for (Header header : headers) {
								  
//									[Content-Encoding: gzip, 
//									Content-Type: application/json,
//									 Date: Tue, 01 Jul 2014 08:25:27 GMT, 
//									 Server: nginx, 
//									 Vary: Accept-Encoding, 
//									 X-DEVICE-ID: 30fafafb52bb6f66f7f932ecc19d4fe7, X-SESSION-ID: 7988753b27077ab847, X-USER-ID: 79887, X-USER-NAME: 79887, transfer-encoding: chunked, Connection: keep-alive]
//								 
									String headerName = header.getName(); 
									if(header.getName().equalsIgnoreCase(KEY_HEADER.X_DEVICE_ID)){
										String hey = header.getValue();
										
										GlobalVariablesHolder.X_DEVICE_ID	= header.getValue();
										contentValues.put(KEY_PROFILE.DEVICE_ID,header.getValue());
										editor.putString(KEY_PROFILE.DEVICE_ID, header.getValue());
									}    
									if(header.getName().equalsIgnoreCase(KEY_HEADER.X_USER_ID)){
										String hey = header.getValue();
										
										GlobalVariablesHolder.X_USER_ID	= header.getValue();
										
										contentValues.put(KEY_PROFILE.USER_ID,header.getValue());
										editor.putString(KEY_PROFILE.USER_ID,header.getValue());
									}
									if(header.getName().equalsIgnoreCase(KEY_HEADER.X_USER_NAME)){
										String hey = header.getValue();
										
										GlobalVariablesHolder.X_USER_NAME	= header.getValue();
										
										contentValues.put(KEY_PROFILE.USER_NAME,header.getValue());
										editor.putString(KEY_PROFILE.USER_NAME,header.getValue());
									}
									if(header.getName().equalsIgnoreCase(KEY_HEADER.X_SESSION_ID)){
										String hey = header.getValue();
										
										GlobalVariablesHolder.X_SESSION_ID	= header.getValue();
										
										contentValues.put(KEY_PROFILE.SESSION_ID,header.getValue());
										editor.putString(KEY_PROFILE.SESSION_ID,header.getValue());
									} 
								} 
								DB.addItem(KEY_PROFILE.TABLE_PROFILE, contentValues); 
							  	editor.commit(); 
							}
						 */ 
					    }
					    else{
					    	SharedPreferences sharedpreferences = getSharedPreferences("com.sportxast.SportXast", Context.MODE_PRIVATE); 
					    	GlobalVariablesHolder.X_DEVICE_ID	= sharedpreferences.getString(KEY_PROFILE.DEVICE_ID, ""); 
							GlobalVariablesHolder.X_SESSION_ID 	= sharedpreferences.getString(KEY_PROFILE.SESSION_ID, ""); 
							GlobalVariablesHolder.X_USER_ID  	= sharedpreferences.getString(KEY_PROFILE.USER_ID, ""); 
							GlobalVariablesHolder.X_USER_NAME 	= sharedpreferences.getString(KEY_PROFILE.USER_NAME, "");    
					    }
						Log.v("appSetting", response.toString());
						
						//######################################################################### 
						//######################################################################### 
						final String PREFS_NAME = "MyPrefsFile";
						 
						SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
						if (settings.getBoolean("my_first_install", true)) {  
						    //the app is being launched for first time, do something        
						    Log.d("Comments", "First time"); 
						    // first time task 
						    // record the fact that the app has been started at least once
						    settings.edit().putBoolean("my_first_install", false).commit();  
						} 
						//#########################################################################
						
						goToTutorial();
					}
					 
					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(e, errorResponse);
						goToTutorial();
						
					}
				});
	}
	   
	/** @category Custom Method
	 * Called on getAppSettings() method. Reloads listView adapter to  view updated data.
	 * @param (String )response -  the string result.
	 * * */ 
	public void parseSetting(JSONObject response) {
		
		JSONObject jsonObject_languageLabels = null;
		try {
			jsonObject_languageLabels = response.getJSONObject("languageLabels");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject jsonObject_settings = null;
		try {
			jsonObject_settings = response.getJSONObject("settings");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		FGlobal_Data.setAppSettings_languageLabels( jsonObject_languageLabels );
		FGlobal_Data.setAppSettings_settings( jsonObject_settings );
		   
		GlobalVariablesHolder.S3_VIDEO_BUCKET = FGlobal_Data.getAppSetting_settings("S3_VIDEO_BUCKET");
		GlobalVariablesHolder.S3_IMAGE_BUCKET = FGlobal_Data.getAppSetting_settings("S3_IMAGE_BUCKET");
		
		/*
		_AppSetting appSetting = new _AppSetting();
		appSetting.parseAppSetting(this, response);
		FGlobal_Data.setAppSetting(appSetting);  
		*/
		
//		JSONObject jObject_response; 
//		try {
//			jObject_response = new JSONObject(response);
//			appSetting.language = jObject_response.getString("language");
//			JSONObject jObject_settings = jObject_response
//					.getJSONObject("settings");
//			appSetting.settings._APP_SHARE_TEXT = jObject_settings
//					.getString("APP_SHARE_TEXT");
//			appSetting.settings._APP_SHARE_URL = jObject_settings
//					.getString("APP_SHARE_URL");
//			appSetting.settings._APP_SHARE_MAIL_SUBJECT = jObject_settings
//					.getString("APP_SHARE_MAIL_SUBJECT");
//			appSetting.settings._MEDIA_SHARE_MAIL_SUBJECT = jObject_settings
//					.getString("MEDIA_SHARE_MAIL_SUBJECT");
//			
//			appSetting.settings._S3_IMAGE_BUCKET = jObject_settings.getString("S3_IMAGE_BUCKET");
//			// appSetting.appSetting.WEBSITE_BASE_URL =
//			// jObject_settings.getString("WEBSITE_BASE_URL");
//
//			// AQuery aq = new AQuery(this);
//			// aq.cache(jObject_response.getString("WEBSITE_BASE_URL"), 0);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		} 
		 
	}
	/** @category Custom Method
	 * Called after fetching data from api.Set user preference using SharedPreference class * */
	
	public void goToSportX_Main(){
		finish();
		startActivity(new Intent(this, SportX2_Main.class));
	}
	
	public void goToTutorial(){
		finish();
		//Toast.makeText(getApplicationContext(), "BOOOOO", Toast.LENGTH_LONG).show();
		//startActivity(new Intent(Splash_Activity.this, Tutorial_Activity.class));
		 
		if(checkAppFirstTimeUse()){
			startActivity(new Intent(Splash_Activity.this, Tutorial_Activity.class));
		}else
		{
			startActivity(new Intent(Splash_Activity.this, SportX2_Main.class));
		}  
	}
	
	

	/** //check if android app is the first ever installation **/
	private  boolean checkAppFirstInstall(){ 
		boolean firstTimeEver = true;
		final String PREFS_NAME = "MyPrefsFile";
 
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		if (settings.getBoolean("my_first_install", true)) {
			/*
		 
		    //the app is being launched for first time, do something        
		    Log.d("Comments", "First time"); 
		             // first time task

		    // record the fact that the app has been started at least once
		    settings.edit().putBoolean("my_first_time", false).commit();  
		    */ 
		    
		    firstTimeEver = true;
		}else{
			firstTimeEver = false;
		}
		
		return firstTimeEver;
	}
	

	/** //check if android app is the first time used **/
	private  boolean checkAppFirstTimeUse(){ 
		boolean firstTimeEver = true;
		final String PREFS_NAME = "MyPrefsFile"; 
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		if (settings.getBoolean("my_first_time", true)) {
			/*
		 
		    //the app is being launched for first time, do something        
		    Log.d("Comments", "First time"); 
		             // first time task

		    // record the fact that the app has been started at least once
		    settings.edit().putBoolean("my_first_time", false).commit();  
		    */ 
		    
		    firstTimeEver = true;
		}else{
			firstTimeEver = false;
		}
		
		return firstTimeEver;
	}
	
	
}

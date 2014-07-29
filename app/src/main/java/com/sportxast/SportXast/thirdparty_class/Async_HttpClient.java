package com.sportxast.SportXast.thirdparty_class;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data.Coordinate;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.localdatabase.DatabaseHelper;
import com.sportxast.SportXast.localdatabase.Keys.KEY_PROFILE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Async_HttpClient {

	Context context;
	SharedPreferences sharedPreferences;

	private static final int  TIME_OUT = 30 *1000; 
 
	private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	
	public Async_HttpClient(Context context) {
		// TODO Auto-generated constructor stub
		this.context =context;
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setTimeout(TIME_OUT);
		setHeaders();
	}

	public void POST(String extendedUrl,RequestParams requestParams,AsyncHttpResponseHandler asyncHttpResponseHandler){

		asyncHttpClient.post(Constants.BASE_URL+extendedUrl, requestParams, asyncHttpResponseHandler);
		}
	
	public void GET(String extendedUrl,RequestParams requestParams,AsyncHttpResponseHandler asyncHttpResponseHandler){

		asyncHttpClient.get(Constants.BASE_URL+extendedUrl, requestParams, asyncHttpResponseHandler);
		}
	  
	public void setHeaders(){
		
		sharedPreferences = context.getSharedPreferences( "com.sportxast.SportXast", Context.MODE_PRIVATE);
		final TelephonyManager tm =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
		asyncHttpClient.addHeader("USERID", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
		asyncHttpClient.addHeader("USERSESSION", sharedPreferences.getString(KEY_PROFILE.SESSION_ID, ""));
		asyncHttpClient.addHeader("PHONELANGUAGE", Locale.getDefault().getLanguage());
		
		String timezone_ = TimeZone.getDefault().getID();
		asyncHttpClient.addHeader("TIMEZONE", ""+TimeZone.getDefault().getID());
		 
		Log.e("headers", ""+ Secure.getString(context.getContentResolver(), Secure.ANDROID_ID) +"\n"
							+sharedPreferences.getString(KEY_PROFILE.SESSION_ID, "")+"\n"
							+Locale.getDefault().getLanguage()+"\n"
							+TimeZone.getDefault().getID()); 
	}
	
	
	public String getAddress(  Coordinate coordinate ) {
		
		double latitude = coordinate.latitude;
		double longitude= coordinate.longitude;
		
	    Geocoder geocoder = new Geocoder(this.context, Locale.getDefault());
	    
	    Address obj = null;
	    try {
	        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
	        obj = addresses.get(0);
	        String add = obj.getAddressLine(0);
	        
	        /*
	        GUIStatics.currentAddress = obj.getSubAdminArea() + "," + obj.getAdminArea();
	        GUIStatics.latitude = obj.getLatitude();
	        GUIStatics.longitude = obj.getLongitude();
	        GUIStatics.currentCity= obj.getSubAdminArea();
	        GUIStatics.currentState= obj.getAdminArea();
	        */
	         
	        
	        add = add + "\n" + obj.getCountryName();
	        add = add + "\n" + obj.getCountryCode();
	        add = add + "\n" + obj.getAdminArea();
	        add = add + "\n" + obj.getPostalCode();
	        add = add + "\n" + obj.getSubAdminArea();
	        add = add + "\n" + obj.getLocality();
	        add = add + "\n" + obj.getSubThoroughfare();
 
	        
	        
	        Log.v("IGA", "Address" + add);
	        // Toast.makeText(this, "Address=>" + add,
	        // Toast.LENGTH_SHORT).show();

	        // TennisAppActivity.showDialog(add);
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	       // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
	    } 
	    return obj.getCountryName();
	}
	
	public Coordinate getCurrentLocation() {
		GPSTracker gpsTracker = new GPSTracker(this.context);
		
		Coordinate coordinate = new Coordinate();
		
		if (gpsTracker.canGetLocation()) {

			
			coordinate.latitude = gpsTracker.getLatitude();
			coordinate.longitude = gpsTracker.getLongitude();

			Log.e("LATITUDE : ", "" + coordinate.latitude);
			Log.e("LONGITUDE : ", "" + coordinate.longitude);
			//FGlobal_Data.setCoordinate(coordinate);
			
		} else {
			coordinate.latitude = 0;
			coordinate.longitude = 0;
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			
			gpsTracker.showSettingsAlert();
		}
		
		return coordinate; 
	}
	
	public void setHeadersXXX(){
		DatabaseHelper  DB  = new DatabaseHelper(context);
		sharedPreferences =context.getSharedPreferences( "com.sportxast.SportXast", Context.MODE_PRIVATE);
		ArrayList<ContentValues> arrayList = DB.getAllItem(KEY_PROFILE.TABLE_PROFILE);
		
		if(arrayList.size()>0){
		
		ContentValues item = arrayList.get(0);
		if(DB.getTableCount(KEY_PROFILE.TABLE_PROFILE)>0){
			Editor editor = sharedPreferences.edit();
			editor.putString(KEY_PROFILE.DEVICE_ID, item.getAsString(KEY_PROFILE.DEVICE_ID));
			editor.putString(KEY_PROFILE.SESSION_ID, item.getAsString(KEY_PROFILE.SESSION_ID));
			editor.putString(KEY_PROFILE.USER_ID,  item.getAsString(KEY_PROFILE.USER_ID));
			editor.putString(KEY_PROFILE.USER_NAME,  item.getAsString(KEY_PROFILE.USER_NAME));
			editor.commit();
		}
		
		if (//sharedPreferences.getString(KEY_PROFILE.DEVICE_ID, "").length() > 0&& 
				sharedPreferences.getString(KEY_PROFILE.USER_ID, "").length() > 0
				//&& sharedPreferences.getString(KEY_PROFILE.USER_NAME, "").length() > 0
				) {
			
			//Toast.makeText(context, "hasHeaders", Toast.LENGTH_SHORT).show();
//			Toast.makeText(context, "user id : "+
//						//sharedPreferences.getString(KEY_PROFILE.DEVICE_ID, "")+"\n"+ 
//						sharedPreferences.getString(KEY_PROFILE.USER_ID, "")+"\n"
//						//sharedPreferences.getString(KEY_PROFILE.USER_NAME, "")+"\n", 
//						,Toast.LENGTH_SHORT).show();

			
		//	asyncHttpClient.addHeader("X-DEVICE-ID",sharedPreferences.getString(KEY_PROFILE.DEVICE_ID, ""));
			asyncHttpClient.addHeader("USERID", sharedPreferences.getString(KEY_PROFILE.USER_ID, ""));
		//	asyncHttpClient.addHeader("X-USER-NAME",sharedPreferences.getString(KEY_PROFILE.USER_NAME, ""));
		}
		}
		
//		Header[] headers = new Header[]{
//		new BasicHeader("X-DEVICE-ID",sharedPreferences.getString(KEY_PROFILE.DEVICE_ID, "")),
//		new BasicHeader("X-USER-ID", sharedPreferences.getString(KEY_PROFILE.USER_ID, "")),
//		new BasicHeader("X-USER-NAME",sharedPreferences.getString(KEY_PROFILE.USER_NAME, ""))
//};
	}
}

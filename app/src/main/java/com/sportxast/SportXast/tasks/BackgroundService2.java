package com.sportxast.SportXast.tasks;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

import com.sportxast.SportXast.Global_Data;
public class BackgroundService2 extends IntentService{

	  private int result = Activity.RESULT_CANCELED;
	  public static final String URL = "urlpath";
	  public static final String FILENAME = "filename";
	  public static final String FILEPATH = "filepath";
	  public static final String RESULT = "result";
	  public static final String NOTIFICATION = "com.vogella.android.service.receiver";
 
	  private Global_Data FGlobal_Data;
	  
	  public BackgroundService2() {
	    super("BackgroundService");
	  }
	  
	  @Override
	  public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	  }
	  
	  // will be called asynchronously by Android
	  @Override
	  protected void onHandleIntent(Intent intent) {
		   
		  
	  } 
	  
	  private void publishResults(String outputPath, int result) {
		  /*
	    Intent intent = new Intent(NOTIFICATION);
	    intent.putExtra(FILEPATH, outputPath);
	    intent.putExtra(RESULT, result);
	    sendBroadcast(intent);
	    */
	  } 
	} 
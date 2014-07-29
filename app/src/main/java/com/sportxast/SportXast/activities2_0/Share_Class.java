package com.sportxast.SportXast.activities2_0;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;

import com.androidquery.AQuery;
import com.sportxast.SportXast.Global_Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import com.sportxast.SportXast.BaseSherlockActivity;

public class Share_Class {

	//Global_Data global_Data;
	private Global_Data FGlobal_Data;
	public Share_Class(Context context) {
		// TODO Auto-generated constructor stub
	
		FGlobal_Data = (Global_Data)context.getApplicationContext();
		//global_Data.setTabGroupActivity5(new TabGroupActivity(this)); 
	
		AQuery aq = new AQuery(context);
		//String url = global_Data.getAppSetting().settings._WEBSITE_BASE_URL;
		String url = FGlobal_Data.getAppSetting_settings("WEBSITE_BASE_URL");
        File file = aq.getCachedFile(url);

       // Intent intent = new Intent(Intent.ACTION_SEND);
         
//    //	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, global_Data.getAppSetting().appSetting.APP_SHARE_MAIL_SUBJECT);
//    	intent.putExtra(Intent.EXTRA_TEXT,global_Data.getAppSetting().appSetting.APP_SHARE_TEXT);
//    	intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//    	intent.setType("text/plain");
//		startActivity(Intent.createChooser(intent, "Share via:"));
		 
		Resources resources = context.getResources();

	    Intent emailIntent = new Intent();
	    emailIntent.setAction(Intent.ACTION_SEND);
	    // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
	    //emailIntent.putExtra(Intent.EXTRA_TEXT, global_Data.getAppSetting().settings._APP_SHARE_TEXT);
	    //emailIntent.putExtra(Intent.EXTRA_SUBJECT, global_Data.getAppSetting().settings._APP_SHARE_MAIL_SUBJECT);
	    emailIntent.putExtra(Intent.EXTRA_TEXT,  	FGlobal_Data.getAppSetting_settings("APP_SHARE_TEXT"));
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT,  FGlobal_Data.getAppSetting_settings("APP_SHARE_MAIL_SUBJECT"));
	     
	    //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	    emailIntent.setType("message/rfc822");

	    PackageManager pm =context.getPackageManager();
	    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
	    sendIntent.setType("text/plain");
 
	    Intent openInChooser = Intent.createChooser(emailIntent,"Share via:");

	    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
	    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
	    for (int i = 0; i < resInfo.size(); i++) {
	        // Extract the label, append it, and repackage it in a LabeledIntent
	        ResolveInfo ri = resInfo.get(i);
	        String packageName = ri.activityInfo.packageName;
	        if(packageName.contains("android.email")) {
	            emailIntent.setPackage(packageName);
	        } else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
	            Intent intent = new Intent();
	            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
	            intent.setAction(Intent.ACTION_SEND);
	            //intent.putExtra(Intent.EXTRA_TEXT, global_Data.getAppSetting().settings._APP_SHARE_TEXT);
	           // intent.putExtra(Intent.EXTRA_SUBJECT, global_Data.getAppSetting().settings._APP_SHARE_MAIL_SUBJECT);
	           
	            intent.putExtra(Intent.EXTRA_TEXT, 		FGlobal_Data.getAppSetting_settings("APP_SHARE_TEXT"));
	            intent.putExtra(Intent.EXTRA_SUBJECT, 	FGlobal_Data.getAppSetting_settings("APP_SHARE_MAIL_SUBJECT"));
	            
	            
	           // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	            intent.setType("text/plain");
	            if(packageName.contains("twitter")) {
	               
	            } else if(packageName.contains("facebook")) {
	                // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
	                // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
	                // will show the <meta content ="..."> text from that page with our link in Facebook.
	              
	            } else if(packageName.contains("mms")) {
	               
	            } else if(packageName.contains("android.gm")) {
	                         
	                intent.setType("message/rfc822");
	            }

	            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
	        }
	    }

	    // convert intentList to array
	    LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

	    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
	    ((Activity)context).startActivity(openInChooser);  
	
	
	}
}

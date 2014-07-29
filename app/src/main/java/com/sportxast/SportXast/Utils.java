package com.sportxast.SportXast;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.localdatabase.Keys;
import com.sportxast.SportXast.models._MediaQueue;
import com.sportxast.SportXast.models._MediaStorage;
import com.sportxast.SportXast.video.capture.CaptureListener;
import com.sportxast.SportXast.video.capture.TrimVideoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Utils {
	public static int getScreenOrientation(Activity act) {
	    int rotation = act.getWindowManager().getDefaultDisplay().getRotation();
	    DisplayMetrics dm = new DisplayMetrics();
	    act.getWindowManager().getDefaultDisplay().getMetrics(dm);
	    int width = dm.widthPixels;
	    int height = dm.heightPixels;
	    int orientation;
	    // if the device's natural orientation is portrait:
	    if ((rotation == Surface.ROTATION_0
	            || rotation == Surface.ROTATION_180) && height > width ||
	        (rotation == Surface.ROTATION_90
	            || rotation == Surface.ROTATION_270) && width > height) {
	        switch(rotation) {
	            case Surface.ROTATION_0:
	                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	                break;
	            case Surface.ROTATION_90:
	                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	                break;
	            case Surface.ROTATION_180:
	                orientation =
	                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
	                break;
	            case Surface.ROTATION_270:
	                orientation =
	                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
	                break;
	            default:
	                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	                break;              
	        }
	    }
	    // if the device's natural orientation is landscape or if the device
	    // is square:
	    else {
	        switch(rotation) {
	            case Surface.ROTATION_0:
	                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	                break;
	            case Surface.ROTATION_90:
	                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	                break;
	            case Surface.ROTATION_180:
	                orientation =
	                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
	                break;
	            case Surface.ROTATION_270:
	                orientation =
	                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
	                break;
	            default:
	                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	                break;              
	        }
	    }

	    return orientation;
	}
	public static void addToFavorites(Context context, final _MediaStorage capture, final CaptureListener vcListner) {
		Log.e("UTILS", "mediaId=" + capture.ID);
//		final ContentValues mediaLists = DB.getItem(KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE, "ID", mediaId);
		capture.isFavorite = "1";
		vcListner.onFavoriteSuccess(capture);
	}
	
	public static void shareMedia(Context context, _MediaStorage capture, CaptureListener vcListner) {
        File file = new File(TrimVideoUtils.getLocalImagesPath(context) + "/" + capture.imageLocalFilename);
		Resources resources = context.getResources();
	    Intent emailIntent = new Intent();
	    emailIntent.setAction(Intent.ACTION_SEND);
	    Global_Data global_Data = (Global_Data) context.getApplicationContext();
	    // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
	    emailIntent.putExtra(Intent.EXTRA_TEXT, capture.shareText);
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT, global_Data.appSetting.settings._MEDIA_SHARE_MAIL_SUBJECT);
	   // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	    emailIntent.setType("message/rfc822");
	    PackageManager pm = context.getPackageManager();
	    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
	    sendIntent.setType("text/plain");

	    Intent openInChooser = Intent.createChooser(emailIntent,"Share via:");
	    
	    int resultFromShare = 0;
	    
	    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
	    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();      
	    for (int i = 0; i < resInfo.size(); i++) {
	        // Extract the label, append it, and repackage it in a LabeledIntent
	        ResolveInfo ri = resInfo.get(i);
	        String packageName = ri.activityInfo.packageName;
	        if(packageName.contains("android.email")) {
	            emailIntent.setPackage(packageName);
	            resultFromShare = Keys.share.RESULT_FROM_EMAIL;
	        } else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
	            Intent intent = new Intent();
	            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
	            intent.setAction(Intent.ACTION_SEND);
	            intent.putExtra(Intent.EXTRA_TEXT, capture.shareText);
	            intent.putExtra(Intent.EXTRA_SUBJECT, global_Data.appSetting.settings._MEDIA_SHARE_MAIL_SUBJECT);
	            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	            intent.setType("text/plain");
	            if(packageName.contains("twitter")) {
	            	  intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			          intent.setType("image/png");
			          resultFromShare = Keys.share.RESULT_FROM_TWITTER;
	            } else if(packageName.contains("facebook")) {
	                // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
	                // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
	                // will show the <meta content ="..."> text from that page with our link in Facebook.
	            	 intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			            intent.setType("image/png");
			            resultFromShare = Keys.share.RESULT_FROM_FACEBOOK;
	            } else if(packageName.contains("mms")) {
	            	resultFromShare = Keys.share.RESULT_FROM_SMS;
	            } else if(packageName.contains("android.gm")) {
	            	resultFromShare = Keys.share.RESULT_FROM_EMAIL;
	                intent.setType("message/rfc822");
	            }

	            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
	        }
	    }

	    // convert intentList to array
	    LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

	    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
	    
		((Activity)context).startActivityForResult(openInChooser, resultFromShare); 		
	}
	
	public static void deleteCapture(final Context context, final _MediaStorage capture, final CaptureListener vcListner) {
		final Dialog dialog;
		dialog = new Dialog(context);
//		 if(Integer.parseInt(mediaLists.mediaLists.get(section).currentUserIsOwner)==0){
//			 dialog.setTitle("Report");
//		 }else{
//			 dialog.setTitle("Delete Highlight?");
//		 }
		dialog.setTitle("Delete Highlight?");
		LinearLayout layout = new LinearLayout(context);
		Button btn_delete = new Button(context);
		Button btn_cancel = new Button(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		layout.setPadding(4, 4, 4, 4);
		layout.setOrientation(LinearLayout.VERTICAL);
		btn_delete.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		btn_cancel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		btn_delete.setBackgroundResource(R.drawable.layerlist_whiteshapewithlinebelow);
		btn_cancel.setBackgroundResource(R.color.white);
		btn_delete.setTextColor(Color.RED);
		btn_cancel.setTextColor(Color.BLUE);
		btn_delete.setText("Delete");
		btn_cancel.setText("Cancel");
		btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				capture.isDeletedByUser = "1";
				vcListner.onDeleteCapture(capture);
				dialog.dismiss();
				// delete local files 
				try {
					Log.e("delete capture img", capture.imageLocalFilename);
					Log.e("delete capture video", capture.videoLocalFilename);
					File img = new File (TrimVideoUtils.getLocalImagesPath(context) + "/" + capture.imageLocalFilename);
					if (img.exists()) {
						img.delete();
					}
					File video = new File (TrimVideoUtils.getLocalVideoPath(context) + "/" + capture.videoLocalFilename);
					if (video.exists()) {
						video.delete();
					}
				} catch (Exception e) {
					
				}
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		layout.addView(btn_delete);
		layout.addView(btn_cancel);
		dialog.setContentView(layout);
		dialog.show();
	}
	public static void addComment(final Context context, final _MediaStorage capture, final RelativeLayout commentView, final CaptureListener vcListner) {
		commentView.setVisibility(View.VISIBLE);
		final EditText edittxt_comment  = (EditText)commentView.findViewById(R.id.edittext_comment);
		edittxt_comment.requestFocus();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edittxt_comment, 0);
		TextView btn_send = (TextView)commentView.findViewById(R.id.btn_send);
		btn_send.setClickable(true);
		commentView.findViewById(R.id.view_outside).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commentView.setVisibility(View.GONE);
				vcListner.hideKeyboard();
			}
		});
		
		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 capture.mediaComment = edittxt_comment.getText().toString();
				 vcListner.onCommentAdd(capture);				 
				 commentView.setVisibility(View.GONE);
			}
		});
	}
	
	public static void addTag(Context context, final _MediaStorage capture, final CaptureListener vcListner) {
		String[] list_tag = new String[]{"Goal","Shot","Almost","Save","Penalty","Defense","Ouch"};
		final Dialog dialog;
		dialog = new Dialog(context);
		dialog.setTitle("Tags");
		
		GridView layout = new GridView(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		layout.setPadding(4, 4, 4, 4);
		layout.setNumColumns(2);
		final ArrayAdapter<String> adapter  = new ArrayAdapter<String>(context, 	 
	            android.R.layout.simple_list_item_1, 
	            list_tag);
	    layout.setAdapter(adapter);
	    layout.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				capture.mediaTags = adapter.getItem(position).toString();
				vcListner.onTagSuccess(capture);
				dialog.dismiss();
			}
		});
	    
//		Button btn_cancel = new Button(context);
//		btn_cancel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//		btn_cancel.setBackgroundResource(R.color.white);
//		btn_cancel.setTextColor(Color.BLUE);
//		btn_cancel.setText("Cancel"); 
//		layout.addView(btn_cancel);
		
		dialog.setContentView(layout);
		dialog.show();		
	}
	
	public static void saveMediaInfo(Context context, final _MediaStorage mediaStorage) {
		Log.e("Utils", "saveMediaInfo");
    	String updateUrl = Constants.BASE_URL + "savemediaInfo";
    	
    	/*
    	"mediaAspectRatio
    	imageFileName
    	videoFileName
    	videoUploadResponseHeaders
    	imageUploadResponseHeaders
    	userLatitude
    	userLongitude
    	mediaEventID"
    	*/  
    	
    	final String mediaAspectRatio   = mediaStorage.mediaAspectRatio; 
    	final String localImageFilePath = TrimVideoUtils.getLocalImagesPath(context) + "/" + mediaStorage.imageLocalFilename; 
    	final String localVideoFilePath = TrimVideoUtils.getLocalVideoPath(context) + "/" + mediaStorage.videoLocalFilename;
    	
    	final String videoUploadResponseHeaders = mediaStorage.videoUploadResponseHeaders; 
    	final String imageUploadResponseHeaders = mediaStorage.imageUploadResponseHeaders; 
    	
  		final String latitude_ 			= String.valueOf( GlobalVariablesHolder.user_Latitude ); 
  	 	final String longitude_ 		= String.valueOf( GlobalVariablesHolder.user_Longitude ); 
  	 	final String eventID 			= mediaStorage.mediaEventID;
    	 
  	 	updateUrl += "?mediaAspectRatio=" 			+ mediaAspectRatio 
  	 				+ "&imageFileName=" 			+ localImageFilePath 
  	 				+ "&videoFileName=" 			+ localVideoFilePath 
  	 				+ "&videoUploadResponseHeaders="+ videoUploadResponseHeaders 
  	 				+ "&imageUploadResponseHeaders="+ imageUploadResponseHeaders 
  	 				+ "&userLatitude=" 				+ latitude_
  	 				+ "&userLongitude=" 			+ longitude_
  	 				+ "&mediaEventID=" 				+ eventID;
  		 
   		updateMediaInfo(context, mediaStorage, updateUrl, "save");
	}

	public static void updateMediaInfo(Context ctx, final _MediaStorage mediaStorage) {
		Log.e("Utils", "updateMediaInfo");
		String updateUrl = Constants.BASE_URL + "updatemediaInfo";
		updateMediaInfo(ctx, mediaStorage, updateUrl, "update");
	}

	public static void updateMediaInfo(Context ctx, final _MediaStorage mediaStorage, String updateUrl, final String action) {
    	/*
		
		JSONObject j = mediaInfoToJson(mediaStorage);
    	Map<String, Object> params = new HashMap<String, Object>();
        params.put("mediaInfo", j.toString());
        Log.e("UTILS", "mediaInfo:" + j.toString());
        final DatabaseHelper DB = new DatabaseHelper(ctx);
        AQuery aq = new AQuery(ctx);
    	mediaStorage.modified = "0";
    	mediaStorage.inQueue = "1";
    	DB.createUpdateCapture(mediaStorage);
    	AjaxCallback<JSONObject> callback = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
            	if (json != null) {
	            	Log.e("UTILS", "***************************updateMediaInfo START*******************");
	            	Log.e("UTILS", "URL: " + url);
	            	Log.e("UTILS", json.toString());
	            	Log.e("UTILS", "***************************updateMediaInfo END********************");
            		mediaStorage.modified = "0";
            		mediaStorage.inQueue = "0";
            		if (action.equals("save")) {
            			mediaStorage.isUploaded = "1";
            		}
            		DB.createUpdateCapture(mediaStorage);
            	} else {
            		mediaStorage.modified = "1";
            		mediaStorage.inQueue = "0";
            		DB.createUpdateCapture(mediaStorage);
            	}
            }
        };
        callback.headers(Utils.getHttpHeaders(ctx));
        aq.ajax(updateUrl, params, JSONObject.class, callback);
        
        */
        
	}

	public static JSONObject mediaInfoToJson( _MediaStorage mediaStorage ) {
		
		/* 
  	 	"{
  	 	\"videoFileName\":\"db\\/event_media\\/2014\\/07\\/04\\/13477.mp4\",
  	 	\"mediaServerId\":13477,
  	 	\"videoUploadResponseHeaders\":\"{\\\"x-amz-request-id\\\":\\\"97679AB4278CE05F\\\",
  	 	\\\"Etag\\\":\\\"\\\\\\\"59a17baece894eea0140c721fa98bb82\\\\\\\"\\\",
  	 	\\\"x-amz-id-2\\\":\\\"M9YiA5zQEiCFFVnjdQYar8jVRPVSu\\\\\\/PzaIqiErfqWfdZpgW3nQBh17g+qbTa0pHD\\\",
  	 	\\\"Content-Length\\\":\\\"0\\\",
  	 	\\\"Server\\\":\\\"AmazonS3\\\",
  	 	\\\"Date\\\":\\\"Fri, 04 Jul 2014 02:35:12 GMT\\\"}\",
  	 	\"mediaCoverPath\":\"\\/private\\/var\\/mobile\\/Applications\\/50F61550-36D1-4C20-BCB6-424A07CE6970\\/tmp\\/0_Movie_64631246766.jpg\",
  	 	\"userLatitude\":\"10.324800\",
  	 	\"isImageUploaded\":1,
  	 	\"mediaEventID\":\"1728\",
  	 	\"mediaDate\":\"2014-07-04 10:35:06\",
  	 	\"mediaOrientation\":3,
  	 	\"inQueue\":1,
  	 	\"isVideoUploaded\":1,
  	 	\"imageFileName\":\"db\\/event_media\\/2014\\/07\\/04\\/13477.jpg\",
  	 	\"userLongitude\":\"123.905752\",
  	 	\"isDeletedByUser\":0,
  	 	\"imageUploadResponseHeaders\":\"{\\\"x-amz-request-id\\\":\\\"5E4114B9D7FE3A58\\\",
  	 	\\\"Etag\\\":\\\"\\\\\\\"fb31f87df9cc0314899b41e1b118eb65\\\\\\\"\\\",
  	 	\\\"x-amz-id-2\\\":\\\"NYfcJgGHUtEhJ5p3CjJQk2g0Nq8ZozGGRGEtPfWGepZqBUdn\\\\\\/Ikn8wEa0OtHm\\\\\\/n8\\\",
  	 	\\\"Content-Length\\\":\\\"0\\\",
  	 	\\\"Server\\\":\\\"AmazonS3\\\",
  	 	\\\"Date\\\":\\\"Fri, 04 Jul 2014 02:35:13 GMT\\\"}\",
  	 	\"mediaFileKey\":\"0Movie64631246766\",
  	 	\"mediaSessionID\":null,
  	 	\"mediaEventId\":\"1728\",
  	 	\"isUploaded\":1,
  	 	\"mediaAspectRatio\":\"16:9\",
  	 	\"mediaPath\":\"\\/private\\/var\\/mobile\\/Applications\\/50F61550-36D1-4C20-BCB6-424A07CE6970\\/tmp\\/0_Movie_64631246766.mp4\"}";
		} 
  	 	*/
 
		JSONObject j = new JSONObject();
		try { 
			j.put("videoFileName", 				mediaStorage.videoServerFilePath);
	  	 	j.put("mediaServerId", 				mediaStorage.mediaServerId);
	  	 	j.put("videoUploadResponseHeaders",	mediaStorage.videoUploadResponseHeaders);
	  	 	j.put("Etag", 						"");
	  	 	j.put("x-amz-id-2", 				"");
	  	 	j.put("Content-Length", 			"0");
	  	 	j.put("Server", 					"AmazonS3");
	  	 	j.put("Date", 						mediaStorage.mediaDateLongFormat);//long date format
	  	 	j.put("mediaCoverPath", 			mediaStorage.imageLocalFilePath); //should be local path of image
	  	 	j.put("userLatitude", 				mediaStorage.userLatitude);
	  	 	j.put("isImageUploaded", 			"1");
	  	 	j.put("mediaEventID", 				mediaStorage.mediaEventID);
	  	 	j.put("mediaDate", 					mediaStorage.mediaDateShortFormat); //short date format
	  	 	j.put("mediaOrientation", 			"3");
	  	 	j.put("inQueue", 					"1");
	  	 	j.put("isVideoUploaded", 			"1");
	  	 	j.put("imageFileName", 				mediaStorage.imageServerFilePath); //should be SERVER path of image
	  	 	j.put("userLongitude", 				mediaStorage.userLongitude);
	  	 	j.put("isDeletedByUser", 			"0");
	  	 	j.put("imageUploadResponseHeaders", mediaStorage.imageUploadResponseHeaders);
	  	 	j.put("Etag", 						"");
	  	 	j.put("x-amz-id-2", 				"");
	  	 	j.put("Content-Length", 			"0");
	  	 	j.put("Server", 					"AmazonS3");
	  	 	j.put("Date", 						mediaStorage.mediaDateLongFormat); //long date format
	  	 	j.put("mediaFileKey", 				"");
	  	 	j.put("mediaSessionID", 			"");
	  	 	j.put("mediaEventId", 				mediaStorage.mediaEventID);
	  	 	j.put("isUploaded", 				"1");
	  	 	j.put("mediaAspectRatio", 			mediaStorage.mediaAspectRatio);
	  	 	j.put("mediaPath", 					mediaStorage.videoLocalFilePath); //should be local path of video
			   
			/*
	    	//ORIGINAL CODE 
			j.put("imageFileName", mediaStorage.mediaCoverPath); // this is imageShortPath on server
			j.put("mediaAspectRatio", mediaStorage.mediaAspectRatio);
			j.put("videoFileName", mediaStorage.mediaPath); // this is videoShortPath on server
			j.put("videoUploadResponseHeaders", mediaStorage.videoUploadResponseHeaders);
			j.put("imageUploadResponseHeaders", mediaStorage.imageUploadResponseHeaders);
			j.put("userLatitude", mediaStorage.userLatitude);
			j.put("userLongitude", mediaStorage.userLongitude);
			j.put("mediaFileKey", mediaStorage.mediaFileKey);
			j.put("mediaServerId", mediaStorage.mediaServerId);
	    	j.put("mediaTags",  mediaStorage.mediaTags);
	    	j.put("isDeletedByUser",  mediaStorage.isDeletedByUser);
	    	j.put("mediaComment",  mediaStorage.mediaComment);
	    	j.put("isFavorite",  mediaStorage.isFavorite);
	    	j.put("isSharedOnFacebook",  mediaStorage.isSharedOnFacebook);
	    	j.put("isSharedOnTwitter", mediaStorage.isSharedOnTwitter);
	    	j.put("isSharedOnMail", mediaStorage.isSharedOnMail);
	    	j.put("isSharedOnSms", mediaStorage.isSharedOnSms);
	    	j.put("mediaEventID", mediaStorage.mediaEventID);
	    	 */
	    	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return j;
	}

	public static void saveQueue(Context ctx, _MediaQueue mediaQueue) {
		/*
		DatabaseHelper DB = new DatabaseHelper(ctx);
		_MediaStorage mediaStorage = DB.getMediaStorageByMediaServerId(mediaQueue.mediaServerId);
		try {
			if (mediaStorage != null && mediaStorage.ID != null && ! mediaStorage.ID.equals("0")) {
				
				if (mediaQueue.mediaStorageId.equals("0") ||  mediaQueue.mediaStorageId == null) {
					mediaQueue.mediaStorageId = mediaStorage.ID;
				}
				
				Log.e("saveQueue-BLAH BLAH", "***********************************************");
				
				Log.e("UTILS", "***********************************************");
		    	Log.e("UTILS", "******************* saveQueue ***************************");
		    	Log.e("UTILS", "******************* mediaStorage.ID: " + mediaStorage.ID + " ***************************");
		    	Log.e("UTILS", "******************* mediaQueue.mediaStorageId: " + mediaQueue.mediaStorageId + " ***************************");
		    	Log.e("UTILS", "***********************************************");
		    	DB.createUpdateQueue(mediaQueue);
				mediaStorage.inQueue = mediaQueue.inQueue;
				mediaStorage.isImageUploaded = mediaQueue.isImageUploaded;
				mediaStorage.isVideoUploaded = mediaQueue.isVideoUploaded;
//				mediaStorage.isUploaded = mediaQueue.isUploaded;
				mediaStorage.mediaPath = mediaQueue.videoFilePath;
				mediaStorage.mediaCoverPath = mediaQueue.imageFilePath;
				
				mediaStorage.imageUploadResponseHeaders = mediaQueue.imageUploadResponseHeaders;
				mediaStorage.videoUploadResponseHeaders = mediaQueue.videoUploadResponseHeaders;
				mediaStorage.modified = "1";
				
				DB.createUpdateCapture(mediaStorage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	public static String getCurrentLocalFilename() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	/*
 //ORIGINAL
	public static Map<String, String> getHttpHeaders(Context c) {
		Map<String, String> map = new HashMap<String, String>();
	    map.put("TIMEZONE", 	TimeZone.getDefault().getDisplayName());
	    SharedPreferences sharedPreferences = c.getSharedPreferences( "com.sportxast.SportXast", Context.MODE_PRIVATE);	    
	    map.put("USERID", 		sharedPreferences.getString(KEY_PROFILE.USER_ID, ""));
	    map.put("USERSESSION", 	sharedPreferences.getString(KEY_PROFILE.SESSION_ID, ""));
	    map.put("PHONELANGUAGE",Locale.getDefault().getDisplayLanguage());
	    map.put("USERCOUNTRY", 	sharedPreferences.getString(KEY_PROFILE.COUNTRY, ""));
	    map.put("USERLOCALITY", sharedPreferences.getString(KEY_PROFILE.LOCALITY, ""));
	    
	    return map;
	} 
	*/ 
	  
	public static Map<String, String> getHttpHeaders(Context context) {
		Map<String, String> map = new HashMap<String, String>();
		
		//map.put("USERID", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
		map.put("USERID", 			GlobalVariablesHolder.X_DEVICE_ID);  
		map.put("USERSESSION", 		GlobalVariablesHolder.X_SESSION_ID );
		map.put("PHONELANGUAGE", 	Locale.getDefault().getLanguage());
		String timezone_ = TimeZone.getDefault().getID();
		map.put("TIMEZONE", 		""+TimeZone.getDefault().getID());
		
		map.put("USERCOUNTRY",  	GlobalVariablesHolder.X_USERCOUNTRY);
		map.put("USERLOCALITY", 	GlobalVariablesHolder.X_USERLOCALITY);
		
	    return map;
	}
	   
	public static void showMediaStorageObj(_MediaStorage currentCapture) {
		try {
		Log.e(Keys.KEY_MEDIA_STORAGE.ID, currentCapture.ID);
		Log.e(Keys.KEY_MEDIA_STORAGE.IMAGE_FILE_NAME,
				currentCapture.imageLocalFilename);
		Log.e(Keys.KEY_MEDIA_STORAGE.IMAGE_UPLOAD_RESPONSE_HEADERS,
				currentCapture.imageUploadResponseHeaders);
		Log.e(Keys.KEY_MEDIA_STORAGE.IN_QUEUE,
				currentCapture.inQueue);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_DELETED_BY_USER,
				currentCapture.isDeletedByUser);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_FAVORITE,
				currentCapture.isFavorite);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_IMAGE_UPLOADED,
				currentCapture.isImageUploaded);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_SHARED_ON_FACEBOOK,
				currentCapture.isSharedOnFacebook);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_SHARED_ON_MAIL,
				currentCapture.isSharedOnMail);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_SHARED_ON_SMS,
				currentCapture.isSharedOnSms);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_SHARED_ON_TWITTER,
				currentCapture.isSharedOnTwitter);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_UPLOADED,
				currentCapture.isUploaded);
		Log.e(Keys.KEY_MEDIA_STORAGE.IS_VIDEO_UPLOADED,
				currentCapture.isVideoUploaded);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_ASPECT_RATIO,
				currentCapture.mediaAspectRatio);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_COMMENT,
				currentCapture.mediaComment);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_COVER_PATH,
				currentCapture.imageServerFilePath);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_DATE,
				currentCapture.mediaDateShortFormat);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_EVENT_ID,
				currentCapture.mediaEventID);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_FILE_KEY,
				currentCapture.mediaFileKey);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_ORIENTATION,
				currentCapture.mediaOrientation);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_PATH,
				currentCapture.videoServerFilePath);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_SERVER_ID,
				currentCapture.mediaServerId);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_SESSION_ID,
				currentCapture.mediaSessionID);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_SHARE_URL,
				currentCapture.mediaShareUrl);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_TAGS,
				currentCapture.mediaTags);
		Log.e(Keys.KEY_MEDIA_STORAGE.MEDIA_SHARE_TEXT,
				currentCapture.shareText);
		Log.e(Keys.KEY_MEDIA_STORAGE.USER_LATITUDE,
				currentCapture.userLatitude);
		Log.e(Keys.KEY_MEDIA_STORAGE.USER_LONGITUDE,
				currentCapture.userLongitude);
		Log.e(Keys.KEY_MEDIA_STORAGE.VIDEO_FILE_NAME,
				currentCapture.videoLocalFilename);
		Log.e(Keys.KEY_MEDIA_STORAGE.VIDEO_UPLOAD_RESPONSE_HEADERS,
				currentCapture.videoUploadResponseHeaders);
		Log.e(Keys.KEY_MEDIA_STORAGE.MODIFIED,
				currentCapture.modified);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



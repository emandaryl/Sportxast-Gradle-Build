package com.sportxast.SportXast.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.models.NextMediaID;
import com.sportxast.SportXast.models._MediaStorage;

import java.util.ArrayList;
 
public class RunnableChecker implements Runnable
{
	   private boolean killMe = false;
	   private Handler handler;
	   private Context context;
	   private Global_Data FGlobal_Data;
	   
	   public void run()
	   { 
	      if(killMe){
	    	 // Toast.makeText(context, "--RunnableChecker-KILLED!-" + "\n", Toast.LENGTH_LONG).show(); 
	         return;
	      }
	      
	      if( GlobalVariablesHolder.currentActivityContext instanceof VideoCaptureActivity ){ 
	    	    /**---STOP THE HANDLER----**/ 
		    /*	killRunnable(); 
		    	//Toast.makeText(context, "CURRENT ACTIVITY: VIDEO CAPTURE" + "\nMUST KILL THREAD", Toast.LENGTH_LONG).show(); 
		    	return;*/
	      }
	      
	      /* do your work  */ 
	      SharedPreferences sharedpreferences = context.getSharedPreferences("com.sportxast.SportXast.highlights", context.MODE_PRIVATE); 
		   if (sharedpreferences.contains( Constants.sharePrefKey_uploadedHighlights ) )
				{  
			    GlobalVariablesHolder.threadUploaderIsRunning = true;
			   //#####################################################################################################################
			   //#####################################################################################################################
			   	String highlightDataInString = sharedpreferences.getString( Constants.sharePrefKey_uploadedHighlights , "");   
			   	
			   	if(highlightDataInString.substring(0, 2).equals("||")){
			   		highlightDataInString = highlightDataInString.substring(2);
			   	}
			   		 
			    if( highlightDataInString.length() > 0 ){ 
			    	/**---STOP THE HANDLER----**/ 
			    	killRunnable();  
			    	String[] arrHighlightsData = highlightDataInString.split("\\|\\|"); 
			    	//################################################################################### 
			    	//################################################################################### 
			    	 ArrayList<NextMediaID> arrNextMediaID = FGlobal_Data.prepareNewlyUploadedHighlightsData(highlightDataInString);   
			    	 /* shareUrl = 
						mediaId = 13435
						shareText = 
					  	added = 
					  	imagePath = db/event_media/2014/07/03/13435.jpg 
					  	videoPath = db/event_media/2014/07/03/13435.mp4 
					 	eventId = 1725 
						coverImage = 20140703153710.jpg 
					  	largeImageUrl = 
					  	videoLocalPath = /storage/sdcard0/Sportx/video/20140703153710.mp4 
						imageLocalPath = /storage/sdcard0/Sportx/video/20140703153710.jpg 
						videoLocalFilename = 20140703153710.mp4  */ 
			    	 /*
		    		 * @param context 
		    		 * @param videoServerFilePath value = db/event_media/2014/07/03/13434.mp4 <-from server 
		    		 * @param videoLocalFilename  value = 20140703150721.mp4 <- from LOCAL
		    		 * @param imageServerFilePath value = db/event_media/2014/07/03/13434.jpg 
		    		 * @param imageLocalFilename  value = 20140703150721.jpg
		    		 * @param filetype
		    		 */
			    	 
			    	 /**---STOP THE HANDLER----**/
			    	 handler.removeCallbacks(this); 
			    	 //Toast.makeText(context, "UPLOAD QUEUE: FINISHED STEP - " + arrNextMediaID.get(0).getUploadQueue(), Toast.LENGTH_LONG).show();  
			    	 
			    	 final int uploadQueue = Integer.parseInt( arrNextMediaID.get(0).getUploadQueue() );
			    	 if(uploadQueue == 1){//Proceed to Step 2
			    		//######--STEP 2 STARTS HERE
	        			 new AmazonProcessingTask2( 
			    			context, 
			    			arrNextMediaID.get(0).getEventId(),
			    			arrNextMediaID.get(0).getMediaId(),
			    			arrNextMediaID.get(0).getVideoServerPath(), 
			    			arrNextMediaID.get(0).getVideoLocalFilename(),
			    			arrNextMediaID.get(0).getImageServerPath(),
			    			arrNextMediaID.get(0).getImageLocalFilename(),
			    			"video",
			    			"",
			    			"",
			    			arrNextMediaID.get(0).getUserLatitude(),
			    			arrNextMediaID.get(0).getUserLongitude(),
			    			arrNextMediaID.get(0).getMediaDateShortFormat(),
			    			arrNextMediaID.get(0).getMediaDateLongFormat(), 
			    			arrNextMediaID.get(0).getVideoLocalPath(),
			    			arrNextMediaID.get(0).getImageLocalPath(),
			    			arrNextMediaID.get(0).getTwitterCardUrl()
			    			).execute();  
			    	 }else if(uploadQueue == 2){//Proceed to Step 3
			    		//######--STEP 3 STARTS HERE
			    		 new AmazonProcessingTask2(
			    			 context,
							 arrNextMediaID.get(0).getEventId(),
							 arrNextMediaID.get(0).getMediaId(),
							 arrNextMediaID.get(0).getVideoServerPath(), 
							 arrNextMediaID.get(0).getVideoLocalFilename(),
							 arrNextMediaID.get(0).getImageServerPath(),
							 arrNextMediaID.get(0).getImageLocalFilename(),
							 "picture",
							 arrNextMediaID.get(0).getVideoUploadResponseHeaders(),
							 arrNextMediaID.get(0).getImageUploadResponseHeaders(),
							 arrNextMediaID.get(0).getUserLatitude(),
							 arrNextMediaID.get(0).getUserLongitude(),
							 arrNextMediaID.get(0).getMediaDateShortFormat(),
							 arrNextMediaID.get(0).getMediaDateLongFormat(), 
							 arrNextMediaID.get(0).getVideoLocalPath(),
							 arrNextMediaID.get(0).getImageLocalPath(),
							 arrNextMediaID.get(0).getTwitterCardUrl()
							 ).execute();
			    	 }else if(uploadQueue == 3){//Proceed to Step 4
			    		//######--STEP 4 STARTS HERE
			    		 _MediaStorage mediaStorage = new _MediaStorage(); 
				    	 mediaStorage.ID = "0"; 
				    	 mediaStorage.uploadQueue			= arrNextMediaID.get(0).getUploadQueue();
				    	 mediaStorage.imageLocalFilename 	= arrNextMediaID.get(0).getImageLocalFilename(); //local filename of image 
				    	 mediaStorage.videoLocalFilename	= arrNextMediaID.get(0).getVideoLocalFilename(); //local filename of Video
				    	 
				    	 mediaStorage.videoServerFilePath 	= arrNextMediaID.get(0).getVideoServerPath();
				    	 mediaStorage.imageServerFilePath 	= arrNextMediaID.get(0).getImageServerPath(); //server image path 
				    	 
				    	 mediaStorage.videoLocalFilePath	= arrNextMediaID.get(0).getVideoLocalPath();
				    	 mediaStorage.imageLocalFilePath 	= arrNextMediaID.get(0).getImageLocalPath();
				    	 
				    	 mediaStorage.videoUploadResponseHeaders = arrNextMediaID.get(0).getVideoUploadResponseHeaders();
				    	 mediaStorage.imageUploadResponseHeaders = arrNextMediaID.get(0).getImageUploadResponseHeaders();
			    		
				    	 mediaStorage.inQueue 				= "0";
				    	 mediaStorage.isDeletedByUser 		= "0";
				    	 mediaStorage.isFavorite 			= "0";
				    	 mediaStorage.isImageUploaded 		= "1";
				    	 mediaStorage.isSharedOnFacebook	= "0";
				    	 mediaStorage.isSharedOnMail 		= "0";
				    	 mediaStorage.isSharedOnSms 		= "0";
				    	 mediaStorage.isSharedOnTwitter 	= "0";
				    	 mediaStorage.isUploaded 			= "1";
				    	 mediaStorage.isVideoUploaded 		= "0";
				    	 mediaStorage.mediaAspectRatio 		= "";
				    	 mediaStorage.mediaComment 			= ""; 
				    	  
				    	 mediaStorage.mediaDateShortFormat	= arrNextMediaID.get(0).getMediaDateShortFormat(); //format should be "2014-07-04 10:35:06"
				    	 mediaStorage.mediaDateLongFormat	= arrNextMediaID.get(0).getMediaDateLongFormat(); //format should be "Fri, 04 Jul 2014 02:35:12 GMT" 
				    	  
				    	 mediaStorage.mediaEventID 			= arrNextMediaID.get(0).getEventId();
				    	 mediaStorage.mediaFileKey 			= "";
				    	 mediaStorage.mediaOrientation 		= "0";
				     
				    	 mediaStorage.mediaServerId 		= arrNextMediaID.get(0).getMediaId();
				    	 mediaStorage.mediaSessionID 		= "";
				    	 mediaStorage.mediaShareUrl 	 	= arrNextMediaID.get(0).getShareUrl();
				    	 mediaStorage.mediaTags 			= "";
				    	 mediaStorage.shareText 			= ""; 
				    	 mediaStorage.userLatitude 			= arrNextMediaID.get(0).getUserLatitude();
				    	 mediaStorage.userLongitude  		= arrNextMediaID.get(0).getUserLongitude();
			    		    
				    	 mediaStorage.modified  			= "1";
				    	 mediaStorage.isSaved 				= "0";    
				    	 mediaStorage.twitterCardUrl		= arrNextMediaID.get(0).getTwitterCardUrl();
				    	  
			    		 FGlobal_Data.saveMediaInfo( mediaStorage, 1 ); 
			    	 }else if(uploadQueue == 4){//Proceed to Step 2
			    		 
			    	 }else if(uploadQueue == 0){//Proceed to Step 1
			    		  
				    	 _MediaStorage mediaStorage = new _MediaStorage(); 
				    	 mediaStorage.ID = "0"; 
				    	 mediaStorage.uploadQueue			= arrNextMediaID.get(0).getUploadQueue();
				    	 mediaStorage.imageLocalFilename 	= arrNextMediaID.get(0).getImageLocalFilename(); //local filename of image 
				    	 mediaStorage.videoLocalFilename	= arrNextMediaID.get(0).getVideoLocalFilename(); //local filename of Video
				    	 
				    	 mediaStorage.videoServerFilePath 	= arrNextMediaID.get(0).getVideoServerPath();
				    	 mediaStorage.imageServerFilePath 	= arrNextMediaID.get(0).getImageServerPath(); //server image path 
				    	 
				    	 mediaStorage.videoLocalFilePath	= arrNextMediaID.get(0).getVideoLocalPath();
				    	 mediaStorage.imageLocalFilePath 	= arrNextMediaID.get(0).getImageLocalPath();
				    	 
				    	 mediaStorage.videoUploadResponseHeaders = "";
				    	 mediaStorage.imageUploadResponseHeaders = "";
			    		
				    	 mediaStorage.inQueue 				= "0";
				    	 mediaStorage.isDeletedByUser 		= "0";
				    	 mediaStorage.isFavorite 			= "0";
				    	 mediaStorage.isImageUploaded 		= "0";
				    	 mediaStorage.isSharedOnFacebook	= "0";
				    	 mediaStorage.isSharedOnMail 		= "0";
				    	 mediaStorage.isSharedOnSms 		= "0";
				    	 mediaStorage.isSharedOnTwitter 	= "0";
				    	 mediaStorage.isUploaded 			= "0";
				    	 mediaStorage.isVideoUploaded 		= "0";
				    	 mediaStorage.mediaAspectRatio 		= "";
				    	 mediaStorage.mediaComment 			= ""; 
				    	  
				    	 mediaStorage.mediaDateShortFormat	= arrNextMediaID.get(0).getMediaDateShortFormat(); //format should be "2014-07-04 10:35:06"
				    	 mediaStorage.mediaDateLongFormat	= arrNextMediaID.get(0).getMediaDateLongFormat(); //format should be "Fri, 04 Jul 2014 02:35:12 GMT" 
				    	  
				    	 mediaStorage.mediaEventID 			= arrNextMediaID.get(0).getEventId();
				    	 mediaStorage.mediaFileKey 			= "";
				    	 mediaStorage.mediaOrientation 		= "0";
				     
				    	 mediaStorage.mediaServerId 		= arrNextMediaID.get(0).getMediaId();
				    	 mediaStorage.mediaSessionID 		= "";
				    	 mediaStorage.mediaShareUrl 	 	= arrNextMediaID.get(0).getShareUrl();
				    	 mediaStorage.mediaTags 			= "";
				    	 mediaStorage.shareText 			= ""; 
				    	 mediaStorage.userLatitude 			= arrNextMediaID.get(0).getUserLatitude();
				    	 mediaStorage.userLongitude  		= arrNextMediaID.get(0).getUserLongitude();
			    		    
				    	 mediaStorage.modified  			= "1";
				    	 mediaStorage.isSaved 				= "0";    
				    	 mediaStorage.twitterCardUrl		= arrNextMediaID.get(0).getTwitterCardUrl();
				    	 
				    	 FGlobal_Data.saveMediaInfo( mediaStorage, 0);  
			    	 }
			    	 return; 
			    }
			}else{ // if sharedpref of highlights is empty, STOP THE CHECKER 
				/**---STOP THE HANDLER----**/ 
		    	killRunnable();  
		    	//Toast.makeText(context, "--RunnableChecker-KILLED 22!-" + "\n", Toast.LENGTH_LONG).show();  
		    	return;
			}

	      /* and here comes the "trick" */
		   handler.postDelayed(this, 3000);
	   }

	   /**---STOP THE HANDLER----**/ 
	   public void killRunnable()
	   {
	      killMe = true; 
	    //  this.handler.removeCallbacks(this);  
	      GlobalVariablesHolder.threadUploaderIsRunning = false;  
	   }
	   
	   /**---RESTART THE HANDLER----**/ 
	   public void restartRunnable()
	   {
	      killMe = false; 
	    //  this.handler.removeCallbacks(this);  
	      GlobalVariablesHolder.threadUploaderIsRunning = true;  
	   }
	    
	   public void setHandler( Handler handler ){
		   this.handler = handler;
	   }
	   
	   public void setContext(Context context){
		   this.context = context;
	   }
	   
	   public void setGlobal_Data(Global_Data global_data){
		   this.FGlobal_Data = global_data;
	   }
	   
}
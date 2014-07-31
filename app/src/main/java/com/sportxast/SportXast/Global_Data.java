package com.sportxast.SportXast;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.sportxast.SportXast.activities2_0.Profile_Activity;
import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.crashreporting.ReportHandler;
import com.sportxast.SportXast.models.NextMediaID;
import com.sportxast.SportXast.models._AppSetting;
import com.sportxast.SportXast.models._EventLists.EventLists;
import com.sportxast.SportXast.models._MediaLists.Comments;
import com.sportxast.SportXast.models._MediaLists.MediaList;
import com.sportxast.SportXast.models._MediaStorage;
import com.sportxast.SportXast.models._RecentEvent;
import com.sportxast.SportXast.tasks.AmazonProcessingTask2;
import com.sportxast.SportXast.tasks.BackgroundService;
import com.sportxast.SportXast.tasks.BackgroundService2;
import com.sportxast.SportXast.tasks.RunnableChecker;
import com.sportxast.SportXast.thirdparty_class.GPSTracker;

public class Global_Data extends Application {

    Coordinate coordinate = new Coordinate();
    ArrayList<Comments> list_comments = new ArrayList<Comments>();
    _AppSetting appSetting= new _AppSetting();
    _RecentEvent newEvent = new _RecentEvent();
    EventLists eventLists = new EventLists();

    //public SharedPreferences sharedPreferences;
    String event_id  = "";
    String FVideoPath= "";

    RunnableChecker FRunnableChecker;
    GPSTracker gpsTracker;


    /** START THE THREAD - AWS UPLOADER **/
    public void runThreadUploaderXXTHREADPOOL(Context context){
        if(GlobalVariablesHolder.threadUploaderIsRunning == true){
            //IGNORE
            //Toast.makeText(context, "ALREADY RUNNING", Toast.LENGTH_LONG).show();
        }
        else{

            int poolSize = 2;
            int maxPoolSize = 2;
            long keepAliveTime = 10;
            ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>( 5 );
            ThreadPoolExecutor threadPool = null;
            threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);

            threadPool.execute( this.runnableX );

        }
    }

    public Runnable runnableX = new Runnable() {
        @Override
        public void run() {
		      /* do what you need to do */
            Log.e("SERVICE SERVICE SERVICE", "SERVICE SERVICE SERVICE SERVICE");
				/* do your work  */
            getApplicationContext();
            SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("com.sportxast.SportXast.highlights", Context.MODE_PRIVATE);
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
                    GlobalVariablesHolder.threadUploaderIsRunning = true;
                    //killRunnable();

                    String[] arrHighlightsData = highlightDataInString.split("\\|\\|");
                    //###################################################################################
                    //###################################################################################
                    ArrayList<NextMediaID> arrNextMediaID = prepareNewlyUploadedHighlightsData(highlightDataInString);
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

                    //Toast.makeText(context, "UPLOAD QUEUE: FINISHED STEP - " + arrNextMediaID.get(0).getUploadQueue(), Toast.LENGTH_LONG).show();
                    final int uploadQueue = Integer.parseInt( arrNextMediaID.get(0).getUploadQueue() );
                    if(uploadQueue == 1){//Proceed to Step 2
                        //######--STEP 2 STARTS HERE
                        new AmazonProcessingTask2(
                                getApplicationContext(),
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
                                getApplicationContext(),
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

                        saveMediaInfo( mediaStorage, 1 );
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

                        saveMediaInfo( mediaStorage, 0);
                    }
                    return;
                }
            }else{ // if sharedpref of highlights is empty, STOP THE CHECKER
                /**---STOP THE HANDLER----**/
                //killRunnable();
                //Toast.makeText(context, "--RunnableChecker-KILLED 22!-" + "\n", Toast.LENGTH_LONG).show();
                System.out.println("BLAHX BLAHX BLAHX BLAHX BLAHX BLAHX BLAHX BLAHX BLAHX");
                GlobalVariablesHolder.threadUploaderIsRunning = false;
                return;
            }
        }
    };


    public void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        //String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_MODE);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    public void getCurrentLocation() {
        gpsTracker = new GPSTracker( getApplicationContext() );

        if (gpsTracker.canGetLocation()) {

            Coordinate coordinate = new Coordinate();
            coordinate.latitude = gpsTracker.getLatitude();
            coordinate.longitude = gpsTracker.getLongitude();

            Log.e("LATITUDE : ", "" + coordinate.latitude);
            Log.e("LONGITUDE : ", "" + coordinate.longitude);

            GlobalVariablesHolder.user_Latitude = gpsTracker.getLatitude();
            /** default value SHOULD be 0 **/
            GlobalVariablesHolder.user_Longitude= gpsTracker.getLongitude();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            //gpsTracker.showSettingsAlert();
        }

        if( ( (int) GlobalVariablesHolder.user_Latitude == 0 ) &&
                ( (int) GlobalVariablesHolder.user_Longitude == 0 ) ){
            Toast.makeText( getApplicationContext(), "WRONG WRONG LOCATION", Toast.LENGTH_LONG ).show();
        }
    }


    /** START THE THREAD - AWS UPLOADER **/
    public void runThreadUploaderORIGINAL(Context context){
        if(this.FRunnableChecker == null){
            this.FRunnableChecker = new RunnableChecker();
            this.FRunnableChecker.setContext( context );
            this.FRunnableChecker.setHandler( handler );
            this.FRunnableChecker.setGlobal_Data( this );
            this.handler.postDelayed(this.FRunnableChecker , 3000);
            //Toast.makeText(context, "STARTED RUNNING", Toast.LENGTH_LONG).show();

        }else{
            if(GlobalVariablesHolder.threadUploaderIsRunning == true){
                //IGNORE
                //Toast.makeText(context, "ALREADY RUNNING", Toast.LENGTH_LONG).show();
            }
            else{
                //Toast.makeText(context, "RESTARTED", Toast.LENGTH_LONG).show();
                this.FRunnableChecker.restartRunnable();
                this.handler.postDelayed(this.FRunnableChecker , 3000);
            }
            //Toast.makeText(context, "RUN RUN RUN", Toast.LENGTH_LONG).show();
        }
    }

    /** STOP/KILL THE THREAD - AWS UPLOADER **/
    public void stopThreadUploader( Context context ){
        if(this.FRunnableChecker == null){
            //IGNORE
        }else{ //if Thread NOT NULL
            this.FRunnableChecker.killRunnable();
        }
    }

    public Handler handler = new Handler();


    /** START THE THREAD - AWS UPLOADER **/
    public void runThreadUploader(Context context){

        Intent in = new Intent(getApplicationContext(), BackgroundService.class);
        startService(in);
		/*
		Toast.makeText( context, "BACKGROUND SERVICE YOLLLLL", Toast.LENGTH_LONG ).show();
		startService(new Intent(context, BackgroundService.class));
		*/
    }

    /** START THE THREAD - VIDEO/IMAGE PROCESSOR **/
    public void runThreadMediaProcessing(Context context){

        Intent in = new Intent(getApplicationContext(), BackgroundService2.class);
        startService(in);
		/*
		Toast.makeText( context, "BACKGROUND SERVICE YOLLLLL", Toast.LENGTH_LONG ).show();
		startService(new Intent(context, BackgroundService.class));
		*/
    }


    //##############################################################################################################
    //##############################################################################################################
    /** APISavingType: 0 - updateMediaInfo, 1 - savemediaInfo  **/
    public void saveMediaInfo( final _MediaStorage mediaStorage, final int APISavingType ){
        String APIname = "";

        if(APISavingType == 1){ //
            APIname = "savemediaInfo";
            //String loll = APIname;
        }else{
            APIname = "updateMediaInfo";
        }

        String url = Constants.BASE_URL + APIname;
		
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
		}  */

        AQuery aq = new AQuery( getApplicationContext() );
        JSONObject mediaInfoJSON = Utils.mediaInfoToJson(mediaStorage);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "mediaInfo", mediaInfoJSON.toString() );
        Log.e( "UTILS", "mediaInfo:" + mediaInfoJSON.toString() );

        mediaStorage.modified = "0";
        mediaStorage.inQueue  = "1";

        //#######################################################################
        //#######################################################################
        AjaxCallback<JSONObject> callback = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    Log.e("UTILS", "***************************savemediaInfo********************");
                    Log.e("UTILS", "URL: " + url);
                    Log.e("UTILS", json.toString());
                    Log.e("UTILS", "***************************/savemediaInfo********************");

                    if(APISavingType == 1){ //
	            		/*
	            		if( GlobalVariablesHolder.currentActivityContext instanceof VideoCaptureActivity ){ 
	    					   
	    					//PAUSE THE UPLOADING 
	    					//PAUSE THE UPLOADING 
	    					//PAUSE THE UPLOADING 
	    					 
	          	    	    //---STOP THE HANDLER---
	          	    	    //killRunnable(); 
	          		    	//Toast.makeText(context, "FINISHED STEP 4: uploaded PICTURE/IMAGE\n"+ "CURRENT ACTIVITY: VIDEO CAPTURE" + "\nMUST KILL THREAD", Toast.LENGTH_LONG).show(); 
	          		    	 
	          		    	return;
	          	      	}
	            		*/

                        getApplicationContext();
                        SharedPreferences sharedpreferences = getSharedPreferences("com.sportxast.SportXast.highlights", Context.MODE_PRIVATE);
                        if (sharedpreferences.contains( Constants.sharePrefKey_uploadedHighlights ) )
                        {
                            String highlightDataInString = sharedpreferences.getString( Constants.sharePrefKey_uploadedHighlights , "");

                            int firstEntryCounter = 0;
                            if( highlightDataInString.split("\\|\\|").length > 1 ){

                                firstEntryCounter = highlightDataInString.indexOf("||") + 2;
                                String updated_highlightData = highlightDataInString.substring(firstEntryCounter);

                                Editor editor = sharedpreferences.edit();
                                editor.putString(Constants.sharePrefKey_uploadedHighlights, updated_highlightData);
                                editor.commit();
                                //Toast.makeText(getApplicationContext(), "NAA PAY DAGHAN BRAD" + json.toString() , Toast.LENGTH_LONG).show();
                            }else{
                                //Toast.makeText(getApplicationContext(), "MAO RA TO SIYA BRAD" + json.toString() , Toast.LENGTH_LONG).show();
                                sharedpreferences.edit().remove( Constants.sharePrefKey_uploadedHighlights ).commit();
                            }
                        }
                        GlobalVariablesHolder.threadUploaderIsRunning = false;
                        runThreadUploader(getApplicationContext());
                        //Toast.makeText(getApplicationContext(), "HUMANA UPLOAD SA TANAN BRAD!!!!" + json.toString() , Toast.LENGTH_LONG).show();

                    }else{

                        JSONObject newFirstHighlightJSONObject = CommonFunctions_1.reformHighlightJSONObject(mediaStorage, "1");
                        CommonFunctions_1.updateSharedPreferencesQueueing(getApplicationContext(), newFirstHighlightJSONObject);
                        if( GlobalVariablesHolder.currentActivityContext instanceof VideoCaptureActivity ){

                            //PAUSE THE UPLOADING
                            //PAUSE THE UPLOADING
                            //PAUSE THE UPLOADING
	    					/*
	          	    	    //---STOP THE HANDLER---
	          	    	    killRunnable(); 
	          		    	Toast.makeText(context, "FINISHED STEP 1: uploaded PICTURE/IMAGE\n"+ "CURRENT ACTIVITY: VIDEO CAPTURE" + "\nMUST KILL THREAD", Toast.LENGTH_LONG).show(); 
	          		    	*/
                            //	return;
                        }

                        if(GlobalVariablesHolder.pauseBackgroundService == true){

                            GlobalVariablesHolder.threadUploaderIsRunning = false;
                            return;
                        }

                        //######--STEP 2 STARTS HERE
                        new AmazonProcessingTask2(
                                getApplicationContext(),
                                mediaStorage.mediaEventID,
                                mediaStorage.mediaServerId,
                                mediaStorage.videoServerFilePath,
                                mediaStorage.videoLocalFilename,
                                mediaStorage.imageServerFilePath,
                                mediaStorage.imageLocalFilename,
                                "video",
                                "",
                                "",
                                mediaStorage.userLatitude,
                                mediaStorage.userLongitude,
                                mediaStorage.mediaDateShortFormat,
                                mediaStorage.mediaDateLongFormat,
                                mediaStorage.videoLocalFilePath,
                                mediaStorage.imageLocalFilePath,
                                mediaStorage.twitterCardUrl
                        ).execute();
                    }

                } else {

                }
            }
        };
        callback.headers(Utils.getHttpHeaders( getApplicationContext() ));
        aq.ajax(url, params, JSONObject.class, callback);
    }

    public ArrayList<NextMediaID> prepareNewlyUploadedHighlightsData(String dataCombinedString){

        ArrayList<NextMediaID> arrNextMediaID = new ArrayList<NextMediaID>();
        String[] arrHighlightsData = dataCombinedString.split("\\|\\|");

        for (int i = 0; i < arrHighlightsData.length; i++) {
            try {
                JSONObject obj = new JSONObject( arrHighlightsData[i].toString() );

                NextMediaID nextMediaID = CommonFunctions_1.parseNextMediaIDList(obj);
                arrNextMediaID.add(nextMediaID);
            } catch (Throwable t) {
                //  Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"";
            }
        }
        return arrNextMediaID;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        ReportHandler.install(this, "mikitu78@gmail.com");
    }
	 
	/*
	public void setEvenIDGlobal(final int eventIdGlobal){
		this.EventID_Global = eventIdGlobal;
	}
	public int getEvenIDGlobal(){
		return this.EventID_Global;
	}
	*/

    /**
     * @category SETTERS
     * **/
    public void setVideoPath(String videoPath){
        FVideoPath = videoPath;
    }

    public String getVideoPath(){
        return FVideoPath;
    }

    ArrayList<MediaList> FNewArrMediaLists;
    ArrayList<NextMediaID> FArrNextMediaID;

    /**
     JSONObject jsonObject,
     String eventId,
     String coverImage,
     String largeImageUrl,
     String videoLocalPath,
     String imageLocalPath,
     String videoLocalFilename,
     String imageLocalFilename

     **/
    public void setNewlyUploadedHighlightsData2(
            JSONObject jsonObject,
            String eventId,
            String imageLocalFilename,
            String largeImageUrl,
            String videoLocalPath,
            String imageLocalPath,
            String videoLocalFilename ){

        JSONObject nextMediaIdObject = CommonFunctions_1.parseNextMediaIdObject(jsonObject, eventId, imageLocalFilename, largeImageUrl, videoLocalPath, imageLocalPath, videoLocalFilename);

        sharedpreferences = getSharedPreferences( "com.sportxast.SportXast.highlights", Context.MODE_PRIVATE );

        String newJSONString = "";
        if (sharedpreferences.contains( Constants.sharePrefKey_uploadedHighlights ) )
        {
            newJSONString = sharedpreferences.getString(Constants.sharePrefKey_uploadedHighlights, "");
        }

        newJSONString = newJSONString +"||"+ nextMediaIdObject.toString();
//############################################################################################ 
        if( newJSONString.substring(0, 2).equals("||") ){
            newJSONString = newJSONString.substring(2);
        }

        Editor editor = sharedpreferences.edit();
        editor.putString(Constants.sharePrefKey_uploadedHighlights, newJSONString);

        editor.commit();

        //######
			 
			 /*
			 if( GlobalVariablesHolder.currentActivityContext instanceof Profile_Activity ){ 
				 ( (Profile_Activity) GlobalVariablesHolder.currentActivityContext ).rePopulateListView();	 
   	      	 }
			 */
    }

    private SharedPreferences sharedpreferences;
    public void setNewlyUploadedHighlightsData(
            JSONObject jsonObject,
            String eventId,
            String coverImage,
            String largeImageUrl,
            String videoLocalPath,
            String imageLocalPath,
            String videoLocalFilename ){

        JSONObject nextMediaIdObject = CommonFunctions_1.parseNextMediaIdObject(jsonObject, eventId, coverImage, largeImageUrl, videoLocalPath, imageLocalPath, videoLocalFilename);

        sharedpreferences = getSharedPreferences( "com.sportxast.SportXast.highlights", this.MODE_PRIVATE );

        String newJSONString = "";
        if (sharedpreferences.contains(eventId))
        {
            newJSONString = sharedpreferences.getString(eventId, "");
        }
        newJSONString = newJSONString +"||"+ nextMediaIdObject.toString();
//################################################################################## 
        if( newJSONString.substring(0, 2).equals("||") ){
            newJSONString = newJSONString.substring(2);
        }

        Editor editor = sharedpreferences.edit();
        editor.putString(eventId, newJSONString);
	      /*editor.putString(Phone, ph);
	      editor.putString(Email, e);
	      editor.putString(Street, s);
	      editor.putString(Place, p);*/
        editor.commit();
    }

    public ArrayList<NextMediaID> getNewlyUploadedHighlightsDataORIGINAL(){
        return FArrNextMediaID;
    }
	
	/*
	public NextMediaID getNewlyUploadedHighlightsDataXXXXXXXXXXX(final String eventId){ 
		
		SharedPreferences  mPrefs =  this.getSharedPreferences( "com.sportxast.SportXast", Context.MODE_PRIVATE);
		
		Gson gson = new Gson();
        String json = mPrefs.getString(eventId, "");
        //MyObject obj = gson.fromJson(json, NextMediaID.class);
        
        NextMediaID nextMediaIdObject = gson.fromJson(json, NextMediaID.class); 
		return nextMediaIdObject; 
	}
	*/

    public void removeNewlyUploadedHighlightsData(){
        FArrNextMediaID.clear();
        FArrNextMediaID = null;
    }

    //########################################################################
    public void setCoordinate(Coordinate c) {
        coordinate = c;
    }

    public void setListComments(ArrayList<Comments> list) {
        list_comments = list;
    }

	/*
	public void setAppSetting(_AppSetting a) {
		appSetting = a;
	}
	*/
//################################################################
//################################################################

    public JSONObject JSON_appSettings_languageLabels;
    public JSONObject JSON_appSettings_settings;

    public void setAppSettings_languageLabels(JSONObject JSON_appSettings_languageLabels){
        this.JSON_appSettings_languageLabels = JSON_appSettings_languageLabels;
    }

    public String getAppSetting_languageLabels(String settingKey){
        try {
            return this.JSON_appSettings_languageLabels.getString(settingKey);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
//################################################################

    public void setAppSettings_settings(JSONObject JSON_appSettings_settings){
        this.JSON_appSettings_settings = JSON_appSettings_settings;
    }

    public String getAppSetting_settings(String settingKey){
        try {
            return this.JSON_appSettings_settings.getString(settingKey);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
//################################################################
//################################################################

    public void setNewEvent(_RecentEvent c) {
        newEvent = c;
    }

    public void setEventList(EventLists e){
        eventLists = e;
    }

    public void setGlobalEventID(String s){
        event_id = s;
    }

    /**
     * @category GETTERS
     * **/
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ArrayList<Comments> getListComments() {
        return list_comments;
    }
	/*
	public _AppSetting getAppSetting() {
		return appSetting;
	}
	*/

    public _RecentEvent getNewEvent() {
        return newEvent;
    }

    public EventLists getEventList(){
        return eventLists;
    }

    public String getGlobalEventID(){
        return event_id;
    }

    /**
     * @category STATIC CLASS
     * **/

    public static class Coordinate {
        public double latitude;
        public double longitude;
    }
	
	/*
	public void initSharedPreferences(Context context){
		sharedPreferences =  context.getSharedPreferences( "com.sportxast.SportXast", Context.MODE_PRIVATE);
	}
	*/

}

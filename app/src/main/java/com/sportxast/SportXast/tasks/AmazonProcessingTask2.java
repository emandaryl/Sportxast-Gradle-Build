package com.sportxast.SportXast.tasks;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.SportX2_Main;
import com.sportxast.SportXast.StaticVariables;
import com.sportxast.SportXast.Utils;
import com.sportxast.SportXast.activities2_0.Highlight_Activity;
import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.models.S3TaskResult;
import com.sportxast.SportXast.models._MediaQueue;
import com.sportxast.SportXast.models._MediaStorage;
import com.sportxast.SportXast.video.capture.TrimVideoUtils;


public class AmazonProcessingTask2 extends AsyncTask<Object, Void, S3TaskResult> {
    //private SyncService act;
    private AmazonS3Client s3Client;
    private String dir;
    private String basename;
    private Global_Data FGlobal_Data;
    TransferManager manager;
    private String filetype;
    //_MediaQueue mediaQueue;
    private String remoteFilePath;
    private String localFilePath;
    private String BucketMedia;
    private String contentType;

    private String mediaEventID;

    private Context context;
    //#################################################
    private String videoServerFilePath;
    private String videoLocalFilename;
    private String imageServerFilePath;
    private String imageLocalFilename;
    private String videoUploadResponseHeaders;
    private String imageUploadResponseHeaders;

    private String userLatitude;
    private String userLongitude;

    private String mediaDate;
    private String mediaDateLongFormat;

    private String mediaServerId;

    private boolean fileNotFound;

    private String videoLocalFilePath = "";
    private String imageLocalFilePath = "";
    private String twitterCardUrl = "";

    /**
     @param Context context,
     @param String mediaEventID,
     @param String mediaServerId,
     @param String videoServerFilePath,
     @param String videoLocalFilename,
     @param String imageServerFilePath,
     @param String imageLocalFilename,
     @param String filetype,
     @param String videoUploadResponseHeaders,
     @param String imageUploadResponseHeaders,
     @param String userLatitude,
     @param String userLongitude,
     @param String mediaDate,
     @param String mediaDateLongFormat
     */

    public AmazonProcessingTask2(Context context,
                                 String mediaEventID,
                                 String mediaServerId,
                                 String videoServerFilePath,
                                 String videoLocalFilename,
                                 String imageServerFilePath,
                                 String imageLocalFilename,
                                 String filetype,
                                 String videoUploadResponseHeaders,
                                 String imageUploadResponseHeaders,
                                 String userLatitude,
                                 String userLongitude,
                                 String mediaDate,
                                 String mediaDateLongFormat,
                                 String videoLocalFilePath,
                                 String imageLocalFilePath,
                                 String twitterCardUrl) {

        this.fileNotFound 			= false;

        this.mediaEventID 			= mediaEventID;
        this.mediaServerId 			= mediaServerId;

        this.videoServerFilePath 	= videoServerFilePath;
        this.videoLocalFilename	 	= videoLocalFilename;
        this.imageServerFilePath 	= imageServerFilePath;
        this.imageLocalFilename  	= imageLocalFilename;

        this.videoUploadResponseHeaders = videoUploadResponseHeaders;
        this.imageUploadResponseHeaders = imageUploadResponseHeaders;

        this.userLatitude 			= userLatitude;
        this.userLongitude			= userLongitude;
        this.mediaDate 				= mediaDate;

        this.mediaDateLongFormat 	= mediaDateLongFormat;

        this.videoLocalFilePath 	= videoLocalFilePath; 	//value = /storage/sdcard0/Sportx/video/20140703150721.mp4 <- from LOCAL
        this.imageLocalFilePath 	= imageLocalFilePath;	//value = /storage/sdcard0/Sportx/images/20140703150721.jpg
    	
    	/*
		this.videoLocalFilePath 	=  dir + "/" + videoLocalFilename; 	//value = /storage/sdcard0/Sportx/video/20140703150721.mp4 <- from LOCAL
    	this.imageLocalFilePath 	=  dir + "/" + imageLocalFilename;	//value = /storage/sdcard0/Sportx/images/20140703150721.jpg
    	*/
        this.twitterCardUrl = twitterCardUrl;
        try {
            this.context = context;
            if( FGlobal_Data == null)
                FGlobal_Data = (Global_Data) this.context.getApplicationContext();

            this.dir = TrimVideoUtils.getLocalVideoPath(context);
            remoteFilePath 	= videoServerFilePath; 				//value = db/event_media/2014/07/03/13434.mp4 <-from server
            localFilePath 	= dir + "/" + videoLocalFilename; 	//value = /storage/sdcard0/Sportx/video/20140703150721.mp4 <- from LOCAL

            File videoFile = new File(localFilePath);

            if( !videoFile.exists() )
                this.fileNotFound = true;

            this.BucketMedia= GlobalVariablesHolder.S3_VIDEO_BUCKET; //value = sportxastbeta
            contentType 	= "video/mp4";

            this.filetype = filetype;
            if (filetype.equals("picture")) {
                this.dir 		= TrimVideoUtils.getLocalImagesPath(context);
                remoteFilePath 	= imageServerFilePath;  			//value = db/event_media/2014/07/03/13434.jpg
                localFilePath 	= dir + "/" + imageLocalFilename;	//value = /storage/sdcard0/Sportx/images/20140703150721.jpg

                this.fileNotFound = false;
                if( !videoFile.exists() )
                    this.fileNotFound = true;

                this.BucketMedia 	= GlobalVariablesHolder.S3_IMAGE_BUCKET;		//value = sportxastbeta-images
                contentType 	= "image/jpeg";
            }

            /** THIS UPLOADS THE MEDIA TO AMAZON S3 **/
            s3Client = new AmazonS3Client( new BasicAWSCredentials(StaticVariables.AWS_ACCESS_KEY_ID, StaticVariables.AWS_SECRET_KEY) );
            manager = new TransferManager(s3Client);

        } catch (Exception e) {
            String xx = "";
            String x = xx;
        }
    }

    protected S3TaskResult doInBackground(Object... params) {
        if( this.fileNotFound ){
            return null;
        }

        S3TaskResult result = new S3TaskResult();

        UploadResult uploadResult = null;
        try {
            File f = new java.io.File(localFilePath);
            if (this.BucketMedia == null) {
                Log.e("Amazon", "bucket is NULL");
            }
            if (remoteFilePath == null) {
                Log.e("Amazon", "remoteFilePath is NULL");
            }
            if (f == null) {
                Log.e("Amazon", "f is NULL");
            }

            PutObjectRequest por = new PutObjectRequest(this.BucketMedia, remoteFilePath, f);
            Upload upload 		 = manager.upload(por);
            uploadResult 		 = upload.waitForUploadResult();

            String heyx = uploadResult.toString();

            result.setUploadresponse(uploadResult.toString());
//			s3Client.putObject(por);
        } catch (Exception exception) {
            exception.printStackTrace();
            result.setErrorMessage(exception.getMessage());
        }
        try {
            // Ensure that the image will be treated as such.
            ResponseHeaderOverrides override = new ResponseHeaderOverrides();
            override.setContentType(contentType);

            // Generate the presigned URL.
            // Added an hour's worth of milliseconds to the current time.
            Date expirationDate = new Date( System.currentTimeMillis() + 365 * 3600000 );
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( this.BucketMedia, remoteFilePath);
            urlRequest.setExpiration(expirationDate);
            urlRequest.setResponseHeaders(override);
            URL url = s3Client.generatePresignedUrl(urlRequest);
            result.setUri(Uri.parse(url.toURI().toString()));

            String uploadedS3Filename = "";
            if (uploadResult != null)
            {
                uploadedS3Filename = uploadResult.getKey();
                Log.e("AMAZON", "getBucketName: " + uploadResult.getBucketName());
                Log.e("AMAZON", "File uploaded with key: " + uploadedS3Filename);
            }
            else {
                Log.e("AMAZON", "uploadResult IS NULLLLLLLLLLL: ");
                return null;
            }

            if (this.filetype.equals("picture")) {
				/*
				"{
				\"x-amz-request-id\":\"DB97CA8B9F968344\",
				  \"Etag\":\"\\\"fc9920651a9d2402b370986b3e26cac8\\\"\",
				  \"x-amz-id-2\":\"v11kfJ9A\\\/9uCLTtGQWYXg\\\/7fFI2jEDK\\\/vNPuUOrxX9WdBM15wAp5vCLdbvUnpqDKvmUlQRkWnLA=\",
				  \"Content-Length\":\"0\",
				  \"Server\":\"AmazonS3\",
				  \"Date\":\"Thu, 31 Jul 2014 01:33:56 GMT\"
				  }", 
				 */
                //this.imageUploadResponseHeaders = uploadResult.toString();
                this.imageUploadResponseHeaders = responseHeaderJSONStr(uploadResult, this.mediaDateLongFormat);

            } else { //if video
                //this.videoUploadResponseHeaders = uploadResult.toString();
                this.videoUploadResponseHeaders = responseHeaderJSONStr(uploadResult, this.mediaDateLongFormat);
            }

            //Utils.saveQueue(act, mediaQueue);
        } catch (Exception exception) {

            exception.printStackTrace();
            result.setErrorMessage(exception.getMessage());

            Log.e("AmazonProcessingTask:", "ERROR77777##########################");
            Log.e("AmazonProcessingTask:", "ERROR77777##########################");
            Log.e("AmazonProcessingTask:", "ERROR77777##########################");
            Log.e("AmazonProcessingTask:", "ERROR77777##########################");
			/*
			if( this.context instanceof SportX2_Main ){
				((SportX2_Main) context).onMediaUploadComplete("ERROR ERROR ERROR UPLOAD");
			}
			*/

        }
        return result;
    }

    private String responseHeaderJSONStr(UploadResult uploadResult, String date){
		/*
		"{
		\"x-amz-request-id\":\"DB97CA8B9F968344\",
		  \"Etag\":\"\\\"fc9920651a9d2402b370986b3e26cac8\\\"\",
		  \"x-amz-id-2\":\"v11kfJ9A\\\/9uCLTtGQWYXg\\\/7fFI2jEDK\\\/vNPuUOrxX9WdBM15wAp5vCLdbvUnpqDKvmUlQRkWnLA=\",
		  \"Content-Length\":\"0\",
		  \"Server\":\"AmazonS3\",
		  \"Date\":\"Thu, 31 Jul 2014 01:33:56 GMT\"
		  }", 
		 */

        JSONObject JSONHeader = new JSONObject();
        try {
            JSONHeader.put("x-amz-request-id", 	"");
            JSONHeader.put("Etag", 				uploadResult.getETag() );
            JSONHeader.put("x-amz-id-2",		"");
            JSONHeader.put("Content-Length",	"0");
            JSONHeader.put("Server", 			"AmazonS3");
            JSONHeader.put("Date", 				date);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONHeader.toString();
    }

    protected void onPostExecute(S3TaskResult result) {

        if( result == null ){//ERROR ERROR ERROR
            return;
        }

        if( result.getErrorMessage() != null ) {
//			Log.e("onPostExecute Error",result.getErrorMessage()); "*************************************************************"); 
            //((SportX2_Main) context).onMediaUploadComplete("ERROR ERROR ERROR UPLOAD");

        } else if (result.getUri() != null) {
            // Display in Browser.
			/*
			Log.e("AMAZON", "S3 " + filetype + " Uri: " + result.getUri());
			Log.e("AmazonProcessingTask:", "SUCCESS5555##########################");   */
            if( filetype.equals("picture") ){

                Log.e("AmazonProcessingTask:", "PICTURE PICTURE PICTURE PICTURE PICTURE");
                Log.e("AmazonProcessingTask:", "PICTURE PICTURE PICTURE PICTURE PICTURE");
                Log.e("AmazonProcessingTask:", "PICTURE PICTURE PICTURE PICTURE PICTURE");

                /**
                 * @param context
                 * @param videoServerFilePath value = db/event_media/2014/07/03/13434.mp4 <-from server
                 * @param videoLocalFilename  value = 20140703150721.mp4 <- from LOCAL
                 * @param imageServerFilePath value = db/event_media/2014/07/03/13434.jpg
                 * @param imageLocalFilename  value = 20140703150721.jpg
                 * @param filetype
                 */

                _MediaStorage mediaStorage 		= new _MediaStorage();
                mediaStorage.ID					= "0";
                mediaStorage.imageLocalFilename	= this.imageLocalFilename;

                mediaStorage.imageLocalFilePath = this.imageLocalFilePath;
                mediaStorage.videoLocalFilePath	= this.videoLocalFilePath;

                mediaStorage.videoServerFilePath = this.videoServerFilePath;
                mediaStorage.imageServerFilePath = this.imageServerFilePath;

                mediaStorage.imageUploadResponseHeaders = this.imageUploadResponseHeaders;
                mediaStorage.inQueue	 		= "0";
                mediaStorage.isDeletedByUser	= "0";
                mediaStorage.isFavorite 		= "0";
                mediaStorage.isVideoUploaded 	= "1";
                mediaStorage.isImageUploaded 	= "1";
                mediaStorage.isUploaded 		= "1";

                mediaStorage.isSharedOnFacebook = "0";
                mediaStorage.isSharedOnMail 	= "0";
                mediaStorage.isSharedOnSms 		= "0";
                mediaStorage.isSharedOnTwitter 	= "0";

                mediaStorage.mediaAspectRatio 	= "";
                mediaStorage.mediaComment 		= "";

                mediaStorage.mediaDateShortFormat	= this.mediaDate;
                mediaStorage.mediaDateLongFormat	= this.mediaDateLongFormat;
                mediaStorage.mediaEventID 		= this.mediaEventID;
                mediaStorage.mediaFileKey 		= "";
                mediaStorage.mediaOrientation 	= "0";
                mediaStorage.mediaServerId 		= this.mediaServerId;
                mediaStorage.mediaSessionID 	= "";
                mediaStorage.mediaShareUrl 		= "";
                mediaStorage.mediaTags 			= "";
                mediaStorage.shareText 			= "";
                mediaStorage.userLatitude 		= this.userLatitude;
                mediaStorage.userLongitude  	= this.userLongitude;
                mediaStorage.videoLocalFilename	= this.videoLocalFilename;
                mediaStorage.videoUploadResponseHeaders = this.videoUploadResponseHeaders;
                mediaStorage.modified  			= "1";
                mediaStorage.isSaved 			= "0";

                mediaStorage.twitterCardUrl		= this.twitterCardUrl;

                JSONObject newFirstHighlightJSONObject = CommonFunctions_1.reformHighlightJSONObject(mediaStorage, "3");
                CommonFunctions_1.updateSharedPreferencesQueueing(this.context, newFirstHighlightJSONObject);
                if( GlobalVariablesHolder.currentActivityContext instanceof VideoCaptureActivity ){

                    //PAUSE THE UPLOADING
                    //PAUSE THE UPLOADING
                    //PAUSE THE UPLOADING
					/*
      	    	    //---STOP THE HANDLER---
      	    	    killRunnable(); 
      		    	Toast.makeText(context, "FINISHED STEP 3: uploaded PICTURE/IMAGE\n"+ "CURRENT ACTIVITY: VIDEO CAPTURE" + "\nMUST KILL THREAD", Toast.LENGTH_LONG).show(); 
      		    	*/
                    //return;
                }

                if(GlobalVariablesHolder.pauseBackgroundService == true){
                    GlobalVariablesHolder.threadUploaderIsRunning = false;
                    return;
                }

                //######--STEP 4 STARTS HERE
                FGlobal_Data.saveMediaInfo( mediaStorage, 1 );
            }
            else{
                _MediaStorage mediaStorage 		= new _MediaStorage();
                mediaStorage.ID = "0";
                mediaStorage.uploadQueue			= "2";
                mediaStorage.imageLocalFilename 	= this.imageLocalFilename; //local filename of image
                mediaStorage.videoLocalFilename	= this.videoLocalFilename; //local filename of Video

                mediaStorage.videoServerFilePath 	= this.videoServerFilePath;
                mediaStorage.imageServerFilePath 	= this.imageServerFilePath; //server image path

                mediaStorage.videoLocalFilePath	= this.videoLocalFilePath;
                mediaStorage.imageLocalFilePath 	= this.imageLocalFilePath;

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

                mediaStorage.mediaDateShortFormat	= this.mediaDate;//format should be "2014-07-04 10:35:06"
                mediaStorage.mediaDateLongFormat	= this.mediaDateLongFormat;//format should be "Fri, 04 Jul 2014 02:35:12 GMT"

                mediaStorage.mediaEventID 			= this.mediaEventID;
                mediaStorage.mediaFileKey 			= "";
                mediaStorage.mediaOrientation 		= "0";

                mediaStorage.mediaServerId 		= this.mediaServerId;
                mediaStorage.mediaSessionID 		= "";
                // mediaStorage.mediaShareUrl 	 	= arrNextMediaID.get(0).getShareUrl();
                mediaStorage.mediaShareUrl 	 	= "";
                mediaStorage.mediaTags 			= "";
                mediaStorage.shareText 			= "";
                mediaStorage.userLatitude 			= this.userLatitude;
                mediaStorage.userLongitude  		= this.userLongitude;

                mediaStorage.modified  			= "1";
                mediaStorage.isSaved 				= "0";

                mediaStorage.twitterCardUrl		= this.twitterCardUrl;

                JSONObject newFirstHighlightJSONObject = CommonFunctions_1.reformHighlightJSONObject(mediaStorage, "2");
                CommonFunctions_1.updateSharedPreferencesQueueing(this.context, newFirstHighlightJSONObject);
                if( GlobalVariablesHolder.currentActivityContext instanceof VideoCaptureActivity ){

                    //PAUSE THE UPLOADING
                    //PAUSE THE UPLOADING
                    //PAUSE THE UPLOADING
					/*
      	    	    //---STOP THE HANDLER---
      	    	    killRunnable(); 
      		    	Toast.makeText(context, "FINISHED STEP 2: uploaded PICTURE/IMAGE\n"+ "CURRENT ACTIVITY: VIDEO CAPTURE" + "\nMUST KILL THREAD", Toast.LENGTH_LONG).show(); 
      		    	*/
                    //return;
                }
                if(GlobalVariablesHolder.pauseBackgroundService == true){
                    GlobalVariablesHolder.threadUploaderIsRunning = false;
                    return;
                }

                Log.e("AmazonProcessingTask:", "VIDEO VIDEO VIDEO VIDEO VIDEO");
                Log.e("AmazonProcessingTask:", "VIDEO VIDEO VIDEO VIDEO VIDEO");
                Log.e("AmazonProcessingTask:", "VIDEO VIDEO VIDEO VIDEO VIDEO");


                //######--STEP 3 STARTS HERE
                new AmazonProcessingTask2(
                        this.context,
                        this.mediaEventID,
                        this.mediaServerId,
                        this.videoServerFilePath,
                        this.videoLocalFilename,
                        this.imageServerFilePath,
                        this.imageLocalFilename,
                        "picture",
                        this.videoUploadResponseHeaders,
                        this.imageUploadResponseHeaders,
                        this.userLatitude,
                        this.userLongitude,
                        this.mediaDate,
                        this.mediaDateLongFormat,
                        this.videoLocalFilePath,
                        this.imageLocalFilePath,
                        this.twitterCardUrl ).execute();
            }

        }
    }
}
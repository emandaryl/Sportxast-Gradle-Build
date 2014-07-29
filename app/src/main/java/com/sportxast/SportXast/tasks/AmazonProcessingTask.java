package com.sportxast.SportXast.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.StaticVariables;
import com.sportxast.SportXast.Utils;
import com.sportxast.SportXast.models.S3TaskResult;
import com.sportxast.SportXast.models._MediaQueue;
import com.sportxast.SportXast.video.capture.TrimVideoUtils;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class AmazonProcessingTask extends AsyncTask<Object, Void, S3TaskResult> {
    private SyncService act;
	private AmazonS3Client s3Client;
	private String dir;
	private String basename;
	Global_Data FGlobal_Data;
	TransferManager manager;
	private String filetype;
	_MediaQueue mediaQueue;
	private String remoteFilePath;
	private String localFilePath;
	private String bucket;
	private String contentType;
	
	public AmazonProcessingTask(SyncService syncService, _MediaQueue mediaQueue, String filetype) {
//		 	Log.e("AMAZON", "**************************INIT***********************************");
//			Log.e("AMAZON", "**************************" + syncService.toString() + "***********************************");
//			Log.e("AMAZON", "**************************" + mediaQueue.toString() + "***********************************");
//			Log.e("AMAZON", "**************************" + filetype + "***********************************");
//			Log.e("AMAZON", "**************************MEDIA QUEUE ID: " + mediaQueue.ID + "***********************************");
			try {
		 	this.act 		= syncService;
	        FGlobal_Data 	= (Global_Data)syncService.getApplicationContext();
		 	
	        this.dir = TrimVideoUtils.getLocalVideoPath(syncService.getApplicationContext());
        	remoteFilePath 	= mediaQueue.videoFilePath; 			//value = db/event_media/2014/07/03/13434.mp4 <-from server
	        localFilePath 	= dir + "/" + mediaQueue.videoFileName; //value = /storage/sdcard0/Sportx/video/20140703150721.mp4 <- from LOCAL
			bucket 			= FGlobal_Data.getAppSetting_settings("S3_VIDEO_BUCKET");;// value = sportxastbeta
			contentType 	= "video/mp4";

			this.filetype = filetype;
//			Log.e ("AMAZON", "Filetype: " + this.filetype); 
			
	        if (filetype.equals("picture")) {
	        	this.dir 		= TrimVideoUtils.getLocalImagesPath(syncService.getApplicationContext());
	        	remoteFilePath 	= mediaQueue.imageFilePath;//value = db/event_media/2014/07/03/13434.jpg
		        localFilePath 	= dir + "/" + mediaQueue.imageFileName;// value = /storage/sdcard0/Sportx/images/20140703150721.jpg
				bucket 			= FGlobal_Data.getAppSetting_settings("S3_IMAGE_BUCKET");;//value = sportxastbeta-images
				contentType 	= "image/jpeg";
	        }
	          
//			Log.e("AmazonProcessingTask", "remoteFilePath=" + remoteFilePath);
//			Log.e("AmazonProcessingTask", "localFilePath=" + localFilePath);

	        this.mediaQueue = mediaQueue;
			mediaQueue.inQueue = "1";
			Utils.saveQueue(act, mediaQueue);
			
			/** THIS UPLOADS THE MEDIA TO AMAZON S3 **/
	        s3Client = new AmazonS3Client( new BasicAWSCredentials(StaticVariables.AWS_ACCESS_KEY_ID, StaticVariables.AWS_SECRET_KEY));
	        
	        manager = new TransferManager(s3Client);
	        
			} catch (Exception e) {
				
			}
	    }
	
	protected S3TaskResult doInBackground(Object... params) {
		S3TaskResult result = new S3TaskResult();
//		Log.e("AMAZON", "**************************LOCAL***********************************");
//		Log.e("AMAZON", "**************************" + localFilePath + "***********************************");
//		Log.e("AMAZON", "**************************REMOTE***********************************");
//		Log.e("AMAZON", "**************************" + remoteFilePath + "***********************************");
		
		UploadResult uploadResult = null;
		
		try { 
			File f = new java.io.File(localFilePath);
			if (bucket == null) {
				Log.e("Amazon", "bucket is NULL");
			}
			if (remoteFilePath == null) {
				Log.e("Amazon", "remoteFilePath is NULL");
			}
			if (f == null) {
				Log.e("Amazon", "f is NULL");
			}
			PutObjectRequest por = new PutObjectRequest(bucket, remoteFilePath, f);
			Upload upload 	= manager.upload(por);
			uploadResult 	= upload.waitForUploadResult();
			
//			while (!upload.isDone()) {
//			    Thread.sleep(200);
//			}
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
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( bucket, remoteFilePath);
			urlRequest.setExpiration(expirationDate);
			urlRequest.setResponseHeaders(override);
			URL url = s3Client.generatePresignedUrl(urlRequest);
			result.setUri(Uri.parse(url.toURI().toString()));
			
//			Log.e("AMAZON", "**************************AMAZON RESPONSE HEADERS***********************************");
//			Log.e("AMAZON", "**************************" + uploadResult.toString() + "***********************************");
            String uploadedS3Filename = "";
			if (uploadResult != null)
            {
				uploadedS3Filename = uploadResult.getKey();
                 Log.e("AMAZON", "getBucketName: " + uploadResult.getBucketName());
                 Log.e("AMAZON", "File uploaded with key: " + uploadedS3Filename);
             }
			else {
                Log.e("AMAZON", "uploadResult IS NULLLLLLLLLLL: ");
			}

			if (this.filetype.equals("picture")) {
				mediaQueue.isImageUploaded = "1";
				mediaQueue.videoUploadResponseHeaders = uploadResult.toString();
			} else {
				mediaQueue.isVideoUploaded = "1";
				mediaQueue.imageUploadResponseHeaders = uploadResult.toString();
			}
			mediaQueue.inQueue = "0";
			Utils.saveQueue(act, mediaQueue);
		} catch (Exception exception) {
			
			exception.printStackTrace();
			result.setErrorMessage(exception.getMessage());
			
			Log.e("AmazonProcessingTask:", "ERROR77777##########################");
			Log.e("AmazonProcessingTask:", "ERROR77777##########################");
			Log.e("AmazonProcessingTask:", "ERROR77777##########################");
			Log.e("AmazonProcessingTask:", "ERROR77777##########################"); 
		}
		
		return result;
    }
    protected void onPostExecute(S3TaskResult result) {
    	if (result.getErrorMessage() != null) {
//			Log.e("onPostExecute Error",result.getErrorMessage());
//			Log.e("AMAZON", "**************************AppSetting***********************************");
//			Log.e("AMAZON", global_Data.getAppSetting().settings.toString());
//			Log.e("AMAZON", "*************************************************************");
//			Log.e("AMAZON", "**************************AppSetting.S3_IMAGE_BUCKET***********************************");
//			Log.e("AMAZON", global_Data.getAppSetting().settings._S3_IMAGE_BUCKET);
//			Log.e("AMAZON", "*************************************************************");
		} else if (result.getUri() != null) {
			// Display in Browser.
			Log.e("AMAZON", "S3 " + filetype + " Uri: " + result.getUri());
			
			Log.e("AmazonProcessingTask:", "SUCCESS5555##########################");
			Log.e("AmazonProcessingTask:", "SUCCESS5555##########################");
			Log.e("AmazonProcessingTask:", "SUCCESS5555##########################");
			Log.e("AmazonProcessingTask:", "SUCCESS5555##########################");
			
		}
    }
}
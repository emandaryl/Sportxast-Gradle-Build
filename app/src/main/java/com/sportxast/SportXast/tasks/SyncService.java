package com.sportxast.SportXast.tasks;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.sportxast.SportXast.Utils;
import com.sportxast.SportXast.localdatabase.DatabaseHelper;
import com.sportxast.SportXast.localdatabase.Keys.KEY_MEDIA_QUEUE;
import com.sportxast.SportXast.models._MediaQueue;
import com.sportxast.SportXast.models._MediaStorage;
import com.sportxast.SportXast.video.capture.TrimVideoUtils;

import java.util.ArrayList;

public class SyncService extends IntentService {
    private DatabaseHelper DB;

	public SyncService() {
        super("SyncService");
     }

     @Override
     protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("SyncService", "SERVICE RUNNING");
        Log.i("SyncService", "SERVICE RUNNING");
        Log.i("SyncService", "SERVICE RUNNING");
        Log.i("SyncService", "SERVICE RUNNING");
        DB = new DatabaseHelper(this);
        this.checkNextMediaServerId();
        this.sync();
        this.getQueue();
        this.syncSave();
     }

	private void checkNextMediaServerId() {
		ArrayList<ContentValues> arrayList = DB.getAllItemForNextMediaId();
    	Log.e("checkNextMediaServerId", "found " + arrayList.size()); 
        for (ContentValues item: arrayList){
        	_MediaStorage mediaStorage = DB.cv2mediaStorage(item);
        	DB.showMediaStorageField(item);
        	Log.e("checkNextMediaServerId", "mediaStorage: Id=>" + mediaStorage.ID); 
       		TrimVideoUtils.getNextMediaId(getApplicationContext(), mediaStorage);
        }
	}

	private void getQueue() {
		ArrayList<ContentValues> arrayList = DB.getQueue();
				
        _MediaQueue mediaQueue;
        Log.e("SYNC", "**********************IN_QUEUE*************************");
        Log.e("SYNC", "********************** " + arrayList.size() + "*************************");
        for (ContentValues item: arrayList){
        	mediaQueue = new _MediaQueue();
        	mediaQueue.ID = item.getAsString(KEY_MEDIA_QUEUE.ID);
        	mediaQueue.imageFileName = item.getAsString(KEY_MEDIA_QUEUE.IMAGE_FILE_NAME);
        	mediaQueue.imageFilePath = item.getAsString(KEY_MEDIA_QUEUE.IMAGE_FILE_PATH);
        	mediaQueue.imageUploadResponseHeaders = item.getAsString(KEY_MEDIA_QUEUE.IMAGE_UPLOAD_RESPONSE_HEADERS);
        	mediaQueue.isImageUploaded = item.getAsString(KEY_MEDIA_QUEUE.IS_IMAGE_UPLOADED);
        	mediaQueue.isVideoUploaded = item.getAsString(KEY_MEDIA_QUEUE.IS_VIDEO_UPLOADED);
        	mediaQueue.isUploaded = item.getAsString(KEY_MEDIA_QUEUE.IS_UPLOADED);
        	mediaQueue.mediaFileKey = item.getAsString(KEY_MEDIA_QUEUE.MEDIA_FILE_KEY);
        	mediaQueue.mediaServerId = item.getAsString(KEY_MEDIA_QUEUE.MEDIA_SERVER_ID);
        	mediaQueue.mediaServerIdFailed = item.getAsString(KEY_MEDIA_QUEUE.MEDIA_SERVER_ID_FAILED);
        	mediaQueue.videoFileName = item.getAsString(KEY_MEDIA_QUEUE.VIDEO_FILE_NAME);
        	mediaQueue.videoFilePath = item.getAsString(KEY_MEDIA_QUEUE.VIDEO_FILE_PATH);
        	mediaQueue.videoUploadResponseHeaders = item.getAsString(KEY_MEDIA_QUEUE.VIDEO_UPLOAD_RESPONSE_HEADERS);
        	Log.e("SYNC", "**********************isImageUploaded*************************");
        	Log.e("SYNC", "******************* " + mediaQueue.isImageUploaded + "***************************");
        	Log.e("SYNC", "**********************isVideoUploaded*************************");
        	Log.e("SYNC", "******************* " + mediaQueue.isVideoUploaded + "***************************");
        	Log.e("SYNC", "***********************************************");
        	mediaQueue.inQueue = "1";
        	Utils.saveQueue(getApplicationContext(), mediaQueue);

        	if (mediaQueue.isImageUploaded.equals("1") && mediaQueue.isVideoUploaded.equals("1")) {
        		mediaQueue.isUploaded = "1";
        		mediaQueue.inQueue = "0";
        		Log.e("SYNC", "***********************************************");
            	Log.e("SYNC", "******************* " + KEY_MEDIA_QUEUE.ID + "***************************");
            	Log.e("SYNC", "***********************************************");
    			Utils.saveQueue(getApplicationContext(), mediaQueue);
        	}
			if (mediaQueue.isImageUploaded.equals("0")) {
        		this.uploadImage(mediaQueue);
        	}
        	if (mediaQueue.isVideoUploaded.equals("0")) {
        		this.uploadVideo(mediaQueue);
        	}
        }
		
	}

	private void uploadVideo(_MediaQueue mediaQueue) {
		new AmazonProcessingTask(this, mediaQueue, "video").execute();
		Log.e("SYNC", "***********************************************");
    	Log.e("SYNC", "******************* uploadVideo ***************************");
    	Log.e("SYNC", "***********************************************");		
	}

	private void uploadImage(_MediaQueue mediaQueue) {
		new AmazonProcessingTask(this, mediaQueue, "picture").execute();
    	Log.e("SYNC", "***********************************************");
    	Log.e("SYNC", "******************* uploadImage ***************************");
    	Log.e("SYNC", "***********************************************");		
	}
	
	private void sync() {
		Log.e("SYNCSAVE1", "***MEDIAINFO********");
    	Log.e("SYNCSAVE1", "***MEDIAINFO********");
    	Log.e("SYNCSAVE1", "***MEDIAINFO********");		
		
		ArrayList<ContentValues> arrayList = DB.getAllModified(); 
        for (ContentValues item: arrayList){
        	Log.e("SYNCSAVE1A", "***MEDIAINFO ARRAY********");	
	    	_MediaStorage mediaStorage = DB.cv2mediaStorage(item);
	    	Utils.updateMediaInfo(getApplicationContext(), mediaStorage);
        }
	}

	private void syncSave() { 
		Log.e("SYNCSAVE", "***MEDIAINFO********");
    	Log.e("SYNCSAVE", "***MEDIAINFO********");
    	Log.e("SYNCSAVE", "***MEDIAINFO********");		
		ArrayList<ContentValues> arrayList = DB.getAllReadyToSave();
        for (ContentValues item: arrayList){
        	Log.e("SYNCSAVE", "***MEDIAINFO ARRAY********");	
        	_MediaStorage mediaStorage = DB.cv2mediaStorage(item);
       		Utils.saveMediaInfo(getApplicationContext(), mediaStorage);
        }
	}
}

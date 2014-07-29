package com.sportxast.SportXast.tasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.sportxast.SportXast.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TaskDownloadAndPostToFb extends AsyncTask<Void, Integer, Integer> {

	private String urlPath;
	private File videoFile;
	private String caption;
	private String mediaId;
	private static final String TAG = "TaskDownloadAndPostToFb";
	private static final int TIMEOUT_CONNECTION = 5000;
	private static final int TIMEOUT_SOCKET = 30000;
	
	private NotificationManager notifManager;
	private NotificationCompat.Builder notifBuilder;
//	private static final int NOTIF_ID = 23;
	public static final String EXTRA_NOTIF = "notif_media";
//	public static boolean isCancelled;
	
	private Context context;
	
	public TaskDownloadAndPostToFb(Context context, File videoFile, String urlPath, String caption, String mediaId) {
		this.videoFile = videoFile;
		this.urlPath = urlPath;
		this.caption = caption;
		this.context = context;
		this.mediaId = mediaId;
		
		notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifBuilder = new NotificationCompat.Builder(context);
	}
	
	@Override
	public Integer doInBackground(Void...voids) {
		
		notifManager.notify(Integer.parseInt(mediaId), buildingNotification());
		
		if(this.videoFile.exists()) {
			Log.i(TAG, "doInbackground file already exists");
		} else {
			downloadVideoFromUrl(videoFile, urlPath);
			Log.i(TAG, "getting video from url");
		}

		return 1;
	}
	
	@Override
	public void onPostExecute(Integer result) {
		Session session = Session.getActiveSession();
		
		if(session != null) {
			Log.i(TAG, "session for posting not null");
			if(this.videoFile.exists()) {
				Log.i(TAG, "video file exists " + this.videoFile.getPath());
				try {
					Request request = Request.newUploadVideoRequest(session, this.videoFile, requestCallback);
					/*
					 * Always get request.getParameters() 
					 * for Bundle object or Facebook
					 * will throw an exception.  
					 */
					Bundle params = request.getParameters();
//					params.putString("title", this.caption);
					params.putString("description", this.caption);
					request.setParameters(params);
					
					request.executeAsync();
					/*
					 * You can use the one below.
					 */
//					RequestAsyncTask taskRequest = new RequestAsyncTask(request);
//					taskRequest.execute();
				} catch (FileNotFoundException e) {
					Log.e(TAG, "Facebook uploading " + e.toString());
					e.printStackTrace();
				}
			} else {
				Log.i(TAG, "video file MISSING");
			}
		}
	}
	
	private Request.Callback requestCallback = new Request.Callback() {
		
		@Override
		public void onCompleted(Response response) {
			/*
			 * Notification should finish here too.
			 */
			if(response.getError() == null) {
				Log.i(TAG, "Video Share SUCCESS");
				Toast.makeText(context, "Video uploaded to Facebook.", Toast.LENGTH_SHORT).show();
			} else {
				Log.i(TAG, "Video Share FAIL " + response);
				Toast.makeText(context, "Video uploading failed.", Toast.LENGTH_SHORT).show();
			}
			
			notifManager.cancel(Integer.parseInt(mediaId));
		}
	};
	
	private void downloadVideoFromUrl(File videoFile, String urlPath) {
//		Log.i(TAG, "url path: " + urlPath);
//		Log.i(TAG, "video file: " + videoFile.getPath());
		try {
			URL urlVideo = new URL(urlPath);
			URLConnection urlConn = urlVideo.openConnection();
			urlConn.setReadTimeout(TIMEOUT_CONNECTION);
			urlConn.setConnectTimeout(TIMEOUT_SOCKET);
			
			InputStream is = urlConn.getInputStream();
			BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
			
			FileOutputStream outStream = new FileOutputStream(videoFile);
			
			byte[] buffer = new byte[5 * 1024];
			int len;
			while((len = inStream.read(buffer)) != -1) {
				Log.v(TAG, "len: " + len);
				outStream.write(buffer, 0, len);
			}
			
			outStream.flush();
			outStream.close();
			inStream.close();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}
	
	private Notification buildingNotification() {
//		Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
//		intent.putExtra(EXTRA_NOTIF, Integer.parseInt(this.mediaId));
		
		// getBroadcast() flags temporary 0
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), NOTIF_ID, intent, 0);
		
		notifBuilder.setContentTitle("SportXast");
		notifBuilder.setContentText("Uploading Video to Facebook");
		notifBuilder.setSmallIcon(R.drawable.ic_launcher);
		notifBuilder.setProgress(0, 0, true);
//		notifBuilder.setDeleteIntent(pendingIntent);
		
		Notification notif = notifBuilder.build();
		notif.flags = Notification.FLAG_ONGOING_EVENT;
		
		return notif;
	}
}

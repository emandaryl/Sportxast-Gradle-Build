package com.sportxast.SportXast.tasks;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.VideoColumns;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.coremedia.iso.IsoFile;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.video.capture.CaptureListener;
import com.sportxast.SportXast.video.capture.TrimVideoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProcessingTask extends AsyncTask<Object, Integer, Integer> {
    private Activity act;
    NotificationCompat.Builder builder;
	public final static String ACTION = "SOSServiceAction"; 
    protected String dir; 
	protected String file = "tmp_rec_tmp.mp4";
	protected String tmpfile;
	protected File iFile;
	protected File oFile;
	protected int cutlength = 3000; // milliseconds
	CaptureListener vcListner;
	Global_Data global_Data;
	private String mediaId;
	private double sampleLength;
	private double startTime;
	private double endTime;
	
	 public ProcessingTask(Activity act, CaptureListener vcListner, String mediaId) {
		 this.act = act;
	        this.vcListner = vcListner;
	        this.mediaId = mediaId;
	        global_Data = (Global_Data)act.getApplicationContext();
	    } 
	protected Integer doInBackground(Object... params) {
   		try {
			getSample();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;

    }
    protected void onPostExecute(Long result) {
    	
    }
  
    public void getSample() throws IOException 
    {
    	dir = TrimVideoUtils.getLocalTmpPath(act) + "/";
		iFile  = new File(dir + file);
	    IsoFile isoFile = new IsoFile(dir + file);
        double length = (double)
                isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
        isoFile.close();
		System.currentTimeMillis();
		try {
//			sampleLength = Double.parseDouble(global_Data.getAppSetting().settings._MOVIE_DURATION);
			sampleLength = 8.0; // static at the appsetting api, this will throw errors.
			startTime = length - sampleLength ;
			if (startTime < 0) startTime = 0;
			System.out.println("*********************************");
			System.out.println("length:" + length + " | startTime:" + startTime + " | sampleLength:" + sampleLength);
			System.out.println("*********************************");
	    	
			try {
	//			getSnapshot();
				startProcessing();
			} catch (Exception e) {
				e.printStackTrace();
			}    	
		} catch (Exception e) {
			e.printStackTrace();
			// Temporary. Thread error during JUnit test.
//			Toast.makeText(act, "Application loast settings", Toast.LENGTH_SHORT).show();
			act.finish();
		}
    }

    private void startProcessing() throws IOException  {
	    System.out.println("Start processing");
	    String outputFile = mediaId + ".mp4";
		outputFile = TrimVideoUtils.getLocalVideoPath(act) + "/" + outputFile ;
	    System.out.println("outputFile: " + outputFile);
//	    createNotification(); 
		endTime = startTime + sampleLength;
		File dest = new File(outputFile);
		TrimVideoUtils.startTrim(iFile, dest , (int) startTime * 1000, (int)endTime * 1000);
        insertContent(dest);
		iFile.delete();
//		mNotificationManager.cancel(NOTIFICATION_ID); 
		vcListner.videoProcessComplete(dir, mediaId);
		
		
		Log.e("ZZZZZZVideoPath", global_Data.getVideoPath() );
		Log.e("ZZZZZZVideoPath", global_Data.getVideoPath() );
		Log.e("ZZZZZZVideoPath", global_Data.getVideoPath() );

	   // oFile.delete();
	}
    
	public void copyFile() throws IOException {
		iFile  = new File(dir + file);
		oFile  = File.createTempFile("prefix",null,new File(dir));
		String filename = oFile.getName();
		tmpfile = dir + filename;
		System.out.println("filename=" + filename);
		copy(iFile, oFile);
	}

	public void copy(File src, File dst) throws IOException {
		System.out.println("Start copy file");
		InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	    System.out.println("End copy file");
	}
	private Uri insertContent(File file) {
        long nowInMs = System.currentTimeMillis();
        long nowInSec = nowInMs / 1000;
        final ContentValues values = new ContentValues(12);
        values.put(Video.Media.TITLE, "Movie title test");
        values.put(Video.Media.DISPLAY_NAME, file.getName());
        values.put(Video.Media.MIME_TYPE, "video/mp4");
        values.put(Video.Media.DATE_TAKEN, nowInMs);
        values.put(Video.Media.DATE_MODIFIED, nowInSec);
        values.put(Video.Media.DATE_ADDED, nowInSec);
        values.put(Video.Media.DATA, file.getAbsolutePath());
        values.put(Video.Media.SIZE, file.length());
        // Copy the data taken and location info from src.
        String[] projection = new String[] {
                VideoColumns.DATE_TAKEN,
                VideoColumns.LATITUDE,
                VideoColumns.LONGITUDE,
                VideoColumns.RESOLUTION,
        };

        // Copy some info from the source file.
        querySource(projection, new ContentResolverQueryCallback() {
            @Override
            public void onCursorResult(Cursor cursor) {
                long timeTaken = cursor.getLong(0);
                if (timeTaken > 0) {
                    values.put(Video.Media.DATE_TAKEN, timeTaken);
                }
                double latitude = cursor.getDouble(1);
                double longitude = cursor.getDouble(2);
                // TODO: Change || to && after the default location issue is
                // fixed.
                if ((latitude != 0f) || (longitude != 0f)) {
                    values.put(Video.Media.LATITUDE, latitude);
                    values.put(Video.Media.LONGITUDE, longitude);
                }
                values.put(Video.Media.RESOLUTION, cursor.getString(3));

            }
        });

        return act.getContentResolver().insert(Video.Media.EXTERNAL_CONTENT_URI, values);
    }
	
	 private interface ContentResolverQueryCallback {
	        void onCursorResult(Cursor cursor);
	    }
	private void querySource(String[] projection, ContentResolverQueryCallback callback) {
        ContentResolver contentResolver = act.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(Uri.fromFile(iFile) , projection, null, null, null);
            if ((cursor != null) && cursor.moveToNext()) {
                callback.onCursorResult(cursor);
            }
        } catch (Exception e) {
            // Ignore error for lacking the data column from the source.
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
package com.sportxast.SportXast.video.capture;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.models._MediaStorage;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Shortens/Crops a track
 */
public class TrimVideoUtils {
    
	static Global_Data FGlobalData;
	
    public static void startTrim(File src, File dst, int startMs, int endMs) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(src, "r");
        Movie movie = MovieCreator.build(src.getAbsolutePath());

        // remove all tracks we will create new tracks from the old
        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());

        double startTime = startMs/1000;
        double endTime = endMs/1000;

        boolean timeCorrected = false;

        // Here we try to find a track that has sync samples. Since we can only start decoding
        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
        // such a frame
        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    // This exception here could be a false positive in case we have multiple tracks
                    // with sync samples at exactly the same positions. E.g. a single movie containing
                    // multiple qualities of the same video (Microsoft Smooth Streaming file)

                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }
                startTime = correctTimeToSyncSample(track, startTime, false);
                endTime = correctTimeToSyncSample(track, endTime, true);
                timeCorrected = true;
            }
        }

        for (Track track : tracks) {
            long currentSample = 0;
            double currentTime = 0;
            long startSample = -1;
            long endSample = -1;

            for (int i = 0; i < track.getSampleDurations().length; i++) {
            	long delta = track.getSampleDurations()[i];
                    // entry.getDelta() is the amount of time the current sample covers.

                    if (currentTime <= startTime) {
                        // current sample is still before the new starttime
                        startSample = currentSample;
                    }
                    if (currentTime <= endTime) {
                        // current sample is after the new start time and still before the new endtime
                        endSample = currentSample;
                    } else {
                        // current sample is after the end of the cropped video
                        break;
                    }
                    currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
                    currentSample++;
                }
            movie.addTrack(new CroppedTrack(track, startSample, endSample));
        }
        long start1 = System.currentTimeMillis();
        Container out = new DefaultMp4Builder().build(movie);
        long start2 = System.currentTimeMillis();
        FileOutputStream fos;
		try {
			fos = new FileOutputStream(dst);
	        FileChannel fc = fos.getChannel();
	        out.writeContainer(fc);
	        fc.close();
	        fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        long start3 = System.currentTimeMillis();
        System.err.println("Building IsoFile took : " + (start2 - start1) + "ms");
        System.err.println("Writing IsoFile took  : " + (start3 - start2) + "ms");
    }

    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];

            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                // samples always start with 1 but we start with zero therefore +1
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;

        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
    
    public static Bitmap getVideoFrame(Context c, String url, long startTime) throws FileNotFoundException, IOException {
    	FileDescriptor FD = c.openFileInput(url).getFD();
    	MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(FD);
            return retriever.getFrameAtTime(startTime);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return null;
    }
    
    public static Bitmap getVideoFrame(String videoPath,long frameTime) throws Exception {
        Bitmap b = null;
//        System.out.println("videoPath:" + videoPath);
//        FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
//    	try {
//    		fmmr.setDataSource(videoPath);
//    		b = fmmr.getFrameAtTime();;
//    		if (b != null) {
//    			Bitmap b2 = fmmr.getFrameAtTime(4000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//    			if (b2 != null) {
//    				b = b2;
//    			}
//    		}
//    	} catch (IllegalArgumentException ex) {
//    		ex.printStackTrace();
//    	} finally {
//    		fmmr.release();
//    	}
//    	return b;
        try {
            MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
            mRetriever.setDataSource(videoPath);
            b = mRetriever.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
          } catch (Exception e) {
        	  e.printStackTrace(); 
        	  }
        return b;
    }

    public static String createDirIfNotExists(String dirname) {
    	File dir = new File (dirname);
    	if (! dir.exists()) {
    		dir.mkdirs();
    	}
    	return dir.getAbsolutePath();
    }
    
    public static String getLocalVideoPath(Context c) {
    	return TrimVideoUtils.createDirIfNotExists(TrimVideoUtils.getLocalSavePath(c) + "/video");
    }

    public static String getLocalImagesPath(Context c) {
    	return TrimVideoUtils.createDirIfNotExists(TrimVideoUtils.getLocalSavePath(c) + "/images");
    }

    public static String getLocalTmpPath(Context c) {
    	return TrimVideoUtils.createDirIfNotExists(TrimVideoUtils.getLocalSavePath(c) + "/tmp");
    }

    private static String getLocalSavePath(Context c) {
		return Environment.getExternalStorageDirectory() + "/Sportx";
//	 new File(
// 	 Environment.getExternalStorageDirectory(), c.getClass().getPackage().getName()).getAbsolutePath();
	}
     
    public static void getNextMediaIdATAN(final Context context, final _MediaStorage capture) { 
    	      /*
    	final DatabaseHelper DB = new DatabaseHelper(context);  
    	final String timeStamp 			= CommonFunctions_1.getCurrentTimeStamp();
    	final String eventID 			= capture.mediaEventID;
    	final String localVideoFilePath = TrimVideoUtils.getLocalVideoPath(context) + "/" + capture.videoFileName; 
    	final String localImageFilePath = TrimVideoUtils.getLocalImagesPath(context) + "/" + capture.imageFileName; 
  		final String latitude_ 			= String.valueOf( GlobalVariablesHolder.user_Latitude ); 
  		final String longitude_ 		= String.valueOf( GlobalVariablesHolder.user_Longitude ); 
  		 
    	RequestParams requestParams = new RequestParams(); 

    	requestParams.put("_ts", 			timeStamp );
    	requestParams.put("eventId", 		eventID );
    	requestParams.put("videoLocalPath", localVideoFilePath );
    	requestParams.put("imageLocalPath",	localImageFilePath );
    	requestParams.put("latitude", 		latitude_ );
    	requestParams.put("longitude", 		longitude_ );
	            
		Async_HttpClient async_HttpClient = new Async_HttpClient(context); 
		async_HttpClient.GET("GetNextMediaId", requestParams,	
			new JsonHttpResponseHandler() { 
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					// Log.v("onStart", "onStart"); 
				}

				@Override
				public void onSuccess(final JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(response);
					if(response != null){
					try {
			        	capture.mediaServerId 	= response.getString("mediaId");
			        	capture.mediaShareUrl 	= response.getString("shareUrl");
			        	capture.shareText 		= response.getString("shareText");
			        	Date cDate = new Date();
			        	String fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cDate);
			        	capture.mediaDateShortFormat = fDate;
						capture.modified = "1";
						capture.inQueue = "0";
			        	_MediaStorage currentCapture = DB.createUpdateCapture(capture);

			        	// create queue
			    		_MediaQueue mq = new _MediaQueue();
			    		mq.inQueue = "0";
			    		mq.mediaStorageId= currentCapture.ID;
			    		mq.mediaServerId = currentCapture.mediaServerId;
			    		mq.imageFileName = currentCapture.imageFileName;
			    		mq.imageFilePath = response.getString("imagePath");
			    		mq.videoFileName = currentCapture.videoFileName;
			    		mq.videoFilePath = response.getString("videoPath");
			    		
			    		Utils.saveQueue(context, mq); 
			    		FGlobalData = (Global_Data) context;
			    		FGlobalData.setVideoPath( response.getString("videoPath") ); 
			    		//######################################################################## 
			    		//String eventId, String coverImage, String largeImageUrl, String videoLocalPath, String imageLocalPath
			    		FGlobalData.setNewlyUploadedHighlightsData( response, capture.mediaEventID, capture.imageFileName, "", localVideoFilePath, localImageFilePath, capture.videoFileName );
			    		//########################################################################
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						capture.inQueue = "0";
					    DB.createUpdateCapture(capture);
					} 
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					// Log.v("onFinish", "onFinish"); 
				}

				@Override
				public void onFailure(String responseBody,
						Throwable error) {
					// TODO Auto-generated method stub
					super.onFailure(responseBody, error);
					Log.v("onFailure", "onFailure :" + responseBody + " : " + error);
					
					capture.inQueue = "0";
				    DB.createUpdateCapture(capture); 
				}
			});  
		
		*/
    }
    
    
    //ORIGINAL
    public static void getNextMediaId(final Context c, final _MediaStorage capture) { 
    	
    	/*
    	
    	final DatabaseHelper DB = new DatabaseHelper(c);
    	String url = Constants.BASE_URL + "GetNextMediaId";
    	//Map<String, Object> rparams = new HashMap<String, Object>();
    	   
    	final String timeStamp 			= CommonFunctions_1.getCurrentTimeStamp();
    	final String eventID 			= capture.mediaEventID;
    	final String localVideoFilePath = TrimVideoUtils.getLocalVideoPath(c) + "/" + capture.videoFileName; 
    	final String localImageFilePath = TrimVideoUtils.getLocalImagesPath(c) + "/" + capture.imageFileName; 
  		final String latitude_ 			= String.valueOf( GlobalVariablesHolder.user_Latitude ); 
  	 	final String longitude_ 		= String.valueOf( GlobalVariablesHolder.user_Longitude ); 
  	  
  		url += "?_ts=" + timeStamp + "&eventId=" + eventID + "&videoLocalPath=" + localVideoFilePath + "&imageLocalPath=" + localImageFilePath + "&latitude=" + latitude_ + "&longitude=" + longitude_;
  	 
		AQuery aq = new AQuery(c);
	    Log.e("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOVideoCapture", "GetNextMediaId:" + url);
	    Log.e("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOVideoCapture", "GetNextMediaId:" + url);
	    Log.e("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOVideoCapture", "GetNextMediaId:" + url);
	    Log.e("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOVideoCapture", "GetNextMediaId:" + url);
	    Log.e("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOVideoCapture", "GetNextMediaId:" + url);
	    Log.e("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOVideoCapture", "GetNextMediaId:" + url);
	    Log.e("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOVideoCapture", "GetNextMediaId:" + url);
	    capture.inQueue = "1";
	         
	    DB.createUpdateCapture(capture);   
	    
	    
	    AjaxCallback<JSONObject> getNextMediaIdCB = new AjaxCallback<JSONObject>() {
			@Override
	        public void callback(String url, final JSONObject json, AjaxStatus status) {
				if(json != null){
				try {
		        	capture.mediaServerId = json.getString("mediaId");
		        	capture.mediaShareUrl = json.getString("shareUrl");
		        	capture.shareText = json.getString("shareText");
		        	Date cDate = new Date();
		        	String fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cDate);
		        	capture.mediaDateShortFormat = fDate;
					capture.modified = "1";
					capture.inQueue = "0"; 
		        	_MediaStorage currentCapture = DB.createUpdateCapture(capture);
		        	
		        	// create queue
		    		_MediaQueue mq = new _MediaQueue();
		    		mq.inQueue = "0";
		    		mq.mediaStorageId= currentCapture.ID;
		    		mq.mediaServerId = currentCapture.mediaServerId;
		    		mq.imageFileName = currentCapture.imageFileName;
		    		mq.imageFilePath = json.getString("imagePath");
		    		mq.videoFileName = currentCapture.videoFileName;
		    		mq.videoFilePath = json.getString("videoPath");
		    		
		    		Utils.saveQueue(c, mq); 
		    		FGlobalData = (Global_Data) c;
		    		FGlobalData.setVideoPath( json.getString("videoPath") );
		    		
		    		//######################################################################## 
		    		//String eventId, String coverImage, String largeImageUrl, String videoLocalPath, String imageLocalPath
		    		FGlobalData.setNewlyUploadedHighlightsData( json, capture.mediaEventID, capture.imageFileName, "", localVideoFilePath, localImageFilePath, capture.videoFileName );
		    		//########################################################################
		    		
				} catch (Exception e) {
					e.printStackTrace();
				}
				} else {
					capture.inQueue = "0";
				    DB.createUpdateCapture(capture);
				}
	        }
	    };
	     
	    getNextMediaIdCB.headers(Utils.getHttpHeaders(c));
	    //aq.ajax(url, rparams, JSONObject.class, getNextMediaIdCB );	 
	    aq.ajax(url, JSONObject.class, getNextMediaIdCB );	 
	    
	    */
	}

}

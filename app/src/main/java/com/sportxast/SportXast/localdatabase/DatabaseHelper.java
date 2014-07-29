package com.sportxast.SportXast.localdatabase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sportxast.SportXast.Utils;
import com.sportxast.SportXast.localdatabase.Keys.KEY_EVENT;
import com.sportxast.SportXast.localdatabase.Keys.KEY_MEDIA_QUEUE;
import com.sportxast.SportXast.localdatabase.Keys.KEY_MEDIA_STORAGE;
import com.sportxast.SportXast.localdatabase.Keys.KEY_PROFILE;
import com.sportxast.SportXast.models._MediaQueue;
import com.sportxast.SportXast.models._MediaStorage;
import com.sportxast.SportXast.models._RecentEvent;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 7;
	public static final String DATABASE_NAME = "sportXast.db";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLE_PROFILE = "CREATE  TABLE "
				+ KEY_PROFILE.TABLE_PROFILE + "(" + KEY_PROFILE.ID
				+ " INTEGER PRIMARY KEY," + KEY_PROFILE.DEVICE_ID + " TEXT,"
				+ KEY_PROFILE.SESSION_ID + " TEXT," + KEY_PROFILE.USER_ID
				+ " TEXT," + KEY_PROFILE.USER_NAME + " TEXT)";
		db.execSQL(CREATE_TABLE_PROFILE);
		String CREATE_TABLE_EVENT = "CREATE TABLE " + KEY_EVENT.TABLE_EVENTS
				+ "(" + KEY_EVENT.ID + " INTEGER PRIMARY KEY,"
				+ KEY_EVENT.SPORTID + " TEXT," + KEY_EVENT.SPORTNAME + " TEXT,"
				+ KEY_EVENT.SPORTFIRSTLETTER + " TEXT," + KEY_EVENT.SPORTLOGO
				+ " TEXT," + KEY_EVENT.VENUEADDR + " TEXT," + KEY_EVENT.VENUEID
				+ " TEXT," + KEY_EVENT.VENUELATITUDE + " TEXT,"
				+ KEY_EVENT.VENUELONGITUDE + " TEXT," + KEY_EVENT.VENUENAME
				+ " TEXT," + KEY_EVENT.FIRSTTEAM + " TEXT,"
				+ KEY_EVENT.SECONDTEAM + " TEXT, "  
				+ KEY_EVENT.EVENT_IS_ENDED + " TEXT, " 
				+ KEY_EVENT.EVENT_LATITUDE + " TEXT, "  
				+ KEY_EVENT.EVENT_LOCATION + " TEXT, "  
				+ KEY_EVENT.EVENT_SHARE_COUNT + " TEXT, "  
				+ KEY_EVENT.EVENT_START_DATE_SHORT + " TEXT, " 
				+ KEY_EVENT.EVENT_START_DATE + " TEXT, "  
				+ KEY_EVENT.EVENT_LONGITUDE + " TEXT, "  
				+ KEY_EVENT.EVENT_FAVORITE_COUNT + " TEXT, "  
				+ KEY_EVENT.EVENT_SCORE + " TEXT, "  
				+ KEY_EVENT.EVENT_TEAMS + " TEXT, "  
				+ KEY_EVENT.EVENT_ID + " TEXT, "  
				+ KEY_EVENT.EVENT_FIRST_TEAM + " TEXT, "  
				+ KEY_EVENT.EVENT_SECOND_TEAM + " TEXT, "  
				+ KEY_EVENT.EVENT_VIEW_COUNT + " TEXT, "  
				+ KEY_EVENT.EVENT_SPORT_ID + " TEXT, "  
				+ KEY_EVENT.EVENT_NAME + " TEXT, "  
				+ KEY_EVENT.EVENT_COMMENT_COUNT + " TEXT, "  
				+ KEY_EVENT.EVENT_IS_OPEN + " TEXT, "  
				+ KEY_EVENT.EVENT_SPORT_NAME + " TEXT, "  
				+ KEY_EVENT.EVENT_MEDIA_COUNT + " TEXT, "  
				+ KEY_EVENT.EVENT_TAGS + " TEXT "  
				+ ")";
		db.execSQL(CREATE_TABLE_EVENT);
		createMediaStorage(db);
		createMediaQueue(db);
	}

	private void createMediaQueue(SQLiteDatabase db) {
		String CREATE_TABLE_MEDIA_QUEUE = "CREATE TABLE "
				+ KEY_MEDIA_QUEUE.TABLE_MEDIA_QUEUE + "(" + KEY_MEDIA_QUEUE.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MEDIA_QUEUE.IMAGE_FILE_NAME
				+ " TEXT," + KEY_MEDIA_QUEUE.IMAGE_FILE_PATH + " TEXT,"
				+ KEY_MEDIA_QUEUE.IMAGE_UPLOAD_RESPONSE_HEADERS + " TEXT,"
				+ KEY_MEDIA_QUEUE.IN_QUEUE + " INTEGER,"
				+ KEY_MEDIA_QUEUE.IS_IMAGE_UPLOADED + " INTEGER,"
				+ KEY_MEDIA_QUEUE.IS_UPLOADED + " INTEGER,"
				+ KEY_MEDIA_QUEUE.IS_VIDEO_UPLOADED + " INTEGER,"
				+ KEY_MEDIA_QUEUE.MEDIA_FILE_KEY + " TEXT,"
				+ KEY_MEDIA_QUEUE.MEDIA_SERVER_ID + " INTEGER,"
				+ KEY_MEDIA_QUEUE.MEDIA_STORAGE_ID + " INTEGER,"
				+ KEY_MEDIA_QUEUE.MEDIA_SERVER_ID_FAILED + " INTEGER,"
				+ KEY_MEDIA_QUEUE.VIDEO_FILE_NAME + " TEXT,"
				+ KEY_MEDIA_QUEUE.VIDEO_FILE_PATH + " TEXT,"
				+ KEY_MEDIA_QUEUE.VIDEO_UPLOAD_RESPONSE_HEADERS + " TEXT)";
		db.execSQL(CREATE_TABLE_MEDIA_QUEUE);
	}

	private void createMediaStorage(SQLiteDatabase db) {
		String CREATE_TABLE_MEDIA_STORAGE = "CREATE TABLE "
				+ KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE + "("
				+ KEY_MEDIA_STORAGE.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_MEDIA_STORAGE.IMAGE_FILE_NAME + " TEXT,"
				+ KEY_MEDIA_STORAGE.IMAGE_UPLOAD_RESPONSE_HEADERS + " TEXT,"
				+ KEY_MEDIA_STORAGE.IN_QUEUE + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_DELETED_BY_USER + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_FAVORITE + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_IMAGE_UPLOADED + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_SHARED_ON_FACEBOOK + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_SHARED_ON_MAIL + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_SHARED_ON_SMS + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_SHARED_ON_TWITTER + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_UPLOADED + " INTEGER,"
				+ KEY_MEDIA_STORAGE.IS_VIDEO_UPLOADED + " INTEGER,"
				+ KEY_MEDIA_STORAGE.MEDIA_ASPECT_RATIO + " TEXT,"
				+ KEY_MEDIA_STORAGE.MEDIA_COMMENT + " TEXT,"
				+ KEY_MEDIA_STORAGE.MEDIA_COVER_PATH + " TEXT,"
				+ KEY_MEDIA_STORAGE.MEDIA_DATE + " TEXT,"
				+ KEY_MEDIA_STORAGE.MEDIA_EVENT_ID + " INTEGER,"
				+ KEY_MEDIA_STORAGE.MEDIA_FILE_KEY + " TEXT,"
				+ KEY_MEDIA_STORAGE.MEDIA_ORIENTATION + " INTEGER,"
				+ KEY_MEDIA_STORAGE.MEDIA_PATH + " TEXT,"
				+ KEY_MEDIA_STORAGE.MEDIA_SERVER_ID + " INTEGER,"
				+ KEY_MEDIA_STORAGE.MEDIA_SESSION_ID + " STRING,"
				+ KEY_MEDIA_STORAGE.MEDIA_SHARE_URL + " TEXT,"
				+ KEY_MEDIA_STORAGE.MEDIA_TAGS + " TEXT,"
				+ KEY_MEDIA_STORAGE.MEDIA_SHARE_TEXT + " TEXT,"
				+ KEY_MEDIA_STORAGE.USER_LATITUDE + " TEXT,"
				+ KEY_MEDIA_STORAGE.USER_LONGITUDE + " TEXT,"
				+ KEY_MEDIA_STORAGE.VIDEO_FILE_NAME + " TEXT,"
				+ KEY_MEDIA_STORAGE.MODIFIED + " TEXT,"
				+ KEY_MEDIA_STORAGE.VIDEO_UPLOAD_RESPONSE_HEADERS + " TEXT)";
		db.execSQL(CREATE_TABLE_MEDIA_STORAGE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + KEY_PROFILE.TABLE_PROFILE);
		db.execSQL("DROP TABLE IF EXISTS " + KEY_EVENT.TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE);
		db.execSQL("DROP TABLE IF EXISTS " + KEY_MEDIA_QUEUE.TABLE_MEDIA_QUEUE);

		// Create tables again
		onCreate(db);
	}

	public long addItem(String TABLE_NAME, ContentValues contentValues) {
		SQLiteDatabase db = this.getWritableDatabase();
		long inserted = db.insert(TABLE_NAME, null, contentValues);
		db.close();
		return inserted;
	}

	public int updateItem(String TABLE_NAME, ContentValues contentValues,
			String KEY, String VALUE) {

		SQLiteDatabase db = this.getWritableDatabase();

		int updated = db.update(TABLE_NAME, contentValues, KEY + "=='" + VALUE
				+ "'", null);
		db.close();
		return updated;
	}

	public int deleteItem(String TABLE_NAME, String KEY, String VALUE) {

		SQLiteDatabase db = this.getWritableDatabase();

		int deleted = db.delete(TABLE_NAME, KEY + "=='" + VALUE + "'", null);
		db.close();
		return deleted;
	}

	public void deleteFirstRow(String TABLE_NAME) {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			String rowId = cursor
					.getString(cursor.getColumnIndex(KEY_EVENT.ID));

			db.delete(TABLE_NAME, KEY_EVENT.ID + "=?", new String[] { rowId });
		}
		cursor.close();
		db.close();
	}

	public ContentValues getItem(String TABLE_NAME, String KEY, String VALUE) {

		ContentValues item = new ContentValues();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, null, KEY + "='" + VALUE + "'",
				null, null, null, null);

		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				item.put(cursor.getColumnName(i), cursor.getString(i));
			}
		}
		cursor.close();
		db.close();
		return item;

	}

	public ArrayList<ContentValues> getAllItem(String TABLE_NAME) {

		ArrayList<ContentValues> arrayList = new ArrayList<ContentValues>();

		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ContentValues item = new ContentValues();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					item.put(cursor.getColumnName(i), cursor.getString(i));

				}
				arrayList.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return arrayList;
	}

	public _RecentEvent getLastEvent() {

		String selectQuery = "SELECT  * FROM " + KEY_EVENT.TABLE_EVENTS + " ORDER BY " + KEY_EVENT.ID + " DESC LIMIT 1";
		_RecentEvent event = new _RecentEvent();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ContentValues item = new ContentValues();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					item.put(cursor.getColumnName(i), cursor.getString(i));

				}
				event.eventId 			= item.getAsString(KEY_EVENT.EVENT_ID);
				event.sportId 			= item.getAsString(KEY_EVENT.SPORTID);
				event.sportName 		= item.getAsString(KEY_EVENT.SPORTNAME);
				event.sportLogo 		= item.getAsString(KEY_EVENT.SPORTLOGO);
				event.sportFirstLetter 	= item.getAsString(KEY_EVENT.SPORTFIRSTLETTER);
				event.venueId 			= item.getAsString(KEY_EVENT.VENUEID);
				event.venueName 		= item.getAsString(KEY_EVENT.VENUENAME);
				event.venueAddress 		= item.getAsString(KEY_EVENT.VENUEADDR);
				event.venueLatitude 	= item.getAsString(KEY_EVENT.VENUELATITUDE);
				event.venueLongitude 	= item.getAsString(KEY_EVENT.VENUELONGITUDE);
				
				ArrayList<String> teams = new ArrayList<String>();
				teams.add(item.getAsString(KEY_EVENT.EVENT_FIRST_TEAM));
				teams.add(item.getAsString(KEY_EVENT.EVENT_FIRST_TEAM));
				event.team = teams;
				
				event.eventLocation = item.getAsString(KEY_EVENT.EVENT_LOCATION);
				event.eventIsEnded = item.getAsString(KEY_EVENT.EVENT_IS_ENDED);
				event.eventLatitude = item.getAsString(KEY_EVENT.EVENT_LATITUDE);
				event.eventShareCount = item.getAsString(KEY_EVENT.EVENT_SHARE_COUNT);
				event.eventStartDateShort = item.getAsString(KEY_EVENT.EVENT_START_DATE_SHORT);
				event.eventStartDate = item.getAsString(KEY_EVENT.EVENT_START_DATE);
				event.eventLongitude = item.getAsString(KEY_EVENT.EVENT_LONGITUDE);
				event.eventFavoriteCount = item.getAsString(KEY_EVENT.EVENT_FAVORITE_COUNT);
				event.eventScore = item.getAsString(KEY_EVENT.EVENT_SCORE);
				event.eventTeams = item.getAsString(KEY_EVENT.EVENT_TEAMS);
				event.eventFirstTeam = item.getAsString(KEY_EVENT.EVENT_FIRST_TEAM);
				event.eventSecondTeam = item.getAsString(KEY_EVENT.EVENT_SECOND_TEAM);
				event.eventViewCount = item.getAsString(KEY_EVENT.EVENT_VIEW_COUNT);
				event.eventSportId = item.getAsString(KEY_EVENT.EVENT_SPORT_ID);
				event.eventName = item.getAsString(KEY_EVENT.EVENT_NAME);
				event.eventCommentCount = item.getAsString(KEY_EVENT.EVENT_COMMENT_COUNT);
				event.eventIsOpen = item.getAsString(KEY_EVENT.EVENT_IS_OPEN);
				event.eventSportName = item.getAsString(KEY_EVENT.EVENT_SPORT_NAME);
				event.eventMediaCount = item.getAsString(KEY_EVENT.EVENT_MEDIA_COUNT);
				event.eventTags = item.getAsString(KEY_EVENT.EVENT_TAGS);
				
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return event;
	}
	
	
	public int getTableCount(String TABLE_NAME) {
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}

	public _MediaStorage createUpdateCapture(_MediaStorage currentCapture) {
		ContentValues contentValues;
		Log.e("createUpdateCapture", "currentCapture.ID = " + currentCapture.ID);
		Log.e("createUpdateCapture", "******************************");
		Utils.showMediaStorageObj(currentCapture);
		Log.e("createUpdateCapture", "******************************");
//		if (Integer.parseInt(currentCapture.ID) == 0) {
//			return;
//		}
		
		ContentValues contentValuesExists = getItem(
				Keys.KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE,
				Keys.KEY_MEDIA_STORAGE.ID, currentCapture.ID);
		contentValues = new ContentValues();
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IMAGE_FILE_NAME,
				currentCapture.imageLocalFilename);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IMAGE_UPLOAD_RESPONSE_HEADERS,
				currentCapture.imageUploadResponseHeaders);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IN_QUEUE,
				currentCapture.inQueue);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_DELETED_BY_USER,
				currentCapture.isDeletedByUser);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_FAVORITE,
				currentCapture.isFavorite);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_IMAGE_UPLOADED,
				currentCapture.isImageUploaded);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_SHARED_ON_FACEBOOK,
				currentCapture.isSharedOnFacebook);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_SHARED_ON_MAIL,
				currentCapture.isSharedOnMail);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_SHARED_ON_SMS,
				currentCapture.isSharedOnSms);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_SHARED_ON_TWITTER,
				currentCapture.isSharedOnTwitter);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_UPLOADED,
				currentCapture.isUploaded);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.IS_VIDEO_UPLOADED,
				currentCapture.isVideoUploaded);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_ASPECT_RATIO,
				currentCapture.mediaAspectRatio);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_COMMENT,
				currentCapture.mediaComment);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_COVER_PATH,
				currentCapture.imageServerFilePath);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_DATE,
				currentCapture.mediaDateShortFormat);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_EVENT_ID,
				currentCapture.mediaEventID);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_FILE_KEY,
				currentCapture.mediaFileKey);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_ORIENTATION,
				currentCapture.mediaOrientation);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_PATH,
				currentCapture.videoServerFilePath);
		if (currentCapture.mediaServerId != null && ! currentCapture.mediaServerId.equals("0")) {
			contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_SERVER_ID,
				currentCapture.mediaServerId);
		}
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_SESSION_ID,
				currentCapture.mediaSessionID);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_SHARE_URL,
				currentCapture.mediaShareUrl);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_TAGS,
				currentCapture.mediaTags);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MEDIA_SHARE_TEXT,
				currentCapture.shareText);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.USER_LATITUDE,
				currentCapture.userLatitude);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.USER_LONGITUDE,
				currentCapture.userLongitude);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.VIDEO_FILE_NAME,
				currentCapture.videoLocalFilename);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.VIDEO_UPLOAD_RESPONSE_HEADERS,
				currentCapture.videoUploadResponseHeaders);
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MODIFIED,
				currentCapture.modified);

		if (contentValuesExists.size() != 0) {
			// exists
			updateItem(Keys.KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE,
					contentValues, Keys.KEY_MEDIA_STORAGE.ID, currentCapture.ID);
			Log.e("createUpdateCapture", "UPDATED: " + currentCapture.ID);
		} else {
			// add
			long id = addItem(Keys.KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE,
					contentValues);
			Log.e("createUpdateCapture", "INSERTED: " + id);
			currentCapture.ID = id + ""; 
		}
		return currentCapture;
	}
	

	public _MediaStorage updateCapture(_MediaStorage capture) {
		return this.createUpdateCapture(capture);
	}
	

	public void createUpdateQueue(_MediaQueue mediaQueue) {
		 
		
		Log.e("createUpdateCapture-ATAN", "INSERTED: " + mediaQueue.isVideoUploaded);
		
		ContentValues contentValuesExists = getItem(
				Keys.KEY_MEDIA_QUEUE.TABLE_MEDIA_QUEUE,
				Keys.KEY_MEDIA_QUEUE.MEDIA_STORAGE_ID, mediaQueue.mediaStorageId);
		
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(Keys.KEY_MEDIA_QUEUE.IMAGE_FILE_NAME,
				mediaQueue.imageFileName);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.IMAGE_FILE_PATH,
				mediaQueue.imageFilePath);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.IMAGE_UPLOAD_RESPONSE_HEADERS,
				mediaQueue.imageUploadResponseHeaders);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.IN_QUEUE, mediaQueue.inQueue);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.IS_IMAGE_UPLOADED,
				mediaQueue.isImageUploaded);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.IS_UPLOADED,
				mediaQueue.isUploaded);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.IS_VIDEO_UPLOADED,
				mediaQueue.isVideoUploaded);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.MEDIA_FILE_KEY,
				mediaQueue.mediaFileKey);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.MEDIA_SERVER_ID,
				mediaQueue.mediaServerId);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.MEDIA_SERVER_ID_FAILED,
				mediaQueue.mediaServerIdFailed);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.VIDEO_FILE_NAME,
				mediaQueue.videoFileName);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.VIDEO_FILE_PATH,
				mediaQueue.videoFilePath);
		contentValues.put(Keys.KEY_MEDIA_QUEUE.VIDEO_UPLOAD_RESPONSE_HEADERS,
				mediaQueue.videoUploadResponseHeaders);

		if (contentValuesExists.size() != 0) {
			// exists
			int idu = updateItem(Keys.KEY_MEDIA_QUEUE.TABLE_MEDIA_QUEUE,
					contentValues, Keys.KEY_MEDIA_QUEUE.ID, mediaQueue.ID);
			Log.e("createUpdateQueue-BLAH-BLAH", "UPDATED: " + idu);
		} else {
			// add
			contentValues.put(Keys.KEY_MEDIA_QUEUE.MEDIA_STORAGE_ID,
					mediaQueue.mediaStorageId);
			long id = addItem(Keys.KEY_MEDIA_QUEUE.TABLE_MEDIA_QUEUE,
					contentValues);
			Log.e("createUpdateQueue-BLAH-BLAH", "INSERTED: " + id);
		}

	}

	public ArrayList<ContentValues> getAllItem(String TABLE_NAME, String KEY,
			String VALUE) {
		ArrayList<ContentValues> arrayList = new ArrayList<ContentValues>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, KEY + "='" + VALUE + "' ",
				null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				ContentValues item = new ContentValues();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					item.put(cursor.getColumnName(i), cursor.getString(i));

				}
				arrayList.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return arrayList;
	}

	public _MediaStorage getMediaStorageByMediaServerId(String iD) {
		ContentValues contentValues = getItem(
				Keys.KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE,
				Keys.KEY_MEDIA_STORAGE.MEDIA_SERVER_ID, iD);
		_MediaStorage mediaStorage = new _MediaStorage();
		if (contentValues.size() != 0) {
			mediaStorage = this.cv2mediaStorage(contentValues);
			return mediaStorage;
		}

		return null;
	}

	public _MediaStorage cv2mediaStorage(ContentValues contentValues) {
		_MediaStorage mediaStorage = new _MediaStorage();
		try {
			mediaStorage.ID 						= contentValues.getAsString(KEY_MEDIA_STORAGE.ID);
			mediaStorage.imageLocalFilename 		= contentValues.getAsString(KEY_MEDIA_STORAGE.IMAGE_FILE_NAME);
			mediaStorage.imageUploadResponseHeaders = contentValues.getAsString(KEY_MEDIA_STORAGE.IMAGE_UPLOAD_RESPONSE_HEADERS);
			mediaStorage.inQueue 					= contentValues.getAsString(KEY_MEDIA_STORAGE.IN_QUEUE);
			mediaStorage.isDeletedByUser 			= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_DELETED_BY_USER);
			mediaStorage.isFavorite 				= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_FAVORITE);
			mediaStorage.isImageUploaded 			= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_IMAGE_UPLOADED);
			mediaStorage.isSharedOnFacebook 		= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_SHARED_ON_FACEBOOK);
			mediaStorage.isSharedOnMail				= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_SHARED_ON_MAIL);
			mediaStorage.isSharedOnSms 				= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_SHARED_ON_SMS);
			mediaStorage.isSharedOnTwitter 			= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_SHARED_ON_TWITTER);
			mediaStorage.isUploaded 				= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_UPLOADED);
			mediaStorage.isVideoUploaded 			= contentValues.getAsString(KEY_MEDIA_STORAGE.IS_VIDEO_UPLOADED);
			mediaStorage.mediaAspectRatio 			= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_ASPECT_RATIO);
			mediaStorage.mediaComment 				= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_COMMENT);
			mediaStorage.imageServerFilePath 			= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_COVER_PATH);
			mediaStorage.mediaDateShortFormat  		= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_DATE);
			mediaStorage.mediaEventID 				= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_EVENT_ID);
			mediaStorage.mediaFileKey 				= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_FILE_KEY);
			mediaStorage.mediaOrientation 			= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_ORIENTATION);
			mediaStorage.videoServerFilePath 		= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_PATH);
			mediaStorage.mediaServerId 				= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_SERVER_ID);
			mediaStorage.mediaSessionID 			= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_SESSION_ID);
			mediaStorage.mediaShareUrl 				= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_SHARE_URL);
			mediaStorage.mediaTags 					= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_TAGS);
			mediaStorage.shareText 					= contentValues.getAsString(KEY_MEDIA_STORAGE.MEDIA_SHARE_TEXT);
			mediaStorage.userLatitude 				= contentValues.getAsString(KEY_MEDIA_STORAGE.USER_LATITUDE);
			mediaStorage.userLongitude 				= contentValues.getAsString(KEY_MEDIA_STORAGE.USER_LONGITUDE);
			mediaStorage.videoLocalFilename 	= contentValues.getAsString(KEY_MEDIA_STORAGE.VIDEO_FILE_NAME);
			mediaStorage.videoUploadResponseHeaders = contentValues.getAsString(KEY_MEDIA_STORAGE.VIDEO_UPLOAD_RESPONSE_HEADERS);
		} catch (Exception e) {

		}
		return mediaStorage;
	}

	public void showMediaStorageField(ContentValues contentValues) {
		try {
			Log.e(KEY_MEDIA_STORAGE.ID + "", contentValues.getAsString(KEY_MEDIA_STORAGE.ID));
			Log.e(KEY_MEDIA_STORAGE.IMAGE_FILE_NAME + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IMAGE_FILE_NAME));
			Log.e(KEY_MEDIA_STORAGE.IMAGE_UPLOAD_RESPONSE_HEADERS  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IMAGE_UPLOAD_RESPONSE_HEADERS));
			Log.e(KEY_MEDIA_STORAGE.IN_QUEUE  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IN_QUEUE));
			Log.e(KEY_MEDIA_STORAGE.IS_DELETED_BY_USER  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_DELETED_BY_USER));
			Log.e(KEY_MEDIA_STORAGE.IS_FAVORITE  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_FAVORITE));
			Log.e(KEY_MEDIA_STORAGE.IS_IMAGE_UPLOADED  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_IMAGE_UPLOADED));
			Log.e(KEY_MEDIA_STORAGE.IS_SHARED_ON_FACEBOOK  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_SHARED_ON_FACEBOOK));
			Log.e(KEY_MEDIA_STORAGE.IS_SHARED_ON_MAIL  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_SHARED_ON_MAIL));
			Log.e(KEY_MEDIA_STORAGE.IS_SHARED_ON_SMS  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_SHARED_ON_SMS));
			Log.e(KEY_MEDIA_STORAGE.IS_SHARED_ON_TWITTER  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_SHARED_ON_TWITTER));
			Log.e(KEY_MEDIA_STORAGE.IS_UPLOADED  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_UPLOADED));
			Log.e(KEY_MEDIA_STORAGE.IS_VIDEO_UPLOADED  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.IS_VIDEO_UPLOADED));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_ASPECT_RATIO  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_ASPECT_RATIO));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_COMMENT  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_COMMENT));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_COVER_PATH  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_COVER_PATH));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_DATE  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_DATE));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_EVENT_ID  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_EVENT_ID));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_FILE_KEY  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_FILE_KEY));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_ORIENTATION  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_ORIENTATION));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_PATH  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_PATH));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_SERVER_ID  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_SERVER_ID));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_SESSION_ID  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_SESSION_ID));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_SHARE_URL  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_SHARE_URL));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_TAGS  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_TAGS));
			Log.e(KEY_MEDIA_STORAGE.MEDIA_SHARE_TEXT  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.MEDIA_SHARE_TEXT));
			Log.e(KEY_MEDIA_STORAGE.USER_LATITUDE  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.USER_LATITUDE));
			Log.e(KEY_MEDIA_STORAGE.USER_LONGITUDE  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.USER_LONGITUDE));
			Log.e(KEY_MEDIA_STORAGE.VIDEO_FILE_NAME  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.VIDEO_FILE_NAME));
			Log.e(KEY_MEDIA_STORAGE.VIDEO_UPLOAD_RESPONSE_HEADERS  + "",  contentValues
					.getAsString(KEY_MEDIA_STORAGE.VIDEO_UPLOAD_RESPONSE_HEADERS));
		} catch (Exception e) {

		}
	}
	
	public void setIsModified(_MediaStorage mediaStorage) {
    	ContentValues contentValues = new ContentValues();
		contentValues.put(Keys.KEY_MEDIA_STORAGE.MODIFIED, mediaStorage.modified);
    	this.updateItem(Keys.KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE,
				contentValues, Keys.KEY_MEDIA_STORAGE.ID, mediaStorage.ID);
	}

	public ArrayList<ContentValues> getAllItemForNextMediaId() {
		String selectQuery = "SELECT  * FROM " + KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE 
											+ " WHERE (" + KEY_MEDIA_STORAGE.MEDIA_SERVER_ID + " = '0' OR "
											+ KEY_MEDIA_STORAGE.MEDIA_SERVER_ID + " IS NULL ) "
				 							+ " AND " + KEY_MEDIA_STORAGE.IN_QUEUE + " = '0' ";
		return selectQuery(selectQuery);
	}

	public ArrayList<ContentValues> getQueue() {
		String selectQuery = "SELECT  * FROM " + KEY_MEDIA_QUEUE.TABLE_MEDIA_QUEUE 
											+ " WHERE " + KEY_MEDIA_QUEUE.IN_QUEUE + " = '0' "
				 							+ " AND " + KEY_MEDIA_QUEUE.IS_UPLOADED + " = '0' ";
		return selectQuery(selectQuery);
	}

	public ArrayList<ContentValues> getAllModified() {
		
		Log.e("getAllModifiedXXXXX", "getAllModified");
		Log.e("getAllModifiedXXXXX", "getAllModified");
		Log.e("getAllModifiedXXXXX", "getAllModified");
		Log.e("getAllModifiedXXXXX", "getAllModified");
		
		String selectQuery = "SELECT  * FROM " + KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE 
											+ " WHERE " + KEY_MEDIA_STORAGE.MODIFIED + " = '1'"
				 							+ " AND " + KEY_MEDIA_STORAGE.IN_QUEUE + " = '0' ";
		
		ArrayList<ContentValues> allModified = selectQuery(selectQuery);
		
		Log.e("GET ALL MODIFIED LIST", ""+ allModified.size() );
		Log.e("GET ALL MODIFIED LIST", ""+ allModified.size() );
		Log.e("GET ALL MODIFIED LIST", ""+ allModified.size() );
		Log.e("GET ALL MODIFIED LIST", ""+ allModified.size() );
		
		return selectQuery(selectQuery);
	}

	public ArrayList<ContentValues> getAllReadyToSave() {
		
		String selectQuery = "SELECT  * FROM " + KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE 
											+ " WHERE " + KEY_MEDIA_STORAGE.IS_UPLOADED + " = '0'"
				 							+ " AND " + KEY_MEDIA_STORAGE.IS_VIDEO_UPLOADED + " = '1' "
											+ " AND " + KEY_MEDIA_STORAGE.IS_IMAGE_UPLOADED + " = '1' ";
		 
		ArrayList<ContentValues> allModified = selectQuery(selectQuery);
		
		Log.e("GET ALL READY TO SAVE", ""+ allModified.size() );
		Log.e("GET ALL READY TO SAVE", ""+ allModified.size() );
		Log.e("GET ALL READY TO SAVE", ""+ allModified.size() );
		Log.e("GET ALL READY TO SAVE", ""+ allModified.size() );
		
		return selectQuery(selectQuery);
		
	}
	
	public ArrayList<ContentValues> selectQuery(String selectQuery) {
		
		ArrayList<ContentValues> arrayList = new ArrayList<ContentValues>();
		Log.e("selectQuery", selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor 	= db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				ContentValues item = new ContentValues();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					item.put(cursor.getColumnName(i), cursor.getString(i));
	
				}
				arrayList.add(item);
			} while (cursor.moveToNext());
		}
		 
		cursor.close();
		db.close();
		return arrayList;
	}
}

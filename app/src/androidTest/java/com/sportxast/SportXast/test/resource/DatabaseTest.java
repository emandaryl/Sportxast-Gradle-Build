package com.sportxast.SportXast.test.resource;

import android.content.ContentValues;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.sportxast.SportXast.localdatabase.DatabaseHelper;
import com.sportxast.SportXast.localdatabase.Keys.KEY_PROFILE;

import java.util.ArrayList;

public class DatabaseTest extends AndroidTestCase {

	//private DatabaseHelper dbHelper;
	private static final String PREFIX_DB = "sportx_test_x";
//	private static int profileIdCount = 0;
	
	private RenamingDelegatingContext context;
	//private DatabaseHelper dbHelper;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		context = new RenamingDelegatingContext(getContext(), PREFIX_DB);
		//dbHelper = new DatabaseHelper(context);
//		context.makeExistingFilesAndDbsAccessible();
	}
	
	public void testAddProfileItems() {
		// bogus data
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_PROFILE.DEVICE_ID, "7676");
		cv.put(KEY_PROFILE.USER_ID, "889");
		cv.put(KEY_PROFILE.USER_NAME, "lawgimenez");
		cv.put(KEY_PROFILE.SESSION_ID, "34");
		
		dbHelper.addItem(KEY_PROFILE.TABLE_PROFILE, cv);

//		assertEquals("Last inserted ID is " + dbHelper.getLastItemAddedId(), dbHelper.getLastItemAddedId(), 1);
		
		//dbHelper.close();
	}
	
	public void testMultipleAddProfileItems() {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		ArrayList<ContentValues> listValues = new ArrayList<ContentValues>();
		
		for(int i=0; i<5; i++) {
			ContentValues cv = new ContentValues();
			cv.put(KEY_PROFILE.DEVICE_ID, "deviceId_" + (i + 1));
			cv.put(KEY_PROFILE.USER_ID, "userId_" + (i + 1));
			cv.put(KEY_PROFILE.USER_NAME, "username_" + (i + 1));
			cv.put(KEY_PROFILE.SESSION_ID, "sessionId_" + (i + 1));
			
			dbHelper.addItem(KEY_PROFILE.TABLE_PROFILE, cv);
			
			listValues.add(cv);
		}
		
		for(int i=0; i<listValues.size(); i++) {
			ContentValues value = dbHelper.getItem(KEY_PROFILE.TABLE_PROFILE, KEY_PROFILE.DEVICE_ID, "deviceId_" + (i + 1));
			String deviceId = value.getAsString(KEY_PROFILE.DEVICE_ID);
					
			assertEquals(listValues.get(i).get(KEY_PROFILE.DEVICE_ID), deviceId);
		}
	}
	
//	public void testSelectAllProfile() {
//		//DatabaseHelper dbHelper = new DatabaseHelper(context);
//		
//		ArrayList<ContentValues> listProfile = dbHelper.getAllItem(KEY_PROFILE.TABLE_PROFILE);
//		int profileItemSize = listProfile.size();
//		assertTrue("Profile Item size is " + profileItemSize, profileItemSize > 0);
//		
//		//dbHelper.close();
//	}
//	
//	public void testSelectAllEvent() {
//		ArrayList<ContentValues> listEvent = dbHelper.getAllItem(KEY_EVENT.TABLE_EVENTS);
//		int eventItemSize = listEvent.size();
//		assertTrue("Event item size is " + eventItemSize, eventItemSize > 0);
//	}
	
	@Override
	public void tearDown() throws Exception {
		//dbHelper.close();
		super.tearDown();
	}
}

package com.sportxast.SportXast.test;

import android.app.Instrumentation.ActivityMonitor;
import android.content.ContentValues;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.Splash_Activity;
import com.sportxast.SportXast.Tutorial_Activity;
import com.sportxast.SportXast.localdatabase.DatabaseHelper;
import com.sportxast.SportXast.localdatabase.Keys.KEY_HEADER;
import com.sportxast.SportXast.localdatabase.Keys.KEY_PROFILE;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class SplashActivityTest extends ActivityInstrumentationTestCase2<Splash_Activity> {
	
	private RenamingDelegatingContext context;
	private Splash_Activity mSplashActivity;
	private ImageView mImageViewSplash;
	private Async_HttpClient mHttpClient;
	private static final String TAG = "SplashActivityTest";
//	private String mHttpResponse;
	private JSONObject jsonResponse = null;
	
	private Global_Data globalData;
	private Header[] arrHeader;
	private DatabaseHelper dbHelper;

	public SplashActivityTest() {
		/*
		 * This constructor is very important.
		 * Pass the Activity to test.
		 */
		super(Splash_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		// If you want to send key events in this test, set this to false to turn off.
		setActivityInitialTouchMode(false);

		context = new RenamingDelegatingContext(getActivity(), Values.PREFIX_TEST);
		dbHelper = new DatabaseHelper(context);
		
		mSplashActivity = (Splash_Activity) getActivity();
		mHttpClient = new Async_HttpClient(mSplashActivity);
		
		globalData = (Global_Data) getActivity().getApplicationContext();
		
		// ImageView for the Splash Image
		// xml id removed from xml resource
		mImageViewSplash = (ImageView) mSplashActivity.findViewById(com.sportxast.SportXast.R.id.imgview_splash);
	}
	
	public void testPreConditions() {
		assertNotNull(context);
		assertNotNull(dbHelper);
		assertNotNull(globalData);
		assertNotNull(mSplashActivity);
		assertNotNull(mImageViewSplash);
		assertNotNull(mHttpClient);
		
		Drawable drawableBg = mSplashActivity.getResources().getDrawable(com.sportxast.SportXast.R.drawable.ic_splash_image);	
		assertNotNull(mImageViewSplash.getDrawable());
		assertTrue(mImageViewSplash.getDrawable().getConstantState().equals(drawableBg.getConstantState()));
		int tableCount = dbHelper.getTableCount(KEY_PROFILE.TABLE_PROFILE);
		assertEquals("Profile table count is " + tableCount, 0, tableCount);
	}
	
	public void testHttpSettings() throws Throwable {
		/*
		 * Test Http get settings.
		 */
		ActivityMonitor monitor = getInstrumentation().addMonitor(Tutorial_Activity.class.getName(), null, false);
		
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				mHttpClient.GET("Settings", new RequestParams(), new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						super.onSuccess(response);
						arrHeader = headers;
						jsonResponse = response;
						signal.countDown();
					}
					
					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						super.onFailure(e, errorResponse);
						Log.e(TAG, "Error" + errorResponse);
						arrHeader = null;
						jsonResponse = null;						
//						mHttpResponse = null;
						signal.countDown();
					}
				});
				
			}
		});
		
		signal.await();
		assertNotNull(jsonResponse);
		assertNotNull(arrHeader);
		
		// add test to database
		ContentValues cv = new ContentValues();
		String deviceId = null;
		
		for(Header header : arrHeader) {
			
			if(header.getName().equalsIgnoreCase(KEY_HEADER.X_DEVICE_ID)) {
				cv.put(KEY_PROFILE.DEVICE_ID, header.getValue());
				deviceId = header.getValue();
			} else if(header.getName().equalsIgnoreCase(KEY_HEADER.X_USER_ID)) {
				cv.put(KEY_PROFILE.USER_ID, header.getValue());
			} else if(header.getName().equalsIgnoreCase(KEY_HEADER.X_USER_NAME)) {
				cv.put(KEY_PROFILE.USER_NAME, header.getValue());
			} else if(header.getName().equalsIgnoreCase(KEY_HEADER.X_SESSION_ID)) {
				cv.put(KEY_PROFILE.SESSION_ID, header.getValue());
			}
		}
		
		dbHelper.addItem(KEY_PROFILE.TABLE_PROFILE, cv);
		ContentValues valuesFromDb = dbHelper.getItem(KEY_PROFILE.TABLE_PROFILE, KEY_PROFILE.DEVICE_ID, deviceId);
		
		assertEquals(cv.get(KEY_PROFILE.DEVICE_ID), valuesFromDb.get(KEY_PROFILE.DEVICE_ID));
		assertEquals(cv.get(KEY_PROFILE.USER_ID), valuesFromDb.get(KEY_PROFILE.USER_ID));
		assertEquals(cv.get(KEY_PROFILE.USER_NAME), valuesFromDb.get(KEY_PROFILE.USER_NAME));
		assertEquals(cv.get(KEY_PROFILE.SESSION_ID), valuesFromDb.get(KEY_PROFILE.SESSION_ID));
		
		mSplashActivity.parseSetting(jsonResponse);
		
//		assertNotNull(globalData.getAppSetting());
		// on purpose for logging
		//assertEquals("App setting is " + globalData.getAppSetting().language, true, false);
		
		Tutorial_Activity tutorialActivity = (Tutorial_Activity) getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
		assertNotNull(tutorialActivity);
		tutorialActivity.finish(); // Make sure to finish
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}

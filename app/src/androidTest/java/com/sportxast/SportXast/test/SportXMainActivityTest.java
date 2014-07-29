package com.sportxast.SportXast.test;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.robotium.solo.Solo;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.Global_Data.Coordinate;
import com.sportxast.SportXast.SportX2_Main;
import com.sportxast.SportXast.activities2_0.Create_Activity;
import com.sportxast.SportXast.activities2_0.EventDetail_Activity;
import com.sportxast.SportXast.activities2_0.Menu_Activity;
import com.sportxast.SportXast.activities2_0.Search_Activity;
import com.sportxast.SportXast.models._EventLists;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.test.utils.SleepUtils;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.GPSTracker;
import com.sportxast.SportXast.thirdparty_class.PullToRefreshListView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class SportXMainActivityTest extends ActivityInstrumentationTestCase2<SportX2_Main> {

	private Global_Data globalData;	
	private SportX2_Main sportMainActivity;	
	//private PullToRefreshListView listView;
	private View headerView;
	private View footerView;
	private Async_HttpClient httpClient;
	private GPSTracker gpsTracker;
	private _EventLists eventLists;
	//private ImageButton imgBtnMenu;
	//private ImageButton imgBtnSearch;
	private Button buttonBack;	
	private JSONObject jsonResponse = null;
	private Solo solo;	
	//private static final String TAG = "MainTest";	
//	private int listCount = 0;	
	private PullToRefreshListView listPullView;
	private View listItemEvents;
	
//	private static final String TAG = "Sport Main Test";
	
	public SportXMainActivityTest() {
		super(SportX2_Main.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		sportMainActivity = getActivity();
		solo = new Solo(getInstrumentation(), getActivity());
		
		// It's okey if not referred from MainActivity, not sure.
		gpsTracker = new GPSTracker(sportMainActivity);	
		httpClient = new Async_HttpClient(sportMainActivity);
		globalData = (Global_Data) getActivity().getApplicationContext();
		eventLists = new _EventLists();

		headerView = sportMainActivity.getLayoutInflater().inflate(com.sportxast.SportXast.R.layout.list_header, null);
		footerView = sportMainActivity.getLayoutInflater().inflate(com.sportxast.SportXast.R.layout.progressbar_small, null);
		listPullView = (PullToRefreshListView) solo.getView(com.sportxast.SportXast.R.id.listvw_events);
		listItemEvents = sportMainActivity.getLayoutInflater().inflate(com.sportxast.SportXast.R.layout.list_item_events, null);
		
		buttonBack = (Button) solo.getView(com.sportxast.SportXast.R.id.imgbtn_back);
	}
	
	public void testPreConditions() {
		getInstrumentation().waitForIdleSync();
		solo.waitForActivity(SportX2_Main.class);
		
		solo.assertCurrentActivity(Values.WRONG_ACTIVITY, SportX2_Main.class);
		solo.assertMemoryNotLow();
		
		assertNotNull(globalData);
		assertNotNull(sportMainActivity);
		assertNotNull(listPullView);
		assertNotNull(headerView);
		assertNotNull(footerView);
		assertNotNull(buttonBack);
		
		assertNotNull(listItemEvents);
	}
//	
	public void testCoordinates() {
		// Coordinates
		Coordinate coord = whatMyCoordinate();
		if(coord != null) {
			globalData.setCoordinate(coord);
			// Latitude values
			assertEquals(coord.latitude, globalData.getCoordinate().latitude);
			assertEquals(coord.longitude, globalData.getCoordinate().longitude);
		} else {
			assertNull(coord);
		}

	}
	
	public void testGetLocationAndFetchDataForTheFirstTime() {
		// Make sure that, you can the location. Big brother
		assertTrue(gpsTracker.canGetLocation());
		// Make sure that, you have my coordinates. 
		assertNotNull(whatMyCoordinate());
		globalData.setCoordinate(whatMyCoordinate());

	}
	
	public void testFetchData() throws Throwable {
		// Coordinates
		Coordinate coord = whatMyCoordinate();
		assertNotNull(whatMyCoordinate());
		
		if(coord != null) {
			globalData.setCoordinate(coord);
			// Latitude values
			assertEquals(coord.latitude, globalData.getCoordinate().latitude);
			assertEquals(coord.longitude, globalData.getCoordinate().longitude);
		} else {
			assertNull(coord);
		}
		// For fetching data when you "pull to refresh"
		// Get current location
		final RequestParams params = new RequestParams();
		params.put(Values.TAG_PAGE_SIZE, "20");
		params.put(Values.TAG_TYPE, "hashtag");// intent passed either "hashtag" or "event"
		params.put(Values.TAG_SINCE_ID, "");
		params.put(Values.TAG_UNTIL_ID, "");
		params.put(Values.TAG_LATITUDE, globalData.getCoordinate().latitude);
		params.put(Values.TAG_LONGITUDE, globalData.getCoordinate().longitude);
		
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				httpClient.GET(Values.GET_EXPORT_EVENTS, params, new JsonHttpResponseHandler() {
					
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						super.onSuccess(statusCode, headers, response);						
						jsonResponse = response;
						signal.countDown();
					}
					
					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						super.onFailure(e, errorResponse);
						jsonResponse = null;
						signal.countDown();
					}
				});
			}
		});
		
		signal.await();
		assertNotNull(jsonResponse);
		eventLists.parseEvents(jsonResponse);
		
		// test click here if all else fails
		getInstrumentation().waitForIdleSync();
		
		int eventIndex = 5;
		sendRepeatedKeys(eventIndex, KeyEvent.KEYCODE_DPAD_DOWN, 1, KeyEvent.KEYCODE_DPAD_CENTER);
		
		SleepUtils.freeze(getInstrumentation(), 4000);
		solo.assertCurrentActivity("I should be in EventDetail_Activity", EventDetail_Activity.class);
		solo.goBack();
	}

	public void testHeaderClick() {
		
		solo.clickOnText("Create an Event");
		
		SleepUtils.freeze(getInstrumentation(), 3000);
		solo.assertCurrentActivity("Take me to Create_Activity", Create_Activity.class);
		solo.goBack();
	}

	public void testMenu() {
		// Click on Menu on top
		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.imgbtn_menu));
		SleepUtils.freeze(getInstrumentation(), 3000);
		solo.assertCurrentActivity("Menu should pop up", Menu_Activity.class);
		solo.goBack();
	}
	
	public void testSearch() {
		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.imgbtn_search));
		SleepUtils.freeze(getInstrumentation(), 3000);
		solo.assertCurrentActivity("Search Activity yes", Search_Activity.class);
		solo.goBack();
	}
	
	public void testNavigation() {
		RelativeLayout header = (RelativeLayout) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.sx_header_wrapper);
		assertNotNull(header);

		// Menu or hamburger icon
		ImageButton imgbtnMenu = (ImageButton) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_menu);
		assertNotNull(imgbtnMenu);
		
		// Menu title, at the center
		TextView txtMenuTitle = (TextView) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_menutitle);
		assertNotNull(txtMenuTitle);
		
		// Search icon, at the right
		ImageButton imgbtnSearch = (ImageButton) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_search);
		assertNotNull(imgbtnSearch);
		
		ViewAsserts.assertOnScreen(header, imgbtnMenu);
		ViewAsserts.assertOnScreen(header, txtMenuTitle);
		ViewAsserts.assertOnScreen(header, imgbtnSearch);
		
		ViewAsserts.assertHorizontalCenterAligned(header, txtMenuTitle);
		ViewAsserts.assertLeftAligned(header, imgbtnMenu, 24);
		ViewAsserts.assertRightAligned(header, imgbtnSearch, 24);
		
//		ViewAsserts.assertBottomAligned(header, headerView);
	}
	
	public void testCreateEventDisplaysUnderNavBar() {
		// aka android:id="@+id/layout_menubar
		RelativeLayout relActionBar = (RelativeLayout) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.layout_menubar);
		assertNotNull(relActionBar);
		
		int[] xyActionBar = new int[2];
		relActionBar.getLocationOnScreen(xyActionBar);

		TextView txtHeaderTitle = (TextView) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_header_title);
		
		int[] xyHeaderTitle = new int[2];
		txtHeaderTitle.getLocationOnScreen(xyHeaderTitle);
		
		// Header title is the sum of ActionBar's y position and height.
		int expectedYPosOfHeaderTitle = xyActionBar[1] + relActionBar.getHeight();
		
		assertEquals(expectedYPosOfHeaderTitle, xyHeaderTitle[1]);
	} 
	
	public void testThumbnailOnLeft() {
//		View listItemEvents = sportMainActivity.getLayoutInflater().inflate(com.sportxast.SportXast.R.layout.list_item_events, null);
		assertNotNull(listItemEvents);
		
		ImageView imgvwPhoto = (ImageView) listItemEvents.findViewById(com.sportxast.SportXast.R.id.imgvw_events_photo);
		assertNotNull(imgvwPhoto);
		
		int[] xyPhoto = new int[2];
		imgvwPhoto.getLocationOnScreen(xyPhoto);
		// x position == 0, means at the left most
		assertTrue("Photo x: " + xyPhoto[0], xyPhoto[0] == 0);
		// y position == 0
		assertTrue("Photo y: " + xyPhoto[1], xyPhoto[1] == 0);
	}
	
	public void testSportIconOnUpperLeft() {
		ImageView imgvwSportsIcon = (ImageView) listItemEvents.findViewById(com.sportxast.SportXast.R.id.imgvw_events_sportsicon);
		assertNotNull(imgvwSportsIcon);
	}
	
	public void testPageScrollDownsLoad() {
		SleepUtils.freeze(getInstrumentation(), 5000);
		assertTrue(solo.scrollDown());
		
		SleepUtils.freeze(getInstrumentation(), 4000);
		assertTrue(solo.scrollDown());
	}
	
	public void testRefreshList() {
		Display display = sportMainActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;
		
		int fromX = screenWidth / 2;
		int toX = screenWidth / 2;
		
		/*
		 * height = 900
		 * width = 450
		 * 
		 * fromX = 450 / 2 = 225
		 * toX = 450 / 2 = 225
		 * 
		 * fromY = (450) + (300) = 750
		 * toY = (450) - (300) = 150
		 */
		
		int fromY = (screenHeight / 2) - (screenHeight / 3);
		int toY = (screenHeight / 2) + (screenHeight / 3);
		
//		ImageView imgvwPullToRefresh = (ImageView) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.pull_to_refresh_image);
//		assertNotNull(imgvwPullToRefresh);
//		
//		ProgressBar imgvwProgress = (ProgressBar) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.pull_to_refresh_progress);
//		assertNotNull(imgvwProgress);
		
		SleepUtils.freeze(getInstrumentation(), 4000);
		solo.drag(fromX, toX, fromY, toY, 50);
		
//		SleepUtils.freeze(getInstrumentation(), 1500);
		
//		assertThat(imgvwPullToRefresh).isVisible();
//		assertThat(imgvwProgress).isVisible();
//		
//		// after dragging down, pull to refresh text should appear
//		TextView txtPullToRefresh = (TextView) sportMainActivity.findViewById(com.sportxast.SportXast.R.id.pull_to_refresh_text);
//		assertNotNull(txtPullToRefresh);
//		assertThat(txtPullToRefresh).isVisible();
//		assertThat(imgvwPullToRefresh).isGone();
	}
	
	public void testTapAnEvent() {
		SleepUtils.freeze(getInstrumentation(), 5000);
		int eventIndex = 10;
		sendRepeatedKeys(eventIndex, KeyEvent.KEYCODE_DPAD_DOWN, 1, KeyEvent.KEYCODE_DPAD_CENTER);
		
		solo.assertCurrentActivity("I should be in EventDetail_Activity", EventDetail_Activity.class);
		
		EventDetail_Activity activity = (EventDetail_Activity) solo.getCurrentActivity();
		TextView txtHighlightsCount = (TextView) activity.findViewById(com.sportxast.SportXast.R.id.txtvw_highlightListCount);
		assertNotNull(txtHighlightsCount);
		
		// make sure highlights count is not 0
		int count = Integer.parseInt(txtHighlightsCount.getText().toString().trim());
		assertTrue("count: " + count, count > 0);
		
		ImageView imgvwVideoThumb = (ImageView) activity.findViewById(com.sportxast.SportXast.R.id.imgvw_photo_eventdetail);
		assertNotNull(imgvwVideoThumb);
//		assertThat(imgvwVideoThumb).isVisible();
		
		solo.goBackToActivity("SportX2_Main");
	}
	
	private Coordinate whatMyCoordinate() {
		if(gpsTracker.canGetLocation()) {
			Coordinate coord = new Coordinate();
			coord.latitude = gpsTracker.getLatitude();
			coord.longitude = gpsTracker.getLongitude();
			
			return coord;
		} else {
			return null;
		}
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
}

package com.sportxast.SportXast.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.activities2_0.ChooseAVenue_Activity;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

public class ChooseAVenueActivityTest extends ActivityInstrumentationTestCase2<ChooseAVenue_Activity> {

	private ChooseAVenue_Activity chooseVenueActivity;
	private HeaderListView headerListView;
	private EditText edtSearch;
	private Async_HttpClient httpClient;
	private Global_Data globalData;
//	private GPSTracker gpsTracker;
	
//	private JSONObject jsonResponse = null;
	private Solo solo;
	
	public ChooseAVenueActivityTest() {
		super(ChooseAVenue_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		chooseVenueActivity = getActivity();
		httpClient = new Async_HttpClient(chooseVenueActivity);
//		gpsTracker = new GPSTracker(chooseVenueActivity);
		solo = new Solo(getInstrumentation(), chooseVenueActivity);
		globalData = (Global_Data) chooseVenueActivity.getApplicationContext();
		
		headerListView = (HeaderListView) chooseVenueActivity.findViewById(com.sportxast.SportXast.R.id.listvw_sportlists);
		edtSearch = (EditText) chooseVenueActivity.findViewById(com.sportxast.SportXast.R.id.edittext_search);
	}
	
	public void testPreconditions() {
		assertNotNull(chooseVenueActivity);
		assertNotNull(headerListView);
		assertNotNull(edtSearch);
		assertNotNull(globalData);
		assertNotNull(solo);
		assertNotNull(httpClient);
		// Make sure edit search is also blank
		assertTrue(edtSearch.getText().toString().isEmpty());
		//String actionBarTitle = chooseVenueActivity.getActionBarTitle();
		//assertEquals("Venues", actionBarTitle);
	}
	
	public void testEnterVenue() {
		solo.enterText(edtSearch, "Ayala");
		assertEquals("Ayala", edtSearch.getText().toString().trim());
//		getInstrumentation().waitForIdleSync();
//		
//		ChooseAVenueListAdapter adapter = (ChooseAVenueListAdapter) headerListView.getAdapter();
//		assertNotNull(adapter);
//		assertTrue("Child count is " + adapter.getCount(), adapter.getCount() > 0);
	}
	
	// Error, check later.
//	public void testFetchData() throws Throwable {
//		final CountDownLatch signal = new CountDownLatch(1);
//		
//		solo.enterText(edtSearch, "Ayala");
//		
//		Coordinate coord = whatMyCoordinate();
//		assertNotNull(coord);
//		final RequestParams params = new RequestParams();
//		params.put(Values.TAG_QUERY, edtSearch.getText().toString()); // test query
//		params.put(Values.TAG_LONGITUDE, coord.longitude);
//		params.put(Values.TAG_LATITUDE, coord.latitude);
//		
//		runTestOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//
//				httpClient.GET(Values.GET_VENUELIST, params, new JsonHttpResponseHandler() {
//					
//					@Override
//					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//						super.onSuccess(statusCode, headers, response);
//						jsonResponse = response;
//						signal.countDown();
//					}
//					
////					@Override
////					public void onSuccess(JSONObject response) {
////						super.onSuccess(response);
////						
////						
////					}
//					
//					@Override
//					public void onFailure(String responseBody, Throwable error) {
//						super.onFailure(responseBody, error);
//						jsonResponse = null;
//						signal.countDown();
//					}
//				});
//			}
//		});
//		
//		signal.await();
//		assertNotNull(jsonResponse);
//	}
	
//	private Coordinate whatMyCoordinate() {
//		if(gpsTracker.canGetLocation()) {
//			Coordinate coord = new Coordinate();
//			coord.latitude = gpsTracker.getLatitude();
//			coord.longitude = gpsTracker.getLongitude();
//			
//			return coord;
//		} else {
//			return null;
//		}
//	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
}

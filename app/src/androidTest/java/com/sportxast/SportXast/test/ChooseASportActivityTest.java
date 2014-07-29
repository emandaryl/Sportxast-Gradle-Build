package com.sportxast.SportXast.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.activities2_0.ChooseASport_Activity;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class ChooseASportActivityTest extends ActivityInstrumentationTestCase2<ChooseASport_Activity> {

	private ChooseASport_Activity chooseSportActivity;
	private HeaderListView headerListView;
	private EditText edtSearch;
	private Async_HttpClient httpClient;
	
	private JSONObject jsonResponse = null;
	//private Solo solo;
	
	public ChooseASportActivityTest() {
		super(ChooseASport_Activity.class);
		//
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		chooseSportActivity = getActivity();
		httpClient = new Async_HttpClient(chooseSportActivity);
		//solo = new Solo(getInstrumentation(), chooseSportActivity);
		
		headerListView = (HeaderListView) chooseSportActivity.findViewById(com.sportxast.SportXast.R.id.listvw_sportlists);
		edtSearch = (EditText) chooseSportActivity.findViewById(com.sportxast.SportXast.R.id.edittext_search);
		
	}
	
	public void testPreconditions() {
		assertNotNull(chooseSportActivity);
		assertNotNull(httpClient);
		assertNotNull(headerListView);
		assertNotNull(edtSearch);
		// Make sure edit search is also blank
		assertTrue(edtSearch.getText().toString().isEmpty());
		//String actionBarTitle = chooseSportActivity.getActionBarTitle();
		//assertEquals("Sports", actionBarTitle);
	}
	
//	public void testNavigationPositions() {
//		//assertEquals(View.GONE, chooseSportActivity.btn_done.getVisibility());
//		//assertEquals(View.VISIBLE, chooseSportActivity.btn_back.getVisibility());
//		
//		//View rootView = chooseSportActivity.btn_back.getRootView();
//		//View viewActionBar = chooseSportActivity.getActionBarBase().getCustomView();
//		
//		//int[] xy = new int[2];
////		chooseSportActivity.btn_back.getLocationOnScreen(xy);
//		//chooseSportActivity.btn_done.getLocationOnScreen(xy);
//		
//		//int[] xyRoot = new int[2];
//		//rootView.getLocationOnScreen(xyRoot);
//		
//		//int[] xyActiobarCustom = new int[2];
//		//viewActionBar.getLocationOnScreen(xyActiobarCustom);
//		
//		//int y = xy[1] - xyRoot[1];
//		assertTrue("Should have position y coordinate.", y >= 0);
//		
//		ViewGroup.MarginLayoutParams paramsActionbar = (ViewGroup.MarginLayoutParams) viewActionBar.getLayoutParams();
//		int margin = paramsActionbar.topMargin + paramsActionbar.bottomMargin;
//		assertTrue("Button back y is " + y + " and " + viewActionBar.getHeight() + margin, false == true);
//	}
	
//	public void testSelectSport() {
//		chooseSportActivity.runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				edtSearch.clearFocus();
//			}
//		});
//		
//		headerListView.requestFocus();
//		getInstrumentation().waitForIdleSync();
//		
//		int sportIndex = 1;
//		
////		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.listvw_sportlists));
//		sendRepeatedKeys(sportIndex, KeyEvent.KEYCODE_DPAD_DOWN, 1, KeyEvent.KEYCODE_DPAD_CENTER);
//		solo.assertCurrentActivity("Should go back to Create_Activity", Create_Activity.class);
//	}
	
//	public void testSearch() {
//		assertTrue(edtSearch.requestFocus());
//		getInstrumentation().waitForIdleSync();
//		getInstrumentation().sendStringSync("Basketball");
//	}
//	
//	public void testScroll() {
//		getInstrumentation().waitForIdleSync();
//		solo.scrollListToBottom(headerListView.getListView());
//		solo.scrollListToLine(headerListView.getListView(), 20);
//	}
	
	public void testFetchData() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				httpClient.GET(Values.GET_SPORTLIST, new RequestParams(), new JsonHttpResponseHandler() {
					
					@Override
					public void onSuccess(JSONObject response) {
						super.onSuccess(response);
						jsonResponse = response;
						signal.countDown();
					}
					
					@Override
					public void onFailure(String responseBody, Throwable error) {
						super.onFailure(responseBody, error);
						jsonResponse = null;
						signal.countDown();
					}
				});
			}
		});
		
		signal.await();
		assertNotNull(jsonResponse);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}

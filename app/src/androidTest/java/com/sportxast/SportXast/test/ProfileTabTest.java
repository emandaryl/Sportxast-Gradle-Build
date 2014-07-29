package com.sportxast.SportXast.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.activities2_0.ProfileTab_Activity;
import com.sportxast.SportXast.commons.Constant;
import com.sportxast.SportXast.test.constants.Values;

public class ProfileTabTest extends ActivityInstrumentationTestCase2<ProfileTab_Activity> {

	private Global_Data globalData;
	private ProfileTab_Activity profileTabActivity;
	private ListView listvwProfileTab;
//	private View footerView;
//	private Async_HttpClient httpClient;
	
	private String username = null;
	private String userId = null;
	
	public ProfileTabTest() {
		super(ProfileTab_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		
		Intent intent = new Intent();
		intent.putExtra(Values.TAG_USERNAME, "law");
		intent.putExtra(Constant.EXTRA_USER_ID, Values.SAMPLE_USER_ID);
		setActivityIntent(intent);
		
		profileTabActivity = getActivity();
//		httpClient = new Async_HttpClient(profileTabActivity);
		globalData = (Global_Data) profileTabActivity.getApplicationContext();
		
		listvwProfileTab = (ListView) profileTabActivity.findViewById(com.sportxast.SportXast.R.id.list_profiletab);
		
		username = profileTabActivity.getIntent().getStringExtra(Values.TAG_USERNAME);
		userId = profileTabActivity.getIntent().getStringExtra(Constant.EXTRA_USER_ID);
	}
	
	public void testPreconditions() {
		assertNotNull(profileTabActivity);
		assertNotNull(listvwProfileTab);
		
		int footerCount = listvwProfileTab.getFooterViewsCount();
		assertTrue("Footer views count is " + footerCount, footerCount > 0);
		
		double lat = globalData.getCoordinate().latitude;
		assertTrue("latitude is " + lat, lat != 0);
		
		double longi = globalData.getCoordinate().longitude;
		assertTrue("longitude is " + longi, globalData.getCoordinate().longitude != 0);
	}
	
	public void testIntents() {
		assertEquals("law", username);
		assertEquals(Values.SAMPLE_USER_ID, userId);
	}
	
	public void testFetchData() {
		final RequestParams params = new RequestParams();
		params.put(Values.TAG_USERNAME, username);
		
	}
}

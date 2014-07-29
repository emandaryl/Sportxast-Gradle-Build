package com.sportxast.SportXast.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.robotium.solo.Solo;
import com.sportxast.SportXast.activities2_0.Profile_Activity;
import com.sportxast.SportXast.commons.Constant;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class ProfileActivityTest extends ActivityInstrumentationTestCase2<Profile_Activity> {

	private Profile_Activity profileActivity;
	
	private TabHost tabHost;
	private ImageView imgvwAvatarProfile;
	private TextView txtHighlightCount;
	private TextView txtFollowerCount;
	private TextView txtFollowingCount;
	
	private static final int TOTAL_NUMBER_TABS = 3;
	private static final String TAB_HIGHLIGHTS = "Highlights";
	private static final String TAB_FOLLOWERS = "Followers";
	private static final String TAB_FOLLOWING = "Following";
	
	private Async_HttpClient httpClient;
	private JSONObject jsonResponse = null;
	private Solo solo;
	
	public ProfileActivityTest() {
		super(Profile_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		
		Intent mock = new Intent();
		mock.putExtra(Constant.EXTRA_USER_ID, Values.SAMPLE_USER_ID);
		mock.putExtra(Values.TAG_USER_DISPLAY_NAME, Values.SAMPLE_DISPLAY_NAME);
		mock.putExtra(Values.TAG_IS_MY_PROFILE, true);//true this is my profile
		setActivityIntent(mock);
		
		profileActivity = getActivity();
		solo = new Solo(getInstrumentation(), profileActivity);
		httpClient = new Async_HttpClient(profileActivity);
		
		tabHost = (TabHost) profileActivity.findViewById(android.R.id.tabhost);
		imgvwAvatarProfile = (ImageView) profileActivity.findViewById(com.sportxast.SportXast.R.id.imgvw_avatar_profile);
		txtHighlightCount = (TextView) profileActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_highlightscount);
		txtFollowerCount = (TextView) profileActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_followercount);
		txtFollowingCount = (TextView) profileActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_followingcount);
	}
	
	public void testPreconditions() {
		solo.assertCurrentActivity("I'm in my profile.", Profile_Activity.class);
		solo.assertMemoryNotLow();
		
		assertNotNull(profileActivity);
		assertNotNull(tabHost);
		assertNotNull(imgvwAvatarProfile);
		assertNotNull(txtHighlightCount);
		assertNotNull(txtFollowerCount);
		assertNotNull(txtFollowingCount);
		assertNotNull(httpClient);
	}
	
	public void testActionBarMenuItemDisplayCorrectly() {
		for(int i=0; i<3; i++) {
			
			setActivityInitialTouchMode(false);
			Intent intent = new Intent();
			if(i == 0) {
				/*
				 * User display name length > 0, display user display name
				 */
				intent.putExtra(Values.TAG_USER_DISPLAY_NAME, Values.SAMPLE_DISPLAY_NAME);	
				intent.putExtra(Constant.EXTRA_USER_ID, "");
			} else if(i == 1) {
				/*
				 * If user id length > 0, this user id should be the one
				 * to be displayed.
				 */
				intent.putExtra(Values.TAG_USER_DISPLAY_NAME, "");
				intent.putExtra(Constant.EXTRA_USER_ID, Values.SAMPLE_USER_ID);
			} else if(i == 2) {
				intent.putExtra(Values.TAG_USER_DISPLAY_NAME, "");
				intent.putExtra(Constant.EXTRA_USER_ID, "");
			}
			
			setActivityIntent(intent);
			
			Profile_Activity activity = (Profile_Activity) getActivity();
//			String extraDisplayName = activity.getIntent().getExtras().getString(Values.TAG_USER_DISPLAY_NAME);
//			String extraUserId = activity.getIntent().getExtras().getString(Constant.EXTRA_USER_ID);
			
//			if(extraDisplayName.length() > 0) {
//				assertEquals("actionbar title is " + activity.getActionBarTitle(), Values.SAMPLE_DISPLAY_NAME, activity.getActionBarTitle());
//			} else if(extraUserId.length() > 0) {
//				assertEquals("actionbar title is " + activity.getActionBarTitle(), Values.SAMPLE_USER_ID, activity.getActionBarTitle());
//			} else {
//				assertEquals("actionbar title is " + activity.getActionBarTitle(), "Profile", activity.getActionBarTitle());
//			}
			
			activity.finish();
			setActivity(null);
		} // End of for loop
	}
	
	public void testIntents() {
		String userId = profileActivity.getIntent().getStringExtra(Constant.EXTRA_USER_ID);
		assertEquals(Values.SAMPLE_USER_ID, userId);
		
		String displayName = profileActivity.getIntent().getStringExtra(Values.TAG_USER_DISPLAY_NAME);
		assertEquals(Values.SAMPLE_DISPLAY_NAME, displayName);
		
		boolean isMyProfile = profileActivity.getIntent().getBooleanExtra(Values.TAG_IS_MY_PROFILE, false);
		assertEquals(true, isMyProfile);
	}
	
	public void testTabs() {
		assertEquals(TOTAL_NUMBER_TABS, tabHost.getTabWidget().getTabCount());
		assertEquals(TAB_HIGHLIGHTS, getTextViewFromLabel(0));
		assertEquals(TAB_FOLLOWERS, getTextViewFromLabel(1));
		assertEquals(TAB_FOLLOWING, getTextViewFromLabel(2));
	}
	
	public void testFetchData() throws Throwable {
		final RequestParams params = new RequestParams();
		params.put(Constant.EXTRA_USER_ID, Values.SAMPLE_USER_ID);
		
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				httpClient.GET(Values.GET_USER, params, new JsonHttpResponseHandler() {
					
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
		// below on purpose
		//assertEquals("response is " + jsonResponse, "whut", jsonResponse);
	}
	
	private String getTextViewFromLabel(int childIndex) {
		TextView tab = (TextView) tabHost.getTabWidget().getChildAt(childIndex)
				.findViewById(com.sportxast.SportXast.R.id.tab);
		return tab.getText().toString().trim();
	}
}

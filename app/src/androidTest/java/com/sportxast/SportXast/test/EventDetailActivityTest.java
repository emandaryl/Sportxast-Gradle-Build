package com.sportxast.SportXast.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.robotium.solo.Solo;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.Global_Data.Coordinate;
import com.sportxast.SportXast.activities2_0.EventDetail_Activity;
import com.sportxast.SportXast.activities2_0.Fav_Tag_Comment_Activity;
import com.sportxast.SportXast.activities2_0.Highlight_Activity;
import com.sportxast.SportXast.commons.Constant;
import com.sportxast.SportXast.models._EventLists.EventLists;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.GPSTracker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class EventDetailActivityTest extends ActivityInstrumentationTestCase2<EventDetail_Activity> {
	
	private EventDetail_Activity eventDetailActivity;
	private Async_HttpClient httpClient;
	private Global_Data globalData;
	
	//Views
	private TextView txtSportsName;
	private TextView txtVenueName;
	private TextView txtDate;
	private TextView txtCommentCount;
	private TextView txtFavCount;
	private TextView txtHighlightListCount;
	private TextView txtTaglistCount;
	private TextView txtFansListCount;
	private RelativeLayout relHighlightHeader;
	private RelativeLayout relTagsHeader;
	private RelativeLayout relFansHeader;
	
	// Highlight reel, VideoView container
	private RelativeLayout relHighlightReel;
	private LinearLayout linTeamList;
	private LinearLayout linHighlightList;
	private LinearLayout linTagList;
	private LinearLayout linFansList;
	private ImageView imgvwMediaPhoto;
	private ImageButton imgbtnPlay;
	private ImageButton imgbtnFav;
	private ImageButton imgbtnComment;
	private ImageButton imgbtnShare;	
	private ImageView imgvwPhoto;
	private TextView txtSubtitle;
	private GPSTracker gpsTracker;
	//private EventLists eventLists = new EventLists();
	private JSONObject jsonResponse = null;
//	private JSONObject jsonReelResponse = null;
	public ArrayList<EventLists> listEvent = null;
//	private _EventLists list;
	private String eventId;
	private String isOpen;
//	private int teamListChildCount = 0;
	private Solo solo;

	public EventDetailActivityTest() {
		super(EventDetail_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		
		// pass a mock intent here bro
		Intent intentMock = new Intent();
		intentMock.putExtra(Constant.EXTRA_EVENT_ID, "2323");
		intentMock.putExtra(Values.TAG_IS_OPEN, "0");
		setActivityIntent(intentMock);
		
		eventDetailActivity = getActivity();
		
		solo = new Solo(getInstrumentation(), getActivity());
		
		httpClient = new Async_HttpClient(eventDetailActivity);
		gpsTracker = new GPSTracker(eventDetailActivity);
		globalData = (Global_Data) getActivity().getApplicationContext();
//		list = new _EventLists();
		
		txtSportsName = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_sportname);
//		txtVenueName = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_venuename);
		txtDate = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_date);
		txtCommentCount = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_commentCount);
		txtFavCount = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_favoriteCount);
		txtHighlightListCount = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_highlightListCount);
		txtTaglistCount = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_taglistCount);
		txtFansListCount = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_fanslistCount);
		
		relHighlightHeader = (RelativeLayout) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.layout_highlight_header);
		relTagsHeader = (RelativeLayout) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.layout_tags_header);
		relFansHeader = (RelativeLayout) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.layout_fans_header);
		
		relHighlightReel = (RelativeLayout) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.layout_highlightreel);
//		linTeamList = (LinearLayout) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.layout_teams_eventdetail);
		linHighlightList = (LinearLayout) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.layout_highlightlist_eventdetail);
		linTagList = (LinearLayout) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.layout_taglist_eventdetail);
		linFansList = (LinearLayout) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.layout_fanslist_eventdetail);
		
		imgvwMediaPhoto = (ImageView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.imgvw_photo_eventdetail);
		imgbtnPlay = (ImageButton) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_play);
		imgbtnFav = (ImageButton) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_favorite);
		imgbtnComment = (ImageButton) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_comment);
		imgbtnShare = (ImageButton) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_shareapp);
		
		// addView()
		imgvwPhoto = (ImageView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.imgvw_photo);
		txtSubtitle = (TextView) eventDetailActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_subtitle);
	}
	
	public void testIntent() {
		eventId = eventDetailActivity.getIntent().getStringExtra(Constant.EXTRA_EVENT_ID);
		assertEquals("2323", eventId);
		isOpen = eventDetailActivity.getIntent().getStringExtra(Values.TAG_IS_OPEN);
		assertEquals("0", isOpen);
	}
	
	public void testPreconditions() {
		solo.waitForActivity(EventDetail_Activity.class);
		solo.assertMemoryNotLow();
		solo.assertCurrentActivity("I should be in EventDetail_Activity", EventDetail_Activity.class);
		
		assertNotNull(httpClient);
		
		assertNotNull(txtSportsName);
		assertNotNull(txtVenueName);
		assertNotNull(txtDate);
		assertNotNull(txtCommentCount);
		assertNotNull(txtFavCount);
		assertNotNull(txtHighlightListCount);
		assertNotNull(txtTaglistCount);
		assertNotNull(txtFansListCount);
		
		assertNotNull(relHighlightHeader);
		assertNotNull(relTagsHeader);
		assertNotNull(relFansHeader);
		
		assertNotNull(relHighlightReel);
		assertNotNull(linTeamList);
		assertNotNull(linHighlightList);
		assertNotNull(linTagList);
		assertNotNull(linFansList);
		
		assertNotNull(imgvwMediaPhoto);
		assertNotNull(imgbtnPlay);
		assertNotNull(imgbtnFav);
		assertNotNull(imgbtnComment);
		assertNotNull(imgbtnShare);
		
		assertNotNull(imgvwPhoto);
		assertNotNull(txtSubtitle);
	}
	
	public void testHighlightClick() {
		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.layout_highlight_header));
		solo.assertCurrentActivity("Highlight Activity", Highlight_Activity.class);
		solo.goBack();
	}
	
	public void testTagsClick() {
		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.layout_tags_header));
		solo.assertCurrentActivity("Fav_Tag_Comment_Activity tags", Fav_Tag_Comment_Activity.class);
		solo.goBack();
	}
	
	public void testFansClick() {
		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.layout_fans_header));
		solo.assertCurrentActivity("Fav_Tag_Comment_Activity fans", Fav_Tag_Comment_Activity.class);
		solo.goBack();
	}
	
	public void testCommentClick() {
		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.imgbtn_comment));
		solo.assertCurrentActivity("Fav_Tag_Comment_Activity comment", Fav_Tag_Comment_Activity.class);
		solo.goBack();
	}

	public void testFavoriteCount() {
		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.txtvw_favoriteCount));
		solo.assertCurrentActivity("Fav_Tag_Comment_Activity fav", Fav_Tag_Comment_Activity.class);
		solo.goBack();
	}
	
	public void testCommentCount() {
		solo.clickOnView(solo.getView(com.sportxast.SportXast.R.id.txtvw_commentCount));
		solo.assertCurrentActivity("Fav_Tag_Comment_Activity comment", Fav_Tag_Comment_Activity.class);
		solo.goBack();
	}
	
	public void testFetchEventInfoData() throws Throwable {
		Coordinate coord = whatMyCoordinate();
		assertNotNull(coord);
		
		globalData.setCoordinate(coord);
		
		assertEquals(coord.latitude, globalData.getCoordinate().latitude);
		assertEquals(coord.longitude, globalData.getCoordinate().longitude);
		
		final RequestParams params = new RequestParams();
		params.put(Constant.EXTRA_EVENT_ID, eventDetailActivity.getIntent().getStringExtra(Constant.EXTRA_EVENT_ID));
		params.put(Values.TAG_LATITUDE, "" + globalData.getCoordinate().latitude);
		params.put(Values.TAG_LONGITUDE, "" + globalData.getCoordinate().longitude);
		
		// This is important to sync httpClient's callback with JUnit.
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				httpClient.GET(Values.GET_EXPORT_EVENT_INFO, params, new JsonHttpResponseHandler() {
					
					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						
						jsonResponse = response;
						
						signal.countDown();
					}
					
					@Override
					public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
						super.onFailure(statusCode, e, errorResponse);
						jsonResponse = null;
						
						signal.countDown();
					}
				});
			}
		});
		
		signal.await();
		// Assert json response is never null.
		assertNotNull(jsonResponse);
		
		//EventLists listOfEvents = eventDetailActivity.eventLists;
		//assertNotNull(listOfEvents);
		
		// make sure textviews are not empty
		// populateContentView()
		String highlightCount = txtHighlightListCount.getText().toString().trim(); 
		assertFalse(highlightCount.isEmpty());
		assertTrue(highlightCount.trim().length() > 0);
		
		String tagListCount = txtTaglistCount.getText().toString().trim(); 
		assertFalse(tagListCount.isEmpty());
		assertTrue(tagListCount.trim().length() > 0);
		
		String fansListCount = txtFansListCount.getText().toString().trim(); 
		assertFalse(fansListCount.isEmpty());
		assertTrue(fansListCount.trim().length() > 0);
		// error on purpose, and...success!
		assertTrue(fansListCount.substring(0, 1).trim().length() > 0);
		
		//int isOpenWhut = Integer.parseInt(listOfEvents.eventIsOpen);
		int isOpenWhut = 0;// temporary
		if(isOpenWhut == 0) {
			//relHighlightReel should be visible
			assertEquals(View.VISIBLE, relHighlightReel.getVisibility());
		} else {
			// otherwise, should be gone
			assertEquals(View.GONE, relHighlightReel.getVisibility());
		}
		
		String sportsName = txtSportsName.getText().toString().trim(); 
		assertFalse(sportsName.isEmpty());
		assertTrue(sportsName.trim().length() > 0);
		
		String venueName = txtVenueName.getText().toString().trim();
		assertFalse(venueName.isEmpty());
		assertTrue(venueName.trim().length() > 0);
		
		String date = txtDate.getText().toString().trim();
		assertFalse(date.isEmpty());
		assertTrue(date.trim().length() > 0);
		
		String favCount = txtFavCount.getText().toString().trim();
		assertFalse(favCount.isEmpty());
		assertTrue(favCount.trim().length() > 0);
		
		//txtCommentCount
		String commentCount = txtCommentCount.getText().toString().trim();
		assertFalse(commentCount.isEmpty());
		assertTrue(commentCount.trim().length() > 0);

		// populateEventTeamView, EventLists listOfEvents = eventDetailActivity.eventLists;
		
		// populateFanListView()  linFansList
	}
	
	// Cant fetch or something. Check later
//	public void testFetchHighlightReelData() throws Throwable {
//		// error in getting data
//		final RequestParams params = new RequestParams();
//		params.put(Values.TAG_EVENT_ID, eventDetailActivity.getIntent().getStringExtra(Values.TAG_EVENT_ID));
//		
//		final CountDownLatch signalReelData = new CountDownLatch(1);
//		runTestOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				httpClient.GET(Values.GET_HIGHLIGHT_REEL, params, new JsonHttpResponseHandler() {
//					
//					@Override
//					public void onSuccess(int statusCode, JSONObject response) {
//						super.onSuccess(statusCode, response);
//						
//						jsonReelResponse = response;
//						signalReelData.countDown();
//					}
//					
//					@Override
//					public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
//						super.onFailure(statusCode, e, errorResponse);
//						
//						jsonReelResponse = null;
//						signalReelData.countDown();
//					}
//				});
//
//			}
//		});
//		
//		signalReelData.await();
//		
//		// json highlight reel response is not null
//		assertNotNull(jsonReelResponse);
//	}

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

}
